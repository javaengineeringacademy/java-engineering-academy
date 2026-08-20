package academy.javaengineering.testing.solutions;

import java.util.*;

/**
 * Mockito Advanced Solutions
 * Complete solutions for spying, argument matchers, and BDD
 */
class MockitoAdvancedSolutions {

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
    // Exercise 1: Argument Matchers Solution
    // ============================================

    /*
     * @Mock PriceCalculator priceCalculator;
     * @Mock InventoryService inventoryService;
     * @InjectMocks OrderService orderService;
     * 
     * @Test
     * void testArgThat() {
     *     // Arrange - Only match product IDs starting with "PROD-"
     *     when(priceCalculator.calculatePrice(
     *         argThat(id -> id.startsWith("PROD-")),
     *         anyInt(),
     *         anyString()
     *     )).thenReturn(100.0);
     * 
     *     // Act & Assert
     *     when(inventoryService.isInStock(anyString(), anyInt())).thenReturn(true);
     *     assertEquals(100.0, orderService.placeOrder("PROD-001", 1, "NONE"));
     *     assertEquals(100.0, orderService.placeOrder("PROD-002", 2, "DISC10"));
     * }
     * 
     * @Test
     * void testArgThatQuantity() {
     *     when(inventoryService.isInStock(anyString(), argThat(qty -> qty > 0 && qty <= 100)))
     *         .thenReturn(true);
     *     when(priceCalculator.calculatePrice(anyString(), anyInt(), anyString()))
     *         .thenReturn(50.0);
     * 
     *     assertEquals(50.0, orderService.placeOrder("PROD-001", 5, "NONE"));
     * }
     */

    // ============================================
    // Exercise 2: Argument Answer Solution
    // ============================================

    /*
     * @Test
     * void testArgumentAnswer() {
     *     // Arrange - Return price based on quantity
     *     when(priceCalculator.calculatePrice(anyString(), anyInt(), anyString()))
     *         .thenAnswer(invocation -> {
     *             String productId = invocation.getArgument(0);
     *             int quantity = invocation.getArgument(1);
     *             return quantity * 10.0; // $10 per item
     *         });
     * 
     *     when(inventoryService.isInStock(anyString(), anyInt())).thenReturn(true);
     * 
     *     // Act & Assert
     *     assertEquals(50.0, orderService.placeOrder("PROD-001", 5, "NONE"));
     *     assertEquals(100.0, orderService.placeOrder("PROD-001", 10, "NONE"));
     *     assertEquals(25.0, orderService.placeOrder("PROD-001", 2, "DISC10"));
     * }
     * 
     * @Test
     * void testConditionalAnswer() {
     *     when(priceCalculator.calculatePrice(anyString(), anyInt(), anyString()))
     *         .thenAnswer(invocation -> {
     *             String discountCode = invocation.getArgument(2);
     *             int quantity = invocation.getArgument(1);
     *             double basePrice = quantity * 10.0;
     *             if ("SAVE20".equals(discountCode)) return basePrice * 0.8;
     *             if ("SAVE10".equals(discountCode)) return basePrice * 0.9;
     *             return basePrice;
     *         });
     * 
     *     when(inventoryService.isInStock(anyString(), anyInt())).thenReturn(true);
     * 
     *     assertEquals(50.0, orderService.placeOrder("PROD-001", 5, "NONE"));
     *     assertEquals(40.0, orderService.placeOrder("PROD-001", 5, "SAVE20"));
     *     assertEquals(45.0, orderService.placeOrder("PROD-001", 5, "SAVE10"));
     * }
     */

    // ============================================
    // Exercise 3: Spy Solution
    // ============================================

    /*
     * @Test
     * void testSpy() {
     *     // Create a real ArrayList with elements
     *     List<String> realList = new ArrayList<>(List.of("A", "B", "C"));
     *     List<String> spyList = spy(realList);
     * 
     *     // Stub only size() method
     *     when(spyList.size()).thenReturn(100);
     * 
     *     // Assert - stubbed method returns mocked value
     *     assertEquals(100, spyList.size());
     * 
     *     // Real methods still work
     *     assertEquals("A", spyList.get(0));
     *     assertTrue(spyList.contains("B"));
     *     assertFalse(spyList.isEmpty());
     * 
     *     // Verify interaction
     *     verify(spyList).get(0);
     *     verify(spyList).contains("B");
     * }
     * 
     * @Test
     * void testSpyDoReturn() {
     *     List<String> realList = new ArrayList<>(List.of("X", "Y"));
     *     List<String> spyList = spy(realList);
     * 
     *     // doReturn is safer - doesn't call real method
     *     doReturn(10).when(spyList).size();
     *     doReturn("Z").when(spyList).get(0);
     * 
     *     assertEquals(10, spyList.size());
     *     assertEquals("Z", spyList.get(0));
     * }
     */

    // ============================================
    // Exercise 4: BDD Style Solution
    // ============================================

    /*
     * @Test
     * void testBDDStyle() {
     *     // Given
     *     given(inventoryService.isInStock("PROD-001", 1)).willReturn(true);
     *     given(priceCalculator.calculatePrice("PROD-001", 1, "NONE")).willReturn(25.0);
     * 
     *     // When
     *     double result = orderService.placeOrder("PROD-001", 1, "NONE");
     * 
     *     // Then
     *     then(result).should().isEqualTo(25.0);
     *     then(inventoryService).should().isInStock("PROD-001", 1);
     *     then(priceCalculator).should().calculatePrice("PROD-001", 1, "NONE");
     * }
     * 
     * @Test
     * void testBDDStyleException() {
     *     // Given
     *     given(inventoryService.isInStock("PROD-001", 1)).willReturn(false);
     * 
     *     // When & Then
     *     thenThrownBy(() -> orderService.placeOrder("PROD-001", 1, "NONE"))
     *         .isInstanceOf(IllegalStateException.class)
     *         .hasMessageContaining("not in stock");
     * }
     */

    // ============================================
    // Exercise 5: InOrder Verification Solution
    // ============================================

    /*
     * @Test
     * void testInOrder() {
     *     // Arrange
     *     when(inventoryService.isInStock("PROD-001", 1)).thenReturn(true);
     *     when(priceCalculator.calculatePrice("PROD-001", 1, "NONE")).thenReturn(25.0);
     * 
     *     // Act
     *     orderService.placeOrder("PROD-001", 1, "NONE");
     * 
     *     // Assert - Verify order of calls
     *     InOrder inOrder = inOrder(inventoryService, priceCalculator);
     *     inOrder.verify(inventoryService).isInStock("PROD-001", 1);
     *     inOrder.verify(priceCalculator).calculatePrice("PROD-001", 1, "NONE");
     * }
     * 
     * @Test
     * void testOrderLog() {
     *     when(inventoryService.isInStock("PROD-001", 1)).thenReturn(true);
     *     when(priceCalculator.calculatePrice("PROD-001", 1, "NONE")).thenReturn(25.0);
     * 
     *     orderService.placeOrder("PROD-001", 1, "NONE");
     * 
     *     assertEquals(1, orderService.getOrderLog().size());
     *     assertTrue(orderService.getOrderLog().get(0).contains("PROD-001"));
     * }
     */

    public static void main(String[] args) {
        System.out.println("=== Mockito Advanced Solutions ===\n");
        System.out.println("These are reference solutions for advanced Mockito exercises.\n");

        System.out.println("--- Argument Matchers ---");
        System.out.println("argThat(condition) - custom matching");
        System.out.println("anyString(), anyInt() - any value matching");
        System.out.println("eq(value) - exact value matching\n");

        System.out.println("--- Argument Answer ---");
        System.out.println("thenAnswer(invocation -> { ... }) - dynamic response");
        System.out.println("invocation.getArgument(index) - get method arguments\n");

        System.out.println("--- Spy ---");
        System.out.println("spy(realObject) - partial mocking");
        System.out.println("doReturn().when(spy) - safer stubbing\n");

        System.out.println("--- BDD Style ---");
        System.out.println("given().willReturn() - arrange");
        System.out.println("then().should().method() - assert interactions\n");

        System.out.println("--- InOrder ---");
        System.out.println("InOrder inOrder = inOrder(mock1, mock2)");
        System.out.println("inOrder.verify(mock1).method()");

        System.out.println("\n=== All solutions completed ===");
    }
}
