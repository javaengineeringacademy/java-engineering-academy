package academy.javaengineering.fundamentals.maven;

/**
 * Demonstrates Maven internals concepts.
 */
public class MavenInternals {

    public static void main(String[] args) {
        System.out.println("=== Maven Internals Demo ===\n");

        // 1. Maven coordinates
        System.out.println("--- Maven Coordinates (GAV) ---");
        String groupId = "academy.javaengineering";
        String artifactId = "java-fundamentals";
        String version = "1.0.0-SNAPSHOT";
        System.out.println("GroupId: " + groupId);
        System.out.println("ArtifactId: " + artifactId);
        System.out.println("Version: " + version);
        System.out.println("Full: " + groupId + ":" + artifactId + ":" + version);

        // 2. POM structure
        System.out.println("\n--- POM Structure ---");
        System.out.println("Project Object Model contains:");
        System.out.println("  - Project coordinates (GAV)");
        System.out.println("  - Dependencies");
        System.out.println("  - Build plugins");
        System.out.println("  - Properties");
        System.out.println("  - Profiles");

        // 3. Build lifecycle
        System.out.println("\n--- Build Lifecycle ---");
        String[] phases = {"validate", "compile", "test", "package", "verify", "install", "deploy"};
        for (String phase : phases) {
            System.out.println("  " + phase);
        }

        // 4. Dependency scope
        System.out.println("\n--- Dependency Scopes ---");
        System.out.println("compile: Available everywhere");
        System.out.println("provided: Available except during test/package");
        System.out.println("runtime: Available during runtime only");
        System.out.println("test: Available during test only");
        System.out.println("system: Uses system path (avoid if possible)");

        System.out.println("\n=== Internals Demo Complete ===");
    }
}
