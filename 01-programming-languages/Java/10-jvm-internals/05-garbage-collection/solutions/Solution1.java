package academy.javaengineering.jvm.gc;

/**
 * Solution 1: GC Observation
 */
public class Solution1 {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== GC Observation ===\n");

        // Task 1: Trigger Minor GC
        System.out.println("--- Task 1: Trigger Minor GC ---");
        System.out.println("Allocating 1M short-lived objects...");
        for (int i = 0; i < 1_000_000; i++) {
            new Object();
        }
        System.out.println("Done. Check GC log for Minor GC events.\n");

        // Task 2: Fill Old Generation
        System.out.println("--- Task 2: Fill Old Generation ---");
        Object[] longLived = new Object[1000];
        for (int i = 0; i < 1000; i++) {
            longLived[i] = new byte[1024 * 100]; // 100KB each
            System.out.printf("  Allocated %d MB%n", (i + 1) * 100 / 1024);
        }
        System.out.println("Long-lived objects created.\n");

        // Task 3: Compare collectors
        System.out.println("--- Task 3: Compare Collectors ---");
        System.out.println("Run with different collectors and compare GC logs:");
        System.out.println("  java -XX:+UseSerialGC -Xlog:gc* Solution1");
        System.out.println("  java -XX:+UseParallelGC -Xlog:gc* Solution1");
        System.out.println("  java -XX:+UseG1GC -Xlog:gc* Solution1");

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100_000; i++) {
            byte[] temp = new byte[1024];
        }
        long elapsed = System.currentTimeMillis() - start;
        System.out.printf("  Allocation time: %d ms%n", elapsed);
    }
}
