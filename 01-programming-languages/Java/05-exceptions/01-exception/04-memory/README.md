# Memory Layout of Exception Objects

## Scope

This topic analyzes the memory overhead of exception objects in the JVM, including object headers, message strings, cause chains, and stack trace arrays.

## Why It Exists

Exceptions are more expensive than most developers realize. Understanding the memory layout helps you make informed decisions about exception frequency, cause chain depth, and stack trace retention in high-throughput systems.

## Design Rationale

An exception object carries diagnostic data that is rarely used after the catch block. The JVM trades memory for debugging capability. Understanding this trade-off helps you decide when to preserve full diagnostic information and when to strip it.

## Exception Object Overhead

Every Java object has a minimum overhead:

| Component | Size (64-bit JVM) | Description |
|---|---|---|
| Object header | 16 bytes | Mark word (8) + class pointer (8) |
| Alignment | 0-7 bytes | Padded to 8-byte boundary |

For a bare `Exception` with no message, no cause, and no stack trace:

- Object header: 16 bytes
- No additional fields (Exception has no fields of its own)
- Total: 16 bytes

## Message String Storage

When you pass a message to the constructor:

```java
new Exception("Disk full")
```

The memory layout adds:

| Component | Size | Description |
|---|---|---|
| Message String reference | 8 bytes | Pointer to String object |
| String object | 40+ bytes | Object header (16) + char[]/byte[] reference (8) + hash (4) + padding + coder (1) |
| char[] or byte[] | 32+ bytes | Object header (16) + length (4) + padding + data (2 bytes per char) |

For a 10-character message: ~100 bytes total for the message chain.

## Cause Chain Memory Cost

Each exception in a cause chain is a full object. For a chain of depth N:

```
Exception (N=3) memory:
  Outer exception:  16 (header) + 8 (message ref) + 8 (cause ref) + 8 (stackTrace ref) + 8 (suppressed ref) = ~48 bytes
  + Message:        ~100 bytes
  + Middle exception: ~48 bytes + message
  + Inner exception:  ~48 bytes + message
```

A chain of depth 3 with short messages: approximately 500-700 bytes.

## Stack Trace Array Memory

This is the largest component. The stack trace is stored as:

```java
private volatile StackTraceElement[] stackTrace;
```

Each `StackTraceElement` contains:

| Field | Size | Description |
|---|---|---|
| Object header | 16 bytes | Standard object overhead |
| declaringClass | 8 bytes | Reference to String |
| methodName | 8 bytes | Reference to String |
| fileName | 8 bytes | Reference to String (may be null) |
| lineNumber | 4 bytes | int (may be -1 for native) |
| Alignment | 4 bytes | Padding |

Plus the String objects for each field name.

For a typical stack depth of 30 frames:

```
StackTraceElement[]: 16 (header) + 4 (length) + 30 * 8 (references) = 260 bytes
Each StackTraceElement: ~64 bytes * 30 = 1,920 bytes
String objects: ~50 bytes * 30 * 3 (class, method, file) = ~4,500 bytes (shared, so not all unique)
```

Total stack trace: approximately 2-5 KB depending on method name lengths.

## Memory Implications of Deep Cause Chains

### The Problem

Deep cause chains multiply the memory cost. Each exception in the chain carries its own stack trace (by default).

```
Exception depth 10:
  10 exception objects * ~48 bytes = 480 bytes
  10 stack traces * ~3 KB = 30 KB
  10 message strings * ~100 bytes = 1 KB
  Total: ~31.5 KB for a single exception
```

### Mitigation Strategies

1. **Override fillInStackTrace()**: Return `this` without filling the trace for exceptions where stack traces are not needed:

```java
public class FastException extends Exception {
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
```

2. **Strip stack traces at boundaries**: When translating exceptions between layers, consider clearing the stack trace of the original:

```java
original.setStackTrace(new StackTraceElement[0]);
```

3. **Limit cause chain depth**: Keep cause chains shallow. Log intermediate exceptions rather than chaining deeply.

4. **Use suppressed exceptions carefully**: Try-with-resources can add suppressed exceptions, increasing the chain length.

## Summary

- A bare Exception is 16 bytes; with message and stack trace, 2-5 KB.
- Stack traces are the largest memory component.
- Deep cause chains multiply memory cost significantly.
- Override fillInStackTrace() for performance-critical exception paths.
- Strip stack traces at architectural boundaries when full traces are not needed.
