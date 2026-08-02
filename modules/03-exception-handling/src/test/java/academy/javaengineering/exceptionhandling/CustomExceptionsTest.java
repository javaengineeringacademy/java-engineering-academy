package academy.javaengineering.exceptionhandling;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for custom exception classes.
 */
class CustomExceptionsTest {

    @Test
    void testValidationExceptionWithMessage() {
        var exception = new CustomExceptions.ValidationException("Email is required");
        assertEquals("Email is required", exception.getMessage());
        assertNull(exception.getFieldName());
    }

    @Test
    void testValidationExceptionWithFieldName() {
        var exception = new CustomExceptions.ValidationException("email", "Invalid format");
        assertEquals("Invalid format", exception.getMessage());
        assertEquals("email", exception.getFieldName());
    }

    @Test
    void testIllegalStateException() {
        var exception = new CustomExceptions.IllegalStateException("Cannot perform action", "INACTIVE");
        assertEquals("Cannot perform action", exception.getMessage());
        assertEquals("INACTIVE", exception.getState());
    }

    @Test
    void testDatabaseExceptionWithChaining() {
        Exception cause = new Exception("Connection refused");
        var exception = new CustomExceptions.DatabaseException("Failed to connect", cause, 1001);

        assertEquals("Failed to connect", exception.getMessage());
        assertEquals(1001, exception.getErrorCode());
        assertNotNull(exception.getCause());
        assertEquals("Connection refused", exception.getCause().getMessage());
    }

    @Test
    void testExceptionInheritance() {
        var validationException = new CustomExceptions.ValidationException("test");
        assertInstanceOf(Exception.class, validationException);

        var stateException = new CustomExceptions.IllegalStateException("msg", "state");
        assertInstanceOf(RuntimeException.class, stateException);
    }

    @Test
    void testExceptionPropagation() {
        assertThrows(CustomExceptions.ValidationException.class, () -> {
            CustomExceptions.validateAge(-1);
        });
    }

    @Test
    void testExceptionChaining() {
        assertThrows(CustomExceptions.DatabaseException.class, () -> {
            CustomExceptions.connectToDatabase();
        });
    }
}
