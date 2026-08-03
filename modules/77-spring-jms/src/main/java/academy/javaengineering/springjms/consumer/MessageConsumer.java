package academy.javaengineering.springjms.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import academy.javaengineering.springjms.model.OrderMessage;
import academy.javaengineering.springjms.model.NotificationMessage;

/**
 * Message consumer using @JmsListener.
 */
@Component
public class MessageConsumer {

    @JmsListener(destination = "order-queue")
    public void receiveOrder(OrderMessage message) {
        System.out.println("Processing order: " + message.getOrderId());
        System.out.println("Customer: " + message.getCustomerId());
        System.out.println("Amount: $" + message.getAmount());
        
        // Process order logic
        message.setStatus("PROCESSED");
        System.out.println("Order processed: " + message);
    }

    @JmsListener(destination = "notification-queue")
    public void receiveNotification(NotificationMessage message) {
        System.out.println("Sending notification to: " + message.getUserId());
        System.out.println("Type: " + message.getType());
        System.out.println("Subject: " + message.getSubject());
        
        // Send notification logic
    }

    @JmsListener(destination = "notification-topic")
    public void receiveTopicNotification(NotificationMessage message) {
        System.out.println("Topic notification received: " + message.getNotificationId());
    }
}
