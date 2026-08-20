package academy.javaengineering.testing.bdd.practices;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercise 1: BDD Scenarios
 *
 * Tasks:
 * 1. Write BDD-style tests with Given-When-Then
 * 2. Test a string validator
 * 3. Use descriptive method names
 */
class Exercise1GherkinScenarios {

    static class EmailValidator {
        boolean isValid(String email) {
            return email != null && email.contains("@") && email.contains(".");
        }
    }

    @Test
    void shouldValidateEmail() {
        // TODO: Write Given-When-Then style test
        // Given a valid email validator
        // When checking a valid email
        // Then it should return true
    }

    @Test
    void shouldRejectInvalidEmail() {
        // TODO: Write Given-When-Then style test
    }
}
