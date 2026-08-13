package academy.javaengineering.oop.internals;

public class EncapsulationInternals {

    static class BankAccount {
        private String owner;
        private double balance;

        public BankAccount(String owner, double initialBalance) {
            this.owner = owner;
            this.balance = initialBalance;
        }

        public String getOwner() { return owner; }

        public double getBalance() { return balance; }

        public void deposit(double amount) {
            if (amount > 0) {
                balance += amount;
                System.out.println("Deposited: " + amount);
            }
        }

        public void withdraw(double amount) {
            if (amount > 0 && amount <= balance) {
                balance -= amount;
                System.out.println("Withdrawn: " + amount);
            } else {
                System.out.println("Invalid amount");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Encapsulation Internals ===\n");

        // 1. Data Hiding
        System.out.println("--- Data Hiding ---");
        BankAccount account = new BankAccount("Alice", 1000);
        System.out.println("Owner: " + account.getOwner());
        System.out.println("Balance: " + account.getBalance());
        // account.balance = -1000; // COMPILE ERROR

        // 2. Getter/Setter Pattern
        System.out.println("\n--- Getter/Setter ---");
        account.deposit(500);
        account.withdraw(200);
        System.out.println("New balance: " + account.getBalance());

        // 3. Validation in Setters
        System.out.println("\n--- Validation ---");
        System.out.println("Setters can validate input");
        System.out.println("Prevents invalid state");
        System.out.println("Maintains invariants");

        // 4. Benefits
        System.out.println("\n--- Benefits ---");
        System.out.println("1. Control over data");
        System.out.println("2. Flexibility to change implementation");
        System.out.println("3. Read-only or write-only fields");
        System.out.println("4. Thread safety possible");
    }
}
