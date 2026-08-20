package academy.javaengineering.testing.unit.solutions;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class Exercise1PureFunctionsSolution {

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

    @Test
    void shouldCalculateFibonacci() {
        assertEquals(0, fibonacci(0));
        assertEquals(1, fibonacci(1));
        assertEquals(5, fibonacci(5));
        assertEquals(55, fibonacci(10));
    }

    @Test
    void shouldRejectNegativeFibonacci() {
        assertThrows(IllegalArgumentException.class, () -> fibonacci(-1));
    }

    @Test
    void shouldReverseString() {
        assertEquals("olleh", reverseString("hello"));
        assertEquals("", reverseString(""));
        assertNull(reverseString(null));
        assertEquals("a", reverseString("a"));
    }

    @Test
    void shouldDetectPalindromes() {
        assertTrue(isPalindrome("racecar"));
        assertTrue(isPalindrome("Madam"));
        assertFalse(isPalindrome("hello"));
        assertTrue(isPalindrome("A man a plan a canal Panama"));
        assertFalse(isPalindrome(null));
    }
}
