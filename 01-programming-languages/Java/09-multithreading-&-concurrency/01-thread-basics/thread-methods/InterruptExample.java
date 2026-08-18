package academy.javaengineering.concurrency.basics.methods;

/**
 * interrupt() in detail
 * - interrupt() sets interrupt flag
 * - InterruptedException when blocked
 * - interrupted() vs isInterrupted()
 * - Proper interrupt handling pattern
 */
public class InterruptExample {

    public static void main(String[] args) {
        System.out.println("=== Interrupt Examples ===\n");

        interruptSetsFlag();
        interruptedVsIsInterrupted();
        properInterruptHandling();
    }

    static void interruptSetsFlag() {
        System.out.println("--- interrupt() sets interrupt flag ---");

        Thread t = new Thread(() -> {
            System.out.println("Before interrupt: " + Thread.currentThread().isInterrupted());
            // Do nothing, just wait to be interrupted
        });

        t.start();

        try {
            Thread.sleep(100); // Let thread start
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t.interrupt(); // Sets interrupt flag

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Thread isAlive: " + t.isAlive());
        System.out.println();
    }

    static void interruptedVsIsInterrupted() {
        System.out.println("--- interrupted() vs isInterrupted() ---");

        Thread t = new Thread(() -> {
            System.out.println("Thread started");
            // Wait to be interrupted
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("InterruptedException caught");
            }
        });

        t.start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Interrupt the thread
        t.interrupt();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Demonstrate difference
        System.out.println("After thread ended:");
        System.out.println("  t.isInterrupted(): " + t.isInterrupted());
        System.out.println("  Thread.interrupted(): " + Thread.interrupted());
        System.out.println("  Thread.interrupted(): " + Thread.interrupted());
        System.out.println();
    }

    static void properInterruptHandling() {
        System.out.println("--- Proper Interrupt Handling Pattern ---");

        Thread worker = new Thread(() -> {
            int work = 0;

            while (!Thread.currentThread().isInterrupted()) {
                // Do work
                work++;

                // Simulate some work
                if (work % 1000000 == 0) {
                    System.out.println("Work done: " + work);
                }

                // Check for interrupt
                if (work > 5000000) {
                    System.out.println("Work limit reached, stopping");
                    break;
                }
            }

            // Cleanup resources
            System.out.println("Final work count: " + work);
            System.out.println("Thread cleaned up and exiting");
        }, "Worker");

        worker.start();

        try {
            Thread.sleep(100); // Let it work a bit
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Requesting worker to stop...");
        worker.interrupt();

        try {
            worker.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Worker finished");
        System.out.println();
    }
}
