# Parallel Operations Internals

## How Parallel Works Internally

### Stream Pipeline

Parallel operations are intermediate operations in the Java Stream API pipeline.

```
Source → Parallel → Intermediate → Terminal → Result
```

### Lazy Evaluation

Parallel operations are lazy - they don't process elements until a terminal operation is invoked.

```java
list.stream()
    .parallel()  // Creates pipeline, no processing yet
    .collect(...);   // Triggers actual processing
```

### Short-Circuit Behavior

Some parallel operations can short-circuit:

```java
// Stops processing when condition is met
list.stream()
    .parallel()
    .findFirst();  // May not process all elements
```

### Internal Iteration

Unlike loops, parallel uses internal iteration:

```java
// External iteration (for loop)
for (T item : list) {
    // Processing
}

// Internal iteration (stream)
list.stream().parallel().map(...);  // Stream controls iteration
```

## Performance Characteristics

| Operation | Time Complexity | Space Complexity |
|-----------|----------------|------------------|
| parallel | O(n) | O(n) |
| sequential | O(n) | O(1) |

## Memory Behavior

- Parallel operations create a new stream
- No intermediate collection is created
- Elements are processed one at a time
- Memory usage is constant during processing

## Thread Safety

- Parallel operations are thread-safe
- Use parallel streams for concurrent processing
- Ensure thread-safe sources if using parallel streams
