package academy.javaengineering.testing.practices;

import java.util.*;

/**
 * Unit Testing Exercises
 * Practice best practices and patterns
 */
class UnitTestingExercises {

    // ============================================
    // Exercise 1: Given-When-Then Pattern
    // ============================================

    static class Calculator {
        private final List<String> history = new ArrayList<>();

        int add(int a, int b) {
            int result = a + b;
            history.add(a + " + " + b + " = " + result);
            return result;
        }

        int subtract(int a, int b) {
            int result = a - b;
            history.add(a + " - " + b + " = " + result);
            return result;
        }

        List<String> getHistory() {
            return Collections.unmodifiableList(history);
        }
    }

    /*
     * TODO: Write tests using Given-When-Then pattern
     * Given: Set up calculator
     * When: Perform operation
     * Then: Verify result and history
     */

    // ============================================
    // Exercise 2: Test Data Builder
    // ============================================

    static class Product {
        private final String name;
        private final double price;
        private final String category;
        private final boolean inStock;

        Product(String name, double price, String category, boolean inStock) {
            this.name = name;
            this.price = price;
            this.category = category;
            this.inStock = inStock;
        }

        String getName() { return name; }
        double getPrice() { return price; }
        String getCategory() { return category; }
        boolean isInStock() { return inStock; }

        static ProductBuilder builder() {
            return new ProductBuilder();
        }

        static class ProductBuilder {
            private String name = "Test Product";
            private double price = 9.99;
            private String category = "General";
            private boolean inStock = true;

            ProductBuilder name(String name) { this.name = name; return this; }
            ProductBuilder price(double price) { this.price = price; return this; }
            ProductBuilder category(String category) { this.category = category; return this; }
            ProductBuilder inStock(boolean inStock) { this.inStock = inStock; return this; }
            Product build() { return new Product(name, price, category, inStock); }
        }
    }

    /*
     * TODO: Write tests using builder pattern
     * Product product = Product.builder()
     *     .name("Laptop")
     *     .price(999.99)
     *     .category("Electronics")
     *     .inStock(true)
     *     .build();
     */

    // ============================================
    // Exercise 3: Exception Testing
    // ============================================

    static class BankAccount {
        private double balance;

        BankAccount(double initialBalance) {
            if (initialBalance < 0) throw new IllegalArgumentException("Negative balance");
            this.balance = initialBalance;
        }

        void deposit(double amount) {
            if (amount <= 0) throw new IllegalArgumentException("Invalid amount");
            balance += amount;
        }

        void withdraw(double amount) {
            if (amount <= 0) throw new IllegalArgumentException("Invalid amount");
            if (amount > balance) throw new IllegalStateException("Insufficient funds");
            balance -= amount;
        }

        double getBalance() { return balance; }
    }

    /*
     * TODO: Write exception tests
     * - Negative initial balance
     * - Invalid deposit amount
     * - Insufficient funds
     */

    // ============================================
    // Exercise 4: Edge Cases
    // ============================================

    static class StringUtils {
        static String reverse(String input) {
            if (input == null) return null;
            return new StringBuilder(input).reverse().toString();
        }

        static boolean isPalindrome(String input) {
            if (input == null) return false;
            String cleaned = input.toLowerCase().replaceAll("[^a-z0-9]", "");
            return cleaned.equals(reverse(cleaned));
        }
    }

    /*
     * TODO: Test edge cases
     * - null input
     * - empty string
     * - single character
     * - special characters
     */

    // ============================================
    // Exercise 5: Complex Object Testing
    // ============================================

    static class ShoppingCart {
        private final Map<String, Integer> items = new LinkedHashMap<>();
        private double discount = 0;

        void addItem(String product, int quantity) {
            items.merge(product, quantity, Integer::sum);
        }

        void removeItem(String product) {
            items.remove(product);
        }

        void setDiscount(double discount) {
            if (discount < 0 || discount > 100) throw new IllegalArgumentException("Invalid discount");
            this.discount = discount;
        }

        int getItemCount() {
            return items.values().stream().mapToInt(Integer::intValue).sum();
        }

        Map<String, Integer> getItems() {
            return new LinkedHashMap<>(items);
        }
    }

    /*
     * TODO: Test complex scenarios
     * - Adding multiple items
     * - Removing items
     * - Setting discount
     * - Edge cases
     */

    public static void main(String[] args) {
        System.out.println("=== Unit Testing Exercises ===");
        System.out.println("Practice best practices and patterns.");
    }
}
