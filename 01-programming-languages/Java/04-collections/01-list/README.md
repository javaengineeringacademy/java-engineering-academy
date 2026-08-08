# List Interface

## 1. What Is It

The `List` interface is an ordered collection (sequence) that allows duplicate elements. It provides positional access, search, iteration, and range-view operations.

## 2. Characteristics

| Characteristic | Description |
|----------------|-------------|
| Ordering | Maintains insertion order (index-based) |
| Duplicates | Allows duplicate elements |
| Null | Allows multiple null elements |
| Indexed | 0-based index access |
| Iteration | Sequential, bidirectional |

## 3. List Interface Methods

### Positional Access

| Method | Description | Complexity |
|--------|-------------|------------|
| `get(int index)` | Returns element at index | O(1) ArrayList, O(n) LinkedList |
| `set(int index, E element)` | Replaces element at index | O(1) ArrayList, O(n) LinkedList |
| `add(int index, E element)` | Inserts element at index | O(n) |
| `remove(int index)` | Removes element at index | O(n) |
| `indexOf(Object o)` | First occurrence index | O(n) |
| `lastIndexOf(Object o)` | Last occurrence index | O(n) |
| `subList(int from, int to)` | Returns view [from, to) | O(1) |

### Search

| Method | Description | Complexity |
|--------|-------------|------------|
| `contains(Object o)` | Checks if element exists | O(n) |
| `isEmpty()` | Checks if empty | O(1) |
| `size()` | Returns element count | O(1) |

## 4. List Contract

1. **Ordered**: Elements have defined index positions
2. **Indexed**: Each element has an integer index (0-based)
3. **Duplicates**: Same element can appear multiple times
4. **Null elements**: Multiple null elements allowed
5. **Fail-fast iterators**: Throw ConcurrentModificationException

## 5. Performance Expectations

| Operation | ArrayList | LinkedList | Vector |
|-----------|-----------|------------|--------|
| get(index) | O(1) | O(n) | O(1) |
| add(end) | O(1) amortized | O(1) | O(1) amortized |
| add(index) | O(n) | O(n) | O(n) |
| remove(index) | O(n) | O(n) | O(n) |
| contains | O(n) | O(n) | O(n) |
| Iterator | O(1) per next | O(1) per next | O(1) per next |

## 6. Implementation Comparison

| Feature | ArrayList | LinkedList | Vector |
|---------|-----------|------------|--------|
| Structure | Dynamic array | Doubly-linked list | Synchronized array |
| Random access | O(1) | O(n) | O(1) |
| Insert/remove mid | O(n) | O(1)* | O(n) |
| Memory | Low | High (nodes) | Low |
| Thread-safe | No | No | Yes (all sync) |
| Best for | Random access | Frequent insert/remove | Legacy code |

*O(1) after finding position

## 7. Common Mistakes

1. **Using indexOf() in hot loops**: O(n) per call
2. **Not pre-allocating capacity**: Wastes time resizing
3. **Modifying during iteration**: Causes ConcurrentModificationException
4. **Using subList as persistent view**: It's a live view of original

## 8. One-Minute Revision

- Ordered collection with index-based access
- Allows duplicates and multiple nulls
- ArrayList: best for random access
- LinkedList: best for frequent insert/remove
- Vector: legacy synchronized, avoid in new code
- Use Iterator.remove() for safe removal during traversal

## 9. References

- [Oracle Java Documentation - List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)
- [Java Collections Framework Tutorial](https://docs.oracle.com/javase/tutorial/collections/)
