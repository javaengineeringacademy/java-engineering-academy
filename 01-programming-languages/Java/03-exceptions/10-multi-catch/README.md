# Multi-Catch Exceptions in Java

## Overview

Multi-catch is a language feature introduced in Java 7 that allows a single `catch`
block to handle multiple exception types. It reduces boilerplate when different
exceptions require identical handling logic.

```java
try {
    // risky code
} catch (IOException | SQLException e) {
    log.error("I/O or SQL error", e);
}
```

---

## Why Multi-Catch Exists

Before Java 7, handling several exception types with the same logic required
either:

1. **Duplicated catch blocks** — identical code repeated for each type.
2. **Catching a common superclass** — risks catching unintended exceptions and
   loses specific type information.

Both approaches violate DRY or weaken type safety. Multi-catch addresses this
trade-off directly.

### The Duplication Problem

```java
try {
    parseConfig();
} catch (FileNotFoundException e) {
    logger.error("Config missing", e);
    useDefaults();
} catch (MalformedURLException e) {
    logger.error("Bad config URL", e);
    useDefaults();
}
```

The recovery logic is identical; only the logged message differs. Multi-catch
collapses this into one block.

---

## Multi-Catch Syntax

The pipe operator `|` separates exception types inside a single `catch` clause.

```java
catch (ExceptionType1 | ExceptionType2 | ExceptionType3 e) {
    // handle all three identically
}
```

### Rules

| Rule | Detail |
|------|--------|
| Types separated by `\|` | No commas allowed |
| No inheritance relationship | The types must not be related by `extends` |
| Variable is implicitly `final` | Cannot reassign `e` inside the block |
| Variable type is the union | The compiler treats `e` as the least upper bound |

### Effectively Final Variable

Inside a multi-catch block, the exception variable is **effectively final**.
You can read it, pass it, or wrap it, but you cannot assign a new value.

```java
catch (IOException | SQLException e) {
    // e = new IOException("x");  // COMPILE ERROR
    Errors.report(e);             // OK
}
```

This restriction exists because the compiler generates a synthetic exception
class behind the scenes; allowing reassignment would break the contract.

---

## Bytecode Behavior

When you write a multi-catch, the compiler generates a **synthetic exception
class** that extends the common superclass of the caught types.

For `catch (IOException | SQLException e)`, the compiler may produce:

```
class SyntheticException extends Exception { ... }
```

At runtime, the JVM performs a single `instanceof` check against this synthetic
class. This means multi-catch is **not slower** than individual catch blocks—
it is equivalent in performance.

```
// Simplified bytecode equivalent
try {
    // body
} catch (SyntheticException e) {
    // handler
}
```

The synthetic class is an implementation detail; you never see or reference it
in source code.

---

## Multi-Catch vs Multiple Catch Blocks

### Side-by-Side Comparison

```java
// Multiple catch blocks
try {
    processData();
} catch (IOException e) {
    handleError(e);
} catch (SQLException e) {
    handleError(e);
} catch (TimeoutException e) {
    handleError(e);
}

// Multi-catch (Java 7+)
try {
    processData();
} catch (IOException | SQLException | TimeoutException e) {
    handleError(e);
}
```

### Multi-Catch vs Multiple Catch Blocks

```
┌──────────────────────────────┐    ┌──────────────────────────────┐
│    Multiple Catch Blocks     │    │       Multi-Catch (Java 7+)  │
├──────────────────────────────┤    ├──────────────────────────────┤
│ try {                        │    │ try {                        │
│   // body                    │    │   // body                    │
│ } catch (IOException e) {   │    │ } catch (IOException |       │
│   handleError(e);            │    │         SQLException |       │
│ } catch (SQLException e) {   │    │         TimeoutException e) {│
│   handleError(e);            │    │   handleError(e);            │
│ } catch (TimeoutException e)│    │ }                            │
│   handleError(e);            │    │                              │
│ }                            │    │                              │
└──────────────────────────────┘    └──────────────────────────────┘
     DRY violation                      Clean, DRY, readable
     3 catch blocks                     1 catch block
     Different types                    All types in one block
```

### When to Prefer Multiple Catch Blocks

- Each exception requires **different recovery** logic.
- You need to access type-specific methods (`SQLException.getSQLState()`).
- Exception types are unrelated and catching them together would be misleading.

### When to Prefer Multi-Catch

- All exceptions require **identical handling**.
- The exceptions are semantically related (e.g., all I/O-related).
- You want to reduce visual noise in error-handling code.

---

## Practical Examples

### Example 1 — Resource Cleanup

```java
public void closeQuietly(AutoCloseable resource) {
    try {
        resource.close();
    } catch (Exception e) {
        LOG.warn("Failed to close resource", e);
    }
}
```

A single `catch (Exception e)` works here, but if you want narrower scope:

```java
public void closeQuietly(Closeable resource) {
    try {
        resource.close();
    } catch (IOException | IllegalArgumentException e) {
        LOG.warn("Failed to close resource", e);
    }
}
```

### Example 2 — Parsing Multiple Formats

```java
public Config loadConfig(String path) {
    try {
        String raw = Files.readString(Path.of(path));
        return objectMapper.readValue(raw, Config.class);
    } catch (IOException | JsonProcessingException e) {
        throw new ConfigException("Failed to load config from " + path, e);
    }
}
```

### Example 3 — Network Operations

```java
public User fetchUser(String id) {
    try {
        HttpResponse response = httpClient.get("/users/" + id);
        return parseUser(response.body());
    } catch (IOException | InterruptedException | TimeoutException e) {
        throw new ServiceException("Could not fetch user " + id, e);
    }
}
```

---

## Common Pitfalls

### 1. Catching Unrelated Exceptions

```java
// BAD: These exceptions have nothing in common
catch (FileNotFoundException | NullPointerException e) {
    // misleading — implies they are related
}
```

Group only exceptions that share a semantic relationship or require the same
handling strategy.

### 2. Losing Type Information

```java
catch (IOException | SQLException e) {
    // Cannot call e.getSQLState() — compiler only knows it is Exception
    // Must use instanceof checks to recover the specific type
    if (e instanceof SQLException se) {
        // use se
    }
}
```

If you need type-specific behavior, separate catch blocks are clearer.

### 3. Catching too Broadly

```java
// BAD: Exception is the root of everything
catch (Exception | Error e) { ... }
```

This catches `OutOfMemoryError`, `StackOverflowError`, and every checked
exception. Be specific.

### 4. Mixing Checked and Unchecked

```java
// Valid but potentially confusing
catch (IOException | RuntimeException e) { ... }
```

This works, but consider whether the grouping makes sense to future readers.

---

## Production Patterns

### Pattern 1 — Exception Translation

```java
public DomainObject load(long id) {
    try {
        return repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Object " + id));
    } catch (NotFoundException e) {
        throw e;
    } catch (IOException | DataAccessException e) {
        throw new ServiceException("Failed to load object " + id, e);
    }
}
```

### Pattern 2 — Retry Logic

```java
public <T> T retry(Supplier<T> action, int maxAttempts) {
    for (int attempt = 1; attempt <= maxAttempts; attempt++) {
        try {
            return action.get();
        } catch (IOException | InterruptedException e) {
            if (attempt == maxAttempts) {
                throw new RuntimeException("Failed after " + maxAttempts + " attempts", e);
            }
            backoff(attempt);
        }
    }
    throw new IllegalStateException("Unreachable");
}
```

### Pattern 3 — Logging Without Swallowing

```java
try {
    externalService.call(request);
} catch (TimeoutException | ConnectionException e) {
    metrics.increment("external.call.failure");
    LOG.warn("External call failed: {}", e.getMessage());
    throw new ServiceException("External call failed", e);
}
```

Always rethrow or propagate after logging. Silent swallowing is a bug.

### Pattern 4 — Fallback with Multi-Catch

```java
public byte[] readBytes(String path) {
    try {
        return Files.readAllBytes(Path.of(path));
    } catch (IOException | SecurityException e) {
        LOG.warn("Cannot read {}, using default", path);
        return DEFAULT_BYTES;
    }
}
```

---

## Effectively Final Details

The Java Language Specification (JLS §14.20) states that the exception
parameter in a multi-catch block is implicitly `final`. This means:

- You cannot assign to it (`e = null` is a compile error).
- You can use it in lambdas and method references (it is effectively final).
- The same restriction applies to single-catch parameters since Java 8.

```java
catch (IOException | SQLException e) {
    Runnable r = () -> System.out.println(e.getMessage()); // OK
}
```

---

## Multi-Catch with Resource Declarations

Multi-catch works seamlessly with try-with-resources:

```java
try (
    var in = new FileInputStream("a.txt");
    var out = new FileOutputStream("b.txt")
) {
    transfer(in, out);
} catch (IOException | SecurityException e) {
    LOG.error("Transfer failed", e);
}
```

---

## Compiler Warnings and Errors

| Error | Cause |
|-------|-------|
| `expecting ','` | Used comma instead of pipe |
| `Types in multi-catch must be disjoint` | One type extends another |
| `Variable 'e' is already defined` | Duplicate type in the list |
| `The parameter 'e' must be effectively final` | Attempted reassignment |

---

## Summary

| Aspect | Detail |
|--------|--------|
| Introduced | Java 7 (JSR 334) |
| Syntax | `catch (A \| B \| C e)` |
| Variable | Effectively final |
| Bytecode | Synthetic exception class |
| Performance | Same as individual catch blocks |
| Best for | Related exceptions, identical handling |
| Avoid when | Different recovery logic needed |

---

## Key Takeaways

1. Multi-catch reduces boilerplate for identical exception handling.
2. The exception variable is effectively final — no reassignment.
3. The compiler generates a synthetic class; no runtime overhead.
4. Group only semantically related exceptions.
5. Use `instanceof` pattern matching inside the block if you need type-specific
   behavior occasionally.
6. Never silently swallow exceptions caught via multi-catch.
