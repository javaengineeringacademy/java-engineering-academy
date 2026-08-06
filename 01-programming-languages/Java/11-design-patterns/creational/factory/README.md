# Factory Pattern

## Overview
Factory Method defines an interface for creating objects, allowing subclasses to alter the type of objects created.

## When to Use
- When object creation logic is complex
- When you need to decouple creation from usage
- When the exact type isn't known until runtime
- When you need to return different implementations

## Code Structure

### Product Interface
```java
public interface Shape {
    void draw();
    double area();
}
```

### Factory
```java
public class ShapeFactory {
    public static Shape createShape(String type, double... dims) {
        return switch (type.toLowerCase()) {
            case "circle" -> new Circle(dims[0]);
            case "rectangle" -> new Rectangle(dims[0], dims[1]);
            default -> throw new IllegalArgumentException();
        };
    }
}
```

### Usage
```java
Shape circle = ShapeFactory.createShape("circle", 5.0);
circle.draw();
```

## Common Mistakes
1. Over-engineering with abstract classes when switch is enough
2. Not validating input parameters
3. Creating factory for simple object creation
4. Tight coupling between factory and concrete classes

## Interview Questions
1. What is the difference between Factory and Abstract Factory?
2. When would you use a static factory method?
3. How does Factory pattern support Open/Closed Principle?
4. Can Factory pattern be used with dependency injection?
5. What are the trade-offs of using Factory pattern?

## Performance

Factory method invocation adds ~10-50ns overhead (a switch/map lookup plus constructor call). This is negligible compared to object initialization cost. For hot paths, cache factory results or use a registry pattern with pre-built instances. Reflection-based factories (e.g., `Class.forName`) add ~1-5 microseconds — avoid in performance-critical code.

## Examples

```java
// Payment processor factory
interface PaymentProcessor {
    void processPayment(double amount);
}

class CreditCardProcessor implements PaymentProcessor {
    @Override
    public void processPayment(double amount) {
        System.out.println("Charging credit card: $" + amount);
    }
}

class PayPalProcessor implements PaymentProcessor {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing PayPal: $" + amount);
    }
}

class PaymentProcessorFactory {
    public static PaymentProcessor create(String type) {
        return switch (type.toLowerCase()) {
            case "credit" -> new CreditCardProcessor();
            case "paypal" -> new PayPalProcessor();
            default -> throw new IllegalArgumentException("Unknown: " + type);
        };
    }
}

// Usage
PaymentProcessor processor = PaymentProcessorFactory.create("credit");
processor.processPayment(99.99);
```

## Internal Working

The factory method encapsulates object creation logic in a centralized location. When called, it evaluates input (type string, enum, config) and returns the appropriate concrete implementation. The client code depends only on the product interface, not on concrete classes. The factory uses polymorphism internally — the switch statement or map lookup resolves to a specific constructor call.

## Why This Concept Exists

Object creation logic often depends on runtime conditions: configuration, user input, or environment. Without a factory, creation code is scattered across the application with `new` calls and conditionals. A factory centralizes this logic, makes it testable (you can mock the factory), and decouples the client from concrete types. It also enables returning different implementations based on context.

## Pitfalls

1. **Over-engineering**: Simple constructors work fine when creation logic is trivial
2. **God factory**: A single factory handling too many types becomes a maintenance burden
3. **Hidden dependencies**: Factory masks which class is actually being created
4. **Testing complexity**: Factory itself needs testing, and mocking factories requires frameworks
5. **Violation of SRP**: Factory that does creation + validation + configuration violates single responsibility

## References

- [Refactoring.Guru - Factory Method](https://refactoring.guru/design-patterns/factory-method)
- [Head First Design Patterns - Factory Pattern](https://www.oreilly.com/library/view/head-first-design/0596007124/)
- [Effective Java - Item 53: Prefer interfaces to reflection](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
