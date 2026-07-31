package academy.javaengineering.oop.bank.project;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Bank - Main bank class that manages accounts and customers.
 * 
 * <p>Single Responsibility: Only handles bank operations.
 * Composition: Bank HAS Accounts, Account HAS Transactions.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class Bank {

    private final String name;
    private final List<Account> accounts;
    private final TransactionLogger logger;

    public Bank(String name) {
        this.name = name;
        this.accounts = new ArrayList<>();
        this.logger = new TransactionLogger();
    }

    public Account createSavingsAccount(Customer customer, double initialDeposit) {
        SavingsAccount account = new SavingsAccount(customer, initialDeposit, 0.05);
        accounts.add(account);
        logger.log("Created Savings Account for " + customer.getFullName());
        return account;
    }

    public Account createCheckingAccount(Customer customer, double initialDeposit) {
        CheckingAccount account = new CheckingAccount(customer, initialDeposit, 500.00);
        accounts.add(account);
        logger.log("Created Checking Account for " + customer.getFullName());
        return account;
    }

    public Account createBusinessAccount(Customer customer, double initialDeposit) {
        BusinessAccount account = new BusinessAccount(customer, initialDeposit, 0.02);
        accounts.add(account);
        logger.log("Created Business Account for " + customer.getFullName());
        return account;
    }

    public boolean transfer(Account from, Account to, double amount) {
        if (from.withdraw(amount)) {
            to.deposit(amount);
            logger.log(String.format("Transfer: $%.2f from %s to %s",
                amount, from.getAccountNumber(), to.getAccountNumber()));
            return true;
        }
        logger.log("Transfer failed: Insufficient funds");
        return false;
    }

    public void applyInterest() {
        for (Account account : accounts) {
            if (account instanceof InterestBearing) {
                ((InterestBearing) account).applyInterest();
            }
        }
    }

    public void displayAllAccounts() {
        for (Account account : accounts) {
            System.out.println("  " + account);
        }
    }

    public String getName() {
        return name;
    }

    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }
}