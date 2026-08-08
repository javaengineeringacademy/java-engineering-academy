# LinkedHashSet Internals

## Backed by LinkedHashMap

```
LinkedHashSet<E> is backed by LinkedHashMap<E, Object>

private transient LinkedHashMap<E, Object> map;
private static final Object PRESENT = new Object();
```

## How LinkedHashSet Works

```
LinkedHashSet<String> set = new LinkedHashSet<>();
set.add("C");
set.add("A");
set.add("B");

Insertion order preserved: C → A → B
```

## LinkedHashMap Structure

```
LinkedHashMap extends HashMap with a doubly-linked list:

HashMap bucket array:
┌────┬────┬────┬────┬────┬────┬────┬────┐
│  0 │  1 │  2 │  3 │  4 │  5 │  6 │  7 │
└──┬─┴────┴────┴────┴────┴────┴────┴────┘
   │
   ▼
  Node("C") → Node("A") → Node("B")
     ↑            ↑            ↑
     └────────────┴────────────┘
        Doubly-linked list maintains order

head → Node("C") → Node("A") → Node("B") → tail
```

## Operation Flow

### add(element)

```
1. map.put(element, PRESENT)
2. LinkedHashMap hooks:
   - afterNodeInsertion(true) — maintains access order if configured
   - Links new node at tail of doubly-linked list
3. Return true if new

Time: O(1) amortized
```

### contains(element)

```
1. map.containsKey(element)
2. Direct hash lookup

Time: O(1)
```

### remove(element)

```
1. map.remove(element)
2. LinkedHashMap hooks:
   - afterNodeRemoval — removes from doubly-linked list
3. Return true if found

Time: O(1)
```

### Iterator

```
Iteration follows linked list order (insertion order):
1. Start at head
2. Follow next pointers
3. Returns elements in insertion order

head → C → A → B → null
        ↓   ↓   ↓
       "C" "A" "B"
```

## Insertion Order vs Access Order

```
LinkedHashMap modes:
  accessOrder = false (default): insertion order
  accessOrder = true:  access order (for LRU cache)

LinkedHashSet always uses accessOrder = false (insertion order)
```

## Memory Layout Diagram

```
LinkedHashSet instance:
┌──────────────────────────────────────┐
│  Object header       (12 bytes)      │
│  LinkedHashMap map ref (8 bytes)     │
└──────────────┬───────────────────────┘
               │
               ▼
     ┌──────────────────────────────────────────────┐
     │  LinkedHashMap                               │
     │  ┌──────────────────────────────────────┐    │
     │  │ HashMap bucket array                  │    │
     │  │ ┌────┬────┬────┬────┬────┬────┬────┐  │    │
     │  │ │  0 │  1 │  2 │  3 │  4 │  5 │  6 │  │    │
     │  │ └──┬─┴────┴──┬─┴────┴────┴────┴────┘  │    │
     │  │    │         │                         │    │
     │  │    ▼         ▼                         │    │
     │  │  Node("A")  Node("B")                 │    │
     │  │    ↑            ↑                      │    │
     │  │    └──linked────┘                      │    │
     │  └──────────────────────────────────────┘    │
     │                                              │
     │  Doubly-linked list for ordering:             │
     │  head → "A" ⇄ "B" → tail                    │
     └──────────────────────────────────────────────┘
```

## Thread Safety

LinkedHashSet is **not** synchronized. Same issues as HashSet:
- ConcurrentModificationException during iteration
- Lost updates
- Corrupted linked list pointers

Use `Collections.synchronizedSet()` or concurrent alternatives.

## Key Implementation Details

1. **Insertion order** — Elements iterate in the order they were inserted.
   First added = first iterated.

2. **O(1) for all operations** — Same performance as HashSet, plus
   ordering overhead (linked list maintenance).

3. **Linked list maintenance** — Every add/remove updates the
   doubly-linked list, adding constant overhead per operation.

4. **No random access** — Like HashSet, no `get(index)`.
   Must iterate to access elements.

5. **Iteration is O(n)** — Traverses the linked list, which is
   exactly n nodes. No wasted traversal.

6. **Consistent iteration** — Even with concurrent modifications,
   iterator sees a consistent snapshot (fail-fast).
