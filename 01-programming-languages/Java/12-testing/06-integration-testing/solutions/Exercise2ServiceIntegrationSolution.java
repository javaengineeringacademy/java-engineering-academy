package academy.javaengineering.testing.integration.solutions;

import org.junit.jupiter.api.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class Exercise2ServiceIntegrationSolution {

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
        Optional<Order> getOrder(String orderId) { return Optional.ofNullable(orders.get(orderId)); }
    }

    private OrderService orderService;

    @BeforeEach
    void setUp() { orderService = new OrderService(); }

    @Test
    void shouldCreateOrder() {
        Order order = orderService.createOrder("PROD-1", 3, 29.99);
        assertNotNull(order.getId());
        assertEquals("CREATED", order.getStatus());
        assertEquals(89.97, order.getTotal(), 0.01);
    }

    @Test
    void shouldCompleteOrder() {
        Order order = orderService.createOrder("PROD-1", 1, 10.0);
        orderService.completeOrder(order.getId());
        assertEquals("COMPLETED", orderService.getOrder(order.getId()).get().getStatus());
    }

    @Test
    void shouldFailOnNonExistentOrder() {
        assertThrows(IllegalArgumentException.class,
            () -> orderService.completeOrder("NON-EXISTENT"));
    }
}
