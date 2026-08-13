package academy.javaengineering.oop.memory;

public class AbstractClassesMemory {

    abstract static class Shape {
        String color;
        Shape(String color) { this.color = color; }
        abstract double getArea();
    }

    static class Circle extends Shape {
        double radius;
        Circle(String color, double radius) {
            super(color);
            this.radius = radius;
        }
        double getArea() { return Math.PI * radius * radius; }
    }

    public static void main(String[] args) {
        System.out.println("=== Abstract Classes Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Abstract Class Cannot Be Instantiated
        System.out.println("--- Abstract Class Memory ---");
        System.out.println("No heap allocation for abstract class");
        System.out.println("Only concrete subclasses use heap");
        System.out.println("Abstract class: Metaspace only");

        // 2. Concrete Subclass Size
        System.out.println("\n--- Concrete Subclass ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Circle circle = new Circle("Red", 5);
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Circle object: " + (after - before) + " bytes");
        System.out.println("Contains: Shape fields + Circle fields");

        // 3. Inheritance Memory
        System.out.println("\n--- Inheritance Memory ---");
        System.out.println("Parent fields included in child");
        System.out.println("No separate parent object");
        System.out.println("Memory: header + parent fields + child fields");
    }
}
