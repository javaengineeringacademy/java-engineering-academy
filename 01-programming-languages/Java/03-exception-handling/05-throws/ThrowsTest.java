import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThrowsTest {

    @Test
    void testThrowsDeclaration() {
        assertThrows(FileNotFoundException.class, () -> {
            ThrowsDemo.throwsDeclarationDemo();
        });
    }

    @Test
    void testMethodChainingWithException() {
        assertDoesNotThrow(() -> ThrowsDemo.processData("valid"));
    }

    @Test
    void testMethodChainingInvalidData() {
        assertThrows(Exception.class, () -> ThrowsDemo.processData(""));
    }

    @Test
    void testReadFileNotFound() {
        assertThrows(FileNotFoundException.class, () -> {
            ThrowsDemo.readFile("nonexistent.txt");
        });
    }

    @Test
    void testMultiExceptionDeclaration() {
        assertThrows(NullPointerException.class, () -> {
            ThrowsDemo.multiExceptionDeclaration();
        });
    }

    @Test
    void testProcessDataNull() {
        assertThrows(Exception.class, () -> ThrowsDemo.processData(null));
    }

    @Test
    void testExceptionPropagation() {
        try {
            ThrowsDemo.processData("");
            fail("Should throw");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("validation"));
        }
    }
}
