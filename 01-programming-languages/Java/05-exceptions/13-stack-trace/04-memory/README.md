# Stack Trace Memory

## Memory Cost of Stack Traces

Every `StackTraceElement` object contains four fields:

| Field        | Type    | Approximate Size |
|--------------|---------|-------------------|
| `className`  | String  | ~60 bytes         |
| `methodName` | String  | ~40 bytes         |
| `fileName`   | String  | ~40 bytes         |
| `lineNumber` | int     | 16 bytes (boxed)  |
| **Object overhead** | | ~16 bytes |
| **Total per frame** | | **~170 bytes** |

For a stack with 50 frames, a single exception's trace costs approximately
**8.5 KB** of heap memory (just for the `StackTraceElement[]` and its contents).

---

## Lazy vs Eager Population

### Eager (Default)

Java's default behavior is **eager**: `fillInStackTrace()` is called in the
`Throwable` constructor, walking the entire stack and allocating all
`StackTraceElement` objects immediately.

```java
// Eager — trace is fully built at construction time
RuntimeException e = new RuntimeException("error");
StackTraceElement[] trace = e.getStackTrace(); // already populated
```

### Lazy

You can implement **lazy** stack traces by overriding `fillInStackTrace()` to defer
the walk:

```java
public class LazyException extends RuntimeException {
    private volatile StackTraceElement[] lazyTrace;

    @Override
    public synchronized Throwable fillInStackTrace() {
        // Don't walk the stack yet — return this immediately
        return this;
    }

    public StackTraceElement[] getStackTrace() {
        if (lazyTrace == null) {
            // Walk the stack only when someone asks for it
            lazyTrace = super.getStackTrace();
        }
        return lazyTrace;
    }
}
```

This defers the cost until `getStackTrace()` is actually called. If the exception is
caught and handled without reading the trace, the cost is near-zero.

---

## Memory Accumulation

In production systems, exceptions can accumulate:

- **Log buffers**: full traces stored in rolling log files.
- **Error reporting**: traces serialized and sent to external services.
- **Exception lists**: code that collects exceptions in a list for batch processing.
- **Weak references**: if exceptions are held by weak references, they can linger
  until GC runs.

### Mitigation Strategies

1. **Limit trace depth**: truncate to N frames before storage.
2. **Filter internal frames**: reduce per-frame string length.
3. **Use lightweight exceptions**: skip `fillInStackTrace` entirely.
4. **Dispose quickly**: don't hold exceptions in collections longer than needed.
5. **Set maximum trace size**: cap at 50–100 frames in production.

---

## String Interning

`StackTraceElement` stores class names, method names, and file names as separate
`String` objects. These are **not** interned by the JVM. This means:

- Multiple exceptions from the same method each have their own copy of the class
  name string.
- For high-volume exceptions, this creates significant string duplication.

You can reduce this by:
- Interning the most common class names (if you control the exception types).
- Using compact exception formats that store indices instead of full strings.

---

## Impact on Garbage Collection

Each `StackTraceElement[]` and its contained objects are short-lived in most cases
(created, logged, discarded). This creates garbage collection pressure:

- **Young generation**: most traces are allocated here.
- **Promotion**: if traces survive one GC cycle, they move to old generation.
- **Tenured objects**: traces held in error lists or caches survive longer.

For high-throughput services, exception-heavy code paths can cause frequent minor
GCs and occasional major GCs if traces accumulate.

---

## Summary

- Each `StackTraceElement` is ~170 bytes; a 50-frame trace costs ~8.5 KB.
- Default behavior is eager — trace is built at construction time.
- Lazy traces defer the cost until `getStackTrace()` is called.
- Filter, truncate, and use lightweight exceptions to control memory.
- Exception accumulation can stress the GC in production systems.
