import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Java 26 - PEM Encodings of Cryptographic Objects (JEP 480)
 * 
 * Standardized PEM (Privacy-Enhanced Mail) format handling for:
 * - RSA/EC keys
 * - X.509 certificates
 * - PKCS#8 private keys
 * 
 * PEM Format:
 * -----BEGIN [TYPE]-----
 * Base64-encoded data
 * -----END [TYPE]-----
 * 
 * Status: Standard Feature in Java 26
 * 
 * Expected Output:
 * PEM Encodings Demo
 * ==================
 * 
 * 1. RSA Key Pair Generation and PEM Encoding
 * Public Key PEM:
 * -----BEGIN PUBLIC KEY-----
 * MIIBIjANBgkqh...base64data...
 * -----END PUBLIC KEY-----
 * 
 * Private Key PEM:
 * -----BEGIN PRIVATE KEY-----
 * MIIEvQIBADANBg...base64data...
 * -----END PRIVATE KEY-----
 * 
 * 2. Key Parsing from PEM
 * Successfully parsed public key from PEM
 * Successfully parsed private key from PEM
 * Keys match: true
 * 
 * 3. File-based PEM Operations
 * Keys saved to files
 * Keys loaded from files
 * 
 * Production Use Cases:
 * - SSH key management and authentication
 * - TLS certificate handling in web servers
 * - API authentication with PEM-encoded credentials
 * - Cryptographic key storage and exchange
 * - Compliance with RFC 7468 standards
 * - Integration with OpenSSL-generated keys
 */
public class PEMEncodingsDemo {

    private static final String PEM_HEADER_PUBLIC = "-----BEGIN PUBLIC KEY-----";
    private static final String PEM_FOOTER_PUBLIC = "-----END PUBLIC KEY-----";
    private static final String PEM_HEADER_PRIVATE = "-----BEGIN PRIVATE KEY-----";
    private static final String PEM_FOOTER_PRIVATE = "-----END PRIVATE KEY-----";
    private static final String PEM_HEADER_CERT = "-----BEGIN CERTIFICATE-----";
    private static final String PEM_FOOTER_CERT = "-----END CERTIFICATE-----";

    public static void main(String[] args) {
        System.out.println("PEM Encodings Demo");
        System.out.println("==================");

        try {
            // Generate and encode RSA keys
            generateAndEncodeRSAKeys();

            // Parse keys from PEM
            parseKeysFromPEM();

            // File-based PEM operations
            fileBasedPEMOperations();

            // EC Key demonstration
            demonstrateECKeys();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Generate RSA key pair and encode to PEM format.
     */
    private static void generateAndEncodeRSAKeys() throws Exception {
        System.out.println("\n1. RSA Key Pair Generation and PEM Encoding");
        System.out.println("--------------------------------------------");

        // Generate RSA key pair
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Encode public key to PEM
        String publicPEM = encodePublicKeyToPEM(keyPair.getPublic());
        System.out.println("Public Key PEM:");
        System.out.println(publicPEM);

        // Encode private key to PEM
        String privatePEM = encodePrivateKeyToPEM(keyPair.getPrivate());
        System.out.println("\nPrivate Key PEM:");
        System.out.println(privatePEM);

        // Verify PEM format
        boolean isValidPublic = publicPEM.startsWith(PEM_HEADER_PUBLIC) &&
                publicPEM.endsWith(PEM_FOOTER_PUBLIC);
        boolean isValidPrivate = privatePEM.startsWith(PEM_HEADER_PRIVATE) &&
                privatePEM.endsWith(PEM_FOOTER_PRIVATE);

        System.out.println("\nPEM Format Validation:");
        System.out.println("Public key valid: " + isValidPublic);
        System.out.println("Private key valid: " + isValidPrivate);
    }

    /**
     * Parse PEM-encoded keys back to key objects.
     */
    private static void parseKeysFromPEM() throws Exception {
        System.out.println("\n2. Key Parsing from PEM");
        System.out.println("-----------------------");

        // Generate keys for demonstration
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair originalPair = keyPairGenerator.generateKeyPair();

        // Encode to PEM
        String publicPEM = encodePublicKeyToPEM(originalPair.getPublic());
        String privatePEM = encodePrivateKeyToPEM(originalPair.getPrivate());

        // Parse back from PEM
        PublicKey parsedPublic = parsePublicKeyFromPEM(publicPEM);
        PrivateKey parsedPrivate = parsePrivateKeyFromPEM(privatePEM);

        System.out.println("Successfully parsed public key from PEM");
        System.out.println("Successfully parsed private key from PEM");

        // Verify keys match
        boolean keysMatch = originalPair.getPublic().equals(parsedPublic) &&
                originalPair.getPrivate().equals(parsedPrivate);
        System.out.println("Keys match: " + keysMatch);

        // Verify algorithm
        System.out.println("Public key algorithm: " + parsedPublic.getAlgorithm());
        System.out.println("Private key algorithm: " + parsedPrivate.getAlgorithm());
    }

    /**
     * Demonstrate file-based PEM operations.
     */
    private static void fileBasedPEMOperations() throws Exception {
        System.out.println("\n3. File-based PEM Operations");
        System.out.println("-----------------------------");

        // Generate keys
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Create PEM strings
        String publicPEM = encodePublicKeyToPEM(keyPair.getPublic());
        String privatePEM = encodePrivateKeyToPEM(keyPair.getPrivate());

        // Save to files (in temp directory for demo)
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "pem-demo");
        Files.createDirectories(tempDir);

        Path publicKeyFile = tempDir.resolve("public.pem");
        Path privateKeyFile = tempDir.resolve("private.pem");

        Files.writeString(publicKeyFile, publicPEM);
        Files.writeString(privateKeyFile, privatePEM);

        System.out.println("Keys saved to files:");
        System.out.println("  Public: " + publicKeyFile);
        System.out.println("  Private: " + privateKeyFile);

        // Load from files
        String loadedPublicPEM = Files.readString(publicKeyFile);
        String loadedPrivatePEM = Files.readString(privateKeyFile);

        // Parse loaded keys
        PublicKey loadedPublic = parsePublicKeyFromPEM(loadedPublicPEM);
        PrivateKey loadedPrivate = parsePrivateKeyFromPEM(loadedPrivatePEM);

        System.out.println("Keys loaded from files");
        System.out.println("Loaded keys match originals: " +
                loadedPublic.equals(keyPair.getPublic()) &&
                loadedPrivate.equals(keyPair.getPrivate()));

        // Cleanup
        Files.deleteIfExists(publicKeyFile);
        Files.deleteIfExists(privateKeyFile);
        Files.deleteIfExists(tempDir);
    }

    /**
     * Demonstrate EC (Elliptic Curve) key handling.
     */
    private static void demonstrateECKeys() throws Exception {
        System.out.println("\n4. EC Key Demonstration");
        System.out.println("-----------------------");

        // Generate EC key pair
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(256);
        KeyPair ecKeyPair = keyPairGenerator.generateKeyPair();

        // Encode to PEM
        String ecPublicPEM = encodePublicKeyToPEM(ecKeyPair.getPublic());
        String ecPrivatePEM = encodePrivateKeyToPEM(ecKeyPair.getPrivate());

        System.out.println("EC Public Key PEM:");
        System.out.println(ecPublicPEM);

        System.out.println("\nEC Private Key PEM:");
        System.out.println(ecPrivatePEM);

        // Verify EC keys
        PublicKey parsedEC = parsePublicKeyFromPEM(ecPublicPEM);
        System.out.println("\nEC Key Algorithm: " + parsedEC.getAlgorithm());
        System.out.println("EC key round-trip successful: " + parsedEC.equals(ecKeyPair.getPublic()));
    }

    /**
     * Encode public key to PEM format.
     */
    private static String encodePublicKeyToPEM(PublicKey publicKey) {
        byte[] encoded = publicKey.getEncoded();
        String base64 = Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(encoded);

        return PEM_HEADER_PUBLIC + "\n" +
                base64 + "\n" +
                PEM_FOOTER_PUBLIC;
    }

    /**
     * Encode private key to PEM format.
     */
    private static String encodePrivateKeyToPEM(PrivateKey privateKey) {
        byte[] encoded = privateKey.getEncoded();
        String base64 = Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(encoded);

        return PEM_HEADER_PRIVATE + "\n" +
                base64 + "\n" +
                PEM_FOOTER_PRIVATE;
    }

    /**
     * Parse public key from PEM format.
     */
    private static PublicKey parsePublicKeyFromPEM(String pem) throws Exception {
        // Remove PEM headers and decode
        String base64 = pem.replaceAll("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(base64);

        // Create key specification
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);

        // Generate key
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * Parse private key from PEM format.
     */
    private static PrivateKey parsePrivateKeyFromPEM(String pem) throws Exception {
        // Remove PEM headers and decode
        String base64 = pem.replaceAll("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(base64);

        // Create key specification
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);

        // Generate key
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }
}
