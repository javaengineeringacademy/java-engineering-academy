package academy.javaengineering.jvm.solutions;

import java.util.*;
import java.util.concurrent.*;

/**
 * JIT Compilation Solutions - Complete implementations
 */
public class JitCompilationSolutions {

    /**
     * Exercise 1 Solution: Trigger JIT compilation
     */
    public static void triggerJitCompilation() {
        System.out.println("=== JIT Compilation Demo ===");
        System.out.println("Run with: java -XX:+PrintCompilation JitCompilationSolutions\n");

        // Cold measurement (before JIT)
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            computeMethod();
        }
        long coldTime = System.nanoTime() - start;
        System.out.println("Cold (1000 calls): " + (coldTime / 1000000) + "ms");

        // Warmup (trigger JIT)
        System.out.println("Warming up JIT...");
        for (int i = 0; i < 10000; i++) {
            computeMethod();
        }

        // Hot measurement (after JIT)
        start = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            computeMethod();
        }
        long hotTime = System.nanoTime() - start;
        System.out.println("Hot (100000 calls): " + (hotTime / 1000000) + "ms");

        System.out.println("JIT speedup: ~" + (coldTime * 100.0 / hotTime) + "x");
    }

    private static long computeMethod() {
        long sum = 0;
        for (int i = 0; i < 1000; i++) {
            sum += i * i;
        }
        return sum;
    }

    /**
     * Exercise 2 Solution: Escape analysis demonstration
     */
    public static void demonstrateEscapeAnalysis() {
        System.out.println("=== Escape Analysis Demo ===");
        System.out.println("Run with: java -XX:+DoEscapeAnalysis JitCompilationSolutions\n");

        // Scenario 1: Object escapes (must be heap-allocated)
        long start = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            int result = escapingMethod();
        }
        long escapeTime = System.nanoTime() - start;

        // Scenario 2: Object doesn't escape (can be stack-allocated)
        start = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            int result = nonEscapingMethod();
        }
        long nonEscapeTime = System.nanoTime() - start;

        System.out.println("Escaping method: " + (escapeTime / 1000000) + "ms");
        System.out.println("Non-escaping method: " + (nonEscapeTime / 1000000) + "ms");

        if (nonEscapeTime < escapeTime) {
            System.out.println("Non-escaping is " + (escapeTime / nonEscapeTime) + "x faster");
            System.out.println("(Stack allocation avoids heap allocation overhead)");
        }
    }

    private static int escapingMethod() {
        // Object escapes - must be heap-allocated
        int[] result = new int[1];
        result[0] = 42;
        return result[0]; // Array escapes via return
    }

    private static int nonEscapingMethod() {
        // Object doesn't escape - can be stack-allocated (scalar replacement)
        Point p = new Point(10, 20);
        return p.x + p.y; // Point is scalar replaced
    }

    static class Point {
        int x, y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Exercise 3 Solution: Method inlining demonstration
     */
    public static void demonstrateInlining() {
        System.out.println("=== Method Inlining Demo ===");
        System.out.println("Run with: java -XX:+UnlockDiagnosticVMOptions -XX:+PrintInlining\n");

        // Small method - good inlining candidate
        long start = System.nanoTime();
        int sum = 0;
        for (int i = 0; i < 10000000; i++) {
            sum += smallMethod(i);
        }
        long smallTime = System.nanoTime() - start;
        System.out.println("Small method (inlined): " + (smallTime / 1000000) + "ms");
        System.out.println("Result: " + sum);

        // Larger method - may not be inlined
        start = System.nanoTime();
        sum = 0;
        for (int i = 0; i < 10000000; i++) {
            sum += largerMethod(i);
        }
        long largeTime = System.nanoTime() - start;
        System.out.println("Larger method: " + (largeTime / 1000000) + "ms");
        System.out.println("Result: " + sum);
    }

    private static int smallMethod(int x) {
        return x * 2 + 1; // 3 bytecodes - prime inlining candidate
    }

    private static int largerMethod(int x) {
        int a = x * 2;
        int b = x + 1;
        int c = a * b;
        if (c > 100) {
            return c - 50;
        } else {
            return c + 50;
        }
    }

    /**
     * Exercise 4 Solution: Deoptimization demonstration
     */
    public static void demonstrateDeoptimization() {
        System.out.println("=== Deoptimization Demo ===");
        System.out.println("Run with: -XX:+UnlockDiagnosticVMOptions -XX:+PrintDeoptimizationEvents\n");

        // Initially monomorphic - C2 optimizes for Impl1
        Base[] objects = new Base[1000];
        for (int i = 0; i < 100; i++) {
            objects[i] = new Impl1();
        }

        // First, let C2 optimize with monomorphic profile
        int sum = 0;
        for (int i = 0; i < 1000000; i++) {
            sum += objects[i % 100].compute();
        }
        System.out.println("Monomorphic phase (Impl1 only): " + sum);

        // Now introduce Impl2 - makes call site megamorphic
        // This triggers deoptimization
        for (int i = 100; i < 200; i++) {
            objects[i] = new Impl2();
        }

        // Continue execution - C2 must deoptimize
        sum = 0;
        for (int i = 0; i < 1000000; i++) {
            sum += objects[i % 200].compute();
        }
        System.out.println("Megamorphic phase (Impl1 + Impl2): " + sum);

        // Add Impl3 - further megamorphic
        for (int i = 200; i < 300; i++) {
            objects[i] = new Impl3();
        }
        sum = 0;
        for (int i = 0; i < 1000000; i++) {
            sum += objects[i % 300].compute();
        }
        System.out.println("Further megamorphic phase (3 types): " + sum);

        System.out.println("Check -XX:+PrintDeoptimizationEvents to see deopt events");
    }

    static abstract class Base {
        abstract int compute();
    }

    static class Impl1 extends Base {
        int compute() { return 1; }
    }

    static class Impl2 extends Base {
        int compute() { return 2; }
    }

    static class Impl3 extends Base {
        int compute() { return 3; }
    }

    /**
     * Exercise 5 Solution: Compare compilation levels
     */
    public static void compareCompilationLevels() {
        System.out.println("=== Compilation Level Comparison ===");

        // Warm up
        System.out.println("Warming up...");
        for (int i = 0; i < 10000; i++) {
            computeMethod();
        }

        // Measure
        long start = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            computeMethod();
        }
        long elapsed = System.nanoTime() - start;
        System.out.println("Execution time: " + (elapsed / 1000000) + "ms");

        System.out.println("\nTo compare compilation levels, run separately:");
        System.out.println("  java -XX:-TieredCompilation -XX:TieredStopAtLevel=0 JitCompilationSolutions  # Interpreted");
        System.out.println("  java -XX:+TieredCompilation -XX:TieredStopAtLevel=1 JitCompilationSolutions  # C1 only");
        System.out.println("  java -XX:+TieredCompilation -XX:TieredStopAtLevel=4 JitCompilationSolutions  # C2 only");
    }

    public static void main(String[] args) {
        System.out.println("=== JIT Compilation Solutions ===\n");

        // Exercise 1
        triggerJitCompilation();

        // Exercise 2
        System.out.println("\n---");
        demonstrateEscapeAnalysis();

        // Exercise 3
        System.out.println("\n---");
        demonstrateInlining();

        // Exercise 4
        System.out.println("\n---");
        demonstrateDeoptimization();

        // Exercise 5
        System.out.println("\n---");
        compareCompilationLevels();
    }
}
