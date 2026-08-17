# Collecting Operations Internals

## How Collecting Works Internally

### Stream Pipeline

Collecting operations are terminal operations in the Java Stream API pipeline.

```
Source → Intermediate → Collecting → Result
```

### Lazy Evaluation

Collecting operations trigger processing of the entire pipeline.

```java
list.stream()
    .filter(...)
    .collect(...);  // Triggers processing of all elements
```

### Short-Circuit Behavior

Collecting operations typically process all elements:

```java
// Processes all elements
list.stream()
    .collect(Collectors.toList());  // Collects all elements
```

### Internal Iteration

Unlike loops, collecting uses internal iteration:

```java
// External iteration (for loop)
List<T> result = new ArrayList<>();
for (T item : list) {
    result.add(item);
}

// Internal iteration (stream)
List<T> result = list.stream().collect(Collectors.toList());
```

## Performance Characteristics

| Operation | Time Complexity | Space Complexity |
|-----------|----------------|------------------|
| toList | O(n) | O(n) |
| toSet | O(n) | O(n) |
| groupingBy | O(n) | O(n) |
| partitioningBy | O(n) | O(n) |

## Memory Behavior

- Collecting operations create a new collection
- Memory usage is proportional to the result size
- Intermediate objects may be created during collection

## Thread Safety

- Collecting operations are not thread-safe
- Use parallel streams for concurrent processing
- Ensure thread-safe collectors if using parallel streams
