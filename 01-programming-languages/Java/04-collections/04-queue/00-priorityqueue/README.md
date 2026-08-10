# PriorityQueue

## Scope

This folder focuses exclusively on PriorityQueue.
Examples, exercises, and quizzes in this folder cover only PriorityQueue concepts.

## 1. Why It Exists

PriorityQueue was introduced in Java 1.5 to provide a priority-based queue implementation. Unlike FIFO queues, PriorityQueue processes elements based on priority (natural ordering or custom Comparator), not insertion order.

## 2. What It Is

PriorityQueue is an unbounded priority queue based on a binary heap. Elements are ordered by natural ordering or a provided Comparator. The head is always the least element.

## 3. Internal Working

```java
// PriorityQueue uses a binary heap (array-based)
private transient Object[] queue;
private int size = 0;
private final Comparator<? super E> comparator;
```

### Binary Heap Structure

```
PriorityQueue with elements [3, 1, 4, 1, 5, 9, 2]:

Array representation:
Index: 0  1  2  3  4  5  6
Value: 1  1  2  3  5  9  4

Tree representation:
              1 (index 0)
            /   \
        1 (1)   2 (2)
       /   \   /   \
    3 (3) 5 (4) 9 (5) 4 (6)

Parent-child relationship:
parent(i) = (i - 1) / 2
left(i) = 2 * i + 1
right(i) = 2 * i + 2
```

### Heap Operations

```java
// siftUp: Maintain heap property upward
private void siftUp(int k, E x) {
    while (k > 0) {
        int parent = (k - 1) >>> 1;
        Object e = queue[parent];
        if (compare(x, e) >= 0) break;
        queue[k] = e;
        k = parent;
    }
    queue[k] = x;
}

// siftDown: Maintain heap property downward
private void siftDown(int k, E x) {
    int half = size >>> 1;
    while (k < half) {
        int child = (k << 1) + 1;
        Object c = queue[child];
        int right = child + 1;
        if (right < size && compare(c, queue[right]) > 0)
            c = queue[right = child];
        if (compare(x, c) <= 0) break;
        queue[k] = c;
        k = right;
    }
    queue[k] = x;
}
```

## 4. Constructors

```java
PriorityQueue<String> pq = new PriorityQueue<>();                    // Natural order
PriorityQueue<String> pq = new PriorityQueue<>(Comparator.reverseOrder()); // Custom comparator
PriorityQueue<String> pq = new PriorityQueue<>(100);                 // Custom initial capacity
PriorityQueue<String> pq = new PriorityQueue<>(collection);          // From collection
PriorityQueue<String> pq = new PriorityQueue<>(List.of("a", "b"));  // From list
```

## 5. Methods

### Queue Methods

| Method | Throws Exception | Returns Special Value | Complexity |
|--------|-----------------|----------------------|------------|
| Insert | `add(E e)` | `offer(E e)` | O(log n) |
| Remove | `remove()` | `poll()` | O(log n) |
| Inspect | `element()` | `peek()` | O(1) |

### PriorityQueue-Specific Methods

| Method | Description | Complexity |
|--------|-------------|------------|
| `add(E e)` | Adds element | O(log n) |
| `offer(E e)` | Adds element | O(1) amortized |
| `remove()` | Removes head | O(log n) |
| `poll()` | Removes head | O(log n) |
| `element()` | Peeks at head | O(1) |
| `peek()` | Peeks at head | O(1) |
| `size()` | Element count | O(1) |
| `isEmpty()` | Checks if empty | O(1) |
| `clear()` | Removes all | O(n) |
| `contains(Object o)` | Checks membership | O(n) |
| `toArray()` | Converts to array | O(n) |

## 6. Complexity Table

| Operation | Time | Space |
|-----------|------|-------|
| add(E) | O(log n) | O(1) |
| offer(E) | O(1) amortized | O(1) |
| remove() | O(log n) | O(1) |
| poll() | O(log n) | O(1) |
| element() | O(1) | O(1) |
| peek() | O(1) | O(1) |
| size() | O(1) | O(1) |
| contains(Object) | O(n) | O(1) |
| Iterator.next() | O(1) | O(1) |

## 7. Thread Safety

PriorityQueue is NOT thread-safe:

```java
// Option 1: Explicit synchronization
PriorityQueue<String> pq = new PriorityQueue<>();
synchronized (pq) {
    // Access pq
}

// Option 2: PriorityBlockingQueue for concurrent access
PriorityBlockingQueue<String> pbq = new PriorityBlockingQueue<>();
```

## 8. Memory Behavior

### Memory Layout

```
PriorityQueue object (on heap):
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ queue reference (8 bytes)   │──────┐
│ size (int, 4 bytes)         │      │
│ comparator (8 bytes)        │      │
│ (padding 4 bytes)           │      │
└─────────────────────────────┘      │
                                     ▼
                              Object[] queue (binary heap)
                              ┌──────────────────┐
                              │ [0] → element 1   │
                              │ [1] → element 2   │
                              │ [2] → element 3   │
                              │ ...              │
                              └──────────────────┘
```

### Memory Comparison

| Type | Per-Element | 1M Elements |
|------|-------------|-------------|
| PriorityQueue | ~8 bytes | ~8 MB |
| ArrayList | ~8 bytes | ~8 MB |
| LinkedList | ~48 bytes | ~48 MB |

## 9. Production Incidents

### Incident 1: Slow Contains Operation

**Problem:** Application slows when checking if element exists.
**Cause:** PriorityQueue.contains() is O(n), not O(1).
**Impact:** Response time increases with queue size.
**Solution:** Use HashSet alongside PriorityQueue for fast membership testing.
**Prevention:** Use appropriate data structure for each operation.

### Incident 2: Memory Leak from Unprocessed Elements

**Problem:** Application crashes with OutOfMemoryError.
**Cause:** Elements added faster than processed.
**Impact:** Application crash, data loss.
**Detection:** Heap dump shows growing queue.
**Solution:** Implement backpressure or bounded queue.
**Prevention:** Monitor queue size, implement limits.

### Incident 3: Stale Elements in Priority Queue

**Problem:** Old elements processed instead of new ones.
**Cause:** PriorityQueue does not support updating priority.
**Impact:** Incorrect processing order.
**Solution:** Remove and re-add element with new priority, or use TreeMap.
**Prevention:** Design for priority update requirements.

## 10. Engineering Decision Framework

### Use PriorityQueue when:
- Priority-based processing needed
- Minimum or maximum element processing
- Merge sorted streams
- Top-K element problems

### Avoid PriorityQueue when:
- FIFO processing needed (use ArrayDeque)
- Priority updates needed (use TreeMap)
- Thread safety needed (use PriorityBlockingQueue)
- Contains operation is frequent (use HashSet)

### When NOT to Use PriorityQueue
- **FIFO**: Use ArrayDeque (faster)
- **Thread safety**: Use PriorityBlockingQueue
- **Unordered**: Use ArrayDeque (no heap maintenance)

### Alternatives

| Alternative | When to Use |
|-------------|-------------|
| ArrayDeque | FIFO processing |
| TreeMap | Priority updates needed |
| PriorityBlockingQueue | Thread-safe priority queue |
| TreeSet | Unique sorted elements |

## 11. Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Slow contains | Profiling | Use HashSet for membership testing |
| Memory leak | Heap dump | Check queue size growth |
| Wrong order | Debug logging | Check comparator implementation |
| ConcurrentModificationException | Thread dump | Use concurrent collection |

## 12. Code Review Checklist

- [ ] PriorityQueue used for priority-based processing
- [ ] Comparator correctly implements ordering
- [ ] Not used for FIFO (use ArrayDeque)
- [ ] Thread safety handled
- [ ] Queue size monitored
- [ ] Contains operation not in hot path

## 13. Architecture Considerations

### Where PriorityQueue Fits in System Design

| Layer | Use Case | Why PriorityQueue |
|-------|----------|-------------------|
| Task Scheduling | Priority-based job queue | O(log n) offer/poll |
| Event Processing | Priority event routing | Head = highest priority |
| Resource Mgmt | Resource allocation | Min/max element access |
| Search | Top-K element problems | Efficient head access |
| Merge | Sorted stream merging | Merge sorted inputs |

### Integration Patterns

```
Client → API Gateway → PriorityQueue → Service → PriorityQueue → Client
                    ↓
            PriorityQueue → Scheduler → PriorityQueue
```

### Scaling Considerations

| Scale | Recommendation |
|-------|----------------|
| < 10K elements | PriorityQueue is optimal |
| 10K - 100K elements | PriorityQueue with proper sizing |
| 100K - 1M elements | Consider PriorityBlockingQueue |
| > 1M elements | Consider database with priority indexing |

### When to Replace PriorityQueue in Architecture

| Pattern | Replacement | Why |
|---------|-------------|-----|
| FIFO processing | ArrayDeque | O(1) vs O(log n) |
| Thread-safe priority | PriorityBlockingQueue | Concurrent access |
| Priority updates | TreeMap | Update priority efficiently |
| Contains needed | HashSet + PriorityQueue | O(1) membership test |

## 14. Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Memory exhaustion | OutOfMemoryError | Set max size, use bounded queue |
| Unbounded growth | DoS | Implement backpressure |
| Comparator manipulation | Incorrect ordering | Validate comparator |

## 15. Evolution & Modernization

| Version | Change | Impact |
|---------|--------|--------|
| Java 5 | PriorityQueue introduced | Priority-based queue |
| Java 8 | Stream support | Stream processing |

## 16. Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| PriorityQueue | 5.0 | Stable |
| Stream support | 8.0 | Stable |

## 17. Best Practices

1. Use appropriate Comparator for ordering
2. Monitor queue size to prevent memory issues
3. Use HashSet for fast membership testing
4. Consider PriorityBlockingQueue for concurrent access
5. Use offer()/poll() for graceful failure handling

## 18. Common Mistakes

1. Using for FIFO processing (use ArrayDeque)
2. Assuming contains() is O(1)
3. Not handling null elements (PriorityQueue allows one null)
4. Ignoring memory growth
5. Using for priority updates (use TreeMap)

## 19. Common Myths

### Myth 1: PriorityQueue maintains insertion order
**Reality:** Maintains priority order, not insertion order.

### Myth 2: PriorityQueue is thread-safe
**Reality:** Not thread-safe. Use PriorityBlockingQueue.

### Myth 3: PriorityQueue.contains() is O(1)
**Reality:** contains() is O(n). Use HashSet for fast membership testing.

### Myth 4: PriorityQueue does not allow null
**Reality:** PriorityQueue allows one null element (treated as lowest priority).

## 20. One-Minute Revision

- Priority-based queue using binary heap
- O(log n) for add/remove, O(1) for peek
- Elements ordered by natural ordering or Comparator
- Head is always least element
- Not thread-safe, use PriorityBlockingQueue
- Best for priority-based processing, not FIFO

## 21. Related Topics

| Topic | Relationship |
|-------|-------------|
| Binary heap | Internal implementation |
| ArrayDeque | FIFO alternative |
| PriorityBlockingQueue | Thread-safe variant |
| Comparator | Custom ordering |
| Heap sort | Sorting algorithm using heap |

## 22. Interview Questions

1. **How does PriorityQueue work internally?** — Binary heap (array-based). Parent at (i-1)/2, children at 2i+1 and 2i+2.

2. **What is the time complexity of PriorityQueue operations?** — O(log n) for add/remove, O(1) for peek.

3. **Does PriorityQueue maintain insertion order?** — No. Maintains priority order (least element at head).

4. **Is PriorityQueue thread-safe?** — No. Use PriorityBlockingQueue for concurrent access.

5. **When should you use PriorityQueue?** — Priority-based processing, merge sorted streams, top-K problems.

## 23. References

- [Oracle Java Documentation - PriorityQueue](https://docs.oracle.com/javase/8/docs/api/java/util/PriorityQueue.html)
- [Java Collections Framework Tutorial](https://docs.oracle.com/javase/tutorial/collections/)
