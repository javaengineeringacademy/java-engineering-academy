[← Part 1](README-Part1.md)

## 8. Common Pitfalls

### 8.1 Catching Unchecked Exceptions Silently

```java
try {
    processOrder(order);
} catch (RuntimeException e) {
    // Swallowing the exception hides bugs
}
```

**Better:** Let the exception propagate or log it meaningfully.

### 8.2 Using Unchecked Exceptions for Flow Control

```java
try {
    return list.get(index);
} catch (IndexOutOfBoundsException e) {
    return defaultValue;
}
```

**Better:** Check the index before accessing.

```java
if (index >= 0 && index < list.size()) {
    return list.get(index);
}
return defaultValue;
```

### 8.3 Overly Broad `catch` Clauses

```java
try {
    riskyOperation();
} catch (Exception e) { // Catches checked AND unchecked
    handleError(e);
}
```

This catches `RuntimeException` subtypes you might want to let propagate.

### 8.4 Declaring Unchecked Exceptions in `throws` Clauses

```java
public void process() throws IllegalArgumentException { // Unnecessary
    // ...
}
```

While technically valid, this clutters the API and is unusual for unchecked
exceptions. Only do this if you want to document a specific unchecked exception
that callers might want to handle.

### 8.5 Catching `Error`

```java
try {
    compute();
} catch (OutOfMemoryError e) {
    // Usually cannot recover
}
```

JVM-level errors like `OutOfMemoryError` should generally not be caught.

---

## 9. Production Patterns

### 9.1 Global Uncaught Exception Handler

For threads that are not directly managed, set a global handler:

```java
Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
    logger.error("Uncaught exception in thread {}: {}",
        thread.getName(), throwable.getMessage(), throwable);
    // Optionally restart the thread or shut down gracefully
});
```

### 9.2 Logging Unchecked Exceptions

Use a logging framework to record the full stack trace:

```java
public void handleRequest(Request request) {
    try {
        processRequest(request);
    } catch (RuntimeException e) {
        logger.error("Failed to process request {}: {}",
            request.getId(), e.getMessage(), e);
        throw e; // Re-throw after logging
    }
}
```

### 9.3 Defensive Programming

Validate inputs at method boundaries to fail fast:

```java
public Order createOrder(List<Item> items) {
    Objects.requireNonNull(items, "items must not be null");
    if (items.isEmpty()) {
        throw new IllegalArgumentException("Order must contain at least one item");
    }
    // proceed
}
```

### 9.4 Using `assert` for Internal Invariants

```java
public void process(Queue queue) {
    assert !queue.isEmpty() : "Queue must not be empty at this point";
    // proceed
}
```

> **Tip:** Enable assertions with `-ea` in development and testing. They are
> disabled by default in production.

### 9.5 Custom Unchecked Exception Hierarchy

For domain-specific errors, create a base unchecked exception:

```java
public class DomainException extends RuntimeException {
    private final String errorCode;

    public DomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
```

Then create specific subtypes:

```java
public class InsufficientFundsException extends DomainException {
    public InsufficientFundsException(double balance, double amount) {
        super("INSUFFICIENT_FUNDS",
            "Balance " + balance + " is less than requested " + amount);
    }
}
```

---

## 10. Summary

| Concept               | Key Point                                          |
|-----------------------|----------------------------------------------------|
| Unchecked exception   | Extends `RuntimeException`; no compile-time check  |
| When to throw         | Programming bugs, invariant violations             |
| When to catch         | Generally should not — fix the bug instead         |
| Checked vs Unchecked  | Checked = recoverable; Unchecked = bug             |
| Production handling   | Global handler, logging, defensive validation      |
| Custom hierarchy      | Extend `RuntimeException` for domain-specific bugs |

---

## 11. Exercises

See the companion files for hands-on practice:

- **Examples:** `examples/UncheckedExceptionExample.java`
- **Exercises:** `exercises/UncheckedExceptionExercises.java`
- **Solutions:** `solutions/UncheckedExceptionSolutions.java`
- **Reference:** `references.md`
- **Decision Guide:** `decision.md`
- **Quiz:** `quiz.md`

---

## Summary

| Concept | Key Point |
|---------|-----------|
| Unchecked Exception | Extends RuntimeException or Error; no compile-time checking |
| When to Throw | Programming bugs, invariant violations, null references, illegal arguments |
| When to Catch | Generally should not; fix the bug instead |
| Checked vs Unchecked | Checked = recoverable external failures; Unchecked = programming bugs |
| Production Handling | Global uncaught handler, logging, defensive programming |
| Common Pitfalls | Silent catching, flow control, broad catch clauses, declaring in throws |
| Subtypes | RuntimeException subtypes (NPE, IAE, etc.) and Error subtypes (OOM, SOOE, etc.) |

## 12. Next Steps

Proceed to the next topic to learn about **custom exceptions** — creating your
own exception classes for domain-specific error handling.
