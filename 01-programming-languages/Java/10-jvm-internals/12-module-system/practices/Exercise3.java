package academy.javaengineering.jvm.modules;

/**
 * Exercise 3: Classpath to Module Migration
 *
 * Task: Migrate a classpath application to the module system.
 */
public class Exercise3 {

    public static void main(String[] args) {
        System.out.println("=== Classpath to Module Migration ===\n");

        // TODO: Steps to migrate:
        // 1. Add module-info.java
        // 2. Declare requires for dependencies
        // 3. Export public API packages
        // 4. Open packages for reflection (if using frameworks)
        // 5. Test with --module-path

        System.out.println("Migration steps:");
        System.out.println("1. jdeps --module-source-path src --generate-module-info=src .");
        System.out.println("2. Review generated module-info.java");
        System.out.println("3. Add exports for public API");
        System.out.println("4. Add opens for framework reflection");
        System.out.println("5. Test: java --module-path mods -m com.example.app/com.example.Main");
    }
}
