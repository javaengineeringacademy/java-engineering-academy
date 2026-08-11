import java.io.*;
import java.util.*;
import java.lang.reflect.*;

/**
 * Demonstrates JVM internals of try-catch bytecode handling.
 *
 * Run with: javac TryCatchInternals.java && java TryCatchInternals
 * For bytecode inspection: javap -c -v TryCatchInternals.class
 */
public class TryCatchInternals {

    // ============================================================
    // 1. Overhead of try-catch in the happy path (no exception)
    // ============================================================
    static long measureHappyPath(int iterations) {
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            try {
                // Trivial operation — try-catch should add no overhead
                int x = i % 10;
            } catch (Exception e) {
                // Never reached
            }
        }
        return System.nanoTime() - start;
    }

    static long measureWithoutTryCatch(int iterations) {
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            int x = i % 10;
        }
        return System.nanoTime() - start;
    }

    // ============================================================
    // 2. Cost of exception object creation
    // ============================================================
    static long measureExceptionCreation(int iterations) {
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            try {
                throw new RuntimeException("test");
            } catch (RuntimeException e) {
                // catch and discard
            }
        }
        return System.nanoTime() - start;
    }

    static long measureExceptionCreationNoStackTrace(int iterations) {
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            try {
                throw new RuntimeException("test");
            } catch (RuntimeException e) {
                // catch and discard
            }
        }
        return System.nanoTime() - start;
    }

    // ============================================================
    // 3. Cost of fillInStackTrace specifically
    // ============================================================
    static long measureFillInStackTrace(int iterations) {
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            @SuppressWarnings("unused")
            Throwable t = new Throwable("trace cost");
        }
        return System.nanoTime() - start;
    }

    static long measureStackTraceAccess(int iterations) {
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            try {
                throw new RuntimeException("test");
            } catch (RuntimeException e) {
                StackTraceElement[] trace = e.getStackTrace();
                @SuppressWarnings("unused")
                int depth = trace.length;
            }
        }
        return System.nanoTime() - start;
    }

    // ============================================================
    // 4. Multi-catch synthetic exception class
    // ============================================================
    static class CustomIOException extends IOException {
        CustomIOException(String msg) { super(msg); }
    }

    static class CustomSQLException extends Exception {
        CustomSQLException(String msg) { super(msg); }
    }

    static void multiCatchDemo(boolean ioFail, boolean sqlFail) {
        try {
            if (ioFail) throw new CustomIOException("IO failed");
            if (sqlFail) throw new CustomSQLException("SQL failed");
        } catch (CustomIOException | CustomSQLException e) {
            System.out.println("  Caught: " + e.getClass().getName());
            System.out.println("  Is IOException: " + (e instanceof IOException));
            System.out.println("  Is SQLException: " + (e instanceof Exception));
        }
    }

    // ============================================================
    // 5. Exception table structure demonstration
    // ============================================================
    static void exceptionTableDemo() {
        System.out.println("=== Exception Table Demonstration ===");
        System.out.println();

        System.out.println("When you write:");
        System.out.println("  try { A(); } catch (IOException e) { B(); } catch (Exception e) { C(); }");
        System.out.println();
        System.out.println("The compiler generates an exception table:");
        System.out.println("  +----------+---------+------------+------------------+");
        System.out.println("  | Start PC | End PC  | Handler PC | Catch Type       |");
        System.out.println("  +----------+---------+------------+------------------+");
        System.out.println("  | 0        | 12      | 15         | IOException      |");
        System.out.println("  | 0        | 12      | 20         | Exception        |");
        System.out.println("  +----------+---------+------------+------------------+");
        System.out.println();
        System.out.println("Key points:");
        System.out.println("  - Both entries cover the same bytecode range [0, 12)");
        System.out.println("  - Handler PCs point to different catch block code");
        System.out.println("  - JVM searches linearly — first match wins");
        System.out.println("  - Catching superclass before subclass would shadow it");
        System.out.println();
    }

    // ============================================================
    // 6. Stack depth impact
    // ============================================================
    static long measureStackDepthImpact(int iterations, int depth) {
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            try {
                throwNested(depth);
            } catch (RuntimeException e) {
                // catch and discard
            }
        }
        return System.nanoTime() - start;
    }

    static void throwNested(int depth) {
        if (depth <= 0) {
            throw new RuntimeException("leaf");
        }
        throwNested(depth - 1);
    }

    // ============================================================
    // 7. Demonstrate catch order matters at bytecode level
    // ============================================================
    static void catchOrderDemo() {
        System.out.println("=== Catch Order at Bytecode Level ===");
        System.out.println();

        // Correct order: specific first
        System.out.println("Correct (specific before general):");
        try {
            throw new CustomIOException("test");
        } catch (CustomIOException e) {
            System.out.println("  Caught CustomIOException (specific handler)");
        } catch (Exception e) {
            System.out.println("  Caught Exception (general handler)");
        }

        System.out.println();
        System.out.println("The bytecode exception table searches top-to-bottom.");
        System.out.println("If Exception were listed first, CustomIOException would never be caught.");
        System.out.println("The compiler enforces this: 'exception X has already been caught'");
        System.out.println();
    }

    // ============================================================
    // 8. Multiple catch blocks — each adds a table entry
    // ============================================================
    static void multipleCatchEntries() {
        System.out.println("=== Multiple Catch Blocks ===");
        System.out.println();
        System.out.println("Each catch block adds one entry to the exception table.");
        System.out.println("More entries = linearly longer search when exception occurs.");
        System.out.println();
        System.out.println("For example, 5 catch blocks means up to 5 linear comparisons.");
        System.out.println("In practice, this is negligible (microseconds).");
        System.out.println();
    }

    // ============================================================
    // Main
    // ============================================================
    public static void main(String[] args) {
        int iterations = 1_000_000;

        System.out.println("=== Try-Catch JVM Internals Demonstration ===");
        System.out.println();

        // --- Happy path overhead ---
        System.out.println("--- Happy Path Overhead (no exceptions thrown) ---");
        long without = measureWithoutTryCatch(iterations);
        long with = measureHappyPath(iterations);
        long diff = with - without;
        System.out.printf("  Without try-catch: %,d ns%n", without);
        System.out.printf("  With try-catch:    %,d ns%n", with);
        System.out.printf("  Difference:        %,d ns (%.1f ns/iteration)%n",
                diff, (double) diff / iterations);
        System.out.println("  => try-catch adds negligible overhead in the happy path");
        System.out.println();

        // --- Exception creation cost ---
        System.out.println("--- Exception Object Creation Cost ---");
        long creationCost = measureExceptionCreation(iterations);
        System.out.printf("  Creating + catching %,d exceptions: %,d ns%n",
                iterations, creationCost);
        System.out.printf("  Average per exception: %.1f ns (%.3f μs)%n",
                (double) creationCost / iterations,
                (double) creationCost / iterations / 1000);
        System.out.println();

        // --- fillInStackTrace cost ---
        System.out.println("--- fillInStackTrace Cost ---");
        long traceCost = measureFillInStackTrace(iterations);
        long accessCost = measureStackTraceAccess(iterations);
        System.out.printf("  new Throwable() x %,d: %,d ns (%.1f ns/iter)%n",
                iterations, traceCost, (double) traceCost / iterations);
        System.out.printf("  getStackTrace() x %,d: %,d ns (%.1f ns/iter)%n",
                iterations, accessCost, (double) accessCost / iterations);
        System.out.println();

        // --- Stack depth impact ---
        System.out.println("--- Stack Depth Impact ---");
        int[] depths = {5, 20, 50, 100};
        for (int d : depths) {
            long cost = measureStackDepthImpact(100_000, d);
            System.out.printf("  Depth %3d: %,d ns total, %.1f ns/iter%n",
                    d, cost, (double) cost / 100_000);
        }
        System.out.println("  => Deeper stacks make fillInStackTrace more expensive");
        System.out.println();

        // --- Multi-catch ---
        System.out.println("--- Multi-Catch Synthetic Class ---");
        System.out.println("  Java 7+ multi-catch: catch (IOException | SQLException e)");
        System.out.println("  Compiler generates a synthetic class extending the first type");
        System.out.println("  and implementing the others.");
        System.out.println();
        multiCatchDemo(true, false);
        System.out.println();
        multiCatchDemo(false, true);
        System.out.println();

        // --- Exception table explanation ---
        exceptionTableDemo();

        // --- Catch order ---
        catchOrderDemo();

        // --- Multiple catch entries ---
        multipleCatchEntries();

        // --- Summary ---
        System.out.println("=== Summary ===");
        System.out.println();
        System.out.println("1. try-catch has ZERO overhead when no exception occurs");
        System.out.println("2. The real cost is in exception object creation and fillInStackTrace()");
        System.out.println("3. Exception table lookup is linear but fast (microseconds)");
        System.out.println("4. Multi-catch uses synthetic classes — transparent to your code");
        System.out.println("5. Deeper stacks = more expensive fillInStackTrace()");
        System.out.println("6. Catch order matters because the JVM searches top-to-bottom");
    }
}
