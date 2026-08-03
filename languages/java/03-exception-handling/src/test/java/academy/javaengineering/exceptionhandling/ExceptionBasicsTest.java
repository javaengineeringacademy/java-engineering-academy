package academy.javaengineering.exceptionhandling;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for basic exception handling examples.
 */
class ExceptionBasicsTest {

    @Test
    void testBasicTryCatch() {
        // Test that ArithmeticException is caught
        assertThrows(ArithmeticException.class, () -> {
            int result = 10 / 0;
        });
    }

    @Test
    void testMultipleCatchBlocks() {
        // Test NullPointerException is caught first
        String nullString = null;
        assertThrows(NullPointerException.class, () -> nullString.length());
    }

    @Test
    void testFinallyBlockExecutes() {
        boolean finallyExecuted = false;
        try {
            int[] arr = {1, 2, 3};
            int val = arr[5]; // This will throw
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected
        } finally {
            finallyExecuted = true;
        }
        assertTrue(finallyExecuted, "Finally block should always execute");
    }

    @Test
    void testNestedTryCatch() {
        String outerMessage = null;

        try {
            try {
                throw new RuntimeException("Inner exception");
            } catch (RuntimeException e) {
                throw new RuntimeException("Outer exception", e);
            }
        } catch (RuntimeException e) {
            outerMessage = e.getMessage();
            assertNotNull(e.getCause());
            assertEquals("Inner exception", e.getCause().getMessage());
        }

        assertEquals("Outer exception", outerMessage);
    }

    @Test
    void testExceptionMessage() {
        try {
            throw new IllegalArgumentException("Invalid value");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid value", e.getMessage());
        }
    }

    @Test
    void testExceptionStackTrace() {
        try {
            throw new RuntimeException("Test exception");
        } catch (RuntimeException e) {
            StackTraceElement[] stackTrace = e.getStackTrace();
            assertTrue(stackTrace.length > 0, "Stack trace should not be empty");
        }
    }
}
