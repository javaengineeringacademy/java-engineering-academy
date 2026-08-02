package academy.javaengineering.projects;

/**
 * Demonstrates project architecture patterns.
 */
public class ProjectArchitecture {

    public record ProjectStructure(
        String name,
        String description,
        java.util.List<String> modules
    ) {}

    public static ProjectStructure createMicroserviceProject(String name) {
        return new ProjectStructure(
            name,
            "Microservice project with Spring Boot",
            java.util.List.of(
                "api-gateway",
                "service-registry",
                "config-server",
                "user-service",
                "order-service",
                "notification-service"
            )
        );
    }

    public static ProjectStructure createMonolithProject(String name) {
        return new ProjectStructure(
            name,
            "Monolithic Spring Boot application",
            java.util.List.of(
                "controller",
                "service",
                "repository",
                "model",
                "config"
            )
        );
    }
}
