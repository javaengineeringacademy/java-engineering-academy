package academy.javaengineering.testing.junit5.solutions;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class Exercise3ParameterizedTestSolution {

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
        assertTrue(isValidEmail(email), email + " should be valid");
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
        assertEquals(expected, isPalindrome(input));
    }
}
