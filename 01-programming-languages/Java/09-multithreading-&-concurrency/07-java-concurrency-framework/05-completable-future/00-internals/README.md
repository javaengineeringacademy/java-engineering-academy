# CompletableFuture Internals

## Overview

This directory explores how `CompletableFuture` works internally — its dependency graph, completion propagation, and thread scheduling.

## Key Internal Components

### Internal State

- `result`: Stores the value (or `AltResult` for null/exception)
- `stack`: Linked list of dependent `UniCompletion` nodes
- `Treiber stack` for completions — lock-free push/pop of dependent actions

### Completion Propagation

- When a stage completes, it traverses the `stack` of dependents
- Each dependent stage is triggered via `UniCompletion.tryFire()`
- Dependent stages execute inline if the triggering thread is fast, or are submitted to an executor

### Execution Flow

```
thenApply(fn) → creates UniApply node → pushes onto caller's stack
    → when caller completes → UniApply.tryFire() → fn.apply(result)
    → stores result in this stage's result → triggers next dependents
```

### Thread Choice

- If an executor is specified, the dependent stage runs on that executor
- If no executor, the completing thread may execute the dependent stage (inline)
- `ForkJoinPool.commonPool()` is the default executor

### Exception Handling

- Exceptional completion stores an `AltResult` containing the exception
- Dependent stages propagate the exception unless a handler (`exceptionally`, `handle`) catches it
- `exceptionallyCompose()` allows async fallback

## Files

- [CompletableFutureInternals.java](CompletableFutureInternals.java) — CompletableFuture execution mechanics
