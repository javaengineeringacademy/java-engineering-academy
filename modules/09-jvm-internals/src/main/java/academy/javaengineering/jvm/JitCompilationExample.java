package academy.javaengineering.jvm;

/**
 * Demonstrates JIT compilation concepts and JVM optimizations.
 *
 * <p>This class explains how the JIT compiler improves performance through
 * hot spot detection, tiered compilation, and various optimizations.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Hot spot detection and compilation</li>
 *   <li>Tiered compilation (C1/C2 compilers)</li>
 *   <li>JVM optimizations (inlining, unrolling, dead code elimination)</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class JitCompilationExample {

    /**
     * Demonstrates hot method detection for JIT compilation.
     */
    public static class HotSpotExample {
        private int[] data = new int[1000];

        /**
         * Method that becomes "hot" after repeated execution.
         */
        public void hotMethod() {
            for (int i = 0; i < data.length; i++) {
                data[i] = i * 2;
            }
        }

        /**
         * Computes a sum for demonstration.
         *
         * @param n the upper bound
         * @return the sum of numbers from 0 to n-1
         */
        public int compute(int n) {
            int sum = 0;
            for (int i = 0; i < n; i++) { sum += i; }
            return sum;
        }
    }

    /**
     * Demonstrates JVM optimization techniques.
     */
    public static class OptimizationExample {
        /**
         * Simple method for inlining demonstration.
         *
         * @param a first operand
         * @param b second operand
         * @return the sum
         */
        public int add(int a, int b) { return a + b; }

        /**
         * Demonstrates loop unrolling optimization.
         */
        public void loopUnrolling() {
            int sum = 0;
            for (int i = 0; i < 100; i++) { sum += i; }
            System.out.println("Sum: " + sum);
        }
    }

    /**
     * Demonstrates JIT compilation concepts.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        System.out.println("=== JIT Compilation Demo ===");
        HotSpotExample hotSpot = new HotSpotExample();
        for (int i = 0; i < 10000; i++) { hotSpot.hotMethod(); }
        System.out.println("Hot method executed 10000 times");
        new OptimizationExample().loopUnrolling();
        System.out.println("JIT Optimizations: Method Inlining, Loop Unrolling, Dead Code Elimination, Constant Folding, Escape Analysis");
    }
}
