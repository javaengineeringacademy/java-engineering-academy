package academy.javaengineering.exceptions.multicatch;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Focused examples of multi-catch patterns in real-world scenarios.
 */
public final class MultiCatchExample {

    private static final Logger LOG = Logger.getLogger(MultiCatchExample.class.getName());

    private MultiCatchExample() {}

    // ---------------------------------------------------------------
    // Example 1 — Identical Handling
    // ---------------------------------------------------------------

    /**
     * Both exceptions get the same log-and-rethrow treatment.
     */
    public static void logAndRethrow() {
        try {
            riskyOperation();
        } catch (IOException | SQLException e) {
            LOG.log(Level.WARNING, "Operation failed", e);
            throw new ServiceException("Wrapped failure", e);
        }
    }

    // ---------------------------------------------------------------
    // Example 2 — Fallback Value
    // ---------------------------------------------------------------

    /**
     * Returns a default when either exception occurs.
     */
    public static String loadWithFallback(String path) {
        try {
            return readFile(path);
        } catch (IOException | SecurityException e) {
            LOG.warning("Using default for " + path);
            return "default-value";
        }
    }

    // ---------------------------------------------------------------
    // Example 3 — Resource Cleanup
    // ---------------------------------------------------------------

    /**
     * Closes a resource, ignoring specific exceptions.
     */
    public static void closeQuietly(java.io.Closeable resource) {
        try {
            resource.close();
        } catch (IOException | IllegalArgumentException e) {
            LOG.log(Level.FINE, "Ignored close error", e);
        }
    }

    // ---------------------------------------------------------------
    // Example 4 — Multi-Catch with Pattern Matching
    // ---------------------------------------------------------------

    /**
     * Uses instanceof inside multi-catch for occasional type-specific logic.
     */
    public static void mixedHandling() {
        try {
            riskyOperation();
        } catch (IOException | SQLException e) {
            if (e instanceof SQLException se) {
                LOG.warning("SQL state: " + se.getSQLState());
            }
            LOG.warning("Error message: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // Example 5 — Retry Loop
    // ---------------------------------------------------------------

    /**
     * Retries up to maxAttempts, catching the same exceptions each time.
     */
    public static void retryWithBackoff(int maxAttempts) {
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                riskyOperation();
                return;
            } catch (IOException | InterruptedException e) {
                LOG.warning("Attempt " + attempt + " failed");
                if (attempt == maxAttempts) {
                    throw new RuntimeException("Exhausted retries", e);
                }
                sleep(attempt * 100L);
            }
        }
    }

    // ---------------------------------------------------------------
    // Example 6 — Conditional Recovery
    // ---------------------------------------------------------------

    /**
     * Different logging based on exception type, but shared recovery.
     */
    public static void conditionalRecovery() {
        try {
            riskyOperation();
        } catch (IOException | SQLException e) {
            if (e instanceof IOException ioe) {
                LOG.warning("I/O issue: " + ioe.getMessage());
            } else {
                LOG.warning("SQL issue: " + ((SQLException) e).getSQLState());
            }
            useDefaults();
        }
    }

    // ---------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------

    private static void riskyOperation() throws IOException, SQLException {
        throw new IOException("Simulated");
    }

    private static String readFile(String path) throws IOException {
        return new String(java.nio.file.Files.readAllBytes(
                java.nio.file.Path.of(path)));
    }

    private static void useDefaults() {
        LOG.info("Using defaults");
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    // ---------------------------------------------------------------
    // Main
    // ---------------------------------------------------------------

    public static void main(String[] args) {
        System.out.println("=== Multi-Catch Examples ===\n");

        System.out.println("--- Log and Rethrow ---");
        try {
            logAndRethrow();
        } catch (ServiceException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        System.out.println("\n--- Fallback ---");
        System.out.println("Result: " + loadWithFallback("missing.txt"));

        System.out.println("\n--- Close Quietly ---");
        closeQuietly(new java.io ByteArrayInputStream(new byte[0]));

        System.out.println("\n--- Mixed Handling ---");
        mixedHandling();

        System.out.println("\n--- Conditional Recovery ---");
        conditionalRecovery();

        System.out.println("\n=== Done ===");
    }
}
