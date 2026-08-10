# Combining Operations Internals

## How Combining Works Internally

### Stream Pipeline

Combining operations are intermediate operations in the Java Stream API pipeline.

```
Source → Combining → Intermediate → Terminal → Result
```

### Lazy Evaluation

Combining operations are lazy - they don't process elements until a terminal operation is invoked.

```java
stream1.concat(stream2)  // Creates pipeline, no processing yet
    .collect(...);   // Triggers actual processing
```

### Short-Circuit Behavior

Some combining operations can short-circuit:

```java
// Stops processing when condition is met
Stream.concat(stream1, stream2)
    .findFirst();  // May not process all elements
```

### Internal Iteration

Unlike loops, combining uses internal iteration:

```java
// External iteration (for loop)
List<T> result = new ArrayList<>();
result.addAll(list1);
result.addAll(list2);

// Internal iteration (stream)
List<T> result = Stream.concat(list1.stream(), list2.stream())
    .collect(Collectors.toList());
```

## Performance Characteristics

| Operation | Time Complexity | Space Complexity |
|-----------|----------------|------------------|
| concat | O(n + m) | O(1) |
| flatMap | O(n) | O(1) |

## Memory Behavior

- Combining operations create a new stream
- No intermediate collection is created
- Elements are processed one at a time
- Memory usage is constant during processing

## Thread Safety

- Combining operations are not thread-safe
- Use parallel streams for concurrent processing
- Ensure thread-safe sources if using parallel streams
