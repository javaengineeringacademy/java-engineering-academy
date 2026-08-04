package academy.javaengineering.fundamentals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link StringBasics}.
 */
class StringBasicsTest {

    @Test
    @DisplayName("String literal creation works correctly")
    void testStringCreation() {
        String s1 = "Hello";
        String s2 = "Hello";
        assertEquals(s1, s2);
        assertSame(s1, s2); // Same reference from pool
    }

    @Test
    @DisplayName("new String creates separate object")
    void testNewString() {
        String s1 = "Hello";
        String s2 = new String("Hello");
        assertEquals(s1, s2);
        assertNotSame(s1, s2); // Different objects
    }

    @Test
    @DisplayName("String from char array works correctly")
    void testCharArrayCreation() {
        char[] chars = {'J', 'a', 'v', 'a'};
        String s = new String(chars);
        assertEquals("Java", s);
    }

    @Test
    @DisplayName("String from byte array works correctly")
    void testByteArrayCreation() {
        byte[] bytes = {72, 101, 108, 108, 111};
        String s = new String(bytes);
        assertEquals("Hello", s);
    }

    @Test
    @DisplayName("Empty and blank strings have correct properties")
    void testEmptyAndBlank() {
        String empty = "";
        String blank = "   ";
        assertTrue(empty.isEmpty());
        assertFalse(blank.isEmpty());
        assertTrue(empty.isBlank());
        assertTrue(blank.isBlank());
        assertEquals(0, empty.length());
        assertEquals(3, blank.length());
    }

    @Test
    @DisplayName("String is immutable - operations return new String")
    void testImmutability() {
        String original = "Hello";
        String upper = original.toUpperCase();
        assertEquals("Hello", original); // Original unchanged
        assertEquals("HELLO", upper);
    }

    @Test
    @DisplayName("length returns correct value")
    void testLength() {
        assertEquals(0, "".length());
        assertEquals(1, "a".length());
        assertEquals(5, "Hello".length());
    }

    @Test
    @DisplayName("charAt returns correct character")
    void testCharAt() {
        String s = "Hello";
        assertEquals('H', s.charAt(0));
        assertEquals('e', s.charAt(1));
        assertEquals('o', s.charAt(4));
    }

    @Test
    @DisplayName("substring extracts correct portion")
    void testSubstring() {
        String s = "Hello, World!";
        assertEquals("World!", s.substring(7));
        assertEquals("Hello", s.substring(0, 5));
        assertEquals("llo", s.substring(2, 5));
    }

    @Test
    @DisplayName("indexOf finds correct position")
    void testIndexOf() {
        String s = "Hello, World!";
        assertEquals(4, s.indexOf('o'));
        assertEquals(7, s.indexOf("World"));
        assertEquals(-1, s.indexOf("xyz"));
        assertEquals(8, s.indexOf('o', 5)); // Search from index 5
    }

    @Test
    @DisplayName("lastIndexOf finds last occurrence")
    void testLastIndexOf() {
        String s = "Hello, World!";
        assertEquals(8, s.lastIndexOf('o'));
        assertEquals(-1, s.lastIndexOf("xyz"));
    }

    @Test
    @DisplayName("contains checks for substring presence")
    void testContains() {
        String s = "Hello, World!";
        assertTrue(s.contains("Hello"));
        assertTrue(s.contains("World"));
        assertFalse(s.contains("Java"));
        assertTrue(s.contains(""));
    }

    @Test
    @DisplayName("startsWith and endsWith work correctly")
    void testStartsEndsWith() {
        String s = "Hello, World!";
        assertTrue(s.startsWith("Hello"));
        assertTrue(s.endsWith("!"));
        assertFalse(s.startsWith("World"));
        assertFalse(s.endsWith("World"));
    }

    @Test
    @DisplayName("replace and replaceFirst work correctly")
    void testReplace() {
        String s = "Hello, World! Hello, Java!";
        assertEquals("Hi, World! Hi, Java!", s.replace("Hello", "Hi"));
        assertEquals("Hi, World! Hello, Java!", s.replaceFirst("Hello", "Hi"));
    }

    @Test
    @DisplayName("replaceAll with regex works correctly")
    void testReplaceAll() {
        String s = "abc123def456";
        assertEquals("abcdef", s.replaceAll("\\d", ""));
        assertEquals("a-b-c-1-2-3-d-e-f-4-5-6", s.replaceAll("([a-zA-Z0-9])", "$1-").replaceAll("-$", ""));
    }

    @Test
    @DisplayName("split divides string correctly")
    void testSplit() {
        String csv = "apple,banana,cherry";
        String[] parts = csv.split(",");
        assertArrayEquals(new String[]{"apple", "banana", "cherry"}, parts);
    }

    @Test
    @DisplayName("split with limit works correctly")
    void testSplitWithLimit() {
        String s = "one two three four";
        String[] parts = s.split(" ", 2);
        assertArrayEquals(new String[]{"one", "two three four"}, parts);
    }

    @Test
    @DisplayName("trim and strip remove whitespace")
    void testTrimStrip() {
        String padded = "  Hello  ";
        assertEquals("Hello", padded.trim());
        assertEquals("Hello", padded.strip());
        assertEquals("Hello  ", padded.stripLeading());
        assertEquals("  Hello", padded.stripTrailing());
    }

    @Test
    @DisplayName("toUpperCase and toLowerCase work correctly")
    void testCaseConversion() {
        assertEquals("HELLO", "hello".toUpperCase());
        assertEquals("hello", "HELLO".toLowerCase());
        assertEquals("HELLO", "Hello".toUpperCase());
    }

    @Test
    @DisplayName("equals and equalsIgnoreCase work correctly")
    void testEquals() {
        assertTrue("Hello".equals("Hello"));
        assertFalse("Hello".equals("hello"));
        assertTrue("Hello".equalsIgnoreCase("hello"));
        assertFalse("Hello".equals("World"));
    }

    @Test
    @DisplayName("compareTo returns correct values")
    void testCompareTo() {
        assertTrue("Apple".compareTo("Banana") < 0);
        assertTrue("Banana".compareTo("Apple") > 0);
        assertEquals(0, "Hello".compareTo("Hello"));
    }

    @Test
    @DisplayName("compareToIgnoreCase ignores case")
    void testCompareToIgnoreCase() {
        assertTrue("apple".compareToIgnoreCase("Banana") < 0);
        assertEquals(0, "Hello".compareToIgnoreCase("hello"));
    }

    @Test
    @DisplayName("toCharArray converts correctly")
    void testToCharArray() {
        char[] chars = "Hello".toCharArray();
        assertArrayEquals(new char[]{'H', 'e', 'l', 'l', 'o'}, chars);
    }

    @Test
    @DisplayName("String.valueOf converts correctly")
    void testValueOf() {
        assertEquals("42", String.valueOf(42));
        assertEquals("3.14", String.valueOf(3.14));
        assertEquals("true", String.valueOf(true));
        assertEquals("null", String.valueOf((Object) null));
    }

    @Test
    @DisplayName("StringBuilder basic operations work")
    void testStringBuilderBasic() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hello");
        sb.append(" ");
        sb.append("World");
        assertEquals("Hello World", sb.toString());
    }

    @Test
    @DisplayName("StringBuilder insert works correctly")
    void testStringBuilderInsert() {
        StringBuilder sb = new StringBuilder("HelloWorld");
        sb.insert(5, " ");
        assertEquals("Hello World", sb.toString());
    }

    @Test
    @DisplayName("StringBuilder delete works correctly")
    void testStringBuilderDelete() {
        StringBuilder sb = new StringBuilder("Hello, World!");
        sb.delete(5, 6);
        assertEquals("Hello World!", sb.toString());
    }

    @Test
    @DisplayName("StringBuilder reverse works correctly")
    void testStringBuilderReverse() {
        StringBuilder sb = new StringBuilder("Hello");
        assertEquals("olleH", sb.reverse().toString());
    }

    @Test
    @DisplayName("StringBuilder replace works correctly")
    void testStringBuilderReplace() {
        StringBuilder sb = new StringBuilder("Hello, World!");
        sb.replace(7, 12, "Java");
        assertEquals("Hello, Java!", sb.toString());
    }

    @Test
    @DisplayName("String.format formats correctly")
    void testStringFormat() {
        String result = String.format("Name: %s, Age: %d", "Alice", 30);
        assertEquals("Name: Alice, Age: 30", result);
    }

    @Test
    @DisplayName("String.format with floating point precision")
    void testStringFormatPrecision() {
        String result = String.format("Pi = %.2f", 3.14159);
        assertEquals("Pi = 3.14", result);
    }

    @Test
    @DisplayName("String.format with width and alignment")
    void testStringFormatWidth() {
        String right = String.format("[%10s]", "right");
        String left = String.format("[%-10s]", "left");
        assertEquals("[     right]", right);
        assertEquals("[left      ]", left);
    }

    @Test
    @DisplayName("Concatenation with + operator works")
    void testConcatenation() {
        String result = "Hello" + " " + "World";
        assertEquals("Hello World", result);
    }

    @Test
    @DisplayName("String repeat works correctly")
    void testRepeat() {
        assertEquals("aaa", "a".repeat(3));
        assertEquals("", "a".repeat(0));
        assertEquals("abab", "ab".repeat(2));
    }
}
