package academy.javaengineering.testing.practices;

import java.util.*;

/**
 * Mockito Advanced Exercises
 * Practice spying, argument matchers, and BDD style
 */
class MockitoAdvancedExercises {

    // ============================================
    // Classes to Work With
    // ============================================

    interface PriceCalculator {
        double calculatePrice(String productId, int quantity, String discountCode);
    }

    interface InventoryService {
        boolean isInStock(String productId, int quantity);
        int getAvailableQuantity(String productId);
    }

    static class OrderService {
        private final PriceCalculator priceCalculator;
        private final InventoryService inventoryService;
        private final List<String> orderLog = new ArrayList<>();

        OrderService(PriceCalculator priceCalculator, InventoryService inventoryService) {
            this.priceCalculator = priceCalculator;
            this.inventoryService = inventoryService;
        }

        double placeOrder(String productId, int quantity, String discountCode) {
            if (!inventoryService.isInStock(productId, quantity)) {
                throw new IllegalStateException("Product not in stock");
            }
            double price = priceCalculator.calculatePrice(productId, quantity, discountCode);
            orderLog.add("Order placed: " + productId + " x" + quantity);
            return price;
        }

        List<String> getOrderLog() {
            return Collections.unmodifiableList(orderLog);
        }
    }

    // ============================================
    // Exercise 1: Argument Matchers
    // ============================================
    // TODO: Use argument matchers for flexible stubbing

    /*
     * @Test
     * void testArgThat() {
     *     // Stub calculatePrice with argThat matcher
     *     // Only match product IDs starting with "PROD-"
     * }
     */

    // ============================================
    // Exercise 2: Argument Answer
    // ============================================
    // TODO: Create dynamic responses based on arguments

    /*
     * @Test
     * void testArgumentAnswer() {
     *     // Stub calculatePrice to return quantity * 10.0
     *     // Use thenAnswer with invocation
     * }
     */

    // ============================================
    // Exercise 3: Spy
    // ============================================
    // TODO: Partial mocking with spy

    /*
     * @Test
     * void testSpy() {
     *     // Create a real ArrayList with elements
     *     // Create a spy on it
     *     // Stub size() to return 100
     *     // Verify get() still calls real method
     * }
     */

    // ============================================
    // Exercise 4: BDD Style
    // ============================================
    // TODO: Use given/when/then style

    /*
     * @Test
     * void testBDDStyle() {
     *     // Given: stub inventory and price calculator
     *     // When: place an order
     *     // Then: verify interactions using then().should()
     * }
     */

    // ============================================
    // Exercise 5: InOrder Verification
    // ============================================
    // TODO: Verify method call order

    /*
     * @Test
     * void testInOrder() {
     *     // Stub dependencies
     *     // Place an order
     *     // Verify that repository is called before price calculator
     * }
     */

    public static void main(String[] args) {
        System.out.println("=== Mockito Advanced Exercises ===");
        System.out.println("Practice spying, argument matchers, and BDD style.");
    }
}
