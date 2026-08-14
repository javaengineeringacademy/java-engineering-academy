package academy.javaengineering.oop.practices;

/**
 * Practice: Encapsulation
 * Complete the TODO items below.
 */
public class Practices {

    public static void main(String[] args) {
        // TODO 1: Create a BankAccount with initial balance 1000
        // BankAccount account = ???
        
        // TODO 2: Deposit 500
        // account.deposit(500);
        
        // TODO 3: Withdraw 200
        // account.withdraw(200);
        
        // TODO 4: Try to withdraw 5000 (should fail)
        // System.out.println("Balance: $" + account.getBalance());
    }
}

// TODO: Create a BankAccount class with:
// - Private fields: id (String), balance (double)
// - Constructor with validation (balance >= 0)
// - Getter for balance (no setter!)
// - void deposit(double amount) - validates amount > 0
// - void withdraw(double amount) - validates amount > 0 and amount <= balance
// - toString() returning "Account{id=..., balance=...}"
class BankAccount {
    // YOUR CODE HERE
}
