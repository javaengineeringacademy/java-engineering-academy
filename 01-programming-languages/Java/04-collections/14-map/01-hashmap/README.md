# HashMap

## 1. Introduction

HashMap is the most widely used implementation of the `Map` interface in Java. It provides O(1) average-time performance for basic operations (get, put, remove) using a hash table data structure. HashMap stores key-value pairs and allows null keys and values.

HashMap works by computing a hash code from the key, using it to determine which "bucket" (index in an internal array) to store the value in. When retrieving, it computes the hash of the key again to find the correct bucket, then searches within that bucket for the exact key using `equals()`.

Understanding HashMap internals is essential for Java developers because:
1. It's the default choice for most Map use cases
2. Improper use of hashCode()/equals() causes subtle bugs
3. Performance depends on hash distribution and load factor
4. Java 8+ introduced treeification (red-black trees for collisions)

## 2. Learning Objectives

- Create and use HashMap with generics
- Understand hashing, buckets, and collision resolution
- Learn about hashCode() and equals() contracts
- Understand the load factor and resizing mechanism
- Master Java 8+ treeification (linked list → red-black tree)
- Compare HashMap vs TreeMap vs LinkedHashMap
- Learn about HashMap null key handling
- Understand thread-safety issues with HashMap

## 3. Prerequisites

- Module 01: Java Fundamentals
- Module 02: Object-Oriented Programming
- Module 03: Generics basics
- Understanding of hashCode() and equals() methods
- Basic knowledge of data structures (arrays, linked lists)

## 4. Why This Concept Exists

Before HashMap, developers used Hashtable (synchronized, slow) or manual array-based lookups. HashMap provides:
1. **O(1) average performance**: Fast lookups regardless of data size
2. **No synchronization overhead**: Faster than Hashtable in single-threaded code
3. **Null support**: Allows one null key and multiple null values
4. **Dynamic resizing**: Automatically grows as data is added
5. **Flexible key types**: Any object can be a key (with proper hashCode/equals)

HashMap is essential for:
- Caching and memoization
- Database indexing
- Configuration storage
- Counting and frequency analysis
- Object relationship mapping

## 4b. Why HashMap Uses Buckets

HashMap's bucket-based design is the foundation of its O(1) average-case performance. Understanding why requires examining each design decision.

**The hash function distributes keys uniformly.** When you call `map.put(key, value)`, HashMap computes `key.hashCode()`, applies a spreading function (`h ^ (h >>> 16)`), and uses the result to determine a bucket index. A good hash function maps different keys to different buckets, minimizing collisions. With 16 buckets and a uniform hash, most keys land in unique buckets — giving O(1) lookup.

**Buckets reduce collision handling overhead.** Without buckets, all keys would need to be compared linearly (O(n)) or stored in a balanced tree (O(log n)). Buckets partition the key space: instead of searching all entries, you search only the entries that hashed to the same index. With a well-distributed hash and a reasonable load factor, most buckets contain 0-1 entries, making lookup effectively constant time.

**Why the bucket count is always a power of 2.** HashMap uses `hash & (capacity - 1)` to compute the bucket index. When capacity is a power of 2, the subtraction creates a bitmask that selects the lower bits efficiently — a single AND instruction versus an expensive modulo operation. This also ensures every bit of the hash contributes to the index, improving distribution.

**Why the load factor is 0.75.** The load factor controls when HashMap resizes (at `capacity * loadFactor` entries). At 0.75:
- **Too low (0.5)**: HashMap wastes 50% of allocated memory; frequent resizing creates garbage
- **Too high (1.0)**: Buckets fill up, collision chains grow, lookup degrades toward O(n)
- **0.75**: Balances memory usage against collision probability. At this threshold, the probability of any bucket containing more than 2-3 entries is very low for well-distributed hashes

The 0.75 factor is also mathematically optimal: assuming a uniform hash function, the expected number of entries per bucket follows a Poisson distribution. At load factor 0.75, the probability of more than 2 collisions is ~0.04 — meaning nearly all buckets have 0-1 entries.

**Summary:**

| Decision | Rationale |
|----------|-----------|
| Buckets | Partition key space for O(1) lookup |
| Power-of-2 capacity | Bitwise AND instead of modulo |
| Load factor 0.75 | Optimal balance of memory vs collisions |
| Treeification (8+) | Worst-case O(log n) when hash is poor |

## 5. Problem Statement

Consider building a phone book application:
- Add contacts with name (key) and phone number (value)
- Look up phone number by name (fast)
- Check if a name exists (fast)
- Remove contacts
- The phone book may have 10 contacts or 100,000 contacts

Without HashMap, you'd need:
- A list of pairs and linear search O(n)
- Or a sorted array and binary search O(log n)
- Both require manual resizing and management

With HashMap, all operations are O(1) average case, and the data structure handles resizing automatically.

## 6. Theory

### Hash Table Structure

HashMap uses an array of `Node` buckets:

```java
transient Node<K,V>[] table;  // Array of buckets
transient int size;           // Number of key-value mappings
int threshold;                // size at which to resize
final float loadFactor;       // resize threshold ratio
```

### Node Structure

```java
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;    // Precomputed hash
    final K key;       // Key (immutable)
    V value;           // Value
    Node<K,V> next;    // Linked list for collisions
}
```

### Hash Computation

```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

The hash is spread across all bits to reduce collisions. The `h >>> 16` operation mixes the upper bits into the lower bits.

### Bucket Index Calculation

```java
int index = hash & (table.length - 1);  // Equivalent to hash % length when length is power of 2
```

### Collision Resolution

When two keys hash to the same bucket:
1. **Java 7**: Linked list (chain hashing)
2. **Java 8+**: Linked list → Red-black tree when bucket has 8+ entries

### Load Factor and Resizing

- Default load factor: 0.75
- Default initial capacity: 16
- When `size > capacity * loadFactor`, the table is resized
- New capacity = old capacity * 2
- All entries are rehashed to new positions

### Capacity as Power of 2

HashMap capacity is always a power of 2 (16, 32, 64, 128, ...). This allows efficient index calculation using bitwise AND instead of modulo.

## 7. Internal Working

### The put() Operation

```java
public V put(K key, Value value) {
    return putVal(hash(key), key, value, false, true);
}

final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;  // Initialize on first put
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);  // Empty bucket
    else {
        Node<K,V> e; K k;
        if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;  // Key exists, replace value
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);  // Tree bucket
        else {
            for (int binCount = 0; ; ++binCount) {
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key, value, null);
                    if (binCount >= TREEIFY_THRESHOLD - 1)  // 7
                        treeifyBin(tab, hash);  // Convert to tree
                    break;
                }
                if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
                    break;  // Key found
                p = e;
            }
        }
        if (e != null) {  // Key already exists
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;  // Replace value
            afterNodeAccess(e);
            return oldValue;
        }
    }
    if (++size > threshold)
        resize();  // Resize if needed
    afterNodeInsertion(evict);
    return null;
}
```

### The get() Operation

```java
public V get(Object key) {
    Node<K,V> e;
    return (e = getNode(hash(key), key)) == null ? null : e.value;
}

final Node<K,V> getNode(int hash, Object key) {
    Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (first = tab[(n - 1) & hash]) != null) {
        if (first.hash == hash && ((k = first.key) == key || (key != null && key.equals(k))))
            return first;  // First node is the key
        if ((e = first.next) != null) {
            if (first instanceof TreeNode)
                return ((TreeNode<K,V>)first).getTreeNode(hash, key);  // Tree lookup
            do {
                if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
                    return e;  // Found in linked list
            } while ((e = e.next) != null);
        }
    }
    return null;
}
```

### The resize() Operation

```java
final Node<K,V>[] resize() {
    Node<K,V>[] oldTab = table;
    int oldCap = (oldTab == null) ? 0 : oldTab.length;
    int oldThr = threshold;
    int newCap, newThr = 0;
    if (oldCap > 0) {
        if (oldCap >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return oldTab;
        }
        else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                 oldCap >= DEFAULT_INITIAL_CAPACITY)
            newThr = oldThr << 1;  // Double threshold
    }
    else if (oldThr > 0)
        newCap = oldThr;
    else {
        newCap = DEFAULT_INITIAL_CAPACITY;  // 16
        newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);  // 12
    }
    if (newThr == 0) {
        float ft = (float)newCap * loadFactor;
        newThr = (newCap < MAXIMUM_CAPACITY && (float)ft < (float)MAXIMUM_CAPACITY ?
                  (int)ft : Integer.MAX_VALUE);
    }
    threshold = newThr;
    @SuppressWarnings({"rawtypes","unchecked"})
    Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
    table = newTab;
    if (oldTab != null) {
        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> e;
            if ((e = oldTab[j]) != null) {
                oldTab[j] = null;
                if (e.next == null)
                    newTab[e.hash & (newCap - 1)] = e;  // Single node
                else if (e instanceof TreeNode)
                    ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);  // Split tree
                else {
                    Node<K,V> loHead = null, loTail = null;
                    Node<K,V> hiHead = null, hiTail = null;
                    Node<K,V> next;
                    do {
                        next = e.next;
                        if ((e.hash & oldCap) == 0) {
                            if (loTail == null)
                                loHead = e;
                            else
                                loTail.next = e;
                            loTail = e;
                        }
                        else {
                            if (hiTail == null)
                                hiHead = e;
                            else
                                hiTail.next = e;
                            hiTail = e;
                        }
                    } while ((e = next) != null);
                    if (loTail != null) {
                        loTail.next = null;
                        newTab[j] = loHead;
                    }
                    if (hiTail != null) {
                        hiTail.next = null;
                        newTab[j + oldCap] = hiHead;
                    }
                }
            }
        }
    }
    return newTab;
}
```

## 8. JVM Perspective

### Memory Allocation

```java
Map<String, Integer> map = new HashMap<>();
// JVM allocates:
// - HashMap object header: 12 bytes
// - table reference: 8 bytes
// - size field: 4 bytes
// - threshold field: 4 bytes
// - loadFactor field: 4 bytes
// Total HashMap object: ~40 bytes

// First put() triggers resize():
// - Creates Node[] of size 16
// - Node[] array: 16 × 8 = 128 bytes (references)
// - Each Node: ~40 bytes (hash, key, value, next)
```

### JIT Optimization

The JIT compiler optimizes HashMap operations:
- Inline `hash()` and `getNode()` methods
- Optimize the bit manipulation for index calculation
- Eliminate null checks for non-null keys
- Devirtualize calls when concrete type is known

### Hash Distribution Quality

Good hash codes distribute keys uniformly across the table. Poor hash codes (e.g., always returning 0) cause all keys to collide in bucket 0, degrading performance to O(n).

## 9. Memory Representation

```
HashMap<String, Integer> map = new HashMap<>();
map.put("Alice", 30);
map.put("Bob", 25);
map.put("Charlie", 35);

Memory layout:
┌───────────────────────────────┐
│ HashMap object                │
├───────────────────────────────┤
│ Object header (12 bytes)      │
│ table ──────────────────────────┐
│ size = 3 (4 bytes)            │     │
│ threshold = 12 (4 bytes)      │     │
│ loadFactor = 0.75f (4 bytes)  │     │
│ (padding 4 bytes)             │     │
└───────────────────────────────┘     │
                                      ▼
                               Node[] table (capacity 16)
                               ┌────────────────────────┐
                               │ [0] → null             │
                               │ [1] → null             │
                               │ [2] → null             │
                               │ ...                    │
                               │ [5] → Node("Alice",30) │ ← hash("Alice") & 15 = 5
                               │ [6] → Node("Bob",25)   │ ← hash("Bob") & 15 = 6
                               │ [7] → null             │
                               │ [8] → null             │
                               │ [9] → null             │
                               │ [10] → null            │
                               │ [11] → null            │
                               │ [12] → null            │
                               │ [13] → null            │
                               │ [14] → null            │
                               │ [15] → null            │
                               └────────────────────────┘

Node("Alice", 30):
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ hash (int, 4 bytes)         │
│ key → String "Alice"        │
│ value → Integer 30          │
│ next → null                 │
└─────────────────────────────┘
```

### Collision Handling

When two keys hash to the same bucket:

```
Bucket[5] → Node("Alice", 30) → Node("David", 40) → null
                   ↑                    ↑
              hash=5, next          hash=5, next=null
```

## 10. Syntax

```java
import java.util.HashMap;
import java.util.Map;

// ============================================
// CREATION
// ============================================
Map<K, V> map = new HashMap<>();
Map<K, V> map = new HashMap<>(16);           // Initial capacity
Map<K, V> map = new HashMap<>(16, 0.75f);   // Capacity and load factor
Map<K, V> map = new HashMap<>(otherMap);    // Copy constructor
Map<K, V> immutable = Map.of("k1", "v1");   // Java 9+

// ============================================
// ADDING/UPDATING
// ============================================
map.put(key, value);                    // Add or replace
map.putIfAbsent(key, value);            // Add only if absent
map.putAll(otherMap);                   // Add all from other map
map.replace(key, value);                // Replace if present
map.replace(key, oldValue, newValue);   // Conditional replace

// ============================================
// RETRIEVING
// ============================================
V value = map.get(key);                 // Returns null if absent
V value = map.getOrDefault(key, default); // Returns default if absent
V value = map.computeIfAbsent(key, k -> createValue(k));


## 📑 Continue Reading

**Part 1** of 3 | [Part 2](README-part2.md) | [Part 3](README-part3.md)

## Related Topics
- [Java Memory Model](../../00-knowledge-atoms/java-memory-model/) — Where HashMap objects live
- [equals() and hashCode](../../00-knowledge-atoms/equals-hashcode/) — Contract for HashMap keys
- [Concurrency](../../09-multithreading/) — ConcurrentHashMap for thread safety
- [Generics](../../05-generics/) — Type-safe collections

