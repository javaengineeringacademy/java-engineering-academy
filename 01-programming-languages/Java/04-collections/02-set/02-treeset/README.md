# TreeSet

## Scope

This folder focuses exclusively on TreeSet.
Examples, exercises, and quizzes in this folder cover only TreeSet concepts.

## 1. Why It Exists

TreeSet was introduced in Java 1.2 to provide a Set implementation that maintains elements in sorted order. HashSet loses ordering, which is problematic when you need both unique elements and sorted iteration.

## 2. What It Is

TreeSet is a NavigableSet implementation based on a TreeMap (Red-Black tree). It maintains elements in natural order or by a custom Comparator. All basic operations (add, remove, contains) are O(log n).

## 3. Internal Working

```java
// TreeSet uses TreeMap internally
private transient NavigableMap<E, Object> m;
private static final Object PRESENT = new Object();

public boolean add(E e) {
    return m.put(e, PRESENT) == null;
}
```

### Red-Black Tree Properties

- Root is black
- All leaves are null and black
- Red nodes have black children
- All paths from root to leaves have same number of black nodes
- Height: O(log n)

## 4. Constructors

```java
TreeSet<String> set = new TreeSet<>();
TreeSet<String> set = new TreeSet<>(Comparator.reverseOrder());
TreeSet<String> set = new TreeSet<>(collection);
```

## 5. Methods

### Set Methods

| Method | Description | Complexity |
|--------|-------------|------------|
| `add(E e)` | Adds element | O(log n) |
| `remove(Object o)` | Removes element | O(log n) |
| `contains(Object o)` | Checks membership | O(log n) |
| `size()` | Element count | O(1) |
| `first()` | Returns lowest element | O(log n) |
| `last()` | Returns highest element | O(log n) |

### NavigableSet Methods

| Method | Description | Complexity |
|--------|-------------|------------|
| `lower(E e)` | Greatest element less than e | O(log n) |
| `higher(E e)` | Least element greater than e | O(log n) |
| `floor(E e)` | Greatest element less than or equal to e | O(log n) |
| `ceiling(E e)` | Least element greater than or equal to e | O(log n) |
| `headSet(E to)` | Elements less than to | O(log n) |
| `tailSet(E from)` | Elements >= from | O(log n) |
| `subSet(E from, E to)` | Elements in range [from, to) | O(log n) |
| `pollFirst()` | Removes and returns lowest | O(log n) |
| `pollLast()` | Removes and returns highest | O(log n) |

## 6. Complexity Table

| Operation | Time | Space |
|-----------|------|-------|
| add(E) | O(log n) | O(1) |
| remove(Object) | O(log n) | O(1) |
| contains(Object) | O(log n) | O(1) |
| first() | O(log n) | O(1) |
| last() | O(log n) | O(1) |
| lower/higher | O(log n) | O(1) |
| headSet/tailSet | O(log n) | O(1) |

## 7. Thread Safety

TreeSet is NOT thread-safe:

```java
NavigableSet<String> syncSet = Collections.synchronizedNavigableSet(new TreeSet<>());
NavigableSet<String> concurrentSet = new ConcurrentSkipListSet<>();
```

## 8. Memory Behavior

Per entry: ~56 bytes (TreeNode with left/right/parent/color pointers)

| Type | Per-Element | 1M Elements |
|------|-------------|-------------|
| TreeSet | ~56 bytes | ~56 MB |
| HashSet | ~40 bytes | ~40 MB |

## 9. Production Incidents

### Incident 1: NullPointerException with Custom Comparator

**Problem:** NullPointerException when adding null element.
**Cause:** Custom comparator does not handle null.
**Impact:** Application crash.
**Solution:** Never add null to TreeSet, or handle null in comparator.

### Incident 2: Slow Performance with Bad Comparator

**Problem:** Application latency spikes.
**Cause:** Comparator causes unbalanced tree.
**Solution:** Ensure comparator produces balanced comparisons.

### Incident 3: Memory Leak from Mutable Objects

**Problem:** Cannot find elements after insertion.
**Cause:** Element's compareTo() depends on mutable fields.
**Solution:** Use immutable objects for TreeSet elements.

## 10. Engineering Decision Framework

### When Should I Use This?
- Sorted elements required
- Range queries needed (headSet, tailSet, subSet)
- Navigation needed (lower, higher, floor, ceiling)
- Elements implement Comparable or you have a Comparator

### When Should I NOT Use This?
- **O(1) operations needed**: TreeSet is O(log n). Use HashSet
- **Memory is constrained**: TreeNode overhead. Use HashSet
- **Insertion order needed**: Use LinkedHashSet
- **Null elements**: TreeSet doesn't allow null

### What Are the Alternatives?

| Alternative | When to Use | Trade-off |
|-------------|-------------|-----------|
| HashSet | O(1) needed, no ordering | Faster, no sorting |
| LinkedHashSet | Insertion order needed | O(1), insertion order |
| EnumSet | Enum constants | Fastest for enums |
| ConcurrentSkipListSet | Thread-safe sorted set | Higher overhead |

### What Trade-offs Am I Making?
- **Sorting vs Speed**: O(log n) sorted vs O(1) unordered
- **Memory vs Sort**: Medium memory for sorting
- **Null vs Safety**: No null elements for sorted order
- **Immutability vs Flexibility**: Mutable vs Set.of() (immutable)

### What Would I Choose in Production?
> Use TreeSet only when you need sorted order — it's slower than HashSet for simple deduplication. Implement Comparable for natural ordering.

### Common Code Review Comments
- "Why are you using TreeSet? HashSet is faster if you don't need sorting."
- "This TreeSet requires Comparable — make sure your elements implement it."
- "Consider using NavigableSet methods for range queries."
- "This TreeSet is being iterated concurrently — use Collections.synchronizedSortedSet()."

### Common Production Mistakes

> Notice: TreeSet requires elements to be Comparable or you must provide a Comparator — otherwise you get ClassCastException.

> Notice: TreeSet doesn't allow null elements — it will throw NullPointerException.

> Notice: TreeSet is not thread-safe — even for reads, concurrent modification can cause data corruption.

> Notice: TreeSet performance is O(log n) — don't use it for simple deduplication where HashSet is faster.

## 11. Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| NullPointerException | Debug logging | Check comparator for null handling |
| Slow performance | Profiling | Check comparator balance |
| Elements not found | Debug logging | Check compareTo() implementation |

## 12. Code Review Checklist

- [ ] Elements implement Comparable or custom Comparator provided
- [ ] Comparator handles all edge cases
- [ ] No null elements added
- [ ] Mutable objects not used as elements
- [ ] Thread safety handled

## 13. Architecture Considerations

### Where TreeSet Fits in System Design

| Layer | Use Case | Why TreeSet |
|-------|----------|-------------|
| Service Layer | Sorted data storage | O(log n) sorted operations |
| API Gateway | Range query support | headSet/tailSet/subSet |
| Data Processing | Priority-based filtering | lower/higher/floor/ceiling |
| Scheduling | Time-sorted events | Natural ordering |
| Search | Ranked results | Sorted iteration |

### Integration Patterns

```
Client → API Gateway → TreeSet → Service → TreeSet → Client
                    ↓
            TreeSet → Range Query Engine → TreeSet
```

### Scaling Considerations

| Scale | Recommendation |
|-------|----------------|
| < 10K elements | TreeSet is optimal |
| 10K - 100K elements | TreeSet with proper Comparator |
| 100K - 1M elements | Consider ConcurrentSkipListSet |
| > 1M elements | Consider database with indexes |

### When to Replace TreeSet in Architecture

| Pattern | Replacement | Why |
|---------|-------------|-----|
| No sorting needed | HashSet | O(1) vs O(log n) |
| Insertion order | LinkedHashSet | O(1) + insertion order |
| Thread-safe sorted | ConcurrentSkipListSet | Concurrent access |
| Enum constants | EnumSet | Faster for enums |

## 14. Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Memory exhaustion | OutOfMemoryError | Set max size |
| DoS via bad comparator | Service degradation | Validate comparator |

## 15. Evolution & Modernization

| Version | Change | Impact |
|---------|--------|--------|
| Java 1.2 | TreeSet introduced | Sorted set |
| Java 6 | NavigableSet added | Navigation methods |
| Java 8 | Stream support | Stream processing |

## 16. Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| TreeSet | 1.2 | Stable |
| NavigableSet | 6.0 | Stable |

## 17. Best Practices

1. Use immutable objects as elements
2. Provide consistent Comparator
3. Never add null elements
4. Set initial capacity for known sizes
5. Use ConcurrentSkipListSet for concurrent sorted sets

## 18. Common Mistakes

1. Adding null elements
2. Using mutable objects
3. Bad Comparator implementation
4. Using when HashSet suffices

## 19. Common Myths

### Myth 1: TreeSet is always O(log n)
**Reality:** Amortized O(log n), but compareTo() can be expensive.

### Myth 2: TreeSet allows null
**Reality:** TreeSet throws NullPointerException for null (unlike HashSet).

### Myth 3: TreeSet is thread-safe
**Reality:** Not thread-safe. Use ConcurrentSkipListSet.

## 20. One-Minute Revision

- Sorted set based on Red-Black tree
- O(log n) for add/remove/contains
- NavigableSet methods: lower, higher, floor, ceiling
- No null elements allowed
- Elements must be Comparable or have Comparator
- Best for sorted unique elements with navigation

## 21. Related Topics

| Topic | Relationship |
|-------|-------------|
| TreeMap | Internal implementation |
| HashSet | Unordered alternative |
| LinkedHashSet | Insertion-order alternative |
| Comparable/Comparator | Sorting mechanism |
| NavigableSet | Navigation interface |

## 22. Interview Questions

1. **How does TreeSet work internally?** — Uses TreeMap (Red-Black tree). add() calls map.put().

2. **What is the time complexity of TreeSet operations?** — O(log n) for add/remove/contains.

3. **Does TreeSet allow null elements?** — No. Throws NullPointerException.

4. **What is the difference between TreeSet and HashSet?** — TreeSet is sorted O(log n), HashSet is unordered O(1).

5. **When should you use TreeSet?** — When sorted elements or range queries are needed.

## 23. References

- [Oracle Java Documentation - TreeSet](https://docs.oracle.com/javase/8/docs/api/java/util/TreeSet.html)
- [Java Collections Framework Tutorial](https://docs.oracle.com/javase/tutorial/collections/)
