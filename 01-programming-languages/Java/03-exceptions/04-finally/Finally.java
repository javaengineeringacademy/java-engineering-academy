package academy.javaengineering.exceptions.finallyblock;

/**
 * Demonstrates the finally block — guaranteed execution, execution order,
 * return value override, exception masking, and resource cleanup patterns
 * including ThreadLocal and timer/metrics.
 *
 * <p><b>Complexity:</b> O(1) per operation unless noted.</p>
 * <p><b>Thread-safety:</b> Not thread-safe — uses static mutable state.</p>
 * <p><b>Key characteristics:</b> Covers finally execution guarantees,
 * dangerous patterns (return override, exception masking), and
 * safe exception preservation.</p>
 */
package academy.javaengineering.exceptions.finallyblock;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates the finally block — guaranteed execution, execution order,
 * return value override, and exception masking.
 */
public class Finally {

    // Simple resource that tracks close calls
    static class TrackableResource implements AutoCloseable {
        private final String name;
        private boolean closed = false;

        TrackableResource(String name) {
            this.name = name;
            System.out.println("Opened: " + name);
        }

        boolean isClosed() {
            return closed;
        }

        @Override
        public void close() {
            closed = true;
            System.out.println("Closed: " + name);
        }
    }

    /**
     * Demonstrates basic try-finally execution order.
     */
    static void basicTryFinally() {
        System.out.println("=== Basic try-finally ===");
        try {
            System.out.println("try block");
        } finally {
            System.out.println("finally block");
        }
        System.out.println();
    }

    /**
     * Demonstrates try-catch-finally execution order.
     */
    static void tryCatchFinally() {
        System.out.println("=== try-catch-finally ===");
        try {
            System.out.println("try block");
            throw new RuntimeException("boom");
        } catch (RuntimeException e) {
            System.out.println("catch block: " + e.getMessage());
        } finally {
            System.out.println("finally block");
        }
        System.out.println();
    }

    /**
     * Demonstrates finally runs even when exception is not caught.
     */
    static void finallyWithoutCatch() {
        System.out.println("=== finally without catch ===");
        try {
            System.out.println("try block");
            throw new IllegalStateException("uncaught");
        } finally {
            System.out.println("finally block runs anyway");
        }
    }

    /**
     * Demonstrates finally execution on normal return.
     */
    static void finallyOnNormalReturn() {
        System.out.println("=== finally on normal return ===");
        String result = doWork();
        System.out.println("Result: " + result);
        System.out.println();
    }

    static String doWork() {
        try {
            System.out.println("try: returning value");
            return "from try";
        } finally {
            System.out.println("finally: runs before return");
        }
    }

    /**
     * Demonstrates finally execution on exception return.
     */
    static void finallyOnExceptionReturn() {
        System.out.println("=== finally on exception return ===");
        String result = doWorkWithException();
        System.out.println("Result: " + result);
        System.out.println();
    }

    static String doWorkWithException() {
        try {
            System.out.println("try: throwing exception");
            throw new RuntimeException("boom");
        } catch (RuntimeException e) {
            System.out.println("catch: " + e.getMessage());
            return "from catch";
        } finally {
            System.out.println("finally: runs before return");
        }
    }

    /**
     * DANGER: Demonstrates return in finally overriding try return.
     */
    static void dangerousReturnOverride() {
        System.out.println("=== DANGEROUS: return in finally ===");
        int result = dangerousReturn();
        System.out.println("Expected 1, got: " + result);
        System.out.println("This is almost always a bug!\n");
    }

    static int dangerousReturn() {
        try {
            System.out.println("try: returning 1");
            return 1;
        } finally {
            System.out.println("finally: returning 2 (overrides!)");
            return 2;
        }
    }

    /**
     * DANGER: Demonstrates finally exception masking try exception.
     */
    static void dangerousExceptionMasking() {
        System.out.println("=== DANGEROUS: exception in finally ===");
        try {
            System.out.println("try: throwing original");
            throw new RuntimeException("original");
        } finally {
            System.out.println("finally: throwing new (masks original)");
            throw new RuntimeException("finally");
        }
    }

    /**
     * Demonstrates multiple finally blocks with nested try.
     */
    static void nestedFinally() {
        System.out.println("=== Nested finally blocks ===");
        try {
            System.out.println("outer try");
            try {
                System.out.println("inner try");
                throw new RuntimeException("inner exception");
            } finally {
                System.out.println("inner finally");
            }
        } catch (RuntimeException e) {
            System.out.println("outer catch: " + e.getMessage());
        } finally {
            System.out.println("outer finally");
        }
        System.out.println();
    }

    /**
     * Demonstrates finally with conditional cleanup logic.
     */
    static void conditionalCleanup() {
        System.out.println("=== Conditional cleanup in finally ===");
        boolean resourceAcquired = false;
        try {
            System.out.println("Acquiring resource...");
            resourceAcquired = true;
            System.out.println("Using resource");
            if (Math.random() > 0.5) {
                throw new RuntimeException("random failure");
            }
            System.out.println("Completed successfully");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (resourceAcquired) {
                System.out.println("Releasing resource in finally");
            }
        }
        System.out.println();
    }

    /**
     * Demonstrates multiple catch blocks with finally.
     */
    static void multipleCatchFinally() {
        System.out.println("=== Multiple catch blocks with finally ===");
        try {
            System.out.println("throwing IllegalArgumentException");
            throw new IllegalArgumentException("bad arg");
        } catch (IllegalArgumentException e) {
            System.out.println("caught IllegalArgumentException: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("caught RuntimeException: " + e.getMessage());
        } finally {
            System.out.println("finally block");
        }
        System.out.println();
    }

    /**
     * Demonstrates finally with resource cleanup pattern.
     */
    static void resourceCleanupPattern() {
        System.out.println("=== Resource cleanup pattern ===");
        TrackableResource resource = new TrackableResource("database");
        try {
            System.out.println("Processing with resource");
            if (Math.random() > 0.7) {
                throw new RuntimeException("processing failed");
            }
            System.out.println("Processing complete");
        } catch (RuntimeException e) {
            System.out.println("Error during processing: " + e.getMessage());
        } finally {
            resource.close();
            System.out.println("Resource cleaned up, isClosed=" + resource.isClosed());
        }
        System.out.println();
    }

    /**
     * Demonstrates finally with ThreadLocal cleanup.
     */
    static void threadLocalCleanup() {
        System.out.println("=== ThreadLocal cleanup in finally ===");
        ThreadLocal<String> context = new ThreadLocal<>();

        context.set("request-123");
        try {
            System.out.println("Context: " + context.get());
            // Process request...
        } finally {
            context.remove(); // prevent memory leak
            System.out.println("Context removed in finally");
        }
        System.out.println("Context after: " + context.get());
        System.out.println();
    }

    /**
     * Demonstrates finally with timer/metrics pattern.
     */
    static void timerPattern() {
        System.out.println("=== Timer/metrics in finally ===");
        long start = System.nanoTime();
        try {
            // Simulate work
            for (int i = 0; i < 1_000_000; i++) {
                Math.sqrt(i);
            }
            System.out.println("Work completed");
        } finally {
            long duration = System.nanoTime() - start;
            System.out.println("Duration: " + duration + " ns");
        }
        System.out.println();
    }

    /**
     * Demonstrates safe exception preservation with finally.
     */
    static void safeExceptionPreservation() {
        System.out.println("=== Safe exception preservation ===");
        RuntimeException original = null;
        try {
            throw new RuntimeException("original error");
        } catch (RuntimeException e) {
            original = e;
        } finally {
            try {
                throw new RuntimeException("cleanup error");
            } catch (RuntimeException e) {
                if (original != null) {
                    original.addSuppressed(e);
                }
            }
        }
        System.out.println("Original: " + original.getMessage());
        System.out.println("Suppressed: " + original.getSuppressed().length);
        System.out.println();
    }

    public static void main(String[] args) {
        basicTryFinally();
        tryCatchFinally();
        // finallyWithoutCatch(); // would throw — uncomment to see behavior
        finallyOnNormalReturn();
        finallyOnExceptionReturn();
        dangerousReturnOverride();
        // dangerousExceptionMasking(); // would throw — uncomment to see behavior
        nestedFinally();
        conditionalCleanup();
        multipleCatchFinally();
        resourceCleanupPattern();
        threadLocalCleanup();
        timerPattern();
        safeExceptionPreservation();
    }
}
