package academy.javaengineering.fundamentals.controlflow;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates control flow memory usage patterns.
 */
public class ControlFlowMemory {

    public static void main(String[] args) {
        System.out.println("=== Control Flow Memory Demo ===\n");

        // 1. Stack frame for loops
        System.out.println("--- Loop Stack Usage ---");
        int sum = 0;
        for (int i = 0; i < 1000; i++) {
            sum += i; // All variables on stack
        }
        System.out.println("Sum of 0-999: " + sum);
        System.out.println("Stack usage: ~12 bytes (i, sum, loop counter)");

        // 2. Nested loop memory
        System.out.println("\n--- Nested Loop Memory ---");
        int product = 0;
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                product = i * j;
            }
        }
        System.out.println("Nested loop completed. Stack: O(1) regardless of iterations");

        // 3. Break and continue memory
        System.out.println("\n--- Break/Continue Memory ---");
        List<String> items = new ArrayList<>();
        items.add("apple");
        items.add(null);
        items.add("banana");
        items.add(null);
        items.add("cherry");

        // For-each creates Iterator (heap allocation)
        int count = 0;
        for (String item : items) {
            if (item == null) continue; // Skip nulls
            count++;
            System.out.println("Processing: " + item);
        }
        System.out.println("Processed " + count + " items (Iterator object allocated on heap)");

        // 4. Guard clause memory
        System.out.println("\n--- Guard Clause Memory ---");
        System.out.println("Processing null order: " + processOrder(null));
        System.out.println("Processing valid order: " + processOrder("ORD-001"));

        System.out.println("\n=== Memory Demo Complete ===");
    }

    static String processOrder(String orderId) {
        // Guard clauses - early return, no nested objects
        if (orderId == null) return "Invalid: null order ID";
        if (orderId.isEmpty()) return "Invalid: empty order ID";
        if (!orderId.startsWith("ORD-")) return "Invalid: must start with ORD-";

        return "Processing order: " + orderId;
    }
}
