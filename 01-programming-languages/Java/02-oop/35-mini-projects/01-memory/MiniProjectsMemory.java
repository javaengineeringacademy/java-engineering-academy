package academy.javaengineering.oop.memory;

public class MiniProjectsMemory {

    static class BankAccount {
        private String owner;
        private double balance;
        BankAccount(String owner, double balance) { this.owner = owner; this.balance = balance; }
    }

    static class Library {
        java.util.List<String> books = new java.util.ArrayList<>();
        void addBook(String book) { books.add(book); }
    }

    public static void main(String[] args) {
        System.out.println("=== Mini Projects Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Bank Account Memory
        System.out.println("--- Bank Account Memory ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        BankAccount account = new BankAccount("Alice", 1000);
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("BankAccount: " + (after - before) + " bytes");

        // 2. Library Memory
        System.out.println("\n--- Library Memory ---");
        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        Library library = new Library();
        library.addBook("Java");
        library.addBook("Python");
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Library: " + (after - before) + " bytes");

        // 3. OOP Principles in Memory
        System.out.println("\n--- OOP Principles ---");
        System.out.println("Encapsulation: no memory cost");
        System.out.println("Inheritance: shared method table");
        System.out.println("Polymorphism: vtable lookup");
    }
}
