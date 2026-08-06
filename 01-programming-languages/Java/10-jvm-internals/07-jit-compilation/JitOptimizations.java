package academy.javaengineering.jvm.jit;

import java.util.Random;

/**
 * JIT Optimization Techniques Deep Dive
 * Covers inline expansion, escape analysis, lock elimination,
 * loop optimizations, dead code elimination, and constant folding.
 */
public class JitOptimizations {

    private static final int WARMUP = 100_000;
    private static final int ITERATIONS = 10_000_000;
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        System.out.println("=== JIT Optimization Techniques Deep Dive ===\n");

        // 1. Inline Expansion
        demonstrateInlining();

        // 2. Escape Analysis
        demonstrateEscapeAnalysis();

        // 3. Lock Elimination
        demonstrateLockElimination();

        // 4. Loop Optimizations
        demonstrateLoopOptimizations();

        // 5. Dead Code Elimination
        demonstrateDeadCodeElimination();

        // 6. Constant Folding
        demonstrateConstantFolding();
    }

    // ============================================
    // 1. INLINE EXPANSION
    // ============================================

    /**
     * Method Inlining: Replaces method call with method body
     * - Eliminates method call overhead (stack frame, argument passing)
     * - Enables further optimizations on inlined code
     * - Small methods are inlined first (up to 35 bytes by default)
     */
    private static void demonstrateInlining() {
        System.out.println("--- 1. Inline Expansion (Method Inlining) ---");

        System.out.println("How inlining works:");
        System.out.println("  Before: int result = add(a, b);");
        System.out.println("  After:  int result = a + b;  (method body inlined)\n");

        System.out.println("Inlining thresholds (default):");
        System.out.println("  -XX:FreqInlineSize=325      (C1: max bytecode size)");
        System.out.println("  -XX:MaxInlineSize=35        (C2: max bytecode size)");
        System.out.println("  -XX:InlineSmallCode=2000    (C2: max native code size)\n");

        // Benchmark: inlined vs non-inlined
        warmUp();

        long startTime = System.nanoTime();
        long sum = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            sum += addInlined(i, i + 1); // Should be inlined
        }
        long inlinedTime = (System.nanoTime() - startTime) / 1_000_000;
        System.out.println("Inlined method (10M iterations): " + inlinedTime + " ms");

        startTime = System.nanoTime();
        sum = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            sum += addNonInlined(i, i + 1); // Less likely to be inlined
        }
        long nonInlinedTime = (System.nanoTime() - startTime) / 1_000_000;
        System.out.println("Non-inlined method (10M iterations): " + nonInlinedTime + " ms");
        System.out.println("Speedup: " + String.format("%.2fx", (double) nonInlinedTime / inlinedTime) + "\n");

        System.out.println("Virtual method inlining:");
        System.out.println("  - Monomorphic call site: 1 type → easy inline");
        System.out.println("  - Bimorphic call site: 2 types → inline both");
        System.out.println("  - Megamorphic call site: 3+ types → no inline\n");
    }

    private static int addInlined(int a, int b) {
        return a + b;
    }

    private static int addNonInlined(int a, int b) {
        return computeComplex(a, b);
    }

    private static int computeComplex(int a, int b) {
        int result = a + b;
        result = result * 2;
        result = result + a * b;
        result = result - b;
        result = result / 2;
        result = result + a;
        result = result - b;
        result = result * 2;
        result = result + 1;
        result = result - 1;
        return result;
    }

    // ============================================
    // 2. ESCAPE ANALYSIS
    // ============================================

    /**
     * Escape Analysis: Determines if objects escape the method
     * - Non-escaping objects can be stack-allocated
     * - Reduces GC pressure
     * - Enables further optimizations (lock elimination, scalar replacement)
     */
    private static void demonstrateEscapeAnalysis() {
        System.out.println("--- 2. Escape Analysis (Stack Allocation) ---");

        System.out.println("Escape analysis determines object lifetime:");
        System.out.println("  - NoEscape: Object stays within method → stack allocation");
        System.out.println("  - ArgEscape: Object passed as argument → heap allocation");
        System.out.println("  - GlobalEscape: Object escapes to heap → heap allocation\n");

        System.out.println("Benefits of stack allocation:");
        System.out.println("  - No GC overhead (automatic cleanup)");
        System.out.println("  - Better cache locality");
        System.out.println("  - No object header overhead\n");

        // Benchmark: stack vs heap allocation
        warmUp();

        long startTime = System.nanoTime();
        long sum = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            sum += stackAllocatedPoint(i, i + 1); // EA may stack-allocate
        }
        long stackTime = (System.nanoTime() - startTime) / 1_000_000;
        System.out.println("Stack-allocated Point (10M iterations): " + stackTime + " ms");

        startTime = System.nanoTime();
        sum = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            sum += heapAllocatedPoint(i, i + 1); // Forces heap allocation
        }
        long heapTime = (System.nanoTime() - startTime) / 1_000_000;
        System.out.println("Heap-allocated Point (10M iterations): " + heapTime + " ms");
        System.out.println("Speedup: " + String.format("%.2fx", (double) heapTime / stackTime) + "\n");

        System.out.println("Scalar replacement:");
        System.out.println("  Point p = new Point(1, 2);");
        System.out.println("  return p.x + p.y;");
        System.out.println("  Becomes: return 1 + 2;  (object eliminated)\n");
    }

    private static int stackAllocatedPoint(int x, int y) {
        // JIT may allocate Point on stack due to escape analysis
        Point p = new Point(x, y);
        return p.x + p.y;
    }

    private static int heapAllocatedPoint(int x, int y) {
        // Forces heap allocation by returning the object
        Point p = new Point(x, y);
        return p.x + p.y + getPointHash(p);
    }

    private static int getPointHash(Point p) {
        return p.x * 31 + p.y;
    }

    static class Point {
        final int x;
        final int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // ============================================
    // 3. LOCK ELIMINATION
    // ============================================

    /**
     * Lock Elimination: Removes unnecessary synchronization
     * - Biased locking for single-threaded access
     * - Lock coarsening for adjacent synchronized blocks
     * - Lock elision when lock doesn't escape
     */
    private static void demonstrateLockElimination() {
        System.out.println("--- 3. Lock Elimination (Biased Locking) ---");

        System.out.println("Lock elimination techniques:");
        System.out.println("  - Lock elision: Remove lock when object doesn't escape");
        System.out.println("  - Lock coarsening: Merge adjacent synchronized blocks");
        System.out.println("  - Biased locking: Optimize for single-threaded access\n");

        System.out.println("Lock coarsening example:");
        System.out.println("  Before:");
        System.out.println("    synchronized(lock) { a = 1; }");
        System.out.println("    synchronized(lock) { b = 2; }");
        System.out.println("    synchronized(lock) { c = 3; }");
        System.out.println("  After:");
        System.out.println("    synchronized(lock) { a = 1; b = 2; c = 3; }\n");

        // Benchmark synchronized vs unsynchronized
        warmUp();

        long startTime = System.nanoTime();
        long sum = 0;
        for (int i = 0; i < ITERATIONS / 100; i++) {
            sum += synchronizedMethod(i);
        }
        long syncTime = (System.nanoTime() - startTime) / 1_000_000;
        System.out.println("Synchronized method (100K iterations): " + syncTime + " ms");

        startTime = System.nanoTime();
        sum = 0;
        for (int i = 0; i < ITERATIONS / 100; i++) {
            sum += unsynchronizedMethod(i);
        }
        long unsyncTime = (System.nanoTime() - startTime) / 1_000_000;
        System.out.println("Unsynchronized method (100K iterations): " + unsyncTime + " ms");
        System.out.println("Speedup: " + String.format("%.2fx", (double) syncTime / unsyncTime) + "\n");

        System.out.println("Biased locking flags:");
        System.out.println("  -XX:+UseBiasedLocking            (enable, default=true)");
        System.out.println("  -XX:BiasedLockingStartupDelay=4000 (ms before biasing)");
        System.out.println("  -XX:+UseOptimizedBias             (use optimized bias)\n");
    }

    private static int synchronizedMethod(int n) {
        synchronized (JitOptimizations.class) {
            return n * n;
        }
    }

    private static int unsynchronizedMethod(int n) {
        return n * n;
    }

    // ============================================
    // 4. LOOP OPTIMIZATIONS
    // ============================================

    /**
     * Loop Optimizations: Improve loop performance
     * - Loop unrolling: Reduce branch overhead
     * - Loop inversion: Convert while to do-while
     * - Bounds check elimination: Remove redundant checks
     * - Loop vectorization: Use SIMD instructions
     */
    private static void demonstrateLoopOptimizations() {
        System.out.println("--- 4. Loop Optimizations ---");

        System.out.println("Loop optimization techniques:");
        System.out.println("  - Loop unrolling: Process multiple iterations per loop");
        System.out.println("  - Loop inversion: while → do-while (removes first branch)");
        System.out.println("  - Bounds check elimination: Remove array bounds checks");
        System.out.println("  - Loop vectorization: Use SIMD for parallel operations\n");

        // Benchmark loop unrolling
        warmUp();

        long startTime = System.nanoTime();
        long sum = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            sum += simpleLoop(i);
        }
        long simpleTime = (System.nanoTime() - startTime) / 1_000_000;
        System.out.println("Simple loop (10M iterations): " + simpleTime + " ms");

        startTime = System.nanoTime();
        sum = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            sum += unrolledLoop(i);
        }
        long unrolledTime = (System.nanoTime() - startTime) / 1_000_000;
        System.out.println("Unrolled loop (10M iterations): " + unrolledTime + " ms");
        System.out.println("Speedup: " + String.format("%.2fx", (double) simpleTime / unrolledTime) + "\n");

        System.out.println("Bounds check elimination:");
        System.out.println("  for (int i = 0; i < arr.length; i++) {");
        System.out.println("    sum += arr[i];  // Bounds check removed");
        System.out.println("  }");
        System.out.println("  JIT verifies i < arr.length, removes redundant check\n");

        System.out.println("Loop vectorization (SIMD):");
        System.out.println("  // Before: processes 1 element per iteration");
        System.out.println("  for (int i = 0; i < n; i++) sum += arr[i];");
        System.out.println("  // After: processes 4+ elements per iteration");
        System.out.println("  for (int i = 0; i < n; i += 4) sum += arr[i] + arr[i+1] + ...\n");
    }

    private static long simpleLoop(int n) {
        long sum = 0;
        for (int i = 0; i < 1000; i++) {
            sum += i;
        }
        return sum;
    }

    private static long unrolledLoop(int n) {
        long sum = 0;
        int i = 0;
        // Manually unrolled loop (JIT does this automatically)
        for (; i < 1000 - 3; i += 4) {
            sum += i;
            sum += i + 1;
            sum += i + 2;
            sum += i + 3;
        }
        for (; i < 1000; i++) {
            sum += i;
        }
        return sum;
    }

    // ============================================
    // 5. DEAD CODE ELIMINATION
    // ============================================

    /**
     * Dead Code Elimination: Remove unreachable code
     * - Constant propagation: Replace variables with constants
     * - Dead code removal: Remove unreachable statements
     * - Branch elimination: Remove always-false branches
     */
    private static void demonstrateDeadCodeElimination() {
        System.out.println("--- 5. Dead Code Elimination ---");

        System.out.println("Dead code elimination techniques:");
        System.out.println("  - Constant propagation: Replace x with 5");
        System.out.println("  - Dead code removal: Remove unreachable code");
        System.out.println("  - Branch elimination: Remove if(false) blocks\n");

        warmUp();

        long startTime = System.nanoTime();
        long sum = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            sum += deadCodeMethod(i);
        }
        long deadCodeTime = (System.nanoTime() - startTime) / 1_000_000;
        System.out.println("With dead code (10M iterations): " + deadCodeTime + " ms");

        startTime = System.nanoTime();
        sum = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            sum += optimizedMethod(i);
        }
        long optimizedTime = (System.nanoTime() - startTime) / 1_000_000;
        System.out.println("Without dead code (10M iterations): " + optimizedTime + " ms");
        System.out.println("Speedup: " + String.format("%.2fx", (double) deadCodeTime / optimizedTime) + "\n");

        System.out.println("What JIT eliminates:");
        System.out.println("  - if (false) { ... }  → removed");
        System.out.println("  - int x = compute();  (x never used) → removed");
        System.out.println("  - while (false) { ... } → removed\n");
    }

    private static int deadCodeMethod(int n) {
        int result = n * 2;
        // Dead code - never executed (JIT removes this)
        if (false) {
            result += computeExpensiveValue();
        }
        // Dead code - result of unused computation
        int unused = n * 3 + 5;
        return result;
    }

    private static int optimizedMethod(int n) {
        return n * 2;
    }

    private static int computeExpensiveValue() {
        int sum = 0;
        for (int i = 0; i < 1000; i++) {
            sum += i;
        }
        return sum;
    }

    // ============================================
    // 6. CONSTANT FOLDING
    // ============================================

    /**
     * Constant Folding: Evaluate constant expressions at compile time
     * - Replace expressions with pre-computed values
     * - Eliminates runtime computation
     * - Enables further optimizations
     */
    private static void demonstrateConstantFolding() {
        System.out.println("--- 6. Constant Folding ---");

        System.out.println("Constant folding examples:");
        System.out.println("  int x = 3 + 4;      → int x = 7;");
        System.out.println("  int y = 2 * 10;     → int y = 20;");
        System.out.println("  String s = \"a\" + \"b\"; → String s = \"ab\";\n");

        warmUp();

        long startTime = System.nanoTime();
        long sum = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            sum += constantComputation(i);
        }
        long constantTime = (System.nanoTime() - startTime) / 1_000_000;
        System.out.println("With constants (10M iterations): " + constantTime + " ms");

        startTime = System.nanoTime();
        sum = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            sum += dynamicComputation(i);
        }
        long dynamicTime = (System.nanoTime() - startTime) / 1_000_000;
        System.out.println("With variables (10M iterations): " + dynamicTime + " ms");
        System.out.println("Speedup: " + String.format("%.2fx", (double) dynamicTime / constantTime) + "\n");

        System.out.println("Constant folding benefits:");
        System.out.println("  - Eliminates runtime computation");
        System.out.println("  - Enables dead code elimination");
        System.out.println("  - Improves branch prediction");
        System.out.println("  - Reduces memory access\n");
    }

    private static int constantComputation(int n) {
        // Constants are folded at compile time
        return n + 7; // 3 + 4 = 7
    }

    private static int dynamicComputation(int n) {
        int a = 3;
        int b = 4;
        return n + a + b;
    }

    private static void warmUp() {
        for (int i = 0; i < WARMUP; i++) {
            simpleLoop(i);
            addInlined(i, i);
        }
    }
}
