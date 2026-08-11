# 04 - Memory

## Scope

This topic covers memory-related Errors in Java — specifically `OutOfMemoryError` and its variants. You will learn about JVM memory layout, what happens when memory is exhausted, and how to diagnose memory problems.

## Why It Exists

Memory exhaustion is the most common Error in production Java applications. Understanding memory layout, heap structure, and garbage collection behavior is essential for diagnosing and preventing `OutOfMemoryError`. This topic bridges the gap between "my application ran out of memory" and "my application leaked 2GB of session objects in an unbounded cache."

## Design Rationale

The JVM manages memory through a combination of heap spaces and non-heap regions. Each region serves a different purpose and has different failure modes:

| Region | Purpose | Failure Mode |
|--------|---------|--------------|
| Eden Space | New object allocation | Minor GC triggers |
| Survivor Space | Objects surviving minor GC | Promoted to Old Gen |
| Old Generation | Long-lived objects | Major GC → Full GC → OOM |
| Metaspace | Class metadata | Class loading OOM |
| Thread Stacks | Stack frames | Thread creation OOM |
| Direct Buffers | NIO direct memory | Direct buffer OOM |

The garbage collector manages the heap automatically, but it cannot prevent exhaustion when the application allocates faster than the collector can reclaim.

## What Happens During OOM

When `OutOfMemoryError` occurs:

```
1. Object allocation request arrives
2. JVM checks available space in current TLAB/eden
3. If insufficient → trigger Young GC (minor collection)
4. After Young GC → check surviving objects
5. If Old Generation full → trigger Full GC (major collection)
6. If Full GC fails to free enough space → throw OutOfMemoryError
```

The sequence before OOM:

| Stage | Action | Pause Time |
|-------|--------|------------|
| 1 | TLAB allocation fails | None |
| 2 | Eden allocation fails | None |
| 3 | Young GC | ~10-50ms |
| 4 | Old Gen full | ~100-500ms |
| 5 | Full GC (with compact) | ~1-10s |
| 6 | OOM thrown | None |

After OOM, the JVM is in a degraded state:
- Some objects may have been collected
- Memory is fragmented
- Further allocations will likely fail
- The application should not continue

## Heap Dump Analysis Basics

A heap dump is a snapshot of all objects in the heap at a point in time. It is the primary tool for diagnosing memory leaks and OOM.

**Capturing a heap dump:**

```bash
# On OOM (recommended for production)
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/var/log/app/heapdump.hprof

# Manual capture (jmap)
jmap -dump:live,format=b,file=dump.hprof <pid>

# From code (use sparingly)
HeapDump.dumpHeap(outputStream, live);
```

**Analyzing a heap dump:**

Tools like Eclipse MAT, VisualVM, and JProfiler can load heap dumps and show:
- Dominator tree (largest objects by retained size)
- GC roots (objects that keep other objects alive)
- Leak suspects (automatic detection of probable leaks)
- Histogram (object count and size by class)

**Key metrics:**

| Metric | Meaning |
|--------|---------|
| Retained size | Memory freed if object is GC'd |
| Shallow size | Memory consumed by the object itself |
| Dominator tree | Objects that retain the most memory |
| GC roots | Objects that prevent garbage collection |

## Memory Leak vs Memory Exhaustion

**Memory Exhaustion:**
- The application legitimately needs more memory than configured
- All allocated memory is reachable and in use
- Solution: Increase heap size (`-Xmx`)

```java
// Legitimate: large data processing
byte[] largeArray = new byte[1024 * 1024 * 100]; // 100MB
// This is not a leak — the array is needed
```

**Memory Leak:**
- Objects are no longer needed but remain reachable
- Memory usage grows over time
- Solution: Fix the code that retains unnecessary references

```java
// Leak: static cache grows unbounded
static List<byte[]> cache = new ArrayList<>();
void process(byte[] data) {
    cache.add(data); // Never removed → leak
}
```

**Signs of a memory leak:**
- Heap usage grows steadily over time
- Full GC frequency increases
- GC reclaims less and less memory
- Eventually, OOM occurs

**Signs of memory exhaustion:**
- Heap usage is consistently high but stable
- GC frequency is normal
- OOM occurs during peak load
- Solution: increase `-Xmx` or optimize memory usage

## GC Overhead Limit Exceeded

This is a specific OOM variant: `java.lang.OutOfMemoryError: GC overhead limit exceeded`

The JVM throws this when:
- GC is triggered frequently (>98% of time)
- GC recovers less than 2% of heap space
- The application is spending almost all its time in GC

This is different from regular OOM:
- Regular OOM: heap is full, allocation fails
- GC overhead: heap has free space, but GC is too slow to reclaim it

**Common causes:**
- Memory leak (most common)
- Heap too small for the application's working set
- Large number of weak/soft references causing excessive GC

**Disable the check (not recommended):**
```
-XX:-UseGCOverheadLimit
```

This does not fix the underlying problem — it just changes the error message to "Java heap space".

## Production Patterns

### Memory Monitoring

```java
MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

scheduler.scheduleAtFixedRate(() -> {
    MemoryUsage heap = memoryBean.getHeapMemoryUsage();
    long used = heap.getUsed();
    long max = heap.getMax();
    double percent = (double) used / max * 100;

    if (percent > 80) {
        logger.warn("Heap usage high: {}%", percent);
    }
    if (percent > 95) {
        logger.error("Heap usage critical: {}%, OOM imminent", percent);
        triggerAlert();
    }
}, 0, 30, TimeUnit.SECONDS);
```

### Preventing OOM

- Set appropriate `-Xmx` based on workload
- Use object pooling for expensive objects
- Implement bounded caches with eviction policies
- Use `SoftReference` and `WeakReference` appropriately
- Monitor and set `-XX:MaxMetaspaceSize`
- Use `-XX:+HeapDumpOnOutOfMemoryError` in production

## Summary

- OOM occurs when the JVM cannot allocate memory after full GC
- Memory leaks cause gradual heap exhaustion
- Memory exhaustion occurs when the heap is too small for the workload
- Heap dumps are the primary tool for diagnosing memory problems
- Monitor heap usage proactively to predict OOM before it occurs
- Use bounded caches and proper reference types to prevent leaks
- Enable heap dumps on OOM for post-mortem analysis