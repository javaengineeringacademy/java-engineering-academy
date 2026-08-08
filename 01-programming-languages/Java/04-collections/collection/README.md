# Collection Interface

## 1. What Is It

The `Collection` interface is the root interface in the Java Collections Framework hierarchy (excluding `Iterable`). It represents a group of objects known as elements.

## 2. Collection Contract

| Method | Description | Complexity |
|--------|-------------|------------|
| `add(E e)` | Adds element | O(1) to O(n) |
| `remove(Object o)` | Removes element | O(1) to O(n) |
| `contains(Object o)` | Checks membership | O(1) to O(n) |
| `iterator()` | Returns iterator | O(1) |
| `size()` | Element count | O(1) |
| `isEmpty()` | Empty check | O(1) |
| `clear()` | Removes all | O(n) |
| `toArray()` | Converts to array | O(n) |

### Bulk Operations

| Method | Description | Complexity |
|--------|-------------|------------|
| `addAll(Collection)` | Adds all from collection | O(n) |
| `removeAll(Collection)` | Removes matching | O(n*m) |
| `retainAll(Collection)` | Keeps only matching | O(n*m) |
| `containsAll(Collection)` | Checks all present | O(n*m) |

## 3. Relationship with Iterable

```
Iterable<E>          →  provides iterator()
└── Collection<E>    →  extends Iterable, adds bulk operations
```

Every Collection can be used in for-each loops because it extends `Iterable<E>`.

## 4. Relationship with List, Set

```
Collection<E>
├── List<E>      →  ordered, duplicates allowed
├── Set<E>       →  no duplicates
├── Queue<E>     →  FIFO operations
└── Deque<E>     →  double-ended queue
```

## 5. Characteristics

| Characteristic | Description |
|----------------|-------------|
| Ordering | Depends on implementation (ordered/unordered) |
| Duplicates | Depends on subinterface (List allows, Set doesn't) |
| Null | Most implementations allow one null element |
| Thread Safety | Not thread-safe by default |

## 6. Implementations Overview

| Implementation | Structure | Ordering | Duplicates |
|---------------|-----------|----------|------------|
| ArrayList | Dynamic array | Index-based | Yes |
| LinkedList | Doubly-linked list | Insertion order | Yes |
| HashSet | Hash table | No order | No |
| LinkedHashSet | Hash + linked list | Insertion order | No |
| TreeSet | Red-black tree | Sorted | No |
| PriorityQueue | Binary heap | Priority order | Yes |
| ArrayDeque | Resizable array | FIFO/LIFO | Yes |

## 7. When to Use Collection Directly

Use `Collection<E>` as parameter type when:
- Writing algorithms that work with any collection type
- Need polymorphic behavior across List, Set, Queue
- Using bulk operations (addAll, removeAll)
- Defining generic APIs that accept any collection

```java
public static <E> Collection<E> filter(Collection<E> c, Predicate<E> p) {
    return c.stream().filter(p).collect(Collectors.toList());
}
```

## 8. Common Mistakes

1. **Raw types**: Always use generics `Collection<String>` not `Collection`
2. **Ignoring return values**: `add()` returns boolean
3. **Modifying during iteration**: Use `Iterator.remove()` or `removeIf()`
4. **Confusing Collection with Collections**: Collection is interface, Collections is utility class

## 9. One-Minute Revision

- Root interface for all collections (excluding Iterable)
- Provides add, remove, contains, size, iterator
- Extends Iterable for for-each support
- Subinterfaces: List, Set, Queue, Deque
- Use specific subinterfaces for specialized operations

## 10. References

- [Oracle Java Documentation - Collection](https://docs.oracle.com/javase/8/docs/api/java/util/Collection.html)
- [Java Collections Framework Tutorial](https://docs.oracle.com/javase/tutorial/collections/)
