package academy.javaengineering.oop.examples;

/**
 * Polymorphism Examples - Why polymorphism exists and how to use it.
 * 
 * WHY POLYMORPHISM EXISTS:
 * - One interface, many forms
 * - Code flexibility and extensibility
 * - Runtime behavior selection
 * 
 * TYPES:
 * - Compile-time (method overloading)
 * - Runtime (method overriding)
 * 
 * ENGINEERING DECISION: Program to interface, not implementation.
 */
public class PolymorphismExamples {

    public static void main(String[] args) {
        System.out.println("=== Polymorphism Examples ===\n");

        // Example 1: Runtime Polymorphism
        example1_RuntimePolymorphism();

        // Example 2: Interface Polymorphism
        example2_InterfacePolymorphism();

        // Example 3: Collection Polymorphism
        example3_CollectionPolymorphism();
    }

    /**
     * WHY: Runtime polymorphism enables dynamic method dispatch.
     * 
     * INTERNAL: JVM uses vtable to find the correct method at runtime.
     */
    private static void example1_RuntimePolymorphism() {
        System.out.println("--- Example 1: Runtime Polymorphism ---");

        Shape circle = new Circle(5);
        Shape rectangle = new Rectangle(4, 6);

        System.out.println("Circle area: " + circle.calculateArea());
        System.out.println("Rectangle area: " + rectangle.calculateArea());

        // Polymorphic method call
        printShapeInfo(circle);
        printShapeInfo(rectangle);
    }

    /**
     * WHY: Interface polymorphism enables loose coupling.
     * 
     * ENGINEERING DECISION: Depend on abstractions, not concretions.
     */
    private static void example2_InterfacePolymorphism() {
        System.out.println("\n--- Example 2: Interface Polymorphism ---");

        Drawable circle = new Circle(3);
        Drawable rectangle = new Rectangle(2, 4);

        circle.draw();
        rectangle.draw();
    }

    /**
     * WHY: Collections can hold any subtype.
     * 
     * ENGINEERING DECISION: Use List<Interface> for flexibility.
     */
    private static void example3_CollectionPolymorphism() {
        System.out.println("\n--- Example 3: Collection Polymorphism ---");

        java.util.List<Shape> shapes = new java.util.ArrayList<>();
        shapes.add(new Circle(5));
        shapes.add(new Rectangle(4, 6));
        shapes.add(new Triangle(3, 4));

        for (Shape shape : shapes) {
            System.out.println(shape.getClass().getSimpleName() + " area: " + shape.calculateArea());
        }
    }

    private static void printShapeInfo(Shape shape) {
        System.out.println("Shape: " + shape.getClass().getSimpleName() + ", Area: " + shape.calculateArea());
    }

    // Supporting classes
    interface Drawable {
        void draw();
    }

    static abstract class Shape implements Drawable {
        public abstract double calculateArea();
    }

    static class Circle extends Shape {
        private double radius;

        public Circle(double radius) {
            this.radius = radius;
        }

        @Override
        public double calculateArea() {
            return Math.PI * radius * radius;
        }

        @Override
        public void draw() {
            System.out.println("Drawing circle with radius " + radius);
        }
    }

    static class Rectangle extends Shape {
        private double width;
        private double height;

        public Rectangle(double width, double height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public double calculateArea() {
            return width * height;
        }

        @Override
        public void draw() {
            System.out.println("Drawing rectangle " + width + " x " + height);
        }
    }

    static class Triangle extends Shape {
        private double base;
        private double height;

        public Triangle(double base, double height) {
            this.base = base;
            this.height = height;
        }

        @Override
        public double calculateArea() {
            return 0.5 * base * height;
        }

        @Override
        public void draw() {
            System.out.println("Drawing triangle with base " + base);
        }
    }
}
