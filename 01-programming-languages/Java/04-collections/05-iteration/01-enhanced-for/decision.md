# Enhanced For-Each Loop Decision Guide

## Decision Tree

```
Need to iterate over a collection?
├── Simple iteration, no modification? → For-each
├── Need index? → Traditional for loop
├── Need to remove elements? → Iterator or removeIf()
├── Need to modify elements? → For loop or stream().map()
├── Functional style? → forEach() or stream()
└── Unknown iterations? → While loop
```

## Comparison Matrix

| Feature | For-Each | For Loop | forEach() | Stream |
|---------|----------|----------|-----------|--------|
| Syntax | Clean | Verbose | Clean | Clean |
| Index access | No | Yes | No | No |
| Remove during | No | No | No | Yes (filter) |
| Break/continue | Yes | Yes | No | Yes (findAny) |
| Null safety | Yes | Yes | Yes | Yes |
| Performance | Best | Best | Good | Good |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Simple iteration | For-each | Cleanest syntax |
| No index needed | For-each | No unnecessary variable |
| Breaking early | For loop | For-each can't break |
| Removing elements | Iterator | Safe removal |
| Functional style | forEach() | Declarative |

## Production Recommendations

> **Use for-each for simple iteration** — it's the cleanest and safest option.

> **Use for-each with arrays** — works with both collections and arrays.

> **Avoid for-each when removing elements** — use Iterator or removeIf().

> **Use forEach() for functional style** — lambda syntax is more modern.

## Engineering Trade-offs

| Trade-off | For-Each | Alternative |
|-----------|----------|-------------|
| Readability vs Control | Clean syntax | For loop: full control |
| Safety vs Flexibility | Safe iteration | Iterator: remove support |
| Simple vs Functional | Imperative | forEach(): declarative |
| Performance vs Features | Fastest | Stream: more features |

## Common Code Review Comments

- "This for loop can be simplified to for-each."
- "You're not using the index — switch to for-each."
- "This for-each modifies the collection — use Iterator."
- "Consider using forEach() for functional style."

## Common Production Mistakes

> Notice: For-each loop throws ConcurrentModificationException if collection is modified — use Iterator.

> Notice: For-each loop variable is final — can't reassign within loop.

> Notice: For-each with null collection throws NullPointerException — add null check.

> Notice: For-each can't break early — use for loop or stream().findFirst().
