# Java Memory Model — Memory Visibility

## Overview

This directory explores how the Java Memory Model defines memory visibility — how threads observe each other's writes and the guarantees that prevent data races. Understanding the hardware memory model is essential for writing correct concurrent Java code.

## Key Topics

### Memory Visibility

Without synchronization, threads may see stale values from CPU caches. The JMM defines which writes are visible to which reads through happens-before relationships.

| Mechanism | Visibility Guarantee | Use Case |
|-----------|---------------------|----------|
| `volatile` | Write visible to subsequent read | Simple flags, state indicators |
| `synchronized` | All writes before unlock visible to next lock | Critical sections, compound operations |
| `final` fields | Visible after constructor completes | Immutable objects |
| `Atomic*` classes | Volatile + atomic CAS | Counters, atomic updates |

### Cache Coherency

Modern CPUs have multi-level caches that sit between the processor and main memory:

```
┌─────────┐  ┌─────────┐
│  Core 0  │  │  Core 1  │
│  L1: 32K │  │  L1: 32K │
│  L2: 256K│  │  L2: 256K│
└────┬─────┘  └────┬─────┘
     │              │
  ┌──┴──────────────┴──┐
  │    L3 Cache: 8MB    │
  └──────────┬──────────┘
             │
      ┌──────┴──────┐
      │ Main Memory  │
      └──────────────┘
```

- Each core has its own L1/L2 cache
- Cache lines are shared between cores via cache coherence protocols (MESI)
- The JMM abstracts over hardware differences
- Volatile/synchronized force cache line invalidation

### Store Buffer and Load Buffer

CPUs use buffers to decouple execution from memory:

```
Execution Unit → Store Buffer → L1 Cache → L3 → Main Memory
Execution Unit ← Load Buffer  ← L1 Cache ← L3 ← Main Memory
```

- **Store Buffer**: A write sits here until the cache is ready. The write is NOT yet visible to other cores.
- **Load Buffer**: A read may be speculatively loaded here. The load may be cancelled if a branch is mispredicted.
- **Memory barriers** flush these buffers to ensure visibility.

### Why Threads Don't See Each Other's Writes

1. **CPU cache hierarchy**: Each core has its own L1/L2 cache. Writes go to Core 0's cache; Core 1 may still see the old value.
2. **Store buffers**: Writes may sit in the store buffer and not yet be visible to other cores.
3. **Compiler reordering**: The compiler may reorder reads/writes, breaking assumptions about ordering.
4. **CPU reordering**: Out-of-order execution may reorder loads/stores.

### Out-of-Thin-Air Values

The JMM prohibits values that were never written by any thread. This prevents aggressive compiler optimizations from breaking concurrency.

```java
int x = 0, y = 0;
// Thread 1:          // Thread 2:
if (x == 0)           if (y == 0)
  y = 1;                x = 1;
// JMM prohibits both threads seeing 0
// (circular reasoning that would allow it is forbidden)
```

### volatile vs synchronized vs Atomic

| Feature | volatile | synchronized | Atomic* |
|---------|----------|-------------|---------|
| Visibility | Yes | Yes | Yes |
| Atomicity | No | Yes | Yes |
| Mutual exclusion | No | Yes | No |
| Blocking | No | Yes | No |
| Performance | Fast | Slower | Fast |
| Use case | Simple flags | Critical sections | Atomic operations |

### Common Visibility Pitfalls

| Pitfall | Root Cause | Solution |
|---------|-----------|----------|
| Stale reads | No happens-before between writer and reader | Use `volatile` or `synchronized` |
| Word tearing on `long`/`double` | JVM may split 64-bit ops into two 32-bit ops | Declare as `volatile` or use `AtomicLong` |
| Double-checked locking broken | Object construction reordered past reference | Use `volatile` on the reference |
| Non-atomic `count++` | `volatile` doesn't make compound ops atomic | Use `AtomicInteger` or `synchronized` |

## Files

- [MemoryModelMemory.java](MemoryModelMemory.java) — Memory visibility, cache hierarchy, store/load buffers, volatile vs atomic
