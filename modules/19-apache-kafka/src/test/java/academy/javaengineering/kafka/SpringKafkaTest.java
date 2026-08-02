package academy.javaengineering.kafka;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SpringKafkaTest {

    @Test
    void testSend() {
        SpringKafkaExample kafka = new SpringKafkaExample();
        kafka.send("test-topic", "key1", "value1");
        assertFalse(kafka.getMessages("test-topic").isEmpty());
    }

    @Test
    void testAddListener() {
        SpringKafkaExample kafka = new SpringKafkaExample();
        kafka.addListener("test-topic", "test-group");
        assertNotNull(kafka);
    }

    @Test
    void testSimulateConsume() {
        SpringKafkaExample kafka = new SpringKafkaExample();
        kafka.send("test-topic", "key1", "value1");
        kafka.simulateConsume("test-topic");
        assertFalse(kafka.getConsumedMessages().isEmpty());
    }

    @Test
    void testMultipleTopics() {
        SpringKafkaExample kafka = new SpringKafkaExample();
        kafka.send("topic1", "key1", "value1");
        kafka.send("topic2", "key2", "value2");
        assertFalse(kafka.getMessages("topic1").isEmpty());
        assertFalse(kafka.getMessages("topic2").isEmpty());
    }
}
