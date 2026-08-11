# 05 - The throw Keyword

## Scope

This topic covers Java's `throw` keyword — the mechanism for explicitly raising exceptions during program execution. You will learn what `throw` is, its syntax, the distinction between `throw` and `throws`, how to throw checked and unchecked exceptions, rethrowing patterns, and exception chaining.

## Why It Exists

Programs need a way to signal that something went wrong. While the JVM automatically throws exceptions for errors like `NullPointerException`, most application errors require manual detection. When a method receives invalid input or encounters an impossible state, the developer needs to explicitly create and throw an exception. That is what `throw` does.

```java
// Without throw, you would return error codes everywhere
public int divide(int a, int b) {
    if (b == 0) {
        throw new ArithmeticException("Division by zero");
    }
    return a / b;
}
```

## Design Rationale

The `throw` keyword is deliberately simple. It takes a single expression — an object that is an instance of `Throwable` — and transfers control to the nearest matching `catch` block. The JVM then walks up the call stack looking for a handler.

1. **Single point of failure signaling** — Every `throw` is a visible, auditable error path
2. **Type safety** — Only `Throwable` subtypes can be thrown
3. **Stack unwinding** — The JVM automatically unwinds the call stack and restores local variables
4. **Resource cleanup** — `finally` blocks execute during stack unwinding

## What Is throw

`throw` is a statement that raises an exception. It creates an immediate, non-local transfer of control to the nearest enclosing `catch` block that can handle the exception type.

```java
throw new IllegalArgumentException("Age cannot be negative");
```

### Basic Syntax

```
throw <exception-expression>;
```

The expression must evaluate to a `Throwable` object. A bare exception class without `new` is not valid.

```java
// Valid
throw new IOException("File not found");

// Invalid — does not compile
// throw IOException("File not found");
```

## throw vs throws

| Aspect | `throw` | `throws` |
|--------|---------|----------|
| **What it is** | A statement | A declaration |
| **Purpose** | Raises an exception | Declares exceptions a method may throw |
| **Placement** | Inside a method body | In the method signature |
| **Required?** | Optional (only when throwing) | Required for checked exceptions |
| **Number of exceptions** | One exception at a time | Can list multiple types |

```
  Method Signature                          Method Body
  ┌──────────────────────┐                 ┌──────────────────────┐
  │ public void process()│                 │ {                    │
  │       throws IOException {             │   if (error) {       │
  │       ─────────────  │                 │     throw new IOException │
  │           │          │                 │       ("failed"); }  │
  │      Declaration     │                 │   }                  │
  │     (contract)       │                 │ }                    │
  └──────────────────────┘                 └──────────────────────┘
         throws                                   throw
    "I might throw"                        "I am throwing NOW"
```

## Throwing Checked Exceptions

Checked exceptions must be declared with `throws` in the method signature.

```java
public String readFirstLine(String path) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(path));
    try {
        String line = reader.readLine();
        if (line == null) {
            throw new IOException("File is empty");
        }
        return line;
    } finally {
        reader.close();
    }
}
```

| Checked Exception | When to Use |
|-------------------|-------------|
| `IOException` | File, network, or I/O failure |
| `SQLException` | Database operation failure |
| `ParseException` | Input parsing failure |

## Throwing Unchecked Exceptions

Unchecked exceptions (`RuntimeException` subclasses) do not require a `throws` declaration.

```java
public void validate(User user) {
    if (user == null) {
        throw new NullPointerException("User cannot be null");
    }
    if (user.getAge() < 0) {
        throw new IllegalArgumentException("Invalid age: " + user.getAge());
    }
}
```

| Unchecked Exception | When to Use |
|---------------------|-------------|
| `IllegalArgumentException` | Invalid method argument |
| `IllegalStateException` | Wrong object state |
| `NullPointerException` | Null where non-null required |
| `UnsupportedOperationException` | Operation not supported |

## Rethrowing Exceptions

### Identical Rethrow

```java
public void process(String input) throws IOException {
    try {
        parseInput(input);
    } catch (IOException e) {
        logger.error("Parse failed", e);
        throw e; // rethrow same exception
    }
}
```

### Exception Translation (Wrapped Rethrow)

```java
public UserDTO getUser(long id) {
    try {
        User user = userDao.findById(id);
        return toDTO(user);
    } catch (SQLException e) {
        throw new DataAccessException("Failed to fetch user: " + id, e);
    }
}
```

## Exception Chaining

Exception chaining preserves the original cause when wrapping exceptions.

```java
// Two-argument form (message + cause)
throw new ServiceException("Operation failed", rootCause);

// One-argument form (cause only)
throw new ServiceException(rootCause);
```

## Common Mistakes

### Throwing in finally Block

An exception thrown in `finally` replaces the exception from `try`. The original is lost.

```java
// DANGEROUS — original lost
try {
    throw new RuntimeException("original");
} finally {
    throw new RuntimeException("finally");
}
```

### Throwing null

Throwing `null` produces a `NullPointerException` with no useful information.

```java
RuntimeException e = null;
throw e; // NullPointerException at runtime
```

### Catching What You Throw

Using exceptions as control flow hides the real logic:

```java
// BAD — exception as control flow
try {
    throw new RuntimeException("no value");
} catch (RuntimeException e) {
    return -1;
}

// BETTER — use a conditional
if (!hasValue) return -1;
```

### Catching Too Broadly

Catching `Exception` or `Throwable` hides specific errors:

```java
// BAD — catches everything including programming errors
try {
    processData();
} catch (Exception e) {
    log.error("Failed", e);
}

// BETTER — catch specific exceptions
try {
    processData();
} catch (IOException e) {
    log.error("I/O error", e);
} catch (DataException e) {
    log.error("Data error", e);
}
```

### Swallowing Exceptions

Catching and ignoring exceptions hides failures:

```java
// BAD — exception silently disappears
try {
    riskyOperation();
} catch (Exception e) {
    // empty catch block
}

// BETTER — at minimum, log the exception
try {
    riskyOperation();
} catch (Exception e) {
    log.warn("Operation failed, continuing", e);
}
```

## Version History

| Version | Change |
|---------|--------|
| JDK 1.0 | `throw` keyword introduced |
| JDK 7 | Precise rethrow — catch and rethrow without declaring caught type |
| JDK 7 | Multi-catch improved throw handling |
| JDK 11 | Stack trace improvement for rethrown exceptions |

## Best Practices

1. **Throw early, catch late** — Throw at the point of failure; catch at the level that can handle it.
2. **Use specific types** — Prefer `IllegalArgumentException` over generic `RuntimeException`.
3. **Include meaningful messages** — Explain what went wrong, not just that something failed.
4. **Preserve the cause** — Always chain exceptions when wrapping.
5. **Don't use exceptions for control flow** — Exceptions are expensive. Use conditionals.
6. **Don't throw in finally** — Cleanup exceptions should be caught and handled, not thrown.

## Production Patterns

### Parameter Validation

```java
public void createUser(String name, int age) {
    Objects.requireNonNull(name, "name must not be null");
    if (age < 0 || age > 150) {
        throw new IllegalArgumentException("Invalid age: " + age);
    }
}
```

### Builder Validation

```java
public HttpRequest build() {
    if (method == null) {
        throw new IllegalStateException("HTTP method is required");
    }
    return this;
}
```

### Factory Exception Pattern

```java
public Connection create(String url) {
    try {
        return DriverManager.getConnection(url);
    } catch (SQLException e) {
        throw new ConnectionException("Failed to create connection: " + url, e);
    }
}
```

### Defensive Throw Pattern

```java
public class Cache<K, V> {
    private final Map<K, V> store = new HashMap<>();

    public V get(K key) {
        if (key == null) {
            throw new NullPointerException("Cache key must not be null");
        }
        return store.get(key);
    }
}
```

## Summary

| Concept | Key Point |
|---------|-----------|
| throw Statement | Raises an exception by creating a Throwable |
| Checked Exceptions | Require `throws` declaration in method signature |
| Unchecked Exceptions | Do not require `throws` declaration |
| Rethrowing | Catch and rethrow, optionally wrapping |
| Exception Chaining | Preserve root cause using the cause constructor |
| throw vs throws | `throw` is a statement; `throws` is a declaration |
| Common Mistakes | Throwing in finally, throwing null, swallowing exceptions |
| Best Practices | Throw early, catch late, preserve causes, use specific types |

## Key Takeaways

- `throw` creates an exception and transfers control to the nearest matching `catch`
- `throw` and `throws` are fundamentally different — one raises, the other declares
- Always chain exceptions when wrapping to preserve the root cause
- Never throw in a `finally` block — it masks the original exception
- Use specific exception types rather than generic `Exception`

---

**Next:** [06 - The throws Declaration](../06-throws/README.md)
