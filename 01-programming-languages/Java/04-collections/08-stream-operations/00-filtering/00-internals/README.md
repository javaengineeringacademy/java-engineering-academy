# Filtering Operations Internals

## How Filtering Works Internally

### Stream Pipeline

Filtering operations are intermediate operations in the Java Stream API pipeline.

```
Source → Filtering → Intermediate → Terminal → Result
```

### Lazy Evaluation

Filtering operations are lazy - they don't process elements until a terminal operation is invoked.

```java
list.stream()
    .filter(...)  // Creates pipeline, no processing yet
    .collect(...);   // Triggers actual processing
```

### Short-Circuit Behavior

Some filtering operations can short-circuit:

```java
// Stops processing when condition is met
list.stream()
    .filter(n -> n > 5)
    .findFirst();  // May not process all elements
```

### Internal Iteration

Unlike loops, filtering uses internal iteration:

```java
// External iteration (for loop)
for (T item : list) {
    // Processing
}

// Internal iteration (stream)
list.stream().filter(...);  // Stream controls iteration
```

## Performance Characteristics

| Operation | Time Complexity | Space Complexity |
|-----------|----------------|------------------|
| filter | O(n) | O(1) |
| distinct | O(n) | O(n) |
| takeWhile | O(n) | O(1) |
| dropWhile | O(n) | O(1) |

## Memory Behavior

- Filtering operations create a new stream
- No intermediate collection is created
- Elements are processed one at a time
- Memory usage is constant during processing

## Thread Safety

- Filtering operations are not thread-safe
- Use parallel streams for concurrent processing
- Ensure thread-safe sources if using parallel streams
