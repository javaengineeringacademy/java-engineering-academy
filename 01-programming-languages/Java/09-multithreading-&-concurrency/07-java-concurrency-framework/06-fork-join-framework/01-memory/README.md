# Fork/Join Framework — Memory Model

## Overview

This directory explores how memory is managed in the Fork/Join framework — work queues, task objects, and the happens-before relationships that make work-stealing safe.

## Key Memory Concepts

### Work Queue Memory

- Each `WorkQueue` is an array-based deque (`ForkJoinTask<?>[]`)
- Array size is always a power of 2 (for efficient modulo)
- Push/pop/steal operations use volatile semantics for visibility between threads
- Array is lazily allocated (initially null, created on first task submission)

### Task Object Memory

- `ForkJoinTask` stores the computed result in `Object status` field
- Tasks are typically short-lived — created, split, computed, result retrieved
- Recursive splitting creates a tree of task objects on the heap
- After `join()`, task objects become eligible for GC

### Work-Stealing Memory Barriers

- **Steal**: Stealing thread reads victim's deque tail (volatile read)
- **Push**: Worker writes task to deque top (volatile write)
- These operations establish happens-before relationships between threads

### Result Propagation

- `ForkJoinTask.get()` blocks until the result is available
- Result is stored in a volatile field, ensuring visibility
- No explicit synchronization needed — CAS on status field handles thread safety

### Common Pool Memory

- `ForkJoinPool.commonPool()` is shared across all parallel streams
- Thread count defaults to `Runtime.getRuntime().availableProcessors() - 1`
- Tasks submitted to the common pool share its work queue — memory overhead is proportional to queue size

### Memory Efficiency Tips

- Avoid holding references to large objects in tasks after they complete
- Use `invokeAll()` to submit multiple tasks and reduce per-task overhead
- Split tasks to a granularity where base-case computation dominates task management

## Files

- [ForkJoinPoolMemory.java](ForkJoinPoolMemory.java) — Memory layout and visibility in Fork/Join
