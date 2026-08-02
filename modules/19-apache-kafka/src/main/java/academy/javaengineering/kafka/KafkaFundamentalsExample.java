package academy.javaengineering.kafka;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class KafkaFundamentalsExample {

    private final Map<String, List<String>> topics = new ConcurrentHashMap<>();
    private final Map<String, Integer> partitions = new ConcurrentHashMap<>();

    public void createTopic(String topicName, int numPartitions) {
        topics.put(topicName, new ArrayList<>());
        partitions.put(topicName, numPartitions);
        System.out.println("Topic created: " + topicName + " with " + numPartitions + " partitions");
    }

    public void sendMessage(String topicName, String key, String value) {
        if (!topics.containsKey(topicName)) {
            throw new IllegalArgumentException("Topic not found: " + topicName);
        }

        int partition = key != null ? Math.abs(key.hashCode()) % partitions.get(topicName) : 0;
        String message = key + ":" + value;
        topics.get(topicName).add(message);
        System.out.println("Message sent to topic: " + topicName + ", partition: " + partition);
    }

    public List<String> consumeMessages(String topicName, int partition) {
        if (!topics.containsKey(topicName)) {
            throw new IllegalArgumentException("Topic not found: " + topicName);
        }
        return new ArrayList<>(topics.get(topicName));
    }

    public Map<String, Integer> getTopicInfo() {
        return new HashMap<>(partitions);
    }

    public static void main(String[] args) {
        KafkaFundamentalsExample kafka = new KafkaFundamentalsExample();

        System.out.println("=== Kafka Fundamentals Demo ===\n");

        kafka.createTopic("orders", 3);
        kafka.createTopic("users", 2);

        System.out.println("\n--- Sending Messages ---");
        kafka.sendMessage("orders", "order-1", "Order created");
        kafka.sendMessage("orders", "order-2", "Order updated");
        kafka.sendMessage("users", "user-1", "User registered");

        System.out.println("\n--- Consuming Messages ---");
        List<String> orders = kafka.consumeMessages("orders", 0);
        System.out.println("Orders: " + orders);

        System.out.println("\n--- Topic Info ---");
        System.out.println(kafka.getTopicInfo());
    }
}
