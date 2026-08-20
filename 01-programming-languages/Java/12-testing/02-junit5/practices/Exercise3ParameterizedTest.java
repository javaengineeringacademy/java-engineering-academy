package academy.javaengineering.testing.junit5.practices;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercise 3: Parameterized Tests
 *
 * Tasks:
 * 1. Write a string validator with @ParameterizedTest
 * 2. Use @ValueSource for single-argument tests
 * 3. Use @CsvSource for multi-argument tests
 * 4. Validate email, phone, and URL formats
 */
class Exercise3ParameterizedTest {

    boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    boolean isPalindrome(String input) {
        if (input == null) return false;
        String reversed = new StringBuilder(input).reverse().toString();
        return input.equalsIgnoreCase(reversed);
    }

    @ParameterizedTest
    @ValueSource(strings = {"test@example.com", "user@domain.org", "a@b.co"})
    @DisplayName("should accept valid emails")
    void shouldAcceptValidEmails(String email) {
        // Arrange, Act, Assert
    }

    @ParameterizedTest
    @CsvSource({
        "racecar, true",
        "hello, false",
        "Madam, true",
        "a, true"
    })
    @DisplayName("should detect palindromes correctly")
    void shouldDetectPalindromes(String input, boolean expected) {
        // Arrange, Act, Assert
    }
}
