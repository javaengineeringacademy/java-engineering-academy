# 00 - Throwable in Java

## Scope

This topic covers `java.lang.Throwable`, the root class of the entire Java exception hierarchy. Understanding Throwable is foundational to mastering exception handling, as every exception, error, and custom throwable in Java descends from this single class.

## Why It Exists

Java needed a single root type that could represent any abnormal condition a program might encounter. Before Java, C++ used integer return codes or disparate class hierarchies with no common ancestor. Throwable provides:

1. A unified type for the JVM and libraries to signal abnormal control flow
2. A container for diagnostic information (message, cause, stack trace)
3. A serializable object that can be transmitted across JVM boundaries (RMI, serialization)
4. The contract that the `throw` and `catch` keywords operate against

Without Throwable, the compiler, JVM, and standard library would have no common language for error propagation.

## Design Rationale

Throwable sits at the intersection of several design decisions:

- **Checked vs. unchecked**: Throwable itself is checked, but its subclasses Exception and Error split into two branches. Exception further splits into checked (IOException) and unchecked (RuntimeException).
- **Single inheritance constraint**: Because Java lacks multiple inheritance, Throwable cannot simultaneously be Serializable, Closeable, or Iterable. It chose Serializable, making throwables transmissible across processes.
- **Performance tradeoff**: Capturing a full stack trace on every Throwable creation is expensive. This is why `new Throwable().fillInStackTrace()` is a native call and why frameworks sometimes override `fillInStackTrace()` to return `this` for performance.
- **Immutability of stack trace**: Once captured, the stack trace array is mutable (you can call `setStackTrace()`), but the backtrace chain that the JVM maintains internally is not exposed.

## What Is Throwable

`java.lang.Throwable` is the superclass of all errors and exceptions in the Java language. It was introduced in JDK 1.0.

```java
public class Throwable implements Serializable {
    private String detailMessage;
    private Throwable cause;
    private StackTraceElement[] stackTrace;
    private List<Throwable> suppressedExceptions;
    // ...
}
```

A Throwable object encapsulates:

| Component | Description |
|---|---|
| Message | A human-readable string describing the failure (`detailMessage` field) |
| Cause | The underlying exception that caused this one (`cause` field, nullable) |
| Stack trace | An array of `StackTraceElement` objects representing the call stack at creation time |
| Suppressed exceptions | A list of exceptions that were suppressed in favor of the primary exception (try-with-resources) |
| Cause chain | A linked list of Throwables tracing back to the root cause |

## Characteristics

| Characteristic | Detail |
|---|---|
| Package | `java.lang` |
| Introduced | JDK 1.0 |
| Implements | `Serializable` |
| Thread-safe | Immutable after creation (stack trace array is mutable but not synchronized) |
| Serializable | Yes — `serialVersionUID = -3042686055658047285L` |
| Has message | Yes — `getMessage()`, constructor accepts `String` |
| Has cause | Yes — `getCause()`, constructor accepts `Throwable` (since JDK 1.4) |
| Has stack trace | Yes — `getStackTrace()`, `fillInStackTrace()` (native) |
| Has suppressed | Yes — `addSuppressed()`, `getSuppressed()` (since JDK 7) |

## The Throwable Contract

### Core Methods

```java
// Message
public String getMessage()           // Returns detailMessage, may be null
public String getLocalizedMessage()  // Overridable, defaults to getMessage()

// Cause
public Throwable getCause()          // Returns cause, may be null
public synchronized Throwable initCause(Throwable cause) // Sets cause, once

// Stack trace
public synchronized StackTraceElement[] getStackTrace()
public void setStackTrace(StackTraceElement[] stackTrace)
public synchronized Throwable fillInStackTrace() // Native, captures current stack

// Suppressed exceptions (JDK 7+)
public final synchronized void addSuppressed(Throwable exception)
public final synchronized Throwable[] getSuppressed()

// Output
public void printStackTrace()                        // Prints to System.err
public void printStackTrace(PrintStream s)           // Prints to stream
public void printStackTrace(PrintWriter s)           // Prints to writer
public String toString()                             // ClassName + ": " + message
```

### Lifecycle

```
new Throwable("msg")
    │
    ├── fillInStackTrace()  ← called automatically by JVM in constructors
    │
    ├── initCause(cause)    ← optional, must be called before stack trace
    │
    ├── addSuppressed(ex)   ← called during try-with-resources unwinding
    │
    └── printStackTrace()   ← output to stream
```

### Constraints

- `initCause()` may only be called once. Calling it a second time throws `IllegalStateException`.
- `fillInStackTrace()` is `synchronized` and native. It walks the Java stack frames and populates the `stackTrace` array.
- `getStackTrace()` returns a copy of the internal array, not a reference to it.
- `addSuppressed()` is `final` — subclasses cannot override suppression behavior.

## Throwable vs Exception vs Error

```
                    Throwable
                   /         \
              Exception       Error
             /        \        \
   [checked]    RuntimeException    [unchecked, serious]
       |              |
  IOException    IllegalArgumentException
  SQLException  NullPointerException
                ArrayIndexOutOfBoundsException
```

| Aspect | Throwable | Exception | Error |
|---|---|---|---|
| Intent | Root of hierarchy | Recoverable conditions | Serious JVM failures |
| Checked? | Yes | Yes (except RuntimeException) | No |
| Catch? | Yes | Yes | Yes, but rarely appropriate |
| Example | Framework code | File not found | Out of memory |
| Recovery | Framework decides | Application can recover | Usually unrecoverable |

**Rule of thumb**: Never catch `Throwable` unless you are building infrastructure that must handle all possible failures (e.g., thread pool uncaught exception handlers, agent attachers).

## Stack Trace Anatomy

```
java.lang.RuntimeException: Something went wrong
    at com.example.Service.process(Service.java:42)
    at com.example.Controller.handleRequest(Controller.java:115)
    at java.base/java.lang.Thread.run(Thread.java:829)
    Suppressed: java.io.IOException: stream closed
        at com.example.Resource.close(Resource.java:88)
        at com.example.Service.process(Service.java:38)
```

Each element in the stack trace is a `StackTraceElement`:

```java
public final class StackTraceElement implements Serializable {
    private String declaringClass;
    private String methodName;
    private String fileName;
    private int lineNumber;
}
```

| Field | Meaning |
|---|---|
| `declaringClass` | Fully qualified class name |
| `methodName` | Method name (or "<init>" for constructors) |
| `fileName` | Source file name, or "Unknown Source" |
| `lineNumber` | Line number, or -1 for native methods, -2 for unknown |

### Reading a Stack Trace

- The **top** is where the exception was thrown (most recent call)
- The **bottom** is the thread entry point
- "Native Method" at the bottom means the stack crossed into native code
- "Unknown Source" means debug info was stripped

## When to Use Throwable Directly

**Almost never.** Use Throwable directly only when:

1. **Building exception frameworks** — your library needs to catch and rethrow anything
2. **Uncaught exception handlers** — `Thread.UncaughtExceptionHandler` receives a `Throwable`
3. **Agent or instrumentation code** — must handle arbitrary failures from instrumented code
4. **Serialization frameworks** — need to deserialize and rethrow any type

```java
// Framework pattern: catch everything, log, and rethrow
try {
    delegate.execute(request);
} catch (Throwable t) {
    logger.error("Execution failed", t);
    if (t instanceof Error) {
        throw (Error) t;
    }
    throw new ExecutionException("Delegate failed", t);
}
```

In application code, always catch `Exception` for recoverable conditions or `Error` for unrecoverable JVM failures. Catching `Throwable` hides the distinction and can lead to catching `OutOfMemoryError` or `StackOverflowError` in contexts where recovery is impossible.

## Common Pitfalls

### 1. Swallowing Throwables

```java
// BAD: exception disappears, root cause is lost
try {
    riskyOperation();
} catch (Throwable t) {
    // nothing
}
```

### 2. Catching Throwable Instead of Exception

```java
// BAD: catches OOM, SOE, and other fatal errors
try {
    process();
} catch (Throwable t) {
    handle(t);
}
```

### 3. Throwing Throwable Instead of Exception

```java
// BAD: forces callers to catch Throwable
public void doSomething() throws Throwable { }
```

### 4. Not Preserving the Cause Chain

```java
// BAD: root cause is lost
try {
    riskyOperation();
} catch (IOException e) {
    throw new ServiceException("Failed"); // cause lost!
}

// GOOD: cause preserved
try {
    riskyOperation();
} catch (IOException e) {
    throw new ServiceException("Failed", e);
}
```

### 5. Calling fillInStackTrace Unnecessarily

```java
// WASTEFUL: captures stack trace just to discard it
Throwable t = new Throwable();
t.fillInStackTrace(); // native call, expensive
// never used the stack trace
```

### 6. Holding Throwable References in Collections

```java
// MEMORY LEAK: Throwable holds strong references to stack frame objects
List<Throwable> errors = new ArrayList<>();
while (condition) {
    try {
        riskyOperation();
    } catch (Throwable t) {
        errors.add(t); // each t holds references to its stack frames
    }
}
// Eventually: OutOfMemoryError from accumulated throwables
```

## Production Patterns

### Pattern 1: Exception Rethrower

```java
public static void sneakyThrow(Throwable t) {
    throwableThrow(t); // compile-time trick
}

private static <T extends Throwable> void throwableThrow(Throwable t) throws T {
    throw (T) t;
}
```

Use when you need to throw a checked exception from a context that does not declare it. Use sparingly — this breaks the checked exception contract.

### Pattern 2: Exception Aggregation

```java
public class MultiException extends Exception {
    private final List<Throwable> exceptions;

    public void add(Throwable t) {
        exceptions.add(t);
    }

    public boolean hasExceptions() {
        return !exceptions.isEmpty();
    }
}
```

### Pattern 3: Checked Exception Wrapper

```java
public class UncheckedWrapper extends RuntimeException {
    public UncheckedWrapper(Throwable cause) {
        super(cause);
    }
}
```

Wraps checked exceptions for code that cannot handle them (lambdas, streams).

### Pattern 4: Throwable Filtering

```java
try {
    process();
} catch (Throwable t) {
    if (t instanceof Error) throw (Error) t;
    if (t instanceof RuntimeException) throw (RuntimeException) t;
    throw new WrappedException("Processing failed", (Exception) t);
}
```

### Pattern 5: Suppressed Exception Collection

```java
public class CloseableHelper implements Closeable {
    private final List<Throwable> suppressed = new ArrayList<>();

    @Override
    public void close() throws IOException {
        IOException first = null;
        for (Throwable t : suppressed) {
            if (first == null) {
                first = new IOException(t);
            } else {
                first.addSuppressed(t);
            }
        }
        if (first != null) throw first;
    }

    public void addSuppressed(Throwable t) {
        suppressed.add(t);
    }
}
```

## Summary

| Concept | Key Takeaway |
|---|---|
| What | Root of all Java exceptions and errors |
| Where | `java.lang.Throwable`, JDK 1.0 |
| When to use directly | Almost never — only in framework code |
| Message | `getMessage()` — human-readable description |
| Cause | `getCause()` — the underlying exception |
| Stack trace | `getStackTrace()` — call stack at creation |
| Suppressed | `addSuppressed()` — try-with-resources additions |
| Pitfall | Catching `Throwable` hides fatal errors |
| Production | Preserve cause chains, filter by type, avoid reference accumulation |
