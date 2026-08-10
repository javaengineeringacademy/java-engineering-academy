# Spliterator Decision Guide

## Decision Tree

```
Need to iterate over a collection?
├── Need parallel processing? → Stream.parallel()
├── Need custom splitting? → Spliterator
├── Need to estimate size? → Spliterator.estimateSize()
├── Simple iteration? → For-each
├── Need functional style? → Stream
└── Need to process in chunks? → Spliterator.trySplit()
```

## Comparison Matrix

| Feature | Spliterator | Stream | parallelStream() | For-Each |
|---------|-------------|--------|------------------|----------|
| Parallel | Custom | Built-in | Built-in | No |
| Splitting | Manual | Automatic | Automatic | No |
| Size estimation | Yes | No | No | No |
| Readability | Low | High | High | High |
| Performance | Custom | Good | Good | Best |
| Complexity | High | Low | Low | Low |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Parallel processing | parallelStream() | Built-in |
| Custom splitting | Spliterator | Full control |
| Size estimation | Spliterator | Built-in |
| Simple iteration | For-each | Cleanest |
| Functional style | Stream | Declarative |

## Production Recommendations

> **Use parallelStream() for most parallel operations** — it handles splitting automatically.

> **Use Spliterator for custom parallel algorithms** — when you need control over splitting.

> **Use trySplit() for chunk processing** — divide work into manageable pieces.

> **Use estimateSize() for progress tracking** — useful for long operations.

## Engineering Trade-offs

| Trade-off | Spliterator | Alternative |
|-----------|-------------|-------------|
| Control vs Simplicity | Full control | parallelStream(): simple |
| Custom vs Standard | Custom splitting | Stream: automatic |
| Power vs Readability | Complex | For-each: clean |
| Performance vs Complexity | Optimal | Stream: good enough |

## Common Code Review Comments

- "This Spliterator can be replaced with parallelStream()."
- "Consider using Stream instead of manual Spliterator."
- "This custom splitting is complex — is parallelStream() sufficient?"
- "Spliterator is overkill for simple parallel operations."

## Common Production Mistakes

> Notice: Spliterator is not thread-safe — each thread needs its own Spliterator.

> Notice: trySplit() may return null — always check before processing.

> Notice: estimateSize() is approximate — don't rely on exact values.

> Notice: Spliterator characteristics affect behavior — check ORDERED, SIZED, etc.
