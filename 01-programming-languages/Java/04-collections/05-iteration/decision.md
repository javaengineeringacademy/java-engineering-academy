# Iteration Patterns Decision Guide

## Decision Tree

```
Need to iterate over a collection?
├── Need index? → for loop with get(i)
├── Need to remove during iteration? → Iterator.remove()
├── Need to modify during iteration? → ListIterator
├── Need parallel processing? → Spliterator
├── Need functional style? → forEach() or stream()
└── Just read elements? → for-each loop (simplest)
```

## Comparison Matrix

| Pattern | Index Access | Remove During | Modify During | Parallel | Functional |
|---------|-------------|---------------|---------------|----------|------------|
| for loop | Yes | Yes | Yes | No | No |
| for-each | No | No | No | No | No |
| Iterator | No | Yes | No | No | No |
| ListIterator | Yes | Yes | Yes | No | No |
| forEach() | No | No | No | No | Yes |
| stream() | No | No | No | Yes | Yes |
| Spliterator | No | No | No | Yes | No |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Simple iteration | for-each | Cleanest syntax |
| Remove during iteration | Iterator.remove() | Safe removal |
| Modify during iteration | ListIterator | Bidirectional, set/add |
| Parallel processing | stream().parallel() | Built-in parallelism |
| Functional style | forEach() | Concise, readable |
| Performance-critical | for loop | Fastest, no overhead |

## Production Recommendations

> **Use for-each for simple iteration** — it's the cleanest and least error-prone.

> **Never modify a collection during for-each iteration** — use Iterator.remove() or list.removeIf().

> **Use stream() for parallel processing** — it handles thread management automatically.

> **Avoid manual index management** — use for-each or stream() unless you need the index.

## Engineering Trade-offs

| Trade-off | Option A | Option B |
|-----------|----------|----------|
| Simplicity vs Control | for-each (simple) | for loop (control) |
| Safety vs Performance | Iterator (safe removal) | for loop (fast, risky) |
| Sequential vs Parallel | forEach() (sequential) | stream().parallel() (parallel) |
| Functional vs Imperative | stream() (functional) | for loop (imperative) |
| Memory vs Speed | stream() (lazy) | for loop (eager) |

## Common Code Review Comments

- "Don't use Iterator.remove() — use list.removeIf() instead."
- "This stream should be parallel — you have a large dataset."
- "Avoid modifying the collection during for-each — use Iterator."
- "This could be a simple for-each loop — no need for index."

## Common Production Mistakes

> Notice: ConcurrentModificationException — modifying a collection during for-each iteration will throw this exception.

> Notice: forEach() doesn't return a value — use map() if you need to transform elements.

> Notice: stream().parallel() doesn't guarantee order — use forEachOrdered() if order matters.

> Notice: Spliterator is for advanced use cases — prefer stream() for most parallel processing.
