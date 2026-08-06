package academy.javaengineering.strings;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StringBuilderTest {

    @Test
    void testBasicAppend() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hello");
        sb.append(" ");
        sb.append("World");
        assertEquals("Hello World", sb.toString());
    }

    @Test
    void testInsert() {
        StringBuilder sb = new StringBuilder("Hello World");
        sb.insert(5, ",");
        assertEquals("Hello, World", sb.toString());
    }

    @Test
    void testDelete() {
        StringBuilder sb = new StringBuilder("Hello World");
        sb.delete(5, 11);
        assertEquals("Hello", sb.toString());
    }

    @Test
    void testReverse() {
        StringBuilder sb = new StringBuilder("Hello");
        assertEquals("olleH", sb.reverse().toString());
    }

    @Test
    void testReplace() {
        StringBuilder sb = new StringBuilder("Hello World");
        sb.replace(6, 11, "Java");
        assertEquals("Hello Java", sb.toString());
    }

    @Test
    void testCapacity() {
        StringBuilder sb = new StringBuilder(100);
        assertEquals(100, sb.capacity());
        assertEquals(0, sb.length());
    }

    @Test
    void testChaining() {
        String result = new StringBuilder()
                .append("Java")
                .append(" ")
                .append("Programming")
                .toString();
        assertEquals("Java Programming", result);
    }
}
