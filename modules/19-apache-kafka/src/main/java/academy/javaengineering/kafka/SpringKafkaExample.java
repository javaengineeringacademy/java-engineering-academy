package academy.javaengineering.kafka;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SpringKafkaExample {

    private final Map<String, List<String>> topics = new ConcurrentHashMap<>();
    private final List<Map<String, Object>> consumedMessages = Collections.synchronizedList(new ArrayList<>());

    public void send(String topic, String key, String value) {
        topics.computeIfAbsent(topic, k -> new ArrayList<>()).add(key + ":" + value);
        System.out.println("KafkaTemplate.send: " + topic + " -> " + key + ":" + value);
    }

    public void addListener(String topic, String groupId) {
        System.out.println("@KafkaListener registered: topic=" + topic + ", groupId=" + groupId);
    }

    public List<String> getMessages(String topic) {
        return topics.getOrDefault(topic, Collections.emptyList());
    }

    public List<Map<String, Object>> getConsumedMessages() {
        return consumedMessages;
    }

    public void simulateConsume(String topic) {
        List<String> messages = topics.getOrDefault(topic, Collections.emptyList());
        for (String msg : messages) {
            consumedMessages.add(Map.of("topic", topic, "message", msg));
            System.out.println("@KafkaListener received: " + msg);
        }
    }

    public static void main(String[] args) {
        SpringKafkaExample kafka = new SpringKafkaExample();

        System.out.println("=== Spring Kafka Demo ===\n");

        kafka.addListener("orders", "order-service");
        kafka.addListener("users", "user-service");

        System.out.println("\n--- Sending messages ---");
        kafka.send("orders", "order-1", "Order created");
        kafka.send("orders", "order-2", "Order updated");
        kafka.send("users", "user-1", "User registered");

        System.out.println("\n--- Consuming messages ---");
        kafka.simulateConsume("orders");
        kafka.simulateConsume("users");

        System.out.println("\n--- All consumed messages ---");
        kafka.getConsumedMessages().forEach(System.out::println);
    }
}
