package academy.javaengineering.exceptionhandling;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionBasicsTest {

    @Test
    void testBasicExceptionDemo() {
        assertThrows(ArithmeticException.class, () -> {
            int result = 10 / 0;
        });
    }

    @Test
    void testNullPointerException() {
        String nullString = null;
        assertThrows(NullPointerException.class, nullString::length);
    }

    @Test
    void testArrayIndexOutOfBoundsException() {
        int[] numbers = {1, 2, 3};
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            System.out.println(numbers[5]);
        });
    }

    @Test
    void testNumberFormatException() {
        assertThrows(NumberFormatException.class, () -> {
            Integer.parseInt("abc");
        });
    }

    @Test
    void testExceptionHasMessage() {
        try {
            throw new IllegalArgumentException("Invalid input");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input", e.getMessage());
        }
    }

    @Test
    void testExceptionHasStackTrace() {
        try {
            throw new RuntimeException("Test");
        } catch (RuntimeException e) {
            assertTrue(e.getStackTrace().length > 0);
        }
    }
}
