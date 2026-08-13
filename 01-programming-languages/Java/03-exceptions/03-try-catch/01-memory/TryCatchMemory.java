package academy.javaengineering.exceptions.trycatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates memory allocation patterns when using try-catch.
 *
 * Run with: java -verbose:gc TryCatchMemory
 * Run with: java -XX:+PrintGCDetails TryCatchMemory
 */
public class TryCatchMemory {

    private static final int ITERATIONS = 1_000_000;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Exception Memory Allocation Patterns ===\n");

        measureBasicExceptionAllocation();
        Thread.sleep(500);

        measureStackedTraceCost();
        Thread.sleep(500);

        measureSuppressedExceptionGrowth();
        Thread.sleep(500);

        measureSingletonPattern();
        Thread.sleep(500);

        measureStacklessException();
        Thread.sleep(500);

        measureExceptionAsControlFlow();
    }

    /**
     * Basic pattern: allocating exceptions in a loop.
     * Each iteration creates a new exception object with full stack trace.
     */
    static void measureBasicExceptionAllocation() {
        System.out.println("--- 1. Basic Exception Allocation ---");
        System.out.println("Allocating " + ITERATIONS + " exceptions with full stack traces.");

        long before = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        List<Exception> sink = new ArrayList<>();
        for (int i = 0; i < ITERATIONS; i++) {
            try {
                throw new RuntimeException("error at iteration " + i);
            } catch (RuntimeException e) {
                sink.add(e);
            }
        }

        long after = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long used = after - before;

        System.out.println("Exceptions held: " + sink.size());
        System.out.println("Estimated heap delta: " + (used / 1024) + " KB");
        System.out.println("Avg bytes per exception: " + (used / ITERATIONS));
        System.out.println();
    }

    /**
     * Deeper stack traces cost more memory.
     * Each additional frame adds a StackTraceElement and string references.
     */
    static void measureStackedTraceCost() {
        System.out.println("--- 2. Stack Depth Impact on Memory ---");

        // Shallow stack
        long shallow = measureTraceMemory(2);

        // Medium stack
        long medium = measureTraceMemory(5);

        // Deep stack
        long deep = measureTraceMemory(10);

        System.out.println("2 frames:  ~" + shallow + " bytes/exception");
        System.out.println("5 frames:  ~" + medium + " bytes/exception");
        System.out.println("10 frames: ~" + deep + " bytes/exception");
        System.out.println();
    }

    static long measureTraceMemory(int depth) {
        List<Exception> sink = new ArrayList<>();
        long before = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        for (int i = 0; i < ITERATIONS; i++) {
            try {
                throwAtDepth(depth, i);
            } catch (RuntimeException e) {
                sink.add(e);
            }
        }

        long after = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        return (after - before) / ITERATIONS;
    }

    static void throwAtDepth(int depth, int id) {
        if (depth <= 0) {
            throw new RuntimeException("depth=" + depth + " id=" + id);
        }
        throwAtDepth(depth - 1, id);
    }

    /**
     * Suppressed exceptions chain additional exception objects.
     * Each suppressed exception carries its own stack trace.
     */
    static void measureSuppressedExceptionGrowth() {
        System.out.println("--- 3. Suppressed Exception Memory Growth ---");

        int suppressCount = 5;
        int iterations = 100_000;
        List<Exception> sink = new ArrayList<>();

        long before = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        for (int i = 0; i < iterations; i++) {
            try {
                try {
                    throw new RuntimeException("outer " + i);
                } finally {
                    for (int s = 0; s < suppressCount; s++) {
                        // Simulate suppressed by adding manually
                        RuntimeException outer = new RuntimeException("outer placeholder");
                        outer.addSuppressed(new RuntimeException("suppressed " + s));
                    }
                }
            } catch (RuntimeException e) {
                sink.add(e);
            }
        }

        long after = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long delta = after - before;

        System.out.println("Exceptions with " + suppressCount + " suppressed each: " + sink.size());
        System.out.println("Estimated heap delta: " + (delta / 1024) + " KB");
        System.out.println("Avg bytes per exception chain: " + (delta / iterations));
        System.out.println();
    }

    /**
     * Singleton exception pattern avoids per-throw allocation.
     * The exception is allocated once and reused.
     */
    static void measureSingletonPattern() {
        System.out.println("--- 4. Singleton Exception Pattern ---");
        System.out.println("Reusing a single exception instance across " + ITERATIONS + " throws.");

        RuntimeException singleton = new RuntimeException("singleton");

        long before = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        List<Throwable> sink = new ArrayList<>();
        for (int i = 0; i < ITERATIONS; i++) {
            try {
                throw singleton;
            } catch (RuntimeException e) {
                sink.add(e);
            }
        }

        long after = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long delta = after - before;

        System.out.println("Exceptions caught: " + sink.size());
        System.out.println("Estimated heap delta: " + (delta / 1024) + " KB");
        System.out.println("Note: minimal allocation — same object reused each time.");
        System.out.println("Tradeoff: stack trace reflects only the last throw site.");
        System.out.println();
    }

    /**
     * Overriding fillInStackTrace eliminates stack trace allocation.
     * This saves 200-400 bytes per frame on the call stack.
     */
    static void measureStacklessException() {
        System.out.println("--- 5. Exception Without Stack Trace ---");

        long standard = measureExceptionSize(false);
        long stackless = measureExceptionSize(true);

        System.out.println("Standard exception:  ~" + standard + " bytes/exception");
        System.out.println("Stackless exception: ~" + stackless + " bytes/exception");
        System.out.println("Memory saved: ~" + (standard - stackless) + " bytes/exception");
        System.out.println();
    }

    static long measureExceptionSize(boolean skipStack) {
        List<Exception> sink = new ArrayList<>();
        long before = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        for (int i = 0; i < ITERATIONS; i++) {
            try {
                if (skipStack) {
                    throw new StacklessException("no trace");
                } else {
                    throw new RuntimeException("with trace");
                }
            } catch (Exception e) {
                sink.add(e);
            }
        }

        long after = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        return (after - before) / ITERATIONS;
    }

    /**
     * Using exceptions as control flow wastes memory on allocation
     * that could be avoided with proper checks.
     */
    static void measureExceptionAsControlFlow() {
        System.out.println("--- 6. Exception as Control Flow vs. Proper Check ---");

        long exceptionTime = measureWithExceptions();
        long checkTime = measureWithCheck();

        System.out.println("Exception-based control flow: " + exceptionTime + " ms");
        System.out.println("Boolean check control flow:    " + checkTime + " ms");
        System.out.println("Memory cost: exceptions allocate + GC, checks allocate nothing.");
        System.out.println();
    }

    static long measureWithExceptions() {
        long start = System.nanoTime();
        List<Exception> sink = new ArrayList<>();

        for (int i = 0; i < ITERATIONS; i++) {
            try {
                if (i % 2 == 0) {
                    throw new RuntimeException("even");
                }
            } catch (RuntimeException e) {
                sink.add(e);
            }
        }

        return (System.nanoTime() - start) / 1_000_000;
    }

    static long measureWithCheck() {
        long start = System.nanoTime();
        int count = 0;

        for (int i = 0; i < ITERATIONS; i++) {
            if (i % 2 == 0) {
                count++;
            }
        }

        return (System.nanoTime() - start) / 1_000_000;
    }

    /**
     * Exception subclass that skips stack trace collection.
     * Demonstrates the memory savings from fillInStackTrace override.
     */
    static class StacklessException extends RuntimeException {
        StacklessException(String message) {
            super(message);
        }

        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }
}
