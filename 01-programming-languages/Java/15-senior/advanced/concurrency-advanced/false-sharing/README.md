# False Sharing

## Overview

False sharing occurs when threads on different processors modify independent variables that happen to reside on the same cache line. This causes unnecessary cache invalidation and performance degradation.

## CPU Cache Architecture

### Cache Lines
- CPU caches operate in units of **cache lines** (typically 64 bytes)
- When a thread writes to a variable, the entire cache line is marked dirty
- Other cores must invalidate their copy of that cache line

### The Problem
```
Core 0: Writes to variable A
Core 1: Writes to variable B
If A and B are on same cache line:
    Core 0's write invalidates Core 1's cache (and vice versa)
    Even though they're writing different variables!
```

## False Sharing Explained

### Symptoms
- Performance degrades with more threads
- Scaling is poor or negative
- CPU counters show high cache misses

### Example
```java
// BAD: These may be on same cache line
class Counters {
    long counter1;  // Thread 1 writes
    long counter2;  // Thread 2 writes
}
```

## Detection

1. **Performance monitoring** - Watch L1/L2 cache miss rates
2. **Profiling tools** - Intel VTune, perf, async-profiler
3. **Benchmarking** - Compare single-threaded vs multi-threaded performance

## Prevention

### 1. Padding
```java
class PaddedCounter {
    long p1, p2, p3, p4, p5, p6, p7;  // Padding
    volatile long value;
    long p8, p9, p10, p11, p12, p13, p14;  // Padding
}
```

### 2. @Contended Annotation (JDK 8+)
```java
class ContendedCounter {
    @sun.misc.Contended
    volatile long value;
}
// Requires: -XX:-RestrictContended
```

### 3. Separate Classes
Put frequently-written fields in separate classes to increase distance.

## When to Worry

- High-performance concurrent data structures
- Multiple threads writing to adjacent memory
- Performance-critical code paths

## When to Ignore

- Low-contention scenarios
- Read-mostly data structures
- When simplicity is more important than performance

## Interview Questions

[5-10 interview questions with answers]

1. **What is this concept?**
   [Answer]

2. **When would you use it?**
   [Answer]

3. **What are the alternatives?**
   [Answer]

4. **What are common mistakes?**
   [Answer]

5. **How does it perform compared to alternatives?**
   [Answer]

## Pitfalls

[Common mistakes and anti-patterns]

## Performance

[Performance considerations and benchmarks]

## Examples

[Code examples demonstrating the concept]

## Internal Working

[How this works under the hood]

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## See Also

- `FalseSharingDemo.java` - Performance comparison demo
- CPU architecture documentation for your platform

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
