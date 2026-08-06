# HashMap Source Code Walkthrough

Understanding HashMap's implementation is crucial for Java performance optimization. This guide walks through the key components and algorithms.

## Internal Structure

### Node Array (Hash Table)

```java
transient Node<K,V>[] table;
```

The core data structure is an array of `Node` objects. Each node represents a key-value pair.

### Node Class

```java
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;
    V value;
    Node<K,V> next; // For collision handling
    
    Node(int hash, K key, V value, Node<K,V> next) {
        this.hash = hash;
        this.key = key;
        this.value = value;
        this.next = next;
    }
}
```

### TreeNode (Treeified Buckets)

```java
static final class TreeNode<K,V> extends LinkedHashMap.Entry<K,V> {
    TreeNode<K,V> parent;
    TreeNode<K,V> left;
    TreeNode<K,V> right;
    TreeNode<K,V> prev;
    boolean red;
}
```

When a bucket grows too long (≥ TREEIFY_THRESHOLD), it converts to a red-black tree for O(log n) lookup.

## Key Constants

```java
static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // 16
static final int MAXIMUM_CAPACITY = 1 << 30;
static final float DEFAULT_LOAD_FACTOR = 0.75f;
static final int TREEIFY_THRESHOLD = 8;
static final int UNTREEIFY_THRESHOLD = 6;
static final int MIN_TREEIFY_CAPACITY = 64;
```

## put() Implementation

### Entry Point

```java
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);
}
```

### Hash Function

```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

**Design Decision**: XOR with upper bits spreads hash distribution, reducing collisions.

### putVal() Method

```java
final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    
    // Step 1: Initialize table if empty
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    
    // Step 2: Find bucket (empty slot)
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    else {
        // Step 3: Handle collision
        Node<K,V> e; K k;
        
        // Case 1: First node matches key
        if (p.hash == hash && 
            ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        
        // Case 2: Bucket is treeified
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        
        // Case 3: Traverse linked list
        else {
            for (int binCount = 0; ; ++binCount) {
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key, value, null);
                    // Treeify if list too long
                    if (binCount >= TREEIFY_THRESHOLD - 1)
                        treeifyBin(tab, hash);
                    break;
                }
                if (e.hash == hash && 
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    break; // Found existing key
                p = e;
            }
        }
        
        // Step 4: Update existing key
        if (e != null) {
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
    }
    
    // Step 5: Check if resize needed
    if (++size > threshold)
        resize();
    
    afterNodeInsertion(evict);
    return null;
}
```

## get() Implementation

### Entry Point

```java
public V get(Object key) {
    Node<K,V> e;
    return (e = getNode(hash(key), key)) == null ? null : e.value;
}
```

### getNode() Method

```java
final Node<K,V> getNode(int hash, Object key) {
    Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
    
    // Check if table exists and has entries
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (first = tab[(n - 1) & hash]) != null) {
        
        // Check first node
        if (first.hash == hash && 
            ((k = first.key) == key || (key != null && key.equals(k))))
            return first;
        
        // Check if bucket is treeified
        if ((e = first.next) != null) {
            if (first instanceof TreeNode)
                return ((TreeNode<K,V>)first).getTreeNode(hash, key);
            
            // Traverse linked list
            do {
                if (e.hash == hash && 
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    return e;
            } while ((e = e.next) != null);
        }
    }
    return null;
}
```

**Design Decision**: Check hash first (fast int comparison) before equals().

## resize() Implementation

### When Resize Happens

```java
// Resize when:
if (++size > threshold)  // size > capacity * loadFactor
    resize();
```

### resize() Method

```java
final Node<K,V>[] resize() {
    Node<K,V>[] oldTab = table;
    int oldCap = (oldTab == null) ? 0 : oldTab.length;
    int oldThr = threshold;
    int newCap, newThr = 0;
    
    // Calculate new capacity
    if (oldCap > 0) {
        if (oldCap >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return oldTab;
        }
        else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                 oldCap >= DEFAULT_INITIAL_CAPACITY)
            newThr = oldThr << 1; // Double threshold
    }
    else if (oldThr > 0)
        newCap = oldThr;
    else {
        newCap = DEFAULT_INITIAL_CAPACITY;
        newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }
    
    // Create new table
    Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
    table = newTab;
    threshold = newThr;
    
    // Rehash all entries
    if (oldTab != null) {
        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> e;
            if ((e = oldTab[j]) != null) {
                oldTab[j] = null;
                
                // Case 1: Single node (no collision)
                if (e.next == null)
                    newTab[e.hash & (newCap - 1)] = e;
                
                // Case 2: Treeified bucket
                else if (e instanceof TreeNode)
                    ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                
                // Case 3: Linked list
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
                    
                    // Place lo list (stays at same index)
                    if (loTail != null) {
                        loTail.next = null;
                        newTab[j] = loHead;
                    }
                    // Place hi list (moves to j + oldCap)
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

**Design Decision**: Split list into lo/hi halves based on bit check. This is efficient because elements either stay or move to exactly `j + oldCap`.

## treeify() Implementation

### When Treeification Happens

```java
// In putVal():
if (binCount >= TREEIFY_THRESHOLD - 1) // 7 (0-indexed)
    treeifyBin(tab, hash);
```

### treeifyBin() Method

```java
final void treeifyBin(Node<K,V>[] tab, int hash) {
    int n, index; Node<K,V> e;
    
    // Don't treeify if table too small
    if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)
        resize();
    
    // Convert linked list to tree
    else if ((e = tab[index = (n - 1) & hash]) != null) {
        TreeNode<K,V> hd = null, tl = null;
        do {
            TreeNode<K,V> p = replacementTreeNode(e, null);
            if (tl == null)
                hd = p;
            else {
                p.prev = tl;
                tl.next = p;
            }
            tl = p;
        } while ((e = e.next) != null);
        
        // Insert tree into table
        if ((tab[index] = hd) != null)
            hd.treeify(tab);
    }
}
```

### treeify() Method

```java
final void treeify(Node<K,V>[] tab) {
    TreeNode<K,V> root = null;
    for (TreeNode<K,V> x = this; x != null; x = x.next) {
        TreeNode<K,V> xp = null;
        
        // Build red-black tree
        if (root == null) {
            x.parent = null;
            x.red = false;
            root = x;
        }
        else {
            // Find insertion point
            K k = x.key;
            int h = x.hash;
            Class<?> kc = null;
            for (TreeNode<K,V> p = root; ; ) {
                int dir, ph;
                K pk = p.key;
                if ((ph = p.hash) > h)
                    dir = -1;
                else if (ph < h)
                    dir = 1;
                else if ((kc == null && 
                         (kc = comparableClassFor(k)) == null) ||
                         (dir = compareComparables(kc, k, pk)) == 0)
                    dir = tieBreakOrder(k, pk);
                
                TreeNode<K,V> xp = p;
                if ((p = (dir <= 0) ? p.left : p.right) == null) {
                    x.parent = xp;
                    if (dir <= 0)
                        xp.left = x;
                    else
                        xp.right = x;
                    root = balanceInsertion(root, x);
                    break;
                }
            }
        }
    }
    moveRootToFront(tab, root);
}
```

## Key Design Decisions

### 1. Load Factor (0.75)

- **Tradeoff**: Memory usage vs collision rate
- **Mathematical basis**: For uniform hash, 0.75 gives optimal performance
- **Empirically tested**: Works well for most use cases

### 2. Treeification Threshold (8)

- **Why 8?**: Poisson distribution of bucket lengths
- **Probability of 8 collisions**: ~0.00000006
- **Tradeoff**: Memory overhead vs lookup performance

### 3. Untreeify Threshold (6)

- **Why 6?**: Hysteresis to prevent thrashing
- **Avoids**: Constant treeify/un-treeify

### 4. Minimum Treeify Capacity (64)

- **Why 64?**: Prevents treeification in small maps
- **Alternative**: Resize instead of treeify

### 5. Hash Spreading

```java
// XOR with upper bits
h ^ (h >>> 16);

// Why? Reduces collision rate
// Example: 0x12345678 ^ 0x00001234
```

### 6. Power of Two Capacity

```java
// Why? Efficient modulo operation
index = hash & (capacity - 1); // Same as hash % capacity

// But faster (bitwise AND)
```

### 7. No Synchronization

- **Thread-unsafety by design**
- **ConcurrentHashMap** for thread safety
- **Better performance** for single-threaded use

## Performance Characteristics

| Operation | Average | Worst Case |
|-----------|---------|------------|
| put() | O(1) | O(log n) |
| get() | O(1) | O(log n) |
| remove() | O(1) | O(log n) |
| containsKey() | O(1) | O(log n) |

**Worst case** occurs when all keys hash to same bucket (treeified).

## Common Mistakes

### 1. Bad Hash Function

```java
// Bad: Poor distribution
class BadKey {
    public int hashCode() {
        return 1; // All keys in same bucket
    }
}

// Good: Spread distribution
class GoodKey {
    public int hashCode() {
        return Objects.hash(field1, field2, field3);
    }
}
```

### 2. Mutable Keys

```java
// Bad: Key changes after put
Map<Point, String> map = new HashMap<>();
Point p = new Point(1, 2);
map.put(p, "value");
p.x = 3; // Key changed!
map.get(p); // null (different hash)

// Good: Use immutable keys
Map<Location, String> map = new HashMap<>();
Location loc = new Location(1, 2); // Immutable
map.put(loc, "value");
```

### 3. Ignoring Load Factor

```java
// Default load factor (0.75) is usually fine
// Only adjust if you have specific memory/performance needs

// Lower load factor: Less collisions, more memory
Map<String, String> map = new HashMap<>(16, 0.5f);

// Higher load factor: More collisions, less memory
Map<String, String> map = new HashMap<>(16, 0.9f);
```

## Interview Questions

[5-10 interview questions with answers]

1. **What is this concept?**
   [Answer]

2. **When would you use it?**
   [Answer]

3. **What are the alternatives?**
   [Answer]

4. **What are common mistakes?**
   [Answer]

5. **How does it perform compared to alternatives?**
   [Answer]

## Examples

[Code examples demonstrating the concept]

## Internal Working

[How this works under the hood]

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Overview

[Brief description of the topic]

## Resources

- **Java HashMap Official Docs**
- **OpenJDK Source**: `src/java.base/java/util/HashMap.java`
- **"Effective Java"** by Joshua Bloch
- **"Java Concurrency in Practice"** for thread safety

## Performance

[Performance considerations and benchmarks]

## Pitfalls

[Common mistakes and anti-patterns]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
