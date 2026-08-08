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

### Use TreeSet when:
- Sorted elements required
- Range queries needed (headSet, tailSet, subSet)
- Navigation needed (lower, higher, floor, ceiling)

### Avoid TreeSet when:
- O(1) operations needed (use HashSet)
- Memory is constrained (use HashSet)
- Insertion order needed (use LinkedHashSet)

### When NOT to Use TreeSet
- **O(1) needed**: TreeSet is O(log n). Use HashSet
- **Memory**: TreeNode overhead. Use HashSet
- **Null elements**: TreeSet doesn't allow null (in most implementations)

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

## 13. Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Memory exhaustion | OutOfMemoryError | Set max size |
| DoS via bad comparator | Service degradation | Validate comparator |

## 14. Evolution & Modernization

| Version | Change | Impact |
|---------|--------|--------|
| Java 1.2 | TreeSet introduced | Sorted set |
| Java 6 | NavigableSet added | Navigation methods |
| Java 8 | Stream support | Stream processing |

## 15. Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| TreeSet | 1.2 | Stable |
| NavigableSet | 6.0 | Stable |

## 16. Best Practices

1. Use immutable objects as elements
2. Provide consistent Comparator
3. Never add null elements
4. Set initial capacity for known sizes
5. Use ConcurrentSkipListSet for concurrent sorted sets

## 17. Common Mistakes

1. Adding null elements
2. Using mutable objects
3. Bad Comparator implementation
4. Using when HashSet suffices

## 18. Common Myths

### Myth 1: TreeSet is always O(log n)
**Reality:** Amortized O(log n), but compareTo() can be expensive.

### Myth 2: TreeSet allows null
**Reality:** TreeSet throws NullPointerException for null (unlike HashSet).

### Myth 3: TreeSet is thread-safe
**Reality:** Not thread-safe. Use ConcurrentSkipListSet.

## 19. One-Minute Revision

- Sorted set based on Red-Black tree
- O(log n) for add/remove/contains
- NavigableSet methods: lower, higher, floor, ceiling
- No null elements allowed
- Elements must be Comparable or have Comparator
- Best for sorted unique elements with navigation

## 20. Related Topics

| Topic | Relationship |
|-------|-------------|
| TreeMap | Internal implementation |
| HashSet | Unordered alternative |
| LinkedHashSet | Insertion-order alternative |
| Comparable/Comparator | Sorting mechanism |
| NavigableSet | Navigation interface |

## 21. Interview Questions

1. **How does TreeSet work internally?** — Uses TreeMap (Red-Black tree). add() calls map.put().

2. **What is the time complexity of TreeSet operations?** — O(log n) for add/remove/contains.

3. **Does TreeSet allow null elements?** — No. Throws NullPointerException.

4. **What is the difference between TreeSet and HashSet?** — TreeSet is sorted O(log n), HashSet is unordered O(1).

5. **When should you use TreeSet?** — When sorted elements or range queries are needed.

## 22. References

- [Oracle Java Documentation - TreeSet](https://docs.oracle.com/javase/8/docs/api/java/util/TreeSet.html)
- [Java Collections Framework Tutorial](https://docs.oracle.com/javase/tutorial/collections/)
