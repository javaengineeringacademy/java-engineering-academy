package academy.javaengineering.exceptions.stacktrace.internals;

/**
 * Demonstrates internal mechanics of stack trace creation and fillInStackTrace.
 */
public class StackTraceInternals {

    /**
     * Exception that skips fillInStackTrace for performance.
     */
    static class LightweightException extends RuntimeException {
        LightweightException(String message) {
            super(message);
        }

        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }

    /**
     * Normal exception — full stack trace captured.
     */
    static class NormalException extends RuntimeException {
        NormalException(String message) {
            super(message);
        }
    }

    /**
     * Deep recursive method to demonstrate stack depth impact.
     */
    static int recursiveSum(int n) {
        if (n <= 0) {
            throw new NormalException("base case reached");
        }
        return n + recursiveSum(n - 1);
    }

    /**
     * Deep recursive method using lightweight exception.
     */
    static int recursiveSumLightweight(int n) {
        if (n <= 0) {
            throw new LightweightException("base case reached");
        }
        return n + recursiveSumLightweight(n - 1);
    }

    /**
     * Demonstrates that re-throwing does not update the stack trace.
     */
    static void demonstrateReThrow() {
        try {
            throw new RuntimeException("original");
        } catch (RuntimeException e) {
            StackTraceElement[] traceBefore = e.getStackTrace();
            try {
                throw e; // re-throw
            } catch (RuntimeException caught) {
                StackTraceElement[] traceAfter = caught.getStackTrace();
                System.out.println("Trace unchanged: "
                        + (traceBefore == traceAfter));
                System.out.println("Frames before: " + traceBefore.length);
                System.out.println("Frames after:  " + traceAfter.length);
            }
        }
    }

    /**
     * Shows the cost of creating exceptions at different stack depths.
     */
    static void benchmarkStackDepth(int depth) {
        // Warm up
        for (int i = 0; i < 1000; i++) {
            try {
                throw new RuntimeException();
            } catch (RuntimeException ignored) {
            }
        }

        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            try {
                throw new RuntimeException();
            } catch (RuntimeException ignored) {
            }
        }
        long elapsed = System.nanoTime() - start;
        System.out.printf("Stack depth ~%d: avg %.2f μs per exception%n",
                depth, elapsed / 1000.0 / 1000.0);
    }

    /**
     * Demonstrates suppressed exceptions in try-with-resources.
     */
    static void demonstrateSuppressed() {
        AutoCloseable resource = new AutoCloseable() {
            @Override
            public void close() throws Exception {
                throw new RuntimeException("close() failed");
            }
        };

        try {
            try (resource) {
                throw new RuntimeException("primary");
            }
        } catch (RuntimeException e) {
            System.out.println("Primary: " + e.getMessage());
            System.out.println("Suppressed count: " + e.getSuppressed().length);
            for (Throwable s : e.getSuppressed()) {
                System.out.println("  Suppressed: " + s.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 1. Re-throw trace preservation ===");
        demonstrateReThrow();

        System.out.println("\n=== 2. Lightweight vs normal exception ===");
        try {
            throw new LightweightException("light");
        } catch (LightweightException e) {
            System.out.println("Lightweight frames: " + e.getStackTrace().length);
        }
        try {
            throw new NormalException("normal");
        } catch (NormalException e) {
            System.out.println("Normal frames: " + e.getStackTrace().length);
        }

        System.out.println("\n=== 3. Suppressed exceptions ===");
        demonstrateSuppressed();

        System.out.println("\n=== 4. Benchmark ===");
        benchmarkStackDepth(100);
        benchmarkStackDepth(500);
        benchmarkStackDepth(1000);
    }
}
