package academy.javaengineering.oop.bank;

/**
 * CheckingAccount3 - Checking account with overdraft protection.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class CheckingAccount3 extends Account2 {

    private final double overdraftLimit;

    public CheckingAccount3(String owner, double initialBalance, double overdraftLimit) {
        super(owner, initialBalance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public double getInterestRate() {
        return 0.001; // Very low interest
    }

    @Override
    public String getAccountType() {
        return "Checking Account";
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount > balance + overdraftLimit) {
            System.out.println("  Exceeds overdraft limit of $" + overdraftLimit);
            return false;
        }
        balance -= amount;
        logTransaction(TransactionType2.WITHDRAWAL, amount);
        System.out.println("  Withdrew $" + amount + " from Checking");
        return true;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }
}