package academy.javaengineering.aws;

import java.util.Map;

public class SQSExample {

    public static void main(String[] args) {
        System.out.println("=== SQS Examples ===\n");
        
        demonstrateQueueTypes();
        demonstrateFeatures();
        demonstrateBestPractices();
    }

    public static void demonstrateQueueTypes() {
        System.out.println("--- SQS Queue Types ---");
        
        Map<String, String> queueTypes = Map.of(
            "Standard", "Best-effort ordering, unlimited throughput",
            "FIFO", "Strict ordering, exactly-once delivery"
        );
        
        queueTypes.forEach((type, description) ->
            System.out.printf("  %-10s - %s%n", type, description)
        );
        System.out.println();
    }

    public static void demonstrateFeatures() {
        System.out.println("--- SQS Features ---");
        
        Map<String, String> features = Map.of(
            "Dead Letter Queue", "Handle failed messages",
            "Visibility Timeout", "Hide processed messages",
            "Long Polling", "Reduce empty responses",
            "Message Retention", "1-14 days",
            "Server-Side Encryption", "Data protection"
        );
        
        features.forEach((feature, description) ->
            System.out.printf("  %-22s - %s%n", feature, description)
        );
        System.out.println();
    }

    public static void demonstrateBestPractices() {
        System.out.println("--- SQS Best Practices ---");
        
        String[] practices = {
            "Use long polling to reduce costs",
            "Implement dead letter queues",
            "Use message batching for throughput",
            "Set appropriate visibility timeout",
            "Enable server-side encryption",
            "Monitor queue depth with CloudWatch"
        };
        
        for (String practice : practices) {
            System.out.println("  " + practice);
        }
        System.out.println();
    }
}
