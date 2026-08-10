# Deque Decision Guide

## Decision Tree

```
Need double-ended operations?
├── Need stack (LIFO)? → ArrayDeque (not Stack)
├── Need queue (FIFO)? → ArrayDeque (not LinkedList)
├── Need both? → ArrayDeque
├── Need thread safety? → LinkedBlockingDeque
└── Need legacy? → LinkedList (but ArrayDeque is faster)
```

## Comparison Matrix

| Feature | ArrayDeque | LinkedList | LinkedBlockingDeque |
|---------|------------|------------|---------------------|
| addFirst/addLast | O(1) | O(1) | O(1) |
| removeFirst/removeLast | O(1) | O(1) | O(1) |
| peekFirst/peekLast | O(1) | O(1) | O(1) |
| Thread-safe | No | No | Yes (locks) |
| Memory | Low | High | Medium |
| Cache locality | Excellent | Poor | Good |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| General deque | ArrayDeque | Fastest for most operations |
| Stack operations | ArrayDeque | Faster than Stack |
| Queue operations | ArrayDeque | Faster than LinkedList |
| Concurrent deque | LinkedBlockingDeque | Thread-safe |
| Legacy code | LinkedList | Don't use in new code |

## Production Recommendations

> **Default to ArrayDeque** for deque operations — it's faster than LinkedList and has better cache locality.

> **Use ArrayDeque for stack operations** — it's faster than Stack and has better cache locality.

> **Use ArrayDeque for queue operations** — it's faster than LinkedList.

> **Use LinkedBlockingDeque for concurrent deque** — it's thread-safe and bounded.

## Engineering Trade-offs

| Trade-off | ArrayDeque | Alternative |
|-----------|------------|-------------|
| Speed vs Thread-safety | Fast, no safety | LinkedBlockingDeque: safe, overhead |
| Memory vs Performance | Low memory, fast | LinkedList: high memory, slow |
| Simplicity vs Features | Simple | LinkedBlockingDeque: feature-rich |
| Immutability vs Flexibility | Mutable | List.of(): immutable |

## Common Code Review Comments

- "Why are you using LinkedList for deque? ArrayDeque is faster."
- "This should be an ArrayDeque — it's faster than Stack for LIFO operations."
- "Consider using LinkedBlockingDeque for concurrent deque operations."
- "ArrayDeque doesn't allow null elements."

## Common Production Mistakes

> Notice: ArrayDeque doesn't allow null elements — it will throw NullPointerException.

> Notice: ArrayDeque is not thread-safe — even for reads, concurrent modification can cause data corruption.

> Notice: ArrayDeque grows automatically — pre-size if you know the approximate capacity.

> Notice: ArrayDeque implements both Queue and Deque — but it's faster for Deque operations.
