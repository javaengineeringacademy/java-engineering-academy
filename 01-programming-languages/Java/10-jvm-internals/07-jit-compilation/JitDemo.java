package jvm;

/**
 * JitDemo - JIT compilation demonstration
 *
 * Covers:
 * - Just-In-Time compilation concept
 * - Method compilation thresholds
 * - JIT optimization techniques
 * - Performance implications
 */
public class JitDemo {

    private static int staticCounter = 0;
    private int instanceCounter = 0;

    public static void main(String[] args) {
        System.out.println("=== JIT Compilation Overview ===");
        jitOverview();

        System.out.println("\n=== JIT Compilation Thresholds ===");
        jitThresholds();

        System.out.println("\n=== JIT Optimizations ===");
        jitOptimizations();

        System.out.println("\n=== Measuring JIT Impact ===");
        jitPerformance();
    }

    static void jitOverview() {
        System.out.println("JIT Compilation Process:");
        System.out.println("1. Interpreter starts executing bytecode");
        System.out.println("2. Hot spots are identified (frequently called methods)");
        System.out.println("3. Hot spots are compiled to native code");
        System.out.println("4. Native code is cached and reused");
        System.out.println();
        System.out.println("Benefits:");
        System.out.println("- Faster execution of hot code paths");
        System.out.println("- Adaptive optimization based on runtime behavior");
        System.out.println("- No ahead-of-time compilation needed");
        System.out.println();
        System.out.println("Trade-offs:");
        System.out.println("- Initial startup is slower");
        System.out.println("- Memory overhead for compiled code cache");
        System.out.println("- JIT compiler uses CPU resources");
    }

    static void jitThresholds() {
        System.out.println("Default JIT Thresholds:");
        System.out.println();
        System.out.println("Client Compiler (C1):");
        System.out.println("  -XX:CompileThreshold=1500");
        System.out.println("  Compiles after 1500 method invocations");
        System.out.println();
        System.out.println("Server Compiler (C2):");
        System.out.println("  -XX:CompileThreshold=10000");
        System.out.println("  Compiles after 10000 method invocations");
        System.out.println();
        System.out.println("Tiered Compilation (default):");
        System.out.println("  Uses both C1 and C2");
        System.out.println("  -XX:+TieredCompilation");
        System.out.println();
        System.out.println("You can monitor JIT with:");
        System.out.println("  -XX:+PrintCompilation");
    }

    static void jitOptimizations() {
        System.out.println("JIT Optimization Techniques:");
        System.out.println();
        System.out.println("1. Method Inlining");
        System.out.println("   - Replaces method call with method body");
        System.out.println("   - Reduces call overhead");
        System.out.println();
        System.out.println("2. Dead Code Elimination");
        System.out.println("   - Removes unreachable code");
        System.out.println();
        System.out.println("3. Loop Optimizations");
        System.out.println("   - Loop unrolling");
        System.out.println("   - Loop fusion/fission");
        System.out.println();
        System.out.println("4. Escape Analysis");
        System.out.println("   - Allocates objects on stack when possible");
        System.out.println("   - Reduces GC pressure");
        System.out.println();
        System.out.println("5. Intrinsics");
        System.out.println("   - Optimized implementations of common methods");
        System.out.println("   - e.g., System.arraycopy(), Math.sqrt()");

        // Demonstrate method inlining
        int result = computeSum(10);
        System.out.println("\ncomputeSum(10) = " + result);
        System.out.println("This method is likely inlined by JIT");
    }

    static int computeSum(int n) {
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += i;
        }
        return sum;
    }

    static void jitPerformance() {
        System.out.println("JIT Performance Impact:");
        System.out.println();

        // Warm up JIT
        System.out.println("Warming up JIT compiler...");
        long startTime = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            computeSum(100);
        }
        long warmupTime = System.nanoTime() - startTime;
        System.out.println("Warmup time: " + (warmupTime / 1000000) + "ms");

        // Measure after JIT
        startTime = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            computeSum(100);
        }
        long optimizedTime = System.nanoTime() - startTime;
        System.out.println("Optimized time: " + (optimizedTime / 1000000) + "ms");

        System.out.println();
        System.out.println("JIT optimizations significantly improve performance");
        System.out.println("for frequently executed code paths.");
    }
}