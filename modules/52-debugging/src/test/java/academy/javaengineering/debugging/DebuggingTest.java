package academy.javaengineering.debugging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Debugging Tests")
class DebuggingTest {

    @Test
    @DisplayName("Binary search should find element")
    void testBinarySearch() {
        var tech = new DebuggingTechniques();
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        
        assertEquals(4, tech.binarySearch(array, 5));
        assertEquals(-1, tech.binarySearch(array, 11));
    }

    @Test
    @DisplayName("Reverse string should work correctly")
    void testReverseString() {
        var tech = new DebuggingTechniques();
        
        assertEquals("olleh", tech.reverseString("hello"));
        assertEquals("a", tech.reverseString("a"));
        assertEquals("", tech.reverseString(""));
    }

    @Test
    @DisplayName("Is palindrome should detect palindromes")
    void testIsPalindrome() {
        var tech = new DebuggingTechniques();
        
        assertTrue(tech.isPalindrome("racecar"));
        assertTrue(tech.isPalindrome("A man a plan a canal Panama"));
        assertFalse(tech.isPalindrome("hello"));
    }
}
