package academy.javaengineering.fundamentals.operators;

/**
 * Demonstrates operator memory usage and performance characteristics.
 */
public class OperatorsMemory {

    public static void main(String[] args) {
        System.out.println("=== Operators Memory Demo ===\n");

        // 1. Primitive vs boxed operator performance
        System.out.println("--- Primitive vs Boxed Operations ---");
        int primitiveCount = 10_000_000;
        Integer boxedCount = 10_000_000;

        long startPrimitive = System.nanoTime();
        int sumP = 0;
        for (int i = 0; i < primitiveCount; i++) {
            sumP += i;
        }
        long timePrimitive = System.nanoTime() - startPrimitive;

        long startBoxed = System.nanoTime();
        Integer sumB = 0;
        for (int i = 0; i < boxedCount; i++) {
            sumB += i;  // Autoboxing on each iteration
        }
        long timeBoxed = System.nanoTime() - startBoxed;

        System.out.println("Primitive sum: " + sumP + " (time: " + timePrimitive + " ns)");
        System.out.println("Boxed sum:     " + sumB + " (time: " + timeBoxed + " ns)");
        System.out.println("Boxed is ~" + (timeBoxed / Math.max(timePrimitive, 1)) + "x slower due to autoboxing");

        // 2. String concatenation memory
        System.out.println("\n--- String Concatenation Memory ---");
        String literals = "Hello" + " " + "World";
        String dynamic = "Hello" + " " + System.currentTimeMillis();
        System.out.println("Compile-time constant: " + literals + " (single pool entry)");
        System.out.println("Dynamic concatenation: " + dynamic + " (StringBuilder created)");

        // 3. Bitwise operation efficiency
        System.out.println("\n--- Bitwise Operation Efficiency ---");
        int value = 0xFF;
        System.out.println("Original: " + value + " (binary: " + Integer.toBinaryString(value) + ")");
        System.out.println("Left shift 1:  " + (value << 1) + " (multiply by 2)");
        System.out.println("Right shift 1: " + (value >> 1) + " (divide by 2)");
        System.out.println("AND mask:      " + (value & 0x0F) + " (extract lower nibble)");

        // 4. Comparison operator memory
        System.out.println("\n--- Comparison Memory ---");
        int a = 5;
        int b = 10;
        boolean result = a < b;  // Returns 1 byte boolean
        System.out.println("Comparison result: " + result + " (1 byte on stack)");

        System.out.println("\n=== Memory Demo Complete ===");
    }
}
