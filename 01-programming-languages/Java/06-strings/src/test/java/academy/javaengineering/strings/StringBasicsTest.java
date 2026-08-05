package academy.javaengineering.strings;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StringBasicsTest {

    @Test
    void testStringCreation() {
        String literal = "Hello";
        String fromChars = new String(new char[]{'H', 'e', 'l', 'l', 'o'});
        assertEquals(literal, fromChars);
    }

    @Test
    void testStringImmutability() {
        String original = "Hello";
        String modified = original.concat(" World");
        assertEquals("Hello", original);
        assertEquals("Hello World", modified);
    }

    @Test
    void testStringPool() {
        String s1 = "Programming";
        String s2 = "Programming";
        String s3 = new String("Programming");
        assertSame(s1, s2);
        assertNotSame(s1, s3);
        assertEquals(s1, s3);
    }

    @Test
    void testStringComparison() {
        String a = "Hello";
        String b = "hello";
        String c = "Hello";
        assertFalse(a.equals(b));
        assertTrue(a.equalsIgnoreCase(b));
        assertTrue(a.equals(c));
        assertEquals(0, a.compareTo(c));
    }
}
