package academy.javaengineering.springintegration;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Filter;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Splitter;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demonstrates Spring Integration annotations for message processing.
 */
public class ServiceActivatorExample {

    /**
     * Configuration class for Spring Integration.
     */
    @Configuration
    @EnableIntegration
    public static class IntegrationConfig {

        @Bean
        public MessageChannel inputChannel() {
            return new DirectChannel();
        }

        @Bean
        public MessageChannel transformChannel() {
            return new DirectChannel();
        }

        @Bean
        public MessageChannel filterChannel() {
            return new DirectChannel();
        }

        @Bean
        public MessageChannel orderChannel() {
            return new DirectChannel();
        }

        @Bean
        public MessageChannel highPriorityChannel() {
            return new DirectChannel();
        }

        @Bean
        public MessageChannel normalPriorityChannel() {
            return new DirectChannel();
        }

        @Bean
        public MessageChannel splitChannel() {
            return new QueueChannel();
        }

        @Bean
        public ServiceActivatorProcessor processor() {
            return new ServiceActivatorProcessor();
        }
    }

    /**
     * Service activator that processes incoming messages.
     */
    public static class ServiceActivatorProcessor {

        private final AtomicInteger counter = new AtomicInteger(0);

        @ServiceActivator(inputChannel = "inputChannel")
        public String processMessage(String payload) {
            int count = counter.incrementAndGet();
            String result = "Processed #" + count + ": " + payload.toUpperCase();
            System.out.println("ServiceActivator: " + result);
            return result;
        }

        @ServiceActivator(inputChannel = "orderChannel")
        public String processOrder(String orderData, @Header("priority") String priority) {
            String result = "Order [" + priority + "]: " + orderData;
            System.out.println("OrderProcessor: " + result);
            return result;
        }

        public int getCounter() {
            return counter.get();
        }
    }

    /**
     * Transformer that converts message payload.
     */
    public static class MessageTransformer {

        @Transformer(inputChannel = "transformChannel", outputChannel = "filterChannel")
        public String transformMessage(String payload) {
            String transformed = "TRANSFORMED: " + payload.trim().toUpperCase();
            System.out.println("Transformer: " + transformed);
            return transformed;
        }

        @Transformer(inputChannel = "inputChannel", outputChannel = "transformChannel")
        public Map<String, Object> enrichMessage(String payload) {
            return Map.of(
                    "original", payload,
                    "length", payload.length(),
                    "processed", true
            );
        }
    }

    /**
     * Filter that decides whether to pass messages through.
     */
    public static class MessageFilter {

        @Filter(inputChannel = "filterChannel", outputChannel = "splitChannel")
        public boolean filterMessage(String payload) {
            boolean pass = payload.length() > 5;
            System.out.println("Filter [" + (pass ? "PASS" : "REJECT") + "]: " + payload);
            return pass;
        }
    }

    /**
     * Router that directs messages to different channels.
     */
    public static class MessageRouter {

        @Router(inputChannel = "orderChannel")
        public String routeOrder(String payload, @Header("priority") String priority) {
            if ("HIGH".equals(priority)) {
                System.out.println("Router: HIGH priority -> highPriorityChannel");
                return "highPriorityChannel";
            }
            System.out.println("Router: Normal priority -> normalPriorityChannel");
            return "normalPriorityChannel";
        }
    }

    /**
     * Splitter that breaks messages into parts.
     */
    public static class MessageSplitter {

        @Splitter(inputChannel = "splitChannel", outputChannel = "inputChannel")
        public List<String> splitMessage(String payload) {
            String[] parts = payload.split(",");
            List<String> result = Arrays.asList(parts);
            System.out.println("Splitter: Split into " + result.size() + " parts");
            return result;
        }
    }

    /**
     * Demonstrates @ServiceActivator for message processing.
     */
    public static void demonstrateServiceActivator() {
        System.out.println("=== @ServiceActivator ===");

        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(IntegrationConfig.class)) {

            DirectChannel inputChannel = context.getBean("inputChannel", DirectChannel.class);
            ServiceActivatorProcessor processor = context.getBean(ServiceActivatorProcessor.class);

            inputChannel.send(MessageBuilder.withPayload("Hello World").build());
            inputChannel.send(MessageBuilder.withPayload("Spring Integration").build());

            System.out.println("Processed count: " + processor.getCounter());
        }
    }

    /**
     * Demonstrates @Transformer for message transformation.
     */
    public static void demonstrateTransformer() {
        System.out.println("\n=== @Transformer ===");

        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(IntegrationConfig.class)) {

            DirectChannel inputChannel = context.getBean("inputChannel", DirectChannel.class);

            inputChannel.send(MessageBuilder.withPayload("  transform me  ").build());
        }
    }

    /**
     * Demonstrates @Filter for message filtering.
     */
    public static void demonstrateFilter() {
        System.out.println("\n=== @Filter ===");

        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(IntegrationConfig.class)) {

            DirectChannel inputChannel = context.getBean("inputChannel", DirectChannel.class);

            inputChannel.send(MessageBuilder.withPayload("short").build());
            inputChannel.send(MessageBuilder.withPayload("this is a long message").build());
        }
    }

    /**
     * Demonstrates @Router for message routing.
     */
    public static void demonstrateRouter() {
        System.out.println("\n=== @Router ===");

        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(IntegrationConfig.class)) {

            DirectChannel orderChannel = context.getBean("orderChannel", DirectChannel.class);

            orderChannel.send(MessageBuilder
                    .withPayload("Laptop Order")
                    .setHeader("priority", "HIGH")
                    .build());

            orderChannel.send(MessageBuilder
                    .withPayload("Mouse Order")
                    .setHeader("priority", "NORMAL")
                    .build());
        }
    }

    /**
     * Demonstrates @Splitter for message splitting.
     */
    public static void demonstrateSplitter() {
        System.out.println("\n=== @Splitter ===");

        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(IntegrationConfig.class)) {

            DirectChannel inputChannel = context.getBean("inputChannel", DirectChannel.class);

            inputChannel.send(MessageBuilder.withPayload("item1,item2,item3,item4").build());
        }
    }

    /**
     * Runs all annotation demonstrations.
     */
    public static void main(String[] args) {
        demonstrateServiceActivator();
        demonstrateTransformer();
        demonstrateFilter();
        demonstrateRouter();
        demonstrateSplitter();
    }
}
