package academy.javaengineering.fundamentals.wrapperclasses;

/**
 * Demonstrates wrapper class memory usage patterns.
 */
public class WrapperClassesMemory {

    public static void main(String[] args) {
        System.out.println("=== Wrapper Classes Memory Demo ===\n");

        // 1. Primitive vs boxed memory
        System.out.println("--- Primitive vs Boxed Memory ---");
        int primitive = 42;
        Integer boxed = 42;
        System.out.println("Primitive: 4 bytes on stack");
        System.out.println("Boxed: 8 bytes reference + 16 bytes object = 24 bytes");

        // 2. Autoboxing in loops
        System.out.println("\n--- Autoboxing in Loops ---");
        long startPrimitive = System.nanoTime();
        int sumP = 0;
        for (int i = 0; i < 10_000_000; i++) {
            sumP += i;
        }
        long timePrimitive = System.nanoTime() - startPrimitive;

        long startBoxed = System.nanoTime();
        Integer sumB = 0;
        for (int i = 0; i < 10_000_000; i++) {
            sumB += i;  // Autoboxing each iteration
        }
        long timeBoxed = System.nanoTime() - startBoxed;

        System.out.println("Primitive: " + timePrimitive + " ns");
        System.out.println("Boxed: " + timeBoxed + " ns");
        System.out.println("Boxed is ~" + (timeBoxed / Math.max(timePrimitive, 1)) + "x slower");

        // 3. Integer cache
        System.out.println("\n--- Integer Cache Memory ---");
        Integer a = 127;
        Integer b = 127;
        System.out.println("Cached values: -128 to 127");
        System.out.println("127 == 127: " + (a == b) + " (same object)");

        System.out.println("\n=== Memory Demo Complete ===");
    }
}
