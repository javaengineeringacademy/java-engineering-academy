package academy.javaengineering.springsecurity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Authorization Tests")
class AuthorizationTest {

    private AuthorizationExample.AuthorizedUser adminUser;
    private AuthorizationExample.AuthorizedUser regularUser;
    private AuthorizationExample.AuthorizedUser managerUser;
    private AuthorizationExample.AccessDecisionSimulator decisionManager;
    private AuthorizationExample.RoleHierarchy roleHierarchy;
    private AuthorizationExample.PreAuthorizeEvaluator evaluator;

    @BeforeEach
    void setUp() {
        adminUser = new AuthorizationExample.AuthorizedUser("admin",
                Set.of(AuthorizationExample.Role.ROLE_ADMIN, AuthorizationExample.Role.ROLE_USER));
        regularUser = new AuthorizationExample.AuthorizedUser("john",
                Set.of(AuthorizationExample.Role.ROLE_USER));
        managerUser = new AuthorizationExample.AuthorizedUser("jane",
                Set.of(AuthorizationExample.Role.ROLE_MANAGER, AuthorizationExample.Role.ROLE_USER));
        decisionManager = new AuthorizationExample.AccessDecisionSimulator();
        roleHierarchy = new AuthorizationExample.RoleHierarchy();
        evaluator = new AuthorizationExample.PreAuthorizeEvaluator();
    }

    @Test
    @DisplayName("Should check user roles correctly")
    void testRoleChecking() {
        assertTrue(adminUser.hasRole(AuthorizationExample.Role.ROLE_ADMIN));
        assertFalse(regularUser.hasRole(AuthorizationExample.Role.ROLE_ADMIN));
        assertTrue(managerUser.hasRole(AuthorizationExample.Role.ROLE_MANAGER));
    }

    @Test
    @DisplayName("Should check multiple roles with hasAnyRole")
    void testHasAnyRole() {
        assertTrue(adminUser.hasAnyRole(AuthorizationExample.Role.ROLE_ADMIN,
                AuthorizationExample.Role.ROLE_MANAGER));
        assertFalse(regularUser.hasAnyRole(AuthorizationExample.Role.ROLE_ADMIN,
                AuthorizationExample.Role.ROLE_MANAGER));
    }

    @Test
    @DisplayName("Should make access decisions correctly")
    void testAccessDecisions() {
        Authentication adminAuth = new UsernamePasswordAuthenticationToken(
                adminUser, null, adminUser.getAuthorities());
        Authentication userAuth = new UsernamePasswordAuthenticationToken(
                regularUser, null, regularUser.getAuthorities());

        var adminAttr = new org.springframework.security.access.SecurityConfig("ROLE_ADMIN");
        var userAttr = new org.springframework.security.access.SecurityConfig("ROLE_USER");

        assertTrue(decisionManager.decide(adminAuth, adminAttr));
        assertFalse(decisionManager.decide(userAuth, adminAttr));
        assertTrue(decisionManager.decide(userAuth, userAttr));
    }

    @Test
    @DisplayName("Should handle role hierarchy correctly")
    void testRoleHierarchy() {
        assertEquals(4, roleHierarchy.getReachableRoles(AuthorizationExample.Role.ROLE_ADMIN).size());
        assertEquals(2, roleHierarchy.getReachableRoles(AuthorizationExample.Role.ROLE_MANAGER).size());

        assertTrue(roleHierarchy.userHasRole(adminUser, AuthorizationExample.Role.ROLE_USER));
        assertTrue(roleHierarchy.userHasRole(managerUser, AuthorizationExample.Role.ROLE_USER));
        assertFalse(roleHierarchy.userHasRole(regularUser, AuthorizationExample.Role.ROLE_ADMIN));
    }

    @Test
    @DisplayName("Should evaluate PreAuthorize expressions")
    void testPreAuthorizeExpressions() {
        Authentication adminAuth = new UsernamePasswordAuthenticationToken(
                adminUser, null, adminUser.getAuthorities());
        Authentication userAuth = new UsernamePasswordAuthenticationToken(
                regularUser, null, regularUser.getAuthorities());

        assertTrue(evaluator.evaluateHasRole(adminAuth, "ROLE_ADMIN"));
        assertFalse(evaluator.evaluateHasRole(userAuth, "ROLE_ADMIN"));
        assertTrue(evaluator.evaluateHasAnyRole(userAuth, "ROLE_USER", "ROLE_ADMIN"));
        assertTrue(evaluator.evaluateIsAuthenticated(adminAuth));
    }
}
