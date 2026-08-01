# Interfaces

## Introduction

An interface in Java is a reference type that defines a contract—a set of abstract methods, default methods, static methods, and constants that implementing classes must follow. Interfaces enable polymorphism, allowing different classes to be treated uniformly through a common type, even if they have no shared inheritance hierarchy.

Since Java 8, interfaces have evolved significantly with the addition of default methods, static methods, and private methods (Java 9+), making them more flexible and powerful than ever before. Interfaces are the primary mechanism for defining contracts in Java and are essential for writing flexible, testable, and maintainable code.

## Learning Objectives

By the end of this topic, you will be able to:

- Define and implement interfaces in Java
- Understand the difference between abstract classes and interfaces
- Use default methods to provide optional implementations
- Apply static methods for utility functions in interfaces
- Use private methods (Java 9+) to share code between default methods
- Identify and resolve the diamond problem
- Apply `@FunctionalInterface` for lambda expressions
- Design clean interface hierarchies following SOLID principles

## Prerequisites

Before studying interfaces, you should be familiar with:

- Basic Java class definitions and inheritance
- Understanding of abstract classes and methods
- Familiarity with method overriding
- Basic knowledge of polymorphism

## Why This Concept Exists

Consider a system where `Dog` and `Cat` both need to `speak()`, but they don't share a common superclass. Without interfaces, there's no way to enforce a common contract across unrelated classes.

```java
// Without interfaces - no common type
class Dog {
    void speak() { System.out.println("Woof"); }
}

class Cat {
    void speak() { System.out.println("Meow"); }
}

// Cannot treat them uniformly
void makeAnimalSpeak(???) {  // What type goes here?
    animal.speak();
}
```

Interfaces solve this by providing a type that any class can implement, regardless of its inheritance hierarchy. They also enable multiple inheritance of type, which Java classes don't support.

## Problem Statement

How do you define a common contract for unrelated classes? How do you allow a class to implement multiple behaviors? How do you provide default implementations that classes can optionally override?

Consider a payment processing system where `CreditCard`, `PayPal`, and `BankTransfer` all need to process payments, but they have no shared base class. You need a way to:

1. Define a common `PaymentMethod` contract
2. Allow any class to be treated as a `PaymentMethod`
3. Provide default receipt printing logic
4. Add utility methods for tax calculations

## Modern Interfaces (Java 8+)

```java
public interface Payable {
    // Abstract - must implement
    void pay(BigDecimal amount);

    // Default - optional override
    default void printReceipt() {
        System.out.println("Receipt printed");
    }

    // Static utility
    static BigDecimal calculateTax(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(0.18));
    }

    // Private (Java 9+)
    private void log(String msg) { System.out.println(msg); }
}
```

## Interface Rules

| Element | Modifiers |
|---------|-----------|
| Fields | `public static final` (implicit) |
| Methods | `public` (default) |
| Constructors | Not allowed |
| Methods since Java 8 | default, static, private |

## Default Methods

```java
interface Drawable {
    void draw();  // Abstract

    default void drawTwice() {  // Default
        draw();
        draw();
    }
}

class Circle implements Drawable {
    @Override public void draw() { System.out.println("Circle"); }
}
```

## Static Methods

```java
interface MathUtils {
    static int max(int a, int b) { return Math.max(a, b); }
}

// Call: MathUtils.max(5, 10)
```

## Private Methods (Java 9+)

```java
interface Validator {
    default boolean validate(String s) { return check(s); }
    private boolean check(String s) { return s != null && !s.isBlank(); }
}
```

## Functional Interfaces

Single abstract method → Lambda support:

```java
@FunctionalInterface
interface Operation {
    int apply(int a, int b);
}

// Lambdas
Operation add = (a, b) -> a + b;
Operation multiply = (a, b) -> a * b;
```

## Functional Interfaces

```java
@FunctionalInterface
interface Calculator {
    int calc(int a, int b);
}

// Built-in: java.util.function
Function<String, Integer> parser = Integer::parseInt;
Predicate<String> isEmpty = String::isEmpty;
Consumer<String> printer = System.out::println;
Supplier<LocalDate> now = LocalDate::now;
```

## Default Methods & Diamond Problem

```java
interface A { default void m() { System.out.println("A"); } }
interface B { default void m() { System.out.println("B"); } }

class C implements A, B {
    @Override public void m() { A.super.m(); }  // Must override
}
```

## Internal Working

When a class implements an interface, the Java compiler generates bytecode that includes interface method tables (itable). The JVM uses these tables to dispatch method calls to the correct implementation.

For default methods, the compiler copies the default method implementation into the implementing class's bytecode if the class doesn't override it. This means default methods are resolved at compile time, not runtime—there is no performance difference between a default method and a concrete method in an abstract class.

The JVM supports multiple interface inheritance through type checking at compile time. The bytecode verifier ensures that a class provides implementations for all abstract methods from all implemented interfaces.

## JVM Perspective

1. **Interface Method Resolution**: The JVM resolves interface method calls using `invokeinterface` bytecode instruction, which is slightly different from `invokevirtual`
2. **Default Methods**: Copied into implementing class bytecode—no runtime overhead
3. **Multiple Interfaces**: The class file stores implemented interfaces in the `interfaces` attribute; the JVM verifies all abstract methods are implemented
4. **Type Checking**: The compiler ensures type safety; the JVM trusts the compiler's verification
5. **`invokeinterface` vs `invokevirtual`**: Interface calls use `invokeinterface` which performs a vtable lookup; slightly slower than direct method calls but negligible in practice

## Memory Representation

When a class implements an interface:

```
Stack Frame              Heap Memory
┌──────────────┐        ┌────────────────────────┐
│ obj          │───────►│ Object of MyClass       │
│ (MyInterface)│        │ ┌──────────────────────┐│
└──────────────┘        │ │ class metadata       ││
                        │ │ - implements MyInterface ││
                        │ └──────────────────────┘│
                        └────────────────────────┘
```

- The object reference is typed as the interface type
- The actual class metadata includes all implemented interfaces
- Method calls are dispatched through interface method tables
- No additional memory overhead for implementing interfaces

## Syntax

### Basic Interface

```java
public interface InterfaceName {
    // Abstract method (implicit public abstract)
    ReturnType methodName(parameters);

    // Constant (implicit public static final)
    TYPE CONSTANT = value;

    // Default method (Java 8+)
    default ReturnType methodName(parameters) {
        // implementation
    }

    // Static method (Java 8+)
    static ReturnType methodName(parameters) {
        // implementation
    }

    // Private method (Java 9+)
    private ReturnType methodName(parameters) {
        // implementation
    }
}
```

### Implementing an Interface

```java
public class ClassName implements InterfaceName {
    @Override
    public ReturnType methodName(parameters) {
        // required implementation
    }
}
```

### Extending Interfaces

```java
public interface ChildInterface extends ParentInterface1, ParentInterface2 {
    // inherits methods from both parents
}
```

## Easy Example

```java
public interface Shape {
    double getArea();
    double getPerimeter();
}

public class Circle implements Shape {
    private final double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }
}

public class Rectangle implements Shape {
    private final double width;
    private final double height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public double getArea() {
        return width * height;
    }

    @Override
    public double getPerimeter() {
        return 2 * (width + height);
    }
}

// Polymorphic usage
void printShapeInfo(Shape shape) {
    System.out.println("Area: " + shape.getArea());
    System.out.println("Perimeter: " + shape.getPerimeter());
}
```

## Medium Example

```java
@FunctionalInterface
public interface Transformer<T, R> {
    R transform(T input);

    default <V> Transformer<T, V> andThen(Transformer<R, V> after) {
        return input -> after.transform(transform(input));
    }

    default <V> Transformer<V, R> compose(Transformer<V, T> before) {
        return input -> transform(before.transform(input));
    }

    static <T> Transformer<T, T> identity() {
        return input -> input;
    }
}

// Usage
Transformer<String, Integer> toLength = String::length;
Transformer<Integer, String> toString = i -> "Length: " + i;

Transformer<String, String> pipeline = toLength.andThen(toString);
System.out.println(pipeline.transform("Hello"));  // "Length: 5"
```

## Hard Example

```java
public interface Repository<T, ID> {
    Optional<T> findById(ID id);
    List<T> findAll();
    T save(T entity);
    void deleteById(ID id);
    boolean existsById(ID id);
    long count();

    default <R> R findByIdOrThrow(ID id, Function<T, R> mapper) {
        return findById(id)
            .map(mapper)
            .orElseThrow(() -> new EntityNotFoundException("Entity not found: " + id));
    }

    default void saveAll(List<T> entities) {
        entities.forEach(this::save);
    }

    private void validate(T entity) {
        Objects.requireNonNull(entity, "Entity cannot be null");
    }
}

public interface Auditable {
    Instant getCreatedAt();
    Instant getUpdatedAt();
    void setUpdatedAt(Instant timestamp);
}

// Concrete implementation
public class UserRepository implements Repository<User, Long>, Auditable {
    private final Map<Long, User> store = new HashMap<>();
    private Instant createdAt;
    private Instant updatedAt;

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(store.values());
    }

    @Override
    public User save(User entity) {
        Objects.requireNonNull(entity);
        updatedAt = Instant.now();
        store.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
        updatedAt = Instant.now();
    }

    @Override
    public boolean existsById(Long id) {
        return store.containsKey(id);
    }

    @Override
    public long count() {
        return store.size();
    }

    @Override
    public Instant getCreatedAt() { return createdAt; }

    @Override
    public Instant getUpdatedAt() { return updatedAt; }

    @Override
    public void setUpdatedAt(Instant timestamp) { this.updatedAt = timestamp; }
}
```

## Enterprise Example

```java
public interface PaymentProcessor {
    PaymentResult processPayment(PaymentRequest request);
    boolean supportsPaymentMethod(PaymentMethod method);
    default void validateRequest(PaymentRequest request) {
        Objects.requireNonNull(request, "Payment request cannot be null");
        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }
}

public interface NotificationService {
    void sendPaymentConfirmation(PaymentResult result);
    void sendPaymentFailure(PaymentResult result);
}

public interface FraudDetector {
    FraudCheckResult checkTransaction(PaymentRequest request);
}

public class CompositePaymentProcessor implements PaymentProcessor {
    private final Map<PaymentMethod, PaymentProcessor> processors;

    public CompositePaymentProcessor(Map<PaymentMethod, PaymentProcessor> processors) {
        this.processors = Map.copyOf(processors);
    }

    @Override
    public PaymentResult processPayment(PaymentRequest request) {
        validateRequest(request);
        PaymentProcessor processor = processors.get(request.method());
        if (processor == null) {
            throw new UnsupportedOperationException(
                "Payment method not supported: " + request.method()
            );
        }
        return processor.processPayment(request);
    }

    @Override
    public boolean supportsPaymentMethod(PaymentMethod method) {
        return processors.containsKey(method);
    }
}
```

## Performance

| Aspect | Impact |
|--------|--------|
| `invokeinterface` | Slightly slower than `invokevirtual` |
| Default methods | Copied to implementing class—no overhead |
| Type checking | Compile-time only |
| Interface hierarchy | Minimal runtime cost |

**Key facts:**

- Interface method calls use `invokeinterface` bytecode instruction
- `invokeinterface` performs a vtable lookup—typically 2-3 nanoseconds slower than direct calls
- The JIT compiler can devirtualize interface calls when the implementing class is known
- Default methods have zero overhead—copied into implementing class bytecode
- Implementing multiple interfaces adds no memory overhead to objects

## Interface vs Abstract Class

| Feature | Interface | Abstract Class |
|---------|-----------|----------------|
| Inheritance | Multiple | Single |
| Fields | Constants only | Instance + constants |
| Constructors | No | Yes |
| Methods | Abstract, default, static | Abstract + concrete |
| Access | public | Any |

## Interface vs Abstract Class

| Scenario | Choice |
|----------|--------|
| Shared code + contract | Abstract class |
| Pure contract | Interface |
| Multiple implementations | Interface |
| Need constructors/fields | Abstract class |
| Functional interface | Interface |

## Best Practices

1. **Use `@FunctionalInterface` annotation**: Mark interfaces with a single abstract method to enable lambda expressions and get compile-time verification
2. **Prefer interfaces for contracts**: Define behavior through interfaces rather than abstract classes when possible
3. **Default methods for backward compatibility**: Use default methods when adding new methods to existing interfaces
4. **Static methods for utilities**: Place utility methods as static methods in the interface rather than in a separate utility class
5. **Keep interfaces focused**: Follow the Interface Segregation Principle (ISP)—clients shouldn't be forced to depend on methods they don't use
6. **Name interfaces as adjectives or verb phrases**: Use names like `Readable`, `Comparable`, `AutoCloseable` rather than `Reader`, `Comparator`, `Closer`
7. **Prefer composition over inheritance**: Use interfaces to define capabilities, not to create deep inheritance hierarchies
8. **Document interface contracts**: Clearly specify preconditions, postconditions, and thread-safety guarantees

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Fat interfaces | Split into smaller, focused interfaces (ISP) |
| Default method conflicts | Override and resolve explicitly |
| Using interfaces for constants | Use constant classes or enums |
| Exposing internal types | Return well-defined types, not implementation details |

## Pitfalls

1. **Diamond problem**: When two interfaces provide default methods with the same signature, the implementing class must override the method
2. **Breaking changes**: Adding new abstract methods to an interface breaks all implementing classes—use default methods instead
3. **Interface pollution**: Adding too many methods to an interface forces implementers to provide unnecessary implementations
4. **Leaking types**: Interface methods that expose implementation-specific types reduce the value of the interface
5. **Default method state**: Default methods cannot access instance fields since interfaces don't have them—use abstract methods to access state
6. **Evolution challenges**: Changing interfaces in production systems requires careful planning to avoid breaking existing implementations

## Debugging Tips

1. **Verify interface implementation**: Use `instanceof` to check if an object implements an interface
2. **Check method resolution**: When diamond problems occur, verify which default method is being called
3. **Use IDE navigation**: IDEs can show you all implementations of an interface—use this to understand the codebase
4. **Inspect bytecode**: Use `javap -c` to see how interface methods are dispatched
5. **Debug with JShell**: Quickly test interface implementations interactively
6. **Check for missing implementations**: Compiler errors will tell you which abstract methods are not implemented

## Comparison Table

| Feature | Interface | Abstract Class |
|---------|-----------|----------------|
| Multiple inheritance | Yes | No |
| Fields | Constants only | Instance fields |
| Constructors | No | Yes |
| Access modifiers | public only | Any |
| Default methods | Yes (Java 8+) | Yes |
| Static methods | Yes (Java 8+) | Yes |
| Performance | Slightly slower dispatch | Direct dispatch |

## Decision Tree

```
Should you use an interface?

Need multiple type inheritance? ──► YES ──► Use interface
       │
       NO
       │
Need shared state/constructors? ──► YES ──► Use abstract class
       │
       NO
       │
Defining a pure contract? ──► YES ──► Use interface
       │
       NO
       │
Need default implementations? ──► Either works
       │
       Consider interface with default methods
```

## Interview Questions

1. **What is an interface?** A reference type defining a contract of abstract methods, default methods, static methods, and constants
2. **Interface vs Abstract class?** Interface: multiple inheritance, no state; Abstract class: single inheritance, can have state
3. **What are default methods?** Methods with implementations in interfaces, added in Java 8 for backward compatibility
4. **Diamond problem?** When two interfaces provide default methods with the same signature—resolved by explicit override
5. **`@FunctionalInterface`?** Annotation marking interfaces with exactly one abstract method for lambda support
6. **Can interfaces have constructors?** No—interfaces cannot be instantiated
7. **What are interface constants?** Fields in interfaces are implicitly `public static final`
8. **Private methods in interfaces?** Java 9+ feature for sharing code between default methods

## Exercises

1. Create a `Comparable<T>` interface and implement it for a `Student` class that compares students by GPA
2. Design a `Logger` interface with default methods for different log levels and a static factory method
3. Create a `Validator<T>` interface with a `validate(T)` method and a default `and()` method that combines validators
4. Implement a `Cache<K, V>` interface with default methods for `getOrCompute()` and `invalidateAll()`

## Assignments

1. **Plugin System**: Design an interface-based plugin system where plugins can be loaded dynamically. Include `Plugin`, `PluginManager`, and `PluginLoader` interfaces with default methods
2. **Strategy Pattern**: Implement a sorting system using interfaces where different sorting strategies (bubble sort, quicksort, merge sort) implement a common `SortStrategy` interface
3. **Event System**: Create an event-driven system with `EventListener<T>` interfaces, `EventPublisher`, and various event types

## Mini Project

**Interface-Based Payment Gateway**

Build a payment gateway using interfaces for extensibility:

- `PaymentMethod` interface with `process()`, `refund()`, and `validate()` methods
- `CreditCard`, `PayPal`, `BankTransfer` classes implementing `PaymentMethod`
- `PaymentGateway` class that routes payments to the correct processor
- `ReceiptGenerator` interface with default receipt formatting methods
- `FraudDetector` interface with composable detection strategies

Requirements:
- Use default methods for common receipt formatting
- Use static methods for utility functions (validation, formatting)
- Apply the Strategy pattern for fraud detection
- Support adding new payment methods without modifying existing code

## Summary

Interfaces are a fundamental Java construct for defining contracts and enabling polymorphism. Key takeaways:

- **Interfaces define contracts**: Abstract methods must be implemented by implementing classes
- **Default methods** (Java 8+) provide optional implementations without breaking existing code
- **Static methods** in interfaces provide utility functions scoped to the contract
- **Private methods** (Java 9+) allow code sharing between default methods
- **Multiple interfaces** can be implemented by a single class (multiple type inheritance)
- **Diamond problem** is resolved by explicitly overriding conflicting default methods
- **`@FunctionalInterface`** enables lambda expressions for single-method interfaces
- **Interface vs Abstract class**: Use interfaces for pure contracts, abstract classes for shared state
- **Performance**: Interface calls are slightly slower than direct calls but JIT can optimize
- **Best practices**: Keep interfaces focused, use ISP, prefer composition over inheritance

## References

- Effective Java, 3rd Edition, Joshua Bloch - Item 19 (Design and Document Interfaces)
- Java Language Specification - Interfaces (https://docs.oracle.com/javase/specs/jls/se21/html/jls-9.html)
- Oracle Java Tutorials - Interfaces (https://docs.oracle.com/javase/tutorial/java/IandI/)
- Head First Design Patterns, 2nd Edition - Interface chapters
- Clean Architecture, Robert Martin - Interface boundaries
