package academy.javaengineering.oop.encapsulation;

/**
 * BankAccount - Demonstrates encapsulation with validation and controlled access.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class BankAccount {

    private final String owner;
    private double balance;
    private final java.util.List<String> transactionLog = new java.util.ArrayList<>();

    public BankAccount(String owner, double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        this.owner = owner;
        this.balance = initialBalance;
        logTransaction("Account created with balance: $" + initialBalance);
    }

    public String getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }

    public boolean deposit(double amount) {
        if (amount <= 0) {
            System.out.println("  Rejected: Deposit must be positive");
            return false;
        }
        balance += amount;
        logTransaction("Deposited: $" + amount);
        return true;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("  Rejected: Withdrawal must be positive");
            return false;
        }
        if (amount > balance) {
            System.out.println("  Rejected: Insufficient funds (balance: $" + balance + ")");
            return false;
        }
        balance -= amount;
        logTransaction("Withdrew: $" + amount);
        return true;
    }

    public java.util.List<String> getTransactionLog() {
        return java.util.Collections.unmodifiableList(transactionLog);
    }

    private void logTransaction(String message) {
        transactionLog.add(java.time.LocalTime.now() + " - " + message);
    }
}