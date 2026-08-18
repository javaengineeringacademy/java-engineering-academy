package academy.javaengineering.concurrency.communication.waitnotify;

/**
 * Demonstrates the lost wakeup problem when using 'if' instead of 'while'
 * with wait().
 */
public class LostWakeupProblem {

    private static final Object lock = new Object();
    private static boolean dataReady = false;

    public static void main(String[] args) {
        System.out.println("=== Lost Wakeup Problem Demo ===");
        System.out.println("Using 'if' - notification can be lost\n");

        // BUG: Using if instead of while
        Thread buggyConsumer = new Thread(() -> {
            synchronized (lock) {
                System.out.println("[BuggyConsumer] Checking if dataReady...");
                // BUG: 'if' checks once, doesn't re-check after wakeup
                if (!dataReady) {
                    System.out.println("[BuggyConsumer] Not ready, waiting...");
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                    // If notify() was called BEFORE wait(), we skip it!
                    System.out.println("[BuggyConsumer] Woke up. dataReady=" + dataReady);
                }
            }
        }, "BuggyConsumer");

        // Simulates notification happening before wait() is called
        Thread buggyProducer = new Thread(() -> {
            synchronized (lock) {
                System.out.println("[BuggyProducer] Setting dataReady=true BEFORE notify");
                dataReady = true;
                lock.notify();
                System.out.println("[BuggyProducer] Notified (but consumer may not be waiting)");
            }
        }, "BuggyProducer");

        buggyConsumer.start();
        try {
            Thread.sleep(100); // Small delay to ensure consumer checks first
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        buggyProducer.start();

        try {
            buggyConsumer.join();
            buggyProducer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n=== Correct Version Using while ===");

        // Reset
        dataReady = false;

        Thread correctConsumer = new Thread(() -> {
            synchronized (lock) {
                System.out.println("[CorrectConsumer] Using while loop to re-check");
                while (!dataReady) {
                    System.out.println("[CorrectConsumer] Not ready, waiting...");
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                System.out.println("[CorrectConsumer] dataReady=true, proceeding");
            }
        }, "CorrectConsumer");

        Thread correctProducer = new Thread(() -> {
            synchronized (lock) {
                System.out.println("[CorrectProducer] Setting dataReady=true");
                dataReady = true;
                lock.notify();
            }
        }, "CorrectProducer");

        correctConsumer.start();
        correctProducer.start();

        try {
            correctConsumer.join();
            correctProducer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n=== Lost Wakeup Problem - Notification Lost ===");
        System.out.println("Scenario: notify() called BEFORE wait() is invoked");
        System.out.println("With 'if': consumer misses the notification and waits forever");
        System.out.println("With 'while': consumer re-checks condition after any wakeup");
        System.out.println("\nKey Takeaway: ALWAYS use 'while' (not 'if') with wait()");
    }
}
