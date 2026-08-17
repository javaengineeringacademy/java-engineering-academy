# Multithreading Decision Guide

## When to Use Threads vs Virtual Threads

### Use Platform Threads When:
- Task count is small and fixed (e.g., 4-8 threads for CPU-bound work)
- Tasks need thread-local resources that cannot be shared
- You need tight control over thread priority and scheduling
- Tasks involve long-running native code or JNI calls
- You are on Java versions before 21

### Use Virtual Threads When:
- Task count is large (thousands to millions of concurrent tasks)
- Tasks are I/O-bound (network calls, file I/O, database queries)
- You want simplified concurrency without thread pool tuning
- You are migrating from blocking I/O code and want minimal changes
- You want to reduce memory footprint per concurrent task

### Key Difference
| Aspect | Platform Thread | Virtual Thread |
|---|---|---|
| Memory per thread | ~1 MB stack | ~1-2 KB (grows as needed) |
| Max practical count | Thousands | Millions |
| Blocking behavior | Ties up OS thread | Frees carrier thread |
| Scheduling | OS scheduler | JVM scheduler |
| Best for | CPU-bound, long tasks | I/O-bound, short tasks |

---

## When to Use `synchronized` vs `Lock`

### Use `synchronized` When:
- You need a simple mutual exclusion (only one critical section)
- The critical section is short and unlikely to be held for long
- You want automatic lock release (no risk of forgetting to unlock)
- You are protecting a single condition with wait/notify
- Readability matters more than advanced features

### Use `Lock` (ReentrantLock, ReadWriteLock) When:
- You need try-lock (attempt to acquire without blocking)
- You need timed lock acquisition (tryLock with timeout)
- You need to lock/unlock across methods or in try-finally blocks
- You have multiple conditions on the same lock (Condition objects)
- You need fair ordering of lock acquisition
- You want to interrupt a thread waiting to acquire the lock
- You need read-write separation (ReadWriteLock for read-heavy workloads)

### Trade-offs
| Feature | `synchronized` | `Lock` |
|---|---|---|
| Syntax complexity | Simple | Requires explicit unlock |
| Auto-release | Yes | No (must use finally) |
| Try-lock | No | Yes |
| Timed lock | No | Yes |
| Interruptible lock | No | Yes |
| Multiple conditions | No (one wait set) | Yes (Condition objects) |
| Fairness policy | No | Configurable |
| Performance (JVM optimized) | Better for simple cases | Better for complex/contended cases |

---

## When to Use `ExecutorService` vs `CompletableFuture`

### Use `ExecutorService` When:
- You want to submit and manage a fixed or bounded set of tasks
- You need a thread pool with fine-grained control (core/max size, queue)
- You want to schedule periodic or delayed tasks
- You need to shut down cleanly with orderly task completion
- You are implementing a producer-consumer pattern
- You want to control backpressure via pool size

### Use `CompletableFuture` When:
- You want to compose multiple async operations (thenApply, thenCompose)
- You need chaining and pipeline-style async workflows
- You want to combine results from multiple independent tasks (allOf, anyOf)
- You want non-blocking transformations with callbacks
- You are building reactive or event-driven pipelines
- You want cleaner error propagation across async chains

### Combine Both When:
- You need an ExecutorService to provide the thread pool
- CompletableFuture runs tasks on that pool but chains the results
- Example: `CompletableFuture.supplyAsync(() -> task(), executorService)`

### Comparison
| Feature | ExecutorService | CompletableFuture |
|---|---|---|
| Task submission | submit() / execute() | supplyAsync() / runAsync() |
| Result handling | Future.get() (blocking) | thenApply() (non-blocking) |
| Chaining | Manual | Fluent API |
| Error handling | try-catch in task | exceptionally() / handle() |
| Combining tasks | Manual coordination | allOf() / anyOf() |
| Thread control | Explicit pool config | Uses ForkJoinPool or custom executor |
| Scheduling | ScheduledExecutorService | Delays via thenDelay() |

---

## When to Use Concurrent Collections vs Synchronized Collections

### Use Synchronized Collections (Collections.synchronized*) When:
- You have simple read-write patterns and low contention
- You want to wrap existing collections with minimal code change
- You need only basic thread safety, not compound operations
- You are iterating and want a snapshot (synchronized + copy)

### Use Concurrent Collections (ConcurrentHashMap, CopyOnWriteArrayList) When:
- You have high contention and need better scalability
- You want lock striping (ConcurrentHashMap reads without locking)
- You need atomic compound operations (putIfAbsent, compute, merge)
- You want weakly consistent iteration (no ConcurrentModificationException)
- You need a queue for producer-consumer (ConcurrentLinkedQueue, BlockingQueue)
- You need snapshot iteration (CopyOnWriteArrayList for read-heavy, rare writes)

### Comparison
| Collection | Synchronized Wrapper | Concurrent Alternative |
|---|---|---|
| Map | Collections.synchronizedMap() | ConcurrentHashMap |
| List | Collections.synchronizedList() | CopyOnWriteArrayList, ConcurrentLinkedDeque |
| Set | Collections.synchronizedSet() | CopyOnWriteArraySet, ConcurrentSkipListSet |
| Queue | Not available | ConcurrentLinkedQueue, LinkedBlockingQueue |
| SortedMap | Collections.synchronizedSortedMap() | ConcurrentSkipListMap |
| Lock granularity | Single lock | Lock striping / CAS operations |
| Iteration | Manual sync or fail-fast | Weakly consistent (no exception) |
| Atomic ops | Manual compound | compute(), merge(), putIfAbsent() |

---

## Trade-offs Summary

| Factor | `synchronized` | `Lock` | `ExecutorService` | `CompletableFuture` |
|---|---|---|---|---|
| **Performance** | Good for low contention | Better for high contention | Controlled by pool size | Depends on executor |
| **Complexity** | Low | Medium | Medium | High |
| **Readability** | Excellent | Good (with practice) | Good | Excellent when chained |
| **Flexibility** | Limited | High | High | Very high |
| **Error handling** | try-finally | try-finally | In task or Future.get() | exceptionally() |
| **Best use case** | Simple sync | Advanced locking | Task management | Async pipelines |

| Factor | Platform Threads | Virtual Threads |
|---|---|---|
| **Performance** | Better for CPU-bound | Better for I/O-bound |
| **Complexity** | Simple | Simple |
| **Memory** | High per thread | Low per thread |
| **Scalability** | Limited (~thousands) | High (~millions) |
| **Blocking** | Holds OS thread | Frees carrier thread |

| Factor | Synchronized Collections | Concurrent Collections |
|---|---|---|
| **Performance** | Degrades under contention | Scales with threads |
| **Complexity** | Low | Medium |
| **Atomic operations** | Manual | Built-in (compute, merge) |
| **Iteration** | Fail-fast | Weakly consistent |
| **Use case** | Low contention, simple | High contention, complex ops |
