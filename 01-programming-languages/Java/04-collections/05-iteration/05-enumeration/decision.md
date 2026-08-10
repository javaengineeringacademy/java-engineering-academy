# Enumeration Decision Guide

## Decision Tree

```
Need to iterate over a collection?
├── Working with legacy code? → Enumeration
├── New code? → Use Iterator or for-each
├── Need thread-safe enumeration? → CopyOnWriteArrayList
├── Need to remove during? → Iterator
├── Simple iteration? → For-each
└── Need functional style? → forEach() or stream()
```

## Comparison Matrix

| Feature | Enumeration | Iterator | For-Each | Stream |
|---------|-------------|----------|----------|--------|
| Legacy support | Yes | Yes | Yes | Yes |
| Remove during | No | Yes | No | Yes (filter) |
| Thread-safe | No | No | No | No |
| Modern code | No | Yes | Yes | Yes |
| Performance | Good | Good | Best | Good |
| Readability | Low | Medium | High | High |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Legacy code | Enumeration | Required |
| New code | Iterator or for-each | Modern |
| Thread-safe | CopyOnWriteArrayList | Safe iteration |
| Remove during | Iterator | Only option |
| Simple iteration | For-each | Cleanest |

## Production Recommendations

> **Avoid Enumeration in new code** — use Iterator or for-each instead.

> **Use Collections.enumeration() to adapt** — converts Collection to Enumeration.

> **Use Enumeration only for legacy APIs** — Vector, Hashtable, StringTokenizer.

> **Replace Enumeration with Iterator** — when maintaining legacy code.

## Engineering Trade-offs

| Trade-off | Enumeration | Alternative |
|-----------|-------------|-------------|
| Legacy vs Modern | Required for legacy | Iterator: modern |
| Safety vs Speed | No safety | For-each: safest |
| Compatibility vs Features | Limited | Stream: full features |
| Simplicity vs Power | Basic | ListIterator: powerful |

## Common Code Review Comments

- "This Enumeration can be replaced with Iterator."
- "Use for-each instead of Enumeration."
- "This is legacy code — consider modernizing."
- "Use Collections.enumeration() if legacy API requires it."

## Common Production Mistakes

> Notice: Enumeration doesn't support remove() — use Iterator for removal.

> Notice: Enumeration is not thread-safe — Vector/Hashtable synchronization doesn't help.

> Notice: Enumeration.nextElement() throws NoSuchElementException — check hasMoreElements().

> Notice: Enumeration is limited to Vector/Hashtable — use Iterator for other collections.
