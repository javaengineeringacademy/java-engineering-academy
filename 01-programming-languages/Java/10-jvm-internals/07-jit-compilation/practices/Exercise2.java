package academy.javaengineering.jvm.jit;

/**
 * Exercise 2: Benchmark Warm-up Effects
 *
 * Task: Demonstrate how JIT warm-up affects benchmark results
 * and implement proper benchmarking methodology.
 */
public class Exercise2 {

    public static void main(String[] args) {
        System.out.println("=== JIT Warm-up Benchmark ===\n");

        int warmupIterations = 10000;
        int measureIterations = 10000;

        // Task 1: Measure without warm-up
        System.out.println("--- Task 1: Without Warm-up ---");
        // TODO: Start timing immediately, run measureIterations
        // TODO: Record time (will be slow due to interpretation)

        // Task 2: Measure with warm-up
        System.out.println("\n--- Task 2: With Warm-up ---");
        // TODO: Run warmupIterations first (don't time)
        // TODO: Then run measureIterations and time
        // TODO: Record time (should be much faster)

        // Task 3: Show that JIT affects all subsequent calls
        System.out.println("\n--- Task 3: JIT Effect Persistence ---");
        // TODO: Show that after warm-up, ALL calls are fast

        System.out.println("\nRun with: java -XX:+PrintCompilation Exercise2");
    }

    static double compute(double x) {
        double result = 0;
        for (int i = 0; i < 100; i++) {
            result += Math.sin(x + i) * Math.cos(x - i);
        }
        return result;
    }
}
