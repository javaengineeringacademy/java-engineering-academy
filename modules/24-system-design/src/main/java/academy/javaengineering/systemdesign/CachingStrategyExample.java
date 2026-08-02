package academy.javaengineering.systemdesign;

import java.util.Map;

public class CachingStrategyExample {

    public static void main(String[] args) {
        System.out.println("=== Caching Strategy Examples ===\n");
        demonstratePatterns();
        demonstrateStrategies();
    }

    public static void demonstratePatterns() {
        System.out.println("--- Caching Patterns ---");
        Map<String, String> patterns = Map.of(
            "Cache-Aside", "App manages cache explicitly",
            "Write-Through", "Write to cache and DB simultaneously",
            "Write-Behind", "Write to cache, async write to DB",
            "Read-Through", "Cache fetches from DB on miss"
        );
        patterns.forEach((k, v) -> System.out.printf("  %-15s - %s%n", k, v));
        System.out.println();
    }

    public static void demonstrateStrategies() {
        System.out.println("--- Eviction Strategies ---");
        Map<String, String> strategies = Map.of(
            "LRU", "Least Recently Used",
            "LFU", "Least Frequently Used",
            "FIFO", "First In First Out",
            "TTL", "Time To Live expiration"
        );
        strategies.forEach((k, v) -> System.out.printf("  %-5s - %s%n", k, v));
        System.out.println();
    }
}
