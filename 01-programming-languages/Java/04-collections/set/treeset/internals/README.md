# TreeSet Internals

## Backed by TreeMap

```
TreeSet<E> is backed by TreeMap<E, Object>

private transient NavigableMap<E, Object> m;
private static final Object PRESENT = new Object();
```

## Red-Black Tree Structure

```
TreeMap uses a Red-Black tree:

        ┌───┐
        │ 5 │  (root, BLACK)
        └─┬─┘
      ┌───┴───┐
      │       │
   ┌──┴──┐ ┌──┴──┐
   │  3   │ │  7   │  (RED)
   └──┬──┘ └──┬──┘
     │         │
  ┌──┴──┐   ┌──┴──┐
  │ 1   │   │ 9   │  (BLACK)
  └─────┘   └─────┘

Red-Black tree properties:
  1. Every node is red or black
  2. Root is always black
  3. All null children are black
  4. Red node's children are both black
  5. All paths from root to null have same black count
```

## TreeMap Internal Structure

```
TreeMap.Entry:
┌──────────────────────────────┐
│  K key                        │
│  V value (PRESENT)            │
│  Entry<K,V> left              │
│  Entry<K,V> right             │
│  Entry<K,V> parent            │
│  boolean color (RED/BLACK)    │
└──────────────────────────────┘
```

## Operation Flow

### add(element)

```
1. map.put(element, PRESENT)
2. TreeMap traverses tree to find insertion point
3. Compares using Comparator or Comparable
4. Inserts new Entry, rebalances (rotations)
5. If element exists: returns PRESENT (not added)

Time: O(log n)
```

### contains(element)

```
1. map.containsKey(element)
2. Binary search through tree
3. Returns true if found

Time: O(log n)
```

### remove(element)

```
1. map.remove(element)
2. Finds node, removes from tree
3. Rebalances if needed
4. Returns PRESENT if removed, null if not found

Time: O(log n)
```

### First/Last

```
first() — leftmost node
last()  — rightmost node

Both O(log n) — traverse one path down
```

## Tree Rotations

```
Left rotation on node X:
      X                Y
     / \              / \
    A   Y    →      X   C
       / \         / \
      B   C       A   B

Right rotation on node Y:
      Y                X
     / \              / \
    X   C    →      A   Y
   / \                 / \
  A   B               B   C
```

## Memory Layout Diagram

```
TreeSet instance:
┌─────────────────────────────────┐
│  Object header     (12 bytes)   │
│  TreeMap m ref     (8 bytes)    │
└──────────────┬──────────────────┘
               │
               ▼
     ┌─────────────────────────────────────────┐
     │  TreeMap                                 │
     │  Entry<K,V> root ──► Tree nodes         │
     │                                          │
     │         ┌──────┐                         │
     │         │  5   │ (root)                 │
     │         └──┬───┘                         │
     │        ┌───┴───┐                         │
     │        │       │                         │
     │    ┌───┴──┐ ┌───┴──┐                    │
     │    │  3   │ │  7   │                    │
     │    └──┬──┘ └──┬──┘                     │
     │       │       │                         │
     │   ┌───┴──┐ ┌───┴──┐                    │
     │   │  1   │ │  9   │                    │
     │   └─────┘ └─────┘                      │
     └─────────────────────────────────────────┘
```

## Thread Safety

TreeSet is **not** synchronized. Concurrent access can cause:
- Corrupted tree structure
- Infinite loops during traversal
- ConcurrentModificationException

Use `Collections.synchronizedSortedSet()` or concurrent alternatives.

## Key Implementation Details

1. **Sorted order** — Elements are always in sorted order.
   Iteration returns elements in ascending order.

2. **No hash-based access** — Unlike HashSet, no O(1) lookup.
   Every operation requires tree traversal O(log n).

3. **Comparator support** — Can use custom Comparator for ordering:
   ```java
   TreeSet<String> set = new TreeSet<>(Comparator.reverseOrder());
   ```

4. **NavigableSet methods** — Provides floor(), ceiling(), lower(),
   higher() for nearest-element queries.

5. **Subsets are views** — subSet(), headSet(), tailSet() return
   views backed by the same tree.

6. **Null elements** — TreeSet does NOT allow null elements
   (unlike HashSet). NullPointerException on add(null).
