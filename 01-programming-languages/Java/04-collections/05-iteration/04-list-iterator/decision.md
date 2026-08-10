# ListIterator Decision Guide

## Decision Tree

```
Need to iterate over a List?
├── Need bidirectional traversal? → ListIterator
├── Need to add during iteration? → ListIterator
├── Need to modify during iteration? → ListIterator
├── Need to get index? → ListIterator
├── Simple iteration? → For-each
└── Reverse only? → Collections.reverse() or for loop
```

## Comparison Matrix

| Feature | ListIterator | Iterator | For-Each | Collections.reverse() |
|---------|--------------|----------|----------|----------------------|
| Bidirectional | Yes | No | No | No |
| Add during | Yes | No | No | No |
| Modify during | Yes | No | No | No |
| Index access | Yes | No | No | Yes |
| Performance | Good | Good | Best | Medium |
| Readability | Medium | Medium | High | High |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Bidirectional | ListIterator | Forward and backward |
| Add during iteration | ListIterator | Only option |
| Modify during iteration | ListIterator | Only option |
| Get index | ListIterator | Built-in |
| Simple iteration | For-each | Cleanest |

## Production Recommendations

> **Use ListIterator for bidirectional traversal** — it's the only iterator that goes both ways.

> **Use ListIterator for add/modify during iteration** — standard pattern.

> **Use listIterator(index) for starting position** — useful for partial iteration.

> **Use for-each when possible** — it's simpler and less error-prone.

## Engineering Trade-offs

| Trade-off | ListIterator | Alternative |
|-----------|--------------|-------------|
| Bidirectional vs Simple | Full control | For-each: simpler |
| Power vs Readability | Complex | For-each: clean |
| Legacy vs Modern | Standard pattern | Stream: modern |
| Flexibility vs Safety | Safe modification | For-each: safest |

## Common Code Review Comments

- "This ListIterator isn't using bidirectional — use for-each instead."
- "Consider using Collections.reverse() if only reversing."
- "This ListIterator add() can be replaced with stream()."
- "ListIterator is overkill for simple forward iteration."

## Common Production Mistakes

> Notice: ListIterator.nextIndex() returns index of element that would be returned by next().

> Notice: ListIterator.previousIndex() returns index of element that would be returned by previous().

> Notice: ListIterator.set() replaces last element returned by next() or previous().

> Notice: ListIterator is not thread-safe — use synchronization if shared.
