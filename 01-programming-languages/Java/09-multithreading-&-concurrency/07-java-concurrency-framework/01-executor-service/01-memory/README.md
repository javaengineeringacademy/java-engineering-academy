# ExecutorService — Memory Model

## Overview

This directory explores how memory is managed in `ExecutorService` — task objects, result storage, and thread-local data interactions.

## Key Memory Concepts

### Task Object Lifecycle

- `submit()` creates a `FutureTask` wrapping the `Callable`/`Runnable`
- `FutureTask` is allocated on the heap and placed in the work queue
- After execution, the result is stored in `FutureTask.callable` and `outcome` fields
- The task object remains reachable until `Future.get()` is called and the result is consumed

### Result Memory

- `FutureTask.outcome` stores the return value (or exception)
- Written once, read once — effectively immutable after completion
- `CompletableFuture` result is stored in `Object result` field with volatile semantics

### Thread-Local Interactions

- Thread-local variables are per-thread, not per-task
- When using `InheritableThreadLocal`, child threads inherit the parent's value at creation time
- Virtual threads use copy-on-write semantics for inheritable thread-locals

### Memory Barriers

- `ExecutorService.submit()` establishes a happens-before edge: everything before submit is visible to the task
- Task completion establishes a happens-before edge: task's writes are visible to `Future.get()` callers

## Files

- [ExecutorServiceMemory.java](ExecutorServiceMemory.java) — Memory layout and visibility in executor service
