package academy.javaengineering.concurrency.locks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.StampedLock;

/**
 * LockMemory - Demonstrates memory aspects of Java lock implementations.
 */
public class LockMemory {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Lock Memory Overhead ===");
        lockMemoryOverhead();

        System.out.println("\n=== ReentrantLock State ===");
        reentrantLockState();

        System.out.println("\n=== StampedLock Stamp Values ===");
        stampedLockStamps();

        System.out.println("\n=== Condition Wait Set ===");
        conditionWaitSet();
    }

    static void lockMemoryOverhead() throws InterruptedException {
        Runtime runtime = Runtime.getRuntime();

        long before = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("  Memory before: " + before / 1024 + " KB");

        // Create many ReentrantLocks
        int count = 10000;
        ReentrantLock[] locks = new ReentrantLock[count];
        for (int i = 0; i < count; i++) {
            locks[i] = new ReentrantLock();
        }

        long after = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("  Memory after creating " + count + " locks: " +
            after / 1024 + " KB");
        System.out.println("  Approximate per-lock overhead: " +
            ((after - before) / count) + " bytes");
    }

    static void reentrantLockState() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();

        System.out.println("  Initial state:");
        System.out.println("    isLocked: " + lock.isLocked());
        System.out.println("    isHeldByCurrentThread: " + lock.isHeldByCurrentThread());
        System.out.println("    getHoldCount: " + lock.getHoldCount());
        System.out.println("    getQueueLength: " + lock.getQueueLength());

        lock.lock();
        System.out.println("  After lock():");
        System.out.println("    isLocked: " + lock.isLocked());
        System.out.println("    isHeldByCurrentThread: " + lock.isHeldByCurrentThread());
        System.out.println("    getHoldCount: " + lock.getHoldCount());

        lock.lock();
        System.out.println("  After 2nd lock():");
        System.out.println("    getHoldCount: " + lock.getHoldCount());

        lock.unlock();
        lock.unlock();
        System.out.println("  After 2 unlocks:");
        System.out.println("    isLocked: " + lock.isLocked());
        System.out.println("    getHoldCount: " + lock.getHoldCount());
    }

    static void stampedLockStamps() {
        StampedLock sl = new StampedLock();

        long optStamp = sl.tryOptimisticRead();
        System.out.println("  Optimistic read stamp: " + optStamp);
        System.out.println("  Valid (no write): " + sl.validate(optStamp));

        long readStamp = sl.readLock();
        System.out.println("  Read lock stamp: " + readStamp);
        sl.unlockRead(readStamp);

        long writeStamp = sl.writeLock();
        System.out.println("  Write lock stamp: " + writeStamp);
        sl.unlockWrite(writeStamp);

        System.out.println("  Stamps are monotonically increasing sequence numbers");
    }

    static void conditionWaitSet() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Condition notEmpty = lock.newCondition();
        Condition notFull = lock.newCondition();

        Thread t1 = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("  T1: entering notEmpty wait set");
                notEmpty.await();
                System.out.println("  T1: removed from notEmpty wait set");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        });

        Thread t2 = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("  T2: entering notFull wait set");
                notFull.await();
                System.out.println("  T2: removed from notFull wait set");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        });

        t1.start();
        t2.start();
        Thread.sleep(100);

        lock.lock();
        try {
            System.out.println("  Signaling notEmpty");
            notEmpty.signal();
        } finally {
            lock.unlock();
        }

        Thread.sleep(100);

        lock.lock();
        try {
            System.out.println("  Signaling notFull");
            notFull.signal();
        } finally {
            lock.unlock();
        }

        t1.join();
        t2.join();
    }
}
