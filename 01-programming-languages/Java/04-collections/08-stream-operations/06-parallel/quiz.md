# Parallel Streams Quiz

## Questions

### Q1: What is a parallel stream?
**Answer:** A stream that processes elements in parallel using multiple threads from the ForkJoinPool.

### Q2: How do you create a parallel stream?
**Answer:** Using collection.parallelStream() or stream.parallel().

### Q3: What is the difference between stream() and parallelStream()?
**Answer:** stream() processes sequentially; parallelStream() processes in parallel.

### Q4: What thread pool do parallel streams use?
**Answer:** The common ForkJoinPool (Runtime.getRuntime().availableProcessors() - 1 threads).

### Q5: When should you use parallel streams?
**Answer:** When processing large datasets with independent operations and no shared mutable state.

### Q6: What is the risk of using parallel streams?
**Answer:** Race conditions if shared mutable state is accessed; thread safety issues.

### Q7: Is parallelStream() always faster?
**Answer:** No, overhead of thread management may make it slower for small datasets.

### Q8: What is the difference between parallel() and sequential()?
**Answer:** parallel() converts to parallel stream; sequential() converts to sequential stream.

### Q9: Can you mix parallel and sequential operations?
**Answer:** Yes, the most recent call (parallel()/sequential()) determines the execution mode.

### Q10: What is the source of parallel stream threads?
**Answer:** The ForkJoinPool.commonPool() by default.

## Bonus Questions

### Q11: How do you control the parallelism level?
**Answer:** Using System.setProperty or by creating a custom ForkJoinPool.

### Q12: What is the difference between parallel() and forEachParallel()?
**Answer:** parallel() returns a parallel stream; forEach is just an operation that can be used on parallel streams.
