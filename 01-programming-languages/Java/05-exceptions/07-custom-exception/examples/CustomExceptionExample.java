package academy.javaengineering.exceptions.custom;

import java.util.HashMap;
import java.util.Map;

/**
 * Practical examples of custom exceptions in a banking domain.
 */
public class CustomExceptionExample {

    // ================================================================
    // Domain Model
    // ================================================================

    public static class Account {
        private final String id;
        private double balance;
        private boolean frozen;

        public Account(String id, double initialBalance) {
            this.id = id;
            this.balance = initialBalance;
            this.frozen = false;
        }

        public String getId() {
            return id;
        }

        public double getBalance() {
            return balance;
        }

        public boolean isFrozen() {
            return frozen;
        }

        public void freeze() {
            this.frozen = true;
        }

        public void debit(double amount) {
            this.balance -= amount;
        }

        public void credit(double amount) {
            this.balance += amount;
        }
    }

    // ================================================================
    // Custom Exceptions
    // ================================================================

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

    public static class AccountFrozenException extends RuntimeException {
        private static final long serialVersionUID = 1L;
        private final String accountId;

        public AccountFrozenException(String accountId) {
            super("Account " + accountId + " is frozen");
            this.accountId = accountId;
        }

        public String getAccountId() {
            return accountId;
        }
    }

    public static class ValidationException extends RuntimeException {
        private static final long serialVersionUID = 1L;
        private final Map<String, String> errors;

        public ValidationException(Map<String, String> errors) {
            super("Validation failed: " + errors);
            this.errors = Map.copyOf(errors);
        }

        public ValidationException(String field, String message) {
            this(Map.of(field, message));
        }

        public Map<String, String> getErrors() {
            return errors;
        }
    }

    // ================================================================
    // Service Layer
    // ================================================================

    public static class BankService {

        public void transfer(Account from, Account to, double amount) {
            if (from.isFrozen()) {
                throw new AccountFrozenException(from.getId());
            }
            if (from.getBalance() < amount) {
                throw new InsufficientFundsException(
                        from.getId(), amount, from.getBalance());
            }
            from.debit(amount);
            to.credit(amount);
            System.out.printf("Transferred $%.2f: %s -> %s%n",
                              amount, from.getId(), to.getId());
        }

        public Account createAccount(String id, double initialBalance) {
            if (id == null || id.isBlank()) {
                throw new ValidationException("id", "Account ID is required");
            }
            if (initialBalance < 0) {
                throw new ValidationException(
                    "balance", "Initial balance cannot be negative");
            }
            return new Account(id, initialBalance);
        }
    }

    // ================================================================
    // Demo
    // ================================================================

    public static void main(String[] args) {
        System.out.println("=== Custom Exception Examples ===\n");

        BankService bank = new BankService();
        Account alice = bank.createAccount("ALICE", 1000.0);
        Account bob = bank.createAccount("BOB", 500.0);

        // Successful transfer
        System.out.println("--- Successful Transfer ---");
        bank.transfer(alice, bob, 200.0);
        System.out.printf("Alice: $%.2f, Bob: $%.2f%n",
                          alice.getBalance(), bob.getBalance());

        // Insufficient funds
        System.out.println("\n--- Insufficient Funds ---");
        try {
            bank.transfer(alice, bob, 5000.0);
        } catch (InsufficientFundsException e) {
            System.out.printf("Error: %s%n", e.getMessage());
            System.out.printf("Deficit: $%.2f%n", e.getDeficit());
        }

        // Account frozen
        System.out.println("\n--- Account Frozen ---");
        alice.freeze();
        try {
            bank.transfer(alice, bob, 100.0);
        } catch (AccountFrozenException e) {
            System.out.printf("Error: %s%n", e.getMessage());
            System.out.printf("Frozen account: %s%n", e.getAccountId());
        }

        // Validation failure
        System.out.println("\n--- Validation Failure ---");
        try {
            bank.createAccount("", -100.0);
        } catch (ValidationException e) {
            System.out.printf("Error: %s%n", e.getMessage());
            e.getErrors().forEach((field, msg) ->
                System.out.printf("  %s: %s%n", field, msg));
        }
    }
}
