# Event Sourcing

Event Sourcing stores all changes as a sequence of events rather than storing current state. The current state is derived by replaying events.

## Table of Contents

1. [Concepts](#concepts)
2. [Event Store](#event-store)
3. [Aggregates](#aggregates)
4. [Projections](#projections)
5. [Best Practices](#best-practices)
6. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Event Sourcing?

Instead of storing current state, store all events that led to current state. State is rebuilt by replaying events.

```
Traditional: Store current state
  Account: { balance: 100 }

Event Sourcing: Store events
  Event 1: AccountCreated
  Event 2: MoneyDeposited(150)
  Event 3: MoneyWithdrawn(50)
  → Current balance = 150 - 50 = 100
```

### Benefits

- **Complete History** - full audit trail
- **Time Travel** - reconstruct state at any point
- **Debugging** - replay events to find bugs
- **Analytics** - analyze patterns in events

---

## Event Store

### Basic Event Store

```java
// Event
public sealed interface AccountEvent
    permits AccountCreated, MoneyDeposited, MoneyWithdrawn {

    record AccountCreated(String accountId, String owner, Instant timestamp) implements AccountEvent {}
    record MoneyDeposited(String accountId, double amount, Instant timestamp) implements AccountEvent {}
    record MoneyWithdrawn(String accountId, double amount, Instant timestamp) implements AccountEvent {}
}

// Event store
public class EventStore {
    private final Map<String, List<AccountEvent>> streams = new HashMap<>();

    public void append(String streamId, AccountEvent event) {
        streams.computeIfAbsent(streamId, k -> new ArrayList<>()).add(event);
    }

    public List<AccountEvent> getEvents(String streamId) {
        return streams.getOrDefault(streamId, List.of());
    }

    public List<AccountEvent> getEventsSince(String streamId, Instant since) {
        return getEvents(streamId).stream()
            .filter(e -> getTimestamp(e).isAfter(since))
            .toList();
    }

    private Instant getTimestamp(AccountEvent event) {
        return switch (event) {
            case AccountCreated e -> e.timestamp();
            case MoneyDeposited e -> e.timestamp();
            case MoneyWithdrawn e -> e.timestamp();
        };
    }
}
```

### Event Store with Database

```java
@Entity
@Table(name = "events")
public class EventEntity {
    @Id @GeneratedValue
    private Long id;
    private String streamId;
    private String eventType;
    private String payload;
    private Instant timestamp;
    private int version;
}

@Repository
public interface EventJpaRepository extends JpaRepository<EventEntity, Long> {
    List<EventEntity> findByStreamIdOrderByVersionAsc(String streamId);
}
```

---

## Aggregates

### Rebuilding State from Events

```java
public class Account {
    private String id;
    private String owner;
    private double balance;
    private int version;

    // Rebuild from events
    public static Account fromEvents(List<AccountEvent> events) {
        Account account = new Account();
        events.forEach(account::apply);
        return account;
    }

    public void apply(AccountEvent event) {
        switch (event) {
            case AccountCreated e -> {
                this.id = e.accountId();
                this.owner = e.owner();
                this.balance = 0;
            }
            case MoneyDeposited e -> this.balance += e.amount();
            case MoneyWithdrawn e -> this.balance -= e.amount();
        }
        this.version++;
    }

    // Command methods produce events
    public AccountEvent deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        return new MoneyDeposited(id, amount, Instant.now());
    }

    public AccountEvent withdraw(double amount) {
        if (amount > balance) throw new IllegalStateException("Insufficient funds");
        return new MoneyWithdrawn(id, amount, Instant.now());
    }
}
```

### Aggregate Root

```java
public class AccountAggregate {
    private final Account account;
    private final List<AccountEvent> uncommittedEvents = new ArrayList<>();

    public static AccountAggregate create(String id, String owner) {
        AccountAggregate agg = new AccountAggregate();
        agg.apply(new AccountCreated(id, owner, Instant.now()));
        return agg;
    }

    public void deposit(double amount) {
        apply(account.deposit(amount));
    }

    public void withdraw(double amount) {
        apply(account.withdraw(amount));
    }

    private void apply(AccountEvent event) {
        account.apply(event);
        uncommittedEvents.add(event);
    }

    public List<AccountEvent> getUncommittedEvents() {
        return List.copyOf(uncommittedEvents);
    }

    public void clearUncommittedEvents() {
        uncommittedEvents.clear();
    }
}
```

---

## Projections

### Building Read Models

```java
public class AccountProjection {
    private final Map<String, AccountSummary> summaries = new HashMap<>();

    public void on(AccountEvent event) {
        switch (event) {
            case AccountCreated e -> summaries.put(e.accountId(),
                new AccountSummary(e.accountId(), e.owner(), 0));
            case MoneyDeposited e -> updateBalance(e.accountId(), e.amount());
            case MoneyWithdrawn e -> updateBalance(e.accountId(), -e.amount());
        }
    }

    private void updateBalance(String accountId, double delta) {
        AccountSummary summary = summaries.get(accountId);
        if (summary != null) {
            summaries.put(accountId, summary.withBalance(summary.balance() + delta));
        }
    }

    public AccountSummary getAccount(String id) {
        return summaries.get(id);
    }
}

public record AccountSummary(String id, String owner, double balance) {
    public AccountSummary withBalance(double newBalance) {
        return new AccountSummary(id, owner, newBalance);
    }
}
```

---

## Best Practices

### Do

```java
// 1. Use immutable events
public record OrderCreatedEvent(String orderId, Instant timestamp) {}

// 2. Version your events
public sealed interface OrderEvent
    permits OrderCreatedV1, OrderCreatedV2 { ... }

// 3. Build projections for reads
public class OrderProjection {
    public void on(OrderEvent event) { ... }
}
```

### Don't

```java
// 1. Don't modify events
// Events are immutable facts

// 2. Don't use for simple CRUD
// Event sourcing adds complexity

// 3. Don't forget event versioning
// Events change over time
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Event Sourcing** | Store events, not state |
| **Event Store** | Append-only event log |
| **Aggregate** | Rebuild state from events |
| **Projection** | Read model built from events |
| **Audit Trail** | Complete history of changes |
| **Time Travel** | Reconstruct past states |
| **Immutability** | Events are immutable facts |
| **Complexity** | Use when audit trail needed |
| **CQRS** | Natural fit with event sourcing |
