# TreeMap

## 1. Introduction

TreeMap is a `SortedMap` implementation based on a red-black tree data structure. It stores key-value pairs in sorted order according to the natural ordering of keys or a custom `Comparator`. TreeMap provides O(log n) time for basic operations (get, put, remove) and guarantees sorted iteration order.

TreeMap is the go-to choice when you need:
- Keys in sorted order
- Range queries (subMap, headMap, tailMap)
- Closest key lookups (floorKey, ceilingKey, lowerKey, higherKey)
- Guaranteed O(log n) performance (unlike HashMap's O(1) average but O(n) worst)

Unlike HashMap, TreeMap does not allow null keys (throws NullPointerException) but allows multiple null values. TreeMap is not thread-safe.

## 2. Learning Objectives

- Create and use TreeMap with natural ordering and custom comparators
- Understand the red-black tree data structure
- Learn about sorted map operations: firstKey, lastKey, subMap, headMap, tailMap
- Master navigation methods: floorKey, ceilingKey, lowerKey, higherKey
- Compare TreeMap vs HashMap vs LinkedHashMap
- Understand when TreeMap's O(log n) guarantees are worth the overhead
- Learn about TreeMap null key handling

## 3. Prerequisites

- Module 01: Java Fundamentals
- Module 02: Object-Oriented Programming
- Module 15: HashMap (understand hash table basics)
- Understanding of Comparable and Comparator interfaces
- Basic knowledge of balanced trees (red-black trees)

## 4. Why This Concept Exists

HashMap provides O(1) average performance but:
1. **No ordering**: Keys are in unpredictable order
2. **No range queries**: Can't efficiently find keys in a range
3. **No closest key lookup**: Can't find the closest key to a given value
4. **Worst case O(n)**: With many hash collisions

TreeMap provides:
1. **Sorted order**: Keys always in sorted order
2. **Range queries**: subMap, headMap, tailMap in O(log n + k)
3. **Navigation methods**: floorKey, ceilingKey, lowerKey, higherKey
4. **Guaranteed O(log n)**: Red-black tree ensures balanced tree

TreeMap is essential for:
- Applications requiring sorted key iteration
- Range-based queries (find all keys between A and M)
- Finding closest matches (nearest key lookup)
- Building ordered indexes

## 5. Problem Statement

Consider building a leaderboard system:
- Players have scores
- Need to display players in score order
- Need to find players in a score range
- Need to find the closest score to a given value
- Need to find the top N players

HashMap can't maintain order. TreeMap provides all these operations efficiently:
- `lastEntry()`: Highest score
- `subMap(low, high)`: Players in score range
- `floorKey(score)`: Closest score below
- `headMap(score)`: All players below a score

## 6. Theory

### Red-Black Tree Structure

TreeMap uses a red-black tree, a self-balancing binary search tree with these properties:
1. Every node is either red or black
2. The root is black
3. Every leaf (null) is black
4. If a node is red, both children are black
5. All paths from root to leaves have the same number of black nodes

These properties ensure the tree remains approximately balanced, guaranteeing O(log n) for all operations.

### Node Structure

```java
static final class Entry<K,V> implements Map.Entry<K,V> {
    K key;
    V value;
    Entry<K,V> left;
    Entry<K,V> right;
    Entry<K,V> parent;
    boolean color = BLACK;
}
```

### Comparison Methods

TreeMap uses either:
1. **Natural ordering**: Keys implement `Comparable<K>`
2. **Custom ordering**: `Comparator<K>` provided at construction

```java
// Natural ordering
TreeMap<String, Integer> natural = new TreeMap<>();

// Custom ordering (reverse)
TreeMap<String, Integer> reverse = new TreeMap<>(Comparator.reverseOrder());

// Custom comparator
TreeMap<String, Integer> byLength = new TreeMap<>(Comparator.comparing(String::length));
```

## 7. Internal Working

### The put() Operation

```java
public V put(K key, V value) {
    Entry<K,V> t = root;
    if (t == null) {
        // Tree is empty, create root
        compare(key, key); // Type check
        root = new Entry<>(key, value, null);
        size = 1;
        modCount++;
        return null;
    }

    int cmp;
    Entry<K,V> parent;
    Comparator<? super K> cpr = comparator;

    if (cpr != null) {
        // Use comparator
        while (true) {
            parent = t;
            cmp = cpr.compare(key, t.key);
            if (cmp < 0)
                t = t.left;
            else if (cmp > 0)
                t = t.right;
            else {
                V oldValue = t.value;
                t.value = value;
                return oldValue;
            }
        }
    } else {
        // Use natural ordering
        if (key == null) throw new NullPointerException();
        @SuppressWarnings("unchecked")
        Comparable<? super K> k = (Comparable<? super K>) key;
        while (true) {
            parent = t;
            cmp = k.compareTo(t.key);
            if (cmp < 0)
                t = t.left;
            else if (cmp > 0)
                t = t.right;
            else {
                V oldValue = t.value;
                t.value = value;
                return oldValue;
            }
        }
    }

    // Insert new entry
    Entry<K,V> e = new Entry<>(key, value, parent);
    if (cmp < 0)
        parent.left = e;
    else
        parent.right = e;
    fixAfterInsertion(e);
    size++;
    modCount++;
    return null;
}
```

### The get() Operation

```java
public V get(Object key) {
    Entry<K,V> p = getEntry(key);
    return (p == null ? null : p.value);
}

final Entry<K,V> getEntry(Object key) {
    if (comparator != null)
        return getEntryUsingComparator(key);
    if (key == null) throw new NullPointerException();
    @SuppressWarnings("unchecked")
    Comparable<? super K> k = (Comparable<? super K>) key;
    Entry<K,V> p = root;
    while (p != null) {
        int cmp = k.compareTo(p.key);
        if (cmp < 0)
            p = p.left;
        else if (cmp > 0)
            p = p.right;
        else
            return p;
    }
    return null;
}
```

### Red-Black Tree Rotations

When the tree becomes unbalanced after insertion or deletion, rotations are performed:

```java
// Left rotation
private void rotateLeft(Entry<K,V> p) {
    Entry<K,V> r = p.right;
    p.right = r.left;
    if (r.left != null)
        r.left.parent = p;
    r.parent = p.parent;
    if (p.parent == null)
        root = r;
    else if (p.parent.left == p)
        p.parent.left = r;
    else
        p.parent.right = r;
    r.left = p;
    p.parent = r;
}

// Right rotation (symmetric)
private void rotateRight(Entry<K,V> p) {
    // Similar to rotateLeft but mirrored
}
```

## 8. JVM Perspective

### Memory Allocation

```java
TreeMap<String, Integer> map = new TreeMap<>();
// JVM allocates:
// - TreeMap object header: 12 bytes
// - comparator reference: 8 bytes
// - size field: 4 bytes
// - modCount: 4 bytes
// - root reference: 8 bytes
// Total TreeMap object: ~40 bytes

// Each Entry:
// - Entry object header: 12 bytes
// - key reference: 8 bytes
// - value reference: 8 bytes
// - left reference: 8 bytes
// - right reference: 8 bytes
// - parent reference: 8 bytes
// - color boolean: 1 byte
// Total per Entry: ~52 bytes (rounded to 56 with alignment)
```

### Red-Black Tree Height

For n entries, the red-black tree height is at most 2 * log2(n + 1). This ensures O(log n) for all operations.

### JIT Optimization

The JIT compiler optimizes TreeMap operations:
- Inline comparison methods
- Optimize tree traversal paths
- Devirtualize Comparable/Comparator calls

## 9. Memory Representation

```
TreeMap<String, Integer> map = new TreeMap<>();
map.put("Charlie", 35);
map.put("Alice", 30);
map.put("Bob", 25);
map.put("Diana", 40);

Memory layout:
┌───────────────────────────────┐
│ TreeMap object                │
├───────────────────────────────┤
│ Object header (12 bytes)      │
│ comparator = null (8 bytes)   │
│ size = 4 (4 bytes)            │
│ modCount = 4 (4 bytes)        │
│ root ─────────────────────────┐
└───────────────────────────────┘
                                │
                                ▼
                         Entry "Charlie" (root, BLACK)
                         ┌────────────────────┐
                         │ key = "Charlie"    │
                         │ value = 35         │
                         │ color = BLACK      │
                         │ left ──────────────────┐
                         │ right ─────────────────────┐
                         │ parent = null      │
                         └────────────────────┘     │     │
                                                    │     ▼
                                              Entry "Alice" (RED)
                                              ┌────────────────────┐
                                              │ key = "Alice"      │
                                              │ value = 30         │
                                              │ color = RED        │
                                              │ left = null        │
                                              │ right = null       │
                                              │ parent ────────────────→ Charlie
                                              └────────────────────┘
                                                        │
                                                        ▼
                                              Entry "Diana" (RED)
                                              ┌────────────────────┐
                                              │ key = "Diana"      │
                                              │ value = 40         │
                                              │ color = RED        │
                                              │ left = null        │
                                              │ right = null       │
                                              │ parent ────────────────→ Charlie
                                              └────────────────────┘

Tree structure (sorted by key):
        Charlie (BLACK)
       /            \
  Alice (RED)    Diana (RED)
     /
  Bob (RED)
```

## 10. Syntax

```java
import java.util.TreeMap;
import java.util.SortedMap;
import java.util.NavigableMap;
import java.util.Comparator;

// ============================================
// CREATION
// ============================================
TreeMap<K, V> map = new TreeMap<>();                    // Natural ordering
TreeMap<K, V> map = new TreeMap<>(Comparator.reverseOrder()); // Reverse
TreeMap<K, V> map = new TreeMap<>(comparator);          // Custom comparator
TreeMap<K, V> map = new TreeMap<>(otherMap);             // Copy

// ============================================
// BASIC MAP OPERATIONS
// ============================================
map.put(key, value);                    // O(log n)
map.get(key);                           // O(log n)
map.remove(key);                        // O(log n)
map.containsKey(key);                   // O(log n)
map.containsValue(value);               // O(n)
map.size();                             // O(1)

// ============================================
// SORTED MAP OPERATIONS
// ============================================
K firstKey = map.firstKey();            // O(log n)
K lastKey = map.lastKey();              // O(log n)
Map.Entry<K,V> first = map.firstEntry(); // O(log n)
Map.Entry<K,V> last = map.lastEntry();   // O(log n)

SortedMap<K,V> head = map.headMap(key);        // Keys < key
SortedMap<K,V> tail = map.tailMap(key);        // Keys >= key
SortedMap<K,V> sub = map.subMap(from, to);     // Keys in [from, to)

// ============================================
// NAVIGABLE MAP OPERATIONS
// ============================================
K floor = map.floorKey(key);           // Greatest key <= key
K ceiling = map.ceilingKey(key);       // Smallest key >= key
K lower = map.lowerKey(key);           // Greatest key < key
K higher = map.higherKey(key);         // Smallest key > key

Map.Entry<K,V> floorEntry = map.floorEntry(key);
Map.Entry<K,V> ceilingEntry = map.ceilingEntry(key);
Map.Entry<K,V> lowerEntry = map.lowerEntry(key);
Map.Entry<K,V> higherEntry = map.higherEntry(key);

// Descending map
NavigableMap<K,V> descending = map.descendingMap();
K firstDescending = descending.firstKey();

// ============================================
// ITERATION
// ============================================
// Natural order (ascending)
for (Map.Entry<K, V> entry : map.entrySet()) {
    System.out.println(entry.getKey() + " = " + entry.getValue());
}

// Reverse order (descending)
for (Map.Entry<K, V> entry : map.descendingMap().entrySet()) {
    System.out.println(entry.getKey() + " = " + entry.getValue());
}

// SubMap iteration
for (Map.Entry<K, V> entry : map.subMap("A", "M").entrySet()) {

## 📑 Continue Reading

**Part 1** of 3 | [Part 2](README-part2.md) | [Part 3](README-part3.md)

