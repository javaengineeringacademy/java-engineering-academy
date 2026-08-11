# 15 - Production Exception Patterns

## Scope

This module covers production-grade exception handling patterns used in real-world
Java applications. You will learn global exception handling, structured error
responses, monitoring integration, resilience patterns, and production checklists.

**Duration:** 45 minutes

## Prerequisites

- Solid understanding of Java exception hierarchy (topics 01-15)
- Familiarity with REST APIs and HTTP status codes
- Basic knowledge of Spring Boot (helpful but not required)

## Why This Matters

Exception handling in production is fundamentally different from learning exercises.
A single unhandled exception can cascade into service outages. Poor error responses
break API contracts. Missing correlation IDs make debugging impossible. This topic
bridges the gap between "knowing how to throw exceptions" and "building resilient
production systems."

---

## What Are Production Exception Patterns?

Production exception patterns are established conventions for handling failures in
deployed applications. They address:

- **Consistency:** Every API returns uniform error structures
- **Observability:** Failures are logged, traced, and alerted
- **Resilience:** Systems degrade gracefully instead of crashing
- **Security:** Internal details are never exposed to clients

The core principle: **treat exceptions as first-class events in your system, not
afterthoughts.**

---

## Global Exception Handling

Instead of catching exceptions in every controller or service, centralize handling
in one place.

### Spring Boot Controller Advice

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            "RESOURCE_NOT_FOUND",
            ex.getMessage(),
            Instant.now(),
            generateTraceId()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .collect(Collectors.joining(", "));
        ErrorResponse error = new ErrorResponse(
            "VALIDATION_FAILED", message, Instant.now());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("Unhandled exception", ex);
        ErrorResponse error = new ErrorResponse(
            "INTERNAL_ERROR",
            "An unexpected error occurred",
            Instant.now(),
            generateTraceId()
        );
        return ResponseEntity.status(
            HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
```

### Servlet Filter Approach

```java
@Component
public class ExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(request, response);
        } catch (BusinessException ex) {
            writeErrorResponse(response, ex.getStatusCode(),
                ex.getErrorCode(), ex.getMessage());
        } catch (Exception ex) {
            log.error("Unhandled exception on {} {}",
                request.getMethod(), request.getRequestURI(), ex);
            writeErrorResponse(response,
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_ERROR", "An unexpected error occurred");
        }
    }
}
```

### Why Centralized Handling?

| Benefit              | Description                                  |
|----------------------|----------------------------------------------|
| Single responsibility| Controllers focus on business logic          |
| Consistent responses | Same error format across all endpoints       |
| Easier maintenance   | Update error handling in one location        |
| Security             | One place to sanitize all error outputs      |

---

## Exception-to-HTTP-Status Mapping

Map exception types to appropriate HTTP status codes. This is your API contract.

### Recommended Mapping

| Exception Type               | HTTP Status                    | Use Case                  |
|------------------------------|--------------------------------|---------------------------|
| `ResourceNotFoundException`  | 404 Not Found                  | Entity does not exist     |
| `ValidationException`        | 400 Bad Request                | Invalid input data        |
| `DuplicateResourceException` | 409 Conflict                   | Resource already exists   |
| `UnauthorizedException`      | 401 Unauthorized               | Missing/invalid creds     |
| `ForbiddenException`         | 403 Forbidden                  | Insufficient permissions  |
| `RateLimitExceededException` | 429 Too Many Requests          | Throttling active         |
| `BusinessRuleException`      | 422 Unprocessable Entity       | Logic constraint violated |
| `ServiceUnavailableException`| 503 Service Unavailable        | Dependency down           |
| `Exception` (fallback)       | 500 Internal Server Error      | Unexpected failure        |

### Mapping Implementation

```java
public class ExceptionStatusMapper {

    private static final Map<Class<? extends Exception>, HttpStatus> MAPPING =
        Map.of(
            ResourceNotFoundException.class, HttpStatus.NOT_FOUND,
            ValidationException.class, HttpStatus.BAD_REQUEST,
            DuplicateResourceException.class, HttpStatus.CONFLICT,
            UnauthorizedException.class, HttpStatus.UNAUTHORIZED,
            ForbiddenException.class, HttpStatus.FORBIDDEN,
            RateLimitExceededException.class, HttpStatus.TOO_MANY_REQUESTS,
            ServiceUnavailableException.class, HttpStatus.SERVICE_UNAVAILABLE
        );

    public static HttpStatus resolve(Exception ex) {
        return MAPPING.getOrDefault(ex.getClass(),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

---

## Structured Error Responses

Every error response should follow a consistent structure. This enables clients
to programmatically handle errors.

### Standard Error Response

```java
public class ErrorResponse {

    private String errorCode;
    private String message;
    private Instant timestamp;
    private String traceId;
    private List<FieldError> fieldErrors;

    public static class FieldError {
        private String field;
        private String rejectedValue;
        private String message;
    }
}
```

### JSON Output

```json
{
    "errorCode": "VALIDATION_FAILED",
    "message": "email: must be a valid email",
    "timestamp": "2026-01-15T10:30:00Z",
    "traceId": "abc-123-def-456",
    "fieldErrors": [
        {
            "field": "email",
            "rejectedValue": "not-an-email",
            "message": "must be a valid email"
        }
    ]
}
```

### Trace ID Generation

```java
public class TraceIdGenerator {
    public static String generate() {
        return UUID.randomUUID().toString()
            .replace("-", "").substring(0, 16);
    }
}
```

### Error Code Convention

Use uppercase snake_case codes that describe the error category:

```
RESOURCE_NOT_FOUND
VALIDATION_FAILED
DUPLICATE_EMAIL
INSUFFICIENT_BALANCE
EXTERNAL_SERVICE_TIMEOUT
RATE_LIMIT_EXCEEDED
```

---

## Exception Monitoring and Alerting

### Logging Exceptions Correctly

```java
log.error("Failed to process order {} for user {}",
    orderId, userId, exception);
```

**Never do this:**

```java
log.error("Error: " + exception.getMessage());  // Loses stack trace
log.error(exception.toString());  // Wrong: toString() loses trace
System.out.println(exception);    // Wrong: not structured logging
```

### Integration with Monitoring Tools

**Sentry:**

```java
Sentry.captureException(ex, new SentryEvent()
    .withTag("service", "order-service")
    .withTag("userId", userId)
    .withExtra("orderId", orderId));
```

**DataDog:**

```java
Span span = tracer.buildSpan("processOrder").start();
try (Scope scope = tracer.activateSpan(span)) {
    span.setTag("order.id", orderId);
    // business logic
} catch (Exception ex) {
    span.setTag("error", true);
    span.setTag("error.message", ex.getMessage());
    throw ex;
} finally {
    span.finish();
}
```

**ELK Stack (Logback + Logstash):**

```json
{
    "timestamp": "2026-01-15T10:30:00Z",
    "level": "ERROR",
    "logger": "OrderService",
    "message": "Failed to process order",
    "traceId": "abc-123-def-456",
    "exception": "com.example.OrderException",
    "stackTrace": "...",
    "orderId": "ORD-789",
    "userId": "USR-456"
}
```

### Alerting Strategy

| Alert Level | Condition              | Action                    |
|-------------|------------------------|---------------------------|
| **Critical**| >50 5xx errors/min     | Page on-call engineer     |
| **Warning** | >10 5xx errors/min     | Investigate within 1 hour |
| **Info**    | Spike in 4xx errors    | Review during business hours |
| **Silent**  | Single 500 error       | Log only, no alert        |

---

---
**Continue:** [Part 2](README-Part2.md)
