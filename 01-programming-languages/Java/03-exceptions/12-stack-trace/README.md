# Stack Trace

## Scope

This topic covers Java stack traces — what they are, how to access and manipulate them,
their performance implications, and production best practices for error reporting and
debugging.

## Why It Exists

Without stack traces, debugging would require setting breakpoints in advance or adding
manual logging at every method boundary. Stack traces give developers a snapshot of the
call chain that led to an exception, making post-mortem analysis possible in production
where interactive debugging is not an option.

## Design Rationale

Java's stack trace model was introduced with the original `Throwable` class. The design
chose to store the trace as an array of `StackTraceElement` objects rather than raw
strings so that:

- Tools (IDEs, log analyzers) can programmatically navigate frames.
- Frames can be filtered, sorted, or reformatted after the fact.
- The trace can be lazily populated (`fillInStackTrace`) or suppressed (overridden with
  `setStackTrace` or empty arrays) when performance matters.

---

## What Is a Stack Trace

A stack trace is a snapshot of the JVM's call stack at a particular point in time.
Every time a method is invoked, a new **stack frame** is pushed onto the calling thread's
stack. When a method returns, its frame is popped. An exception captures the entire
stack at the moment it is created (or when `fillInStackTrace` is called).

Internally, a stack trace is represented as an array of `StackTraceElement` objects — one
per frame. The order is **most-recently-called first** (top of the stack) to
**oldest (main/entry point) last** (bottom of the stack).

```text
Exception in thread "main" java.lang.NullPointerException
    at com.example.Service.handle(Service.java:42)
    at com.example.Controller.process(Controller.java:18)
    at com.example.Main.main(Main.java:7)
```

Each line above is one stack frame.

---

## Stack Frame Layout

```
┌─────────────────────────────────────────────────────────┐
│                      Thread Stack                        │
├─────────────────────────────────────────────────────────┤
│  Top of Stack (most recent call)                        │
│  ┌─────────────────────────────────────────────────┐    │
│  │ Frame 0: Service.handle(Service.java:42)        │ ◄── most recent
│  └─────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────┐    │
│  │ Frame 1: Controller.process(Controller.java:18) │    │
│  └─────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────┐    │
│  │ Frame 2: Main.main(Main.java:7)                 │ ◄── entry point
│  └─────────────────────────────────────────────────┘    │
│  Bottom of Stack (oldest call)                          │
├─────────────────────────────────────────────────────────┤
│  Each StackTraceElement contains:                       │
│  ┌──────────────┐ ┌───────────┐ ┌─────────┐ ┌────────┐ │
│  │ className    │ │methodName │ │fileName │ │lineNum │ │
│  │ (String)     │ │ (String)  │ │ (String)│ │ (int)  │ │
│  └──────────────┘ └───────────┘ └─────────┘ └────────┘ │
└─────────────────────────────────────────────────────────┘
```

## StackTraceElement Fields

`StackTraceElement` is an immutable class with four key fields:

| Field        | Type    | Description                                          |
|--------------|---------|------------------------------------------------------|
| `className`  | String  | Fully qualified class name of the method's owner     |
| `methodName` | String  | Name of the method (not the signature)               |
| `fileName`   | String  | Source file name, or `"Unknown"` / `null`            |
| `lineNumber` | int     | Line number in the source file, or `-2` (native), `-1` (unavailable) |

```java
StackTraceElement frame = stackTrace[0];
frame.getClassName();   // "com.example.Service"
frame.getMethodName();  // "handle"
frame.getFileName();    // "Service.java"
frame.getLineNumber();  // 42
```

### Line Number Conventions

| Value | Meaning                            |
|-------|-------------------------------------|
| `-2`  | Method is a native (JNI) method     |
| `-1`  | Line number unavailable at runtime  |
| `> 0` | Actual line in source file          |

---

## Accessing Stack Trace

### getStackTrace()

Every `Throwable` has a `getStackTrace()` method returning a `StackTraceElement[]`:

```java
try {
    riskyOperation();
} catch (Exception e) {
    StackTraceElement[] frames = e.getStackTrace();
    for (StackTraceElement frame : frames) {
        System.out.println(frame);
    }
}
```

### setStackTrace()

You can replace the stack trace after creation — useful in frameworks or when wrapping
exceptions:

```java
catch (Exception e) {
    MyException wrapper = new MyException("wrapped");
    wrapper.setStackTrace(e.getStackTrace());
    throw wrapper;
}
```

### setStackTrace with empty array

Suppressing the trace entirely is common in performance-sensitive hot paths:

```java
catch (Exception e) {
    // Re-throw without expensive stack trace
    e.setStackTrace(new StackTraceElement[0]);
    throw e;
}
```

---

## Stack Trace Performance Cost

### fillInStackTrace

When a `Throwable` is constructed, its constructor calls `fillInStackTrace()`. This
method walks the JVM stack and allocates a `StackTraceElement` for every frame.
For deep stacks (e.g., recursive algorithms, framework-heavy applications), this can
be expensive:

- **Time**: O(stack depth) — each frame requires reflection-like inspection.
- **Memory**: Each `StackTraceElement` is a separate object (4 fields ≈ 40+ bytes each).

### Suppression Strategies

1. **Override fillInStackTrace**: Make your exception class override the method to
   return `this` immediately.
2. **setStackTrace**: Replace with an empty array after creation.
3. **Pre-built exceptions**: For predictable error conditions, construct once and reuse.

```java
public class LightweightException extends RuntimeException {
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this; // Skip stack walk — very fast
    }
}
```

> **Caveat**: Skipping `fillInStackTrace` means the exception has no diagnostic
> information. Use only when the exception is transient or the stack is irrelevant.

### Benchmarks (approximate)

| Action                          | Time (deep stack) | Notes                  |
|---------------------------------|--------------------|------------------------|
| `new Exception()`               | ~1–5 μs            | Depends on stack depth  |
| `new Exception()` + suppressed  | ~0.01 μs           | Override fillInStackTrace |
| `getStackTrace()`               | ~0.1 μs            | Array is already built  |
| `setStackTrace(new[0])`         | ~0.01 μs           | Trivial                 |

---

## Creating Stack Traces Without Throwing

You can capture the current stack without creating an exception:

```java
StackTraceElement[] current = Thread.currentThread().getStackTrace();
```

This is useful for:

- **Logging**: Record what called a method without throwing.
- **Debugging**: Identify caller context in production.
- **Profiling**: Lightweight call-site tracking.

> **Note**: `getStackTrace()` on a `Thread` has its own performance cost (similar to
> `fillInStackTrace`) — avoid in tight loops.

---

## Stack Trace in Logs

### Format

Standard Java stack trace format:

```
ExceptionType: message
    at package.Class.method(File.java:line)
    at package.Class.method(File.java:line)
    ...
```

### Filtering Internal Frames

Most logging frameworks and IDEs allow hiding frames that are internal to the runtime:

- **IDE**: "Collapse library frames" in IntelliJ / "Hide frames from libraries" in Eclipse
- **Log frameworks**: Custom pattern layouts or filters
- **Manual filtering**: Iterate and skip frames by package prefix

```java
public static StackTraceElement[] filterInternal(StackTraceElement[] trace) {
    return Arrays.stream(trace)
        .filter(f -> !f.getClassName().startsWith("java.lang."))
        .filter(f -> !f.getClassName().startsWith("sun."))
        .toArray(StackTraceElement[]::new);
}
```

### Best Practices for Logs

1. **Include class name, method name, line number** — always.
2. **Limit depth** — 10–20 frames is usually sufficient.
3. **Highlight application frames** — put your code first, library frames after.
4. **Use structured logging** — store frames as data, not just text, for querying.

---

## Common Pitfalls

### 1. Stack Trace Size

Very deep recursion can produce thousands of frames. Each frame is an object, and
`getStackTrace()` returns a full copy. This can cause:

- High memory usage
- Slow serialization to logs
- OutOfMemoryError in pathological cases

### 2. Performance Impact

Creating exceptions for control flow is an anti-pattern. Even with empty stack traces,
the exception constructor, message formatting, and object allocation add overhead.

### 3. Filtering Too Aggressively

Removing frames can hide the root cause. Always keep enough context to understand the
call path.

### 4. Ignoring Causal Chains

`getCause()` often reveals the real problem. Logging only the top-level exception
misses critical information.

### 5. Thread-Unsafe Access

`getStackTrace()` returns a copy, so iterating is safe. But `fillInStackTrace()` is
`synchronized` — concurrent exception creation on the same instance is serialized.

---

## Production Patterns

### Pattern 1: Error Reporting Service

Send structured stack traces to a centralized service (Sentry, Datadog, etc.):

```java
catch (Exception e) {
    ErrorReport report = new ErrorReport.Builder()
        .type(e.getClass().getSimpleName())
        .message(e.getMessage())
        .stackTrace(e.getStackTrace())
        .timestamp(System.currentTimeMillis())
        .build();
    errorService.report(report);
    throw e;
}
```

### Pattern 2: Stack Trace Sampling

In high-throughput systems, log every Nth exception with full trace, and a summary
for the rest:

```java
if (counter++ % 100 == 0) {
    logger.error("Exception", e); // full trace
} else {
    logger.warn("Exception count++ {}: {}", e.getClass().getSimpleName(),
        e.getMessage());
}
```

### Pattern 3: Exception Classification

Analyze stack traces to classify errors:

- **Transient**: Network timeouts, temporary DB locks → retry.
- **Permanent**: Null pointer, illegal argument → alert immediately.
- **Systemic**: OutOfMemoryError, StackOverflowError → scale up or investigate.

### Pattern 4: De-duplication

Hash the top N frames + exception type to group identical failures:

```java
String fingerprint = e.getClass().getName() + ":"
    + Arrays.stream(e.getStackTrace())
        .limit(5)
        .map(StackTraceElement::toString)
        .collect(Collectors.joining("\n"));
```

### Pattern 5: Lazy Stack Traces

For high-volume exceptions (e.g., validation errors in a hot path), skip trace capture:

```java
public class ValidationException extends RuntimeException {
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this; // no stack trace
    }
}
```

---

## Summary

- A stack trace is an array of `StackTraceElement` — one per stack frame.
- `getStackTrace()` returns the trace; `setStackTrace()` replaces it.
- `fillInStackTrace()` is the expensive operation; it walks the JVM stack.
- Skip traces when performance matters; restore them when debugging is needed.
- Filter and format traces for readability in logs.
- In production, use sampling, de-duplication, and structured reporting.
