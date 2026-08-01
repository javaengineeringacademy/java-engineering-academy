# Enterprise OOP Design

## Table of Contents

1. [Introduction](#1-introduction)
2. [Learning Objectives](#2-learning-objectives)
3. [Prerequisites](#3-prerequisites)
4. [Why This Concept Exists](#4-why-this-concept-exists)
5. [Problem Statement](#5-problem-statement)
6. [Theory](#6-theory)
7. [Internal Working](#7-internal-working)
8. [JVM Perspective](#8-jvm-perspective)
9. [Memory Representation](#9-memory-representation)
10. [Syntax](#10-syntax)
11. [Easy Example](#11-easy-example)
12. [Medium Example](#12-medium-example)
13. [Hard Example](#13-hard-example)
14. [Enterprise Example](#14-enterprise-example)
15. [Performance](#15-performance)
16. [Best Practices](#16-best-practices)
17. [Common Mistakes](#17-common-mistakes)
18. [Pitfalls](#18-pitfalls)
19. [Debugging Tips](#19-debugging-tips)
20. [Comparison Table](#20-comparison-table)
21. [Decision Tree](#21-decision-tree)
22. [Interview Questions](#22-interview-questions)
23. [Exercises](#23-exercises)
24. [Assignments](#24-assignments)
25. [Mini Project](#25-mini-project)
26. [Summary](#26-summary)
27. [References](#27-references)

---

## 1. Introduction

Enterprise Object-Oriented Programming (OOP) design encompasses the principles, patterns, and architectural decisions that enable building scalable, maintainable, and robust software systems. Unlike small-scale applications, enterprise systems must handle complex business domains, multiple teams, evolving requirements, and decades-long lifespans.

Enterprise OOP design combines:

- **Design Patterns** — Reusable solutions to recurring problems (Singleton, Factory, Builder, Observer, Strategy)
- **Architectural Patterns** — Layering, hexagonal architecture, event-driven systems
- **Domain-Driven Design (DDD)** — Aligning code structure with business domains
- **Package Design** — Organizing code for cohesion and encapsulation
- **SOLID Principles** — Foundation for all design decisions

This document provides a comprehensive guide to designing enterprise Java applications using Java 21 features, real-world code examples, and Google Java Style conventions.

---

## 2. Learning Objectives

By the end of this document, you will be able to:

- **Identify** when to apply design patterns (Singleton, Factory, Builder, Observer, Strategy)
- **Design** layered architectures with clear separation of concerns
- **Implement** Domain-Driven Design concepts (entities, value objects, aggregates, domain events)
- **Apply** SOLID principles to enterprise codebases
- **Structure** packages using domain-based organization
- **Evaluate** trade-offs between patterns using decision trees
- **Debug** enterprise applications using systematic techniques
- **Avoid** common anti-patterns (anemic domain, god objects, tight coupling)
- **Build** complete enterprise applications with proper transaction management and exception handling

---

## 3. Prerequisites

Before studying enterprise OOP design, ensure proficiency in:

| Topic | Required Level | Resource |
|-------|---------------|----------|
| Java fundamentals | Advanced | Variables, control flow, OOP basics |
| Collections framework | Proficient | List, Map, Stream API |
| Exception handling | Proficient | Try-catch, custom exceptions |
| Generics | Intermediate | Type parameters, bounds |
| Records | Basic | Java 16+ records |
| Sealed classes | Basic | Java 17+ sealed classes |
| Pattern matching | Basic | Java 21 switch expressions |
| SOLID principles | Familiar | Single responsibility, etc. |
| Basic design patterns | Familiar | At least 2-3 patterns |

---

## 4. Why This Concept Exists

### The Scale Problem

Small applications can survive with ad-hoc design. Enterprise systems cannot:

```
Small App (1-5 developers)         Enterprise App (50-500 developers)
├── One package                    ├── Hundreds of packages
├── Direct method calls            ├── Service boundaries
├── Simple data flow               ├── Complex event flows
├── Manual testing                 ├── Automated test suites
├── Single deployment              ├── Multi-module builds
└── Works for months               └── Must work for decades
```

### The Complexity Problem

Enterprise systems face:

1. **Domain complexity** — Banking, healthcare, logistics have intricate business rules
2. **Integration complexity** — Multiple external systems, APIs, databases
3. **Team complexity** — Parallel development, merge conflicts, ownership boundaries
4. **Evolution complexity** — Requirements change, technologies evolve

### The Solution

Design patterns and architectural principles provide:

- **Shared vocabulary** — Teams communicate using "Factory" instead of describing object creation
- **Proven solutions** — Patterns are battle-tested approaches
- **Enforced boundaries** — Layering prevents tangled dependencies
- **Testability** — Proper design enables unit and integration testing
- **Maintainability** — Clear structure reduces cognitive load

---

## 5. Problem Statement

### Core Problem

How do you design a system that:
1. Models complex business domains accurately
2. Allows parallel development across teams
3. Remains maintainable as requirements evolve
4. Handles failures gracefully
5. Scales horizontally when needed

### Specific Challenges

| Challenge | Example | Pattern Solution |
|-----------|---------|-----------------|
| Object creation complexity | Creating an Order with 15 fields | Builder |
| Ensuring single instances | Database connection pool | Singleton |
| Decoupling producers/consumers | Order placed → notification sent | Observer |
| Varying algorithms | Different pricing strategies | Strategy |
| Complex object families | Different UI components per platform | Factory |
| Cross-cutting concerns | Logging, security, transactions | AOP + Layering |

### What Happens Without Design

```java
// Anti-pattern: God Object
public class OrderManager {
    // 500+ lines doing everything
    public void createOrder(...) { /* validation, DB, email, inventory */ }
    public void cancelOrder(...) { /* validation, DB, refund, email */ }
    public void generateReport(...) { /* query, format, export, email */ }
    // Everything coupled to everything
}
```

Problems: untestable, unmodifiable, single-point failure, team conflicts.

---

## 6. Theory

### 6.1 SOLID Principles

| Principle | Definition | Enterprise Application |
|-----------|-----------|----------------------|
| **S** — Single Responsibility | One reason to change | Each service handles one bounded context |
| **O** — Open/Closed | Open for extension, closed for modification | Strategy pattern for swappable algorithms |
| **L** — Liskov Substitution | Subtypes must be substitutable | Repository interfaces with multiple implementations |
| **I** — Interface Segregation | Many specific interfaces | Separate Read/Write repositories |
| **D** — Dependency Inversion | Depend on abstractions | Inject interfaces, not concrete classes |

### 6.2 Design Patterns

#### Singleton Pattern
Ensures exactly one instance exists. Use for configuration, connection pools, caches.

```java
public final class ApplicationConfig {
    private static final AtomicReference<ApplicationConfig> INSTANCE =
        new AtomicReference<>();

    private final Map<String, String> settings;

    private ApplicationConfig(Map<String, String> settings) {
        this.settings = Map.copyOf(settings);
    }

    public static ApplicationConfig getInstance() {
        return INSTANCE.updateAndGet(current -> {
            if (current == null) {
                return new ApplicationConfig(loadSettings());
            }
            return current;
        });
    }

    public String get(String key) {
        return settings.get(key);
    }
}
```

#### Factory Pattern
Delegates object creation to subclasses or methods. Use when creation logic is complex.

```java
public interface NotificationSender {
    void send(Notification notification);
}

public final class NotificationSenderFactory {
    private NotificationSenderFactory() {}

    public static NotificationSender create(NotificationType type) {
        return switch (type) {
            case EMAIL -> new EmailSender();
            case SMS -> new SmsSender();
            case PUSH -> new PushNotificationSender();
            case WEBHOOK -> new WebhookSender();
        };
    }
}
```

#### Builder Pattern
Constructs complex objects step-by-step. Use when constructors have many parameters.

```java
public final class Order {
    private final OrderId id;
    private final CustomerId customerId;
    private final List<OrderLine> lines;
    private final Money totalAmount;
    private final Instant createdAt;
    private final OrderStatus status;
    private final String notes;

    private Order(Builder builder) {
        this.id = builder.id;
        this.customerId = builder.customerId;
        this.lines = List.copyOf(builder.lines);
        this.totalAmount = builder.totalAmount;
        this.createdAt = builder.createdAt;
        this.status = builder.status;
        this.notes = builder.notes;
    }

    public static Builder builder(OrderId id, CustomerId customerId) {
        return new Builder(id, customerId);
    }

    public static final class Builder {
        private final OrderId id;
        private final CustomerId customerId;
        private final List<OrderLine> lines = new ArrayList<>();
        private Money totalAmount = Money.ZERO;
        private Instant createdAt = Instant.now();
        private OrderStatus status = OrderStatus.DRAFT;
        private String notes = "";

        private Builder(OrderId id, CustomerId customerId) {
            this.id = Objects.requireNonNull(id);
            this.customerId = Objects.requireNonNull(customerId);
        }

        public Builder addLine(ProductId productId, int quantity, Money unitPrice) {
            this.lines.add(new OrderLine(productId, quantity, unitPrice));
            this.totalAmount = totalAmount.add(unitPrice.multiply(quantity));
            return this;
        }

        public Builder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public Builder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public Order build() {
            if (lines.isEmpty()) {
                throw new IllegalStateException("Order must have at least one line");
            }
            return new Order(this);
        }
    }
}
```

#### Observer Pattern
Defines a one-to-many dependency. Use for event-driven architectures.

```java
public interface DomainEventPublisher {
    void publish(DomainEvent event);
}

public interface DomainEventListener {
    void onEvent(DomainEvent event);
}

public final class InMemoryEventPublisher implements DomainEventPublisher {
    private final Map<Class<?>, List<DomainEventListener>> listeners =
        new ConcurrentHashMap<>();

    public <T extends DomainEvent> void subscribe(
        Class<T> eventType, DomainEventListener listener) {
        listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>())
            .add(listener);
    }

    @Override
    public void publish(DomainEvent event) {
        listeners.getOrDefault(event.getClass(), List.of())
            .forEach(listener -> listener.onEvent(event));
    }
}
```

#### Strategy Pattern
Defines a family of algorithms. Use when behavior varies at runtime.

```java
public interface PricingStrategy {
    Money calculatePrice(Order order);
}

public final class StandardPricing implements PricingStrategy {
    @Override
    public Money calculatePrice(Order order) {
        return order.lines().stream()
            .map(line -> line.unitPrice().multiply(line.quantity()))
            .reduce(Money.ZERO, Money::add);
    }
}

public final class MemberPricing implements PricingStrategy {
    private final BigDecimal discountRate;

    public MemberPricing(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public Money calculatePrice(Order order) {
        Money base = new StandardPricing().calculatePrice(order);
        return base.multiply(BigDecimal.ONE.subtract(discountRate));
    }
}
```

### 6.3 Architectural Patterns

#### Layered Architecture

```
┌─────────────────────────────────────────┐
│           Presentation Layer            │  Controllers, DTOs, Validation
├─────────────────────────────────────────┤
│           Application Layer             │  Use Cases, Commands, Queries
├─────────────────────────────────────────┤
│            Domain Layer                 │  Entities, Value Objects, Domain Services
├─────────────────────────────────────────┤
│         Infrastructure Layer            │  Persistence, External APIs, Config
└─────────────────────────────────────────┘
```

**Dependency rule:** Dependencies point inward. Domain has zero external dependencies.

#### Hexagonal Architecture (Ports & Adapters)

```
                    ┌──────────────────┐
                    │   Domain Core    │
                    │  (Use Cases)     │
                    └──────────────────┘
                    ↑                ↑
            Port (interface)   Port (interface)
                    ↑                ↑
         ┌──────────┐        ┌──────────┐
         │ Adapter  │        │ Adapter  │
         │ (REST)   │        │ (JPA)    │
         └──────────┘        └──────────┘
```

### 6.4 Domain-Driven Design

#### Building Blocks

| Concept | Definition | Example |
|---------|-----------|---------|
| **Entity** | Object with identity | `Account`, `Customer` |
| **Value Object** | Immutable, no identity | `Money`, `Address` |
| **Aggregate** | Cluster of entities with consistency boundary | `Order` + `OrderLine` |
| **Domain Event** | Something that happened | `OrderPlacedEvent` |
| **Repository** | Persistence abstraction | `AccountRepository` |
| **Domain Service** | Logic that doesn't fit in entity | `TransferService` |

#### Entities vs Value Objects

```java
// Entity: has identity, mutable state
public final class Account {
    private final AccountId id;  // Identity
    private Money balance;       // Mutable state

    public void deposit(Money amount) {
        this.balance = balance.add(amount);  // State change
    }
}

// Value Object: no identity, immutable
public record Money(BigDecimal amount, Currency currency) {
    public Money {
        Objects.requireNonNull(amount);
        Objects.requireNonNull(currency);
    }

    public Money add(Money other) {
        return new Money(amount.add(other.amount()), currency);  // New instance
    }
}
```

---

## 7. Internal Working

### 7.1 Singleton Lifecycle

```
Application Start
       │
       ▼
┌─────────────┐     First call      ┌─────────────────┐
│ getInstance()│ ──────────────────► │ Create instance  │
└─────────────┘                      └─────────────────┘
       │                                      │
       │                                      ▼
       │                              ┌─────────────────┐
       │  Subsequent calls            │ Store in static  │
       └────────────────────────────► │ field            │
                                      └─────────────────┘
```

### 7.2 Factory Creation Flow

```
Client Code
     │
     ▼
┌──────────────┐
│ Factory.create│
└──────────────┘
     │
     ├─ Type.A → new ConcreteA()
     ├─ Type.B → new ConcreteB()
     └─ Type.C → new ConcreteC()
           │
           ▼
    Return interface type
```

### 7.3 Observer Event Flow

```
Domain Entity                  Event Publisher              Listeners
     │                              │                         │
     │── publish(event) ──────────►│                         │
     │                              │── Route by type ───────►│
     │                              │                         │── Handle event
     │                              │                         │── Side effects
     │                              │                         │    (email, cache,
     │                              │                         │     audit log)
```

### 7.4 Strategy Resolution

```
Context
   │
   ├─ Strategy field (set via constructor or setter)
   │
   ▼
context.execute()
   │
   ├─ If StandardPricing → calculate normally
   ├─ If MemberPricing → apply discount
   └─ If BulkPricing → apply volume discount
```

---

## 8. JVM Perspective

### 8.1 Class Loading

Enterprise applications load thousands of classes. The JVM uses a delegation model:

```
Bootstrap ClassLoader (JDK core)
        ↑
Application ClassLoader (classpath)
        ↑
Custom ClassLoaders (plugins, hot-reload)
```

**Implications for Singletons:**
- Different classloaders create separate instances
- In application servers, use library-priority loading

### 8.2 JIT Compilation

HotSpot JIT aggressively optimizes:

| Optimization | Impact on Patterns |
|-------------|-------------------|
| Method inlining | Strategy calls become zero-overhead |
| Escape analysis | Builder temporaries eliminated |
| Null check elimination | Objects.requireNonNull helps JIT |
| Branch prediction | Switch-based factory optimized |

### 8.3 Memory Model (JMM)

Enterprise applications rely on thread safety:

```java
// Safe: volatile + AtomicReference
public final class Singleton {
    private static volatile Singleton instance;

    public static Singleton getInstance() {
        if (instance == null) {                    // First check (no sync)
            synchronized (Singleton.class) {
                if (instance == null) {            // Second check (synced)
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}

// Safer: Using AtomicReference (Java 21)
public final class Singleton {
    private static final AtomicReference<Singleton> INSTANCE =
        new AtomicReference<>();

    public static Singleton getInstance() {
        return INSTANCE.updateAndGet(curr ->
            curr == null ? new Singleton() : curr
        );
    }
}
```

### 8.4 Virtual Threads (Java 21)

Enterprise applications benefit from virtual threads:

```java
// Traditional: one platform thread per request
ExecutorService executor = Executors.newFixedThreadPool(200);

// Java 21: virtual threads — millions concurrent
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    IntStream.range(0, 100_000).forEach(i ->
        executor.submit(() -> processRequest(i))
    );
}
```

**Pattern impact:**
- Singleton must be thread-safe (virtual threads don't change this)
- Strategy pattern becomes more powerful (each virtual thread can use different strategy)
- Observer pattern benefits from concurrent event handling

---

## 9. Memory Representation

### 9.1 Object Layout in Heap

```
┌─────────────────────────────────────┐
│ Account Instance (Heap)             │
├─────────────────────────────────────┤
│ Object Header (16 bytes)            │
│ ├─ Mark Word (8 bytes)             │
│ └─ Klass Pointer (4 bytes)         │
│ ├─ Padding (4 bytes)               │
├─────────────────────────────────────┤
│ Fields:                             │
│ ├─ id: AccountId reference (8B)    │
│ ├─ balance: Money reference (8B)   │
│ ├─ ownerId: CustomerId ref (8B)    │
│ └─ status: AccountStatus enum (4B) │
├─────────────────────────────────────┤
│ Total: ~48 bytes + referenced data  │
└─────────────────────────────────────┘
```

### 9.2 Value Object Memory

Records are more memory-efficient:

```java
// Record: compact layout
public record Money(BigDecimal amount, Currency currency) {}

// Traditional class: same fields, more overhead
public class MoneyOld {
    private BigDecimal amount;     // 8 bytes ref
    private Currency currency;     // 8 bytes ref
    // + object header (16 bytes)
    // + possible padding
}
```

### 9.3 Generational Garbage Collection

Enterprise objects are mostly short-lived:

```
Young Generation (Eden + Survivor)
├── Request-scoped objects (DTOs, builders)
├── Temporary collections (stream results)
└── Strategy instances (if created per-request)

Old Generation (Tenured)
├── Singleton instances
├── Repository implementations
├── Event publishers
└── Long-lived caches
```

**Tuning tips:**
- Use `-XX:MaxRAMPercentage=75.0` for containers
- Monitor with `-Xlog:gc*` (Java 21 unified logging)
- Consider `-XX:+UseZGC` for low-latency requirements

---

## 10. Syntax

### 10.1 Records (Java 16+)

```java
// Basic record
public record AccountId(String value) {
    public AccountId {
        Objects.requireNonNull(value);
    }
}

// Record with methods
public record Money(BigDecimal amount, Currency currency) {
    public static final Money ZERO = new Money(BigDecimal.ZERO, Currency.USD);

    public Money add(Money other) {
        verifySameCurrency(other);
        return new Money(amount.add(other.amount()), currency);
    }

    public Money multiply(int factor) {
        return new Money(amount.multiply(BigDecimal.valueOf(factor)), currency);
    }

    private void verifySameCurrency(Money other) {
        if (!currency.equals(other.currency())) {
            throw new CurrencyMismatchException(currency, other.currency);
        }
    }
}
```

### 10.2 Sealed Classes (Java 17+)

```java
// Sealed interface
public sealed interface DomainEvent
    permits OrderPlacedEvent, OrderCancelledEvent, PaymentReceivedEvent {
    Instant occurredAt();
    String eventId();
}

// Sealed record implementations
public record OrderPlacedEvent(
    String eventId,
    Instant occurredAt,
    OrderId orderId,
    CustomerId customerId
) implements DomainEvent {}

public record OrderCancelledEvent(
    String eventId,
    Instant occurredAt,
    OrderId orderId,
    String reason
) implements DomainEvent {}
```

### 10.3 Pattern Matching (Java 21)

```java
// Switch with pattern matching
public String describe(DomainEvent event) {
    return switch (event) {
        case OrderPlacedEvent e ->
            "Order %s placed by customer %s".formatted(e.orderId(), e.customerId());
        case OrderCancelledEvent e ->
            "Order %s cancelled: %s".formatted(e.orderId(), e.reason());
        case PaymentReceivedEvent e ->
            "Payment received for order %s".formatted(e.orderId());
    };
}

// Pattern matching with guards
public Money calculateShipping(Order order) {
    return switch (order) {
        case Order o when o.totalAmount().isAfter(Money.of(100, "USD")) ->
            Money.ZERO;  // Free shipping over $100
        case Order o when o.lines().size() > 5 ->
            Money.of(9.99, "USD");  // Flat rate for many items
        default ->
            Money.of(14.99, "USD");  // Standard shipping
    };
}
```

### 10.4 Text Blocks (Java 15+)

```java
public class SqlQueries {
    public static final String FIND_ACTIVE_ACCOUNTS = """
        SELECT a.id, a.balance, a.status
        FROM accounts a
        WHERE a.status = 'ACTIVE'
          AND a.balance > 0
        ORDER BY a.balance DESC
        """;
}
```

### 10.5 Switch Expressions (Java 14+)

```java
public OrderStatus nextStatus(OrderStatus current) {
    return switch (current) {
        case DRAFT -> SUBMITTED;
        case SUBMITTED -> CONFIRMED;
        case CONFIRMED -> PROCESSING;
        case PROCESSING -> SHIPPED;
        case SHIPPED -> DELIVERED;
        case DELIVERED, CANCELLED -> current;  // Terminal states
    };
}
```

---

## 11. Easy Example

### Singleton: Application Configuration

```java
public final class AppConfig {
    private static final AtomicReference<AppConfig> INSTANCE =
        new AtomicReference<>();

    private final String applicationName;
    private final int maxConnections;
    private final Duration timeout;

    private AppConfig(String applicationName, int maxConnections, Duration timeout) {
        this.applicationName = applicationName;
        this.maxConnections = maxConnections;
        this.timeout = timeout;
    }

    public static AppConfig getInstance() {
        return INSTANCE.updateAndGet(current -> {
            if (current == null) {
                return new AppConfig(
                    System.getenv().getOrDefault("APP_NAME", "default"),
                    Integer.parseInt(
                        System.getenv().getOrDefault("MAX_CONNECTIONS", "10")),
                    Duration.ofSeconds(30)
                );
            }
            return current;
        });
    }

    public String applicationName() { return applicationName; }
    public int maxConnections() { return maxConnections; }
    public Duration timeout() { return timeout; }
}
```

### Factory: Notification Sender

```java
public sealed interface Notification
    permits EmailNotification, SmsNotification, PushNotification {
    String recipient();
    String body();
}

public record EmailNotification(String recipient, String body, String subject)
    implements Notification {}

public record SmsNotification(String recipient, String body)
    implements Notification {}

public record PushNotification(String recipient, String body, String deviceId)
    implements Notification {}

public final class NotificationSenderFactory {
    private NotificationSenderFactory() {}

    public static void send(Notification notification) {
        switch (notification) {
            case EmailNotification e -> sendEmail(e);
            case SmsNotification s -> sendSms(s);
            case PushNotification p -> sendPush(p);
        }
    }

    private static void sendEmail(EmailNotification e) {
        System.out.println("Email to %s: %s".formatted(e.recipient(), e.body()));
    }

    private static void sendSms(SmsNotification s) {
        System.out.println("SMS to %s: %s".formatted(s.recipient(), s.body()));
    }

    private static void sendPush(PushNotification p) {
        System.out.println("Push to %s: %s".formatted(p.deviceId(), p.body()));
    }
}
```

### Builder: Simple Query

```java
public final class UserQuery {
    private final String namePattern;
    private final int minAge;
    private final int maxAge;
    private final int limit;

    private UserQuery(Builder builder) {
        this.namePattern = builder.namePattern;
        this.minAge = builder.minAge;
        this.maxAge = builder.maxAge;
        this.limit = builder.limit;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String namePattern = "%";
        private int minAge = 0;
        private int maxAge = Integer.MAX_VALUE;
        private int limit = 100;

        public Builder namePattern(String pattern) {
            this.namePattern = pattern;
            return this;
        }

        public Builder ageRange(int min, int max) {
            this.minAge = min;
            this.maxAge = max;
            return this;
        }

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public UserQuery build() {
            if (minAge < 0 || maxAge < minAge) {
                throw new IllegalArgumentException("Invalid age range");
            }
            return new UserQuery(this);
        }
    }
}
```

---

## 12. Medium Example

### Strategy: Payment Processing

```java
public record PaymentRequest(
    OrderId orderId,
    Money amount,
    PaymentMethod method
) {}

public sealed interface PaymentMethod
    permits CreditCard, BankTransfer, DigitalWallet {
}

public record CreditCard(String cardNumber, String cvv) implements PaymentMethod {}
public record BankTransfer(String accountNumber, String routingNumber)
    implements PaymentMethod {}
public record DigitalWallet(String walletId) implements PaymentMethod {}

public interface PaymentProcessor {
    PaymentResult process(PaymentRequest request);
}

public final class CreditCardProcessor implements PaymentProcessor {
    @Override
    public PaymentResult process(PaymentRequest request) {
        CreditCard card = (CreditCard) request.method();
        // Validate and process credit card
        System.out.println("Processing credit card: %s".formatted(
            maskCardNumber(card.cardNumber())));
        return new PaymentResult(true, "CC-" + UUID.randomUUID());
    }

    private String maskCardNumber(String number) {
        return "****-****-****-" + number.substring(number.length() - 4);
    }
}

public final class BankTransferProcessor implements PaymentProcessor {
    @Override
    public PaymentResult process(PaymentRequest request) {
        BankTransfer transfer = (BankTransfer) request.method();
        System.out.println("Processing bank transfer from: %s".formatted(
            transfer.accountNumber()));
        return new PaymentResult(true, "BT-" + UUID.randomUUID());
    }
}

public record PaymentResult(boolean success, String transactionId) {}

public final class PaymentService {
    private final Map<Class<?>, PaymentProcessor> processors = Map.of(
        CreditCard.class, new CreditCardProcessor(),
        BankTransfer.class, new BankTransferProcessor()
    );

    public PaymentResult processPayment(PaymentRequest request) {
        PaymentProcessor processor = processors.get(request.method().getClass());
        if (processor == null) {
            throw new UnsupportedPaymentMethodException(request.method());
        }
        return processor.process(request);
    }
}
```

### Observer: Order Lifecycle Events

```java
public interface EventListener<T> {
    void handle(T event);
}

public final class OrderEventBus {
    private final Map<Class<?>, List<EventListener<?>>> listeners =
        new ConcurrentHashMap<>();

    public <T> void subscribe(Class<T> eventType, Listener<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>())
            .add(listener);
    }

    @SuppressWarnings("unchecked")
    public <T> void publish(T event) {
        listeners.getOrDefault(event.getClass(), List.of())
            .forEach(listener -> ((Listener<T>) listener).handle(event));
    }

    @FunctionalInterface
    public interface Listener<T> {
        void handle(T event);
    }
}

// Domain events
public record OrderPlacedEvent(OrderId orderId, CustomerId customerId,
    Money totalAmount, Instant occurredAt) {}

public record OrderShippedEvent(OrderId orderId, String trackingNumber,
    Instant occurredAt) {}

// Event handlers
public class InventoryHandler implements OrderEventBus.Listener<OrderPlacedEvent> {
    @Override
    public void handle(OrderPlacedEvent event) {
        System.out.println("Reserving inventory for order: %s".formatted(
            event.orderId()));
    }
}

public class NotificationHandler implements OrderEventBus.Listener<OrderPlacedEvent> {
    @Override
    public void handle(OrderPlacedEvent event) {
        System.out.println("Sending confirmation email for order: %s".formatted(
            event.orderId()));
    }
}

public class ShippingHandler implements OrderEventBus.Listener<OrderShippedEvent> {
    @Override
    public void handle(OrderShippedEvent event) {
        System.out.println("Tracking number: %s".formatted(
            event.trackingNumber()));
    }
}
```

### Factory + Strategy: Pricing

```java
public interface PricingStrategy {
    Money calculatePrice(List<OrderLine> lines);
}

public record OrderLine(ProductId productId, int quantity, Money unitPrice) {
    public Money subtotal() {
        return unitPrice.multiply(quantity);
    }
}

public final class StandardPricing implements PricingStrategy {
    @Override
    public Money calculatePrice(List<OrderLine> lines) {
        return lines.stream()
            .map(OrderLine::subtotal)
            .reduce(Money.ZERO, Money::add);
    }
}

public final class BulkPricing implements PricingStrategy {
    private final int threshold;
    private final BigDecimal discountRate;

    public BulkPricing(int threshold, BigDecimal discountRate) {
        this.threshold = threshold;
        this.discountRate = discountRate;
    }

    @Override
    public Money calculatePrice(List<OrderLine> lines) {
        Money base = lines.stream()
            .map(OrderLine::subtotal)
            .reduce(Money.ZERO, Money::add);

        long totalItems = lines.stream()
            .mapToLong(OrderLine::quantity)
            .sum();

        if (totalItems >= threshold) {
            return base.multiply(BigDecimal.ONE.subtract(discountRate));
        }
        return base;
    }
}

public final class PricingStrategyFactory {
    private PricingStrategyFactory() {}

    public static PricingStrategy create(String customerType) {
        return switch (customerType) {
            case "STANDARD" -> new StandardPricing();
            case "BULK" -> new BulkPricing(10, new BigDecimal("0.10"));
            case "PREMIUM" -> new BulkPricing(5, new BigDecimal("0.20"));
            default -> throw new IllegalArgumentException(
                "Unknown customer type: " + customerType);
        };
    }
}
```

---

## 13. Hard Example

### Complete DDD: Banking Transfer

```java
// Value Objects
public record AccountId(UUID value) {
    public AccountId {
        Objects.requireNonNull(value);
    }

    public static AccountId generate() {
        return new AccountId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

public record Money(BigDecimal amount, Currency currency) implements Comparable<Money> {
    public static final Money ZERO = new Money(BigDecimal.ZERO, Currency.USD);

    public Money {
        Objects.requireNonNull(amount, "Amount required");
        Objects.requireNonNull(currency, "Currency required");
        if (amount.scale() > currency.getDefaultFractionDigits()) {
            throw new IllegalArgumentException(
                "Too many decimal places for " + currency);
        }
    }

    public static Money of(double amount, Currency currency) {
        return new Money(
            BigDecimal.valueOf(amount)
                .setScale(currency.getDefaultFractionDigits(), RoundingMode.HALF_UP),
            currency);
    }

    public Money add(Money other) {
        verifySameCurrency(other);
        return new Money(amount.add(other.amount()), currency);
    }

    public Money subtract(Money other) {
        verifySameCurrency(other);
        return new Money(amount.subtract(other.amount()), currency);
    }

    public Money multiply(int factor) {
        return new Money(
            amount.multiply(BigDecimal.valueOf(factor)), currency);
    }

    public boolean isGreaterThan(Money other) {
        verifySameCurrency(other);
        return amount.compareTo(other.amount()) > 0;
    }

    public boolean isNegative() {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

    @Override
    public int compareTo(Money other) {
        verifySameCurrency(other);
        return amount.compareTo(other.amount());
    }

    private void verifySameCurrency(Money other) {
        if (!currency.equals(other.currency())) {
            throw new CurrencyMismatchException(currency, other.currency());
        }
    }
}

// Domain Events
public sealed interface AccountEvent
    permits AccountOpenedEvent, MoneyDepositedEvent, MoneyWithdrawnEvent,
            AccountClosedEvent {
    AccountId accountId();
    Instant occurredAt();
    String eventId();
}

public record AccountOpenedEvent(
    String eventId, Instant occurredAt, AccountId accountId, CustomerId ownerId
) implements AccountEvent {}

public record MoneyDepositedEvent(
    String eventId, Instant occurredAt, AccountId accountId, Money amount,
    Money newBalance
) implements AccountEvent {}

public record MoneyWithdrawnEvent(
    String eventId, Instant occurredAt, AccountId accountId, Money amount,
    Money newBalance
) implements AccountEvent {}

public record AccountClosedEvent(
    String eventId, Instant occurredAt, AccountId accountId, String reason
) implements AccountEvent {}

// Aggregate Root
public final class Account {
    private final AccountId id;
    private final CustomerId ownerId;
    private Money balance;
    private AccountStatus status;
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public Account(AccountId id, CustomerId ownerId, Money initialBalance) {
        this.id = Objects.requireNonNull(id);
        this.ownerId = Objects.requireNonNull(ownerId);
        this.balance = Objects.requireNonNull(initialBalance);
        this.status = AccountStatus.ACTIVE;
        addEvent(new AccountOpenedEvent(
            UUID.randomUUID().toString(), Instant.now(), id, ownerId));
    }

    public void deposit(Money amount) {
        ensureActive();
        if (amount.isNegative()) {
            throw new NegativeAmountException(amount);
        }
        balance = balance.add(amount);
        addEvent(new MoneyDepositedEvent(
            UUID.randomUUID().toString(), Instant.now(), id, amount, balance));
    }

    public void withdraw(Money amount) {
        ensureActive();
        if (amount.isNegative()) {
            throw new NegativeAmountException(amount);
        }
        if (balance.isLessThan(amount)) {
            throw new InsufficientFundsException(id, balance, amount);
        }
        balance = balance.subtract(amount);
        addEvent(new MoneyWithdrawnEvent(
            UUID.randomUUID().toString(), Instant.now(), id, amount, balance));
    }

    public void close(String reason) {
        ensureActive();
        if (!balance.equals(Money.ZERO)) {
            throw new AccountNotEmptyException(id, balance);
        }
        status = AccountStatus.CLOSED;
        addEvent(new AccountClosedEvent(
            UUID.randomUUID().toString(), Instant.now(), id, reason));
    }

    public List<DomainEvent> pullEvents() {
        List<DomainEvent> events = List.copyOf(domainEvents);
        domainEvents.clear();
        return events;
    }

    private void ensureActive() {
        if (status != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException(id, status);
        }
    }

    private void addEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    // Getters
    public AccountId id() { return id; }
    public CustomerId ownerId() { return ownerId; }
    public Money balance() { return balance; }
    public AccountStatus status() { return status; }
}

// Domain Service
public final class TransferService {
    private final AccountRepository accounts;
    private final EventPublisher events;

    public TransferService(AccountRepository accounts, EventPublisher events) {
        this.accounts = accounts;
        this.events = events;
    }

    @Transactional
    public void transfer(AccountId fromId, AccountId toId, Money amount) {
        Account from = accounts.findById(fromId)
            .orElseThrow(() -> new AccountNotFoundException(fromId));
        Account to = accounts.findById(toId)
            .orElseThrow(() -> new AccountNotFoundException(toId));

        from.withdraw(amount);
        to.deposit(amount);

        accounts.save(from);
        accounts.save(to);

        from.pullEvents().forEach(events::publish);
        to.pullEvents().forEach(events::publish);
    }
}

// Repository Interface
public interface AccountRepository {
    Optional<Account> findById(AccountId id);
    List<Account> findByOwnerId(CustomerId ownerId);
    void save(Account account);
    void delete(AccountId id);
}

// Repository Implementation
@Repository
public class JpaAccountRepository implements AccountRepository {
    private final EntityManager entityManager;

    public JpaAccountRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Account> findById(AccountId id) {
        return Optional.ofNullable(
            entityManager.find(AccountJpaEntity.class, id.value()))
            .map(this::toDomain);
    }

    @Override
    public void save(Account account) {
        AccountJpaEntity entity = toEntity(account);
        entityManager.merge(entity);
    }

    private Account toDomain(AccountJpaEntity entity) {
        return new Account(
            new AccountId(entity.getId()),
            new CustomerId(entity.getOwnerId()),
            new Money(entity.getBalance(), entity.getCurrency()));
    }

    private AccountJpaEntity toEntity(Account account) {
        return new AccountJpaEntity(
            account.id().value(),
            account.ownerId().value(),
            account.balance().amount(),
            account.balance().currency(),
            account.status().name());
    }
}
```

---

## 14. Enterprise Example

### Complete Order Management System

```java
// ==================== DOMAIN LAYER ====================

// Value Objects
public record OrderId(UUID value) {
    public OrderId { Objects.requireNonNull(value); }
    public static OrderId generate() { return new OrderId(UUID.randomUUID()); }
}

public record CustomerId(UUID value) {
    public CustomerId { Objects.requireNonNull(value); }
}

public record ProductId(UUID value) {
    public ProductId { Objects.requireNonNull(value); }
}

public record Money(BigDecimal amount, Currency currency) implements Comparable<Money> {
    public static final Money ZERO = new Money(BigDecimal.ZERO, Currency.USD);

    public Money {
        Objects.requireNonNull(amount);
        Objects.requireNonNull(currency);
    }

    public static Money of(double amount, String currencyCode) {
        Currency currency = Currency.getInstance(currencyCode);
        return new Money(
            BigDecimal.valueOf(amount)
                .setScale(currency.getDefaultFractionDigits(), RoundingMode.HALF_UP),
            currency);
    }

    public Money add(Money other) {
        verifyCurrency(other);
        return new Money(amount.add(other.amount()), currency);
    }

    public Money multiply(int factor) {
        return new Money(amount.multiply(BigDecimal.valueOf(factor)), currency);
    }

    @Override
    public int compareTo(Money other) {
        verifyCurrency(other);
        return amount.compareTo(other.amount());
    }

    private void verifyCurrency(Money other) {
        if (!currency.equals(other.currency())) {
            throw new IllegalArgumentException("Currency mismatch");
        }
    }
}

// Enums
public enum OrderStatus {
    DRAFT, SUBMITTED, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED;

    public boolean isTerminal() {
        return this == DELIVERED || this == CANCELLED;
    }

    public OrderStatus next() {
        return switch (this) {
            case DRAFT -> SUBMITTED;
            case SUBMITTED -> CONFIRMED;
            case CONFIRMED -> PROCESSING;
            case PROCESSING -> SHIPPED;
            case SHIPPED -> DELIVERED;
            case DELIVERED, CANCELLED -> this;
        };
    }
}

// Domain Events
public sealed interface OrderEvent
    permits OrderCreatedEvent, OrderSubmittedEvent, OrderConfirmedEvent,
            OrderShippedEvent, OrderCancelledEvent {
    OrderId orderId();
    Instant occurredAt();
    String eventId();

    default String eventId() {
        return UUID.randomUUID().toString();
    }
}

public record OrderCreatedEvent(
    OrderId orderId, Instant occurredAt, CustomerId customerId
) implements OrderEvent {}

public record OrderSubmittedEvent(
    OrderId orderId, Instant occurredAt
) implements OrderEvent {}

public record OrderConfirmedEvent(
    OrderId orderId, Instant occurredAt
) implements OrderEvent {}

public record OrderShippedEvent(
    OrderId orderId, Instant occurredAt, String trackingNumber
) implements OrderEvent {}

public record OrderCancelledEvent(
    OrderId orderId, Instant occurredAt, String reason
) implements OrderEvent {}

// Aggregate Root
public final class Order {
    private final OrderId id;
    private final CustomerId customerId;
    private final List<OrderLine> lines;
    private OrderStatus status;
    private Money totalAmount;
    private String notes;
    private final Instant createdAt;
    private Instant updatedAt;
    private final List<OrderEvent> domainEvents = new ArrayList<>();

    private Order(Builder builder) {
        this.id = builder.id;
        this.customerId = builder.customerId;
        this.lines = List.copyOf(builder.lines);
        this.status = OrderStatus.DRAFT;
        this.totalAmount = calculateTotal();
        this.notes = builder.notes;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        addEvent(new OrderCreatedEvent(id, createdAt, customerId));
    }

    public static Builder builder(OrderId id, CustomerId customerId) {
        return new Builder(id, customerId);
    }

    public void submit() {
        ensureStatus(OrderStatus.DRAFT);
        if (lines.isEmpty()) {
            throw new EmptyOrderException(id);
        }
        status = OrderStatus.SUBMITTED;
        updatedAt = Instant.now();
        addEvent(new OrderSubmittedEvent(id, updatedAt));
    }

    public void confirm() {
        ensureStatus(OrderStatus.SUBMITTED);
        status = OrderStatus.CONFIRMED;
        updatedAt = Instant.now();
        addEvent(new OrderConfirmedEvent(id, updatedAt));
    }

    public void ship(String trackingNumber) {
        ensureStatus(OrderStatus.CONFIRMED);
        status = OrderStatus.PROCESSING;
        updatedAt = Instant.now();
        addEvent(new OrderShippedEvent(id, updatedAt, trackingNumber));
    }

    public void cancel(String reason) {
        if (status.isTerminal()) {
            throw new InvalidOrderStateException(
                id, status, "Cannot cancel terminal state");
        }
        status = OrderStatus.CANCELLED;
        updatedAt = Instant.now();
        addEvent(new OrderCancelledEvent(id, updatedAt, reason));
    }

    public void addLine(ProductId productId, int quantity, Money unitPrice) {
        ensureStatus(OrderStatus.DRAFT);
        lines.add(new OrderLine(productId, quantity, unitPrice));
        totalAmount = calculateTotal();
        updatedAt = Instant.now();
    }

    public List<OrderEvent> pullEvents() {
        List<OrderEvent> events = List.copyOf(domainEvents);
        domainEvents.clear();
        return events;
    }

    private Money calculateTotal() {
        return lines.stream()
            .map(line -> line.unitPrice().multiply(line.quantity()))
            .reduce(Money.ZERO, Money::add);
    }

    private void ensureStatus(OrderStatus expected) {
        if (status != expected) {
            throw new InvalidOrderStateException(id, status, expected);
        }
    }

    private void addEvent(OrderEvent event) {
        domainEvents.add(event);
    }

    // Getters
    public OrderId id() { return id; }
    public CustomerId customerId() { return customerId; }
    public List<OrderLine> lines() { return lines; }
    public OrderStatus status() { return status; }
    public Money totalAmount() { return totalAmount; }
    public String notes() { return notes; }
    public Instant createdAt() { return createdAt; }
    public Instant updatedAt() { return updatedAt; }

    public static final class Builder {
        private final OrderId id;
        private final CustomerId customerId;
        private final List<OrderLine> lines = new ArrayList<>();
        private String notes = "";

        private Builder(OrderId id, CustomerId customerId) {
            this.id = Objects.requireNonNull(id);
            this.customerId = Objects.requireNonNull(customerId);
        }

        public Builder addLine(ProductId productId, int quantity, Money unitPrice) {
            this.lines.add(new OrderLine(productId, quantity, unitPrice));
            return this;
        }

        public Builder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}

public record OrderLine(ProductId productId, int quantity, Money unitPrice) {}

// ==================== APPLICATION LAYER ====================

// Commands
public record CreateOrderCommand(
    CustomerId customerId,
    List<OrderLineDto> lines,
    String notes
) {}

public record OrderLineDto(ProductId productId, int quantity, Money unitPrice) {}

public record SubmitOrderCommand(OrderId orderId) {}
public record CancelOrderCommand(OrderId orderId, String reason) {}

// Command Handlers
@Service
public class CreateOrderHandler {
    private final OrderRepository orderRepository;
    private final EventPublisher eventPublisher;

    public CreateOrderHandler(OrderRepository orderRepository,
                               EventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrderId handle(CreateOrderCommand command) {
        OrderId orderId = OrderId.generate();
        Order.Builder builder = Order.builder(orderId, command.customerId());

        for (OrderLineDto line : command.lines()) {
            builder.addLine(line.productId(), line.quantity(), line.unitPrice());
        }

        Order order = builder.notes(command.notes()).build();
        orderRepository.save(order);
        order.pullEvents().forEach(eventPublisher::publish);

        return orderId;
    }
}

@Service
public class SubmitOrderHandler {
    private final OrderRepository orderRepository;
    private final EventPublisher eventPublisher;

    public SubmitOrderHandler(OrderRepository orderRepository,
                               EventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void handle(SubmitOrderCommand command) {
        Order order = orderRepository.findById(command.orderId())
            .orElseThrow(() -> new OrderNotFoundException(command.orderId()));
        order.submit();
        orderRepository.save(order);
        order.pullEvents().forEach(eventPublisher::publish);
    }
}

// Queries
public record GetOrderQuery(OrderId orderId) {}
public record OrderDto(
    OrderId id, CustomerId customerId, OrderStatus status,
    Money totalAmount, Instant createdAt
) {}

@Service
public class GetOrderHandler {
    private final OrderRepository orderRepository;

    public GetOrderHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderDto handle(GetOrderQuery query) {
        Order order = orderRepository.findById(query.orderId())
            .orElseThrow(() -> new OrderNotFoundException(query.orderId()));
        return new OrderDto(
            order.id(), order.customerId(), order.status(),
            order.totalAmount(), order.createdAt());
    }
}

// ==================== DOMAIN SERVICES ====================

public interface OrderRepository {
    Optional<Order> findById(OrderId id);
    List<Order> findByCustomerId(CustomerId customerId);
    void save(Order order);
}

public interface EventPublisher {
    void publish(DomainEvent event);
}

// ==================== INFRASTRUCTURE LAYER ====================

@Repository
public class JpaOrderRepository implements OrderRepository {
    private final EntityManager entityManager;

    public JpaOrderRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Order> findById(OrderId id) {
        return Optional.ofNullable(
            entityManager.find(OrderJpaEntity.class, id.value()))
            .map(this::toDomain);
    }

    @Override
    public void save(Order order) {
        OrderJpaEntity entity = toEntity(order);
        entityManager.merge(entity);
    }

    private Order toDomain(OrderJpaEntity entity) {
        // Reconstitute aggregate from persistence
        Order.Builder builder = Order.builder(
            new OrderId(entity.getId()),
            new CustomerId(entity.getCustomerId()));

        for (OrderLineJpaEntity line : entity.getLines()) {
            builder.addLine(
                new ProductId(line.getProductId()),
                line.getQuantity(),
                new Money(line.getUnitPrice(), Currency.getInstance("USD")));
        }

        return builder.build();
    }

    private OrderJpaEntity toEntity(Order order) {
        OrderJpaEntity entity = new OrderJpaEntity();
        entity.setId(order.id().value());
        entity.setCustomerId(order.customerId().value());
        entity.setStatus(order.status().name());
        entity.setTotalAmount(order.totalAmount().amount());
        entity.setCreatedAt(order.createdAt());
        return entity;
    }
}

@Service
public class SpringEventPublisher implements EventPublisher {
    private final ApplicationEventPublisher publisher;

    public SpringEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(DomainEvent event) {
        publisher.publishEvent(event);
    }
}

// ==================== PRESENTATION LAYER ====================

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final CreateOrderHandler createHandler;
    private final SubmitOrderHandler submitHandler;
    private final GetOrderHandler getHandler;

    public OrderController(CreateOrderHandler createHandler,
                           SubmitOrderHandler submitHandler,
                           GetOrderHandler getHandler) {
        this.createHandler = createHandler;
        this.submitHandler = submitHandler;
        this.getHandler = getHandler;
    }

    @PostMapping
    public ResponseEntity<OrderId> create(@RequestBody CreateOrderRequest request) {
        CreateOrderCommand command = mapToCommand(request);
        OrderId orderId = createHandler.handle(command);
        return ResponseEntity.created(
            URI.create("/api/orders/" + orderId)).body(orderId);
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<Void> submit(@PathVariable UUID id) {
        submitHandler.handle(new SubmitOrderCommand(new OrderId(id)));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable UUID id) {
        OrderDto order = getHandler.handle(new GetOrderQuery(new OrderId(id)));
        return ResponseEntity.ok(order);
    }

    private CreateOrderCommand mapToCommand(CreateOrderRequest request) {
        List<OrderLineDto> lines = request.lines().stream()
            .map(l -> new OrderLineDto(
                new ProductId(l.productId()), l.quantity(),
                Money.of(l.unitPrice(), "USD")))
            .toList();
        return new CreateOrderCommand(
            new CustomerId(request.customerId()), lines, request.notes());
    }
}

// DTOs
public record CreateOrderRequest(
    UUID customerId,
    List<OrderLineRequest> lines,
    String notes
) {}

public record OrderLineRequest(
    UUID productId, int quantity, double unitPrice
) {}

// ==================== CONFIGURATION ====================

@Configuration
@EnableTransactionManagement
public class OrderModuleConfig {

    @Bean
    public CreateOrderHandler createOrderHandler(
            OrderRepository orderRepository, EventPublisher eventPublisher) {
        return new CreateOrderHandler(orderRepository, eventPublisher);
    }

    @Bean
    public SubmitOrderHandler submitOrderHandler(
            OrderRepository orderRepository, EventPublisher eventPublisher) {
        return new SubmitOrderHandler(orderRepository, eventPublisher);
    }

    @Bean
    public GetOrderHandler getOrderHandler(OrderRepository orderRepository) {
        return new GetOrderHandler(orderRepository);
    }
}

// ==================== TESTING ====================

@SpringBootTest
class OrderIntegrationTest {
    @Autowired private CreateOrderHandler createHandler;
    @Autowired private SubmitOrderHandler submitHandler;
    @Autowired private GetOrderHandler getHandler;
    @Autowired private OrderRepository orderRepository;

    @Test
    void fullOrderLifecycle() {
        // Create
        CreateOrderCommand createCmd = new CreateOrderCommand(
            new CustomerId(UUID.randomUUID()),
            List.of(new OrderLineDto(
                new ProductId(UUID.randomUUID()), 2, Money.of(29.99, "USD"))),
            "Test order");
        OrderId orderId = createHandler.handle(createCmd);

        // Verify created
        OrderDto order = getHandler.handle(new GetOrderQuery(orderId));
        assertThat(order.status()).isEqualTo(OrderStatus.DRAFT);

        // Submit
        submitHandler.handle(new SubmitOrderCommand(orderId));
        order = getHandler.handle(new GetOrderQuery(orderId));
        assertThat(order.status()).isEqualTo(OrderStatus.SUBMITTED);
    }
}
```

---

## 15. Performance

### 15.1 Pattern Performance Characteristics

| Pattern | Time Complexity | Memory Overhead | Thread Safety Cost |
|---------|----------------|-----------------|-------------------|
| Singleton | O(1) access | Minimal | Low (one-time init) |
| Factory | O(1) creation | Depends on product | Low |
| Builder | O(n) field setting | Temporary objects | None (usually) |
| Observer | O(n) notification | Listener list | Medium (concurrent) |
| Strategy | O(1) delegation | Strategy object | Depends on state |

### 15.2 Micro-Optimizations

```java
// Pre-compiled strategy (avoid per-request creation)
private static final PricingStrategy STANDARD = new StandardPricing();
private static final PricingStrategy BULK = new BulkPricing(10, BigDecimal.TEN);

// Use records for value objects (JVM-optimized)
public record Money(BigDecimal amount, Currency currency) {}

// Avoid boxing in hot paths
public interface PricingStrategy {
    Money calculatePrice(Order order);  // Returns Money, not double
}

// Use ConcurrentHashMap for observer registries
private final Map<Class<?>, List<EventListener<?>>> listeners =
    new ConcurrentHashMap<>();
```

### 15.3 Benchmarking

```java
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@Fork(2)
public class PatternBenchmarks {

    @Benchmark
    public Money standardPricing() {
        return STANDARD_PRICING.calculatePrice(sampleOrder);
    }

    @Benchmark
    public Money bulkPricing() {
        return BULK_PRICING.calculatePrice(sampleOrder);
    }

    @Benchmark
    public Money builderCreation() {
        return Money.builder()
            .amount(new BigDecimal("29.99"))
            .currency(Currency.USD)
            .build();
    }
}
```

---

## 16. Best Practices

### 16.1 Design Principles

| Principle | Application | Example |
|-----------|-------------|---------|
| **Fail Fast** | Validate early, throw immediately | `Objects.requireNonNull()` in constructors |
| **Immutability** | Prefer records and final fields | Value objects as records |
| **Encapsulation** | Hide internal state | Private fields, public behavior |
| **Composition** | Prefer over inheritance | Strategy pattern |
| **Dependency Injection** | Constructor injection | All dependencies in constructor |

### 16.2 Code Organization

```
com.example.orders/
├── domain/                    # Pure domain logic
│   ├── model/
│   │   ├── Order.java        # Aggregate root
│   │   ├── OrderLine.java    # Entity
│   │   ├── OrderId.java      # Value object
│   │   └── OrderStatus.java  # Enum
│   ├── event/
│   │   ├── OrderEvent.java   # Sealed interface
│   │   └── OrderCreatedEvent.java
│   ├── repository/
│   │   └── OrderRepository.java  # Interface only
│   └── service/
│       └── PricingService.java   # Domain service
├── application/               # Use cases
│   ├── command/
│   │   ├── CreateOrderHandler.java
│   │   └── CreateOrderCommand.java
│   └── query/
│       ├── GetOrderHandler.java
│       └── GetOrderQuery.java
├── infrastructure/            # External concerns
│   ├── persistence/
│   │   ├── JpaOrderRepository.java
│   │   └── OrderJpaEntity.java
│   └── messaging/
│       └── SpringEventPublisher.java
└── presentation/              # UI layer
    ├── OrderController.java
    └── OrderDto.java
```

### 16.3 Naming Conventions

| Element | Convention | Example |
|---------|-----------|---------|
| Entities | Noun, PascalCase | `Order`, `Account` |
| Value Objects | Noun, PascalCase | `Money`, `OrderId` |
| Events | Past tense, -ed/-d | `OrderCreated`, `PaymentReceived` |
| Commands | Verb + Noun | `CreateOrder`, `TransferFunds` |
| Handlers | Command/Query + Handler | `CreateOrderHandler` |
| Repositories | Aggregate + Repository | `OrderRepository` |
| Services | Domain + Service | `PricingService` |

### 16.4 Testing Strategy

| Layer | Test Type | Tools | Focus |
|-------|----------|-------|-------|
| Domain | Unit tests | JUnit, AssertJ | Business logic |
| Application | Unit tests | JUnit, Mockito | Use case orchestration |
| Infrastructure | Integration tests | Testcontainers | Database, messaging |
| Presentation | Controller tests | MockMvc | HTTP behavior |

---

## 17. Common Mistakes

### Mistake 1: Anemic Domain Model

```java
// BAD: Logic in service, entity is just data
public class OrderService {
    public void submitOrder(Order order) {
        if (order.getStatus() != OrderStatus.DRAFT) {
            throw new IllegalStateException("Not draft");
        }
        if (order.getLines().isEmpty()) {
            throw new IllegalStateException("No lines");
        }
        order.setStatus(OrderStatus.SUBMITTED);
    }
}

// GOOD: Entity encapsulates behavior
public final class Order {
    public void submit() {
        ensureStatus(OrderStatus.DRAFT);
        if (lines.isEmpty()) {
            throw new EmptyOrderException(id);
        }
        status = OrderStatus.SUBMITTED;
        addEvent(new OrderSubmittedEvent(id, Instant.now()));
    }
}
```

### Mistake 2: Leaking Domain Objects

```java
// BAD: Returning mutable internal state
public class Order {
    private List<OrderLine> lines = new ArrayList<>();

    public List<OrderLine> getLines() {
        return lines;  // Client can modify!
    }
}

// GOOD: Return immutable copy
public final class Order {
    private final List<OrderLine> lines = new ArrayList<>();

    public List<OrderLine> lines() {
        return List.copyOf(lines);  // Defensive copy
    }
}
```

### Mistake 3: God Factory

```java
// BAD: Factory does too much
public class ObjectFactory {
    public Object create(String type) {
        if (type.equals("order")) return createOrder();
        if (type.equals("customer")) return createCustomer();
        if (type.equals("product")) return createProduct();
        // ... 100 more types
    }
}

// GOOD: Focused factories
public class OrderFactory {
    public Order create(CreateOrderCommand cmd) { ... }
}

public class CustomerFactory {
    public Customer create(CreateCustomerCommand cmd) { ... }
}
```

### Mistake 4: Wrong Pattern Choice

```java
// BAD: Singleton for request-scoped data
@Service  // Singleton by default!
public class OrderContext {
    private Order currentOrder;  // Shared across requests!

    public void setOrder(Order order) {
        this.currentOrder = order;  // Thread safety issue
    }
}

// GOOD: Use request scope or pass as parameter
@Service
public class OrderService {
    public OrderDto processOrder(OrderId orderId) {
        Order order = repository.findById(orderId);
        return mapToDto(order);
    }
}
```

### Mistake 5: Over-Engineering

```java
// BAD: Abstract Factory for simple creation
public interface NotificationSenderFactory {
    NotificationSender create(NotificationConfig config);
}

// GOOD: Simple factory method when no polymorphism needed
public final class NotificationSender {
    public static void send(Notification notification) {
        // Direct implementation, no interface needed
        emailService.send(notification.recipient(), notification.body());
    }
}
```

---

## 18. Pitfalls

### Pitfall 1: Singleton Testing Difficulty

```java
// Problem: Singleton state leaks between tests
public final class Cache {
    private static final Cache INSTANCE = new Cache();
    private final Map<String, Object> data = new HashMap<>();

    public void put(String key, Object value) { data.put(key, value); }
    public Object get(String key) { return data.get(key); }
}

// Solution: Make injectable
public interface Cache {
    void put(String key, Object value);
    Object get(String key);
}

public class InMemoryCache implements Cache {
    private final Map<String, Object> data = new ConcurrentHashMap<>();

    @Override
    public void put(String key, Object value) { data.put(key, value); }

    @Override
    public Object get(String key) { return data.get(key); }
}

// In tests: use InMemoryCache directly
// In production: configure as singleton via DI
```

### Pitfall 2: Observer Memory Leaks

```java
// Problem: Listeners not removed, preventing GC
public class EventBus {
    private final List<Listener> listeners = new ArrayList<>();

    public void subscribe(Listener listener) {
        listeners.add(listener);  // Never removed!
    }
}

// Solution: Use WeakReference or explicit unsubscribe
public class EventBus {
    private final List<WeakReference<Listener>> listeners =
        Collections.synchronizedList(new ArrayList<>());

    public void subscribe(Listener listener) {
        listeners.add(new WeakReference<>(listener));
    }

    public void unsubscribe(Listener listener) {
        listeners.removeIf(ref -> ref.get() == null || ref.get() == listener);
    }
}
```

### Pitfall 3: Builder Anti-Object

```java
// Problem: Builder creates invalid states
public class Email.Builder {
    private String from;
    private String to;
    private String subject;
    private String body;

    public Email build() {
        // No validation! Client must remember to set required fields
        return new Email(from, to, subject, body);
    }
}

// Solution: Required fields in constructor
public final class Email {
    private final String from;  // Required
    private final String to;    // Required
    private final String subject;
    private final String body;

    private Email(String from, String to, String subject, String body) {
        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);
        this.subject = subject;
        this.body = body;
    }

    public static Builder builder(String from, String to) {
        return new Builder(from, to);
    }

    public static final class Builder {
        private final String from;
        private final String to;
        private String subject = "";
        private String body = "";

        private Builder(String from, String to) {
            this.from = Objects.requireNonNull(from);
            this.to = Objects.requireNonNull(to);
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Email build() {
            return new Email(from, to, subject, body);
        }
    }
}
```

### Pitfall 4: Strategy State Confusion

```java
// Problem: Strategy with mutable state shared across threads
public class CachedPricing implements PricingStrategy {
    private final Map<String, Money> cache = new HashMap<>();  // Not thread-safe!

    @Override
    public Money calculatePrice(List<OrderLine> lines) {
        String key = lines.toString();
        return cache.computeIfAbsent(key, k -> computePrice(lines));
    }
}

// Solution: Stateless strategy or thread-safe cache
public class CachedPricing implements PricingStrategy {
    private final ConcurrentHashMap<String, Money> cache =
        new ConcurrentHashMap<>();

    @Override
    public Money calculatePrice(List<OrderLine> lines) {
        String key = lines.toString();
        return cache.computeIfAbsent(key, this::computePrice);
    }
}
```

### Pitfall 5: Factory Creates Tight Coupling

```java
// Problem: Factory knows about all implementations
public class ServiceFactory {
    public static Service create(String type) {
        return switch (type) {
            case "email" -> new EmailService();
            case "sms" -> new SmsService();
            case "push" -> new PushService();
            // Adding new type requires modifying factory
        };
    }
}

// Solution: Use DI container or registry
public class ServiceRegistry {
    private final Map<String, Supplier<Service>> factories = new ConcurrentHashMap<>();

    public void register(String type, Supplier<Service> factory) {
        factories.put(type, factory);
    }

    public Service create(String type) {
        Supplier<Service> factory = factories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Unknown type: " + type);
        }
        return factory.get();
    }
}
```

---

## 19. Debugging Tips

### 19.1 Systematic Approach

```
1. Reproduce the issue
   └─ Create minimal test case

2. Identify the layer
   └─ Presentation? Application? Domain? Infrastructure?

3. Check state
   └─ Is the object in expected state?
   └─ Are fields set correctly?

4. Check behavior
   └─ Is the right method called?
   └─ Are preconditions met?

5. Check interactions
   └─ Is the right observer notified?
   └─ Is the strategy selected correctly?
```

### 19.2 Logging Strategy

```java
// Domain events (structured logging)
public class OrderEventListener implements EventListener<OrderEvent> {
    private static final Logger log = LoggerFactory.getLogger(
        OrderEventListener.class);

    @Override
    public void handle(OrderEvent event) {
        log.info("Processing order event: type={}, orderId={}, timestamp={}",
            event.getClass().getSimpleName(),
            event.orderId(),
            event.occurredAt());
    }
}

// Application layer (operation logging)
@Service
public class CreateOrderHandler {
    private static final Logger log = LoggerFactory.getLogger(
        CreateOrderHandler.class);

    @Transactional
    public OrderId handle(CreateOrderCommand command) {
        log.info("Creating order for customer: {}", command.customerId());
        OrderId orderId = createOrder(command);
        log.info("Order created: {}", orderId);
        return orderId;
    }
}
```

### 19.3 Debugging Tools

| Tool | Purpose | When to Use |
|------|---------|------------|
| IDE Debugger | Step through code | Complex logic issues |
| JProfiler/YourKit | Memory/CPU profiling | Performance issues |
| Arthas | Runtime diagnostics | Production issues |
| VisualVM | JVM monitoring | Memory leaks |
| Log correlation | Request tracing | Distributed systems |

### 19.4 Common Debug Scenarios

```java
// Scenario: Observer not notified
// Debug steps:
// 1. Verify listener is registered
// 2. Verify event type matches subscription
// 3. Verify publish is called with correct type
// 4. Check for exceptions in listener

// Scenario: Strategy not applied
// Debug steps:
// 1. Verify strategy is injected
// 2. Check strategy selection logic
// 3. Verify method is actually called
// 4. Check for inheritance issues

// Scenario: Builder produces wrong state
// Debug steps:
// 1. Check builder field values at build time
// 2. Verify validation in build()
// 3. Check for thread safety issues
// 4. Verify constructor assignment
```

---

## 20. Comparison Table

### Design Patterns Comparison

| Pattern | Intent | Complexity | Use When | Avoid When |
|---------|--------|-----------|----------|------------|
| **Singleton** | Single instance | Low | Configuration, caches | Testing, distributed systems |
| **Factory** | Delegate creation | Low | Complex creation logic | Simple constructors |
| **Builder** | Step-by-step construction | Medium | Many optional parameters | Few parameters (<4) |
| **Observer** | Event notification | Medium | Decoupled communication | Simple linear flow |
| **Strategy** | Swappable algorithms | Medium | Runtime behavior variation | Static behavior |

### Architecture Comparison

| Architecture | Coupling | Testability | Scalability | Complexity |
|-------------|----------|-------------|-------------|------------|
| Layered | Medium | Medium | Medium | Low |
| Hexagonal | Low | High | High | Medium |
| Event-Driven | Very Low | High | Very High | High |
| CQRS | Low | High | High | Medium |

### DDD Building Blocks

| Concept | Mutable? | Identity? | Persistence? | Example |
|---------|----------|-----------|-------------|---------|
| Entity | Yes | Yes | Via repository | `Order`, `Account` |
| Value Object | No | No | Embedded | `Money`, `Address` |
| Aggregate | Boundary | Root identity | Via repository | `Order` (with lines) |
| Domain Event | No | Yes | Event store | `OrderPlaced` |
| Domain Service | Stateless | No | No | `TransferService` |

---

## 21. Decision Tree

### Pattern Selection

```
Do you need a single instance?
├─ Yes → Singleton (or scoped singleton via DI)
└─ No ↓

Is object creation complex?
├─ Yes, many parameters → Builder
├─ Yes, type-dependent → Factory
└─ No ↓

Do you need to notify multiple objects?
├─ Yes → Observer
└─ No ↓

Do you need runtime behavior variation?
├─ Yes → Strategy
└─ No ↓

Is this cross-cutting concern?
├─ Yes → AOP (Decorator/Proxy)
└─ No → Simple method/class
```

### Architecture Selection

```
Team size?
├─ < 5 developers → Layered architecture
├─ 5-20 developers → Hexagonal architecture
└─ > 20 developers → Microservices + Event-driven

Domain complexity?
├─ Simple → CRUD-based
├─ Medium → DDD Lite (entities + value objects)
└─ Complex → Full DDD (aggregates, domain events, bounded contexts)

Performance requirements?
├─ Standard → Synchronous processing
├─ High throughput → Async + Message queues
└─ Real-time → Event-driven + WebSockets
```

### Repository Pattern Decision

```
Need persistence abstraction?
├─ Yes → Repository pattern
│   ├─ Simple CRUD → Spring Data JPA
│   ├─ Complex queries → Custom repository
│   └─ Multiple data sources → Separate repositories per source
└─ No → Direct data access (for simple apps)
```

---

## 22. Interview Questions

### Fundamentals

1. **What is the difference between Singleton and Factory patterns?**
   - Singleton ensures one instance; Factory delegates object creation.

2. **When would you use Builder over Constructor?**
   - When there are more than 4 parameters, especially with optional ones.

3. **Explain the Observer pattern with a real-world example.**
   - Order placed → notification service, inventory service, analytics service.

4. **What problem does Strategy pattern solve?**
   - Allows swapping algorithms at runtime without changing the context.

### Architecture

5. **What is the difference between Layered and Hexagonal architecture?**
   - Hexagonal inverts dependencies: domain has no external dependencies.

6. **Explain DDD Aggregate.**
   - Cluster of entities with consistency boundary; root entity enforces invariants.

7. **What is CQRS? When would you use it?**
   - Command Query Responsibility Segregation; separate read/write models for scalability.

8. **How do you prevent anemic domain model?**
   - Put behavior in entities, not services; entities should enforce invariants.

### Design

9. **How do you make Singleton testable?**
   - Use DI; inject interface, not concrete singleton.

10. **Explain Open/Closed Principle with an example.**
    - Strategy pattern: add new pricing without modifying existing code.

11. **When would you NOT use design patterns?**
    - Over-engineering; when simpler solution works; YAGNI principle.

12. **How do you handle cross-cutting concerns?**
    - AOP (Aspect-Oriented Programming); decorators; interceptors.

### Java 21 Specific

13. **How do sealed classes help in DDD?**
    - Limit domain event types; compiler enforces exhaustiveness.

14. **Explain pattern matching for switch in factory pattern.**
    - Type-safe pattern matching eliminates explicit casts.

15. **What are virtual threads and how do they affect enterprise design?**
    - Millions of concurrent threads; simplify async code; no callback hell.

### Scenario-Based

16. **Design a payment processing system with multiple providers.**
    - Strategy pattern for providers; Factory for selection; Observer for notifications.

17. **How would you implement audit logging without modifying domain code?**
    - Observer pattern; domain events; event listener for audit.

18. **Design an order system with complex state transitions.**
    - State pattern or enum-based transitions; aggregate enforces valid transitions.

---

## 23. Exercises

### Exercise 1: Singleton Implementation
Implement a thread-safe Singleton for a database connection pool using Java 21 features.

```java
// Your implementation here
public final class ConnectionPool {
    // TODO: Implement thread-safe singleton
    // TODO: Add connection acquisition and release
    // TODO: Handle pool exhaustion
}
```

### Exercise 2: Factory Pattern
Create a factory for different notification types (Email, SMS, Push) using sealed classes.

```java
public sealed interface Notification permits Email, Sms, Push {}
// TODO: Implement factory method
// TODO: Add notification sending logic
```

### Exercise 3: Builder Pattern
Implement a Builder for a complex `Report` object with 8+ fields, some required and some optional.

```java
public final class Report {
    // TODO: Implement builder with required/optional fields
    // TODO: Add validation in build()
}
```

### Exercise 4: Observer Pattern
Create an event system for a shopping cart where adding/removing items triggers multiple listeners.

```java
// TODO: Implement CartEvent interface
// TODO: Create listeners: InventoryListener, PricingListener, AnalyticsListener
// TODO: Implement EventBus
```

### Exercise 5: Strategy Pattern
Implement a discount calculation system with multiple strategies (percentage, fixed, buy-one-get-one).

```java
public interface DiscountStrategy {
    Money calculateDiscount(Order order);
}
// TODO: Implement three discount strategies
// TODO: Create strategy selector
```

### Exercise 6: DDD Refactoring
Refactor an anemic domain model into a rich domain model.

```java
// BEFORE (anemic)
public class OrderService {
    public void submit(Order order) {
        order.setStatus(OrderStatus.SUBMITTED);
    }
}

// TODO: Move behavior to Order entity
```

### Exercise 7: Repository Pattern
Implement a repository interface and in-memory implementation for an `Employee` aggregate.

```java
public interface EmployeeRepository {
    Optional<Employee> findById(EmployeeId id);
    List<Employee> findByDepartment(Department dept);
    void save(Employee employee);
}
// TODO: Implement InMemoryEmployeeRepository
```

### Exercise 8: Package Restructuring
Given a flat package structure, reorganize into domain-based packages.

```
// BEFORE
com.example/
├── Order.java
├── Customer.java
├── OrderService.java
├── CustomerService.java
├── OrderRepository.java
└── CustomerRepository.java

// TODO: Reorganize into domain-based structure
```

---

## 24. Assignments

### Assignment 1: E-Commerce Order System (Difficulty: Medium)

Build an order management system with:
- Order aggregate with lines
- State transitions (Draft → Submitted → Confirmed → Shipped → Delivered)
- Builder for Order creation
- Observer for order events
- Repository interface

**Requirements:**
1. Value objects for OrderId, CustomerId, Money
2. Domain events for each state transition
3. Repository with CRUD operations
4. At least 3 event listeners

### Assignment 2: Multi-Tenant SaaS Platform (Difficulty: Hard)

Design a multi-tenant system with:
- Tenant-scoped Singleton for configuration
- Factory for tenant-specific services
- Strategy for different pricing tiers
- CQRS for read/write separation

**Requirements:**
1. Tenant isolation
2. Pluggable authentication strategies
3. Event-driven notifications
4. Performance monitoring

### Assignment 3: Plugin Architecture (Difficulty: Hard)

Create a plugin system with:
- Dynamic plugin loading
- Strategy pattern for plugin behavior
- Observer for plugin events
- Registry for plugin management

**Requirements:**
1. Plugin interface definition
2. Plugin loader with hot-reload
3. Event system for plugin lifecycle
4. Configuration management

### Assignment 4: Domain-Driven Design Application (Difficulty: Medium)

Implement a complete DDD application for a library management system:
- Aggregates: Book, Member, Loan
- Value Objects: ISBN, Email, Money
- Domain Events: BookBorrowed, BookReturned, FineCharged
- Repository pattern

**Requirements:**
1. Rich domain model (no anemic entities)
2. Aggregate boundaries with consistency rules
3. At least 5 domain events
4. Unit tests for domain logic

---

## 25. Mini Project: Task Management System

### Overview

Build a task management system demonstrating all major design patterns.

### Requirements

```
Functional:
├── Create, update, delete tasks
├── Assign tasks to users
├── Change task status (Todo → In Progress → Done)
├── Filter tasks by status, assignee, priority
└── Activity log for all changes

Non-Functional:
├── Thread-safe for concurrent access
├── Extensible for new task types
├── Event-driven notifications
├── Proper error handling
└── 80%+ test coverage
```

### Architecture

```
com.tasks/
├── domain/
│   ├── model/
│   │   ├── Task.java          # Aggregate root
│   │   ├── TaskId.java        # Value object
│   │   ├── UserId.java        # Value object
│   │   ├── Priority.java      # Enum
│   │   └── TaskStatus.java    # Enum
│   ├── event/
│   │   ├── TaskEvent.java     # Sealed interface
│   │   ├── TaskCreatedEvent.java
│   │   ├── TaskAssignedEvent.java
│   │   └── TaskCompletedEvent.java
│   ├── repository/
│   │   └── TaskRepository.java
│   └── service/
│       └── TaskDomainService.java
├── application/
│   ├── command/
│   │   ├── CreateTaskHandler.java
│   │   ├── AssignTaskHandler.java
│   │   └── CompleteTaskHandler.java
│   └── query/
│       ├── GetTaskHandler.java
│       └── ListTasksHandler.java
├── infrastructure/
│   ├── persistence/
│   │   └── InMemoryTaskRepository.java
│   └── event/
│       └── InMemoryEventPublisher.java
└── presentation/
    └── TaskCLI.java
```

### Implementation Guide

**Step 1: Domain Layer**
```java
// Task aggregate
public final class Task {
    private final TaskId id;
    private final String title;
    private final String description;
    private UserId assignee;
    private Priority priority;
    private TaskStatus status;
    private final List<TaskEvent> events = new ArrayList<>();

    // Builder for creation
    public static Builder builder(TaskId id, String title) { ... }

    // Behavior
    public void assign(UserId userId) { ... }
    public void start() { ... }
    public void complete() { ... }
    public void changePriority(Priority priority) { ... }

    // Event management
    public List<TaskEvent> pullEvents() { ... }
}
```

**Step 2: Application Layer**
```java
@Service
public class CreateTaskHandler {
    private final TaskRepository repository;
    private final EventPublisher eventPublisher;

    @Transactional
    public TaskId handle(CreateTaskCommand command) {
        Task task = Task.builder(TaskId.generate(), command.title())
            .description(command.description())
            .priority(command.priority())
            .build();
        repository.save(task);
        task.pullEvents().forEach(eventPublisher::publish);
        return task.id();
    }
}
```

**Step 3: Infrastructure Layer**
```java
public class InMemoryTaskRepository implements TaskRepository {
    private final Map<TaskId, Task> store = new ConcurrentHashMap<>();

    @Override
    public Optional<Task> findById(TaskId id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void save(Task task) {
        store.put(task.id(), task);
    }
}
```

**Step 4: Presentation Layer**
```java
public class TaskCLI {
    private final CreateTaskHandler createHandler;
    private final GetTaskHandler getHandler;
    // ... other handlers

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            processCommand(input);
        }
    }

    private void processCommand(String input) {
        String[] parts = input.split(" ");
        switch (parts[0]) {
            case "create" -> handleCreate(parts);
            case "assign" -> handleAssign(parts);
            case "complete" -> handleComplete(parts);
            case "list" -> handleList(parts);
            case "exit" -> System.exit(0);
            default -> System.out.println("Unknown command");
        }
    }
}
```

### Deliverables

1. Complete source code
2. Unit tests for domain logic
3. Integration tests for application layer
4. README with setup instructions
5. Design decisions document

---

## 26. Summary

Enterprise OOP design provides the foundation for building scalable, maintainable, and robust software systems. Key takeaways:

### Core Patterns

| Pattern | Key Benefit | When to Use |
|---------|------------|-------------|
| **Singleton** | Single instance control | Configuration, caches |
| **Factory** | Creation encapsulation | Complex object creation |
| **Builder** | Step-by-step construction | Many optional parameters |
| **Observer** | Loose coupling | Event-driven communication |
| **Strategy** | Algorithm flexibility | Runtime behavior variation |

### Architecture Principles

1. **SOLID** — Foundation for all design decisions
2. **DDD** — Align code with business domains
3. **Layering** — Separate concerns clearly
4. **Dependency Injection** — Invert control for testability

### Best Practices

- Prefer immutability (records, final fields)
- Use sealed classes for type safety
- Apply fail-fast validation
- Keep domain logic in entities
- Test at appropriate layers

### Common Anti-Patterns to Avoid

- Anemic domain model (logic in services)
- God objects (doing everything)
- Tight coupling (direct dependencies)
- Over-engineering (unnecessary complexity)

### Java 21 Features to Leverage

- **Records** — Immutable value objects
- **Sealed classes** — Restricted type hierarchies
- **Pattern matching** — Type-safe switch expressions
- **Virtual threads** — Simplified concurrency
- **Text blocks** — SQL and configuration

---

## 27. References

### Books

1. **"Domain-Driven Design"** by Eric Evans — The foundational DDD book
2. **"Implementation Patterns"** by Kent Beck — Java-specific patterns
3. **"Effective Java"** by Joshua Bloch — Java best practices
4. **"Clean Architecture"** by Robert C. Martin — Architecture principles
5. **"Head First Design Patterns"** by Eric Freeman — Beginner-friendly patterns

### Online Resources

1. [Oracle Design Patterns Documentation](https://docs.oracle.com/en/java/)
2. [Baeldung Design Patterns](https://www.baeldung.com/java-design-patterns)
3. [Refactoring Guru](https://refactoring.guru/design-patterns)
4. [Martin Fowler's Blog](https://martinfowler.com)

### Java 21 Features

1. [Records (JEP 395)](https://openjdk.org/jeps/395)
2. [Sealed Classes (JEP 409)](https://openjdk.org/jeps/409)
3. [Pattern Matching for switch (JEP 441)](https://openjdk.org/jeps/441)
4. [Virtual Threads (JEP 444)](https://openjdk.org/jeps/444)

### Tools

1. [JUnit 5](https://junit.org/junit5/) — Testing framework
2. [Mockito](https://site.mockito.org/) — Mocking library
3. [AssertJ](https://assertj.github.io/doc/) — Fluent assertions
4. [JMH](https://openjdk.org/projects/code-tools/jmh/) — Microbenchmarks

---

*Last updated: Java 21 | Google Java Style*
