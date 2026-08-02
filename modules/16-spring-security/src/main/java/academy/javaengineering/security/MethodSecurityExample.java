package academy.javaengineering.security;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MethodSecurityExample {

    private final Map<String, Set<String>> userRoles = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Boolean>> permissions = new ConcurrentHashMap<>();

    public void assignRole(String username, String role) {
        userRoles.computeIfAbsent(username, k -> new HashSet<>()).add(role);
        System.out.println("Assigned role " + role + " to user: " + username);
    }

    public boolean hasRole(String username, String role) {
        Set<String> roles = userRoles.getOrDefault(username, Collections.emptySet());
        return roles.contains("ROLE_" + role) || roles.contains(role);
    }

    public boolean hasAnyRole(String username, String... roles) {
        for (String role : roles) {
            if (hasRole(username, role)) {
                return true;
            }
        }
        return false;
    }

    public void grantPermission(String username, String resource, String permission) {
        permissions.computeIfAbsent(username, k -> new ConcurrentHashMap<>())
                .put(resource + ":" + permission, true);
    }

    public boolean hasPermission(String username, String resource, String permission) {
        Map<String, Boolean> userPerms = permissions.getOrDefault(username, Collections.emptyMap());
        return userPerms.getOrDefault(resource + ":" + permission, false);
    }

    @PreAuthorizeAnnotation("hasRole('ADMIN')")
    public String adminOnlyOperation() {
        return "Admin operation executed";
    }

    @PreAuthorizeAnnotation("hasRole('USER') or hasRole('ADMIN')")
    public String userOperation() {
        return "User operation executed";
    }

    @SecuredAnnotation({"ROLE_USER", "ROLE_VIEWER"})
    public String securedOperation() {
        return "Secured operation executed";
    }

    public String checkAuthorization(String username, String operation) {
        System.out.println("Checking authorization for user: " + username + ", operation: " + operation);

        boolean authorized = false;
        String reason = "";

        switch (operation) {
            case "ADMIN_ONLY":
                authorized = hasRole(username, "ADMIN");
                reason = authorized ? "User has ADMIN role" : "User lacks ADMIN role";
                break;
            case "USER_OPERATION":
                authorized = hasAnyRole(username, "USER", "ADMIN");
                reason = authorized ? "User has USER or ADMIN role" : "User lacks required roles";
                break;
            case "VIEW_CONTENT":
                authorized = hasAnyRole(username, "USER", "VIEWER", "ADMIN");
                reason = authorized ? "User has access" : "User lacks access";
                break;
            case "EDIT_CONTENT":
                authorized = hasPermission(username, "content", "write");
                reason = authorized ? "User has write permission" : "User lacks write permission";
                break;
            default:
                reason = "Unknown operation";
        }

        System.out.println("Authorization result: " + reason);
        return reason;
    }

    public Map<String, Object> getUserSecurityProfile(String username) {
        Map<String, Object> profile = new HashMap<>();
        profile.put("username", username);
        profile.put("roles", userRoles.getOrDefault(username, Collections.emptySet()));
        profile.put("permissions", permissions.getOrDefault(username, Collections.emptyMap()));
        return profile;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface PreAuthorizeAnnotation {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface SecuredAnnotation {
        String[] value();
    }

    public static class SecurityInterceptor {

        public boolean invoke(Method method, Object[] args, String currentUser,
                              MethodSecurityExample securityExample) {
            System.out.println("Security interceptor checking method: " + method.getName());

            PreAuthorizeAnnotation preAuthorize = method.getAnnotation(PreAuthorizeAnnotation.class);
            if (preAuthorize != null) {
                String expression = preAuthorize.value();
                return evaluateExpression(currentUser, expression, securityExample);
            }

            SecuredAnnotation secured = method.getAnnotation(SecuredAnnotation.class);
            if (secured != null) {
                for (String role : secured.value()) {
                    if (securityExample.hasRole(currentUser, role)) {
                        return true;
                    }
                }
                return false;
            }

            return true;
        }

        private boolean evaluateExpression(String username, String expression,
                                           MethodSecurityExample securityExample) {
            if (expression.contains(" or ")) {
                String[] parts = expression.split(" or ");
                for (String part : parts) {
                    if (evaluateSingleExpression(username, part.trim(), securityExample)) {
                        return true;
                    }
                }
                return false;
            }

            if (expression.contains(" and ")) {
                String[] parts = expression.split(" and ");
                for (String part : parts) {
                    if (!evaluateSingleExpression(username, part.trim(), securityExample)) {
                        return false;
                    }
                }
                return true;
            }

            return evaluateSingleExpression(username, expression, securityExample);
        }

        private boolean evaluateSingleExpression(String username, String expression,
                                                 MethodSecurityExample securityExample) {
            if (expression.startsWith("hasRole('")) {
                String role = expression.replace("hasRole('", "").replace("')", "");
                return securityExample.hasRole(username, role);
            }
            if (expression.startsWith("hasAnyRole('")) {
                String rolesStr = expression.replace("hasAnyRole('", "").replace("')", "");
                String[] roles = rolesStr.split("', '");
                return securityExample.hasAnyRole(username, roles);
            }
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        MethodSecurityExample example = new MethodSecurityExample();
        SecurityInterceptor interceptor = new SecurityInterceptor();

        System.out.println("=== Method Security Demo ===\n");

        example.assignRole("admin", "ROLE_ADMIN");
        example.assignRole("admin", "ROLE_USER");
        example.assignRole("user", "ROLE_USER");
        example.assignRole("viewer", "ROLE_VIEWER");

        example.grantPermission("admin", "content", "write");
        example.grantPermission("user", "content", "read");

        System.out.println("\n--- Testing Admin ---");
        Method adminMethod = MethodSecurityExample.class.getMethod("adminOnlyOperation");
        boolean adminAccess = interceptor.invoke(adminMethod, null, "admin", example);
        System.out.println("Admin access to adminOnlyOperation: " + adminAccess);

        System.out.println("\n--- Testing User ---");
        boolean userAccess = interceptor.invoke(adminMethod, null, "user", example);
        System.out.println("User access to adminOnlyOperation: " + userAccess);

        System.out.println("\n--- Testing User Operation ---");
        Method userMethod = MethodSecurityExample.class.getMethod("userOperation");
        boolean userOpAccess = interceptor.invoke(userMethod, null, "user", example);
        System.out.println("User access to userOperation: " + userOpAccess);

        System.out.println("\n--- Security Profiles ---");
        System.out.println("Admin profile: " + example.getUserSecurityProfile("admin"));
        System.out.println("User profile: " + example.getUserSecurityProfile("user"));
        System.out.println("Viewer profile: " + example.getUserSecurityProfile("viewer"));
    }
}
