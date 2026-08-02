package academy.javaengineering.hibernate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Hibernate Tests")
class HibernateTest {

    @Test
    @DisplayName("Order should be created with default status")
    void testOrderCreation() {
        Order order = new Order("ORD-001", 99.99);
        
        assertEquals("ORD-001", order.getOrderNumber());
        assertEquals(99.99, order.getTotalAmount(), 0.01);
        assertEquals(Order.OrderStatus.PENDING, order.getStatus());
        assertNotNull(order.getCreatedAt());
    }

    @Test
    @DisplayName("Customer should have empty orders by default")
    void testCustomerCreation() {
        Customer customer = new Customer("John", "john@example.com");
        
        assertEquals("John", customer.getName());
        assertEquals("john@example.com", customer.getEmail());
        assertNotNull(customer.getOrders());
        assertTrue(customer.getOrders().isEmpty());
    }

    @Test
    @DisplayName("Order status should change correctly")
    void testOrderStatusChange() {
        Order order = new Order("ORD-002", 150.00);
        
        assertEquals(Order.OrderStatus.PENDING, order.getStatus());
        order.setStatus(Order.OrderStatus.CONFIRMED);
        assertEquals(Order.OrderStatus.CONFIRMED, order.getStatus());
        order.setStatus(Order.OrderStatus.SHIPPED);
        assertEquals(Order.OrderStatus.SHIPPED, order.getStatus());
    }
}
