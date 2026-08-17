package academy.javaengineering.concurrency.synchronization;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Practices - Exercises for Synchronization.
 * Complete each exercise by filling in the TODO sections.
 */
public class Practices {

    /**
     * Exercise 1: Fix the Race Condition
     * The counter below has a race condition. Fix it using synchronized.
     * The final count should be exactly 1000000.
     *
     * TODO: Add synchronization to make the counter thread-safe.
     */
    static void exercise1() throws InterruptedException {
        System.out.println("Exercise 1: Fix the Race Condition");
        System.out.println("===================================");

        int[] counter = {0}; // RACE CONDITION!

        // TODO: Create 10 threads that each increment counter[0] 100000 times
        // TODO: Use synchronized to make it thread-safe
        // TODO: Print expected (1000000) and actual count

        System.out.println();
    }

    /**
     * Exercise 2: volatile Visibility
     * Create a worker thread that loops until a flag is set.
     * Use volatile to ensure the worker sees the flag change.
     *
     * TODO: Implement volatile flag for cross-thread visibility.
     */
    static void exercise2() throws InterruptedException {
        System.out.println("Exercise 2: volatile Visibility");
        System.out.println("================================");

        // TODO: Create a volatile boolean flag
        // TODO: Create a worker thread that loops while flag is false
        // TODO: After 500ms, set flag to true
        // TODO: Worker should stop and print "Worker stopped"

        System.out.println();
    }

    /**
     * Exercise 3: AtomicInteger Operations
     * Use AtomicInteger to implement a thread-safe counter that supports
     * increment, decrement, and get operations.
     *
     * TODO: Use AtomicInteger for thread-safe counter operations.
     */
    static void exercise3() throws InterruptedException {
        System.out.println("Exercise 3: AtomicInteger Operations");
        System.out.println("====================================");

        // TODO: Create AtomicInteger counter
        // TODO: Create 5 threads that increment 1000 times each
        // TODO: Create 3 threads that decrement 500 times each
        // TODO: Print final value (expected: 3500)

        System.out.println();
    }

    /**
     * Exercise 4: Producer-Consumer with synchronized
     * Implement a bounded buffer (size 5) using synchronized blocks.
     * Producer adds 1-20, consumer removes and prints them.
     *
     * TODO: Implement producer-consumer with synchronized.
     */
    static void exercise4() throws InterruptedException {
        System.out.println("Exercise 4: Producer-Consumer (synchronized)");
        System.out.println("=============================================");

        // TODO: Create shared buffer of size 5
        // TODO: Create producer thread (adds 1-20)
        // TODO: Create consumer thread (removes and prints)
        // TODO: Use synchronized with wait/notify

        System.out.println();
    }

    /**
     * Exercise 5: Deadlock Prevention
     * Two threads need locks A and B. Thread 1 locks A then B,
     * Thread 2 locks B then A (deadlock!). Fix by ensuring consistent
     * lock ordering.
     *
     * TODO: Fix the deadlock by using consistent lock order.
     */
    static void exercise5() throws InterruptedException {
        System.out.println("Exercise 5: Deadlock Prevention");
        System.out.println("================================");

        final Object lockA = new Object();
        final Object lockB = new Object();

        // TODO: Thread 1: lock A then B (with consistent ordering)
        // TODO: Thread 2: lock A then B (same order as Thread 1!)
        // TODO: Both threads should complete without deadlock

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
