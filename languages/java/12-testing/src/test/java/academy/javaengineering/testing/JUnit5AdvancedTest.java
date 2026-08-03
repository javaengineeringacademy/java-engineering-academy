package academy.javaengineering.testing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JUnit5AdvancedTest {

    private final JUnit5AdvancedExample example = new JUnit5AdvancedExample();

    @Test
    void shouldGreetWithName() {
        assertEquals("Hello, Java!", example.greet("Java"));
    }

    @Test
    void shouldGreetStrangerWhenNull() {
        assertEquals("Hello, Stranger!", example.greet(null));
    }

    @Test
    void shouldGreetStrangerWhenBlank() {
        assertEquals("Hello, Stranger!", example.greet("  "));
    }

    @Test
    void shouldSortArray() {
        assertArrayEquals(new int[]{1, 2, 3}, example.sort(new int[]{3, 1, 2}));
    }

    @Test
    void shouldReturnNullForNullArray() {
        assertNull(example.sort(null));
    }

    @Test
    void shouldRepeatText() {
        assertEquals("HaHaHa", example.repeat("Ha", 3));
    }

    @Test
    void shouldReturnEmptyForNegativeTimes() {
        assertEquals("", example.repeat("Ha", -1));
    }
}
