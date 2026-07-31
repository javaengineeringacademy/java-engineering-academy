package academy.javaengineering.oop.bank.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Account - Abstract base class for all account types.
 * 
 * <p>Template Method pattern: Defines algorithm, subclasses override steps.
 * Encapsulation: Private fields with controlled access.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public abstract class Account {

    private final String accountNumber;
    private final Customer customer;
    protected double balance;
    private final List<Transaction> transactions;
    private final TransactionLogger logger;

    protected Account(Customer customer, double initialBalance) {
        this.accountNumber = generateAccountNumber();
        this.customer = customer;
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
        this.logger = new TransactionLogger();
    }

    // Abstract methods - subclasses must implement
    public abstract String getAccountType();
    public abstract double getInterestRate();

    // Template method
    public final boolean deposit(double amount) {
        if (amount <= 0) {
            logger.log("Invalid deposit amount: $" + amount);
            return false;
        }
        balance += amount;
        logTransaction(TransactionType.DEPOSIT, amount);
        logger.log(String.format("Deposited $%.2f to %s", amount, accountNumber));
        return true;
    }

    // Template method with hook
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            logger.log("Invalid withdrawal amount: $" + amount);
            return false;
        }
        if (amount > balance) {
            logger.log("Insufficient funds for withdrawal: $" + amount);
            return false;
        }
        balance -= amount;
        logTransaction(TransactionType.WITHDRAWAL, amount);
        logger.log(String.format("Withdrew $%.2f from %s", amount, accountNumber));
        return true;
    }

    protected void logTransaction(TransactionType type, double amount) {
        transactions.add(new Transaction(type, amount, LocalDateTime.now()));
    }

    private static String generateAccountNumber() {
        return "ACC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Getters
    public String getAccountNumber() {
        return accountNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    @Override
    public String toString() {
        return String.format("%s[%s] Owner: %s, Balance: $%.2f, Rate: %.1f%%",
            getAccountType(),
            accountNumber,
            customer.getFullName(),
            balance,
            getInterestRate() * 100);
    }
}