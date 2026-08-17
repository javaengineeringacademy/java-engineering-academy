package academy.javaengineering.concurrency.practices;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Exercises: Threads and Synchronization
 *
 * Complete the TODO sections below.
 */
public class ThreadExercises {

    // TODO 1: Create a thread-safe counter
    // Implement using synchronized keyword
    public static class SynchronizedCounter {
        private int count = 0;

        public synchronized void increment() {
            // TODO: implement this
        }

        public synchronized void decrement() {
            // TODO: implement this
        }

        public synchronized int getCount() {
            // TODO: implement this
            return 0;
        }
    }

    // TODO 2: Create a thread-safe counter using AtomicInteger
    public static class AtomicCounter {
        private final AtomicInteger count = new AtomicInteger(0);

        public void increment() {
            // TODO: implement this
        }

        public void decrement() {
            // TODO: implement this
        }

        public int getCount() {
            // TODO: implement this
            return 0;
        }
    }

    // TODO 3: Implement a producer-consumer pattern
    // BoundedBuffer has a fixed capacity
    // Producer adds items, Consumer removes items
    // Must block when buffer is full or empty
    public static class BoundedBuffer<T> {
        private final java.util.LinkedList<T> buffer = new java.util.LinkedList<>();
        private final int capacity;

        public BoundedBuffer(int capacity) {
            this.capacity = capacity;
        }

        public synchronized void put(T item) throws InterruptedException {
            // TODO: implement this - wait if full, then add
        }

        public synchronized T take() throws InterruptedException {
            // TODO: implement this - wait if empty, then remove and return
            return null;
        }

        public synchronized int size() {
            return buffer.size();
        }
    }

    // TODO 4: Implement a reader-writer lock pattern
    // Multiple readers can read simultaneously
    // Only one writer can write, and no readers during writing
    public static class ReadWriteLock {
        private final ReentrantLock readLock = new ReentrantLock();
        private final ReentrantLock writeLock = new ReentrantLock();
        private int readers = 0;

        public void readLock() throws InterruptedException {
            // TODO: implement this
        }

        public void readUnlock() {
            // TODO: implement this
        }

        public void writeLock() throws InterruptedException {
            // TODO: implement this
        }

        public void writeUnlock() {
            // TODO: implement this
        }
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) throws Exception {
        int passed = 0;
        int total = 0;

        System.out.println("=== ThreadExercises Tests ===\n");

        // Test 1: Synchronized Counter
        total++;
        try {
            SynchronizedCounter counter = new SynchronizedCounter();
            Thread[] threads = new Thread[10];
            for (int i = 0; i < 10; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < 1000; j++) counter.increment();
                });
                threads[i].start();
            }
            for (Thread t : threads) t.join();
            if (counter.getCount() == 10000) {
                System.out.println("Test 1 PASSED: SynchronizedCounter");
                passed++;
            } else {
                System.out.println("Test 1 FAILED: SynchronizedCounter - expected 10000, got " + counter.getCount());
            }
        } catch (InterruptedException e) {
            System.out.println("Test 1 FAILED: SynchronizedCounter - " + e.getMessage());
        }

        // Test 2: Atomic Counter
        total++;
        try {
            AtomicCounter atomicCounter = new AtomicCounter();
            Thread[] threads = new Thread[10];
            for (int i = 0; i < 10; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < 1000; j++) atomicCounter.increment();
                });
                threads[i].start();
            }
            for (Thread t : threads) t.join();
            if (atomicCounter.getCount() == 10000) {
                System.out.println("Test 2 PASSED: AtomicCounter");
                passed++;
            } else {
                System.out.println("Test 2 FAILED: AtomicCounter - expected 10000, got " + atomicCounter.getCount());
            }
        } catch (InterruptedException e) {
            System.out.println("Test 2 FAILED: AtomicCounter - " + e.getMessage());
        }

        // Test 3: Bounded Buffer
        total++;
        try {
            BoundedBuffer<Integer> buffer = new BoundedBuffer<>(5);
            List<Integer> produced = Collections.synchronizedList(new ArrayList<>());
            List<Integer> consumed = Collections.synchronizedList(new ArrayList<>());

            Thread producer = new Thread(() -> {
                for (int i = 0; i < 20; i++) {
                    try {
                        buffer.put(i);
                        produced.add(i);
                    } catch (InterruptedException e) { break; }
                }
            });

            Thread consumer = new Thread(() -> {
                for (int i = 0; i < 20; i++) {
                    try {
                        Integer val = buffer.take();
                        consumed.add(val);
                    } catch (InterruptedException e) { break; }
                }
            });

            producer.start();
            consumer.start();
            producer.join(5000);
            consumer.join(5000);

            if (consumed.size() == 20 && consumed.containsAll(produced)) {
                System.out.println("Test 3 PASSED: BoundedBuffer");
                passed++;
            } else {
                System.out.println("Test 3 FAILED: BoundedBuffer");
            }
        } catch (InterruptedException e) {
            System.out.println("Test 3 FAILED: BoundedBuffer - " + e.getMessage());
        }

        // Test 4: ReadWriteLock
        total++;
        try {
            ReadWriteLock rwLock = new ReadWriteLock();
            AtomicInteger readCount = new AtomicInteger(0);
            AtomicInteger writeCount = new AtomicInteger(0);

            Thread[] readers = new Thread[5];
            for (int i = 0; i < 5; i++) {
                readers[i] = new Thread(() -> {
                    try {
                        rwLock.readLock();
                        readCount.incrementAndGet();
                        Thread.sleep(10);
                        rwLock.readUnlock();
                    } catch (InterruptedException e) {}
                });
            }

            Thread writer = new Thread(() -> {
                try {
                    rwLock.writeLock();
                    writeCount.incrementAndGet();
                    Thread.sleep(10);
                    rwLock.writeUnlock();
                } catch (InterruptedException e) {}
            });

            for (Thread r : readers) r.start();
            writer.start();
            for (Thread r : readers) r.join();
            writer.join();

            if (readCount.get() == 5 && writeCount.get() == 1) {
                System.out.println("Test 4 PASSED: ReadWriteLock");
                passed++;
            } else {
                System.out.println("Test 4 FAILED: ReadWriteLock");
            }
        } catch (InterruptedException e) {
            System.out.println("Test 4 FAILED: ReadWriteLock - " + e.getMessage());
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
