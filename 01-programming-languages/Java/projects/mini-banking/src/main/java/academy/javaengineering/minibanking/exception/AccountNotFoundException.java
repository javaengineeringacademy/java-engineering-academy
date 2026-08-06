package academy.javaengineering.minibanking.exception;

/**
 * Exception thrown when attempting to access a non-existent account.
 *
 * <p>Engineering Decision: Unchecked exception.
 * WHY: Account lookup failures are typically programming errors (wrong ID)
 * rather than expected runtime conditions. Unchecked keeps the API clean.</p>
 */
public class AccountNotFoundException extends RuntimeException {

    private final String accountId;

    /**
     * Constructs exception with the missing account ID.
     *
     * @param accountId the ID of the account that was not found
     */
    public AccountNotFoundException(String accountId) {
        super(String.format("Account not found: %s", accountId));
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }
}
