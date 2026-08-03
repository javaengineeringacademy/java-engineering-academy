package academy.javaengineering.oop.polymorphism;

/**
 * Demonstrates compile-time (static) and runtime (dynamic) polymorphism.
 *
 * <p>Polymorphism allows objects to take many forms. Compile-time polymorphism
 * is achieved via method overloading, while runtime polymorphism uses method
 * overriding with inheritance.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Compile-time polymorphism: method overloading</li>
 *   <li>Runtime polymorphism: method overriding with dynamic dispatch</li>
 *   <li>Liskov Substitution Principle</li>
 *   <li>Polymorphic collections and behavior</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @version 1.0
 */
public class PolymorphismExample {

    // ==================== Compile-Time Polymorphism ====================

    /**
     * Payment processor demonstrating method overloading.
     * Different parameter lists enable compile-time polymorphism.
     */
    public static class PaymentProcessor {

        /** Process payment with default currency. */
        public String processPayment(double amount) {
            return processPayment(amount, "USD");
        }

        /** Process payment with specified currency. */
        public String processPayment(double amount, String currency) {
            return "Processed: %.2f %s".formatted(amount, currency);
        }

        /** Process payment with currency and discount. */
        public String processPayment(double amount, String currency, double discountPercent) {
            double discounted = amount * (1 - discountPercent / 100);
            return "Processed: %.2f %s (discount: %.1f%%)".formatted(discounted, currency, discountPercent);
        }

        /** Process payment with item description. */
        public String processPayment(double amount, String currency, String itemDescription) {
            return "Processed: %.2f %s for '%s'".formatted(amount, currency, itemDescription);
        }
    }

    // ==================== Runtime Polymorphism ====================

    /**
     * Shape hierarchy demonstrating runtime polymorphism.
     */
    public static sealed abstract class Shape permits Circle, Rectangle, Triangle {
        protected final String name;

        protected Shape(String name) {
            this.name = name;
        }

        public String getName() { return name; }

        public abstract double area();
        public abstract double perimeter();

        public String describe() {
            return "%s [area=%.2f, perimeter=%.2f]".formatted(name, area(), perimeter());
        }
    }

    public static final class Circle extends Shape {
        private final double radius;

        public Circle(double radius) {
            super("Circle");
            this.radius = radius;
        }

        public double getRadius() { return radius; }

        @Override
        public double area() { return Math.PI * radius * radius; }

        @Override
        public double perimeter() { return 2 * Math.PI * radius; }
    }

    public static final class Rectangle extends Shape {
        private final double width;
        private final double height;

        public Rectangle(double width, double height) {
            super("Rectangle");
            this.width = width;
            this.height = height;
        }

        public double getWidth() { return width; }
        public double getHeight() { return height; }

        @Override
        public double area() { return width * height; }

        @Override
        public double perimeter() { return 2 * (width + height); }

        public boolean isSquare() { return width == height; }
    }

    public static final class Triangle extends Shape {
        private final double a, b, c;

        public Triangle(double a, double b, double c) {
            super("Triangle");
            this.a = a;
            this.b = b;
            this.c = c;
        }

        @Override
        public double area() {
            double s = (a + b + c) / 2;
            return Math.sqrt(s * (s - a) * (s - b) * (s - c));
        }

        @Override
        public double perimeter() { return a + b + c; }
    }

    /**
     * Demonstrates polymorphic behavior with a shape collection.
     */
    public static class ShapeAnalyzer {

        public static double totalArea(Shape[] shapes) {
            double total = 0;
            for (Shape shape : shapes) {
                total += shape.area(); // Dynamic dispatch
            }
            return total;
        }

        public static Shape findLargest(Shape[] shapes) {
            if (shapes == null || shapes.length == 0) return null;
            Shape largest = shapes[0];
            for (int i = 1; i < shapes.length; i++) {
                if (shapes[i].area() > largest.area()) {
                    largest = shapes[i];
                }
            }
            return largest;
        }

        /**
         * Pattern matching with polymorphism - modern Java approach.
         */
        public static String shapeDetails(Shape shape) {
            return switch (shape) {
                case Circle c -> "Circle(r=%.2f)".formatted(c.getRadius());
                case Rectangle r -> r.isSquare()
                        ? "Square(s=%.2f)".formatted(r.getWidth())
                        : "Rectangle(%.2f x %.2f)".formatted(r.getWidth(), r.getHeight());
                case Triangle t -> "Triangle(perimeter=%.2f)".formatted(t.perimeter());
            };
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Polymorphism Demo ===\n");

        // Compile-time polymorphism
        System.out.println("--- Compile-Time (Overloading) ---");
        PaymentProcessor processor = new PaymentProcessor();
        System.out.println(processor.processPayment(99.99));
        System.out.println(processor.processPayment(99.99, "EUR"));
        System.out.println(processor.processPayment(99.99, "USD", 15.0));
        System.out.println(processor.processPayment(99.99, "GBP", "Premium Widget"));

        // Runtime polymorphism
        System.out.println("\n--- Runtime (Overriding) ---");
        Shape[] shapes = {
                new Circle(5.0),
                new Rectangle(4.0, 6.0),
                new Triangle(3.0, 4.0, 5.0),
                new Rectangle(5.0, 5.0) // Square
        };

        for (Shape shape : shapes) {
            System.out.println(ShapeAnalyzer.shapeDetails(shape));
        }

        System.out.printf("%nTotal area: %.2f%n", ShapeAnalyzer.totalArea(shapes));

        Shape largest = ShapeAnalyzer.findLargest(shapes);
        System.out.printf("Largest: %s (area=%.2f)%n", largest.getName(), largest.area());
    }
}
