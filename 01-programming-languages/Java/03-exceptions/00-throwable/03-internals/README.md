# 03 - Internals: How Throwable Works in the JVM

## Scope

This topic dives into the JVM-level implementation of `java.lang.Throwable`, covering how the JVM creates Throwable objects, captures stack traces, and manages the backtrace pointer chain. This is essential for understanding performance characteristics and debugging tools.

## Why It Exists

Understanding Throwable internals is critical for:

- Diagnosing performance issues caused by excessive exception creation
- Understanding why stack trace capture is expensive
- Knowing how profilers and debuggers extract stack traces from Throwable objects
- Building high-performance exception handling in frameworks

## Design Rationale

The JVM's Throwable implementation balances several concerns:

1. **Diagnostic fidelity**: Full stack traces are invaluable for debugging
2. **Performance**: Capturing the stack is the most expensive part of exception creation
3. **Memory**: Stack trace data must be retained without preventing garbage collection
4. **Serialization**: Stack traces must survive serialization across JVM instances
5. **Native interop**: The JVM's native code must be able to walk the same stack

## How the JVM Creates a Throwable

When you write `new RuntimeException("msg")`, the following happens:

```
1. JVM allocates memory for the RuntimeException object
2. Object header is initialized (mark word + klass pointer)
3. Constructor call: super("msg") → Throwable(String message)
4. Throwable constructor calls: fillInStackTrace()  ← native method
5. fillInStackTrace() walks the Java stack from bottom to top
6. For each frame, a StackTraceElement is created
7. The stackTrace array is populated
8. detailMessage field is set from the constructor argument
9. Object reference is returned
```

The critical step is #4-6. `fillInStackTrace()` is a `synchronized native` method that:

1. Acquires the thread's current stack lock
2. Walks the native stack frames to find Java frames
3. For each Java frame, extracts: class name, method name, file name, line number
4. Stores this information in a compact array of `StackTraceElement` objects
5. Returns `this` (the Throwable itself)

## Stack Trace Capture Mechanism

### Native Implementation

The `fillInStackTrace()` method is implemented in the JVM's C++ code. The core logic lives in `share/vm/runtime/thread.cpp` and `share/vm/oops/instanceKlass.cpp`.

```
fillInStackTrace() entry point
    │
    ├── Lock thread's stack lock (synchronized)
    │
    ├── Walk stack frames from bottom to top
    │   │
    │   ├── For each frame:
    │   │   ├── Extract method metadata from metadata pointers
    │   │   ├── Determine source file from class metadata
    │   │   ├── Determine line number from line number table
    │   │   └── Create StackTraceElement object
    │   │
    │   └── Store all elements in stackTrace[] array
    │
    └── Return this
```

### Backtrace Pointer Chain

Internally, each Java stack frame has a pointer to the previous frame. The JVM maintains this as a doubly-linked list called the "backtrace":

```
Thread stack:
┌──────────────┐
│ Frame 0      │ ← bottom (native/VM frames)
│ backtrace: ──┼──→ Frame 1
├──────────────┤
│ Frame 1      │
│ backtrace: ──┼──→ Frame 2
├──────────────┤
│ Frame 2      │ ← top (current execution point)
│ backtrace: ──┼──→ null
└──────────────┘
```

When `fillInStackTrace()` runs, it walks this chain and converts each frame's metadata into a `StackTraceElement`. The Throwable object then holds a reference to the array, which keeps the stack frame data alive until the Throwable is garbage collected.

### Thread Safety

`fillInStackTrace()` is `synchronized` because:

1. The stack is mutable while the thread is executing
2. Another thread might be inspecting the stack (debugger, profiler)
3. The synchronization ensures a consistent snapshot

This is why `Throwable` constructors are not `synchronized` — only the stack trace capture needs synchronization.

## Throwable Object Layout in Memory

```
Throwable object (64-bit JVM, compressed oops):
┌─────────────────────────────────────────────────────────┐
│ Object Header (16 bytes)                                │
│   ├─ Mark Word (8 bytes): lock state, hashCode, age    │
│   └─ Klass Pointer (4 bytes): → RuntimeException class │
│   └─ Padding (4 bytes)                                 │
├─────────────────────────────────────────────────────────┤
│ Java Fields                                             │
│   ├─ detailMessage (4 bytes): → String "msg"           │
│   ├─ cause (4 bytes): → Throwable or null              │
│   ├─ stackTrace (4 bytes): → StackTraceElement[]       │
│   └─ suppressedExceptions (4 bytes): → List or null    │
├─────────────────────────────────────────────────────────┤
│ Total: 32 bytes (object header + 4 reference fields)   │
└─────────────────────────────────────────────────────────┘
         │
         ├──→ String object
         │      ├─ header (16 bytes)
         │      ├─ hash (4 bytes)
         │      ├─ value (4 bytes) → char[] or byte[]
         │      └─ Total: ~24 bytes + char[] array
         │
         ├──→ StackTraceElement[] array
         │      ├─ header (16 bytes)
         │      ├─ length (4 bytes)
         │      └─ elements (4 bytes each)
         │         └─ Each StackTraceElement:
         │              ├─ header (16 bytes)
         │              ├─ declaringClass (4 bytes)
         │              ├─ methodName (4 bytes)
         │              ├─ fileName (4 bytes)
         │              └─ lineNumber (4 bytes)
         │              └─ Total: 32 bytes each
         │
         └──→ List<Throwable> (suppressed exceptions)
                └─ Typically empty, ArrayList or null
```

## JVM Internal Fields

| Field | Type | Description |
|---|---|---|
| `detailMessage` | `String` | The human-readable message, set via constructor |
| `cause` | `Throwable` | The underlying cause, set via `initCause()` |
| `stackTrace` | `StackTraceElement[]` | Captured stack trace, set by `fillInStackTrace()` |
| `suppressedExceptions` | `TransientList<Throwable>` | Suppressed exceptions from try-with-resources |

### Field Access Patterns

- `detailMessage` is accessed by `getMessage()` and `getLocalizedMessage()`
- `cause` is accessed by `getCause()` and `initCause()`
- `stackTrace` is accessed by `getStackTrace()`, `setStackTrace()`, and `fillInStackTrace()`
- `suppressedExceptions` is accessed by `addSuppressed()` and `getSuppressed()`

## Performance Cost of Stack Trace Capture

### Measurement

The cost of `fillInStackTrace()` scales linearly with stack depth:

| Stack Depth | Typical Cost | Notes |
|---|---|---|
| 5 frames | ~1 μs | Simple call chain |
| 20 frames | ~3-5 μs | Typical web application |
| 50 frames | ~10-15 μs | Deep recursion or framework-heavy code |
| 100+ frames | ~20+ μs | Recursive algorithms |

### Impact on Hot Paths

Creating exceptions in a tight loop can reduce throughput by 10-100x:

```java
// BAD: 100x slower than returning a value
for (int i = 0; i < 1_000_000; i++) {
    try {
        if (i % 1000 == 0) {
            throw new RuntimeException("checkpoint");
        }
    } catch (RuntimeException e) {
        // swallowed
    }
}
```

### Mitigations

1. **Override fillInStackTrace()**: Return `this` without capturing the stack:

```java
public class FastException extends RuntimeException {
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this; // skip stack trace capture
    }
}
```

2. **Use stack trace only when needed**: Lazy capture, or use a flag:

```java
public class ConditionalException extends RuntimeException {
    private final boolean captureStack;

    public ConditionalException(String msg, boolean captureStack) {
        super(msg);
        this.captureStack = captureStack;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return captureStack ? super.fillInStackTrace() : this;
    }
}
```

3. **Avoid exceptions for control flow**: Use return values or Optional instead.

## When to Override fillInStackTrace

| Scenario | Override? | Why |
|---|---|---|
| Application exceptions | No | Stack trace is valuable for debugging |
| High-throughput frameworks | Maybe | Performance-critical paths may need fast exceptions |
| Signal-only exceptions | Yes | If stack trace is never used, skip it |
| Custom exception types | Consider | If the type is always caught and never logged with stack |

## Common Pitfalls

### 1. Assuming Stack Trace Is Free

Every `new Exception()` captures a full stack trace. In hot loops, this dominates CPU time.

### 2. Calling fillInStackTrace Twice

The method can be called multiple times, but each call re-walks the entire stack. Only the last call's result is stored.

### 3. Holding Stack Trace References Indefinitely

The `stackTrace` array holds references to class metadata and source file names. Keeping Throwable objects alive prevents this metadata from being garbage collected.

### 4. Serialization Without Stack Trace

When a Throwable is deserialized, the stack trace is deserialized too. If the original Throwable had no stack trace (e.g., overridden `fillInStackTrace`), the deserialized version will have an empty array.

## Production Patterns

### Pattern 1: Fast Exceptions for Signal Use

```java
public class SignalException extends RuntimeException {
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
```

Use for exceptions that signal a condition but whose stack trace is irrelevant.

### Pattern 2: Deferred Stack Trace Capture

```java
public class LazyException extends RuntimeException {
    private volatile StackTraceElement[] stackTrace;

    @Override
    public synchronized Throwable fillInStackTrace() {
        stackTrace = null; // defer capture
        return this;
    }

    @Override
    public synchronized StackTraceElement[] getStackTrace() {
        if (stackTrace == null) {
            stackTrace = super.getStackTrace();
        }
        return stackTrace.clone();
    }
}
```

### Pattern 3: Stack Trace Filtering

```java
public class FilteredException extends RuntimeException {
    private static final String IGNORE_PREFIX = "sun.reflect";

    @Override
    public StackTraceElement[] getStackTrace() {
        return Arrays.stream(super.getStackTrace())
            .filter(e -> !e.getClassName().startsWith(IGNORE_PREFIX))
            .toArray(StackTraceElement[]::new);
    }
}
```

## Summary

| Concept | Key Takeaway |
|---|---|
| fillInStackTrace | Native method, walks Java stack, captures frames |
| Backtrace chain | Doubly-linked list of stack frames maintained by JVM |
| Performance cost | Linear with stack depth; microseconds to milliseconds |
| Memory cost | 32 bytes per StackTraceElement + array overhead |
| Thread safety | fillInStackTrace is synchronized on the Throwable instance |
| Override | Return `this` to skip stack trace capture for performance |
| Pitfall | Exception creation in hot paths destroys throughput |
| Production | Use fast exceptions for signal-only patterns |
