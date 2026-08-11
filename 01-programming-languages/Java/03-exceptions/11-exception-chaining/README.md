# Exception Chaining in Java

## Table of Contents

1. [Scope](#scope)
2. [Why It Exists](#why-it-exists)
3. [Design Rationale](#design-rationale)
4. [What Is Exception Chaining](#what-is-exception-chaining)
5. [Constructors for Chaining](#constructors-for-chaining)
6. [Cause Retrieval](#cause-retrieval)
7. [When to Chain](#when-to-chain)
8. [Exception Translation Pattern](#exception-translation-pattern)
9. [Root Cause Analysis](#root-cause-analysis)
10. [Common Pitfalls](#common-pitfalls)
11. [Production Patterns](#production-patterns)
12. [Best Practices Summary](#best-practices-summary)

---

## Scope

This topic covers **exception chaining** (also called **exception wrapping**) in Java. It
explains how to link exceptions together to preserve the full error context as errors propagate
through layers of an application. Exception chaining is a critical pattern for debugging
production systems where errors originate deep in the call stack but need to be surfaced
at higher layers.

**Prerequisites:** Basic exception handling, try-catch-finally, checked vs. unchecked exceptions,
custom exceptions.

---

## Why It Exists

When an exception occurs deep in a call stack, the original error details can be lost by the
time the exception reaches the caller. Without exception chaining, a developer debugging a
production issue would see only a high-level error message with no indication of what caused it.

**Problem without chaining:**

```java
// Layer: Data Access
try {
    connection.execute(sql);
} catch (SQLException e) {
    throw new ServiceException("Database error occurred"); // original cause lost!
}
```

The `ServiceException` discards the `SQLException`, making it impossible to trace back to the
root cause. Exception chaining solves this by preserving the full causal chain.

---

## Design Rationale

Java's exception chaining was introduced in **Java 1.4** (JDK 1.4, 2002) as part of the
revised exception handling model. The design goals were:

1. **Preserve causal information** — Every exception in the chain is retained so developers
   can trace the full path of error propagation.
2. **Layered abstraction** — Higher layers can present domain-specific error messages while
   keeping the technical details available for debugging.
3. **Standardized mechanism** — A single API (`Throwable` constructors and methods) for
   all exception chaining, rather than ad-hoc solutions.
4. **Thread safety** — The cause is set once at construction and is immutable afterward.

The `Throwable` class was extended with:
- `Throwable(String message, Throwable cause)` constructor
- `initCause(Throwable cause)` method
- `getCause()` method

---

## What Is Exception Chaining

Exception chaining is the practice of **wrapping one exception inside another** so that the
causal relationship is preserved. When you catch an exception and throw a new one, you can
attach the original exception as the "cause" of the new exception.

```java
try {
    // some operation that fails
    riskyOperation();
} catch (IOException e) {
    // Wrap the low-level exception in a higher-level one
    throw new ServiceException("Failed to process data", e);
}
```

The resulting exception chain looks like:

```
ServiceException: Failed to process data
  -> at com.example.Service.process(Service.java:42)
Caused by: java.io.IOException: Connection refused
  at com.example.DataReader.read(DataReader.java:15)
```

The `ServiceException` is the **outer exception**; the `IOException` is the **root cause**.

---

## Constructors for Chaining

### Constructor with cause

```java
public MyException(String message, Throwable cause) {
    super(message, cause);
}
```

### Constructor with message, cause, and flags

```java
public MyException(String message, Throwable cause, boolean enableSuppression,
                   boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
}
```

### initCause() — for cases where constructor chaining is not available

```java
try {
    riskyOperation();
} catch (IOException e) {
    MyException myEx = new MyException("Failed to process");
    myEx.initCause(e); // must be called exactly once
    throw myEx;
}
```

**Rule:** `initCause()` can only be called once. Calling it again throws
`IllegalStateException`. If you use the constructor with a cause, do not call `initCause()`
again.

---

## Cause Retrieval

### getCause()

```java
Throwable cause = exception.getCause();
if (cause != null) {
    System.err.println("Caused by: " + cause.getMessage());
}
```

### printStackTrace() — shows the full chain

When you call `printStackTrace()` on a chained exception, Java prints the entire chain:

```
academy.javaengineering.exceptions.chaining.ServiceException: Failed to process data
        at com.example.Service.process(Service.java:42)
        at com.example.Main.main(Main.java:10)
Caused by: java.io.IOException: Connection refused
        at com.example.DataReader.read(DataReader.java:15)
        at com.example.Service.process(Service.java:38)
        ... 1 more
```

### Iterating the cause chain

```java
Throwable t = exception;
while (t != null) {
    System.out.println(t.getClass().getName() + ": " + t.getMessage());
    t = t.getCause();
}
```

---

## When to Chain

### 1. Wrapping checked exceptions

When a method must declare a checked exception but the underlying cause is a different
checked exception, wrap it:

```java
public class ServiceException extends Exception {
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Usage
public void processData() throws ServiceException {
    try {
        readData();
    } catch (IOException e) {
        throw new ServiceException("Failed to read data", e);
    }
}
```

### 2. Translating exceptions

When you need to convert a low-level exception into a domain-specific exception:

```java
public User findUser(long id) throws UserNotFoundException {
    try {
        return userDao.findById(id);
    } catch (DataAccessException e) {
        throw new UserNotFoundException("User not found: " + id, e);
    }
}
```

### 3. Combining multiple exceptions

When you need to propagate multiple causes (e.g., in parallel processing):

```java
public class CompositeException extends RuntimeException {
    private final List<Throwable> causes;

    public CompositeException(List<Throwable> causes) {
        super("Multiple errors occurred");
        this.causes = causes;
    }

    public List<Throwable> getCauses() {
        return Collections.unmodifiableList(causes);
    }
}
```

---

## Exception Translation Pattern

The **exception translation pattern** (also called **exception wrapping pattern**) is one of
the most important uses of exception chaining. It involves catching a low-level exception and
rethrowing it as a higher-level exception while preserving the original cause.

### Why translate?

- Low-level exceptions (e.g., `SQLException`, `IOException`) leak implementation details
  to higher layers.
- Higher layers should present domain-specific error messages.
- The original cause should still be available for debugging.

### Example

```java
public class DataAccessException extends RuntimeException {
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}

public class UserRepository {
    public User findById(long id) {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            // ... map to User
            return user;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find user with id " + id, e);
        }
    }
}
```

### Translation layers

```
Low-level:        SQLException
                     ↓
Middle-level:     DataAccessException
                     ↓
High-level:       ServiceException
                     ↓
Presentation:     UserNotFoundException (user-facing message)
```

---

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
