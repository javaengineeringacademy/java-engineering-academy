# Abstract Classes in Java

## 1. Introduction

An **abstract class** is a class declared with the `abstract` keyword that cannot be instantiated directly and may contain both abstract methods (without bodies) and concrete methods (with implementations). Abstract classes form the backbone of the Template Method pattern and serve as a contract-plus-implementation mechanism in object-oriented design.

Abstract classes sit at the intersection of interfaces and concrete classes: they define **what** subclasses must do while providing **shared logic** that subclasses inherit. Unlike interfaces, abstract classes can hold state (instance fields), define constructors, and enforce access modifiers beyond `public`.

In Java, every class participates in single inheritance. Abstract classes leverage this by creating a common base with partial implementation, while interfaces provide the flexibility of multiple contracts.

---

## 2. Learning Objectives

After completing this lesson, you will be able to:

- Declare abstract classes and abstract methods
- Understand when to use abstract classes vs interfaces
- Implement the Template Method pattern
- Work with constructors in abstract classes
- Apply abstract classes in enterprise scenarios (DAO, service layers, payment processing)
- Recognize common mistakes and pitfalls
- Debug issues with abstract class hierarchies

---

## 3. Prerequisites

Before studying abstract classes, you should be comfortable with:

- **Inheritance and polymorphism** — overriding methods, `super` calls
- **Interfaces** — implementing multiple contracts
- **Access modifiers** — `public`, `protected`, `private`, package-private
- **Final keyword** — final classes, methods, variables
- **Generics basics** — understanding type parameters (for generic abstract classes)

---

## 4. Why This Concept Exists

Consider a system with `Circle`, `Rectangle`, and `Triangle`. Each has `area()` and `perimeter()`, but the formulas differ. Without abstract classes, you would:

1. Duplicate the shared field `color` in every shape
2. Forget to implement a required method with no compiler error
3. Have no mechanism to enforce a common constructor signature

Abstract classes solve all three problems:

| Problem | Solution |
|---------|----------|
| Code duplication | Concrete methods and instance fields shared via inheritance |
| Missing implementation | Abstract methods force subclasses to provide bodies |
| No constructor enforcement | Abstract classes define constructors invoked by subclasses |

---

## 5. Problem Statement

**Without abstract classes**, imagine a payment processing system:

```java
// Each class duplicates logic — no shared contract enforcement
class CreditCardPayment {
    public void authorize() { /* ... */ }
    public void capture() { /* ... */ }
    public void refund() { /* ... */ }
}

class UpiPayment {
    public void authorize() { /* ... */ }
    public void capture() { /* ... */ }
    // Missing refund — no compile-time error!
}
```

**With abstract classes**, the compiler catches the missing implementation:

```java
public abstract class Payment {
    protected final String transactionId;

    protected Payment(String transactionId) {
        this.transactionId = Objects.requireNonNull(transactionId);
    }

    public abstract void authorize();
    public abstract void capture();
    public abstract void refund();

    public final String getTransactionId() { return transactionId; }
}
```

Any concrete payment type **must** implement all three methods or remain abstract.

---

## 6. Theory

### 6.1 Abstract Classes

An abstract class:

- Is declared with the `abstract` modifier
- **Cannot** be instantiated with `new`
- **May** contain abstract methods (no body, terminated with `;`)
- **May** contain concrete methods (full implementation)
- **May** contain instance fields, constructors, and any access modifier
- **May** be a subclass of another abstract or concrete class

### 6.2 Abstract Methods

An abstract method:

- Has no body — just a signature ending in `;`
- **Must** be implemented by the first concrete (non-abstract) subclass
- A class with **any** abstract method must itself be declared `abstract`
- Cannot be `final` or `private` (subclasses must override it)

### 6.3 Concrete Methods in Abstract Classes

Abstract classes can provide concrete methods that subclasses inherit:

- **Regular concrete methods** — can be overridden by subclasses
- **Final concrete methods** — cannot be overridden (template enforcement)
- These share implementation logic across the hierarchy

### 6.4 Relationship to Interfaces

| Aspect | Abstract Class | Interface |
|--------|---------------|-----------|
| Inheritance | Single (extends) | Multiple (implements) |
| Fields | Instance fields + constants | `public static final` only |
| Constructors | Yes | No |
| Methods | Abstract + concrete | Abstract + `default` + `static` |
| Access modifiers | Any | `public` for methods |
| State | Yes (instance fields) | No (constants only) |

---

## 7. Internal Working

When the JVM encounters an abstract class:

1. **Compilation**: The compiler generates a class file for the abstract class just like any other class. Abstract methods have no bytecode — they are placeholders.

2. **Loading**: The abstract class is loaded by the class loader when first referenced. Its static initializers run.

3. **Subclass linkage**: When a concrete subclass is loaded, the JVM verifies that all abstract methods from the parent have concrete implementations. A `VerifyError` is thrown if any are missing.

4. **Method resolution**: At runtime, virtual method dispatch (`invokevirtual`) works the same for abstract and concrete classes. The JVM looks up the method in the actual object's class, not the declared type.

5. **Instantiation guard**: Attempting `new AbstractClass()` produces a compile-time error. The JVM never creates an instance of an abstract class.

```
// Compilation flow:
abstract class Shape ──> Shape.class (bytecode)
        │
        ▼
class Circle extends Shape ──> Circle.class (must implement area(), perimeter())
```

---

## 8. JVM Perspective

### 8.1 Class File Structure

An abstract class produces a standard `.class` file with the `ACC_ABSTRACT` flag set in the access flags:

```
// Simplified class file access flags
ACC_PUBLIC | ACC_ABSTRACT | ACC_SUPER  // for public abstract class
```

Abstract methods have entries in the method table with no `Code` attribute (no bytecode).

### 8.2 Method Dispatch

```
Shape s = new Circle("red");
double a = s.area();  // invokevirtual Shape.area()D
                       // Actual method: Circle.area()D (resolved at runtime)
```

The JVM uses the vtable (virtual method table) to dispatch calls. Abstract methods occupy slots in the vtable just like concrete methods — the subclass's implementation fills the slot.

### 8.3 Verification

The JVM's verifier ensures:
- A concrete class implements all inherited abstract methods
- No abstract method is declared `final` in a concrete class
- Abstract methods are not invoked directly (no `invokevirtual` on abstract methods without a concrete target)

---

## 9. Memory Representation

```
// Memory layout when: Shape s = new Circle("red", 5.0);

┌─────────────────────────┐
│ Circle instance (heap)  │
├─────────────────────────┤
│ Object header           │ ← 12 bytes (mark + class pointer)
│ String color            │ ← from Shape (inherited field)
│ double radius           │ ← Circle-specific field
└─────────────────────────┘

┌─────────────────────────────────────┐
│ Method area / Metaspace             │
├─────────────────────────────────────┤
│ Shape class metadata                │
│   - vtable: [area(), perimeter()]   │ ← abstract slots
│   - field: color (offset)           │
│   - constructor: Shape(String)      │
├─────────────────────────────────────┤
│ Circle class metadata               │
│   - vtable: [area(), perimeter()]   │ ← concrete implementations
│   - field: radius (offset)          │
│   - constructor: Circle(String,d)   │
└─────────────────────────────────────┘
```

Key observations:
- **No Shape instance** exists in heap memory (cannot instantiate)
- Circle instances carry inherited fields directly
- Both Shape and Circle have separate metadata in the method area
- Virtual method dispatch resolves through the actual class's vtable

---

## 10. Syntax

### 10.1 Abstract Class Declaration

```java
public abstract class Animal {
    // Instance fields
    protected final String name;

    // Constructor
    protected Animal(String name) {
        this.name = Objects.requireNonNull(name);
    }

    // Abstract methods — no body
    public abstract void speak();
    public abstract String diet();

    // Concrete methods — with implementation
    public String getName() {
        return name;
    }

    public final void introduce() {
        System.out.println("I am " + name);
    }
}
```

### 10.2 Abstract Method Declaration

```java
// Abstract method — no body, no final, no private
public abstract ReturnType methodName(params);

// Examples:
public abstract double area();
protected abstract void processData(String input);
abstract boolean validate(); // package-private
```

### 10.3 Concrete Subclass

```java
public class Dog extends Animal {
    private final String breed;

    public Dog(String name, String breed) {
        super(name);
        this.breed = Objects.requireNonNull(breed);
    }

    @Override
    public void speak() {
        System.out.println("Woof!");
    }

    @Override
    public String diet() {
        return "Omnivore";
    }

    public String getBreed() {
        return breed;
    }
}
```

### 10.4 Usage

```java
public class Main {
    public static void main(String[] args) {
        // Animal a = new Animal("test"); // Compile error — abstract
        Animal dog = new Dog("Buddy", "Labrador");
        dog.speak();          // Woof!
        System.out.println(dog.diet()); // Omnivore
        dog.introduce();      // I am Buddy (inherited final method)
    }
}
```

---

## 11. Easy Example

### Shape Hierarchy

```java
public abstract class Shape {
    protected final String color;

    protected Shape(String color) {
        this.color = Objects.requireNonNull(color);
    }

    public abstract double area();
    public abstract double perimeter();

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[color=" + color + "]";
    }
}

public class Circle extends Shape {
    private final double radius;

    public Circle(String color, double radius) {
        super(color);
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be positive");
        }
        this.radius = radius;
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }

    @Override
    public double perimeter() {
        return 2 * Math.PI * radius;
    }
}

public class Rectangle extends Shape {
    private final double width;
    private final double height;

    public Rectangle(String color, double width, double height) {
        super(color);
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Dimensions must be positive");
        }
        this.width = width;
        this.height = height;
    }

    @Override
    public double area() {
        return width * height;
    }

    @Override
    public double perimeter() {
        return 2 * (width + height);
    }
}
```

### Usage

```java
public class ShapeDemo {
    public static void main(String[] args) {
        Shape circle = new Circle("Red", 5.0);
        Shape rect = new Rectangle("Blue", 4.0, 6.0);

        System.out.println(circle + " area=" + circle.area());
        System.out.println(rect + " area=" + rect.area());
    }
}
```

**Output:**
```
Circle[color=Red] area=78.53981633974483
Rectangle[color=Blue] area=24.0
```

---

## 12. Medium Example

### Notification System with Template Method

```java
public abstract class Notification {
    protected final String recipient;
    protected final String message;

    protected Notification(String recipient, String message) {
        this.recipient = Objects.requireNonNull(recipient);
        this.message = Objects.requireNonNull(message);
    }

    // Template method — final, defines the algorithm
    public final void send() {
        boolean authenticated = authenticate();
        if (authenticated) {
            String formatted = formatMessage();
            deliver(formatted);
            logDelivery();
        } else {
            logFailure("Authentication failed");
        }
    }

    // Abstract — subclasses decide how each step works
    protected abstract boolean authenticate();
    protected abstract String formatMessage();
    protected abstract void deliver(String formattedMessage);

    // Concrete — shared implementation
    protected void logDelivery() {
        System.out.println("Delivered to " + recipient);
    }

    protected void logFailure(String reason) {
        System.err.println("Failed for " + recipient + ": " + reason);
    }
}

public class EmailNotification extends Notification {
    private final String smtpServer;

    public EmailNotification(String recipient, String message, String smtpServer) {
        super(recipient, message);
        this.smtpServer = Objects.requireNonNull(smtpServer);
    }

    @Override
    protected boolean authenticate() {
        System.out.println("Authenticating with SMTP: " + smtpServer);
        return true;
    }

    @Override
    protected String formatMessage() {
        return "Subject: Notification\n\n" + message;
    }

    @Override
    protected void deliver(String formattedMessage) {
        System.out.println("Sending email via " + smtpServer);
        System.out.println(formattedMessage);
    }
}

public class SmsNotification extends Notification {
    private final String apiKey;

    public SmsNotification(String recipient, String message, String apiKey) {
        super(recipient, message);
        this.apiKey = Objects.requireNonNull(apiKey);
    }

    @Override
    protected boolean authenticate() {
        System.out.println("Authenticating with API key");
        return apiKey.startsWith("sk-");
    }

    @Override
    protected String formatMessage() {
        return message.length() > 160
            ? message.substring(0, 157) + "..."
            : message;
    }

    @Override
    protected void deliver(String formattedMessage) {
        System.out.println("SMS to " + recipient + ": " + formattedMessage);
    }
}
```

### Usage

```java
public class NotificationDemo {
    public static void main(String[] args) {
        Notification email = new EmailNotification(
            "user@example.com", "Your order shipped!", "smtp.example.com");
        Notification sms = new SmsNotification(
            "+1234567890", "Your OTP is 4829", "sk-abc123");

        email.send();
        System.out.println("---");
        sms.send();
    }
}
```

---

## 13. Hard Example

### Generic Abstract DAO with Unit of Work

```java
import java.util.*;

public abstract class AbstractRepository<T, ID> {
    protected final Map<ID, T> storage = new LinkedHashMap<>();
    protected final Class<T> entityClass;

    protected AbstractRepository(Class<T> entityClass) {
        this.entityClass = Objects.requireNonNull(entityClass);
    }

    // Template methods
    public final Optional<T> findById(ID id) {
        validateId(id);
        return Optional.ofNullable(storage.get(id));
    }

    public final List<T> findAll() {
        return List.copyOf(storage.values());
    }

    public final T save(T entity) {
        Objects.requireNonNull(entity, "Entity cannot be null");
        ID id = extractId(entity);
        validateForSave(entity);
        T saved = beforeSave(entity);
        storage.put(id, saved);
        afterSave(saved);
        return saved;
    }

    public final void delete(ID id) {
        validateId(id);
        T removed = storage.remove(id);
        if (removed != null) {
            afterDelete(removed);
        }
    }

    // Abstract hooks — subclasses implement
    protected abstract ID extractId(T entity);
    protected abstract void validateForSave(T entity);
    protected abstract T beforeSave(T entity);

    // Optional hooks — concrete with empty defaults
    protected void afterSave(T entity) { }
    protected void afterDelete(T entity) { }
    protected void validateId(ID id) {
        Objects.requireNonNull(id, "ID cannot be null");
    }

    // Utility
    public int count() {
        return storage.size();
    }
}

// Concrete implementation
public class InMemoryUserRepository extends AbstractRepository<User, UUID> {

    public InMemoryUserRepository() {
        super(User.class);
    }

    @Override
    protected UUID extractId(User user) {
        return user.getId();
    }

    @Override
    protected void validateForSave(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            throw new IllegalArgumentException("User name cannot be blank");
        }
    }

    @Override
    protected User beforeSave(User user) {
        if (user.getId() == null) {
            return new User(UUID.randomUUID(), user.getName(), user.getEmail());
        }
        return user;
    }
}

// Entity
public record User(UUID id, String name, String email) { }
```

### Usage

```java
public class DaoDemo {
    public static void main(String[] args) {
        AbstractRepository<User, UUID> repo = new InMemoryUserRepository();

        User saved = repo.save(new User(null, "Alice", "alice@example.com"));
        System.out.println("Saved: " + saved);
        System.out.println("Count: " + repo.count());

        repo.findById(saved.id()).ifPresent(u ->
            System.out.println("Found: " + u));
    }
}
```

---

## 14. Enterprise Example

### Payment Processing Pipeline

```java
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

// Abstract payment processor with Template Method
public abstract class PaymentProcessor {
    protected final String merchantId;

    protected PaymentProcessor(String merchantId) {
        this.merchantId = Objects.requireNonNull(merchantId);
    }

    // Template method — final, defines the workflow
    public final PaymentResult processPayment(PaymentRequest request) {
        validateRequest(request);
        FraudCheckResult fraudCheck = performFraudCheck(request);
        if (fraudCheck.isRejected()) {
            return PaymentResult.rejected(fraudCheck.reason());
        }

        AuthorizationResult authResult = authorize(request);
        if (!authResult.success()) {
            return PaymentResult.failed(authResult.errorMessage());
        }

        captureFunds(authResult, request);
        return PaymentResult.success(
            authResult.transactionId(),
            Instant.now()
        );
    }

    // Abstract steps — payment-provider-specific
    protected abstract FraudCheckResult performFraudCheck(PaymentRequest request);
    protected abstract AuthorizationResult authorize(PaymentRequest request);
    protected abstract void captureFunds(AuthorizationResult auth, PaymentRequest request);

    // Concrete shared logic
    protected void validateRequest(PaymentRequest request) {
        Objects.requireNonNull(request, "Payment request cannot be null");
        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    // Inner records
    public record PaymentRequest(
        String customerId, BigDecimal amount, String currency
    ) { }

    public record PaymentResult(
        boolean success, String transactionId, String errorMessage, Instant timestamp
    ) {
        static PaymentResult success(String txnId, Instant ts) {
            return new PaymentResult(true, txnId, null, ts);
        }
        static PaymentResult failed(String error) {
            return new PaymentResult(false, null, error, null);
        }
        static PaymentResult rejected(String reason) {
            return new PaymentResult(false, null, "Rejected: " + reason, null);
        }
    }

    public record FraudCheckResult(boolean rejected, String reason) {
        static FraudCheckResult passed() {
            return new FraudCheckResult(false, null);
        }
        static FraudCheckResult rejected(String reason) {
            return new FraudCheckResult(true, reason);
        }
    }

    public record AuthorizationResult(
        boolean success, String transactionId, String errorMessage
    ) { }
}

// Concrete: Stripe
public class StripePaymentProcessor extends PaymentProcessor {
    private final String apiKey;

    public StripePaymentProcessor(String merchantId, String apiKey) {
        super(merchantId);
        this.apiKey = Objects.requireNonNull(apiKey);
    }

    @Override
    protected FraudCheckResult performFraudCheck(PaymentRequest request) {
        System.out.println("[Stripe] Running fraud check via Radar API");
        return FraudCheckResult.passed();
    }

    @Override
    protected AuthorizationResult authorize(PaymentRequest request) {
        System.out.println("[Stripe] Authorizing charge: " + request.amount());
        return new AuthorizationResult(true, "txn_stripe_" + UUID.randomUUID(), null);
    }

    @Override
    protected void captureFunds(AuthorizationResult auth, PaymentRequest request) {
        System.out.println("[Stripe] Capturing funds for " + auth.transactionId());
    }
}

// Concrete: PayPal
public class PayPalPaymentProcessor extends PaymentProcessor {
    private final String clientId;

    public PayPalPaymentProcessor(String merchantId, String clientId) {
        super(merchantId);
        this.clientId = Objects.requireNonNull(clientId);
    }

    @Override
    protected FraudCheckResult performFraudCheck(PaymentRequest request) {
        System.out.println("[PayPal] Running fraud check");
        return FraudCheckResult.passed();
    }

    @Override
    protected AuthorizationResult authorize(PaymentRequest request) {
        System.out.println("[PayPal] Authorizing payment");
        return new AuthorizationResult(true, "pp_txn_" + UUID.randomUUID(), null);
    }

    @Override
    protected void captureFunds(AuthorizationResult auth, PaymentRequest request) {
        System.out.println("[PayPal] Capturing " + request.amount());
    }
}
```

### Usage

```java
import java.math.BigDecimal;

public class PaymentDemo {
    public static void main(String[] args) {
        PaymentProcessor stripe = new StripePaymentProcessor("M-001", "sk_live_xxx");
        PaymentProcessor paypal = new PayPalPaymentProcessor("M-001", "client-id");

        var req = new PaymentProcessor.PaymentRequest(
            "CUST-42", new BigDecimal("99.99"), "USD");

        PaymentProcessor.PaymentResult r1 = stripe.processPayment(req);
        System.out.println("Stripe: " + r1.success());

        PaymentProcessor.PaymentResult r2 = paypal.processPayment(req);
        System.out.println("PayPal: " + r2.success());
    }
}
```

---

## 15. Performance

### 15.1 Runtime Cost

- **No extra overhead**: Abstract classes perform identically to concrete classes at runtime. Virtual method dispatch (`invokevirtual`) is the same mechanism.
- **Method inlining**: The JVM's JIT compiler can inline concrete methods from abstract classes just like any other class.
- **Memory**: An abstract class adds no per-instance overhead compared to a regular class with the same fields.

### 15.2 Comparison

| Aspect | Abstract Class | Interface (pre-Java 8) | Interface (Java 8+) |
|--------|---------------|------------------------|---------------------|
| Method dispatch | `invokevirtual` | `invokeinterface` | `invokeinterface` |
| JIT inlining | Yes (concrete methods) | No (pre-8) | Yes (default methods) |
| Per-instance overhead | Fields only | None | None |
| vtable entry cost | Same as concrete class | Slightly more (indirect) | Slightly more |

### 15.3 When Performance Matters

- In tight loops with millions of calls, `invokevirtual` is marginally faster than `invokeinterface` because the JVM can more easily devirtualize it.
- For most applications, the difference is negligible. Choose based on design, not performance.

### 15.4 Best Practice

Prefer abstract classes when:
- You have shared state (instance fields)
- You want to lock down the algorithm (Template Method with `final`)
- You need constructors to enforce invariants

---

## 16. Best Practices

### 16.1 Prefer Composition for Code Sharing

```java
// Good — abstract class defines contract
public abstract class DataSource {
    public abstract byte[] read();
    public abstract void write(byte[] data);
}

// Avoid — abstract class used only for code sharing (use composition)
public abstract class AbstractLogger {
    public void log(String msg) { /* shared */ }
}
// Better: inject a LogFormatter instead
```

### 16.2 Keep Abstract Classes Minimal

```java
// Good — small, focused abstraction
public abstract class Repository<T> {
    public abstract Optional<T> findById(Object id);
    public abstract T save(T entity);
}

// Avoid — god abstract class with 20 abstract methods
```

### 16.3 Use `final` on Template Methods

```java
public abstract class DataPipeline {
    // Prevents subclasses from breaking the algorithm
    public final void execute() {
        extract();
        transform();
        load();
    }

    protected abstract void extract();
    protected abstract void transform();
    protected abstract void load();
}
```

### 16.4 Document Abstract Methods Clearly

```java
/**
 * Validates the entity before persistence.
 * Must throw IllegalArgumentException if invalid.
 */
protected abstract void validate(T entity);
```

### 16.5 Prefer Interfaces for Pure Contracts

Use interfaces when:
- You don't need shared state
- You want multiple inheritance
- You're defining a capability (e.g., `Comparable`, `Serializable`)

Use abstract classes when:
- You need shared implementation + state
- You want to enforce a constructor signature
- You're implementing the Template Method pattern

---

## 17. Common Mistakes

### Mistake 1: Forgetting `abstract` on the class

```java
// COMPILE ERROR — class has abstract method but isn't abstract
class Shape {
    public abstract double area(); // Error: abstract method in non-abstract class
}

// Fix:
abstract class Shape {
    public abstract double area();
}
```

### Mistake 2: Instantiating an abstract class

```java
abstract class Shape { }

Shape s = new Shape(); // Compile error: Shape is abstract; cannot be instantiated
```

### Mistake 3: Declaring an abstract method as `final` or `private`

```java
abstract class Shape {
    public final abstract double area(); // Error: illegal combination
    private abstract double area();       // Error: abstract method cannot be private
}
```

### Mistake 4: Not implementing all abstract methods

```java
abstract class Shape {
    public abstract double area();
    public abstract double perimeter();
}

class Circle extends Shape {
    public double area() { return 3.14; }
    // COMPILE ERROR: Circle is not abstract and does not override perimeter()
}
```

### Mistake 5: Using abstract when an interface suffices

```java
// Unnecessary abstract class — no state, no concrete methods
abstract class Printable {
    public abstract void print();
}

// Better:
interface Printable {
    void print();
}
```

---

## 18. Pitfalls

### 18.1 Fragile Base Class Problem

Changes to the abstract class can break subclasses:

```java
public abstract class Logger {
    public void log(String msg) {
        write(format(msg)); // Changed from write(msg)
    }
    protected abstract void write(String msg);
    protected String format(String msg) { return msg; }
}

public class ConsoleLogger extends Logger {
    @Override
    protected void write(String msg) {
        // Developer expected raw message, now gets formatted
        System.out.println(msg);
    }
}
```

**Mitigation**: Use `final` on template methods and document the contract clearly.

### 18.2 Constructor Calling Order

```java
public abstract class Base {
    protected Base() {
        System.out.println("Base constructor");
        callOverridable(); // DANGER — calls subclass before it's initialized
    }
    public abstract void init();
}

public class Child extends Base {
    private final int value;
    public Child(int value) {
        super(); // prints "Base constructor", then calls Child.init()
        this.value = value;
    }
    @Override
    public void init() {
        // value is 0 here, not the passed argument!
    }
}
```

**Mitigation**: Never call overridable methods from constructors.

### 18.3 Deep Hierarchies

```java
// Hard to follow and maintain
abstract class A { }
abstract class B extends A { }
abstract class C extends B { }
class D extends C { }

// Prefer: flat hierarchies with composition
```

### 18.4 Mixing Abstract Classes and Interfaces

Overuse of both in the same hierarchy creates confusion:

```java
// Confusing — which methods come from where?
abstract class AbstractAnimal implements Soundable, Movable {
    // ...
}
```

**Mitigation**: Use abstract classes for shared state, interfaces for capabilities.

---

## 19. Debugging Tips

### 19.1 Verify Abstract Methods Are Implemented

```bash
# Compile and check for missing implementations
javac -d out src/**/*.java
# Error will clearly state which method is missing
```

### 19.2 Use `-verbose` to Inspect Class Structure

```bash
javap -verbose -p Shape.class | grep -A2 "abstract"
```

### 19.3 Runtime Type Checking

```java
// Check if a class is abstract at runtime
if (Modifier.isAbstract(shape.getClass().getModifiers())) {
    System.out.println("Cannot instantiate abstract class");
}
```

### 19.4 Breakpoint in Constructor

When debugging the constructor ordering issue:
1. Set a breakpoint in the abstract class constructor
2. Step into the overridable method call
3. Observe the subclass fields are still `null`/default

### 19.5 Check Subclass Coverage

```java
// Debug helper to verify all abstract methods are overridden
public static void checkImplementation(Class<?> clazz) {
    for (Method m : clazz.getDeclaredMethods()) {
        if (Modifier.isAbstract(m.getModifiers())) {
            System.out.println("WARNING: " + m.getName() + " not implemented");
        }
    }
}
```

---

## 20. Comparison Table (Abstract Class vs Interface)

| Feature | Abstract Class | Interface |
|---------|---------------|-----------|
| **Instantiation** | No | No |
| **Constructors** | Yes | No |
| **Instance fields** | Yes (any modifier) | Only `public static final` |
| **Static fields** | Yes | Yes (`public static final`) |
| **Instance methods** | Yes (concrete or abstract) | Only `default` or `static` |
| **Static methods** | Yes | Yes |
| **Multiple inheritance** | No (single `extends`) | Yes (multiple `implements`) |
| **Access modifiers** | Any | `public` for methods, `public static final` for fields |
| **Keyword** | `abstract class` | `interface` |
| **Inheritance** | `extends` | `implements` |
| **Purpose** | Shared code + contract | Pure contract / capability |
| **State** | Yes (instance fields) | No (constants only) |
| **Sealed** | Yes (Java 17+) | Yes (Java 17+) |
| **Records** | N/A | N/A |

---

## 21. Decision Tree

```
Do you need shared state (instance fields)?
├── YES → Abstract Class
└── NO
    │
    Do you need a constructor to enforce invariants?
    ├── YES → Abstract Class
    └── NO
        │
        Do you need multiple inheritance?
        ├── YES → Interface
        └── NO
            │
            Do you have a template algorithm to enforce?
            ├── YES → Abstract Class (Template Method)
            └── NO
                │
                Is it a pure capability/contract?
                ├── YES → Interface
                └── NO → Consider both; prefer the simpler option
```

### Quick Reference

| Scenario | Recommendation |
|----------|---------------|
| Shared state + contract | Abstract class |
| Pure capability (e.g., `Comparable`) | Interface |
| Template Method pattern | Abstract class |
| Multiple implementations of same behavior | Interface |
| Need `protected` methods | Abstract class |
| Functional interface (one abstract method) | Interface |
| Sealed hierarchy (Java 17+) | Interface |

---

## 22. Interview Questions

### Q1: Can an abstract class have a constructor?
**Answer**: Yes. Abstract class constructors are called by subclass constructors via `super()`. They initialize fields inherited by the subclass.

### Q2: Can an abstract class have a `main` method?
**Answer**: Yes. An abstract class can contain `public static void main(String[] args)`. It cannot be run directly (since you cannot instantiate it), but it can be invoked as `java AbstractClass` if it has a main method and the class is accessible. However, it is uncommon.

### Q3: Can an abstract method be `synchronized`?
**Answer**: No. An abstract method cannot have `synchronized` because synchronization is a method implementation detail, and abstract methods have no body. The `synchronized` keyword belongs on concrete method implementations.

### Q4: What happens if an abstract class implements an interface but doesn't implement all methods?
**Answer**: The abstract class does not need to implement the interface methods. The responsibility is deferred to the first concrete subclass. If the concrete subclass also doesn't implement them, it must be declared `abstract`.

### Q5: Abstract class vs interface — which is better?
**Answer**: Neither is universally better. Use abstract classes when you need shared state, constructors, or the Template Method pattern. Use interfaces when you need multiple inheritance or are defining a pure contract. Many well-designed systems use both.

### Q6: Can an abstract class extend a concrete class?
**Answer**: Yes. An abstract class can extend any class (concrete or abstract). This allows you to add abstract methods to an existing class hierarchy.

### Q7: Can you have an abstract class with no abstract methods?
**Answer**: Yes. This is called a "marker abstract class." It prevents instantiation while providing concrete methods. Example: `abstract class NonInstantiable { }`.

---

## 23. Exercises

### Exercise 1: Vehicle Hierarchy

Create an abstract `Vehicle` class with:
- Fields: `make`, `model`, `year`
- Abstract methods: `start()`, `stop()`
- Concrete methods: `getInfo()` returning a formatted string
- Subclasses: `Car`, `Truck`

### Exercise 2: File Parser

Create an abstract `FileParser<T>` class:
- Generic type for parsed output
- Template method: `parse(File file)` that calls `read()`, `validate()`, `transform()`
- Implementations: `CsvParser`, `JsonParser`

### Exercise 3: Logger Hierarchy

Create an abstract `Logger` class:
- Fields: `minLevel`, `name`
- Abstract method: `write(String message)`
- Concrete methods: `info()`, `warn()`, `error()` that check level then call `write()`
- Implementations: `FileLogger`, `ConsoleLogger`

### Exercise 4: Template Method — Order Processing

```java
public abstract class OrderProcessor {
    public final void process(Order order) {
        validate(order);
        chargePayment(order);
        shipItems(order);
        sendConfirmation(order);
    }
    protected abstract void validate(Order order);
    protected abstract void chargePayment(Order order);
    protected abstract void shipItems(Order order);
    protected void sendConfirmation(Order order) {
        System.out.println("Order " + order.id() + " confirmed");
    }
}
```

Implement `StandardOrderProcessor` and `ExpressOrderProcessor` (skips validation for express).

### Exercise 5: Generic Repository

Implement `AbstractRepository<T, ID>` with:
- `findById`, `findAll`, `save`, `delete`
- Subclass-specific `extractId` and `validate` hooks

---

## 24. Assignments

### Assignment 1: Bank Account System

Design a bank account hierarchy:

```
Account (abstract)
├── SavingsAccount
├── CheckingAccount
└── FixedDepositAccount
```

Requirements:
- Abstract class with `accountNumber`, `balance`, `owner`
- Abstract methods: `calculateInterest()`, `withdraw(BigDecimal amount)`
- Concrete methods: `deposit()`, `getBalance()`
- Each account type has different interest calculation and withdrawal rules

### Assignment 2: HTTP Client Abstraction

Create an abstract `HttpClient` class:

```java
public abstract class HttpClient {
    protected abstract HttpResponse execute(HttpRequest request);
    protected abstract void configureConnection();
    public final HttpResponse get(String url) { ... }
    public final HttpResponse post(String url, String body) { ... }
}
```

Implement `ApacheHttpClient` and `JavaHttpClient`.

### Assignment 3: Game Entity System

Create a game entity hierarchy:

```
Entity (abstract)
├── Character (abstract)
│   ├── Player
│   └── Enemy (abstract)
│       ├── Goblin
│       └── Dragon
└── Item
```

Requirements:
- Entity: `position`, `health`, abstract `update()`, `render()`
- Character: `attack()`, `takeDamage()`
- Enemy: abstract `getDifficulty()`
- Player: `level`, `experience`

---

## 25. Mini Project: Payment Gateway Abstraction

### Project Structure

```
payment-gateway/
├── src/main/java/
│   └── com/example/payment/
│       ├── model/
│       │   ├── PaymentRequest.java
│       │   ├── PaymentResult.java
│       │   └── RefundResult.java
│       ├── processor/
│       │   ├── PaymentProcessor.java (abstract)
│       │   ├── StripePaymentProcessor.java
│       │   ├── RazorpayPaymentProcessor.java
│       │   └── MockPaymentProcessor.java (for testing)
│       ├── fraud/
│       │   ├── FraudDetector.java (abstract)
│       │   ├── BasicFraudDetector.java
│       │   └── AdvancedFraudDetector.java
│       └── pipeline/
│           └── PaymentPipeline.java
```

### Key Classes

```java
// Abstract processor with Template Method
public abstract class PaymentProcessor {
    protected final String merchantId;

    protected PaymentProcessor(String merchantId) {
        this.merchantId = Objects.requireNonNull(merchantId);
    }

    public final PaymentResult process(PaymentRequest request) {
        validate(request);
        FraudResult fraud = checkFraud(request);
        if (fraud.rejected()) {
            return PaymentResult.rejected(fraud.reason());
        }
        AuthorizationResult auth = authorize(request);
        if (!auth.success()) {
            return PaymentResult.failed(auth.error());
        }
        confirm(auth, request);
        return PaymentResult.success(auth.transactionId());
    }

    protected abstract void validate(PaymentRequest request);
    protected abstract FraudResult checkFraud(PaymentRequest request);
    protected abstract AuthorizationResult authorize(PaymentRequest request);
    protected abstract void confirm(AuthorizationResult auth, PaymentRequest request);
}
```

### Requirements

1. Implement `StripePaymentProcessor` and `RazorpayPaymentProcessor`
2. Implement `MockPaymentProcessor` for unit testing
3. Create a `PaymentPipeline` that tries processors in order (fallback pattern)
4. Write unit tests for each processor
5. Demonstrate polymorphism: store multiple processors in a `List<PaymentProcessor>`

---

## 26. Summary

| Concept | Key Takeaway |
|---------|-------------|
| **Abstract class** | Cannot be instantiated; provides partial implementation |
| **Abstract method** | No body; forces subclasses to implement |
| **Concrete methods** | Shared logic; can be `final` to prevent override |
| **Constructors** | Initialize inherited state; called via `super()` |
| **Template Method** | `final` method defining algorithm; abstract steps for variation |
| **vs Interface** | Abstract class = shared state + contract; Interface = pure contract |
| **Single inheritance** | Java allows only one abstract class per hierarchy |
| **Use when** | You need shared state, constructors, or algorithm enforcement |

### Key Rules

1. A class with any abstract method **must** be declared `abstract`
2. Abstract methods **cannot** be `final` or `private`
3. Concrete subclasses **must** implement all inherited abstract methods
4. Abstract classes **can** have constructors, fields, and any access modifier
5. Use `final` on template methods to lock down the algorithm

---

## 27. References

- **Effective Java**, 3rd Edition — Joshua Bloch, Item 20: "Prefer interfaces to abstract classes"
- **Head First Design Patterns** — Template Method pattern
- **Java Language Specification** — [§8.1.1.1 abstract Classes](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.1.1.1)
- **Oracle Tutorials** — [Abstract Classes and Methods](https://docs.oracle.com/javase/tutorial/java/IandI/abstract.html)
- **Design Patterns: Elements of Reusable OO Software** — GoF, Template Method pattern
- **Clean Code** — Robert C., Chapter 10: Classes (small, focused abstractions)
- **Java 21 JEPs** — [JEP 477: Implicitly Declared Classes](https://openjdk.org/jeps/477) (no impact on abstract classes)
