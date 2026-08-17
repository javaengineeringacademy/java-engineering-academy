package academy.javaengineering.concurrency.synchronization;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Solutions - Complete solutions for Synchronization exercises.
 */
public class Solutions {

    /**
     * Solution 1: Fix the Race Condition
     */
    static void exercise1() throws InterruptedException {
        System.out.println("Exercise 1: Fix the Race Condition");
        System.out.println("===================================");

        final Object lock = new Object();
        int[] counter = {0};

        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100000; j++) {
                    synchronized (lock) {
                        counter[0]++;
                    }
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) t.join();

        System.out.println("  Expected: 1000000");
        System.out.println("  Actual: " + counter[0]);
        System.out.println("  Thread-safe: " + (counter[0] == 1000000));
        System.out.println();
    }

    /**
     * Solution 2: volatile Visibility
     */
    static void exercise2() throws InterruptedException {
        System.out.println("Exercise 2: volatile Visibility");
        System.out.println("================================");

        volatile boolean running = false;

        Thread worker = new Thread(() -> {
            int count = 0;
            while (!running) {
                count++;
            }
            System.out.println("  Worker stopped after " + count + " iterations");
        });

        worker.start();
        Thread.sleep(500);
        running = true;
        worker.join();
        System.out.println();
    }

    /**
     * Solution 3: AtomicInteger Operations
     */
    static void exercise3() throws InterruptedException {
        System.out.println("Exercise 3: AtomicInteger Operations");
        System.out.println("====================================");

        AtomicInteger counter = new AtomicInteger(0);

        Thread[] incThreads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            incThreads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) counter.incrementAndGet();
            });
            incThreads[i].start();
        }

        Thread[] decThreads = new Thread[3];
        for (int i = 0; i < 3; i++) {
            decThreads[i] = new Thread(() -> {
                for (int j = 0; j < 500; j++) counter.decrementAndGet();
            });
            decThreads[i].start();
        }

        for (Thread t : incThreads) t.join();
        for (Thread t : decThreads) t.join();

        System.out.println("  Expected: " + (5 * 1000 - 3 * 500));
        System.out.println("  Actual: " + counter.get());
        System.out.println();
    }

    /**
     * Solution 4: Producer-Consumer (synchronized)
     */
    static void exercise4() throws InterruptedException {
        System.out.println("Exercise 4: Producer-Consumer (synchronized)");
        System.out.println("=============================================");

        final int BUFFER_SIZE = 5;
        final int ITEMS = 20;
        final int[] buffer = new int[BUFFER_SIZE];
        final Object lock = new Object();
        final int[] count = {0};

        Thread producer = new Thread(() -> {
            for (int i = 1; i <= ITEMS; i++) {
                synchronized (lock) {
                    while (count[0] == BUFFER_SIZE) {
                        try { lock.wait(); } catch (InterruptedException e) { return; }
                    }
                    buffer[count[0]++] = i;
                    System.out.println("  Produced: " + i + " (size: " + count[0] + ")");
                    lock.notify();
                }
            }
        }, "Producer");

        Thread consumer = new Thread(() -> {
            for (int i = 0; i < ITEMS; i++) {
                synchronized (lock) {
                    while (count[0] == 0) {
                        try { lock.wait(); } catch (InterruptedException e) { return; }
                    }
                    int val = buffer[--count[0]];
                    System.out.println("  Consumed: " + val + " (size: " + count[0] + ")");
                    lock.notify();
                }
            }
        }, "Consumer");

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        System.out.println();
    }

    /**
     * Solution 5: Deadlock Prevention
     */
    static void exercise5() throws InterruptedException {
        System.out.println("Exercise 5: Deadlock Prevention");
        System.out.println("================================");

        final Object lockA = new Object();
        final Object lockB = new Object();

        // Both threads lock A first, then B (consistent ordering = no deadlock)
        Thread t1 = new Thread(() -> {
            synchronized (lockA) {
                System.out.println("  T1: acquired lock A");
                try { Thread.sleep(50); } catch (InterruptedException e) { return; }
                synchronized (lockB) {
                    System.out.println("  T1: acquired lock B");
                    System.out.println("  T1: doing work with both locks");
                }
            }
            System.out.println("  T1: done");
        }, "T1");

        Thread t2 = new Thread(() -> {
            synchronized (lockA) { // Same order as T1!
                System.out.println("  T2: acquired lock A");
                try { Thread.sleep(50); } catch (InterruptedException e) { return; }
                synchronized (lockB) {
                    System.out.println("  T2: acquired lock B");
                    System.out.println("  T2: doing work with both locks");
                }
            }
            System.out.println("  T2: done");
        }, "T2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();
        System.out.println("  No deadlock! Both threads completed.");
        System.out.println();
    }

    public static void main(String[] args) throws InterruptedException {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }
}
