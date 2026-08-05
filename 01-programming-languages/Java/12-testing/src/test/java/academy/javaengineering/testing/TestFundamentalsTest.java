package academy.javaengineering.testing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class TestFundamentalsTest {

    private TestFundamentalsExample example;

    @BeforeEach
    void setUp() {
        example = new TestFundamentalsExample();
    }

    @Test
    void shouldAddTwoNumbers() {
        assertEquals(5, example.add(2, 3));
    }

    @Test
    void shouldDivideTwoNumbers() {
        assertEquals(5, example.divide(10, 2));
    }

    @Test
    void shouldThrowExceptionWhenDivideByZero() {
        assertThrows(ArithmeticException.class, () -> example.divide(10, 0));
    }

    @Test
    void shouldIdentifyEvenNumber() {
        assertTrue(example.isEven(4));
        assertFalse(example.isEven(3));
    }

    @Test
    void shouldFormatName() {
        assertEquals("John Doe", example.formatName("John", "Doe"));
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> example.formatName(null, "Doe"));
    }
}
