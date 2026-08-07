import java.math.BigDecimal;
import java.util.Objects;

public final class Exercise2 {
    public static void main(String[] args) {
        BankAccount account = new BankAccount("ACC-001", new BigDecimal("1000.00"));
        BankAccount afterDeposit = account.deposit(new BigDecimal("500.00"));
        BankAccount afterWithdraw = afterDeposit.withdraw(new BigDecimal("200.00"));

        System.out.println("Original: " + account);
        System.out.println("After deposit: " + afterDeposit);
        System.out.println("After withdraw: " + afterWithdraw);

        // Try to overdraw
        try {
            afterWithdraw.withdraw(new BigDecimal("9999.00"));
        } catch (IllegalArgumentException e) {
            System.out.println("Overdraw prevented: " + e.getMessage());
        }
    }
}

/*
 * TODO: Implement the immutable BankAccount class below.
 *
 * Requirements:
 * - All fields must be private and final
 * - deposit() returns a NEW BankAccount with increased balance
 * - withdraw() returns a NEW BankAccount with decreased balance
 * - withdraw() throws IllegalArgumentException if insufficient funds
 * - Proper equals(), hashCode(), toString()
 */
final class BankAccount {
    private final String accountId;
    private final BigDecimal balance;

    // TODO: Constructor

    // TODO: Getters

    // TODO: deposit method

    // TODO: withdraw method

    // TODO: equals, hashCode, toString
}
