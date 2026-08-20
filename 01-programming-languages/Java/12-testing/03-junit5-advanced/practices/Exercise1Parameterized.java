package academy.javaengineering.testing.junit5.advanced.practices;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercise 1: Parameterized Tests
 *
 * Tasks:
 * 1. Write parameterized tests for a string validator
 * 2. Use @ValueSource for single-argument validation
 * 3. Use @CsvSource for multi-argument validation
 * 4. Test password strength checker
 */
class Exercise1Parameterized {

    boolean isStrongPassword(String password) {
        if (password == null || password.length() < 8) return false;
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        return hasUpper && hasLower && hasDigit;
    }

    @ParameterizedTest
    @ValueSource(strings = {"Password1", "Abc12345", "Test1234"})
    void shouldAcceptStrongPasswords(String password) {
        // TODO: Assert password is strong
    }

    @ParameterizedTest
    @CsvSource({
        "abc, false",
        "ABC123, false",
        "password, false",
        "Pass1234, true",
        "Str0ngPass, true"
    })
    void shouldValidatePasswordStrength(String password, boolean expected) {
        // TODO: Assert password strength matches expected
    }
}
