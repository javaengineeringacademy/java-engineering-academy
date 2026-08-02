package academy.javaengineering.security;

/**
 * Demonstrates authentication and authorization patterns.
 */
public class AuthPatterns {

    public record AuthToken(
        String token,
        String type,
        long expiresIn
    ) {}

    public record Permission(
        String resource,
        java.util.List<String> actions
    ) {}

    public static AuthToken createMockToken(String userId) {
        return new AuthToken("mock-jwt-" + userId, "Bearer", 3600);
    }

    public static boolean validateToken(String token) {
        return token != null && token.startsWith("mock-jwt-");
    }

    public static java.util.List<Permission> getDefaultPermissions() {
        return java.util.List.of(
            new Permission("users", java.util.List.of("read", "write")),
            new Permission("orders", java.util.List.of("read")),
            new Permission("reports", java.util.List.of("read", "write", "delete"))
        );
    }
}
