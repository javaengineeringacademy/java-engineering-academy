/**
 * RuntimeExceptionMemory.java
 *
 * Demonstrates memory allocation patterns and management for RuntimeException objects.
 * Covers object creation costs, heap impact, and memory-efficient exception usage patterns.
 *
 * @author Java Exception Handling Series
 * @version 1.0
 */
public class RuntimeExceptionMemory {

    private static final int ITERATIONS = 50_000;

    /**
     * Entry point for demonstrating RuntimeException memory management.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        demonstrateMemoryFootprint();
        demonstrateStackTraceAllocation();
        demonstrateExceptionChaining();
        demonstrateMemoryEfficientPatterns();
        demonstratePerformanceImpact();
    }

    /**
     * Shows the memory footprint of RuntimeException objects.
     */
    private static void demonstrateMemoryFootprint() {
        System.out.println("=== Memory Footprint Analysis ===");

        Runtime runtime = Runtime.getRuntime();
        long beforeGC = runtime.totalMemory() - runtime.freeMemory();

        // Create exceptions with different characteristics
        RuntimeException simple = new RuntimeException("Simple");
        RuntimeException withMessage = new RuntimeException(
                "A longer message that contains more detail about the error condition");
        RuntimeException withCause = new RuntimeException(
                "Exception with cause",
                new IllegalArgumentException("Root cause detail"));
        RuntimeException withChain = new RuntimeException(
                "Top level",
                new RuntimeException(
                        "Middle level",
                        new IllegalArgumentException("Bottom level")));

        long afterGC = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = afterGC - beforeGC;

        System.out.println("Simple exception created");
        System.out.println("Exception with long message created");
        System.out.println("Exception with cause created");
        System.out.println("Exception with 3-level chain created");
        System.out.println("Approximate heap delta: " + memoryUsed + " bytes");
        System.out.println();
    }

    /**
     * Demonstrates stack trace memory allocation.
     */
    private static void demonstrateStackTraceAllocation() {
        System.out.println("=== Stack Trace Allocation ===");

        // Capture stack trace at different depths
        RuntimeException shallowException = createShallowException();
        RuntimeException deepException = createDeepException(0);

        StackTraceElement[] shallowTrace = shallowException.getStackTrace();
        StackTraceElement[] deepTrace = deepException.getStackTrace();

        System.out.println("Shallow stack trace depth: " + shallowTrace.length + " frames");
        System.out.println("Deep stack trace depth: " + deepTrace.length + " frames");

        // Calculate approximate memory usage
        int shallowMemory = estimateStackTraceMemory(shallowTrace);
        int deepMemory = estimateStackTraceMemory(deepTrace);

        System.out.println("Shallow stack trace memory: ~" + shallowMemory + " bytes");
        System.out.println("Deep stack trace memory: ~" + deepMemory + " bytes");
        System.out.println();
    }

    /**
     * Creates an exception with a shallow stack trace.
     *
     * @return RuntimeException with few stack frames
     */
    private static RuntimeException createShallowException() {
        return new RuntimeException("Shallow exception");
    }

    /**
     * Creates an exception with a deep stack trace through recursive calls.
     *
     * @param depth current recursion depth
     * @return RuntimeException with many stack frames
     */
    private static RuntimeException createDeepException(int depth) {
        if (depth >= 50) {
            return new RuntimeException("Deep exception");
        }
        return createDeepException(depth + 1);
    }

    /**
     * Estimates the memory used by a stack trace.
     *
     * @param stackTrace the stack trace to estimate
     * @return estimated memory in bytes
     */
    private static int estimateStackTraceMemory(StackTraceElement[] stackTrace) {
        int memory = 0;
        // Array overhead
        memory += 16; // Object header
        memory += 4;  // Length field
        memory += 4;  // Padding
        memory += 8 * stackTrace.length; // Element references

        // Per element
        for (StackTraceElement element : stackTrace) {
            memory += 48; // StackTraceElement object
            memory += estimateStringLength(element.getClassName());
            memory += estimateStringLength(element.getMethodName());
            if (element.getFileName() != null) {
                memory += estimateStringLength(element.getFileName());
            }
        }
        return memory;
    }

    /**
     * Estimates the memory used by a string.
     *
     * @param str the string to estimate
     * @return estimated memory in bytes
     */
    private static int estimateStringLength(String str) {
        if (str == null) return 0;
        return 16 + 8 + 4 + 1 + 3 + (str.length() * 2); // String object + char data
    }

    /**
     * Demonstrates memory impact of exception chaining.
     */
    private static void demonstrateExceptionChaining() {
        System.out.println("=== Exception Chaining Memory Impact ===");

        Runtime runtime = Runtime.getRuntime();
        long beforeGC = runtime.totalMemory() - runtime.freeMemory();

        // Create exception chain of varying depths
        RuntimeException chain1 = new RuntimeException("Level 1");

        RuntimeException chain2 = new RuntimeException(
                "Level 2",
                new RuntimeException("Level 1"));

        RuntimeException chain3 = new RuntimeException(
                "Level 3",
                new RuntimeException(
                        "Level 2",
                        new RuntimeException("Level 1")));

        RuntimeException chain4 = new RuntimeException(
                "Level 4",
                new RuntimeException(
                        "Level 3",
                        new RuntimeException(
                                "Level 2",
                                new RuntimeException("Level 1"))));

        long afterGC = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = afterGC - beforeGC;

        System.out.println("Chain depth 1 created");
        System.out.println("Chain depth 2 created");
        System.out.println("Chain depth 3 created");
        System.out.println("Chain depth 4 created");
        System.out.println("Approximate heap delta: " + memoryUsed + " bytes");
        System.out.println();
    }

    /**
     * Demonstrates memory-efficient exception patterns.
     */
    private static void demonstrateMemoryEfficientPatterns() {
        System.out.println("=== Memory-Efficient Patterns ===");

        Runtime runtime = Runtime.getRuntime();

        // Pattern 1: Normal exception
        long before = runtime.totalMemory() - runtime.freeMemory();
        for (int i = 0; i < ITERATIONS; i++) {
            try {
                throw new RuntimeException("Normal exception");
            } catch (RuntimeException e) {
                // Catch and discard
            }
        }
        long normalMemory = runtime.totalMemory() - runtime.freeMemory() - before;

        // Force GC for clean measurement
        System.gc();
        before = runtime.totalMemory() - runtime.freeMemory();

        // Pattern 2: Exception with suppressed stack trace
        for (int i = 0; i < ITERATIONS; i++) {
            try {
                throw new NoStackTraceException("Suppressed exception");
            } catch (RuntimeException e) {
                // Catch and discard
            }
        }
        long suppressedMemory = runtime.totalMemory() - runtime.freeMemory() - before;

        System.out.println("Normal exceptions (" + ITERATIONS + " iterations)");
        System.out.println("Heap delta: " + normalMemory + " bytes");
        System.out.println("\nSuppressed stack trace exceptions (" + ITERATIONS + " iterations)");
        System.out.println("Heap delta: " + suppressedMemory + " bytes");
        System.out.println("\nMemory savings: " + (normalMemory - suppressedMemory) + " bytes");
        System.out.println();
    }

    /**
     * Demonstrates performance impact of exception creation.
     */
    private static void demonstratePerformanceImpact() {
        System.out.println("=== Performance Impact ===");

        // Benchmark: Exception creation cost
        long startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            new RuntimeException("Test");
        }
        long creationTime = System.nanoTime() - startTime;

        // Benchmark: Exception throw and catch
        startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            try {
                throw new RuntimeException("Test");
            } catch (RuntimeException e) {
                // Catch and discard
            }
        }
        long throwCatchTime = System.nanoTime() - startTime;

        // Benchmark: Normal operation (no exception)
        startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            @SuppressWarnings("unused")
            int value = i * 2;
        }
        long normalTime = System.nanoTime() - startTime;

        System.out.printf("Exception creation only: %.2f ms%n", creationTime / 1_000_000.0);
        System.out.printf("Exception throw + catch: %.2f ms%n", throwCatchTime / 1_000_000.0);
        System.out.printf("Normal operations: %.2f ms%n", normalTime / 1_000_000.0);
        System.out.printf("Exception overhead: %.2fx%n", (double) throwCatchTime / normalTime);
        System.out.println();
    }

    /**
     * Custom RuntimeException that suppresses stack trace capture for memory efficiency.
     * This pattern reduces memory allocation when stack traces are not needed.
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
         * This eliminates the cost of stack trace allocation and native stack walking.
         *
         * @return this exception instance
         */
        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }
}
