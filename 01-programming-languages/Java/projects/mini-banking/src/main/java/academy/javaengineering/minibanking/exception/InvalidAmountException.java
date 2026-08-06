package academy.javaengineering.minibanking.exception;

/**
 * Exception thrown when a transaction amount violates business rules.
 *
 * <p>Engineering Decision: Separate from InsufficientFundsException.
 * WHY: Invalid amount (negative/zero) is different from insufficient funds.
 * Separate exceptions enable different handling strategies and clearer error messages.</p>
 */
public class InvalidAmountException extends RuntimeException {

    private final double amount;

    /**
     * Constructs exception with the invalid amount.
     *
     * @param amount the amount that failed validation
     */
    public InvalidAmountException(double amount) {
        super(String.format("Invalid amount: %.2f. Amount must be positive.", amount));
        this.amount = amount;
    }

    /**
     * Constructs exception with custom message.
     *
     * @param amount  the invalid amount
     * @param message custom error message
     */
    public InvalidAmountException(double amount, String message) {
        super(message);
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}
