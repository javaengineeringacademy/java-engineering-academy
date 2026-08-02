package academy.javaengineering.certification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Certification Guide Tests")
class CertificationTest {

    @Test
    @DisplayName("Should have Java certifications")
    void testCertifications() {
        var certs = CertificationGuide.getCertifications();
        assertFalse(certs.isEmpty());
        assertTrue(certs.stream().anyMatch(c -> c.name().contains("Java SE 17")));
    }

    @Test
    @DisplayName("Should have study tips")
    void testStudyTips() {
        var tips = ExamPreparation.getStudyTips();
        assertFalse(tips.isEmpty());
        assertTrue(tips.size() >= 5);
    }

    @Test
    @DisplayName("Should have topic weights")
    void testTopicWeights() {
        var weights = ExamPreparation.getTopicWeights();
        assertFalse(weights.isEmpty());
        assertTrue(weights.containsKey("Java Object-Oriented Approach"));
    }
}
