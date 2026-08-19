package academy.javaengineering.jvm.practices;

import java.io.*;
import java.security.*;
import java.util.*;

/**
 * JVM Security Exercises
 * Complete each exercise by implementing the required method.
 * Focus on SecurityManager, permissions, and security best practices.
 */
public class JvmSecurityExercises {

    /**
     * Exercise 1: Create a custom SecurityManager
     * Write a SecurityManager that:
     * 1. Allows file read access to specific directories
     * 2. Blocks file write access
     * 3. Allows network access to specific hosts
     * 4. Logs all security checks
     *
     * NOTE: SecurityManager is deprecated in Java 17
     * This exercise is for understanding legacy systems
     */
    static class CustomSecurityManager extends SecurityManager {
        private final Set<String> allowedReadDirs = new HashSet<>();
        private final Set<String> allowedHosts = new HashSet<>();

        public CustomSecurityManager() {
            allowedReadDirs.add(System.getProperty("user.home"));
            allowedHosts.add("localhost");
            allowedHosts.add("example.com");
        }

        @Override
        public void checkRead(String file) {
            // TODO: Implement file read permission check
            // Check if file is in allowedReadDirs
            System.out.println("[SECURITY] checkRead: " + file);
        }

        @Override
        public void checkWrite(String file) {
            // TODO: Implement file write permission check
            // Block all writes
            System.out.println("[SECURITY] checkWrite (BLOCKED): " + file);
            // throw new SecurityException("Write access denied");
        }

        @Override
        public void checkConnect(String host, int port) {
            // TODO: Implement network permission check
            // Check if host is in allowedHosts
            System.out.println("[SECURITY] checkConnect: " + host + ":" + port);
        }

        @Override
        public void checkExit(int status) {
            // TODO: Implement exit permission check
            System.out.println("[SECURITY] checkExit: " + status);
            // throw new SecurityException("System.exit blocked");
        }
    }

    /**
     * Exercise 2: Implement permission checking
     * Write code that:
     * 1. Creates different permission types
     * 2. Checks if current code has specific permissions
     * 3. Handles AccessControlException
     *
     * Permission types to check:
     * - FilePermission
     * - SocketPermission
     * - RuntimePermission
     */
    public static void checkPermissions() {
        // TODO: Implement permission checking
        AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
            try {
                // Check file read permission
                FilePermission filePerm = new FilePermission(
                        System.getProperty("user.home") + "/test.txt", "read");
                System.out.println("File read permission created: " + filePerm);

                // Check socket permission
                SocketPermission socketPerm = new SocketPermission(
                        "example.com:80", "connect");
                System.out.println("Socket permission created: " + socketPerm);

                // Check runtime permission
                RuntimePermission runtimePerm = new RuntimePermission("exitVM");
                System.out.println("Runtime permission created: " + runtimePerm);

                // Check current permissions
                PermissionCollection perms = AccessController.getContext().getPermissions();
                System.out.println("\nCurrent permissions: " + perms.size());

            } catch (Exception e) {
                System.out.println("Permission check failed: " + e.getMessage());
            }
            return null;
        });
    }

    /**
     * Exercise 3: Implement a file access controller
     * Write code that:
     * 1. Defines allowed directories for read/write
     * 2. Validates file paths before access
     * 3. Prevents path traversal attacks (../)
     * 4. Logs all access attempts
     *
     * Security requirements:
     * - Only allow access to /tmp/secure/ directory
     * - Block path traversal (../)
     * - Log all access attempts
     */
    static class FileAccessController {
        private final String allowedBaseDir;
        private final List<String> accessLog = new ArrayList<>();

        public FileAccessController(String allowedBaseDir) {
            this.allowedBaseDir = allowedBaseDir;
        }

        // TODO: Implement isPathSafe method
        public boolean isPathSafe(String filePath) {
            // 1. Normalize the path
            // 2. Check for path traversal (../)
            // 3. Verify path starts with allowedBaseDir
            return false;
        }

        // TODO: Implement secureRead method
        public String secureRead(String filePath) throws IOException {
            // 1. Validate path
            // 2. Log access attempt
            // 3. Read file if valid
            // 4. Throw exception if invalid
            throw new SecurityException("Not implemented");
        }

        // TODO: Implement secureWrite method
        public void secureWrite(String filePath, String content) throws IOException {
            // 1. Validate path
            // 2. Log access attempt
            // 3. Write file if valid
            // 4. Throw exception if invalid
            throw new SecurityException("Not implemented");
        }

        public List<String> getAccessLog() {
            return Collections.unmodifiableList(accessLog);
        }
    }

    /**
     * Exercise 4: Implement password hashing
     * Write secure password handling code:
     * 1. Hash password with salt using PBKDF2
     * 2. Verify password against stored hash
     * 3. Use constant-time comparison
     *
     * Requirements:
     * - Use SecureRandom for salt generation
     * - Use PBKDF2WithHmacSHA256
     * - Minimum 100,000 iterations
     */
    static class PasswordHasher {
        private static final int ITERATIONS = 100000;
        private static final int SALT_LENGTH = 16;
        private static final int KEY_LENGTH = 256;

        // TODO: Implement hashPassword method
        public static String hashPassword(String password) throws NoSuchAlgorithmException,
                InvalidKeySpecException {
            // 1. Generate salt using SecureRandom
            // 2. Create PBEKeySpec with password, salt, iterations, key length
            // 3. Generate secret key using SecretKeyFactory
            // 4. Return Base64 encoded salt + hash
            return null;
        }

        // TODO: Implement verifyPassword method
        public static boolean verifyPassword(String password, String storedHash)
                throws NoSuchAlgorithmException, InvalidKeySpecException {
            // 1. Decode stored hash
            // 2. Extract salt
            // 3. Hash input password with same salt
            // 4. Use MessageDigest.isEqual for constant-time comparison
            return false;
        }
    }

    /**
     * Exercise 5: Implement secure random number generation
     * Write code that:
     * 1. Generates cryptographically secure random numbers
     * 2. Avoids common pitfalls (Math.random, java.util.Random)
     * 3. Generates secure tokens/keys
     *
     * Requirements:
     * - Use SecureRandom
     * - Generate 32-byte token
     * - Generate AES-256 key
     */
    public static void secureRandomGeneration() {
        // TODO: Implement secure random generation

        // Bad: Math.random() - not cryptographically secure
        double badRandom = Math.random();
        System.out.println("Math.random() (AVOID): " + badRandom);

        // Bad: java.util.Random - predictable
        java.util.Random badRandom2 = new java.util.Random();
        System.out.println("java.util.Random (AVOID): " + badRandom2.nextLong());

        // Good: SecureRandom
        SecureRandom secureRandom = new SecureRandom();
        System.out.println("SecureRandom: " + secureRandom.nextLong());

        // Generate 32-byte token
        byte[] token = new byte[32];
        secureRandom.nextBytes(token);
        System.out.println("Secure token (Base64): " +
                java.util.Base64.getEncoder().encodeToString(token));

        // Generate AES-256 key
        try {
            javax.crypto.KeyGenerator keyGen = javax.crypto.KeyGenerator.getInstance("AES");
            keyGen.init(256, secureRandom);
            javax.crypto.SecretKey key = keyGen.generateKey();
            System.out.println("AES-256 key (Base64): " +
                    java.util.Base64.getEncoder().encodeToString(key.getEncoded()));
        } catch (Exception e) {
            System.out.println("Key generation error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("=== JVM Security Exercises ===\n");

        // Test Exercise 1
        System.out.println("Exercise 1: Custom SecurityManager");
        // CustomSecurityManager sm = new CustomSecurityManager();
        // System.setSecurityManager(sm);
        System.out.println("SecurityManager is deprecated in Java 17+");
        System.out.println("For understanding legacy systems only");

        // Test Exercise 2
        System.out.println("\nExercise 2: Permission Checking");
        checkPermissions();

        // Test Exercise 3
        System.out.println("\nExercise 3: File Access Controller");
        FileAccessController fac = new FileAccessController("/tmp/secure/");
        System.out.println("Path /tmp/secure/file.txt safe: " +
                fac.isPathSafe("/tmp/secure/file.txt"));
        System.out.println("Path /tmp/../../etc/passwd safe: " +
                fac.isPathSafe("/tmp/../../etc/passwd"));

        // Test Exercise 4
        System.out.println("\nExercise 4: Password Hashing");
        try {
            String password = "MySecurePassword123!";
            String hash = PasswordHasher.hashPassword(password);
            System.out.println("Password hash: " + hash.substring(0, 20) + "...");
            System.out.println("Verification (correct): " +
                    PasswordHasher.verifyPassword(password, hash));
            System.out.println("Verification (wrong): " +
                    PasswordHasher.verifyPassword("WrongPassword", hash));
        } catch (Exception e) {
            System.out.println("Hashing error: " + e.getMessage());
        }

        // Test Exercise 5
        System.out.println("\nExercise 5: Secure Random Generation");
        secureRandomGeneration();
    }
}
