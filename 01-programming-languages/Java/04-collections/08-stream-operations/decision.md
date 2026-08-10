# Stream Operations — Engineering Decision Framework

## Decision Tree

```
Need to process collections?
├── Simple iteration → for-each loop
├── Need to filter/transform → Stream API
├── Need parallel processing → parallelStream()
└── Need imperative style → traditional loops
```

## Comparison Matrix

| Approach | Readability | Performance | Memory | When to Use |
|----------|-------------|-------------|--------|-------------|
| Traditional Loop | Medium | High | Low | Simple operations |
| Stream API | High | Medium-High | Medium | Complex pipelines |
| Parallel Stream | Medium | High (CPU-bound) | High | Large datasets, CPU-bound |

## Selection Criteria

| Criterion | Stream | Parallel Stream |
|-----------|--------|-----------------|
| Data size | < 10K elements | > 10K elements |
| Operation type | Sequential | CPU-bound |
| Thread safety | N/A | Required |
| Ordering | Required | Not required |

## Production Recommendations

1. **Default to streams** for complex pipelines
2. **Use parallel streams** only for CPU-bound operations on large datasets
3. **Avoid parallel streams** for I/O-bound operations
4. **Use try-with-resources** for stream sources that implement AutoCloseable
5. **Prefer collectors** over manual accumulation

## Engineering Trade-offs

| Trade-off | Decision |
|-----------|----------|
| Readability vs Performance | Streams for readability; loops for performance-critical code |
| Memory vs Speed | Streams create intermediate objects; loops are more memory-efficient |
| Parallelism vs Complexity | Parallel streams add thread-safety concerns |

## Common Code Review Comments

1. "Use `filter().map().collect()` instead of manual loops"
2. "This parallel stream could cause thread-safety issues"
3. "Use `toList()` instead of `collect(Collectors.toList())`"
4. "Avoid side effects in stream operations"
5. "Use `flatMap()` for nested collections"

## Common Production Mistakes

1. Using parallel streams with shared mutable state
2. Not closing stream sources that implement AutoCloseable
3. Using `reduce()` incorrectly (missing identity value)
4. Creating streams in tight loops (performance overhead)
5. Using `findFirst()` when order doesn't matter (use `findAny()`)
