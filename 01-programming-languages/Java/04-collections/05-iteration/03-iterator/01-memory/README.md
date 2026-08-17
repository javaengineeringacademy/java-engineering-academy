# Iterator Memory Behavior

## Memory Characteristics

### Iterator Object
- Each Iterator is a separate object on heap
- Holds reference to collection structure
- Maintains current position index internally

### ConcurrentModificationException
- Detected by comparing modification count
- Fast fail mechanism prevents data corruption
- Exception creation has memory overhead

## Iterator Types

| Type | Memory Overhead | Features |
|------|-----------------|----------|
| ArrayList Iterator | Low | Random access, index-based |
| LinkedList Iterator | Medium | Node traversal, no index |
| HashSet Iterator | Medium | Hash bucket traversal |

## Memory Patterns

```java
// Safe removal pattern
Iterator<T> it = list.iterator();
while (it.hasNext()) {
    T item = it.next();
    if (condition) {
        it.remove(); // No ConcurrentModificationException
    }
}
```

## Best Practices

1. Always use Iterator.remove() for safe removal
2. Don't create multiple iterators on same collection
3. Consider for-each loop for read-only operations
4. Use explicit Iterator for complex modification patterns
