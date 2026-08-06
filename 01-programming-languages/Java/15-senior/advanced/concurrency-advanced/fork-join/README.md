# Fork/Join Framework

## Core Concepts

The Fork/Join framework enables parallel divide-and-conquer algorithms using
work-stealing thread pools. Introduced in Java 7 as part of `java.util.concurrent`.

### Key Classes

| Class | Description |
|-------|-------------|
| `ForkJoinPool` | Specialized executor for fork/join tasks |
| `RecursiveTask<T>` | Task that returns a value of type T |
| `RecursiveAction` | Task with no return value |

### Work-Stealing Algorithm

Each thread maintains its own deque (double-ended queue):
- Tasks are pushed/popped from the **tail** (LIFO)
- Idle threads **steal** from the **head** of other deques (FIFO)
- Reduces contention and improves load balancing

```
Thread A: [task3, task2, task1]  <- pop from tail
Thread B: []                     <- steal from head of A
Thread C: [task6, task5, task4]
```

---

## When to Use vs ExecutorService

| Use Fork/Join When | Use ExecutorService When |
|-------------------|------------------------|
| Problem can be recursively divided | Tasks are independent |
| Subtasks depend on each other | No natural divide/decode |
| Workload is unevenly distributed | Uniform task execution |
| You need work-stealing efficiency | Simple task submission |

### Fork/Join Best Practices

1. **Set a threshold** for when to stop dividing
2. **Fork one branch, compute the other** (avoids unnecessary forking)
3. **Use `invokeAll`** when both subtasks need to run
4. **Avoid blocking** inside compute methods
5. **Don't fork small tasks** (overhead exceeds benefit)

---

## Parallel Streams vs Fork/Join

### Parallel Streams

```java
long sum = LongStream.rangeClosed(1, 1_000_000)
    .parallel()
    .sum();
```

- Uses `ForkJoinPool.commonPool()` by default
- Best for simple data-parallel operations
- No control over parallelism level

### Fork/Join Direct

```java
ForkJoinPool customPool = new ForkJoinPool(8);
customPool.invoke(new ParallelSum(array, 0, array.length));
```

- Custom parallelism and pool configuration
- Full control over task decomposition
- Better for complex divide-and-conquer algorithms

### When to Choose

| Scenario | Choice |
|----------|--------|
| Simple data processing | Parallel streams |
| Complex algorithm (merge sort) | Fork/Join |
| Custom thread pool needed | Fork/Join |
| Quick parallel aggregation | Parallel streams |
| Fine-grained control needed | Fork/Join |

---

## Common Pitfalls

1. **Using common ForkJoinPool for blocking I/O** - blocks a carrier thread
2. **Too much forking** - overhead exceeds parallelism benefit
3. **Returning null from compute** - use `RecursiveAction` instead
4. **Not joining forked tasks** - leads to incomplete results
5. **Sharing mutable state** between tasks without synchronization

## Interview Questions

[5-10 interview questions with answers]

1. **What is this concept?**
   [Answer]

2. **When would you use it?**
   [Answer]

3. **What are the alternatives?**
   [Answer]

4. **What are common mistakes?**
   [Answer]

5. **How does it perform compared to alternatives?**
   [Answer]

## Performance

[Performance considerations and benchmarks]

## Examples

[Code examples demonstrating the concept]

## Internal Working

[How this works under the hood]

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Overview

[Brief description of the topic]

## Pitfalls

[Common mistakes and anti-patterns]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
