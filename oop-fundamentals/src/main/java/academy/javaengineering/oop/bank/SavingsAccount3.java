package academy.javaengineering.oop.bank;

/**
 * SavingsAccount3 - Savings account with interest.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class SavingsAccount3 extends Account2 implements InterestBearing {

    private final double interestRate;

    public SavingsAccount3(String owner, double initialBalance, double interestRate) {
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

    @Override
    public void applyInterest() {
        double interest = balance * interestRate;
        balance += interest;
        logTransaction(TransactionType2.INTEREST, interest);
        System.out.println("  Applied interest: $" + String.format("%.2f", interest) + 
            " to " + getOwner() + "'s savings");
    }
}