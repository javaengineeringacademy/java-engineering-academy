# Keywords - Solutions

```java
// Practice 1: Final class
final class ImmutablePoint {
    final int x, y;
    ImmutablePoint(int x, int y) { this.x = x; this.y = y; }
}
// class SubPoint extends ImmutablePoint {} // ERROR: cannot inherit from final

// Practice 2: Static factory
class Color {
    final int r, g, b;
    Color(int r, int g, int b) { this.r = r; this.g = g; this.b = b; }
    static Color of(int r, int g, int b) { return new Color(r, g, b); }
    static Color red() { return new Color(255, 0, 0); }
}

// Practice 3: This vs Super
class Animal {
    String type;
    Animal(String type) { this.type = type; }
    Animal() { this("Unknown"); }
}
class Dog extends Animal {
    String name;
    Dog(String name) {
        super("Dog");
        this.name = name;
    }
}

// Practice 4: Abstract class
abstract class Shape {
    abstract double area();
    void describe() { System.out.println("Area: " + area()); }
}
class Circle extends Shape {
    double radius;
    Circle(double r) { this.radius = r; }
    double area() { return Math.PI * radius * radius; }
}

public class KeywordsSolutions {
    public static void main(String[] args) {
        // Static factory
        Color red = Color.red();
        System.out.printf("Red: %d,%d,%d%n", red.r, red.g, red.b);

        // This/Super
        Dog dog = new Dog("Rex");
        System.out.println(dog.name + " is a " + dog.type);

        // Abstract
        Shape circle = new Circle(5);
        circle.describe();

        // Pattern matching instanceof
        Object obj = "Hello World";
        if (obj instanceof String s && s.length() > 5) {
            System.out.println("Long string: " + s);
        }
    }
}
```
