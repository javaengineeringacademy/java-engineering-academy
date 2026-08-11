# Decision Guide: try-catch in Java

## Why This Exists

`try-catch` is Java's primary mechanism for handling checked exceptions at the call site. It forces the programmer to acknowledge that a failure is possible and to decide, explicitly, what to do about it. This guide exists to help engineers make that decision deliberately rather than reflexively.

The central question is not *how* to write a try-catch block. It is *whether* to write one at all, and if so, *where*, *how wide*, and *what to do with the exception once caught*.

---

## 1. When to Use try-catch vs. Letting Exceptions Propagate

### Decision Tree

```
Is the exception recoverable at this layer?
│
├── YES ──→ Can you provide meaningful fallback behavior?
│            │
│            ├── YES ──→ Use try-catch here.
│            │
│            └── NO ───→ Log, wrap, and rethrow.
│
└── NO ────→ Does the caller have better context to handle it?
             │
             ├── YES ──→ Declare it (throws). Let it propagate.
             │
             └── NO ───→ Catch, log, and convert to a runtime
                          exception or return an error result.
```

### Comparison Table

| Scenario | Approach | Rationale |
|---|---|---|
| Parsing user input | `try-catch` at the input boundary | The caller can present a validation error. |
| Database connectivity failure | Declare `throws` | The service layer has retry/failover logic. |
| `InterruptedException` in a thread | Catch and restore interrupt flag | The thread pool manages interruption semantics. |
| `IOException` in a utility method | Declare `throws` | No meaningful recovery; let the caller decide. |
| `NullPointerException` | Do not catch | Fix the bug. This is a programming error. |
| `OutOfMemoryError` | Do not catch | JVM-level failure. Recovery is unreliable. |

### Principles

1. **Catch at the layer that can act on it.** If you catch an exception only to log it and rethrow, you have added noise without value.
2. **Favor checked exceptions for recoverable conditions.** If the caller *must* handle it, the type system should enforce that.
3. **Do not use try-catch for control flow.** It is slower, less readable, and masks intent.
4. **When in doubt, propagate.** A method that declares `throws` is making an honest contract. A method that catches and swallows is not.

---

## 2. Single Catch vs. Multiple Catch vs. Multi-Catch

### Decision Tree

```
Do the exceptions require different handling?
│
├── YES ──→ Are there more than 2 distinct handling paths?
│            │
│            ├── YES ──→ Use multiple catch blocks.
│            │            Order: most specific to most general.
│            │
│            └── NO ───→ Use multiple catch blocks.
│                         (Multi-catch is not appropriate.)
│
└── NO ────→ Do the exceptions share a common superclass?
             │
             ├── YES ──→ Use a single catch with the superclass.
             │
             └── NO ───→ Use multi-catch (Java 7+).
                          Example: catch (A | B e)
```

### Comparison

| Approach | Syntax | When to Use | Drawback |
|---|---|---|---|
| Single catch | `catch (Exception e)` | All exceptions need the same handling. | Overly broad; catches unintended types. |
| Multiple catch | `catch (A e) { } catch (B e) { }` | Different exceptions, different handling. | Verbose; risk of incorrect ordering. |
| Multi-catch | `catch (A \| B e)` | Different exceptions, same handling, no shared superclass. | Cannot combine with variable assignment. |

### Ordering Rule

Catch blocks must be ordered from **most specific** to **most general**. The compiler enforces this. A common mistake is placing `Exception` before a specific subclass:

```java
// WRONG: Compilation error. Exception already covers IOException.
catch (Exception e) { }
catch (IOException e) { }

// CORRECT
catch (IOException e) { }
catch (Exception e) { }
```

---

## 3. When to Use Nested try-catch

### Decision Tree

```
Is the inner operation independent of the outer failure?
│
├── YES ──→ Can the inner operation fail without invalidating
│            the outer operation's result?
│            │
│            ├── YES ──→ Use nested try-catch.
│            │            Example: writing to a fallback file
│            │            when primary write fails.
│            │
│            └── NO ───→ Handle both at the same level.
│
└── NO ────→ Is the outer operation a resource acquisition
             that must be cleaned up?
             │
             ├── YES ──→ Use try-with-resources instead.
             │
             └── NO ───→ Flatten the logic. Nested try-catch
                          is rarely the right structure.
```

### When Nesting Is Appropriate

- **Fallback behavior**: Try primary I/O, catch failure, attempt secondary I/O.
- **Partial cleanup**: Outer block acquires a resource; inner block performs an operation that may fail independently.
- **Contextual wrapping**: Inner catch adds context before rethrowing.

### When to Avoid

- When nesting creates more than two levels of indentation. Refactor into smaller methods.
- When the inner catch does nothing but log. This is better handled at a single point.
- When the nesting exists only to handle multiple unrelated exceptions. Use multiple catch blocks instead.

### Anti-Pattern: Pyramid of Doom

```java
try {
    try {
        try {
            // deep logic
        } catch (IOException e) { }
    } catch (SQLException e) { }
} catch (Exception e) { }
```

This is a code smell. Extract each try-catch into a named method.

---

## 4. When to Rethrow vs. Wrap Exceptions

### Decision Tree

```
Does the current layer have additional context?
│
├── YES ──→ Can you wrap the original exception in a
│            more meaningful exception type?
│            │
│            ├── YES ──→ Wrap and rethrow.
│            │            Use: throw new ServiceException("msg", cause)
│            │
│            └── NO ───→ Rethrow the original.
│
└── NO ────→ Is the original exception a checked exception?
             │
             ├── YES ──→ Declare it. Do not wrap it in a
             │            RuntimeException just to avoid the throws clause.
             │
             └── NO ───→ Rethrow as-is.
```

### Comparison

| Strategy | When to Use | When to Avoid |
|---|---|---|
| **Rethrow** (`throw e`) | The exception is already the right type. The caller expects it. | When you lose context (e.g., the original stack trace is shallow). |
| **Wrap** (`throw new X(msg, e)`) | The current layer adds context. The caller should see a domain-specific exception. | When wrapping obscures the root cause. Never wrap without setting the cause. |
| **Swallow** (empty catch) | Almost never. Only in rare cases like `InterruptedException` where you restore state. | In production code. This hides bugs. |

### Exception Chaining

Always preserve the causal chain:

```java
catch (IOException e) {
    throw new DataAccessException("Failed to load user profile", e);
}
```

The constructor that accepts a `Throwable` as the last argument is essential. Without it, you lose the original stack trace and make debugging significantly harder.

---

## 5. Common Code Review Comments

These are phrases an experienced reviewer will write. Each implies a specific violation.

| Comment | Implied Issue | Fix |
|---|---|---|
| "This catch is too broad." | `catch (Exception e)` when a specific type was intended. | Narrow the catch to the actual exception type. |
| "Swallowed exception." | Empty catch block or catch that only logs without recovery. | Either handle the exception meaningfully or propagate it. |
| "Missing cause in constructor." | `throw new X("msg")` without wrapping the original. | Add the cause: `throw new X("msg", originalException)`. |
| "Catch ordering is wrong." | A general catch precedes a specific one. | Reorder from most specific to most general. |
| "Don't catch RuntimeException." | Catching `NullPointerException`, `IllegalStateException`, etc. | Fix the root cause instead of catching it. |
| "This should be try-with-resources." | Manual `finally` block closing a `Closeable`. | Use `try (resource)` syntax (Java 7+). |
| "Why is this checked?" | A new checked exception that adds no recovery value. | Consider whether a runtime exception is more appropriate. |
| "Lost interrupt." | Catching `InterruptedException` without restoring the flag. | Call `Thread.currentThread().interrupt()`. |

---

## 6. Common Production Mistakes

### Mistake 1: Catching Too Broadly

```java
try {
    service.process(order);
} catch (Exception e) {
    logger.error("Processing failed", e);
    // What about NullPointerException? Should that be handled the same way?
}
```

**Problem**: A `NullPointerException` is a bug, not an expected failure. Catching it alongside `IOException` masks the bug.

**Fix**: Catch the specific checked exception. Let `NullPointerException` propagate and be caught by a global handler or cause a restart.

### Mistake 2: Swallowing Exceptions

```java
try {
    Files.delete(path);
} catch (IOException e) {
    // file will be cleaned up later
}
```

**Problem**: If deletion fails consistently, you have a silent resource leak. The comment is not a strategy.

**Fix**: Log at a level appropriate to the impact. Track the failure. If it truly does not matter, document why in a comment that references a ticket.

### Mistake 3: Losing the Cause

```java
catch (SQLException e) {
    throw new ServiceException("Database error");
    // Original exception is lost.
}
```

**Problem**: The stack trace in `ServiceException` will not contain the SQL state, query, or original stack. Debugging in production becomes impossible.

**Fix**: Always pass the cause: `throw new ServiceException("Database error", e)`.

### Mistake 4: Using try-catch for Flow Control

```java
int value;
try {
    value = Integer.parseInt(input);
} catch (NumberFormatException e) {
    value = 0;
}
```

**Problem**: Exception handling is expensive. The JVM must build a stack trace object. This is orders of magnitude slower than a conditional check.

**Fix**: `if (input.matches("\\d+")) { value = Integer.parseInt(input); } else { value = 0; }`

### Mistake 5: Restoring Interrupts Incorrectly

```java
catch (InterruptedException e) {
    Thread.currentThread().interrupt(); // This is correct...
    throw new RuntimeException(e);     // ...but now the interrupt is consumed.
}
```

**Problem**: If you rethrow after restoring the interrupt, the caller sees the interrupt via the exception, not the flag. This can cause subtle concurrency bugs.

**Fix**: If you rethrow, do not restore the flag. If you do not rethrow, restore it. Not both.

### Mistake 6: Finally Block Modifying Return Value

```java
public int divide(int a, int b) {
    try {
        return a / b;
    } catch (ArithmeticException e) {
        return -1;
    } finally {
        return 0; // Overrides both the normal and exceptional return.
    }
}
```

**Problem**: The `finally` block always executes and its `return` overrides the try/catch return. This is a well-known footgun.

**Fix**: Never return from a `finally` block. Restructure the logic.

---

## 7. Summary: Decision Checklist

Before writing a try-catch, ask:

1. **Is this exception recoverable?** If not, propagate it.
2. **Can I catch a more specific type?** If yes, do so.
3. **Do multiple exceptions need different handling?** Use multiple catch blocks, ordered specific-first.
4. **Do multiple exceptions need the same handling and share no superclass?** Use multi-catch.
5. **Am I adding context?** Wrap the exception with a domain-specific type and preserve the cause.
6. **Am I catching just to log and rethrow?** Remove the try-catch. Let it propagate.
7. **Am I catching `RuntimeException` or `Exception`?** Reconsider. This is almost always too broad.
8. **Am I using try-catch instead of an if-check?** Use a conditional for predictable failures.
9. **Does my `finally` block have a return statement?** Remove it.
10. **Did I preserve the cause chain?** Pass the original exception to the wrapper constructor.
