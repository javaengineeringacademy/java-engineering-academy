package academy.javaengineering.openapi;

import java.util.Map;

/**
 * Demonstrates API documentation best practices.
 */
public class ApiDocumentation {

    public static Map<String, String> getBestPractices() {
        return Map.of(
            "Naming", "Use nouns for resources, HTTP verbs for actions",
            "Versioning", "Include version in URL path",
            "Pagination", "Use page/size or cursor-based pagination",
            "Error Handling", "Return consistent error responses",
            "Authentication", "Use OAuth2 or JWT tokens"
        );
    }

    public record ErrorResponse(
        int status,
        String error,
        String message,
        String path
    ) {}

    public static ErrorResponse createErrorResponse(int status, String message, String path) {
        return new ErrorResponse(status, "error", message, path);
    }
}
