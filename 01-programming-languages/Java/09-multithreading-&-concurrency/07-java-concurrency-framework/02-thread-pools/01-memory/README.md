# Thread Pools — Memory Model

## Overview

This directory explores how memory is managed in thread pools — thread stacks, task objects, and queue memory overhead.

## Key Memory Concepts

### Thread Stack Memory

- Each thread has a private stack (default 1MB, configurable with `-Xss`)
- Stack frames hold local variables, method parameters, and return addresses
- Deep recursion can cause `StackOverflowError`
- Virtual threads use ~1KB heap-allocated stacks

### Queue Memory

- `LinkedBlockingQueue` allocates a `Node` object per task (object overhead + reference)
- `ArrayBlockingQueue` uses a fixed array (pre-allocated, no per-task overhead)
- `SynchronousQueue` has zero queue memory — direct handoff between threads

### Task Object Memory

- Each task is a heap object (lambda instance or anonymous class)
- Captured variables increase the task object's size
- After execution, the task is eligible for GC if no references remain

### Memory Pool Tuning

- Too few threads: tasks queue, increasing latency
- Too many threads: excessive stack memory and context switching
- Optimal pool size: CPU-bound = number of cores; I/O-bound = more than cores

## Files

- [ThreadPoolMemory.java](ThreadPoolMemory.java) — Memory layout and tuning in thread pools
