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
