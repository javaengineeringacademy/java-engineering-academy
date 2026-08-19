package academy.javaengineering.jvm.jit;

/**
 * Solution 2: Benchmark Warm-up
 */
public class Solution2 {

    public static void main(String[] args) {
        System.out.println("=== JIT Warm-up Benchmark ===\n");

        int warmupIterations = 10000;
        int measureIterations = 10000;

        // Task 1: Without warm-up
        System.out.println("--- Task 1: Without Warm-up ---");
        long start = System.nanoTime();
        for (int i = 0; i < measureIterations; i++) {
            compute(i * 0.001);
        }
        long noWarmup = System.nanoTime() - start;
        System.out.printf("Without warm-up: %d ms%n", noWarmup / 1_000_000);

        // Task 2: With warm-up
        System.out.println("\n--- Task 2: With Warm-up ---");
        // Warm up
        for (int i = 0; i < warmupIterations; i++) {
            compute(i * 0.001);
        }
        // Measure
        start = System.nanoTime();
        for (int i = 0; i < measureIterations; i++) {
            compute(i * 0.001);
        }
        long withWarmup = System.nanoTime() - start;
        System.out.printf("With warm-up: %d ms%n", withWarmup / 1_000_000);
        System.out.printf("Speedup: %.1fx%n", (double) noWarmup / withWarmup);

        // Task 3: JIT persistence
        System.out.println("\n--- Task 3: JIT Persistence ---");
        start = System.nanoTime();
        for (int i = 0; i < measureIterations; i++) {
            compute(i * 0.001);
        }
        long afterWarmup = System.nanoTime() - start;
        System.out.printf("After warm-up: %d ms (same as warm-up batch)%n", afterWarmup / 1_000_000);
    }

    static double compute(double x) {
        double result = 0;
        for (int i = 0; i < 100; i++) {
            result += Math.sin(x + i) * Math.cos(x - i);
        }
        return result;
    }
}
