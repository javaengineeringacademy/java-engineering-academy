package academy.javaengineering.concurrency.communication.volatile;

/**
 * Compares volatile vs synchronized for thread-safety.
 *
 * Same problem solved with two different approaches:
 * 1. volatile: Visibility only, no atomicity
 * 2. synchronized: Both visibility and atomicity
 */
public class VolatileVsSynchronized {

    // ============================================
    // Approach 1: Volatile (visibility only)
    // ============================================
    static class VolatileCounter {
        private volatile int count = 0;

        // NOT thread-safe! Race condition exists.
        public void increment() {
            count++;
        }

        public int getCount() {
            return count;
        }
    }

    // ============================================
    // Approach 2: Synchronized (visibility + atomicity)
    // ============================================
    static class SynchronizedCounter {
        private int count = 0;

        // Thread-safe! Both visibility and atomicity.
        public synchronized void increment() {
            count++;
        }

        public synchronized int getCount() {
            return count;
        }
    }

    // ============================================
    // Approach 3: Volatile for flag (correct usage)
    // ============================================
    static class VolatileFlag {
        private volatile boolean active = true;
        private volatile int processedCount = 0;

        public void stop() {
            active = false;
        }

        public boolean isActive() {
            return active;
        }

        public void incrementProcessed() {
            processedCount++;
        }

        public int getProcessedCount() {
            return processedCount;
        }
    }

    // ============================================
    // Approach 4: Synchronized for flag (also correct)
    // ============================================
    static class SynchronizedFlag {
        private boolean active = true;
        private int processedCount = 0;

        public synchronized void stop() {
            active = false;
        }

        public synchronized boolean isActive() {
            return active;
        }

        public synchronized void incrementProcessed() {
            processedCount++;
        }

        public synchronized int getProcessedCount() {
            return processedCount;
        }
    }

    // ============================================
    // Main method - demonstrates both approaches
    // ============================================
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Volatile vs Synchronized Comparison ===");
        System.out.println();

        // Test 1: Volatile counter (will lose increments)
        System.out.println("--- Test 1: Volatile Counter (Race Condition) ---");
        VolatileCounter volatileCounter = new VolatileCounter();
        int numThreads = 4;
        int incrementsPerThread = 1_000_000;

        Thread[] vThreads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            vThreads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    volatileCounter.increment();
                }
            });
        }

        long start1 = System.currentTimeMillis();
        for (Thread t : vThreads) t.start();
        for (Thread t : vThreads) t.join();
        long time1 = System.currentTimeMillis() - start1;

        int expected = numThreads * incrementsPerThread;
        System.out.println("Expected: " + expected);
        System.out.println("Actual:   " + volatileCounter.getCount());
        System.out.println("Lost:     " + (expected - volatileCounter.getCount()));
        System.out.println("Time:     " + time1 + "ms");
        System.out.println();

        // Test 2: Synchronized counter (correct)
        System.out.println("--- Test 2: Synchronized Counter (Thread-Safe) ---");
        SynchronizedCounter syncCounter = new SynchronizedCounter();

        Thread[] sThreads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            sThreads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    syncCounter.increment();
                }
            });
        }

        long start2 = System.currentTimeMillis();
        for (Thread t : sThreads) t.start();
        for (Thread t : sThreads) t.join();
        long time2 = System.currentTimeMillis() - start2;

        System.out.println("Expected: " + expected);
        System.out.println("Actual:   " + syncCounter.getCount());
        System.out.println("Lost:     " + (expected - syncCounter.getCount()));
        System.out.println("Time:     " + time2 + "ms");
        System.out.println();

        // Test 3: Volatile flag (correct usage)
        System.out.println("--- Test 3: Volatile Flag (Correct Usage) ---");
        VolatileFlag vFlag = new VolatileFlag();

        Thread worker1 = new Thread(() -> {
            while (vFlag.isActive()) {
                vFlag.incrementProcessed();
            }
            System.out.println("[Worker 1] Processed: " + vFlag.getProcessedCount());
        });

        worker1.start();
        Thread.sleep(50);
        vFlag.stop();
        worker1.join();
        System.out.println();

        // Test 4: Synchronized flag (correct usage)
        System.out.println("--- Test 4: Synchronized Flag (Correct Usage) ---");
        SynchronizedFlag sFlag = new SynchronizedFlag();

        Thread worker2 = new Thread(() -> {
            while (sFlag.isActive()) {
                sFlag.incrementProcessed();
            }
            System.out.println("[Worker 2] Processed: " + sFlag.getProcessedCount());
        });

        worker2.start();
        Thread.sleep(50);
        sFlag.stop();
        worker2.join();
        System.out.println();

        // Comparison
        System.out.println("--- Comparison Summary ---");
        System.out.println();
        System.out.println("Feature           | volatile              | synchronized");
        System.out.println("------------------|-----------------------|---------------------");
        System.out.println("Visibility        | Yes                   | Yes");
        System.out.println("Atomicity         | No                    | Yes");
        System.out.println("Mutual Exclusion  | No                    | Yes");
        System.out.println("Blocking          | No                    | Yes");
        System.out.println("Performance       | Fast                  | Slower");
        System.out.println("Use Case          | Simple flags          | Compound operations");
        System.out.println("Deadlock Risk     | None                  | Yes");
        System.out.println();
        System.out.println("Key Insight: For simple boolean flags, volatile is preferred");
        System.out.println("because it's faster and doesn't cause blocking.");
    }
}
