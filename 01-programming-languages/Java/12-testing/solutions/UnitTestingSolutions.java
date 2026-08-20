package academy.javaengineering.testing.solutions;

import java.util.*;

/**
 * Unit Testing Solutions
 * Complete solutions for best practices and patterns
 */
class UnitTestingSolutions {

    // ============================================
    // Exercise 1: Given-When-Then Pattern Solution
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
     * Given-When-Then Solution:
     * 
     * @Test
     * void testAddition() {
     *     // Given
     *     Calculator calculator = new Calculator();
     * 
     *     // When
     *     int result = calculator.add(5, 3);
     * 
     *     // Then
     *     assertEquals(8, result);
     *     assertEquals(1, calculator.getHistory().size());
     *     assertTrue(calculator.getHistory().get(0).contains("+"));
     * }
     * 
     * @Test
     * void testHistory() {
     *     // Given
     *     Calculator calculator = new Calculator();
     * 
     *     // When
     *     calculator.add(1, 2);
     *     calculator.subtract(5, 3);
     * 
     *     // Then
     *     assertEquals(2, calculator.getHistory().size());
     *     assertTrue(calculator.getHistory().get(0).contains("1 + 2"));
     *     assertTrue(calculator.getHistory().get(1).contains("5 - 3"));
     * }
     */

    // ============================================
    // Exercise 2: Test Data Builder Solution
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
     * Builder Pattern Solution:
     * 
     * @Test
     * void testProductCreation() {
     *     // Arrange
     *     Product product = Product.builder()
     *         .name("Laptop")
     *         .price(999.99)
     *         .category("Electronics")
     *         .inStock(true)
     *         .build();
     * 
     *     // Act & Assert
     *     assertEquals("Laptop", product.getName());
     *     assertEquals(999.99, product.getPrice(), 0.01);
     *     assertEquals("Electronics", product.getCategory());
     *     assertTrue(product.isInStock());
     * }
     * 
     * @Test
     * void testProductDefaults() {
     *     // Arrange
     *     Product product = Product.builder().build();
     * 
     *     // Act & Assert
     *     assertEquals("Test Product", product.getName());
     *     assertEquals(9.99, product.getPrice(), 0.01);
     *     assertEquals("General", product.getCategory());
     *     assertTrue(product.isInStock());
     * }
     * 
     * @Test
     * void testOutOfStockProduct() {
     *     // Arrange
     *     Product product = Product.builder()
     *         .name("TV")
     *         .price(599.99)
     *         .inStock(false)
     *         .build();
     * 
     *     // Act & Assert
     *     assertFalse(product.isInStock());
     * }
     */

    // ============================================
    // Exercise 3: Exception Testing Solution
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
     * Exception Testing Solution:
     * 
     * @Test
     * void testNegativeInitialBalance() {
     *     IllegalArgumentException exception = assertThrows(
     *         IllegalArgumentException.class,
     *         () -> new BankAccount(-100)
     *     );
     *     assertEquals("Negative balance", exception.getMessage());
     * }
     * 
     * @Test
     * void testInvalidDeposit() {
     *     BankAccount account = new BankAccount(100);
     *     IllegalArgumentException exception = assertThrows(
     *         IllegalArgumentException.class,
     *         () -> account.deposit(-50)
     *     );
     *     assertEquals("Invalid amount", exception.getMessage());
     * }
     * 
     * @Test
     * void testInsufficientFunds() {
     *     BankAccount account = new BankAccount(100);
     *     IllegalStateException exception = assertThrows(
     *         IllegalStateException.class,
     *         () -> account.withdraw(200)
     *     );
     *     assertEquals("Insufficient funds", exception.getMessage());
     * }
     * 
     * @Test
     * void testValidOperations() {
     *     BankAccount account = new BankAccount(100);
     *     assertDoesNotThrow(() -> account.deposit(50));
     *     assertDoesNotThrow(() -> account.withdraw(30));
     *     assertEquals(120, account.getBalance(), 0.01);
     * }
     */

    // ============================================
    // Exercise 4: Edge Cases Solution
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
     * Edge Cases Solution:
     * 
     * @Test
     * void testReverseNull() {
     *     assertNull(StringUtils.reverse(null));
     * }
     * 
     * @Test
     * void testReverseEmpty() {
     *     assertEquals("", StringUtils.reverse(""));
     * }
     * 
     * @Test
     * void testReverseSingleChar() {
     *     assertEquals("a", StringUtils.reverse("a"));
     * }
     * 
     * @Test
     * void testReverseNormal() {
     *     assertEquals("olleh", StringUtils.reverse("hello"));
     * }
     * 
     * @Test
     * void testIsPalindromeNull() {
     *     assertFalse(StringUtils.isPalindrome(null));
     * }
     * 
     * @Test
     * void testIsPalindromeEmpty() {
     *     assertTrue(StringUtils.isPalindrome(""));
     * }
     * 
     * @Test
     * void testIsPalindromeSingleChar() {
     *     assertTrue(StringUtils.isPalindrome("a"));
     * }
     * 
     * @Test
     * void testIsPalindromeWithSpecialChars() {
     *     assertTrue(StringUtils.isPalindrome("A man a plan a canal Panama"));
     * }
     * 
     * @Test
     * void testIsNotPalindrome() {
     *     assertFalse(StringUtils.isPalindrome("hello"));
     * }
     */

    // ============================================
    // Exercise 5: Complex Object Testing Solution
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
     * Complex Object Testing Solution:
     * 
     * @Test
     * void testAddItem() {
     *     ShoppingCart cart = new ShoppingCart();
     *     cart.addItem("Laptop", 1);
     *     cart.addItem("Mouse", 2);
     * 
     *     assertEquals(3, cart.getItemCount());
     *     assertEquals(1, cart.getItems().get("Laptop"));
     *     assertEquals(2, cart.getItems().get("Mouse"));
     * }
     * 
     * @Test
     * void testAddSameItemTwice() {
     *     ShoppingCart cart = new ShoppingCart();
     *     cart.addItem("Laptop", 1);
     *     cart.addItem("Laptop", 2);
     * 
     *     assertEquals(3, cart.getItemCount());
     *     assertEquals(3, cart.getItems().get("Laptop"));
     * }
     * 
     * @Test
     * void testRemoveItem() {
     *     ShoppingCart cart = new ShoppingCart();
     *     cart.addItem("Laptop", 1);
     *     cart.removeItem("Laptop");
     * 
     *     assertEquals(0, cart.getItemCount());
     *     assertTrue(cart.getItems().isEmpty());
     * }
     * 
     * @Test
     * void testSetDiscount() {
     *     ShoppingCart cart = new ShoppingCart();
     *     cart.setDiscount(10);
     * 
     *     assertEquals(10, cart.getDiscount(), 0.01);
     * }
     * 
     * @Test
     * void testInvalidDiscount() {
     *     ShoppingCart cart = new ShoppingCart();
     * 
     *     assertThrows(IllegalArgumentException.class, () -> cart.setDiscount(-10));
     *     assertThrows(IllegalArgumentException.class, () -> cart.setDiscount(150));
     * }
     * 
     * @Test
     * void testEmptyCart() {
     *     ShoppingCart cart = new ShoppingCart();
     * 
     *     assertEquals(0, cart.getItemCount());
     *     assertTrue(cart.getItems().isEmpty());
     * }
     */

    public static void main(String[] args) {
        System.out.println("=== Unit Testing Solutions ===\n");

        System.out.println("--- Given-When-Then Pattern ---");
        System.out.println("Given: Set up initial state");
        System.out.println("When: Execute the action");
        System.out.println("Then: Verify the result\n");

        System.out.println("--- Test Data Builder ---");
        System.out.println("Product.builder().name(\"X\").price(10).build()");
        System.out.println("Provides readable, maintainable test data\n");

        System.out.println("--- Exception Testing ---");
        System.out.println("assertThrows(Exception.class, () -> { ... })");
        System.out.println("assertDoesNotThrow(() -> { ... })\n");

        System.out.println("--- Edge Cases ---");
        System.out.println("Test null, empty, single element inputs");
        System.out.println("Test boundary values\n");

        System.out.println("--- Complex Objects ---");
        System.out.println("Test state changes across operations");
        System.out.println("Test error conditions");

        System.out.println("\n=== All solutions completed ===");
    }
}
