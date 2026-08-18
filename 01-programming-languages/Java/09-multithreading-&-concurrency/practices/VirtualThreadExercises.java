package academy.javaengineering.concurrency.practices;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class VirtualThreadExercises {

    public static void main(String[] args) throws InterruptedException {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }

    /**
     * Exercise 1: Create virtual thread
     * TODO: Create a virtual thread using Thread.ofVirtual().start().
     *       Compare it with a platform thread by printing thread details.
     */
    static void exercise1() throws InterruptedException {
        System.out.println("=== Exercise 1: Virtual Thread Basics ===");
        // TODO: Implement here
        // Hint: Thread.startVirtualThread(() -> System.out.println("Hello from virtual thread"));
    }

    /**
     * Exercise 2: Virtual thread with ExecutorService
     * TODO: Use Executors.newVirtualThreadPerTaskExecutor() to create a virtual thread executor.
     *       Submit 100 tasks and observe how virtual threads handle concurrency.
     */
    static void exercise2() throws InterruptedException {
        System.out.println("=== Exercise 2: Virtual Thread Executor ===");
        // TODO: Implement here
        // Hint: ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    }

    /**
     * Exercise 3: Virtual thread pinning
     * TODO: Demonstrate virtual thread pinning by using synchronized block.
     *       Virtual threads pin when blocking inside synchronized.
     *       Fix it by using ReentrantLock instead.
     */
    static void exercise3() throws InterruptedException {
        System.out.println("=== Exercise 3: Virtual Thread Pinning ===");
        // TODO: Implement here
        // Hint: synchronized blocks cause pinning, ReentrantLock does not
    }

    /**
     * Exercise 4: Structured concurrency (preview)
     * TODO: Use StructuredTaskScope to run two tasks concurrently.
     *       Both tasks return results, and you combine them.
     *       Note: Requires --enable-preview flag in Java 21+.
     */
    static void exercise4() throws Exception {
        System.out.println("=== Exercise 4: Structured Concurrency ===");
        // TODO: Implement here
        // Hint: try (var scope = new StructuredTaskScope.ShutdownOnFailure()) { ... }
    }

    /**
     * Exercise 5: Virtual thread with I/O
     * TODO: Create 1000 virtual threads that each perform blocking I/O.
     *       Compare memory usage with platform threads doing the same.
     *       Virtual threads should use significantly less memory.
     */
    static void exercise5() throws InterruptedException {
        System.out.println("=== Exercise 5: Virtual Thread I/O ===");
        // TODO: Implement here
        // Hint: Thread.sleep() simulates blocking I/O, virtual threads handle this efficiently
    }
}
