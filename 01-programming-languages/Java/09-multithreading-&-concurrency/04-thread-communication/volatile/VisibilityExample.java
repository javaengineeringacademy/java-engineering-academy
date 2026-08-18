package academy.javaengineering.concurrency.communication.volatile;

/**
 * Demonstrates the VISIBILITY PROBLEM without volatile.
 *
 * Without volatile, Thread B may never see the change made by Thread A
 * because the JVM may cache the value in Thread B's CPU register/cache.
 */
public class VisibilityExample {

    // WITHOUT volatile - Thread B may never see changes!
    private boolean running = true;

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

        // Give Thread A time to start and potentially cache the value
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("[Main] Setting running = false...");
        running = false;  // This change may NOT be visible to Thread A!

        try {
            a.join(3000); // Wait up to 3 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (a.isAlive()) {
            System.out.println("[Main] WARNING: Thread A is STILL RUNNING after 3 seconds!");
            System.out.println("[Main] This proves the VISIBILITY PROBLEM - Thread A cannot see our change.");
            System.out.println("[Main] Thread A is likely stuck in its loop reading a cached value.");
            a.interrupt();
        } else {
            System.out.println("[Main] Thread A stopped (might happen sometimes due to JVM behavior)");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Visibility Problem WITHOUT Volatile ===");
        System.out.println("This demonstrates why volatile is needed for visibility.");
        System.out.println();

        VisibilityExample example = new VisibilityExample();
        example.startThreadA();

        System.out.println();
        System.out.println("=== Key Takeaway ===");
        System.out.println("Without volatile, the JVM/CPU may cache the 'running' field");
        System.out.println("in Thread A's CPU register, so it never sees the updated value.");
        System.out.println("The fix is: private volatile boolean running = true;");
    }
}
