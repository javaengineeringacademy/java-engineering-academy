# PriorityQueue Decision Guide

## Decision Tree

```
Need priority-based processing?
├── Need natural ordering? → PriorityQueue (implements Comparable)
├── Need custom ordering? → PriorityQueue(Comparator)
├── Need thread safety? → PriorityBlockingQueue
├── Need bounded queue? → Use custom implementation with PriorityQueue
└── Need FIFO? → ArrayDeque (not PriorityQueue)
```

## Comparison Matrix

| Feature | PriorityQueue | PriorityBlockingQueue | TreeSet |
|---------|---------------|----------------------|---------|
| Ordering | Priority | Priority | Sorted |
| Thread-safe | No | Yes | No |
| Null elements | No | No | No |
| Performance | O(log n) | O(log n) | O(log n) |
| Memory | Low | Medium | Medium |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Priority processing | PriorityQueue | O(log n) insert/remove |
| Concurrent priority | PriorityBlockingQueue | Thread-safe |
| Sorted set | TreeSet | No duplicates |
| FIFO queue | ArrayDeque | Faster than PriorityQueue |

## Production Recommendations

> **Use PriorityQueue for single-threaded priority processing** — it's the fastest priority queue.

> **Use PriorityBlockingQueue for concurrent priority processing** — it's thread-safe.

> **Never use PriorityQueue in concurrent code without synchronization** — it's not thread-safe.

> **Use comparator() for custom ordering** — it's more readable than Comparable.

## Engineering Trade-offs

| Trade-off | PriorityQueue | Alternative |
|-----------|---------------|-------------|
| Priority vs FIFO | Priority ordering | ArrayDeque: FIFO ordering |
| Thread-safety vs Performance | No safety | PriorityBlockingQueue: safe, overhead |
| Memory vs Sort | Low memory | TreeSet: sorted, higher memory |
| Simplicity vs Performance | Simple | PriorityBlockingQueue: simple wrapper |

## Common Code Review Comments

- "Why are you using PriorityQueue? ArrayDeque is faster for FIFO operations."
- "This PriorityQueue is not thread-safe — use PriorityBlockingQueue for concurrent access."
- "Consider using comparator() for custom ordering."
- "This PriorityQueue doesn't allow null elements."

## Common Production Mistakes

> Notice: PriorityQueue doesn't allow null elements — it will throw NullPointerException.

> Notice: PriorityQueue is not thread-safe — even for reads, concurrent modification can cause data corruption.

> Notice: PriorityQueue.iterator() doesn't guarantee ordering — use poll() to process in priority order.

> Notice: PriorityQueue.grow() doubles the size — pre-size if you know the approximate capacity.
