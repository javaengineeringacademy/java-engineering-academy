package academy.javaengineering.oop.bank.project;

/**
 * TransactionType - Enum for transaction types.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public enum TransactionType {
    DEPOSIT("Deposit"),
    WITHDRAWAL("Withdrawal"),
    TRANSFER("Transfer"),
    INTEREST("Interest"),
    FEE("Fee");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}