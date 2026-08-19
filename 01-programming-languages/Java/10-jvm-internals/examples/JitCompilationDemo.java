package academy.javaengineering.jvm.examples;

import java.util.*;
import java.util.concurrent.*;

/**
 * JIT Compilation Demo
 * Demonstrates Just-In-Time compilation concepts, tiered compilation,
 * C1/C2 compilers, on-stack replacement, and optimization techniques.
 */
public class JitCompilationDemo {

    private static int callCount = 0;

    /**
     * DEMO 1: JIT Compilation Overview
     */
    public static void demonstrateJitOverview() {
        System.out.println("=== JIT Compilation Overview ===");
        System.out.println("JIT converts bytecode -> native machine code at runtime");
        System.out.println("Goal: Best of both worlds (portability + performance)");
        System.out.println();
        System.out.println("Execution flow:");
        System.out.println("  1. Bytecode loaded by classloader");
        System.out.println("  2. Interpreter executes bytecode directly");
        System.out.println("  3. Profiling collects method invocation counts");
        System.out.println("  4. Hot methods identified (invocation threshold)");
        System.out.println("  5. JIT compiler generates optimized native code");
        System.out.println("  6. Native code replaces interpreted execution");

        System.out.println("\nCompile thresholds:");
        System.out.println("  -XX:CompileThreshold=10000  (C1, client)");
        System.out.println("  -XX:CompileThreshold=10000  (C2, server, default in tiered)");
    }

    /**
     * DEMO 2: Tiered Compilation
     */
    public static void demonstrateTieredCompilation() {
        System.out.println("\n=== Tiered Compilation ===");
        System.out.println("JDK uses tiered compilation by default (C1 + C2 pipeline):");
        System.out.println();
        System.out.println("┌────────┬──────────────┬───────────────────────────────────┐");
        System.out.println("│ Tier   │ Compiler     │ Description                       │");
        System.out.println("├────────┼──────────────┼───────────────────────────────────┤");
        System.out.println("│ 0      │ Interpreter  │ Pure interpretation               │");
        System.out.println("│ 1      │ C1           │ Limited profiling, no C2           │");
        System.out.println("│ 2      │ C1           │ Full profiling, no C2              │");
        System.out.println("│ 3      │ C1           │ C1 compiled, no profiling          │");
        System.out.println("│ 4      │ C2           │ Fully optimized compiled code      │");
        System.out.println("└────────┴──────────────┴───────────────────────────────────┘");

        System.out.println("\nTier progression:");
        System.out.println("  Tier 0 -> Tier 1 (after CompileThreshold/5 invocations)");
        System.out.println("  Tier 1 -> Tier 2 (collect profiling data)");
        System.out.println("  Tier 2 -> Tier 4 (send to C2 for optimization)");
        System.out.println("\nDisable tiered: -XX:-TieredCompilation");
        System.out.println("Compile only C2: -XX:+TieredCompilation -XX:TieredStopAtLevel=4");
    }

    /**
     * DEMO 3: C1 vs C2 Compilers
     */
    public static void demonstrateC1vsC2() {
        System.out.println("\n=== C1 vs C2 Compilers ===");
        System.out.println("C1 (Client Compiler):");
        System.out.println("  - Fast compilation");
        System.out.println("  - Basic optimizations:");
        System.out.println("    * Method inlining");
        System.out.println("    * Constant folding");
        System.out.println("    * Dead code elimination");
        System.out.println("    * Null check elimination");
        System.out.println("  - Limited escape analysis");
        System.out.println("  - No loop unrolling");

        System.out.println("\nC2 (Server Compiler):");
        System.out.println("  - Slower compilation (worth it for hot methods)");
        System.out.println("  - Aggressive optimizations:");
        System.out.println("    * Advanced inlining (polymorphic, megamorphic)");
        System.out.println("    * Loop unrolling and vectorization");
        System.out.println("    * Escape analysis (scalar replacement)");
        System.out.println("    * Intrinsics (native optimized methods)");
        System.out.println("    * Loop peeling and unswitching");
        System.out.println("    * Range check elimination");
        System.out.println("    * Instruction scheduling");
        System.out.println("    * Register allocation");
    }

    /**
     * DEMO 4: Key JIT Optimizations
     */
    public static void demonstrateOptimizations() {
        System.out.println("\n=== Key JIT Optimizations ===");

        // Method Inlining
        System.out.println("1. Method Inlining:");
        System.out.println("   Before: int result = add(a, b);");
        System.out.println("   After:  int result = a + b;  (body substituted)");
        System.out.println("   -xx:MaxInlineSize=35 (bytecodes, before inlining)");
        System.out.println("   -xx:FreqInlineSize=325 (bytecodes, hot methods)");

        // Escape Analysis
        System.out.println("\n2. Escape Analysis:");
        System.out.println("   Determines if object escapes the method/thread");
        System.out.println("   Scalar replacement: object fields -> local variables");
        System.out.println("   Stack allocation: object allocated on stack, not heap");
        System.out.println("   Lock elision: remove unnecessary synchronization");
        System.out.println("   java -XX:+DoEscapeAnalysis -XX:+EliminateAllocations MyApp");

        // Loop Optimizations
        System.out.println("\n3. Loop Optimizations:");
        System.out.println("   Loop unrolling: reduce branch overhead");
        System.out.println("   Loop vectorization: SIMD instructions");
        System.out.println("   Loop peeling: handle first iteration separately");
        System.out.println("   Loop unswitching: move loop-invariant condition outside");

        // Intrinsics
        System.out.println("\n4. Method Intrinsics:");
        System.out.println("   Compiler replaces method call with optimized assembly");
        System.out.println("   Examples: System.arraycopy, Math.sin, Integer.bitCount");
        System.out.println("   Can use CPU-specific instructions (AVX, SSE)");
    }

    /**
     * DEMO 5: On-Stack Replacement (OSR)
     */
    public static void demonstrateOSR() {
        System.out.println("\n=== On-Stack Replacement (OSR) ===");
        System.out.println("Replaces interpreted code with compiled code mid-execution");
        System.out.println("Useful for: methods with long-running loops");
        System.out.println();
        System.out.println("How it works:");
        System.out.println("  1. Method enters at interpreter (tier 0)");
        System.out.println("  2. Hot loop detected during execution");
        System.out.println("  3. C2 compiles method while interpreter continues");
        System.out.println("  4. At next loop iteration, switch to compiled code");
        System.out.println("  5. Continue executing compiled code");

        System.out.println("\nOSR compile threshold:");
        System.out.println("  -XX:OnStackReplacePercentage=140");
        System.out.println("  -XX:InterpreterProfilePercentage=33");

        // Demo loop that would trigger OSR
        System.out.println("\nDemo: long loop that triggers OSR compilation:");
        long sum = 0;
        for (int i = 0; i < 10_000_000; i++) {
            sum += i;
        }
        System.out.println("Loop completed. Sum = " + sum);
    }

    /**
     * DEMO 6: JIT Compilation Logging
     */
    public static void demonstrateJitLogging() {
        System.out.println("\n=== JIT Compilation Logging ===");
        System.out.println("Print compilation events:");
        System.out.println("  -XX:+PrintCompilation");
        System.out.println("  Output: time flags pid compile_id attributes method size");
        System.out.println();
        System.out.println("Print inlining decisions:");
        System.out.println("  -XX:+UnlockDiagnosticVMOptions -XX:+PrintInlining");
        System.out.println();
        System.out.println("Print assembly (requires hsdis):");
        System.out.println("  -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly");
        System.out.println("  -XX:+UnlockDiagnosticVMOptions -XX:+PrintOptoAssembly");
        System.out.println();
        System.out.println("Print relocation information:");
        System.out.println("  -XX:+UnlockDiagnosticVMOptions -XX:+PrintRelocations");
    }

    /**
     * DEMO 7: Deoptimization
     * When compiled code assumptions are violated
     */
    public static void demonstrateDeoptimization() {
        System.out.println("\n=== Deoptimization ===");
        System.out.println("Triggers for deoptimization:");
        System.out.println("  1. Class loading invalidates compiled code");
        System.out.println("  2. Unloaded interface/abstract class");
        System.out.println("  3. Null check failure (speculative optimization)");
        System.out.println("  4. Array store check failure");
        System.out.println("  5. Unsafe access");
        System.out.println("  6. Monitor contention");
        System.out.println("  7. OSR compilation failure");
        System.out.println("  8. Call site change (monomorphic -> megamorphic)");
        System.out.println("\nDeoptimization is normal - don't panic!");
        System.out.println("Monitor with -XX:+PrintDeoptimizationEvents");
    }

    /**
     * DEMO 8: Performance Comparison
     */
    public static void demonstratePerformance() {
        System.out.println("\n=== Performance: Interpreted vs Compiled ===");

        // Warm up JIT
        long start = System.nanoTime();
        for (int iter = 0; iter < 100; iter++) {
            computeSum();
        }
        long jitTime = System.nanoTime() - start;
        System.out.println("After JIT warmup: " + (jitTime / 1000) + " μs per 100 iterations");

        // Measure after warmup
        start = System.nanoTime();
        for (int iter = 0; iter < 1000; iter++) {
            computeSum();
        }
        long hotTime = System.nanoTime() - start;
        System.out.println("After JIT compilation: " + (hotTime / 1000) + " μs per 1000 iterations");
        System.out.println("Speedup: ~" + (jitTime * 10.0 / hotTime) + "x");
    }

    private static long computeSum() {
        long sum = 0;
        for (int i = 0; i < 1000; i++) {
            sum += i;
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      JIT COMPILATION DEMO           ║");
        System.out.println("╚══════════════════════════════════════╝\n");

        demonstrateJitOverview();
        demonstrateTieredCompilation();
        demonstrateC1vsC2();
        demonstrateOptimizations();
        demonstrateOSR();
        demonstrateJitLogging();
        demonstrateDeoptimization();
        demonstratePerformance();

        System.out.println("\n=== Quick Reference ===");
        System.out.println("JIT Flags:");
        System.out.println("  -XX:+TieredCompilation (default)");
        System.out.println("  -XX:+PrintCompilation");
        System.out.println("  -XX:CompileThreshold=10000");
        System.out.println("  -XX:+DoEscapeAnalysis (default)");
        System.out.println("  -XX:+UnlockDiagnosticVMOptions -XX:+PrintInlining");
    }
}
