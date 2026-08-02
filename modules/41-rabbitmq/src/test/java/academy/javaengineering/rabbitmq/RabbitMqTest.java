package academy.javaengineering.rabbitmq;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RabbitMQ Tests")
class RabbitMqTest {

    @Test
    @DisplayName("OrderMessage should be created correctly")
    void testOrderMessage() {
        var message = new RabbitMqMessage.OrderMessage(
            "ORD-001", "CUST-123", 99.99, "2024-01-01T10:00:00"
        );
        
        assertEquals("ORD-001", message.orderId());
        assertEquals("CUST-123", message.customerId());
        assertEquals(99.99, message.amount(), 0.01);
    }

    @Test
    @DisplayName("NotificationMessage should be created correctly")
    void testNotificationMessage() {
        var message = new RabbitMqMessage.NotificationMessage(
            "USER-456", "EMAIL", "Welcome!", "Hello there!"
        );
        
        assertEquals("USER-456", message.userId());
        assertEquals("EMAIL", message.type());
        assertEquals("Welcome!", message.subject());
    }

    @Test
    @DisplayName("EventMessage should be created correctly")
    void testEventMessage() {
        var event = new RabbitMqMessage.EventMessage(
            "EVT-001", "ORDER_CREATED", "{\"orderId\":\"1\"}", "order-service"
        );
        
        assertEquals("EVT-001", event.eventId());
        assertEquals("ORDER_CREATED", event.eventType());
        assertEquals("order-service", event.source());
    }
}
