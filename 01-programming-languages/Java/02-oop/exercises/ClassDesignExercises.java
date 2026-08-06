package academy.javaengineering.exercises;

/**
 * Exercises: Class Design (Encapsulation, Constructors, Getters/Setters)
 *
 * Complete the TODO sections below.
 */
public class ClassDesignExercises {

    // TODO 1: Design a BankAccount class with proper encapsulation
    // Requirements:
    // - Private fields: accountNumber (String), balance (double), accountType (String)
    // - Constructor that takes accountNumber and initial balance
    // - Validate balance >= 0 in constructor, throw IllegalArgumentException otherwise
    // - Getter methods for all fields (balance getter should return copy)
    // - deposit(double amount) - throws if amount <= 0
    // - withdraw(double amount) - throws if amount <= 0 or amount > balance
    // - getAccountSummary() returns formatted string "TYPE-XXXX: $Y.YY"
    // TODO: Create the BankAccount inner class below


    // TODO 2: Design an immutable Address class
    // Requirements:
    // - All fields private final: street, city, state, zipCode
    // - Constructor takes all 4 fields
    // - Only getters, no setters
    // - toString() returns formatted address
    // TODO: Create the Address inner class below


    // TODO 3: Design a Builder pattern for a Computer class
    // Computer has: brand (String), ram (int), storage (int), hasGraphicsCard (boolean)
    // Required fields in constructor: brand, ram
    // Optional fields set via builder: storage (default 256), hasGraphicsCard (default false)
    // TODO: Create the Computer and ComputerBuilder classes below


    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        int passed = 0;
        int total = 0;

        System.out.println("=== ClassDesignExercises Tests ===\n");

        // Test 1: BankAccount
        total++;
        try {
            // Uncomment when BankAccount is implemented
            // BankAccount account = new BankAccount("ACC-1234", 1000.0);
            // account.deposit(500.0);
            // account.withdraw(200.0);
            // if (Math.abs(account.getBalance() - 1300.0) < 0.01
            //     && "ACC-1234".equals(account.getAccountNumber())
            //     && account.getAccountSummary().contains("1300.00")) {
            //     System.out.println("Test 1 PASSED: BankAccount");
            //     passed++;
            // } else {
            //     System.out.println("Test 1 FAILED: BankAccount");
            // }
            System.out.println("Test 1 SKIPPED: BankAccount - implement inner class");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Test 1 FAILED: BankAccount - " + e.getMessage());
        }

        // Test 2: Address (Immutable)
        total++;
        try {
            // Uncomment when Address is implemented
            // Address addr = new Address("123 Main St", "Springfield", "IL", "62701");
            // String addrStr = addr.toString();
            // if ("123 Main St".equals(addr.getStreet())
            //     && "Springfield".equals(addr.getCity())
            //     && addrStr.contains("IL")
            //     && addrStr.contains("62701")) {
            //     System.out.println("Test 2 PASSED: Address (Immutable)");
            //     passed++;
            // } else {
            //     System.out.println("Test 2 FAILED: Address");
            // }
            System.out.println("Test 2 SKIPPED: Address - implement inner class");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Test 2 FAILED: Address - " + e.getMessage());
        }

        // Test 3: Computer Builder
        total++;
        try {
            // Uncomment when Computer is implemented
            // Computer pc = new Computer.ComputerBuilder("Dell", 16)
            //     .storage(512)
            //     .hasGraphicsCard(true)
            //     .build();
            // if ("Dell".equals(pc.getBrand())
            //     && pc.getRam() == 16
            //     && pc.getStorage() == 512
            //     && pc.isHasGraphicsCard()) {
            //     System.out.println("Test 3 PASSED: Computer Builder");
            //     passed++;
            // } else {
            //     System.out.println("Test 3 FAILED: Computer Builder");
            // }
            System.out.println("Test 3 SKIPPED: Computer Builder - implement inner classes");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Test 3 FAILED: Computer Builder - " + e.getMessage());
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
        System.out.println("Note: Uncomment the test code above after implementing the inner classes.");
    }
}
