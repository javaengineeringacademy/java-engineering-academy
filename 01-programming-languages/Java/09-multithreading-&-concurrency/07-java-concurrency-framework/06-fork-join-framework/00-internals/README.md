# Fork/Join Framework — Internals

## Overview

This directory explores how `ForkJoinPool` and `ForkJoinTask` work internally — work-stealing, task splitting, and parallel execution.

## Key Internal Components

### ForkJoinPool Internals

- **Work-stealing**: Each worker thread has its own deque. Idle threads steal from the tail of other threads' deques.
- **Submission queue**: External tasks go into a `ConcurrentLinkedQueue` (external queue)
- **Worker threads**: `ForkJoinWorkerThread` instances, each with a `WorkQueue`
- **Common pool**: Shared pool for parallel streams, `CompletableFuture` defaults

### Work-Stealing Algorithm

```
Worker deque:  [task1] [task2] [task3]  ← push/pop at top
                                          ↑
                        steal from bottom (other threads)
```

- Push/pop from the "top" (LIFO for locality)
- Steal from the "bottom" (FIFO for load balancing)

### ForkJoinTask Internals

- **fork()**: Pushes the task onto the current worker's deque
- **join()**: If the joined task is from another thread, help complete it (work-stealing)
- **invoke()**: fork + join in one call
- Uses `status` field with CAS for completion tracking

### Task Splitting

- `RecursiveTask<V>`: Returns a result
- `RecursiveAction`: No result
- Good splitting: ~1000-10000 base cases per split
- Too fine: overhead dominates. Too coarse: poor parallelism.

### Managed Blocking

- `ForkJoinPool.ManagedBlocker` allows virtual threads to block without wasting carrier threads
- Used internally by parallel streams for I/O operations

## Files

- [ForkJoinPoolInternals.java](ForkJoinPoolInternals.java) — Fork/Join execution mechanics
