package academy.javaengineering.cicd;

/**
 * Demonstrates deployment strategies.
 */
public class DeploymentStrategies {

    public record DeploymentStrategy(
        String name,
        String description,
        String riskLevel,
        java.util.List<String> requirements
    ) {}

    public static java.util.List<DeploymentStrategy> getStrategies() {
        return java.util.List.of(
            new DeploymentStrategy(
                "Blue-Green",
                "Two identical environments, switch traffic",
                "Low",
                java.util.List.of("Double infrastructure", "Load balancer")
            ),
            new DeploymentStrategy(
                "Canary",
                "Gradually roll out to small percentage",
                "Low",
                java.util.List.of("Monitoring", "Traffic splitting")
            ),
            new DeploymentStrategy(
                "Rolling",
                "Update instances one at a time",
                "Medium",
                java.util.List.of("Health checks", "Rollback plan")
            ),
            new DeploymentStrategy(
                "Recreate",
                "Stop all instances, deploy new version",
                "High",
                java.util.List.of("Maintenance window", "Backup")
            )
        );
    }
}
