# parallelStream() Decision Guide

## Decision Tree

```
Need parallel processing?
├── Large dataset? → parallelStream()
├── Small dataset? → stream()
├── CPU-intensive? → parallelStream()
├── I/O-intensive? → CompletableFuture
├── Ordered results needed? → stream()
└── Unordered results ok? → parallelStream()
```

## Comparison Matrix

| Feature | parallelStream() | stream() | CompletableFuture |
|---------|------------------|----------|-------------------|
| Thread safety | ForkJoinPool | Single thread | Custom executor |
| Time | O(n/p) | O(n) | O(n/p) |
| Use case | CPU-intensive | Small datasets | I/O-intensive |
| Ordering | Unordered | Ordered | Custom |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Large dataset | parallelStream() | Multi-threaded |
| Small dataset | stream() | Single-threaded |
| CPU-intensive | parallelStream() | Multi-threaded |
| I/O-intensive | CompletableFuture | Custom executor |
| Ordered results | stream() | Single-threaded |
| Unordered results | parallelStream() | Multi-threaded |

## Production Recommendations

> **Use parallelStream() for large datasets** — it's faster for CPU-intensive operations.

> **Use stream() for small datasets** — it's simpler and faster for small datasets.

> **Use CompletableFuture for I/O-intensive** — it's more flexible for I/O operations.

> **Use parallelStream() for unordered results** — it's faster for unordered operations.

## Engineering Trade-offs

| Trade-off | parallelStream() | stream() |
|-----------|------------------|----------|
| Speed vs Simplicity | Faster | Simpler |
| Ordered vs Unordered | Unordered | Ordered |
| Thread-safe vs Single-thread | Thread-safe | Single-thread |
| Large vs Small | Good for large | Good for small |

## Common Code Review Comments

- "Use parallelStream() for large datasets — it's faster."
- "Use stream() for small datasets — it's simpler."
- "Use CompletableFuture for I/O-intensive — it's more flexible."
- "This stream() should be parallelStream() for large datasets."

## Common Production Mistakes

> Notice: parallelStream() uses ForkJoinPool — don't block threads in parallel stream.

> Notice: parallelStream() is unordered — don't use when order matters.

> Notice: parallelStream() can cause race conditions — use thread-safe operations.

> Notice: parallelStream() is O(n/p) — don't use for small datasets.