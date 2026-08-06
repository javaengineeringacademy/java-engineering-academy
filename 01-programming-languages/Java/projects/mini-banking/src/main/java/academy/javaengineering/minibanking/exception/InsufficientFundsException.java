package academy.javaengineering.minibanking.exception;

/**
 * Exception thrown when a withdrawal exceeds available account balance.
 *
 * <p>Engineering Decision: Checked vs Unchecked exception.
 * WHY: This is an unchecked exception because insufficient funds represents
 * a business rule violation that callers shouldn't be forced to handle.
 * It's a programming error to attempt withdrawal without checking balance.</p>
 *
 * <p>Engineering Decision: Extending RuntimeException.
 * WHY: RuntimeException subclasses don't require try-catch blocks,
 * keeping calling code cleaner while still allowing error handling when needed.</p>
 */
public class InsufficientFundsException extends RuntimeException {

    private final String accountId;
    private final double requestedAmount;
    private final double availableBalance;

    /**
     * Constructs exception with detailed financial context.
     *
     * @param accountId       the account with insufficient funds
     * @param requestedAmount the amount attempted to withdraw
     * @param availableBalance the actual available balance
     */
    public InsufficientFundsException(String accountId, double requestedAmount, double availableBalance) {
        super(String.format("Insufficient funds for account %s: requested %.2f, available %.2f",
                accountId, requestedAmount, availableBalance));
        this.accountId = accountId;
        this.requestedAmount = requestedAmount;
        this.availableBalance = availableBalance;
    }

    public String getAccountId() {
        return accountId;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }
}
