# Thread Pools

## Pool Types

| Pool | Queue | Best For | Risk |
|------|-------|----------|------|
| Fixed | Unbounded | CPU-bound | OOM |
| Cached | None | Short IO-bound | Thread explosion |
| Single | Unbounded | Sequential | Bottleneck |
| Scheduled | DelayedWorkQueue | Periodic | Starvation |

## Sizing

- **CPU-bound**: `numCPUcores + 1`
- **IO-bound**: `numCPUcores × (1 + waitTime/computeTime)`

## Monitoring

```java
ThreadPoolExecutor pool = (ThreadPoolExecutor) executor;
pool.getActiveCount();
pool.getPoolSize();
pool.getQueue().size();
pool.getCompletedTaskCount();
pool.getLargestPoolSize();
```
