package academy.javaengineering.minibanking.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a bank account in the system.
 *
 * <p>Engineering Decision: Immutable-ish design (no setters).
 * WHY: Prevents accidental state mutation. Balance changes happen only through
 * explicit deposit()/withdraw() methods that enforce business rules and can
 * throw exceptions for invalid operations.</p>
 *
 * <p>Engineering Decision: Using double for balance.
     * WHY: For demonstration purposes, double is sufficient. In production,
     * BigDecimal would be used to avoid floating-point precision issues with
     * financial calculations.</p>
 *
 * <p>Engineering Decision: Using LocalDateTime for timestamp.
 * WHY: LocalDateTime is timezone-agnostic, which is appropriate for a
 * banking system where business logic doesn't depend on timezone conversions.</p>
 *
 * <p>Engineering Topics Demonstrated:
 * - Encapsulation (private fields, controlled access)
 * - Object lifecycle (constructor, toString, equals, hashCode)
 * - Business logic in model (deposit, withdraw methods)</p>
 */
public class Account {

    private final String id;
    private final String owner;
    private double balance;
    private final LocalDateTime createdAt;

    /**
     * Constructs a new Account with zero initial balance.
     *
     * @param id    unique account identifier
     * @param owner account holder's name
     * @throws IllegalArgumentException if id or owner is null/blank
     */
    public Account(String id, String owner) {
        this(id, owner, 0.0);
    }

    /**
     * Constructs a new Account with specified initial balance.
     *
     * @param id      unique account identifier
     * @param owner   account holder's name
     * @param balance initial balance
     * @throws IllegalArgumentException if id/owner is null/blank or balance is negative
     */
    public Account(String id, String owner, double balance) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Account ID cannot be null or blank");
        }
        if (owner == null || owner.isBlank()) {
            throw new IllegalArgumentException("Owner cannot be null or blank");
        }
        if (balance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        this.id = id;
        this.owner = owner;
        this.balance = balance;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Deposits amount into this account.
     *
     * @param amount the amount to deposit (must be positive)
     * @throws academy.javaengineering.minibanking.exception.InvalidAmountException
     *         if amount is zero or negative
     */
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new academy.javaengineering.minibanking.exception.InvalidAmountException(amount);
        }
        this.balance += amount;
    }

    /**
     * Withdraws amount from this account.
     *
     * @param amount the amount to withdraw (must be positive and <= balance)
     * @throws academy.javaengineering.minibanking.exception.InvalidAmountException
     *         if amount is zero or negative
     * @throws academy.javaengineering.minibanking.exception.InsufficientFundsException
     *         if amount exceeds available balance
     */
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new academy.javaengineering.minibanking.exception.InvalidAmountException(amount);
        }
        if (amount > this.balance) {
            throw new academy.javaengineering.minibanking.exception.InsufficientFundsException(
                    this.id, amount, this.balance);
        }
        this.balance -= amount;
    }

    // Getters only - no setters to maintain encapsulation

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Compares accounts by ID only.
     *
     * <p>Engineering Decision: Compare by ID, not all fields.
     * WHY: Two Account objects with same ID represent the same account
     * regardless of other field differences (like transient balance changes).</p>
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id);
    }

    /**
     * Hash code based on ID only, consistent with equals().
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Returns formatted string representation of the account.
     */
    @Override
    public String toString() {
        return String.format("Account{id='%s', owner='%s', balance=%.2f, createdAt=%s}",
                id, owner, balance, createdAt);
    }
}
