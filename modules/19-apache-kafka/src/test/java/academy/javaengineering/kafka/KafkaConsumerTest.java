package academy.javaengineering.kafka;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class KafkaConsumerTest {

    @Test
    void testConsumeMessages() {
        KafkaConsumerExample consumer = new KafkaConsumerExample();
        consumer.produceMessage("test-topic", "key1", "value1");
        List<Map<String, Object>> messages = consumer.consume("test-topic", "test-group");
        assertFalse(messages.isEmpty());
    }

    @Test
    void testCommitOffset() {
        KafkaConsumerExample consumer = new KafkaConsumerExample();
        consumer.produceMessage("test-topic", "key1", "value1");
        consumer.consume("test-topic", "test-group");
        consumer.commitOffset("test-group", "test-topic");
        assertEquals("OFFSET_COMMITTED", consumer.getConsumerGroups().get("test-topic-test-group"));
    }

    @Test
    void testConsumerGroups() {
        KafkaConsumerExample consumer = new KafkaConsumerExample();
        consumer.consume("topic1", "group1");
        consumer.consume("topic2", "group2");
        assertEquals(2, consumer.getConsumerGroups().size());
    }
}
