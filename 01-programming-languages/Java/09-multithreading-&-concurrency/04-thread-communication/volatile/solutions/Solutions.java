package academy.javaengineering.concurrency.communication.volatile.solutions;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Complete solutions to the volatile practice exercises.
 */
public class Solutions {

    // ============================================
    // Solution 1: Graceful Shutdown with volatile
    // ============================================
    static class GracefulShutdown {
        private volatile boolean running = true; // volatile for visibility

        public void shutdown() {
            running = false;
        }

        public void startWorker() {
            Thread worker = new Thread(() -> {
                int count = 0;
                while (running) { // volatile read ensures visibility
                    count++;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
                System.out.println("[Solution 1] Worker completed " + count + " iterations");
            });
            worker.start();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            shutdown();
            try {
                worker.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // ============================================
    // Solution 2: AtomicInteger for thread-safe counting
    // ============================================
    static class SafeCounter {
        private final AtomicInteger count = new AtomicInteger(0); // Atomic!

        public void increment() {
            count.incrementAndGet(); // Atomic operation - thread-safe!
        }

        public int getCount() {
            return count.get(); // Atomic read
        }
    }

    // ============================================
    // Solution 3: Thread-safe status checker
    // ============================================
    static class StatusChecker {
        private volatile int status = 0; // volatile for visibility
        // 0=IDLE, 1=RUNNING, 2=COMPLETE, 3=ERROR

        public void setStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }

        public boolean isRunning() {
            return status == 1;
        }
    }

    // ============================================
    // Solution 4: Safe lazy initialization with DCL
    // ============================================
    static class LazyInit {
        private static volatile LazyInit instance; // volatile for DCL

        private LazyInit() {
            System.out.println("[Solution 4] LazyInit constructor called");
        }

        public static LazyInit getInstance() {
            if (instance == null) {                    // First check
                synchronized (LazyInit.class) {
                    if (instance == null) {            // Second check
                        instance = new LazyInit();     // volatile prevents reordering
                    }
                }
            }
            return instance;
        }
    }

    // ============================================
    // Solution 5: Fixed visibility problem
    // ============================================
    static class VisibilityTest {
        private volatile int flag = 0; // volatile for visibility
        private volatile String message = ""; // volatile for visibility

        public void setFlag(String msg) {
            message = msg;
            flag = 1; // volatile write ensures visibility
        }

        public String waitForFlag() {
            while (flag == 0) { // volatile read ensures visibility
                // Busy wait - volatile ensures we see the flag
            }
            return message;
        }
    }

    // ============================================
    // Main method - demonstrates all solutions
    // ============================================
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Volatile Solutions ===");
        System.out.println();

        // Solution 1: Graceful Shutdown
        System.out.println("--- Solution 1: Graceful Shutdown ---");
        GracefulShutdown shutdown = new GracefulShutdown();
        shutdown.startWorker();
        Thread.sleep(50);
        System.out.println("[Solution 1] Shutdown signal sent");
        System.out.println();

        // Solution 2: AtomicInteger
        System.out.println("--- Solution 2: AtomicInteger Counter ---");
        SafeCounter counter = new SafeCounter();
        int numThreads = 4;
        int incrementsPerThread = 1_000_000;

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    counter.increment();
                }
            });
        }

        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();

        int expected = numThreads * incrementsPerThread;
        System.out.println("[Solution 2] Expected: " + expected);
        System.out.println("[Solution 2] Actual:   " + counter.getCount());
        System.out.println("[Solution 2] Lost:     " + (expected - counter.getCount()));
        System.out.println();

        // Solution 3: Status Checker
        System.out.println("--- Solution 3: Status Checker ---");
        StatusChecker statusChecker = new StatusChecker();

        Thread statusThread = new Thread(() -> {
            System.out.println("[Solution 3] Status: " + statusChecker.getStatus());
            statusChecker.setStatus(1); // RUNNING
            System.out.println("[Solution 3] Status after set: " + statusChecker.getStatus());
            System.out.println("[Solution 3] Is running: " + statusChecker.isRunning());
            statusChecker.setStatus(2); // COMPLETE
            System.out.println("[Solution 3] Final status: " + statusChecker.getStatus());
        });
        statusThread.start();
        statusThread.join();
        System.out.println();

        // Solution 4: Lazy Init
        System.out.println("--- Solution 4: Lazy Initialization ---");
        Thread t1 = new Thread(() -> {
            LazyInit l1 = LazyInit.getInstance();
            System.out.println("[Solution 4] Thread 1: " + l1.hashCode());
        });
        Thread t2 = new Thread(() -> {
            LazyInit l2 = LazyInit.getInstance();
            System.out.println("[Solution 4] Thread 2: " + l2.hashCode());
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println();

        // Solution 5: Visibility Fix
        System.out.println("--- Solution 5: Visibility Fix ---");
        VisibilityTest visTest = new VisibilityTest();

        Thread writer = new Thread(() -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            visTest.setFlag("Hello from writer!");
            System.out.println("[Solution 5] Writer set flag");
        });

        Thread reader = new Thread(() -> {
            String msg = visTest.waitForFlag();
            System.out.println("[Solution 5] Reader got: " + msg);
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();
        System.out.println();

        // Summary
        System.out.println("=== Summary ===");
        System.out.println();
        System.out.println("1. volatile: Use for simple flags (boolean, status)");
        System.out.println("2. AtomicInteger: Use for counters (i++)");
        System.out.println("3. DCL: Use volatile to prevent reference publication");
        System.out.println("4. Immutable objects: Best for shared state");
        System.out.println("5. synchronized: For complex compound operations");
    }
}
