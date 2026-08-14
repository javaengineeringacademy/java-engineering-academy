package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Encapsulation Patterns ===\n");

        // WHY: Encapsulation protects invariants, enables change without breaking consumers
        // INTERNAL: Access modifiers control visibility at bytecode level
        // ENGINEERING: Validate in setters, never expose mutable internal state

        BankAccount account = new BankAccount("ACC001", 1000);
        System.out.println("Balance: $" + account.getBalance());

        account.deposit(500);
        System.out.println("After deposit: $" + account.getBalance());

        account.withdraw(200);
        System.out.println("After withdrawal: $" + account.getBalance());

        try {
            account.withdraw(5000);  // Should fail
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }

        // TRADE-OFF: Getters/setters vs record (Java 16+)
        // Getters/setters: validation, flexibility, mutable
        // Record: immutable, concise, no validation
    }
}

class BankAccount {
    private final String id;
    private double balance;

    public BankAccount(String id, double initialBalance) {
        if (initialBalance < 0) throw new IllegalArgumentException("Balance cannot be negative");
        this.id = id;
        this.balance = initialBalance;
    }

    public String getId() { return id; }
    public double getBalance() { return balance; }

    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit must be positive");
        this.balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Withdrawal must be positive");
        if (amount > balance) throw new IllegalArgumentException("Insufficient funds");
        this.balance -= amount;
    }
}
