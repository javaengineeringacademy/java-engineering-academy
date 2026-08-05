# Bridge Design Pattern

## Overview
Bridge pattern decouples an abstraction from its implementation so that the two can vary independently. It uses composition to bind abstractions with implementations.

## When to Use
- You want to avoid a permanent binding between an abstraction and its implementation
- Both the abstractions and their implementations should be extensible by subclassing
- Changes in implementations should not affect clients

## Code Example

```java
public abstract class Shape {
    protected Color color;

    public Shape(Color color) {
        this.color = color;
    }

    public abstract String draw();
}

public class Circle extends Shape {
    public Circle(Color color) {
        super(color);
    }

    @Override
    public String draw() {
        return "Circle " + color.fill();
    }
}
```

## Common Mistakes
- Creating too many bridge classes for simple hierarchies
- Not identifying the right abstraction and implementation boundaries
- Overcomplicating when inheritance would suffice

## Interview Questions
1. What is the difference between Bridge and Strategy patterns?
2. How does Bridge pattern help with platform independence?
3. When would you choose Bridge over multiple inheritance?
