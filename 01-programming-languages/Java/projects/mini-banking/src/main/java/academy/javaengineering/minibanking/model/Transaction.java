package academy.javaengineering.minibanking.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Immutable record of a financial transaction.
 *
 * <p>Engineering Decision: Using class instead of Java record.
 * WHY: Records are immutable and final by design, which is perfect for
 * transactions. However, we're using a class with final fields to demonstrate
 * the traditional approach while maintaining immutability benefits.</p>
 *
 * <p>Engineering Decision: Immutability.
 * WHY: Transactions are historical facts that should never change after creation.
 * Immutability ensures audit trail integrity and thread safety.</p>
 *
 * <p>Engineering Topics Demonstrated:
 * - Encapsulation with final fields
 * - Value objects (compared by all fields)
 * - Factory method pattern (static create method)</p>
 */
public final class Transaction {

    private final String id;
    private final String accountId;
    private final TransactionType type;
    private final double amount;
    private final LocalDateTime timestamp;

    /**
     * Private constructor - use factory method instead.
     */
    private Transaction(String id, String accountId, TransactionType type, double amount, LocalDateTime timestamp) {
        this.id = id;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    /**
     * Factory method to create a new Transaction.
     *
     * <p>Engineering Decision: Factory method over public constructor.
     * WHY: Provides clearer intent, can return different implementations,
     * and allows adding validation or caching without changing call sites.</p>
     *
     * @param id        unique transaction identifier
     * @param accountId the account this transaction belongs to
     * @param type      transaction type (DEPOSIT or WITHDRAWAL)
     * @param amount    transaction amount
     * @return new Transaction instance
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public static Transaction create(String id, String accountId, TransactionType type, double amount) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Transaction ID cannot be null or blank");
        }
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("Account ID cannot be null or blank");
        }
        if (type == null) {
            throw new IllegalArgumentException("Transaction type cannot be null");
        }
        if (amount <= 0) {
            throw new academy.javaengineering.minibanking.exception.InvalidAmountException(amount);
        }
        return new Transaction(id, accountId, type, amount, LocalDateTime.now());
    }

    // All fields are final - no setters needed for immutability

    public String getId() {
        return id;
    }

    public String getAccountId() {
        return accountId;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Compares transactions by all fields.
     *
     * <p>Engineering Decision: Value equality for transactions.
     * WHY: Two transactions are equal if all their properties match,
     * since transactions represent distinct historical facts.</p>
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Double.compare(that.amount, amount) == 0 &&
                Objects.equals(id, that.id) &&
                Objects.equals(accountId, that.accountId) &&
                type == that.type &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, type, amount, timestamp);
    }

    @Override
    public String toString() {
        return String.format("Transaction{id='%s', accountId='%s', type=%s, amount=%.2f, timestamp=%s}",
                id, accountId, type, amount, timestamp);
    }
}
