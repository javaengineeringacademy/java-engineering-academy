package academy.javaengineering.projects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Projects Tests")
class ProjectsTest {

    @Test
    @DisplayName("Microservice project should have required modules")
    void testMicroserviceProject() {
        var project = ProjectArchitecture.createMicroserviceProject("myapp");
        
        assertEquals("myapp", project.name());
        assertTrue(project.modules().contains("api-gateway"));
        assertTrue(project.modules().contains("user-service"));
    }

    @Test
    @DisplayName("Coding standards should not be empty")
    void testCodingStandards() {
        var standards = ProjectBestPractices.getCodingStandards();
        assertFalse(standards.isEmpty());
        assertTrue(standards.size() >= 5);
    }

    @Test
    @DisplayName("Testing strategies should include unit tests")
    void testTestingStrategies() {
        var strategies = ProjectBestPractices.getTestingStrategies();
        assertTrue(strategies.stream().anyMatch(s -> s.contains("unit tests")));
    }
}
