package academy.javaengineering.fundamentals.maven;

/**
 * Demonstrates Maven memory usage patterns.
 */
public class MavenMemory {

    public static void main(String[] args) {
        System.out.println("=== Maven Memory Demo ===\n");

        // 1. Local repository structure
        System.out.println("--- Local Repository ---");
        System.out.println("Location: ~/.m2/repository/");
        System.out.println("Contains: JARs, POMs, metadata");
        System.out.println("Typical size: 50-500MB");

        // 2. Build output
        System.out.println("\n--- Build Output ---");
        System.out.println("target/classes/ - Compiled .class files");
        System.out.println("target/test-classes/ - Test classes");
        System.out.println("target/*.jar - Packaged artifact");

        // 3. Dependency resolution
        System.out.println("\n--- Dependency Resolution ---");
        System.out.println("1. Read pom.xml");
        System.out.println("2. Resolve transitive dependencies");
        System.out.println("3. Check local cache");
        System.out.println("4. Download if needed");

        // 4. Plugin execution
        System.out.println("\n--- Plugin Execution ---");
        System.out.println("Each plugin has its own ClassLoader");
        System.out.println("Plugins execute during lifecycle phases");

        System.out.println("\n=== Memory Demo Complete ===");
    }
}
