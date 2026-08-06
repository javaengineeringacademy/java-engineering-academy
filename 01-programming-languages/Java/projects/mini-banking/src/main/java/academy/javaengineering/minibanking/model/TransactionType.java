package academy.javaengineering.minibanking.model;

/**
 * Enum representing transaction types in the banking system.
 *
 * <p>Engineering Decision: Using enum instead of String constants.
 * WHY: Enums provide type safety, prevent invalid values, and can have
 * associated behavior. The compiler catches invalid comparisons at compile time.</p>
 *
 * <p>Engineering Decision: Using uppercase names (DEPOSIT, WITHDRAWAL).
 * WHY: Follows Java naming conventions for enum constants and makes
 * code self-documenting when used in switch statements.</p>
 */
public enum TransactionType {
    DEPOSIT("Deposit"),
    WITHDRAWAL("Withdrawal");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the human-readable display name for this transaction type.
     *
     * @return formatted display name
     */
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
