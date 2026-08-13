package academy.javaengineering.oop.internals;

public class PolymorphismInternals {

    static class Shape {
        double getArea() {
            return 0;
        }
    }

    static class Circle extends Shape {
        double radius;
        Circle(double radius) { this.radius = radius; }
        @Override
        double getArea() { return Math.PI * radius * radius; }
    }

    static class Rectangle extends Shape {
        double width, height;
        Rectangle(double w, double h) { width = w; height = h; }
        @Override
        double getArea() { return width * height; }
    }

    public static void main(String[] args) {
        System.out.println("=== Polymorphism Internals ===\n");

        // 1. Compile-Time Polymorphism
        System.out.println("--- Compile-Time (Overloading) ---");
        System.out.println("Same method name, different parameters");
        System.out.println("Compiler resolves at compile time");
        System.out.println("Example: add(int) vs add(double)");

        // 2. Runtime Polymorphism
        System.out.println("\n--- Runtime (Overriding) ---");
        Shape s1 = new Circle(5);
        Shape s2 = new Rectangle(4, 6);
        System.out.println("Circle area: " + s1.getArea());
        System.out.println("Rectangle area: " + s2.getArea());
        System.out.println("JVM decides at runtime");

        // 3. Dynamic Dispatch
        System.out.println("\n--- Dynamic Dispatch ---");
        System.out.println("Object type determines method called");
        System.out.println("Not reference type");
        System.out.println("Enables flexibility");

        // 4. Benefits
        System.out.println("\n--- Benefits ---");
        System.out.println("1. Code reusability");
        System.out.println("2. Loose coupling");
        System.out.println("3. Easy to extend");
    }
}
