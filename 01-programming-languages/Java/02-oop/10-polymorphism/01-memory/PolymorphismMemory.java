package academy.javaengineering.oop.memory;

public class PolymorphismMemory {

    static class Shape {
        double getArea() { return 0; }
    }

    static class Circle extends Shape {
        double radius;
        Circle(double r) { radius = r; }
        @Override
        double getArea() { return Math.PI * radius * radius; }
    }

    public static void main(String[] args) {
        System.out.println("=== Polymorphism Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Polymorphic Object Size
        System.out.println("--- Polymorphic Object ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Shape circle = new Circle(5);
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Circle (as Shape): " + (after - before) + " bytes");
        System.out.println("Same as Circle - no wrapper");

        // 2. Virtual Method Table
        System.out.println("\n--- Virtual Method Table ---");
        System.out.println("Each class has vtable");
        System.out.println("Cost: 8 bytes per object (pointer)");
        System.out.println("Lookup: ~2-3 cycles");

        // 3. Polymorphism vs Switch
        System.out.println("\n--- Polymorphism vs Switch ---");
        System.out.println("Polymorphism: O(1) method lookup");
        System.out.println("Switch: O(n) pattern matching");
        System.out.println("Polymorphism preferred for OOP");
    }
}
