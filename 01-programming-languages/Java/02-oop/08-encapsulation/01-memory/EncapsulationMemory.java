package academy.javaengineering.oop.memory;

public class EncapsulationMemory {

    static class BankAccount {
        private String owner;
        private double balance;
    }

    public static void main(String[] args) {
        System.out.println("=== Encapsulation Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Access Modifier Overhead
        System.out.println("--- Access Modifier Overhead ---");
        System.out.println("Private/public: zero runtime cost");
        System.out.println("Enforced at compile-time only");
        System.out.println("No impact on object size");

        // 2. Getter/Setter Overhead
        System.out.println("\n--- Getter/Setter Cost ---");
        System.out.println("Method call: ~2-3 cycles");
        System.out.println("JIT inlines small methods");
        System.out.println("Net cost: nearly zero");

        // 3. Object Size with Encapsulation
        System.out.println("\n--- Object Size ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        BankAccount account = new BankAccount();
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("BankAccount: " + (after - before) + " bytes");
        System.out.println("Same as package-private fields");
        System.out.println("Encapsulation: zero memory cost");
    }
}
