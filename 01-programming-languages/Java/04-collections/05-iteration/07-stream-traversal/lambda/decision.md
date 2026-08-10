# Lambda/forEach Traversal Decision Guide

## Decision Tree

```
Need to iterate over a collection?
├── Functional style preferred? → forEach() or stream()
├── Concise code needed? → forEach()
├── Need to transform? → stream().map()
├── Need to filter? → stream().filter()
├── Need index? → IntStream.range()
└── Complex logic? → For loop
```

## Comparison Matrix

| Feature | forEach() | Stream | For-Each | For Loop |
|---------|-----------|--------|----------|----------|
| Functional | Yes | Yes | No | No |
| Concise | Yes | Yes | Yes | No |
| Index access | No | Via IntStream | No | Yes |
| Break/continue | No | findAny/findFirst | Yes | Yes |
| Readability | High | High | High | Medium |
| Performance | Good | Good | Best | Best |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Functional style | forEach() | Lambda syntax |
| Transform elements | stream().map() | Functional |
| Filter elements | stream().filter() | Declarative |
| Need index | IntStream.range() | Functional with index |
| Complex logic | For loop | Full control |

## Production Recommendations

> **Use forEach() for simple side effects** — it's concise and readable.

> **Use stream() for complex operations** — filter, map, reduce are built-in.

> **Use for-each when performance is critical** — it has less overhead.

> **Avoid forEach() for complex logic** — use for loop for clarity.

## Engineering Trade-offs

| Trade-off | forEach() | Alternative |
|-----------|-----------|-------------|
| Conciseness vs Control | Concise | For loop: full control |
| Functional vs Imperative | Functional | For-each: imperative |
| Readability vs Power | Readable | Stream: more features |
| Performance vs Features | Fast | Stream: more features |

## Common Code Review Comments

- "This forEach can be replaced with stream().map().filter()."
- "Consider using stream() for this complex operation."
- "This forEach has side effects — consider using stream().collect()."
- "This forEach is overkill — use for-each for simple iteration."

## Common Production Mistakes

> Notice: forEach() is terminal operation — can't chain after it.

> Notice: forEach() doesn't return value — use map().collect() instead.

> Notice: forEach() with side effects can cause issues in parallel streams.

> Notice: forEach() is not thread-safe — use synchronized or concurrent collections.
