# BlockingQueue Decision Guide

## Decision Tree

```
Need bounded queue?
├── Need producer-consumer? → ArrayBlockingQueue or LinkedBlockingQueue
├── Need handoff? → SynchronousQueue
├── Need unbounded? → LinkedBlockingQueue
├── Need lock-free? → ConcurrentLinkedQueue
└── Need priority? → PriorityBlockingQueue
```

## Comparison Matrix

| Feature | ArrayBlockingQueue | LinkedBlockingQueue | SynchronousQueue |
|---------|-------------------|--------------------|--------------------|
| Bounded | Yes | Optional | Yes (capacity 1) |
| Thread-safe | Yes (locks) | Yes (locks) | Yes (locks) |
| Ordering | FIFO | FIFO | FIFO |
| Performance | Good | Good | Good |
| Memory | Low | Medium | Very Low |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Producer-consumer | ArrayBlockingQueue | Bounded, thread-safe |
| Unbounded queue | LinkedBlockingQueue | No capacity limit |
| Handoff | SynchronousQueue | No storage, direct handoff |
| Lock-free queue | ConcurrentLinkedQueue | No locks, CAS-based |
| Priority queue | PriorityBlockingQueue | Priority ordering |

## Production Recommendations

> **Use ArrayBlockingQueue for producer-consumer** — it's bounded and prevents memory overflow.

> **Use LinkedBlockingQueue for unbounded queues** — but be careful of memory issues.

> **Use SynchronousQueue for handoff** — it's perfect for direct producer-consumer.

> **Never use unbounded queues in production** — they can cause memory overflow.

## Engineering Trade-offs

| Trade-off | ArrayBlockingQueue | Alternative |
|-----------|-------------------|-------------|
| Bounded vs Unbounded | Bounded, prevents overflow | LinkedBlockingQueue: unbounded, risk |
| Lock-based vs Lock-free | Lock-based, simple | ConcurrentLinkedQueue: lock-free, complex |
| Memory vs Performance | Low memory, good perf | LinkedBlockingQueue: medium memory, good perf |
| Simplicity vs Features | Simple | PriorityBlockingQueue: priority ordering |

## Common Code Review Comments

- "This queue should be bounded to prevent memory overflow."
- "Consider using SynchronousQueue for handoff operations."
- "This LinkedBlockingQueue is unbounded — consider adding a capacity."
- "ConcurrentLinkedQueue is lock-free for better performance."

## Common Production Mistakes

> Notice: ArrayBlockingQueue is bounded — if the queue is full, put() blocks and offer() returns false.

> Notice: LinkedBlockingQueue.size() is O(n) — don't use it to check if the queue is empty. Use isEmpty().

> Notice: SynchronousQueue doesn't store elements — it's for direct handoff between threads.

> Notice: PriorityBlockingQueue doesn't allow null elements — it will throw NullPointerException.
