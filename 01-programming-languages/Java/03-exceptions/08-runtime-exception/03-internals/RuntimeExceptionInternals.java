/**
 * RuntimeExceptionInternals.java
 *
 * Demonstrates the internal mechanics of RuntimeException at the JVM level.
 * Covers exception creation, stack trace capture, unchecked exception processing,
 * and performance comparisons between checked and unchecked exceptions.
 *
 * @author Java Exception Handling Series
 * @version 1.0
 */
public class RuntimeExceptionInternals {

    private static final int ITERATIONS = 100_000;

    /**
     * Entry point for demonstrating RuntimeException internals.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        demonstrateCreation();
        demonstrateStackTraceCapture();
        demonstrateUncheckedPropagation();
        demonstratePerformanceComparison();
        demonstrateStackTraceSuppression();
    }

    /**
     * Shows how RuntimeException objects are created and initialized.
     */
    private static void demonstrateCreation() {
        System.out.println("=== RuntimeException Creation ===");

        // Basic creation with message
        RuntimeException basic = new RuntimeException("Basic runtime exception");
        System.out.println("Message: " + basic.getMessage());
        System.out.println("Class: " + basic.getClass().getName());

        // Creation with cause
        RuntimeException withCause = new RuntimeException(
                "Runtime exception with cause",
                new IllegalArgumentException("Root cause")
        );
        System.out.println("Cause: " + withCause.getCause().getClass().getName());
        System.out.println("Cause message: " + withCause.getCause().getMessage());

        // Subclass creation
        IllegalArgumentException illegalArg = new IllegalArgumentException("Invalid argument");
        NullPointerException nullPointer = new NullPointerException("Null reference");
        ArrayIndexOutOfBoundsException arrayBounds = new ArrayIndexOutOfBoundsException(5);

        System.out.println("IllegalArgument: " + illegalArg.getClass().getSimpleName());
        System.out.println("NullPointer: " + nullPointer.getClass().getSimpleName());
        System.out.println("ArrayBounds: " + arrayBounds.getClass().getSimpleName());
        System.out.println();
    }

    /**
     * Demonstrates stack trace capture and inspection.
     */
    private static void demonstrateStackTraceCapture() {
        System.out.println("=== Stack Trace Capture ===");

        RuntimeException exception = new RuntimeException("Stack trace demo");
        StackTraceElement[] stackTrace = exception.getStackTrace();

        System.out.println("Stack depth: " + stackTrace.length);
        System.out.println("\nTop 5 stack frames:");
        for (int i = 0; i < Math.min(5, stackTrace.length); i++) {
            StackTraceElement frame = stackTrace[i];
            System.out.printf("  %d: %s.%s(%s:%d)%n",
                    i,
                    frame.getClassName(),
                    frame.getMethodName(),
                    frame.getFileName(),
                    frame.getLineNumber());
        }
        System.out.println();
    }

    /**
     * Shows how unchecked exceptions propagate without compiler enforcement.
     */
    private static void demonstrateUncheckedPropagation() {
        System.out.println("=== Unchecked Exception Propagation ===");

        // Unchecked exceptions propagate without explicit declaration
        try {
            uncheckedMethodA();
        } catch (RuntimeException e) {
            System.out.println("Caught at top level: " + e.getMessage());
            System.out.println("Origin: " + e.getStackTrace()[0].getMethodName());
        }

        // Multiple propagation levels
        try {
            nestedMethod(0);
        } catch (RuntimeException e) {
            System.out.println("Nested propagation caught: " + e.getMessage());
            System.out.println("Throw depth: " + e.getStackTrace().length + " frames");
        }
        System.out.println();
    }

    /**
     * Demonstrates that unchecked exceptions are not declared in throws clauses.
     * This method throws NullPointerException without declaring it.
     */
    private static void uncheckedMethodA() {
        // This can throw NullPointerException - no throws declaration needed
        uncheckedMethodB();
    }

    /**
     * Demonstrates unchecked exception propagation through method calls.
     */
    private static void uncheckedMethodB() {
        // Compiler does not require try-catch for RuntimeException
        String nullString = null;
        nullString.length(); // Throws NullPointerException
    }

    /**
     * Shows propagation through multiple nesting levels.
     *
     * @param depth current nesting depth
     */
    private static void nestedMethod(int depth) {
        if (depth == 10) {
            throw new RuntimeException("Exception from depth " + depth);
        }
        nestedMethod(depth + 1);
    }

    /**
     * Compares performance of checked vs unchecked exceptions.
     * Note: The distinction exists at compile time only; JVM handles both identically.
     */
    private static void demonstratePerformanceComparison() {
        System.out.println("=== Performance Comparison ===");

        // Benchmark: RuntimeException creation with stack trace
        long startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            try {
                throw new RuntimeException("Unchecked: " + i);
            } catch (RuntimeException e) {
                // Catch and discard
            }
        }
        long uncheckedTime = System.nanoTime() - startTime;

        // Benchmark: Checked exception creation with stack trace
        startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            try {
                throw new Exception("Checked: " + i);
            } catch (Exception e) {
                // Catch and discard
            }
        }
        long checkedTime = System.nanoTime() - startTime;

        System.out.printf("RuntimeException: %.2f ms (%d iterations)%n",
                uncheckedTime / 1_000_000.0, ITERATIONS);
        System.out.printf("Checked Exception: %.2f ms (%d iterations)%n",
                checkedTime / 1_000_000.0, ITERATIONS);
        System.out.printf("Ratio: %.2f%n", (double) checkedTime / uncheckedTime);
        System.out.println();
    }

    /**
     * Demonstrates stack trace suppression for performance improvement.
     */
    private static void demonstrateStackTraceSuppression() {
        System.out.println("=== Stack Trace Suppression ===");

        // Normal exception with full stack trace
        long startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            try {
                throw new RuntimeException("With stack trace");
            } catch (RuntimeException e) {
                // Catch and discard
            }
        }
        long normalTime = System.nanoTime() - startTime;

        // Exception with suppressed stack trace
        startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            try {
                throw new NoStackTraceException("Without stack trace");
            } catch (RuntimeException e) {
                // Catch and discard
            }
        }
        long suppressedTime = System.nanoTime() - startTime;

        System.out.printf("With stack trace: %.2f ms%n", normalTime / 1_000_000.0);
        System.out.printf("Without stack trace: %.2f ms%n", suppressedTime / 1_000_000.0);
        System.out.printf("Speedup: %.2fx%n", (double) normalTime / suppressedTime);
        System.out.println();
    }

    /**
     * Custom RuntimeException that suppresses stack trace capture for improved performance.
     * Use this pattern for exceptions thrown in tight loops where stack traces are not needed.
     */
    private static class NoStackTraceException extends RuntimeException {

        /**
         * Creates a NoStackTraceException with a message.
         *
         * @param message the detail message
         */
        NoStackTraceException(String message) {
            super(message);
        }

        /**
         * Overrides fillInStackTrace to suppress stack trace capture.
         * This eliminates the cost of walking the stack and allocating the frame array.
         *
         * @return this exception instance
         */
        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }
}
