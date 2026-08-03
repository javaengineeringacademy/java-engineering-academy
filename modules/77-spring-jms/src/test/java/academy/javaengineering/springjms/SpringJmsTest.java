package academy.javaengineering.springjms;

import academy.javaengineering.springjms.model.OrderMessage;
import academy.javaengineering.springjms.model.NotificationMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Spring JMS Tests")
class SpringJmsTest {

    @Test
    @DisplayName("OrderMessage should be created correctly")
    void testOrderMessage() {
        OrderMessage message = new OrderMessage("ORD-001", "CUST-123", 99.99);
        
        assertEquals("ORD-001", message.getOrderId());
        assertEquals("CUST-123", message.getCustomerId());
        assertEquals(99.99, message.getAmount(), 0.01);
        assertEquals("CREATED", message.getStatus());
        assertNotNull(message.getTimestamp());
    }

    @Test
    @DisplayName("NotificationMessage should be created correctly")
    void testNotificationMessage() {
        NotificationMessage message = new NotificationMessage(
            "USER-456", "EMAIL", "Welcome!", "Hello there!"
        );
        
        assertEquals("USER-456", message.getUserId());
        assertEquals("EMAIL", message.getType());
        assertEquals("Welcome!", message.getSubject());
        assertNotNull(message.getNotificationId());
        assertNotNull(message.getTimestamp());
    }

    @Test
    @DisplayName("OrderMessage status should be updatable")
    void testOrderStatusUpdate() {
        OrderMessage message = new OrderMessage("ORD-002", "CUST-456", 150.00);
        
        assertEquals("CREATED", message.getStatus());
        message.setStatus("PROCESSED");
        assertEquals("PROCESSED", message.getStatus());
        message.setStatus("SHIPPED");
        assertEquals("SHIPPED", message.getStatus());
    }
}
