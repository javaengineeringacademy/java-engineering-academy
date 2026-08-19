package academy.javaengineering.jvm.gc_algorithms;

/**
 * Exercise 1: Compare GC Algorithm Performance
 *
 * Task: Run the same workload with different GC algorithms and compare
 * throughput, pause times, and memory usage.
 *
 * Run with:
 *   java -XX:+UseG1GC -Xlog:gc* Exercise1
 *   java -XX:+UseZGC -Xlog:gc* Exercise1
 *   java -XX:+UseShenandoahGC -Xlog:gc* Exercise1
 */
public class Exercise1 {

    public static void main(String[] args) {
        System.out.println("=== GC Algorithm Comparison ===\n");
        System.out.println("Current GC: " + getGCName());
        System.out.println("Heap: " + Runtime.getRuntime().maxMemory() / (1024 * 1024) + " MB\n");

        long startTime = System.currentTimeMillis();
        int iterations = 500_000;

        for (int i = 0; i < iterations; i++) {
            allocateAndProcess(i);
        }

        long elapsed = System.currentTimeMillis() - startTime;
        System.out.printf("Completed %d iterations in %d ms%n", iterations, elapsed);
        System.out.printf("Throughput: %.1f ops/s%n", (iterations * 1000.0 / elapsed));

        // TODO: Run with different GC algorithms and compare results
        // TODO: Record pause times from GC logs
        // TODO: Compare memory usage
    }

    static void allocateAndProcess(int id) {
        // Allocate short-lived objects
        byte[] data = new byte[1024];
        // Some processing
        double result = Math.sin(id) * Math.cos(id);
    }

    static String getGCName() {
        String gc = System.getProperty("java.vm.info", "unknown");
        return gc;
    }
}
