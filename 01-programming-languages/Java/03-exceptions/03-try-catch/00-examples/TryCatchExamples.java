package academy.javaengineering.exceptions.trycatch;

/**
 * Comprehensive demonstration of try-catch variations in Java.
 *
 * Covers: basic try-catch, multiple catch blocks, multi-catch (Java 7+),
 * and nested try-catch — each as a standalone method.
 *
 * Complexity: O(1) per method — no loops or recursive logic.
 * Thread-safety: Yes — all methods are stateless and use only local variables.
 * Key characteristics: Illustrates the four fundamental try-catch patterns
 *   that form the basis of exception handling in Java.
 */
public class TryCatchExamples {

    // -----------------------------------------------------------
    // 1. Basic try-catch
    // -----------------------------------------------------------
    static void basicTryCatch() {
        System.out.println("=== 1. Basic Try-Catch ===");
        try {
            int result = 10 / 0;
            System.out.println("Result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        System.out.println("Program continues after catch.\n");
    }

    // -----------------------------------------------------------
    // 2. Multiple catch blocks (specific → general order)
    // -----------------------------------------------------------
    static void multipleCatch() {
        System.out.println("=== 2. Multiple Catch Blocks ===");
        String[] values = {"42", "hello", null};

        for (String v : values) {
            try {
                int num = Integer.parseInt(v);
                int div = 100 / num;
                System.out.println("  100 / " + num + " = " + div);
            } catch (NumberFormatException e) {
                System.out.println("  Format error for \"" + v + "\": " + e.getMessage());
            } catch (ArithmeticException e) {
                System.out.println("  Arithmetic error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("  Unexpected error: " + e.getMessage());
            }
        }
        System.out.println();
    }

    // -----------------------------------------------------------
    // 3. Multi-catch (Java 7+) — single block for multiple types
    // -----------------------------------------------------------
    static void multiCatch() {
        System.out.println("=== 3. Multi-Catch (Java 7+) ===");
        String[] inputs = {"abc", null};

        for (String s : inputs) {
            try {
                if (s == null) throw new NullPointerException("null input");
                int len = s.length();
                int firstChar = s.charAt(0);
                System.out.println("  \"" + s + "\": length=" + len + ", first=" + firstChar);
            } catch (NullPointerException | StringIndexOutOfBoundsException e) {
                System.out.println("  Caught (" + e.getClass().getSimpleName() + "): " + e.getMessage());
            }
        }
        System.out.println();
    }

    // -----------------------------------------------------------
    // 4. Nested try-catch
    // -----------------------------------------------------------
    static void nestedTryCatch() {
        System.out.println("=== 4. Nested Try-Catch ===");
        try {
            int[] arr = {1, 2, 3};
            System.out.println("  Outer try: accessing arr[1] = " + arr[1]);

            try {
                System.out.println("  Inner try: dividing by arr[0]...");
                int result = 10 / arr[0];
                System.out.println("  Result = " + result);

                System.out.println("  Inner try: accessing arr[5]...");
                int val = arr[5];
                System.out.println("  Value = " + val);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("  Inner catch: " + e.getMessage());
            }

            System.out.println("  After inner catch — program continues in outer try.");
        } catch (ArithmeticException e) {
            System.out.println("  Outer catch: " + e.getMessage());
        }
        System.out.println();
    }

    // -----------------------------------------------------------
    // main
    // -----------------------------------------------------------
    public static void main(String[] args) {
        System.out.println("Try-Catch Examples\n");

        basicTryCatch();
        multipleCatch();
        multiCatch();
        nestedTryCatch();

        System.out.println("All demos completed.");
    }
}
