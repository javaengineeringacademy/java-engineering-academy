package academy.javaengineering.concurrency.introduction;

import java.util.ArrayList;
import java.util.List;

/**
 * Practices - Exercises for Introduction to Multithreading.
 * Complete each exercise by filling in the TODO sections.
 */
public class Practices {

    /**
     * Exercise 1: Create two threads that count down from 5 to 1 concurrently.
     * Both threads should print "Thread-X: N" where X is thread number and N is count.
     * The main thread should wait for both to finish before printing "Done".
     *
     * TODO: Implement the countdown logic using two threads.
     */
    static void exercise1() throws InterruptedException {
        System.out.println("Exercise 1: Concurrent Countdown");
        System.out.println("==================================");

        // TODO: Create two threads that count down from 5 to 1
        // TODO: Start both threads
        // TODO: Wait for both threads to complete
        // TODO: Print "Done" after both complete

        System.out.println();
    }

    /**
     * Exercise 2: Implement a thread-safe counter using synchronization.
     * Create a shared counter that 10 threads each increment 1000 times.
     * The final count should be exactly 10000.
     *
     * TODO: Fix the race condition to get the correct count.
     */
    static void exercise2() throws InterruptedException {
        System.out.println("Exercise 2: Thread-Safe Counter");
        System.out.println("===============================");

        int[] counter = {0}; // Shared counter - THIS HAS A RACE CONDITION

        // TODO: Create 10 threads that each increment counter[0] 1000 times
        // TODO: Use synchronization to prevent race conditions
        // TODO: Wait for all threads to complete
        // TODO: Print expected (10000) and actual count

        System.out.println();
    }

    /**
     * Exercise 3: Producer-Consumer with bounded buffer.
     * Implement a producer that generates numbers 1-20 and a consumer that
     * reads them. The buffer should hold at most 5 items at a time.
     *
     * TODO: Implement the producer-consumer with bounded buffer.
     */
    static void exercise3() throws InterruptedException {
        System.out.println("Exercise 3: Bounded Producer-Consumer");
        System.out.println("======================================");

        // TODO: Create a bounded buffer of size 5
        // TODO: Create a producer thread that generates 1-20
        // TODO: Create a consumer thread that reads and prints values
        // TODO: Use wait/notify for coordination

        System.out.println();
    }

    /**
     * Exercise 4: Thread Interruption Pattern.
     * Create a worker thread that performs a long-running task.
     * After 500ms, interrupt the worker. The worker should catch
     * the interruption, clean up, and print a shutdown message.
     *
     * TODO: Implement graceful shutdown using interrupts.
     */
    static void exercise4() throws InterruptedException {
        System.out.println("Exercise 4: Graceful Shutdown");
        System.out.println("=============================");

        // TODO: Create a worker thread that does work in a loop
        // TODO: The worker should check for interrupts
        // TODO: After 500ms, interrupt the worker from main
        // TODO: The worker should clean up and print "Shutting down..."

        System.out.println();
    }

    /**
     * Exercise 5: Thread.join with Timeout.
     * Create a thread that takes 3 seconds to complete.
     * Use join with a 1-second timeout, then check if the thread is still alive.
     * If alive, print "Still working..." and wait for actual completion.
     *
     * TODO: Implement join with timeout logic.
     */
    static void exercise5() throws InterruptedException {
        System.out.println("Exercise 5: Join with Timeout");
        System.out.println("=============================");

        // TODO: Create a thread that sleeps for 3 seconds
        // TODO: Start the thread
        // TODO: Use join(1000) to wait up to 1 second
        // TODO: Check if thread is alive, print status
        // TODO: Wait for actual completion

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
