package academy.javaengineering.concurrency.examples;

import java.util.concurrent.TimeUnit;

public class SynchronizationExamples {

    private static int sharedCounter = 0;
    private static volatile boolean running = true;
    private static final Object lockObject = new Object();

    public static void main(String[] args) throws InterruptedException {
        example1_SynchronizedMethod();
        example2_SynchronizedBlock();
        example3_StaticSynchronization();
        example4_VolatileKeyword();
        example5_WaitNotifyBasic();
    }

    // Example 1: synchronized method
    static void example1_SynchronizedMethod() throws InterruptedException {
        System.out.println("=== Example 1: Synchronized Method ===");

        Counter counter = new Counter();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        }, "Thread-1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        }, "Thread-2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Final count (synchronized): " + counter.getCount());
        System.out.println("Expected: 2000");

        System.out.println();
    }

    // Example 2: synchronized block
    static void example2_SynchronizedBlock() throws InterruptedException {
        System.out.println("=== Example 2: Synchronized Block ===");

        int[] sharedArray = {0};

        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                synchronized (sharedArray) {
                    sharedArray[0]++;
                }
            }
        };

        Thread t1 = new Thread(task, "Array-1");
        Thread t2 = new Thread(task, "Array-2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Array value (synchronized block): " + sharedArray[0]);

        // Fine-grained locking
        System.out.println("Fine-grained synchronized block example:");
        String separateLock1 = "lock1";
        String separateLock2 = "lock2";

        Thread t3 = new Thread(() -> {
            synchronized (separateLock1) {
                System.out.println("Thread-3 holds lock1");
                try { TimeUnit.MILLISECONDS.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        });

        Thread t4 = new Thread(() -> {
            synchronized (separateLock2) {
                System.out.println("Thread-4 holds lock2 (different lock, no contention)");
            }
        });

        t3.start();
        t4.start();
        t3.join();
        t4.join();

        System.out.println();
    }

    // Example 3: static synchronization
    static void example3_StaticSynchronization() throws InterruptedException {
        System.out.println("=== Example 3: Static Synchronization ===");

        // Static synchronized uses the CLASS monitor, not instance
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                StaticCounter.increment();
                try { TimeUnit.MILLISECONDS.sleep(10); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        }, "Static-1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                StaticCounter.increment();
                try { TimeUnit.MILLISECONDS.sleep(10); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        }, "Static-2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Static counter value: " + StaticCounter.getCount());

        System.out.println();
    }

    // Example 4: volatile keyword
    static void example4_VolatileKeyword() throws InterruptedException {
        System.out.println("=== Example 4: Volatile Keyword ===");

        // Without volatile, the loop might never see the update
        // because each thread may cache the value in its CPU cache
        Thread worker = new Thread(() -> {
            int count = 0;
            while (running) { // Without volatile, this could loop forever
                count++;
                if (count % 1000000 == 0) {
                    System.out.println("Worker iterations: " + count);
                }
            }
            System.out.println("Worker stopped after " + count + " iterations");
        }, "VolatileWorker");

        worker.start();
        TimeUnit.MILLISECONDS.sleep(100);

        System.out.println("Setting running = false");
        running = false; // volatile ensures visibility across threads

        worker.join();
        System.out.println();

        // Volatile vs synchronized
        System.out.println("Volatile guarantees:");
        System.out.println("- Visibility: changes are immediately visible to other threads");
        System.out.println("- Ordering: prevents reordering of reads/writes");
        System.out.println("- Does NOT provide atomicity for compound operations (i++)");
        System.out.println();

        System.out.println();
    }

    // Example 5: wait/notify basics
    static void example5_WaitNotifyBasic() throws InterruptedException {
        System.out.println("=== Example 5: wait() / notify() Basics ===");

        Object monitor = new Object();

        Thread waiter = new Thread(() -> {
            synchronized (monitor) {
                System.out.println("Waiter: acquired lock, going to wait...");
                try {
                    monitor.wait(); // Releases lock and waits
                    System.out.println("Waiter: notified! Resuming...");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "Waiter");

        Thread notifier = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            synchronized (monitor) {
                System.out.println("Notifier: acquired lock, sending notification...");
                monitor.notify(); // Wakes up one waiting thread
                System.out.println("Notifier: notification sent");
            }
        }, "Notifier");

        waiter.start();
        notifier.start();

        waiter.join();
        notifier.join();

        System.out.println();
    }

    // Inner class for Counter example
    static class Counter {
        private int count = 0;

        public synchronized void increment() {
            count++;
        }

        public int getCount() {
            return count;
        }
    }

    // Static synchronized counter
    static class StaticCounter {
        private static int count = 0;

        public static synchronized void increment() {
            count++;
        }

        public static int getCount() {
            return count;
        }
    }
}
