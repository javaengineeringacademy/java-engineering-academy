package academy.javaengineering.strings;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StringMethodsTest {

    @Test
    void testLengthAndAccess() {
        String text = "Hello";
        assertEquals(5, text.length());
        assertEquals('H', text.charAt(0));
        assertEquals('o', text.charAt(4));
    }

    @Test
    void testSearchMethods() {
        String text = "Hello, World!";
        assertTrue(text.contains("World"));
        assertFalse(text.contains("Python"));
        assertTrue(text.startsWith("Hello"));
        assertTrue(text.endsWith("!"));
        assertEquals(7, text.indexOf("World"));
        assertEquals(-1, text.indexOf("Python"));
    }

    @Test
    void testSubstring() {
        String text = "Hello, World!";
        assertEquals("World!", text.substring(7));
        assertEquals("Hello", text.substring(0, 5));
        assertEquals("World", text.substring(7, 12));
    }

    @Test
    void testModification() {
        String text = "Hello, World!";
        assertEquals("Hello, Java!", text.replace("World", "Java"));
        assertEquals("HELLO, WORLD!", text.toUpperCase());
        assertEquals("hello, world!", text.toLowerCase());
        assertEquals("Hello", "  Hello  ".trim());
    }

    @Test
    void testSplitAndJoin() {
        String csv = "apple,banana,cherry";
        String[] expected = {"apple", "banana", "cherry"};
        assertArrayEquals(expected, csv.split(","));

        String joined = String.join("-", expected);
        assertEquals("apple-banana-cherry", joined);
    }

    @Test
    void testUtilityMethods() {
        assertEquals("42", String.valueOf(42));
        assertTrue("".isEmpty());
        assertFalse("Hello".isEmpty());
        assertTrue("".isBlank());
        assertTrue("   ".isBlank());
        assertFalse("Hello".isBlank());
    }
}
