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

## Exception Cause Chain Structure

```
┌─────────────────────────────────────────────────────────┐
│                   Exception Chain                       │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  ┌───────────────────────────────────────────────┐      │
│  │ ServiceException ("Failed to process data")   │      │
│  │    │                                          │      │
│  │    │  getCause()                              │      │
│  │    ▼                                          │      │
│  │  ┌─────────────────────────────────────┐      │      │
│  │  │ IOException ("Connection refused")  │      │      │
│  │  │    │                                │      │      │
│  │  │    │  getCause()                    │      │      │
│  │  │    ▼                                │      │      │
│  │  │  ┌──────────────────────────┐       │      │      │
│  │  │  │ SQLException ("timeout")│       │      │      │
│  │  │  │    │                    │       │      │      │
│  │  │  │    │  getCause()        │       │      │      │
│  │  │  │    ▼                    │       │      │      │
│  │  │  │  ┌────────────┐        │       │      │      │
│  │  │  │  │ null       │        │       │      │      │
│  │  │  │  │ (end)      │        │       │      │      │
│  │  │  │  └────────────┘        │       │      │      │
│  │  │  └──────────────────────────┘       │      │      │
│  │  └─────────────────────────────────────┘      │      │
│  └───────────────────────────────────────────────┘      │
│                                                         │
│  Outermost ──────────────────────────────► Innermost    │
│  (highest layer)                      (root cause)      │
└─────────────────────────────────────────────────────────┘
```

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

## Summary

| Concept | Key Point |
|---------|-----------|
| Exception Chaining | Wrapping one exception inside another to preserve causal context |
| Constructors | Use `(String, Throwable)` or `initCause()` to link exceptions |
| Cause Retrieval | `getCause()` and `printStackTrace()` reveal the full exception chain |
| Exception Translation | Convert low-level exceptions to domain-specific exceptions at each layer |
| When to Chain | Wrapping checked exceptions, translating exceptions, combining multiple causes |
| Root Cause Analysis | Iterate cause chain to find the original failure point |
| initCause() Rule | Can only be called once; calling again throws IllegalStateException |
| Production Pattern | Use exception translation in layered architecture to isolate implementation details |

---
**Continue:** [Part 2](README-Part2.md)
