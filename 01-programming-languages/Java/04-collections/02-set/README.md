# Set Interface

## Scope

This folder focuses on the Set interface.
Examples and exercises compare and combine all Set implementations (HashSet, LinkedHashSet, TreeSet, EnumSet).

## Why It Exists

Before Java 1.2, developers used lists to represent mathematical sets, leading to:

1. **Duplicate elements**: Lists allowed duplicates where sets shouldn't
2. **Unordered iteration**: No guarantee of consistency
3. **Membership checking**: O(n) vs O(1) for HashSet
4. **Memory waste**: Storing duplicates where only unique elements needed

Set solved these problems with unique element guarantee, hashing for O(1) operations, and standardized mathematical set semantics.

## Design Rationale

JDK designers introduced Set separate from List because:

1. **Mathematical foundation**: Sets have clear contract (no duplicates, unordered but iterator order stable)
2. **Performance needs**: HashSet provides O(1) contains/lookup for membership testing
3. **Clarity**: Option in API design: List for ordered sequences, Set for unique collections
4. **HashSet optimization**: Uses HashMap internally with dummy values for presence tracking

**Trade-offs**: No ordering guarantees (HashSet), sorted order (TreeSet), or insertion order (LinkedHashSet) depending on implementation.

## 1. What Is It

The `Set` interface is a collection that contains no duplicate elements. It models the mathematical set abstraction and provides operations for membership testing.

## 2. Characteristics

| Characteristic | Description |
|----------------|-------------|
| No duplicates | Each element appears at most once |
| Membership testing | O(1) for HashSet, O(log n) for TreeSet |
| Ordering | Depends on implementation |
| Null | At most one null element (HashSet) |
| Mathematical Set | Models mathematical set |

## 3. Set Contract

| Method | Description | Complexity |
|--------|-------------|------------|
| `add(E e)` | Adds element if not present | O(1) HashSet, O(log n) TreeSet |
| `remove(Object o)` | Removes element | O(1) HashSet, O(log n) TreeSet |
| `contains(Object o)` | Checks membership | O(1) HashSet, O(log n) TreeSet |
| `size()` | Returns element count | O(1) |
| `isEmpty()` | Checks if empty | O(1) |
| `clear()` | Removes all elements | O(n) |

### Bulk Operations

| Method | Description |
|--------|-------------|
| `addAll(Collection)` | Union |
| `retainAll(Collection)` | Intersection |
| `removeAll(Collection)` | Difference |

## 4. Implementations Overview

| Implementation | Structure | Ordering | Null | Thread-Safe |
|---------------|-----------|----------|------|-------------|
| HashSet | Hash table | No order | One null | No |
| LinkedHashSet | Hash + linked list | Insertion order | One null | No |
| TreeSet | Red-black tree | Sorted | No | No |
| EnumSet | Bit vector | Enum order | No | No |
| CopyOnWriteArraySet | Array copy | No order | One null | Yes |

## 5. Performance Comparison

| Operation | HashSet | LinkedHashSet | TreeSet | EnumSet |
|-----------|---------|---------------|---------|---------|
| add | O(1) | O(1) | O(log n) | O(1) |
| remove | O(1) | O(1) | O(log n) | O(1) |
| contains | O(1) | O(1) | O(log n) | O(1) |
| Iteration | O(n) | O(n) | O(n) | O(n) |

## 6. When to Use Each

| Use Case | Implementation |
|----------|---------------|
| Fastest lookup, no order | HashSet |
| Insertion order matters | LinkedHashSet |
| Sorted elements | TreeSet |
| Enum constants | EnumSet |
| Thread-safe | CopyOnWriteArraySet |

## 7. Common Mistakes

1. **Overriding hashCode/equals incorrectly**: Breaks Set behavior
2. **Using mutable objects as elements**: Hash changes, element lost
3. **Not considering null behavior**: HashSet allows one null, TreeSet doesn't

## 8. One-Minute Revision

- No duplicate elements
- Membership testing: contains() is O(1) for HashSet
- HashSet: fastest, no order
- LinkedHashSet: maintains insertion order
- TreeSet: sorted, O(log n) operations
- EnumSet: best for enum constants

## 9. References

- [Oracle Java Documentation - Set](https://docs.oracle.com/javase/8/docs/api/java/util/Set.html)
- [Java Collections Framework Tutorial](https://docs.oracle.com/javase/tutorial/collections/)
