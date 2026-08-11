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

## Engineering Trade-offs

| Decision | Gain | Loss |
|----------|------|------|
| Catching Throwable | Catches everything, simple catch-all | Swallows Errors you should let propagate; masks JVM failures |
| Using Throwable as parameter type | Maximum flexibility for frameworks | Callers lose type safety; harder to reason about what can be thrown |
| Throwing Throwable from API | Covers all failure modes | Violates principle of least surprise; callers cannot distinguish failure types |
| Catching Exception vs Throwable | Keeps JVM errors visible | May miss some throwable conditions in generic handlers |
| Using Error for everything serious | Clear semantic boundary | Some "serious" conditions are recoverable (e.g., OOM from cache); misclassification |

## Common Code Review Comments

- "Why are you catching `Throwable` here? Catch `Exception` and let `Error` propagate."
- "This method declares `throws Throwable` — that's a code smell. Narrow the type."
- "Never throw `Throwable` directly from application code — use a specific exception subclass."
- "If you need to handle both checked and unchecked, catch `Exception` — not `Throwable`."
- "The `Callable` interface returns `Throwable` for flexibility, but your wrapper should translate it."

## Common Production Mistakes

- **Swallowing OutOfMemoryError**: Catching `Throwable` in a thread pool handler and continuing — the JVM is now in an undefined state. Always re-throw `Error` subclasses.
- **Losing stack traces**: Catching `Throwable` and creating a new exception without passing the cause — root cause becomes invisible in production logs.
- **Catching Throwable in finally blocks**: Masks the original exception being thrown from the try/catch block — the real failure is silently replaced.
- **Using Throwable in method signatures**: Forces every caller to catch or declare `Throwable`, destroying the value of checked exceptions and compiler enforcement.

## When to Escalate

- You are designing a framework that must handle arbitrary throwables (e.g., custom class loaders, agent instrumentation).
- You are writing a generic `UncaughtExceptionHandler` or thread pool error handler.
- A production incident involves swallowed `Error` subclasses — escalate to architect to review error handling strategy across the system.
- You need to decide whether a condition is truly an `Error` or a recoverable `Exception` — this is an architectural decision that affects the entire error hierarchy.
