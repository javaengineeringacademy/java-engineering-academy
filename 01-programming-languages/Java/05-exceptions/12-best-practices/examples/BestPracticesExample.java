package academy.javaengineering.exceptions.bestpractices.examples;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Runnable examples demonstrating each of the 10 exception best practices.
 *
 * <p>Run main() to see each rule in action.
 */
public class BestPracticesExample {

    public static void main(String[] args) {
        System.out.println("=== Exception Best Practices Examples ===\n");

        demonstrateCatchSpecific();
        demonstrateDontSwallow();
        demonstrateChainExceptions();
        demonstrateTryWithResources();
        demonstrateDontUseForControlFlow();
        demonstrateIncludeContext();
        demonstrateCustomDomainExceptions();

        System.out.println("\nAll demonstrations complete.");
    }

    // Rule 1: Catch specific types
    static void demonstrateCatchSpecific() {
        System.out.println("--- Rule 1: Catch Specific Types ---");
        try {
            String content = readConfigFile("/nonexistent/config.properties");
            System.out.println("Content: " + content);
        } catch (ConfigNotFoundException e) {
            System.out.println("Caught specific exception: " + e.getMessage());
        }
        System.out.println();
    }

    // Rule 2: Don't swallow exceptions
    static void demonstrateDontSwallow() {
        System.out.println("--- Rule 2: Don't Swallow Exceptions ---");
        try {
            sendAlert("user@example.com", "System restart scheduled");
        } catch (AlertException e) {
            System.out.println("Exception propagated and logged: " + e.getMessage());
        }
        System.out.println();
    }

    // Rule 3: Chain exceptions
    static void demonstrateChainExceptions() {
        System.out.println("--- Rule 3: Chain Exceptions ---");
        try {
            parseConfiguration("badly formatted json");
        } catch (ConfigException e) {
            System.out.println("Exception message: " + e.getMessage());
            System.out.println("Cause preserved: " + (e.getCause() != null));
        }
        System.out.println();
    }

    // Rule 4: try-with-resources (conceptual)
    static void demonstrateTryWithResources() {
        System.out.println("--- Rule 4: try-with-resources ---");
        System.out.println("try-with-resources ensures AutoCloseable resources are");
        System.out.println("closed even if an exception occurs.");
        System.out.println("Example pattern:");
        System.out.println("  try (var is = Files.newInputStream(path)) { ... }");
        System.out.println("  // 'is' is guaranteed closed here\n");
    }

    // Rule 6: Don't use for control flow
    static void demonstrateDontUseForControlFlow() {
        System.out.println("--- Rule 6: Don't Use Exceptions for Control Flow ---");
        Map<String, String> config = Map.of("host", "localhost", "port", "8080");

        String host = config.getOrDefault("host", "default-host");
        String missing = config.getOrDefault("timeout", "30");
        System.out.println("host = " + host + " (found via getOrDefault)");
        System.out.println("timeout = " + missing + " (default via getOrDefault)");
        System.out.println();
    }

    // Rule 7: Include context in messages
    static void demonstrateIncludeContext() {
        System.out.println("--- Rule 7: Include Context in Messages ---");
        try {
            validateTransferAmount("ACC-123", "ACC-456", -50.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Descriptive message: " + e.getMessage());
        }
        System.out.println();
    }

    // Rule 9: Custom domain exceptions
    static void demonstrateCustomDomainExceptions() {
        System.out.println("--- Rule 9: Custom Domain Exceptions ---");
        try {
            withdrawFunds("ACC-001", 1000.00, 500.00);
        } catch (InsufficientFundsException e) {
            System.out.println("Domain exception caught:");
            System.out.println("  Error code: " + e.getErrorCode());
            System.out.println("  Message: " + e.getMessage());
        }
        System.out.println();
    }

    // =====================================================================
    // Helper methods
    // =====================================================================

    static String readConfigFile(String path) {
        if (!Files.exists(Path.of(path))) {
            throw new ConfigNotFoundException("Config file not found: " + path);
        }
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            throw new ConfigException("Failed to read config: " + path, e);
        }
    }

    static void sendAlert(String recipient, String message) {
        System.out.println("[LOG] Sending alert to " + recipient + ": " + message);
        try {
            throw new IOException("SMTP server unavailable");
        } catch (IOException e) {
            System.err.println("[LOG] Alert failed: " + e.getMessage());
            throw new AlertException("Could not send alert to " + recipient, e);
        }
    }

    static Map<String, String> parseConfiguration(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new ConfigException(
                "Configuration is empty or null", new IllegalArgumentException("empty input"));
        }
        if (!raw.contains(":")) {
            throw new ConfigException(
                "Configuration is not in key:value format: " + raw,
                new IllegalArgumentException("invalid format"));
        }
        return Map.of("parsed", raw);
    }

    static void validateTransferAmount(String from, String to, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                String.format("Transfer amount must be positive, got %.2f from %s to %s",
                    amount, from, to));
        }
        System.out.println("Transfer validated successfully");
    }

    static void withdrawFunds(String accountId, double balance, double amount) {
        if (amount > balance) {
            throw new InsufficientFundsException(accountId, balance, amount);
        }
        System.out.printf("Withdrew %.2f from %s (remaining: %.2f)%n",
            amount, accountId, balance - amount);
    }

    // =====================================================================
    // Exception classes
    // =====================================================================

    static class ConfigNotFoundException extends RuntimeException {
        ConfigNotFoundException(String message) { super(message); }
    }

    static class ConfigException extends RuntimeException {
        ConfigException(String message, Throwable cause) { super(message, cause); }
    }

    static class AlertException extends RuntimeException {
        AlertException(String message, Throwable cause) { super(message, cause); }
    }

    static class InsufficientFundsException extends RuntimeException {
        private final String errorCode;

        InsufficientFundsException(String accountId, double balance, double amount) {
            super(String.format("Insufficient funds in %s: balance=%.2f, requested=%.2f",
                accountId, balance, amount));
            this.errorCode = "ERR_INSUFFICIENT_FUNDS";
        }

        String getErrorCode() { return errorCode; }
    }
}
