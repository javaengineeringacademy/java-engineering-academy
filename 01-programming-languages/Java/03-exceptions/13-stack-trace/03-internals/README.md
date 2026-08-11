# Stack Trace Internals

## How fillInStackTrace Works

When a `Throwable` is constructed, the constructor calls `fillInStackTrace()`. This
native method walks the JVM's call stack from the current frame to the bottom
(frame 0), recording each frame's details.

### Step-by-Step

1. **Caller invokes `new Exception()`** — constructor calls `fillInStackTrace()`.
2. **JVM enters native code** — `fillInStackTrace0()` is a JVM intrinsic.
3. **Stack walk begins** — starting from the frame immediately below the Throwable
   constructor, walking downward toward `main` / thread entry.
4. **For each frame**, the JVM records:
   - Class name (from constant pool)
   - Method name (from constant pool)
   - Source file name (from `LineNumberTable` attribute)
   - Line number (from `LineNumberTable` attribute)
5. **A `StackTraceElement[]` is allocated** and stored in the `Throwable` instance.
6. **Method returns `this`** — the Throwable is now fully constructed with its trace.

### Backtrace Pointers

Internally, each stack frame has a **backtrace pointer** — a linked-list structure
that connects frames. The JVM uses these to walk the stack. During GC, backtrace
pointers are updated to handle frame relocation (e.g., when methods are de-optimized
or compiled on-the-fly).

```
Frame N (Throwable constructor)
    ↓ backtrace
Frame N-1 (your method)
    ↓ backtrace
Frame N-2 (caller)
    ↓ backtrace
    ...
Frame 0 (Thread.run / main)
```

### JVM Implementation Details

- The native method is in `java.lang.Throwable` (JDK) and implemented in the HotSpot
  VM source as `JVM_FillInStackTrace`.
- It uses the VM's internal `vframe` (virtual frame) abstraction to walk interpreted,
  compiled, and native frames uniformly.
- On exception creation, the VM may also compute the **backtrace** (the full list of
  frames) separately from the `StackTraceElement[]` — the backtrace is stored for GC
  and re-throw purposes, while the `StackTraceElement[]` is the public API.

---

## Thrown Backtrace vs StackTraceElement[]

The JVM maintains **two** representations:

| Representation          | Purpose                          | Accessible?       |
|-------------------------|----------------------------------|--------------------|
| Backtrace (native)      | GC, re-throw, stack walking      | No (internal)      |
| `StackTraceElement[]`   | Public API for diagnostics       | Yes                |

When `printStackTrace()` is called, the JVM reads the `StackTraceElement[]` — it
does **not** walk the live stack. This means:

- The trace reflects the state at creation time, not at print time.
- Re-throwing an exception preserves the original trace (unless `fillInStackTrace`
  is called again, which only happens if you create a new exception).

---

## fillInStackTrace on Re-throw

A common question: does re-throwing an exception update its stack trace?

**No.** The trace is captured once at construction time. Re-throwing uses the same
trace:

```java
try {
    throw new RuntimeException("oops");
} catch (Exception e) {
    throw e; // Same trace — does NOT capture new frames
}
```

This is by design — the trace represents where the exception was *created*, not where
it was caught and re-thrown.

---

## Suppressed Exceptions (try-with-resources)

Java 7 introduced suppressed exceptions. When an exception is thrown inside a
try-with-resources block and another exception occurs during `close()`, both are
preserved:

```java
try (var resource = openResource()) {
    throw new RuntimeException("primary");
} catch (Exception e) {
    // e has one suppressed: the close() exception
    Throwable[] suppressed = e.getSuppressed();
}
```

Suppressed exceptions have their own full stack traces, captured independently.

---

## fillInStackTrace Performance

The method is `synchronized` and performs a native stack walk. For deep stacks
(1000+ frames), this can take microseconds. The cost scales linearly with stack depth.

### Why synchronized?

Because multiple threads could theoretically share a Throwable (though this is
rare and bad practice), the JVM serializes access to the backtrace pointer during
fill-in.

---

## Custom FillInStackTrace

You can override `fillInStackTrace()` in a subclass:

```java
public class FastException extends RuntimeException {
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this; // No-op — no stack walk
    }
}
```

This is the official mechanism for creating "lightweight" exceptions. The JVM
respects the override and skips the native stack walk entirely.

---

## Summary

- `fillInStackTrace()` is a native JVM method that walks the call stack.
- It builds a `StackTraceElement[]` stored in the `Throwable`.
- The JVM also maintains an internal backtrace for GC purposes.
- Re-throwing does not update the trace — it captures creation-time state.
- Override `fillInStackTrace()` to skip the walk for performance-critical paths.
