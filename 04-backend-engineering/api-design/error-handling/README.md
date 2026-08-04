# API Error Handling

## Comprehensive Guide to API Error Responses

Consistent error handling is crucial for API usability. This guide covers error response formats, Problem Details (RFC 7807), and best practices.

---

## Table of Contents

1. [Error Handling Overview](#error-handling-overview)
2. [Error Response Format](#error-response-format)
3. [Problem Details (RFC 7807)](#problem-details-rfc-7807)
4. [Global Exception Handling](#global-exception-handling)
5. [Validation Errors](#validation-errors)
6. [Best Practices](#best-practices)

---

## Error Handling Overview

### Error Categories

```
1xx: Informational
2xx: Success
3xx: Redirection
4xx: Client Errors (Your fault)
  - 400 Bad Request
  - 401 Unauthorized
  - 403 Forbidden
  - 404 Not Found
  - 409 Conflict
  - 422 Unprocessable Entity
  - 429 Too Many Requests
5xx: Server Errors (Our fault)
  - 500 Internal Server Error
  - 502 Bad Gateway
  - 503 Service Unavailable
```

---

## Error Response Format

### Simple Format

```json
{
    "error": {
        "code": "USER_NOT_FOUND",
        "message": "User with id 123 not found",
        "details": {
            "userId": "123"
        }
    }
}
```

### Detailed Format

```json
{
    "error": {
        "code": "VALIDATION_ERROR",
        "message": "Request validation failed",
        "details": [
            {
                "field": "email",
                "message": "must be a valid email address",
                "rejectedValue": "invalid-email"
            },
            {
                "field": "password",
                "message": "must be at least 8 characters",
                "rejectedValue": "short"
            }
        ],
        "timestamp": "2024-01-15T10:30:00Z",
        "path": "/api/users",
        "traceId": "abc123"
    }
}
```

### Error Response Class

```java
@Data
@Builder
public class ErrorResponse {
    private String code;
    private String message;
    private List<FieldError> details;
    private LocalDateTime timestamp;
    private String path;
    private String traceId;

    @Data
    @Builder
    public static class FieldError {
        private String field;
        private String message;
        private Object rejectedValue;
    }

    public static ErrorResponse of(String code, String message) {
        return ErrorResponse.builder()
            .code(code)
            .message(message)
            .timestamp(LocalDateTime.now())
            .build();
    }
}
```

---

## Problem Details (RFC 7807)

### Problem Response Format

```json
{
    "type": "https://api.example.com/errors/validation",
    "title": "Validation Error",
    "status": 422,
    "detail": "The request body contains invalid fields",
    "instance": "/api/users",
    "errors": [
        {
            "field": "email",
            "message": "must be a valid email"
        }
    ]
}
```

### Problem Details Class

```java
@Data
@Builder
public class ProblemDetail {
    private String type;
    private String title;
    private int status;
    private String detail;
    private String instance;
    private Map<String, Object> properties;

    public static ProblemDetail forStatus(int status) {
        return ProblemDetail.builder()
            .status(status)
            .title(getTitleForStatus(status))
            .build();
    }

    public static ProblemDetail forStatusAndDetail(int status, String detail) {
        return forStatus(status)
            .withDetail(detail);
    }

    public ProblemDetail withDetail(String detail) {
        this.detail = detail;
        return this;
    }

    public ProblemDetail withProperty(String key, Object value) {
        if (this.properties == null) {
            this.properties = new HashMap<>();
        }
        this.properties.put(key, value);
        return this;
    }

    private static String getTitleForStatus(int status) {
        return switch (status) {
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 409 -> "Conflict";
            case 422 -> "Unprocessable Entity";
            case 429 -> "Too Many Requests";
            case 500 -> "Internal Server Error";
            default -> "Error";
        };
    }
}
```

### Spring Boot Implementation

```java
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage()
        );
        problem.setType("https://api.example.com/errors/not-found");
        problem.setProperty("resourceType", ex.getResourceType());
        problem.setProperty("resourceId", ex.getResourceId());
        return problem;
    }

    @ExceptionHandler(ValidationException.class)
    public ProblemDetail handleValidation(ValidationException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.UNPROCESSABLE_ENTITY.value(),
            "Validation failed"
        );
        problem.setType("https://api.example.com/errors/validation");
        problem.setProperty("errors", ex.getErrors());
        return problem;
    }
}
```

---

## Global Exception Handling

### Exception Hierarchy

```java
// Base exception
public class ApiException extends RuntimeException {
    private final String code;
    private final int status;

    public ApiException(String code, String message, int status) {
        super(message);
        this.code = code;
        this.status = status;
    }
}

// Specific exceptions
public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException(String resourceType, Object resourceId) {
        super("NOT_FOUND",
            String.format("%s with id %s not found", resourceType, resourceId),
            404);
    }
}

public class DuplicateResourceException extends ApiException {
    public DuplicateResourceException(String resourceType, String field, Object value) {
        super("ALREADY_EXISTS",
            String.format("%s with %s '%s' already exists", resourceType, field, value),
            409);
    }
}

public class ValidationException extends ApiException {
    private final List<FieldError> errors;

    public ValidationException(List<FieldError> errors) {
        super("VALIDATION_ERROR", "Validation failed", 422);
        this.errors = errors;
    }
}
```

### Handler Implementation

```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final ErrorMetrics errorMetrics;

    @ExceptionHandler(ApiException.class)
    public ProblemDetail handleApiException(ApiException ex,
                                            HttpServletRequest request) {
        log.warn("API error: {} - {}", ex.getCode(), ex.getMessage());

        errorMetrics.recordError(ex.getCode());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            ex.getStatus(), ex.getMessage());
        problem.setType("https://api.example.com/errors/" + ex.getCode().toLowerCase());
        problem.setInstance(request.getRequestURI());
        problem.setProperty("traceId", getTraceId());

        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(
            MethodArgumentNotValidException ex) {

        List<ErrorResponse.FieldError> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(fieldError -> ErrorResponse.FieldError.builder()
                .field(fieldError.getField())
                .message(fieldError.getDefaultMessage())
                .rejectedValue(fieldError.getRejectedValue())
                .build())
            .collect(Collectors.toList());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            422, "Validation failed");
        problem.setProperty("errors", errors);

        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemHandle handleGenericException(Exception ex) {
        log.error("Unexpected error", ex);

        return ProblemDetail.forStatusAndDetail(
            500, "An unexpected error occurred");
    }
}
```

---

## Validation Errors

### Request Validation

```java
public class CreateUserRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$",
        message = "Password must contain uppercase, lowercase, and number")
    private String password;
}
```

### Validation Error Response

```json
{
    "type": "https://api.example.com/errors/validation",
    "title": "Validation Error",
    "status": 422,
    "detail": "The request body contains invalid fields",
    "errors": [
        {
            "field": "email",
            "message": "Email must be valid",
            "rejectedValue": "invalid-email"
        },
        {
            "field": "password",
            "message": "Password must be at least 8 characters",
            "rejectedValue": "short"
        }
    ]
}
```

---

## Best Practices

### 1. Use Consistent Error Codes

```java
public final class ErrorCodes {
    public static final String NOT_FOUND = "NOT_FOUND";
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    public static final String FORBIDDEN = "FORBIDDEN";
    public static final String CONFLICT = "CONFLICT";
    public static final String RATE_LIMITED = "RATE_LIMITED";
}
```

### 2. Include Request Context

```java
@ExceptionHandler(Exception.class)
public ProblemDetail handleException(Exception ex,
                                      HttpServletRequest request) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(500, "Error");
    problem.setProperty("path", request.getRequestURI());
    problem.setProperty("method", request.getMethod());
    problem.setProperty("timestamp", Instant.now());
    problem.setProperty("traceId", getTraceId());
    return problem;
}
```

### 3. Log Errors Appropriately

```java
@ExceptionHandler(ResourceNotFoundException.class)
public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
    // Don't log 404s at error level
    log.debug("Resource not found: {}", ex.getMessage());
    return ProblemDetail.forStatusAndDetail(404, ex.getMessage());
}

@ExceptionHandler(Exception.class)
public ProblemHandle handleException(Exception ex) {
    // Log unexpected errors
    log.error("Unexpected error", ex);
    return ProblemDetail.forStatusAndDetail(500, "Internal server error");
}
```

### 4. Never Expose Internal Details

```java
// Bad
@ExceptionHandler(Exception.class)
public ProblemDetail handleException(Exception ex) {
    return ProblemDetail.forStatusAndDetail(500, ex.getMessage());
}

// Good
@ExceptionHandler(Exception.class)
public ProblemDetail handleException(Exception ex) {
    log.error("Internal error", ex);
    return ProblemDetail.forStatusAndDetail(500, "Internal server error");
}
```

### 5. Document Error Responses

```java
@Operation(summary = "Get user",
    responses = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
@GetMapping("/{id}")
public ResponseEntity<User> getUser(@PathVariable Long id) {
    return ResponseEntity.ok(userService.findById(id));
}
```

### 6. Use Proper HTTP Status Codes

```java
// 400 - Client sent bad data
@ExceptionHandler(ValidationException.class)
public ResponseEntity<ProblemDetail> handleValidation(ValidationException ex) {
    return ResponseEntity.badRequest().body(...);
}

// 401 - Not authenticated
@ExceptionHandler(AuthenticationException.class)
public ResponseEntity<ProblemDetail> handleAuth(AuthenticationException ex) {
    return ResponseEntity.status(401).body(...);
}

// 403 - Authenticated but not authorized
@ExceptionHandler(AccessDeniedException.class)
public ResponseEntity<ProblemDetail> handleAccess(AccessDeniedException ex) {
    return ResponseEntity.status(403).body(...);
}

// 404 - Resource not found
@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<ProblemDetail> handleNotFound(ResourceNotFoundException ex) {
    return ResponseEntity.status(404).body(...);
}

// 409 - Conflict
@ExceptionHandler(DuplicateResourceException.class)
public ResponseEntity<ProblemDetail> handleDuplicate(DuplicateResourceException ex) {
    return ResponseEntity.status(409).body(...);
}
```

---

## Further Reading

- [RFC 7807 - Problem Details for HTTP APIs](https://datatracker.ietf.org/doc/html/rfc7807)
- [Microsoft REST API Guidelines - Error Handling](https://github.com/microsoft/api-guidelines/blob/vNext/Guidelines.md#7102-error-condition-responses)
- [Google API Design Guide - Errors](https://cloud.google.com/apis/design/errors)
- [Spring Boot Error Handling](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-web-applications.spring-mvc.error-handling)
