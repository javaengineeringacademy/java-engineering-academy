# OOP Best Practices

## 1. Introduction

Object-Oriented Programming (OOP) best practices are proven principles and patterns that lead to maintainable, testable, and scalable Java code. This guide covers class design, naming conventions, immutability, encapsulation, composition over inheritance, error handling, and testing strategies. Following these practices helps teams write consistent, production-quality code.

Java 21 provides modern language features like records, sealed classes, pattern matching, and virtual threads that reinforce good OOP design. This document covers both timeless principles and modern Java idioms.

## 2. Learning Objectives

- Apply SOLID principles to Java class design
- Design immutable and thread-safe classes
- Choose composition over inheritance appropriately
- Follow Google Java Style naming conventions
- Implement proper encapsulation with access modifiers
- Handle errors using custom exceptions and structured approaches
- Write testable OOP code with proper separation of concerns
- Use modern Java 21 features (records, sealed classes) effectively

## 3. Prerequisites

- Basic Java syntax and OOP concepts (classes, interfaces, inheritance)
- Familiarity with Java collections framework
- Understanding of access modifiers (public, private, protected, package-private)
- Basic knowledge of unit testing (JUnit 5)
- IDE experience (IntelliJ IDEA or Eclipse)

## 4. Why This Concept Exists

Without established best practices, teams produce inconsistent, fragile, and hard-to-maintain code. Common problems include:

- **Tight coupling**: Changes in one class break many others
- **Fragile base class problem**: Inheritance hierarchies that break when modified
- **God classes**: Classes that do too much and are impossible to test
- **Mutable shared state**: Thread-safety bugs and race conditions
- **Inconsistent naming**: Developers spending time deciphering code instead of reading it

Best practices address these issues by providing a shared vocabulary, proven patterns, and standards that make code easier to understand, modify, and debug.

## 5. Problem Statement

Consider an e-commerce system where a `ShoppingCart` class violates multiple OOP principles:

```java
// BAD: Multiple violations
public class ShoppingCart {
    public List<Item> items; // No encapsulation
    private User user;

    public ShoppingCart(User user) {
        this.user = user;
        this.items = new ArrayList<>();
    }

    // God method: does too much
    public void processEverything() {
        calculateTotal();
        applyDiscount();
        processPayment();
        sendEmail();
        updateInventory();
    }

    // Mutable state without synchronization
    public void addItem(Item item) {
        items.add(item); // Not thread-safe
    }

    public double calculateTotal() {
        double total = 0;
        for (Item item : items) {
            total += item.price * item.quantity;
        }
        return total;
    }
}
```

This code has no encapsulation, violates Single Responsibility, has no thread safety, and is difficult to test. The following sections show how to fix these issues.

## 6. Theory

### SOLID Principles

| Principle | Definition | Example |
|-----------|------------|---------|
| **Single Responsibility** | One reason to change | `UserService` only handles user operations |
| **Open/Closed** | Open for extension, closed for modification | Use interfaces and abstract classes |
| **Liskov Substitution** | Subtypes must be substitutable | `Dog extends Animal` must honor `Animal`'s contract |
| **Interface Segregation** | Many small interfaces | `Readable`, `Writable` instead of `IOInterface` |
| **Dependency Inversion** | Depend on abstractions | Depend on `Repository` interface, not `MySQLRepository` |

### Composition vs Inheritance

| Aspect | Composition | Inheritance |
|--------|-------------|-------------|
| **Relationship** | "has-a" | "is-a" |
| **Flexibility** | Runtime (inject dependencies) | Compile-time (class hierarchy) |
| **Coupling** | Loose (program to interfaces) | Tight (depends on base class) |
| **Testability** | Easy (mock dependencies) | Hard (depends on parent) |
| **Reuse** | Delegate to composed objects | Override parent methods |

### Immutability Theory

An immutable object's state cannot change after construction. Benefits:
- Thread-safe without synchronization
- Safe for caching and hashing
- No defensive copies needed
- Referential transparency

```java
// Immutable class requirements:
// 1. Class is final (or methods are final)
// 2. All fields are final
// 3. No setter methods
// 4. Constructor deep copies mutable objects
// 5. Return defensive copies from getters
```

## 7. Internal Working

### How Encapsulation Works at the JVM Level

When a field is `private`, the compiler generates synthetic accessor methods. The JVM inlines these at runtime, so there is no performance penalty for encapsulation:

```java
public class Person {
    private String name; // Private field

    public String getName() {
        return name; // Compiler generates accessor
    }
}

// JVM inlines this to direct field access at runtime
```

### How Records Work (Java 16+)

Records are implicitly immutable and generate:
- `private final` fields
- Public accessor methods (not getters)
- `equals()`, `hashCode()`, `toString()`
- A canonical constructor

```java
public record Point(int x, int y) {}
// Compiles to approximately:
// private final int x;
// private final int y;
// public int x() { return x; }
// public int y() { return y; }
// plus equals, hashCode, toString
```

### How Sealed Classes Work (Java 17+)

Sealed classes restrict which classes can extend them. The JVM verifies this at class loading time:

```java
public sealed interface Shape
    permits Circle, Rectangle, Triangle {
}

public final class Circle implements Shape { }
public final class Rectangle implements Shape { }
public non-sealed class Triangle implements Shape { }
```

The compiler and JVM enforce that only permitted classes can implement the sealed interface, enabling exhaustive pattern matching.

## 8. JVM Perspective

### Method Dispatch and Virtual Methods

The JVM uses virtual method dispatch for instance methods. When `animal.makeSound()` is called, the JVM looks up the actual class at runtime:

```
animal.makeSound()
    │
    ▼
JVM checks: what is the actual class of animal?
    │
    ├── Dog → Dog.makeSound() (bark)
    ├── Cat → Cat.makeSound() (meow)
    └── Animal → Animal.makeSound() (default)
```

Static methods are dispatched at compile time (no virtual dispatch), making them slightly faster. However, the performance difference is negligible in most applications.

### Interface Default Methods (Java 8+)

Default methods in interfaces are dispatched using the `invokeinterface` bytecode instruction. The JVM caches interface method lookups for performance.

### Record Performance

Records have no performance penalty compared to manually written POJOs. The JVM treats them as regular classes with compiler-generated methods. The `==` reference comparison is often sufficient for value objects that are cached or interned.

## 9. Memory Representation

### Object Memory Layout

```
Person Object (16 bytes header + fields)
├── Object Header (12 bytes)
│   ├── Mark Word (8 bytes)
│   └── Class Pointer (4 bytes, compressed)
├── name reference (4 bytes, compressed)
├── age (4 bytes, int)
└── Padding (4 bytes, alignment to 8)
Total: 24 bytes
```

### Record Memory Layout

```
Point Record (similar to manual POJO)
├── Object Header (12 bytes)
├── x (4 bytes, int)
├── y (4 bytes, int)
└── Padding (4 bytes, alignment to 8)
Total: 24 bytes
```

### Impact of Design Choices on Memory

| Choice | Memory Impact |
|--------|---------------|
| `String` vs `enum` for fixed values | Enum: 16 bytes vs String: 40+ bytes |
| `int` vs `Integer` | 4 bytes vs 16 bytes (object overhead) |
| `List` vs `record` with array | List: 48+ bytes vs record: 24 bytes |
| `Map<String, Object>` vs typed record | Map: 100+ bytes per entry vs typed fields |

## 10. Architecture Diagram

### Layered Architecture with OOP Principles

```
┌─────────────────────────────────────────────────────────┐
│                  Presentation Layer                      │
│            (Controllers, DTOs, Validation)               │
│                  Depends on: Service                     │
├─────────────────────────────────────────────────────────┤
│                    Service Layer                         │
│         (Business Logic, Orchestration)                  │
│              Depends on: Repository                      │
├─────────────────────────────────────────────────────────┤
│                  Domain Layer                            │
│        (Entities, Value Objects, Domain Events)          │
│              Depends on: Nothing                         │
├─────────────────────────────────────────────────────────┤
│                Infrastructure Layer                      │
│     (Repository Impl, External APIs, Config)             │
│              Depends on: Domain, External                │
└─────────────────────────────────────────────────────────┘

Dependency Direction: Outer → Inner
Domain Layer: No dependencies (pure OOP)
```

### Dependency Inversion Example

```
┌──────────────────┐        ┌──────────────────┐
│   UserService    │───────►│  UserRepository   │ ← Interface
│  (Service Layer) │        │   (Abstraction)   │
└──────────────────┘        └────────┬─────────┘
                                     │
                              ┌──────┴──────┐
                              │             │
                    ┌─────────▼──┐  ┌───────▼──────┐
                    │  MySQLUser │  │ MongoUser     │
                    │ Repository │  │ Repository    │
                    └────────────┘  └──────────────┘
```

## 11. Flow Diagram

### Class Design Decision Flow

```
Need a new class?
    │
    ▼
Is it a value holder with no behavior?
    ├── Yes → Use a record (record User(String name, int age))
    └── No
        │
        ▼
    Is it a collection of constants?
        ├── Yes → Use an enum (enum Status { ACTIVE, INACTIVE })
        └── No
            │
            ▼
        Does it have state and behavior?
            ├── Yes → Use a regular class
            │         ├── Make fields private
            │         ├── Make class final (or document for inheritance)
            │         └── Minimize mutable state
            └── No → Use an interface (for behavior contracts)
```

### Composition vs Inheritance Decision

```
Need code reuse?
    │
    ▼
Is it a true "is-a" relationship?
├── Yes → Is the parent class designed for inheritance?
│         ├── Yes → Consider inheritance (use @Override)
│         └── No → Use composition + delegation
└── No → Use composition
         ├── Inject dependencies via constructor
         ├── Program to interfaces
         └── Delegate behavior to composed objects
```

## 12. Syntax

### Records (Java 16+)

```java
// Simple record
public record Point(int x, int y) {}

// Record with validation
public record Money(BigDecimal amount, Currency currency) {
    public Money {
        Objects.requireNonNull(amount, "amount must not be null");
        Objects.requireNonNull(currency, "currency must not be null");
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("amount must be non-negative");
        }
    }

    // Custom methods
    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }
}

// Record implementing interface
public record Email(String value) implements CharSequence {
    @Override public int length() { return value.length(); }
    @Override public char charAt(int index) { return value.charAt(index); }
    @Override public CharSequence subSequence(int start, int end) {
        return value.subSequence(start, end);
    }
}
```

### Sealed Classes (Java 17+)

```java
// Sealed interface
public sealed interface Payment
    permits CreditCardPayment, BankTransferPayment, CryptoPayment {
}

// Permitted classes must be final, sealed, or non-sealed
public final record CreditCardPayment(
    String cardNumber, String expiry) implements Payment {}

public final record BankTransferPayment(
    String accountNumber, String routingNumber) implements Payment {}

public non-sealed class CryptoPayment implements Payment {
    private final String walletAddress;
    // ...
}
```

### Pattern Matching (Java 21)

```java
// Switch with pattern matching
public static String describe(Payment payment) {
    return switch (payment) {
        case CreditCardPayment cc -> "Credit card: " + cc.cardNumber();
        case BankTransferPayment bt -> "Bank transfer: " + bt.accountNumber();
        case CryptoPayment cp -> "Crypto: " + cp.getWalletAddress();
    };
}

// Pattern matching with guards
public static double calculateFee(Payment payment) {
    return switch (payment) {
        case CreditCardPayment cc when cc.cardNumber().startsWith("4") -> 0.02;
        case CreditCardPayment cc -> 0.03;
        case BankTransferPayment bt -> 0.01;
        case CryptoPayment cp -> 0.005;
    };
}
```

### Immutability with Final and Records

```java
// Immutable class (manual)
public final class ImmutablePerson {
    private final String name;
    private final List<String> addresses;

    public ImmutablePerson(String name, List<String> addresses) {
        this.name = name;
        this.addresses = List.copyOf(addresses); // Defensive copy
    }

    public String name() { return name; }
    public List<String> addresses() { return addresses; }
}

// Equivalent record (recommended)
public record ImmutablePerson(String name, List<String> addresses) {
    public ImmutablePerson {
        addresses = List.copyOf(addresses); // Defensive copy
    }
}
```

## 13. Easy Example

### Basic Class Design

```java
public final class BankAccount {
    private final String accountId;
    private final String ownerName;
    private BigDecimal balance;

    public BankAccount(String accountId, String ownerName, BigDecimal initialBalance) {
        this.accountId = Objects.requireNonNull(accountId);
        this.ownerName = Objects.requireNonNull(ownerName);
        this.balance = Objects.requireNonNull(initialBalance);
        if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
    }

    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException(balance, amount);
        }
        this.balance = this.balance.subtract(amount);
    }

    public String accountId() { return accountId; }
    public String ownerName() { return ownerName; }
    public BigDecimal balance() { return balance; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BankAccount that)) return false;
        return accountId.equals(that.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }

    @Override
    public String toString() {
        return "BankAccount{id='%s', owner='%s', balance=%s}".formatted(
            accountId, ownerName, balance);
    }
}
```

**Key practices demonstrated:**
- `final` class prevents subclassing
- `final` fields for immutability
- `Objects.requireNonNull` for null validation
- Encapsulation (private fields, public methods)
- Proper `equals()`, `hashCode()`, `toString()`

## 14. Medium Example

### Composition-Based Design

```java
// Interface (abstraction)
public interface NotificationService {
    void send(String recipient, String message);
}

// Implementation 1
public class EmailNotificationService implements NotificationService {
    private final EmailSender emailSender;

    public EmailNotificationService(EmailSender emailSender) {
        this.emailSender = Objects.requireNonNull(emailSender);
    }

    @Override
    public void send(String recipient, String message) {
        emailSender.send(recipient, "Notification", message);
    }
}

// Implementation 2
public class SmsNotificationService implements NotificationService {
    private final SmsGateway smsGateway;

    public SmsNotificationService(SmsGateway smsGateway) {
        this.smsGateway = Objects.requireNonNull(smsGateway);
    }

    @Override
    public void send(String recipient, String message) {
        smsGateway.send(recipient, message);
    }
}

// Composite service using composition
public class MultiChannelNotificationService implements NotificationService {
    private final List<NotificationService> services;

    public MultiChannelNotificationService(List<NotificationService> services) {
        this.services = List.copyOf(services); // Immutable copy
    }

    @Override
    public void send(String recipient, String message) {
        services.forEach(service -> service.send(recipient, message));
    }
}

// Usage
NotificationService service = new MultiChannelNotificationService(List.of(
    new EmailNotificationService(emailSender),
    new SmsNotificationService(smsGateway)
));
service.send("user@example.com", "Your order is ready!");
```

**Key practices demonstrated:**
- Program to interfaces (`NotificationService`)
- Composition over inheritance (MultiChannel)
- Constructor injection for dependencies
- Immutability (`List.copyOf`)
- Single Responsibility per class

## 15. Hard Example

### Domain-Driven Design with Value Objects

```java
// Value Object (immutable, identity-less)
public record Email(String value) {
    public Email {
        Objects.requireNonNull(value, "Email must not be null");
        if (!value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email: " + value);
        }
    }

    public String domain() {
        return value.substring(value.indexOf('@') + 1);
    }
}

// Value Object
public record Money(BigDecimal amount, Currency currency) {
    public Money {
        Objects.requireNonNull(amount);
        Objects.requireNonNull(currency);
        if (amount.scale() > currency.getDefaultFractionDigits()) {
            throw new IllegalArgumentException(
                "Too many decimal places for " + currency);
        }
    }

    public Money add(Money other) {
        assertSameCurrency(other);
        return new Money(amount.add(other.amount), currency);
    }

    public Money multiply(int factor) {
        return new Money(amount.multiply(BigDecimal.valueOf(factor)), currency);
    }

    private void assertSameCurrency(Money other) {
        if (!currency.equals(other.currency)) {
            throw new CurrencyMismatchException(currency, other.currency);
        }
    }
}

// Entity (has identity, mutable state)
public final class Order {
    private final OrderId id;
    private final List<OrderLine> lines;
    private OrderStatus status;

    public Order(OrderId id, List<OrderLine> lines) {
        this.id = Objects.requireNonNull(id);
        this.lines = new ArrayList<>(lines); // Defensive copy
        this.status = OrderStatus.PENDING;
    }

    public void addLine(OrderLine line) {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException(
                "Cannot modify order in status: " + status);
        }
        lines.add(line);
    }

    public Money totalAmount() {
        return lines.stream()
            .map(OrderLine::lineTotal)
            .reduce(Money::add)
            .orElse(new Money(BigDecimal.ZERO, Currency.USD));
    }

    public void confirm() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Order is not pending");
        }
        if (lines.isEmpty()) {
            throw new IllegalStateException("Cannot confirm empty order");
        }
        this.status = OrderStatus.CONFIRMED;
    }

    // Getters (no setters except for specific state transitions)
    public OrderId id() { return id; }
    public List<OrderLine> lines() { return List.copyOf(lines); }
    public OrderStatus status() { return status; }
}

// Value Object for OrderLine
public record OrderLine(Product product, int quantity, Money unitPrice) {
    public OrderLine {
        Objects.requireNonNull(product);
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        Objects.requireNonNull(unitPrice);
    }

    public Money lineTotal() {
        return unitPrice.multiply(quantity);
    }
}
```

**Key practices demonstrated:**
- Value objects for domain concepts (Email, Money, OrderLine)
- Entities for identity-based objects (Order)
- Encapsulated state transitions (order status)
- Defensive copying
- Domain validation in constructors
- Records for simple value objects

## 16. Enterprise Example

### E-Commerce Service Layer

```java
// Domain Event
public record OrderPlacedEvent(
    String orderId,
    String customerId,
    Money totalAmount,
    Instant timestamp
) {}

// Service Interface
public interface OrderService {
    Order placeOrder(String customerId, List<OrderItemRequest> items);
    void cancelOrder(String orderId);
    Optional<Order> getOrder(String orderId);
}

// Service Implementation
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;
    private final PaymentService paymentService;
    private final EventPublisher eventPublisher;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            InventoryService inventoryService,
            PaymentService paymentService,
            EventPublisher eventPublisher) {
        this.orderRepository = Objects.requireNonNull(orderRepository);
        this.inventoryService = Objects.requireNonNull(inventoryService);
        this.paymentService = Objects.requireNonNull(paymentService);
        this.eventPublisher = Objects.requireNonNull(eventPublisher);
    }

    @Override
    public Order placeOrder(String customerId, List<OrderItemRequest> items) {
        Objects.requireNonNull(customerId, "Customer ID required");
        Objects.requireNonNull(items, "Items required");
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }

        // Validate inventory
        items.forEach(item ->
            inventoryService.reserve(item.productId(), item.quantity()));

        // Create order
        Order order = OrderFactory.create(customerId, items);

        try {
            // Process payment
            paymentService.charge(order.customerId(), order.totalAmount());

            // Confirm order
            order.confirm();
            orderRepository.save(order);

            // Publish event
            eventPublisher.publish(new OrderPlacedEvent(
                order.id().value(),
                order.customerId(),
                order.totalAmount(),
                Instant.now()
            ));

            return order;
        } catch (PaymentFailedException e) {
            // Release inventory
            items.forEach(item ->
                inventoryService.release(item.productId(), item.quantity()));
            throw new OrderPlacementException("Payment failed", e);
        }
    }

    @Override
    public void cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (order.status() != OrderStatus.CONFIRMED) {
            throw new IllegalStateException(
                "Cannot cancel order in status: " + order.status());
        }

        order.cancel();
        orderRepository.save(order);

        // Release inventory
        order.lines().forEach(line ->
            inventoryService.release(line.product().id(), line.quantity()));

        // Refund payment
        paymentService.refund(order.customerId(), order.totalAmount());
    }

    @Override
    public Optional<Order> getOrder(String orderId) {
        return orderRepository.findById(orderId);
    }
}
```

**Enterprise patterns demonstrated:**
- Dependency injection via constructor
- Interface-based design (loose coupling)
- Transaction-like behavior with rollback
- Domain events for decoupling
- Null validation with `Objects.requireNonNull`
- Specific exception types for different failures

## 17. Performance

### Performance Impact of OOP Choices

| Choice | Performance Impact | Recommendation |
|--------|-------------------|----------------|
| Records vs POJOs | Negligible difference | Use records for value objects |
| Interface vs Abstract class | Similar dispatch speed | Use interfaces for flexibility |
| Composition vs Inheritance | Slight delegation overhead | Favor composition |
| Immutability | Slight creation overhead, no sync needed | Favor immutability |
| Encapsulation | Inlined by JVM | Always use |

### String Operations

```java
// BAD: O(n²) time complexity
String result = "";
for (String s : list) {
    result += s; // Creates new String each iteration
}

// GOOD: O(n) time complexity
StringBuilder sb = new StringBuilder(list.size() * 16);
for (String s : list) {
    sb.append(s);
}
String result = sb.toString();

// BETTER: Stream API
String result = String.join("", list);
```

### Collection Sizing

```java
// BAD: Multiple resizes
List<String> items = new ArrayList<>();
for (int i = 0; i < 10000; i++) {
    items.add(generateItem(i)); // Resizes ~14 times
}

// GOOD: Pre-sized
List<String> items = new ArrayList<>(10000);
for (int i = 0; i < 10000; i++) {
    items.add(generateItem(i)); // No resizes
}
```

## 18. Time Complexity

| Operation | Time Complexity | Notes |
|-----------|-----------------|-------|
| ArrayList.get(index) | O(1) | Random access |
| ArrayList.add(element) | O(1) amortized | Occasional resize |
| ArrayList.remove(element) | O(n) | Shift elements |
| LinkedList.get(index) | O(n) | Traverse list |
| LinkedList.add(element) | O(1) | If position known |
| HashMap.get(key) | O(1) amortized | Hash + bucket |
| TreeMap.get(key) | O(log n) | Red-black tree |
| HashSet.contains(element) | O(1) amortized | Hash + bucket |
| Record accessors | O(1) | Direct field access |
| Stream operations | O(n) per operation | Lazy evaluation helps |

## 19. Space Complexity

| Data Structure | Per-Element Overhead | When to Use |
|---------------|---------------------|-------------|
| `int[]` | 4 bytes/element | Primitive arrays |
| `Integer[]` | 16+ bytes/element | When object needed |
| `ArrayList<Integer>` | 20+ bytes/element | Dynamic sizing |
| `HashMap<K,V>` | 50+ bytes/entry | Key-value lookup |
| `record Point(int x, int y)` | 24 bytes total | Immutable value object |
| `class Point { int x, y; }` | 24 bytes total | Mutable value object |
| `LinkedList<Integer>` | 40+ bytes/element | Frequent insert/delete |

## 20. Thread Safety

### Thread-Safe Class Design

```java
// Immutable class (inherently thread-safe)
public record Point(int x, int y) {}

// Thread-safe class with synchronization
public class ThreadSafeCounter {
    private int count = 0;

    public synchronized void increment() {
        count++;
    }

    public synchronized int getCount() {
        return count;
    }
}

// Thread-safe class with atomic operations
public class AtomicCounter {
    private final AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet();
    }

    public int getCount() {
        return count.get();
    }
}

// Thread-safe using volatile
public class VolatileFlag {
    private volatile boolean running = true;

    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }
}
```

### Avoiding Common Thread-Safety Mistakes

```java
// BAD: Not thread-safe
public class UnsafeCache {
    private final Map<String, Object> cache = new HashMap<>();
    public Object get(String key) { return cache.get(key); }
    public void put(String key, Object value) { cache.put(key, value); }
}

// GOOD: Thread-safe
public class SafeCache {
    private final ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();
    public Object get(String key) { return cache.get(key); }
    public void put(String key, Object value) { cache.put(key, value); }
}
```

## 21. Best Practices

### Class Design Checklist

```java
// 1. Make classes final unless designed for inheritance
public final class UserService { }

// 2. Make fields private
private String name;

// 3. Minimize mutable state
private final List<String> items;

// 4. Validate in constructors
public UserService(String name) {
    this.name = Objects.requireNonNull(name, "name must not be null");
}

// 5. Return defensive copies
public List<String> getItems() {
    return List.copyOf(items); // Unmodifiable copy
}

// 6. Implement equals, hashCode, toString
@Override
public boolean equals(Object o) { ... }
@Override
public int hashCode() { ... }
@Override
public String toString() { ... }
```

### Naming Conventions

| Element | Convention | Example |
|---------|------------|---------|
| Classes | PascalCase | `UserService` |
| Interfaces | PascalCase | `UserRepository` |
| Records | PascalCase | `Point` |
| Enums | PascalCase | `OrderStatus` |
| Methods | camelCase | `getUserById` |
| Constants | UPPER_SNAKE | `MAX_RETRIES` |
| Packages | lowercase | `com.company.module` |
| Type parameters | Single uppercase | `T`, `E`, `K`, `V` |
| Boolean getters | `is`/`has` prefix | `isActive()`, `hasItems()` |

### Error Handling

```java
// BAD: Catching generic exceptions
try {
    service.process(order);
} catch (Exception e) {
    log.error("Failed", e);
}

// GOOD: Catching specific exceptions
try {
    service.process(order);
} catch (ValidationException e) {
    return ResponseEntity.badRequest().body(e.getMessage());
} catch (PaymentFailedException e) {
    return ResponseEntity.status(502).body("Payment gateway error");
} catch (ServiceException e) {
    log.error("Unexpected service error", e);
    return ResponseEntity.internalServerError().body("Internal error");
}
```

## 22. Common Mistakes

### Mistake 1: Using Inheritance for Code Reuse

```java
// BAD: Inheritance for code reuse
public class ArrayList extends AbstractList { }

// GOOD: Composition
public class SpecialList {
    private final List<Object> delegate = new ArrayList<>();
    public void add(Object item) { delegate.add(item); }
}
```

### Mistake 2: Exposing Internal Mutable State

```java
// BAD: Returns mutable internal state
public class Person {
    private final List<String> hobbies = new ArrayList<>();
    public List<String> getHobbies() { return hobbies; }
}
Person p = new Person();
p.getHobbies().add("hacking"); // Modifies internal state!

// GOOD: Returns defensive copy
public List<String> getHobbies() { return List.copyOf(hobbies); }
```

### Mistake 3: Using Null Instead of Empty Collections

```java
// BAD: Null returns
public List<Order> getOrders() {
    return hasOrders ? orders : null;
}
// Caller must check: if (orders != null) ...

// GOOD: Empty collections
public List<Order> getOrders() {
    return hasOrders ? List.copyOf(orders) : List.of();
}
// Caller can safely iterate: orders.forEach(...)
```

### Mistake 4: God Class

```java
// BAD: Does everything
public class UserManager {
    void createUser() { }
    void deleteUser() { }
    void sendEmail() { }
    void generateReport() { }
    void processPayment() { }
    void updateInventory() { }
}

// GOOD: Single Responsibility
public class UserRegistrationService { void register(User user) { } }
public class UserDeletionService { void delete(UserId id) { } }
public class EmailService { void send(Email email) { } }
```

### Mistake 5: Mutable Shared State

```java
// BAD: Shared mutable state
public class OrderProcessor {
    private final List<Order> processedOrders = new ArrayList<>(); // Shared!

    public void process(Order order) {
        // ... processing ...
        processedOrders.add(order); // Race condition!
    }
}

// GOOD: Immutable or synchronized
public class OrderProcessor {
    private final List<Order> processedOrders =
        Collections.synchronizedList(new ArrayList<>());

    public void process(Order order) {
        // ... processing ...
        processedOrders.add(order);
    }
}
```

## 23. Pitfalls

| Pitfall | Description | Impact | Solution |
|---------|-------------|--------|----------|
| **Over-inheritance** | Deep class hierarchies | Fragile base class | Use composition |
| **Anemic domain model** | Entities with only getters/setters | Lost domain logic | Put behavior in entities |
| **Null as valid value** | Methods return null | NPE, unclear API | Return Optional or empty |
| **Breaking encapsulation** | Public fields or getters+setters for everything | Tight coupling | Design proper abstractions |
| **Premature abstraction** | Creating interfaces too early | Over-engineering | Wait for 2+ implementations |
| **Ignoring equals/hashCode** | Using default Object methods | Broken collections | Always override when needed |
| **Mutable records** | Records with mutable fields | Violates record contract | Use records only for immutable data |

## 24. Debugging Tips

### Identifying OOP Issues

1. **God Class Detection**: If a class has more than 10 public methods or more than 7 fields, consider splitting it.

2. **Feature Envy**: If method A uses method B's data more than its own, move the method to class B.

3. **Shotgun Surgery**: If a change requires modifications in many classes, the design has high coupling.

4. **Long Parameter Lists**: If a method has more than 3 parameters, consider using a record or builder pattern.

### Debugging Encapsulation Violations

```java
// Use your IDE's "Find Usages" to detect:
// 1. Direct field access from outside the class
// 2. Setters that are called from multiple places
// 3. Mutable fields returned without defensive copies
```

### Debugging Thread Safety

```java
// Use jstack to detect deadlocks
jstack <pid>

// Use ThreadMXBean to detect deadlocks programmatically
ThreadMXBean tmx = ManagementFactory.getThreadMXBean();
long[] deadlockedThreads = tmx.findDeadlockedThreads();
if (deadlockedThreads != null) {
    System.err.println("Deadlock detected!");
}
```

## 25. Comparison Table

### OOP Principles Comparison

| Principle | Violation Example | Correct Implementation |
|-----------|-------------------|----------------------|
| SRP | `UserManager` does registration, email, reports | Separate services for each |
| OCP | Adding new payment type requires modifying switch | Use polymorphism or strategy |
| LSP | `Square extends Rectangle` breaks setWidth/setHeight | Separate class hierarchy |
| ISP | `interface IWorker { work(); eat(); sleep(); }` | Split into `Workable`, `Feedable` |
| DIP | Service directly instantiates `MySQLRepository` | Inject `Repository` interface |

### Modern Java Features Comparison

| Feature | Pre-Java 16 | Java 16+ (Records) | Java 17+ (Sealed) | Java 21 (Pattern) |
|---------|-------------|--------------------|--------------------|-------------------|
| Value object | POJO with equals/hashCode | `record Point(int x, int y)` | — | `switch (p) { case Point(x, y) -> ... }` |
| Type hierarchy | Open inheritance | — | `sealed interface` | Exhaustive matching |
| Pattern match | `instanceof` + cast | — | — | `case Point(int x, _) ->` |

## 26. Decision Tree

### Should I Use a Record, Class, or Interface?

```
Need a data carrier?
├── Yes
│   ├── Immutable? → Yes → Use a record
│   ├── Mutable? → Yes → Use a final class
│   └── Need inheritance? → Yes → Use an abstract class
└── No
    ├── Defining behavior contract? → Yes → Use an interface
    └── Need shared implementation? → Yes → Use an abstract class
```

### Composition vs Inheritance?

```
Need to reuse code?
├── Is it a true "is-a"? → No → Use composition
├── Is parent designed for inheritance? → No → Use composition
├── Will you need to change behavior at runtime? → Yes → Use composition
└── Is it a simple hierarchy with stable API? → Yes → Consider inheritance
```

## 27. Interview Questions

### Basic

1. **What are the four pillars of OOP?**
   Encapsulation (hiding internal state), Abstraction (hiding complexity), Inheritance (code reuse), Polymorphism (same interface, different behavior).

2. **What is the difference between abstraction and encapsulation?**
   Abstraction defines what an object does (interface). Encapsulation defines how it does it (implementation hiding). Abstraction is about design; encapsulation is about protection.

3. **What is the SOLID principle?**
   Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, Dependency Inversion. A set of design principles for maintainable OOP systems.

### Intermediate

4. **When would you use composition over inheritance?**
   When the relationship is not a true "is-a", when you need runtime flexibility, when the parent class is not designed for inheritance, or when you want to reduce coupling.

5. **What makes a class immutable?**
   The class is final, all fields are final, no setter methods, constructor deep copies mutable fields, and getters return defensive copies.

6. **What is a record in Java and when should you use it?**
   A record is a concise syntax for immutable data classes. Use it for value objects, DTOs, and data carriers. It auto-generates constructors, accessors, equals, hashCode, and toString.

### Advanced

7. **Explain the Liskov Substitution Principle with an example.**
   If `Squawk extends Bird`, then anywhere a `Bird` is expected, a `Squawk` must work correctly. If `Bird.fly()` throws `UnsupportedOperationException` for `Squawk`, it violates LSP.

8. **How do sealed classes improve OOP design?**
   They restrict inheritance, enabling exhaustive pattern matching, better compiler checks, and clearer domain modeling. The compiler knows all possible subtypes.

9. **What is the difference between an abstract class and an interface in Java 21?**
   Interfaces can have default methods, static methods, and be sealed. Abstract classes can have constructors, instance fields, and non-public members. Use interfaces for contracts, abstract classes for partial implementations.

10. **How do you design a thread-safe mutable class?**
    Use synchronization (synchronized methods/blocks), atomic classes (AtomicInteger), volatile fields, or immutable objects. Consider ConcurrentHashMap for thread-safe collections.

## 28. Exercises

### Exercise 1: Class Design (Beginner)

Design a `Temperature` class that:
- Stores temperature in Celsius
- Provides methods to get temperature in Celsius, Fahrenheit, and Kelvin
- Is immutable
- Validates that temperature is above absolute zero (-273.15°C)
- Implements equals, hashCode, and toString

### Exercise 2: Composition (Intermediate)

Refactor this inheritance hierarchy using composition:

```java
// Current: inheritance-based
class Vehicle { void start() { } }
class Car extends Vehicle { void openTrunk() { } }
class Truck extends Vehicle { void loadCargo() { } }
```

Create interfaces for `Startable`, `Trunkable`, `CargoLoadable` and use composition.

### Exercise 3: Sealed Classes (Intermediate)

Design a sealed class hierarchy for a payment processing system:
- `Payment` interface (sealed)
- `CreditCardPayment`, `BankTransferPayment`, `CryptoPayment`
- Pattern matching in a `PaymentProcessor` to calculate fees

### Exercise 4: Error Handling (Advanced)

Design an error handling framework for an e-commerce system:
- Base `ServiceException` with error code
- Specific exceptions: `ValidationException`, `PaymentException`, `InventoryException`
- A `Result<T>` class (like Rust's Result) for error handling without exceptions

### Exercise 5: Enterprise Design (Advanced)

Design a complete OOP solution for a notification system:
- Support email, SMS, and push notifications
- Support different providers (SendGrid, Twilio, Firebase)
- Use dependency injection
- Be fully testable with mocks
- Handle failures gracefully with retries

## 29. Assignments

### Assignment 1: Design Patterns Implementation

Implement the following design patterns with proper OOP principles:
1. **Strategy Pattern**: Sort with different algorithms (Bubble, Quick, Merge)
2. **Observer Pattern**: Event system with publishers and subscribers
3. **Factory Pattern**: Shape factory creating Circle, Rectangle, Triangle

Each must demonstrate encapsulation, abstraction, and proper error handling.

### Assignment 2: Domain Model

Design a complete domain model for a library management system:
- Entities: Book, Member, Loan
- Value Objects: ISBN, DateRange, Money
- Services: LendingService, NotificationService
- Use records, sealed classes, and pattern matching

### Assignment 3: Code Review

Review the following code and identify OOP violations:

```java
public class DataProcessor {
    public static List<Map<String, Object>> processData(
            String input, String format, boolean validate,
            boolean log, String outputPath) {
        // 500 lines of mixed concerns
    }
}
```

Refactor into proper OOP design with SRP, DIP, and proper encapsulation.

### Assignment 4: Test-Driven Development

Write a `ShoppingCart` class following TDD:
1. Write tests first for adding items, calculating total, applying discounts
2. Implement the class to pass all tests
3. Refactor while keeping tests green
4. Achieve 100% line coverage

## 30. Mini Project: E-Commerce Domain Model

### Project Description

Build a complete OOP domain model for an e-commerce platform using modern Java 21 features.

### Requirements

1. **Entities**: Customer, Product, Order, OrderLine
2. **Value Objects**: Email, Money, Address, ProductId
3. **Enums**: OrderStatus, PaymentMethod
4. **Services**: OrderService, PaymentService
5. **Use Records**: For all value objects
6. **Use Sealed Classes**: For payment types
7. **Use Pattern Matching**: For status-based logic
8. **Thread Safety**: Concurrent order processing
9. **Testing**: 90%+ coverage with JUnit 5

### Implementation Structure

```
ecommerce-domain/
├── src/main/java/
│   ├── com/example/ecommerce/
│   │   ├── model/
│   │   │   ├── Customer.java
│   │   │   ├── Product.java
│   │   │   ├── Order.java
│   │   │   └── OrderLine.java
│   │   ├── valueobject/
│   │   │   ├── Email.java
│   │   │   ├── Money.java
│   │   │   ├── Address.java
│   │   │   └── ProductId.java
│   │   ├── payment/
│   │   │   ├── Payment.java (sealed)
│   │   │   ├── CreditCardPayment.java
│   │   │   └── BankTransferPayment.java
│   │   └── service/
│   │       ├── OrderService.java
│   │       └── PaymentService.java
│   └── module-info.java
├── src/test/java/
│   └── com/example/ecommerce/
│       ├── model/OrderTest.java
│       ├── valueobject/MoneyTest.java
│       └── service/OrderServiceTest.java
└── pom.xml
```

### Evaluation Criteria

- Proper OOP principles (SOLID, encapsulation, composition)
- Effective use of Java 21 features (records, sealed, pattern matching)
- Thread safety for concurrent operations
- Comprehensive test coverage
- Clean, readable code following Google Java Style

## 31. Summary

| Practice | Key Point |
|----------|-----------|
| **SOLID** | Five principles for maintainable OOP design |
| **Composition** | Prefer "has-a" over "is-a" |
| **Immutability** | Use records and final fields |
| **Encapsulation** | Private fields, public methods, defensive copies |
| **Naming** | PascalCase for types, camelCase for methods |
| **Error Handling** | Specific exceptions, not generic Exception |
| **Testing** | Program to interfaces, inject dependencies |
| **Java 21** | Records, sealed classes, pattern matching |
| **Thread Safety** | Immutable classes, synchronized, atomic operations |
| **Defense** | Validate in constructors, null-check, validate state |

### Quick Reference

```java
// Immutable value object
public record Point(int x, int y) {}

// Sealed type hierarchy
public sealed interface Shape permits Circle, Rectangle {}

// Pattern matching
switch (shape) {
    case Circle c -> "Circle with radius " + c.radius();
    case Rectangle r -> "Rectangle " + r.width() + "x" + r.height();
}

// Defensive copy
public List<String> getItems() { return List.copyOf(items); }

// Null validation
Objects.requireNonNull(name, "name must not be null");
```

## 32. References

- [Effective Java, 3rd Edition](https://www.oreilly.com/library/view/effective-java/9780134686097/) — Joshua Bloch
- [Clean Code](https://www.oreilly.com/library/view/clean-code/9780136083238/) — Robert C. Martin
- [Design Patterns: Elements of Reusable Object-Oriented Software](https://www.oreilly.com/library/view/design-patterns-elements/9780134655581/) — Gang of Four
- [Java Language Specification](https://docs.oracle.com/javase/specs/jls/se21/html/jls-21.html)
- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- [Oracle Java Tutorials - Object-Oriented Programming](https://docs.oracle.com/javase/tutorial/java/concepts/)
- [OpenJDK 21 Documentation](https://openjdk.org/projects/jdk/21/)
- [Java Records (JEP 395)](https://openjdk.org/jeps/395)
- [Sealed Classes (JEP 409)](https://openjdk.org/jeps/409)
- [Pattern Matching for switch (JEP 441)](https://openjdk.org/jeps/441)
