package academy.javaengineering.jvm.solutions;

import java.util.ServiceLoader;

/**
 * Module System (JPMS) Solutions - Complete implementations
 */
public class ModuleSystemSolutions {

    /**
     * Exercise 1 Solution: Design a modular application
     */
    public static void designModularApp() {
        System.out.println("=== Modular Application Design ===\n");

        System.out.println("Module Structure:");
        System.out.println();
        System.out.println("com.example.core (module-info.java):");
        System.out.println("  module com.example.core {");
        System.out.println("      exports com.example.core.model;");
        System.out.println("      exports com.example.core.util;");
        System.out.println("  }");
        System.out.println();
        System.out.println("com.example.persistence (module-info.java):");
        System.out.println("  module com.example.persistence {");
        System.out.println("      requires com.example.core;");
        System.out.println("      exports com.example.persistence.repository;");
        System.out.println("      opens com.example.persistence.entity to orm.library;");
        System.out.println("  }");
        System.out.println();
        System.out.println("com.example.service (module-info.java):");
        System.out.println("  module com.example.service {");
        System.out.println("      requires com.example.core;");
        System.out.println("      requires com.example.persistence;");
        System.out.println("      exports com.example.service;");
        System.out.println("  }");
        System.out.println();
        System.out.println("com.example.api (module-info.java):");
        System.out.println("  module com.example.api {");
        System.out.println("      requires com.example.core;");
        System.out.println("      requires com.example.service;");
        System.out.println("      requires transitive java.json;");
        System.out.println("      exports com.example.api.controller;");
        System.out.println("  }");

        System.out.println("\nDependency Graph:");
        System.out.println("  com.example.api");
        System.out.println("    ├── com.example.service");
        System.out.println("    │     ├── com.example.core");
        System.out.println("    │     └── com.example.persistence");
        System.out.println("    │           └── com.example.core");
        System.out.println("    └── com.example.core");
    }

    /**
     * Exercise 2 Solution: Service provider interface
     */
    interface Formatter {
        String format(Object obj);
    }

    static class JsonFormatter implements Formatter {
        @Override
        public String format(Object obj) {
            return "{\"value\": \"" + obj + "\", \"type\": \"json\"}";
        }
    }

    static class XmlFormatter implements Formatter {
        @Override
        public String format(Object obj) {
            return "<value type=\"xml\">" + obj + "</value>";
        }
    }

    public static void demonstrateServiceProvider() {
        System.out.println("\n=== Service Provider Interface ===\n");

        System.out.println("Module declarations:");
        System.out.println();
        System.out.println("formatter.api (module-info.java):");
        System.out.println("  module formatter.api {");
        System.out.println("      exports com.example.formatter;");
        System.out.println("      uses com.example.formatter.Formatter;");
        System.out.println("  }");
        System.out.println();
        System.out.println("formatter.json (module-info.java):");
        System.out.println("  module formatter.json {");
        System.out.println("      requires formatter.api;");
        System.out.println("      provides com.example.formatter.Formatter");
        System.out.println("          with com.example.formatter.json.JsonFormatter;");
        System.out.println("  }");
        System.out.println();
        System.out.println("formatter.xml (module-info.java):");
        System.out.println("  module formatter.xml {");
        System.out.println("      requires formatter.api;");
        System.out.println("      provides com.example.formatter.Formatter");
        System.out.println("          with com.example.formatter.xml.XmlFormatter;");
        System.out.println("  }");

        System.out.println("\nRuntime usage:");
        System.out.println("  ServiceLoader<Formatter> formatters =");
        System.out.println("      ServiceLoader.load(Formatter.class);");
        System.out.println("  for (Formatter f : formatters) {");
        System.out.println("      System.out.println(f.format(\"test\"));");
        System.out.println("  }");

        // Practical demo
        System.out.println("\nPractical demo:");
        Formatter json = new JsonFormatter();
        Formatter xml = new XmlFormatter();
        System.out.println("  JSON: " + json.format("Hello"));
        System.out.println("  XML: " + xml.format("Hello"));
    }

    /**
     * Exercise 3 Solution: Classpath to module path migration
     */
    public static void migrateClasspath() {
        System.out.println("\n=== Classpath to Module Path Migration ===\n");

        System.out.println("Migration Steps:");
        System.out.println();
        System.out.println("1. Identify all dependencies:");
        System.out.println("   jdeps --multi-release 11 --class-path 'lib/*' -s src/**/*.java");
        System.out.println();
        System.out.println("2. Check for split packages:");
        System.out.println("   jdeps --check --class-path 'lib/*' src/module-info.java");
        System.out.println();
        System.out.println("3. Add Automatic-Module-Name to JARs:");
        System.out.println("   MANIFEST.MF:");
        System.out.println("   Automatic-Module-Name: com.example.library");
        System.out.println();
        System.out.println("4. Create module-info.java:");
        System.out.println("   module com.example.myapp {");
        System.out.println("       requires com.example.library;");
        System.out.println("       requires java.sql;");
        System.out.println("       exports com.example.myapp.api;");
        System.out.println("       opens com.example.myapp.model to jackson.databind;");
        System.out.println("   }");
        System.out.println();
        System.out.println("5. Replace internal APIs:");
        System.out.println("   sun.misc.BASE64Encoder -> java.util.Base64");
        System.out.println("   sun.misc.BASE64Decoder -> java.util.Base64");
        System.out.println("   com.sun.net.httpserver -> jdk.httpserver");
        System.out.println("   com.sun.org.apache.xerces -> java.xml");
        System.out.println();
        System.out.println("6. Handle illegal reflective access:");
        System.out.println("   --add-opens java.base/java.lang=ALL-UNNAMED");
        System.out.println("   Better: declare opens in module-info.java");
        System.out.println();
        System.out.println("7. Test on module path:");
        System.out.println("   java --module-path out -m com.example.myapp/com.example.Main");

        System.out.println("\nCommon Issues and Fixes:");
        System.out.println("  SplitPackageException -> Rename or merge packages");
        System.out.println("  IllegalAccessError -> Add exports/opens");
        System.out.println("  FindException -> Add module to module path");
    }

    /**
     * Exercise 4 Solution: Qualified exports
     */
    public static void demonstrateQualifiedExports() {
        System.out.println("\n=== Qualified Exports ===\n");

        System.out.println("module com.example.lib {");
        System.out.println("    // Public API - available to all modules");
        System.out.println("    exports com.example.lib.api;");
        System.out.println();
        System.out.println("    // Internal API - only for plugin module");
        System.out.println("    exports com.example.lib.internal to com.example.plugin;");
        System.out.println();
        System.out.println("    // Opens for Jackson reflection");
        System.out.println("    opens com.example.lib.model to com.fasterxml.jackson.databind;");
        System.out.println();
        System.out.println("    // Opens for Spring DI");
        System.out.println("    opens com.example.lib.service to org.springframework.core;");
        System.out.println();
        System.out.println("    // Opens for scripting");
        System.out.println("    opens com.example.lib.scripting to jdk.scripting.nashorn;");
        System.out.println("}");

        System.out.println("\nAccess Control Matrix:");
        System.out.println("┌───────────────────────┬────────┬────────┬────────┬────────┐");
        System.out.println("│ Package               │ All    │ Plugin │ Jackson│ Spring │");
        System.out.println("├───────────────────────┼────────┼────────┼────────┼────────┤");
        System.out.println("│ lib.api               │ public │ public │ public │ public │");
        System.out.println("│ lib.internal          │ hidden │ public │ hidden │ hidden │");
        System.out.println("│ lib.model             │ public │ public │ reflect│ public │");
        System.out.println("│ lib.service           │ public │ public │ public │ reflect│");
        System.out.println("└───────────────────────┴────────┴────────┴────────┴────────┘");

        System.out.println("\nBenefits:");
        System.out.println("  - Clear API boundaries");
        System.out.println("  - Framework support via opens");
        System.out.println("  - Strong encapsulation");
        System.out.println("  - Prevents illegal reflective access");
    }

    /**
     * Exercise 5 Solution: Debug module system issues
     */
    public static void debugModuleIssues() {
        System.out.println("\n=== Debug Module System Issues ===\n");

        System.out.println("Common Errors and Solutions:");
        System.out.println();

        System.out.println("1. FindException: Module not found");
        System.out.println("   Error: java.lang.module.FindException: Module com.example.app not found");
        System.out.println("   Cause: Module not on module path");
        System.out.println("   Fix: java --module-path out -m com.example.app/com.example.Main");
        System.out.println("   Debug: java --describe-module com.example.app");
        System.out.println();

        System.out.println("2. IllegalAccessError: Package not accessible");
        System.out.println("   Error: java.lang.IllegalAccessError: package com.sun.xml.internal");
        System.out.println("   Cause: Package not exported or opens");
        System.out.println("   Fix: Add exports/opens in module-info.java");
        System.out.println("   Debug: java --list-modules");
        System.out.println();

        System.out.println("3. ResolutionException: Requires not found");
        System.out.println("   Error: java.lang.module.ResolutionException: Requires mysql.connector");
        System.out.println("   Cause: Required module not available");
        System.out.println("   Fix: Add all required modules to module path");
        System.out.println("   Debug: jar --describe-module --file=mysql-connector.jar");
        System.out.println();

        System.out.println("4. SplitPackageException");
        System.out.println("   Error: java.lang.module.FindException: Split package: com.example.util");
        System.out.println("   Cause: Same package in two modules");
        System.out.println("   Fix: Rename or merge packages");
        System.out.println();

        System.out.println("Debugging Commands:");
        System.out.println("  java --list-modules                    # List all modules");
        System.out.println("  java --describe-module com.example.app # Module details");
        System.out.println("  jar --describe-module --file=app.jar   # JAR module info");
        System.out.println("  jdeps --module-source-path src src/module-info.java  # Dependencies");
        System.out.println("  jdeps --check --class-path 'lib/*' src/module-info.java  # Check");
        System.out.println("  java -Djdk.module.showAllResolution=true --module-path mods -m app  # Verbose");
        System.out.println();

        System.out.println("Module Path vs Classpath:");
        System.out.println("  Module Path: -m module/class, --module-path");
        System.out.println("  Classpath: -cp, -classpath (no module system)");
        System.out.println("  Unnamed module: JARs on classpath go to unnamed module");
        System.out.println("  Automatic module: JAR with Automatic-Module-Name");
    }

    public static void main(String[] args) {
        System.out.println("=== Module System (JPMS) Solutions ===\n");

        // Exercise 1
        System.out.println("Exercise 1: Modular Application Design");
        designModularApp();

        // Exercise 2
        System.out.println("\n---");
        System.out.println("Exercise 2: Service Provider Interface");
        demonstrateServiceProvider();

        // Exercise 3
        System.out.println("\n---");
        System.out.println("Exercise 3: Classpath to Module Path Migration");
        migrateClasspath();

        // Exercise 4
        System.out.println("\n---");
        System.out.println("Exercise 4: Qualified Exports");
        demonstrateQualifiedExports();

        // Exercise 5
        System.out.println("\n---");
        System.out.println("Exercise 5: Debug Module System Issues");
        debugModuleIssues();
    }
}
