# RuntimeException Memory Management

## Memory Layout of RuntimeException Objects

### Object Header

Every RuntimeException object on the Java heap consists of:

1. **Object header (16 bytes on 64-bit JVM)**:
   - Mark word (8 bytes): Contains identity hash code, GC age, lock state
   - Klass pointer (8 bytes): Points to the class metadata in Metaspace

2. **Instance fields** (inherited from Throwable):
   - `detailMessage` (reference, 8 bytes): Points to the message string
   - `cause` (reference, 8 bytes): Points to the cause Throwable
   - `stackTrace` (reference, 8 bytes): Points to the StackTraceElement array
   - `suppressedExceptions` (reference, 8 bytes): Points to suppressed exceptions list

3. **Total base size**: 48 bytes for the RuntimeException object itself (excluding the message string and stack trace array).

### Throwable Field Layout

The Throwable class declares these instance fields:

```java
private String message;
private Throwable cause;
private StackTraceElement[] stackTrace;
private transient List<Throwable> suppressedExceptions = Collections.emptyList();
```

These fields are stored in declaration order in the object layout. The JVM may reorder fields for alignment, but the standard layout follows declaration order.

### String and Array Overhead

The message string adds:
- String object header: 16 bytes
- `value` reference: 8 bytes
- `hash` field: 4 bytes
- `coder` field: 1 byte
- Padding: 3 bytes
- **Total String overhead**: ~32 bytes + character data

The stack trace array adds:
- Array header: 16 bytes
- Length field: 4 bytes
- Padding: 4 bytes
- Per-element reference: 8 bytes
- **Total for N frames**: ~24 + (8 * N) bytes

### Complete Memory Footprint

For a RuntimeException with a 50-character message and 20 stack frames:

| Component | Size |
|-----------|------|
| RuntimeException object | 48 bytes |
| Message String | 82 bytes (32 + 50 chars) |
| StackTraceElement array | 184 bytes (24 + 8*20) |
| StackTraceElement objects (20) | 20 * 56 = 1120 bytes |
| **Total** | ~1434 bytes |

This does not include the internal string data for class names, method names, and file names within each StackTraceElement.

## Stack Trace Memory Allocation

### StackTraceElement Structure

Each StackTraceElement contains four String fields:

```java
private String declaringClass;
private String methodName;
private String fileName;
private int lineNumber;
```

The memory cost per StackTraceElement:

| Field | Size |
|-------|------|
| Object header | 16 bytes |
| declaringClass reference | 8 bytes |
| methodName reference | 8 bytes |
| fileName reference | 8 bytes |
| lineNumber (int) | 4 bytes |
| Padding | 4 bytes |
| **Total per element** | 48 bytes |

Plus the string data for each field. In practice, class names and method names are often interned, reducing per-element cost.

### Stack Trace Array Allocation

When `fillInStackTrace()` is called, the JVM allocates:

1. A `StackTraceElement[]` array of the stack depth
2. Individual `StackTraceElement` objects for each frame
3. String objects for class names, method names, and file names

The array allocation happens in a single contiguous block. Individual elements are allocated separately on the heap.

### Native Stack Walk Cost

The native `fillInStackTrace()` implementation:

1. Reads the current thread's stack pointer
2. Walks each frame from top to bottom
3. Extracts class, method, file, and line information
4. Creates StackTraceElement objects for each frame

This involves JNI calls and object allocation. The cost scales linearly with stack depth.

### Shallow vs Deep Copy

When an exception is rethrown, the stack trace is copied:

- **Shallow copy**: The same StackTraceElement objects are shared
- **Deep copy**: New StackTraceElement objects are created

The standard `fillInStackTrace()` creates a new array but may reuse String objects through interning.

## Unchecked Exceptions and Heap Impact

### Allocation Patterns

Unchecked exceptions follow allocation patterns similar to any Java object:

1. **Young generation allocation**: New exceptions are allocated in Eden space
2. **Minor GC collection**: Surviving exceptions are promoted to Survivor space
3. **Major GC collection**: Long-lived exceptions move to Old generation

For exceptions that are thrown and caught within a method, they typically die in Eden and are collected by minor GC.

### Exception Chaining Memory

Exception chaining creates a linked structure of Throwable objects:

```
RuntimeException (48 bytes)
  └── cause: RuntimeException (48 bytes)
        └── cause: IllegalArgumentException (48 bytes)
              └── cause: IllegalStateException (48 bytes)
```

Each chained exception adds its own object overhead, message string, and stack trace. A chain of 5 exceptions can consume 7000+ bytes.

### Suppressed Exceptions

The `suppressedExceptions` list adds overhead:

- ArrayList object: 40 bytes
- Internal array: 16 + (8 * capacity) bytes
- Per suppressed exception: Full exception object size

### Memory Pressure from Exception-Heavy Code

In code that throws exceptions frequently:

1. **Eden space fills quickly**: Each exception allocates 1000+ bytes
2. **GC pressure increases**: More frequent minor GC cycles
3. **Allocation rate spikes**: Can trigger GC thrashing
4. **Cache pollution**: Exception objects pollute CPU caches

### Generational Collection Benefits

Exceptions that are caught immediately benefit from generational collection:

- Short-lived objects are collected efficiently in young generation
- No tenuring overhead for exceptions that don't survive a GC cycle
- Eden allocation is fast (bump-the-pointer)

## Memory-Efficient Patterns

### Pattern 1: Stack Trace Suppression

Override `fillInStackTrace()` to avoid stack trace allocation:

```java
public class LightweightException extends RuntimeException {
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
```

This saves the cost of stack trace allocation and native stack walking.

### Pattern 2: Exception Caching

Cache frequently thrown exceptions to avoid repeated allocation:

```java
public class CachedExceptions {
    private static final IllegalArgumentException INVALID_STATE =
            new IllegalArgumentException("Invalid state");

    public static void throwInvalidState() {
        throw INVALID_STATE;
    }
}
```

Note: Cached exceptions lose their stack trace on subsequent throws unless `fillInStackTrace()` is overridden.

### Pattern 3: Avoid Exception Chaining for Performance

Exception chaining adds memory overhead. For performance-critical code, consider:

```java
// Instead of chaining:
throw new RuntimeException("Failed", cause);

// Use a single exception with a descriptive message:
throw new RuntimeException("Failed: " + cause.getMessage());
```

### Pattern 4: Pre-allocated Exception Templates

For exceptions with dynamic messages, use templates:

```java
public class TemplateException extends RuntimeException {
    private final String template;

    public TemplateException(String template) {
        super(template);
        this.template = template;
    }

    public TemplateException withArgs(Object... args) {
        return new TemplateException(String.format(template, args));
    }
}
```

### Pattern 5: Avoid Exceptions for Control Flow

The most memory-efficient pattern is to avoid exceptions entirely:

```java
// Inefficient - allocates exception object:
try {
    return map.get(key);
} catch (NullPointerException e) {
    return defaultValue;
}

// Efficient - no allocation:
if (map.containsKey(key)) {
    return map.get(key);
}
return defaultValue;
```

### Pattern 6: Use Primitive Checks Before Throwing

Validate conditions before creating exceptions:

```java
public void process(int[] array, int index) {
    // Check before throwing
    if (index < 0 || index >= array.length) {
        throw new ArrayIndexOutOfBoundsException(index);
    }
    // Process array
}
```

### Pattern 7: Limit Exception Depth

Keep exception chains shallow to reduce memory overhead:

```java
// Shallow chain:
throw new RuntimeException("Root cause");

// Deep chain (expensive):
RuntimeException root = new RuntimeException("Root");
RuntimeException mid = new RuntimeException("Middle", root);
throw new RuntimeException("Top", mid);
```

## Monitoring and Profiling

### JFR Event Tracking

Java Flight Recorder tracks exception events:

- `jdk.ThrowableThrow`: Records exception creation with stack trace
- `jdk.ExceptionThrow`: Records exception throw locations
- `jdk.JavaExceptionThrow`: Records Java exception throws

### Heap Dump Analysis

When analyzing heap dumps for exception-related memory:

1. Search for `java.lang.Throwable` instances
2. Check the `stackTrace` array sizes
3. Look for exception chains (cause references)
4. Identify high-allocation-rate code paths

### GC Root Analysis

Exception objects are typically GC roots through:

- Thread-local exception handlers
- Exception handler table references
- JNI local references

### Memory Metrics

Track these metrics for exception-heavy applications:

- Total Throwable instance count
- Average stack trace depth
- Exception chain depth
- Allocation rate in exceptions per second
- Memory retained by exception objects

## Summary

RuntimeException objects consume significant heap memory due to stack trace capture, string allocations, and object overhead. A single exception can consume 1000+ bytes. Memory-efficient patterns include stack trace suppression, exception caching, avoiding exception chaining, and using exceptions only for exceptional conditions rather than control flow. The generational GC design helps with short-lived exceptions, but high-frequency exception throwing can cause GC pressure and memory pressure.
