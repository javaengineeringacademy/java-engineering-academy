package academy.javaengineering.jvm.modules;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;

/**
 * Solution 1: Basic Module Creation
 */
public class Solution1 {

    private static final Logger logger = Logger.getLogger(Solution1.class.getName());

    public static void main(String[] args) {
        System.out.println("=== Basic Module Creation ===\n");

        // Demonstrate module access
        System.out.println("Module: " + Solution1.class.getModule().getName());
        System.out.println("Package: " + Solution1.class.getPackageName());

        // Show JDK modules
        System.out.println("\nJDK Modules loaded:");
        ModuleLayer.boot().modules().stream()
            .limit(5)
            .forEach(m -> System.out.println("  " + m.getName()));

        // module-info.java should contain:
        System.out.println("\nmodule-info.java:");
        System.out.println("  module com.example.app {");
        System.out.println("      requires java.sql;");
        System.out.println("      requires java.logging;");
        System.out.println("      exports com.example.api;");
        System.out.println("  }");
    }
}
