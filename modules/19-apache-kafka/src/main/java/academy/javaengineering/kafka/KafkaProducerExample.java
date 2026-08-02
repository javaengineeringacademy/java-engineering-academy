package academy.javaengineering.kafka;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class KafkaProducerExample {

    private final Map<String, List<Map<String, Object>>> topicMessages = new ConcurrentHashMap<>();
    private int messageCount = 0;

    public enum AckMode {
        NONE, ONE, ALL
    }

    public KafkaProducerExample() {
    }

    public void send(String topic, String key, String value) {
        send(topic, key, value, AckMode.ALL);
    }

    public void send(String topic, String key, String value, AckMode ackMode) {
        Map<String, Object> message = new HashMap<>();
        message.put("key", key);
        message.put("value", value);
        message.put("timestamp", System.currentTimeMillis());
        message.put("ackMode", ackMode);
        message.put("partition", key != null ? Math.abs(key.hashCode()) % 3 : 0);

        topicMessages.computeIfAbsent(topic, k -> new ArrayList<>()).add(message);
        messageCount++;

        System.out.printf("Sent message to %s: key=%s, value=%s, ack=%s%n",
                topic, key, value, ackMode);
    }

    public List<Map<String, Object>> getMessages(String topic) {
        return topicMessages.getOrDefault(topic, Collections.emptyList());
    }

    public int getMessageCount() {
        return messageCount;
    }

    public static void main(String[] args) {
        KafkaProducerExample producer = new KafkaProducerExample();

        System.out.println("=== Kafka Producer Demo ===\n");

        producer.send("orders", "order-1", "Create order", AckMode.ALL);
        producer.send("orders", "order-2", "Update order", AckMode.ONE);
        producer.send("users", "user-1", "User created", AckMode.ALL);

        System.out.println("\n--- Messages in orders topic ---");
        producer.getMessages("orders").forEach(System.out::println);

        System.out.println("\n--- Total messages sent: " + producer.getMessageCount());
    }
}
