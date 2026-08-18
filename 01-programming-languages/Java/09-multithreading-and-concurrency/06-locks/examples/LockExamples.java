package academy.javaengineering.concurrency.locks.examples;

import java.util.concurrent.locks.*;
import java.util.concurrent.TimeUnit;

public class LockExamples {
    public static void main(String[] args) throws InterruptedException {
        // Example 1: ReentrantLock basic usage
        System.out.println("=== ReentrantLock ===");
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        try {
            System.out.println("Lock acquired, hold count: " + lock.getHoldCount());
            lock.lock(); // reentrant
            System.out.println("Reentrant: " + lock.getHoldCount());
            lock.unlock();
        } finally {
            lock.unlock();
        }

        // Example 2: Try-lock with timeout
        System.out.println("\n=== Try-Lock ===");
        if (lock.tryLock(100, TimeUnit.MILLISECONDS)) {
            try {
                System.out.println("Got lock within timeout");
            } finally {
                lock.unlock();
            }
        } else {
            System.out.println("Could not get lock");
        }

        // Example 3: ReadWriteLock
        System.out.println("\n=== ReadWriteLock ===");
        ReadWriteLock rwLock = new ReentrantReadWriteLock();
        java.util.concurrent.atomic.AtomicInteger sharedData = new java.util.concurrent.atomic.AtomicInteger(0);

        Runnable reader = () -> {
            rwLock.readLock().lock();
            try {
                System.out.println("Read: " + sharedData.get());
            } finally {
                rwLock.readLock().unlock();
            }
        };

        Runnable writer = () -> {
            rwLock.writeLock().lock();
            try {
                sharedData.incrementAndGet();
                System.out.println("Written: " + sharedData.get());
            } finally {
                rwLock.writeLock().unlock();
            }
        };

        Thread r1 = new Thread(reader);
        Thread r2 = new Thread(reader);
        Thread w = new Thread(writer);
        w.start();
        w.join();
        r1.start();
        r2.start();
        r1.join();
        r2.join();

        // Example 4: Condition variables
        System.out.println("\n=== Conditions ===");
        ReentrantLock condLock = new ReentrantLock();
        Condition notEmpty = condLock.newCondition();
        Condition notFull = condLock.newCondition();
        java.util.LinkedList<Integer> buffer = new java.util.LinkedList<>();
        int capacity = 3;

        Thread producer = new Thread(() -> {
            for (int i = 0; i < 6; i++) {
                condLock.lock();
                try {
                    while (buffer.size() == capacity) notFull.await();
                    buffer.add(i);
                    System.out.println("Produced: " + i);
                    notEmpty.signal();
                } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                finally { condLock.unlock(); }
            }
        });

        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 6; i++) {
                condLock.lock();
                try {
                    while (buffer.isEmpty()) notEmpty.await();
                    System.out.println("  Consumed: " + buffer.remove());
                    notFull.signal();
                } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                finally { condLock.unlock(); }
            }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }
}
