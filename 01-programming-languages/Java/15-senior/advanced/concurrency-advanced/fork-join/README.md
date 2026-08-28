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

## Overview

The Fork/Join framework is a specialized `ExecutorService` designed for divide-and-conquer parallel algorithms. Introduced in Java 7 (`java.util.concurrent.forkjoinpool`), it uses work-stealing to dynamically balance load across threads. Tasks extend `RecursiveTask<T>` (returns a value) or `RecursiveAction` (no return value) and recursively split work until a threshold is reached.

## Interview Questions

1. **How does work-stealing differ from work-sharing? What are the performance implications?**
   Work-sharing distributes tasks from a central queue to idle threads, causing contention on the queue lock. Work-stealing gives each thread its own deque: tasks are pushed/popped from the tail (LIFO for cache locality), and idle threads steal from the head of other deques (FIFO for load balancing). Work-stealing scales better because (1) most tasks are produced/consumed by the same thread (zero contention), (2) stealing only happens when threads are idle, and (3) deque operations are lock-free via `volatile` arrays with CAS.

2. **What is the optimal threshold for stopping recursion?**
   The threshold determines granularity: too low creates excessive task overhead (~100ns per fork/join), too high underutilizes parallelism. Rule of thumb: each leaf task should take 100μs-1ms. For compute-bound tasks: threshold = total_size / (parallelism × 4). For merge sort: typically 10,000-50,000 elements. Profile with `ForkJoinPool` metrics (`getStealCount()`, `getRunningThreadCount()`) to tune.

3. **When should you use `invokeAll` vs `fork` + `join`?**
   `invokeAll(f1, f2)` submits both tasks and waits for both — it's equivalent to `f1.fork(); f2.compute(); f1.join()` but clearer. Use `invokeAll` when both subtasks should execute. Use `fork` + explicit `join` when you want to compute one branch in the current thread (avoids one unnecessary fork). The pattern `left.fork(); right.compute(); left.join()` avoids one task queuing overhead.

4. **How does `ForkJoinPool.ManagedBlocker` work and when do you need it?**
   `ManagedBlocker` allows tasks to block without reducing parallelism. When a `ForkJoinWorkerThread` blocks, the pool compensates by creating or waking another thread. Implement `isReleasable()` (returns true if not blocking) and `block()` (performs the blocking operation). Required when fork/join tasks perform blocking I/O. Without it, blocking a worker thread reduces the pool's effective parallelism.

5. **What are the common Fork/Join anti-patterns?**
   - Forking both branches (wasteful — compute one, fork the other)
   - Returning null from `compute()` (use `RecursiveAction` instead)
   - Blocking inside `compute()` without `ManagedBlocker`
   - Setting threshold too low (overhead exceeds parallelism benefit)
   - Sharing mutable state between tasks without synchronization
   - Using the common `ForkJoinPool` for blocking I/O tasks

## Performance

**Parallel merge sort benchmarks (1M elements, 8 cores):**

| Implementation | Time | Speedup vs sequential |
|---------------|------|----------------------|
| Sequential merge sort | 320ms | 1x |
| `ExecutorService` + 8 threads | 85ms | 3.8x |
| `ForkJoinPool` parallelism=8 | 48ms | 6.7x |
| Parallel streams | 52ms | 6.2x |
| `Arrays.parallelSort()` | 45ms | 7.1x |

**Fork/Join overhead:**
- Task creation: ~50-100ns
- `fork()`: ~100-200ns
- `join()`: ~100-200ns
- Work steal: ~300-500ns (cross-thread)

**Optimal parallelism:** `Runtime.getRuntime().availableProcessors()` for CPU-bound tasks. For mixed workloads: `cores × (1 + blocking_time/compute_time)`.

**Steal count ratio:** `pool.getStealCount() / pool.getParallelism()` — ratio > 1 indicates good load balancing; > 5 suggests threshold is too high.

## Examples

### Parallel Array Map-Reduce
```java
public class ParallelMapReduce<T, R> extends RecursiveTask<R> {
    private static final int THRESHOLD = 1000;
    private final List<T> data;
    private final Function<T, R> mapper;
    private final BinaryOperator<R> reducer;
    private final int lo, hi;

    public ParallelMapReduce(List<T> data, Function<T, R> mapper,
                             BinaryOperator<R> reducer, int lo, int hi) {
        this.data = data; this.mapper = mapper;
        this.reducer = reducer; this.lo = lo; this.hi = hi;
    }

    @Override
    protected R compute() {
        if (hi - lo <= THRESHOLD) {
            R result = mapper.apply(data.get(lo));
            for (int i = lo + 1; i < hi; i++) {
                result = reducer.apply(result, mapper.apply(data.get(i)));
            }
            return result;
        }
        int mid = (lo + hi) / 2;
        ParallelMapReduce<T, R> left = new ParallelMapReduce<>(data, mapper, reducer, lo, mid);
        ParallelMapReduce<T, R> right = new ParallelMapReduce<>(data, mapper, reducer, mid, hi);
        left.fork();
        R rightResult = right.compute();
        R leftResult = left.join();
        return reducer.apply(leftResult, rightResult);
    }

    public R execute() {
        ForkJoinPool pool = new ForkJoinPool();
        try {
            return pool.invoke(this);
        } finally {
            pool.shutdown();
        }
    }
}
```

### Parallel File Processing with ManagedBlocker
```java
public class ParallelFileProcessor extends RecursiveAction {
    private final Path directory;
    private final Consumer<Path> processor;

    public ParallelFileProcessor(Path directory, Consumer<Path> processor) {
        this.directory = directory;
        this.processor = processor;
    }

    @Override
    protected void compute() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            List<ParallelFileProcessor> tasks = new ArrayList<>();
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    tasks.add(new ParallelFileProcessor(entry, processor));
                } else {
                    processFile(entry); // May block on I/O
                }
            }
            invokeAll(tasks);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void processFile(Path file) {
        ForkJoinPool.managedBlock(() -> {
            // Blocking I/O — managed pool compensates
            String content = Files.readString(file);
            processor.accept(file);
            return null;
        });
    }
}
```

### Recursive Action for Tree Traversal
```java
public class ParallelTreeTraversal<T> extends RecursiveAction {
    private final TreeNode<T> node;
    private final Consumer<TreeNode<T>> action;

    public ParallelTreeTraversal(TreeNode<T> node, Consumer<TreeNode<T>> action) {
        this.node = node;
        this.action = action;
    }

    @Override
    protected void compute() {
        action.accept(node);
        List<ParallelTreeTraversal<T>> children = node.children().stream()
            .map(child -> new ParallelTreeTraversal<>(child, action))
            .collect(Collectors.toList());
        invokeAll(children);
    }
}
```

## Internal Working

**WorkQueue implementation:**
Each `ForkJoinWorkerThread` has a `WorkQueue` — a `volatile` array-based deque. `push()` adds to the tail (index `top`), `pop()` removes from the tail, `poll()` steals from the head (index `base`). Arrays use `volatile` stores; CAS is used only for contention (most operations are uncontended).

**Work stealing algorithm:**
1. `ForkJoinWorkerThread.scan()` starts at a random index and scans all queues
2. `WorkQueue.trySteal()` takes the oldest task from the victim's head
3. If no work found, thread parks (via `LockSupport.park`) after `awaitWork()` timeout
4. `signalWork()` wakes parked threads when new tasks are submitted

**Task execution:**
`ForkJoinTask.doExec()` runs the task's `compute()` method. If the task forks subtasks, they're pushed to the current thread's deque. After forking, the thread can continue with other work (the forked task is picked up by stealers or the thread itself when it runs out of local work).

**ForkJoinPool internals:**
- `ctl` field: 64-bit value encoding parallelism, thread count, and steal count
- `acquireLock()`/`releaseLock()`: striped locks for thread management
- `submit()`/`invoke()`: pushes task to submitting thread's deque or creates a new worker
- `commonPool`: static pool for parallel streams and `CompletableFuture`; sized to `Runtime.availableProcessors() - 1`

**Memory layout:**
`ForkJoinTask` objects are stored as `volatile Object[]` entries in `WorkQueue`. Each entry is either a task or a negative marker (for signaling). The `qlock` field provides CAS-based locking for steal operations.

## Why This Concept Exists

Parallel algorithms naturally decompose into subproblems (merge sort, quicksort, tree traversal, matrix multiplication). The Fork/Join framework exists because:

1. **Recursive decomposition**: Divide-and-conquer algorithms need a way to split work into subtasks and combine results. Manual thread creation for each split is impractical.
2. **Load balancing**: Real workloads have uneven task sizes. Work-stealing automatically redistributes work from busy threads to idle ones.
3. **Scalability**: Sequential algorithms don't scale with cores. Fork/Join provides near-linear speedup for divide-and-conquer problems.
4. **Efficiency**: Thread creation is expensive (~1MB stack). Fork/Join reuses a fixed set of threads, amortizing creation cost.
5. **Parallel streams**: Java 8 parallel streams use `ForkJoinPool.commonPool()` under the hood. Understanding Fork/Join is essential for tuning parallel stream performance.

Before Fork/Join, developers used `ExecutorService` with manual task splitting, which lacked work-stealing and required complex load-balancing logic.

## Pitfalls

- **Using `ForkJoinPool.commonPool()` for blocking I/O** — it has fixed parallelism (cores-1), and blocking one thread blocks the entire pool
- **Not setting a threshold** — infinite recursion causes `StackOverflowError`
- **Both branches fork** — always compute one branch inline and fork the other to avoid unnecessary task creation
- **Returning null from `compute()`** — use `RecursiveAction` for tasks with no return value
- **Not joining forked tasks** — leads to incomplete results and memory leaks
- **Sharing mutable state** between tasks without synchronization — each task should operate on its own data or use atomics
- **Calling `join()` before `fork()`** — deadlock! Always fork first, then compute locally, then join

## References

- [Doug Lea: A Fork/Join Framework for Java](http://gee.cs.oswego.edu/dl/papers/fj.pdf) — original paper
- [Oracle: ForkJoinPool Javadoc](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ForkJoinPool.html)
- [OpenJDK ForkJoinPool Source](https://hg.openjdk.java.net/jdk8u/jdk8u/jdk/file/tip/src/share/classes/java/util/concurrent/ForkJoinPool.java)
- [Brian Goetz: Java Concurrency in Practice, Chapter 8](https://jcip.net/)
- [Aleksey Shipilëv: Fork/Join Benchmarks](https://shipilev.net/)
- [Oracle: Parallel Sorting with Fork/Join](https://docs.oracle.com/javase/8/docs/technotes/guides/concurrency/)
