package academy.javaengineering.jvm.modulesystem;

import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.util.Optional;
import java.util.Set;

/**
 * Java Platform Module System (JPMS) Deep Dive
 * Covers module-info.java, requires/exports, module path vs classpath,
 * strong encapsulation, and reliable configuration.
 */
public class ModuleSystem {

    public static void main(String[] args) {
        System.out.println("=== Java Platform Module System (JPMS) ===\n");

        demonstrateModuleBasics();
        demonstrateModuleDeclaration();
        demonstrateRequiresAndExports();
        demonstrateModuleVsClasspath();
        demonstrateStrongEncapsulation();
        demonstrateReliableConfiguration();
        demonstrateModuleIntrospection();
    }

    private static void demonstrateModuleBasics() {
        System.out.println("--- 1. Module Basics ---");

        System.out.println("Java 9 Module System (Project Jigsaw):");
        System.out.println("  - Introduced in Java 9 (JEP 261)");
        System.out.println("  - Replaces classpath with module path");
        System.out.println("  - Provides strong encapsulation");
        System.out.println("  - Enables reliable configuration");
        System.out.println("  - Better performance and security\n");

        System.out.println("Key concepts:");
        System.out.println("  - Module: A named, reusable group of packages");
        System.out.println("  - module-info.java: Module descriptor file");
        System.out.println("  - Module path: Replaces classpath for modules");
        System.out.println("  - Module layer: Isolated module environment\n");

        Module currentModule = ModuleSystem.class.getModule();
        System.out.println("Current module: " + currentModule.getName());
        System.out.println("Module classloader: " + currentModule.getClassLoader());
        System.out.println("Is named: " + currentModule.isNamed());
        System.out.println();
    }

    private static void demonstrateModuleDeclaration() {
        System.out.println("--- 2. Module Declaration ---");

        System.out.println("module-info.java syntax:");
        System.out.println("  module com.example.myapp {");
        System.out.println("      requires java.sql;");
        System.out.println("      requires java.logging;");
        System.out.println("      exports com.example.api;");
        System.out.println("      exports com.example.model to com.example.client;");
        System.out.println("      opens com.example.impl to com.example.testing;");
        System.out.println("  }\n");

        System.out.println("Module declaration elements:");
        System.out.println("  - module: Declares a module");
        System.out.println("  - requires: Declares dependency on another module");
        System.out.println("  - requires transitive: Dependency exposed to dependents");
        System.out.println("  - exports: Makes package public to other modules");
        System.out.println("  - exports...to: Restricts exports to specific modules");
        System.out.println("  - opens: Opens package for reflection");
        System.out.println("  - opens...to: Opens to specific modules only");
        System.out.println("  - uses: Declares service consumption");
        System.out.println("  - provides...with: Declares service provider\n");

        System.out.println("Automatic module (unnamed modules on module path):");
        System.out.println("  - JAR without module-info.java");
        System.out.println("  - Module name derived from JAR filename");
        System.out.println("  - All packages are exported\n");
    }

    private static void demonstrateRequiresAndExports() {
        System.out.println("--- 3. Requires and Exports ---");

        System.out.println("requires (dependency):");
        System.out.println("  requires java.sql;          // compile-time dependency");
        System.out.println("  requires transitive java.logging;  // transitive dependency\n");

        System.out.println("Transitive dependency:");
        System.out.println("  If module A requires transitive module B,");
        System.out.println("  then any module requiring A also requires B.");
        System.out.println("  This prevents Split Package problems.\n");

        System.out.println("exports (visibility):");
        System.out.println("  exports com.example.api;    // public to all modules");
        System.out.println("  exports com.example.api     // restricted");
        System.out.println("      to com.example.client;  // only client can see\n");

        System.out.println("opens (reflection):");
        System.out.println("  opens com.example.impl;     // open for reflection to all");
        System.out.println("  opens com.example.impl      // restricted");
        System.out.println("      to com.example.testing; // only testing can reflect\n");

        System.out.println("Key differences:");
        System.out.println("  exports: Compile-time access (imports work)");
        System.out.println("  opens: Runtime access (reflection works)");
        System.out.println("  Both can be restricted with 'to' clause\n");
    }

    private static void demonstrateModuleVsClasspath() {
        System.out.println("--- 4. Module Path vs Classpath ---");

        System.out.println("Classpath (legacy):");
        System.out.println("  java -cp lib/*.jar com.example.Main");
        System.out.println("  - Flat namespace (all classes visible)");
        System.out.println("  - No encapsulation");
        System.out.println("  - Fragile (missing classes at runtime)");
        System.out.println("  - JAR hell\n");

        System.out.println("Module path (modern):");
        System.out.println("  java --module-path mods -m com.example/com.example.Main");
        System.out.println("  - Named modules with encapsulation");
        System.out.println("  - Reliable configuration");
        System.out.println("  - Better performance");
        System.out.println("  - Startup optimization\n");

        System.out.println("Command syntax:");
        System.out.println("  java --module-path mods/...");
        System.out.println("  java -p mods/...                  (short form)");
        System.out.println("  java -m module/class              (module main class)");
        System.out.println("  java --module module/main         (long form)\n");

        System.out.println("Migration strategy:");
        System.out.println("  1. Add module-info.java to each JAR");
        System.out.println("  2. Declare requires for dependencies");
        System.out.println("  3. Export public API packages");
        System.out.println("  4. Open packages needed for reflection");
        System.out.println("  5. Test with --module-path before switching\n");
    }

    private static void demonstrateStrongEncapsulation() {
        System.out.println("--- 5. Strong Encapsulation ---");

        System.out.println("Without module system:");
        System.out.println("  - All public classes accessible via reflection");
        System.out.println("  - Internal APIs can be accessed");
        System.out.println("  - Frameworks rely on deep JDK internals\n");

        System.out.println("With module system:");
        System.out.println("  - Only exported packages are accessible");
        System.out.println("  - Reflection restricted to opened packages");
        System.out.println("  - Internal APIs hidden by default");
        System.out.println("  - Clean API boundaries\n");

        System.out.println("Encapsulation rules:");
        System.out.println("  1. Unexported package: No compile-time access");
        System.out.println("  2. Unopened package: No reflection access");
        System.out.println("  3. Module boundary: Strong encapsulation");
        System.out.println("  4. Reflection requires 'opens' directive\n");

        System.out.println("Migration challenges:");
        System.out.println("  - Frameworks using deep reflection need opens");
        System.out.println("  - Libraries using internal APIs need refactoring");
        System.out.println("  - Testing frameworks need opens for mocking\n");
    }

    private static void demonstrateReliableConfiguration() {
        System.out.println("--- 6. Reliable Configuration ---");

        System.out.println("Classpath problems:");
        System.out.println("  - ClassNotFoundException at runtime");
        System.out.println("  - NoNameFoundError for missing dependencies");
        System.out.println("  - Version conflicts between JARs");
        System.out.println("  - Split packages (same package in multiple JARs)\n");

        System.out.println("Module system solutions:");
        System.out.println("  - All dependencies declared explicitly");
        System.out.println("  - Configuration verified at startup");
        System.out.println("  - No split packages allowed");
        System.out.println("  - Missing modules caught immediately\n");

        System.out.println("Reliable configuration process:");
        System.out.println("  1. Module system resolves all dependencies");
        System.out.println("  2. Checks for split packages");
        System.out.println("  3. Verifies all required modules are present");
        System.out.println("  4. Fails fast if configuration is invalid\n");

        System.out.println("Module resolution:");
        System.out.println("  - Module graph resolution at startup");
        System.out.println("  - Catches missing modules early");
        System.out.println("  - Prevents runtime ClassNotFoundException\n");
    }

    private static void demonstrateModuleIntrospection() {
        System.out.println("--- 7. Module Introspection ---");

        Module module = ModuleSystem.class.getModule();
        System.out.println("Current module: " + module.getName());
        System.out.println("Is named: " + module.isNamed());
        System.out.println("Is automatic: " + module.isAutomatic());
        System.out.println();

        System.out.println("Module descriptor:");
        Optional<ModuleDescriptor> descriptor = module.getDescriptor();
        if (descriptor.isPresent()) {
            ModuleDescriptor desc = descriptor.get();
            System.out.println("  Name: " + desc.name());
            System.out.println("  Requires: " + desc.requires());
            System.out.println("  Exports: " + desc.exports());
            System.out.println("  Opens: " + desc.opens());
            System.out.println("  Provides: " + desc.provides());
            System.out.println("  Uses: " + desc.uses());
        }
        System.out.println();

        System.out.println("Runtime modules:");
        ModuleLayer layer = ModuleLayer.boot();
        Set<Module> modules = layer.modules();
        System.out.println("  Total modules in boot layer: " + modules.size());
        System.out.println();

        System.out.println("Key modules in JDK:");
        System.out.println("  java.base: Core classes (String, Object, etc.)");
        System.out.println("  java.sql: JDBC API");
        System.out.println("  java.logging: Logging API");
        System.out.println("  java.management: JMX API");
        System.out.println("  java.desktop: AWT/Swing");
        System.out.println("  java.xml: XML processing");
        System.out.println();
    }
}
