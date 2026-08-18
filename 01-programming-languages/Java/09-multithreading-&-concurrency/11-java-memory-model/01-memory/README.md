# Java Memory Model — Memory Model

## Overview

This directory explores how the Java Memory Model defines memory visibility — how threads observe each other's writes and the guarantees that prevent data races.

## Key Topics

### Memory Visibility

- Without synchronization, threads may see stale values from CPU caches
- The JMM defines which writes are visible to which reads
- `volatile` and `synchronized` establish visibility guarantees
- `final` fields provide special visibility after construction

### Cache Coherency

- Modern CPUs have multi-level caches (L1, L2, L3)
- Cache lines are shared between cores via cache coherence protocols (MESI)
- The JMM abstracts over hardware differences
- Volatile/synchronized force cache line invalidation

### Store Buffer and Load Buffer

- CPUs use store buffers to decouple writes from the cache
- Store buffers may delay visibility of writes to other cores
- Memory barriers flush store buffers and invalidate load buffers
- This is why volatile is needed for cross-thread visibility

### Out-of-Thin-Air Values

- The JMM prohibits values that were never written by any thread
- This prevents aggressive compiler optimizations from breaking concurrency
- Example: `int x = 0; int y = 0; // Thread 1: x = 1; if (y == 0) // Thread 2: y = 1; if (x == 0) // JMM prohibits both threads seeing 0`

## Files

- [MemoryModelMemory.java](MemoryModelMemory.java) — Memory visibility and cache behavior
