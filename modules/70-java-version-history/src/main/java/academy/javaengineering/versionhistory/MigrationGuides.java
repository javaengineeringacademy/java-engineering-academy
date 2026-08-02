package academy.javaengineering.versionhistory;

/**
 * Demonstrates migration guides between versions.
 */
public class MigrationGuides {

    public record MigrationGuide(
        String from,
        String to,
        java.util.List<String> steps,
        java.util.List<String> breakingChanges
    ) {}

    public static java.util.List<MigrationGuide> getGuides() {
        return java.util.List.of(
            new MigrationGuide(
                "Java 8",
                "Java 11",
                java.util.List.of(
                    "Update build tool",
                    "Remove deprecated APIs",
                    "Update dependencies"
                ),
                java.util.List.of(
                    "Java EE modules removed",
                    "JAXB removed from JDK"
                )
            ),
            new MigrationGuide(
                "Java 11",
                "Java 17",
                java.util.List.of(
                    "Enable preview features",
                    "Update code to use records",
                    "Replace switch with enhanced switch"
                ),
                java.util.List.of(
                    "Nashorn removed",
                    "Security manager deprecated"
                )
            )
        );
    }
}
