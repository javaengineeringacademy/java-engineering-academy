# 03 - Exception Chaining Internals

## Table of Contents

1. [Scope](#scope)
2. [The Throwable.cause Field](#the-throwablecause-field)
3. [How initCause() Stores the Cause](#how-initcause-stores-the-cause)
4. [Cause Chain Traversal with getCause()](#cause-chain-traversal-with-getcause)
5. [Memory Layout of Chained Exceptions](#memory-layout-of-chained-exceptions)
6. [fillInStackTrace() and Chaining](#fillinstacktrace-and-chaining)
7. [Performance Cost of Deep Chains](#performance-cost-of-deep-chains)
8. [Internal State and Thread Safety](#internal-state-and-thread-safety)
9. [Stack Trace Population Mechanics](#stack-trace-population-mechanics)
10. [Summary](#summary)

---

## Scope

This document examines how exception chaining works inside the JVM and the `Throwable`
class. It covers the internal data structures, memory layout, and performance
characteristics of chained exceptions. Understanding these internals helps developers
make informed decisions about how deeply to chain exceptions and when to suppress
stack trace collection.

**Prerequisites:** Exception chaining basics (Topic 13, Parts 1-2).

---

## The Throwable.cause Field

The cause chain is stored as a single private field in `java.lang.Throwable`:

```java
private Throwable cause = this;
```

This field defaults to `this` (not `null`) -- a sentinel value indicating that the cause
has not been set. Once a cause is assigned via the constructor or `initCause()`, the field
points to the causal exception. The chain terminates when a `Throwable` has its `cause`
field set to `this` (meaning it has no cause) or to `null` (for exceptions created before
Java 1.4).

**Key characteristics:**

- The field is `private`, so direct access is not possible from outside `Throwable`.
- It is not `final` -- it can be set once after construction via `initCause()`.
- The default value `this` distinguishes "no cause" from "cause not yet checked."

---

## How initCause() Stores the Cause

`initCause()` allows setting the cause after construction. Here is the internal logic:

```java
public synchronized Throwable initCause(Throwable cause) {
    if (this.cause != this) {
        throw new IllegalStateException("Can't overwrite cause");
    }
    if (cause == this) {
        throw new IllegalArgumentException("Self-causal cycle");
    }
    this.cause = cause;
    return this;
}
```

**Rules enforced:**

1. `initCause()` can be called exactly once per `Throwable` instance.
2. The cause cannot be the same object as the exception itself (prevents self-referential cycles).
3. Once set, the cause is immutable for the lifetime of the exception.

**When to use initCause():**

The constructor `new MyException(String, Throwable)` is preferred. `initCause()` exists for
cases where the exception class does not provide a constructor accepting a cause. This
typically happens with legacy exception classes or when using static factory methods:

```java
public static MyException fromCode(int code) {
    MyException ex = new MyException("Error code: " + code);
    ex.initCause(new ErrorCodeException(code));
    return ex;
}
```

---

## Cause Chain Traversal with getCause()

The `getCause()` method is straightforward:

```java
public Throwable getCause() {
    return (cause == this) ? null : cause;
}
```

The sentinel check `(cause == this)` converts the internal representation into the
public API contract: return `null` when no cause exists. Traversal is a simple linked
list walk:

```java
Throwable current = exception;
while (current != null) {
    process(current);
    current = current.getCause();
}
```

The chain is always a **singly-linked list** -- each exception points to exactly one
cause. There are no branching paths or DAGs in the standard implementation. The chain
is linear from the outermost exception to the root cause.

**Chain depth in practice:**

- Typical applications: 1-3 levels
- Framework-heavy applications: 3-5 levels
- Pathological cases: 10+ levels (usually a design problem)

---

## Memory Layout of Chained Exceptions

Each `Throwable` in the chain is a separate object on the JVM heap. The memory cost
includes:

**Per-exception overhead:**

| Component | Description |
|-----------|-------------|
| Object header | 16 bytes (compressed oops on 64-bit JVM) |
| `detailMessage` | String reference (8 bytes compressed) |
| `cause` | Throwable reference (8 bytes compressed) |
| `stackTrace` | StackTraceElement array reference (8 bytes compressed) |
| `suppressedExceptions` | List reference (8 bytes compressed) |
| Padding | Varies by JVM alignment |

A minimal exception with a message and a cause consumes roughly 64-80 bytes of heap.
The `stackTrace` array (populated by `fillInStackTrace()`) can be significantly larger:
each `StackTraceElement` holds class name, method name, file name, and line number
references.

**Chained exception memory cost:**

For a chain of N exceptions, each with a full stack trace of depth D:

- Exception objects: N x ~72 bytes
- Stack trace arrays: N x D x ~32 bytes per StackTraceElement
- Total approximate cost: N x (72 + D x 32) bytes

A chain of 5 exceptions with 30-frame stack traces costs roughly:

5 x (72 + 30 x 32) = 5 x 1032 = 5160 bytes

In most applications this is negligible, but in tight loops or high-throughput systems
that generate many chained exceptions, the allocation pressure matters.

---

## fillInStackTrace() and Chaining

`fillInStackTrace()` is the method that captures the current execution stack into
the exception's `stackTrace` field. It interacts with chaining in two important ways.

**1. Each exception captures its own stack independently.**

When you throw a wrapped exception, only the outermost exception's stack trace is
relevant at the throw site. The inner cause retains the stack trace from where it
was originally created:

```
ServiceException (created at Service.java:42)
  stackTrace: Service.process(), Main.main(), ...

  Caused by: IOException (created at DataReader.java:15)
    stackTrace: DataReader.read(), Service.process(), ...
```

The two stack traces overlap in the middle (the call sites where wrapping occurred).
This is expected and useful -- it shows both where the error originated and where it
was translated.

**2. fillInStackTrace() is expensive and can be suppressed.**

The four-argument constructor allows disabling stack trace collection:

```java
public MyException(String message, Throwable cause,
                   boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
}
```

When `writableStackTrace` is `false`, `fillInStackTrace()` is skipped entirely. The
exception still holds its cause chain but has no stack trace. This is useful in
performance-critical paths where the exception is used purely as a control flow signal.

**Suppressed exceptions and chaining:**

The `suppressedExceptions` list is separate from the cause chain. When using
try-with-resources, exceptions thrown from `close()` are added to this list rather
than forming a second cause chain:

```
PrimaryException
  |- cause: CauseException
  |- suppressed: CloseException
```

---

## Performance Cost of Deep Chains

Deep exception chains have measurable costs in three areas.

**1. Construction time.**

Each exception in the chain calls `fillInStackTrace()`, which walks the JVM call stack.
This is the most expensive part of exception creation. A single exception with a
30-frame stack trace takes 1-10 microseconds depending on the JVM and stack depth.
With N chained exceptions, you pay this cost N times.

**2. Stack trace printing.**

`printStackTrace()` traverses the full chain and prints each stack trace. For deeply
chained exceptions, this produces large amounts of output. In log files, this can
obscure the actual error.

**3. Garbage collection pressure.**

Each exception object and its stack trace array become garbage once the exception is
handled. Deep chains create many short-lived objects. In tight loops that catch and
rethrow exceptions, this adds GC overhead.

**Mitigation strategies:**

| Strategy | Trade-off |
|----------|-----------|
| Limit chain depth to 3-5 levels | May lose context in complex systems |
| Use `writableStackTrace=false` for control flow | Loses debugging information |
| Flatten the chain at boundaries | Loses some context |
| Cache and reuse exception types | Not always possible with checked exceptions |

**Benchmarks (approximate):**

- 1-level chain: 1-3 microseconds to create and print
- 5-level chain: 5-15 microseconds to create and print
- 10-level chain: 10-30 microseconds to create and print

These numbers scale roughly linearly with chain depth.

---

## Internal State and Thread Safety

The `Throwable` class uses `synchronized` on `initCause()` and `getStackTrace()` to
provide basic thread safety. However, the cause chain itself is not designed for
concurrent modification -- once constructed, it is effectively immutable.

**Thread safety guarantees:**

- `initCause()` is synchronized -- safe to call from any thread (but only once).
- `getCause()` is not synchronized, but reads a reference that does not change after
  construction, so it is safe in practice.
- `fillInStackTrace()` is synchronized -- captures a snapshot of the current thread's
  stack.
- `getStackTrace()` and `setStackTrace()` are synchronized.

**Practical implication:** A `Throwable` object can be safely published to another thread
after construction. The cause chain and stack trace are stable once the constructor
returns (assuming `initCause()` was called in the constructor or not at all).

---

## Stack Trace Population Mechanics

When `fillInStackTrace()` executes, the JVM captures the call stack using native code.
The process works as follows:

1. The JVM walks the current thread's call stack from the most recent frame inward.
2. For each frame, it captures the declaring class, method name, file name, and line
   number.
3. The frames are stored in a `StackTraceElement[]` array assigned to the `stackTrace`
   field.
4. Frames belonging to the `Throwable` class itself (the `fillInStackTrace` call) are
   excluded from the output.

**The "1 more" annotation:**

When `printStackTrace()` prints a cause chain, it uses the notation "... N more" to
indicate shared frames between the outer and inner exception. This works by comparing
the stack traces of consecutive exceptions in the chain and counting the overlapping
frames at the end of the inner stack trace.

```
com.example.ServiceException: Service failed
    at com.example.Service.process(Service.java:42)
Caused by: com.example.DataAccessException: Data access failed
    at com.example.DataAccess.query(DataAccess.java:15)
    at com.example.Service.process(Service.java:38)
    ... 1 more
```

The "1 more" means one frame (in this case, `Service.process`) is shared between the
two stack traces.

---

## Summary

| Concept | Key Point |
|---------|-----------|
| `Throwable.cause` field | Private, non-final, defaults to `this` as a sentinel |
| `initCause()` | Sets cause once; throws `IllegalStateException` on second call |
| `getCause()` | Returns `null` when cause equals `this` (sentinel check) |
| Chain structure | Singly-linked list, linear from outer to root cause |
| Memory per exception | ~72 bytes header + stack trace array (N frames x ~32 bytes each) |
| `fillInStackTrace()` | Captures JVM call stack; expensive (1-10 microseconds per exception) |
| `writableStackTrace=false` | Suppresses stack trace collection for performance |
| Thread safety | `Throwable` is safe to publish after construction |
| Deep chain cost | Linear scaling of construction time, print time, and GC pressure |
| Best practice | Keep chains shallow (3-5 levels); use `writableStackTrace` for control flow |

---

**See also:**
- [ExceptionChainingInternals.java](ExceptionChainingInternals.java) -- demonstration code
- [Part 1: Exception Chaining Basics](../README.md)
