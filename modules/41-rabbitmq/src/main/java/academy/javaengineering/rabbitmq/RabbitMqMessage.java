package academy.javaengineering.rabbitmq;

/**
 * Demonstrates RabbitMQ message types.
 */
public class RabbitMqMessage {

    public record OrderMessage(
        String orderId,
        String customerId,
        double amount,
        String timestamp
    ) {}

    public record NotificationMessage(
        String userId,
        String type,
        String subject,
        String body
    ) {}

    public record EventMessage(
        String eventId,
        String eventType,
        String payload,
        String source
    ) {}
}
