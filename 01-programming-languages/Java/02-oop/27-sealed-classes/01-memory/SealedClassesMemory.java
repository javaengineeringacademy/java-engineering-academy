package academy.javaengineering.oop.memory;

public class SealedClassesMemory {

    sealed interface Shape permits Circle, Rectangle {}
    record Circle(double radius) implements Shape {}
    record Rectangle(double width, double height) implements Shape {}

    public static void main(String[] args) {
        System.out.println("=== Sealed Classes Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Record Memory
        System.out.println("--- Record Memory ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Circle circle = new Circle(5);
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Circle record: " + (after - before) + " bytes");
        System.out.println("Same as regular class");

        // 2. Sealed Class Overhead
        System.out.println("\n--- Sealed Class Overhead ---");
        System.out.println("No extra memory for sealed");
        System.out.println("Compiler metadata only");
        System.out.println("No runtime overhead");

        // 3. Pattern Matching
        System.out.println("\n--- Pattern Matching ---");
        System.out.println("Exhaustive switch: no default needed");
        System.out.println("Compiler verifies all cases");
        System.out.println("Optimized bytecode");
    }
}
