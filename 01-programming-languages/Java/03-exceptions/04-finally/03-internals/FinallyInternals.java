import java.io.*;
import java.util.*;

/**
 * Demonstrates JVM internals of finally block handling.
 *
 * Run with: javac FinallyInternals.java && java FinallyInternals
 * For bytecode inspection: javap -c -v FinallyInternals.class
 */
public class FinallyInternals {

    static int cleanupCount = 0;

    static void cleanup() {
        cleanupCount++;
        System.out.println("  [cleanup] Called (count: " + cleanupCount + ")");
    }

    // ============================================================
    // 1. Finally bytecode duplication (visible via javap)
    // ============================================================
    static int normalReturn() {
        try {
            System.out.println("  [try] Normal path");
            return 1;
        } finally {
            cleanup();
        }
    }

    // ============================================================
    // 2. Return value override problem
    // ============================================================
    static int finallyOverridesReturn() {
        try {
            System.out.println("  [try] Returning 1");
            return 1;
        } finally {
            System.out.println("  [finally] Returning 2 (overrides!)");
            return 2;
        }
    }

    // ============================================================
    // 3. Exception swallowing problem
    // ============================================================
    static void finallySwallowsException() {
        try {
            System.out.println("  [try] Throwing original exception");
            throw new RuntimeException("original");
        } finally {
            System.out.println("  [finally] Throwing new exception (swallows original!)");
            throw new RuntimeException("from-finally");
        }
    }

    // ============================================================
    // 4. Multiple exit paths — duplication count
    // ============================================================
    static int multipleExitPaths(boolean flag) {
        try {
            if (flag) {
                return 1; // exit path 1: return from try
            }
            return 2; // exit path 2: return from try (different point)
        } catch (Exception e) {
            return 3; // exit path 3: return from catch
        } finally {
            cleanup(); // This is duplicated into ALL THREE exit paths
        }
    }

    // ============================================================
    // 5. Finally runs even on exception (caught by caller)
    // ============================================================
    static void finallyRunsOnException() {
        try {
            System.out.println("  [try] Throwing exception");
            throw new IllegalStateException("boom");
        } finally {
            cleanup(); // Runs even though exception propagates
        }
    }

    // ============================================================
    // 6. Nested try-finally duplication
    // ============================================================
    static int nestedFinally() {
        try {
            try {
                return 1;
            } finally {
                cleanup(); // duplicated for inner try's exit paths
            }
        } finally {
            cleanup(); // duplicated for outer try's exit paths
        }
    }

    // ============================================================
    // 7. Finally with break/continue in loops
    // ============================================================
    static int finallyWithLoop() {
        int sum = 0;
        for (int i = 0; i < 5; i++) {
            try {
                if (i == 3) continue; // finally still runs before continue
                sum += i;
            } finally {
                // This runs before every continue and every break
            }
        }
        return sum;
    }

    // ============================================================
    // 8. Performance measurement
    // ============================================================
    static long measureWithFinally(int iterations) {
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            try {
                @SuppressWarnings("unused")
                int x = i % 10;
            } finally {
                // Empty finally — should have minimal cost
            }
        }
        return System.nanoTime() - start;
    }

    static long measureWithoutFinally(int iterations) {
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            @SuppressWarnings("unused")
            int x = i % 10;
        }
        return System.nanoTime() - start;
    }

    static long measureWithFinallyBody(int iterations) {
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            try {
                @SuppressWarnings("unused")
                int x = i % 10;
            } finally {
                cleanupCount++; // Non-trivial finally body
            }
        }
        return System.nanoTime() - start;
    }

    // ============================================================
    // Main
    // ============================================================
    public static void main(String[] args) {
        int iterations = 1_000_000;

        System.out.println("=== Finally Block JVM Internals ===");
        System.out.println();

        // --- Normal return ---
        System.out.println("--- Normal Return (finally runs after try) ---");
        cleanupCount = 0;
        int result = normalReturn();
        System.out.println("  Returned: " + result);
        System.out.println();

        // --- Return value override ---
        System.out.println("--- Return Value Override Problem ---");
        System.out.println("  When finally returns, it overrides the try return:");
        int overridden = finallyOverridesReturn();
        System.out.println("  Final returned value: " + overridden + " (not 1!)");
        System.out.println("  => This is a common source of bugs");
        System.out.println();

        // --- Exception swallowing ---
        System.out.println("--- Exception Swallowing Problem ---");
        System.out.println("  When finally throws, it replaces any pending exception:");
        try {
            finallySwallowsException();
        } catch (RuntimeException e) {
            System.out.println("  Caught: " + e.getMessage());
            System.out.println("  => The 'original' exception was lost!");
        }
        System.out.println();

        // --- Multiple exit paths ---
        System.out.println("--- Multiple Exit Paths (duplication) ---");
        System.out.println("  The finally block is duplicated for each exit path:");
        System.out.println("  Path 1: return from try (if flag=true)");
        System.out.println("  Path 2: return from try (if flag=false)");
        System.out.println("  Path 3: return from catch");
        System.out.println("  Each path gets its own copy of the finally bytecode.");
        cleanupCount = 0;
        System.out.println("  flag=true:  " + multipleExitPaths(true));
        System.out.println("  flag=false: " + multipleExitPaths(false));
        System.out.println();

        // --- Finally on uncaught exception ---
        System.out.println("--- Finally on Uncaught Exception ---");
        System.out.println("  Finally runs even when exception is not caught here:");
        cleanupCount = 0;
        try {
            finallyRunsOnException();
        } catch (IllegalStateException e) {
            System.out.println("  Caught by outer handler: " + e.getMessage());
            System.out.println("  Cleanup was called: " + (cleanupCount > 0));
        }
        System.out.println();

        // --- Nested finally ---
        System.out.println("--- Nested Try-Finally Duplication ---");
        cleanupCount = 0;
        int nested = nestedFinally();
        System.out.println("  Returned: " + nested);
        System.out.println("  Cleanup called " + cleanupCount + " times (each finally duplicated)");
        System.out.println();

        // --- Finally with loops ---
        System.out.println("--- Finally with Loop Control Flow ---");
        int loopSum = finallyWithLoop();
        System.out.println("  Sum of 0+1+2+4 (skip 3): " + loopSum);
        System.out.println("  => finally runs before continue/break in the loop");
        System.out.println();

        // --- Performance ---
        System.out.println("--- Performance Impact ---");
        long noFinally = measureWithoutFinally(iterations);
        long emptyFinally = measureWithFinally(iterations);
        long bodyFinally = measureWithFinallyBody(iterations);
        System.out.printf("  Without finally:   %,d ns%n", noFinally);
        System.out.printf("  Empty finally:     %,d ns (diff: %,d ns)%n",
                emptyFinally, emptyFinally - noFinally);
        System.out.printf("  Finally with body: %,d ns (diff: %,d ns)%n",
                bodyFinally, bodyFinally - noFinally);
        System.out.println("  => Empty finally has near-zero overhead");
        System.out.println("  => Non-trivial finally body adds measurable cost");
        System.out.println();

        // --- Summary ---
        System.out.println("=== Summary ===");
        System.out.println();
        System.out.println("1. finally is a COMPILER transformation, not a JVM instruction");
        System.out.println("2. The finally bytecode is duplicated into every exit path");
        System.out.println("3. return from finally silently overrides the try return value");
        System.out.println("4. throw from finally replaces any pending exception");
        System.out.println("5. Nested try-finally causes multiplicative duplication");
        System.out.println("6. Empty finally blocks have negligible runtime cost");
        System.out.println("7. The JVM has no native 'finally' support in bytecode");
        System.out.println();
        System.out.println("Inspect bytecode with: javap -c -v FinallyInternals.class");
        System.out.println("Look for duplicated invokestatic cleanup() calls at each return point.");
    }
}
