package academy.javaengineering.concurrency.locks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

/**
 * LockInternals - Demonstrates internal workings of Java lock implementations.
 */
public class LockInternals {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== AQS State Machine ===");
        aqsStateMachine();

        System.out.println("\n=== ReentrantLock Reentrancy ===");
        reentrantLockReentrancy();

        System.out.println("\n=== ReadWriteLock Internals ===");
        readWriteLockInternals();

        System.out.println("\n=== StampedLock Internals ===");
        stampedLockInternals();

        System.out.println("\n=== Condition Internals ===");
        conditionInternals();
    }

    static void aqsStateMachine() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();

        System.out.println("  Before lock: holdCount=" + lock.getHoldCount() +
            ", isLocked=" + lock.isLocked() +
            ", isHeldByCurrentThread=" + lock.isHeldByCurrentThread());

        lock.lock();
        System.out.println("  After 1st lock: holdCount=" + lock.getHoldCount() +
            ", isLocked=" + lock.isLocked());

        lock.lock();
        System.out.println("  After 2nd lock: holdCount=" + lock.getHoldCount());

        lock.unlock();
        System.out.println("  After 1st unlock: holdCount=" + lock.getHoldCount() +
            ", isLocked=" + lock.isLocked());

        lock.unlock();
        System.out.println("  After 2nd unlock: holdCount=" + lock.getHoldCount() +
            ", isLocked=" + lock.isLocked());
    }

    static void reentrantLockReentrancy() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();

        Thread t = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("  Outer lock acquired");
                lock.lock(); // Reentrant - same thread can acquire again
                try {
                    System.out.println("  Inner lock acquired (reentrant)");
                } finally {
                    lock.unlock();
                    System.out.println("  Inner lock released");
                }
                System.out.println("  Still holding outer lock: " + lock.isHeldByCurrentThread());
            } finally {
                lock.unlock();
                System.out.println("  Outer lock released");
            }
        });

        t.start();
        t.join();
        System.out.println("  Lock available: " + !lock.isLocked());
    }

    static void readWriteLockInternals() throws InterruptedException {
        ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();

        Thread reader1 = new Thread(() -> {
            readLock.lock();
            try {
                System.out.println("  Reader 1: acquired read lock");
                System.out.println("  Reader 1: write lock available: " +
                    !writeLock.isLocked());
            } finally {
                readLock.unlock();
            }
        });

        Thread reader2 = new Thread(() -> {
            try { Thread.sleep(50); } catch (InterruptedException e) { return; }
            readLock.lock();
            try {
                System.out.println("  Reader 2: acquired read lock (concurrent with Reader 1)");
            } finally {
                readLock.unlock();
            }
        });

        reader1.start();
        reader2.start();

        reader1.join();
        reader2.join();
    }

    static void stampedLockInternals() {
        StampedLock sl = new StampedLock();

        // Optimistic read
        long stamp = sl.tryOptimisticRead();
        System.out.println("  Optimistic read stamp: " + stamp);
        System.out.println("  Stamp valid: " + sl.validate(stamp));

        // Read lock
        stamp = sl.readLock();
        System.out.println("  Read lock stamp: " + stamp);
        sl.unlockRead(stamp);

        // Write lock
        stamp = sl.writeLock();
        System.out.println("  Write lock stamp: " + stamp);
        sl.unlockWrite(stamp);
    }

    static void conditionInternals() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        Thread waiter = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("  Waiter: entering wait set");
                condition.await(); // Releases lock, enters condition's wait set
                System.out.println("  Waiter: removed from wait set");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        });

        Thread signer = new Thread(() -> {
            try { Thread.sleep(200); } catch (InterruptedException e) { return; }
            lock.lock();
            try {
                System.out.println("  Signaler: signaling condition");
                condition.signal(); // Moves one thread to lock's entry set
            } finally {
                lock.unlock();
            }
        });

        waiter.start();
        signer.start();

        waiter.join();
        signer.join();
    }
}
