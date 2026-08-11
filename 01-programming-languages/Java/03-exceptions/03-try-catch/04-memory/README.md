# Exception Memory Layout

## How try-catch Interacts with JVM Memory

When exceptions are thrown and caught, the JVM allocates memory for exception objects on the heap. Understanding this allocation pattern is critical for performance-sensitive code.

## Exception Object Allocation

Every `new Exception()` call allocates an object on the heap. The JVM follows standard object allocation rules:

```
Exception ex = new RuntimeException("error");
```

The above allocates:
1. Object header (12-16 bytes depending on JVM flags)
2. `detailMessage` field reference (4-8 bytes)
3. `cause` field reference (4-8 bytes)
4. `stackTrace` field reference (4-8 bytes)
5. `suppressedExceptions` list reference (4-8 bytes)

Minimum size: ~32 bytes per exception object before string contents and stack trace.

## Stack Trace Storage

The `stackTrace` field is a `StackTraceElement[]` array. Each element contains:

```
String declaringClass   — 4-8 bytes (reference)
String methodName       — 4-8 bytes (reference)
String fileName         — 4-8 bytes (reference)
int    lineNumber       — 4 bytes
```

For a stack with 50 frames, the array itself costs:

```
16 bytes (array header + length) + 50 × 4 bytes (references) = 216 bytes
```

Plus each `StackTraceElement` object:

```
50 × (12 header + 16 fields) = 1400 bytes
```

Plus the string data for class names, method names, and file names. A typical exception with 50 frames uses 4-8 KB total.

## The Cost of fillInStackTrace

The `fillInStackTrace()` method is called during exception construction. It walks the current call stack and records every frame. This is expensive:

- CPU: Each frame requires a native call to walk the stack
- Memory: Every call allocates the `StackTraceElement[]` and element objects
- GC: All this memory becomes garbage after the exception is caught

The stack walking allocates roughly 200-400 bytes per frame. A 100-frame stack means 20-40 KB allocated just for the trace.

## Suppressed Exceptions and Memory Growth

When exceptions are suppressed (via `addSuppressed()`), the suppressed exception object and its stack trace are retained:

```java
try {
    throw new IOException("outer");
} finally {
    suppressed.add(new IOException("inner")); // second allocation
}
```

Both exceptions carry full stack traces. The outer exception holds a reference to the inner one. This doubles the memory cost.

In nested try-with-resources scenarios, a chain of suppressed exceptions can grow linearly:

```
Exception A
  ├── suppressed: Exception B
  │     ├── suppressed: Exception C
  │     │     └── suppressed: Exception D
```

Each link in the chain retains its own stack trace. A 10-deep chain with 50 frames each consumes 40-80 KB.

## Memory-Efficient Exception Patterns

### 1. Avoid Exceptions for Control Flow

Using exceptions as control flow allocates and discards exception objects rapidly:

```java
// Bad: allocates exception on every iteration
while (hasNext) {
    try {
        process(next);
    } catch (Exception e) {
        // control flow
    }
}
```

### 2. Use Pre-allocated Singletons for Common Cases

```java
// Singleton for signaling empty state
private static final NoSuchElementException EMPTY =
    new NoSuchElementException();

public T next() {
    if (!hasNext) throw EMPTY; // no allocation
    return elements[index++];
}
```

The singleton approach avoids allocation but shares the stack trace across all throws. The stack trace reflects the last throw site.

### 3. Disable Stack Trace Collection in Production

```java
public class LightweightException extends RuntimeException {
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this; // skip stack walk
    }
}
```

This eliminates the 200-400 bytes per frame allocation. Stack traces are available only if explicitly captured.

### 4. Cache Exception Objects for Repeated Conditions

```java
private static final IOException FILE_NOT_FOUND =
    new IOException("file not found");

throw FILE_NOT_FOUND; // zero allocation per throw
```

This trades stack trace accuracy for memory efficiency.

## JVM Internals: Exception Object Lifecycle

1. **Allocation**: Object allocated on Eden space (young generation)
2. **Stack walk**: `fillInStackTrace()` populates `StackTraceElement[]`
3. **Throw**: Exception reference pushed to operand stack
4. **Catch**: Handler pops exception, stack unwinds
5. **GC**: Exception object becomes unreachable, collected in next minor GC

Exceptions that escape to the top of the stack and are logged may be promoted to old generation before collection.

## String Deduplication

The `detailMessage` string and stack trace strings are not deduplicated by the JVM. Two exceptions with the same message create two separate string objects. The `String.intern()` method can reduce this but adds GC pressure on the string pool.

## Measuring Exception Memory

Use JMH with allocation profiling:

```java
@Benchmark
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public void measureExceptionAllocation(Blackhole bh) {
    Exception ex = new RuntimeException("test");
    bh.consume(ex); // prevent dead code elimination
}
```

Run with `-prof gc` to see allocation rates:

```
Benchmark                  Mode  Cnt   Score   Error   Units
measureExceptionAllocation  avgt   20  245.3 ± 12.1  ns/op
·gc.alloc.rate             avgt   20  162.4 ±  8.3  MB/sec
```

The allocation rate shows how much memory exceptions consume under load.

## Summary

Exception memory costs are non-trivial. Each exception object carries stack trace data that grows linearly with call depth. Suppressed exceptions compound this cost. In hot paths, avoiding exception allocation through singletons or disabling stack traces provides measurable memory savings.
