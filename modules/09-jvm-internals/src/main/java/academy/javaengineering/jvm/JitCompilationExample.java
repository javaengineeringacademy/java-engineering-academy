package academy.javaengineering.jvm;

/**
 * JIT Compilation - C1/C2 Compilers, Tiered Compilation, Optimizations.
 */
public class JitCompilationExample {

    public static class HotSpotExample {
        private int[] data = new int[1000];

        public void hotMethod() {
            for (int i = 0; i < data.length; i++) {
                data[i] = i * 2;
            }
        }

        public int compute(int n) {
            int sum = 0;
            for (int i = 0; i < n; i++) {
                sum += i;
            }
            return sum;
        }
    }

    public static class OptimizationExample {
        public int add(int a, int b) {
            return a + b;
        }

        public void loopUnrolling() {
            int sum = 0;
            for (int i = 0; i < 100; i++) {
                sum += i;
            }
            System.out.println("Sum: " + sum);
        }

        public void methodInlining(String a, String b) {
            String result = concatenate(a, b);
            System.out.println("Result: " + result);
        }

        private String concatenate(String a, String b) {
            return a + b;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== JIT Compilation Demo ===");

        HotSpotExample hotSpot = new HotSpotExample();
        for (int i = 0; i < 10000; i++) {
            hotSpot.hotMethod();
        }
        System.out.println("Hot method executed 10000 times");

        OptimizationExample optimization = new OptimizationExample();
        optimization.loopUnrolling();
        optimization.methodInlining("Hello", " World");

        System.out.println("\n=== JIT Optimization Types ===");
        System.out.println("1. Method Inlining");
        System.out.println("2. Loop Unrolling");
        System.out.println("3. Dead Code Elimination");
        System.out.println("4. Constant Folding");
        System.out.println("5. Escape Analysis");
    }
}
