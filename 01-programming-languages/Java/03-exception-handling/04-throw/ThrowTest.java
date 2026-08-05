import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThrowTest {

    @Test
    void testThrowBasicDemo() {
        assertThrows(IllegalArgumentException.class, () -> {
            ThrowDemo.validateAge(-1);
        });
    }

    @Test
    void testThrowWithMessage() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            ThrowDemo.processInput("");
        });
        assertEquals("Input cannot be null or empty", e.getMessage());
    }

    @Test
    void testThrowChainedException() {
        RuntimeException e = assertThrows(RuntimeException.class, () -> {
            ThrowDemo.processData(null);
        });
        assertNotNull(e.getCause());
    }

    @Test
    void testValidateAgeValid() {
        assertDoesNotThrow(() -> ThrowDemo.validateAge(25));
    }

    @Test
    void testProcessInputNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            ThrowDemo.processInput(null);
        });
    }

    @Test
    void testValidateRangeOutOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> {
            ThrowDemo.validateRange(15, 1, 10);
        });
    }
}
