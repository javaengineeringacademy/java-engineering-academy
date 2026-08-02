package academy.javaengineering.rest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionHandlingTest {

    @Test
    void testHandleApiException() {
        ExceptionHandlingExample.GlobalExceptionHandler handler =
                new ExceptionHandlingExample.GlobalExceptionHandler();
        ExceptionHandlingExample.ApiException ex = new ExceptionHandlingExample.ApiException(
                404, "NOT_FOUND", "User not found", null);
        ExceptionHandlingExample.ErrorResponse response = handler.handleApiException(ex);
        assertEquals(404, response.toString().contains("404") ? 404 : 0);
    }

    @Test
    void testHandleValidationException() {
        ExceptionHandlingExample.GlobalExceptionHandler handler =
                new ExceptionHandlingExample.GlobalExceptionHandler();
        ExceptionHandlingExample.ErrorResponse response = handler.handleValidationException(
                "email", "Email is required");
        assertNotNull(response);
    }

    @Test
    void testHandleResourceNotFound() {
        ExceptionHandlingExample.GlobalExceptionHandler handler =
                new ExceptionHandlingExample.GlobalExceptionHandler();
        ExceptionHandlingExample.ErrorResponse response = handler.handleResourceNotFoundException(
                "User", 999L);
        assertNotNull(response);
    }

    @Test
    void testUserServiceValidation() {
        ExceptionHandlingExample.UserService service = new ExceptionHandlingExample.UserService();
        assertThrows(ExceptionHandlingExample.ApiException.class, () -> {
            service.createUser("", "test@example.com");
        });
    }

    @Test
    void testUserServiceConflict() {
        ExceptionHandlingExample.UserService service = new ExceptionHandlingExample.UserService();
        service.createUser("John", "john@example.com");
        assertThrows(ExceptionHandlingExample.ApiException.class, () -> {
            service.createUser("John2", "john@example.com");
        });
    }
}
