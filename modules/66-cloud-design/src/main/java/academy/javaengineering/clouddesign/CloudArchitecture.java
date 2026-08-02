package academy.javaengineering.clouddesign;

/**
 * Demonstrates cloud architecture components.
 */
public class CloudArchitecture {

    public record ArchitecturalComponent(
        String name,
        String type,
        String purpose
    ) {}

    public static java.util.List<ArchitecturalComponent> getComponents() {
        return java.util.List.of(
            new ArchitecturalComponent("API Gateway", "Networking", "Route requests, rate limiting"),
            new ArchitecturalComponent("Load Balancer", "Networking", "Distribute traffic"),
            new ArchitecturalComponent("Message Queue", "Messaging", "Async communication"),
            new ArchitecturalComponent("Cache", "Storage", "Improve performance"),
            new ArchitecturalComponent("Database", "Storage", "Persist data")
        );
    }

    public static java.util.Map<String, String> getBestPractices() {
        return java.util.Map.of(
            "Design for failure", "Assume components will fail",
            "Scale horizontally", "Add more instances, not bigger ones",
            "Use managed services", "Focus on business logic",
            "Implement observability", "Monitor, log, trace"
        );
    }
}
