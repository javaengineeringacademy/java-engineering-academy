package academy.javaengineering.jvm.tuning;

/**
 * Exercise 1: Baseline Performance Measurement
 *
 * Task: Measure application performance with different heap sizes
 * and GC algorithms to establish a baseline.
 */
public class Exercise1 {

    public static void main(String[] args) {
        System.out.println("=== Baseline Performance Measurement ===\n");
        System.out.println("Heap: " + Runtime.getRuntime().maxMemory() / (1024 * 1024) + " MB");
        System.out.println("GC: " + System.getProperty("java.vm.info", "unknown") + "\n");

        // TODO: Run workload and measure:
        // - Throughput (operations per second)
        // - Average latency
        // - Max latency
        // - GC pause times

        Runtime rt = Runtime.getRuntime();
        long start = System.currentTimeMillis();
        int ops = 0;

        while (System.currentTimeMillis() - start < 5000) { // Run for 5 seconds
            doWork();
            ops++;
        }

        long elapsed = System.currentTimeMillis() - start;
        System.out.printf("Operations: %d%n", ops);
        System.out.printf("Throughput: %.0f ops/s%n", (ops * 1000.0 / elapsed));
        System.out.printf("Memory used: %d MB%n", (rt.totalMemory() - rt.freeMemory()) / (1024 * 1024));
    }

    static void doWork() {
        byte[] data = new byte[4096];
        double result = Math.sin(System.nanoTime() % 1000);
    }
}
