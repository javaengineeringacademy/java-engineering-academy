package academy.javaengineering.concurrency.practices;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceExercises {

    public static void main(String[] args) throws InterruptedException {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }

    /**
     * Exercise 1: Fixed thread pool
     * TODO: Create a fixed thread pool with 3 threads.
     *       Submit 10 tasks that print their thread name and task number.
     *       Shutdown the executor and wait for termination.
     */
    static void exercise1() throws InterruptedException {
        System.out.println("=== Exercise 1: Fixed Thread Pool ===");
        // TODO: Implement here
        // Hint: ExecutorService executor = Executors.newFixedThreadPool(3);
    }

    /**
     * Exercise 2: Cached thread pool
     * TODO: Create a cached thread pool.
     *       Submit 10 tasks that sleep for 1 second each.
     *       Observe that threads are reused.
     */
    static void exercise2() throws InterruptedException {
        System.out.println("=== Exercise 2: Cached Thread Pool ===");
        // TODO: Implement here
        // Hint: ExecutorService executor = Executors.newCachedThreadPool();
    }

    /**
     * Exercise 3: Scheduled thread pool
     * TODO: Create a scheduled thread pool.
     *       Schedule a task to run after 2 seconds delay.
     *       Schedule another task to run every 1 second.
     */
    static void exercise3() throws InterruptedException {
        System.out.println("=== Exercise 3: Scheduled Thread Pool ===");
        // TODO: Implement here
        // Hint: ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    }

    /**
     * Exercise 4: Callable and Future
     * TODO: Submit a Callable that returns a computed value after sleeping.
     *       Use Future.get() to retrieve the result (blocks until available).
     */
    static void exercise4() throws Exception {
        System.out.println("=== Exercise 4: Callable and Future ===");
        // TODO: Implement here
        // Hint: Future<Integer> future = executor.submit(() -> { Thread.sleep(1000); return 42; });
    }

    /**
     * Exercise 5: Graceful shutdown
     * TODO: Demonstrate proper shutdown: shutdown(), awaitTermination(), shutdownNow().
     *       Show the difference between shutdown() and shutdownNow().
     */
    static void exercise5() throws InterruptedException {
        System.out.println("=== Exercise 5: Graceful Shutdown ===");
        // TODO: Implement here
        // Hint: executor.shutdown(); executor.awaitTermination(5, TimeUnit.SECONDS);
    }
}
