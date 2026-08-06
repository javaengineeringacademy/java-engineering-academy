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

[Performance considerations and benchmarks]

## Examples

[Code examples demonstrating the concept]

## Internal Working

[How this works under the hood]

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Pitfalls

[Common mistakes and anti-patterns]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
