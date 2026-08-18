# Thread Pools - Decision Guide

## Pool Type Selection

| Criteria | Fixed | Cached | Single | Scheduled |
|----------|-------|--------|--------|-----------|
| Stable concurrency | Yes | No | No | Yes |
| Burst traffic | Poor | Excellent | Poor | No |
| Task ordering | No | No | Sequential | No |
| Delayed tasks | No | No | No | Yes |
| Resource control | Good | Poor | Good | Good |
| Production ready | Yes | Risky | Limited | Yes |

## Thread Count Sizing

| Workload Type | Formula | Example (8 cores) |
|---------------|---------|-------------------|
| CPU-bound | cores + 1 | 9 threads |
| IO-bound | cores x (1 + wait/compute) | 8 x (1 + 4) = 40 |
| Mixed | cores x targetUtil x (1 + wait/compute) | 8 x 0.7 x 5 = 28 |

## Queue Selection

| Queue | Capacity | Blocking | Use Case |
|-------|----------|----------|----------|
| LinkedBlockingQueue | Integer.MAX | On take | Fixed pools, high throughput |
| ArrayBlockingQueue | Fixed | On put and take | Bounded memory, backpressure |
| SynchronousQueue | 0 | On put | Cached pool, direct handoff |
| PriorityBlockingQueue | Unbounded | On take | Priority-based execution |

## Monitoring Metrics to Track

| Metric | Healthy Range | Action if Out of Range |
|--------|---------------|----------------------|
| activeCount / poolSize | < maxPoolSize | Increase max or optimize tasks |
| queue.size() | < queue capacity | Increase pool or queue |
| completedTaskCount | Growing steadily | Normal |
| largestPoolSize | < maxPoolSize | Normal |
