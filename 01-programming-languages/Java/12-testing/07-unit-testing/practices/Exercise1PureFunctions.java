package academy.javaengineering.testing.unit.practices;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercise 1: Testing Pure Functions
 *
 * Tasks:
 * 1. Test string utility methods
 * 2. Test mathematical functions
 * 3. Test validation logic
 * 4. Cover edge cases thoroughly
 */
class Exercise1PureFunctions {

    static int fibonacci(int n) {
        if (n < 0) throw new IllegalArgumentException("Negative input");
        if (n <= 1) return n;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    static String reverseString(String input) {
        if (input == null) return null;
        return new StringBuilder(input).reverse().toString();
    }

    static boolean isPalindrome(String input) {
        if (input == null) return false;
        String clean = input.toLowerCase().replaceAll("[^a-z0-9]", "");
        return clean.equals(reverseString(clean));
    }

    // TODO: Write tests for fibonacci
    @Test
    @DisplayName("fibonacci should return correct values")
    void shouldCalculateFibonacci() {
        // Arrange, Act, Assert
    }

    // TODO: Write tests for reverseString
    @Test
    @DisplayName("reverseString should reverse input")
    void shouldReverseString() {
        // Arrange, Act, Assert
    }

    // TODO: Write tests for isPalindrome
    @Test
    @DisplayName("isPalindrome should detect palindromes")
    void shouldDetectPalindromes() {
        // Arrange, Act, Assert
    }
}
