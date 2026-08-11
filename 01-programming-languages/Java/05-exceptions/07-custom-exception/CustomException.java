package academy.javaengineering.exceptions.custom;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Demonstrates creating and using custom exceptions in Java.
 *
 * <p>This class defines several custom exception types and shows how they
 * integrate into domain logic.</p>
 */
public final class CustomException {

    private CustomException() {}

    // ================================================================
    // Custom Checked Exception
    // ================================================================

    /**
     * Thrown when a payment is declined by the payment processor.
     *
     * <p>This is a checked exception because callers must handle
     * payment failures (retry, notify user, etc.).</p>
     */
    public static class PaymentDeclinedException extends Exception {

        private static final long serialVersionUID = 1L;

        private final String transactionId;
        private final String reason;

        public PaymentDeclinedException(String transactionId, String reason) {
            super("Payment declined for transaction " + transactionId
                  + ": " + reason);
            this.transactionId = transactionId;
            this.reason = reason;
        }

        public PaymentDeclinedException(
                String transactionId, String reason, Throwable cause) {
            super("Payment declined for transaction " + transactionId
                  + ": " + reason, cause);
            this.transactionId = transactionId;
            this.reason = reason;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public String getReason() {
            return reason;
        }
    }

    // ================================================================
    // Custom Unchecked Exceptions
    // ================================================================

    /**
     * Thrown when an account does not have sufficient funds.
     *
     * <p>This is unchecked because the caller may not need to handle it,
     * and it represents an expected business condition.</p>
     */
    public static class InsufficientFundsException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        private final String accountId;
        private final double requested;
        private final double available;

        public InsufficientFundsException(
                String accountId, double requested, double available) {
            super(String.format(
                "Account %s: requested $%.2f, available $%.2f",
                accountId, requested, available));
            this.accountId = accountId;
            this.requested = requested;
            this.available = available;
        }

        public String getAccountId() {
            return accountId;
        }

        public double getRequested() {
            return requested;
        }

        public double getAvailable() {
            return available;
        }

        public double getDeficit() {
            return requested - available;
        }
    }

    /**
     * Thrown when input validation fails.
     *
     * <p>Carries a map of field-level errors for structured error handling.</p>
     */
    public static class ValidationException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        private final Map<String, String> fieldErrors;

        public ValidationException(Map<String, String> fieldErrors) {
            super("Validation failed: " + fieldErrors);
            this.fieldErrors = Map.copyOf(fieldErrors);
        }

        public ValidationException(String field, String message) {
            this(Map.of(field, message));
        }

        public Map<String, String> getFieldErrors() {
            return fieldErrors;
        }

        public boolean hasFieldError(String field) {
            return fieldErrors.containsKey(field);
        }
    }

    // ================================================================
    // Abstract Base Exception
    // ================================================================

    /**
     * Base class for all domain-specific exceptions.
     *
     * <p>Provides common fields like errorCode and timestamp.</p>
     */
    public static abstract class DomainException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        private final String errorCode;
        private final long timestamp;

        protected DomainException(String errorCode, String message) {
            super(message);
            this.errorCode = errorCode;
            this.timestamp = System.currentTimeMillis();
        }

        protected DomainException(
                String errorCode, String message, Throwable cause) {
            super(message, cause);
            this.errorCode = errorCode;
            this.timestamp = System.currentTimeMillis();
        }

        public String getErrorCode() {
            return errorCode;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    /**
     * Thrown when an account is frozen and cannot perform transactions.
     */
    public static class AccountFrozenException extends DomainException {

        private static final long serialVersionUID = 1L;

        private final String accountId;

        public AccountFrozenException(String accountId) {
            super("ACCT_FROZEN",
                  "Account " + accountId + " is frozen");
            this.accountId = accountId;
        }

        public String getAccountId() {
            return accountId;
        }
    }

    // ================================================================
    // Exception Factory
    // ================================================================

    /**
     * Factory for creating common domain exceptions.
     *
     * <p>Provides readable static methods instead of verbose constructors.</p>
     */
    public static final class Exceptions {

        private Exceptions() {}

        public static InsufficientFundsException insufficientFunds(
                String accountId, double requested, double available) {
            return new InsufficientFundsException(
                    accountId, requested, available);
        }

        public static ValidationException validationFailed(
                Map<String, String> errors) {
            return new ValidationException(errors);
        }

        public static ValidationException validationFailed(
                String field, String message) {
            return new ValidationException(field, message);
        }

        public static AccountFrozenException accountFrozen(String accountId) {
            return new AccountFrozenException(accountId);
        }
    }

    // ================================================================
    // Demo Methods
    // ================================================================

    /**
     * Simulates a bank transfer that may fail with insufficient funds.
     */
    public static void transfer(
            String fromAccountId, String toAccountId, double amount) {
        double balance = 1000.0; // mock balance
        if (amount > balance) {
            throw Exceptions.insufficientFunds(fromAccountId, amount, balance);
        }
        System.out.printf("Transferred $%.2f from %s to %s%n",
                          amount, fromAccountId, toAccountId);
    }

    /**
     * Simulates user creation with validation.
     */
    public static void createUser(String name, String email) {
        Map<String, String> errors = new HashMap<>();
        if (name == null || name.isBlank()) {
            errors.put("name", "Name is required");
        }
        if (email == null || !email.contains("@")) {
            errors.put("email", "Must be a valid email address");
        }
        if (!errors.isEmpty()) {
            throw Exceptions.validationFailed(errors);
        }
        System.out.printf("Created user: %s <%s>%n", name, email);
    }

    /**
     * Simulates a payment that may be declined.
     */
    public static void processPayment(String transactionId, double amount)
            throws PaymentDeclinedException {
        if (amount > 10000) {
            throw new PaymentDeclinedException(
                    transactionId, "Amount exceeds daily limit");
        }
        System.out.printf("Payment processed: %s ($%.2f)%n",
                          transactionId, amount);
    }

    /**
     * Demonstrates exception hierarchy with catch blocks.
     */
    public static void demonstrateHierarchy() {
        try {
            throw Exceptions.accountFrozen("ACC-001");
        } catch (DomainException e) {
            System.out.printf("Domain error [%s]: %s (at %d)%n",
                              e.getErrorCode(), e.getMessage(),
                              e.getTimestamp());
        }
    }

    /**
     * Main entry point for demonstration.
     */
    public static void main(String[] args) {
        System.out.println("=== Custom Exception Demo ===\n");

        // 1. Insufficient funds
        System.out.println("--- Insufficient Funds ---");
        try {
            transfer("ACC-001", "ACC-002", 5000.0);
        } catch (InsufficientFundsException e) {
            System.out.printf("Error: %s%n", e.getMessage());
            System.out.printf("Deficit: $%.2f%n", e.getDeficit());
        }

        // 2. Validation failure
        System.out.println("\n--- Validation Failure ---");
        try {
            createUser("", "not-an-email");
        } catch (ValidationException e) {
            System.out.printf("Error: %s%n", e.getMessage());
            e.getFieldErrors().forEach((field, msg) ->
                System.out.printf("  %s: %s%n", field, msg));
        }

        // 3. Account frozen
        System.out.println("\n--- Account Frozen ---");
        demonstrateHierarchy();

        // 4. Successful operations
        System.out.println("\n--- Successful Operations ---");
        transfer("ACC-001", "ACC-002", 100.0);
        createUser("Alice", "alice@example.com");
    }
}
