package academy.javaengineering.exceptions.suppressed.exercises;

import java.io.Closeable;
import java.io.IOException;

/**
 * Exercises for suppressed exception handling.
 * Complete each method according to the instructions.
 */
public class SuppressedExceptionsExercises {

    public static void main(String[] args) {
        System.out.println("=== Exercise 1: TWR Suppressed ===");
        exercise1();
        System.out.println("\n=== Exercise 2: Manual Suppressed ===");
        exercise2();
        System.out.println("\n=== Exercise 3: Parallel Aggregation ===");
        exercise3();
    }

    /**
     * Exercise 1: Use try-with-resources with a resource whose close()
     * throws an exception. The try block also throws. Print the primary
     * and all suppressed exceptions.
     */
    static void exercise1() {
        // TODO: Implement this method
        // Hint: try (FailingCloseResource r = new FailingCloseResource()) { ... }
        // Then catch and print primary + suppressed
    }

    /**
     * Exercise 2: Manually add suppressed exceptions.
     * Create a primary exception, add two suppressed exceptions,
     * then print all three.
     */
    static void exercise2() {
        // TODO: Implement this method
        // Hint: primary.addSuppressed(suppressed)
    }

    /**
     * Exercise 3: Run 5 tasks in a loop. Some throw exceptions.
     * Aggregate all exceptions using addSuppressed, with the first
     * as primary. Print the final result.
     */
    static void exercise3() {
        // TODO: Implement this method
        // Hint: Loop and catch, using addSuppressed to aggregate
    }

    // --- Helper Types ---

    static class FailingCloseResource implements Closeable {
        @Override
        public void close() {
            throw new RuntimeException("close() failed");
        }
    }

    static class IOException extends Exception {
        IOException(String message) {
            super(message);
        }
    }
}
