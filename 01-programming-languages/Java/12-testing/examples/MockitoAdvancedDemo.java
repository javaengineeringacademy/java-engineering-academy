package academy.javaengineering.testing.examples;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Mockito Advanced - Spying, Argument Matchers, Answer, BDD
 */
@ExtendWith(MockitoExtension.class)
class MockitoAdvancedDemo {

    // ============================================
    // Classes to Demonstrate Advanced Features
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
    // Advanced Argument Matchers
    // ============================================

    @Mock
    PriceCalculator priceCalculator;

    @Mock
    InventoryService inventoryService;

    @InjectMocks
    OrderService orderService;

    @Test
    @DisplayName("ArgumentMatchers - argThat with custom condition")
    void testArgThat() {
        // Arrange
        when(priceCalculator.calculatePrice(
            argThat(id -> id.startsWith("PROD-")),
            anyInt(),
            anyString()
        )).thenReturn(100.0);

        // Act & Assert
        assertEquals(100.0, orderService.placeOrder("PROD-001", 1, "NONE"));
        assertEquals(100.0, orderService.placeOrder("PROD-002", 2, "DISC10"));
    }

    @Test
    @DisplayName("ArgumentMatchers - argThat for quantity validation")
    void testArgThatQuantity() {
        // Arrange
        when(inventoryService.isInStock(anyString(), argThat(qty -> qty > 0 && qty <= 100)))
            .thenReturn(true);
        when(priceCalculator.calculatePrice(anyString(), anyInt(), anyString()))
            .thenReturn(50.0);

        // Act & Assert
        assertEquals(50.0, orderService.placeOrder("PROD-001", 5, "NONE"));
    }

    // ============================================
    // Argument Answer - Dynamic Responses
    // ============================================

    @Test
    @DisplayName("ArgumentAnswer - dynamic response based on arguments")
    void testArgumentAnswer() {
        // Arrange - Return price based on quantity
        when(priceCalculator.calculatePrice(anyString(), anyInt(), anyString()))
            .thenAnswer(invocation -> {
                String productId = invocation.getArgument(0);
                int quantity = invocation.getArgument(1);
                return quantity * 10.0; // $10 per item
            });

        // Act & Assert
        assertEquals(50.0, orderService.placeOrder("PROD-001", 5, "NONE"));
        assertEquals(100.0, orderService.placeOrder("PROD-001", 10, "NONE"));
    }

    // ============================================
    // Spy - Partial Mocking
    // ============================================

    @Test
    @DisplayName("Spy - partial mocking of real object")
    void testSpyAdvanced() {
        // Arrange
        List<String> realList = new ArrayList<>(List.of("A", "B", "C"));
        List<String> spyList = spy(realList);

        // Stub only specific method
        when(spyList.size()).thenReturn(100);

        // Assert - stubbed method returns mocked value
        assertEquals(100, spyList.size());

        // Real methods still work
        assertEquals("A", spyList.get(0));
        assertTrue(spyList.contains("B"));

        // Verify interaction
        verify(spyList).get(0);
        verify(spyList).contains("B");
    }

    @Test
    @DisplayName("Spy - doReturn to avoid calling real method")
    void testSpyDoReturn() {
        List<String> realList = new ArrayList<>(List.of("A", "B"));
        List<String> spyList = spy(realList);

        // doReturn is safer - doesn't call real method
        doReturn(10).when(spyList).size();
        doReturn("X").when(spyList).get(0);

        assertEquals(10, spyList.size());
        assertEquals("X", spyList.get(0));
    }

    // ============================================
    // BDD Style (Given-When-Then)
    // ============================================

    @Test
    @DisplayName("BDDMockito - Given-When-Then style")
    void testBDDStyle() {
        // Arrange (Given)
        given(inventoryService.isInStock("PROD-001", 1)).willReturn(true);
        given(priceCalculator.calculatePrice("PROD-001", 1, "NONE")).willReturn(25.0);

        // Act (When)
        double result = orderService.placeOrder("PROD-001", 1, "NONE");

        // Assert (Then)
        then(result).should().isEqualTo(25.0);
        then(inventoryService).should().isInStock("PROD-001", 1);
        then(priceCalculator).should().calculatePrice("PROD-001", 1, "NONE");
    }

    // ============================================
    // Additional Answers
    // ============================================

    @Test
    @DisplayName("AdditionalAnswers - Returns arguments")
    void testAdditionalAnswers() {
        // Arrange - Return the first argument passed
        when(priceCalculator.calculatePrice(anyString(), anyInt(), anyString()))
            .thenAnswer(invocation -> invocation.getArgument(1) * 15.0);

        // Act
        double result = orderService.placeOrder("PROD-001", 3, "NONE");

        // Assert
        assertEquals(45.0, result);
    }

    // ============================================
    // Mocking Void Methods
    // ============================================

    @Test
    @DisplayName("Mocking void methods with doNothing/doThrow")
    void testVoidMethods() {
        // Arrange
        InventoryService mockInventory = mock(InventoryService.class);
        doNothing().when(mockInventory).someVoidMethod(anyString());
        doThrow(new RuntimeException("Error")).when(mockInventory).failingVoidMethod(anyString());
    }

    // ============================================
    // Reset Mock
    // ============================================

    @Test
    @DisplayName("Reset mock - clear all stubbings")
    void testResetMock() {
        // Arrange
        InventoryService mockInventory = mock(InventoryService.class);
        when(mockInventory.getAvailableQuantity("PROD-001")).thenReturn(10);

        // Act & Assert - Before reset
        assertEquals(10, mockInventory.getAvailableQuantity("PROD-001"));

        // Reset
        reset(mockInventory);

        // After reset - stubbing is cleared
        assertEquals(0, mockInventory.getAvailableQuantity("PROD-001"));
    }

    // ============================================
    // Strict Stubs
    // ============================================

    @Test
    @DisplayName("StrictStubs - detect unnecessary stubbings")
    void testStrictStubs() {
        // Arrange - Strict mode will fail if there are unnecessary stubbings
        when(priceCalculator.calculatePrice("PROD-001", 1, "NONE")).thenReturn(25.0);

        // Act
        double result = orderService.placeOrder("PROD-001", 1, "NONE");

        // Assert
        assertEquals(25.0, result);
    }

    // ============================================
    // Mocking Final Classes and Methods
    // ============================================

    static final class FinalClass {
        final String finalMethod() {
            return "final";
        }
    }

    @Test
    @DisplayName("Mocking final classes (requires mockito-extensions)")
    void testFinalClass() {
        // With Mockito inline mock maker, final classes can be mocked
        FinalClass mock = mock(FinalClass.class);
        when(mock.finalMethod()).thenReturn("mocked");
        assertEquals("mocked", mock.finalMethod());
    }
}
