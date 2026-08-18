# Virtual Threads — Memory Model

## Overview

This directory explores how memory is managed for virtual threads — heap-allocated stacks, pinning memory effects, and structured concurrency cleanup.

## Key Topics

### Heap-Allocated Stacks

- Virtual thread stacks are `StackChunk` objects on the JVM heap
- Default size: ~1KB (much smaller than platform thread's 1MB)
- Stack frames are lazily allocated as needed
- Stacks can grow and shrink dynamically
- Eligible for GC when the virtual thread is unmounted

### Memory Efficiency

- 1 million virtual threads ≈ 1GB heap (vs 1TB for platform threads)
- Carrier threads share OS thread memory
- No per-thread OS stack allocation
- Reduces memory pressure in I/O-heavy applications

### Pinning Memory Impact

- When a virtual thread pins (synchronized/native), the carrier thread is blocked
- The pinned virtual thread's stack remains in heap memory
- Multiple pinned virtual threads can exhaust the carrier pool
- Monitor pinning: JVM allocates a heavyweight monitor on the heap

### Structured Concurrency Memory

- `StructuredTaskScope` manages child task references
- When scope closes, child tasks are cancelled and references cleared
- Prevents memory leaks from orphaned tasks
- Clean error propagation without leftover Future objects

### Copy-on-Write for InheritableThreadLocal

- When forking a virtual thread, inheritable values are copied
- Each virtual thread gets its own snapshot of parent's ThreadLocal values
- No shared references between parent and child virtual threads

## Files

- [VirtualThreadMemory.java](VirtualThreadMemory.java) — Memory layout and efficiency of virtual threads
