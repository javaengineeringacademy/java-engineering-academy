package academy.javaengineering.exceptions.multicatch;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Solutions for multi-catch exception handling exercises.
 */
public final class MultiCatchSolutions {

    private MultiCatchSolutions() {}

    // ---------------------------------------------------------------
    // Solution 1 — Basic Multi-Catch
    // ---------------------------------------------------------------

    /**
     * Single multi-catch block handles both exception types.
     */
    public static boolean solution1(RiskyOperation operation) {
        try {
            operation.execute();
            return true;
        } catch (IOException | SQLException e) {
            return false;
        }
    }

    // ---------------------------------------------------------------
    // Solution 2 — Multi-Catch with Rethrow
    // ---------------------------------------------------------------

    /**
     * Wraps caught exceptions in a RuntimeException.
     */
    public static void solution2(RiskyOperation operation) {
        try {
            operation.execute();
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Operation failed", e);
        }
    }

    // ---------------------------------------------------------------
    // Solution 3 — Three Exception Types
    // ---------------------------------------------------------------

    /**
     * Handles three exception types and returns the message.
     */
    public static String solution3(RiskyOperationWithIllegal operation) {
        try {
            operation.execute();
            return null;
        } catch (IOException | SQLException | IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    // ---------------------------------------------------------------
    // Solution 4 — Multi-Catch with Type Check
    // ---------------------------------------------------------------

    /**
     * Uses instanceof pattern matching inside multi-catch.
     */
    public static String solution4(RiskyOperation operation) {
        try {
            operation.execute();
            return "OK";
        } catch (IOException | SQLException e) {
            if (e instanceof SQLException se) {
                return se.getSQLState();
            }
            return "IO_ERROR";
        }
    }

    // ---------------------------------------------------------------
    // Solution 5 — Multi-Catch with Logging
    // ---------------------------------------------------------------

    /**
     * Returns the simple class name of the caught exception.
     */
    public static String solution5(SecurityAwareOperation operation) {
        try {
            operation.execute();
            return "None";
        } catch (IOException | SecurityException e) {
            return e.getClass().getSimpleName();
        }
    }

    // ---------------------------------------------------------------
    // Solution 6 — Bonus: Multi-Catch in a Retry Loop
    // ---------------------------------------------------------------

    /**
     * Retries the operation, catching IOException or InterruptedException.
     */
    public static void solution6(RetryableOperation operation, int maxAttempts) {
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                operation.execute();
                return;
            } catch (IOException | InterruptedException e) {
                if (attempt == maxAttempts) {
                    throw new RuntimeException("Failed after " + maxAttempts, e);
                }
                sleep(attempt * 50L);
            }
        }
    }

    // ---------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    // ---------------------------------------------------------------
    // Functional Interfaces (mirror of exercises)
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

    @FunctionalInterface
    public interface RetryableOperation {
        void execute() throws IOException, InterruptedException;
    }

    // ---------------------------------------------------------------
    // Main — verify solutions
    // ---------------------------------------------------------------

    public static void main(String[] args) {
        System.out.println("=== Multi-Catch Solutions ===\n");

        // Solution 1
        RiskyOperation op1 = () -> { throw new IOException("test"); };
        System.out.println("Solution 1: " + solution1(op1));

        // Solution 2
        RiskyOperation op2 = () -> { throw new SQLException("test"); };
        try {
            solution2(op2);
        } catch (RuntimeException e) {
            System.out.println("Solution 2: caught " + e.getCause().getClass().getSimpleName());
        }

        // Solution 3
        RiskyOperationWithIllegal op3 = () -> { throw new IllegalArgumentException("bad"); };
        System.out.println("Solution 3: " + solution3(op3));

        // Solution 4
        RiskyOperation op4 = () -> { throw new SQLException("err", "08001"); };
        System.out.println("Solution 4: " + solution4(op4));

        // Solution 5
        SecurityAwareOperation op5 = () -> { throw new SecurityException("denied"); };
        System.out.println("Solution 5: " + solution5(op5));

        // Solution 6
        RetryableOperation op6 = () -> { throw new IOException("timeout"); };
        try {
            solution6(op6, 2);
        } catch (RuntimeException e) {
            System.out.println("Solution 6: " + e.getMessage());
        }

        System.out.println("\n=== Done ===");
    }
}
