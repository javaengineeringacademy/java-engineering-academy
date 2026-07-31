package academy.javaengineering.oop.bank.project;

/**
 * Main application for the Bank Management System.
 * 
 * <p>Demonstrates OOP concepts:
 * <ul>
 *   <li>Encapsulation - Private fields, getters/setters, validation</li>
 *   <li>Inheritance - Account hierarchy (Savings, Checking, Business)</li>
 *   <li>Polymorphism - Different account behaviors</li>
 *   <li>Abstraction - Abstract Account class, interfaces</li>
 *   <li>Interfaces - TransactionProcessor, NotificationService</li>
 *   <li>Composition - Bank HAS Accounts, Account HAS Transactions</li>
 *   <li>SOLID Principles - Single Responsibility, Open/Closed, etc.</li>
 * </ul>
 * 
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public class BankApplication {

    public static void main(String[] args) {
        System.out.println("=== Java Engineering Bank ===\n");

        // Create bank
        Bank bank = new Bank("Java Engineering Bank");

        // Create customers
        Customer alice = new Customer("C001", "Alice", "Johnson", "alice@email.com");
        Customer bob = new Customer("C002", "Bob", "Smith", "bob@email.com");

        // Create accounts
        Account savings = bank.createSavingsAccount(alice, 10000.00);
        Account checking = bank.createCheckingAccount(bob, 5000.00);
        Account business = bank.createBusinessAccount(alice, 50000.00);

        // Display initial state
        System.out.println("--- Initial Balances ---");
        bank.displayAllAccounts();

        // Perform transactions
        System.out.println("\n--- Transactions ---");
        savings.deposit(2000.00);
        checking.withdraw(1000.00);
        bank.transfer(checking, savings, 500.00);

        // Apply interest
        System.out.println("\n--- Apply Interest ---");
        bank.applyInterest();

        // Display final state
        System.out.println("\n--- Final Balances ---");
        bank.displayAllAccounts();

        // Demonstrate polymorphism
        System.out.println("\n--- Account Types (Polymorphism) ---");
        for (Account account : bank.getAccounts()) {
            System.out.printf("  %s - Rate: %.2f%%%n",
                account.getAccountType(),
                account.getInterestRate() * 100);
        }
    }
}