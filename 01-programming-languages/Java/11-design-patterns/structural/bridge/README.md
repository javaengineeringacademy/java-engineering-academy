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

## Performance

Bridge adds one level of indirection (~5ns per delegation). For I/O-bound or business logic operations, this is negligible. The benefit is structural: without bridge, you need M×N subclasses for M abstractions × N implementations. Bridge reduces this to M+N classes.

## Examples

```java
// Shape × Color bridge
interface Color {
    String fill();
}

class Red implements Color {
    @Override public String fill() { return "Red"; }
}

class Blue implements Color {
    @Override public String fill() { return "Blue"; }
}

abstract class Shape {
    protected Color color;
    
    Shape(Color color) { this.color = color; }
    
    abstract void draw();
}

class Circle extends Shape {
    Circle(Color color) { super(color); }
    
    @Override
    void draw() {
        System.out.println("Drawing Circle in " + color.fill());
    }
}

class Square extends Shape {
    Square(Color color) { super(color); }
    
    @Override
    void draw() {
        System.out.println("Drawing Square in " + color.fill());
    }
}

// Usage - combine any shape with any color
Shape redCircle = new Circle(new Red());
Shape blueSquare = new Square(new Blue());
redCircle.draw();   // Drawing Circle in Red
blueSquare.draw();  // Drawing Square in Blue
```

## Internal Working

Bridge separates two hierarchies: abstraction (Shape) and implementation (Color). The abstraction holds a reference to the implementation interface. Concrete abstractions call implementation methods through this reference. This decouples the two — you can add new shapes without touching colors, and new colors without touching shapes. The two hierarchies evolve independently.

## Why This Concept Exists

When you have two independent dimensions of variation (shape × color, platform × format), combining them with inheritance creates a class explosion: RedCircle, BlueCircle, RedSquare, BlueSquare. Bridge uses composition instead: Shape holds a Color reference. Adding Green requires one new Color class, not three new shape-color classes. This is the same principle as "favor composition over inheritance."

## Pitfalls

1. **Over-engineering**: Simple hierarchies (2×2) don't need bridge — inheritance is clearer
2. **Wrong boundaries**: If abstraction and implementation are not independent, bridge adds complexity without benefit
3. **Indirection confusion**: Two levels of hierarchy can be harder to follow than direct inheritance
4. **Configuration**: Selecting the right implementation at runtime requires a factory or DI
5. **Limited support**: Not all languages handle bridge well — Java's single inheritance makes it natural

## References

- [Refactoring.Guru - Bridge Pattern](https://refactoring.guru/design-patterns/bridge)
- [Head First Design Patterns - Bridge Pattern](https://www.oreilly.com/library/view/head-first-design/0596007124/)
- [Java AWT Component hierarchy](https://docs.oracle.com/en/java/javase/21/docs/api/java.desktop/java/awt/package-summary.html)
