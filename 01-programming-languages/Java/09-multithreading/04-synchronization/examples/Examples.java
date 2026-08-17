package academy.javaengineering.concurrency.synchronization;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Examples - Runnable examples demonstrating synchronization concepts.
 */
public class Examples {

    /**
     * Example 1: Race Condition vs Synchronized
     * Shows the difference between unsynchronized and synchronized counter.
     */
    static class Example1_RaceCondition {
        private static int unsafeCounter = 0;

        public static void main(String[] args) throws InterruptedException {
            System.out.println("Example 1: Race Condition vs Synchronized");
            System.out.println("==========================================");

            // Unsafe counter
            Thread[] unsafe = new Thread[10];
            for (int i = 0; i < 10; i++) {
                unsafe[i] = new Thread(() -> {
                    for (int j = 0; j < 100000; j++) unsafeCounter++;
                });
                unsafe[i].start();
            }
            for (Thread t : unsafe) t.join();
            System.out.println("  Unsafe counter: " + unsafeCounter + " (expected 1000000)");

            // Safe counter
            SafeCounter safeCounter = new SafeCounter();
            Thread[] safe = new Thread[10];
            for (int i = 0; i < 10; i++) {
                safe[i] = new Thread(() -> {
                    for (int j = 0; j < 100000; j++) safeCounter.increment();
                });
                safe[i].start();
            }
            for (Thread t : safe) t.join();
            System.out.println("  Safe counter: " + safeCounter.getCount() + " (expected 1000000)");
            System.out.println();
        }

        static class SafeCounter {
            private int count = 0;
            public synchronized void increment() { count++; }
            public synchronized int getCount() { return count; }
        }
    }

    /**
     * Example 2: volatile for Flags
     * Demonstrates using volatile for cross-thread visibility.
     */
    static class Example2_VolatileFlag {
        private static volatile boolean running = true;

        public static void main(String[] args) throws InterruptedException {
            System.out.println("Example 2: volatile Flag");
            System.out.println("========================");

            Thread worker = new Thread(() -> {
                int count = 0;
                while (running) {
                    count++;
                }
                System.out.println("  Worker stopped after " + count + " iterations");
            });

            worker.start();
            Thread.sleep(100);
            running = false;
            worker.join();

            System.out.println("  Main set running=false, worker observed it");
            System.out.println();
        }
    }

    /**
     * Example 3: AtomicInteger Operations
     * Shows various atomic operations for thread-safe counters.
     */
    static class Example3_AtomicOperations {
        private static AtomicInteger counter = new AtomicInteger(0);

        public static void main(String[] args) throws InterruptedException {
            System.out.println("Example 3: AtomicInteger Operations");
            System.out.println("====================================");

            Thread[] threads = new Thread[10];
            for (int i = 0; i < 10; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < 100000; j++) {
                        counter.incrementAndGet();
                    }
                });
                threads[i].start();
            }

            for (Thread t : threads) t.join();

            System.out.println("  Final count: " + counter.get());
            System.out.println("  Expected: 1000000");

            // Other atomic operations
            AtomicInteger value = new AtomicInteger(10);
            System.out.println("  getAndAdd(5): " + value.getAndAdd(5) + " -> " + value.get());
            System.out.println("  addAndGet(3): " + value.addAndGet(3) + " -> " + value.get());
            System.out.println("  compareAndSet(18, 100): " +
                value.compareAndSet(18, 100) + " -> " + value.get());
            System.out.println();
        }
    }

    /**
     * Example 4: synchronized Block vs Method
     * Compares synchronized blocks with synchronized methods.
     */
    static class Example4_SyncBlockVsMethod {
        private int count = 0;
        private final Object lock = new Object();

        public synchronized void syncMethod() {
            count++;
        }

        public void syncBlock() {
            synchronized (lock) {
                count++;
            }
        }

        public int getCount() { return count; }

        public static void main(String[] args) throws InterruptedException {
            System.out.println("Example 4: synchronized Block vs Method");
            System.out.println("========================================");

            Example4_SyncBlockVsMethod instance = new Example4_SyncBlockVsMethod();

            // Sync method
            Thread[] methodThreads = new Thread[5];
            for (int i = 0; i < 5; i++) {
                methodThreads[i] = new Thread(() -> {
                    for (int j = 0; j < 100000; j++) instance.syncMethod();
                });
                methodThreads[i].start();
            }
            for (Thread t : methodThreads) t.join();
            System.out.println("  syncMethod count: " + instance.getCount());

            // Reset
            instance.count = 0;

            // Sync block
            Thread[] blockThreads = new Thread[5];
            for (int i = 0; i < 5; i++) {
                blockThreads[i] = new Thread(() -> {
                    for (int j = 0; j < 100000; j++) instance.syncBlock();
                });
                blockThreads[i].start();
            }
            for (Thread t : blockThreads) t.join();
            System.out.println("  syncBlock count: " + instance.getCount());
            System.out.println();
        }
    }

    /**
     * Example 5: Thread-Safe Lazy Initialization
     * Shows double-checked locking pattern.
     */
    static class Example5_DoubleCheckedLocking {
        private static volatile Instance instance;

        static class Instance {
            private final String value;
            Instance(String value) {
                this.value = value;
                System.out.println("  Instance created: " + value);
            }
            String getValue() { return value; }
        }

        static Instance getInstance() {
            if (instance == null) { // First check (no lock)
                synchronized (Example5_DoubleCheckedLocking.class) {
                    if (instance == null) { // Second check (with lock)
                        instance = new Instance("singleton-" +
                            Thread.currentThread().getName());
                    }
                }
            }
            return instance;
        }

        public static void main(String[] args) throws InterruptedException {
            System.out.println("Example 5: Double-Checked Locking");
            System.out.println("==================================");

            Thread[] threads = new Thread[10];
            for (int i = 0; i < 10; i++) {
                threads[i] = new Thread(() -> {
                    Instance inst = getInstance();
                    System.out.println("  " + Thread.currentThread().getName() +
                        " got: " + inst.getValue());
                });
                threads[i].start();
            }

            for (Thread t : threads) t.join();
            System.out.println();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Example1_RaceCondition.main(args);
        Example2_VolatileFlag.main(args);
        Example3_AtomicOperations.main(args);
        Example4_SyncBlockVsMethod.main(args);
        Example5_DoubleCheckedLocking.main(args);
    }
}
