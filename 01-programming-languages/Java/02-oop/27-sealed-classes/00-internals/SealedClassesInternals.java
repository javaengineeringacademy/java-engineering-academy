package academy.javaengineering.oop.internals;

public class SealedClassesInternals {

    sealed interface Shape permits Circle, Rectangle, Triangle {}
    
    record Circle(double radius) implements Shape {}
    record Rectangle(double width, double height) implements Shape {}
    record Triangle(double base, double height) implements Shape {}

    public static void main(String[] args) {
        System.out.println("=== Sealed Classes Internals ===\n");

        // 1. Sealed Classes
        System.out.println("--- Sealed Classes ---");
        Shape shape = new Circle(5);
        System.out.println("Shape type: " + shape.getClass().getSimpleName());
        System.out.println("Only permitted classes can implement");

        // 2. Permits Clause
        System.out.println("\n--- Permits Clause ---");
        System.out.println("sealed interface Shape permits Circle, Rectangle, Triangle");
        System.out.println("Compiler enforces permitted classes");
        System.out.println("Exhaustive pattern matching");

        // 3. Benefits
        System.out.println("\n--- Benefits ---");
        System.out.println("1. Controlled hierarchy");
        System.out.println("2. Exhaustive switch expressions");
        System.out.println("3. Better type safety");
        System.out.println("4. Compiler optimizations");
    }
}
