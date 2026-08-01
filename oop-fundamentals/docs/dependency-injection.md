# Dependency Injection: Decoupling Object Creation from Usage

## 1. Introduction

Dependency Injection (DI) is a design pattern where an object's dependencies are provided externally rather than created internally. Instead of a class instantiating its own collaborators, they are "injected" from the outside — typically via constructor, setter, or field. DI is the cornerstone of modern software architecture, enabling testability, flexibility, and loose coupling.

## 2. Learning Objectives

- Understand the difference between DI and manual dependency creation
- Implement all four injection types (constructor, setter, field, method)
- Recognize the role of IoC containers and DI frameworks
- Compare DI with the Factory Pattern and Service Locator
- Apply DI principles in real-world applications
- Build a simple DI container from scratch

## 3. Prerequisites

- Solid understanding of Java classes, constructors, and interfaces
- Familiarity with OOP principles (encapsulation, polymorphism)
- Basic knowledge of collections (`Map`, `List`)
- Understanding of `final` keyword and immutability

## 4. Why This Concept Exists

Without DI, classes create their own dependencies internally:

```java
class OrderService {
    private final PaymentGateway gateway = new StripeGateway(); // Hard-coded
}
```

Problems:
- **Untestable:** Cannot substitute a mock gateway for unit tests
- **Tightly coupled:** Switching to PayPal requires editing `OrderService`
- **Violates SRP:** Class responsible for both business logic AND object creation

DI solves these by inverting control: the class *requests* dependencies; an external entity *provides* them.

## 5. Problem Statement

```java
// Problem: Hard-coded dependencies
class UserService {
    private final UserRepository repo = new PostgresUserRepository();
    private final EmailService email = new SmtpEmailService();

    public void register(String name, String emailAddr) {
        User user = new User(name, emailAddr);
        repo.save(user);
        email.send(emailAddr, "Welcome!");
    }
}
```

- Cannot test `UserService` without a real database
- Cannot switch email providers without editing the class
- `UserService` knows about implementation details (Postgres, SMTP)

**Goal:** `UserService` should depend on abstractions (`UserRepository`, `EmailService` interfaces) and receive implementations externally.

## 6. Theory

### 6.1 Inversion of Control (IoC)

DI is a form of IoC: the flow of control is inverted. Instead of the class controlling its dependencies, an external container controls the wiring.

```
Traditional:    Class → creates → Dependency
IoC/DI:        Class ← receives ← Dependency (from container)
```

### 6.2 The Four Injection Types

| Type | Mechanism | Lifecycle Control | Testability |
|------|-----------|-------------------|-------------|
| Constructor | Passed via constructor | Compile-time enforced | Excellent |
| Setter | Passed via setter method | Runtime modifiable | Good |
| Field | Annotated field | Framework-managed | Poor |
| Method | Passed as method parameter | Method-scope only | Excellent |

### 6.3 DI Containers

A DI container is a runtime object that manages dependency creation, wiring, and lifecycle. Popular examples: Spring IoC, Guice, Dagger.

### 6.4 DI vs Factory Pattern

| Aspect | DI | Factory |
|--------|-----|---------|
| Who creates? | Container/client | Factory class |
| Coupling | Very loose | Moderate |
| Configuration | Declarative | Programmatic |
| Lifecycle management | Container-managed | Manual |

## 7. Internal Working

### Constructor Injection Flow

```
1. Client creates OrderService(PaymentGateway gateway)
2. Container resolves PaymentGateway → StripeGateway
3. Container calls: new OrderService(new StripeGateway())
4. OrderService stores the injected gateway
5. OrderService uses gateway in processOrder()
```

### Setter Injection Flow

```
1. Client creates OrderService() (no-arg constructor)
2. Container calls: orderService.setGateway(new StripeGateway())
3. OrderService stores the gateway via setter
4. OrderService uses gateway in processOrder()
```

### Method Injection Flow

```
1. Client calls: orderService.processOrder(order, paymentGateway)
2. Gateway used only for this method call
3. No field storage — dependency is ephemeral
```

## 8. JVM Perspective

### Constructor Injection

```
Stack                          Heap
┌──────────────┐
│ main()       │
│──────────────│
│ gateway ─────┼────→ StripeGateway (instance)
│ orderService ┼────→ OrderService (instance)
│              │       │
│              │       └── gateway ──→ StripeGateway (same instance)
└──────────────┘
```

- Dependencies stored as instance fields — one reference per dependency
- GC collects both the service and its dependencies when unreachable

### Setter Injection

```
Stack                          Heap
┌──────────────┐
│ main()       │
│──────────────│
│ service ─────┼────→ OrderService (instance)
│              │       │
│              │       └── gateway ──→ StripeGateway (same)
└──────────────┘
  No intermediate state — field set via setter
```

### Field Injection (Framework-managed)

```
Reflection access bypasses final keyword:
Field f = OrderService.class.getDeclaredField("gateway");
f.setAccessible(true); // Bypasses access control
f.set(serviceInstance, new StripeGateway());
```

This is why field injection is fragile — it bypasses compile-time safety.

## 9. Memory Representation

```
CONSTRUCTOR INJECTION
┌──────────────┐
│ OrderService │
│──────────────│
│ gateway ─────┼──→ StripeGateway
│ logger   ────┼──→ FileLogger
│ repo     ────┼──→ PostgresRepo
└──────────────┘
All fields set at construction — immutable after creation

SETTER INJECTION
┌──────────────┐
│ OrderService │
│──────────────│
│ gateway ─────┼──→ StripeGateway  (can change at runtime)
└──────────────┘

METHOD INJECTION (no field storage)
┌──────────────┐
│ OrderService │
│──────────────│
│ (no gateway field)
│ processOrder(order, gateway) ← parameter, lives on stack
└──────────────┘
```

## 10. Syntax

### Constructor Injection
```java
public final class OrderService {
    private final PaymentGateway gateway;
    private final Logger logger;

    @Inject
    public OrderService(PaymentGateway gateway, Logger logger) {
        this.gateway = Objects.requireNonNull(gateway);
        this.logger = Objects.requireNonNull(logger);
    }
}
```

### Setter Injection
```java
public class OrderService {
    private PaymentGateway gateway;
    private Logger logger;

    public void setGateway(PaymentGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    public void setLogger(Logger logger) {
        this.logger = Objects.requireNonNull(logger);
    }
}
```

### Field Injection
```java
public class OrderService {
    @Autowired
    private PaymentGateway gateway;

    @Autowired
    private Logger logger;
}
```

### Method Injection
```java
public class OrderService {
    public OrderConfirmation processOrder(Order order, PaymentGateway gateway) {
        // gateway used only for this method
        return gateway.charge(order.calculateTotal());
    }
}
```

## 11. Easy Example

```java
public class Main {
    public static void main(String[] args) {
        // Constructor injection — easy to test
        MessageService service = new ConsoleMessageService();
        MessageController controller = new MessageController(service);

        controller.sendMessage("Hello, World!");
    }
}

interface MessageService {
    void send(String message);
}

final class ConsoleMessageService implements MessageService {
    @Override
    public void send(String message) {
        System.out.println("[CONSOLE] " + message);
    }
}

final class MessageController {
    private final MessageService service;

    public MessageController(MessageService service) {
        this.service = Objects.requireNonNull(service);
    }

    public void sendMessage(String message) {
        service.send(message);
    }
}
```

## 12. Medium Example

```java
// Full DI example with multiple dependencies
public interface UserRepository {
    void save(User user);
    Optional<User> findById(String id);
}

public interface EmailService {
    void sendWelcomeEmail(String email);
}

public record User(String id, String name, String email) {}

public final class InMemoryUserRepository implements UserRepository {
    private final Map<String, User> store = new ConcurrentHashMap<>();

    @Override
    public void save(User user) {
        store.put(user.id(), user);
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }
}

public final class SmtpEmailService implements EmailService {
    private final String smtpHost;

    public SmtpEmailService(String smtpHost) {
        this.smtpHost = Objects.requireNonNull(smtpHost);
    }

    @Override
    public void sendWelcomeEmail(String email) {
        System.out.println("Sending welcome email to " + email
            + " via " + smtpHost);
    }
}

public final class UserService {
    private final UserRepository repo;
    private final EmailService email;

    public UserService(UserRepository repo, EmailService email) {
        this.repo = Objects.requireNonNull(repo);
        this.email = Objects.requireNonNull(email);
    }

    public void register(String name, String emailAddr) {
        User user = new User(UUID.randomUUID().toString(), name, emailAddr);
        repo.save(user);
        email.sendWelcomeEmail(emailAddr);
    }
}

public class MediumExample {
    public static void main(String[] args) {
        UserRepository repo = new InMemoryUserRepository();
        EmailService email = new SmtpEmailService("smtp.example.com");

        UserService service = new UserService(repo, email);
        service.register("Alice", "alice@example.com");
    }
}
```

## 13. Hard Example

```java
// DI container implementation from scratch
public final class SimpleContainer {
    private final Map<Class<?>, Supplier<?>> factories = new HashMap<>();
    private final Map<Class<?>, Object> singletons = new ConcurrentHashMap<>();

    public <T> void register(Class<T> type, Supplier<T> factory) {
        factories.put(type, factory);
    }

    public <T> void registerSingleton(Class<T> type, T instance) {
        singletons.put(type, instance);
    }

    @SuppressWarnings("unchecked")
    public <T> T resolve(Class<T> type) {
        if (singletons.containsKey(type)) {
            return (T) singletons.get(type);
        }

        Supplier<?> factory = factories.get(type);
        if (factory == null) {
            throw new IllegalStateException("No registration for " + type.getName());
        }

        T instance = (T) factory.get();
        singletons.put(type, instance);
        return instance;
    }
}

// Usage
public class ContainerDemo {
    public static void main(String[] args) {
        SimpleContainer container = new SimpleContainer();

        // Register implementations
        container.register(UserRepository.class, InMemoryUserRepository::new);
        container.register(EmailService.class, () -> new SmtpEmailService("smtp.example.com"));

        // Register singleton
        container.registerSingleton(Logger.class, new FileLogger("/var/log/app.log"));

        // Resolve — constructor dependencies are auto-resolved
        UserService service = container.resolve(UserService.class);
        service.register("Bob", "bob@example.com");
    }
}
```

## 14. Enterprise Example

```java
// Spring-style DI in plain Java
public interface OrderRepository {
    void save(Order order);
    Optional<Order> findById(String id);
}

public interface PaymentGateway {
    PaymentResult charge(Money amount, PaymentMethod method);
}

public interface InventoryService {
    boolean reserve(String productId, int quantity);
}

public record Order(String id, String productId, int quantity,
                     Money total, PaymentMethod method) {}

public record PaymentResult(boolean success, String transactionId) {}

public final class DatabaseOrderRepository implements OrderRepository {
    private final DataSource dataSource;

    public DatabaseOrderRepository(DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource);
    }

    @Override
    public void save(Order order) {
        // JDBC insert using dataSource
        System.out.println("Saving order " + order.id() + " to database");
    }

    @Override
    public Optional<Order> findById(String id) {
        return Optional.empty();
    }
}

public final class StripePaymentGateway implements PaymentGateway {
    private final String apiKey;

    public StripePaymentGateway(String apiKey) {
        this.apiKey = Objects.requireNonNull(apiKey);
    }

    @Override
    public PaymentResult charge(Money amount, PaymentMethod method) {
        System.out.println("Charging " + amount + " via Stripe");
        return new PaymentResult(true, "txn_" + UUID.randomUUID());
    }
}

public final class OrderService {
    private final OrderRepository repo;
    private final PaymentGateway gateway;
    private final InventoryService inventory;
    private final Logger logger;

    public OrderService(OrderRepository repo, PaymentGateway gateway,
                        InventoryService inventory, Logger logger) {
        this.repo = Objects.requireNonNull(repo);
        this.gateway = Objects.requireNonNull(gateway);
        this.inventory = Objects.requireNonNull(inventory);
        this.logger = Objects.requireNonNull(logger);
    }

    public OrderConfirmation placeOrder(String productId, int quantity,
                                         Money price, PaymentMethod method) {
        // 1. Check inventory
        if (!inventory.reserve(productId, quantity)) {
            throw new InsufficientInventoryException(productId);
        }

        // 2. Create order
        Order order = new Order(
            UUID.randomUUID().toString(), productId, quantity,
            price.multiply(quantity), method
        );

        // 3. Process payment
        PaymentResult result = gateway.charge(order.total(), method);
        if (!result.success()) {
            throw new PaymentFailedException(result.transactionId());
        }

        // 4. Persist
        repo.save(order);

        // 5. Log
        logger.info("Order placed: " + order.id());

        return new OrderConfirmation(order.id(), result.transactionId());
    }
}
```

## 15. Performance

| Injection Type | Allocation | Access Speed | GC Impact | Thread Safety |
|---------------|-----------|-------------|-----------|---------------|
| Constructor | Once at creation | Fastest (final field) | Minimal | Safe (immutable) |
| Setter | Once at creation | Fast | Minimal | Mutable — requires sync |
| Field | Framework-managed | Moderate (reflection) | Moderate | Unsafe |
| Method | Per-call (stack) | Fast (stack only) | None | Safe |

**Constructor injection is fastest** — `final` fields can be optimized by the JVM (inlining, no null checks after construction).

**Field injection is slowest** — reflection access overhead on every injection.

## 16. Best Practices

1. **Use constructor injection by default** — Enforces required dependencies at compile time
2. **Make fields `final`** — Ensures immutability after construction
3. **Use interfaces for dependencies** — Enables swapping implementations
4. **Validate with `Objects.requireNonNull()`** — Fail fast on null dependencies
5. **Avoid circular dependencies** — Redesign if container detects cycles
6. **Prefer constructor injection over setter** — Setters imply optional dependencies
7. **Use method injection for transient needs** — Don't store what you only use once
8. **Keep constructors small** — Delegate complex wiring to init methods if needed
9. **Document which dependencies are required vs optional** — Use annotations or Javadoc

## 17. Common Mistakes

| Mistake | Why It's Wrong | Fix |
|---------|---------------|-----|
| Field injection with `@Autowired` | Untestable, bypasses `final` | Use constructor injection |
| Null constructor parameters | NPE at runtime | Use `Objects.requireNonNull()` |
| Creating dependencies inside constructor | Still coupled | Accept abstractions as parameters |
| Circular constructor dependencies | Stack overflow / container error | Break cycle with setter or redesign |
| Mutable setter injection without sync | Thread safety issues | Synchronize or use constructor |
| Over-injecting (too many parameters) | God class indicator | Split into smaller focused classes |
| Ignoring optional dependencies | Confusing API | Use `Optional<T>` or setter |

## 18. Pitfalls

1. **Constructor overload explosion** — Too many optional parameters create combinatorial constructors
2. **Framework dependency** — Using Spring/Guice for everything adds startup overhead
3. **Debugging difficulty** — DI containers hide wiring; harder to trace object creation
4. **Startup time** — Large containers scan thousands of classes
5. **Circular dependency detection** — Some containers fail silently on cycles
6. **Scope mismatch** — Singleton service calling request-scoped dependency causes bugs

## 19. Debugging Tips

1. **Use `--debug` flags** — Spring Boot: `--debug` shows auto-configuration
2. **Enable container logging** — `logging.level.org.springframework=DEBUG`
3. **Inspect bean definitions** — Spring: `ApplicationContext.getBeanDefinitionNames()`
4. **Use constructor injection** — Stack traces show exactly which dependency failed
5. **Write integration tests** — Verify the full object graph is wired correctly
6. **Use `@Primary` or `@Qualifier`** — Disambiguate when multiple implementations exist
7. **Check for `NoSuchBeanDefinitionException`** — Missing registration

## 20. Comparison Table

| Aspect | Constructor | Setter | Field | Method |
|--------|------------|--------|-------|--------|
| Required deps | Yes | No | No | N/A |
| Optional deps | No | Yes | No | N/A |
| Immutability | Yes | No | No | N/A |
| Testability | Excellent | Good | Poor | Excellent |
| Framework needed | No | No | Yes | No |
| Null safety | Enforced | Manual | N/A | N/A |
| Circular deps | Detected | Allowed | Allowed | N/A |
| Complexity | Low | Low | Low | Low |

## 21. Decision Tree

```
Does the dependency need to be available throughout the object's lifecycle?
│
├── YES
│   │
│   ├── Is it always required (non-optional)?
│   │   └── YES → Constructor Injection
│   │
│   ├── Is it optional or changeable at runtime?
│   │   └── YES → Setter Injection
│   │
│   └── Can it be managed by a framework?
│       └── YES → Field Injection (if framework enforces it)
│
└── NO (only needed for a single method call)
    └── Method Injection
```

## 22. Interview Questions

**Q1: What is Dependency Injection?**
A: DI is a pattern where an object's dependencies are provided externally rather than created internally. It inverts control — the class requests dependencies; a container provides them.

**Q2: What are the types of DI?**
A: Constructor injection (recommended), setter injection, field injection (avoid), and method injection.

**Q3: Why is constructor injection preferred?**
A: It enforces required dependencies at compile time, ensures immutability with `final` fields, and doesn't require a framework.

**Q4: What is the difference between DI and IoC?**
A: IoC is a broad principle (inverting control flow). DI is a specific implementation of IoC where dependencies are injected externally.

**Q5: How does a DI container work?**
A: A container maintains a registry of types to implementations. When a type is requested, it resolves all constructor dependencies recursively, creates the instance, and manages its lifecycle.

**Q6: What is the difference between singleton and prototype scope?**
A: Singleton scope returns the same instance every time. Prototype scope creates a new instance per request.

**Q7: Can you have circular dependencies in constructor injection?**
A: No — constructors must complete before the object exists. Circles are detected at startup. Use setter injection to break the cycle.

## 23. Exercises

1. **Convert to Constructor Injection:** Refactor a class that uses field injection to use constructor injection with `final` fields.

2. **Build a Simple Container:** Implement a DI container that resolves constructor dependencies recursively (see Section 13).

3. **Mock Injection:** Write a unit test for `UserService` that injects a mock `UserRepository` and `EmailService`.

4. **Scope Experiment:** Implement singleton vs prototype scope in your container. Verify behavior with tests.

5. **Qualifiers:** Implement `@Qualifier` annotation to select between multiple implementations of the same interface.

## 24. Assignments

1. **DI Container v2:** Extend the simple container from Section 13 with:
   - Lifecycle management (singleton, prototype, request-scoped)
   - `@PostConstruct` and `@PreDestroy` callbacks
   - Circular dependency detection
   - Qualifier support

2. **Service Registry:** Build a plugin system where services register themselves and are resolved by interface type. Include hot-reloading of implementations.

3. **Spring Migration:** Take a class using `new` for dependencies and convert it to use Spring-style constructor injection with `@Component` and `@Autowired` annotations.

## 25. Mini Project

### Task Management System with DI

Build a task management system using dependency injection throughout:

**Requirements:**
- `TaskService` depends on `TaskRepository`, `NotificationService`, `UserService` (all interfaces)
- Implement `InMemoryTaskRepository` and `DatabaseTaskRepository`
- Implement `EmailNotificationService` and `SmsNotificationService`
- Build a `ServiceContainer` that wires everything together
- Include constructor injection for required deps, setter for optional
- Write tests that inject mock implementations

**Deliverables:**
- Complete Java implementation with interfaces
- Custom DI container with recursive resolution
- Unit tests with mocked dependencies
- Integration test with real implementations

## 26. Summary

- **DI** = dependencies provided externally, not created internally
- **Constructor injection** is the recommended default — enforces required deps, immutable, testable
- **Setter injection** is for optional or runtime-changeable dependencies
- **Field injection** should be avoided — untestable, bypasses `final`
- **Method injection** is for transient, method-scoped dependencies
- **DI containers** manage creation, wiring, and lifecycle automatically
- DI enables the **Strategy**, **Service Locator**, and **Factory** patterns
- DI is the foundation of modern frameworks (Spring, Guice, Dagger)

## 27. References

- *Effective Java* by Joshua Bloch — Item 17: Minimize mutability
- *Clean Architecture* by Robert C. Martin — Chapter 5: Dependency Rule
- *Spring in Action* by Craig Walls — Chapter 2: Wiring beans
- Martin Fowler, "Inversion of Control Containers and the Dependency Injection Pattern"
- Java SE Documentation — https://docs.oracle.com/en/java/javase/21/docs/api/
- Google Guice Documentation — https://github.com/google/guice

---

*Last updated: August 2026*
