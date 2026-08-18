# Scheduled Executor — Internals

## Overview

This directory explores how `ScheduledThreadPoolExecutor` works internally — task scheduling, delayed execution, and periodic task management.

## Key Internal Components

### ScheduledThreadPoolExecutor Internals

- Extends `ThreadPoolExecutor` with `DelayedWorkQueue` (a min-heap)
- Tasks are `ScheduledFutureTask` objects with a delay time
- Uses `System.nanoTime()` for time-based scheduling

### DelayedWorkQueue

- Priority queue ordered by execution time (earliest first)
- `take()` blocks until the head element is due
- `offer()` places the task and re-heapifies

### Execution Flow

```
schedule(task, delay) → new ScheduledFutureTask<>(task, triggerTime)
    → queue.offer() → worker thread takes task when due → task.run()
    → reschedule if periodic → done() → offer() again for next run
```

### Periodic Task Types

- **Fixed-rate**: `scheduleAtFixedRate()` — runs at regular intervals regardless of execution time
- **Fixed-delay**: `scheduleWithFixedDelay()` — waits `delay` after each execution completes
- If a task throws, subsequent runs are cancelled for that task

### Cancellation

- `Future.cancel(false)`: Removes from queue but does not interrupt running task
- `Future.cancel(true)`: Sets interrupt flag on the running thread
- `setRemoveOnCancelPolicy(true)`: Eagerly removes cancelled tasks from queue

## Files

- [ScheduledExecutorInternals.java](ScheduledExecutorInternals.java) — Scheduled executor execution mechanics
