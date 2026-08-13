package academy.javaengineering.exceptions.throwable.internals;

import java.util.concurrent.TimeUnit;

/**
 * Demonstrates the performance cost of {@link Throwable#fillInStackTrace()}.
 *
 * <p><b>Complexity:</b> O(depth) per fillInStackTrace call, where depth is the
 * number of stack frames.</p>
 * <p><b>Thread-safety:</b> Single-threaded benchmark; results are reproducible
 * on the same JVM configuration.</p>
 * <p><b>Key characteristics:</b> fillInStackTrace is native and synchronized;
 * cost scales linearly with stack depth.</p>
 */
public class ThrowableInternalsDemo {

    private static final int WARMUP = 50_000;
    private static final int MEASUREMENT = 100_000;

    public static void main(String[] args) {
        demoFillInStackTraceCost();
        demoStackDepthImpact();
        demoOverrideFillInStackTrace();
    }

    private static void demoFillInStackTraceCost() {
        System.out.println("=== fillInStackTrace cost ===");
        System.out.println("Warmup: " + WARMUP + " iterations");

        // Warmup
        for (int i = 0; i < WARMUP; i++) {
            new Throwable().fillInStackTrace();
        }

        long start = System.nanoTime();
        for (int i = 0; i < MEASUREMENT; i++) {
            new Throwable().fillInStackTrace();
        }
        long elapsed = System.nanoTime() - start;

        double perCallNs = (double) elapsed / MEASUREMENT;
        System.out.printf("Measured: %d iterations in %d ms%n", MEASUREMENT,
                TimeUnit.NANOSECONDS.toMillis(elapsed));
        System.out.printf("Per call: %.1f ns (%.3f μs)%n", perCallNs, perCallNs / 1000.0);
        System.out.println();
    }

    private static void demoStackDepthImpact() {
        System.out.println("=== Stack depth impact ===");

        int[] depths = {5, 10, 20, 50, 100};
        for (int depth : depths) {
            measureAtDepth(depth);
        }
        System.out.println();
    }

    private static void measureAtDepth(int targetDepth) {
        // Warmup
        for (int i = 0; i < 10_000; i++) {
            recursiveThrow(0, targetDepth);
        }

        long start = System.nanoTime();
        for (int i = 0; i < 50_000; i++) {
            recursiveThrow(0, targetDepth);
        }
        long elapsed = System.nanoTime() - start;

        double perCallNs = (double) elapsed / 50_000;
        System.out.printf("  Depth %3d: %8.1f ns/call (%.3f μs)%n",
                targetDepth, perCallNs, perCallNs / 1000.0);
    }

    private static void recursiveThrow(int current, int depth) {
        if (current >= depth) {
            new Throwable().fillInStackTrace();
            return;
        }
        recursiveThrow(current + 1, depth);
    }

    private static void demoOverrideFillInStackTrace() {
        System.out.println("=== Override fillInStackTrace ===");

        // Warmup
        for (int i = 0; i < WARMUP; i++) {
            new FastException("test");
        }

        long start = System.nanoTime();
        for (int i = 0; i < MEASUREMENT; i++) {
            new FastException("test");
        }
        long elapsed = System.nanoTime() - start;

        double perCallNs = (double) elapsed / MEASUREMENT;
        System.out.printf("FastException (no stack): %.1f ns (%.3f μs)%n",
                perCallNs, perCallNs / 1000.0);

        // Compare with standard Throwable
        start = System.nanoTime();
        for (int i = 0; i < MEASUREMENT; i++) {
            new Throwable("test");
        }
        elapsed = System.nanoTime() - start;

        perCallNs = (double) elapsed / MEASUREMENT;
        System.out.printf("Throwable (full stack):    %.1f ns (%.3f μs)%n",
                perCallNs, perCallNs / 1000.0);
        System.out.println();
    }

    /**
     * Exception subclass that skips stack trace capture for performance.
     */
    private static class FastException extends RuntimeException {
        FastException(String message) {
            super(message);
        }

        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }
}
