package academy.javaengineering.jvm.tuning;

/**
 * Solution 1: Baseline Performance Measurement
 */
public class Solution1 {

    public static void main(String[] args) {
        System.out.println("=== Baseline Performance Measurement ===\n");
        System.out.println("Heap: " + Runtime.getRuntime().maxMemory() / (1024 * 1024) + " MB");
        System.out.println("GC: " + System.getProperty("java.vm.info", "unknown") + "\n");

        Runtime rt = Runtime.getRuntime();
        long start = System.currentTimeMillis();
        int ops = 0;

        while (System.currentTimeMillis() - start < 5000) {
            doWork();
            ops++;
        }

        long elapsed = System.currentTimeMillis() - start;
        System.out.printf("Results:%n");
        System.out.printf("  Operations: %d%n", ops);
        System.out.printf("  Throughput: %.0f ops/s%n", (ops * 1000.0 / elapsed));
        System.out.printf("  Memory used: %d MB%n", (rt.totalMemory() - rt.freeMemory()) / (1024 * 1024));
        System.out.printf("  Memory total: %d MB%n", rt.totalMemory() / (1024 * 1024));
    }

    static void doWork() {
        byte[] data = new byte[4096];
        double result = Math.sin(System.nanoTime() % 1000);
    }
}
