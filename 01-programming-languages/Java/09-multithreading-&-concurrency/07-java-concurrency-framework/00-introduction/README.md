# Concurrency Framework — Internals

## Overview

This directory explores the internal implementation details of Java's concurrency framework — how `ExecutorService`, thread pools, `CompletableFuture`, and `ForkJoinPool` work under the hood.

## Key Internal Components

### ThreadPoolExecutor Internals

- **Core pool**: Fixed number of threads kept alive waiting for tasks
- **Work queue**: `BlockingQueue<Runnable>` holding pending tasks
- **Maximum pool size**: Upper limit on threads when core is full and queue is full
- **Rejection policy**: What happens when both pool and queue are full
- **Rejection policies**: `AbortPolicy` (default, throws exception), `CallerRunsPolicy` (submits to caller thread), `DiscardPolicy` (silently drops), `DiscardOldestPolicy` (drops oldest queued task)

### ForkJoinPool Internals

- **Work-stealing**: Idle threads steal tasks from busy threads' deque
- **Task splitting**: `ForkJoinTask.fork()` splits large tasks recursively
- **Common pool**: `ForkJoinPool.commonPool()` used by parallel streams
- **Managed blocking**: Threads block only when pool permits it

### CompletableFuture Internals

- **Dependency graph**: Each stage is a node in a DAG of dependent operations
- **Completion stack**: Actions triggered when a stage completes
- **Executor binding**: By default uses `ForkJoinPool.commonPool()`, can be customized

## Files

- [ForkJoinPoolInternals.java](ForkJoinPoolInternals.java) — Fork/Join pool mechanics
