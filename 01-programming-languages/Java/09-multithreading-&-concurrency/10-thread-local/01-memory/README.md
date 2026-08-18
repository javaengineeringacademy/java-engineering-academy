# ThreadLocal — Memory Model

## Overview

This directory explores how memory is managed for ThreadLocal variables — per-thread storage, memory leaks, and cleanup.

## Key Topics

### ThreadLocalMap Memory

- Stored in `Thread.threadLocals` field (not shared between threads)
- Open-addressing hash map: `Entry[]` array
- Each `Entry` extends `WeakReference<ThreadLocal>` — key is weakly referenced
- Value is strongly referenced until explicitly removed or GC cleans stale entries

### Memory Leak Scenario

- ThreadLocal value is strongly referenced by the Entry
- The key (ThreadLocal) is weakly referenced — GC may collect it
- Entry remains with a null key and non-null value (memory leak)
- Next `get()`/`set()` cleans stale entries, but only if you access the map

### Thread Pool Memory Leak

- Pooled threads reuse their ThreadLocalMap
- Old values persist until removed or the thread dies
- Always call `threadLocal.remove()` after use in thread pools
- Class loader leak: ThreadLocal holds references preventing GC during hot reload

### Virtual Thread Memory

- Virtual threads use copy-on-write for InheritableThreadLocal
- Each forked virtual thread gets a snapshot of the parent's values
- Virtual thread stacks are heap-allocated (~1KB), much smaller than platform threads

## Files

- [ThreadLocalMemory.java](ThreadLocalMemory.java) — Memory layout and cleanup for ThreadLocal
