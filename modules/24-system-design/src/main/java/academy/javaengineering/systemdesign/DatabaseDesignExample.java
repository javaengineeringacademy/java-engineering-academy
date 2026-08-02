package academy.javaengineering.systemdesign;

import java.util.Map;

public class DatabaseDesignExample {

    public static void main(String[] args) {
        System.out.println("=== Database Design Examples ===\n");
        demonstrateTechniques();
    }

    public static void demonstrateTechniques() {
        System.out.println("--- Scaling Techniques ---");
        Map<String, String> techniques = Map.of(
            "Sharding", "Split data across servers by key",
            "Replication", "Copy data to multiple servers",
            "Partitioning", "Split tables vertically",
            "Read Replicas", "Scale reads with async copies"
        );
        techniques.forEach((k, v) -> System.out.printf("  %-15s - %s%n", k, v));
        System.out.println();
    }
}
