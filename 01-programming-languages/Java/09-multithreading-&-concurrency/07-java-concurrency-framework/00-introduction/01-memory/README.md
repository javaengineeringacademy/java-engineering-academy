# Concurrency Framework — Memory Model

## Overview

This directory explores how memory is managed in Java's concurrency framework — how tasks, results, and thread-local data are allocated and communicated between threads.

## Key Memory Concepts

### Task Memory

- Each `Runnable`/`Callable` submitted to an executor is a heap-allocated object
- Task state is captured in the closure (lambda captures) or instance fields
- Tasks are stored in the work queue until executed

### Future Result Memory

- `CompletableFuture` stores the result (or exception) in an internal `Object` field
- Dependent stages read the result through happens-before relationships
- `ForkJoinTask` uses a result field with volatile semantics

### Thread Stack Memory

- Each platform thread has its own stack (default 1MB)
- Virtual threads use ~1KB stacks on the heap
- Local variables and method frames live on the thread stack

### Memory Visibility

- Submitting a task to an executor establishes a happens-before relationship between the submit and the task's execution
- `CompletableFuture` stages are linked by happens-before edges through their completion triggers

## Files

- [ForkJoinPoolMemory.java](ForkJoinPoolMemory.java) — Memory layout and visibility in Fork/Join
