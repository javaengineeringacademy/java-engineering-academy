# HashSet Internals

## Backed by HashMap

```
HashSet<E> is backed by HashMap<E, Object>

private transient HashMap<E, Object> map;

private static final Object PRESENT = new Object();
```

All values in the underlying HashMap are the same dummy object `PRESENT`.

## How HashSet Works

```
HashSet<Integer> set = new HashSet<>();
set.add(42);
set.add(100);
set.add(42);   // duplicate, ignored

Internally:
  map.put(42, PRESENT)  →  null (new entry)
  map.put(100, PRESENT) →  null (new entry)
  map.put(42, PRESENT)  →  PRESENT (existing, returns old value)

  map: {
    42  → PRESENT,
    100 → PRESENT
  }
```

## Operation Flow

### add(element)

```
1. map.put(element, PRESENT)
2. If return value is null: element was added → return true
3. If return value is PRESENT: duplicate → return false

Time: O(1) amortized
```

### contains(element)

```
1. map.containsKey(element)
2. Return true if key exists

Time: O(1)
```

### remove(element)

```
1. map.remove(element)
2. If return value is PRESENT: removed → return true
3. If return value is null: not found → return false

Time: O(1)
```

### size()

```
Return: map.size()

Time: O(1)
```

## Memory Layout Diagram

```
HashSet instance:
┌─────────────────────────────────┐
│  Object header     (12 bytes)   │
│  HashMap map ref   (8 bytes)    │
└──────────────┬──────────────────┘
               │
               ▼
     ┌─────────────────────────────────────────┐
     │  HashMap internal structure              │
     │  Node[] table                            │
     │  ┌───┬───┬───┬───┬───┬───┬───┬───┐     │
     │  │ 0 │ 1 │ 2 │ 3 │ 4 │ 5 │ 6 │ 7 │     │
     │  └─┬─┴───┴───┴─┬─┴───┴───┴─┬─┴───┘     │
     │    │           │           │              │
     │    ▼           ▼           ▼              │
     │  ┌──────┐   ┌──────┐   ┌──────┐         │
     │  │42    │   │100   │   │7     │         │
     │  │PRESENT│  │PRESENT│  │PRESENT│         │
     │  │ null │   │ null │   │ null │         │
     │  └──────┘   └──────┘   └──────┘         │
     └─────────────────────────────────────────┘
```

## No Duplicate Guarantee

```
HashMap keys are unique. HashSet leverages this:

  set.add("A") → map.put("A", PRESENT) → null → added
  set.add("A") → map.put("A", PRESENT) → PRESENT → duplicate, ignored

  Set invariant: for all x in set, count(x) == 1
```

## Null Element

```
HashSet allows one null element:

  set.add(null) → map.put(null, PRESENT) → null → added
  set.add(null) → map.put(null, PRESENT) → PRESENT → duplicate

  HashMap stores null key at index 0
```

## Thread Safety

HashSet is **not** synchronized. Concurrent access can cause:
- ConcurrentModificationException during iteration
- Lost additions
- Corrupted internal state

Use `Collections.synchronizedSet()` or `ConcurrentHashMap.newKeySet()`.

## Key Implementation Details

1. **PRESENT is shared** — All map values reference the same static
   final Object. No per-element value allocation.

2. **No index access** — Unlike List, Set has no `get(index)`.
   Iteration is the only way to access elements.

3. **Hash-based** — Element position determined by hashCode().
   Good hashCode() distribution = good performance.

4. **Load factor** — Inherits HashMap's 0.75 default load factor.
   Triggers resize when 75% full.

5. **Iteration order** — Undefined. Depends on hash codes and
   table layout. Use LinkedHashSet for insertion order.
