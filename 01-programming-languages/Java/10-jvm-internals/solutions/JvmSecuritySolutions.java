package academy.javaengineering.jvm.solutions;

import java.io.*;
import java.security.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * JVM Security Solutions - Complete implementations
 */
public class JvmSecuritySolutions {

    /**
     * Exercise 1 Solution: Custom SecurityManager
     */
    static class CustomSecurityManager extends SecurityManager {
        private final Set<String> allowedReadDirs = new HashSet<>();
        private final Set<String> allowedHosts = new HashSet<>();
        private final List<String> accessLog = new ArrayList<>();

        public CustomSecurityManager() {
            allowedReadDirs.add(System.getProperty("user.home"));
            allowedReadDirs.add("/tmp");
            allowedHosts.add("localhost");
            allowedHosts.add("example.com");
        }

        @Override
        public void checkRead(String file) {
            String logEntry = "READ: " + file;
            accessLog.add(logEntry);
            System.out.println("[SECURITY] " + logEntry);

            // Check if file is in allowed directories
            boolean allowed = allowedReadDirs.stream()
                    .anyMatch(dir -> file.startsWith(dir));

            if (!allowed) {
                System.out.println("  -> DENIED (not in allowed directories)");
                // throw new SecurityException("Read access denied: " + file);
            } else {
                System.out.println("  -> ALLOWED");
            }
        }

        @Override
        public void checkWrite(String file) {
            String logEntry = "WRITE: " + file;
            accessLog.add(logEntry);
            System.out.println("[SECURITY] " + logEntry + " -> DENIED (writes blocked)");
            // throw new SecurityException("Write access denied: " + file);
        }

        @Override
        public void checkConnect(String host, int port) {
            String logEntry = "CONNECT: " + host + ":" + port;
            accessLog.add(logEntry);
            System.out.println("[SECURITY] " + logEntry);

            boolean allowed = allowedHosts.contains(host);
            if (!allowed) {
                System.out.println("  -> DENIED (host not in allowed list)");
            } else {
                System.out.println("  -> ALLOWED");
            }
        }

        @Override
        public void checkExit(int status) {
            String logEntry = "EXIT: " + status;
            accessLog.add(logEntry);
            System.out.println("[SECURITY] " + logEntry);
            // throw new SecurityException("System.exit blocked");
        }

        public List<String> getAccessLog() {
            return Collections.unmodifiableList(accessLog);
        }
    }

    /**
     * Exercise 2 Solution: Permission checking
     */
    public static void checkPermissions() {
        System.out.println("=== Permission Checking ===\n");

        AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
            // Create permission objects
            FilePermission filePerm = new FilePermission(
                    System.getProperty("user.home") + "/test.txt", "read");
            SocketPermission socketPerm = new SocketPermission(
                    "example.com:80", "connect");
            RuntimePermission runtimePerm = new RuntimePermission("exitVM");

            System.out.println("Permission objects created:");
            System.out.println("  File: " + filePerm);
            System.out.println("  Socket: " + socketPerm);
            System.out.println("  Runtime: " + runtimePerm);

            // Check current permissions
            AccessControlContext context = AccessController.getContext();
            PermissionCollection perms = context.getPermissions();

            System.out.println("\nCurrent permissions: " + perms.size());

            // Check specific permissions
            System.out.println("\nPermission checks:");
            System.out.println("  File read: " + filePerm.implies(
                    new FilePermission("/home/user/test.txt", "read")));
            System.out.println("  Socket connect: " + socketPerm.implies(
                    new SocketPermission("example.com:80", "connect")));
            System.out.println("  Exit VM: " + runtimePerm.implies(
                    new RuntimePermission("exitVM")));

            return null;
        });
    }

    /**
     * Exercise 3 Solution: File access controller
     */
    static class FileAccessController {
        private final String allowedBaseDir;
        private final List<String> accessLog = new ArrayList<>();

        public FileAccessController(String allowedBaseDir) {
            this.allowedBaseDir = allowedBaseDir;
        }

        public boolean isPathSafe(String filePath) {
            try {
                // Normalize the path
                File file = new File(filePath);
                String canonicalPath = file.getCanonicalPath();
                String normalizedPath = canonicalPath.replace("\\", "/");

                // Check for path traversal
                if (normalizedPath.contains("..")) {
                    accessLog.add("BLOCKED path traversal: " + filePath);
                    return false;
                }

                // Check if path starts with allowed directory
                String allowedCanonical = new File(allowedBaseDir).getCanonicalPath();
                if (!normalizedPath.startsWith(allowedCanonical.replace("\\", "/"))) {
                    accessLog.add("BLOCKED outside allowed dir: " + filePath);
                    return false;
                }

                accessLog.add("ALLOWED: " + filePath);
                return true;

            } catch (IOException e) {
                accessLog.add("ERROR validating path: " + filePath);
                return false;
            }
        }

        public String secureRead(String filePath) throws IOException {
            if (!isPathSafe(filePath)) {
                throw new SecurityException("Path not allowed: " + filePath);
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                return content.toString();
            }
        }

        public void secureWrite(String filePath, String content) throws IOException {
            if (!isPathSafe(filePath)) {
                throw new SecurityException("Path not allowed: " + filePath);
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write(content);
            }
        }

        public List<String> getAccessLog() {
            return Collections.unmodifiableList(accessLog);
        }
    }

    /**
     * Exercise 4 Solution: Password hashing
     */
    static class PasswordHasher {
        private static final int ITERATIONS = 100000;
        private static final int SALT_LENGTH = 16;
        private static final int KEY_LENGTH = 256;

        public static String hashPassword(String password) throws NoSuchAlgorithmException,
                InvalidKeySpecException {

            // Generate salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);

            // Create PBEKeySpec
            PBEKeySpec spec = new PBEKeySpec(
                    password.toCharArray(),
                    salt,
                    ITERATIONS,
                    KEY_LENGTH
            );

            // Generate hash
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();

            // Combine salt and hash
            byte[] combined = new byte[salt.length + hash.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(hash, 0, combined, salt.length, hash.length);

            return Base64.getEncoder().encodeToString(combined);
        }

        public static boolean verifyPassword(String password, String storedHash)
                throws NoSuchAlgorithmException, InvalidKeySpecException {

            // Decode stored hash
            byte[] combined = Base64.getDecoder().decode(storedHash);

            // Extract salt and hash
            byte[] salt = new byte[SALT_LENGTH];
            byte[] hash = new byte[combined.length - SALT_LENGTH];
            System.arraycopy(combined, 0, salt, 0, salt.length);
            System.arraycopy(combined, salt.length, hash, 0, hash.length);

            // Hash input password with same salt
            PBEKeySpec spec = new PBEKeySpec(
                    password.toCharArray(),
                    salt,
                    ITERATIONS,
                    KEY_LENGTH
            );

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] testHash = factory.generateSecret(spec).getEncoded();

            // Constant-time comparison
            return MessageDigest.isEqual(hash, testHash);
        }
    }

    /**
     * Exercise 5 Solution: Secure random generation
     */
    public static void secureRandomGeneration() {
        System.out.println("=== Secure Random Generation ===\n");

        // Bad: Math.random()
        System.out.println("Insecure methods (AVOID):");
        double badRandom = Math.random();
        System.out.println("  Math.random(): " + badRandom);
        System.out.println("  - Not cryptographically secure");
        System.out.println("  - Predictable seed");

        // Bad: java.util.Random
        java.util.Random badRandom2 = new java.util.Random();
        System.out.println("  java.util.Random: " + badRandom2.nextLong());
        System.out.println("  - Predictable sequence");
        System.out.println("  - Not suitable for security");

        // Good: SecureRandom
        System.out.println("\nSecure methods:");
        SecureRandom secureRandom = new SecureRandom();
        System.out.println("  SecureRandom: " + secureRandom.nextLong());
        System.out.println("  - Cryptographically strong");
        System.out.println("  - Uses /dev/urandom or similar");

        // Generate 32-byte token
        byte[] token = new byte[32];
        secureRandom.nextBytes(token);
        String tokenBase64 = Base64.getEncoder().encodeToString(token);
        System.out.println("\n  Secure token: " + tokenBase64.substring(0, 20) + "...");
        System.out.println("  Token length: " + token.length + " bytes");

        // Generate AES-256 key
        try {
            javax.crypto.KeyGenerator keyGen = javax.crypto.KeyGenerator.getInstance("AES");
            keyGen.init(256, secureRandom);
            javax.crypto.SecretKey key = keyGen.generateKey();
            String keyBase64 = Base64.getEncoder().encodeToString(key.getEncoded());
            System.out.println("\n  AES-256 key: " + keyBase64.substring(0, 20) + "...");
            System.out.println("  Key algorithm: " + key.getAlgorithm());
            System.out.println("  Key format: " + key.getFormat());
        } catch (Exception e) {
            System.out.println("  Key generation error: " + e.getMessage());
        }

        System.out.println("\nBest practices:");
        System.out.println("  - Always use SecureRandom for security");
        System.out.println("  - Use /dev/urandom (not /dev/random) for better performance");
        System.out.println("  - Generate unique salt per password");
        System.out.println("  - Use constant-time comparison (MessageDigest.isEqual)");
    }

    public static void main(String[] args) {
        System.out.println("=== JVM Security Solutions ===\n");

        // Exercise 1
        System.out.println("Exercise 1: Custom SecurityManager");
        CustomSecurityManager sm = new CustomSecurityManager();
        // System.setSecurityManager(sm); // Deprecated in Java 17
        System.out.println("SecurityManager created (deprecated in Java 17+)");
        System.out.println("Access log size: " + sm.getAccessLog().size());

        // Exercise 2
        System.out.println("\n---");
        System.out.println("Exercise 2: Permission Checking");
        checkPermissions();

        // Exercise 3
        System.out.println("\n---");
        System.out.println("Exercise 3: File Access Controller");
        FileAccessController fac = new FileAccessController("/tmp/secure/");

        System.out.println("Path tests:");
        System.out.println("  /tmp/secure/file.txt safe: " +
                fac.isPathSafe("/tmp/secure/file.txt"));
        System.out.println("  /tmp/../../etc/passwd safe: " +
                fac.isPathSafe("/tmp/../../etc/passwd"));
        System.out.println("  /home/user/test.txt safe: " +
                fac.isPathSafe("/home/user/test.txt"));

        System.out.println("\nAccess log:");
        for (String entry : fac.getAccessLog()) {
            System.out.println("  " + entry);
        }

        // Exercise 4
        System.out.println("\n---");
        System.out.println("Exercise 4: Password Hashing");
        try {
            String password = "MySecurePassword123!";
            String hash = PasswordHasher.hashPassword(password);
            System.out.println("Password hash: " + hash.substring(0, Math.min(30, hash.length())) + "...");
            System.out.println("Verification (correct password): " +
                    PasswordHasher.verifyPassword(password, hash));
            System.out.println("Verification (wrong password): " +
                    PasswordHasher.verifyPassword("WrongPassword", hash));
        } catch (Exception e) {
            System.out.println("Hashing error: " + e.getMessage());
        }

        // Exercise 5
        System.out.println("\n---");
        System.out.println("Exercise 5: Secure Random Generation");
        secureRandomGeneration();
    }
}
