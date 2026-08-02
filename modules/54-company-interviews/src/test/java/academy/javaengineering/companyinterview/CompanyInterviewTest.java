package academy.javaengineering.companyinterview;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Company Interview Tests")
class CompanyInterviewTest {

    @Test
    @DisplayName("Should have questions from multiple companies")
    void testQuestions() {
        var questions = CompanyInterviewQuestions.getQuestions();
        assertFalse(questions.isEmpty());
        assertTrue(questions.stream().anyMatch(q -> q.company().equals("Google")));
        assertTrue(questions.stream().anyMatch(q -> q.company().equals("Amazon")));
    }

    @Test
    @DisplayName("STAR response should be created correctly")
    void testSTARResponse() {
        var response = BehavioralInterview.createSTARResponse(
            "Situation", "Task", "Action", "Result"
        );
        
        assertEquals("Situation", response.situation());
        assertEquals("Task", response.task());
        assertEquals("Action", response.action());
        assertEquals("Result", response.result());
    }

    @Test
    @DisplayName("Should have common behavioral questions")
    void testCommonQuestions() {
        var questions = BehavioralInterview.getCommonQuestions();
        assertFalse(questions.isEmpty());
        assertTrue(questions.contains("Tell me about yourself"));
    }
}
