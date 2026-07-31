package academy.javaengineering.oop.bank;

/**
 * Represents a financial transaction.
 */
public record Transaction(
    String transactionId,
    TransactionType type,
    Money amount,
    String description,
    java.time.LocalDateTime timestamp
) {
    public Transaction {
        Objects.requireNonNull(transactionId);
        Objects.requireNonNull(type);
        Objects.requireNonNull(amount);
        Objects.requireNonNull(timestamp);
    }

    public static Transaction create(TransactionType type, Money amount, String description) {
        return new Transaction(
            UUID.randomUUID().toString(),
            type,
            amount,
            description,
            LocalDateTime.now()
        );
    }
}