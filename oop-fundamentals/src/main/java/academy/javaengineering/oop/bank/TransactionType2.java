package academy.javaengineering.oop.bank;

/**
 * TransactionType2 - Enum for transaction types.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public enum TransactionType2 {
    DEPOSIT("Deposit"),
    WITHDRAWAL("Withdrawal"),
    TRANSFER("Transfer"),
    INTEREST("Interest"),
    FEE("Fee");

    private final String description;

    TransactionType2(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}