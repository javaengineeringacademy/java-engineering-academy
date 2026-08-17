package academy.javaengineering.concurrency.solutions;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Solutions: Threads and Synchronization
 */
public class ThreadSolutions {

    // Solution 1: Synchronized counter
    public static class SynchronizedCounter {
        private int count = 0;

        public synchronized void increment() { count++; }
        public synchronized void decrement() { count--; }
        public synchronized int getCount() { return count; }
    }

    // Solution 2: Atomic counter
    public static class AtomicCounter {
        private final AtomicInteger count = new AtomicInteger(0);

        public void increment() { count.incrementAndGet(); }
        public void decrement() { count.decrementAndGet(); }
        public int getCount() { return count.get(); }
    }

    // Solution 3: Bounded buffer (producer-consumer)
    public static class BoundedBuffer<T> {
        private final java.util.LinkedList<T> buffer = new java.util.LinkedList<>();
        private final int capacity;

        public BoundedBuffer(int capacity) { this.capacity = capacity; }

        public synchronized void put(T item) throws InterruptedException {
            while (buffer.size() == capacity) wait();
            buffer.add(item);
            notifyAll();
        }

        public synchronized T take() throws InterruptedException {
            while (buffer.isEmpty()) wait();
            T item = buffer.removeFirst();
            notifyAll();
            return item;
        }

        public synchronized int size() { return buffer.size(); }
    }

    // Solution 4: Read-write lock
    public static class ReadWriteLock {
        private final ReentrantLock readLock = new ReentrantLock();
        private final ReentrantLock writeLock = new ReentrantLock();
        private int readers = 0;

        public void readLock() throws InterruptedException {
            readLock.lock();
            try { readers++; } finally { readLock.unlock(); }
        }

        public void readUnlock() {
            readLock.lock();
            try { readers--; if (readers == 0) writeLock.lock(); } finally { readLock.unlock(); }
        }

        public void writeLock() throws InterruptedException {
            writeLock.lock();
        }

        public void writeUnlock() {
            writeLock.unlock();
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== Thread Solutions ===\n");

        // Test SynchronizedCounter
        SynchronizedCounter syncCounter = new SynchronizedCounter();
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) syncCounter.increment();
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();
        System.out.println("SynchronizedCounter: " + syncCounter.getCount() + " (expected 10000)");

        // Test AtomicCounter
        AtomicCounter atomicCounter = new AtomicCounter();
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) atomicCounter.increment();
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();
        System.out.println("AtomicCounter: " + atomicCounter.getCount() + " (expected 10000)");

        // Test BoundedBuffer
        BoundedBuffer<Integer> buffer = new BoundedBuffer<>(5);
        java.util.List<Integer> consumed = java.util.Collections.synchronizedList(new java.util.ArrayList<>());
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                try { buffer.put(i); } catch (InterruptedException e) { break; }
            }
        });
        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                try { consumed.add(buffer.take()); } catch (InterruptedException e) { break; }
            }
        });
        producer.start();
        consumer.start();
        producer.join(5000);
        consumer.join(5000);
        System.out.println("BoundedBuffer: consumed " + consumed.size() + " items (expected 20)");
    }
}
