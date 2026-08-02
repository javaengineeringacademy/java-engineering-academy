# Sealed Classes

## Introduction

Sealed classes (Java 17+) restrict which classes can extend or implement them. This provides precise control over inheritance hierarchies, enabling better reasoning about code and improving pattern matching capabilities.

## Learning Objectives

By the end of this topic, you will be able to:

- Understand why sealed classes exist and when to use them
- Declare sealed classes and interfaces with `permits`
- Use `final`, `non-sealed`, and `sealed` modifiers on permitted subclasses
- Combine sealed classes with pattern matching
- Apply sealed classes in domain modeling

## Prerequisites

- Classes and Objects
- Inheritance
- Interfaces
- Abstract Classes

## Why This Concept Exists

### The Problem

In traditional Java inheritance, any class can extend any non-final class. This makes it impossible to:

- Reason about all possible subtypes at compile time
- Guarantee exhaustive pattern matching
- Control the hierarchy for domain integrity

### The Solution

Sealed classes restrict which classes can extend them, enabling:

- **Exhaustive checking**: Compiler knows all possible subtypes
- **Better pattern matching**: Switch expressions can be exhaustive
- **Domain modeling**: Prevent invalid hierarchies
- **Security**: Control who can extend your classes

### Real-World Analogy

Think of a sealed class as a "members-only club":

- The club (sealed class) decides who can join (extend it)
- New members must be explicitly approved (permits)
- The club knows exactly who its members are
- This enables the club to make decisions based on complete membership

## Internal Working

### How Sealed Classes Work

1. **Declaration**: Sealed class lists permitted subclasses
2. **Permitted classes**: Must be in the same module or package
3. **Modifiers**: Each permitted class must be `final`, `sealed`, or `non-sealed`
4. **Compiler check**: Ensures all permitted classes are accounted for

### Module System Integration

Sealed classes work with the Java module system:

```java
module com.example {
    exports com.example.shapes;
    // Sealed class must be in same module or exported package
}
```

## Memory Representation

Sealed classes don't affect memory layout. The JVM sees them as regular classes with metadata about permitted subtypes.

## Syntax

### Basic Sealed Class

```java
public sealed class Shape permits Circle, Rectangle, Triangle {
    // Common fields and methods
}
```

### Sealed Interface

```java
public sealed interface Payment permits CreditCardPayment, PayPalPayment, CryptoPayment {
    void process(double amount);
}
```

### Permitted Class Modifiers

```java
// Final - cannot be extended further
public final class Circle extends Shape {
    // Implementation
}

// Sealed - can be extended but with restrictions
public sealed class Polygon extends Shape permits Square, Pentagon {
    // Implementation
}

// Non-sealed - can be extended by anyone
public non-sealed class Triangle extends Shape {
    // Implementation
}
```

### With Records

```java
public sealed interface Shape permits Circle, Rectangle {
    double area();
}

public record Circle(double radius) implements Shape {
    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
}

public record Rectangle(double width, double height) implements Shape {
    @Override
    public double area() {
        return width * height;
    }
}
```

## Easy Examples

### Example 1: Basic Sealed Class

**Problem Statement**: Create a sealed class hierarchy for different animal types.

**Approach**: Define a sealed Animal class with permitted subclasses for specific animal types.

**Implementation**:

```java
// Sealed class - restricts who can extend it
public sealed class Animal permits Dog, Cat, Bird {
    private final String name;
    
    protected Animal(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public abstract String sound();
}

// Final - cannot be extended
public final class Dog extends Animal {
    public Dog(String name) {
        super(name);
    }
    
    @Override
    public String sound() {
        return "Woof!";
    }
}

// Final - cannot be extended
public final class Cat extends Animal {
    public Cat(String name) {
        super(name);
    }
    
    @Override
    public String sound() {
        return "Meow!";
    }
}

// Non-sealed - can be extended by anyone
public non-sealed class Bird extends Animal {
    public Bird(String name) {
        super(name);
    }
    
    @Override
    public String sound() {
        return "Tweet!";
    }
}
```

**Code Walkthrough**:
1. `Animal` is sealed - only Dog, Cat, and Bird can extend it
2. `Dog` and `Cat` are final - no further extension allowed
3. `Bird` is non-sealed - anyone can extend it

**Output**:
```java
Animal dog = new Dog("Rex");
Animal cat = new Cat("Whiskers");
Animal bird = new Bird("Tweety");

System.out.println(dog.getName() + ": " + dog.sound());  // Rex: Woof!
System.out.println(cat.getName() + ": " + cat.sound());  // Whiskers: Meow!
System.out.println(bird.getName() + ": " + bird.sound()); // Tweety: Tweet!
```

**Complexity**: O(1) for all operations

**Best Practices**:
- Use `final` when you don't need further extension
- Use `non-sealed` sparingly - it breaks the sealed guarantee
- Prefer sealed interfaces for flexibility

### Example 2: Sealed Interface with Records

**Problem Statement**: Create a payment system using sealed interface and records.

**Implementation**:

```java
public sealed interface Payment permits CreditCardPayment, PayPalPayment, BankTransfer {
    double amount();
    String currency();
    
    default String summary() {
        return String.format("Payment: %.2f %s via %s", 
            amount(), currency(), paymentMethod());
    }
    
    String paymentMethod();
}

public record CreditCardPayment(
    double amount, 
    String currency, 
    String cardNumber
) implements Payment {
    
    @Override
    public String paymentMethod() {
        return "Credit Card";
    }
    
    @Override
    public String summary() {
        return String.format("Credit Card Payment: %.2f %s (****%s)", 
            amount(), currency(), cardNumber.substring(cardNumber.length() - 4));
    }
}

public record PayPalPayment(
    double amount, 
    String currency, 
    String email
) implements Payment {
    
    @Override
    public String paymentMethod() {
        return "PayPal";
    }
}

public record BankTransfer(
    double amount, 
    String currency, 
    String accountNumber
) implements Payment {
    
    @Override
    public String paymentMethod() {
        return "Bank Transfer";
    }
}
```

**Output**:
```java
Payment creditCard = new CreditCardPayment(100.00, "USD", "4111111111111234");
Payment paypal = new PayPalPayment(50.00, "EUR", "user@example.com");
Payment bankTransfer = new BankTransfer(200.00, "GBP", "1234567890");

System.out.println(creditCard.summary()); // Credit Card Payment: 100.00 USD (****1234)
System.out.println(paypal.summary());     // Payment: 50.00 EUR via PayPal
System.out.println(bankTransfer.summary()); // Payment: 200.00 GBP via Bank Transfer
```

## Medium Examples

### Example 3: Exhaustive Pattern Matching

**Problem Statement**: Use sealed classes with pattern matching for exhaustive switch expressions.

**Implementation**:

```java
public sealed interface Result<T> permits Success, Failure, Loading {
}

public record Success<T>(T data) implements Result<T> {}
public record Failure<T>(String error, Exception cause) implements Result<T> {}
public record Loading<T>() implements Result<T> {}

public class ResultProcessor {
    
    // Exhaustive switch - compiler ensures all cases are handled
    public static <T> String processResult(Result<T> result) {
        return switch (result) {
            case Success<T> success -> "Success: " + success.data();
            case Failure<T> failure -> "Error: " + failure.error();
            case Loading<T> loading -> "Loading...";
            // No default needed - all cases covered!
        };
    }
    
    // Pattern matching with guards
    public static <T> String processWithGuard(Result<T> result) {
        return switch (result) {
            case Success<T> s when s.data() == null -> "Success with null data";
            case Success<T> s -> "Success: " + s.data();
            case Failure<T> f -> "Error: " + f.error();
            case Loading<T> _ -> "Loading...";
        };
    }
}
```

**Output**:
```java
Result<String> success = new Success<>("Hello");
Result<String> failure = new Failure<>("Network error", new IOException());
Result<String> loading = new Loading<>();

System.out.println(ResultProcessor.processResult(success));  // Success: Hello
System.out.println(ResultProcessor.processResult(failure));  // Error: Network error
System.out.println(ResultProcessor.processResult(loading));  // Loading...
```

### Example 4: Domain Modeling with Sealed Classes

**Problem Statement**: Model an order status system with sealed classes.

**Implementation**:

```java
public sealed interface OrderStatus permits Pending, Confirmed, Shipped, Delivered, Cancelled {
    String description();
    boolean canBeModified();
}

public record Pending(Instant createdAt) implements OrderStatus {
    @Override
    public String description() {
        return "Order is pending confirmation";
    }
    
    @Override
    public boolean canBeModified() {
        return true;
    }
}

public record Confirmed(Instant confirmedAt, String confirmationCode) implements OrderStatus {
    @Override
    public String description() {
        return "Order confirmed with code: " + confirmationCode;
    }
    
    @Override
    public boolean canBeModified() {
        return true;
    }
}

public record Shipped(Instant shippedAt, String trackingNumber) implements OrderStatus {
    @Override
    public String description() {
        return "Order shipped, tracking: " + trackingNumber;
    }
    
    @Override
    public boolean canBeModified() {
        return false;
    }
}

public record Delivered(Instant deliveredAt) implements OrderStatus {
    @Override
    public String description() {
        return "Order delivered";
    }
    
    @Override
    public boolean canBeModified() {
        return false;
    }
}

public record Cancelled(Instant cancelledAt, String reason) implements OrderStatus {
    @Override
    public String description() {
        return "Order cancelled: " + reason;
    }
    
    @Override
    public boolean canBeModified() {
        return false;
    }
}

// Order class using sealed status
public class Order {
    private final String id;
    private OrderStatus status;
    
    public Order(String id) {
        this.id = id;
        this.status = new Pending(Instant.now());
    }
    
    public void confirm(String confirmationCode) {
        if (status instanceof Pending) {
            this.status = new Confirmed(Instant.now(), confirmationCode);
        } else {
            throw new IllegalStateException("Order cannot be confirmed from current status");
        }
    }
    
    public void ship(String trackingNumber) {
        if (status instanceof Confirmed) {
            this.status = new Shipped(Instant.now(), trackingNumber);
        } else {
            throw new IllegalStateException("Order cannot be shipped from current status");
        }
    }
    
    public void deliver() {
        if (status instanceof Shipped) {
            this.status = new Delivered(Instant.now());
        } else {
            throw new IllegalStateException("Order cannot be delivered from current status");
        }
    }
    
    public String getStatusDescription() {
        return switch (status) {
            case Pending p -> p.description();
            case Confirmed c -> c.description();
            case Shipped s -> s.description();
            case Delivered d -> d.description();
            case Cancelled c -> c.description();
        };
    }
}
```

**Output**:
```java
Order order = new Order("ORD-001");
System.out.println(order.getStatusDescription()); // Order is pending confirmation

order.confirm("CONF-123");
System.out.println(order.getStatusDescription()); // Order confirmed with code: CONF-123

order.ship("TRACK-456");
System.out.println(order.getStatusDescription()); // Order shipped, tracking: TRACK-456

order.deliver();
System.out.println(order.getStatusDescription()); // Order delivered
```

## Hard Examples

### Example 5: Sealed Class with Generics and State Machines

**Problem Statement**: Implement a generic state machine using sealed classes.

**Implementation**:

```java
public sealed interface State<S, A> permits Idle, Processing, Success, Error {
    
    State<S, A> transition(A action);
    
    S currentState();
}

public record Idle<S, A>(S state) implements State<S, A> {
    
    @Override
    public State<S, A> transition(A action) {
        // Transition from Idle to Processing
        return new Processing<>(state, action);
    }
    
    @Override
    public S currentState() {
        return state;
    }
}

public record Processing<S, A>(S state, A action) implements State<S, A> {
    
    @Override
    public State<S, A> transition(A action) {
        // Processing can only transition to Success or Error
        throw new IllegalStateException("Processing cannot accept new actions");
    }
    
    public State<S, A> complete(S newState) {
        return new Success<>(newState);
    }
    
    public State<S, A> fail(String error) {
        return new Error<>(state, error);
    }
    
    @Override
    public S currentState() {
        return state;
    }
}

public record Success<S, A>(S state) implements State<S, A> {
    
    @Override
    public State<S, A> transition(A action) {
        throw new IllegalStateException("Success is a terminal state");
    }
    
    @Override
    public S currentState() {
        return state;
    }
}

public record Error<S, A>(S state, String error) implements State<S, A> {
    
    @Override
    public State<S, A> transition(A action) {
        // Error can transition back to Idle for retry
        return new Idle<>(state);
    }
    
    @Override
    public S currentState() {
        return state;
    }
}

// Generic state machine
public class StateMachine<S, A> {
    private State<S, A> currentState;
    
    public StateMachine(S initialState) {
        this.currentState = new Idle<>(initialState);
    }
    
    public void process(A action) {
        currentState = currentState.transition(action);
    }
    
    public String getStatus() {
        return switch (currentState) {
            case Idle<S, A> i -> "IDLE: " + i.currentState();
            case Processing<S, A> p -> "PROCESSING: " + p.currentState();
            case Success<S, A> s -> "SUCCESS: " + s.currentState();
            case Error<S, A> e -> "ERROR: " + e.error();
        };
    }
}
```

**Output**:
```java
StateMachine<String, String> machine = new StateMachine<>("initial");
System.out.println(machine.getStatus()); // IDLE: initial

machine.process("start");
System.out.println(machine.getStatus()); // PROCESSING: initial
```

**Complexity**: O(1) for state transitions

**Best Practices**:
- Use sealed classes for state machines to guarantee exhaustive handling
- Combine with records for immutable state representations
- Use pattern matching for clean state handling

## Exercises

### Easy

1. **Shape Hierarchy**: Create a sealed Shape class with Circle, Rectangle, and Triangle subclasses. Add an `area()` method to each.

2. **Payment Types**: Create a sealed Payment interface with CreditCard, PayPal, and BankTransfer implementations using records.

### Medium

3. **Result Type**: Implement a generic Result<T> sealed interface with Success, Failure, and Loading states. Add methods to handle each case.

4. **Order Status**: Extend the Order example to include refund and return states.

### Hard

5. **State Machine**: Implement a generic state machine for a vending machine with states: Idle, HasMoney, Dispensing, and SoldOut.

6. **Parser**: Create a sealed AST (Abstract Syntax Tree) for a simple expression parser with Number, Add, Subtract, Multiply, and Divide nodes.

## Interview Questions

### Beginner

1. **What is a sealed class?**
   A sealed class is a class that restricts which other classes can extend it. It uses the `permits` keyword to list allowed subclasses.

2. **What modifiers can permitted classes use?**
   - `final`: Cannot be extended further
   - `sealed`: Can be extended but with its own restrictions
   - `non-sealed`: Can be extended by anyone

3. **Can sealed classes be interfaces?**
   Yes, interfaces can also be sealed.

### Intermediate

4. **What is the benefit of sealed classes?**
   - Exhaustive pattern matching (compiler knows all subtypes)
   - Better domain modeling (prevents invalid hierarchies)
   - Improved security (controls who can extend)
   - Better documentation (explicit hierarchy)

5. **Can permitted classes be in different packages?**
   Yes, but they must be in the same module or the package must be exported.

6. **How do sealed classes work with records?**
   Records can implement sealed interfaces, providing immutable implementations with automatic equals/hashCode/toString.

### Senior

7. **How do sealed classes interact with the module system?**
   Sealed classes work within module boundaries. The permitted classes must be accessible from the module where the sealed class is defined.

8. **Can you have a sealed class with no permitted classes?**
   Yes, but it would be equivalent to a final class with no subclasses.

9. **How do sealed classes affect serialization?**
   The Java serialization mechanism handles sealed classes, but you must ensure permitted classes are also serializable if needed.

### Architecture

10. **When would you use sealed classes over enums?**
    Use sealed classes when you need:
    - Complex behavior in each subtype
    - Different fields per subtype
    - Extensibility (non-sealed)
    - Generic type parameters

11. **How do sealed classes support domain-driven design?**
    Sealed classes enable precise domain modeling by:
    - Enforcing business rules in the type system
    - Making invalid states unrepresentable
    - Providing exhaustive handling of all cases

12. **How would you design a plugin system using sealed classes?**
    Use a sealed class for the plugin interface with non-sealed permitted classes for extension points.

### Scenario

13. **You need to model different notification types (Email, SMS, Push). How would you use sealed classes?**

14. **You're building a compiler and need to represent AST nodes. How would sealed classes help?**

15. **You have a legacy system with many subclasses. How would you migrate to sealed classes?**

### Coding

16. **Implement a sealed interface for different discount types (Percentage, Fixed, BuyOneGetOneFree).**

17. **Create a sealed class hierarchy for file system nodes (File, Directory, Symlink).**

18. **Design a sealed interface for game events (PlayerJoined, PlayerLeft, GameStarted, GameEnded).**

### Production

19. **How would you handle versioning of sealed class hierarchies in a library?**

20. **What happens if you add a new permitted class to a sealed class in a published library?**

### Debugging

21. **Why am I getting "class is not allowed here" when extending a sealed class?**

22. **How do I handle the case where a sealed class has many permitted classes in a switch?**

## Common Pitfalls

### 1. Using `non-sealed` Defeats the Purpose

**Wrong**:
```java
public sealed class Shape permits Circle, Rectangle, Triangle {}
public non-sealed class Circle extends Shape {} // Anyone can extend Circle
public non-sealed class Rectangle extends Shape {}
public non-sealed class Triangle extends Shape {}
```

**Right**:
```java
public sealed class Shape permits Circle, Rectangle, Triangle {}
public final class Circle extends Shape {}
public final class Rectangle extends Shape {}
public final class Triangle extends Shape {}
```

### 2. Forgetting Exhaustive Handling

**Wrong**:
```java
public String describe(Shape shape) {
    if (shape instanceof Circle) {
        return "Circle";
    } else if (shape instanceof Rectangle) {
        return "Rectangle";
    }
    // Missing Triangle case!
    return "Unknown";
}
```

**Right**:
```java
public String describe(Shape shape) {
    return switch (shape) {
        case Circle c -> "Circle";
        case Rectangle r -> "Rectangle";
        case Triangle t -> "Triangle";
        // All cases covered - no default needed
    };
}
```

### 3. Not Using Records with Sealed Interfaces

**Wrong**:
```java
public sealed interface Result permits Success, Failure {
}
public class Success implements Result {
    private final Object data;
    // Manual constructor, getters, equals, hashCode, toString
}
```

**Right**:
```java
public sealed interface Result permits Success, Failure {
}
public record Success(Object data) implements Result {}
public record Failure(String error) implements Result {}
```

## Best Practices

### 1. Prefer `final` Over `non-sealed`

Use `final` when you don't need further extension. This maintains the sealed guarantee.

### 2. Use Records for Immutable Subtypes

Records provide automatic implementations of equals, hashCode, toString, and are immutable by default.

### 3. Combine with Pattern Matching

Use switch expressions with sealed classes for exhaustive handling without default cases.

### 4. Document the Hierarchy

Add Javadoc to sealed classes explaining why the hierarchy is sealed and what each permitted class represents.

### 5. Consider Module Boundaries

Sealed classes work best within module boundaries. Consider module structure when designing sealed hierarchies.

## Real World Usage

### JDK Usage

The JDK uses sealed classes in several places:

```java
// java.lang.constant.ConstantDesc (sealed interface)
public sealed interface ConstantDesc 
    permits ClassDesc, MethodHandleDesc, MethodTypeDesc, DynamicConstantDesc, ConstantBootstraps {
}
```

### Spring Framework

Spring uses sealed classes for internal type hierarchies:

```java
// Spring's internal use (simplified)
public sealed interface BeanDefinition permits RootBeanDefinition, ScannedGenericBeanDefinition {
}
```

### Pattern Matching

Sealed classes enable exhaustive pattern matching:

```java
// Java 21 pattern matching with sealed classes
public sealed interface Shape permits Circle, Rectangle {
}

public static double area(Shape shape) {
    return switch (shape) {
        case Circle c -> Math.PI * c.radius() * c.radius();
        case Rectangle r -> r.width() * r.height();
    };
}
```

## Summary

Sealed classes provide precise control over inheritance hierarchies in Java. Key takeaways:

- **Purpose**: Restrict which classes can extend a class or implement an interface
- **Benefits**: Exhaustive pattern matching, better domain modeling, improved security
- **Modifiers**: `final`, `sealed`, `non-sealed` for permitted classes
- **Best with**: Records, pattern matching, module system
- **Use cases**: State machines, domain modeling, compiler ASTs, plugin systems
- **When to use**: When you need to control the hierarchy and enable exhaustive handling

**Next Steps**: Learn about enums for fixed constant sets, or pattern matching for advanced type-based dispatch.

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                    Sealed Class Hierarchy                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   ┌─────────────────────┐                                       │
│   │   sealed class       │                                       │
│   │      Shape           │                                       │
│   │  ┌───────────────┐  │                                       │
│   │  │ permits        │  │                                       │
│   │  │ Circle, Rect,  │  │                                       │
│   │  │ Triangle       │  │                                       │
│   │  └───────┬───────┘  │                                       │
│   └──────────┼──────────┘                                       │
│              │                                                   │
│   ┌──────────┼──────────┬──────────────┐                        │
│   │          │          │              │                        │
│   ▼          ▼          ▼              ▼                        │
│ ┌──────┐ ┌──────┐ ┌──────────┐ ┌────────────┐                  │
│ │final │ │final │ │non-sealed│ │sealed      │                  │
│ │Circle│ │Rect  │ │Triangle  │ │Polygon     │                  │
│ └──────┘ └──────┘ └──────────┘ └─────┬──────┘                  │
│                                       │                         │
│                            ┌──────────┼──────────┐              │
│                            │                     │              │
│                            ▼                     ▼              │
│                      ┌──────────┐         ┌──────────┐         │
│                      │final     │         │final     │         │
│                      │Square    │         │Pentagon  │         │
│                      └──────────┘         └──────────┘         │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘

Component Relationships:
─────────────────────────────────────────────────────────────────
  Sealed Class ──permits──▶ Subtype 1 (final)
                 │
                 ├──permits──▶ Subtype 2 (sealed) ──permits──▶ Sub-subtype
                 │
                 └──permits──▶ Subtype 3 (non-sealed)
─────────────────────────────────────────────────────────────────
```

## Flow Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                Sealed Class Processing Flow                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   ┌──────────────┐                                              │
│   │  Define       │                                              │
│   │  Sealed Class │                                              │
│   └──────┬───────┘                                              │
│          │                                                       │
│          ▼                                                       │
│   ┌──────────────┐     ┌─────────────────┐                      │
│   │ List         │────▶│ Verify          │                      │
│   │ Permits      │     │ Same Module     │                      │
│   └──────────────┘     └────────┬────────┘                      │
│                                 │                                │
│                    ┌────────────┴────────────┐                   │
│                    │                         │                   │
│                    ▼                         ▼                   │
│           ┌──────────────┐         ┌──────────────┐             │
│           │  Pass        │         │  Fail        │             │
│           │  Compilation │         │  Compilation │             │
│           └──────┬───────┘         └──────────────┘             │
│                  │                                               │
│                  ▼                                               │
│   ┌──────────────────────────────────────────┐                  │
│   │         Process Subtypes                  │                  │
│   └──────────────────┬───────────────────────┘                  │
│                      │                                          │
│         ┌────────────┼────────────┬────────────┐                │
│         │            │            │            │                │
│         ▼            ▼            ▼            ▼                │
│   ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐         │
│   │  final   │ │ sealed   │ │non-sealed│ │  record  │         │
│   │ subclass │ │ subclass │ │ subclass │ │ subtype  │         │
│   └────┬─────┘ └────┬─────┘ └────┬─────┘ └────┬─────┘         │
│        │            │            │            │                  │
│        ▼            ▼            ▼            ▼                  │
│   ┌──────────────────────────────────────────────────────┐     │
│   │          Exhaustive Pattern Matching                  │     │
│   │   switch (sealed) {                                    │     │
│   │       case SubType1 s1 -> ...                         │     │
│   │       case SubType2 s2 -> ...                         │     │
│   │       // No default needed - all cases covered        │     │
│   │   }                                                    │     │
│   └──────────────────────────────────────────────────────┘     │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

## Time Complexity

| Operation | Time Complexity | Description |
|-----------|----------------|-------------|
| Class loading | O(n) | Where n = number of permitted subtypes |
| Type checking | O(1) | Compiler checks are constant time |
| Pattern matching | O(1) | Each case is constant time |
| Switch expression | O(1) | Dispatch is constant time |
| instanceof check | O(1) | Runtime type check |
| Field access | O(1) | Same as regular classes |
| Method invocation | O(1) | Virtual dispatch is constant |
| Serialization | O(n) | Where n = object graph size |
| Deserialization | O(n) | Where n = object graph size |
| Reflection API | O(n) | Where n = permitted subtypes |

**Note**: Sealed classes themselves don't introduce runtime overhead. The benefits are primarily compile-time.

## Space Complexity

| Component | Space Complexity | Description |
|-----------|-----------------|-------------|
| Sealed class metadata | O(p) | p = number of permitted subtypes |
| Subtype records | O(1) per field | Same as regular records |
| Pattern matching | O(1) | No extra space needed |
| Switch tables | O(p) | Compiler generates lookup tables |
| Runtime type info | O(p) | JVM stores permitted subtypes |
| Module metadata | O(p) | Module system tracks hierarchy |

**Memory Layout**:
```
┌─────────────────────────────────────────┐
│  Sealed Class Instance                   │
├─────────────────────────────────────────┤
│  Object Header (16 bytes)               │
│  ┌─────────────────────────────────┐    │
│  │  Class Pointer → SealedMeta     │    │
│  │  - Permitted subtypes (O(p))    │    │
│  │  - Modifier flags               │    │
│  └─────────────────────────────────┘    │
│  Fields (same as non-sealed class)      │
└─────────────────────────────────────────┘
```

## Thread Safety

Sealed classes don't affect thread safety directly, but they enable patterns that improve concurrent code:

```java
// Thread-safe state machine using sealed classes
public sealed interface ThreadSafeState permits Idle, Active, Terminated {
    
    ThreadSafeState next();
    
    // Immutable record - thread safe by design
    public record Idle() implements ThreadSafeState {
        @Override
        public ThreadSafeState next() {
            return new Active();
        }
    }
    
    public record Active() implements ThreadSafeState {
        @Override
        public ThreadSafeState next() {
            return new Terminated();
        }
    }
    
    public record Terminated() implements ThreadSafeState {
        @Override
        public ThreadSafeState next() {
            throw new IllegalStateException("Cannot transition from terminated");
        }
    }
}

// Thread-safe processor using sealed class guarantees
public class ThreadSafeProcessor {
    private final AtomicReference<ThreadSafeState> state = 
        new AtomicReference<>(new ThreadSafeState.Idle());
    
    public ThreadSafeState transition() {
        return state.updateAndGet(current -> current.next());
    }
    
    // Exhaustive switch ensures all states handled
    public String describe() {
        return switch (state.get()) {
            case ThreadSafeState.Idle i -> "Idle";
            case ThreadSafeState.Active a -> "Active";
            case ThreadSafeState.Terminated t -> "Terminated";
        };
    }
}
```

**Thread Safety Guarantees**:
- Immutable records: Thread-safe by default
- Exhaustive matching: No missing state handling
- AtomicReference: Safe state transitions
- No shared mutable state: No synchronization needed

## Comparison Table

| Feature | Sealed Classes | Enums | Regular Inheritance | Interfaces |
|---------|---------------|-------|-------------------|------------|
| **Subtype Control** | Yes (permits) | Fixed set | Open | Open |
| **Exhaustive Switch** | Yes | Yes | No | No |
| **Fields per Subtype** | Yes | No | Yes | No (constants only) |
| **Generic Types** | Yes | Limited | Yes | Yes |
| **Multiple Inheritance** | No | No | No | Yes |
| **Pattern Matching** | Full | Full | Partial | Full |
| **Compile-time Safety** | High | High | Low | Medium |
| **Runtime Performance** | O(1) | O(1) | O(1) | O(1) |
| **Memory Overhead** | Minimal | Minimal | Minimal | Minimal |
| **Extensibility** | Controlled | None | Open | Open |
| **Use Case** | Domain modeling | Constants | General | Contracts |
| **Example** | `sealed class Shape` | `enum Color` | `class Animal` | `interface Serializable` |

## Decision Tree

```
Should you use a Sealed Class?
═══════════════════════════════════════════════════════════════════

Do you need to restrict which classes can extend/implement?
├── YES
│   ├── Do you need exhaustive pattern matching?
│   │   ├── YES → Use Sealed Class/Interface ✓
│   │   └── NO
│   │       ├── Do you need different fields per subtype?
│   │       │   ├── YES → Use Sealed Class with Records ✓
│   │       │   └── NO → Consider Enum (if fixed set)
│   │       └── Do you need generic type parameters?
│   │           ├── YES → Use Sealed Interface ✓
│   │           └── NO → Consider Enum
│   └── Is this for domain modeling?
│       ├── YES → Use Sealed Class ✓
│       └── NO → Use Sealed Interface ✓
└── NO
    ├── Do you need a fixed set of constants?
    │   ├── YES → Use Enum
    │   └── NO → Use Regular Interface/Class
    └── Is extensibility important?
        ├── YES → Use Regular Interface/Abstract Class
        └── NO → Use Sealed Class/Interface ✓

═══════════════════════════════════════════════════════════════════

Example Decision Paths:
─────────────────────────────────────────────────────────────────

Path 1: Payment Processing
  Need different payment methods? YES
  Need exhaustive handling? YES
  Different fields per type? YES
  → Use Sealed Interface with Records

Path 2: State Machine
  Need to restrict states? YES
  Need exhaustive transitions? YES
  Different behavior per state? YES
  → Use Sealed Class with Pattern Matching

Path 3: Configuration
  Fixed set of options? YES
  Need different behavior? NO
  → Use Enum

Path 4: Plugin System
  Need to restrict plugins? YES
  Need open extension? YES
  → Use Sealed Interface with non-sealed implementations
```

## Assignments

### Assignment 1: File System Model (Easy)

**Objective**: Create a sealed class hierarchy for a file system.

**Requirements**:
1. Create a sealed `FileSystemNode` interface
2. Implement `File`, `Directory`, and `SymbolicLink` as records
3. Add methods: `getName()`, `getSize()`, `isDirectory()`
4. Implement a `FileSystem` class that can traverse the hierarchy
5. Use pattern matching to display node information

**Starter Code**:
```java
public sealed interface FileSystemNode permits File, Directory, SymbolicLink {
    String getName();
    long getSize();
    boolean isDirectory();
}
```

### Assignment 2: Command Pattern (Medium)

**Objective**: Implement a command pattern using sealed classes.

**Requirements**:
1. Create a sealed `Command` interface
2. Implement `MoveCommand`, `ResizeCommand`, `RotateCommand`, `ColorCommand`
3. Each command should have `execute()`, `undo()`, and `description()`
4. Create a `CommandHistory` class that tracks executed commands
5. Use exhaustive pattern matching for undo operations

### Assignment 3: Validation System (Medium)

**Objective**: Build a validation system using sealed classes.

**Requirements**:
1. Create a sealed `ValidationResult` interface
2. Implement `Valid`, `Invalid`, `Warning`, `Error` types
3. Each type should carry different data (field, message, severity)
4. Create a `Validator` class that chains validations
5. Use pattern matching to generate validation reports

### Assignment 4: State Machine Framework (Hard)

**Objective**: Create a reusable state machine framework.

**Requirements**:
1. Create a generic sealed `State<S, E>` interface
2. Implement `Idle`, `Active`, `Terminal` states
3. Create `Transition` records for state changes
4. Build a `StateMachine` class with `onTransition()` callbacks
5. Use pattern matching to enforce valid transitions
6. Add logging and error handling

### Assignment 5: Expression Parser (Hard)

**Objective**: Build an expression parser with sealed AST.

**Requirements**:
1. Create a sealed `Expression` interface
2. Implement `Number`, `Add`, `Subtract`, `Multiply`, `Divide`, `Negate`
3. Create an `ExpressionParser` that parses string expressions
4. Implement an `ExpressionEvaluator` using pattern matching
5. Add an `ExpressionPrinter` for display

## Mini Project: Event Sourcing System

**Objective**: Build an event sourcing system using sealed classes.

### Project Structure

```
event-sourcing/
├── src/main/java/com/example/eventsourcing/
│   ├── Event.java
│   ├── EventStore.java
│   ├── AggregateRoot.java
│   ├── Command.java
│   └── OrderAggregate.java
└── src/test/java/com/example/eventsourcing/
    └── OrderAggregateTest.java
```

### Implementation

**Event.java**:
```java
public sealed interface Event permits 
    OrderCreated, 
    ItemAdded, 
    ItemRemoved, 
    OrderConfirmed, 
    OrderShipped, 
    OrderDelivered {
    
    UUID eventId();
    Instant timestamp();
    String aggregateId();
}

public record OrderCreated(
    UUID eventId,
    Instant timestamp,
    String aggregateId,
    String customerId
) implements Event {}

public record ItemAdded(
    UUID eventId,
    Instant timestamp,
    String aggregateId,
    String productId,
    int quantity,
    double price
) implements Event {}

public record ItemRemoved(
    UUID eventId,
    Instant timestamp,
    String aggregateId,
    String productId,
    int quantity
) implements Event {}

public record OrderConfirmed(
    UUID eventId,
    Instant timestamp,
    String aggregateId,
    Instant confirmedAt
) implements Event {}

public record OrderShipped(
    UUID eventId,
    Instant timestamp,
    String aggregateId,
    String trackingNumber,
    Instant shippedAt
) implements Event {}

public record OrderDelivered(
    UUID eventId,
    Instant timestamp,
    String aggregateId,
    Instant deliveredAt
) implements Event {}
```

**Command.java**:
```java
public sealed interface Command permits 
    CreateOrder, 
    AddItem, 
    RemoveItem, 
    ConfirmOrder, 
    ShipOrder, 
    DeliverOrder {
    
    String aggregateId();
}

public record CreateOrder(String aggregateId, String customerId) implements Command {}
public record AddItem(String aggregateId, String productId, int quantity, double price) implements Command {}
public record RemoveItem(String aggregateId, String productId, int quantity) implements Command {}
public record ConfirmOrder(String aggregateId) implements Command {}
public record ShipOrder(String aggregateId, String trackingNumber) implements Command {}
public record DeliverOrder(String aggregateId) implements Command {}
```

**EventStore.java**:
```java
public class EventStore {
    private final Map<String, List<Event>> events = new HashMap<>();
    private final List<Consumer<Event>> subscribers = new ArrayList<>();
    
    public void append(Event event) {
        events.computeIfAbsent(event.aggregateId(), k -> new ArrayList<>())
               .add(event);
        subscribers.forEach(sub -> sub.accept(event));
    }
    
    public List<Event> getEvents(String aggregateId) {
        return events.getOrDefault(aggregateId, List.of());
    }
    
    public void subscribe(Consumer<Event> subscriber) {
        subscribers.add(subscriber);
    }
}
```

**OrderAggregate.java**:
```java
public class OrderAggregate {
    private String id;
    private String customerId;
    private List<OrderItem> items = new ArrayList<>();
    private OrderStatus status;
    private String trackingNumber;
    
    public OrderAggregate(String id) {
        this.id = id;
        this.status = OrderStatus.CREATED;
    }
    
    // Apply events using exhaustive pattern matching
    public void apply(Event event) {
        switch (event) {
            case OrderCreated e -> {
                this.id = e.aggregateId();
                this.customerId = e.customerId();
                this.status = OrderStatus.CREATED;
            }
            case ItemAdded e -> {
                items.add(new OrderItem(e.productId(), e.quantity(), e.price()));
            }
            case ItemRemoved e -> {
                items.removeIf(item -> item.productId().equals(e.productId()));
            }
            case OrderConfirmed e -> {
                this.status = OrderStatus.CONFIRMED;
            }
            case OrderShipped e -> {
                this.status = OrderStatus.SHIPPED;
                this.trackingNumber = e.trackingNumber();
            }
            case OrderDelivered e -> {
                this.status = OrderStatus.DELIVERED;
            }
        }
    }
    
    // Process commands
    public List<Event> processCommand(Command command) {
        return switch (command) {
            case CreateOrder c -> List.of(
                new OrderCreated(UUID.randomUUID(), Instant.now(), c.aggregateId(), c.customerId())
            );
            case AddItem c -> List.of(
                new ItemAdded(UUID.randomUUID(), Instant.now(), c.aggregateId(), 
                    c.productId(), c.quantity(), c.price())
            );
            case RemoveItem c -> List.of(
                new ItemRemoved(UUID.randomUUID(), Instant.now(), c.aggregateId(), 
                    c.productId(), c.quantity())
            );
            case ConfirmOrder c -> List.of(
                new OrderConfirmed(UUID.randomUUID(), Instant.now(), c.aggregateId(), Instant.now())
            );
            case ShipOrder c -> List.of(
                new OrderShipped(UUID.randomUUID(), Instant.now(), c.aggregateId(), 
                    c.trackingNumber(), Instant.now())
            );
            case DeliverOrder c -> List.of(
                new OrderDelivered(UUID.randomUUID(), Instant.now(), c.aggregateId(), Instant.now())
            );
        };
    }
    
    // Rebuild from events
    public void rebuildFromEvents(List<Event> events) {
        events.forEach(this::apply);
    }
    
    // Get current state
    public Map<String, Object> getState() {
        return Map.of(
            "id", id,
            "customerId", customerId,
            "items", items,
            "status", status,
            "trackingNumber", trackingNumber != null ? trackingNumber : "N/A"
        );
    }
    
    private enum OrderStatus {
        CREATED, CONFIRMED, SHIPPED, DELIVERED
    }
    
    private record OrderItem(String productId, int quantity, double price) {}
}
```

### Usage Example

```java
public class EventSourcingDemo {
    public static void main(String[] args) {
        EventStore store = new EventStore();
        String orderId = "ORDER-001";
        
        // Create order
        CreateOrder createCmd = new CreateOrder(orderId, "CUST-001");
        OrderAggregate aggregate = new OrderAggregate(orderId);
        
        List<Event> events = aggregate.processCommand(createCmd);
        events.forEach(store::append);
        events.forEach(aggregate::apply);
        
        // Add items
        AddItem addCmd = new AddItem(orderId, "PROD-001", 2, 29.99);
        events = aggregate.processCommand(addCmd);
        events.forEach(store::append);
        events.forEach(aggregate::apply);
        
        // Confirm order
        ConfirmOrder confirmCmd = new ConfirmOrder(orderId);
        events = aggregate.processCommand(confirmCmd);
        events.forEach(store::append);
        events.forEach(aggregate::apply);
        
        // Display state
        System.out.println("Order State: " + aggregate.getState());
        
        // Rebuild from events
        OrderAggregate rebuilt = new OrderAggregate(orderId);
        rebuilt.rebuildFromEvents(store.getEvents(orderId));
        System.out.println("Rebuilt State: " + rebuilt.getState());
    }
}
```

## Use Cases

### 1. Domain-Driven Design (DDD)

```java
// Value Objects
public sealed interface Money permits Dollar, Euro, Pound {
    double amount();
    String currency();
}

public record Dollar(double amount) implements Money {
    @Override
    public String currency() { return "USD"; }
}

public record Euro(double amount) implements Money {
    @Override
    public String currency() { return "EUR"; }
}

public record Pound(double amount) implements Money {
    @Override
    public String currency() { return "GBP"; }
}

// Domain Events
public sealed interface DomainEvent permits 
    AccountCreated, MoneyDeposited, MoneyWithdrawn, AccountClosed {
    
    String accountId();
    Instant occurredOn();
}
```

### 2. Compiler/Interpreter Design

```java
// Abstract Syntax Tree (AST)
public sealed interface AST permits 
    NumberLiteral, 
    StringLiteral, 
    BinaryOperation, 
    UnaryOperation,
    Variable,
    FunctionCall {
    
    Type type();
}

public record NumberLiteral(double value) implements AST {
    @Override
    public Type type() { return Type.NUMBER; }
}

public record BinaryOperation(AST left, Operator op, AST right) implements AST {
    @Override
    public Type type() { return Type.NUMBER; }
    
    public enum Operator { ADD, SUBTRACT, MULTIPLY, DIVIDE }
}
```

### 3. API Response Handling

```java
public sealed interface ApiResponse<T> permits 
    Success, 
    Error, 
    Loading, 
    Paginated {
    
    boolean isSuccessful();
}

public record Success<T>(T data) implements ApiResponse<T> {
    @Override
    public boolean isSuccessful() { return true; }
}

public record Error<T>(String message, int code) implements ApiResponse<T> {
    @Override
    public boolean isSuccessful() { return false; }
}

public record Loading<T>() implements ApiResponse<T> {
    @Override
    public boolean isSuccessful() { return false; }
}

public record Paginated<T>(List<T> data, int page, int totalPages) implements ApiResponse<T> {
    @Override
    public boolean isSuccessful() { return true; }
}
```

### 4. Configuration Management

```java
public sealed interface DatabaseConfig permits 
    PostgreSQLConfig, 
    MySQLConfig, 
    MongoDBConfig {
    
    String host();
    int port();
    String database();
}

public record PostgreSQLConfig(String host, int port, String database, boolean useSSL) 
    implements DatabaseConfig {
    
    public PostgreSQLConfig {
        if (port <= 0) throw new IllegalArgumentException("Port must be positive");
    }
}

public record MySQLConfig(String host, int port, String database, String charset) 
    implements DatabaseConfig {}
```

### 5. Game Development

```java
public sealed interface GameEvent permits 
    PlayerJoined, 
    PlayerLeft, 
    PlayerMoved, 
    PlayerAttacked,
    ItemCollected,
    GameOver {
    
    String gameId();
    Instant timestamp();
}

public record PlayerJoined(String gameId, Instant timestamp, String playerId) 
    implements GameEvent {}

public record PlayerAttacked(
    String gameId, 
    Instant timestamp, 
    String attackerId, 
    String targetId, 
    int damage
) implements GameEvent {}
```

## Testing Strategies

### Unit Testing Sealed Classes

```java
@DisplayName("Shape Sealed Class Tests")
class ShapeTest {
    
    @Test
    @DisplayName("Should create all permitted subtypes")
    void shouldCreateAllSubtypes() {
        Shape circle = new Circle(5.0);
        Shape rectangle = new Rectangle(4.0, 6.0);
        Shape triangle = new Triangle(3.0, 4.0, 5.0);
        
        assertNotNull(circle);
        assertNotNull(rectangle);
        assertNotNull(triangle);
    }
    
    @Test
    @DisplayName("Should handle exhaustive switch")
    void shouldHandleExhaustiveSwitch() {
        List<Shape> shapes = List.of(
            new Circle(5.0),
            new Rectangle(4.0, 6.0),
            new Triangle(3.0, 4.0, 5.0)
        );
        
        shapes.forEach(shape -> {
            String result = switch (shape) {
                case Circle c -> "Circle";
                case Rectangle r -> "Rectangle";
                case Triangle t -> "Triangle";
            };
            assertNotNull(result);
        });
    }
    
    @Test
    @DisplayName("Should calculate area for each subtype")
    void shouldCalculateArea() {
        Shape circle = new Circle(5.0);
        Shape rectangle = new Rectangle(4.0, 6.0);
        
        assertEquals(Math.PI * 25, circle.area(), 0.001);
        assertEquals(24.0, rectangle.area(), 0.001);
    }
    
    @Test
    @DisplayName("Should reject non-permitted subtypes at compile time")
    void shouldRejectNonPermittedSubtypes() {
        // This test verifies compile-time enforcement
        // The following would cause a compilation error:
        // public class InvalidShape extends Shape {}
        
        // Verify sealed class is sealed
        assertTrue(Shape.class.isSealed());
    }
    
    @ParameterizedTest
    @CsvSource({
        "5.0, 78.539",
        "10.0, 314.159",
        "1.0, 3.141"
    })
    @DisplayName("Should calculate circle area correctly")
    void shouldCalculateCircleArea(double radius, double expected) {
        Circle circle = new Circle(radius);
        assertEquals(expected, circle.area(), 0.001);
    }
}

// Mock testing with sealed classes
@ExtendWith(MockitoExtension.class)
class PaymentProcessorTest {
    
    @Mock
    private PaymentGateway gateway;
    
    @Test
    @DisplayName("Should process different payment types")
    void shouldProcessDifferentPaymentTypes() {
        Payment creditCard = new CreditCardPayment(100.0, "USD", "4111111111111234");
        Payment paypal = new PayPalPayment(50.0, "EUR", "user@example.com");
        
        when(gateway.process(any())).thenReturn(true);
        
        PaymentProcessor processor = new PaymentProcessor(gateway);
        
        assertDoesNotThrow(() -> processor.process(creditCard));
        assertDoesNotThrow(() -> processor.process(paypal));
    }
}
```

### Integration Testing

```java
@SpringBootTest
class OrderIntegrationTest {
    
    @Autowired
    private OrderRepository repository;
    
    @Test
    @DisplayName("Should persist and retrieve sealed order status")
    void shouldPersistAndRetrieveOrderStatus() {
        Order order = new Order("TEST-001");
        order.confirm("CONF-123");
        
        repository.save(order);
        
        Order retrieved = repository.findById("TEST-001").orElseThrow();
        
        String status = switch (retrieved.getStatus()) {
            case Pending p -> "PENDING";
            case Confirmed c -> "CONFIRMED";
            case Shipped s -> "SHIPPED";
            case Delivered d -> "DELIVERED";
            case Cancelled c -> "CANCELLED";
        };
        
        assertEquals("CONFIRMED", status);
    }
}
```

### Property-Based Testing

```java
class SealedClassProperties {
    
    @Property
    void allSubtypesAreHandled(@ForAll Shape shape) {
        // Property: Every shape must have a valid area
        double area = switch (shape) {
            case Circle c -> Math.PI * c.radius() * c.radius();
            case Rectangle r -> r.width() * r.height();
            case Triangle t -> calculateTriangleArea(t);
        };
        
        assertTrue(area >= 0, "Area must be non-negative");
    }
    
    @Property
    void exhaustiveSwitchIsComplete(@ForAll OrderStatus status) {
        // Property: Every status must have a description
        String description = switch (status) {
            case Pending p -> p.description();
            case Confirmed c -> c.description();
            case Shipped s -> s.description();
            case Delivered d -> d.description();
            case Cancelled c -> c.description();
        };
        
        assertNotNull(description);
        assertFalse(description.isEmpty());
    }
    
    private double calculateTriangleArea(Triangle t) {
        double s = (t.a() + t.b() + t.c()) / 2;
        return Math.sqrt(s * (s - t.a()) * (s - t.b()) * (s - t.c()));
    }
}
```

## Advanced Patterns

### 1. Sealed Class with Builder Pattern

```java
public sealed interface HttpResult permits Success, Redirect, ClientError, ServerError {
    
    int statusCode();
    String message();
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private int statusCode;
        private String message;
        private Map<String, String> headers = new HashMap<>();
        
        public Builder statusCode(int code) {
            this.statusCode = code;
            return this;
        }
        
        public Builder message(String msg) {
            this.message = msg;
            return this;
        }
        
        public Builder header(String key, String value) {
            this.headers.put(key, value);
            return this;
        }
        
        public HttpResult build() {
            return switch (statusCode / 100) {
                case 2 -> new Success(statusCode, message, headers);
                case 3 -> new Redirect(statusCode, message, headers.get("Location"));
                case 4 -> new ClientError(statusCode, message);
                case 5 -> new ServerError(statusCode, message);
                default -> throw new IllegalArgumentException("Invalid status code: " + statusCode);
            };
        }
    }
}

public record Success(int statusCode, String message, Map<String, String> headers) 
    implements HttpResult {}

public record Redirect(int statusCode, String message, String location) 
    implements HttpResult {}

public record ClientError(int statusCode, String message) 
    implements HttpResult {}

public record ServerError(int statusCode, String message) 
    implements HttpResult {}
```

### 2. Sealed Class with Visitor Pattern

```java
public sealed interface ASTVisitor<T> permits 
    NumberEvaluator, 
    StringEvaluator, 
    BooleanEvaluator {
    
    T visit(NumberLiteral expr);
    T visit(BinaryOperation expr);
    T visit(UnaryOperation expr);
}

public class NumberEvaluator implements ASTVisitor<Double> {
    
    @Override
    public Double visit(NumberLiteral expr) {
        return expr.value();
    }
    
    @Override
    public Double visit(BinaryOperation expr) {
        double left = expr.left().accept(this);
        double right = expr.right().accept(this);
        
        return switch (expr.op()) {
            case ADD -> left + right;
            case SUBTRACT -> left - right;
            case MULTIPLY -> left * right;
            case DIVIDE -> left / right;
        };
    }
    
    @Override
    public Double visit(UnaryOperation expr) {
        double value = expr.operand().accept(this);
        return switch (expr.op()) {
            case NEGATE -> -value;
        };
    }
}
```

### 3. Sealed Class with Strategy Pattern

```java
public sealed interface CompressionStrategy permits 
    GzipCompression, 
    DeflateCompression, 
    BrotliCompression, 
    NoCompression {
    
    byte[] compress(byte[] data);
    byte[] decompress(byte[] data);
    String algorithm();
}

public class CompressionService {
    private final CompressionStrategy strategy;
    
    public CompressionService(CompressionStrategy strategy) {
        this.strategy = strategy;
    }
    
    public byte[] process(byte[] data, boolean compress) {
        return compress ? strategy.compress(data) : strategy.decompress(data);
    }
    
    // Factory method using pattern matching
    public static CompressionService create(String algorithm) {
        CompressionStrategy strategy = switch (algorithm.toLowerCase()) {
            case "gzip" -> new GzipCompression();
            case "deflate" -> new DeflateCompression();
            case "brotli" -> new BrotliCompression();
            case "none" -> new NoCompression();
            default -> throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
        };
        
        return new CompressionService(strategy);
    }
}
```

### 4. Sealed Class with State Pattern

```java
public sealed interface ConnectionState permits 
    Disconnected, 
    Connecting, 
    Connected, 
    Error {
    
    ConnectionState connect();
    ConnectionState disconnect();
    ConnectionState send(byte[] data);
    boolean isConnected();
}

public record Disconnected() implements ConnectionState {
    @Override
    public ConnectionState connect() {
        return new Connecting();
    }
    
    @Override
    public ConnectionState disconnect() {
        return this; // Already disconnected
    }
    
    @Override
    public ConnectionState send(byte[] data) {
        throw new IllegalStateException("Cannot send data while disconnected");
    }
    
    @Override
    public boolean isConnected() {
        return false;
    }
}

public record Connecting() implements ConnectionState {
    @Override
    public ConnectionState connect() {
        return new Connected();
    }
    
    @Override
    public ConnectionState disconnect() {
        return new Disconnected();
    }
    
    @Override
    public ConnectionState send(byte[] data) {
        throw new IllegalStateException("Cannot send data while connecting");
    }
    
    @Override
    public boolean isConnected() {
        return false;
    }
}
```

## Common Pitfalls

### 4. Not Leveraging Exhaustive Matching

**Wrong**:
```java
public String describe(Shape shape) {
    if (shape instanceof Circle) {
        return "Circle";
    } else if (shape instanceof Rectangle) {
        return "Rectangle";
    }
    // Missing Triangle - will compile but loses sealed benefit
    return "Unknown";
}
```

**Right**:
```java
public String describe(Shape shape) {
    return switch (shape) {
        case Circle c -> "Circle";
        case Rectangle r -> "Rectangle";
        case Triangle t -> "Triangle";
    };
}
```

### 5. Overusing non-sealed

**Wrong**:
```java
public sealed class Shape permits Circle, Rectangle, Triangle {}
public non-sealed class Circle extends Shape {} // Breaks sealed guarantee
public non-sealed class Rectangle extends Shape {}
public non-sealed class Triangle extends Shape {}
```

**Right**:
```java
public sealed class Shape permits Circle, Rectangle, Triangle {}
public final class Circle extends Shape {}
public final class Rectangle extends Shape {}
public final class Triangle extends Shape {}
```

### 6. Forgetting Module Boundaries

**Wrong**:
```java
// Module A
module com.example.modulea {
    exports com.example.modulea.shapes;
}

// Module B - won't work if not in same module
module com.example.moduleb {
    opens com.example.moduleb.impl to com.example.modulea;
}
```

**Right**:
```java
// Keep sealed hierarchy in same module
module com.example.shapes {
    exports com.example.shapes;
    // All permitted classes in same module
}
```

## Glossary

| Term | Definition |
|------|-----------|
| **Sealed Class** | A class that restricts which other classes can extend it |
| **Sealed Interface** | An interface that restricts which classes can implement it |
| **permits** | Keyword listing allowed subtypes of a sealed class |
| **final modifier** | Prevents further extension of a permitted subtype |
| **sealed modifier** | Allows extension but with its own restrictions |
| **non-sealed modifier** | Opens the subtype for unrestricted extension |
| **Exhaustive matching** | Compiler ensures all cases are handled in switch |
| **Pattern matching** | Type-based dispatch with automatic casting |
| **Subtype** | A class that extends or implements a sealed type |
| **Supertype** | The sealed class or interface being extended |
| **Module boundary** | Java module that contains the sealed hierarchy |
| **Record** | Immutable class with automatic methods |

## Version History

| Java Version | Feature | Description |
|-------------|---------|-------------|
| 15 (Preview) | Sealed Classes | First preview with `permits` keyword |
| 16 (Preview) | Sealed Classes | Second preview with improvements |
| 17 (Final) | Sealed Classes | Finalized feature |
| 17 | Pattern Matching | Basic pattern matching for instanceof |
| 18 (Preview) | Pattern Matching | Enhanced pattern matching |
| 19 (Preview) | Pattern Matching | Third preview |
| 20 (Preview) | Pattern Matching | Fourth preview |
| 21 (Final) | Pattern Matching | Finalized pattern matching for switch |
| 21 | Record Patterns | Destructuring records in patterns |
| 21 | Sealed + Pattern | Full integration of sealed classes with pattern matching |

## Migration Guide

### Migrating from Enums

**Before (Enum)**:
```java
public enum ShapeType {
    CIRCLE, RECTANGLE, TRIANGLE
}
```

**After (Sealed Class)**:
```java
public sealed interface Shape permits Circle, Rectangle, Triangle {
    double area();
}

public record Circle(double radius) implements Shape {
    @Override
    public double area() { return Math.PI * radius * radius; }
}

public record Rectangle(double width, double height) implements Shape {
    @Override
    public double area() { return width * height; }
}

public record Triangle(double a, double b, double c) implements Shape {
    @Override
    public double area() {
        double s = (a + b + c) / 2;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }
}
```

### Migrating from Abstract Classes

**Before (Abstract Class)**:
```java
public abstract class Shape {
    public abstract double area();
}

public class Circle extends Shape {
    private final double radius;
    public Circle(double radius) { this.radius = radius; }
    @Override
    public double area() { return Math.PI * radius * radius; }
}
```

**After (Sealed Interface)**:
```java
public sealed interface Shape permits Circle, Rectangle {
    double area();
}

public record Circle(double radius) implements Shape {
    @Override
    public double area() { return Math.PI * radius * radius; }
}

public record Rectangle(double width, double height) implements Shape {
    @Override
    public double area() { return width * height; }
}
```

## Tool Support

### IDE Support

| IDE | Support Level | Features |
|-----|--------------|----------|
| IntelliJ IDEA | Full | Code completion, refactoring, inspection |
| Eclipse | Full | Code completion, quick fixes |
| VS Code | Full | Extension support, syntax highlighting |
| NetBeans | Full | Code completion, hints |

### Build Tool Support

| Build Tool | Support | Configuration |
|-----------|---------|---------------|
| Maven | Full | No special config needed |
| Gradle | Full | No special config needed |
| Ant | Full | Requires Java 17+ compiler |

### Static Analysis Tools

| Tool | Support | Notes |
|------|---------|-------|
| SonarQube | Full | Detects sealed class violations |
| PMD | Partial | Basic sealed class checks |
| Checkstyle | Full | Enforces sealed class conventions |
| SpotBugs | Full | Detects potential issues |

## Community Resources

### Official Documentation

- [JEP 409: Sealed Classes](https://openjdk.org/jeps/409)
- [Java Language Specification: Sealed Classes](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.1.1.2)

### Books

- "Effective Java" by Joshua Bloch
- "Modern Java in Action" by Urma, Fusco, and Mycroft
- "Java: The Complete Reference" by Herbert Schildt

### Online Resources

- [Baeldung: Sealed Classes](https://www.baeldung.com/java-sealed-classes)
- [InfoQ: Sealed Classes in Java](https://www.infoq.com/articles/java-sealed-classes/)
- [Dev.to: Java Sealed Classes](https://dev.to/t/java/sealed)

### Community Projects

- [Sealed Classes Examples](https://github.com/sealed-classes-examples)
- [Pattern Matching Library](https://github.com/pattern-matching-lib)
- [State Machine Framework](https://github.com/state-machine-framework)

## Anti-Patterns

### 1. Using Sealed for Everything

**Wrong**:
```java
// Don't seal classes that should be extensible
public sealed class StringProcessor permits TrimProcessor, UpperProcessor {}
```

**Right**:
```java
// Only seal when you need to restrict the hierarchy
public sealed interface Result permits Success, Failure {}
```

### 2. Overcomplicating Hierarchies

**Wrong**:
```java
// Too many levels of sealed classes
public sealed class A permits B {}
public sealed class B permits C {}
public sealed class C permits D {}
public sealed class D permits E {}
public final class E extends D {}
```

**Right**:
```java
// Keep hierarchies flat when possible
public sealed interface A permits B, C, D, E {}
public final class B implements A {}
public final class C implements A {}
public final class D implements A {}
public final class E implements A {}
```

### 3. Mixing Sealed with Open Inheritance

**Wrong**:
```java
public sealed class Shape permits Circle, Rectangle {}
public non-sealed class Circle extends Shape {} // Anyone can extend Circle
public class CustomCircle extends Circle {} // Breaks sealed guarantee
```

**Right**:
```java
public sealed class Shape permits Circle, Rectangle {}
public final class Circle extends Shape {} // No further extension
public final class Rectangle extends Shape {}
```

### 4. Ignoring Exhaustive Matching

**Wrong**:
```java
// Using if-else chains loses sealed class benefits
public String describe(Shape shape) {
    if (shape instanceof Circle) {
        return "Circle";
    } else if (shape instanceof Rectangle) {
        return "Rectangle";
    }
    return "Unknown"; // Should never reach here with sealed class
}
```

**Right**:
```java
// Use exhaustive switch for maximum benefit
public String describe(Shape shape) {
    return switch (shape) {
        case Circle c -> "Circle";
        case Rectangle r -> "Rectangle";
    };
}
```

## Performance Considerations

### Runtime Performance

| Operation | Impact | Notes |
|-----------|--------|-------|
| Type checking | None | Same as regular classes |
| Pattern matching | None | Compiler-generated code |
| Method dispatch | None | Virtual dispatch unchanged |
| Memory usage | Minimal | Small metadata overhead |
| Serialization | None | Standard Java serialization |

### Compile-Time Performance

| Aspect | Impact | Notes |
|--------|--------|-------|
| Compilation speed | Slightly slower | Additional hierarchy checks |
| Error detection | Better | Catches invalid hierarchies early |
| IDE support | Minimal impact | Modern IDEs handle well |

### Best Practices for Performance

1. **Use records**: Immutable and memory-efficient
2. **Keep hierarchies flat**: Reduce dispatch overhead
3. **Avoid deep nesting**: Limits pattern matching complexity
4. **Use exhaustive matching**: Compiler optimizes switch expressions

## Code Examples Summary

### Basic Sealed Class

```java
public sealed class Shape permits Circle, Rectangle, Triangle {}
public final class Circle extends Shape {}
public final class Rectangle extends Shape {}
public final class Triangle extends Shape {}
```

### Sealed Interface with Records

```java
public sealed interface Result permits Success, Failure {}
public record Success<T>(T data) implements Result {}
public record Failure(String error) implements Result {}
```

### Exhaustive Pattern Matching

```java
public String describe(Shape shape) {
    return switch (shape) {
        case Circle c -> "Circle: " + c.radius();
        case Rectangle r -> "Rectangle: " + r.width() + "x" + r.height();
        case Triangle t -> "Triangle";
    };
}
```

### Generic Sealed Interface

```java
public sealed interface Container<T> permits Box, Empty {
    T get();
}

public record Box<T>(T value) implements Container<T> {
    @Override
    public T get() { return value; }
}

public record Empty<T>() implements Container<T> {
    @Override
    public T get() { return null; }
}
```
