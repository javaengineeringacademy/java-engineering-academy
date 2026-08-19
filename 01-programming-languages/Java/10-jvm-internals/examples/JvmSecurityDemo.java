package academy.javaengineering.jvm.examples;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;

/**
 * JVM Security Demo
 * Demonstrates SecurityManager, permissions, policies,
 * classloader isolation, and security best practices.
 */
public class JvmSecurityDemo {

    /**
     * DEMO 1: SecurityManager Overview
     * NOTE: SecurityManager is deprecated for removal in Java 17+
     * Included for historical understanding and legacy systems
     */
    public static void demonstrateSecurityManager() {
        System.out.println("=== SecurityManager (Deprecated in Java 17) ===");
        System.out.println("SecurityManager was the JVM's security mechanism");
        System.out.println("It intercepts sensitive operations and checks permissions");
        System.out.println();

        System.out.println("Sensitive operations checked:");
        System.out.println("  - File I/O (read, write, delete)");
        System.out.println("  - Network I/O (connect, accept, listen)");
        System.out.println("  - Thread operations (create, modify)");
        System.out.println("  - System.exit() calls");
        System.out.println("  - Runtime.exec() (process creation)");
        System.out.println("  - Reflection (setAccessible)");
        System.out.println("  - ClassLoader operations");
        System.out.println("  - Clipboard access");
        System.out.println("  - Printing");
        System.out.println();

        System.out.println("Note: Deprecated for removal in Java 17 (JEP 411)");
        System.out.println("Modern alternative: Module system + security policies");
    }

    /**
     * DEMO 2: Permission Types
     */
    public static void demonstratePermissions() {
        System.out.println("\n=== Permission Types ===");

        System.out.println("File permissions:");
        System.out.println("  java.io.FilePermission \"path\" \"read,write,delete,execute\"");
        System.out.println("  java.io.FilePermission \"<<ALL FILES>>\" \"read\"");
        System.out.println();

        System.out.println("Network permissions:");
        System.out.println("  java.net.SocketPermission \"host:port\" \"connect,accept,listen\"");
        System.out.println("  java.net.NetPermission \"specifyStreamHandler\"");
        System.out.println("  java.net.NetPermission \"getProxySelector\"");
        System.out.println();

        System.out.println("Runtime permissions:");
        System.out.println("  java.lang.RuntimePermission \"exitVM\"");
        System.out.println("  java.lang.RuntimePermission \"exec\"");
        System.out.println("  java.lang.RuntimePermission \"loadLibrary.*\"");
        System.out.println("  java.lang.RuntimePermission \"accessClassInPackage.*\"");
        System.out.println();

        System.out.println("Security permissions:");
        System.out.println("  java.security.SecurityPermission \"insertProvider\"");
        System.out.println("  java.security.SecurityPermission \"getPolicy\"");
        System.out.println();

        System.out.println("Property permissions:");
        System.out.println("  java.util.PropertyPermission \"java.home\" \"read\"");
        System.out.println("  java.util.PropertyPermission \"*\" \"read,write\"");
    }

    /**
     * DEMO 3: Policy Files
     */
    public static void demonstratePolicyFiles() {
        System.out.println("\n=== Policy Files ===");
        System.out.println("Policy file format:");
        System.out.println("  grant codeBase \"file:/path/to/app.jar\" {");
        System.out.println("    permission java.io.FilePermission \"/data/*\", \"read\";");
        System.out.println("    permission java.net.SocketPermission \"api.example.com\", \"connect\";");
        System.out.println("  };");
        System.out.println();

        System.out.println("Default policy locations:");
        System.out.println("  $JAVA_HOME/lib/security/java.policy");
        System.out.println("  ~/.java.policy");
        System.out.println();

        System.out.println("System properties:");
        System.out.println("  java.security.policy==<file>  (additive)");
        System.out.println("  java.security.policy=<file>   (replace)");
        System.out.println("  java.security.manager         (enable SM)");
    }

    /**
     * DEMO 4: ClassLoader Isolation for Security
     */
    public static void demonstrateClassLoaderIsolation() {
        System.out.println("\n=== ClassLoader Security Isolation ===");
        System.out.println("Separate classloaders for security boundaries:");
        System.out.println();
        System.out.println("  Web Server (Common CL)");
        System.out.println("  ├── WebApp1 (Custom CL)");
        System.out.println("  │   ├── /WEB-INF/classes");
        System.out.println("  │   └── /WEB-INF/lib/*.jar");
        System.out.println("  └── WebApp2 (Custom CL)");
        System.out.println("      ├── /WEB-INF/classes");
        System.out.println("      └── /WEB-INF/lib/*.jar");
        System.out.println();
        System.out.println("Benefits:");
        System.out.println("  - WebApp1 cannot access WebApp2's classes");
        System.out.println("  - Each webapp has its own namespace");
        System.out.println("  - Classloading can be restricted");
        System.out.println("  - Resources can be isolated");

        System.out.println("\nUsed by:");
        System.out.println("  - Tomcat (WebappClassLoader)");
        System.out.println("  - OSGi (bundle classloaders)");
        System.out.println("  - Java EE (module classloaders)");
    }

    /**
     * DEMO 5: Java Cryptography Architecture (JCA)
     */
    public static void demonstrateJCA() {
        System.out.println("\n=== Java Cryptography Architecture ===");
        System.out.println("Provider-based architecture:");
        System.out.println("  - SunJCE (default)");
        System.out.println("  - Bouncy Castle (third-party)");
        System.out.println();

        System.out.println("Available algorithms:");
        System.out.println("  Symmetric: AES, DES, 3DES, Blowfish");
        System.out.println("  Asymmetric: RSA, DSA, EC");
        System.out.println("  Hashing: SHA-256, SHA-3, MD5");
        System.out.println("  MAC: HmacSHA256, HmacSHA512");
        System.out.println("  Key Exchange: ECDH, Diffie-Hellman");

        // Practical example
        try {
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/GCM/NoPadding");
            System.out.println("\nAES/GCM available: " + (cipher != null));
            System.out.println("Provider: " + cipher.getProvider().getName());
        } catch (Exception e) {
            System.out.println("Crypto provider error: " + e.getMessage());
        }
    }

    /**
     * DEMO 6: Secure Coding Practices
     */
    public static void demonstrateSecureCoding() {
        System.out.println("\n=== Secure Coding Practices ===");

        System.out.println("Input validation:");
        System.out.println("  - Validate all external input");
        System.out.println("  - Use whitelist over blacklist");
        System.out.println("  - Parameterized queries (prevent SQL injection)");
        System.out.println("  - Escape output (prevent XSS)");

        System.out.println("\nPassword handling:");
        System.out.println("  - Never store plain text passwords");
        System.out.println("  - Use PBKDF2, bcrypt, or Argon2");
        System.out.println("  - Use SecureRandom for salt");
        System.out.println("  - Constant-time comparison (MessageDigest.isEqual)");

        System.out.println("\nSecret management:");
        System.out.println("  - Never hardcode secrets in source");
        System.out.println("  - Use environment variables");
        System.out.println("  - Use vault solutions (HashiCorp Vault)");
        System.out.println("  - Rotate secrets regularly");

        System.out.println("\nDeserialization:");
        System.out.println("  - Avoid native Java deserialization");
        System.out.println("  - Use JSON/Jackson instead");
        System.out.println("  - Validate types in ObjectInputStream");
        System.out.println("  - Consider ObjectInputFilter (JDK 9+)");
    }

    /**
     * DEMO 7: Java Module System Security
     */
    public static void demonstrateModuleSecurity() {
        System.out.println("\n=== Module System Security ===");
        System.out.println("JPMS provides strong encapsulation:");
        System.out.println();
        System.out.println("Access control:");
        System.out.println("  - Public API: exported packages");
        System.out.println("  - Internal: unexported packages");
        System.out.println("  - Qualified exports: specific packages only");
        System.out.println();

        System.out.println("module com.example.app {");
        System.out.println("    requires java.sql;");
        System.out.println("    requires transitive java.logging;");
        System.out.println("    exports com.example.api;");
        System.out.println("    opens com.example.model to Jackson;");
        System.out.println("    opens com.example.internal to java.scripting;");
        System.out.println("}");
        System.out.println();

        System.out.println("Security benefits:");
        System.out.println("  - Prevents illegal reflective access");
        System.out.println("  - Enforces module boundaries");
        System.out.println("  - Reduces attack surface");
        System.out.println("  - Clear dependency declarations");
    }

    /**
     * DEMO 8: JVM Security Flags
     */
    public static void demonstrateSecurityFlags() {
        System.out.println("\n=== JVM Security Flags ===");

        System.out.println("Deprecated (for removal):");
        System.out.println("  -Djava.security.manager          Enable SecurityManager");
        System.out.println("  -Djava.security.policy=<file>    Policy file");
        System.out.println("  -Djava.security.auth.login.config=<file>");
        System.out.println();

        System.out.println("Security-related:");
        System.out.println("  -Djava.security.egd=file:/dev/urandom  # Faster RNG");
        System.out.println("  -Djdk.module.illegalAccess=deny       # Block illegal access");
        System.out.println("  -Djava.security.debug=all              # Security debug");
        System.out.println();

        System.out.println("TLS/SSL:");
        System.out.println("  -Djdk.tls.client.protocols=TLSv1.3");
        System.out.println("  -Djdk.tls.client.cipherSuites=TLS_AES_256_GCM_SHA384");
        System.out.println("  -Dhttps.protocols=TLSv1.2,TLSv1.3");
        System.out.println();
        System.out.println("Check supported protocols:");
        System.out.println("  java -Djavax.net.debug=all -version");
    }

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      JVM SECURITY DEMO              ║");
        System.out.println("╚══════════════════════════════════════╝\n");

        demonstrateSecurityManager();
        demonstratePermissions();
        demonstratePolicyFiles();
        demonstrateClassLoaderIsolation();
        demonstrateJCA();
        demonstrateSecureCoding();
        demonstrateModuleSecurity();
        demonstrateSecurityFlags();

        System.out.println("\n=== Security Checklist ===");
        System.out.println("□ Use latest JDK (security patches)");
        System.out.println("□ Enable TLS 1.3");
        System.out.println("□ Use strong algorithms (AES-256, SHA-256)");
        System.out.println("□ Validate all input");
        System.out.println("□ Use parameterized queries");
        System.out.println("□ Don't store secrets in source");
        System.out.println("□ Use SecurityManager (if still available)");
        System.out.println("□ Review permissions regularly");
        System.out.println("□ Enable security debugging for troubleshooting");
    }
}
