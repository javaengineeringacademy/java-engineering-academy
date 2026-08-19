package academy.javaengineering.jvm.memorymodel;

/**
 * Solution 1: Data Race Demonstration
 */
public class Solution1 {

    private static volatile boolean running = true;
    private static volatile int counter = 0;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Data Race Demonstration ===\n");

        // Task 1: Visibility problem
        System.out.println("--- Task 1: Visibility Problem ---");
        Thread worker = new Thread(() -> {
            int localCounter = 0;
            while (running) {
                localCounter++;
            }
            System.out.println("  Worker thread counted to: " + localCounter);
        });
        worker.start();
        Thread.sleep(100);
        running = false;
        worker.join();
        System.out.println("  Main thread set running=false (visible via volatile)\n");

        // Task 3: Compound operation race
        System.out.println("--- Task 3: Compound Operation Race ---");
        counter = 0;
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    counter++; // NOT atomic even with volatile!
                }
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();
        System.out.println("  Expected: 100000, Actual: " + counter);
        System.out.println("  (volatile does NOT make ++ atomic)\n");
    }
}
