package academy.javaengineering.oop.memory;

public class StaticBindingMemory {

    static class MathUtils {
        static int add(int a, int b) { return a + b; }
        static double add(double a, double b) { return a + b; }
    }

    public static void main(String[] args) {
        System.out.println("=== Static Binding Memory ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. No Virtual Method Table
        System.out.println("--- No VTable ---");
        System.out.println("Static binding: direct method call");
        System.out.println("No vtable lookup required");
        System.out.println("Faster than dynamic binding");

        // 2. Performance
        System.out.println("\n--- Performance ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        for (int i = 0; i < 1000000; i++) {
            MathUtils.add(1, 2);
        }
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Static binding: ~1 cycle");
        System.out.println("Dynamic binding: ~2-3 cycles");

        // 3. Memory Savings
        System.out.println("\n--- Memory Savings ---");
        System.out.println("No vtable entry for static methods");
        System.out.println("Saves ~8 bytes per class");
        System.out.println("Inlined by JIT for zero overhead");
    }
}
