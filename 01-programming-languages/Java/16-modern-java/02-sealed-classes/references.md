# Sealed Classes References

## Official Documentation

- [JEP 409: Sealed Classes](https://openjdk.org/jeps/409)
- [JEP 360: Sealed Classes (Preview)](https://openjdk.org/jeps/360)
- [Java Language Specification - Sealed Classes](https://docs.oracle.com/javase/specs/jls/se17/html/jls-8.html#jls-8.1.1.2)

## Key Concepts

| Concept | Description |
|---------|-------------|
| Sealed Class | A class that restricts which classes can extend it |
| Permits Clause | Lists the allowed subclasses |
| Final Subclass | Cannot be extended further |
| Sealed Subclass | Can only be extended by its own permitted subclasses |
| Non-sealed Subclass | Open to extension by any class |

## Code Examples

### Basic Sealed Hierarchy
```java
public sealed interface Shape 
    permits Circle, Rectangle, Triangle {}

public record Circle(double radius) implements Shape {}
public record Rectangle(double width, double height) implements Shape {}
public record Triangle(double base, double height) implements Shape {}
```

### Sealed with Inheritance
```java
public sealed abstract class Animal 
    permits Dog, Cat {}

public final class Dog extends Animal {}
public non-sealed class Cat extends Animal {}
```

### Sealed with Pattern Matching
```java
String describe(Shape shape) {
    return switch (shape) {
        case Circle c -> "Circle: " + c.radius();
        case Rectangle r -> "Rectangle: " + r.width() + "x" + r.height();
        case Triangle t -> "Triangle: " + t.base() + "x" + t.height();
    };
}
```

## Common Patterns

1. **Algebraic Data Types:** `sealed interface Result<T> permits Success, Failure {}`
2. **State Machines:** `sealed interface State permits Idle, Running, Stopped {}`
3. **Domain Models:** `sealed interface Payment permits CreditCard, BankTransfer {}`
