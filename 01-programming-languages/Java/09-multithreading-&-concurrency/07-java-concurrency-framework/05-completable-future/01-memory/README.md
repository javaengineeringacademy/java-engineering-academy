# CompletableFuture — Memory Model

## Overview

This directory explores how memory is managed in `CompletableFuture` — result storage, dependency chain memory, and completion propagation barriers.

## Key Memory Concepts

### Result Storage

- `Object result` field: stores the value, `AltResult` (null), or `AltResult` (exception)
- Written once via CAS on `result` field (volatile semantics)
- Subsequent reads use volatile read — no additional synchronization needed

### Dependency Chain Memory

- Each `thenApply`/`thenAccept`/`thenRun` creates a `UniCompletion` node
- Nodes are linked via the `stack` field (Treiber stack)
- After a node fires, it is removed from the stack — eligible for GC
- Deep chains create many short-lived objects

### Memory Barriers

- `complete()` / `completeExceptionally()`: volatile write to `result`
- `join()` / `get()`: volatile read from `result`
- This establishes a happens-before edge between completion and retrieval

### Cancellation and Cleanup

- `cancel(false)` sets the result to `CancellationException` without interrupting
- Dependent stages receive the cancellation as an exceptional completion
- Completed stages with no dependents can be garbage collected

### Memory Efficiency

- Each stage is a heap object (~64-128 bytes)
- Large dependency graphs (hundreds of stages) consume significant heap
- Consider using `thenCompose()` for flat composition to reduce object count

## Files

- [CompletableFutureMemory.java](CompletableFutureMemory.java) — Memory layout and visibility in CompletableFuture
