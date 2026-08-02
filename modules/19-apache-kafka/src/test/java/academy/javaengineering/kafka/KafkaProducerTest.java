package academy.javaengineering.kafka;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class KafkaProducerTest {

    @Test
    void testSendMessage() {
        KafkaProducerExample producer = new KafkaProducerExample();
        producer.send("test-topic", "key1", "value1");
        assertEquals(1, producer.getMessageCount());
    }

    @Test
    void testSendMessageWithAck() {
        KafkaProducerExample producer = new KafkaProducerExample();
        producer.send("test-topic", "key1", "value1", KafkaProducerExample.AckMode.ALL);
        assertEquals(1, producer.getMessageCount());
    }

    @Test
    void testGetMessages() {
        KafkaProducerExample producer = new KafkaProducerExample();
        producer.send("test-topic", "key1", "value1");
        producer.send("test-topic", "key2", "value2");
        List<Map<String, Object>> messages = producer.getMessages("test-topic");
        assertEquals(2, messages.size());
    }

    @Test
    void testMultipleTopics() {
        KafkaProducerExample producer = new KafkaProducerExample();
        producer.send("topic1", "key1", "value1");
        producer.send("topic2", "key2", "value2");
        assertEquals(2, producer.getMessageCount());
    }
}
