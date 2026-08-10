# Transforming Operations Internals

## How Transforming Works Internally

### Stream Pipeline

Transforming operations are intermediate operations in the Java Stream API pipeline.

```
Source → Transforming → Intermediate → Terminal → Result
```

### Lazy Evaluation

Transforming operations are lazy - they don't process elements until a terminal operation is invoked.

```java
list.stream()
    .map(...)  // Creates pipeline, no processing yet
    .collect(...);   // Triggers actual processing
```

### Short-Circuit Behavior

Some transforming operations can short-circuit:

```java
// Stops processing when condition is met
list.stream()
    .map(n -> n * 2)
    .findFirst();  // May not process all elements
```

### Internal Iteration

Unlike loops, transforming uses internal iteration:

```java
// External iteration (for loop)
for (T item : list) {
    // Processing
}

// Internal iteration (stream)
list.stream().map(...);  // Stream controls iteration
```

## Performance Characteristics

| Operation | Time Complexity | Space Complexity |
|-----------|----------------|------------------|
| map | O(n) | O(1) |
| flatMap | O(n) | O(1) |
| peek | O(n) | O(1) |

## Memory Behavior

- Transforming operations create a new stream
- No intermediate collection is created
- Elements are processed one at a time
- Memory usage is constant during processing

## Thread Safety

- Transforming operations are not thread-safe
- Use parallel streams for concurrent processing
- Ensure thread-safe sources if using parallel streams
