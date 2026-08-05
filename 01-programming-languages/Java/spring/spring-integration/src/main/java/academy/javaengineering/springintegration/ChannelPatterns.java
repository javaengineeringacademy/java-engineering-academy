package academy.javaengineering.springintegration;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.endpoint.EventDrivenConsumer;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrates various Spring Integration channel patterns
 * including point-to-point, pub-sub, executor, and queue channels.
 */
public class ChannelPatterns {

    /**
     * Configuration for channel patterns demo.
     */
    @Configuration
    @EnableIntegration
    public static class ChannelConfig {

        @Bean
        public MessageChannel directChannel() {
            return new DirectChannel();
        }

        @Bean
        public MessageChannel publishSubscribeChannel() {
            return new PublishSubscribeChannel();
        }

        @Bean
        public QueueChannel queueChannel() {
            return new QueueChannel(5);
        }

        @Bean
        public MessageChannel executorChannel() {
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(2);
            executor.setMaxPoolSize(4);
            executor.setQueueCapacity(10);
            executor.setThreadNamePrefix("exec-");
            executor.initialize();
            return new ExecutorChannel(executor);
        }

        @Bean
        public MessageChannel priorityChannel() {
            return new DirectChannel();
        }
    }

    /**
     * Demonstrates DirectChannel for point-to-point messaging.
     * Only one subscriber receives the message.
     */
    public static void demonstrateDirectChannel() {
        System.out.println("=== DirectChannel (Point-to-Point) ===");

        DirectChannel channel = new DirectChannel();
        List<String> subscriber1Messages = new CopyOnWriteArrayList<>();
        List<String> subscriber2Messages = new CopyOnWriteArrayList<>();

        channel.subscribe(message -> {
            subscriber1Messages.add((String) message.getPayload());
            System.out.println("Subscriber 1: " + message.getPayload());
        });

        channel.subscribe(message -> {
            subscriber2Messages.add((String) message.getPayload());
            System.out.println("Subscriber 2: " + message.getPayload());
        });

        channel.send(MessageBuilder.withPayload("Point-to-Point 1").build());
        channel.send(MessageBuilder.withPayload("Point-to-Point 2").build());

        System.out.println("Subscriber 1 count: " + subscriber1Messages.size());
        System.out.println("Subscriber 2 count: " + subscriber2Messages.size());
    }

    /**
     * Demonstrates PublishSubscribeChannel for broadcast messaging.
     * All subscribers receive the message.
     */
    public static void demonstratePublishSubscribeChannel() {
        System.out.println("\n=== PublishSubscribeChannel (Pub-Sub) ===");

        PublishSubscribeChannel channel = new PublishSubscribeChannel();
        List<String> allMessages = new CopyOnWriteArrayList<>();

        for (int i = 1; i <= 3; i++) {
            final int subscriberId = i;
            channel.subscribe(message -> {
                allMessages.add("S" + subscriberId + ": " + message.getPayload());
                System.out.println("Subscriber " + subscriberId + ": " + message.getPayload());
            });
        }

        channel.send(MessageBuilder.withPayload("Broadcast 1").build());
        channel.send(MessageBuilder.withPayload("Broadcast 2").build());

        System.out.println("Total messages received: " + allMessages.size());
    }

    /**
     * Demonstrates QueueChannel for buffered messaging.
     * Messages are stored in an internal queue.
     */
    public static void demonstrateQueueChannel() {
        System.out.println("\n=== QueueChannel (Buffered) ===");

        QueueChannel queueChannel = new QueueChannel(10);

        queueChannel.send(MessageBuilder.withPayload("Queued 1").build());
        queueChannel.send(MessageBuilder.withPayload("Queued 2").build());
        queueChannel.send(MessageBuilder.withPayload("Queued 3").build());

        System.out.println("Queue size before: " + queueChannel.getQueueSize());

        Message<?> msg1 = queueChannel.receive(1000);
        System.out.println("Received: " + (msg1 != null ? msg1.getPayload() : "null"));

        Message<?> msg2 = queueChannel.receive(1000);
        System.out.println("Received: " + (msg2 != null ? msg2.getPayload() : "null"));

        System.out.println("Queue size after: " + queueChannel.getQueueSize());

        Message<?> remaining = queueChannel.receive(1000);
        System.out.println("Remaining: " + (remaining != null ? remaining.getPayload() : "null"));
    }

    /**
     * Demonstrates ExecutorChannel for asynchronous messaging.
     * Messages are processed on separate threads.
     */
    public static void demonstrateExecutorChannel() throws InterruptedException {
        System.out.println("\n=== ExecutorChannel (Async) ===");

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("async-");
        executor.initialize();

        ExecutorChannel executorChannel = new ExecutorChannel(executor);
        List<String> processedMessages = new CopyOnWriteArrayList<>();
        CountDownLatch latch = new CountDownLatch(3);

        executorChannel.subscribe(message -> {
            String threadName = Thread.currentThread().getName();
            processedMessages.add(threadName + ": " + message.getPayload());
            System.out.println("Processed on " + threadName + ": " + message.getPayload());
            latch.countDown();
        });

        executorChannel.send(MessageBuilder.withPayload("Task 1").build());
        executorChannel.send(MessageBuilder.withPayload("Task 2").build());
        executorChannel.send(MessageBuilder.withPayload("Task 3").build());

        latch.await(5, TimeUnit.SECONDS);
        System.out.println("All tasks processed on different threads");
        System.out.println("Processed count: " + processedMessages.size());

        executor.shutdown();
    }

    /**
     * Demonstrates channel interceptors for monitoring.
     */
    public static void demonstrateChannelInterceptors() {
        System.out.println("\n=== Channel Interceptors ===");

        DirectChannel channel = new DirectChannel();
        List<String> interceptLog = new ArrayList<>();

        channel.addInterceptor(new org.springframework.messaging.support.ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel ch) {
                interceptLog.add("PRE: " + message.getPayload());
                System.out.println("Interceptor PRE: " + message.getPayload());
                return message;
            }

            @Override
            public void postSend(Message<?> message, MessageChannel ch, boolean sent) {
                interceptLog.add("POST: " + message.getPayload());
                System.out.println("Interceptor POST: " + message.getPayload());
            }
        });

        channel.subscribe(message ->
                System.out.println("Handler: " + message.getPayload()));

        channel.send(MessageBuilder.withPayload("Intercepted Message").build());

        System.out.println("Interceptor log size: " + interceptLog.size());
    }

    /**
     * Demonstrates message routing based on content.
     */
    public static void demonstrateContentBasedRouting() {
        System.out.println("\n=== Content-Based Routing ===");

        DirectChannel inputChannel = new DirectChannel();
        DirectChannel highPriorityChannel = new DirectChannel();
        DirectChannel normalPriorityChannel = new DirectChannel();

        List<String> highPriorityMessages = new CopyOnWriteArrayList<>();
        List<String> normalPriorityMessages = new CopyOnWriteArrayList<>();

        highPriorityChannel.subscribe(message -> {
            highPriorityMessages.add((String) message.getPayload());
            System.out.println("HIGH: " + message.getPayload());
        });

        normalPriorityChannel.subscribe(message -> {
            normalPriorityMessages.add((String) message.getPayload());
            System.out.println("NORMAL: " + message.getPayload());
        });

        inputChannel.subscribe(message -> {
            String payload = (String) message.getPayload();
            if (payload.contains("URGENT")) {
                highPriorityChannel.send(message);
            } else {
                normalPriorityChannel.send(message);
            }
        });

        inputChannel.send(MessageBuilder.withPayload("URGENT: Server down").build());
        inputChannel.send(MessageBuilder.withPayload("Regular report").build());
        inputChannel.send(MessageBuilder.withPayload("URGENT: Database full").build());
        inputChannel.send(MessageBuilder.withPayload("Weekly summary").build());

        System.out.println("High priority: " + highPriorityMessages.size());
        System.out.println("Normal priority: " + normalPriorityMessages.size());
    }

    /**
     * Runs all channel pattern demonstrations.
     */
    public static void main(String[] args) throws InterruptedException {
        demonstrateDirectChannel();
        demonstratePublishSubscribeChannel();
        demonstrateQueueChannel();
        demonstrateExecutorChannel();
        demonstrateChannelInterceptors();
        demonstrateContentBasedRouting();
    }
}
