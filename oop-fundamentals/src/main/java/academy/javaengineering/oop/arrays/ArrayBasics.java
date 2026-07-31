package academy.javaengineering.oop.arrays;

/**
 * ArrayBasics - Demonstrates array declaration, creation, initialization, and access.
 * 
 * <p><b>Array Fundamentals:</b>
 * <ul>
 *   <li>Fixed-size, homogeneous collection (same type)</li>
 *   <li>Zero-based indexing</li>
 *   <li>Stored on heap (reference type)</li>
 *   <li>Length is fixed after creation</li>
 *   <li>Default values: 0/false/null</li>
 * </ul>
 * 
 * <p><b>Real-world analogy:</b> Like a row of numbered lockers - 
 * each locker holds one item of the same type, 
 * you access by number (index), can't add more lockers.
 * 
 * <p><b>Declaration Styles:</b>
 * <pre>
 * int[] arr;           // Preferred (type + brackets)
 * int arr[];           // Valid but discouraged (C-style)
 * int[] arr = new int[5];
 * int[] arr = {1, 2, 3};
 * int[] arr = new int[]{1, 2, 3}; // Anonymous array
 * </pre>
 * 
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class ArrayBasics {

    private ArrayBasics() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("=== Array Basics ===\n");

        // Declaration and creation
        System.out.println("--- Declaration & Creation ---");
        int[] numbers;                    // Declared, not initialized (null)
        numbers = new int[5];             // Created: [0, 0, 0, 0, 0]
        System.out.println("Length: " + numbers.length); // 5

        // Declaration with initialization
        int[] scores = new int[]{90, 85, 95, 78, 92};
        System.out.println("Scores: " + java.util.Arrays.toString(scores));

        // Array literal (only in declaration)
        String[] names = {"Alice", "Bob", "Charlie"};
        System.out.println("Names: " + java.util.Arrays.toString(names));

        // Anonymous array (useful for method arguments)
        System.out.println("\n--- Anonymous Array ---");
        printArray(new int[]{1, 2, 3, 4, 5});

        // Accessing elements
        System.out.println("\n--- Element Access ---");
        System.out.println("First: " + scores[0]);
        System.out.println("Last: " + scores[scores.length - 1]);
        // scores[5] // ArrayIndexOutOfBoundsException!

        // Modifying elements
        scores[0] = 100;
        System.out.println("Modified: " + java.util.Arrays.toString(scores));

        // Default values
        System.out.println("\n--- Default Values ---");
        int[] intArr = new int[3];
        double[] doubleArr = new double[3];
        boolean[] boolArr = new boolean[3];
        String[] strArr = new String[3];
        System.out.println("int: " + java.util.Arrays.toString(intArr));    // [0, 0, 0]
        System.out.println("double: " + java.util.Arrays.toString(doubleArr)); // [0.0, 0.0, 0.0]
        System.out.println("boolean: " + java.util.Arrays.toString(boolArr)); // [false, false, false]
        System.out.println("String: " + java.util.Arrays.toString(strArr));   // [null, null, null]

        // Array reference vs copy
        System.out.println("\n--- Reference vs Copy ---");
        int[] original = {1, 2, 3};
        int[] reference = original;          // Same array!
        int[] copy = original.clone();       // New array, same content
        int[] copy2 = java.util.Arrays.copyOf(original, original.length); // Another way
        
        reference[0] = 99;
        System.out.println("Original after reference change: " + java.util.Arrays.toString(original)); // [99, 2, 3]
        System.out.println("Copy unchanged: " + java.util.Arrays.toString(copy)); // [1, 2, 3]

        // Iteration
        System.out.println("\n--- Iteration ---");
        for (int i = 0; i < scores.length; i++) {
            System.out.print(scores[i] + " ");
        }
        System.out.println();
        
        for (int score : scores) {
            System.out.print(score + " ");
        }
        System.out.println();

        // Varargs (variable arguments) - syntactic sugar for arrays
        System.out.println("\n--- Varargs ---");
        printNumbers(1, 2, 3);
        printNumbers(new int[]{4, 5, 6}); // Can also pass array
        printNumbers(); // Empty

        // Expected output demonstrates array fundamentals
    }

    static void printArray(int[] arr) {
        System.out.println("Received: " + java.util.Arrays.toString(arr));
    }

    static void printNumbers(int... numbers) { // int... is int[] inside
        System.out.println("Count: " + numbers.length + ", Values: " + java.util.Arrays.toString(numbers));
    }
}