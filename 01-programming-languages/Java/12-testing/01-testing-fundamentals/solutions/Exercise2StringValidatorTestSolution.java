package academy.javaengineering.testing.fundamentals.solutions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class StringValidatorSolution {
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

class Exercise2StringValidatorTestSolution {

    private StringValidatorSolution validator;

    @BeforeEach
    void setUp() {
        validator = new StringValidatorSolution();
    }

    @Test
    @DisplayName("isValidEmail should return true for valid email")
    void shouldReturnTrueForValidEmail() {
        assertTrue(validator.isValidEmail("user@example.com"));
        assertTrue(validator.isValidEmail("test.name@domain.co.uk"));
    }

    @Test
    @DisplayName("isValidEmail should throw for null input")
    void shouldThrowForNullEmail() {
        assertThrows(IllegalArgumentException.class,
            () -> validator.isValidEmail(null));
    }

    @Test
    @DisplayName("isValidEmail should return false for invalid emails")
    void shouldReturnFalseForInvalidEmails() {
        assertFalse(validator.isValidEmail(""));
        assertFalse(validator.isValidEmail("   "));
        assertFalse(validator.isValidEmail("noatsign"));
        assertFalse(validator.isValidEmail("no@domain"));
    }

    @Test
    @DisplayName("isNotBlank should handle empty and blank strings")
    void shouldHandleBlankStrings() {
        assertFalse(validator.isNotBlank(null));
        assertFalse(validator.isNotBlank(""));
        assertFalse(validator.isNotBlank("   "));
        assertTrue(validator.isNotBlank("hello"));
    }

    @Test
    @DisplayName("truncate should add ellipsis when string exceeds limit")
    void shouldTruncateLongString() {
        assertEquals("Hel...", validator.truncate("Hello World", 3));
        assertEquals("Hello...", validator.truncate("Hello World", 5));
    }

    @Test
    @DisplayName("truncate should not modify string within limit")
    void shouldNotTruncateShortString() {
        assertEquals("Hi", validator.truncate("Hi", 10));
        assertEquals("Hello", validator.truncate("Hello", 5));
    }

    @Test
    @DisplayName("reverse should return reversed string")
    void shouldReverseString() {
        assertEquals("olleH", validator.reverse("Hello"));
        assertEquals("", validator.reverse(""));
        assertEquals("a", validator.reverse("a"));
        assertEquals("ba", validator.reverse("ab"));
    }
}
