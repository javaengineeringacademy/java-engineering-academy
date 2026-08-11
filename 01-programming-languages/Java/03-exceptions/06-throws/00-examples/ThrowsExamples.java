/**
 * Demonstrates throws declaration patterns including checked exception contracts,
 * multiple exceptions in throws, exception translation, and method chaining.
 *
 * <p><b>Complexity:</b> O(1) per operation unless noted.</p>
 * <p><b>Thread-safety:</b> Not thread-safe — uses static mutable state.</p>
 * <p><b>Key characteristics:</b> Covers throws syntax with checked exceptions,
 * multiple exception declarations, exception translation across layers,
 * and method chain propagation patterns.</p>
 */
package academy.javaengineering.exceptions.throwsdeclaration.examples;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ThrowsExamples {

    // ──────────────────────────────────────────────
    // 1. Checked exception — must declare throws
    // ──────────────────────────────────────────────

    /**
     * Demonstrates that methods using checked exceptions must declare throws.
     * The caller is forced to handle or propagate the exception.
     */
    static void checkedExceptionDeclaration() {
        System.out.println("=== Checked exception — must declare throws ===");
        try {
            String content = readFirstLine("config.txt");
            System.out.println("Content: " + content);
        } catch (IOException e) {
            System.out.println("Caught: " + e.getClass().getSimpleName() + " — " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * This method MUST declare throws IOException — the compiler requires it.
     * The caller must either catch or propagate.
     */
    static String readFirstLine(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        try {
            return reader.readLine();
        } finally {
            reader.close();
        }
    }

    // ──────────────────────────────────────────────
    // 2. Multiple exceptions in throws
    // ──────────────────────────────────────────────

    /**
     * Demonstrates declaring multiple checked exceptions in a single throws clause.
     * Each exception type can be caught separately by the caller.
     */
    static void multipleExceptionsInThrows() {
        System.out.println("=== Multiple exceptions in throws ===");
        try {
            transferFunds("account-1", "account-2", 500.0);
        } catch (InsufficientFundsException e) {
            System.out.println("Insufficient funds: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Network error: " + e.getMessage());
        }
        System.out.println();
    }

    static void transferFunds(String from, String to, double amount)
            throws InsufficientFundsException, IOException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (amount > 10000) {
            throw new InsufficientFundsException("Exceeds daily limit: " + amount);
        }
        // Simulate network call
        if (Math.random() > 0.5) {
            throw new IOException("Connection timeout to bank service");
        }
        System.out.println("Transferred $" + amount + " from " + from + " to " + to);
    }

    static class InsufficientFundsException extends Exception {
        InsufficientFundsException(String message) {
            super(message);
        }
    }

    // ──────────────────────────────────────────────
    // 3. Exception translation pattern
    // ──────────────────────────────────────────────

    /**
     * Demonstrates catching a low-level checked exception and rethrowing
     * as a domain-specific unchecked exception. This hides implementation
     * details while preserving the root cause.
     */
    static void exceptionTranslation() {
        System.out.println("=== Exception translation ===");
        try {
            OrderDTO order = getOrderDetails(42);
            System.out.println("Order: " + order);
        } catch (OrderNotFoundException e) {
            System.out.println("Not found: " + e.getMessage());
        } catch (DataAccessException e) {
            System.out.println("Data error: " + e.getMessage());
            System.out.println("Root cause: " + e.getCause().getClass().getSimpleName());
        }
        System.out.println();
    }

    static OrderDTO getOrderDetails(long orderId) {
        try {
            return queryDatabase(orderId);
        } catch (SQLException e) {
            // Translate low-level exception to domain exception
            throw new DataAccessException("Failed to query order: " + orderId, e);
        }
    }

    static OrderDTO queryDatabase(long orderId) throws SQLException {
        // Simulate database error
        throw new SQLException("Table 'orders' not found");
    }

    static class OrderDTO {
        private final long id;
        OrderDTO(long id) { this.id = id; }
        @Override
        public String toString() { return "OrderDTO{id=" + id + "}"; }
    }

    static class OrderNotFoundException extends RuntimeException {
        OrderNotFoundException(long id) {
            super("Order not found: " + id);
        }
    }

    static class DataAccessException extends RuntimeException {
        DataAccessException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // ──────────────────────────────────────────────
    // 4. Method chain with throws propagation
    // ──────────────────────────────────────────────

    /**
     * Demonstrates how exceptions propagate through a chain of method calls.
     * Each layer can catch, translate, or rethrow the exception.
     */
    static void methodChainWithThrows() {
        System.out.println("=== Method chain with throws ===");
        try {
            String result = controllerLayer("user-123");
            System.out.println("Result: " + result);
        } catch (ServiceException e) {
            System.out.println("Service error: " + e.getMessage());
            System.out.println("Root cause: " + e.getCause().getMessage());
        }
        System.out.println();
    }

    static String controllerLayer(String userId) throws ServiceException {
        try {
            return serviceLayer(userId);
        } catch (DataAccessException e) {
            // Translate at controller boundary
            throw new ServiceException("Failed to process request for user: " + userId, e);
        }
    }

    static String serviceLayer(String userId) throws DataAccessException {
        try {
            return repositoryLayer(userId);
        } catch (SQLException e) {
            // Translate at service boundary
            throw new DataAccessException("Database query failed for user: " + userId, e);
        }
    }

    static String repositoryLayer(String userId) throws SQLException {
        // Simulate database error
        throw new SQLException("Connection refused to database");
    }

    static class ServiceException extends RuntimeException {
        ServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // ──────────────────────────────────────────────
    // 5. throws with try-with-resources
    // ──────────────────────────────────────────────

    /**
     * Demonstrates throws declaration combined with try-with-resources
     * for automatic resource cleanup.
     */
    static void throwsWithTryWithResources() {
        System.out.println("=== throws with try-with-resources ===");
        try {
            String content = readFileContent("data.txt");
            System.out.println("Content: " + content);
        } catch (IOException e) {
            System.out.println("Read failed: " + e.getMessage());
        }
        System.out.println();
    }

    static String readFileContent(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString().trim();
        }
    }

    // ──────────────────────────────────────────────
    // 6. throws in interface contracts
    // ──────────────────────────────────────────────

    /**
     * Demonstrates how throws is used in interfaces to define contracts
     * that all implementing classes must follow.
     */
    static void interfaceContract() {
        System.out.println("=== throws in interface contracts ===");
        DataSource source = new FileDataSource("config.txt");
        try {
            String data = source.read();
            System.out.println("Data: " + data);
        } catch (IOException e) {
            System.out.println("Read failed: " + e.getMessage());
        }
        System.out.println();
    }

    interface DataSource {
        String read() throws IOException;
    }

    static class FileDataSource implements DataSource {
        private final String path;
        FileDataSource(String path) { this.path = path; }

        @Override
        public String read() throws IOException {
            throw new IOException("File not found: " + path);
        }
    }

    // ──────────────────────────────────────────────
    // main
    // ──────────────────────────────────────────────

    public static void main(String[] args) {
        checkedExceptionDeclaration();
        multipleExceptionsInThrows();
        exceptionTranslation();
        methodChainWithThrows();
        throwsWithTryWithResources();
        interfaceContract();
    }
}
