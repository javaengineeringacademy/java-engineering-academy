package academy.javaengineering.jvm.jit;

/**
 * Solution 3: Deoptimization Detection
 */
public class Solution3 {

    static abstract class Shape {
        abstract double area();
    }

    static class Circle extends Shape {
        double r;
        Circle(double r) { this.r = r; }
        double area() { return Math.PI * r * r; }
    }

    static class Rectangle extends Shape {
        double w, h;
        Rectangle(double w, double h) { this.w = w; this.h = h; }
        double area() { return w * h; }
    }

    static class Triangle extends Shape {
        double b, h;
        Triangle(double b, double h) { this.b = b; this.h = h; }
        double area() { return 0.5 * b * h; }
    }

    public static void main(String[] args) {
        System.out.println("=== Deoptimization Detection ===\n");

        System.out.println("Run with: java -XX:+PrintCompilation Solution3");
        System.out.println("Look for 'Deoptimization' events\n");

        // Phase 1: Monomorphic (one type)
        System.out.println("--- Phase 1: Monomorphic ---");
        Shape[] shapes = new Shape[10000];
        for (int i = 0; i < shapes.length; i++) shapes[i] = new Circle(1.0);
        long start = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            for (Shape s : shapes) s.area();
        }
        long mono = System.nanoTime() - start;
        System.out.printf("Monomorphic: %d ms%n", mono / 1_000_000);

        // Phase 2: Bimorphic (two types)
        System.out.println("\n--- Phase 2: Bimorphic ---");
        for (int i = 0; i < shapes.length / 2; i++) shapes[i] = new Rectangle(1.0, 1.0);
        start = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            for (Shape s : shapes) s.area();
        }
        long bimorphic = System.nanoTime() - start;
        System.out.printf("Bimorphic: %d ms%n", bimorphic / 1_000_000);

        // Phase 3: Polymorphic (three types - triggers deopt)
        System.out.println("\n--- Phase 3: Polymorphic (may trigger deoptimization) ---");
        for (int i = 0; i < shapes.length / 3; i++) shapes[i] = new Triangle(1.0, 1.0);
        start = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            for (Shape s : shapes) s.area();
        }
        long poly = System.nanoTime() - start;
        System.out.printf("Polymorphic: %d ms%n", poly / 1_000_000);
    }
}
