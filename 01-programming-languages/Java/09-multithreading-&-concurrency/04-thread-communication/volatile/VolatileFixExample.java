package academy.javaengineering.concurrency.communication.volatile;

/**
 * Demonstrates how volatile FIXES the visibility problem.
 *
 * With volatile, Thread A will always read the latest value from main memory,
 * ensuring it sees the change made by Thread B.
 */
public class VolatileFixExample {

    // WITH volatile - Thread A will always see the latest value
    private volatile boolean running = true;

    public void startThreadA() {
        Thread a = new Thread(() -> {
            System.out.println("[Thread A] Starting work...");
            int counter = 0;
            while (running) {
                counter++;
                if (counter % 500_000_000 == 0) {
                    System.out.println("[Thread A] Still working... (iteration: " + counter + ")");
                }
            }
            System.out.println("[Thread A] Stopped after " + counter + " iterations.");
        }, "Thread-A");

        a.start();

        // Give Thread A time to start
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("[Main] Setting running = false...");
        running = false;  // This change WILL be visible to Thread A!

        try {
            a.join(3000); // Wait up to 3 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (a.isAlive()) {
            System.out.println("[Main] WARNING: Thread A is STILL RUNNING (unexpected with volatile)");
            a.interrupt();
        } else {
            System.out.println("[Main] Thread A stopped - volatile ensured visibility!");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Volatile Fix for Visibility Problem ===");
        System.out.println("This demonstrates how volatile ensures visibility.");
        System.out.println();

        VolatileFixExample example = new VolatileFixExample();
        example.startThreadA();

        System.out.println();
        System.out.println("=== Key Takeaway ===");
        System.out.println("With volatile, the JVM inserts memory barriers that force:");
        System.out.println("1. Thread A to read from main memory, not cache");
        System.out.println("2. Thread B's write to be flushed to main memory");
        System.out.println("This guarantees Thread A sees the updated value promptly.");
    }
}
