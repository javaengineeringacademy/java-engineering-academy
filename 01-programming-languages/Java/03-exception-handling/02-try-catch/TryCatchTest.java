import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TryCatchTest {

    @Test
    void testSingleCatchBlock() {
        String text = "Hello";
        assertThrows(StringIndexOutOfBoundsException.class, () -> text.charAt(10));
    }

    @Test
    void testMultipleCatchBlocks() {
        String input = "abc";
        assertThrows(NumberFormatException.class, () -> Integer.parseInt(input));
    }

    @Test
    void testMultiCatchWithOrOperator() {
        try {
            throw new IllegalArgumentException("Null data");
        } catch (IllegalArgumentException | NullPointerException e) {
            assertNotNull(e);
        }
    }

    @Test
    void testHandleInputValid() {
        assertEquals("HELLO", TryCatchDemo.handleInput("hello"));
    }

    @Test
    void testHandleInputNull() {
        assertEquals("DEFAULT", TryCatchDemo.handleInput(null));
    }

    @Test
    void testDivideSafely() {
        assertEquals(5, TryCatchDemo.divideSafely(10, 2));
    }

    @Test
    void testDivideSafelyByZero() {
        assertEquals(0, TryCatchDemo.divideSafely(10, 0));
    }
}
