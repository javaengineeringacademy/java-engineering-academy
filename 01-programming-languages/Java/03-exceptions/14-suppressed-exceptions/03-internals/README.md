# Suppressed Exceptions Internals

## Overview

This document explains how the JVM and runtime handle suppressed exceptions
at the internal level.

## How TWR Generates Suppressed Exceptions

### Bytecode Level

When the compiler encounters a try-with-resources statement, it generates
synthetic `finally` blocks that call `close()`. If the `try` block threw
an exception and the `close()` also throws, the compiler generates:

```java
Throwable primary = tryBlockException;
Throwable closeException = closeResource();
if (primary != null) {
    if (closeException != null) {
        primary.addSuppressed(closeException);
    }
    throw primary;
} else {
    throw closeException;
}
```

### Multiple Resources

For multiple resources, the compiler processes them in reverse order.
Each resource's `close()` can produce a suppressed exception independently:

```java
// Resource A close throws, resource B close throws
// Both are added as suppressed to the primary
```

The order of suppression matches the reverse order of resource declarations.

## The Throwable Suppressed List

### Storage

The suppressed exceptions are stored in a transient array on the
`Throwable` class:

```java
private transient Throwable[] suppressedExceptions = ZERO_SERIALIZEABLE_THROWABLE;
```

### Access

- `addSuppressed(Throwable exception)` — adds to the list
- `getSuppressed()` — returns a copy of the list

### Thread Safety

The `suppressedExceptions` array is not synchronized. It is safe to call
`addSuppressed()` from within a single thread. However, if multiple threads
attempt to add suppressed exceptions to the same throwable, the behavior
is undefined and may cause data corruption.

### Self-Suppression

Calling `addSuppressed()` with the same exception as the primary throws
an `IllegalArgumentException`. This prevents infinite recursion.

## Exception Serialization

Suppressed exceptions are serialized as part of the `Throwable` object.
When deserializing, the suppressed exceptions are reconstructed from
the serialized data.

### Java Serialization

The `writeObject` and `readObject` methods on `Throwable` handle
suppressed exceptions. The suppressed list is serialized as an array
of `Throwable` objects.

### Java Serialization Format

```
Throwable
  ├── message (String)
  ├── cause (Throwable)
  ├── suppressed (Throwable[])
  ├── stackTrace (StackTraceElement[])
  └── ...
```

## Performance Considerations

### Stack Trace Generation

Getting the stack trace for suppressed exceptions requires additional
memory and CPU. In high-throughput systems, this can be significant.

### Log Overhead

Logging suppressed exceptions requires iterating over the suppressed
array. If there are many suppressed exceptions, this can be slow.

### Memory Overhead

Each suppressed exception is a separate `Throwable` object with its own
stack trace. In systems with many exceptions, this can consume significant
memory.

## The suppress() Method

Java 9 added the `Throwable.suppress()` method (related to try-with-resources
enhancements). This method is equivalent to `addSuppressed()` but returns
the throwable itself for fluent use:

```java
throw primary.suppress(cleanupException);
```

## Debugging with JVM Flags

### `-XX:+DisableAttachOnDemand`

Disables the attach mechanism, which can affect how suppressed exceptions
are handled in some debugging scenarios.

### `-XX:+ShowCodeDetailsInExceptionMessages`

Shows additional details in exception messages, including suppressed
exception information.

## The Role of the Throwable Class

The `Throwable` class is the root of all exception hierarchies. It
maintains:

1. **message** — the exception message
2. **cause** — the wrapped exception (cause chaining)
3. **suppressed** — the list of suppressed exceptions
4. **stackTrace** — the stack trace array

These are the core data structures that support exception handling
in the JVM.

## Summary

| Aspect | Description |
|--------|-------------|
| Storage | Transient array on Throwable |
| Thread Safety | Not thread-safe for concurrent access |
| Self-Suppression | Prevented by IllegalArgumentException |
| Serialization | Serialized as part of Throwable |
| Performance | Each suppressed exception adds overhead |
| Java 9 | Added `suppress()` fluent method |

---

For more details, see the JLS §14.20.3 and the Throwable source code.
