package academy.javaengineering.oop.bank;

/**
 * Account2 - Abstract base class for all account types.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public abstract class Account2 {

    private final String accountNumber;
    private final String owner;
    protected double balance;
    private final java.util.List<Transaction2> transactions;

    protected Account2(String owner, double initialBalance) {
        this.accountNumber = generateAccountNumber();
        this.owner = owner;
        this.balance = initialBalance;
        this.transactions = new java.util.ArrayList<>();
    }

    public abstract double getInterestRate();
    public abstract String getAccountType();

    public boolean deposit(double amount) {
        if (amount <= 0) {
            System.out.println("  Invalid deposit amount");
            return false;
        }
        balance += amount;
        logTransaction(TransactionType2.DEPOSIT, amount);
        System.out.println("  Deposited $" + amount + " to " + getAccountType());
        return true;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("  Invalid withdrawal amount");
            return false;
        }
        if (amount > balance) {
            System.out.println("  Insufficient funds");
            return false;
        }
        balance -= amount;
        logTransaction(TransactionType2.WITHDRAWAL, amount);
        System.out.println("  Withdrew $" + amount + " from " + getAccountType());
        return true;
    }

    protected void logTransaction(TransactionType2 type, double amount) {
        transactions.add(new Transaction2(type, amount));
    }

    private static String generateAccountNumber() {
        return "ACC-" + System.currentTimeMillis() % 100000;
    }

    public String getAccountNumber() { return accountNumber; }
    public String getOwner() { return owner; }
    public double getBalance() { return balance; }
    public java.util.List<Transaction2> getTransactions() { 
        return java.util.Collections.unmodifiableList(transactions); 
    }

    @Override
    public String toString() {
        return String.format("%s[%s] Owner: %s, Balance: $%.2f, Interest: %.1f%%",
            getAccountType(), accountNumber, owner, balance, getInterestRate() * 100);
    }
}