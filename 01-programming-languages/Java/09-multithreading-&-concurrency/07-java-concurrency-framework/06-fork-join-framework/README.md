# Fork-Join Framework

## Overview

`ForkJoinPool` is a specialized `ExecutorService` designed for **divide-and-conquer parallelism**. It efficiently processes large tasks by recursively splitting them into smaller subtasks, executing them in parallel, and combining results. The key innovation is **work stealing** — idle threads dynamically steal tasks from busy threads' deques, balancing load without centralized scheduling.

```
┌─────────────────────────────────────────────────┐
│                  ForkJoinPool                    │
│                                                  │
│  Thread 1 ──┬──► Task A ──┬──► SubTask A1       │
│             │             ├──► SubTask A2        │
│             │             └──► SubTask A3        │
│                                                  │
│  Thread 2 ──┬──► Task B ──┬──► SubTask B1       │
│             │             └──► SubTask B2        │
│                                                  │
│  Thread 3 ──(idle)──steal──► SubTask A3          │
└─────────────────────────────────────────────────┘
```

## ForkJoinTask Hierarchy

```
ForkJoinTask<V>                     (abstract base)
├── RecursiveTask<V>               (returns a value)
├── RecursiveAction                (no return value)
└── CountedCompleter<V>            (completion-triggered)
```

### Core Methods

| Method | Description |
|--------|-------------|
| `fork()` | Schedules task for asynchronous execution |
| `join()` | Waits for task result, blocks current thread |
| `invoke()` | Fork + join in one call |
| `compute()` | Abstract — implement task logic here |
| `isCompletedNormally()` | Check if task finished without exception |

## RecursiveTask\<T\> — Returns a Value

`RecursiveTask<T>` computes and returns a result of type `T`. Ideal for operations where subtask results must be combined.

```java
class SumTask extends RecursiveTask<Long> {
    private static final int THRESHOLD = 1000;
    private final long[] array;
    private final int start, end;

    SumTask(long[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        // Base case: small enough to compute directly
        if (end - start <= THRESHOLD) {
            long sum = 0;
            for (int i = start; i < end; i++) sum += array[i];
            return sum;
        }

        // Recursive case: split into subtasks
        int mid = (start + end) / 2;
        SumTask left = new SumTask(array, start, mid);
        SumTask right = new SumTask(array, mid, end);

        left.fork();                          // execute left asynchronously
        long rightResult = right.compute();   // compute right in current thread
        long leftResult = left.join();        // wait for left result

        return leftResult + rightResult;
    }
}
```

### Key Pattern: Fork One, Compute One

```
                    compute()
                   /         \
              fork(left)   compute(right)
                  |              |
              [async]        [sync]
                  |              |
              join(left)        |
                  \            /
              return left + right
```

Always fork **one** subtask and compute the **other** in the current thread. This avoids unnecessary overhead and utilizes the calling thread.

## RecursiveAction — No Return Value

`RecursiveAction` performs work without returning a value. Use it for side-effect operations like parallel array updates, file processing, or data transformations.

```java
class ParallelUpdater extends RecursiveAction {
    private static final int THRESHOLD = 1000;
    private final double[] array;
    private final int start, end;

    ParallelUpdater(double[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        if (end - start <= THRESHOLD) {
            for (int i = start; i < end; i++) {
                array[i] = Math.sqrt(array[i]);
            }
            return;
        }
        int mid = (start + end) / 2;
        invokeAll(                    // fork both, wait for both
            new ParallelUpdater(array, start, mid),
            new ParallelUpdater(array, mid, end)
        );
    }
}
```

### invokeAll() Convenience

`invokeAll(task1, task2)` forks both tasks then waits for completion. Shorthand for:
```java
task1.fork();
task2.fork();
task1.join();
task2.join();
```

## RecursiveEnumerationTask — Returns Vector

`RecursiveEnumerationTask<T>` returns a `Vector<T>` of enumerated results. Useful when the result set size is dynamic.

```java
class FileSearcher extends RecursiveEnumerationTask<Path> {
    private final File directory;
    private final String extension;

    FileSearcher(File directory, String extension) {
        this.directory = directory;
        this.extension = extension;
    }

    @Override
    protected void compute() {
        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                taskSubmit(new FileSearcher(file, extension));  // fork subtask
            } else if (file.getName().endsWith(extension)) {
                enumerate(file.toPath());                        // add to result
            }
        }
    }
}
```

## Fork/Join Workflow

```
1. create Task ──► 2. invoke(ForkJoinPool) ──► 3. compute()
                                                      │
                                              ┌───────┴───────┐
                                              │  Threshold     │
                                              │  Reached?      │
                                              └───────┬───────┘
                                                  YES │    │ NO
                                              ┌───────┴────┐ │
                                              │  Compute   │ │
                                              │  directly  │ │
                                              └───────┬────┘ │
                                                      │  ┌───┴───┐
                                                      │  │ Split  │
                                                      │  │ into 2 │
                                                      │  └───┬───┘
                                                      │      │
                                              ┌───────┴──────┴──┐
                                              │  fork(left)     │
                                              │  compute(right) │
                                              └───────┬────────┘
                                                      │
                                              ┌───────┴────┐
                                              │  join(left)│
                                              │  combine   │
                                              └───────┬────┘
                                                      │
                                              ┌───────┴────┐
                                              │  return    │
                                              │  result    │
                                              └────────────┘
```

### Step-by-Step

| Step | Action | Thread |
|------|--------|--------|
| 1 | Create `RecursiveTask` or `RecursiveAction` | Caller |
| 2 | Submit via `pool.invoke()` or `pool.execute()` | Caller |
| 3 | `compute()` is called | Worker |
| 4 | Check threshold — if small enough, compute directly | Worker |
| 5 | If above threshold: create 2 subtasks | Worker |
| 6 | `fork()` one subtask (async) | Worker |
| 7 | `compute()` the other subtask (sync) | Worker |
| 8 | `join()` to get forked result | Worker |
| 9 | Combine and return | Worker |

## When to Split vs Compute

### Split When:
- Task size exceeds `THRESHOLD`
- Subtasks are independent (no shared mutable state)
- Result can be combined from sub-results (associative operation)
- Parallelism provides measurable speedup

### Compute Directly When:
- Task is small enough for sequential execution
- Overhead of forking exceeds computation cost
- Task has dependencies on other tasks
- I/O bound (ForkJoinPool doesn't help with blocking I/O)

### Choosing THRESHOLD

| Workload | Suggested Threshold |
|----------|-------------------|
| Simple arithmetic | 10,000–100,000 |
| Object traversal | 1,000–10,000 |
| Complex computation | 100–1,000 |
| I/O operations | Avoid ForkJoinPool |

## Work Stealing Algorithm

```
┌───────────────────────────────────────────┐
│           Work Stealing Process           │
├───────────────────────────────────────────┤
│                                           │
│  Each Thread has its own Deque:           │
│                                           │
│  Thread 1: [A1][A2][A3] ← own tasks      │
│  Thread 2: [B1][B2]      ← own tasks     │
│  Thread 3: []             ← idle!         │
│                                           │
│  Thread 3 steals from Thread 1's deque:   │
│  Thread 3: [A3] ← stolen! (takes bottom) │
│                                           │
│  Result: Thread 1 has [A1][A2]            │
│          Thread 3 has [A3]                │
└───────────────────────────────────────────┘
```

**LIFO for own tasks** (split), **FIFO for stolen tasks** (top of deque) — ensures locality and fairness.

## ForkJoinPool vs ExecutorService

| Feature | ForkJoinPool | ExecutorService |
|---------|-------------|-----------------|
| Task type | Recursive, divide-and-conquer | Independent, unrelated |
| Scheduling | Work stealing | Work queuing |
| Thread use | Blocks only on `join()` | May block on `get()` |
| Best for | CPU-bound, recursive | Mixed workloads |
| Parallelism | `Runtime.getRuntime().availableProcessors()` | Configurable pool size |

## Best Practices

1. **Threshold tuning**: Too low = overhead; too high = poor parallelism
2. **Fork one, compute one**: Avoid forking both subtasks
3. **Use `invokeAll()`** for RecursiveAction when both subtasks are independent
4. **Avoid blocking operations** inside `compute()`
5. **Don't create new pools** per task — reuse a shared `ForkJoinPool`
6. **CommonPool** (`ForkJoinPool.commonPool()`) for lightweight tasks

## Files in This Directory

| File | Description |
|------|-------------|
| `examples/WorkStealingExample.java` | Work stealing demo with performance comparison |
| `examples/RecursiveTaskExample.java` | Complete RecursiveTask parallel sum |
| `examples/RecursiveActionExample.java` | Parallel array processing |
| `examples/ForkJoinVsExecutorService.java` | Head-to-head performance comparison |
| `practices/Practices.java` | 3 exercises to solve |
| `solutions/Solutions.java` | Complete solutions |

## Quick Start

```java
import java.util.concurrent.*;

// 1. Define task
class MyTask extends RecursiveTask<Long> {
    private static final int THRESHOLD = 1000;

    @Override
    protected Long compute() {
        if (/* base case */) return directComputation();
        // split, fork, compute, join
    }
}

// 2. Create pool and submit
ForkJoinPool pool = new ForkJoinPool();
long result = pool.invoke(new MyTask(...));
pool.shutdown();
```
