# Collections Framework - Decision Guide

## Choosing the Right Collection

### Need ordered elements with index access?
- **ArrayList** - Fast random access, slow insert/delete in middle
- **LinkedList** - Slow random access, fast insert/delete at ends

### Need unique elements?
- **HashSet** - O(1) lookup, no order guarantee
- **LinkedHashSet** - O(1) lookup, insertion order preserved
- **TreeSet** - O(log n) lookup, sorted order

### Need key-value pairs?
- **HashMap** - O(1) lookup, no order
- **LinkedHashMap** - O(1) lookup, insertion order
- **TreeMap** - O(log n) lookup, sorted by key
- **Hashtable** - Legacy, synchronized, avoid using

### Need queue/deque behavior?
- **ArrayDeque** - Resizable array, faster than LinkedList for queues
- **PriorityQueue** - Elements processed by priority (natural or custom ordering)
- **LinkedList** - Implements both Queue and Deque

### Thread safety needed?
- **CopyOnWriteArrayList** - Read-heavy, write-light scenarios
- **ConcurrentHashMap** - High-concurrency read/write
- **Collections.synchronizedList()** - Simple synchronization wrapper
- **CopyOnWriteArraySet** - Thread-safe set for small sets, read-heavy

## Performance Comparison

| Operation | ArrayList | LinkedList | HashSet | HashMap |
|-----------|-----------|------------|---------|---------|
| Add       | O(1)*     | O(1)       | O(1)    | O(1)    |
| Remove    | O(n)      | O(1)       | O(1)    | O(1)    |
| Search    | O(1)      | O(n)       | O(1)    | O(1)    |
| Get by i  | O(1)      | O(n)       | N/A     | N/A     |

*amortized

## Stream Operations on Collections
- Use `stream()` for parallel processing of large datasets
- Use `filter().map().collect()` for functional transformations
- Use `Collectors.groupingBy()` for grouping operations
- Use `Collectors.toUnmodifiableList()` for immutable results

## Common Pitfalls
- Modifying a collection while iterating (use `Iterator.remove()` or `removeIf()`)
- Using `hashCode()`/`equals()` inconsistently in HashMap keys
- Forgetting to override both `hashCode()` and `equals()` together
- Using raw types instead of parameterized types
