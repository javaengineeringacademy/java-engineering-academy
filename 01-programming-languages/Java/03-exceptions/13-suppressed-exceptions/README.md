# Suppressed Exceptions

## Scope

This topic covers suppressed exceptions in Java — what they are, how they work internally,
why they were introduced, and how to use them correctly in production code.

## Prerequisites

- Basic exception handling (try-catch-finally)
- Try-with-resources (TWR)
- Exception chaining

## Learning Objectives

- Understand the design rationale behind suppressed exceptions
- Know when and how the JVM automatically adds suppressed exceptions
- Use `addSuppressed()` and `getSuppressed()` correctly
- Debug and log suppressed exceptions in production

---

## Why Suppressed Exceptions Exist

### The Problem Before Java 7

Before Java 7, if an exception was thrown during resource cleanup (in a `finally` block),
the original exception was lost:

```java
try {
    throw new IOException("read failed");
} finally {
    throw new RuntimeException("cleanup failed");
}
// Only RuntimeException("cleanup failed") is visible
// IOException is gone forever
```

This was a serious problem. You could lose the root cause of a failure because
cleanup code also failed.

### The Design Rationale

Java 7 introduced **suppressed exceptions** to solve this. When an exception is thrown
and a second exception occurs during cleanup, the second exception is "suppressed"
— attached to the first exception rather than replacing it.

The primary exception is the one that originally caused the failure. The suppressed
exception is the one that occurred during cleanup. Both are preserved and accessible.

---

## What Are Suppressed Exceptions

A suppressed exception is an exception that was suppressed in favor of another
exception. In practice, this happens when:

1. An exception is propagating out of a `try` block
2. A `finally` block or `close()` call throws an exception
3. The cleanup exception is added as a **suppressed exception** on the primary

### How They Work

The `Throwable` class maintains a list of suppressed exceptions:

```java
// Add a suppressed exception
primaryException.addSuppressed(cleanupException);

// Retrieve suppressed exceptions
Throwable[] suppressed = primaryException.getSuppressed();
```

### Key Rules

- A suppressed exception is **not** the cause. It is a separate exception
  attached as a sibling.
- A throwable can have **multiple** suppressed exceptions.
- A throwable **cannot** suppress itself.
- Setting a suppressed exception that is equal to the primary throws
  `IllegalArgumentException`.

---

## Suppressed Exception Flow

```
┌─────────────────────────────────────────────────────────┐
│                    TWR Execution                         │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  ┌─────────────────────────────────────────┐            │
│  │ try (Resource r = new Resource())       │            │
│  │    │                                    │            │
│  │    ▼                                    │            │
│  │  ┌────────────────────────────────┐     │            │
│  │  │ Body throws: IOException       │     │            │
│  │  └────────────────┬───────────────┘     │            │
│  │                   │                     │            │
│  │                   ▼                     │            │
│  │  ┌────────────────────────────────┐     │            │
│  │  │ r.close() throws RuntimeException│    │            │
│  │  └────────────────┬───────────────┘     │            │
│  │                   │                     │            │
│  └───────────────────┼─────────────────────┘            │
│                      │                                  │
│                      ▼                                  │
│  ┌──────────────────────────────────────────┐           │
│  │ Result:                                  │           │
│  │                                          │           │
│  │  Primary:  IOException                   │           │
│  │  Suppressed: RuntimeException            │           │
│  │  (from r.close())                        │           │
│  │                                          │           │
│  │  primary.getSuppressed() returns:        │           │
│  │    [RuntimeException]                    │           │
│  └──────────────────────────────────────────┘           │
└─────────────────────────────────────────────────────────┘
```

## Try-With-Resources and Suppressed Exceptions

TWR is where suppressed exceptions appear most often. When a resource's
`close()` throws an exception while another exception is propagating,
the `close()` exception becomes suppressed.

### Basic TWR Behavior

```java
try (MyResource r = new MyResource()) {
    throw new IOException("primary");
}
// If MyResource.close() throws RuntimeException,
// it becomes a suppressed exception on the IOException
```

### Multiple Resources

When multiple resources are declared, each resource's `close()` can produce
a suppressed exception independently. They are all added to the primary:

```java
try (ResourceA a = new ResourceA();
     ResourceB b = new ResourceB()) {
    throw new IOException("primary");
}
// If a.close() throws, it is suppressed on IOException
// If b.close() throws, it is also suppressed on IOException
```

### The Close Exception vs the Primary

The primary exception is the one thrown from the `try` block body.
The close exception is always the suppressed one. This is deliberate —
the primary is typically the more interesting exception.

---

## Exception Chaining with Suppressed

Do not confuse suppressed exceptions with **cause** chaining:

```java
// Cause chaining — the original exception wrapped inside another
catch (Exception e) {
    throw new ServiceException("wrapping", e);
}

// Suppressed exceptions — additional exceptions attached alongside
primary.addSuppressed(suppressed);
```

A cause is wrapped inside another exception. A suppressed exception sits
beside the primary. Both can coexist:

```java
try {
    try (Resource r = new Resource()) {
        throw new IOException("io error");
    }
} catch (IOException e) {
    Throwable[] suppressed = e.getSuppressed(); // from close()
    throw new ServiceException("service failed", e); // cause chaining
}
```

---

## When to Use Suppressed Exceptions Manually

TWR handles suppressed exceptions automatically. You should only call
`addSuppressed()` manually in specific scenarios.

### Scenario 1: Custom Resource Management

When you manage resources manually and need to preserve cleanup failures:

```java
InputStream in = null;
try {
    in = new FileInputStream("data.txt");
    // process
} catch (IOException e) {
    throw e;
} finally {
    if (in != null) {
        try {
            in.close();
        } catch (IOException closeEx) {
            e.addSuppressed(closeEx);
        }
    }
}
```

### Scenario 2: Multiple Parallel Operations

When running parallel operations and need to aggregate failures:

```java
IOException primary = null;
for (Callable<Void> task : tasks) {
    try {
        task.call();
    } catch (IOException e) {
        if (primary == null) {
            primary = e;
        } else {
            primary.addSuppressed(e);
        }
    }
}
if (primary != null) {
    throw primary;
}
```

### Scenario 3: Wrapper Code That Must Preserve All Exceptions

When wrapping operations and need to preserve both the operation exception
and the cleanup exception without losing either.

---

## Common Pitfalls

### Pitfall 1: Ignoring Suppressed Exceptions

```java
try (Resource r = new Resource()) {
    // ...
} catch (Exception e) {
    // Logs the primary but ignores suppressed
    logger.error("Failed", e);
    // Suppressed exceptions are lost in logs!
}
```

Always log all suppressed exceptions:

```java
catch (Exception e) {
    logger.error("Failed: {}", e.getMessage());
    for (Throwable suppressed : e.getSuppressed()) {
        logger.error("  Suppressed: {}", suppressed.getMessage());
    }
}
```

### Pitfall 2: Stack Trace Pollution

When many exceptions are suppressed, the stack trace becomes very long
and hard to read. In high-throughput systems, this can impact memory
and log readability.

### Pitfall 3: Assuming Cause Instead of Suppressed

Developers often check `getCause()` when they should check
`getSuppressed()`. The cause wraps an exception inside another.
The suppressed exception sits alongside the primary.

### Pitfall 4: Relying on Suppressed Order

The order of suppressed exceptions depends on implementation details
of TWR and the order resources are closed. Do not write code that
depends on a specific order.

---

## Production Patterns

### Pattern 1: Logging Suppressed Exceptions

```java
try (Connection conn = dataSource.getConnection()) {
    // business logic
} catch (SQLException e) {
    logger.error("Query failed: {}", e.getMessage(), e);
    for (Throwable suppressed : e.getSuppressed()) {
        logger.error("Suppressed: {}", suppressed.getMessage(), suppressed);
    }
    throw new DataAccessException("Query failed", e);
}
```

### Pattern 2: Aggregating Exceptions in Bulk Operations

```java
IOException primary = null;
for (String file : files) {
    try {
        processFile(file);
    } catch (IOException e) {
        if (primary == null) {
            primary = e;
        } else {
            primary.addSuppressed(e);
        }
    }
}
if (primary != null) {
    throw primary;
}
```

### Pattern 3: Debugging Suppressed Exceptions

When debugging, always check suppressed exceptions:

```java
try {
    try (Resource r = problematicResource()) {
        // ...
    }
} catch (Exception e) {
    System.out.println("Primary: " + e);
    for (Throwable s : e.getSuppressed()) {
        System.out.println("Suppressed: " + s);
    }
}
```

### Pattern 4: Custom Exception Types with Suppressed Support

```java
public class PipelineException extends Exception {
    public PipelineException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        for (Throwable suppressed : getSuppressed()) {
            sb.append("\n  Suppressed: ").append(suppressed);
        }
        return sb.toString();
    }
}
```

---

## Summary

| Concept | Description |
|---------|-------------|
| Primary exception | The exception that caused the failure |
| Suppressed exception | An additional exception from cleanup |
| `addSuppressed()` | Attach a suppressed exception |
| `getSuppressed()` | Retrieve all suppressed exceptions |
| TWR | Automatically manages suppressed exceptions |
| Cause vs Suppressed | Cause wraps inside; suppressed sits alongside |

---

## Next Steps

- Review the internals in `03-internals/`
- Practice with exercises in `exercises/`
- See production patterns in `examples/`
