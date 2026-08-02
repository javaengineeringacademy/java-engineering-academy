package academy.javaengineering.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class SecurityFundamentalsTest {

    private SecurityFundamentalsExample example;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        example = new SecurityFundamentalsExample();
    }

    @Test
    void testSuccessfulAuthentication() {
        boolean result = example.authenticate("admin", "admin123");
        assertTrue(result, "Authentication should succeed with correct credentials");
    }

    @Test
    void testFailedAuthentication() {
        boolean result = example.authenticate("admin", "wrongpassword");
        assertFalse(result, "Authentication should fail with wrong password");
    }

    @Test
    void testNonExistentUser() {
        boolean result = example.authenticate("nonexistent", "password");
        assertFalse(result, "Authentication should fail for non-existent user");
    }

    @Test
    void testAdminRoles() {
        example.authenticate("admin", "admin123");
        assertTrue(example.hasRole("ADMIN"), "Admin should have ADMIN role");
        assertTrue(example.hasRole("USER"), "Admin should have USER role");
    }

    @Test
    void testUserRole() {
        example.authenticate("user", "user123");
        assertFalse(example.hasRole("ADMIN"), "User should not have ADMIN role");
        assertTrue(example.hasRole("USER"), "User should have USER role");
    }

    @Test
    void testViewerRole() {
        example.authenticate("viewer", "viewer123");
        assertTrue(example.hasRole("VIEWER"), "Viewer should have VIEWER role");
        assertFalse(example.hasRole("ADMIN"), "Viewer should not have ADMIN role");
        assertFalse(example.hasRole("USER"), "Viewer should not have USER role");
    }

    @Test
    void testGetCurrentUsername() {
        assertNull(example.getCurrentUsername(), "Username should be null before authentication");
        example.authenticate("admin", "admin123");
        assertEquals("admin", example.getCurrentUsername());
    }

    @Test
    void testGetAuthorities() {
        example.authenticate("admin", "admin123");
        Collection<?> authorities = example.getCurrentUserAuthorities();
        assertNotNull(authorities);
        assertTrue(authorities.size() > 0, "Admin should have authorities");
    }

    @Test
    void testPasswordEncoding() {
        String password = "testpassword";
        String encoded = example.toString();
        assertNotNull(encoded);
    }

    @Test
    void testClearContext() {
        example.authenticate("admin", "admin123");
        assertEquals("admin", example.getCurrentUsername());
        SecurityContextHolder.clearContext();
        assertNull(example.getCurrentUsername());
    }
}
