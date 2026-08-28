# Memory Leaks in Java

## Memory Leak Types

### 1. Static Collection Growth
Static maps/lists that grow without bound. Objects are never eligible for GC.

### 2. Unclosed Resources
Streams, connections, or channels not closed properly. Leaks native memory and file descriptors.

### 3. Inner Class References
Non-static inner classes hold implicit reference to outer instance, preventing GC.

### 4. ThreadLocal Without Cleanup
ThreadLocal values live as long as the thread. In thread pools, values accumulate.

### 5. Listener/Callback Accumulation
Registered listeners never unregistered. Objects referenced forever.

### 6. Classloader Leaks
Custom classloaders not unloaded, taking all loaded classes with them.

## Detection Tools

### Eclipse MAT (Memory Analyzer Tool)
- Dominator tree: shows who holds most memory
- Leak Suspects report: automatic analysis
- OQL: query heap objects like SQL

### VisualVM
- Heap dumps with instance inspection
- Real-time memory monitoring
- Sampler for allocation tracking

### Command Line
```bash
# Take heap dump
jmap -dump:format=b,file=heap.bin <pid>

# Auto dump on OOM
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/tmp/heapdump.hprof

# Live heap analysis
jmap -histo:live <pid> | head -20

# GC stats
jstat -gcutil <pid> 1000
```

### JProfiler / YourKit
- Commercial profilers with allocation tracking
- Real-time memory views
- Automatic leak detection

## Prevention Strategies

### Use Try-With-Resources
```java
try (var conn = dataSource.getConnection();
     var stmt = conn.prepareStatement(sql)) {
    // resources auto-closed
}
```

### Bounded Caches
```java
// Guava/Caffeine with eviction
Cache<String, Object> cache = CacheBuilder.newBuilder()
    .maximumSize(10000)
    .expireAfterAccess(Duration.ofMinutes(10))
    .build();
```

### WeakHashMap for Associations
```java
Map<Key, Value> cache = new WeakHashMap<>();
// Entries GC'd when key has no strong references
```

### ThreadLocal Cleanup
```java
try {
    ThreadLocal.set(value);
    // work
} finally {
    ThreadLocal.remove(); // always remove
}
```

### Static Nested Classes
```java
// BAD: inner class holds outer reference
class Outer {
    class Inner { } // implicit Outer$ reference
}

// GOOD: static nested class
class Outer {
    static class Nested { } // no outer reference
}
```

## Common Scenarios

| Scenario | Cause | Fix |
|----------|-------|-----|
| Heap keeps growing | Static cache without eviction | Bounded cache with TTL |
| `OutOfMemoryError: Metaspace` | Classloader leak | Fix classloader lifecycle |
| FD leak | Unclosed streams | Try-with-resources |
| Slow GC cycles | Large ThreadLocal in pool | ThreadLocal.remove() |
| High old gen usage | Premature promotion | Tune young gen size |

## Overview

Java memory leaks occur when objects are no longer needed but cannot be garbage collected because they're still reachable from the GC root. Unlike C/C++ leaks (lost pointers), Java leaks are logical—objects remain referenced by collections, caches, listeners, or classloaders. Detection requires heap dump analysis with tools like Eclipse MAT, VisualVM, or JProfiler. Prevention involves bounded caches, try-with-resources, WeakReference, and careful ThreadLocal usage.

## Why This Concept Exists

Memory leaks exist in Java because the garbage collector can only reclaim unreachable objects. If a `static Map` accumulates entries, or a `ThreadLocal` is never removed, or a listener is never unregistered, those objects remain reachable indefinitely. The JVM's GC root set includes static fields, active threads, JNI references, and monitor locks. Any object reachable from these roots cannot be collected, causing memory to grow until `OutOfMemoryError`.

## Internal Working

### GC Root Reachability Analysis

```
GC Roots:
├── Static fields (Class objects)
├── Active threads (Thread objects)
├── JNI global references
├── Monitor locks (blocked threads)
├── JVM internal structures

Reachability chain:
Root → Static Map → Entry → Value → Large Object
                      ↑
            Still reachable = NOT collected
```

### Heap Dump Analysis

```bash
# Take heap dump
jmap -dump:format=b,file=heap.bin <pid>

# Auto dump on OOM
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/tmp/heapdump.hprof

# Live heap dump (forces full GC first)
jmap -dump:live,format=b,file=heap.bin <pid>

# Eclipse MAT analysis:
# 1. Dominator Tree: shows who holds most memory
# 2. Leak Suspects: automatic analysis
# 3. OQL: query heap objects like SQL
# 4. Path to GC Roots: trace references
```

### Common Leak Patterns

```java
// Pattern 1: Static collection growth
private static final Map<String, Object> cache = new HashMap<>();
// Entries never removed → memory grows indefinitely

// Pattern 2: Unclosed resources
InputStream is = new FileInputStream("file.txt");
// Not closed → file descriptor leak + memory leak

// Pattern 3: Inner class reference
class Outer {
    private int[] data = new int[1000000];
    class Inner { } // Holds implicit reference to Outer
}
// Inner class prevents Outer from being GC'd

// Pattern 4: ThreadLocal without cleanup
private static final ThreadLocal<byte[]> BUFFER = 
    ThreadLocal.withInitial(() -> new byte[1024]);
// In thread pools, ThreadLocal values accumulate

// Pattern 5: Listener accumulation
button.addActionListener(new ActionListener() { ... });
// Never removed → button holds reference forever
```

## Examples

### Bounded Cache Implementation

```java
// BAD: Unbounded cache
private static final Map<String, User> cache = new HashMap<>();
public User getUser(String id) {
    return cache.computeIfAbsent(id, this::loadFromDB);
}
// Memory grows indefinitely

// GOOD: Bounded cache with eviction
private static final Cache<String, User> cache = CacheBuilder.newBuilder()
    .maximumSize(10_000)
    .expireAfterAccess(Duration.ofMinutes(10))
    .recordStats()
    .build();

public User getUser(String id) {
    return cache.get(id, this::loadFromDB);
}
```

### ThreadLocal Cleanup

```java
// BAD: ThreadLocal in thread pool
private static final ThreadLocal<UserContext> CONTEXT = 
    new ThreadLocal<>();

public void process(Request request) {
    CONTEXT.set(new UserContext(request));
    try {
        handleRequest();
    } finally {
        // Forgot to remove!
    }
}

// GOOD: Always remove in finally
public void process(Request request) {
    CONTEXT.set(new UserContext(request));
    try {
        handleRequest();
    } finally {
        CONTEXT.remove(); // Always remove
    }
}
```

### WeakReference for Caches

```java
// WeakHashMap: entries GC'd when key has no strong references
private static final Map<Key, Value> cache = new WeakHashMap<>();

public Value getValue(Key key) {
    Value value = cache.get(key);
    if (value == null) {
        value = loadFromDB(key);
        cache.put(key, value);
    }
    return value;
}

// When Key has no strong references, entry is GC'd
// Prevents memory leaks for transient keys
```

### Resource Leak Detection

```java
// AutoCloseable with leak detection
public class TrackedConnection implements AutoCloseable {
    private static final AtomicInteger openCount = new AtomicInteger();
    private final Connection delegate;

    public TrackedConnection(Connection conn) {
        this.delegate = conn;
        openCount.incrementAndGet();
        System.out.println("Connections open: " + openCount.get());
    }

    @Override
    public void close() throws SQLException {
        delegate.close();
        int remaining = openCount.decrementAndGet();
        System.out.println("Connections open: " + remaining);
    }
}

// Usage
try (var conn = new TrackedConnection(dataSource.getConnection())) {
    // Auto-closed, count decremented
}
```

## Performance

### Memory Leak Impact

| Leak Type | Growth Rate | Detection Time | Impact |
|-----------|-------------|----------------|--------|
| Static cache | Linear | Hours-Days | OOM |
| ThreadLocal | Per-thread | Days-Weeks | GC pressure |
| Listener | Per-registration | Days-Weeks | Memory growth |
| Classloader | Per-deploy | Weeks-Months | Metaspace OOM |
| FD leak | Per-operation | Hours | FD exhaustion |

### Heap Dump Analysis Time

| Heap Size | Dump Time | MAT Analysis | OQL Query |
|-----------|-----------|--------------|-----------|
| 256MB | 5s | 10s | <1s |
| 1GB | 15s | 30s | 2s |
| 4GB | 60s | 2min | 5s |
| 16GB | 5min | 10min | 15s |

### Leak Detection Tools Comparison

| Tool | Type | Cost | Features |
|------|------|------|----------|
| Eclipse MAT | Heap analysis | Free | OQL, Leak Suspects |
| VisualVM | Runtime monitoring | Free | Heap dumps, sampling |
| JProfiler | Profiler | Commercial | Allocation tracking |
| YourKit | Profiler | Commercial | Memory, CPU profiling |
| Arthas | Diagnostic | Free | Runtime analysis |

## Pitfalls

### 1. Static Collection Growth

```java
// BAD: Static map without eviction
private static final Map<String, byte[]> cache = new HashMap<>();
public void cache(String key, byte[] data) {
    cache.put(key, data); // Never removed
}

// GOOD: Bounded cache with TTL
private static final Cache<String, byte[]> cache = CacheBuilder.newBuilder()
    .maximumSize(1_000)
    .expireAfterWrite(Duration.ofHours(1))
    .build();
```

### 2. Unclosed Resources

```java
// BAD: Resource not closed
InputStream is = new FileInputStream("file.txt");
byte[] data = is.readAllBytes();
// is never closed → file descriptor leak

// GOOD: Try-with-resources
try (var is = new FileInputStream("file.txt")) {
    byte[] data = is.readAllBytes();
}
// Auto-closed even on exception
```

### 3. Inner Class References

```java
// BAD: Non-static inner class
class Outer {
    private int[] data = new int[1_000_000];
    class Inner { } // Implicit reference to Outer
}

// GOOD: Static nested class
class Outer {
    private int[] data = new int[1_000_000];
    static class Nested { } // No reference to Outer
}
```

### 4. ThreadLocal Without Cleanup

```java
// BAD: ThreadLocal in thread pool
private static final ThreadLocal<byte[]> BUFFER = 
    ThreadLocal.withInitial(() -> new byte[65536]);

// GOOD: Always remove
try {
    BUFFER.set(new byte[65536]);
    process();
} finally {
    BUFFER.remove();
}
```

### 5. Listener Accumulation

```java
// BAD: Listener never unregistered
eventBus.register(handler);
// handler holds reference → memory leak

// GOOD: Unregister when done
eventBus.register(handler);
try {
    process();
} finally {
    eventBus.unregister(handler);
}
```

## References

- [Java Memory Management](https://www.oracle.com/technetwork/articles/java/vmoptions-139243.html)
- [Eclipse MAT Documentation](https://help.eclipse.org/2020-09/topic/org.eclipse.mat.ui.help/)
- [OpenJDK: Memory Management](https://openjdk.org/groups/hotspot/)
- *Java Performance* by Scott Oaks
- [VisualVM](https://visualvm.github.io/)
- [Arthas Diagnostic Tool](https://arthas.aliyun.com/)
