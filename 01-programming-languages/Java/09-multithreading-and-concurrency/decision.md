# Multithreading Decision Guide

## Platform Threads vs Virtual Threads

| Aspect | Platform Thread | Virtual Thread |
|--------|----------------|----------------|
| Memory | ~1 MB stack | ~1-2 KB (grows) |
| Max count | Thousands | Millions |
| Blocking | Ties up OS thread | Frees carrier thread |
| Scheduling | OS scheduler | JVM scheduler |
| Best for | CPU-bound | I/O-bound |

**Use platform threads when:** Task count is small, tasks need thread-local resources, tight scheduling control.
**Use virtual threads when:** Task count is large, tasks are I/O-bound, simplified concurrency.

---

## synchronized vs Lock

| Feature | `synchronized` | `Lock` |
|---------|----------------|--------|
| Syntax | Simple | Requires explicit unlock |
| Auto-release | Yes | No (must use finally) |
| Try-lock | No | Yes |
| Timed lock | No | Yes |
| Multiple conditions | No | Yes (Condition objects) |
| Fairness | No | Configurable |
| Performance | Better for simple cases | Better for contended cases |

---

## ExecutorService vs CompletableFuture

| Feature | ExecutorService | CompletableFuture |
|---------|----------------|-------------------|
| Task submission | submit() / execute() | supplyAsync() / runAsync() |
| Result handling | Future.get() (blocking) | thenApply() (non-blocking) |
| Chaining | Manual | Fluent API |
| Error handling | try-catch | exceptionally() / handle() |
| Thread control | Explicit pool config | ForkJoinPool or custom executor |

---

## Synchronized vs Concurrent Collections

| Collection | Synchronized Wrapper | Concurrent Alternative |
|------------|---------------------|----------------------|
| Map | Collections.synchronizedMap() | ConcurrentHashMap |
| List | Collections.synchronizedList() | CopyOnWriteArrayList |
| Queue | Not available | ConcurrentLinkedQueue, BlockingQueue |
| Lock granularity | Single lock | Lock striping / CAS |
| Iteration | Fail-fast | Weakly consistent |
