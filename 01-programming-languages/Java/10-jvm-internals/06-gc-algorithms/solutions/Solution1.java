package academy.javaengineering.jvm.gc_algorithms;

/**
 * Solution 1: GC Algorithm Comparison
 */
public class Solution1 {

    public static void main(String[] args) {
        System.out.println("=== GC Algorithm Comparison ===\n");
        System.out.println("GC: " + System.getProperty("java.vm.info", "unknown"));
        System.out.println("Heap: " + Runtime.getRuntime().maxMemory() / (1024 * 1024) + " MB");
        System.out.println("Processors: " + Runtime.getRuntime().availableProcessors() + "\n");

        int iterations = 500_000;
        Runtime rt = Runtime.getRuntime();
        long startMem = rt.totalMemory() - rt.freeMemory();

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            byte[] data = new byte[1024];
            double result = Math.sin(i) * Math.cos(i);
        }
        long elapsed = System.currentTimeMillis() - startTime;
        long endMem = rt.totalMemory() - rt.freeMemory();

        System.out.printf("Results:%n");
        System.out.printf("  Time: %d ms%n", elapsed);
        System.out.printf("  Throughput: %.0f ops/s%n", (iterations * 1000.0 / elapsed));
        System.out.printf("  Memory delta: %d KB%n", (endMem - startMem) / 1024);
        System.out.println("\nCompare with different collectors:");
        System.out.println("  java -XX:+UseG1GC -Xlog:gc* Solution1");
        System.out.println("  java -XX:+UseZGC -Xlog:gc* Solution1");
        System.out.println("  java -XX:+UseParallelGC -Xlog:gc* Solution1");
    }
}
