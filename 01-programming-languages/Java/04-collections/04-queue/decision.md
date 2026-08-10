# Queue Interface Decision Guide

## Decision Tree

```
Need FIFO/LIFO operations?
├── Need priority ordering? → PriorityQueue
├── Need double-ended operations? → ArrayDeque (default)
├── Need bounded queue? → ArrayBlockingQueue
├── Need unbounded queue? → LinkedBlockingQueue
├── Need thread-safe queue?
│   ├── Producer-consumer → ArrayBlockingQueue or LinkedBlockingQueue
│   ├── Single producer/single consumer → SynchronousQueue
│   └── General → ConcurrentLinkedQueue
└── Need legacy queue? → LinkedList (but ArrayDeque is faster)
```

## Comparison Matrix

| Implementation | Bounded | Thread-Safe | Ordering | Performance | Use Case |
|---------------|---------|-------------|----------|-------------|----------|
| PriorityQueue | No | No | Priority | O(log n) | Priority processing |
| ArrayDeque | No | No | FIFO/LIFO | O(1) | General deque |
| LinkedList | No | No | FIFO/LIFO | O(1) | Legacy deque |
| ArrayBlockingQueue | Yes | Yes (locks) | FIFO | O(1) | Producer-consumer |
| LinkedBlockingQueue | Optional | Yes (locks) | FIFO | O(1) | Producer-consumer |
| ConcurrentLinkedQueue | No | Yes (CAS) | FIFO | O(1) | Lock-free queue |
| SynchronousQueue | Yes | Yes (locks) | FIFO | O(1) | Handoff |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| General deque | ArrayDeque | Fastest for most operations |
| Priority processing | PriorityQueue | O(log n) insert/remove |
| Producer-consumer | ArrayBlockingQueue | Bounded, thread-safe |
| Lock-free queue | ConcurrentLinkedQueue | No locks, CAS-based |
| Immutable queue | Use List + index | No built-in immutable queue |

## Production Recommendations

> **Default to ArrayDeque** for queue/deque operations — it's faster than LinkedList and has better cache locality.

> **Use ArrayBlockingQueue for producer-consumer** — it's bounded and prevents memory overflow.

> **Avoid LinkedList for queue operations** — ArrayDeque is faster in all cases.

> **Never use PriorityQueue in concurrent code** — it's not thread-safe and has no good concurrent alternative.

## Engineering Trade-offs

| Trade-off | Option A | Option B |
|-----------|----------|----------|
| Speed vs Ordering | ArrayDeque (fast, FIFO/LIFO) | PriorityQueue (sorted, O(log n)) |
| Bounded vs Unbounded | ArrayBlockingQueue (bounded) | LinkedBlockingQueue (unbounded) |
| Lock-based vs Lock-free | ArrayBlockingQueue (locks) | ConcurrentLinkedQueue (CAS) |
| Memory vs Performance | ArrayDeque (compact) | LinkedList (scattered nodes) |
| Immutability vs Flexibility | List.of() (immutable) | ArrayDeque (mutable) |

## Common Code Review Comments

- "Why are you using LinkedList for queue? ArrayDeque is faster."
- "This queue should be bounded to prevent memory overflow."
- "Consider using ConcurrentLinkedQueue for lock-free queue operations."
- "PriorityQueue is not thread-safe — use a wrapper or ConcurrentSkipListSet for concurrent priority queue."

## Common Production Mistakes

> Notice: PriorityQueue doesn't allow null elements — it will throw NullPointerException.

> Notice: ArrayDeque doesn't allow null elements — it will throw NullPointerException.

> Notice: ArrayBlockingQueue is bounded — if the queue is full, put() blocks and offer() returns false.

> Notice: ConcurrentLinkedQueue.size() is O(n) — don't use it to check if the queue is empty. Use isEmpty() instead.
