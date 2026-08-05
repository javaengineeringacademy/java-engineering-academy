package academy.javaengineering.exceptionhandling;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FinallyTest {

    @Test
    void testFinallyExecutesAfterException() {
        boolean finallyExecuted = false;
        try {
            int result = 10 / 0;
        } catch (ArithmeticException e) {
            // Expected
        } finally {
            finallyExecuted = true;
        }
        assertTrue(finallyExecuted);
    }

    @Test
    void testFinallyExecutesWithoutException() {
        boolean finallyExecuted = false;
        try {
            int result = 10 / 2;
            assertEquals(5, result);
        } finally {
            finallyExecuted = true;
        }
        assertTrue(finallyExecuted);
    }

    @Test
    void testFinallyRunsBeforeReturn() {
        int result = finallyRunsBeforeReturn();
        assertEquals(1, result);
    }

    private int finallyRunsBeforeReturn() {
        try {
            return 1;
        } finally {
            // Finally executes even with return
        }
    }

    @Test
    void testNestedFinallyBlocks() {
        boolean outerFinally = false;
        boolean innerFinally = false;
        try {
            try {
                throw new RuntimeException("Inner");
            } catch (RuntimeException e) {
                // Inner catch
            } finally {
                innerFinally = true;
            }
        } finally {
            outerFinally = true;
        }
        assertTrue(innerFinally);
        assertTrue(outerFinally);
    }
}
