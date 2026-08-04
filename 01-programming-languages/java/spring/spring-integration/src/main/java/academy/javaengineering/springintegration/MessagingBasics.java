package academy.javaengineering.springintegration;

import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.endpoint.SourcePollingChannelAdapter;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Demonstrates Spring Integration messaging basics including
 * Message, MessageChannel, MessageHandler, and channel types.
 */
public class MessagingBasics {

    /**
     * Simple message payload for demonstration.
     */
    public record Order(String orderId, String product, int quantity, double price) {
    }

    /**
     * Demonstrates creating and sending messages with headers.
     */
    public static void demonstrateMessages() {
        System.out.println("=== Message Basics ===");

        Order order = new Order("ORD-001", "Laptop", 1, 999.99);
        Message<Order> message = MessageBuilder
                .withPayload(order)
                .setHeader("priority", "HIGH")
                .setHeader("source", "WEB")
                .setHeader("retry-count", 0)
                .build();

        System.out.println("Payload: " + message.getPayload());
        System.out.println("Headers: " + message.getHeaders());
        System.out.println("ID: " + message.getHeaders().getId());
        System.out.println("Timestamp: " + message.getHeaders().getTimestamp());
    }

    /**
     * Demonstrates DirectChannel for point-to-point messaging.
     */
    public static void demonstrateDirectChannel() {
        System.out.println("\n=== DirectChannel ===");

        DirectChannel channel = new DirectChannel();
        List<String> receivedMessages = new ArrayList<>();

        channel.subscribe(message -> {
            System.out.println("Received: " + message.getPayload());
            receivedMessages.add((String) message.getPayload());
        });

        channel.send(MessageBuilder.withPayload("Hello Direct").build());
        channel.send(MessageBuilder.withPayload("Second Message").build());

        System.out.println("Messages received: " + receivedMessages.size());
    }

    /**
     * Demonstrates PublishSubscribeChannel for broadcast messaging.
     */
    public static void demonstratePublishSubscribeChannel() {
        System.out.println("\n=== PublishSubscribeChannel ===");

        PublishSubscribeChannel channel = new PublishSubscribeChannel();
        List<String> subscriber1Messages = new ArrayList<>();
        List<String> subscriber2Messages = new ArrayList<>();

        channel.subscribe(message -> {
            subscriber1Messages.add((String) message.getPayload());
            System.out.println("Subscriber 1: " + message.getPayload());
        });

        channel.subscribe(message -> {
            subscriber2Messages.add((String) message.getPayload());
            System.out.println("Subscriber 2: " + message.getPayload());
        });

        channel.send(MessageBuilder.withPayload("Broadcast Message").build());

        System.out.println("Subscriber 1 received: " + subscriber1Messages.size());
        System.out.println("Subscriber 2 received: " + subscriber2Messages.size());
    }

    /**
     * Demonstrates QueueChannel for buffered messaging.
     */
    public static void demonstrateQueueChannel() {
        System.out.println("\n=== QueueChannel ===");

        QueueChannel queueChannel = new QueueChannel(10);
        List<String> receivedMessages = new ArrayList<>();

        queueChannel.send(MessageBuilder.withPayload("Queued Message 1").build());
        queueChannel.send(MessageBuilder.withPayload("Queued Message 2").build());
        queueChannel.send(MessageBuilder.withPayload("Queued Message 3").build());

        for (int i = 0; i < 3; i++) {
            Message<?> msg = queueChannel.receive(1000);
            if (msg != null) {
                receivedMessages.add((String) msg.getPayload());
                System.out.println("Received from queue: " + msg.getPayload());
            }
        }

        System.out.println("Queue size: " + queueChannel.getQueueSize());
    }

    /**
     * Demonstrates ExecutorChannel for async messaging.
     */
    public static void demonstrateExecutorChannel() throws InterruptedException {
        System.out.println("\n=== ExecutorChannel ===");

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("async-");
        executor.initialize();

        ExecutorChannel executorChannel = new ExecutorChannel(executor);
        CountDownLatch latch = new CountDownLatch(3);
        AtomicReference<String> threadName = new AtomicReference<>();

        executorChannel.subscribe(message -> {
            threadName.set(Thread.currentThread().getName());
            System.out.println("Processed on: " + Thread.currentThread().getName());
            System.out.println("Payload: " + message.getPayload());
            latch.countDown();
        });

        executorChannel.send(MessageBuilder.withPayload("Async Task 1").build());
        executorChannel.send(MessageBuilder.withPayload("Async Task 2").build());
        executorChannel.send(MessageBuilder.withPayload("Async Task 3").build());

        latch.await(5, TimeUnit.SECONDS);
        System.out.println("Processing thread: " + threadName.get());

        executor.shutdown();
    }

    /**
     * Demonstrates MessageHandler interface implementation.
     */
    public static void demonstrateMessageHandler() {
        System.out.println("\n=== MessageHandler ===");

        DirectChannel channel = new DirectChannel();

        channel.subscribe(new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                System.out.println("Custom handler received: " + message.getPayload());
                System.out.println("Headers: " + message.getHeaders());
            }
        });

        channel.send(MessageBuilder
                .withPayload("Handler Demo")
                .setHeader("custom-key", "custom-value")
                .build());
    }

    /**
     * Demonstrates message conversion and transformation patterns.
     */
    public static void demonstrateMessageConversion() {
        System.out.println("\n=== Message Conversion ===");

        Order order = new Order("ORD-100", "Phone", 2, 599.99);
        Message<Order> orderMessage = MessageBuilder.withPayload(order).build();

        String csv = order.orderId() + "," + order.product() + ","
                + order.quantity() + "," + order.price();
        Message<String> csvMessage = MessageBuilder
                .withPayload(csv)
                .copyHeaders(orderMessage.getHeaders())
                .build();

        System.out.println("Original: " + orderMessage.getPayload());
        System.out.println("Converted: " + csvMessage.getPayload());
    }

    /**
     * Runs all messaging demonstrations.
     */
    public static void main(String[] args) throws InterruptedException {
        demonstrateMessages();
        demonstrateDirectChannel();
        demonstratePublishSubscribeChannel();
        demonstrateQueueChannel();
        demonstrateExecutorChannel();
        demonstrateMessageHandler();
        demonstrateMessageConversion();
    }
}
