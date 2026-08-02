package academy.javaengineering.enterprise;

import java.util.Map;

public class ProjectArchitectureExample {
    public static void main(String[] args) {
        System.out.println("=== Project Architecture Examples ===\n");
        demonstrateCleanArchitecture();
        demonstrateHexagonal();
        demonstrateDDD();
    }

    public static void demonstrateCleanArchitecture() {
        System.out.println("--- Clean Architecture Layers ---");
        Map<String, String> layers = Map.of(
            "Entities", "Business objects",
            "Use Cases", "Application business rules",
            "Interface Adapters", "Data conversion",
            "Frameworks", "External tools"
        );
        layers.forEach((k, v) -> System.out.printf("  %-20s - %s%n", k, v));
        System.out.println();
    }

    public static void demonstrateHexagonal() {
        System.out.println("--- Hexagonal Architecture ---");
        Map<String, String> components = Map.of(
            "Ports", "Interfaces for business logic",
            "Adapters", "External system implementations",
            "Core", "Business logic"
        );
        components.forEach((k, v) -> System.out.printf("  %-12s - %s%n", k, v));
        System.out.println();
    }

    public static void demonstrateDDD() {
        System.out.println("--- DDD Building Blocks ---");
        Map<String, String> blocks = Map.of(
            "Aggregate", "Cluster of domain objects",
            "Value Object", "Immutable object by attributes",
            "Domain Event", "Something that happened",
            "Repository", "Data persistence abstraction",
            "Domain Service", "Business logic outside entities"
        );
        blocks.forEach((k, v) -> System.out.printf("  %-18s - %s%n", k, v));
        System.out.println();
    }
}
