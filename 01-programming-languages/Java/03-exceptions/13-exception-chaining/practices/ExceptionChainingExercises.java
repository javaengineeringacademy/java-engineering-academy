package academy.javaengineering.exceptions.chaining.exercises;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Exception chaining exercises.
 *
 * <p>Complete the methods below. Each method requires you to chain exceptions
 * correctly, translating low-level exceptions into higher-level ones while
 * preserving the cause chain.
 *
 * <p>Google Java Style: no comments, clean formatting.
 */
public class ExceptionChainingExercises {

    // ──────────────────────────────────────────────
    // Exception definitions
    // ──────────────────────────────────────────────

    public static class ServiceException extends Exception {
        public ServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class DataException extends RuntimeException {
        public DataException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class NetworkException extends RuntimeException {
        public NetworkException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // ──────────────────────────────────────────────
    // Exercise 1: Chain exceptions
    // ──────────────────────────────────────────────

    /**
     * TODO: Complete this method.
     * 1. Call riskyOperation() which throws IOException
     * 2. Catch the IOException
     * 3. Wrap it in a ServiceException with the original cause
     * 4. Throw the ServiceException
     */
    public static void exercise1() throws ServiceException {
        // TODO: implement
    }

    private static void riskyOperation() throws IOException {
        throw new IOException("Something went wrong");
    }

    // ──────────────────────────────────────────────
    // Exercise 2: Exception translation
    // ──────────────────────────────────────────────

    /**
     * TODO: Complete this method.
     * 1. Call databaseOperation() which throws IOException
     * 2. Catch the IOException
     * 3. Translate it to a DataException with the original cause
     * 4. Throw the DataException
     */
    public static void exercise2() {
        // TODO: implement
    }

    private static void databaseOperation() throws IOException {
        throw new IOException("Database connection failed");
    }

    // ──────────────────────────────────────────────
    // Exercise 3: Root cause analysis
    // ──────────────────────────────────────────────

    /**
     * TODO: Complete this method.
     * 1. Traverse the cause chain of the given exception
     * 2. Find the root cause (the deepest exception in the chain)
     * 3. Return the root cause
     *
     * @param e the exception to analyze
     * @return the root cause exception
     */
    public static Throwable getRootCause(Throwable e) {
        // TODO: implement
        return null;
    }

    // ──────────────────────────────────────────────
    // Exercise 4: Find specific cause in chain
    // ──────────────────────────────────────────────

    /**
     * TODO: Complete this method.
     * 1. Traverse the cause chain of the given exception
     * 2. Find an exception of the specified type
     * 3. Return the found exception, or null if not found
     *
     * @param e    the exception to analyze
     * @param type the exception type to find
     * @param <T>  the exception type
     * @return the found exception, or null
     */
    public static <T extends Throwable> T findCauseInChain(
            Throwable e, Class<T> type) {
        // TODO: implement
        return null;
    }

    // ──────────────────────────────────────────────
    // Exercise 5: Multi-layer exception translation
    // ──────────────────────────────────────────────

    /**
     * TODO: Complete this method.
     * 1. Create a chain: IOException -> DataException -> ServiceException
     * 2. Catch the ServiceException
     * 3. Print the root cause
     */
    public static void exercise5() {
        // TODO: implement
    }

    // ──────────────────────────────────────────────
    // Exercise 6: Exception logging
    // ──────────────────────────────────────────────

    /**
     * TODO: Complete this method.
     * 1. Traverse the cause chain
     * 2. Print each cause with its depth and class name
     *
     * @param e       the exception to log
     * @param context the context string
     */
    public static void logExceptionChain(Throwable e, String context) {
        // TODO: implement
    }

    // ──────────────────────────────────────────────
    // Main
    // ──────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("=== Exercise 1 ===");
        try {
            exercise1();
        } catch (ServiceException e) {
            System.out.println("Caught: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
        }

        System.out.println("\n=== Exercise 2 ===");
        exercise2();

        System.out.println("\n=== Exercise 3 ===");
        // TODO: test getRootCause

        System.out.println("\n=== Exercise 4 ===");
        // TODO: test findCauseInChain

        System.out.println("\n=== Exercise 5 ===");
        exercise5();

        System.out.println("\n=== Exercise 6 ===");
        // TODO: test logExceptionChain
    }
}
