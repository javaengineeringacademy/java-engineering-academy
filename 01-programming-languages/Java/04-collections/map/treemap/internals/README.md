# TreeMap Internals

## Red-Black Tree Structure

```
TreeMap uses a Red-Black tree for sorted key-value storage.

static final class Entry<K,V> {
    K key;
    V value;
    Entry<K,V> left;
    Entry<K,V> right;
    Entry<K,V> parent;
    boolean color = BLACK;
}
```

## How TreeMap Works

```
TreeMap<Integer, String> map = new TreeMap<>();
map.put(5, "E");
map.put(3, "C");
map.put(7, "G");
map.put(1, "A");
map.put(4, "D");

Tree structure:
        ┌───┐
        │ 5 │ (root, BLACK)
        │ "E"│
        └─┬─┘
      ┌───┴───┐
      │       │
   ┌──┴──┐ ┌──┴──┐
   │  3   │ │  7   │ (RED)
   │ "C"  │ │ "G"  │
   └──┬──┘ └──┬──┘
     │         │
  ┌──┴──┐   ┌──┴──┐
  │  1   │ │  4   │ (BLACK)
  │ "A"  │ │ "D"  │
  └─────┘   └─────┘
```

## Entry Structure

```
Entry on heap:
┌──────────────────────────────┐
│  Object header     (12 bytes)│
│  K key ref          (8 bytes)│
│  V value ref        (8 bytes)│
│  Entry left ref     (8 bytes)│
│  Entry right ref    (8 bytes)│
│  Entry parent ref   (8 bytes)│
│  boolean color      (4 bytes)│
│  Padding             (4 bytes)│
├──────────────────────────────┤
│  TOTAL:              60 bytes│
└──────────────────────────────┘
```

## Red-Black Tree Properties

```
1. Every node is RED or BLACK
2. Root is always BLACK
3. All null children (NIL) are BLACK
4. Red node's children are both BLACK (no two reds in a row)
5. All paths from root to NIL have the same black count

These properties ensure the tree is approximately balanced.
Height ≤ 2 × log(n + 1)
```

## Core Operations

### put(key, value)

```
1. Find insertion point (binary search)
2. Compare keys using Comparator or Comparable
3. Insert new Entry at leaf position
4. Fix violations (rotate + recolor)
5. Set root to BLACK

Time: O(log n)
```

### get(key)

```
1. Start at root
2. Compare key with current node
3. If equal: return value
4. If less: go left
5. If greater: go right
6. If null: return null

Time: O(log n)
```

### remove(key)

```
1. Find node with key
2. If node has two children:
   - Find successor (leftmost in right subtree)
   - Swap data
   - Remove successor (has at most one child)
3. Remove node (fix violations)
4. Restore properties

Time: O(log n)
```

## Tree Rotations

```
Left rotation on X:
      X                Y
     / \              / \
    A   Y    →      X   C
       / \         / \
      B   C       A   B

Right rotation on Y:
      Y                X
     / \              / \
    X   C    →      A   Y
   / \                 / \
  A   B               B   C

Rotations preserve BST property and fix violations.
```

## Navigation Methods

```
firstEntry()  — leftmost node
lastEntry()   — rightmost node
lowerEntry(k) — rightmost node with key < k
floorEntry(k) — rightmost node with key ≤ k
ceilingEntry(k)— leftmost node with key ≥ k
higherEntry(k)— leftmost node with key > k
```

## Memory Layout Diagram

```
TreeMap instance:
┌──────────────────────────────────┐
│  Object header      (12 bytes)   │
│  Entry root ref      (8 bytes)   │
│  int size            (4 bytes)   │
│  Comparator comp ref (8 bytes)   │
│  int modCount        (4 bytes)   │
│  Padding             (4 bytes)   │
└──────────────┬───────────────────┘
               │
               ▼
     ┌─────────────────────────────────────────────┐
     │  Entry root ──► Tree structure              │
     │                                              │
     │         ┌──────────┐                         │
     │         │ Entry(5) │ (root)                 │
     │         │ "E"      │                         │
     │         └────┬─────┘                         │
     │        ┌─────┴─────┐                         │
     │        │           │                         │
     │   ┌────┴────┐ ┌────┴────┐                   │
     │   │ Entry(3)│ │ Entry(7)│                   │
     │   │ "C"     │ │ "G"     │                   │
     │   └────┬────┘ └────┬────┘                   │
     │        │           │                         │
     │   ┌────┴────┐ ┌────┴────┐                   │
     │   │ Entry(1)│ │ Entry(4)│                   │
     │   │ "A"     │ │ "D"     │                   │
     │   └─────────┘ └─────────┘                   │
     └─────────────────────────────────────────────┘
```

## Thread Safety

TreeMap is **not** synchronized. Concurrent access can cause:
- Corrupted tree structure
- Infinite loops during traversal
- ConcurrentModificationException

Use `Collections.synchronizedSortedMap()` or concurrent alternatives.

## Key Implementation Details

1. **Sorted by key** — Keys are always in sorted order.
   Iteration returns entries in ascending key order.

2. **Comparator support** — Custom ordering via Comparator:
   ```java
   TreeMap<String, Integer> map = new TreeMap<>(Comparator.reverseOrder());
   ```

3. **No hash overhead** — Unlike HashMap, no hash computation or
   bucket array. Direct tree traversal.

4. **Consistent O(log n)** — Performance guaranteed by tree balancing.
   No worst-case O(n) like HashMap with collisions.

5. **Sub-map views** — subMap(), headMap(), tailMap() return views
   backed by the same tree.

6. **No null keys** — TreeMap does NOT allow null keys.
   NullPointerException on put(null, value).

7. **Null values allowed** — Values can be null, unlike keys.
