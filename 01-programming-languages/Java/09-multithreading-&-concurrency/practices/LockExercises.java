package academy.javaengineering.concurrency.practices;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.StampedLock;

public class LockExercises {

    public static void main(String[] args) throws InterruptedException {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }

    /**
     * Exercise 1: ReentrantLock basics
     * TODO: Use ReentrantLock to protect a critical section.
     *       Demonstrate lock/unlock with try-finally pattern.
     */
    static void exercise1() throws InterruptedException {
        System.out.println("=== Exercise 1: ReentrantLock Basics ===");
        // TODO: Implement here
        // Hint: ReentrantLock lock = new ReentrantLock(); lock.lock(); try { ... } finally { lock.unlock(); }
    }

    /**
     * Exercise 2: tryLock() non-blocking
     * TODO: Use tryLock() to attempt acquiring a lock without blocking.
     *       If lock is not available, print "Could not acquire lock".
     */
    static void exercise2() throws InterruptedException {
        System.out.println("=== Exercise 2: tryLock() ===");
        // TODO: Implement here
        // Hint: boolean acquired = lock.tryLock();
    }

    /**
     * Exercise 3: tryLock with timeout
     * TODO: Use tryLock(timeout) to wait for a lock with a timeout.
     *       If timeout expires, print "Lock acquisition timed out".
     */
    static void exercise3() throws InterruptedException {
        System.out.println("=== Exercise 3: tryLock with Timeout ===");
        // TODO: Implement here
        // Hint: lock.tryLock(1, TimeUnit.SECONDS)
    }

    /**
     * Exercise 4: ReadWriteLock
     * TODO: Use ReadWriteLock to allow multiple readers but exclusive writer.
     *       Demonstrate concurrent reads and exclusive write.
     */
    static void exercise4() throws InterruptedException {
        System.out.println("=== Exercise 4: ReadWriteLock ===");
        // TODO: Implement here
        // Hint: ReadWriteLock rwLock = new ReentrantReadWriteLock();
    }

    /**
     * Exercise 5: StampedLock
     * TODO: Use StampedLock with optimistic reading.
     *       Demonstrate tryOptimisticRead() and validate pattern.
     */
    static void exercise5() throws InterruptedException {
        System.out.println("=== Exercise 5: StampedLock ===");
        // TODO: Implement here
        // Hint: long stamp = lock.tryOptimisticRead(); if (!lock.validate(stamp)) { ... }
    }
}
