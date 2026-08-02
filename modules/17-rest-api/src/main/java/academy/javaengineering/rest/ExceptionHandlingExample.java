package academy.javaengineering.rest;

import java.util.*;

public class ExceptionHandlingExample {

    public static class ApiException extends RuntimeException {
        private final int status;
        private final String errorCode;
        private final Map<String, String> details;

        public ApiException(int status, String errorCode, String message, Map<String, String> details) {
            super(message);
            this.status = status;
            this.errorCode = errorCode;
            this.details = details;
        }

        public int getStatus() { return status; }
        public String getErrorCode() { return errorCode; }
        public Map<String, String> getDetails() { return details; }
    }

    public static class ErrorResponse {
        private final int status;
        private final String errorCode;
        private final String message;
        private final Map<String, String> details;
        private final String timestamp;

        public ErrorResponse(int status, String errorCode, String message,
                            Map<String, String> details) {
            this.status = status;
            this.errorCode = errorCode;
            this.message = message;
            this.details = details;
            this.timestamp = java.time.Instant.now().toString();
        }

        @Override
        public String toString() {
            return "ErrorResponse{status=" + status + ", errorCode='" + errorCode +
                    "', message='" + message + "', details=" + details +
                    ", timestamp='" + timestamp + "'}";
        }
    }

    public static class GlobalExceptionHandler {

        public ErrorResponse handleApiException(ApiException ex) {
            System.out.println("Handling API Exception: " + ex.getMessage());
            return new ErrorResponse(
                    ex.getStatus(),
                    ex.getErrorCode(),
                    ex.getMessage(),
                    ex.getDetails()
            );
        }

        public ErrorResponse handleValidationException(String field, String message) {
            System.out.println("Handling Validation Exception: " + field + " - " + message);
            return new ErrorResponse(
                    400,
                    "VALIDATION_ERROR",
                    "Validation failed",
                    Map.of(field, message)
            );
        }

        public ErrorResponse handleResourceNotFoundException(String resource, Long id) {
            System.out.println("Handling Not Found: " + resource + " with id " + id);
            return new ErrorResponse(
                    404,
                    "RESOURCE_NOT_FOUND",
                    resource + " not found with id: " + id,
                    Map.of("resource", resource, "id", String.valueOf(id))
            );
        }

        public ErrorResponse handleConflictException(String message) {
            System.out.println("Handling Conflict: " + message);
            return new ErrorResponse(
                    409,
                    "CONFLICT",
                    message,
                    null
            );
        }

        public ErrorResponse handleGenericException(Exception ex) {
            System.out.println("Handling Generic Exception: " + ex.getMessage());
            return new ErrorResponse(
                    500,
                    "INTERNAL_ERROR",
                    "An unexpected error occurred",
                    Map.of("exception", ex.getClass().getSimpleName())
            );
        }
    }

    public static class UserService {
        private final Map<Long, Map<String, Object>> users = new HashMap<>();
        private long idCounter = 1;

        public Map<String, Object> createUser(String name, String email) {
            if (name == null || name.isBlank()) {
                throw new ApiException(400, "VALIDATION_ERROR", "Name is required", Map.of("field", "name"));
            }
            if (email == null || email.isBlank()) {
                throw new ApiException(400, "VALIDATION_ERROR", "Email is required", Map.of("field", "email"));
            }

            boolean emailExists = users.values().stream()
                    .anyMatch(u -> email.equals(u.get("email")));
            if (emailExists) {
                throw new ApiException(409, "CONFLICT", "Email already exists", Map.of("email", email));
            }

            Long id = idCounter++;
            Map<String, Object> user = Map.of("id", id, "name", name, "email", email);
            users.put(id, user);
            return user;
        }

        public Map<String, Object> getUser(Long id) {
            Map<String, Object> user = users.get(id);
            if (user == null) {
                throw new ApiException(404, "RESOURCE_NOT_FOUND", "User not found", Map.of("id", String.valueOf(id)));
            }
            return user;
        }

        public void deleteUser(Long id) {
            if (!users.containsKey(id)) {
                throw new ApiException(404, "RESOURCE_NOT_FOUND", "User not found", Map.of("id", String.valueOf(id)));
            }
            users.remove(id);
        }
    }

    public static void main(String[] args) {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        UserService userService = new UserService();

        System.out.println("=== Exception Handling Demo ===\n");

        System.out.println("--- Validation Error ---");
        try {
            userService.createUser("", "test@example.com");
        } catch (ApiException e) {
            ErrorResponse response = handler.handleApiException(e);
            System.out.println("Response: " + response);
        }

        System.out.println("\n--- Conflict Error ---");
        try {
            userService.createUser("John", "john@example.com");
            userService.createUser("John2", "john@example.com");
        } catch (ApiException e) {
            ErrorResponse response = handler.handleApiException(e);
            System.out.println("Response: " + response);
        }

        System.out.println("\n--- Not Found Error ---");
        try {
            userService.getUser(999L);
        } catch (ApiException e) {
            ErrorResponse response = handler.handleApiException(e);
            System.out.println("Response: " + response);
        }

        System.out.println("\n--- Generic Exception ---");
        ErrorResponse response = handler.handleGenericException(
                new RuntimeException("Something went wrong"));
        System.out.println("Response: " + response);
    }
}
