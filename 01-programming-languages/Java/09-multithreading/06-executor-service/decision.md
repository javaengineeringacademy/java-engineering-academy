# ExecutorService Decision Framework

## When to Use ExecutorService

| Scenario | Use ExecutorService? | Alternative |
|----------|---------------------|-------------|
| Multiple concurrent tasks | Yes | - |
| Single long-running task | No | Raw Thread |
| I/O-bound with massive concurrency | No | Virtual Threads (Java 21+) |
| Simple async composition | No | CompletableFuture |
| One-off task with no reuse | No | Raw Thread |
| Periodic/scheduled tasks | Yes | ScheduledExecutorService |
| Recursive divide-and-conquer | No | ForkJoinPool |

## Pool Type Decision

```
Is the task CPU-bound?
├── Yes → FixedThreadPool (cores + 1)
└── No (I/O-bound)
    ├── Tasks are short-lived? → CachedThreadPool
    ├── Need rate limiting? → FixedThreadPool with bounded queue
    └── Need scheduled execution? → ScheduledThreadPool
```

## Queue Type Decision

| Requirement | Queue | Risk |
|-------------|-------|------|
| Prevent OOM at all costs | ArrayBlockingQueue | Task rejection |
| High throughput, no rejection | LinkedBlockingQueue | OOM under load |
| Direct handoff (no buffering) | SynchronousQueue | Thread explosion |
| Priority ordering | PriorityBlockingQueue | Starvation |
| Delayed execution | DelayedWorkQueue | Starvation |

## Rejection Policy Decision

| Scenario | Policy | Behavior |
|----------|--------|----------|
| Must not lose tasks | CallerRunsPolicy | Backpressure on caller |
| OK to drop low-priority tasks | DiscardOldestPolicy | Drops oldest queued |
| OK to drop tasks silently | DiscardPolicy | Silent discard |
| Must know about rejections | AbortPolicy | Throws exception |

## Pool Sizing Decision

### CPU-Bound
```
poolSize = numCPUcores + 1
```

### I/O-Bound
```
poolSize = numCPUcores * (1 + waitTime / computeTime)
```

### Mixed Workload
```
poolSize = numCPUcores * targetUtilization * (1 + waitTime / computeTime)
```

## Shutdown Decision

| Situation | Strategy |
|-----------|----------|
| Normal shutdown | shutdown() + awaitTermination() |
| Must stop immediately | shutdownNow() |
| Graceful with deadline | shutdown() → awaitTermination() → shutdownNow() |

## Quick Reference

- **Multiple tasks + thread reuse** → ExecutorService
- **Single task** → Raw Thread
- **Massive I/O concurrency** → Virtual Threads
- **Async composition** → CompletableFuture
- **Recursive parallelism** → ForkJoinPool
