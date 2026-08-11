# 12 - Best Practices (Part 2)
**Previous:** [Part 1](README.md)

### Rule 9: Use Custom Exceptions for Domain Errors

Define a domain-specific exception hierarchy for your application's business errors.

```java
public abstract class DomainException extends RuntimeException {
    private final String errorCode;

    protected DomainException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

public class InsufficientFundsException extends DomainException {
    public InsufficientFundsException(BigDecimal balance, BigDecimal amount) {
        super(
            String.format("Insufficient funds: balance=%s, requested=%s", balance, amount),
            "ERR_INSUFFICIENT_FUNDS",
            null);
    }
}
```

**Why:** Domain exceptions carry meaning. A caller can `catch (InsufficientFundsException)`
without guessing. They also carry error codes that map to API responses.

---

### Rule 10: Prefer Unchecked for Programming Bugs

Use `RuntimeException` for precondition violations, argument errors, and internal
invariants. Reserve checked exceptions for recoverable, external failures.

```java
// GOOD: unchecked — caller made a mistake
public void setAge(int age) {
    if (age < 0 || age > 150) {
        throw new IllegalArgumentException("Age must be between 0 and 150, got: " + age);
    }
    this.age = age;
}

// GOOD: checked — external failure, caller can recover
public Connection getConnection() throws SQLException {
    // ...
}
```

**Why:** Programming bugs cannot be recovered from at the call site. Forcing callers to
handle `IllegalArgumentException` adds ceremony without value. Checked exceptions are for
failures where the caller has a meaningful recovery strategy.

---

## Common Pitfalls

### Pitfall: Catching and Rethrowing

```java
// BAD — loses the cause
try {
    service.process(data);
} catch (ServiceException e) {
    throw new ProcessingException("failed");
}

// GOOD — chains the cause
try {
    service.process(data);
} catch (ServiceException e) {
    throw new ProcessingException("failed", e);
}
```

### Pitfall: Using Exceptions as Error Codes

```java
// BAD
public int divide(int a, int b) {
    if (b == 0) throw new ArithmeticException();
    return a / b;
}

// GOOD
public int divide(int a, int b) {
    if (b == 0) throw new ArithmeticException("Cannot divide " + a + " by zero");
    return a / b;
}
```

### Pitfall: Catching Too Early

```java
// BAD — catches outside the loop, swallows per-item failures
try {
    for (String id : ids) {
        process(id);
    }
} catch (ProcessingException e) {
    log.error("one item failed", e);
}

// GOOD — handle each item individually
List<String> failed = new ArrayList<>();
for (String id : ids) {
    try {
        process(id);
    } catch (ProcessingException e) {
        log.warn("failed to process {}: {}", id, e.getMessage());
        failed.add(id);
    }
}
if (!failed.isEmpty()) {
    throw new BatchProcessingException("Failed items: " + failed, failed);
}
```

### Pitfall: Exception in Exception Constructor

```java
// BAD — the message constructor itself can throw
public ConfigException(String path) {
    super("Failed to read: " + Files.readString(Path.of(path)));
}

// GOOD — keep constructors simple
public ConfigException(String path, Throwable cause) {
    super("Failed to read config: " + path, cause);
}
```

---

## Production Patterns

### Pattern: Global Exception Handler

In Spring Boot, use `@ControllerAdvice` to centralize exception handling.

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomain(DomainException ex) {
        return ResponseEntity
            .status(ex.getHttpStatus())
            .body(new ErrorResponse(ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntity
            .status(500)
            .body(new ErrorResponse("ERR_INTERNAL", "An unexpected error occurred"));
    }
}
```

### Pattern: Exception Hierarchy

Design a layered hierarchy where broad types are checked and narrow types carry meaning.

```
RuntimeException
├── DomainException (abstract)
│   ├── InsufficientFundsException
│   ├── DuplicateOrderException
│   └── OrderNotFoundException
├── ValidationException
└── InternalException
```

### Pattern: Error Code Mapping

Map exceptions to HTTP status codes or error codes at the boundary, not inside business
logic.

```java
public enum ErrorCodeMapping {
    INSUFFICIENT_FUNDS("ERR_INSUFFICIENT_FUNDS", 409),
    DUPLICATE_ORDER("ERR_DUPLICATE_ORDER", 409),
    ORDER_NOT_FOUND("ERR_ORDER_NOT_FOUND", 404),
    INTERNAL("ERR_INTERNAL", 500);

    private final String code;
    private final int httpStatus;

    // ...
}
```

### Pattern: Retry with Exception Awareness

Not all exceptions are retryable. Distinguish transient from permanent failures.

```java
public <T> T executeWithRetry(Callable<T> action, int maxAttempts) {
    int attempts = 0;
    while (true) {
        try {
            return action.call();
        } catch (TransientException e) {
            attempts++;
            if (attempts >= maxAttempts) {
                throw e;
            }
            sleep(e.getRetryDelay());
        } catch (PermanentException e) {
            throw e; // don't retry
        }
    }
}
```

### Pattern: Structured Logging

Use MDC to attach exception context to log entries.

```java
try {
    processPayment(order);
} catch (PaymentException e) {
    MDC.put("orderId", order.getId());
    MDC.put("errorCode", e.getErrorCode());
    log.error("Payment failed for order {}: {}", order.getId(), e.getMessage(), e);
    MDC.clear();
    throw e;
}
```

---

## Summary

| Rule | One-liner |
|------|-----------|
| Catch specific types | Don't catch `Exception` or `Throwable` |
| Don't swallow | Always log or rethrow |
| Chain exceptions | Preserve the cause |
| Use try-with-resources | Let the language manage cleanup |
| Document checked exceptions | `@throws` in Javadoc |
| Don't use for control flow | Use `if` instead of `try-catch` |
| Include context | Messages must be descriptive |
| Avoid broad catches | Only at infrastructure boundaries |
| Custom domain exceptions | Carry meaning and error codes |
| Unchecked for bugs | Checked for recoverable external failures |