package academy.javaengineering.modern.multicatch;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Multi-catch with real-world patterns.
 */
public class MultiCatchPatterns {

    private static final Logger logger = Logger.getLogger(MultiCatchPatterns.class.getName());

    public static void main(String[] args) {
        // Database operations with multi-catch
        System.out.println("=== Database Operations ===");
        try {
            executeDatabaseOperation();
        } catch (SQLException | IOException e) {
            logger.severe("Database operation failed: " + e.getMessage());
        }

        // File operations with multi-catch
        System.out.println("\n=== File Operations ===");
        try {
            processFile("test.txt");
        } catch (IOException | SecurityException e) {
            System.out.println("File processing error: " + e.getMessage());
        }

        // Network operations with multi-catch
        System.out.println("\n=== Network Operations ===");
        try {
            connectToServer("localhost", 8080);
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Network error: " + e.getMessage());
        }

        // Validation with multi-catch
        System.out.println("\n=== Validation ===");
        String[] emails = {"user@example.com", "invalid-email", "another@example.com"};
        for (String email : emails) {
            try {
                validateEmail(email);
                System.out.println("Valid: " + email);
            } catch (IllegalArgumentException | SecurityException e) {
                System.out.println("Invalid: " + email + " - " + e.getMessage());
            }
        }
    }

    static void executeDatabaseOperation() throws SQLException, IOException {
        // Simulate database operation
        if (Math.random() < 0.5) {
            throw new SQLException("Connection failed");
        } else {
            throw new IOException("IO error during query");
        }
    }

    static void processFile(String filename) throws IOException, SecurityException {
        // Simulate file processing
        if (filename == null) {
            throw new IOException("Filename is null");
        }
        if (filename.contains("forbidden")) {
            throw new SecurityException("Access denied");
        }
        System.out.println("Processing file: " + filename);
    }

    static void connectToServer(String host, int port) throws IOException, IllegalArgumentException {
        // Simulate network connection
        if (host == null || host.isEmpty()) {
            throw new IllegalArgumentException("Invalid host");
        }
        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("Invalid port: " + port);
        }
        if (Math.random() < 0.5) {
            throw new IOException("Connection refused");
        }
        System.out.println("Connected to " + host + ":" + port);
    }

    static void validateEmail(String email) throws IllegalArgumentException, SecurityException {
        // Simulate email validation
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is empty");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (email.contains("blocked")) {
            throw new SecurityException("Email is blocked");
        }
    }
}
