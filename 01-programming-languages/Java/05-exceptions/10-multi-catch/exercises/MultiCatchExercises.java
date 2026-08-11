package academy.javaengineering.exceptions.multicatch;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Exercises for multi-catch exception handling.
 *
 * <p>Complete each method according to the instructions.
 * Do not change method signatures.
 */
public final class MultiCatchExercises {

    private MultiCatchExercises() {}

    // ---------------------------------------------------------------
    // Exercise 1 — Basic Multi-Catch
    // ---------------------------------------------------------------

    /**
     * Replace the two separate catch blocks with a single multi-catch
     * that handles both IOException and SQLException.
     *
     * @param operation the operation to attempt
     * @return true if successful, false if either exception is caught
     */
    public static boolean exercise1(RiskyOperation operation) {
        try {
            operation.execute();
            return true;
        } catch (IOException e) {
            return false;
        } catch (SQLException e) {
            return false;
        }
    }

    // ---------------------------------------------------------------
    // Exercise 2 — Multi-Catch with Rethrow
    // ---------------------------------------------------------------

    /**
     * Use multi-catch to catch IOException or SQLException and wrap
     * them in a RuntimeException with the original as the cause.
     *
     * @param operation the operation to attempt
     */
    public static void exercise2(RiskyOperation operation) {
        try {
            operation.execute();
        } catch (IOException e) {
            throw new RuntimeException("I/O failure", e);
        } catch (SQLException e) {
            throw new RuntimeException("SQL failure", e);
        }
    }

    // ---------------------------------------------------------------
    // Exercise 3 — Three Exception Types
    // ---------------------------------------------------------------

    /**
     * Use multi-catch to handle IOException, SQLException, and
     * IllegalArgumentException in a single block that returns
     * the exception's message.
     *
     * @param operation the operation to attempt
     * @return the exception message, or null if no exception
     */
    public static String exercise3(RiskyOperationWithIllegal operation) {
        try {
            operation.execute();
            return null;
        } catch (IOException | SQLException | IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    // ---------------------------------------------------------------
    // Exercise 4 — Multi-Catch with Type Check
    // ---------------------------------------------------------------

    /**
     * Use multi-catch for IOException and SQLException.
     * If the caught exception is a SQLException, return its SQL state.
     * Otherwise, return "IO_ERROR".
     *
     * @param operation the operation to attempt
     * @return the SQL state or "IO_ERROR"
     */
    public static String exercise4(RiskyOperation operation) {
        try {
            operation.execute();
            return "OK";
        } catch (IOException | SQLException e) {
            // TODO: implement with instanceof check
            return null;
        }
    }

    // ---------------------------------------------------------------
    // Exercise 5 — Multi-Catch with Logging
    // ---------------------------------------------------------------

    /**
     * Use multi-catch for IOException and SecurityException.
     * Return the exception's class simple name (e.g., "IOException").
     *
     * @param operation the operation to attempt
     * @return the exception class name, or "None" if no exception
     */
    public static String exercise5(SecurityAwareOperation operation) {
        try {
            operation.execute();
            return "None";
        } catch (IOException | SecurityException e) {
            // TODO: return e.getClass().getSimpleName()
            return null;
        }
    }

    // ---------------------------------------------------------------
    // Functional Interfaces
    // ---------------------------------------------------------------

    @FunctionalInterface
    public interface RiskyOperation {
        void execute() throws IOException, SQLException;
    }

    @FunctionalInterface
    public interface RiskyOperationWithIllegal {
        void execute() throws IOException, SQLException, IllegalArgumentException;
    }

    @FunctionalInterface
    public interface SecurityAwareOperation {
        void execute() throws IOException, SecurityException;
    }
}
