import java.math.BigDecimal;
import java.util.Objects;

public final class Solution2 {
    public static void main(String[] args) {
        BankAccount account = new BankAccount("ACC-001", new BigDecimal("1000.00"));
        BankAccount afterDeposit = account.deposit(new BigDecimal("500.00"));
        BankAccount afterWithdraw = afterDeposit.withdraw(new BigDecimal("200.00"));

        System.out.println("Original: " + account);
        System.out.println("After deposit: " + afterDeposit);
        System.out.println("After withdraw: " + afterWithdraw);

        try {
            afterWithdraw.withdraw(new BigDecimal("9999.00"));
        } catch (IllegalArgumentException e) {
            System.out.println("Overdraw prevented: " + e.getMessage());
        }
    }
}

final class BankAccount {
    private final String accountId;
    private final BigDecimal balance;

    public BankAccount(String accountId, BigDecimal balance) {
        this.accountId = accountId;
        this.balance = balance;
    }

    public String getAccountId() { return accountId; }
    public BigDecimal getBalance() { return balance; }

    public BankAccount deposit(BigDecimal amount) {
        return new BankAccount(this.accountId, this.balance.add(amount));
    }

    public BankAccount withdraw(BigDecimal amount) {
        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        return new BankAccount(this.accountId, this.balance.subtract(amount));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BankAccount that)) return false;
        return Objects.equals(accountId, that.accountId) &&
               Objects.equals(balance, that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, balance);
    }

    @Override
    public String toString() {
        return "BankAccount{accountId='" + accountId + "', balance=" + balance + "}";
    }
}
