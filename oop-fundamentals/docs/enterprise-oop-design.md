# Enterprise OOP Design

## Layered Architecture

```
┌─────────────────────────────────────────┐
│           Presentation Layer            │  (Controllers, DTOs)
├─────────────────────────────────────────┤
│           Application Layer             │  (Use Cases, Commands, Queries)
├─────────────────────────────────────────┤
│            Domain Layer                 │  (Entities, Value Objects, Services)
├─────────────────────────────────────────┤
│         Infrastructure Layer            │  (Persistence, External APIs, Config)
└─────────────────────────────────────────┘
```

## Domain-Driven Design

### Entities
```java
public final class Account {
    private final AccountId id;
    private Money balance;
    private final CustomerId ownerId;
    private AccountStatus status;

    public Account(AccountId id, CustomerId ownerId, Money initialBalance) {
        this.id = Objects.requireNonNull(id);
        this.ownerId = Objects.requireNonNull(ownerId);
        this.balance = Objects.requireNonNull(initialBalance);
        this.status = AccountStatus.ACTIVE;
    }

    public void deposit(Money amount) {
        validateActive();
        this.balance = balance.add(amount);
        addDomainEvent(new AccountCreditedEvent(this.id, amount));
    }

    public void withdraw(Money amount) {
        validateActive();
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException(this.id);
        }
        this.balance = balance.subtract(amount);
        addDomainEvent(new AccountDebitedEvent(this.id, amount));
    }

    private void validateActive() {
        if (status != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Account not active");
        }
    }
}
```

### Value Objects
```java
public record Money(BigDecimal amount, Currency currency) {
    public Money {
        Objects.requireNonNull(amount, "Amount required");
        Objects.requireNonNull(currency, "Currency required");
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount >= 0");
        }
    }

    public Money add(Money other) {
        if (!currency.equals(other.currency())) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        return new Money(amount.add(other.amount()), currency);
    }
}
```

### Domain Events
```java
public interface DomainEvent {
    Instant occurredAt();
    String eventId();
}

public record AccountCreditedEvent(
    String eventId,
    Instant occurredAt,
    AccountId accountId,
    Money amount
) implements DomainEvent {}
```

## Repository Pattern

```java
public interface AccountRepository {
    Optional<Account> findById(AccountId id);
    List<Account> findByOwner(CustomerId ownerId);
    void save(Account account);
    void delete(AccountId id);
}

@Repository
class JpaAccountRepository implements AccountRepository {
    private final EntityManager em;

    @Override
    public Optional<Account> findById(AccountId id) {
        return Optional.ofNullable(em.find(AccountEntity.class, id.value()))
            .map(this::toDomain);
    }

    @Override
    public void save(Account account) {
        em.merge(toEntity(account));
    }
}
```

## CQRS Pattern

```java
// Command Side
public interface CommandHandler<C> {
    void handle(C command);
}

@Component
class TransferFundsHandler implements CommandHandler<TransferFundsCommand> {
    private final AccountRepository accounts;

    @Override
    @Transactional
    public void handle(TransferFundsCommand cmd) {
        Account from = accounts.findById(cmd.fromId()).orElseThrow();
        Account to = accounts.findById(cmd.toId()).orElseThrow();
        from.transferTo(to, cmd.amount());
        accounts.save(from);
        accounts.save(to);
    }
}

// Query Side
@QueryHandler
class AccountBalanceQueryHandler {
    private final AccountViewRepository views;

    BigDecimal handle(GetBalanceQuery query) {
        return views.findById(query.accountId())
            .map(AccountView::balance)
            .orElseThrow();
    }
}
```

## Transaction Management

```java
@Service
@Transactional
class AccountService {
    private final AccountRepository accounts;

    public void transfer(AccountId from, AccountId to, Money amount) {
        Account source = accounts.findById(from).orElseThrow();
        Account target = accounts.findById(to).orElseThrow();
        source.transferTo(target, amount);
    }
}
```

## Exception Handling

```java
@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientFundsException.class)
    ResponseEntity<ErrorResponse> handle(InsufficientFundsException e) {
        return ResponseEntity
            .status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(new ErrorResponse("INSUFFICIENT_FUNDS", e.getMessage()));
    }

    @ExceptionHandler(AccountNotFoundException.class)
    ResponseEntity<ErrorResponse> handle(AccountNotFoundException e) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse("ACCOUNT_NOT_FOUND", e.getMessage()));
    }
}
```

## Configuration

```java
@Configuration
@EnableTransactionManagement
class AppConfig {

    @Bean
    PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
```

## Testing Strategy

```java
@DataJpaTest
class AccountRepositoryTest {
    @Autowired AccountRepository repository;

    @Test
    void findById_returnsAccount() {
        Account account = new Account(new AccountId("1"), new CustomerId("1"), Money.of(100, "USD"));
        repository.save(account);

        Optional<Account> found = repository.findById(new AccountId("1"));
        assertThat(found).isPresent().get().isEqualTo(account);
    }
}

@SpringBootTest
class TransferFundsIntegrationTest {
    @Autowired AccountService service;

    @Test
    void transfer_movesFundsBetweenAccounts() {
        Account a = save(new Account("1", "cust1", Money.of(1000)));
        Account b = save(new Account("2", "cust2", Money.of(500)));

        service.transfer(a.getId(), b.getId(), Money.of(200));

        assertThat(repository.findById(a.getId()).get().getBalance()).isEqualTo(Money.of(800));
        assertThat(repository.findById(b.getId()).get().getBalance()).isEqualTo(Money.of(700));
    }
}
```

---

## Key Patterns Summary

| Pattern | Use Case |
|---------|----------|
| Repository | Data access abstraction |
| Unit of Work | Transaction management |
| Domain Events | Cross-aggregate consistency |
| CQRS | Read/write separation |
| Aggregate | Consistency boundary |
| Value Object | Immutable data carriers |
| Factory | Complex object creation |
| Specification | Query composition |
| Specification | Query composition |

---

## Anti-Patterns to Avoid

| Anti-Pattern | Problem | Fix |
|--------------|---------|-----|
| Anemic Domain | Logic in services | Rich domain model |
| Fat Controllers | Business logic in controllers | Move to domain services |
| Anemic Repository | Logic in repository | Repository = data access only |
| DTOs in Domain | Leaking infrastructure | Separate DTOs |
| God Service | Does everything | Split by bounded context |
| God Service | Does everything | Split by bounded context |

---

## 📝 Quick Reference Card

| Concept | Key Point |
|---------|-----------|
| `main` signature | `public static void main(String[] args)` |
| Integer division | `10/3 = 3` (not 3.33) |
| String comparison | `.equals()` not `==` |
| String mutability | Immutable (use StringBuilder) |
| Pass-by-value | Always (references passed by value) |
| Switch expression | Returns value, no fall-through |
| Varargs | `type...` last parameter only |
| Default char | `'\u0000'` |
| Default boolean | `false` |

---

## 🎯 Score Interpretation
- **28-30:** Excellent (Mastery)
- **24-27:** Good (Proficient)
- **20-23:** Fair (Needs review)
- **<20:** Retake recommended

---

*Self-grade honestly. Review wrong answers with theory.md.*