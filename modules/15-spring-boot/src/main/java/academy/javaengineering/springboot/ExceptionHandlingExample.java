package academy.javaengineering.springboot;

import java.util.ArrayList;
import java.util.List;

/**
 * Exception Handling - @ControllerAdvice, @ExceptionHandler.
 */
public class ExceptionHandlingExample {

    public static class ErrorResponse {
        private final int status;
        private final String message;
        private final long timestamp;

        public ErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }

        public int getStatus() { return status; }
        public String getMessage() { return message; }
        public long getTimestamp() { return timestamp; }
    }

    public static class GlobalExceptionHandler {
        private final List<ErrorResponse> errors = new ArrayList<>();

        public ErrorResponse handleNotFound(String message) {
            ErrorResponse error = new ErrorResponse(404, message);
            errors.add(error);
            return error;
        }

        public ErrorResponse handleBadRequest(String message) {
            ErrorResponse error = new ErrorResponse(400, message);
            errors.add(error);
            return error;
        }

        public ErrorResponse handleInternal(String message) {
            ErrorResponse error = new ErrorResponse(500, message);
            errors.add(error);
            return error;
        }

        public List<ErrorResponse> getErrors() { return errors; }
    }

    public static void main(String[] args) {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        System.out.println(handler.handleNotFound("User not found"));
        System.out.println(handler.handleBadRequest("Invalid input"));
    }
}
