package academy.javaengineering.exceptionhandling;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Demonstrates try-with-resources and AutoCloseable interface.
 *
 * <p>Key concepts covered:
 * <ul>
 *   <li>AutoCloseable interface</li>
 *   <li>Try-with-resources syntax</li>
 *   <li>Multiple resources</li>
 *   <li>Suppressed exceptions</li>
 * </ul>
 */
public class TryWithResources {

    /**
     * Custom AutoCloseable resource implementation.
     */
    public static class DatabaseConnection implements AutoCloseable {

        private final String url;
        private boolean connected;

        /**
         * Constructs a DatabaseConnection.
         *
         * @param url the database URL
         */
        public DatabaseConnection(String url) {
            this.url = url;
            this.connected = true;
            System.out.println("Connected to: " + url);
        }

        /**
         * Executes a query (simulated).
         *
         * @param query the SQL query
         */
        public void executeQuery(String query) {
            if (!connected) {
                throw new IllegalStateException("Connection is closed");
            }
            System.out.println("Executing: " + query);
        }

        /**
         * Closes the database connection.
         */
        @Override
        public void close() {
            connected = false;
            System.out.println("Connection closed: " + url);
        }
    }

    /**
     * Custom AutoCloseable that may throw exception on close.
     */
    public static class Transaction implements AutoCloseable {

        private final String id;

        /**
         * Constructs a Transaction.
         *
         * @param id the transaction ID
         */
        public Transaction(String id) {
            this.id = id;
            System.out.println("Transaction started: " + id);
        }

        /**
         * Commits the transaction (simulated).
         */
        public void commit() {
            System.out.println("Transaction committed: " + id);
        }

        /**
         * Closes and rolls back if not committed.
         */
        @Override
        public void close() {
            System.out.println("Transaction closed (rollback): " + id);
        }
    }

    /**
     * Demonstrates try-with-resources usage.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Basic try-with-resources
        basicTryWithResources();

        // Multiple resources
        multipleResources();

        // Custom AutoCloseable
        customAutoCloseable();

        // Suppressed exceptions
        suppressedExceptionsDemo();
    }

    /**
     * Basic try-with-resources example.
     */
    public static void basicTryWithResources() {
        System.out.println("=== Basic Try-With-Resources ===");
        try (var connection = new DatabaseConnection("jdbc:mysql://localhost/db")) {
            connection.executeQuery("SELECT * FROM users");
        }
        // Expected output:
        // === Basic Try-With-Resources ===
        // Connected to: jdbc:mysql://localhost/db
        // Executing: SELECT * FROM users
        // Connection closed: jdbc:mysql://localhost/db
    }

    /**
     * Multiple resources in try-with-resources.
     */
    public static void multipleResources() {
        System.out.println("\n=== Multiple Resources ===");
        try (var db = new DatabaseConnection("jdbc:mysql://localhost/db");
             var transaction = new Transaction("TXN-001")) {
            db.executeQuery("INSERT INTO users VALUES (1, 'John')");
            transaction.commit();
        }
        // Expected output:
        // === Multiple Resources ===
        // Connected to: jdbc:mysql://localhost/db
        // Transaction started: TXN-001
        // Executing: INSERT INTO users VALUES (1, 'John')
        // Transaction committed: TXN-001
        // Transaction closed (rollback): TXN-001
        // Connection closed: jdbc:mysql://localhost/db
    }

    /**
     * Custom AutoCloseable implementation example.
     */
    public static void customAutoCloseable() {
        System.out.println("\n=== Custom AutoCloseable ===");
        try (var resource = new CustomResource("MyResource")) {
            resource.doWork();
        }
        // Expected output:
        // === Custom AutoCloseable ===
        // Resource MyResource opened
        // Working with resource
        // Resource MyResource closed
    }

    /**
     * Demonstrates suppressed exceptions.
     */
    public static void suppressedExceptionsDemo() {
        System.out.println("\n=== Suppressed Exceptions ===");
        try (var resource = new ExceptionOnCloseResource()) {
            throw new RuntimeException("Main exception");
        } catch (RuntimeException e) {
            System.out.println("Main: " + e.getMessage());
            for (Throwable suppressed : e.getSuppressed()) {
                System.out.println("Suppressed: " + suppressed.getMessage());
            }
        }
        // Expected output:
        // === Suppressed Exceptions ===
        // Resource opened
        // Main: Main exception
        // Suppressed: Close exception
        // Resource closed (with exception)
    }

    /**
     * Simple custom resource implementation.
     */
    public static class CustomResource implements AutoCloseable {

        private final String name;

        /**
         * Constructs a CustomResource.
         *
         * @param name the resource name
         */
        public CustomResource(String name) {
            this.name = name;
            System.out.println("Resource " + name + " opened");
        }

        /**
         * Performs work with the resource.
         */
        public void doWork() {
            System.out.println("Working with resource");
        }

        /**
         * Closes the resource.
         */
        @Override
        public void close() {
            System.out.println("Resource " + name + " closed");
        }
    }

    /**
     * Resource that throws exception on close.
     */
    public static class ExceptionOnCloseResource implements AutoCloseable {

        /**
         * Constructs an ExceptionOnCloseResource.
         */
        public ExceptionOnCloseResource() {
            System.out.println("Resource opened");
        }

        /**
         * Closes the resource with an exception.
         */
        @Override
        public void close() {
            System.out.println("Resource closed (with exception)");
            throw new RuntimeException("Close exception");
        }
    }
}
