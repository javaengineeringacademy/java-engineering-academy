package academy.javaengineering.springsecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Demonstrates Spring Security password encoding including BCrypt,
 * DelegatingPasswordEncoder, SCrypt, and custom encoding strategies.
 */
public class PasswordEncoderExample {

    // Custom password encoder for demonstration
    public static class SimpleSha256PasswordEncoder implements PasswordEncoder {
        @Override
        public String encode(CharSequence rawPassword) {
            try {
                java.security.MessageDigest digest =
                        java.security.MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(rawPassword.toString().getBytes(StandardCharsets.UTF_8));
                return Base64.getEncoder().encodeToString(hash);
            } catch (java.security.NoSuchAlgorithmException e) {
                throw new RuntimeException("SHA-256 algorithm not available", e);
            }
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return encode(rawPassword).equals(encodedPassword);
        }
    }

    // Custom No-Op encoder for testing
    public static class PlainTextPasswordEncoder implements PasswordEncoder {
        @Override
        public String encode(CharSequence rawPassword) {
            return rawPassword.toString();
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return rawPassword.toString().equals(encodedPassword);
        }
    }

    // Password strength validator
    public static class PasswordStrengthChecker {
        public enum Strength {
            WEAK, FAIR, GOOD, STRONG
        }

        public Strength checkStrength(String password) {
            int score = 0;
            if (password.length() >= 8) score++;
            if (password.length() >= 12) score++;
            if (password.matches(".*[A-Z].*")) score++;
            if (password.matches(".*[a-z].*")) score++;
            if (password.matches(".*[0-9].*")) score++;
            if (password.matches(".*[!@#$%^&*()_+=\\\\[\\\\]{};':\"\\\\\\\\|,.<>/?-].*")) score++;

            if (score <= 2) return Strength.WEAK;
            if (score <= 3) return Strength.FAIR;
            if (score <= 5) return Strength.GOOD;
            return Strength.STRONG;
        }
    }

    // Password encoder factory
    public static class EncoderFactory {
        public static PasswordEncoder createBCrypt() {
            return new BCryptPasswordEncoder();
        }

        public static PasswordEncoder createBCryptWithStrength(int strength) {
            return new BCryptPasswordEncoder(strength);
        }

        public static PasswordEncoder createDelegating() {
            return new BCryptPasswordEncoder();
        }

        public static PasswordEncoder createSCrypt() {
            return new SCryptPasswordEncoder(16, 8, 1, 32, 64);
        }

        public static PasswordEncoder createPbkdf2() {
            return new Pbkdf2PasswordEncoder("secret", 10000, 256, 10000);
        }

        public static PasswordEncoder createPlainText() {
            return new PlainTextPasswordEncoder();
        }

        public static PasswordEncoder createSha256() {
            return new SimpleSha256PasswordEncoder();
        }
    }

    // Password utility methods
    public static class PasswordUtils {
        public static String generateRandomPassword(int length) {
            String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
            StringBuilder sb = new StringBuilder();
            java.util.Random random = new java.util.Random();
            for (int i = 0; i < length; i++) {
                sb.append(chars.charAt(random.nextInt(chars.length())));
            }
            return sb.toString();
        }

        public static boolean isPasswordReEncodable(PasswordEncoder encoder) {
            String raw = "testpassword";
            String encoded1 = encoder.encode(raw);
            String encoded2 = encoder.encode(raw);
            return !encoded1.equals(encoded2);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Spring Security Password Encoding Examples ===\n");

        // Demo 1: BCrypt Encoding
        System.out.println("--- Demo 1: BCrypt Encoding ---");
        PasswordEncoder bcrypt = EncoderFactory.createBCrypt();
        String rawPassword = "mySecurePassword123";
        String encodedPassword = bcrypt.encode(rawPassword);
        System.out.println("Raw password: " + rawPassword);
        System.out.println("BCrypt encoded: " + encodedPassword);
        System.out.println("Matches: " + bcrypt.matches(rawPassword, encodedPassword));
        System.out.println("BCrypt is non-deterministic: " +
                PasswordUtils.isPasswordReEncodable(bcrypt));

        // Demo 2: BCrypt Strength Levels
        System.out.println("\n--- Demo 2: BCrypt Strength Levels ---");
        for (int strength = 4; strength <= 10; strength += 2) {
            PasswordEncoder encoder = EncoderFactory.createBCryptWithStrength(strength);
            String encoded = encoder.encode("password");
            System.out.println("Strength " + strength + ": " + encoded);
        }

        // Demo 3: DelegatingPasswordEncoder
        System.out.println("\n--- Demo 3: DelegatingPasswordEncoder ---");
        PasswordEncoder delegating = EncoderFactory.createDelegating();
        String delegatingEncoded = delegating.encode("myPassword");
        System.out.println("Delegating encoded: " + delegatingEncoded);
        System.out.println("Matches: " + delegating.matches("myPassword", delegatingEncoded));
        System.out.println("Default encoder: " + delegatingEncoded.substring(0, delegatingEncoded.indexOf('$')));

        // Demo 4: SCrypt Encoding
        System.out.println("\n--- Demo 4: SCrypt Encoding ---");
        PasswordEncoder scrypt = EncoderFactory.createSCrypt();
        String scryptEncoded = scrypt.encode("myPassword");
        System.out.println("SCrypt encoded: " + scryptEncoded);
        System.out.println("Matches: " + scrypt.matches("myPassword", scryptEncoded));

        // Demo 5: Pbkdf2 Encoding
        System.out.println("\n--- Demo 5: Pbkdf2 Encoding ---");
        PasswordEncoder pbkdf2 = EncoderFactory.createPbkdf2();
        String pbkdf2Encoded = pbkdf2.encode("myPassword");
        System.out.println("Pbkdf2 encoded: " + pbkdf2Encoded);
        System.out.println("Matches: " + pbkdf2.matches("myPassword", pbkdf2Encoded));

        // Demo 6: Custom SHA-256 Encoder
        System.out.println("\n--- Demo 6: Custom SHA-256 Encoder ---");
        PasswordEncoder sha256 = EncoderFactory.createSha256();
        String sha256Encoded = sha256.encode("myPassword");
        System.out.println("SHA-256 encoded: " + sha256Encoded);
        System.out.println("Matches: " + sha256.matches("myPassword", sha256Encoded));

        // Demo 7: Password Strength Checking
        System.out.println("\n--- Demo 7: Password Strength ---");
        var strengthChecker = new PasswordStrengthChecker();
        String[] passwords = {"abc", "password", "Password1", "P@ssw0rd123!", "C0mpl3x!P@ssw0rd#2024"};
        for (String pwd : passwords) {
            System.out.println("\"" + pwd + "\" -> " + strengthChecker.checkStrength(pwd));
        }

        // Demo 8: Password Migration
        System.out.println("\n--- Demo 8: Password Migration (Delegating) ---");
        PasswordEncoder legacyEncoder = EncoderFactory.createPlainText();
        PasswordEncoder modernEncoder = EncoderFactory.createBCrypt();

        String legacyHash = legacyEncoder.encode("userPassword");
        System.out.println("Legacy hash: " + legacyHash);

        if (modernEncoder.matches("userPassword", legacyHash)) {
            String newHash = modernEncoder.encode("userPassword");
            System.out.println("Migrated to BCrypt: " + newHash);
            System.out.println("New hash matches: " + modernEncoder.matches("userPassword", newHash));
        } else {
            System.out.println("Legacy hash incompatible, re-encoding needed");
            String newHash = modernEncoder.encode("userPassword");
            System.out.println("New BCrypt hash: " + newHash);
        }

        System.out.println("\n=== All demos completed successfully ===");
    }
}
