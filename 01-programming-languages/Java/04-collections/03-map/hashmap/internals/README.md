# HashMap Internals

## Bucket Array Structure

```java
transient Node<K,V>[] table;
transient int size;

static class Node<K,V> {
    final int hash;
    final K key;
    V value;
    Node<K,V> next;
}
```

## How HashMap Works

```
HashMap stores key-value pairs in a bucket array.
Index: index = hash(key) & (n - 1)  where n = table.length

Example with 16 buckets:
  hash("A") & 15 = 3
  hash("B") & 15 = 7
  hash("C") & 15 = 3  ← collision with "A"
```

## Bucket Array Layout

```
table (capacity = 16):
┌────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┐
│  0 │  1 │  2 │  3 │  4 │  5 │  6 │  7 │  8 │  9 │ 10 │ 11 │ 12 │ 13 │ 14 │ 15 │
└────┴────┴────┴──┬─┴────┴────┴────┴──┬─┴────┴────┴────┴────┴────┴────┴────┴────┘
                  │                   │
                  ▼                   ▼
            ┌──────────┐        ┌──────────┐
            │"A"=100   │        │"B"=200   │
            │ next ────────►    │ next=null│
            └──────────┘        └──────────┘
                 │
                 ▼
            ┌──────────┐
            │"C"=300   │
            │ next=null│
            └──────────┘

Collision: "A" and "C" hash to same bucket
Resolution: Linked list (chaining)
```

## Hash Function

```
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}

1. Compute hashCode()
2. XOR high bits with low bits (spreads bits)
3. Index = hash & (table.length - 1)

Example:
  key = "A", hashCode = 65
  hash = 65 ^ (65 >>> 16) = 65
  index = 65 & 15 = 1
```

## Collision Resolution: Linked List → Tree

```
When bucket has 8+ entries:
  Linked list → Red-Black tree (treeifyBin)

Before (linked list):
  bucket[3] → "A" → "C" → "D" → "E" → "F" → "G" → "H" → "I"
  Search: O(n) linear scan

After (tree, TREEIFY_THRESHOLD = 8):
  bucket[3] → TreeNode("E")
                /        \
         TreeNode("C")  TreeNode("G")
          /      \        /      \
       "A"      "D"    "F"      "I"
  Search: O(log n) binary search
```

## Growth Algorithm

```
DEFAULT_INITIAL_CAPACITY = 16
DEFAULT_LOAD_FACTOR = 0.75f
MAXIMUM_CAPACITY = 1 << 30

Threshold = capacity × loadFactor
When size > threshold → resize()

resize():
  newCapacity = oldCapacity × 2
  newTable = new Node[newCapacity]
  Rehash all entries
```

### Growth Example

```
Initial: capacity=16, loadFactor=0.75
Threshold = 16 × 0.75 = 12

After 12 inserts:
  size = 12, threshold = 12
  Next insert triggers resize

After resize:
  capacity = 32
  threshold = 32 × 0.75 = 24
```

## Core Operations

### put(key, value)

```
1. if table empty: resize() to create table
2. Compute index: i = hash(key) & (n-1)
3. if table[i] == null:
     table[i] = new Node(hash, key, value, null)
4. else:
     traverse chain:
     if key exists: replace value
     else: append Node to chain
5. if ++size > threshold: resize()

Time: O(1) amortized
```

### get(key)

```
1. Compute index: i = hash(key) & (n-1)
2. Traverse chain at table[i]:
   if key.equals(node.key): return node.value
3. if not found: return null

Time: O(1) average, O(n) worst case (all collisions)
```

### remove(key)

```
1. Compute index: i = hash(key) & (n-1)
2. Find node in chain
3. Unlink node (update prev.next)
4. size--

Time: O(1) average
```

## Resize Process

```
BEFORE (capacity=4, entries at 0,1,2):
table: [ A ] [ B ] [ C ] [ ]
         ↓
        Node("A",1)

AFTER resize (capacity=8):
table: [ A ] [   ] [   ] [   ] [   ] [ B ] [   ] [ C ]
         ↓                          ↓              ↓
        Node("A",1)            Node("B",2)  Node("C",3)

Entries rehash to new positions based on new capacity.
```

## Memory Layout Diagram

```
HashMap instance:
┌──────────────────────────────────┐
│  Object header      (12 bytes)   │
│  Node[] table ref   (8 bytes)    │
│  int size            (4 bytes)   │
│  int threshold       (4 bytes)   │
│  float loadFactor    (4 bytes)   │
│  int modCount         (4 bytes)  │
│  Set<Entry> entrySet (8 bytes)   │
│  Padding              (4 bytes)  │
└──────────────┬───────────────────┘
               │
               ▼
     ┌─────────────────────────────────────────┐
     │  Node[] table                            │
     │  ┌────┬────┬────┬────┬────┬────┬────┐   │
     │  │  0 │  1 │  2 │  3 │  4 │  5 │... │   │
     │  └──┬─┴────┴────┴──┬─┴────┴────┴────┘   │
     │     │              │                     │
     │     ▼              ▼                     │
     │  ┌──────┐      ┌──────┐                 │
     │  │ Node │      │ Node │                 │
     │  │ A=1  │      │ B=2  │                 │
     │  │ next─┼──►   │ next │                 │
     │  └──────┘      └──────┘                 │
     └─────────────────────────────────────────┘
```

## Thread Safety

HashMap is **not** synchronized. Concurrent access can cause:
- Infinite loops during resize (Java 7)
- Lost updates
- Corrupted linked list/tree structure
- ConcurrentModificationException

Use `ConcurrentHashMap` or `Collections.synchronizedMap()`.

## Key Implementation Details

1. **Null key** — HashMap allows one null key, stored at index 0.

2. **Hash spread** — XOR of high and low bits prevents clustering
   when low bits are similar.

3. **Treeification** — When chain length ≥ 8 and table capacity ≥ 64,
   linked list converts to tree for O(log n) lookup.

4. **Untreeification** — When tree size ≤ 6, converts back to
   linked list (UNTREEIFY_THRESHOLD = 6).

5. **Capacity is always power of 2** — Enables bitwise AND for index:
   index = hash & (capacity - 1)

6. **Load factor 0.75** — Balance between time (fewer collisions) and
   space (not too empty). Higher = more memory, lower = more resizing.
