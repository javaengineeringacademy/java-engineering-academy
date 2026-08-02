package academy.javaengineering.bestpractices;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Best Practices Tests")
class BestPracticesTest {

    @Test
    @DisplayName("Should have core best practices")
    void testBestPractices() {
        var practices = JavaBestPractices.getBestPractices();
        assertFalse(practices.isEmpty());
        assertTrue(practices.size() >= 8);
    }

    @Test
    @DisplayName("Code review checklist should have categories")
    void testCodeReviewChecklist() {
        var checklist = JavaBestPractices.getCodeReviewChecklist();
        assertFalse(checklist.isEmpty());
        assertTrue(checklist.stream().anyMatch(c -> c.category().equals("Code Quality")));
    }

    @Test
    @DisplayName("Design principles should include SOLID")
    void testDesignPrinciples() {
        var principles = DesignBestPractices.getDesignPrinciples();
        assertTrue(principles.stream().anyMatch(p -> p.contains("Single Responsibility")));
        assertTrue(principles.stream().anyMatch(p -> p.contains("DRY")));
    }
}
