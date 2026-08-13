package academy.javaengineering.oop.internals;

public class SealedHierarchyInternals {

    sealed interface Shape permits Circle, Rectangle, Triangle {}
    
    record Circle(double radius) implements Shape {}
    record Rectangle(double width, double height) implements Shape {}
    record Triangle(double base, double height) implements Shape {}

    public static void main(String[] args) {
        System.out.println("=== Sealed Hierarchy Internals ===\n");

        // 1. Sealed Hierarchy
        System.out.println("--- Sealed Hierarchy ---");
        Shape shape = new Circle(5);
        System.out.println("Shape: " + shape);

        // 2. Pattern Matching
        System.out.println("\n--- Pattern Matching ---");
        String description = switch (shape) {
            case Circle c -> "Circle with radius " + c.radius();
            case Rectangle r -> "Rectangle " + r.width() + "x" + r.height();
            case Triangle t -> "Triangle " + t.base() + "x" + t.height();
        };
        System.out.println("Description: " + description);

        // 3. Exhaustive Switch
        System.out.println("\n--- Exhaustive Switch ---");
        System.out.println("Compiler ensures all cases covered");
        System.out.println("No default needed");
        System.out.println("Type-safe");
    }
}
