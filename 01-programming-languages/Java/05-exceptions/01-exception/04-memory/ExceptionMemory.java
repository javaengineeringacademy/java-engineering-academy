package academy.javaengineering.exceptions.exception.memory;

/**
 * Demonstrates memory characteristics of exception objects.
 *
 * <p>Run with {@code -XX:+PrintFlagsFinal | grep UseCompressedOops} to check
 * compressed object pointers. Memory sizes vary by JVM implementation and
 * configuration.</p>
 */
public class ExceptionMemoryDemo {

    // ============================================================
    // Fast exception: override fillInStackTrace
    // ============================================================

    /**
     * An exception that does not fill the stack trace.
     *
     * <p>fillInStackTrace() walks the entire call stack and is the most
     * expensive part of exception creation. Overriding it to return this
     * skips the stack walk entirely.</p>
     */
    static class FastException extends Exception {
        public FastException(String message) {
            super(message);
        }

        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }

    // ============================================================
    // Memory measurement
    // ============================================================

    /**
     * Estimates the shallow size of an object using a reference-counting
     * technique. This is an approximation; actual sizes depend on JVM
     * implementation and GC strategy.
     *
     * <p>Methodology: allocate many objects, measure heap growth, divide.</p>
     */
    static long estimateShallowSize(Runnable factory, int count) {
        // Force GC to get clean baseline
        System.gc();
        try { Thread.sleep(100); } catch (InterruptedException ignored) {}

        Runtime runtime = Runtime.getRuntime();
        long before = runtime.totalMemory() - runtime.freeMemory();

        Object[] holders = new Object[count];
        for (int i = 0; i < count; i++) {
            factory.run();
        }

        // Force GC again
        System.gc();
        try { Thread.sleep(100); } catch (InterruptedException ignored) {}

        long after = runtime.totalMemory() - runtime.freeMemory();
        return (after - before) / count;
    }

    // ============================================================
    // Exception object sizes
    // ============================================================

    static void measureExceptionSizes() {
        int count = 100_000;

        // Bare exception (no message)
        long bareSize = estimateShallowSize(
                () -> new Exception(), count);

        // Exception with message
        long messageSize = estimateShallowSize(
                () -> new Exception("Something went wrong"), count);

        // Exception with message and cause
        long chainedSize = estimateShallowSize(
                () -> new Exception("wrapper", new RuntimeException("root")), count);

        // Fast exception (no stack trace)
        long fastSize = estimateShallowSize(
                () -> new FastException("fast"), count);

        // Deep cause chain (depth 5)
        long deepChainSize = estimateShallowSize(() -> {
            Exception leaf = new Exception("level-5");
            for (int i = 4; i >= 1; i--) {
                leaf = new Exception("level-" + i, leaf);
            }
            new Exception("level-0", leaf);
        }, count / 5);

        System.out.println("Approximate exception object sizes:");
        System.out.printf("  Bare exception (no message):     ~%d bytes%n", bareSize);
        System.out.printf("  With message:                    ~%d bytes%n", messageSize);
        System.out.printf("  With message + cause:            ~%d bytes%n", chainedSize);
        System.out.printf("  Fast exception (no stack trace): ~%d bytes%n", fastSize);
        System.out.printf("  Deep chain (5 levels):           ~%d bytes each%n", deepChainSize);
    }

    // ============================================================
    // Stack trace memory
    // ============================================================

    static void measureStackTraceMemory() {
        Exception e = new Exception("trace memory");
        StackTraceElement[] trace = e.getStackTrace();

        System.out.println("\nStack trace information:");
        System.out.printf("  Stack depth: %d frames%n", trace.length);

        // Estimate memory per StackTraceElement
        // Each has: declaringClass (String), methodName (String), fileName (String), lineNumber (int)
        long stringMemory = 0;
        for (StackTraceElement ste : trace) {
            if (ste.getClassName() != null) stringMemory += ste.getClassName().length() * 2;
            if (ste.getMethodName() != null) stringMemory += ste.getMethodName().length() * 2;
            if (ste.getFileName() != null) stringMemory += ste.getFileName().length() * 2;
        }
        System.out.printf("  String data in trace: ~%d bytes%n", stringMemory);
        System.out.printf("  StackTraceElement[] overhead: ~%d bytes (header + length + refs)%n",
                16 + 4 + trace.length * 8L);
        System.out.printf("  Estimated total trace memory: ~%d bytes%n",
                stringMemory + 16 + 4 + trace.length * 8L + trace.length * 64L);
    }

    // ============================================================
    // Performance: fillInStackTrace cost
    // ============================================================

    static void measureFillInStackTraceCost() {
        int iterations = 100_000;

        // Standard exception
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            new Exception("test");
        }
        long standardTime = System.nanoTime() - start;

        // Fast exception (no stack trace)
        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            new FastException("test");
        }
        long fastTime = System.nanoTime() - start;

        System.out.println("\nfillInStackTrace() cost:");
        System.out.printf("  Standard exception: %,d ns for %d iterations (avg: %,.1f ns)%n",
                standardTime, iterations, (double) standardTime / iterations);
        System.out.printf("  Fast exception:     %,d ns for %d iterations (avg: %,.1f ns)%n",
                fastTime, iterations, (double) fastTime / iterations);
        System.out.printf("  Speedup: %.1fx%n", (double) standardTime / fastTime);
    }

    // ============================================================
    // Main
    // ============================================================

    public static void main(String[] args) throws Exception {
        System.out.println("=== Exception Memory Analysis ===\n");
        measureExceptionSizes();
        measureStackTraceMemory();
        measureFillInStackTraceCost();
    }
}
