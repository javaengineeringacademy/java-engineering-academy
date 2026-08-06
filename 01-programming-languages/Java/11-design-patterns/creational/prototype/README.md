# Prototype Pattern

## Overview
 Prototype creates new objects by copying an existing instance, avoiding expensive initialization.

## When to Use
- Object creation is expensive
- You need many similar objects
- Avoiding subclassing
- Runtime determines object type

## Code Structure

### Prototype Interface
```java
public interface Shape extends Cloneable {
    Shape clone();
    void draw();
}
```

### Concrete Prototype
```java
public class Circle implements Shape {
    private double radius;
    private String color;

    public Circle(Circle source) {
        this.radius = source.radius;
        this.color = source.color;
    }

    @Override
    public Circle clone() {
        return new Circle(this);
    }
}
```

### Usage
```java
Circle original = new Circle(5.0, "red");
Circle copy = original.clone();
copy.setColor("blue"); // Original unchanged
```

## Common Mistakes
1. Shallow copying mutable fields
2. Not implementing Cloneable properly
3. Forgetting to deep copy collections
4. Creating too many prototype registries

## Interview Questions
1. What is the difference between shallow and deep copy?
2. How does Prototype differ from Factory Method?
3. When would you use Prototype instead of Factory?
4. What are the problems with Java's Cloneable?
5. How do you implement a prototype registry?

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
