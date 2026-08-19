package academy.javaengineering.jvm.practices;

import java.util.*;
import java.util.concurrent.*;

/**
 * JIT Compilation Exercises
 * Complete each exercise by implementing the required method.
 * Focus on tiered compilation, optimizations, and JVM behavior.
 */
public class JitCompilationExercises {

    /**
     * Exercise 1: Trigger JIT compilation
     * Write code that demonstrates JIT compilation by:
     * 1. Creating a method that does heavy computation
     * 2. Calling it many times to trigger compilation
     * 3. Measuring performance before and after JIT
     * 4. Using -XX:+PrintCompilation to observe compilation events
     *
     * Expected: After warmup, method should be significantly faster
     */
    public static void triggerJitCompilation() {
        // TODO: Implement this exercise
        // 1. Create computeMethod() with loop/arithmetic
        // 2. Time 1000 calls (cold)
        // 3. Call 10000 times for warmup
        // 4. Time 10000 calls (hot)
        // 5. Compare performance

        System.out.println("Run with: java -XX:+PrintCompilation JitCompilationExercises");
    }

    private static long computeMethod() {
        // TODO: Implement a computationally intensive method
        long sum = 0;
        for (int i = 0; i < 1000; i++) {
            sum += i;
        }
        return sum;
    }

    /**
     * Exercise 2: Demonstrate escape analysis
     * Show the effect of escape analysis on object allocation.
     *
     * Create two scenarios:
     * 1. Method where object escapes (must be heap-allocated)
     * 2. Method where object doesn't escape (can be stack-allocated)
     *
     * Compare allocation times using -XX:+DoEscapeAnalysis
     */
    public static void demonstrateEscapeAnalysis() {
        // TODO: Implement this exercise
        // Scenario 1: Object escapes method
        long start = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            int result = escapingMethod();
        }
        long escapeTime = System.nanoTime() - start;

        // Scenario 2: Object doesn't escape
        start = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            int result = nonEscapingMethod();
        }
        long nonEscapeTime = System.nanoTime() - start;

        System.out.println("Escaping method: " + (escapeTime / 1000000) + "ms");
        System.out.println("Non-escaping method: " + (nonEscapeTime / 1000000) + "ms");
        System.out.println("Run with -XX:+DoEscapeAnalysis to see difference");
    }

    private static int escapingMethod() {
        // TODO: Method where allocated object escapes
        // Create an object that is returned or stored in a field
        return 0;
    }

    private static int nonEscapingMethod() {
        // TODO: Method where allocated object doesn't escape
        // Create an object that is only used within this method
        return 0;
    }

    /**
     * Exercise 3: Demonstrate method inlining
     * Show how method inlining affects performance.
     *
     * 1. Create small helper methods that are good inlining candidates
     * 2. Create larger methods that are poor inlining candidates
     * 3. Measure the difference
     *
     * Use -XX:+UnlockDiagnosticVMOptions -XX:+PrintInlining to observe
     */
    public static void demonstrateInlining() {
        // TODO: Implement this exercise

        System.out.println("Run with: java -XX:+UnlockDiagnosticVMOptions -XX:+PrintInlining JitCompilationExercises");
    }

    private static int smallMethod(int x) {
        return x * 2 + 1;
    }

    private static int computeWithSmallMethod() {
        int sum = 0;
        for (int i = 0; i < 1000; i++) {
            sum += smallMethod(i);
        }
        return sum;
    }

    /**
     * Exercise 4: Demonstrate deoptimization
     * Create a scenario where compiled code gets deoptimized.
     *
     * Common triggers:
     * 1. Class loading that invalidates assumptions
     * 2. Virtual call site becoming megamorphic
     * 3. Speculative optimization failure
     *
     * Create code that demonstrates one of these scenarios.
     */
    public static void demonstrateDeoptimization() {
        // TODO: Implement this exercise
        // Create polymorphic method calls that change type profile
        // This can trigger C2 deoptimization

        System.out.println("Run with: -XX:+UnlockDiagnosticVMOptions -XX:+PrintDeoptimizationEvents");
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
     * Exercise 5: Compare interpreted vs compiled performance
     * Create a benchmark that shows:
     * 1. Pure interpreted performance
     * 2. C1 compiled performance
     * 3. C2 compiled performance
     *
     * Use -XX:-TieredCompilation -XX:TieredStopAtLevel=0 for interpreted
     * Use -XX:+TieredCompilation -XX:TieredStopAtLevel=1 for C1 only
     * Use -XX:+TieredCompilation -XX:TieredStopAtLevel=4 for C2 only
     */
    public static void compareCompilationLevels() {
        // TODO: Implement this exercise
        // Warm up JIT
        for (int i = 0; i < 10000; i++) {
            computeMethod();
        }

        // Measure performance
        long start = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            computeMethod();
        }
        long elapsed = System.nanoTime() - start;
        System.out.println("Execution time: " + (elapsed / 1000000) + "ms");

        System.out.println("Run with different -XX:TieredStopAtLevel values:");
        System.out.println("  0: Interpreted only");
        System.out.println("  1: C1 only");
        System.out.println("  4: C2 only");
    }

    public static void main(String[] args) {
        System.out.println("=== JIT Compilation Exercises ===\n");

        // Test Exercise 1
        System.out.println("Exercise 1: Trigger JIT Compilation");
        triggerJitCompilation();

        // Test Exercise 2
        System.out.println("\nExercise 2: Escape Analysis");
        demonstrateEscapeAnalysis();

        // Test Exercise 3
        System.out.println("\nExercise 3: Method Inlining");
        demonstrateInlining();

        // Test Exercise 4
        System.out.println("\nExercise 4: Deoptimization");
        demonstrateDeoptimization();

        // Test Exercise 5
        System.out.println("\nExercise 5: Compilation Levels");
        compareCompilationLevels();
    }
}
