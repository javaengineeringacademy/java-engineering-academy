package academy.javaengineering.strings;

import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class StringPatternsTest {

    @Test
    void testPalindrome() {
        assertTrue(StringPatterns.isPalindrome("racecar"));
        assertTrue(StringPatterns.isPalindrome("A man a plan a canal Panama"));
        assertFalse(StringPatterns.isPalindrome("hello"));
        assertFalse(StringPatterns.isPalindrome(null));
    }

    @Test
    void testAnagram() {
        assertTrue(StringPatterns.isAnagram("listen", "silent"));
        assertTrue(StringPatterns.isAnagram("triangle", "integral"));
        assertFalse(StringPatterns.isAnagram("hello", "world"));
        assertFalse(StringPatterns.isAnagram(null, "test"));
    }

    @Test
    void testAnagramCounting() {
        assertTrue(StringPatterns.isAnagramCounting("listen", "silent"));
        assertTrue(StringPatterns.isAnagramCounting("aabb", "bbaa"));
        assertFalse(StringPatterns.isAnagramCounting("hello", "world"));
        assertFalse(StringPatterns.isAnagramCounting("abc", "abcd"));
    }

    @Test
    void testReverse() {
        assertEquals("olleH", StringPatterns.reverseString("Hello"));
        assertEquals("avaJ", StringPatterns.reverseString("Java"));
        assertEquals("", StringPatterns.reverseString(""));
        assertNull(StringPatterns.reverseString(null));
    }

    @Test
    void testReverseCharArray() {
        assertEquals("olleH", StringPatterns.reverseCharArray("Hello"));
        assertEquals("avaJ", StringPatterns.reverseCharArray("Java"));
        assertEquals("", StringPatterns.reverseCharArray(""));
    }

    @Test
    void testDuplicateChars() {
        Set<Character> duplicates = StringPatterns.findDuplicateChars("programming");
        assertTrue(duplicates.contains('r'));
        assertTrue(duplicates.contains('g'));
        assertFalse(duplicates.contains('p'));
    }

    @Test
    void testCharacterFrequency() {
        Map<Character, Integer> freq = StringPatterns.characterFrequency("aabbbccc");
        assertEquals(2, freq.get('a'));
        assertEquals(3, freq.get('b'));
        assertEquals(3, freq.get('c'));
    }
}
