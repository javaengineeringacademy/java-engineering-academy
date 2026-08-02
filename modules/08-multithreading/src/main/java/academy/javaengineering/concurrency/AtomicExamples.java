package academy.javaengineering.concurrency;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Demonstrates atomic variables for lock-free thread-safe operations.
 * Shows AtomicInteger, AtomicLong, and AtomicReference usage.
 */
public class AtomicExamples {

    private final AtomicInteger atomicCounter = new AtomicInteger(0);
    private final AtomicLong atomicLong = new AtomicLong(0);
    private final AtomicReference<String> atomicReference = new AtomicReference<>("initial");

    public static void main(String[] args) throws InterruptedException {
        AtomicExamples example = new AtomicExamples();
        example.demonstrateAtomicInteger();
        example.demonstrateAtomicLong();
        example.demonstrateAtomicReference();
        example.demonstrateAtomicOperations();
    }

    /**
     * Demonstrates AtomicInteger for thread-safe counter.
     */
    public void demonstrateAtomicInteger() throws InterruptedException {
        atomicCounter.set(0);
        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    atomicCounter.incrementAndGet();
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("AtomicInteger Counter: " + atomicCounter.get());
        // Expected output: AtomicInteger Counter: 10000
    }

    /**
     * Demonstrates AtomicLong for thread-safe long operations.
     */
    public void demonstrateAtomicLong() throws InterruptedException {
        atomicLong.set(0);
        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    atomicLong.addAndGet(100);
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("AtomicLong Value: " + atomicLong.get());
        // Expected output: AtomicLong Value: 1000000
    }

    /**
     * Demonstrates AtomicReference for thread-safe reference updates.
     */
    public void demonstrateAtomicReference() throws InterruptedException {
        atomicReference.set("initial");
        Thread[] threads = new Thread[5];

        for (int i = 0; i < 5; i++) {
            final int threadNum = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    String current;
                    do {
                        current = atomicReference.get();
                    } while (!atomicReference.compareAndSet(current, current + "-" + threadNum));
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("AtomicReference Value: " + atomicReference.get());
        // Expected output: AtomicReference Value: initial-0-1-2-3-4 (order may vary)
    }

    /**
     * Demonstrates various atomic operations.
     */
    public void demonstrateAtomicOperations() throws InterruptedException {
        AtomicInteger value = new AtomicInteger(10);

        System.out.println("Initial value: " + value.get());
        System.out.println("After incrementAndGet: " + value.incrementAndGet());
        System.out.println("After getAndAdd: " + value.getAndAdd(5));
        System.out.println("After compareAndSet: " + value.compareAndSet(15, 100));
        System.out.println("Final value: " + value.get());

        // Expected output:
        // Initial value: 10
        // After incrementAndGet: 11
        // After getAndAdd: 11
        // After compareAndSet: true
        // Final value: 100
    }
}
