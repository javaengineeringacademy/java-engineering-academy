package academy.javaengineering.fundamentals.methods;

/**
 * Demonstrates method memory usage patterns.
 */
public class MethodsMemory {

    public static void main(String[] args) {
        System.out.println("=== Methods Memory Demo ===\n");

        // 1. Primitive parameter passing
        System.out.println("--- Primitive Pass by Value ---");
        int original = 10;
        System.out.println("Before method: original = " + original);
        modifyPrimitive(original);
        System.out.println("After method:  original = " + original + " (unchanged)");

        // 2. Reference parameter passing
        System.out.println("\n--- Reference Pass by Value ---");
        StringBuilder sb = new StringBuilder("Hello");
        System.out.println("Before method: sb = " + sb);
        modifyReference(sb);
        System.out.println("After method:  sb = " + sb + " (object modified)");

        // 3. Recursion stack depth
        System.out.println("\n--- Recursion Stack Depth ---");
        long result = recursiveSum(1000);
        System.out.println("Sum 1-1000: " + result);
        System.out.println("Stack frames used: ~1000 (one per call)");

        // 4. Varargs memory
        System.out.println("\n--- Varargs Memory ---");
        printAll("apple", "banana", "cherry");
        printAll("single");

        System.out.println("\n=== Memory Demo Complete ===");
    }

    static void modifyPrimitive(int x) {
        x = 100;  // Modifies local copy only
        System.out.println("Inside method: x = " + x);
    }

    static void modifyReference(StringBuilder sb) {
        sb.append(" World");  // Modifies the heap object
        System.out.println("Inside method: sb = " + sb);
    }

    static long recursiveSum(int n) {
        if (n <= 0) return 0;
        return n + recursiveSum(n - 1);
    }

    static void printAll(String... values) {
        System.out.println("Varargs array length: " + values.length);
        for (String v : values) {
            System.out.println("  - " + v);
        }
    }
}
