package academy.javaengineering.exceptions.multicatch;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Demonstrates multi-catch syntax, effectively final variables,
 * bytecode behavior, and production patterns.
 */
public final class MultiCatch {

    private static final Logger LOG = Logger.getLogger(MultiCatch.class.getName());

    private MultiCatch() {}

    // ---------------------------------------------------------------
    // 1. Basic Multi-Catch Syntax
    // ---------------------------------------------------------------

    /**
     * Catches two checked exceptions with identical handling.
     */
    public static void basicMultiCatch() {
        try {
            riskyOperation();
        } catch (IOException | SQLException e) {
            LOG.log(Level.WARNING, "I/O or SQL error occurred", e);
        }
    }

    // ---------------------------------------------------------------
    // 2. Effectively Final Variable
    // ---------------------------------------------------------------

    /**
     * The exception parameter in multi-catch is effectively final.
     * Reassignment is a compile error.
     */
    public static void effectivelyFinalDemo() {
        try {
            riskyOperation();
        } catch (IOException | SQLException e) {
            // e = new IOException("nope"); // COMPILE ERROR
            String message = e.getMessage();
            LOG.warning("Message: " + message);

            // Can pass to lambdas (effectively final allows capture)
            Runnable reporter = () -> LOG.warning("Captured: " + e);
            reporter.run();
        }
    }

    // ---------------------------------------------------------------
    // 3. Multi-Catch vs Multiple Catch Blocks
    // ---------------------------------------------------------------

    /** Uses duplicate catch blocks (pre-Java 7 style). */
    public static void withoutMultiCatch() {
        try {
            riskyOperation();
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Handled IOException", e);
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Handled SQLException", e);
        }
    }

    /** Uses multi-catch (Java 7+ style). */
    public static void withMultiCatch() {
        try {
            riskyOperation();
        } catch (IOException | SQLException e) {
            LOG.log(Level.WARNING, "Handled exception", e);
        }
    }

    // ---------------------------------------------------------------
    // 4. Three or More Exception Types
    // ---------------------------------------------------------------

    /**
     * Multi-catch supports any number of types separated by pipes.
     */
    public static void threeTypes() {
        try {
            veryRiskyOperation();
        } catch (IOException | SQLException | IllegalArgumentException e) {
            LOG.log(Level.SEVERE, "Operation failed", e);
        }
    }

    // ---------------------------------------------------------------
    // 5. Multi-Catch with try-with-resources
    // ---------------------------------------------------------------

    /**
     * Combines multi-catch with try-with-resources for clean resource
     * management and exception handling.
     */
    public static void withTryWithResources() {
        try (var reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(System.in))) {
            String line = reader.readLine();
            if (line == null) {
                throw new IOException("Empty input");
            }
        } catch (IOException | SecurityException e) {
            LOG.log(Level.WARNING, "Read failed", e);
        }
    }

    // ---------------------------------------------------------------
    // 6. Exception Translation Pattern
    // ---------------------------------------------------------------

    /**
     * Wraps low-level exceptions into a domain-specific exception.
     */
    public static void exceptionTranslation() {
        try {
            riskyOperation();
        } catch (IOException | SQLException e) {
            throw new ServiceException("Translation failed", e);
        }
    }

    // ---------------------------------------------------------------
    // 7. Multi-Catch with instanceof Inside
    // ---------------------------------------------------------------

    /**
     * Sometimes you need type-specific logic inside multi-catch.
     * Use instanceof pattern matching (Java 16+).
     */
    public static void withTypeCheck() {
        try {
            riskyOperation();
        } catch (IOException | SQLException e) {
            if (e instanceof SQLException se) {
                LOG.warning("SQL state: " + se.getSQLState());
            } else {
                LOG.warning("I/O error: " + e.getMessage());
            }
        }
    }

    // ---------------------------------------------------------------
    // 8. Catching Unchecked Exceptions
    // ---------------------------------------------------------------

    /**
     * Multi-catch works with unchecked exceptions too.
     */
    public static void uncheckedMultiCatch() {
        try {
            parseNumber("not_a_number");
        } catch (NumberFormatException | ArithmeticException e) {
            LOG.log(Level.WARNING, "Calculation error", e);
        }
    }

    // ---------------------------------------------------------------
    // 9. Multi-Catch in Private Helper
    // ---------------------------------------------------------------

    private static void safeClose(java.io.Closeable resource) {
        if (resource == null) {
            return;
        }
        try {
            resource.close();
        } catch (IOException | IllegalArgumentException e) {
            LOG.log(Level.FINE, "Close failed", e);
        }
    }

    // ---------------------------------------------------------------
    // 10. Production Pattern — Retry with Multi-Catch
    // ---------------------------------------------------------------

    /**
     * Retries an operation that may fail with specific exceptions.
     */
    public static void retryPattern(int maxAttempts) {
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                riskyOperation();
                LOG.info("Succeeded on attempt " + attempt);
                return;
            } catch (IOException | SQLException e) {
                LOG.warning("Attempt " + attempt + " failed: " + e.getMessage());
                if (attempt == maxAttempts) {
                    throw new ServiceException("All attempts exhausted", e);
                }
                sleepQuietly(attempt * 100L);
            }
        }
    }

    // ---------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------

    private static void riskyOperation() throws IOException, SQLException {
        if (Math.random() > 0.5) {
            throw new IOException("Simulated I/O failure");
        }
        throw new SQLException("Simulated SQL failure");
    }

    private static void veryRiskyOperation()
            throws IOException, SQLException {
        throw new IOException("Simulated failure");
    }

    private static int parseNumber(String s) {
        return Integer.parseInt(s);
    }

    private static void sleepQuietly(long millis) {
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
        System.out.println("=== Multi-Catch Demo ===");

        System.out.println("\n--- Basic Multi-Catch ---");
        basicMultiCatch();

        System.out.println("\n--- Effectively Final ---");
        effectivelyFinalDemo();

        System.out.println("\n--- Without Multi-Catch ---");
        withoutMultiCatch();

        System.out.println("\n--- With Multi-Catch ---");
        withMultiCatch();

        System.out.println("\n--- Three Types ---");
        threeTypes();

        System.out.println("\n--- Try-With-Resources ---");
        withTryWithResources();

        System.out.println("\n--- Exception Translation ---");
        try {
            exceptionTranslation();
        } catch (ServiceException e) {
            System.out.println("Caught ServiceException: " + e.getMessage());
        }

        System.out.println("\n--- Type Check Inside ---");
        withTypeCheck();

        System.out.println("\n--- Unchecked Multi-Catch ---");
        uncheckedMultiCatch();

        System.out.println("\n--- Retry Pattern ---");
        retryPattern(2);

        System.out.println("\n=== Done ===");
    }
}
