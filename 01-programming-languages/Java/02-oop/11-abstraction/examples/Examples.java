package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Abstraction Patterns ===\n");

        // WHY: Abstraction hides complexity, defines contracts without implementation details
        // INTERNAL: JVM resolves abstract method calls via vtable at runtime
        // ENGINEERING: Program to interfaces, not implementations

        Shape circle = new Circle(5);
        Shape rect = new Rectangle(4, 6);

        System.out.println("Circle area: " + circle.area());
        System.out.println("Rectangle area: " + rect.area());
        System.out.println("Circle perimeter: " + circle.perimeter());

        // TRADE-OFF: Abstract class vs interface
        // Abstract class: can have state, constructors, protected members
        // Interface: multiple inheritance, no state (until default methods)
        drawShape(circle);
        drawShape(rect);
    }

    static void drawShape(Shape s) {
        System.out.println("Drawing: " + s.getClass().getSimpleName() + " area=" + String.format("%.2f", s.area()));
    }
}

abstract class Shape {
    abstract double area();
    abstract double perimeter();

    public void printInfo() {
        System.out.printf("Area: %.2f, Perimeter: %.2f%n", area(), perimeter());
    }
}

class Circle extends Shape {
    private final double radius;
    Circle(double r) { this.radius = r; }
    @Override double area() { return Math.PI * radius * radius; }
    @Override double perimeter() { return 2 * Math.PI * radius; }
}

class Rectangle extends Shape {
    private final double width, height;
    Rectangle(double w, double h) { this.width = w; this.height = h; }
    @Override double area() { return width * height; }
    @Override double perimeter() { return 2 * (width + height); }
}
