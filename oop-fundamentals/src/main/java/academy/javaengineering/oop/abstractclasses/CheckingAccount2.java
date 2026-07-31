package academy.javaengineering.oop.abstractclasses;

/**
 * CheckingAccount - Concrete implementation of abstract BankAccount class.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class CheckingAccount extends BankAccount {

    private final double overdraftLimit;

    public CheckingAccount(String owner, double initialBalance, double overdraftLimit) {
        super(owner, initialBalance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public double getInterestRate() {
        return 0.001; // Very low interest for checking
    }

    @Override
    public String getAccountType() {
        return "Checking Account";
    }

    @Override
    protected void applyInterest() {
        // Override to skip interest for checking accounts
        System.out.println("  No interest applied to checking accounts");
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }
}