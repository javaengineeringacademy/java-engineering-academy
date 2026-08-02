package academy.javaengineering.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Demonstrates RabbitMQ message producer.
 */
@Component
public class MessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public MessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrderMessage(RabbitMqMessage.OrderMessage message) {
        rabbitTemplate.convertAndSend("order.exchange", "order.created", message);
        System.out.println("Order message sent: " + message.orderId());
    }

    public void sendNotification(RabbitMqMessage.NotificationMessage message) {
        rabbitTemplate.convertAndSend("notification.exchange", "notification.send", message);
        System.out.println("Notification sent to: " + message.userId());
    }

    public void publishEvent(RabbitMqMessage.EventMessage event) {
        rabbitTemplate.convertAndSend("event.exchange", event.eventType(), event);
        System.out.println("Event published: " + event.eventType());
    }
}
