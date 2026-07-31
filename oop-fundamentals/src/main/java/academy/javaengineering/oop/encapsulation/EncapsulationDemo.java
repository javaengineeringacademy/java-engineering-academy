package academy.javaengineering.oop.encapsulation;

/**
 * EncapsulationDemo - Demonstrates encapsulation with private fields and public getters/setters.
 * 
 * <p><b>Encapsulation</b> bundles data (fields) and methods operating on that data
 * within a class, restricting direct access to some components.
 * 
 * <p><b>Benefits:</b>
 * <ul>
 *   <li>Data hiding - internal state protected from invalid modification</li>
 *   <li>Controlled access through getters/setters</li>
 *   <li>Flexibility to change internal implementation without affecting callers</li>
 *   <li>Validation logic in setters ensures data integrity</li>
 *   <li>Read-only or write-only fields via selective getters/setters</li>
 * </ul>
 * 
 * <p><b>Real-world analogy:</b> Like a bank account - you can't directly modify the balance,
 * you must go through deposit/withdraw methods that enforce rules.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class EncapsulationDemo {

    private EncapsulationDemo() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("=== Encapsulation Demo ===\n");

        // Encapsulated class
        System.out.println("--- Creating Person (Encapsulated) ---");
        Person person = new Person("Alice", 30);
        System.out.println("Name: " + person.getName());
        System.out.println("Age: " + person.getAge());
        System.out.println("Age Category: " + person.getAgeCategory());

        // Setting valid values
        person.setAge(35);
        System.out.println("Updated age: " + person.getAge());

        // Invalid values rejected
        System.out.println("Trying negative age:");
        boolean success = person.setAge(-5);
        System.out.println("Success: " + success); // false
        System.out.println("Age unchanged: " + person.getAge()); // 35

        System.out.println("Trying age > 150:");
        success = person.setAge(200);
        System.out.println("Success: " + success); // false

        // Read-only field (no setter)
        System.out.println("\n--- Read-Only Field ---");
        System.out.println("Created: " + person.getCreatedTimestamp());
        System.out.println("(No setter - cannot modify creation time)");

        // Write-only field (no getter)
        System.out.println("\n--- Write-Only Field ---");
        person.setSSN("123-45-6789");
        System.out.println("SSN set (no getter for security)");

        // Encapsulated list - controlled access
        System.out.println("\n--- Controlled Collection Access ---");
        BankAccount account = new BankAccount("John", 1000.0);
        System.out.println("Balance: $" + account.getBalance());
        
        account.deposit(500.0);
        System.out.println("After deposit $500: $" + account.getBalance());
        
        account.withdraw(200.0);
        System.out.println("After withdraw $200: $" + account.getBalance());
        
        System.out.println("Trying to withdraw $10000:");
        boolean withdrawSuccess = account.withdraw(10000.0);
        System.out.println("Success: " + withdrawSuccess); // false
        System.out.println("Balance unchanged: $" + account.getBalance());

        // Demonstrating immutability through encapsulation
        System.out.println("\n--- Immutable Object ---");
        Money price1 = new Money(100.50, "USD");
        Money price2 = new Money(200.00, "USD");
        Money total = price1.add(price2);
        System.out.println("Price 1: " + price1); // Unchanged
        System.out.println("Price 2: " + price2); // Unchanged
        System.out.println("Total: " + total);     // New object
    }
}