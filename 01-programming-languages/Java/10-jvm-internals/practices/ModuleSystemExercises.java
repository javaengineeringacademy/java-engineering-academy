package academy.javaengineering.jvm.practices;

import java.util.ServiceLoader;

/**
 * Module System (JPMS) Exercises
 * Complete each exercise by implementing the required method.
 * Focus on module declarations, requires/exports, services, and migration.
 */
public class ModuleSystemExercises {

    /**
     * Exercise 1: Design a modular application
     * Design a module structure for a simple web application:
     *
     * Modules needed:
     * 1. com.example.core - Core domain model
     * 2. com.example.api - REST API
     * 3. com.example.service - Business logic
     * 4. com.example.persistence - Database access
     *
     * Requirements:
     * - Core is used by all other modules
     * - Service depends on Core and Persistence
     * - API depends on Core and Service
     * - Persistence is only accessed by Service (not API)
     *
     * Write the module-info.java for each module
     */
    public static void designModularApp() {
        System.out.println("Exercise 1: Design a modular application");
        System.out.println("Write module-info.java for each module:");
        System.out.println();
        System.out.println("com.example.core:");
        System.out.println("  exports com.example.core.model;");
        System.out.println("  exports com.example.core.util;");
        System.out.println();
        System.out.println("com.example.persistence:");
        System.out.println("  requires com.example.core;");
        System.out.println("  exports com.example.persistence.repository;");
        System.out.println("  opens com.example.persistence.entity to orm.library;");
        System.out.println();
        System.out.println("com.example.service:");
        System.out.println("  requires com.example.core;");
        System.out.println("  requires com.example.persistence;");
        System.out.println("  exports com.example.service;");
        System.out.println();
        System.out.println("com.example.api:");
        System.out.println("  requires com.example.core;");
        System.out.println("  requires com.example.service;");
        System.out.println("  exports com.example.api.controller;");
        System.out.println("  requires transitive java.json;");
    }

    /**
     * Exercise 2: Implement service provider interface
     * Create a service provider pattern using JPMS:
     *
     * 1. Define a service interface (Formatter)
     * 2. Create two implementations (JsonFormatter, XmlFormatter)
     * 3. Register providers in module-info.java
     * 4. Load and use services in consumer module
     *
     * Service interface:
     * public interface Formatter {
     *     String format(Object obj);
     * }
     */
    interface Formatter {
        String format(Object obj);
    }

    // TODO: Implement JsonFormatter
    static class JsonFormatter implements Formatter {
        @Override
        public String format(Object obj) {
            // Implement JSON formatting
            return "{\"value\": \"" + obj + "\"}";
        }
    }

    // TODO: Implement XmlFormatter
    static class XmlFormatter implements Formatter {
        @Override
        public String format(Object obj) {
            // Implement XML formatting
            return "<value>" + obj + "</value>";
        }
    }

    public static void demonstrateServiceProvider() {
        System.out.println("\nExercise 2: Service Provider Interface");
        System.out.println("Module declarations:");
        System.out.println("  formatter.api module:");
        System.out.println("    exports com.example.formatter;");
        System.out.println("    uses com.example.formatter.Formatter;");
        System.out.println();
        System.out.println("  formatter.json module:");
        System.out.println("    requires formatter.api;");
        System.out.println("    provides com.example.formatter.Formatter");
        System.out.println("      with com.example.formatter.json.JsonFormatter;");
        System.out.println();
        System.out.println("  formatter.xml module:");
        System.out.println("    requires formatter.api;");
        System.out.println("    provides com.example.formatter.Formatter");
        System.out.println("      with com.example.formatter.xml.XmlFormatter;");
        System.out.println();
        System.out.println("Consumer code:");
        System.out.println("  ServiceLoader<Formatter> formatters =");
        System.out.println("      ServiceLoader.load(Formatter.class);");
        System.out.println("  for (Formatter f : formatters) {");
        System.out.println("      System.out.println(f.format(\"test\"));");
        System.out.println("  }");
    }

    /**
     * Exercise 3: Migrate from classpath to module path
     * Given a non-modular JAR, create an automatic module:
     *
     * Steps:
     * 1. Add Automatic-Module-Name to MANIFEST.MF
     * 2. Create module-info.java (optional, for full modularization)
     * 3. Handle split packages
     * 4. Test on module path
     *
     * Migration checklist:
     * - Identify all dependencies
     * - Check for split packages
     * - Replace internal API usage
     * - Add module-info.java
     */
    public static void migrateClasspath() {
        System.out.println("\nExercise 3: Classpath to Module Path Migration");
        System.out.println("Migration steps:");
        System.out.println("1. Add Automatic-Module-Name to MANIFEST.MF:");
        System.out.println("   Automatic-Module-Name: com.example.library");
        System.out.println();
        System.out.println("2. Fix split packages:");
        System.out.println("   Two JARs cannot contain the same package");
        System.out.println("   Rename or merge packages");
        System.out.println();
        System.out.println("3. Replace internal APIs:");
        System.out.println("   sun.misc.BASE64Encoder -> java.util.Base64");
        System.out.println("   com.sun.net.httpserver -> jdk.httpserver");
        System.out.println();
        System.out.println("4. Handle illegal reflective access:");
        System.out.println("   --add-opens java.base/java.lang=ALL-UNNAMED");
        System.out.println("   Better: declare opens in module-info.java");
        System.out.println();
        System.out.println("5. Test on module path:");
        System.out.println("   java --module-path mods -m com.example.app/100");
    }

    /**
     * Exercise 4: Use qualified exports for encapsulation
     * Create a module that:
     * 1. Exports public API to all modules
     * 2. Exports internal API only to specific modules
     * 3. Opens packages for framework reflection
     *
     * Example:
     * - com.example.lib exports com.example.lib.api (public)
     * - com.example.lib exports com.example.lib.internal to com.example.plugin
     * - com.example.lib opens com.example.lib.model to jackson.databind
     */
    public static void demonstrateQualifiedExports() {
        System.out.println("\nExercise 4: Qualified Exports");
        System.out.println("module com.example.lib {");
        System.out.println("    // Public API - available to all");
        System.out.println("    exports com.example.lib.api;");
        System.out.println();
        System.out.println("    // Internal API - only for plugin module");
        System.out.println("    exports com.example.lib.internal to com.example.plugin;");
        System.out.println();
        System.out.println("    // Opens for reflection - only for Jackson");
        System.out.println("    opens com.example.lib.model to com.fasterxml.jackson.databind;");
        System.out.println();
        System.out.println("    // Opens for scripting");
        System.out.println("    opens com.example.lib.scripting to jdk.scripting.nashorn;");
        System.out.println("}");
        System.out.println();
        System.out.println("Benefits:");
        System.out.println("  - Strong encapsulation");
        System.out.println("  - Clear API boundaries");
        System.out.println("  - Framework support via opens");
    }

    /**
     * Exercise 5: Debug module system issues
     * Write code that:
     * 1. Diagnoses common module errors
     * 2. Provides solutions for each error
     * 3. Demonstrates module system debugging tools
     *
     * Common errors:
     * - java.lang.module.FindException: Module X not found
     * - java.lang.IllegalAccessError: Package X not accessible
     * - java.lang.module.ResolutionException: Requires X not found
     */
    public static void debugModuleIssues() {
        System.out.println("\nExercise 5: Debug Module System Issues");
        System.out.println("Common errors and solutions:");
        System.out.println();

        System.out.println("1. FindException: Module not found");
        System.out.println("   Cause: Module not on module path");
        System.out.println("   Fix: java --module-path mods -m com.example.app/100");
        System.out.println("   Debug: java --describe-module com.example.app");
        System.out.println();

        System.out.println("2. IllegalAccessError: Package not accessible");
        System.out.println("   Cause: Package not exported or opens");
        System.out.println("   Fix: Add exports/opens in module-info.java");
        System.out.println("   Debug: java --list-modules");
        System.out.println();

        System.out.println("3. ResolutionException: Requires not found");
        System.out.println("   Cause: Required module not available");
        System.out.println("   Fix: Add all required modules to module path");
        System.out.println("   Debug: jar --describe-module --file=app.jar");
        System.out.println();

        System.out.println("4. SplitPackageException");
        System.out.println("   Cause: Same package in two modules");
        System.out.println("   Fix: Rename or merge packages");
        System.out.println();

        System.out.println("Debugging commands:");
        System.out.println("  java --list-modules");
        System.out.println("  java --describe-module com.example.app");
        System.out.println("  jar --describe-module --file=app.jar");
        System.out.println("  jdeps --module-source-path src src/module-info.java");
        System.out.println("  java -Djdk.module.showAllResolution=true --module-path mods -m app");
    }

    public static void main(String[] args) {
        System.out.println("=== Module System (JPMS) Exercises ===\n");

        // Test Exercise 1
        designModularApp();

        // Test Exercise 2
        demonstrateServiceProvider();

        // Test Exercise 3
        migrateClasspath();

        // Test Exercise 4
        demonstrateQualifiedExports();

        // Test Exercise 5
        debugModuleIssues();

        // Practical ServiceLoader demo
        System.out.println("\n=== ServiceLoader Demo ===");
        ServiceLoader<Formatter> formatters = ServiceLoader.load(Formatter.class);
        Formatter json = new JsonFormatter();
        Formatter xml = new XmlFormatter();
        System.out.println("JSON: " + json.format("Hello"));
        System.out.println("XML: " + xml.format("Hello"));
    }
}
