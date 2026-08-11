# 16 - Production Patterns (Part 2)
**Previous:** [Part 1](README.md)

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
