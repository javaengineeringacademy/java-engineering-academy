/**
 * Visibility Demo
 * Demonstrates thread visibility issues without synchronization
 */
public class VisibilityDemo {

    // Shared state without volatile - visibility issue
    private boolean running = true;
    private int counter = 0;

    public void startWorker() {
        Thread worker = new Thread(() -> {
            System.out.println("Worker started, waiting for signal...");
            int localCount = 0;
            while (running) {
                localCount++;
                if (localCount % 100000000 == 0) {
                    System.out.println("  Worker still running... (looped " + localCount + " times)");
                }
            }
            System.out.println("Worker stopped. Looped " + localCount + " times");
        });
        worker.start();
    }

    public void stopWorker() {
        try {
            Thread.sleep(100); // Let worker start
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Main thread: setting running = false");
        running = false; // May NOT be visible to worker thread!
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Visibility Demo ===\n");

        System.out.println("--- Demo 1: Visibility Issue (no volatile) ---");
        VisibilityDemo demo1 = new VisibilityDemo();
        demo1.startWorker();
        demo1.stopWorker();
        Thread.sleep(500); // Give time for output

        // Note: This might run indefinitely on some JVMs because
        // the worker thread may never see running = false
        System.out.println("(If worker didn't stop, that's the visibility issue)\n");

        System.out.println("--- Demo 2: Counter Visibility Issue ---");
        CounterWithoutSync counter = new CounterWithoutSync();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                counter.increment();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                counter.increment();
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Expected count: 200000");
        System.out.println("Actual count:   " + counter.getCount());
        System.out.println("Difference shows race condition (non-atomic increment)\n");

        System.out.println("--- Why Visibility Issues Happen ---");
        System.out.println("1. CPU caching: value cached in register, not re-read from main memory");
        System.out.println("2. Compiler optimization: loop hoisted, value loaded once");
        System.out.println("3. Store buffer: write pending in CPU store buffer");
        System.out.println("4. No memory barrier: JVM free to reorder operations");
        System.out.println();
        System.out.println("Fix: Use volatile, synchronized, or java.util.concurrent");

        System.out.println("\n=== End of Visibility Demo ===");
    }

    static class CounterWithoutSync {
        private int count = 0;

        public void increment() {
            count++; // NOT atomic: read, add, write
        }

        public int getCount() {
            return count;
        }
    }
}

/*
Expected Output (approximate):
=== Visibility Demo ===

--- Demo 1: Visibility Issue (no volatile) ---
Worker started, waiting for signal...
  Worker still running... (looped 100000000 times)
  Worker still running... (looped 200000000 times)
...
Main thread: setting running = false
(Worker may never see running = false - visibility issue)

--- Demo 2: Counter Visibility Issue ---
Expected count: 200000
Actual count:   187432 (varies, never 200000)
Difference shows race condition (non-atomic increment)

--- Why Visibility Issues Happen ---
1. CPU caching: value cached in register, not re-read from main memory
2. Compiler optimization: loop hoisted, value loaded once
3. Store buffer: write pending in CPU store buffer
4. No memory barrier: JVM free to reorder operations

Fix: Use volatile, synchronized, or java.util.concurrent

=== End of Visibility Demo ===
*/
