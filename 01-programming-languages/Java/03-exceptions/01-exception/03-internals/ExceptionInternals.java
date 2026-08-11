package academy.javaengineering.exceptions.exception.internals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;

/**
 * Demonstrates bytecode-level exception table behavior.
 *
 * <p>This class shows how the JVM stores and dispatches exceptions
 * using exception tables in class files. Use {@code javap -c} to
 * inspect the bytecode and see the exception table entries.</p>
 */
public class ExceptionInternalsDemo {

    // ============================================================
    // Exception table demonstration
    // ============================================================

    /**
     * Simple try-catch. Inspect with {@code javap -c} to see:
     * <pre>
     * Exception table:
     *     from    to  target type
     *         0     6     9   Class java/io/IOException
     * </pre>
     */
    static String simpleTryCatch() {
        try {
            riskyOperation();
            return "success";
        } catch (IOException e) {
            return "caught: " + e.getMessage();
        }
    }

    /**
     * Multiple catch blocks. Each generates a separate exception table entry.
     * The JVM tries them in order; first match wins.
     */
    static String multipleCatch() {
        try {
            riskyOperation();
            return "success";
        } catch (IOException e) {
            return "io: " + e.getMessage();
        } catch (IllegalArgumentException e) {
            return "illegal: " + e.getMessage();
        } catch (Exception e) {
            return "other: " + e.getMessage();
        }
    }

    /**
     * Finally block. The compiler generates multiple exception table entries:
     * one for normal flow and one for the exception path. The finally code
     * is duplicated in both paths.
     */
    static String withFinally() {
        try {
            riskyOperation();
            return "success";
        } catch (IOException e) {
            return "caught: " + e.getMessage();
        } finally {
            cleanup();
        }
    }

    /**
     * Nested try-catch. Generates separate exception tables for each try block.
     * The inner try has its own table; if the inner catch handles the exception,
     * the outer table is never consulted for that exception type.
     */
    static String nestedTryCatch() {
        try {
            try {
                riskyOperation();
            } catch (IOException e) {
                return "inner caught: " + e.getMessage();
            }
            return "outer success";
        } catch (Exception e) {
            return "outer caught: " + e.getMessage();
        }
    }

    // ============================================================
    // Helper methods
    // ============================================================

    static void riskyOperation() throws IOException {
        throw new IOException("disk error");
    }

    static void cleanup() {
        // cleanup logic
    }

    // ============================================================
    // Reflection: inspect exception table at runtime
    // ============================================================

    /**
     * Uses ASM-free reflection to print the bytecode disassembly of a method.
     * Run with {@code javap -c -p} for full output including exception tables.
     */
    static void showBytecodeInfo() throws Exception {
        Method method = ExceptionInternalsDemo.class.getDeclaredMethod("simpleTryCatch");
        System.out.println("Method: " + method.getName());
        System.out.println("Exception types declared: ");
        for (Class<?> ex : method.getExceptionTypes()) {
            System.out.println("  " + ex.getName());
        }
        System.out.println();
        System.out.println("Run: javap -c -p academy.javaengineering.exceptions.exception.internals.ExceptionInternalsDemo");
        System.out.println("Look for 'Exception table:' in the output of simpleTryCatch");
    }

    // ============================================================
    // Performance demonstration
    // ============================================================

    /**
     * Demonstrates the cost of exception creation vs normal flow.
     *
     * <p>Exception creation is expensive because fillInStackTrace() walks
     * the entire call stack. Normal control flow has zero overhead from
     * exception tables.</p>
     */
    static void performanceComparison() {
        int iterations = 10_000;

        // Normal flow - no exceptions
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            try {
                // no exception thrown
            } catch (Exception e) {
                // never reached
            }
        }
        long normalTime = System.nanoTime() - start;

        // Exception path - exceptions thrown and caught
        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            try {
                throw new Exception("test");
            } catch (Exception e) {
                // caught
            }
        }
        long exceptionTime = System.nanoTime() - start;

        System.out.printf("Normal flow:  %,d ns for %d iterations (avg: %,.1f ns/iter)%n",
                normalTime, iterations, (double) normalTime / iterations);
        System.out.printf("Exception path: %,d ns for %d iterations (avg: %,.1f ns/iter)%n",
                exceptionTime, iterations, (double) exceptionTime / iterations);
        System.out.printf("Ratio: %.1fx slower%n", (double) exceptionTime / normalTime);
    }

    // ============================================================
    // Main
    // ============================================================

    public static void main(String[] args) throws Exception {
        System.out.println("=== Simple Try-Catch ===");
        System.out.println(simpleTryCatch());

        System.out.println("\n=== Multiple Catch ===");
        System.out.println(multipleCatch());

        System.out.println("\n=== With Finally ===");
        System.out.println(withFinally());

        System.out.println("\n=== Nested Try-Catch ===");
        System.out.println(nestedTryCatch());

        System.out.println("\n=== Bytecode Info ===");
        showBytecodeInfo();

        System.out.println("\n=== Performance Comparison ===");
        performanceComparison();
    }
}
