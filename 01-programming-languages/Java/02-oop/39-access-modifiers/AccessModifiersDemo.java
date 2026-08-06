package academy.javaengineering.oop.accessmodifiers;

import java.math.BigDecimal;

/**
 * Access modifiers - public, protected, default, private
 *
 * This file demonstrates:
 * - Four access levels and their visibility
 * - When to use each modifier
 * - How access modifiers work with inheritance
 * - How access modifiers work with packages
 */
public class AccessModifiersDemo {

    // =========================================================
    // 1. CLASS WITH ALL ACCESS LEVELS
    // =========================================================

    public static class BankAccount {
        private BigDecimal balance;
        private String ownerName;
        protected String accountType;
        int transactionCount;  // package-private

        public BankAccount(String ownerName, BigDecimal initialBalance) {
            this.ownerName = ownerName;
            this.balance = initialBalance;
            this.accountType = "CHECKING";
            this.transactionCount = 0;
        }

        // Public: Part of the public API
        public BigDecimal getBalance() {
            return balance;
        }

        public String getOwnerName() {
            return ownerName;
        }

        public void deposit(BigDecimal amount) {
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Amount must be positive");
            }
            balance = balance.add(amount);
            transactionCount++;
        }

        public void withdraw(BigDecimal amount) {
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Amount must be positive");
            }
            if (balance.compareTo(amount) < 0) {
                throw new IllegalArgumentException("Insufficient funds");
            }
            balance = balance.subtract(amount);
            transactionCount++;
        }

        // Protected: Available to subclasses
        protected String getAccountInfo() {
            return "Account: " + ownerName + ", Type: " + accountType;
        }

        // Package-private: Available within same package only
        void logTransaction(String type, BigDecimal amount) {
            System.out.println("[LOG] " + type + ": " + amount + " for " + ownerName);
        }

        // Private: Not visible outside this class
        private boolean hasSufficientFunds(BigDecimal amount) {
            return balance.compareTo(amount) >= 0;
        }
    }

    // =========================================================
    // 2. SUBCLASS DEMONSTRATING PROTECTED ACCESS
    // =========================================================

    public static class SavingsAccount extends BankAccount {
        private BigDecimal interestRate;

        public SavingsAccount(String ownerName, BigDecimal initialBalance, BigDecimal interestRate) {
            super(ownerName, initialBalance);
            this.interestRate = interestRate;
            this.accountType = "SAVINGS";  // Can access protected field
        }

        public void applyInterest() {
            BigDecimal interest = getBalance().multiply(interestRate);
            deposit(interest);
            logTransaction("INTEREST", interest);  // Can access package-private
        }

        @Override
        protected String getAccountInfo() {
            return super.getAccountInfo() + ", Rate: " + interestRate;
        }
    }

    // =========================================================
    // 3. DEMONSTRATION
    // =========================================================

    public static void main(String[] args) {
        System.out.println("=== Access Modifiers Demo ===\n");

        BankAccount account = new BankAccount("Alice", BigDecimal.valueOf(1000));

        // Public access - works everywhere
        System.out.println("--- Public Access ---");
        System.out.println("Owner: " + account.getOwnerName());
        System.out.println("Balance: " + account.getBalance());

        // Protected access - works within package
        System.out.println("\n--- Protected Access ---");
        System.out.println("Account Info: " + account.getAccountInfo());

        // Package-private access - works within package
        System.out.println("\n--- Package-Private Access ---");
        account.logTransaction("DEPOSIT", BigDecimal.valueOf(500));

        // Private access - compile error if uncommented
        // account.hasSufficientFunds(BigDecimal.valueOf(100));  // ERROR: private

        // Demonstrate inheritance
        System.out.println("\n--- Inheritance Demo ---");
        SavingsAccount savings = new SavingsAccount("Bob", BigDecimal.valueOf(2000), BigDecimal.valueOf(0.05));
        savings.applyInterest();

        System.out.println("\n=== Summary ===");
        System.out.println("private    → Only this class");
        System.out.println("default   → Same package only");
        System.out.println("protected → Same package + subclasses");
        System.out.println("public    → Everywhere");
    }
}
