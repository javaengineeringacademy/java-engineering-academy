package academy.javaengineering.testing.integration.practices;

import org.junit.jupiter.api.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercise 2: Service Integration Test
 *
 * Tasks:
 * 1. Test service with real repository
 * 2. Test transaction-like behavior
 * 3. Test error propagation
 * 4. Test multi-step operations
 */
class Exercise2ServiceIntegration {

    static class Order {
        private String id;
        private String productId;
        private int quantity;
        private double total;
        private String status;

        Order(String id, String productId, int quantity, double total) {
            this.id = id; this.productId = productId;
            this.quantity = quantity; this.total = total;
            this.status = "CREATED";
        }
        String getId() { return id; }
        String getStatus() { return status; }
        void setStatus(String status) { this.status = status; }
        double getTotal() { return total; }
    }

    static class OrderService {
        private final Map<String, Order> orders = new HashMap<>();

        Order createOrder(String productId, int quantity, double price) {
            String id = "ORD-" + (orders.size() + 1);
            Order order = new Order(id, productId, quantity, quantity * price);
            orders.put(id, order);
            return order;
        }

        void completeOrder(String orderId) {
            Order order = orders.get(orderId);
            if (order == null) throw new IllegalArgumentException("Order not found");
            order.setStatus("COMPLETED");
        }

        Optional<Order> getOrder(String orderId) {
            return Optional.ofNullable(orders.get(orderId));
        }
    }

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService();
    }

    @Test
    @DisplayName("should create order successfully")
    void shouldCreateOrder() {
        // Arrange, Act, Assert
    }

    @Test
    @DisplayName("should complete order")
    void shouldCompleteOrder() {
        // Arrange, Act, Assert
    }

    @Test
    @DisplayName("should fail completing non-existent order")
    void shouldFailOnNonExistentOrder() {
        // Arrange, Act, Assert
    }
}
