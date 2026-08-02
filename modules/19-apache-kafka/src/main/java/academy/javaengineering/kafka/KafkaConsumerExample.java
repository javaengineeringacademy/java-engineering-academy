package academy.javaengineering.kafka;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class KafkaConsumerExample {

    private final Map<String, List<Map<String, Object>>> topicMessages = new ConcurrentHashMap<>();
    private final AtomicInteger offset = new AtomicInteger(0);
    private final Map<String, String> consumerGroups = new ConcurrentHashMap<>();

    public void produceMessage(String topic, String key, String value) {
        Map<String, Object> message = new HashMap<>();
        message.put("key", key);
        message.put("value", value);
        message.put("offset", offset.getAndIncrement());
        message.put("partition", key != null ? Math.abs(key.hashCode()) % 3 : 0);

        topicMessages.computeIfAbsent(topic, k -> new ArrayList<>()).add(message);
    }

    public List<Map<String, Object>> consume(String topic, String groupId) {
        String groupKey = topic + "-" + groupId;
        consumerGroups.put(groupKey, "ACTIVE");

        System.out.println("Consumer group " + groupId + " consuming from " + topic);

        return topicMessages.getOrDefault(topic, Collections.emptyList());
    }

    public Map<String, String> getConsumerGroups() {
        return new HashMap<>(consumerGroups);
    }

    public void commitOffset(String groupId, String topic) {
        String groupKey = topic + "-" + groupId;
        consumerGroups.put(groupKey, "OFFSET_COMMITTED");
        System.out.println("Offset committed for group: " + groupId);
    }

    public static void main(String[] args) {
        KafkaConsumerExample consumer = new KafkaConsumerExample();

        System.out.println("=== Kafka Consumer Demo ===\n");

        consumer.produceMessage("orders", "order-1", "Order 1");
        consumer.produceMessage("orders", "order-2", "Order 2");
        consumer.produceMessage("orders", "order-3", "Order 3");

        System.out.println("\n--- Consuming messages ---");
        List<Map<String, Object>> messages = consumer.consume("orders", "order-group");
        messages.forEach(System.out::println);

        System.out.println("\n--- Committing offset ---");
        consumer.commitOffset("order-group", "orders");

        System.out.println("\n--- Consumer groups ---");
        System.out.println(consumer.getConsumerGroups());
    }
}
