package academy.javaengineering.clouddesign;

/**
 * Demonstrates cloud design patterns.
 */
public class CloudPatterns {

    public record DesignPattern(
        String name,
        String description,
        java.util.List<String> useCases
    ) {}

    public static java.util.List<DesignPattern> getPatterns() {
        return java.util.List.of(
            new DesignPattern(
                "Circuit Breaker",
                "Prevent cascading failures",
                java.util.List.of("External service calls", "Database connections")
            ),
            new DesignPattern(
                "Retry",
                "Retry failed operations",
                java.util.List.of("Network calls", "Message processing")
            ),
            new DesignPattern(
                "Cache Aside",
                "Cache frequently accessed data",
                java.util.List.of("Database queries", "API responses")
            ),
            new DesignPattern(
                "Event Sourcing",
                "Store state changes as events",
                java.util.List.of("Audit trails", "Event-driven systems")
            )
        );
    }
}
