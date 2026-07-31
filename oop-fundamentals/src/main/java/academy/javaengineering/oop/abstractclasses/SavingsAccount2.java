package academy.javaengineering.oop.abstractclasses;

/**
 * SavingsAccount - Concrete implementation of abstract BankAccount class.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class SavingsAccount extends BankAccount {

    private final double interestRate;

    public SavingsAccount(String owner, double initialBalance, double interestRate) {
        super(owner, initialBalance);
        this.interestRate = interestRate;
    }

    @Override
    public double getInterestRate() {
        return interestRate;
    }

    @Override
    public String getAccountType() {
        return "Savings Account";
    }
}