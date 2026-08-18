# ExecutorService — Internals

## Overview

This directory explores the internal mechanics of `ExecutorService` — how tasks are queued, scheduled, and executed across threads.

## Key Internal Components

### ThreadPoolExecutor Internals

- **Worker threads**: Created on-demand up to core pool size, then to max pool size
- **Core vs non-core threads**: Core threads stay alive; non-core threads time out after `keepAliveTime`
- **Worker loop**: Each worker runs in a loop: get task from queue → execute → get next task
- **Task rejection**: When pool is full and queue is full, the rejection handler is invoked

### Execution Flow

```
submit(callable) → AbstractExecutorService.newTaskFor() → execute() → addWorker()
                                                              ↓
                                                        corePool full? → queue offer
                                                              ↓                ↓
                                                        maxPool full?   rejected? → reject policy
```

### Thread Creation

- Workers are `ThreadFactory`-created threads (default: `DefaultThreadFactory`)
- Each worker thread wraps a `Worker` object containing the task's `Runnable`
- Workers are stored in a `HashSet<Worker>` with a `mainLock` for synchronization

## Files

- [ExecutorServiceInternals.java](ExecutorServiceInternals.java) — ExecutorService execution mechanics
