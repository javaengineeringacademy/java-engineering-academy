package academy.javaengineering.oop.bank;

import java.time.LocalDateTime;

/**
 * Transaction2 - Immutable record for transaction history.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public record Transaction2(TransactionType2 type, double amount, LocalDateTime timestamp) {

    public Transaction2(TransactionType2 type, double amount) {
        this(type, amount, LocalDateTime.now());
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: $%.2f", timestamp, type.getDescription(), amount);
    }
}