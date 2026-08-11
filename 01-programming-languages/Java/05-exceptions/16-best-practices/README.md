# 16. Production Exception Patterns: Best Practices

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

## Circuit Breaker Pattern

The circuit breaker prevents cascading failures when a dependency is unavailable.

### Resilience4j Configuration

```java
CircuitBreakerConfig config = CircuitBreakerConfig.custom()
    .failureRateThreshold(50)
    .waitDurationInOpenState(Duration.ofSeconds(30))
    .slidingWindowSize(10)
    .build();

CircuitBreaker circuitBreaker =
    CircuitBreaker.of("paymentService", config);

Supplier<PaymentResult> decoratedSupplier = CircuitBreaker
    .decorateSupplier(circuitBreaker,
        () -> paymentService.process(order));

Try<PaymentResult> result = Try.ofSupplier(decoratedSupplier)
    .recover(CallNotPermittedException.class, ex -> {
        log.warn("Circuit breaker OPEN for payment service");
        return PaymentResult.degraded();
    });
```

### Circuit States

```
CLOSED ---(failure threshold)---> OPEN
  ^                                  |
  |                          (wait duration)
  |                                  v
  +---(success)--- HALF_OPEN ---(success)---> CLOSED
                         |
                    (failure)
                         v
                       OPEN
```

### Spring Boot Integration

```java
@Service
public class OrderService {

    @CircuitBreaker(name = "paymentService",
        fallbackMethod = "paymentFallback")
    public PaymentResult processPayment(Order order) {
        return paymentClient.charge(order);
    }

    public PaymentResult paymentFallback(Order order, Exception ex) {
        log.warn("Payment service unavailable, queuing for retry");
        return PaymentResult.queued(order.getId());
    }
}
```

---

## Retry Pattern with Exceptions

Retries handle transient failures. Not all exceptions are retryable.

### Retryable vs Non-Retryable

| Retryable                    | Non-Retryable              |
|------------------------------|----------------------------|
| `SocketTimeoutException`     | `InvalidDataException`     |
| `HttpServerErrorException`   | `AuthenticationException`  |
| `CircuitBreakerOpenException`| `ResourceNotFoundException`|
| `DatabaseConnectionException`| `ConstraintViolationException`|

### Resilience4j Retry Configuration

```java
RetryConfig retryConfig = RetryConfig.custom()
    .maxAttempts(3)
    .waitDuration(Duration.ofMillis(500))
    .exponentialBackoff(2, Duration.ofSeconds(5))
    .retryExceptions(
        SocketTimeoutException.class,
        HttpServerErrorException.class)
    .ignoreExceptions(
        ValidationException.class,
        ResourceNotFoundException.class)
    .build();

Retry retry = Retry.of("apiCall", retryConfig);

Supplier<Response> decoratedSupplier = Retry
    .decorateSupplier(retry, () -> externalApi.call());

Try<Response> result = Try.ofSupplier(decoratedSupplier);
```

### Spring Retry

```java
@Service
public class NotificationService {

    @Retryable(
        value = {MessagingException.class, TimeoutException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void sendNotification(Notification notification) {
        messagingClient.send(notification);
    }

    @Recover
    public void handleNotificationFailure(
            Notification notification, Exception ex) {
        log.error("Notification failed after retries, "
            + "saving to dead letter queue", ex);
        deadLetterQueue.save(notification, ex);
    }
}
```

---

## Graceful Degradation Strategies

When a dependency fails, degrade rather than crash.

### Fallback Hierarchy

```java
public class ProductService {

    public Product getProduct(String id) {
        try {
            return primaryDatabase.find(id);
        } catch (DatabaseException e) {
            log.warn("Primary DB failed, trying replica");
            try {
                return replicaDatabase.find(id);
            } catch (DatabaseException e2) {
                log.warn("Replica also failed, using cache");
                return cache.get(id).orElseThrow(() ->
                    new ServiceUnavailableException(
                        "Product service degraded"));
            }
        }
    }
}
```

### Cached Fallback

```java
@CircuitBreaker(name = "productService",
    fallbackMethod = "cachedFallback")
@Retry(name = "productService")
public Product getProduct(String id) {
    Product product = productServiceClient.get(id);
    cache.put(id, product);
    return product;
}

private Product cachedFallback(String id, Exception ex) {
    return cache.get(id).orElseThrow(() ->
        new ServiceUnavailableException("Product unavailable"));
}
```

---

## Common Pitfalls

### 1. Exposing Internal Details

```java
// BAD: Exposes stack trace to client
@ExceptionHandler(Exception.class)
public String handleError(Exception ex) {
    return ex.toString();
}

// GOOD: Sanitized response
@ExceptionHandler(Exception.class)
public ResponseEntity<ErrorResponse> handleError(Exception ex) {
    log.error("Internal error", ex);
    return ResponseEntity.status(500).body(
        new ErrorResponse("INTERNAL_ERROR",
            "An unexpected error occurred", Instant.now()));
}
```

### 2. No Correlation IDs

```java
// BAD: No way to trace across services
log.error("Failed to process order " + orderId);

// GOOD: Correlation ID propagated through request
MDC.put("traceId", traceId);
log.error("Failed to process order {} [traceId={}]",
    orderId, traceId);
```

### 3. Catching and Swallowing

```java
// BAD: Silent failure
try {
    service.process();
} catch (Exception e) {
    // silently ignored
}

// GOOD: At minimum, log it
try {
    service.process();
} catch (Exception e) {
    log.error("Failed to process", e);
    throw new ProcessingException("Processing failed", e);
}
```

### 4. Overly Broad Catch Blocks

```java
// BAD: Catches everything including programming errors
try {
    processOrder(order);
} catch (Exception e) {
    return "error";
}

// GOOD: Catch specific exceptions
try {
    processOrder(order);
} catch (InventoryException e) {
    return handleInventoryError(order, e);
} catch (PaymentException e) {
    return handlePaymentError(order, e);
}
```

### 5. Using Exceptions for Control Flow

```java
// BAD: Exception as flow control
public User findUser(String id) {
    try {
        return repository.get(id);
    } catch (NotFoundException e) {
        return User.defaultUser();
    }
}

// GOOD: Check existence first
public User findUser(String id) {
    return repository.findById(id)
        .orElse(User.defaultUser());
}
```

---

## Production Checklist

Use this checklist before deploying to production:

- [ ] **Global exception handler** configured for all controllers
- [ ] **Error responses** follow consistent structure (code, message, timestamp, traceId)
- [ ] **HTTP status mapping** defined for all business exceptions
- [ ] **No stack traces** exposed in API responses
- [ ] **No sensitive data** (passwords, tokens, internal IPs) in error messages
- [ ] **Correlation IDs** generated and propagated across services
- [ ] **Logging** captures exception class, message, and stack trace
- [ ] **Circuit breakers** configured for external service calls
- [ ] **Retry policies** defined for transient failures only
- [ ] **Fallback mechanisms** in place for critical paths
- [ ] **Alerting** configured for error rate thresholds
- [ ] **Dead letter queues** for failed async processing
- [ ] **Graceful shutdown** handles in-flight requests
- [ ] **Health checks** report dependency status
- [ ] **Load testing** verifies behavior under failure conditions

---

## Key Takeaways

1. **Centralize** exception handling with `@RestControllerAdvice`
2. **Map** exceptions to HTTP status codes systematically
3. **Structure** error responses with errorCode, message, timestamp, traceId
4. **Never** expose stack traces or internals to clients
5. **Monitor** exceptions with tools like Sentry, DataDog, ELK
6. **Protect** against cascading failures with circuit breakers
7. **Retry** only transient failures with exponential backoff
8. **Degrade** gracefully with fallbacks and cached data
9. **Log** exceptions with full context, not just the message
10. **Test** failure scenarios as thoroughly as happy paths
