# Lambda/Stream Memory Behavior

## Memory Characteristics

### Stream Objects
- Each stream operation creates new Stream object
- Intermediate operations are lazy
- Terminal operations trigger actual processing

### Lambda Capture
- Lambdas capture variables from enclosing scope
- Effectively final variables can be captured
- Captured variables create closure objects

## Stream Pipeline Memory

```java
list.stream()           // Creates Stream object
    .filter(...)        // Creates new Stream (lazy)
    .map(...)           // Creates new Stream (lazy)
    .collect(...)       // Terminal operation, processes all
```

## Memory Overhead

| Operation | Memory Impact |
|-----------|---------------|
| Stream creation | Stream object |
| Intermediate ops | New Stream objects |
| Lambda capture | Closure object |
| Parallel stream | ForkJoinPool threads |

## Best Practices

1. Prefer method references over lambdas
2. Avoid creating streams in tight loops
3. Use parallel streams for CPU-intensive operations
4. Consider memory overhead for small collections
