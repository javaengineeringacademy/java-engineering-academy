# ExecutorService - Decision Guide

## Choosing an ExecutorService Factory Method

| Method | Core | Max | Queue | Use Case |
|--------|------|-----|-------|----------|
| newFixedThreadPool(n) | n | n | Unbounded | Stable load, known concurrency |
| newCachedThreadPool() | 0 | Integer.MAX_VALUE | Synchronous | Bursty, short-lived tasks |
| newSingleThreadExecutor() | 1 | 1 | Unbounded | Sequential, ordered execution |
| newScheduledThreadPool(n) | n | n | DelayedWorkQueue | Delayed/periodic tasks |

## Custom ThreadPoolExecutor Configuration

| Parameter | CPU-Bound | IO-Bound |
|-----------|-----------|----------|
| corePoolSize | numCPU + 1 | numCPU x (1 + wait/compute) |
| maxPoolSize | 2 x core | 4 x core |
| keepAliveTime | 60s | 60s |
| queueType | LinkedBlockingQueue | SynchronousQueue or ArrayBlockingQueue |
| rejectionPolicy | CallerRunsPolicy | CallerRunsPolicy |

## Rejection Policy Selection

| Policy | Behavior | Best For |
|--------|----------|----------|
| AbortPolicy (default) | Throws RejectedExecutionException | When submission failure is unacceptable |
| CallerRunsPolicy | Submitter thread runs task | Backpressure, no task loss |
| DiscardPolicy | Silently drops task | Non-critical tasks |
| DiscardOldestPolicy | Drops oldest queued task | Priority-based processing |

## Shutdown Decision Tree

```
Need to stop?
├── Graceful? → shutdown() → awaitTermination()
│   └── Still running? → shutdownNow()
└── Immediate? → shutdownNow()
    └── Tasks need compensation? → Handle in task code via Thread.interrupted()
```
