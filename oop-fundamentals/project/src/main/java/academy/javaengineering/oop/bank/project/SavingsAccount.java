package academy.javaengineering.oop.bank.project;

/**
 * SavingsAccount - Savings account with interest.
 * 
 * <p>Implements InterestBearing for interest calculation.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class SavingsAccount extends Account implements InterestBearing {

    private final double interestRate;

    public SavingsAccount(Customer customer, double initialBalance, double interestRate) {
        super(customer, initialBalance);
        this.interestRate = interestRate;
    }

    @Override
    public String getAccountType() {
        return "Savings Account";
    }

    @Override
    public double getInterestRate() {
        return interestRate;
    }

    @Override
    public void applyInterest() {
        double interest = balance * interestRate;
        balance += interest;
        logTransaction(TransactionType.INTEREST, interest);
        System.out.printf("  Applied interest: $%.2f to %s%n",
            interest, getCustomer().getFullName());
    }
}