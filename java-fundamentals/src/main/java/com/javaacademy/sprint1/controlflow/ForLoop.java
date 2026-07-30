package com.javaacademy.sprint1.controlflow;

/**
 * ForLoop - Demonstrates for, for-each, and labeled loops.
 * 
 * <p><b>Loop Types:</b>
 * <ul>
 *   <li><b>for (init; condition; update):</b> Traditional counter loop</li>
 *   <li><b>for-each (enhanced for):</b> Iterate collections/arrays (no index)</li>
 *   <li><b>Labeled loops:</b> break/continue outer loop</li>
 * </ul>
 * 
 * <p><b>Real-world analogy:</b> 
 * - for = "Do this 10 times"
 * - for-each = "For each item in the box, do this"
 * 
 * <p><b>Best Practices:</b>
 * <ul>
 *   <li>Prefer for-each when index not needed (cleaner, less error-prone)</li>
 *   <li>Use traditional for when you need index or complex iteration</li>
 *   <li>Avoid labeled loops - refactor to methods instead</li>
 * </ul>
 * 
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class ForLoop {

    private ForLoop() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        int[] numbers = {1, 2, 3, 4, 5};
        String[] names = {"Alice", "Bob", "Charlie"};

        System.out.println("=== For Loops ===\n");

        // Traditional for loop
        System.out.println("--- Traditional for ---");
        for (int i = 0; i < numbers.length; i++) {
            System.out.println("Index " + i + ": " + numbers[i]);
        }

        // For loop variations
        System.out.println("\n--- For Variations ---");
        // Count down
        for (int i = 5; i >= 1; i--) {
            System.out.print(i + " ");
        }
        System.out.println();

        // Multiple variables
        for (int i = 0, j = 10; i < j; i++, j--) {
            System.out.println("i=" + i + ", j=" + j);
        }

        // Infinite loop (use break inside)
        System.out.println("\n--- Infinite for (with break) ---");
        for (int i = 0; ; i++) {
            if (i == 3) break;
            System.out.print(i + " ");
        }
        System.out.println();

        // For-each loop (enhanced for)
        System.out.println("\n--- For-Each (Enhanced For) ---");
        for (int num : numbers) {
            System.out.println("Value: " + num);
        }

        // For-each with strings
        System.out.println("\nNames:");
        for (String name : names) {
            System.out.println("  " + name);
        }

        // For-each limitation: no index access
        System.out.println("\n--- For-Each Limitation ---");
        // Cannot modify array elements directly (only local copy)
        for (int num : numbers) {
            num = num * 2; // Modifies local copy only!
        }
        System.out.println("Original array unchanged: " + java.util.Arrays.toString(numbers));

        // To modify, use traditional for
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] *= 2;
        }
        System.out.println("After modification: " + java.util.Arrays.toString(numbers));

        // Labeled loops (break/continue outer loop)
        System.out.println("\n--- Labeled Loops ---");
        outer: for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                if (i == 2 && j == 2) {
                    System.out.println("Breaking outer loop at i=2, j=2");
                    break outer; // Breaks outer loop
                }
                System.out.println("i=" + i + ", j=" + j);
            }
        }

        // Continue with label
        System.out.println("\n--- Continue Label ---");
        outer: for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                if (j == 2) {
                    System.out.println("Skipping j=2, continuing outer i=" + i);
                    continue outer; // Skips to next iteration of outer
                }
                System.out.println("i=" + i + ", j=" + j);
            }
        }

        // Expected output shows all for loop variants
    }
}