# 11 - Exception Chaining (Part 2)
**Previous:** [Part 1](README.md)

## Root Cause Analysis

When debugging a production issue, you often need to find the **root cause** — the exception
at the very bottom of the chain. This is the exception that originally triggered the cascade.

### Finding the root cause

```java
public static Throwable getRootCause(Throwable e) {
    Throwable cause = e.getCause();
    while (cause != null) {
        Throwable nextCause = cause.getCause();
        if (nextCause == null) {
            return cause;
        }
        cause = nextCause;
    }
    return e;
}
```

### Finding a specific exception type in the chain

```java
public static <T extends Throwable> T findCauseInChain(Throwable e, Class<T> type) {
    Throwable cause = e;
    while (cause != null) {
        if (type.isInstance(cause)) {
            return type.cast(cause);
        }
        cause = cause.getCause();
    }
    return null;
}
```

### Logging the full chain

```java
public static void logFullChain(Throwable e) {
    Throwable current = e;
    int depth = 0;
    while (current != null) {
        logger.error("Cause #{}: {} - {}", depth, current.getClass().getSimpleName(),
                current.getMessage(), current);
        current = current.getCause();
        depth++;
    }
}
```

---

## Common Pitfalls

### 1. Circular cause chains

Do not create circular references in cause chains. This will cause infinite loops when
iterating or printing the stack trace.

```java
Exception a = new Exception("A");
Exception b = new Exception("B", a);
a.initCause(b); // CIRCULAR! a -> b -> a -> b -> ...
```

### 2. Losing the root cause

Always pass the original exception as the cause when wrapping:

```java
// BAD — original cause is lost
try {
    riskyOperation();
} catch (IOException e) {
    throw new ServiceException("Failed to process"); // e is lost!
}

// GOOD — original cause is preserved
try {
    riskyOperation();
} catch (IOException e) {
    throw new ServiceException("Failed to process", e); // e is preserved
}
```

### 3. Double-wrapping

Avoid wrapping an exception that is already wrapped at a higher level:

```java
// BAD — double wrapping
try {
    try {
        riskyOperation();
    } catch (IOException e) {
        throw new ServiceException("Service failed", e);
    }
} catch (ServiceException e) {
    throw new RuntimeException("Wrapper", e); // double wrapping
}
```

### 4. Throwing exceptions in the constructor of the cause

Never throw an exception inside a cause constructor:

```java
// BAD
throw new Exception("msg", new RuntimeException("cause"));

// This is fine — both are created normally
throw new Exception("msg", new RuntimeException("cause"));
```

### 5. Swallowing exceptions

Never catch an exception and silently ignore it without rethrowing:

```java
// BAD
try {
    riskyOperation();
} catch (IOException e) {
    // silently ignored — impossible to debug
}
```

---

## Production Patterns

### 1. Exception wrapper utility

Create a utility class to wrap exceptions consistently across your application:

```java
public class ExceptionUtils {

    public static ServiceException wrapException(String message, Throwable cause) {
        if (cause instanceof ServiceException) {
            return (ServiceException) cause;
        }
        return new ServiceException(message, cause);
    }
}
```

### 2. Error response mapping

Map exception chains to structured error responses for APIs:

```java
public class ErrorResponse {
    private String code;
    private String message;
    private String rootCause;

    public static ErrorResponse fromException(Exception e) {
        Throwable rootCause = ExceptionUtils.getRootCause(e);
        return new ErrorResponse(
            determineErrorCode(e),
            e.getMessage(),
            rootCause.getMessage()
        );
    }
}
```

### 3. Exception hierarchy design

Design your exception hierarchy to leverage chaining effectively:

```java
// Base exception
public class AppException extends Exception {
    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Domain-specific exceptions
public class ServiceException extends AppException {
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

public class DataException extends AppException {
    public DataException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

### 4. Exception logging with context

Log the full cause chain with context about where the exception occurred:

```java
public class ExceptionLogger {

    public static void logException(Throwable e, String context) {
        StringBuilder sb = new StringBuilder();
        sb.append("Exception in context: ").append(context).append("\n");

        Throwable current = e;
        int depth = 0;
        while (current != null) {
            sb.append("  ".repeat(depth));
            sb.append("[").append(current.getClass().getSimpleName()).append("] ");
            sb.append(current.getMessage()).append("\n");
            current = current.getCause();
            depth++;
        }

        logger.error(sb.toString(), e);
    }
}
```

### 5. Exception translation in service layer

Use exception translation to hide infrastructure details from the business layer:

```java
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findUser(long id) {
        try {
            return userRepository.findById(id);
        } catch (DataAccessException e) {
            throw new UserNotFoundException("User not found: " + id, e);
        }
    }
}
```

---

## Best Practices Summary

1. **Always preserve the cause** — Pass the original exception as the cause when wrapping.
2. **Translate at layer boundaries** — Catch low-level exceptions and translate them at
   each layer's boundary.
3. **Use constructors over initCause()** — Prefer the constructor with cause parameter
   over `initCause()`.
4. **Avoid circular chains** — Never create circular references in cause chains.
5. **Log the full chain** — When logging exceptions, ensure the full cause chain is logged.
6. **Design exception hierarchies** — Create domain-specific exception hierarchies with
   chaining support.
7. **Expose root cause for debugging** — Make the root cause available for debugging
   while presenting a user-friendly message.
8. **Use exception translation** — Translate exceptions at layer boundaries to keep
   implementation details private.
9. **Test exception chains** — Write tests that verify the cause chain is correct.
10. **Document exception behavior** — Document which exceptions are thrown and how they
    are wrapped in your API.

---

## Summary

Exception chaining is a fundamental Java feature that allows developers to preserve
the full causal chain when exceptions propagate through layers. By wrapping exceptions
and translating them at layer boundaries, you can maintain clean abstractions while
ensuring that the root cause of errors is never lost. Understanding exception chaining
is essential for building robust, maintainable Java applications.

---

## Navigation

- **Previous:** [Exception Hierarchy](../10-exception-hierarchy/)
- **Next:** [Exception Design Patterns](../12-exception-design-patterns/)
