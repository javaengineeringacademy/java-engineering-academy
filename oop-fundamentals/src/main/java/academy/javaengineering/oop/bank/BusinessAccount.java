package academy.javaengineering.oop.bank;

/**
 * BusinessAccount - Business account with transaction fees.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class BusinessAccount extends Account2 implements InterestBearing {

    private final double interestRate;
    private static final double TRANSACTION_FEE = 2.50;

    public BusinessAccount(String owner, double initialBalance, double interestRate) {
        super(owner, initialBalance);
        this.interestRate = interestRate;
    }

    @Override
    public double getInterestRate() {
        return interestRate;
    }

    @Override
    public String getAccountType() {
        return "Business Account";
    }

    @Override
    public boolean withdraw(double amount) {
        double totalAmount = amount + TRANSACTION_FEE;
        if (totalAmount > balance) {
            System.out.println("  Insufficient funds (includes $" + TRANSACTION_FEE + " fee)");
            return false;
        }
        balance -= totalAmount;
        logTransaction(TransactionType2.WITHDRAWAL, amount);
        logTransaction(TransactionType2.FEE, TRANSACTION_FEE);
        System.out.println("  Withdrew $" + amount + " + $" + TRANSACTION_FEE + " fee");
        return true;
    }

    @Override
    public void applyInterest() {
        double interest = balance * interestRate;
        balance += interest;
        logTransaction(TransactionType2.INTEREST, interest);
        System.out.println("  Applied interest: $" + String.format("%.2f", interest) + 
            " to business account");
    }

    @Override
    public String toString() {
        return super.toString() + " (Fee: $" + TRANSACTION_FEE + " per withdrawal)";
    }
}