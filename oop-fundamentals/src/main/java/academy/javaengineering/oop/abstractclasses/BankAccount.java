package academy.javaengineering.oop.abstractclasses;

/**
 * BankAccount - Abstract class demonstrating shared state and template methods.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public abstract class BankAccount {

    protected final String owner;
    protected double balance;

    protected BankAccount(String owner, double initialBalance) {
        this.owner = owner;
        this.balance = initialBalance;
    }

    // Abstract methods
    public abstract double getInterestRate();
    public abstract String getAccountType();

    // Template method - defines algorithm, subclasses override steps
    public final void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("  Invalid deposit amount");
            return;
        }
        balance += amount;
        System.out.println("  Deposited $" + amount + " into " + getAccountType());
        applyInterest();
    }

    public final void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("  Invalid withdrawal amount");
            return;
        }
        if (amount > balance) {
            System.out.println("  Insufficient funds");
            return;
        }
        balance -= amount;
        System.out.println("  Withdrew $" + amount + " from " + getAccountType());
    }

    // Hook method - subclasses can override
    protected void applyInterest() {
        double interest = balance * getInterestRate();
        balance += interest;
        System.out.println("  Applied interest: $" + String.format("%.2f", interest));
    }

    public double getBalance() {
        return balance;
    }

    public String getOwner() {
        return owner;
    }
}