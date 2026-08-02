package academy.javaengineering.clouddesign;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cloud Design Tests")
class CloudDesignTest {

    @Test
    @DisplayName("Should have cloud design patterns")
    void testPatterns() {
        var patterns = CloudPatterns.getPatterns();
        assertFalse(patterns.isEmpty());
        assertTrue(patterns.stream().anyMatch(p -> p.name().equals("Circuit Breaker")));
    }

    @Test
    @DisplayName("Should have architectural components")
    void testComponents() {
        var components = CloudArchitecture.getComponents();
        assertFalse(components.isEmpty());
        assertTrue(components.stream().anyMatch(c -> c.name().equals("API Gateway")));
    }

    @Test
    @DisplayName("Should have best practices")
    void testBestPractices() {
        var practices = CloudArchitecture.getBestPractices();
        assertFalse(practices.isEmpty());
        assertTrue(practices.containsKey("Design for failure"));
    }
}
