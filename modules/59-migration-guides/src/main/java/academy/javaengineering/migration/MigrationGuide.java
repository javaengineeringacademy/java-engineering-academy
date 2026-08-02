package academy.javaengineering.migration;

/**
 * Demonstrates Java version migration guides.
 */
public class MigrationGuide {

    public record VersionMigration(
        String fromVersion,
        String toVersion,
        java.util.List<String> changes,
        java.util.List<String> breakingChanges
    ) {}

    public static java.util.List<VersionMigration> getMigrations() {
        return java.util.List.of(
            new VersionMigration(
                "Java 8",
                "Java 11",
                java.util.List.of("HTTP Client API", "Local variable syntax"),
                java.util.List.of("Removed Java EE modules", "JAXB removed")
            ),
            new VersionMigration(
                "Java 11",
                "Java 17",
                java.util.List.of("Records", "Sealed classes", "Pattern matching"),
                java.util.List.of("Removed Nashorn", "Security manager deprecated")
            ),
            new VersionMigration(
                "Java 17",
                "Java 21",
                java.util.List.of("Virtual threads", "Pattern matching switch", "Sequenced collections"),
                java.util.List.of("Security manager removed")
            )
        );
    }
}
