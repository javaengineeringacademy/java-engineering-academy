package academy.javaengineering.security;

/**
 * Demonstrates security best practices.
 */
public class SecurityBestPractices {

    public static java.util.List<String> getSecurityPrinciples() {
        return java.util.List.of(
            "Defense in depth",
            "Least privilege",
            "Separation of duties",
            "Fail securely",
            "Keep security simple"
        );
    }

    public static java.util.Map<String, String> getVulnerabilityPrevention() {
        return java.util.Map.of(
            "SQL Injection", "Use parameterized queries",
            "XSS", "Escape output, use CSP headers",
            "CSRF", "Use CSRF tokens",
            "Authentication", "Use OAuth2/JWT",
            "Authorization", "Implement RBAC"
        );
    }
}
