# Spliterator Pattern

## 1. Scope

This folder covers the `Spliterator` interface for parallel and bulk iteration of collections in Java.
Examples and exercises demonstrate tryAdvance(), trySplit(), characteristics, and integration with the Stream API.

## 2. Why It Exists

Iterator processes one element at a time on a single thread. For large collections, this leaves multi-core CPUs underutilized:

```java
// Iterator — single-threaded, one element at a time
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    process(it.next()); // Only one CPU core used
}
```

Spliterator solves two problems:
1. **Splitting**: Divide a collection into independent chunks for parallel processing
2. **Bulk processing**: Process multiple elements per call for efficiency

It is the foundation of `parallelStream()` — the Stream API uses Spliterator to divide work across threads.

## 3. What It Is

`Spliterator<E>` (splitable iterator) extends the concept of Iterator with the ability to split itself into multiple Spliterators for parallel traversal.

```java
public interface Spliterator<E> {
    boolean tryAdvance(Consumer<? super E> action);
    Spliterator<E> trySplit();
    long estimateSize();
    int characteristics();
}
```

Key traits:
- Split-and-conquer pattern for parallel processing
- `tryAdvance()` processes one element (like Iterator)
- `trySplit()` creates a new Spliterator for a portion of the data
- `characteristics()` describes the Spliterator's properties
- Foundation for `parallelStream()`

## 4. Internal Working

### Splitting pattern

```
Original Spliterator: [A, B, C, D, E, F, G, H]

trySplit() #1:
┌─────────────┐  ┌─────────────┐
│ [A, B, C, D] │  │ [E, F, G, H] │
│  spliterator │  │  spliterator │
└──────┬──────┘  └──────┬──────┘
       │                │
trySplit() #2a        trySplit() #2b
┌───────┐ ┌───────┐  ┌───────┐ ┌───────┐
│[A, B] │ │[C, D] │  │[E, F] │ │[G, H] │
└───┬───┘ └───┬───┘  └───┬───┘ └───┬───┘
    │         │          │         │
   Leaf     Leaf        Leaf      Leaf
(small enough to process directly)
```

### How tryAdvance works

```
Spliterator: [A, B, C, D]
cursor = 0

tryAdvance(consumer):
  if cursor < elements.length:
      consumer.accept(elements[cursor])
      cursor++
      return true
  return false
```

### How ArrayList's Spliterator works

```java
private final class ArrayListSpliterator implements Spliterator<E> {
    int cursor;
    int fence;
    int expectedModCount;

    ArrayListSpliterator(int origin, int fence, int expectedModCount) {
        this.cursor = origin;
        this.fence = fence;
        this.expectedModCount = expectedModCount;
    }

    public Spliterator<E> trySplit() {
        int mid = (cursor + fence) >>> 1; // Split in half
        if (mid <= cursor) return null;    // Too small to split
        int oldCursor = cursor;
        cursor = mid;
        return new ArrayListSpliterator(oldCursor, mid, expectedModCount);
    }

    public boolean tryAdvance(Consumer<? super E> consumer) {
        if (consumer == null) throw new NullPointerException();
        int i = cursor;
        if (i < fence) {
            cursor = i + 1;
            consumer.accept(elementData[i]);
            return true;
        }
        return false;
    }

    public long estimateSize() {
        return (long)(fence - cursor);
    }
}
```

### Characteristics

```
SIZED        ──► estimateSize() is exact (not -1)
ORDERED      ──► Elements have a defined encounter order
DISTINCT     ──► No duplicate elements (optional)
SORTED       ──► Elements are in sorted order (optional)
NONNULL      ──► No null elements
IMMUTABLE    ──► Structure cannot change during traversal
CONCURRENT   ──► Multiple threads can safely iterate
SUBSIZED     ──► All splits are also SIZED
```

## 5. Constructors / Usage

### Basic tryAdvance
```java
Spliterator<String> spliterator = list.spliterator();
while (spliterator.tryAdvance(System.out::println)) {
    // Processes one element at a time
}
```

### Splitting for parallel processing
```java
Spliterator<String> spliterator = list.spliterator();
Spliterator<String> half1 = spliterator.trySplit();
Spliterator<String> half2 = spliterator.trySplit(); // May return null

// Now process in parallel
ExecutorService executor = Executors.newFixedThreadPool(2);
executor.submit(() -> half1.forEachRemaining(System.out::println));
executor.submit(() -> half2.forEachRemaining(System.out::println));
executor.shutdown();
```

### Using with Stream API
```java
// Spliterator is the backbone of streams
list.parallelStream() // Uses spliterator() internally
    .filter(s -> s.length() > 3)
    .forEach(System.out::println);
```

### Custom Spliterator
```java
class ArraySpliterator implements Spliterator<Integer> {
    private final int[] array;
    private int cursor;

    ArraySpliterator(int[] array) {
        this.array = array;
        this.cursor = 0;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Integer> action) {
        if (cursor < array.length) {
            action.accept(array[cursor++]);
            return true;
        }
        return false;
    }

    @Override
    public Spliterator<Integer> trySplit() {
        int mid = (cursor + array.length) >>> 1;
        if (mid <= cursor) return null;
        int oldCursor = cursor;
        cursor = mid;
        return new ArraySpliterator(Arrays.copyOfRange(array, oldCursor, mid));
    }

    @Override
    public long estimateSize() {
        return array.length - cursor;
    }

    @Override
    public int characteristics() {
        return SIZED | ORDERED | IMMUTABLE | NONNULL;
    }
}
```

### forEachRemaining for bulk processing
```java
Spliterator<String> spliterator = list.spliterator();
spliterator.forEachRemaining(s -> {
    // Processes remaining elements in bulk
    process(s);
});
```

## 6. Methods

| Method | Description | Returns |
|--------|-------------|---------|
| `tryAdvance(Consumer)` | Process one element if available | boolean |
| `trySplit()` | Split off a portion of remaining elements | Spliterator or null |
| `estimateSize()` | Estimated number of remaining elements | long |
| `characteristics()` | Bitmask of Spliterator properties | int |
| `getExactSizeIfKnown()` | Exact size if SIZED, else -1 | long |
| `forEachRemaining(Consumer)` | Process all remaining elements | void |
| `hasCharacteristics(int)` | Check if characteristics match | boolean |

## 7. Complexity Table

| Operation | Time | Space | Notes |
|-----------|------|-------|-------|
| tryAdvance() | O(1) | O(1) | Process one element |
| trySplit() | O(1) | O(1) | Create new Spliterator |
| estimateSize() | O(1) | O(1) | Return calculated size |
| forEachRemaining() | O(n) | O(1) | Process all remaining |
| Full parallel traversal | O(n/p) | O(p) | p = number of processors |
| Sequential traversal | O(n) | O(1) | Single-threaded |

## 8. Thread Safety

Spliterator's thread safety depends on its characteristics:

```java
// CONCURRENT characteristic — safe for parallel access
Spliterator<String> spliterator = concurrentCollection.spliterator();
// Multiple threads can call tryAdvance() concurrently

// Not CONCURRENT — need external synchronization
Spliterator<String> spliterator = arrayList.spliterator();
// Only one thread should call tryAdvance() at a time
```

| Characteristic | Thread Safety | Notes |
|---------------|--------------|-------|
| CONCURRENT | Thread-safe | Multiple threads can iterate |
| Not CONCURRENT | Not thread-safe | Single thread or external sync |
| IMMUTABLE | Safe to share | No structural changes possible |
| NOT IMMUTABLE | Dangerous to share | Collection might change |

For parallel streams, the Stream API handles thread management and splitting automatically.

## 9. Memory Behavior

```
Spliterator object:
┌────────────────────────────────┐
│ Object header (12 bytes)       │
│ cursor (int, 4 bytes)          │
│ fence (int, 4 bytes)           │
│ expectedModCount (int, 4 bytes)│
│ (padding 4 bytes)              │
└────────────────────────────────┘
Total: ~28-32 bytes per Spliterator

Each trySplit() creates a new Spliterator:
  Original: [A B C D E F G H]
  Split 1:  [A B C D] (new object)
  Split 2:  [E F G H] (original modified)
```

| Operation | Memory | Notes |
|-----------|--------|-------|
| spliterator() | ~32 bytes | Initial object |
| trySplit() | ~32 bytes per split | New Spliterator per split |
| forEachRemaining() | O(1) | No additional allocation |
| Parallel streams | O(p × 32) | p = parallelism level |

## 10. Production Incidents

### Incident 1: Custom Spliterator Returns Wrong estimateSize()

**Problem:** Parallel stream processes fewer elements than expected.
**Cause:** Custom Spliterator returned incorrect `estimateSize()`, causing the Stream framework to not split the data enough.
**Impact:** Some elements skipped, data loss.
**Solution:** Fixed `estimateSize()` to return accurate count.
**Prevention:** Test `estimateSize()` against actual element count. Implement `characteristics()` with SIZED flag if size is exact.

### Incident 2: Spliterator Split Creates Imbalanced Chunks

**Problem:** Parallel stream slower than sequential on uneven data.
**Cause:** Custom Spliterator always splits in the middle, but some chunks take much longer to process than others.
**Impact:** One thread finishes early, others still working. Parallelism wasted.
**Solution:** Implemented weighted splitting based on estimated work per element.
**Prevention:** Consider work distribution when designing Spliterator splits.

### Incident 3: Spliterator Used Across Collection Modification

**Problem:** ConcurrentModificationException in parallel stream.
**Cause:** Collection was modified while parallel stream was processing it. Spliterator detected the modification.
**Impact:** Stream processing fails.
**Solution:** Used ConcurrentHashMap or CopyOnWriteArrayList for parallel streams.
**Prevention:** Use concurrent collections for parallel stream processing.

## 11. Engineering Decision Framework

### When Should I Use This?
- Implementing a custom collection that needs parallel iteration
- Building a data source for parallel streams
- Processing large datasets that benefit from splitting
- Needing fine-grained control over parallel traversal

### When Should I NOT Use This?
- Simple sequential iteration (use Iterator or enhanced for)
- Small collections (parallel overhead exceeds benefits)
- You don't need parallel processing (use Stream API or Iterator)

### What Are the Alternatives?

| Alternative | When to Use | Trade-off |
|-------------|-------------|-----------|
| Iterator | Sequential traversal | No parallel support |
| Stream API | Functional pipelines | Less control over splitting |
| parallelStream() | Built-in parallelism | Automatic, less customizable |
| ForkJoinPool | Custom parallel algorithms | More control, more complexity |

### What Trade-offs Am I Making?
- **Complexity**: Spliterator is harder to implement correctly
- **Control**: Full control over splitting strategy vs automatic Stream API
- **Performance**: Parallel processing benefits depend on data size and split quality

### What Would I Choose in Production?
> Use `parallelStream()` for most parallel processing needs. Implement custom Spliterator only when you need fine-grained control over splitting behavior or have a non-standard data source.

### Code Review Comments
- "Use parallelStream() instead of manual Spliterator — simpler."
- "Your Spliterator.estimateSize() is wrong — it skips elements."
- "This collection is not CONCURRENT — don't use it for parallel streams."
- "Consider the SIZED characteristic if you know the exact size."

## 12. Performance

| Operation | Time | Space | Notes |
|-----------|------|-------|-------|
| tryAdvance() | O(1) | O(1) | Process one element |
| trySplit() | O(1) | O(1) | Create new Spliterator |
| Sequential traversal | O(n) | O(1) | Single thread |
| Parallel traversal | O(n/p) | O(p × 32 bytes) | p = parallelism level |
| Split overhead | O(1) | O(32 bytes) | Per split |

Performance factors:
- **Split quality**: Balanced splits maximize parallelism
- **Split cost**: Some collections have expensive splits (e.g., LinkedList)
- **Element processing cost**: Parallelism only helps if per-element work is significant
- **Data size**: Small collections have parallel overhead that exceeds benefits

## 13. Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Fewer elements processed | Check estimateSize() | Verify it returns correct count |
| Parallel slower than sequential | Profiling | Check split balance and overhead |
| ConcurrentModificationException | Stack trace | Use concurrent collections |
| NullPointerException in Spliterator | Debug | Check for null elements in data source |
| Uneven parallel workload | Thread dump | Check split strategy |

## 14. Code Review Checklist

- [ ] Custom Spliterator has correct estimateSize()
- [ ] characteristics() accurately describes the Spliterator
- [ ] trySplit() returns balanced chunks
- [ ] CONCURRENT flag set correctly for thread-safe collections
- [ ] SIZED flag set when size is exact
- [ ] Not splitting too small (< threshold wastes overhead)
- [ ] forEachRemaining() implemented for bulk processing

## 15. Architecture Considerations

### Where Spliterator Fits in System Design

| Layer | Use Case | Why Spliterator |
|-------|----------|----------------|
| Stream API | Parallel stream backbone | Automatic splitting |
| Custom Collections | Parallel iteration support | Enables parallelStream() |
| Data Pipelines | Parallel data processing | Split-and-conquer |
| Large Datasets | Parallel batch processing | Multi-core utilization |

### Integration with Stream API

```
Collection.spliterator() ──► StreamSupport.stream() ──► parallelStream()
                    │
                    ├── trySplit() ──► ForkJoinPool threads
                    ├── tryAdvance() ──► Per-element processing
                    └── characteristics() ──► Stream optimization hints
```

## 16. Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Race condition in parallel traversal | Data corruption | Use CONCURRENT collections |
| Memory exhaustion from too many splits | OOM | Limit split depth |
| Incorrect estimateSize() | Data loss or duplication | Test against actual count |

## 17. Evolution & Modernization

| Version | Change | Impact |
|---------|--------|--------|
| Java 8 | Spliterator introduced | Foundation for parallel streams |
| Java 8 | Stream API | Uses Spliterator for parallel processing |
| Java 9 | Collectors improvements | Better parallel collection support |
| Java 16 | Record patterns | Future deconstruction Spliterator |

## 18. Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| Spliterator | 8.0 | Stable |
| trySplit() | 8.0 | Stable |
| characteristics() | 8.0 | Stable |
| parallelStream() | 8.0 | Stable |
| forEachRemaining() | 8.0 | Stable |

## 19. Best Practices

1. Let the Stream API handle Spliterator creation for parallel streams
2. Implement custom Spliterator only for non-standard data sources
3. Set characteristics() accurately for optimization hints
4. Implement trySplit() to create balanced chunks
5. Use forEachRemaining() for efficient bulk processing
6. Test estimateSize() against actual element count
7. Use CONCURRENT flag for thread-safe collections
8. Use SIZED flag when exact size is known

## 20. Common Mistakes

1. **Wrong estimateSize()**: Causes Stream framework to miscalculate splitting
2. **Unbalanced trySplit()**: Leaves some threads idle while others work
3. **Missing characteristics()**: Stream API cannot optimize
4. **Splitting too small**: Overhead exceeds parallelism benefits
5. **Not implementing forEachRemaining()**: Misses bulk processing optimization
6. **Using Spliterator for small collections**: Parallel overhead exceeds benefits

## 21. Common Myths

### Myth 1: Spliterator is always faster than Iterator
**Reality:** For sequential processing, Iterator and Spliterator are equivalent. Spliterator only helps with parallel processing.

### Myth 2: Spliterator handles thread safety automatically
**Reality:** Only if the Spliterator has the CONCURRENT characteristic. Otherwise, you need external synchronization.

### Myth 3: More splits always mean better performance
**Reality:** Over-splitting creates overhead. Each split creates a new object and the Stream framework has overhead managing many small chunks.

## 22. One-Minute Revision

- Spliterator splits collections for parallel processing
- tryAdvance() processes one element, trySplit() divides the data
- characteristics() tells the Stream API what optimizations are possible
- CONCURRENT flag means thread-safe iteration
- SIZED flag means estimateSize() is exact
- parallelStream() uses Spliterator internally
- Custom Spliterator needed only for non-standard data sources
- Parallel processing only helps for large datasets with significant per-element work

## 23. Related Topics

| Topic | Relationship |
|-------|-------------|
| Iterator | Sequential-only variant |
| Stream API | Uses Spliterator for parallel processing |
| parallelStream() | Automatic Spliterator-based parallelism |
| ForkJoinPool | Thread pool for parallel stream execution |
| Collection.spliterator() | Factory method for Spliterator |

## 24. Interview Questions

1. **What is the difference between Iterator and Spliterator?** — Spliterator supports splitting for parallel processing, has characteristics for optimization, and can process elements in bulk.

2. **What does trySplit() do?** — It divides the Spliterator into two parts: one for the caller and one for the returned Spliterator.

3. **What is the CONCURRENT characteristic?** — It indicates that the Spliterator is thread-safe and multiple threads can call tryAdvance() concurrently.

4. **When should you use parallel streams?** — Large datasets with significant per-element processing. Not worth it for small collections or fast operations.

5. **How does parallelStream() use Spliterator?** — It calls spliterator() to get a Spliterator, splits it across multiple threads, and each thread processes its portion using tryAdvance().

## 25. References

- [Oracle Java Documentation - Spliterator](https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html)
- [JLS - Interface Spliterator](https://docs.oracle.com/javase/specs/jls/se21/html/jls-6.html#jls-6.8.1)
- [Parallelism in Java 8 — Brian Goetz](https://www.youtube.com/watch?v=oE4p2sL0vQ0)
- [Effective Java - Item 48: Use parallel streams judiciously](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
