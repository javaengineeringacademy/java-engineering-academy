# Callable & Future — Memory Model

## Overview

This directory explores how memory is managed in `Callable` and `Future` — result storage, exception handling, and thread visibility.

## Key Memory Concepts

### Result Storage

- `FutureTask.outcome` field stores the return value after completion
- Written once via `set()` — effectively immutable
- Volatile read in `get()` ensures visibility of the result

### Exception Storage

- Exceptions are stored in `FutureTask.exception` field
- `get()` wraps the original exception in `ExecutionException`
- Stack trace is preserved through the exception chain

### Memory Barriers

- Task submission establishes a happens-before edge: submit → task execution
- Task completion establishes a happens-before edge: task writes → Future.get() reads
- These guarantees are implemented via `volatile` state field and CAS operations

### Callable Capture

- Lambda capturing variables creates a reference chain: Callable → captured variables
- If captured objects are large, memory consumption increases per task
- Avoid capturing large objects unnecessarily

## Files

- [CallableFutureMemory.java](CallableFutureMemory.java) — Memory layout and visibility in Callable/Future
