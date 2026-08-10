# Iterator Pattern Decision Guide

## Decision Tree

```
Need to iterate over a collection?
├── Need to remove during iteration? → Iterator
├── Need to add during iteration? → ListIterator
├── Simple iteration? → For-each
├── Need to transform? → Stream
├── Need to filter? → Stream.filter()
└── Need to reduce? → Stream.reduce()
```

## Comparison Matrix

| Feature | Iterator | For-Each | removeIf() | Stream |
|---------|----------|----------|------------|--------|
| Remove during | Yes | No | Yes | Yes (filter) |
| Add during | No | No | No | No |
| Read-only | No | Yes | No | Yes |
| Functional | No | No | Yes | Yes |
| Performance | Good | Best | Good | Good |
| Readability | Medium | High | High | High |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Remove during iteration | Iterator | Safe removal |
| Simple iteration | For-each | Cleanest |
| Remove matching | removeIf() | Declarative |
| Transform elements | Stream | Functional |
| Legacy code | Iterator | Standard pattern |

## Production Recommendations

> **Use Iterator for safe removal** — it's the only way to remove during iteration.

> **Use removeIf() for bulk removal** — cleaner than Iterator loop.

> **Use for-each when possible** — it's simpler and less error-prone.

> **Use Stream for complex operations** — filter, map, reduce are built-in.

## Engineering Trade-offs

| Trade-off | Iterator | Alternative |
|-----------|----------|-------------|
| Safety vs Simplicity | Safe removal | For-each: simpler |
| Control vs Readability | Full control | removeIf(): cleaner |
| Legacy vs Modern | Standard pattern | Stream: modern |
| Performance vs Features | Fast | Stream: more features |

## Common Code Review Comments

- "This Iterator loop can be replaced with removeIf()."
- "Consider using for-each when not removing elements."
- "This Iterator is not used for removal — use for-each instead."
- "Use try-with-resources if Iterator implements AutoCloseable."

## Common Production Mistakes

> Notice: Iterator.remove() is optional — not all implementations support it.

> Notice: Calling collection.remove() during Iterator loop causes ConcurrentModificationException.

> Notice: Iterator is not thread-safe — use synchronization if shared.

> Notice: Iterator.forEachRemaining() is more efficient than manual loop.
