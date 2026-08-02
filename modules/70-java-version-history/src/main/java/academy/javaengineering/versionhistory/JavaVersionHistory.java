package academy.javaengineering.versionhistory;

/**
 * Demonstrates Java version history.
 */
public class JavaVersionHistory {

    public record JavaVersion(
        String version,
        int year,
        java.util.List<String> keyFeatures
    ) {}

    public static java.util.List<JavaVersion> getVersions() {
        return java.util.List.of(
            new JavaVersion("Java 8", 2014, java.util.List.of("Lambda expressions", "Streams", "Optional")),
            new JavaVersion("Java 11", 2018, java.util.List.of("HTTP Client", "Local variable syntax")),
            new JavaVersion("Java 17", 2021, java.util.List.of("Records", "Sealed classes", "Pattern matching")),
            new JavaVersion("Java 21", 2023, java.util.List.of("Virtual threads", "Pattern matching switch", "Sequenced collections"))
        );
    }
}
