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
