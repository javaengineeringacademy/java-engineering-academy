# Fork-Join Framework - Decision Guide

## When to Use ForkJoinPool vs ExecutorService

| Scenario | ForkJoinPool | ExecutorService |
|----------|-------------|-----------------|
| Divide-and-conquer problems | Excellent | Poor |
| Independent tasks | Poor | Excellent |
| Recursive splitting | Excellent (work-stealing) | N/A |
| Simple thread pool needs | Overkill | Appropriate |
| Parallel stream operations | Yes (commonPool) | No |

## RecursiveTask vs RecursiveAction

| Criteria | RecursiveTask | RecursiveAction |
|----------|---------------|-----------------|
| Returns value | Yes (V) | No (void) |
| Use when | Need result from subtasks | Side-effect only |
| Example | Parallel sum | Parallel sort |

## ForkJoinPool Configuration

| Parameter | Default | Guidance |
|-----------|---------|----------|
| parallelism | Runtime.availableProcessors() | Set to cores for CPU-bound |
| ForkJoinPool.defaultForkJoinPool | 2 | Use factory for custom pools |
| asyncMode | false | true for FIFO scheduling |

## Work-Stealing Decision Points

| Condition | Recommendation |
|-----------|---------------|
| Task splits into 2+ subtasks | Use fork/join |
| Task is leaf (no split) | Use compute() directly |
| Subtask size > threshold | Keep splitting |
| Subtask size <= threshold | Compute directly |
| Many small tasks | Batch to reduce fork overhead |

## Common Pitfalls

| Pitfall | Solution |
|---------|----------|
| Forking without joining | Always join() forked tasks |
| Excessive forking | Set minimum computation size |
| Using commonPool for blocking IO | Create dedicated ForkJoinPool |
| Calling pool.managedBlock() incorrectly | Ensure block() is called when task is blocking |
