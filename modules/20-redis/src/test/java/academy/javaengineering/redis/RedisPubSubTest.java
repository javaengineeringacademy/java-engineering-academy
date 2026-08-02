package academy.javaengineering.redis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class RedisPubSubTest {

    private RedisPubSubExample pubSub;

    @BeforeEach
    void setUp() {
        pubSub = new RedisPubSubExample();
    }

    @Test
    void testPublishAndSubscribe() {
        AtomicReference<String> received = new AtomicReference<>();
        pubSub.subscribe("test", (channel, message) -> received.set(message));
        pubSub.publish("test", "hello");
        assertEquals("hello", received.get());
    }

    @Test
    void testMultipleSubscribers() {
        AtomicReference<String> sub1 = new AtomicReference<>();
        AtomicReference<String> sub2 = new AtomicReference<>();
        pubSub.subscribe("test", (ch, msg) -> sub1.set(msg));
        pubSub.subscribe("test", (ch, msg) -> sub2.set(msg));
        pubSub.publish("test", "hello");
        assertEquals("hello", sub1.get());
        assertEquals("hello", sub2.get());
    }

    @Test
    void testUnsubscribe() {
        AtomicReference<String> received = new AtomicReference<>();
        RedisPubSubExample.MessageListener listener = (ch, msg) -> received.set(msg);
        pubSub.subscribe("test", listener);
        pubSub.unsubscribe("test", listener);
        pubSub.publish("test", "hello");
        assertNull(received.get());
    }

    @Test
    void testMessageLog() {
        pubSub.publish("test", "message1");
        pubSub.publish("test", "message2");
        assertEquals(2, pubSub.getMessageLog().size());
    }
}
