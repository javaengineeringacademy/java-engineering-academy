package academy.javaengineering.exceptions.exception.exercises;

import java.io.IOException;

/**
 * Exercises for practicing Exception handling.
 *
 * <p>Complete each method according to the instructions.
 * See solutions/ExceptionSolutions.java for reference answers.</p>
 */
public class ExceptionExercises {

    // ============================================================
    // Exercise 1: Custom checked exception
    // ============================================================

    /**
     * Create a checked exception called {@code NotFoundException} that:
     * <ul>
     *   <li>Extends Exception</li>
     *   <li>Has a constructor that takes a String message</li>
     *   <li>Has a constructor that takes a String message and Throwable cause</li>
     *   <li>Has a method {@code getResourceId()} that returns a String</li>
     * </ul>
     *
     * <p>Then implement this method to throw NotFoundException when
     * the resource is not found.</p>
     *
     * @param resourceId the ID to look up
     * @return the resource data
     * @throws NotFoundException if the resource does not exist
     */
    static String findResource(String resourceId) throws Exception {
        // TODO: implement
        // throw new NotFoundException if resourceId is null or empty
        throw new UnsupportedOperationException("Exercise 1 not implemented");
    }

    // ============================================================
    // Exercise 2: Exception chaining
    // ============================================================

    /**
     * This method simulates a database operation that throws SQLException.
     * Wrap it in a custom {@code DataAccessException} and chain the cause.
     *
     * @param query the SQL query
     * @return the result
     * @throws Exception if the operation fails
     */
    static String executeQuery(String query) throws Exception {
        // TODO: wrap the inner operation in DataAccessException
        // Preserve the original SQLException as the cause
        throw new UnsupportedOperationException("Exercise 2 not implemented");
    }

    // ============================================================
    // Exercise 3: Multi-catch
    // ============================================================

    /**
     * Parse a string as an integer. Handle both NumberFormatException
     * and a custom ValidationException using multi-catch.
     *
     * <p>Return the parsed integer on success, or -1 on any failure.</p>
     *
     * @param input the string to parse
     * @return the parsed integer, or -1 on failure
     */
    static int safeParseInt(String input) {
        // TODO: implement using multi-catch
        throw new UnsupportedOperationException("Exercise 3 not implemented");
    }

    // ============================================================
    // Exercise 4: Exception in a loop
    // ============================================================

    /**
     * Process a list of strings. Each string should be converted to uppercase.
     * If any string is null, skip it and continue processing the rest.
     * Return a list of processed (uppercased) strings.
     *
     * @param items the input strings
     * @return the processed strings
     */
    static java.util.List<String> processItems(java.util.List<String> items) {
        // TODO: implement
        // Catch IllegalArgumentException for null items and skip them
        throw new UnsupportedOperationException("Exercise 4 not implemented");
    }

    // ============================================================
    // Exercise 5: fillInStackTrace override
    // ============================================================

    /**
     * Create a lightweight exception that overrides fillInStackTrace()
     * to return {@code this} without filling the stack trace.
     *
     * <p>This is useful in performance-critical code where stack traces
     * are not needed.</p>
     *
     * @param message the error message
     * @return a FastException instance
     */
    static Exception createFastException(String message) {
        // TODO: implement
        throw new UnsupportedOperationException("Exercise 5 not implemented");
    }

    // ============================================================
    // Exercise 6: try-with-resources
    // ============================================================

    /**
     * Implement a method that uses try-with-resources to read from a
     * Closeable resource. The method should:
     * <ul>
     *   <li>Open the resource in try-with-resources</li>
     *   <li>Read from it</li>
     *   <li>Return the result</li>
     *   <li>If the resource throws an exception during close, add it
     *       as a suppressed exception</li>
     * </ul>
     *
     * <p>Implement a mock Closeable that throws during close to test this.</p>
     *
     * @return a string result from the resource
     * @throws IOException if reading fails
     */
    static String readWithResource() throws IOException {
        // TODO: implement
        throw new UnsupportedOperationException("Exercise 6 not implemented");
    }

    // ============================================================
    // Exercise 7: Cause chain depth
    // ============================================================

    /**
     * Create a chain of exceptions 3 levels deep:
     * <pre>
     *   ExceptionA (wrapper)
     *     -> ExceptionB (middle)
     *       -> ExceptionC (root cause)
     * </pre>
     *
     * <p>Return the outermost exception. Verify that getCause() traverses
     * the full chain.</p>
     *
     * @return the chained exception
     */
    static Exception createCauseChain() {
        // TODO: implement
        throw new UnsupportedOperationException("Exercise 7 not implemented");
    }

    public static void main(String[] args) {
        System.out.println("Exercises are not yet implemented.");
        System.out.println("Implement each method and run again to verify.");
    }
}
