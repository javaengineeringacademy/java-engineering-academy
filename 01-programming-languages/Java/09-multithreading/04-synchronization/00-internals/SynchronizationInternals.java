package academy.javaengineering.concurrency.synchronization;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * SynchronizationInternals - Demonstrates internal workings of synchronization mechanisms.
 */
public class SynchronizationInternals {

    private static int plainCounter = 0;
    private static volatile int volatileCounter = 0;
    private static AtomicInteger atomicCounter = new AtomicInteger(0);
    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== CAS Operation Internals ===");
        casInternals();

        System.out.println("\n=== Monitor Lock Internals ===");
        monitorLockInternals();

        System.out.println("\n=== volatile Memory Fence ===");
        volatileMemoryFence();

        System.out.println("\n=== synchronized Method Internals ===");
        synchronizedMethodInternals();
    }

    static void casInternals() throws InterruptedException {
        atomicCounter.set(0);
        int numThreads = 10;
        int increments = 100000;

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < increments; j++) {
                    // CAS loop: read, compute, swap atomically
                    atomicCounter.incrementAndGet();
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) t.join();

        int expected = numThreads * increments;
        System.out.println("  CAS result: " + atomicCounter.get());
        System.out.println("  Expected: " + expected);
        System.out.println("  Lock-free and correct: " + (atomicCounter.get() == expected));
    }

    static void monitorLockInternals() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("  T1: acquired monitor (reentrant test)");
                synchronized (lock) {
                    System.out.println("  T1: re-entered same monitor (reentrant)");
                }
                System.out.println("  T1: exiting inner sync");
            }
        }, "T1");

        Thread t2 = new Thread(() -> {
            try { Thread.sleep(50); } catch (InterruptedException e) { return; }
            synchronized (lock) {
                System.out.println("  T2: acquired monitor after T1 released");
            }
        }, "T2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    static void volatileMemoryFence() throws InterruptedException {
        volatileCounter = 0;
        volatile boolean[] stop = {false};

        Thread writer = new Thread(() -> {
            for (int i = 0; i < 1000000; i++) {
                volatileCounter = i; // volatile write: memory fence
            }
            stop[0] = true;
        });

        Thread reader = new Thread(() -> {
            int last = 0;
            while (!stop[0]) {
                if (volatileCounter != last) { // volatile read: memory fence
                    last = volatileCounter;
                }
            }
            System.out.println("  Reader saw: " + last);
        });

        writer.start();
        reader.start();
        writer.join(2000);
        reader.join(2000);
    }

    static void synchronizedMethodInternals() throws InterruptedException {
        SynchronizedCounter counter = new SynchronizedCounter();

        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100000; j++) {
                    counter.increment(); // Acquires 'this' monitor
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) t.join();

        System.out.println("  Synchronized result: " + counter.getCount());
        System.out.println("  Expected: 1000000");
        System.out.println("  Correct: " + (counter.getCount() == 1000000));
    }

    static class SynchronizedCounter {
        private int count = 0;

        public synchronized void increment() {
            count++; // 'this' monitor acquired
        }

        public synchronized int getCount() {
            return count; // 'this' monitor acquired
        }
    }
}
