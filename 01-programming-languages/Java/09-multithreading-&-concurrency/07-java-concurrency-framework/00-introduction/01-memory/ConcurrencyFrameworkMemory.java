import java.util.concurrent.*;

/**
 * Demonstrates memory layout and visibility in Java's Concurrency Framework.
 */
public class ConcurrencyFrameworkMemory {

    private static volatile boolean taskCompleted = false;
    private static int sharedValue = 0;

    public static void main(String[] args) throws Exception {
        demonstrateSubmitVisibility();
        demonstrateFutureResultVisibility();
    }

    static void demonstrateSubmitVisibility() throws InterruptedException {
        System.out.println("=== Submit Happens-Before Visibility ===");
        sharedValue = 0;
        taskCompleted = false;

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            // This read sees sharedValue = 0 because submit() establishes happens-before
            System.out.println("  Task sees sharedValue = " + sharedValue);
            sharedValue = 42;
            taskCompleted = true;
        });

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);

        // This read may or may not see 42 — no happens-before from task → main
        System.out.println("  Main sees sharedValue = " + sharedValue);
        System.out.println();
    }

    static void demonstrateFutureResultVisibility() throws Exception {
        System.out.println("=== Future.get() Visibility ===");
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<Integer> future = executor.submit(() -> {
            Thread.sleep(100);
            return 42;
        });

        // get() establishes happens-before: task's writes are visible after get()
        int result = future.get();
        System.out.println("  Future result: " + result);
        System.out.println("  (get() guarantees all task-side writes are visible)");

        executor.shutdown();
    }
}
