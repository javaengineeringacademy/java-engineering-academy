package academy.javaengineering.systemdesign;

import java.util.Map;

public class MessageQueueExample {

    public static void main(String[] args) {
        System.out.println("=== Message Queue Examples ===\n");
        demonstrateTechnologies();
        demonstratePatterns();
    }

    public static void demonstrateTechnologies() {
        System.out.println("--- Message Queue Technologies ---");
        Map<String, String> techs = Map.of(
            "Kafka", "Distributed event streaming",
            "RabbitMQ", "Traditional message broker",
            "SQS", "AWS managed queue",
            "Pulsar", "Cloud-native messaging"
        );
        techs.forEach((k, v) -> System.out.printf("  %-10s - %s%n", k, v));
        System.out.println();
    }

    public static void demonstratePatterns() {
        System.out.println("--- Messaging Patterns ---");
        Map<String, String> patterns = Map.of(
            "Point-to-Point", "One producer, one consumer",
            "Pub/Sub", "One producer, many consumers",
            "Event Sourcing", "Store events, not state",
            "CQRS", "Separate read/write models"
        );
        patterns.forEach((k, v) -> System.out.printf("  %-15s - %s%n", k, v));
        System.out.println();
    }
}
