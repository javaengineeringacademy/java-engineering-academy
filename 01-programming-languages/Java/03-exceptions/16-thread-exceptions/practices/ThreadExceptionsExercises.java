package academy.javaengineering.exceptions.thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Exercises for thread exception handling.
 */
public class ThreadExceptionsExercises {

    /**
     * Exercise 1: Set up a UncaughtExceptionHandler that logs exceptions
     * to System.err with format: "ERROR [thread-name]: exception-message"
     */
    static void exercise1() throws InterruptedException {
        System.out.println("--- Exercise 1: UncaughtExceptionHandler ---");

        // TODO: Set default UncaughtExceptionHandler with logging format
        // Then create and start a thread that throws RuntimeException

        System.out.println("Exercise 1 not implemented yet\n");
    }

    /**
     * Exercise 2: Demonstrate the difference between execute() and submit()
     * by catching exceptions from both approaches.
     */
    static void exercise2() throws Exception {
        System.out.println("--- Exercise 2: execute() vs submit() ---");

        ExecutorService executor = Executors.newFixedThreadPool(2);

        // TODO: Use execute() with a task that throws exception
        // Observe what happens (check UncaughtExceptionHandler)

        // TODO: Use submit() with a task that throws exception
        // Retrieve the exception using Future.get()

        executor.shutdown();
        System.out.println("Exercise 2 not implemented yet\n");
    }

    /**
     * Exercise 3: Build a CompletableFuture chain that handles exceptions
     * at each stage and recovers with fallback values.
     */
    static void exercise3() {
        System.out.println("--- Exercise 3: CompletableFuture Chain ---");

        // TODO: Create a CompletableFuture chain:
        // Stage 1: Supply "initial" or throw RuntimeException
        // Stage 2: Transform to uppercase (should handle exception)
        // Stage 3: Append " processed" (should handle exception)
        // Stage 4: Add exceptionally() handler with fallback

        System.out.println("Exercise 3 not implemented yet\n");
    }

    /**
     * Exercise 4: Create a thread that throws exception in finally block
     * and observe what happens to the original exception.
     */
    static void exercise4() throws InterruptedException {
        System.out.println("--- Exercise 4: Exception in finally ---");

        // TODO: Create thread that:
        // 1. Throws RuntimeException in try block
        // 2. Throws different exception in finally block
        // 3. Observe which exception is reported

        System.out.println("Exercise 4 not implemented yet\n");
    }

    /**
     * Exercise 5: Create a simple thread pool exception monitoring pattern
     * that tracks task failures.
     */
    static void exercise5() throws Exception {
        System.out.println("--- Exercise 5: Thread Pool Monitoring ---");

        // TODO: Create ExecutorService with monitoring:
        // 1. Submit 5 tasks, some failing
        // 2. Track failure count
        // 3. Report total failures

        System.out.println("Exercise 5 not implemented yet\n");
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== Thread Exception Exercises ===\n");

        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();

        System.out.println("Complete the exercises by implementing the TODO sections.");
    }
}
