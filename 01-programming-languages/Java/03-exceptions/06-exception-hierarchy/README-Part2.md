# 06 - Exception Hierarchy (Part 2)
**Previous:** [Part 1](README.md)

## 9. Custom Hierarchy Design Patterns

### 9.1 Base Exception Class

Create a base exception for your application or library:

```java
public class MyApplicationException extends Exception {
    public MyApplicationException(String message) {
        super(message);
    }
    public MyApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

### 9.2 Domain-Specific Exception

Extend the base exception for specific domains:

```java
public class PaymentException extends MyApplicationException {
    public PaymentException(String message) {
        super(message);
    }
    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

### 9.3 Unchecked Exception Base

For programming errors:

```java
public class MyApplicationRuntimeException extends RuntimeException {
    public MyApplicationRuntimeException(String message) {
        super(message);
    }
    public MyApplicationRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

### 9.4 Exception Hierarchy Pattern

```
MyApplicationException (checked)
├── PaymentException
├── OrderException
├── UserException
└── InventoryException

MyApplicationRuntimeException (unchecked)
├── PaymentRuntimeException
├── OrderRuntimeException
├── UserRuntimeException
└── InventoryRuntimeException
```

### 9.5 Exception Code Pattern

Use an enum for error codes:

```java
public enum ErrorCode {
    PAYMENT_FAILED,
    PAYMENT_TIMEOUT,
    ORDER_NOT_FOUND,
    USER_NOT_AUTHORIZED
}

public class PaymentException extends MyApplicationException {
    private final ErrorCode errorCode;
    public PaymentException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
```

## 10. Common Pitfalls

### 10.1 Catching Too High

```java
// BAD: Catches everything including errors
try {
    // risky code
} catch (Exception e) {
    // swallows OutOfMemoryError, StackOverflowError, etc.
    // too broad
}
```

### 10.2 Wrong Level in Hierarchy

```java
// BAD: Catches checked exception but ignores unchecked
try {
    // risky code
} catch (IOException e) {
    // handles checked IOException
    // but NullPointerException is NOT caught — it propagates up
}
```

### 10.3 Empty Catch Block

```java
try {
    // risky code
} catch (Exception e) {
    // silently swallows the exception
}
```

### 10.4 Catching `Throwable` Instead of `Exception`

```java
// BAD: Catches Error too
try {
    // risky code
} catch (Throwable t) {
    // catches OutOfMemoryError, StackOverflowError
    // which should not be caught
}
```

### 10.5 Not Chaining Exceptions

```java
try {
    // risky code
} catch (IOException e) {
    throw new MyException(e); // Good: preserves cause
}
// vs
try {
    // risky code
} catch (IOException e) {
    throw new MyException(e.getMessage()); // Bad: loses cause
}
```

### 10.6 Catching `RuntimeException` to Hide Bugs

```java
try {
    // risky code
} catch (RuntimeException e) {
    // handles NullPointerException, etc.
    // hides programming bugs
}
```

## 11. Production Patterns

### 11.1 Exception Translation

Convert low-level exceptions into application-level exceptions:

```java
try {
    // JDBC code
} catch (SQLException e) {
    throw new DataAccessException("Failed to query database", e);
}
```

### 11.2 Exception Unwrapping

Use `getCause()` to extract the original exception:

```java
try {
    // code
} catch (MyException e) {
    Throwable cause = e.getCause();
    if (cause instanceof IOException) {
        // handle IO error
    }
}
```

### 11.3 Exception Hierarchy as Documentation

The hierarchy documents the application's failure modes. Use custom exceptions to
create a clear, self-documenting exception structure.

### 11.4 Exception Mapping

Map exceptions to HTTP status codes in web applications:

```java
if (exception instanceof ResourceNotFoundException) {
    return 404;
} else if (exception instanceof ValidationException) {
    return 400;
} else if (exception instanceof UnauthorizedException) {
    return 401;
} else {
    return 500;
}
```

### 11.5 Exception Logging

Log exceptions with context:

```java
try {
    // code
} catch (MyException e) {
    logger.error("Failed to process request: {}", request, e);
    throw e;
}
```

## 12. Summary

- The Java exception hierarchy is rooted in `Throwable`.
- `Error` is for unrecoverable JVM errors; `Exception` is for application errors.
- `RuntimeException` is the unchecked exception branch; other `Exception` subclasses
  are checked.
- The JVM finds the first matching `catch` block in the call stack.
- Custom exceptions should extend from a base exception for your domain.
- Avoid catching too high or too low in the hierarchy.
- Use exception chaining to preserve the cause.
- Design your exception hierarchy to document your application's failure modes.

## 13. Key Takeaways

1. `Throwable` → `Error` / `Exception` → `RuntimeException` / checked exceptions.
2. Checked exceptions enforce handling at compile time.
3. Unchecked exceptions (RuntimeException) indicate programming bugs.
4. Catch the most specific exception type first.
5. Custom exceptions should extend `Exception` (checked) or `RuntimeException` (unchecked).
6. Never catch `Error` unless you have a specific recovery strategy.
7. Use exception chaining to preserve the original cause.
8. Design your exception hierarchy to mirror your application's failure modes.
