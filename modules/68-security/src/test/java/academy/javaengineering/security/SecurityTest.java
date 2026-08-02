package academy.javaengineering.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Security Tests")
class SecurityTest {

    @Test
    @DisplayName("Should have security principles")
    void testSecurityPrinciples() {
        var principles = SecurityBestPractices.getSecurityPrinciples();
        assertFalse(principles.isEmpty());
        assertTrue(principles.contains("Least privilege"));
    }

    @Test
    @DisplayName("Should have vulnerability prevention")
    void testVulnerabilityPrevention() {
        var prevention = SecurityBestPractices.getVulnerabilityPrevention();
        assertFalse(prevention.isEmpty());
        assertTrue(prevention.containsKey("SQL Injection"));
    }

    @Test
    @DisplayName("Should validate tokens correctly")
    void testTokenValidation() {
        assertTrue(AuthPatterns.validateToken("mock-jwt-user1"));
        assertFalse(AuthPatterns.validateToken(null));
        assertFalse(AuthPatterns.validateToken("invalid"));
    }
}
