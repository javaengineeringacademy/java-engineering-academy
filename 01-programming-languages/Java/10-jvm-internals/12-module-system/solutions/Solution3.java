package academy.javaengineering.jvm.modules;

/**
 * Solution 3: Classpath to Module Migration
 */
public class Solution3 {

    public static void main(String[] args) {
        System.out.println("=== Classpath to Module Migration ===\n");

        System.out.println("Migration Steps:");
        System.out.println("=================\n");

        System.out.println("Step 1: Analyze dependencies");
        System.out.println("  jdeps --module-source-path src --generate-module-info=src .\n");

        System.out.println("Step 2: Create module-info.java");
        System.out.println("  module com.example.app {");
        System.out.println("      requires java.sql;");
        System.out.println("      requires java.logging;");
        System.out.println("      requires transitive java.xml;");
        System.out.println("      exports com.example.api;");
        System.out.println("      opens com.example.impl to com.example.testing;");
        System.out.println("  }\n");

        System.out.println("Step 3: Fix split packages");
        System.out.println("  Ensure same package exists in only one module\n");

        System.out.println("Step 4: Add opens for reflection");
        System.out.println("  Spring, Hibernate, Jackson need opens for annotation processing\n");

        System.out.println("Step 5: Test with module path");
        System.out.println("  javac --module-source-path src -m com.example.app");
        System.out.println("  java --module-path mods -m com.example.app/com.example.Main\n");

        System.out.println("Step 6: Create custom runtime (optional)");
        System.out.println("  jlink --module-path mods --add-modules com.example.app --output runtime");
        System.out.println("  runtime/bin/java -m com.example.app/com.example.Main");
    }
}
