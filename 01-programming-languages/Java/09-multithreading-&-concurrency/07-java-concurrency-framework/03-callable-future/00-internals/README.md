# Callable & Future — Internals

## Overview

This directory explores how `Callable` and `Future` work internally — how results are produced, stored, and retrieved.

## Key Internal Components

### FutureTask Internals

- `FutureTask` implements `RunnableFuture< V>` (both `Runnable` and `Future`)
- States: `NEW` → `COMPLETING` → `NORMAL` / `EXCEPTIONAL` / `CANCELLED`
- Uses `Unsafe` CAS operations for thread-safe state transitions

### Execution Flow

```
executor.submit(callable) → new FutureTask<>(callable) → queue.add(task)
    → worker thread calls task.run() → callable.call() → set result
    → Future.get() returns result (or blocks if not yet complete)
```

### Blocking get()

- `Future.get()` without timeout: threads park in `WaitNode` linked list
- When task completes, all waiting threads are unparked
- Uses `LockSupport.unpark()` for efficient thread wake-up

### Cancellation

- `cancel(true)`: Sets interrupt flag on the running thread
- `cancel(false)`: Only prevents execution if not yet started
- `isCancelled()` returns true after cancellation

## Files

- [CallableFutureInternals.java](CallableFutureInternals.java) — Callable/Future execution mechanics
