package multithreading;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * SyncExamples - synchronized, volatile, and atomic operations
 *
 * Covers:
 * - synchronized method and block
 * - volatile keyword
 * - AtomicInteger for lock-free operations
 * - Race conditions and thread safety
 */
public class SyncExamples {

    private static int counter = 0;
    private static volatile boolean running = true;
    private static AtomicInteger atomicCounter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Race Condition ===");
        raceConditionDemo();

        System.out.println("\n=== synchronized Method ===");
        synchronizedMethodDemo();

        System.out.println("\n=== synchronized Block ===");
        synchronizedBlockDemo();

        System.out.println("\n=== volatile ===");
        volatileDemo();

        System.out.println("\n=== AtomicInteger ===");
        atomicIntegerDemo();
    }

    static void raceConditionDemo() throws InterruptedException {
        counter = 0;
        int numThreads = 10;
        int incrementsPerThread = 100000;

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    counter++; // NOT thread-safe!
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        int expected = numThreads * incrementsPerThread;
        System.out.println("Expected: " + expected);
        System.out.println("Actual: " + counter);
        System.out.println("Race condition occurred: " + (counter != expected));
    }

    static void synchronizedMethodDemo() throws InterruptedException {
        SynchronizedCounter syncCounter = new SynchronizedCounter();
        int numThreads = 10;
        int incrementsPerThread = 100000;

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    syncCounter.increment();
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        int expected = numThreads * incrementsPerThread;
        System.out.println("Expected: " + expected);
        System.out.println("Actual: " + syncCounter.getCount());
        System.out.println("Thread-safe: " + (syncCounter.getCount() == expected));
    }

    static void synchronizedBlockDemo() throws InterruptedException {
        final int[] counter = {0};
        Object lock = new Object();
        int numThreads = 10;
        int incrementsPerThread = 100000;

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    synchronized (lock) {
                        counter[0]++;
                    }
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        int expected = numThreads * incrementsPerThread;
        System.out.println("Expected: " + expected);
        System.out.println("Actual: " + counter[0]);
        System.out.println("Thread-safe: " + (counter[0] == expected));
    }

    static void volatileDemo() throws InterruptedException {
        running = true;

        Thread worker = new Thread(() -> {
            int count = 0;
            while (running) { // Without volatile, may loop forever
                count++;
            }
            System.out.println("Worker stopped after " + count + " iterations");
        });

        worker.start();
        Thread.sleep(100);
        running = false; // Without volatile, worker may not see this

        worker.join();
        System.out.println("Main thread set running = false");
    }

    static void atomicIntegerDemo() throws InterruptedException {
        atomicCounter.set(0);
        int numThreads = 10;
        int incrementsPerThread = 100000;

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    atomicCounter.incrementAndGet();
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        int expected = numThreads * incrementsPerThread;
        System.out.println("Expected: " + expected);
        System.out.println("Actual: " + atomicCounter.get());
        System.out.println("Lock-free and thread-safe: " + (atomicCounter.get() == expected));
    }

    static class SynchronizedCounter {
        private int count = 0;

        public synchronized void increment() {
            count++;
        }

        public synchronized int getCount() {
            return count;
        }
    }
}