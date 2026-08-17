# Thread Pool Configuration Decision Framework

## Pool Type Selection

| Workload | Pool Type | Queue | Max Threads |
|----------|-----------|-------|-------------|
| CPU-bound (computation) | Fixed | ArrayBlockingQueue | cores + 1 |
| I/O-bound (short tasks) | Cached | SynchronousQueue | Unlimited |
| I/O-bound (controlled) | Fixed | ArrayBlockingQueue | cores * wait/compute |
| Periodic/delayed | Scheduled | DelayedWorkQueue | cores + 1 |
| Recursive divide-and-conquer | ForkJoin | WorkStealingQueue | cores |

## Sizing Decision Tree

```
What type of work?
├── CPU-bound
│   └── poolSize = cores + 1
├── I/O-bound
│   └── poolSize = cores * (1 + waitTime / computeTime)
├── Mixed
│   └── poolSize = cores * targetUtilization * (1 + waitTime / computeTime)
└── Unknown
    └── Start with cores * 2, tune under load
```

## Queue Selection Decision

| Need | Queue | Trade-off |
|------|-------|-----------|
| Prevent OOM | ArrayBlockingQueue | Tasks rejected when full |
| No task loss | LinkedBlockingQueue | OOM risk under load |
| Direct handoff | SynchronousQueue | Need enough threads |
| Priority ordering | PriorityBlockingQueue | Starvation risk |
| Delayed execution | DelayedWorkQueue | Starvation risk |

## Rejection Policy Selection

| Policy | When to Use |
|--------|-------------|
| CallerRunsPolicy | Backpressure — slow down submission |
| AbortPolicy | Fail fast — caller must handle |
| DiscardOldestPolicy | Low-priority tasks can be dropped |
| DiscardPolicy | Fire-and-forget tasks |

## Monitoring Thresholds

| Metric | Warning | Critical |
|--------|---------|----------|
| Queue depth | > 50% capacity | > 80% capacity |
| Active threads | > 80% max | 100% max |
| Rejection rate | > 0 | > 1% |
| Task latency | > 2x baseline | > 5x baseline |
