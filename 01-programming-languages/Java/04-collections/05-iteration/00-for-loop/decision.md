# Traditional For Loop Decision Guide

## Decision Tree

```
Need to iterate over a collection?
├── Need index access? → for loop
├── Need to skip elements? → for loop
├── Need reverse iteration? → for loop
├── Simple iteration, no index? → for-each loop
├── Functional style? → forEach() or stream()
└── Unknown iterations? → while loop
```

## Comparison Matrix

| Feature | For Loop | For-Each | forEach() | Stream |
|---------|----------|----------|-----------|--------|
| Index access | Yes | No | No | No |
| Reverse iteration | Yes | No | No | Yes (via List) |
| Skip elements | Yes | No | No | Yes (skip()) |
| Readability | Medium | High | High | High |
| Performance | Best | Good | Good | Good |
| Remove during iteration | Yes (careful) | No | No | Yes (filter) |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Need index | For loop | Direct index access |
| Performance critical | For loop | Minimal overhead |
| Simple iteration | For-each | Cleanest syntax |
| Functional style | forEach() or stream() | Declarative |
| Parallel processing | Stream | Built-in parallelism |

## Production Recommendations

> **Use for loop when index is needed** — it's the only loop with direct index access.

> **Use for-each for simple iteration** — it's cleaner and less error-prone.

> **Avoid modifying collection during for-each** — use for loop or Iterator instead.

> **Use stream() for complex operations** — filter, map, reduce are built-in.

## Engineering Trade-offs

| Trade-off | For Loop | Alternative |
|-----------|----------|-------------|
| Index vs Readability | Index access | For-each: cleaner |
| Performance vs Safety | Fast, but error-prone | For-each: safe |
| Flexibility vs Simplicity | Full control | Stream: declarative |
| Reverse vs Forward | Manual reverse | Collections.reverse(): simpler |

## Common Code Review Comments

- "Why use for loop when index isn't needed? For-each is cleaner."
- "This for loop can be replaced with forEach() for better readability."
- "Consider using stream() for this filter-map-reduce operation."
- "This for loop modifies the collection — use Iterator instead."

## Common Production Mistakes

> Notice: Off-by-one errors are common with for loops — use `list.size()` not `list.size()-1` when appropriate.

> Notice: Modifying collection during for loop causes ConcurrentModificationException — use Iterator.

> Notice: Using `i++` in for loop header when `i` is modified in body — leads to unexpected skips.

> Notice: For-each loop doesn't have index — use AtomicInteger or for loop if needed.
