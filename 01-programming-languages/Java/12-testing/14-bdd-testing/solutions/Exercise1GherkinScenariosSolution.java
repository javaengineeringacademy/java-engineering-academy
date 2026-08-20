package academy.javaengineering.testing.bdd.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Exercise1GherkinScenariosSolution {

    static class EmailValidator {
        boolean isValid(String email) {
            return email != null && email.contains("@") && email.contains(".");
        }
    }

    @Test
    void shouldValidateEmail() {
        // Given
        EmailValidator validator = new EmailValidator();
        // When
        boolean result = validator.isValid("user@example.com");
        // Then
        assertTrue(result);
    }

    @Test
    void shouldRejectInvalidEmail() {
        // Given
        EmailValidator validator = new EmailValidator();
        // When
        boolean result = validator.isValid("invalid");
        // Then
        assertFalse(result);
    }
}
