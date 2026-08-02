package academy.javaengineering.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Demonstrates RabbitMQ message consumer.
 */
@Component
public class MessageConsumer {

    @RabbitListener(queues = "order.queue")
    public void handleOrderMessage(RabbitMqMessage.OrderMessage message) {
        System.out.println("Processing order: " + message.orderId());
        // Process order logic
    }

    @RabbitListener(queues = "notification.queue")
    public void handleNotification(RabbitMqMessage.NotificationMessage message) {
        System.out.println("Sending notification to: " + message.userId());
        // Send notification logic
    }

    @RabbitListener(queues = "event.queue")
    public void handleEvent(RabbitMqMessage.EventMessage event) {
        System.out.println("Handling event: " + event.eventType());
        // Handle event logic
    }
}
