# LinkedHashMap Internals

## HashMap + Doubly-Linked List

```
LinkedHashMap extends HashMap with a doubly-linked list
that maintains iteration order (insertion or access order).

static class Entry<K,V> extends HashMap.Node<K,V> {
    Entry<K,V> before, after;
}
```

## How LinkedHashMap Works

```
LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
map.put("C", 3);
map.put("A", 1);
map.put("B", 2);

Iteration order: C → A → B (insertion order)
```

## Entry Structure

```
LinkedHashMap.Entry extends HashMap.Node:

HashMap.Node:
┌──────────────────────────────┐
│  int hash                     │
│  K key                        │
│  V value                      │
│  Node next (hash chain)       │
└──────────────────────────────┘

LinkedHashMap.Entry adds:
┌──────────────────────────────┐
│  Entry before (linked list)   │
│  Entry after  (linked list)   │
└──────────────────────────────┘

Total per entry: 60 bytes
```

## Doubly-Linked List

```
HashMap bucket array:
┌────┬────┬────┬────┬────┬────┬────┬────┐
│  0 │  1 │  2 │  3 │  4 │  5 │  6 │  7 │
└──┬─┴────┴────┴──┬─┴────┴────┴────┴────┘
   │              │
   ▼              ▼
 Node("A")     Node("C")
   │              │
   └────linked────┘

Linked list (insertion order):
head ⇄ "C" ⇄ "A" ⇄ "B" ⇄ tail

head → before → "C" → after → "A" → after → "B" → after → tail
tail → after → "B" → before → "A" → before → "C" → before → head
```

## Insertion Order vs Access Order

```
Insertion order (default, accessOrder=false):
  put("C", 3)  →  C
  put("A", 1)  →  C, A
  put("B", 2)  →  C, A, B
  get("C")     →  C, A, B  (no change)

Access order (accessOrder=true, for LRU cache):
  put("C", 3)  →  C
  put("A", 1)  →  C, A
  put("B", 2)  →  C, A, B
  get("C")     →  A, B, C  (C moved to end)
```

## LRU Cache Pattern

```java
LinkedHashMap<Integer, String> cache = new LinkedHashMap<>(16, 0.75f, true) {
    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > MAX_ENTRIES;
    }
};
```

```
LRU behavior:
  put(1, "A")  → [1]
  put(2, "B")  → [1, 2]
  put(3, "C")  → [1, 2, 3]
  get(1)       → [2, 3, 1]    (1 moved to end)
  put(4, "D")  → [3, 1, 4]    (2 evicted, oldest removed)
```

## Core Operations

### put(key, value)

```
1. HashMap.put(key, value)  → insert/replace
2. afterNodeInsertion(evict)  → LinkedHashMap hook
3. If accessOrder: move node to tail
4. If insertOrder: link node at tail
5. afterNodeAccess(e)  → maintain linked list

Time: O(1) amortized
```

### get(key)

```
1. HashMap.get(key)  → O(1) lookup
2. afterNodeAccess(e)  → move to tail (if accessOrder)
3. Return value

Time: O(1)
```

### remove(key)

```
1. HashMap.remove(key)  → remove node
2. afterNodeRemoval(e)  → unlink from doubly-linked list
3. Return value

Time: O(1)
```

### Iterator

```
Iteration follows linked list order:
1. Start at head
2. Follow after pointers
3. Returns entries in linked list order

head → C → A → B → tail
        ↓   ↓   ↓
      (C,3) (A,1) (B,2)
```

## Memory Layout Diagram

```
LinkedHashMap instance:
┌──────────────────────────────────────┐
│  Object header       (12 bytes)      │
│  Node[] table ref     (8 bytes)      │
│  int size              (4 bytes)     │
│  int threshold         (4 bytes)     │
│  float loadFactor      (4 bytes)     │
│  int modCount          (4 bytes)     │
│  Set<Entry> entrySet   (8 bytes)     │
│  Entry head ref        (8 bytes)     │  ← linked list
│  Entry tail ref        (8 bytes)     │  ← linked list
│  boolean accessOrder   (4 bytes)     │
│  Padding               (4 bytes)     │
└──────────────┬───────────────────────┘
               │
               ▼
     ┌──────────────────────────────────────────────┐
     │  HashMap bucket array + linked list           │
     │                                               │
     │  head ⇄ Node("A") ⇄ Node("C") ⇄ Node("B") ⇄ tail
     │   │         │              │              │    │
     │   └─────────┴──────────────┴──────────────┘    │
     │              Doubly-linked list                 │
     └──────────────────────────────────────────────┘
```

## Thread Safety

LinkedHashMap is **not** synchronized. Same issues as HashMap plus:
- Corrupted linked list pointers
- Broken iteration order
- Consistency issues between HashMap and linked list

## Key Implementation Details

1. **Two orders** — Insertion order (default) or access order (for LRU).

2. **removeEldestEntry** — Override to implement eviction policy.
   Called after every put().

3. **Same performance as HashMap** — O(1) for get/put, with small
   constant overhead for linked list maintenance.

4. **Iteration is O(n)** — Traverses linked list, which is exactly
   n entries. No wasted traversal.

5. **No null keys/values** — Inherits HashMap's null key support.
   One null key allowed.

6. **Consistent view** — entrySet(), keySet(), values() all reflect
   linked list order, not bucket order.
