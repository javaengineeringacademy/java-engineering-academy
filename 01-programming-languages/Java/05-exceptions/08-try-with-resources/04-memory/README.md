# Try-with-Resources and Memory

## Memory Management Overview

Try-with-resources affects memory management in several ways:

1. **Deterministic cleanup** — Resources are released immediately at scope exit
2. **Reduced heap pressure** — No need to hold references for finally blocks
3. **GC friendliness** — Shorter object lifetimes improve garbage collection
4. **Native memory** — Critical for resources backed by native memory (files, sockets)

## Deterministic Cleanup

With TWR, resources are closed at the end of the statement, not when the garbage collector runs. This is crucial for:

- **File handles** — OS file descriptors are limited
- **Database connections** — Connection pools can be exhausted
- **Network sockets** — Port exhaustion under load
- **Native memory** — Direct ByteBuffer, memory-mapped files

```java
// Good: deterministic cleanup
try (BufferedReader reader = new BufferedReader(new FileReader("big.txt"))) {
    processFile(reader);
}
// reader is closed immediately — file handle released

// Bad: non-deterministic cleanup
BufferedReader reader = new BufferedReader(new FileReader("big.txt"));
processFile(reader);
// reader closed only when GC runs — file handle held
```

## Heap Memory Implications

### Object Lifetime

TWR shortens the lifetime of resource objects:

```java
// Resource lives for entire method
void processAll() {
    Connection conn = getConnection();
    try {
        // ... work with conn ...
    } finally {
        conn.close();
    }
    // conn still referenced until GC
}

// Resource lives only in TWR scope
void processAll() {
    try (Connection conn = getConnection()) {
        // ... work with conn ...
    }
    // conn no longer referenced — eligible for GC
}
```

### Reference Retention

TWR variables are implicitly final, preventing accidental reference retention:

```java
// Bad: reference retained
BufferedReader reader = new BufferedReader(new FileReader("data.txt"));
try {
    // ... work with reader ...
} finally {
    reader.close();
}
// reader still in scope — GC cannot collect

// Good: reference scoped to TWR
try (BufferedReader reader = new BufferedReader(new FileReader("data.txt"))) {
    // ... work with reader ...
}
// reader out of scope — GC can collect
```

## Native Memory Resources

Some resources allocate native (off-heap) memory:

- `DirectByteBuffer` — Native memory outside JVM heap
- `MappedByteBuffer` — Memory-mapped files
- `FileChannel` — Native file operations
- `Socket` — Native socket buffers

TWR ensures native memory is released promptly:

```java
try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
    MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
    // buffer uses native memory
}
// channel closed — native memory eligible for release
```

Without TWR, native memory leaks are harder to detect because they don't show in heap dumps.

## Connection Pool Behavior

In connection pool scenarios, TWR is critical:

```java
// Connection returned to pool immediately
try (Connection conn = pool.getConnection();
     PreparedStatement ps = conn.prepareStatement(sql)) {
    // work with connection
}
// conn returned to pool — available for other threads

// Without TWR — connection held until GC
Connection conn = pool.getConnection();
// work...
conn.close(); // manual close — easy to forget in exception paths
```

## Memory-Efficient Patterns

### 1. Short-lived Resources

```java
// Process and close immediately
try (Stream<String> lines = Files.lines(path)) {
    lines.forEach(this::processLine);
}
// Stream closed — memory released
```

### 2. Nested Scopes

```java
try (ResourceA a = createA()) {  // A allocated
    try (ResourceB b = createB()) {  // B allocated
        // both open
    }  // B closed — memory released
    // only A open
}  // A closed — memory released
```

### 3. Lazy Initialization

```java
// Resource created only when needed
try (BufferedReader reader = Files.newBufferedReader(path)) {
    // reader created and closed in minimal scope
}
```

## Common Memory Pitfalls

### 1. Holding References

```java
// Bad: reference retained in collection
List<BufferedReader> readers = new ArrayList<>();
try (BufferedReader r = new BufferedReader(new FileReader("data.txt"))) {
    readers.add(r);  // prevents GC
    // work...
}
// r closed but still in readers list — memory leak
```

### 2. Circular References

```java
// Bad: resource holds reference to itself
class BadResource implements AutoCloseable {
    private BadResource referenceToSelf;

    @Override
    public void close() {
        referenceToSelf = null;  // too late
    }
}
```

### 3. Large Buffers

```java
// Bad: large buffer lives too long
try (BufferedReader reader = new BufferedReader(new FileReader("huge.txt"), 1024 * 1024)) {
    // 1MB buffer allocated
    process(reader);
}
// buffer released — but was 1MB for entire scope
```

## Memory Monitoring

### Heap Dumps

With TWR, resource objects appear in heap dumps only during their scope:

```bash
# Take heap dump
jmap -dump:live,format=b,file=heap.bin <pid>

# Analyze with jhat or VisualVM
# Look for file handles, connections in use
```

### Native Memory Tracking

For native memory resources:

```bash
# Enable native memory tracking
java -XX:NativeMemoryTracking=summary -jar app.jar

# Check native memory
jcmd <pid> VM.native_memory summary
```

## Performance Considerations

- **TWR has zero runtime overhead** — same as manual finally
- **Multiple resources** = multiple finally blocks (same as manual nesting)
- **GC pressure reduced** — shorter object lifetimes
- **No hidden allocations** — TWR doesn't create wrapper objects

## Best Practices for Memory Efficiency

1. **Minimize resource scope** — Declare resources as close to use as possible
2. **Prefer TWR over finally** — Automatic cleanup, shorter lifetimes
3. **Close early** — Exit TWR scope as soon as resource is no longer needed
4. **Avoid unnecessary buffering** — Use appropriate buffer sizes
5. **Monitor native memory** — Use jcmd for off-heap tracking

## Summary

Try-with-resources improves memory management by:
- Providing deterministic cleanup
- Shortening resource lifetimes
- Reducing GC pressure
- Preventing reference retention
- Enabling efficient native memory management

---

**Next:** [Examples](../examples/TryWithResourcesExample.java) — Practical examples.
