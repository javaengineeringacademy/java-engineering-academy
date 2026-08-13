package academy.javaengineering.oop.internals;

public class MiniProjectsInternals {

    static class BankAccount {
        private String owner;
        private double balance;

        BankAccount(String owner, double initialBalance) {
            this.owner = owner;
            this.balance = initialBalance;
        }

        void deposit(double amount) {
            if (amount > 0) balance += amount;
        }

        void withdraw(double amount) {
            if (amount > 0 && amount <= balance) balance -= amount;
        }

        double getBalance() { return balance; }
    }

    static class Library {
        private java.util.List<String> books = new java.util.ArrayList<>();

        void addBook(String book) { books.add(book); }
        void removeBook(String book) { books.remove(book); }
        int getBookCount() { return books.size(); }
    }

    public static void main(String[] args) {
        System.out.println("=== Mini Projects Internals ===\n");

        // 1. Bank Account
        System.out.println("--- Bank Account ---");
        BankAccount account = new BankAccount("Alice", 1000);
        account.deposit(500);
        account.withdraw(200);
        System.out.println("Balance: " + account.getBalance());

        // 2. Library
        System.out.println("\n--- Library ---");
        Library library = new Library();
        library.addBook("Java Programming");
        library.addBook("Design Patterns");
        System.out.println("Books: " + library.getBookCount());

        // 3. Design Patterns Used
        System.out.println("\n--- Design Patterns ---");
        System.out.println("Encapsulation: private fields");
        System.out.println("Abstraction: public methods");
        System.out.println("Single Responsibility");
    }
}
