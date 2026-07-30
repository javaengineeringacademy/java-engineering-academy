package com.javaacademy.sprint1.methods;

/**
 * VarargsDemo - Demonstrates variable arguments (varargs) in Java.
 * 
 * <p><b>Varargs Syntax:</b> {@code type... parameterName}
 * <ul>
 *   <li>Must be the LAST parameter</li>
 *   <li>Only ONE varargs per method</li>
 *   <li>Inside method, treated as array</li>
 *   <li>Can pass zero, one, or multiple arguments</li>
 *   <li>Can also pass an array directly</li>
 * </ul>
 * 
 * <p><b>Real-world analogy:</b> Like a function that accepts "any number of items" -
 * you can hand it 0, 1, or 100 items without changing the signature.
 * 
 * <p><b>Performance Note:</b> Creates array each call. 
 * For hot paths, consider overloaded methods for common cases (0, 1, 2 args).
 * 
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class VarargsDemo {

    private VarargsDemo() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("=== Varargs Demo ===\n");

        // Basic varargs usage
        System.out.println("--- Basic Usage ---");
        printNumbers(1, 2, 3);
        printNumbers(10);
        printNumbers(); // Zero arguments OK

        // Passing array
        System.out.println("\n--- Passing Array ---");
        int[] nums = {1, 2, 3, 4, 5};
        printNumbers(nums); // Same as printNumbers(1, 2, 3, 4, 5)

        // Mixed parameters (varargs must be last)
        System.out.println("\n--- Mixed Parameters ---");
        greet("Hello", "Alice", "Bob", "Charlie");
        greet("Hi"); // Only required params

        // Overloading with varargs
        System.out.println("\n--- Overloading with Varargs ---");
        System.out.println("sum() = " + sum());           // calls varargs
        System.out.println("sum(1) = " + sum(1));         // calls varargs (or could have int overload)
        System.out.println("sum(1, 2) = " + sum(1, 2));
        System.out.println("sum(1, 2, 3) = " + sum(1, 2, 3));

        // Varargs with generics (heap pollution warning)
        System.out.println("\n--- Varargs with Generics ---");
        // @SafeVarargs suppresses warning for generic varargs
        printList("A", "B", "C");
        printList(1, 2, 3);

        // Practical: String.join (uses varargs internally)
        System.out.println("\n--- String.join ---");
        String joined = String.join("-", "2024", "01", "15");
        System.out.println("Date: " + joined);

        // Practical: printf
        System.out.println("\n--- printf ---");
        System.out.printf("Name: %s, Age: %d, Score: %.1f%n", "Alice", 25, 95.5);

        // Expected output demonstrates all varargs scenarios
    }

    // Simple varargs method
    static void printNumbers(int... numbers) {
        System.out.print("Numbers: ");
        for (int n : numbers) {
            System.out.print(n + " ");
        }
        System.out.println("(count: " + numbers.length + ")");
    }

    // Required params + varargs
    static void greet(String greeting, String... names) {
        System.out.print(greeting + ": ");
        for (String name : names) {
            System.out.print(name + " ");
        }
        System.out.println();
    }

    // Overloaded sum with varargs
    static int sum(int... numbers) {
        int total = 0;
        for (int n : numbers) total += n;
        return total;
    }

    // Generic varargs (heap pollution possible - use @SafeVarargs)
    @SafeVarargs
    static <T> void printList(T... items) {
        System.out.print("List: ");
        for (T item : items) {
            System.out.print(item + " ");
        }
        System.out.println();
    }
}