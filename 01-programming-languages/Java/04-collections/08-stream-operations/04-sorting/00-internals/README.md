# Sorting Operations Internals

## How Sorting Works Internally

### Stream Pipeline

Sorting operations are intermediate operations in the Java Stream API pipeline.

```
Source → Sorting → Intermediate → Terminal → Result
```

### Lazy Evaluation

Sorting operations are lazy - they don't process elements until a terminal operation is invoked.

```java
list.stream()
    .sorted(...)  // Creates pipeline, no processing yet
    .collect(...);   // Triggers actual processing
```

### Short-Circuit Behavior

Some sorting operations can short-circuit:

```java
// Stops processing when condition is met
list.stream()
    .sorted()
    .findFirst();  // May not process all elements
```

### Internal Iteration

Unlike loops, sorting uses internal iteration:

```java
// External iteration (for loop)
List<T> sorted = new ArrayList<>(list);
Collections.sort(sorted);

// Internal iteration (stream)
List<T> sorted = list.stream().sorted().collect(Collectors.toList());
```

## Performance Characteristics

| Operation | Time Complexity | Space Complexity |
|-----------|----------------|------------------|
| sorted | O(n log n) | O(n) |
| sorted(comparator) | O(n log n) | O(n) |

## Memory Behavior

- Sorting operations create a new stream
- No intermediate collection is created
- Elements are processed one at a time
- Memory usage is constant during processing

## Thread Safety

- Sorting operations are not thread-safe
- Use parallel streams for concurrent processing
- Ensure thread-safe sources if using parallel streams
