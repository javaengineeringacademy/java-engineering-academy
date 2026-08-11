# 04 - Memory: Throwable Memory Layout

## Scope

This topic covers the memory representation of Throwable objects on the JVM, including object headers, field sizes, reference tracking, and memory leak patterns caused by holding Throwable references.

## Why It Exists

Understanding Throwable memory layout helps you:

- Estimate memory overhead of exception-heavy code paths
- Diagnose memory leaks caused by accumulating Throwable objects
- Optimize frameworks that create many exceptions
- Understand why Throwable objects are surprisingly large

## Design Rationale

Throwable objects are heavier than most Java objects because they carry:

1. **Object header**: Standard 16 bytes on 64-bit JVMs (compressed oops)
2. **Multiple reference fields**: message (String), cause (Throwable), stackTrace (array), suppressedExceptions (List)
3. **Stack trace array**: Variable size, typically 5-50 elements, each 32+ bytes
4. **String deduplication**: Message strings may be deduplicated by G1 GC
5. **Cause chain**: Linked list of Throwable objects, each with its own overhead

## Object Header (64-bit JVM)

Every Java object has a header:

| Component | Size | Content |
|---|---|---|
| Mark word | 8 bytes | Lock state, hashCode, GC age |
| Klass pointer | 4 bytes (compressed) | Pointer to class metadata |
| **Total header** | **16 bytes** (with padding) | Aligned to 8-byte boundary |

On 64-bit JVMs with compressed oops (the default), the header is 16 bytes. Without compressed oops, it is 16 bytes (8 + 8) due to 8-byte klass pointer alignment.

## Throwable Field Layout

```
Throwable object (64-bit, compressed oops):
┌──────────────────────────────────────────┐
│ Object Header          │ 16 bytes        │
├──────────────────────────────────────────┤
│ detailMessage (String) │ 4 bytes (ref)   │
│ cause (Throwable)      │ 4 bytes (ref)   │
│ stackTrace (array)     │ 4 bytes (ref)   │
│ suppressedEx (List)    │ 4 bytes (ref)   │
├──────────────────────────────────────────┤
│ Padding                │ 4 bytes         │
├──────────────────────────────────────────┤
│ Total                  │ 32 bytes        │
└──────────────────────────────────────────┘
```

The object itself is 32 bytes. But it references other objects that consume additional memory.

## Reference Field Sizes

| Reference | Target | Size (compressed) | Size (uncompressed) |
|---|---|---|---|
| `detailMessage` | String | 4 bytes | 8 bytes |
| `cause` | Throwable | 4 bytes | 8 bytes |
| `stackTrace` | StackTraceElement[] | 4 bytes | 8 bytes |
| `suppressedExceptions` | List<Throwable> | 4 bytes | 8 bytes |

With compressed oops (default on heaps < 32 GB), each reference is 4 bytes.

## String Deduplication for Messages

The G1 garbage collector can deduplicate strings. When a Throwable's message is a string that already exists in the heap, G1 may deduplicate them:

- String deduplication is enabled by `-XX:+UseStringDeduplication` (G1 only)
- Two Throwable objects with the same message may share the underlying char/byte array
- This saves memory when many exceptions carry the same message
- Deduplication happens during GC, not at creation time

## Stack Trace Array Memory Cost

Each `StackTraceElement` in the stack trace array consumes:

```
StackTraceElement object:
┌──────────────────────────────────────────┐
│ Object Header          │ 16 bytes        │
├──────────────────────────────────────────┤
│ declaringClass (String)│ 4 bytes         │
│ methodName (String)    │ 4 bytes         │
│ fileName (String)      │ 4 bytes         │
│ lineNumber (int)       │ 4 bytes         │
├──────────────────────────────────────────┤
│ Total                  │ 32 bytes        │
└──────────────────────────────────────────┘
```

Plus the `StackTraceElement[]` array header (16 bytes + 4 bytes length) and 4 bytes per element reference.

### Per-Frame Cost

| Component | Size |
|---|---|
| StackTraceElement object | 32 bytes |
| Element reference in array | 4 bytes |
| **Subtotal per frame** | **36 bytes** |

### Total Stack Trace Cost

| Stack Depth | Array Cost | Element Cost | Total |
|---|---|---|---|
| 5 frames | 20 bytes | 160 bytes | 180 bytes |
| 10 frames | 24 bytes | 320 bytes | 344 bytes |
| 20 frames | 32 bytes | 640 bytes | 672 bytes |
| 50 frames | 56 bytes | 1,600 bytes | 1,656 bytes |
| 100 frames | 96 bytes | 3,200 bytes | 3,296 bytes |

## Per-Throwable Overhead Calculation

```
Throwable object:                    32 bytes
String (message, ~20 chars avg):     24 bytes + 44 bytes (char array) = 68 bytes
StackTraceElement[] array:           20-96 bytes (depending on depth)
StackTraceElement objects:           160-3,200 bytes (5-100 frames)
List<Throwable> (empty ArrayList):   40 bytes
Total (typical, 20 frames):         ~760 bytes
Total (deep, 100 frames):          ~3,340 bytes
```

For a typical exception with a 20-frame stack trace:

| Component | Bytes |
|---|---|
| Throwable object | 32 |
| Message string | 68 |
| Stack trace array header | 24 |
| 20 × StackTraceElement | 640 |
| 20 × references in array | 80 |
| Empty suppressed list | 40 |
| **Total** | **~884 bytes** |

## Memory Leak Patterns

### Pattern 1: Accumulating Throwables in Collections

```java
// MEMORY LEAK: each Throwable holds its stack trace in memory
List<Throwable> errorLog = new ArrayList<>();
while (processing) {
    try {
        riskyOperation();
    } catch (Throwable t) {
        errorLog.add(t); // accumulates forever
    }
}
// Eventually: OutOfMemoryError from accumulated throwables
```

**Fix**: Use a bounded queue or clear periodically:

```java
Deque<Throwable> errorLog = new ArrayDeque<>(1000);
// When adding, evict oldest if full
if (errorLog.size() >= 1000) {
    errorLog.pollFirst();
}
errorLog.addLast(t);
```

### Pattern 2: Exception in Cache Keys

```java
// BAD: Throwable as map key retains stack trace references
Map<Throwable, String> cache = new HashMap<>();
cache.put(new RuntimeException("key1"), "value1");
// Throwable's stack trace keeps class metadata alive
```

**Fix**: Use exception message or type as key, not the Throwable itself.

### Pattern 3: Long-Lived Throwable References

```java
// BAD: static field holds Throwable forever
class ErrorHolder {
    static Throwable lastError;
}
```

**Fix**: Clear static references when no longer needed.

### Pattern 4: Throwable in ThreadLocal

```java
// BAD: ThreadLocal holds Throwable across requests
ThreadLocal<Throwable> context = ThreadLocal.withInitial(() -> null);
```

**Fix**: Clear ThreadLocal values in finally blocks.

### Pattern 5: Suppressed Exception Chain Growth

```java
// BAD: suppressed exceptions accumulate during retry
Throwable lastError = null;
for (int i = 0; i < 100; i++) {
    try {
        riskyOperation();
        break;
    } catch (Throwable t) {
        if (lastError != null) {
            t.addSuppressed(lastError); // chain grows
        }
        lastError = t;
    }
}
```

**Fix**: Cap the number of suppressed exceptions or use a fresh exception each retry.

## Production Patterns

### Pattern 1: Throwable Pool

```java
public class ThrowablePool {
    private final Queue<FastException> pool = new ConcurrentLinkedQueue<>();

    public FastException acquire(String message) {
        FastException e = pool.poll();
        if (e != null) {
            e.init(message);
            return e;
        }
        return new FastException(message);
    }

    public void release(FastException e) {
        e.clear();
        pool.offer(e);
    }
}
```

### Pattern 2: Bounded Error Log

```java
public class BoundedErrorLog {
    private final Deque<Throwable> log;
    private final int maxSize;

    public BoundedErrorLog(int maxSize) {
        this.maxSize = maxSize;
        this.log = new ArrayDeque<>(maxSize);
    }

    public synchronized void add(Throwable t) {
        if (log.size() >= maxSize) {
            log.pollFirst();
        }
        log.addLast(t);
    }
}
```

### Pattern 3: Weak Reference Throwable Cache

```java
public class ThrowableCache {
    private final Map<String, WeakReference<Throwable>> cache = new HashMap<>();

    public void cache(String key, Throwable t) {
        cache.put(key, new WeakReference<>(t));
    }

    public Throwable get(String key) {
        WeakReference<Throwable> ref = cache.get(key);
        return ref != null ? ref.get() : null;
    }
}
```

## Summary

| Concept | Key Takeaway |
|---|---|
| Object header | 16 bytes on 64-bit JVM with compressed oops |
| Throwable fields | 4 reference fields = 16 bytes + 16 header = 32 bytes |
| Stack trace cost | ~36 bytes per frame (object + reference) |
| Typical total | ~800-900 bytes for a 20-frame exception |
| String deduplication | G1 GC can share message strings across Throwable instances |
| Memory leak | Accumulating Throwable references in collections or static fields |
| Mitigation | Bounded queues, weak references, clearing references in finally |
