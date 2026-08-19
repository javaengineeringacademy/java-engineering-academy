package academy.javaengineering.jvm.memorymodel;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Exercise 1: Demonstrate Data Races and Visibility Issues
 *
 * Task: Create programs that demonstrate data races and memory visibility
 * problems, then fix them using volatile and synchronized.
 */
public class Exercise1 {

    private static boolean running = true;
    private static int counter = 0;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Data Race Demonstration ===\n");

        // Task 1: Demonstrate visibility problem
        System.out.println("--- Task 1: Visibility Problem ---");
        // TODO: Start a thread that increments counter in a loop
        // TODO: Read counter from main thread
        // TODO: Observe that main thread may see stale value

        // Task 2: Fix with volatile
        System.out.println("\n--- Task 2: Fix with volatile ---");
        // TODO: Make counter volatile and observe correct visibility

        // Task 3: Demonstrate compound operation race
        System.out.println("\n--- Task 3: Compound Operation Race ---");
        // TODO: Use multiple threads to increment a non-atomic counter
        // TODO: Show that final value is less than expected
        // TODO: Fix using AtomicInteger

        System.out.println("\n[Complete the TODO sections above]");
    }
}
