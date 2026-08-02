package academy.javaengineering.openapi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OpenAPI Tests")
class OpenApiTest {

    @Test
    @DisplayName("Should generate OpenAPI spec")
    void testGenerateSpec() {
        String spec = OpenApiExamples.generateOpenApiSpec();
        assertNotNull(spec);
        assertTrue(spec.contains("openapi: 3.0.0"));
        assertTrue(spec.contains("/api/users"));
    }

    @Test
    @DisplayName("Should have example endpoints")
    void testExampleEndpoints() {
        var endpoints = OpenApiExamples.getExampleEndpoints();
        assertFalse(endpoints.isEmpty());
        assertTrue(endpoints.stream().anyMatch(e -> e.path().equals("/api/users")));
    }

    @Test
    @DisplayName("Should have documentation best practices")
    void testBestPractices() {
        var practices = ApiDocumentation.getBestPractices();
        assertFalse(practices.isEmpty());
        assertTrue(practices.containsKey("Versioning"));
    }
}
