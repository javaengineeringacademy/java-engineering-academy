package academy.javaengineering.aws;

import java.util.Map;

public class LambdaExample {

    public static void main(String[] args) {
        System.out.println("=== Lambda Examples ===\n");
        
        demonstrateLambdaLimits();
        demonstrateTriggerTypes();
        demonstrateBestPractices();
    }

    public static void demonstrateLambdaLimits() {
        System.out.println("--- Lambda Limits ---");
        
        Map<String, String> limits = Map.of(
            "Memory", "128MB - 10GB",
            "Timeout", "15 minutes",
            "Package Size", "250MB (unzipped)",
            "Concurrent Executions", "1,000 (default)",
            "/tmp Storage", "512MB - 10GB"
        );
        
        limits.forEach((limit, value) ->
            System.out.printf("  %-22s - %s%n", limit, value)
        );
        System.out.println();
    }

    public static void demonstrateTriggerTypes() {
        System.out.println("--- Lambda Trigger Types ---");
        
        Map<String, String> triggers = Map.of(
            "API Gateway", "HTTP/REST APIs",
            "S3", "Object events",
            "DynamoDB", "Stream events",
            "SQS", "Queue messages",
            "SNS", "Notifications",
            "EventBridge", "Scheduled events"
        );
        
        triggers.forEach((trigger, description) ->
            System.out.printf("  %-15s - %s%n", trigger, description)
        );
        System.out.println();
    }

    public static void demonstrateBestPractices() {
        System.out.println("--- Lambda Best Practices ---");
        
        String[] practices = {
            "Minimize deployment package size",
            "Use environment variables for configuration",
            "Implement connection pooling",
            "Use provisioned concurrency for critical functions",
            "Monitor with CloudWatch",
            "Handle errors gracefully"
        };
        
        for (String practice : practices) {
            System.out.println("  " + practice);
        }
        System.out.println();
    }
}
