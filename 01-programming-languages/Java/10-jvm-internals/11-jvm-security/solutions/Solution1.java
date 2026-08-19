package academy.javaengineering.jvm.security;

import java.io.*;
import java.nio.file.*;

/**
 * Solution 1: Security Manager Configuration
 */
public class Solution1 {

    public static void main(String[] args) {
        System.out.println("=== Security Manager Configuration ===\n");

        // Check Security Manager status
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            System.out.println("Security Manager is ENABLED");
            System.out.println("Class: " + sm.getClass().getName());
        } else {
            System.out.println("Security Manager is DISABLED");
            System.out.println("Run with: java -Djava.security.manager Solution1");
        }

        // Test file access
        System.out.println("\n--- File Access Test ---");
        try {
            Files.readString(Path.of("/etc/hosts"));
            System.out.println("File access: ALLOWED");
        } catch (AccessControlException e) {
            System.out.println("File access: DENIED - " + e.getMessage());
        } catch (Exception e) {
            System.out.println("File access: ERROR - " + e.getMessage());
        }

        // Test runtime permission
        System.out.println("\n--- Runtime Permission Test ---");
        try {
            Runtime.getRuntime().exec("echo test");
            System.out.println("Runtime exec: ALLOWED");
        } catch (AccessControlException e) {
            System.out.println("Runtime exec: DENIED - " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Runtime exec: ERROR - " + e.getMessage());
        }
    }
}
