package academy.javaengineering.springintegration;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demonstrates Spring Integration MessagingGateway for
 * request-reply and one-way messaging patterns.
 */
public class GatewayExample {

    /**
     * Configuration class for gateway integration.
     */
    @Configuration
    @EnableIntegration
    public static class GatewayConfig {

        @Bean
        public MessageChannel requestChannel() {
            return new DirectChannel();
        }

        @Bean
        public MessageChannel replyChannel() {
            return new QueueChannel();
        }

        @Bean
        public MessageChannel oneWayChannel() {
            return new DirectChannel();
        }

        @Bean
        public OrderGateway orderGateway() {
            return new OrderGatewayImpl();
        }

        @Bean
        public NotificationGateway notificationGateway() {
            return new NotificationGatewayImpl();
        }
    }

    /**
     * Messaging gateway interface for order processing.
     */
    @MessagingGateway(defaultRequestChannel = "requestChannel",
            defaultReplyChannel = "replyChannel")
    public interface OrderGateway {

        @Gateway(requestChannel = "requestChannel")
        String processOrder(@Payload String orderData);

        @Gateway(requestChannel = "requestChannel")
        String processOrderWithPriority(@Payload String orderData,
                                        @Header("priority") String priority);

        CompletableFuture<String> processOrderAsync(@Payload String orderData);
    }

    /**
     * One-way messaging gateway for notifications.
     */
    @MessagingGateway(defaultRequestChannel = "oneWayChannel")
    public interface NotificationGateway {

        void sendNotification(@Payload String notification);

        void sendNotificationWithSource(@Payload String notification,
                                        @Header("source") String source);
    }

    /**
     * Order gateway implementation for demo purposes.
     */
    public static class OrderGatewayImpl implements OrderGateway {

        private final AtomicInteger orderCount = new AtomicInteger(0);
        private final List<String> processedOrders = new ArrayList<>();

        @Override
        public String processOrder(String orderData) {
            int count = orderCount.incrementAndGet();
            String result = "ORDER-" + count + ": Processed [" + orderData + "]";
            processedOrders.add(result);
            System.out.println("OrderGateway: " + result);
            return result;
        }

        @Override
        public String processOrderWithPriority(String orderData, String priority) {
            int count = orderCount.incrementAndGet();
            String result = "ORDER-" + count + " [" + priority + "]: " + orderData;
            processedOrders.add(result);
            System.out.println("OrderGateway (Priority): " + result);
            return result;
        }

        @Override
        public CompletableFuture<String> processOrderAsync(String orderData) {
            return CompletableFuture.supplyAsync(() -> {
                int count = orderCount.incrementAndGet();
                String result = "ASYNC-ORDER-" + count + ": " + orderData;
                processedOrders.add(result);
                System.out.println("OrderGateway (Async): " + result);
                return result;
            });
        }

        public int getOrderCount() {
            return orderCount.get();
        }

        public List<String> getProcessedOrders() {
            return List.copyOf(processedOrders);
        }
    }

    /**
     * Notification gateway implementation for demo purposes.
     */
    public static class NotificationGatewayImpl implements NotificationGateway {

        private final List<String> notifications = new ArrayList<>();

        @Override
        public void sendNotification(String notification) {
            notifications.add(notification);
            System.out.println("Notification: " + notification);
        }

        @Override
        public void sendNotificationWithSource(String notification, String source) {
            String enriched = "[" + source + "] " + notification;
            notifications.add(enriched);
            System.out.println("Notification (Source): " + enriched);
        }

        public List<String> getNotifications() {
            return List.copyOf(notifications);
        }
    }

    /**
     * Demonstrates request-reply gateway pattern.
     */
    public static void demonstrateRequestReply() {
        System.out.println("=== Request-Reply Gateway ===");

        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(GatewayConfig.class)) {

            OrderGateway gateway = context.getBean(OrderGateway.class);

            String result1 = gateway.processOrder("Laptop x1");
            System.out.println("Reply 1: " + result1);

            String result2 = gateway.processOrderWithPriority("Phone x2", "HIGH");
            System.out.println("Reply 2: " + result2);
        }
    }

    /**
     * Demonstrates one-way gateway pattern.
     */
    public static void demonstrateOneWay() {
        System.out.println("\n=== One-Way Gateway ===");

        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(GatewayConfig.class)) {

            NotificationGateway gateway = context.getBean(NotificationGateway.class);

            gateway.sendNotification("Server is down!");
            gateway.sendNotificationWithSource("High CPU usage", "monitoring");
            gateway.sendNotification("Backup completed");
        }
    }

    /**
     * Demonstrates async gateway pattern.
     */
    public static void demonstrateAsyncGateway() throws Exception {
        System.out.println("\n=== Async Gateway ===");

        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(GatewayConfig.class)) {

            OrderGateway gateway = context.getBean(OrderGateway.class);

            CompletableFuture<String> future1 = gateway.processOrderAsync("Tablet x1");
            CompletableFuture<String> future2 = gateway.processOrderAsync("Monitor x2");

            CompletableFuture.allOf(future1, future2).join();

            System.out.println("Async Result 1: " + future1.get());
            System.out.println("Async Result 2: " + future2.get());
        }
    }

    /**
     * Runs all gateway demonstrations.
     */
    public static void main(String[] args) throws Exception {
        demonstrateRequestReply();
        demonstrateOneWay();
        demonstrateAsyncGateway();
    }
}
