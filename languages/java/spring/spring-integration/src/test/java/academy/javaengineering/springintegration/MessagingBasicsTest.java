package academy.javaengineering.springintegration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Spring Integration messaging basics.
 */
@DisplayName("Messaging Basics Tests")
class MessagingBasicsTest {

    @Test
    @DisplayName("Should create message with payload and headers")
    void shouldCreateMessageWithHeaders() {
        MessagingBasics.Order order = new MessagingBasics.Order("ORD-1", "Laptop", 1, 999.99);
        Message<MessagingBasics.Order> message = MessageBuilder
                .withPayload(order)
                .setHeader("priority", 1)
                .setHeader("source", "WEB")
                .build();

        assertNotNull(message);
        assertEquals("Laptop", message.getPayload().product());
        assertEquals(1, message.getHeaders().get("priority"));
        assertNotNull(message.getHeaders().getId());
        assertTrue(message.getHeaders().getTimestamp() > 0);
    }

    @Test
    @DisplayName("Should send and receive messages on DirectChannel")
    void shouldSendAndReceiveOnDirectChannel() {
        DirectChannel channel = new DirectChannel();
        List<String> received = new ArrayList<>();

        channel.subscribe(message -> received.add((String) message.getPayload()));

        channel.send(MessageBuilder.withPayload("Hello").build());
        channel.send(MessageBuilder.withPayload("World").build());

        assertEquals(2, received.size());
        assertEquals("Hello", received.get(0));
        assertEquals("World", received.get(1));
    }

    @Test
    @DisplayName("Should broadcast to all subscribers on PublishSubscribeChannel")
    void shouldBroadcastToAllSubscribers() {
        PublishSubscribeChannel channel = new PublishSubscribeChannel();
        AtomicInteger subscriber1Count = new AtomicInteger(0);
        AtomicInteger subscriber2Count = new AtomicInteger(0);

        channel.subscribe(message -> subscriber1Count.incrementAndGet());
        channel.subscribe(message -> subscriber2Count.incrementAndGet());

        channel.send(MessageBuilder.withPayload("Broadcast").build());

        assertEquals(1, subscriber1Count.get());
        assertEquals(1, subscriber2Count.get());
    }

    @Test
    @DisplayName("Should buffer messages in QueueChannel")
    void shouldBufferMessagesInQueueChannel() {
        QueueChannel queueChannel = new QueueChannel(5);

        queueChannel.send(MessageBuilder.withPayload("Msg1").build());
        queueChannel.send(MessageBuilder.withPayload("Msg2").build());
        queueChannel.send(MessageBuilder.withPayload("Msg3").build());

        assertEquals(3, queueChannel.getQueueSize());

        Message<?> received = queueChannel.receive(1000);
        assertNotNull(received);
        assertEquals("Msg1", received.getPayload());

        assertEquals(2, queueChannel.getQueueSize());
    }

    @Test
    @DisplayName("Should process messages asynchronously on ExecutorChannel")
    void shouldProcessMessagesAsynchronously() throws InterruptedException {
        org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor executor =
                new org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("test-");
        executor.initialize();

        org.springframework.integration.channel.ExecutorChannel executorChannel =
                new org.springframework.integration.channel.ExecutorChannel(executor);

        List<String> threadNames = new CopyOnWriteArrayList<>();
        CountDownLatch latch = new CountDownLatch(3);

        executorChannel.subscribe(message -> {
            threadNames.add(Thread.currentThread().getName());
            latch.countDown();
        });

        executorChannel.send(MessageBuilder.withPayload("Task1").build());
        executorChannel.send(MessageBuilder.withPayload("Task2").build());
        executorChannel.send(MessageBuilder.withPayload("Task3").build());

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        assertEquals(3, threadNames.size());
        assertTrue(threadNames.get(0).startsWith("test-"));

        executor.shutdown();
    }

    @Test
    @DisplayName("Should handle custom MessageHandler")
    void shouldHandleCustomMessageHandler() {
        DirectChannel channel = new DirectChannel();
        List<String> handled = new ArrayList<>();

        channel.subscribe(new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                handled.add((String) message.getPayload());
            }
        });

        channel.send(MessageBuilder.withPayload("Custom Handler").build());

        assertEquals(1, handled.size());
        assertEquals("Custom Handler", handled.get(0));
    }

    @Test
    @DisplayName("Should copy headers between messages")
    void shouldCopyHeadersBetweenMessages() {
        MessagingBasics.Order order = new MessagingBasics.Order("ORD-2", "Phone", 2, 599.99);
        Message<MessagingBasics.Order> original = MessageBuilder
                .withPayload(order)
                .setHeader("trace-id", "abc-123")
                .build();

        String csv = order.orderId() + "," + order.product();
        Message<String> converted = MessageBuilder
                .withPayload(csv)
                .copyHeaders(original.getHeaders())
                .build();

        assertEquals("abc-123", converted.getHeaders().get("trace-id"));
        assertEquals("ORD-2,Phone", converted.getPayload());
    }
}
