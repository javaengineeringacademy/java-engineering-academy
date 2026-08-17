package academy.javaengineering.concurrency.threadcreation;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Practices - Exercises for Thread Creation.
 * Complete each exercise by filling in the TODO sections.
 */
public class Practices {

    /**
     * Exercise 1: Create 5 threads using each of the 3 creation methods.
     * Each thread should print "Thread created using [method]: [name]"
     *
     * TODO: Implement using extends Thread, implements Runnable, and lambda.
     */
    static void exercise1() throws InterruptedException {
        System.out.println("Exercise 1: Three Creation Methods");
        System.out.println("===================================");

        // TODO: Create thread 1 and 2 by extending Thread
        // TODO: Create thread 3 and 4 by implementing Runnable
        // TODO: Create thread 5 using lambda
        // TODO: Start all threads and wait for completion

        System.out.println();
    }

    /**
     * Exercise 2: Use Callable to compute factorial of numbers 1-10.
     * Submit all 10 tasks to an ExecutorService and print results.
     *
     * TODO: Implement Callable for factorial calculation.
     */
    static void exercise2() {
        System.out.println("Exercise 2: Factorial with Callable");
        System.out.println("====================================");

        // TODO: Create an ExecutorService with 3 threads
        // TODO: Create Callable<Long> for factorial of n
        // TODO: Submit tasks for n = 1 to 10
        // TODO: Collect and print all results using Future.get()
        // TODO: Shut down executor

        System.out.println();
    }

    /**
     * Exercise 3: Create a ThreadFactory that produces named threads.
     * The factory should prefix threads with "Pool-" and auto-increment.
     *
     * TODO: Implement ThreadFactory interface.
     */
    static void exercise3() throws InterruptedException {
        System.out.println("Exercise 3: Custom Thread Factory");
        System.out.println("=================================");

        // TODO: Implement ThreadFactory that creates "Pool-N" threads
        // TODO: Use the factory to create 5 threads
        // TODO: Each thread should print its name
        // TODO: Start and join all threads

        System.out.println();
    }

    /**
     * Exercise 4: Use CompletableFuture to chain operations.
     * Step 1: Generate a random number (1-100)
     * Step 2: Square the number
     * Step 3: Check if even or odd
     * Step 4: Print the final result
     *
     * TODO: Implement CompletableFuture chain.
     */
    static void exercise4() {
        System.out.println("Exercise 4: CompletableFuture Chain");
        System.out.println("====================================");

        // TODO: Use CompletableFuture.supplyAsync to generate random number
        // TODO: Chain thenApply to square it
        // TODO: Chain thenApply to determine even/odd
        // TODO: Chain thenAccept to print result
        // TODO: Use join() to wait for completion

        System.out.println();
    }

    /**
     * Exercise 5: Create a daemon thread that prints a heartbeat every 500ms.
     * The main thread should wait 2 seconds then print "Main done".
     * The daemon thread should stop automatically when main exits.
     *
     * TODO: Implement daemon thread with heartbeat pattern.
     */
    static void exercise5() throws InterruptedException {
        System.out.println("Exercise 5: Daemon Heartbeat");
        System.out.println("============================");

        // TODO: Create a daemon thread with infinite loop
        // TODO: Thread should print heartbeat with timestamp every 500ms
        // TODO: Main thread should sleep 2 seconds
        // TODO: Main thread prints "Main done"
        // TODO: Daemon stops automatically (JVM exits)

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
