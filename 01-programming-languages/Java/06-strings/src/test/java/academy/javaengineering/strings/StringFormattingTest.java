package academy.javaengineering.strings;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StringFormattingTest {

    @Test
    void testBasicFormatting() {
        String name = "John";
        int age = 30;
        String result = String.format("Name: %s, Age: %d", name, age);
        assertEquals("Name: John, Age: 30", result);
    }

    @Test
    void testNumericFormatting() {
        assertEquals("0042", String.format("%04d", 42));
        assertEquals("42    ", String.format("%-6d", 42));
        assertEquals("1,234,567", String.format("%,d", 1234567));
    }

    @Test
    void testFloatFormatting() {
        assertEquals("3.14", String.format("%.2f", 3.14159));
        assertEquals("03.14", String.format("%05.2f", 3.14159));
    }

    @Test
    void testStringFormatting() {
        assertEquals("Hello World", String.format("%s %s", "Hello", "World"));
        assertEquals("Hello     ", String.format("%-10s", "Hello"));
        assertEquals("     Hello", String.format("%10s", "Hello"));
    }

    @Test
    void testTextBlock() {
        String expected = "Line 1\nLine 2\nLine 3\n";
        String actual = """
                Line 1
                Line 2
                Line 3
                """;
        assertEquals(expected, actual);
    }

    @Test
    void testMultipleArguments() {
        String result = String.format("Name: %s, Age: %d, Salary: %.2f",
                "John", 30, 75000.50);
        assertTrue(result.contains("John"));
        assertTrue(result.contains("30"));
        assertTrue(result.contains("75000.50"));
    }
}
