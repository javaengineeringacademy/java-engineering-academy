package academy.javaengineering.oop.bank.project;

import java.time.LocalDateTime;

/**
 * Transaction - Immutable record for transaction history.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public record Transaction(TransactionType type, double amount, LocalDateTime timestamp) {

    @Override
    public String toString() {
        return String.format("[%s] %s: $%.2f",
            timestamp.toLocalTime(),
            type.getDescription(),
            amount);
    }
}