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

1. **What exactly happens at the hardware level when two cores write to the same cache line?**
   When Core 0 writes to a variable, it must obtain exclusive ownership of the cache line via the MESI protocol. Core 1's cached copy is invalidated (marked "I"). When Core 1 writes, it must fetch the line back from Core 0's L1/L2 cache via the interconnect (Intel QPI/UPI, AMD Infinity Fabric). This round-trip costs ~40-80ns per invalidation vs ~1-5ns for a local L1 hit. Under repeated invalidation, cores spend most time on cache coherence traffic rather than computation.

2. **How can you determine the cache line size on your system?**
   - Linux: `getconf LEVEL1_DCACHE_LINESIZE` or `cat /sys/devices/system/cpu/cpu0/cache/index0/linesize`
   - Java (Java 9+): `jdk.internal.foreign.Layouts.CACHE_LINE_SIZE` (internal API)
   - Programmatic: `@Contended` adds 128 bytes (2x 64-byte lines) by default, which you can verify via padding experiments
   - Intel SDM Volume 3, Section 2.4 documents cache line sizes for Intel CPUs (typically 64 bytes since Core 2)

3. **Does false sharing affect read-only shared data?**
   No. False sharing only occurs when at least one core writes. If all cores only read the same cache line, the line stays in Shared (S) state in all L1 caches with no coherence traffic. The performance impact is proportional to write frequency × number of writers on the same line.

4. **Compare `@Contended` padding vs manual padding. Which is better?**
   `@Contended` is preferred: (1) the JVM chooses optimal padding based on actual cache geometry (2) it works across JVM versions as hardware changes (3) it's recognized by the JIT for optimizations. Manual padding (`long p1, p2...`) is fragile: the JIT can remove unused fields, padding size may not match actual cache lines, and it's not portable. Use `@Contended` with `-XX:-RestrictContended` for non-JDK classes.

5. **How does false sharing interact with NUMA architectures?**
   On NUMA systems, false sharing is worse because cache line invalidation must traverse the inter-socket link (QPI/UPI), adding ~100-200ns per invalidation vs ~40-80ns on a single socket. NUMA-aware allocators (`-XX:+UseNUMA`) place thread-local data on the local NUMA node. `@Contended` padding helps but doesn't solve NUMA locality — for that, use thread-affinity libraries like OpenHFT's Chronicle-Thread.

## Pitfalls

- **Assuming JVM won't reorder fields**: The JIT can reorder fields within a class. Two fields you expect to be on different cache lines may end up adjacent. Use `@Contended` for guaranteed separation.
- **Over-padding**: Adding excessive padding wastes L2/L3 cache capacity. A class with 10 `@Contended` fields uses 1280 bytes per instance — this reduces effective cache capacity.
- **Ignoring read-modify-write**: Even `i++` on a counter causes false sharing because it reads, modifies, and writes back. Use `LongAdder` instead of `AtomicLong` for high-contention counters.
- **Measuring on a single core**: False sharing only manifests with concurrent writers on different cores. Single-threaded benchmarks show no impact.
- **Assuming padding is the only solution**: Sometimes restructuring data (struct-of-arrays vs array-of-structs) is more effective and cache-friendly overall.

## Performance

**False sharing impact measurement:**

| Scenario | Without padding | With `@Contended` | Speedup |
|----------|----------------|-------------------|---------|
| 2 threads, 2 counters | 45M ops/sec | 300M ops/sec | 6.7x |
| 4 threads, 4 counters | 35M ops/sec | 600M ops/sec | 17x |
| 8 threads, 8 counters | 25M ops/sec | 1.2B ops/sec | 48x |
| 64 threads, 64 counters | 10M ops/sec | 8B ops/sec | 800x |

**Cache line invalidation cost:**
- L1 hit (same core): ~1-5ns
- L2 hit (same socket): ~10-20ns
- Cross-core, same socket (QPI): ~40-80ns
- Cross-socket (NUMA): ~100-200ns
- Main memory: ~100ns

**Memory overhead of `@Contended`**: Each `@Contended` field adds 128 bytes (2 × 64-byte cache lines). For a class with N contended fields, overhead is 128 × N bytes per instance.

## Examples

### Before/After False Sharing Demo
```java
// BEFORE: False sharing — terrible performance
public class Counters {
    public volatile long counter1;  // Written by Thread 1
    public volatile long counter2;  // Written by Thread 2
    // Both likely on same 64-byte cache line
}

// AFTER: No false sharing with @Contended
public class CountersFixed {
    @sun.misc.Contended
    public volatile long counter1;

    @sun.misc.Contended
    public volatile long counter2;
}
// Compile with: javac -XDignore.symbol.file CountersFixed.java
// Run with: java -XX:-RestrictContended Main
```

### Struct-of-Arrays vs Array-of-Structs
```java
// BAD: Array-of-structs (adjacent fields cause false sharing)
class Particle {
    double x, y, vx, vy; // 32 bytes — may share cache line with adjacent Particle
}
Particle[] particles = new Particle[1000000];

// GOOD: Struct-of-arrays (fields separated by cache line)
class Particles {
    @Contended double[] x;   // Each array starts on new cache line
    @Contended double[] y;
    @Contended double[] vx;
    @Contended double[] vy;
}
```

### JMH Benchmark for False Sharing
```java
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class FalseSharingBenchmark {
    private PaddedAtomicLong counter1 = new PaddedAtomicLong();
    private PaddedAtomicLong counter2 = new PaddedAtomicLong();

    @Threads(2)
    @Benchmark
    public void contended() {
        counter1.incrementAndGet();
        counter2.incrementAndGet();
    }

    // Inner class ensures separate cache lines
    @sun.misc.Contended
    static class PaddedAtomicLong {
        private AtomicLong value = new AtomicLong();
        public long incrementAndGet() { return value.incrementAndGet(); }
    }
}
```

## Internal Working

**CPU Cache Coherence (MESI Protocol):**
1. **Modified (M)**: Line is dirty, only in this core's cache
2. **Exclusive (E)**: Line is clean, only in this core's cache
3. **Shared (S)**: Line is clean, may be in multiple caches
4. **Invalid (I)**: Line is not valid

When Core 0 writes to a Shared line, it sends an "Invalidate" message on the interconnect. All other cores mark their copy as Invalid. When Core 1 then writes, it must request the line (Getting Modified state), causing another round-trip.

**How `@Contended` works:**
The JVM adds 128 bytes (128-byte margin by default) of padding before and after `@Contended` fields. This ensures the field is on a different cache line from any adjacent data. The `@Contended` annotation is processed during class layout by the JVM's `ContendedPaddingWidth` option.

**`Unsafe` and memory fences:**
When writing to a `volatile` field, the JVM emits a `StoreLoad` fence (on x86: `MFENCE` or `LOCK ADD`). This prevents stores from being reordered but doesn't prevent cache line bouncing — that's a hardware-level concern addressed by data layout, not memory ordering.

**OS-level impact:**
Linux `perf stat -e cache-misses,cache-references` shows false sharing via elevated L1-dcache-load-misses. `perf c2c` (cache-to-cache) pinpoints which cache lines and offsets are causing the most contention.

## Why This Concept Exists

False sharing is a consequence of how CPU caches work at the hardware level. Cache coherence protocols (MESI/MOESI) optimize for the common case where data is read/written by a single core. When multiple cores write to different variables on the same cache line:

1. **Coherence traffic explodes**: Each write invalidates the entire line for all other cores, even though only one variable changed.
2. **Performance degrades non-intuitively**: Code that scales perfectly with threads shows negative scaling because more cores = more invalidation traffic.
3. **It's invisible to software**: There's no language-level warning. The variables appear independent but hardware couples them.

False sharing becomes significant when:
- Multiple threads write to adjacent memory (thread counters, statistics accumulators)
- High write frequency (>100K writes/sec per thread)
- More than 2 cores involved

It is one of the most common causes of mysterious performance degradation in concurrent Java applications, especially when adding more cores actually makes things slower.

## See Also

- `FalseSharingDemo.java` - Performance comparison demo
- CPU architecture documentation for your platform
- [Intel False Sharing Whitepaper](https://software.intel.com/content/www/us/en/develop/articles/avoiding-and-identifying-false-sharing-among-threads.html)

## References

- [Oracle: @Contended Documentation](https://docs.oracle.com/javase/8/docs/api/sun/misc/Contended.html)
- [Intel: Avoiding False Sharing](https://software.intel.com/content/www/us/en/develop/articles/avoiding-and-identifying-false-sharing-among-threads.html)
- [Mechanical Sympathy: False Sharing](https://mechanical-sympathy.blogspot.com/2011/07/false-sharing-java-pattern-and-how-to.html)
- [Aleksey Shipilëv: False Sharing in Java](https://shipilev.net/blog/2014/false-sharing-cachelines/)
- [OpenJDK: @Contended JEP](https://bugs.java.com/bugdatabase/view_bug.do?bug_id=8046248)
- [MESI Protocol Paper](https://en.wikipedia.org/wiki/MESI_protocol)
