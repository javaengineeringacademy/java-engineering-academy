package academy.javaengineering.openapi;

import java.util.List;

/**
 * Demonstrates OpenAPI/Swagger examples.
 */
public class OpenApiExamples {

    public record ApiEndpoint(
        String method,
        String path,
        String summary,
        List<String> tags
    ) {}

    public static List<ApiEndpoint> getExampleEndpoints() {
        return List.of(
            new ApiEndpoint("GET", "/api/users", "Get all users", List.of("Users")),
            new ApiEndpoint("POST", "/api/users", "Create user", List.of("Users")),
            new ApiEndpoint("GET", "/api/users/{id}", "Get user by ID", List.of("Users")),
            new ApiEndpoint("PUT", "/api/users/{id}", "Update user", List.of("Users")),
            new ApiEndpoint("DELETE", "/api/users/{id}", "Delete user", List.of("Users")),
            new ApiEndpoint("POST", "/api/auth/login", "User login", List.of("Auth"))
        );
    }

    public static String generateOpenApiSpec() {
        return """
            openapi: 3.0.0
            info:
              title: Java Engineering Academy API
              version: 1.0.0
            paths:
              /api/users:
                get:
                  summary: Get all users
                  tags:
                    - Users
                post:
                  summary: Create user
                  tags:
                    - Users
            """;
    }
}
