package academy.javaengineering.jvm.examples;

import java.util.ServiceLoader;

/**
 * JPMS (Java Platform Module System) Demo
 * Demonstrates module declarations, requires/exports, services,
 * migration from classpath, and module system benefits.
 */
public class ModuleSystemDemo {

    /**
     * DEMO 1: Module Declaration Syntax
     */
    public static void demonstrateModuleSyntax() {
        System.out.println("=== Module Declaration Syntax ===");
        System.out.println("module-info.java (at root of source tree):");
        System.out.println();
        System.out.println("  module com.example.myapp {");
        System.out.println("      // Dependencies");
        System.out.println("      requires java.sql;");
        System.out.println("      requires transitive java.logging;");
        System.out.println("      requires static java.management;");
        System.out.println();
        System.out.println("      // Exported packages");
        System.out.println("      exports com.example.api;");
        System.out.println("      exports com.example.model;");
        System.out.println();
        System.out.println("      // Qualified exports (to specific modules)");
        System.out.println("      exports com.example.internal to com.example.plugin;");
        System.out.println();
        System.out.println("      // Opens packages for deep reflection");
        System.out.println("      opens com.example.model to jackson.databind;");
        System.out.println("      opens com.example.config to spring.core;");
        System.out.println();
        System.out.println("      // Service registration");
        System.out.println("      provides com.example.spi.Plugin with com.example.impl.PluginImpl;");
        System.out.println("      uses com.example.spi.Formatter;");
        System.out.println();
        System.out.println("      // Main class (JDK 9+)");
        System.out.println("      mainClass com.example.Main;");
        System.out.println("  }");
    }

    /**
     * DEMO 2: Requires Directives
     */
    public static void demonstrateRequires() {
        System.out.println("\n=== Requires Directives ===");

        System.out.println("requires <module>:");
        System.out.println("  - Mandatory dependency");
        System.out.println("  - Module must be on module path");
        System.out.println("  - All exported packages accessible");

        System.out.println("\nrequires transitive <module>:");
        System.out.println("  - Dependency is transitive");
        System.out.println("  - Modules that depend on THIS module");
        System.out.println("    automatically get access to <module>");
        System.out.println("  - Use when your API exposes types from <module>");

        System.out.println("\nrequires static <module>:");
        System.out.println("  - Compile-time only dependency");
        System.out.println("  - Optional at runtime");
        System.out.println("  - Module may be absent at runtime");

        System.out.println("\nrequires <module> marked:");
        System.out.println("  - (JDK 9) prevents reflection access");
        System.out.println("  - Deprecated in JDK 9, removed in JDK 17");

        System.out.println("\nExamples:");
        System.out.println("  requires java.base;           // Implicit, always available");
        System.out.println("  requires java.sql;            // JDBC");
        System.out.println("  requires transitive java.desktop; // AWT/Swing");
        System.out.println("  requires static java.management;  // JMX (optional)");
        System.out.println("  requires gson;                // Third-party library");
    }

    /**
     * DEMO 3: Exports and Opens
     */
    public static void demonstrateExportsOpens() {
        System.out.println("\n=== Exports and Opens Directives ===");

        System.out.println("exports <package>:");
        System.out.println("  - Makes package public");
        System.out.println("  - Only public types accessible");
        System.out.println("  - No deep reflection");

        System.out.println("\nexports <package> to <module1>, <module2>:");
        System.out.println("  - Qualified export");
        System.out.println("  - Only specified modules can access");
        System.out.println("  - All other modules see package as unexported");

        System.out.println("\nopens <package>:");
        System.out.println("  - Allows deep reflective access");
        System.out.println("  - All types accessible via reflection");
        System.out.println("  - Required for frameworks using reflection");

        System.out.println("\nopens <package> to <module>:");
        System.out.println("  - Qualified opens");
        System.out.println("  - Only specified module can reflect");

        System.out.println("\nWhen to use opens:");
        System.out.println("  - JSON serialization (Jackson, Gson)");
        System.out.println("  - ORM frameworks (Hibernate)");
        System.out.println("  - Dependency injection (Spring)");
        System.out.println("  - Testing frameworks (JUnit)");
    }

    /**
     * DEMO 4: Module Services (SPI)
     */
    public static void demonstrateServices() {
        System.out.println("\n=== Module Services (SPI) ===");
        System.out.println("Service Provider Interface pattern:");
        System.out.println();
        System.out.println("Provider module:");
        System.out.println("  provides com.example.spi.Plugin with com.example.impl.PluginImpl;");
        System.out.println();
        System.out.println("Consumer module:");
        System.out.println("  uses com.example.spi.Plugin;");
        System.out.println();
        System.out.println("Runtime usage:");
        System.out.println("  ServiceLoader<Plugin> loader = ServiceLoader.load(Plugin.class);");
        System.out.println("  for (Plugin p : loader) {");
        System.out.println("      p.execute();");
        System.out.println("  }");
        System.out.println();

        System.out.println("Benefits:");
        System.out.println("  - Decoupling between provider and consumer");
        System.out.println("  - Module system manages service wiring");
        System.out.println("  - No classpath scanning needed");
        System.out.println("  - Strong encapsulation maintained");

        // Demonstrate ServiceLoader
        System.out.println("\nServiceLoader in action:");
        ServiceLoader<String> stringLoader = ServiceLoader.load(String.class);
        System.out.println("  String services found: 0 (no providers in java.base)");
    }

    /**
     * DEMO 5: Migration from Classpath
     */
    public static void demonstrateMigration() {
        System.out.println("\n=== Migration from Classpath ===");
        System.out.println("Strategy 1: Modular from scratch");
        System.out.println("  - Create module-info.java");
        System.out.println("  - Declare all dependencies");
        System.out.println("  - Run and fix errors iteratively");
        System.out.println();

        System.out.println("Strategy 2: Automatic module (JAR on module path)");
        System.out.println("  - JAR without module-info.java");
        System.out.println("  - Module name derived from manifest or filename");
        System.out.println("  - Can require and export packages");
        System.out.println("  - Good intermediate step");

        System.out.println("\nAutomatic module name resolution:");
        System.out.println("  1. MANIFEST.MF: Automatic-Module-Name: com.example.lib");
        System.out.println("  2. Filename: mylib-1.0.jar -> mylib (remove version)");
        System.out.println("  3. JarFile.getManifest() -> Automatic-Module-Name");

        System.out.println("\nStrategy 3: Multi-release JAR");
        System.out.println("  - Support both classpath and module path");
        System.out.println("  - module-info.java in root and META-INF/versions/");

        System.out.println("\nMigration checklist:");
        System.out.println("  □ Add module-info.java");
        System.out.println("  □ Fix split packages");
        System.out.println("  □ Fix illegal reflective access");
        System.out.println("  □ Replace sun.misc.* with java.* equivalents");
        System.out.println("  □ Test on module path");
    }

    /**
     * DEMO 6: Common Module Issues
     */
    public static void demonstrateCommonIssues() {
        System.out.println("\n=== Common Module Issues ===");

        System.out.println("Split package (FATAL):");
        System.out.println("  Two modules export same package");
        System.out.println("  Fix: Merge or rename packages");
        System.out.println();

        System.out.println("Illegal reflective access:");
        System.out.println("  --add-opens java.base/java.lang=ALL-UNNAMED");
        System.out.println("  Better: declare opens in module-info.java");
        System.out.println();

        System.out.println("Missing exports:");
        System.out.println("  Exception: java.lang.IllegalAccessError");
        System.out.println("  Fix: Add appropriate exports/opens");
        System.out.println();

        System.out.println("Automatic module name not set:");
        System.out.println("  Warning: Required automatic module name not found");
        System.out.println("  Fix: Add Automatic-Module-Name to MANIFEST.MF");
        System.out.println();

        System.out.println("Modules on classpath:");
        System.out.println("  module-info.class is ignored on classpath");
        System.out.println("  All packages automatically in unnamed module");
    }

    /**
     * DEMO 7: Useful Module Commands
     */
    public static void demonstrateModuleCommands() {
        System.out.println("\n=== Useful Module Commands ===");

        System.out.println("List modules:");
        System.out.println("  java --list-modules");
        System.out.println("  jar --describe-module --file=app.jar");
        System.out.println();

        System.out.println("Compile with modules:");
        System.out.println("  javac -d out --module-source-path src $(find src -name '*.java')");
        System.out.println("  javac -d out -cp lib/* --module-path mods src/module-info.java");
        System.out.println();

        System.out.println("Run module:");
        System.out.println("  java --module-path mods -m com.example.app/com.example.Main");
        System.out.println("  java --module-path mods --module com.example.app/100");
        System.out.println();

        System.out.println("Debug module system:");
        System.out.println("  java -Djdk.module.showAllResolution=true --module-path mods -m app");
        System.out.println("  java --describe-module com.example.app");
        System.out.println();

        System.out.println("Module graph analysis:");
        System.out.println("  jdeps --module-source-path src src/module-info.java");
        System.out.println("  jdeps --module-path mods -m com.example.app");
    }

    /**
     * DEMO 8: Module System Benefits
     */
    public static void demonstrateBenefits() {
        System.out.println("\n=== Module System Benefits ===");

        System.out.println("Strong encapsulation:");
        System.out.println("  - Internal APIs hidden");
        System.out.println("  - Controlled public surface");
        System.out.println("  - Prevents illegal reflection");

        System.out.println("\nReliable configuration:");
        System.out.println("  - Explicit dependencies");
        System.out.println("  - Compile-time resolution");
        System.out.println("  - No ClassNotFoundException at runtime");

        System.out.println("\nImproved security:");
        System.out.println("  - Reduced attack surface");
        System.out.println("  - Access control enforced by JVM");
        System.out.println("  - Clear permission boundaries");

        System.out.println("\nBetter performance:");
        System.out.println("  - Ahead-of-time compilation potential");
        System.out.println("  - Reduced classpath scanning");
        System.out.println("  - More efficient JIT (closed-world assumption)");
        System.out.println("  - jlink: custom runtime images");

        System.out.println("\nEcosystem:");
        System.out.println("  - Jigsaw (JDK 9)");
        System.out.println("  - OSGi alternative");
        System.out.println("  - Spring Framework 5+");
        System.out.println("  - Most major libraries support modules");
    }

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   JPMS MODULE SYSTEM DEMO           ║");
        System.out.println("╚══════════════════════════════════════╝\n");

        demonstrateModuleSyntax();
        demonstrateRequires();
        demonstrateExportsOpens();
        demonstrateServices();
        demonstrateMigration();
        demonstrateCommonIssues();
        demonstrateModuleCommands();
        demonstrateBenefits();
    }
}
