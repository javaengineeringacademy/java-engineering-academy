package academy.javaengineering.jvm.modules;

/**
 * Exercise 1: Basic Module Creation
 *
 * Task: Create a module with proper exports and requires.
 * The module should export its public API and require java.sql.
 */
public class Exercise1 {

    public static void main(String[] args) {
        System.out.println("=== Basic Module Creation ===\n");

        // TODO: Create module-info.java with:
        // - Module name: com.example.app
        // - requires java.sql
        // - requires java.logging
        // - exports com.example.api

        System.out.println("Create module-info.java:");
        System.out.println("  module com.example.app {");
        System.out.println("      requires java.sql;");
        System.out.println("      requires java.logging;");
        System.out.println("      exports com.example.api;");
        System.out.println("  }");
    }
}
