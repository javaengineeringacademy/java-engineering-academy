package academy.javaengineering.concurrency.synchronization;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * SynchronizationMemory - Demonstrates memory model concepts in synchronization.
 */
public class SynchronizationMemory {

    private static int plainInt = 0;
    private static volatile int volatileInt = 0;
    private static AtomicInteger atomicInt = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Memory Visibility: plain vs volatile ===");
        memoryVisibility();

        System.out.println("\n=== CAS Atomicity ===");
        casAtomicity();

        System.out.println("\n=== Synchronized Memory Barrier ===");
        synchronizedMemoryBarrier();

        System.out.println("\n=== False Sharing ===");
        falseSharingDemo();
    }

    static void memoryVisibility() throws InterruptedException {
        plainInt = 0;
        volatileInt = 0;
        volatile boolean[] stop = {false};

        Thread writer = new Thread(() -> {
            for (int i = 1; i <= 1000000; i++) {
                plainInt = i;
                volatileInt = i;
            }
            stop[0] = true;
        });

        Thread reader = new Thread(() -> {
            int plainLast = 0;
            int volatileLast = 0;
            while (!stop[0]) {
                int p = plainInt; // May read stale value
                int v = volatileInt; // Always reads fresh value
                if (p != plainLast) plainLast = p;
                if (v != volatileLast) volatileLast = v;
            }
            System.out.println("  Plain int final: " + plainLast);
            System.out.println("  Volatile int final: " + volatileLast);
            System.out.println("  Actual: 1000000");
        });

        writer.start();
        reader.start();
        writer.join(2000);
        reader.join(2000);
    }

    static void casAtomicity() throws InterruptedException {
        atomicInt.set(0);
        int numThreads = 10;
        int increments = 100000;

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < increments; j++) {
                    // CAS loop: atomic read-modify-write
                    int oldVal, newVal;
                    do {
                        oldVal = atomicInt.get();
                        newVal = oldVal + 1;
                    } while (!atomicInt.compareAndSet(oldVal, newVal));
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) t.join();

        int expected = numThreads * increments;
        System.out.println("  CAS result: " + atomicInt.get());
        System.out.println("  Expected: " + expected);
        System.out.println("  Atomicity guaranteed: " + (atomicInt.get() == expected));
    }

    static void synchronizedMemoryBarrier() throws InterruptedException {
        final Object[] data = {null};
        final boolean[] ready = {false};
        final Object lock = new Object();

        Thread writer = new Thread(() -> {
            synchronized (lock) {
                data[0] = "Hello from writer";
                ready[0] = true;
                System.out.println("  Writer: data prepared");
            } // Memory barrier on exit
        });

        Thread reader = new Thread(() -> {
            synchronized (lock) { // Memory barrier on entry
                while (!ready[0]) {
                    try { lock.wait(); } catch (InterruptedException e) { return; }
                }
                System.out.println("  Reader: " + data[0]);
            }
        });

        writer.start();
        reader.start();

        writer.join();
        reader.join();
    }

    static void falseSharingDemo() throws InterruptedException {
        // Without padding: counters share cache line
        long[] results = new long[2];
        long start = System.nanoTime();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100_000_000; i++) {
                results[0]++;
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100_000_000; i++) {
                results[1]++;
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        long elapsed = System.nanoTime() - start;
        System.out.println("  Time with false sharing: " + (elapsed / 1_000_000) + "ms");
        System.out.println("  (Padded arrays would be faster)");
    }
}
