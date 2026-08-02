package academy.javaengineering.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MethodSecurityTest {

    private MethodSecurityExample example;
    private MethodSecurityExample.SecurityInterceptor interceptor;

    @BeforeEach
    void setUp() {
        example = new MethodSecurityExample();
        interceptor = new MethodSecurityExample.SecurityInterceptor();
    }

    @Test
    void testAssignRole() {
        example.assignRole("user1", "ROLE_ADMIN");
        assertTrue(example.hasRole("user1", "ADMIN"));
    }

    @Test
    void testHasRole() {
        example.assignRole("user1", "ROLE_USER");
        assertTrue(example.hasRole("user1", "USER"));
        assertFalse(example.hasRole("user1", "ADMIN"));
    }

    @Test
    void testHasAnyRole() {
        example.assignRole("user1", "ROLE_VIEWER");
        assertTrue(example.hasAnyRole("user1", "USER", "ADMIN", "VIEWER"));
        assertFalse(example.hasAnyRole("user1", "USER", "ADMIN"));
    }

    @Test
    void testGrantPermission() {
        example.grantPermission("user1", "document", "read");
        assertTrue(example.hasPermission("user1", "document", "read"));
        assertFalse(example.hasPermission("user1", "document", "write"));
    }

    @Test
    void testAdminOnlyOperation() throws Exception {
        example.assignRole("admin", "ROLE_ADMIN");
        Method method = MethodSecurityExample.class.getMethod("adminOnlyOperation");

        boolean adminAccess = interceptor.invoke(method, null, "admin", example);
        assertTrue(adminAccess, "Admin should have access");

        example.assignRole("user", "ROLE_USER");
        boolean userAccess = interceptor.invoke(method, null, "user", example);
        assertFalse(userAccess, "User should not have access");
    }

    @Test
    void testUserOperation() throws Exception {
        example.assignRole("user", "ROLE_USER");
        Method method = MethodSecurityExample.class.getMethod("userOperation");

        boolean userAccess = interceptor.invoke(method, null, "user", example);
        assertTrue(userAccess, "User should have access");

        example.assignRole("viewer", "ROLE_VIEWER");
        boolean viewerAccess = interceptor.invoke(method, null, "viewer", example);
        assertFalse(viewerAccess, "Viewer should not have access");
    }

    @Test
    void testCheckAuthorization() {
        example.assignRole("admin", "ROLE_ADMIN");
        String result = example.checkAuthorization("admin", "ADMIN_ONLY");
        assertTrue(result.contains("ADMIN role"));
    }

    @Test
    void testCheckAuthorizationFailure() {
        example.assignRole("user", "ROLE_USER");
        String result = example.checkAuthorization("user", "ADMIN_ONLY");
        assertTrue(result.contains("lacks"));
    }

    @Test
    void testGetUserSecurityProfile() {
        example.assignRole("user1", "ROLE_USER");
        example.grantPermission("user1", "document", "read");

        Map<String, Object> profile = example.getUserSecurityProfile("user1");
        assertNotNull(profile);
        assertEquals("user1", profile.get("username"));
    }

    @Test
    void testSecurityInterceptorWithExpression() throws Exception {
        example.assignRole("admin", "ROLE_ADMIN");
        Method method = MethodSecurityExample.class.getMethod("adminOnlyOperation");

        boolean access = interceptor.invoke(method, null, "admin", example);
        assertTrue(access);
    }
}
