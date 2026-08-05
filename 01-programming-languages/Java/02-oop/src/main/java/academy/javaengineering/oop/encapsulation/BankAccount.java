package academy.javaengineering.oop.encapsulation;

import java.math.BigDecimal;

/**
 * Demonstrates encapsulation with private fields, getters, and validation.
 *
 * <p>Encapsulation hides internal state and exposes only controlled access.
 * This ensures data integrity and reduces coupling between components.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Private fields prevent direct access</li>
 *   <li>Getters provide read-only access</li>
 *   <li>Setters enforce validation rules</li>
 *   <li>Business logic encapsulated within the class</li>
 * </ul>
 */
public class BankAccount {

    private final String accountId;
    private String ownerName;
    private BigDecimal balance;
    private boolean active;

    public BankAccount(String accountId, String ownerName, BigDecimal initialBalance) {
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("Account ID required");
        }
        if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        this.accountId = accountId;
        this.ownerName = ownerName;
        this.balance = initialBalance;
        this.active = true;
    }

    // Read-only getters
    public String getAccountId() { return accountId; }
    public boolean isActive() { return active; }

    public BigDecimal getBalance() {
        return balance; // BigDecimal is already immutable
    }

    public String getOwnerName() { return ownerName; }

    // Validated setter
    public void setOwnerName(String ownerName) {
        if (ownerName == null || ownerName.isBlank()) {
            throw new IllegalArgumentException("Owner name cannot be blank");
        }
        this.ownerName = ownerName;
    }

    /** Deposits money with validation. */
    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit must be positive");
        }
        if (!active) {
            throw new IllegalStateException("Account is inactive");
        }
        balance = balance.add(amount);
    }

    /** Withdraws money with sufficient funds check. */
    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal must be positive");
        }
        if (!active) {
            throw new IllegalStateException("Account is inactive");
        }
        if (balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        balance = balance.subtract(amount);
    }

    /** Deactivates the account. */
    public void close() {
        this.active = false;
    }

    @Override
    public String toString() {
        return "BankAccount{id='%s', owner='%s', balance=%s, active=%s}".formatted(
                accountId, ownerName, balance, active);
    }
}
