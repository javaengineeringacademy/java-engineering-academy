package academy.javaengineering.jvm.jit;

import java.lang.management.CompilationMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.Random;

/**
 * Execution Engine Deep Dive
 * Covers Interpreter, C1 Compiler, C2 Compiler, Graal JIT, and Tiered Compilation.
 */
public class ExecutionEngine {

    private static final int WARMUP_ITERATIONS = 100_000;
    private static final int BENCHMARK_ITERATIONS = 10_000_000;

    public static void main(String[] args) {
        System.out.println("=== Execution Engine Deep Dive ===\n");

        // 1. Interpreter
        demonstrateInterpreter();

        // 2. C1 Compiler
        demonstrateC1Compiler();

        // 3. C2 Compiler
        demonstrateC2Compiler();

        // 4. Graal JIT
        demonstrateGraalJIT();

        // 5. Tiered Compilation
        demonstrateTieredCompilation();

        // 6. Compilation Statistics
        demonstrateCompilationStats();
    }

    /**
     * Interpreter executes bytecode line-by-line
     * - No compilation overhead
     * - Slow execution
     * - Used during startup and for cold methods
     */
    private static void demonstrateInterpreter() {
        System.out.println("--- 1. Interpreter (Bytecode Execution) ---");

        System.out.println("Interpreter characteristics:");
        System.out.println("  - Executes bytecode one instruction at a time");
        System.out.println("  - No compilation overhead");
        System.out.println("  - Slow execution (10-100x slower than native)");
        System.out.println("  - Used for cold/infrequently executed methods");
        System.out.println("  - Collects profiling data for JIT compilation\n");

        // Measure interpreted performance
        long startTime = System.nanoTime();
        int result = 0;
        for (int i = 0; i < 1_000_000; i++) {
            result += computeInterpreted(i);
        }
        long interpretedTime = (System.nanoTime() - startTime) / 1_000_000;

        System.out.println("Interpreted execution (1M iterations): " + interpretedTime + " ms");
        System.out.println("Result: " + result + "\n");
    }

    private static int computeInterpreted(int n) {
        return n * 2 + 1;
    }

    /**
     * C1 Compiler (Client Compiler)
     * - Fast compilation, basic optimizations
     * - Lower memory usage
     * - Good for startup performance
     * - Used in tiered compilation (levels 1-3)
     */
    private static void demonstrateC1Compiler() {
        System.out.println("--- 2. C1 Compiler (Client Compiler) ---");

        System.out.println("C1 Compiler characteristics:");
        System.out.println("  - Fast compilation speed");
        System.out.println("  - Basic optimizations: method inlining, constant folding");
        System.out.println("  - Lower memory footprint");
        System.out.println("  - Good for startup-time optimization");
        System.out.println("  - Used in -client mode\n");

        // Simulate C1 compilation benefits
        System.out.println("C1 optimizations:");
        System.out.println("  - Method inlining (small methods)");
        System.out.println("  - Constant folding");
        System.out.println("  - Dead code elimination");
        System.out.println("  - Basic escape analysis");
        System.out.println("  - Peephole optimizations\n");

        // Benchmark with C1-like behavior
        warmUpVM();
        long startTime = System.nanoTime();
        long sum = 0;
        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            sum += c1OptimizedMethod(i);
        }
        long c1Time = (System.nanoTime() - startTime) / 1_000_000;

        System.out.println("C1-optimized execution (10M iterations): " + c1Time + " ms");
        System.out.println("Sum: " + sum + "\n");
    }

    private static long c1OptimizedMethod(int n) {
        return (long) n * n + n;
    }

    /**
     * C2 Compiler (Server Compiler)
     * - Slower compilation, aggressive optimizations
     * - Higher memory usage
     * - Good for peak performance
     * - Used in tiered compilation (level 4)
     */
    private static void demonstrateC2Compiler() {
        System.out.println("--- 3. C2 Compiler (Server Compiler) ---");

        System.out.println("C2 Compiler characteristics:");
        System.out.println("  - Slower compilation speed");
        System.out.println("  - Aggressive optimizations: deep inlining, loop unrolling");
        System.out.println("  - Higher memory footprint");
        System.out.println("  - Best for peak performance");
        System.out.println("  - Used in -server mode\n");

        System.out.println("C2 advanced optimizations:");
        System.out.println("  - Aggressive method inlining (including virtual calls)");
        System.out.println("  - Loop unrolling and vectorization");
        System.out.println("  - Escape analysis (stack allocation)");
        System.out.println("  - Lock elimination (biased locking)");
        System.out.println("  - Null check elimination");
        System.out.println("  - Range check elimination");
        System.out.println("  - Intrinsics (System.arraycopy, Math.sin, etc.)\n");

        // Benchmark with C2 optimizations
        warmUpVM();
        long startTime = System.nanoTime();
        long sum = 0;
        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            sum += c2OptimizedMethod(i);
        }
        long c2Time = (System.nanoTime() - startTime) / 1_000_000;

        System.out.println("C2-optimized execution (10M iterations): " + c2Time + " ms");
        System.out.println("Sum: " + sum + "\n");
    }

    private static long c2OptimizedMethod(int n) {
        long x = (long) n;
        return x * x * x + x * x + x + 1;
    }

    /**
     * Graal JIT Compiler
     * - Written in Java (self-hosting)
     * - Advanced optimizations
     * - Experimental (Java 17+)
     * - Enables AOT compilation with GraalVM Native Image
     */
    private static void demonstrateGraalJIT() {
        System.out.println("--- 4. Graal JIT Compiler ---");

        System.out.println("Graal JIT characteristics:");
        System.out.println("  - Written in Java (self-hosting compiler)");
        System.out.println("  - Advanced optimization techniques");
        System.out.println("  - Supports Partial Escape Analysis");
        System.out.println("  - Enables Java-based compiler research");
        System.out.println("  - Experimental in OpenJDK\n");

        System.out.println("Graal advantages over C2:");
        System.out.println("  - Better Partial Escape Analysis");
        System.out.println("  - Improved inlining decisions");
        System.out.println("  - Better loop optimizations");
        System.out.println("  - Enables Native Image (AOT compilation)\n");

        System.out.println("GraalVM Native Image:");
        System.out.println("  - Ahead-of-time compilation to native executable");
        System.out.println("  - Fast startup (<100ms)");
        System.out.println("  - Low memory footprint");
        System.out.println("  - No JIT compilation at runtime\n");

        System.out.println("Enable Graal JIT:");
        System.out.println("  java -XX:+UnlockExperimentalVMOptions -XX:+UseJVMCICompiler MyApp");
        System.out.println("  Or use GraalVM: https://www.graalvm.org/\n");
    }

    /**
     * Tiered Compilation
     * Levels 0-4: Interpreter → C1 → C2
     */
    private static void demonstrateTieredCompilation() {
        System.out.println("--- 5. Tiered Compilation ---");

        System.out.println("Tiered Compilation Levels:");
        System.out.println("  Level 0: Interpreter");
        System.out.println("    - Bytecode is interpreted");
        System.out.println("    - Profiling data collected");
        System.out.println("  Level 1: C1 - Simple (no profiling)");
        System.out.println("    - Fast compilation, basic optimizations");
        System.out.println("    - No profiling overhead");
        System.out.println("  Level 2: C1 - Limited profiling");
        System.out.println("    - Basic profiling, moderate optimizations");
        System.out.println("  Level 3: C1 - Full profiling");
        System.out.println("    - Full profiling data for C2");
        System.out.println("    - Most C1 optimizations");
        System.out.println("  Level 4: C2 - Full optimization");
        System.out.println("    - All C2 optimizations applied");
        System.out.println("    - Best peak performance\n");

        System.out.println("Tiered Compilation Flow:");
        System.out.println("  1. Method called → Interpreter (L0)");
        System.out.println("  2. Hot method detected → C1 compile (L3)");
        System.out.println("  3. C1 collects profiling data");
        System.out.println("  4. Method still hot → C2 compile (L4)");
        System.out.println("  5. C2 applies aggressive optimizations\n");

        System.out.println("Tiered Compilation Flags:");
        System.out.println("  -XX:+TieredCompilation        (enable, default=true)");
        System.out.println("  -XX:TieredStopAtLevel=4       (max level, default=4)");
        System.out.println("  -XX:CompileThreshold=10000    (invocations before compile)");
        System.out.println("  -XX:-TieredCompilation        (disable for C2 only)\n");

        // Demonstrate tiered compilation benefits
        System.out.println("Tiered compilation benefits:");
        System.out.println("  - Faster startup (C1 compiles quickly)");
        System.out.println("  - Better peak performance (C2 optimizes hot paths)");
        System.out.println("  - Smooth performance ramp-up\n");
    }

    /**
     * Compilation Statistics
     * Monitor JIT compilation activity
     */
    private static void demonstrateCompilationStats() {
        System.out.println("--- 6. Compilation Statistics ---");

        CompilationMXBean compilationBean = ManagementFactory.getCompilationMXBean();
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

        // Warm up and run workload
        warmUpVM();
        runWorkload();

        System.out.println("Compilation Information:");
        System.out.println("  Compiler: " + compilationBean.getName());
        System.out.println("  Total compilation time: " +
                compilationBean.getTotalCompilationTime() + " ms\n");

        System.out.println("Memory Usage:");
        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        MemoryUsage nonHeapUsage = memoryBean.getNonHeapMemoryUsage();
        System.out.println("  Heap Used: " + formatMB(heapUsage.getUsed()));
        System.out.println("  Heap Committed: " + formatMB(heapUsage.getCommitted()));
        System.out.println("  Non-Heap Used: " + formatMB(nonHeapUsage.getUsed()));
        System.out.println("  Non-Heap Committed: " + formatMB(nonHeapUsage.getCommitted()));
        System.out.println();

        System.out.println("JIT Diagnostic Flags:");
        System.out.println("  -XX:+PrintCompilation              (print compilation events)");
        System.out.println("  -XX:+UnlockDiagnosticVMOptions");
        System.out.println("  -XX:+PrintInlining                  (print inlining decisions)");
        System.out.println("  -XX:+PrintCodeCache                 (print code cache usage)");
        System.out.println("  -XX:+PrintEscapeAnalysis            (print escape analysis)");
        System.out.println("  -XX:+PrintAssembly                  (print native assembly)");
        System.out.println();

        System.out.println("Code Cache Configuration:");
        System.out.println("  -XX:InitialCodeCacheSize=256k       (initial size)");
        System.out.println("  -XX:ReservedCodeCacheSize=256m      (max size, default=240MB)");
        System.out.println("  -XX:CodeCacheExpansionSize=64       (expansion size in bytes)");
        System.out.println();
    }

    private static void warmUpVM() {
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            computeWorkload(i);
        }
    }

    private static void runWorkload() {
        long startTime = System.nanoTime();
        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            computeWorkload(i);
        }
        long duration = (System.nanoTime() - startTime) / 1_000_000;
        System.out.println("Workload execution: " + duration + " ms");
    }

    private static long computeWorkload(int n) {
        return (long) n * n + n * 3 + 7;
    }

    private static String formatMB(long bytes) {
        return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
    }
}
