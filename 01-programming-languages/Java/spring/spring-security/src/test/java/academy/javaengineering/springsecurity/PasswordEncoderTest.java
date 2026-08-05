package academy.javaengineering.springsecurity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PasswordEncoder Tests")
class PasswordEncoderTest {

    private PasswordEncoder bcryptEncoder;
    private PasswordEncoder plainEncoder;

    @BeforeEach
    void setUp() {
        bcryptEncoder = new BCryptPasswordEncoder();
        plainEncoder = new PasswordEncoderExample.PlainTextPasswordEncoder();
    }

    @Test
    @DisplayName("Should encode and verify password with BCrypt")
    void testBCryptEncoding() {
        String rawPassword = "myPassword123";
        String encoded = bcryptEncoder.encode(rawPassword);

        assertNotNull(encoded);
        assertNotEquals(rawPassword, encoded);
        assertTrue(bcryptEncoder.matches(rawPassword, encoded));
        assertFalse(bcryptEncoder.matches("wrongPassword", encoded));
    }

    @Test
    @DisplayName("Should produce different hashes for same input with BCrypt")
    void testBCryptNonDeterministic() {
        String rawPassword = "samePassword";
        String hash1 = bcryptEncoder.encode(rawPassword);
        String hash2 = bcryptEncoder.encode(rawPassword);

        assertNotEquals(hash1, hash2);
        assertTrue(bcryptEncoder.matches(rawPassword, hash1));
        assertTrue(bcryptEncoder.matches(rawPassword, hash2));
    }

    @Test
    @DisplayName("Should encode and verify with PlainText encoder")
    void testPlainTextEncoding() {
        String rawPassword = "plaintext";
        String encoded = plainEncoder.encode(rawPassword);

        assertEquals(rawPassword, encoded);
        assertTrue(plainEncoder.matches(rawPassword, encoded));
    }

    @Test
    @DisplayName("Should validate password strength")
    void testPasswordStrength() {
        var checker = new PasswordEncoderExample.PasswordStrengthChecker();

        assertEquals(PasswordEncoderExample.PasswordStrengthChecker.Strength.WEAK,
                checker.checkStrength("abc"));
        assertEquals(PasswordEncoderExample.PasswordStrengthChecker.Strength.GOOD,
                checker.checkStrength("Password1"));
        assertEquals(PasswordEncoderExample.PasswordStrengthChecker.Strength.STRONG,
                checker.checkStrength("C0mpl3x!P@ssw0rd#2024"));
    }

    @Test
    @DisplayName("Should create encoder via factory")
    void testEncoderFactory() {
        PasswordEncoder bcrypt = PasswordEncoderExample.EncoderFactory.createBCrypt();
        assertNotNull(bcrypt);

        PasswordEncoder delegating = PasswordEncoderExample.EncoderFactory.createDelegating();
        assertNotNull(delegating);
        String encoded = delegating.encode("test");
        assertTrue(delegating.matches("test", encoded));
    }
}
