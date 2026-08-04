package academy.javaengineering.springintegration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Spring Integration channel patterns.
 */
@DisplayName("Channel Patterns Tests")
class ChannelPatternsTest {

    @Test
    @DisplayName("Should route point-to-point messages correctly")
    void shouldRoutePointToPointMessages() {
        DirectChannel channel = new DirectChannel();
        AtomicInteger subscriberCount = new AtomicInteger(0);

        channel.subscribe(message -> subscriberCount.incrementAndGet());

        channel.send(MessageBuilder.withPayload("P2P 1").build());
        channel.send(MessageBuilder.withPayload("P2P 2").build());

        assertEquals(2, subscriberCount.get());
    }

    @Test
    @DisplayName("Should broadcast to multiple subscribers")
    void shouldBroadcastToMultipleSubscribers() {
        PublishSubscribeChannel channel = new PublishSubscribeChannel();
        AtomicInteger sub1 = new AtomicInteger(0);
        AtomicInteger sub2 = new AtomicInteger(0);
        AtomicInteger sub3 = new AtomicInteger(0);

        channel.subscribe(message -> sub1.incrementAndGet());
        channel.subscribe(message -> sub2.incrementAndGet());
        channel.subscribe(message -> sub3.incrementAndGet());

        channel.send(MessageBuilder.withPayload("Broadcast").build());
        channel.send(MessageBuilder.withPayload("Another").build());

        assertEquals(2, sub1.get());
        assertEquals(2, sub2.get());
        assertEquals(2, sub3.get());
    }

    @Test
    @DisplayName("Should queue messages for later retrieval")
    void shouldQueueMessagesForLaterRetrieval() {
        QueueChannel queue = new QueueChannel(10);

        queue.send(MessageBuilder.withPayload("First").build());
        queue.send(MessageBuilder.withPayload("Second").build());

        assertEquals(2, queue.getQueueSize());

        Message<?> first = queue.receive(1000);
        assertNotNull(first);
        assertEquals("First", first.getPayload());

        Message<?> second = queue.receive(1000);
        assertNotNull(second);
        assertEquals("Second", second.getPayload());

        assertEquals(0, queue.getQueueSize());
    }

    @Test
    @DisplayName("Should process messages asynchronously")
    void shouldProcessMessagesAsynchronously() throws InterruptedException {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("test-exec-");
        executor.initialize();

        ExecutorChannel channel = new ExecutorChannel(executor);
        List<String> results = new CopyOnWriteArrayList<>();
        CountDownLatch latch = new CountDownLatch(3);

        channel.subscribe(message -> {
            results.add(Thread.currentThread().getName() + ":" + message.getPayload());
            latch.countDown();
        });

        channel.send(MessageBuilder.withPayload("A").build());
        channel.send(MessageBuilder.withPayload("B").build());
        channel.send(MessageBuilder.withPayload("C").build());

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        assertEquals(3, results.size());

        executor.shutdown();
    }

    @Test
    @DisplayName("Should intercept messages on channel")
    void shouldInterceptMessages() {
        DirectChannel channel = new DirectChannel();
        List<String> interceptLog = new ArrayList<>();
        List<String> handlerLog = new ArrayList<>();

        channel.addInterceptor(new org.springframework.messaging.support.ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel ch) {
                interceptLog.add("PRE:" + message.getPayload());
                return message;
            }
        });

        channel.subscribe(message -> handlerLog.add((String) message.getPayload()));

        channel.send(MessageBuilder.withPayload("Intercepted").build());

        assertEquals(1, interceptLog.size());
        assertEquals("PRE:Intercepted", interceptLog.get(0));
        assertEquals(1, handlerLog.size());
        assertEquals("Intercepted", handlerLog.get(0));
    }

    @Test
    @DisplayName("Should route messages based on content")
    void shouldRouteBasedOnContent() {
        DirectChannel input = new DirectChannel();
        DirectChannel urgent = new DirectChannel();
        DirectChannel normal = new DirectChannel();

        List<String> urgentMsgs = new CopyOnWriteArrayList<>();
        List<String> normalMsgs = new CopyOnWriteArrayList<>();

        urgent.subscribe(message -> urgentMsgs.add((String) message.getPayload()));
        normal.subscribe(message -> normalMsgs.add((String) message.getPayload()));

        input.subscribe(message -> {
            String payload = (String) message.getPayload();
            if (payload.contains("URGENT")) {
                urgent.send(message);
            } else {
                normal.send(message);
            }
        });

        input.send(MessageBuilder.withPayload("URGENT alert").build());
        input.send(MessageBuilder.withPayload("Regular update").build());
        input.send(MessageBuilder.withPayload("URGENT error").build());

        assertEquals(2, urgentMsgs.size());
        assertEquals(1, normalMsgs.size());
    }
}
