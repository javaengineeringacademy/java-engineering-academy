# Decision Tree: Throwable vs Exception vs Error

## Quick Decision Flowchart

```
Do you need to signal an abnormal condition?
│
├─ NO → Use normal return values
│
└─ YES → What kind of condition?
   │
   ├─ Serious JVM/system failure (OOM, SOE, linkage error)
   │  → Use Error (or subclass)
   │
   ├─ Recoverable condition (file not found, invalid input, connection failed)
   │  → Use Exception (or subclass)
   │  │
   │  ├─ Can caller reasonably recover?
   │  │  ├─ YES → Checked Exception (declare in throws)
   │  │  └─ NO  → Unchecked Exception (RuntimeException subclass)
   │  │
   │  └─ Does it represent a programming bug?
   │     ├─ YES → Unchecked Exception (IllegalArgumentException, IllegalStateException)
   │     └─ NO  → Checked Exception (IOException, SQLException)
   │
   └─ You are building framework/infrastructure code
      → Consider Throwable (but prefer Exception)
```

## Comparison Matrix

| Criterion | Throwable | Exception | Error |
|---|---|---|---|
| **Semantic intent** | Root of hierarchy | Recoverable problem | Serious failure |
| **Checked** | Yes | Yes (except RuntimeException) | No |
| **Catch in app code** | Rarely | Yes, often | Usually no |
| **Throw from app code** | No | Yes | No |
| **Rethrow in finally** | Yes | Yes | Yes |
| **Uncaught handler** | Yes | Yes | Yes |
| **Serialization** | Yes | Yes | Yes |
| **Stack trace captured** | Yes | Yes | Yes |
| **Performance cost** | High | High | High |

## When to Use Each

### Use Exception when:
- Reporting a condition that application code can handle
- Signaling invalid input, missing resources, or failed operations
- You want the compiler to enforce that callers handle or declare the exception

### Use Error when:
- Reporting an unrecoverable JVM-level failure
- The JVM itself is in a compromised state
- Continuing execution is unsafe

### Use Throwable when:
- You are writing a generic catch-all handler (thread pools, agents)
- You need to catch `Exception` and `Error` separately but handle them in the same flow
- The API contract requires the broadest possible type (e.g., `Callable.call()`)

## Decision Rules

1. **Default to Exception** — it is the right choice 99% of the time
2. **Never catch Throwable in application code** — catch Exception or Error specifically
3. **Never throw Throwable from application code** — throw a specific Exception subclass
4. **Only use Throwable as a parameter type** in generic frameworks that must handle anything
5. **Preserve the cause chain** regardless of which type you use
