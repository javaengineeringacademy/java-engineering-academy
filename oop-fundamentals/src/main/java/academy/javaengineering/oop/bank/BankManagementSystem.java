package academy.javaengineering.oop.bank;

import java.util.ArrayList;
import java.util.List;

/**
 * BankManagementSystem - Main application demonstrating OOP concepts in a banking system.
 * 
 * <p>This project integrates:
 * <ul>
 *   <li>Encapsulation - Private fields, getters/setters, validation</li>
 *   <li>Inheritance - Account hierarchy (Savings, Checking, Business)</li>
 *   <li>Polymorphism - Different account types, transaction processing</li>
 *   <li>Abstraction - Abstract Account class, TransactionProcessor interface</li>
 *   <li>Interfaces - TransactionProcessor, NotificationService</li>
 *   <li>Composition - Bank HAS Accounts, Account HAS Transactions</li>
 *   <li>SOLID Principles - Single Responsibility, Open/Closed, etc.</li>
 * </ul>
 * 
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public class BankManagementSystem {

    private final String bankName;
    private final List<Account2> accounts;
    private final TransactionLogger logger;

    public BankManagementSystem(String bankName) {
        this.bankName = bankName;
        this.accounts = new ArrayList<>();
        this.logger = new TransactionLogger();
    }

    public Account2 createSavingsAccount(String owner, double initialDeposit) {
        Account2 account = new SavingsAccount3(owner, initialDeposit, 0.05);
        accounts.add(account);
        logger.log("Created Savings Account for " + owner);
        return account;
    }

    public Account2 createCheckingAccount(String owner, double initialDeposit) {
        Account2 account = new CheckingAccount3(owner, initialDeposit, 500);
        accounts.add(account);
        logger.log("Created Checking Account for " + owner);
        return account;
    }

    public Account2 createBusinessAccount(String owner, double initialDeposit) {
        Account2 account = new BusinessAccount(owner, initialDeposit, 0.02);
        accounts.add(account);
        logger.log("Created Business Account for " + owner);
        return account;
    }

    public void transfer(Account2 from, Account2 to, double amount) {
        if (from.withdraw(amount)) {
            to.deposit(amount);
            logger.log("Transfer: $" + amount + " from " + from.getOwner() + " to " + to.getOwner());
        } else {
            logger.log("Transfer failed: Insufficient funds");
        }
    }

    public void displayAllAccounts() {
        System.out.println("\n=== " + bankName + " - All Accounts ===");
        for (Account2 account : accounts) {
            System.out.println("  " + account);
        }
    }

    public void applyInterestToAll() {
        System.out.println("\n--- Applying Interest ---");
        for (Account2 account : accounts) {
            if (account instanceof InterestBearing) {
                ((InterestBearing) account).applyInterest();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Bank Management System ===\n");

        BankManagementSystem bank = new BankManagementSystem("Java Engineering Bank");

        // Create accounts
        Account2 savings = bank.createSavingsAccount("Alice", 10000);
        Account2 checking = bank.createCheckingAccount("Bob", 5000);
        Account2 business = bank.createBusinessAccount("Charlie's Corp", 50000);

        // Display initial state
        bank.displayAllAccounts();

        // Perform transactions
        System.out.println("\n--- Transactions ---");
        savings.deposit(2000);
        checking.withdraw(1000);
        bank.transfer(checking, savings, 500);

        // Apply interest
        bank.applyInterestToAll();

        // Display final state
        bank.displayAllAccounts();

        // Demonstrate polymorphism
        System.out.println("\n--- Account Types ---");
        for (Account2 account : bank.accounts) {
            System.out.println("  " + account.getClass().getSimpleName() + 
                " - Interest Rate: " + account.getInterestRate() * 100 + "%");
        }
    }
}