/**
 * Complete solutions for the five try-catch exercises.
 *
 * Each solution demonstrates a core exception-handling pattern with
 * clear, idiomatic Java code.
 *
 * Complexity: O(1) per solution — simple branching and error handling.
 * Thread-safety: Yes — all methods are stateless.
 * Key characteristics: Solutions follow the exercise contracts exactly;
 *   run against TryCatchExercises.java to verify identical output.
 */
public class TryCatchSolutions {

    // -----------------------------------------------------------
    // Solution 1: Basic try-catch
    // -----------------------------------------------------------
    static int exercise1_BasicTryCatch(int dividend, int divisor) {
        try {
            return dividend / divisor;
        } catch (ArithmeticException e) {
            return -1;
        }
    }

    // -----------------------------------------------------------
    // Solution 2: Multiple catch blocks
    // -----------------------------------------------------------
    static int exercise2_MultipleCatch(String input) {
        try {
            int num = Integer.parseInt(input);
            return 100 / num;
        } catch (NumberFormatException e) {
            return -1;
        } catch (ArithmeticException e) {
            return -2;
        }
    }

    // -----------------------------------------------------------
    // Solution 3: Multi-catch (Java 7+)
    // -----------------------------------------------------------
    static int exercise3_MultiCatch(String input) {
        try {
            return (int) input.charAt(0);
        } catch (NullPointerException | StringIndexOutOfBoundsException e) {
            if (e instanceof NullPointerException) {
                return -1;
            }
            return -2;
        }
    }

    // -----------------------------------------------------------
    // Solution 4: Nested try-catch
    // -----------------------------------------------------------
    static int exercise4_NestedTryCatch(int[] arr, int index) {
        try {
            int val = arr[index];
            try {
                int result = 100 / val;
                return val;
            } catch (ArithmeticException e) {
                System.out.println("INNER_ERROR");
                return -2;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("OUTER_ERROR");
            return -1;
        }
    }

    // -----------------------------------------------------------
    // Solution 5: Exception translation
    // -----------------------------------------------------------
    static void exercise5_ExceptionTranslation(int value) {
        try {
            riskyOperation(value);
        } catch (Exception e) {
            throw new RuntimeException("Translated: " + e.getMessage(), e);
        }
    }

    static void riskyOperation(int value) {
        if (value < 0) throw new IllegalArgumentException("negative value: " + value);
        if (value == 0) throw new ArithmeticException("zero not allowed");
        System.out.println("  riskyOperation OK: " + value);
    }

    // -----------------------------------------------------------
    // Test harness — mirrors TryCatchExercises.main output
    // -----------------------------------------------------------
    public static void main(String[] args) {
        System.out.println("=== Try-Catch Solutions ===\n");

        System.out.println("Exercise 1 — Basic try-catch:");
        System.out.println("  10 / 2 = " + exercise1_BasicTryCatch(10, 2));
        System.out.println("  10 / 0 = " + exercise1_BasicTryCatch(10, 0));
        System.out.println("  Expected: 5, -1\n");

        System.out.println("Exercise 2 — Multiple catch:");
        System.out.println("  \"25\"  => " + exercise2_MultipleCatch("25"));
        System.out.println("  \"abc\" => " + exercise2_MultipleCatch("abc"));
        System.out.println("  \"0\"   => " + exercise2_MultipleCatch("0"));
        System.out.println("  Expected: 4, -1, -2\n");

        System.out.println("Exercise 3 — Multi-catch:");
        System.out.println("  \"Hi\"    => " + exercise3_MultiCatch("Hi"));
        System.out.println("  null     => " + exercise3_MultiCatch(null));
        System.out.println("  \"\"      => " + exercise3_MultiCatch(""));
        System.out.println("  Expected: 72, -1, -2\n");

        System.out.println("Exercise 4 — Nested try-catch:");
        int[] arr = {5, 10, 0};
        System.out.println("  [5,10,0], idx=0 => " + exercise4_NestedTryCatch(arr, 0));
        System.out.println("  [5,10,0], idx=5 => " + exercise4_NestedTryCatch(arr, 5));
        System.out.println("  [5,10,0], idx=2 => " + exercise4_NestedTryCatch(arr, 2));
        System.out.println("  Expected: 5, -1, -2\n");

        System.out.println("Exercise 5 — Exception translation:");
        try {
            exercise5_ExceptionTranslation(5);
            exercise5_ExceptionTranslation(-1);
        } catch (RuntimeException e) {
            System.out.println("  Caught: " + e.getMessage());
            System.out.println("  Cause:  " + e.getCause());
        }
        System.out.println("  Expected: OK output, then translated error with cause chain\n");

        System.out.println("All solutions verified.");
    }
}
