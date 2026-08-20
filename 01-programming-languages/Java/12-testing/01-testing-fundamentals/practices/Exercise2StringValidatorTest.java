package academy.javaengineering.testing.fundamentals.practices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercise 2: Write tests for StringValidator covering edge cases.
 *
 * Tasks:
 * 1. Test isValidEmail() with valid emails
 * 2. Test isValidEmail() with invalid emails (null, empty, no @)
 * 3. Test isNotBlank() with various inputs
 * 4. Test truncate() with strings longer and shorter than limit
 * 5. Test reverse() with empty string, single char, normal string
 * 6. Test that null inputs throw IllegalArgumentException
 */
class StringValidator {

    boolean isValidEmail(String email) {
        if (email == null) throw new IllegalArgumentException("Email cannot be null");
        return email.contains("@") && email.contains(".") && !email.isBlank();
    }

    boolean isNotBlank(String input) {
        return input != null && !input.isBlank();
    }

    String truncate(String input, int maxLength) {
        if (input == null) throw new IllegalArgumentException("Input cannot be null");
        if (maxLength < 0) throw new IllegalArgumentException("Max length cannot be negative");
        return input.length() <= maxLength ? input : input.substring(0, maxLength) + "...";
    }

    String reverse(String input) {
        if (input == null) throw new IllegalArgumentException("Input cannot be null");
        return new StringBuilder(input).reverse().toString();
    }
}

class Exercise2StringValidatorTest {

    private StringValidator validator;

    @BeforeEach
    void setUp() {
        validator = new StringValidator();
    }

    // TODO: Test isValidEmail with valid email
    @Test
    @DisplayName("isValidEmail should return true for valid email")
    void shouldReturnTrueForValidEmail() {
        // Arrange, Act, Assert
    }

    // TODO: Test isValidEmail with null
    @Test
    @DisplayName("isValidEmail should throw for null input")
    void shouldThrowForNullEmail() {
        // Arrange, Act, Assert
    }

    // TODO: Test isValidEmail with invalid emails
    @Test
    @DisplayName("isValidEmail should return false for invalid emails")
    void shouldReturnFalseForInvalidEmails() {
        // Arrange, Act, Assert
    }

    // TODO: Test isNotBlank
    @Test
    @DisplayName("isNotBlank should handle empty and blank strings")
    void shouldHandleBlankStrings() {
        // Arrange, Act, Assert
    }

    // TODO: Test truncate with long string
    @Test
    @DisplayName("truncate should add ellipsis when string exceeds limit")
    void shouldTruncateLongString() {
        // Arrange, Act, Assert
    }

    // TODO: Test truncate with short string
    @Test
    @DisplayName("truncate should not modify string within limit")
    void shouldNotTruncateShortString() {
        // Arrange, Act, Assert
    }

    // TODO: Test reverse
    @Test
    @DisplayName("reverse should return reversed string")
    void shouldReverseString() {
        // Arrange, Act, Assert
    }
}
