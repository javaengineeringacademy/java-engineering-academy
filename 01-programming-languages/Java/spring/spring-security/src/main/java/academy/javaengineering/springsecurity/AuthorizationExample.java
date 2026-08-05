package academy.javaengineering.springsecurity;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Demonstrates Spring Security authorization concepts including
 * role-based access control, method security, and access decisions.
 */
public class AuthorizationExample {

    // Simulated roles enum
    public enum Role {
        ROLE_USER("User"),
        ROLE_ADMIN("Administrator"),
        ROLE_MANAGER("Manager"),
        ROLE_MODERATOR("Moderator"),
        ROLE_AUDITOR("Auditor");

        private final String displayName;

        Role(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // User with roles for authorization demos
    public record AuthorizedUser(String username, Set<Role> roles) {
        public boolean hasRole(Role role) {
            return roles.contains(role);
        }

        public boolean hasAnyRole(Role... roles) {
            return Arrays.stream(roles).anyMatch(this.roles::contains);
        }

        public List<GrantedAuthority> getAuthorities() {
            return roles.stream()
                    .map(role -> (GrantedAuthority) new SimpleGrantedAuthority(role.name()))
                    .toList();
        }
    }

    // Method security annotations (for documentation purposes)
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiresRole {
        Role[] value();
    }

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiresAllRoles {
        Role[] value();
    }

    // Simulated access decision manager
    public static class AccessDecisionSimulator {
        public boolean decide(Authentication authentication, ConfigAttribute requiredAttribute) {
            if (authentication == null || !authentication.isAuthenticated()) {
                return false;
            }

            String requiredRole = requiredAttribute.getAttribute();
            if (requiredRole == null) {
                return true;
            }

            return authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals(requiredRole));
        }

        public boolean decideAny(Authentication authentication, ConfigAttribute... attributes) {
            return Arrays.stream(attributes).anyMatch(attr -> decide(authentication, attr));
        }

        public boolean decideAll(Authentication authentication, ConfigAttribute... attributes) {
            return Arrays.stream(attributes).allMatch(attr -> decide(authentication, attr));
        }
    }

    // Role hierarchy simulation
    public static class RoleHierarchy {
        private final Map<Role, Set<Role>> hierarchy;

        public RoleHierarchy() {
            hierarchy = Map.of(
                    Role.ROLE_ADMIN, Set.of(Role.ROLE_MANAGER, Role.ROLE_MODERATOR, Role.ROLE_USER),
                    Role.ROLE_MANAGER, Set.of(Role.ROLE_USER),
                    Role.ROLE_MODERATOR, Set.of(Role.ROLE_USER)
            );
        }

        public Set<Role> getReachableRoles(Role role) {
            Set<Role> reachable = new java.util.HashSet<>();
            reachable.add(role);
            Set<Role> directChildren = hierarchy.get(role);
            if (directChildren != null) {
                reachable.addAll(directChildren);
            }
            return reachable;
        }

        public boolean userHasRole(AuthorizedUser user, Role requiredRole) {
            return user.roles().stream()
                    .flatMap(role -> getReachableRoles(role).stream())
                    .anyMatch(reachable -> reachable == requiredRole);
        }
    }

    // PreAuthorize expression evaluator
    public static class PreAuthorizeEvaluator {
        public boolean evaluateHasRole(Authentication authentication, String role) {
            return authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals(role));
        }

        public boolean evaluateHasAnyRole(Authentication authentication, String... roles) {
            return Arrays.stream(roles)
                    .anyMatch(role -> evaluateHasRole(authentication, role));
        }

        public boolean evaluateIsAuthenticated(Authentication authentication) {
            return authentication != null && authentication.isAuthenticated();
        }

        public boolean evaluateAnd(Authentication authentication, String... expressions) {
            return Arrays.stream(expressions)
                    .allMatch(expr -> evaluateSingleExpression(authentication, expr));
        }

        private boolean evaluateSingleExpression(Authentication authentication, String expression) {
            if (expression.startsWith("hasRole(")) {
                String role = extractRoleFromExpression(expression);
                return evaluateHasRole(authentication, role);
            }
            if (expression.startsWith("hasAnyRole(")) {
                String[] roles = extractRolesFromExpression(expression);
                return evaluateHasAnyRole(authentication, roles);
            }
            if ("isAuthenticated()".equals(expression)) {
                return evaluateIsAuthenticated(authentication);
            }
            return false;
        }

        private String extractRoleFromExpression(String expression) {
            int start = expression.indexOf('(');
            int end = expression.indexOf(')');
            return expression.substring(start + 1, end);
        }

        private String[] extractRolesFromExpression(String expression) {
            int start = expression.indexOf('(');
            int end = expression.indexOf(')');
            String rolesStr = expression.substring(start + 1, end);
            return Arrays.stream(rolesStr.split(","))
                    .map(String::trim)
                    .toArray(String[]::new);
        }
    }

    // Service class demonstrating method-level security patterns
    public static class DocumentService {
        private final PreAuthorizeEvaluator evaluator = new PreAuthorizeEvaluator();

        @PreAuthorize("hasRole('ROLE_USER')")
        public String readDocument(String docId, Authentication authentication) {
            if (!evaluator.evaluateHasRole(authentication, "ROLE_USER")) {
                throw new AccessDeniedException("Access denied to document: " + docId);
            }
            return "Document content: " + docId;
        }

        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
        public void updateDocument(String docId, Authentication authentication) {
            if (!evaluator.evaluateHasAnyRole(authentication, "ROLE_ADMIN", "ROLE_MANAGER")) {
                throw new AccessDeniedException("Cannot update document: " + docId);
            }
            System.out.println("Document updated: " + docId);
        }

        @Secured("ROLE_ADMIN")
        public void deleteDocument(String docId, Authentication authentication) {
            if (!evaluator.evaluateHasRole(authentication, "ROLE_ADMIN")) {
                throw new AccessDeniedException("Cannot delete document: " + docId);
            }
            System.out.println("Document deleted: " + docId);
        }

        @PreAuthorize("isAuthenticated()")
        public String listDocuments(Authentication authentication) {
            if (!evaluator.evaluateIsAuthenticated(authentication)) {
                throw new InsufficientAuthenticationException("Authentication required");
            }
            return "Listing all accessible documents for: " + authentication.getName();
        }
    }

    // Access decision voter simulation
    public static class AccessDecisionVoter {
        public int vote(Authentication authentication, Object object,
                         ConfigAttribute... attributes) {
            if (authentication == null) {
                return ACCESS_DENIED;
            }

            for (ConfigAttribute attribute : attributes) {
                String requiredRole = attribute.getAttribute();
                if (requiredRole != null &&
                    authentication.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals(requiredRole))) {
                    return ACCESS_GRANTED;
                }
            }
            return ACCESS_DENIED;
        }

        public static final int ACCESS_GRANTED = 1;
        public static final int ACCESS_ABSTAIN = 0;
        public static final int ACCESS_DENIED = -1;
    }

    // Authorization manager simulation
    public static class AuthorizationManager {
        private final RoleHierarchy roleHierarchy = new RoleHierarchy();
        private final AccessDecisionVoter voter = new AccessDecisionVoter();

        public boolean isAuthorized(AuthorizedUser user, Role requiredRole) {
            return user.roles().stream()
                    .flatMap(role -> roleHierarchy.getReachableRoles(role).stream())
                    .anyMatch(reachable -> reachable == requiredRole);
        }

        public boolean isAuthorizedAny(AuthorizedUser user, Role... requiredRoles) {
            return Arrays.stream(requiredRoles)
                    .anyMatch(role -> isAuthorized(user, role));
        }

        public boolean isAuthorizedAll(AuthorizedUser user, Role... requiredRoles) {
            return Arrays.stream(requiredRoles)
                    .allMatch(role -> isAuthorized(user, role));
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Spring Security Authorization Examples ===\n");

        // Demo 1: Basic Role Checking
        System.out.println("--- Demo 1: Basic Role Checking ---");
        var admin = new AuthorizedUser("admin", Set.of(Role.ROLE_ADMIN, Role.ROLE_USER));
        var user = new AuthorizedUser("john", Set.of(Role.ROLE_USER));
        var manager = new AuthorizedUser("jane", Set.of(Role.ROLE_MANAGER, Role.ROLE_USER));

        System.out.println("Admin has ROLE_ADMIN: " + admin.hasRole(Role.ROLE_ADMIN));
        System.out.println("User has ROLE_ADMIN: " + user.hasRole(Role.ROLE_ADMIN));
        System.out.println("Manager has ROLE_MANAGER: " + manager.hasRole(Role.ROLE_MANAGER));
        System.out.println("Admin has any role: " + admin.hasAnyRole(Role.ROLE_ADMIN, Role.ROLE_MANAGER));

        // Demo 2: Access Decision Manager
        System.out.println("\n--- Demo 2: Access Decision Manager ---");
        var decisionManager = new AccessDecisionSimulator();
        var adminAuth = new org.springframework.security.authentication
                .UsernamePasswordAuthenticationToken(
                admin, null, admin.getAuthorities());
        var userAuth = new org.springframework.security.authentication
                .UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities());

        ConfigAttribute adminAttr = new SecurityConfig("ROLE_ADMIN");
        ConfigAttribute userAttr = new SecurityConfig("ROLE_USER");

        System.out.println("Admin accessing ADMIN resource: " +
                decisionManager.decide(adminAuth, adminAttr));
        System.out.println("User accessing ADMIN resource: " +
                decisionManager.decide(userAuth, adminAttr));
        System.out.println("User accessing USER resource: " +
                decisionManager.decide(userAuth, userAttr));

        // Demo 3: Role Hierarchy
        System.out.println("\n--- Demo 3: Role Hierarchy ---");
        var roleHierarchy = new RoleHierarchy();
        System.out.println("Admin reachable roles: " +
                roleHierarchy.getReachableRoles(Role.ROLE_ADMIN));
        System.out.println("Manager reachable roles: " +
                roleHierarchy.getReachableRoles(Role.ROLE_MANAGER));
        System.out.println("Admin has USER role (via hierarchy): " +
                roleHierarchy.userHasRole(admin, Role.ROLE_USER));
        System.out.println("Manager has USER role (via hierarchy): " +
                roleHierarchy.userHasRole(manager, Role.ROLE_USER));

        // Demo 4: PreAuthorize Expressions
        System.out.println("\n--- Demo 4: PreAuthorize Expressions ---");
        var evaluator = new PreAuthorizeEvaluator();
        System.out.println("Admin hasRole('ROLE_ADMIN'): " +
                evaluator.evaluateHasRole(adminAuth, "ROLE_ADMIN"));
        System.out.println("User hasRole('ROLE_ADMIN'): " +
                evaluator.evaluateHasRole(userAuth, "ROLE_ADMIN"));
        System.out.println("User hasAnyRole('ROLE_USER', 'ROLE_ADMIN'): " +
                evaluator.evaluateHasAnyRole(userAuth, "ROLE_USER", "ROLE_ADMIN"));
        System.out.println("Admin isAuthenticated: " +
                evaluator.evaluateIsAuthenticated(adminAuth));

        // Demo 5: Document Service with Method Security
        System.out.println("\n--- Demo 5: Document Service ---");
        var docService = new DocumentService();
        System.out.println(docService.readDocument("DOC-001", userAuth));
        System.out.println(docService.listDocuments(adminAuth));

        // Demo 6: Authorization Manager with Hierarchy
        System.out.println("\n--- Demo 6: Authorization Manager ---");
        var authManager = new AuthorizationManager();
        System.out.println("Admin authorized for ADMIN: " +
                authManager.isAuthorized(admin, Role.ROLE_ADMIN));
        System.out.println("Admin authorized for USER: " +
                authManager.isAuthorized(admin, Role.ROLE_USER));
        System.out.println("User authorized for ADMIN: " +
                authManager.isAuthorized(user, Role.ROLE_ADMIN));
        System.out.println("Manager authorized for ADMIN or MANAGER: " +
                authManager.isAuthorizedAny(manager, Role.ROLE_ADMIN, Role.ROLE_MANAGER));

        // Demo 7: Access Decision Voter
        System.out.println("\n--- Demo 7: Access Decision Voter ---");
        var voter = new AccessDecisionVoter();
        int adminVote = voter.vote(adminAuth, null, adminAttr);
        int userVote = voter.vote(userAuth, null, adminAttr);
        System.out.println("Admin vote for ADMIN resource: " + formatVote(adminVote));
        System.out.println("User vote for ADMIN resource: " + formatVote(userVote));

        System.out.println("\n=== All demos completed successfully ===");
    }

    private static String formatVote(int vote) {
        return switch (vote) {
            case AccessDecisionVoter.ACCESS_GRANTED -> "GRANTED";
            case AccessDecisionVoter.ACCESS_DENIED -> "DENIED";
            case AccessDecisionVoter.ACCESS_ABSTAIN -> "ABSTAIN";
            default -> "UNKNOWN";
        };
    }
}
