package academy.javaengineering.exceptions.bestpractices;

/**
 * Demonstrates good vs bad exception handling patterns.
 * <p>
 * Complexity: O(n) per example, O(1) space.
 * Thread-safety: Stateless utility methods, inherently thread-safe.
 * Key characteristics: Empty catch vs proper handling, catching Exception vs specific,
 * swallowing vs rethrowing, proper logging, exception translation patterns.
 */
public class BestPracticesExamples {

    public static void main(String[] args) {
        System.out.println("=== BAD PRACTICES ===");

        System.out.println("\n--- Anti-Pattern 1: Empty Catch Block ---");
        badEmptyCatch();

        System.out.println("\n--- Anti-Pattern 2: Catching Generic Exception ---");
        badCatchGeneric();

        System.out.println("\n--- Anti-Pattern 3: Swallowing Exceptions ---");
        badSwallowException();

        System.out.println("\n--- Anti-Pattern 4: Using Exceptions for Flow Control ---");
        badExceptionFlowControl();

        System.out.println("\n--- Anti-Pattern 5: Poor Logging ---");
        badLogging();

        System.out.println("\n=== GOOD PRACTICES ===");

        System.out.println("\n--- Good Pattern 1: Proper Catch and Handle ---");
        goodProperCatch();

        System.out.println("\n--- Good Pattern 2: Catching Specific Exceptions ---");
        goodCatchSpecific();

        System.out.println("\n--- Good Pattern 3: Rethrowing with Context ---");
        goodRethrowWithContext();

        System.out.println("\n--- Good Pattern 4: Exception Translation ---");
        goodExceptionTranslation();

        System.out.println("\n--- Good Pattern 5: Proper Logging ---");
        goodLogging();
    }

    // ==================== BAD PRACTICES ====================

    /**
     * BAD: Empty catch block silently ignores errors.
     */
    static void badEmptyCatch() {
        String[] items = {"a", "b", "c"};
        try {
            int index = Integer.parseInt("not_a_number");
            System.out.println("Item: " + items[index]);
        } catch (Exception e) {
            // Empty — error silently ignored
        }
        System.out.println("  Code continues with no idea something went wrong.");
    }

    /**
     * BAD: Catching generic Exception hides the real problem.
     */
    static void badCatchGeneric() {
        try {
            String result = riskyOperation();
            System.out.println("  Result: " + result);
        } catch (Exception e) {
            System.out.println("  Caught generic exception: " + e.getMessage());
        }
    }

    /**
     * BAD: Swallowing an exception — no logging, no rethrowing.
     */
    static void badSwallowException() {
        try {
            riskyOperation();
        } catch (RuntimeException e) {
            // Swallowed — lost forever
        }
        System.out.println("  Operation appears to have succeeded when it did not.");
    }

    /**
     * BAD: Using exceptions for normal flow control.
     */
    static void badExceptionFlowControl() {
        int[] numbers = {1, 2, 3};
        for (int i = 0; i < 10; i++) {
            try {
                System.out.println("  Value at " + i + ": " + numbers[i]);
            } catch (ArrayIndexOutOfBoundsException e) {
                break;
            }
        }
    }

    /**
     * BAD: Logging exception message but losing the stack trace.
     */
    static void badLogging() {
        try {
            riskyOperation();
        } catch (Exception e) {
            // Only logs the message — stack trace lost forever
            System.out.println("  Error: " + e.getMessage());
        }
    }

    // ==================== GOOD PRACTICES ====================

    /**
     * GOOD: Catch specific exceptions and handle them properly.
     */
    static void goodProperCatch() {
        try {
            int value = Integer.parseInt("42");
            System.out.println("  Parsed value: " + value);
        } catch (NumberFormatException e) {
            System.out.println("  Handled: invalid number format — " + e.getMessage());
        }
    }

    /**
     * GOOD: Catch the most specific exception type possible.
     */
    static void goodCatchSpecific() {
        try {
            String result = riskyOperation();
            System.out.println("  Result: " + result);
        } catch (NullPointerException e) {
            System.out.println("  Handled NPE specifically: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("  Handled IllegalArgument specifically: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("  Handled other runtime: " + e.getMessage());
        }
    }

    /**
     * GOOD: Rethrow with additional context.
     */
    static void goodRethrowWithContext() {
        try {
            riskyOperation();
        } catch (RuntimeException e) {
            System.out.println("  Adding context and rethrowing...");
            // In production: throw new ServiceException("Failed to process", e);
            System.out.println("  Would rethrow: ServiceException(\"Failed to process\", cause)");
        }
    }

    /**
     * GOOD: Translate low-level exceptions to meaningful application exceptions.
     */
    static void goodExceptionTranslation() {
        try {
            int result = divideNumbers(10, 0);
            System.out.println("  Result: " + result);
        } catch (ArithmeticException e) {
            // Translate to a meaningful application-level exception
            System.out.println("  Translated to: CalculationException(\"Cannot divide by zero\")");
            // throw new CalculationException("Cannot divide by zero", e);
        }
    }

    /**
     * GOOD: Log the exception with full stack trace for debugging.
     */
    static void goodLogging() {
        try {
            riskyOperation();
        } catch (Exception e) {
            // In production use a logging framework:
            // logger.error("Operation failed with input {}", input, e);
            System.out.println("  Logged: [ERROR] Operation failed — " + e.getMessage());
            System.out.println("  Stack trace preserved for debugging.");
        }
    }

    // ==================== HELPER METHODS ====================

    static String riskyOperation() {
        return null; // Will cause NPE
    }

    static int divideNumbers(int a, int b) {
        return a / b;
    }
}
