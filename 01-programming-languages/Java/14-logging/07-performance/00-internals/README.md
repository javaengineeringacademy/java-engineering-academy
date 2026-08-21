# Internals: Logging Performance Mechanics

## String Interpolation Cost

```java
// MessageFormatter (SLF4J) vs String concatenation

// Concatenation:
"User " + userId + " logged in"
// 1. StringBuilder created
// 2. 3 append() calls
// 3. toString() creates final String
// Total: ~4 objects, ~100-200ns

// Parameterized:
"User {} logged in", userId
// 1. Check level (O(1))
// 2. Object[1] created
// 3. MessageFormatter replaces {}
// 4. Single String created
// Total: ~2 objects, ~20-50ns
```

## Async Queue Mechanics

```
Synchronous flow:
  Thread → Write → Block → Continue
  (Latency = I/O time)

Async flow:
  Thread → Enqueue → Continue (non-blocking)
  Writer → Dequeue → Write
  (Latency = enqueue time ~10-100ns)
```

**Queue full behavior:**
- `discardingThreshold > 0`: Discard events below threshold
- `discardingThreshold = 0`: Block caller until space available
- `neverBlock = true`: Drop events silently

## I/O Batching

```xml
<!-- Immediate flush: 1 syscall per log event -->
<immediateFlush>true</immediateFlush>

<!-- Buffered: batch multiple events per syscall -->
<immediateFlush>false</immediateFlush>
```

**Syscall cost:**
- Each write() syscall: ~1-10 microseconds
- With 1000 events/sec: 1000 syscalls/sec
- Buffered: ~10-100 syscalls/sec (10-100x reduction)

## Garbage Collection Impact

```java
// Each log event creates objects:
// 1. LogEvent (if not pooled)
// 2. Object[] for parameters
// 3. FormattingTuple
// 4. Final message String

// With 10K events/sec:
// 40K objects/sec → GC pressure

// With async + pooling:
// Objects reused, reduced allocation rate
```

## Thread Contention

```java
// Synchronized appender:
// Multiple threads → lock contention → serialization

// Async appender:
// Multiple threads → lock-free queue → parallel enqueue
// Single writer thread → no contention on write
```
