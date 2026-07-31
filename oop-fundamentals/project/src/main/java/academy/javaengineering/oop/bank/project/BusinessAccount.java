package academy.javaengineering.oop.bank.project;

/**
 * BusinessAccount - Business account with transaction fees.
 * 
 * <p>Implements InterestBearing for interest calculation.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class BusinessAccount extends Account implements InterestBearing {

    private final double interestRate;
    private static final double TRANSACTION_FEE = 2.50;

    public BusinessAccount(Customer customer, double initialBalance, double interestRate) {
        super(customer, initialBalance);
        this.interestRate = interestRate;
    }

    @Override
    public String getAccountType() {
        return "Business Account";
    }

    @Override
    public double getInterestRate() {
        return interestRate;
    }

    @Override
    public boolean withdraw(double amount) {
        double totalAmount = amount + TRANSACTION_FEE;
        if (totalAmount > balance) {
            System.out.printf("  Insufficient funds (includes $%.2f fee)%n", TRANSACTION_FEE);
            return false;
        }
        balance -= totalAmount;
        logTransaction(TransactionType.WITHDRAWAL, amount);
        logTransaction(TransactionType.FEE, TRANSACTION_FEE);
        System.out.printf("  Withdrew $%.2f + $%.2f fee%n", amount, TRANSACTION_FEE);
        return true;
    }

    @Override
    public void applyInterest() {
        double interest = balance * interestRate;
        balance += interest;
        logTransaction(TransactionType.INTEREST, interest);
        System.out.printf("  Applied interest: $%.2f to business account%n", interest);
    }

    @Override
    public String toString() {
        return super.toString() + " (Fee: $" + TRANSACTION_FEE + "/withdrawal)";
    }
}