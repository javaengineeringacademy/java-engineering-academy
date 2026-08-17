package academy.javaengineering.concurrency.locks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

/**
 * Practices - Exercises for Locks.
 * Complete each exercise by filling in the TODO sections.
 */
public class Practices {

    /**
     * Exercise 1: ReentrantLock Counter
     * Create a thread-safe counter using ReentrantLock.
     * 10 threads should each increment 100000 times.
     * Use tryLock for one thread to demonstrate non-blocking behavior.
     *
     * TODO: Implement ReentrantLock-based counter.
     */
    static void exercise1() throws InterruptedException {
        System.out.println("Exercise 1: ReentrantLock Counter");
        System.out.println("=================================");

        // TODO: Create ReentrantLock
        // TODO: Create 10 threads that increment counter using lock/unlock
        // TODO: One thread should use tryLock and print if it succeeded
        // TODO: Print final count (expected: 1000000)

        System.out.println();
    }

    /**
     * Exercise 2: ReadWriteLock Cache
     * Implement a simple cache using ReadWriteLock.
     * Multiple reader threads should read concurrently.
     * One writer thread should update the cache exclusively.
     *
     * TODO: Implement ReadWriteLock-based cache.
     */
    static void exercise2() throws InterruptedException {
        System.out.println("Exercise 2: ReadWriteLock Cache");
        System.out.println("===============================");

        // TODO: Create ReadWriteLock and shared data
        // TODO: Create 5 reader threads that read data
        // TODO: Create 1 writer thread that updates data
        // TODO: Readers should run concurrently, writer should be exclusive

        System.out.println();
    }

    /**
     * Exercise 3: StampedLock Optimistic Read
     * Use StampedLock to implement optimistic reading.
     * If validation fails, fall back to pessimistic read lock.
     *
     * TODO: Implement StampedLock optimistic read pattern.
     */
    static void exercise3() throws InterruptedException {
        System.out.println("Exercise 3: StampedLock Optimistic Read");
        System.out.println("=======================================");

        // TODO: Create StampedLock and shared data (x, y)
        // TODO: Reader uses tryOptimisticRead + validate
        // TODO: If invalid, fall back to readLock
        // TODO: Writer updates data with writeLock

        System.out.println();
    }

    /**
     * Exercise 4: Condition for Bounded Buffer
     * Implement a bounded buffer (size 5) using ReentrantLock + Condition.
     * Producer adds 1-20, consumer removes and prints.
     * Use separate Conditions for notFull and notEmpty.
     *
     * TODO: Implement bounded buffer with Conditions.
     */
    static void exercise4() throws InterruptedException {
        System.out.println("Exercise 4: Condition Bounded Buffer");
        System.out.println("=====================================");

        // TODO: Create ReentrantLock with notFull and notEmpty Conditions
        // TODO: Producer awaits notFull when buffer is full
        // TODO: Consumer awaits notEmpty when buffer is empty
        // TODO: Producer signals notEmpty, Consumer signals notFull

        System.out.println();
    }

    /**
     * Exercise 5: tryLock with Timeout
     * Implement a thread that attempts to acquire a lock with a timeout.
     * If it can't acquire within 500ms, it should print "Giving up" and continue.
     *
     * TODO: Implement tryLock with timeout.
     */
    static void exercise5() throws InterruptedException {
        System.out.println("Exercise 5: tryLock with Timeout");
        System.out.println("=================================");

        // TODO: Create ReentrantLock
        // TODO: One thread holds the lock for 2 seconds
        // TODO: Another thread tries to acquire with 500ms timeout
        // TODO: If timeout, print "Giving up"

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
