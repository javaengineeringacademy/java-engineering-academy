/**
 * Exercises for try-catch exception handling patterns.
 *
 * Each method contains a TODO describing the task. Implement the body
 * to make the assertions pass (or uncomment the provided test logic).
 *
 * Complexity: O(1) per exercise — simple branching and error handling.
 * Thread-safety: Yes — all methods are stateless.
 * Key characteristics: Covers basic try-catch, multiple catch blocks,
 *   multi-catch syntax, nested try-catch, and exception translation.
 */
public class TryCatchExercises {

    // -----------------------------------------------------------
    // Exercise 1: Basic try-catch
    // -----------------------------------------------------------
    /**
     * Divide two integers. If divisor is zero, catch the
     * ArithmeticException and return -1 instead of propagating.
     *
     * @return result of dividend / divisor, or -1 on division by zero
     */
    static int exercise1_BasicTryCatch(int dividend, int divisor) {
        // TODO: Wrap division in try-catch.
        //       Catch ArithmeticException and return -1.
        return 0;
    }

    // -----------------------------------------------------------
    // Exercise 2: Multiple catch blocks
    // -----------------------------------------------------------
    /**
     * Parse a string to an integer and divide 100 by the result.
     *
     * Return values:
     *   100 / parsed  — on success
     *   -1            — if the string is not a valid integer (NumberFormatException)
     *   -2            — if the parsed integer is zero (ArithmeticException)
     */
    static int exercise2_MultipleCatch(String input) {
        // TODO: Use multiple catch blocks to distinguish
        //       NumberFormatException and ArithmeticException.
        return 0;
    }

    // -----------------------------------------------------------
    // Exercise 3: Multi-catch (Java 7+)
    // -----------------------------------------------------------
    /**
     * Access first character of a string and its char value as an int.
     *
     * Return values:
     *   (int) first char  — on success
     *   -1                — if input is null (NullPointerException)
     *   -2                — if string is empty (StringIndexOutOfBoundsException)
     *
     * Use a SINGLE multi-catch block for both exception types.
     */
    static int exercise3_MultiCatch(String input) {
        // TODO: Use multi-catch (catch (A | B e)) to handle both
        //       NullPointerException and StringIndexOutOfBoundsException.
        return 0;
    }

    // -----------------------------------------------------------
    // Exercise 4: Nested try-catch
    // -----------------------------------------------------------
    /**
     * Demonstrate nested try-catch.
     *
     * Outer try: access arr[index]. If ArrayIndexOutOfBoundsException,
     *   print "OUTER_ERROR" and return -1.
     * Inner try: divide 100 by arr[index]. If ArithmeticException,
     *   print "INNER_ERROR" and return -2.
     * On success: return arr[index].
     *
     * The inner catch must NOT propagate to outer — handle it locally.
     */
    static int exercise4_NestedTryCatch(int[] arr, int index) {
        // TODO: Implement nested try-catch with the described behavior.
        return 0;
    }

    // -----------------------------------------------------------
    // Exercise 5: Exception translation
    // -----------------------------------------------------------
    /**
     * Call riskyOperation(value). If it throws any exception,
     * wrap it in a custom RuntimeException with message
     * "Translated: <original message>" and re-throw.
     *
     * Use exception chaining (pass the original as the cause).
     */
    static void exercise5_ExceptionTranslation(int value) {
        // TODO: Call riskyOperation, catch any Exception,
        //       wrap in RuntimeException with chaining, re-throw.
    }

    static void riskyOperation(int value) {
        if (value < 0) throw new IllegalArgumentException("negative value: " + value);
        if (value == 0) throw new ArithmeticException("zero not allowed");
        System.out.println("  riskyOperation OK: " + value);
    }

    // -----------------------------------------------------------
    // Test harness
    // -----------------------------------------------------------
    public static void main(String[] args) {
        System.out.println("=== Try-Catch Exercises ===\n");

        // Exercise 1
        System.out.println("Exercise 1 — Basic try-catch:");
        System.out.println("  10 / 2 = " + exercise1_BasicTryCatch(10, 2));
        System.out.println("  10 / 0 = " + exercise1_BasicTryCatch(10, 0));
        System.out.println("  Expected: 5, -1\n");

        // Exercise 2
        System.out.println("Exercise 2 — Multiple catch:");
        System.out.println("  \"25\"  => " + exercise2_MultipleCatch("25"));
        System.out.println("  \"abc\" => " + exercise2_MultipleCatch("abc"));
        System.out.println("  \"0\"   => " + exercise2_MultipleCatch("0"));
        System.out.println("  Expected: 4, -1, -2\n");

        // Exercise 3
        System.out.println("Exercise 3 — Multi-catch:");
        System.out.println("  \"Hi\"    => " + exercise3_MultiCatch("Hi"));
        System.out.println("  null     => " + exercise3_MultiCatch(null));
        System.out.println("  \"\"      => " + exercise3_MultiCatch(""));
        System.out.println("  Expected: 72, -1, -2\n");

        // Exercise 4
        System.out.println("Exercise 4 — Nested try-catch:");
        int[] arr = {5, 10, 0};
        System.out.println("  [5,10,0], idx=0 => " + exercise4_NestedTryCatch(arr, 0));
        System.out.println("  [5,10,0], idx=5 => " + exercise4_NestedTryCatch(arr, 5));
        System.out.println("  [5,10,0], idx=2 => " + exercise4_NestedTryCatch(arr, 2));
        System.out.println("  Expected: 5, -1, -2\n");

        // Exercise 5
        System.out.println("Exercise 5 — Exception translation:");
        try {
            exercise5_ExceptionTranslation(5);
            exercise5_ExceptionTranslation(-1);
        } catch (RuntimeException e) {
            System.out.println("  Caught: " + e.getMessage());
            System.out.println("  Cause:  " + e.getCause());
        }
        System.out.println("  Expected: OK output, then translated error with cause chain\n");

        System.out.println("Exercises complete. Implement the TODOs to make assertions pass.");
    }
}
