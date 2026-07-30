# Abstract Classes

## What is an Abstract Class?
Cannot be instantiated. May contain abstract and concrete methods.

```java
public abstract class Shape {
    protected String color;

    public Shape(String color) { this.color = color; }

    public abstract double area();  // Must implement
    public abstract double perimeter();

    public String getColor() { return color; }  // Concrete
}
```

## Abstract Methods
- No body, ends with semicolon
- Must be implemented by concrete subclass
- Class with abstract methods must be abstract

## When to Use
- "Is-a" relationship with shared code
- Template Method pattern
- Need to enforce contract