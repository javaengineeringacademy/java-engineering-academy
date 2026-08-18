# Thread Pools — Internals

## Overview

This directory explores how thread pools work internally — how threads are managed, tasks are queued, and resources are reclaimed.

## Key Internal Components

### Pool Sizing

- **Core pool size**: Minimum threads kept alive (even if idle)
- **Maximum pool size**: Upper thread limit when queue is full
- **Keep alive time**: How long non-core threads wait for work before termination
- **Queue capacity**: Number of tasks that can wait when all threads are busy

### Work Queue Types

| Queue | Type | Behavior |
|-------|------|----------|
| `LinkedBlockingQueue` | Unbounded | Can cause OOM if tasks are added faster than consumed |
| `ArrayBlockingQueue` | Bounded | Backpressure when full |
| `SynchronousQueue` | Zero capacity | Direct handoff — every submit needs an idle thread |
| `PriorityBlockingQueue` | Unbounded priority | Tasks execute by priority, not FIFO |

### Thread Lifecycle in Pool

```
Idle → getTask() returns null → thread terminates
Idle → getTask() returns task → execute task → getTask() again
```

### Monitoring

- `getActiveCount()`: Currently executing tasks
- `getPoolSize()`: Current thread count
- `getQueue().size()`: Pending tasks

## Files

- [ThreadPoolInternals.java](ThreadPoolInternals.java) — Thread pool execution mechanics
