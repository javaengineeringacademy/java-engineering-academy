package academy.javaengineering.jvm.jit;

/**
 * Solution 1: JIT Compilation Observation
 */
public class Solution1 {

    public static void main(String[] args) {
        System.out.println("=== JIT Compilation Observation ===\n");

        // Task 1: Trigger compilation
        System.out.println("--- Task 1: Trigger Compilation ---");
        long start = System.nanoTime();
        for (int i = 0; i < 30000; i++) {
            hotMethod(i);
        }
        System.out.println("30000 calls completed");

        // Task 2: Performance measurement
        System.out.println("\n--- Task 2: Performance Measurement ---");

        // First batch (likely interpreted)
        start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            hotMethod(i);
        }
        long firstBatch = System.nanoTime() - start;

        // Second batch (likely compiled)
        start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            hotMethod(i);
        }
        long secondBatch = System.nanoTime() - start;

        System.out.printf("First 1000 calls: %d ns (%d ns/call)%n", firstBatch, firstBatch / 1000);
        System.out.printf("Next 1000 calls:  %d ns (%d ns/call)%n", secondBatch, secondBatch / 1000);
        System.out.printf("Speedup: %.1fx%n", (double) firstBatch / secondBatch);

        System.out.println("\nRun with: java -XX:+PrintCompilation Solution1");
    }

    static int hotMethod(int x) {
        return x * x + x;
    }
}
