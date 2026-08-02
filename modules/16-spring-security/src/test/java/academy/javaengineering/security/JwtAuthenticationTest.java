package academy.javaengineering.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationTest {

    private JwtAuthenticationExample jwtExample;
    private static final String SECRET = "test-secret-key";
    private static final long EXPIRATION_MS = 3600000;

    @BeforeEach
    void setUp() {
        jwtExample = new JwtAuthenticationExample(SECRET, EXPIRATION_MS);
    }

    @Test
    void testGenerateToken() {
        String token = jwtExample.generateToken("testuser", null);
        assertNotNull(token, "Token should not be null");
        assertTrue(token.contains("."), "Token should contain dots");
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length, "Token should have 3 parts");
    }

    @Test
    void testGenerateTokenWithClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", java.util.List.of("USER", "ADMIN"));
        String token = jwtExample.generateToken("testuser", claims);
        assertNotNull(token);
    }

    @Test
    void testValidateValidToken() {
        String token = jwtExample.generateToken("testuser", null);
        JwtAuthenticationExample.JwtClaims claims = jwtExample.validateToken(token);
        assertNotNull(claims);
        assertEquals("testuser", claims.getSubject());
    }

    @Test
    void testValidateInvalidToken() {
        assertThrows(IllegalArgumentException.class, () -> {
            jwtExample.validateToken("invalid.token.here");
        });
    }

    @Test
    void testValidateExpiredToken() {
        JwtAuthenticationExample shortLivedJwt = new JwtAuthenticationExample(SECRET, -1000);
        String token = shortLivedJwt.generateToken("testuser", null);

        assertThrows(SecurityException.class, () -> {
            shortLivedJwt.validateToken(token);
        });
    }

    @Test
    void testValidateTamperedToken() {
        String token = jwtExample.generateToken("testuser", null);
        String tampered = token.substring(0, token.length() - 5) + "XXXXX";

        assertThrows(SecurityException.class, () -> {
            jwtExample.validateToken(tampered);
        });
    }

    @Test
    void testGenerateRefreshToken() {
        String refreshToken = jwtExample.generateRefreshToken("testuser");
        assertNotNull(refreshToken);
        assertFalse(refreshToken.isEmpty());
    }

    @Test
    void testRefreshAccessToken() {
        String refreshToken = jwtExample.generateRefreshToken("testuser");
        String newAccessToken = jwtExample.refreshAccessToken(refreshToken);
        assertNotNull(newAccessToken);

        JwtAuthenticationExample.JwtClaims claims = jwtExample.validateToken(newAccessToken);
        assertEquals("testuser", claims.getSubject());
    }

    @Test
    void testRefreshTokenReplay() {
        String refreshToken = jwtExample.generateRefreshToken("testuser");
        jwtExample.refreshAccessToken(refreshToken);

        assertThrows(SecurityException.class, () -> {
            jwtExample.refreshAccessToken(refreshToken);
        });
    }

    @Test
    void testRevokeRefreshToken() {
        String refreshToken = jwtExample.generateRefreshToken("testuser");
        jwtExample.revokeRefreshToken(refreshToken);

        assertThrows(SecurityException.class, () -> {
            jwtExample.refreshAccessToken(refreshToken);
        });
    }

    @Test
    void testTokenClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", java.util.List.of("USER"));
        String token = jwtExample.generateToken("testuser", claims);

        JwtAuthenticationExample.JwtClaims validated = jwtExample.validateToken(token);
        assertNotNull(validated.getId());
        assertTrue(validated.getIssuedAt() > 0);
        assertTrue(validated.getExpiration() > validated.getIssuedAt());
    }
}
