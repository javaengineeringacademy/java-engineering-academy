package academy.javaengineering.kafka;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class KafkaFundamentalsTest {

    @Test
    void testCreateTopic() {
        KafkaFundamentalsExample kafka = new KafkaFundamentalsExample();
        kafka.createTopic("test-topic", 3);
        assertTrue(kafka.getTopicInfo().containsKey("test-topic"));
    }

    @Test
    void testSendMessage() {
        KafkaFundamentalsExample kafka = new KafkaFundamentalsExample();
        kafka.createTopic("test-topic", 3);
        kafka.sendMessage("test-topic", "key1", "value1");
        List<String> messages = kafka.consumeMessages("test-topic", 0);
        assertFalse(messages.isEmpty());
    }

    @Test
    void testTopicNotFound() {
        KafkaFundamentalsExample kafka = new KafkaFundamentalsExample();
        assertThrows(IllegalArgumentException.class, () -> {
            kafka.sendMessage("nonexistent", "key", "value");
        });
    }

    @Test
    void testGetTopicInfo() {
        KafkaFundamentalsExample kafka = new KafkaFundamentalsExample();
        kafka.createTopic("topic1", 3);
        kafka.createTopic("topic2", 2);
        Map<String, Integer> info = kafka.getTopicInfo();
        assertEquals(2, info.size());
    }
}
