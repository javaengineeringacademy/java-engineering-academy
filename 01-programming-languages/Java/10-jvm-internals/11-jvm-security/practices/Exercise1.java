package academy.javaengineering.jvm.security;

import java.security.AccessControlException;
import java.security.AllPermission;

/**
 * Exercise 1: Security Manager Configuration
 *
 * Task: Configure Security Manager policies and test permission checks.
 * Run with: java -Djava.security.manager Exercise1
 */
public class Exercise1 {

    public static void main(String[] args) {
        System.out.println("=== Security Manager Configuration ===\n");

        // TODO: Check if Security Manager is enabled
        // TODO: Test file access permission
        // TODO: Test network permission
        // TODO: Test runtime permission

        System.out.println("Security Manager: " + System.getSecurityManager());
    }
}
