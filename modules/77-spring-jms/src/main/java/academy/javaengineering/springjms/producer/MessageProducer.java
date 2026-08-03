package academy.javaengineering.springjms.producer;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import academy.javaengineering.springjms.model.OrderMessage;
import academy.javaengineering.springjms.model.NotificationMessage;

/**
 * Message producer using JmsTemplate.
 */
@Component
public class MessageProducer {

    private final JmsTemplate jmsTemplate;

    public MessageProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendOrder(OrderMessage message) {
        jmsTemplate.convertAndSend("order-queue", message);
        System.out.println("Order sent: " + message.getOrderId());
    }

    public void sendNotification(NotificationMessage message) {
        jmsTemplate.convertAndSend("notification-queue", message);
        System.out.println("Notification sent: " + message.getNotificationId());
    }

    public void sendToTopic(NotificationMessage message) {
        jmsTemplate.convertAndSend("notification-topic", message);
        System.out.println("Notification published to topic: " + message.getNotificationId());
    }
}
