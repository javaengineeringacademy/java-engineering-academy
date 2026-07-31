package academy.javaengineering.oop.bank.project;

/**
 * CheckingAccount - Checking account with overdraft protection.
 * 
 * <p>Overrides withdraw to support overdraft.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class CheckingAccount extends Account {

    private final double overdraftLimit;

    public CheckingAccount(Customer customer, double initialBalance, double overdraftLimit) {
        super(customer, initialBalance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public String getAccountType() {
        return "Checking Account";
    }

    @Override
    public double getInterestRate() {
        return 0.001; // Very low interest
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            return false;
        }
        if (amount > balance + overdraftLimit) {
            System.out.printf("  Exceeds overdraft limit of $%.2f%n", overdraftLimit);
            return false;
        }
        balance -= amount;
        logTransaction(TransactionType.WITHDRAWAL, amount);
        System.out.printf("  Withdrew $%.2f from Checking%n", amount);
        return true;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    @Override
    public String toString() {
        return super.toString() + " (Overdraft: $" + overdraftLimit + ")";
    }
}