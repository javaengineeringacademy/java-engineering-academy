# Java Concurrency Framework Decision Guide

## ExecutorService Selection

| Pool Type | Queue | Best For | Risk |
|-----------|-------|----------|------|
| Fixed | LinkedBlockingQueue (unbounded) | CPU-bound | OOM |
| Cached | SynchronousQueue (none) | Short IO-bound | Thread explosion |
| Single | LinkedBlockingQueue | Sequential tasks | Bottleneck |
| Scheduled | DelayedWorkQueue | Periodic/delayed | Starvation |

## Pool Sizing

| Workload | Formula |
|----------|---------|
| CPU-bound | numCPUcores + 1 |
| IO-bound | numCPUcores × (1 + waitTime/computeTime) |
| Mixed | numCPUcores × targetUtilization × (1 + waitTime/computeTime) |

## When to Use Each

| Situation | Use |
|-----------|-----|
| Submit tasks, get results | ExecutorService + Future |
| Chain async operations | CompletableFuture |
| Divide-and-conquer | ForkJoinPool |
| Delayed/periodic tasks | ScheduledExecutorService |
| One-time barrier | CountDownLatch |
| Reusable barrier | CyclicBarrier |
