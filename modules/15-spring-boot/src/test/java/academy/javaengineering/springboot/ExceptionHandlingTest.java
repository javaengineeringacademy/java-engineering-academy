package academy.javaengineering.springboot;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExceptionHandlingTest {

    @Test
    void shouldHandleNotFound() {
        ExceptionHandlingExample.GlobalExceptionHandler handler = new ExceptionHandlingExample.GlobalExceptionHandler();
        ExceptionHandlingExample.Response error = handler.handleNotFound("Not found");
        assertEquals(404, error.getStatus());
    }

    @Test
    void shouldHandleBadRequest() {
        ExceptionHandlingExample.GlobalExceptionHandler handler = new ExceptionHandlingExample.GlobalExceptionHandler();
        ExceptionHandlingExample.Response error = handler.handleBadRequest("Bad request");
        assertEquals(400, error.getStatus());
    }

    @Test
    void shouldHandleInternal() {
        ExceptionHandlingExample.GlobalExceptionHandler handler = new ExceptionHandlingExample.GlobalExceptionHandler();
        ExceptionHandlingExample.Response error = handler.handleInternal("Error");
        assertEquals(500, error.getStatus());
    }
}
