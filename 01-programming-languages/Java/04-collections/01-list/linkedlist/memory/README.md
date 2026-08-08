# LinkedList Memory Usage

## Per-Instance Overhead

```
┌────────────────────────────────────┐
│  Object header (Mark + Klass)  12B │
│  int size                      4B  │
│  Node first ref                8B  │
│  Node last ref                 8B  │
├────────────────────────────────────┤
│  TOTAL INSTANCE:              32B  │
└────────────────────────────────────┘
```

## Per-Node Overhead

```
┌────────────────────────────────────┐
│  Object header (Mark + Klass)  12B │
│  Node prev ref                 8B  │
│  Node next ref                 8B  │
│  E item ref                    4B  │
│  Padding                       4B  │
├────────────────────────────────────┤
│  TOTAL PER NODE:              36B  │
└────────────────────────────────────┘
```

## Per-Element Cost

### Reference storage
```
Each node stores:
  - prev reference:  8 bytes
  - next reference:  8 bytes
  - item reference:  4 bytes (pointing to actual object)
  - Object header:  12 bytes
  ────────────────────────────
  Total per node:   32 bytes
```

### Example: Integer objects

```
LinkedList<Integer> list = new LinkedList<>();
list.add(42);
```

```
LinkedList instance:         32 bytes
Node object:
  Object header:             12 bytes
  prev ref (null):            8 bytes
  next ref (null):            8 bytes
  item ref                   4 bytes
  ─────────────────────────────────
  Node total:                32 bytes

Integer object:
  Object header:             12 bytes
  int value:                  4 bytes
  ─────────────────────────────────
  Integer total:             16 bytes
```

## Scaling: 1000 Integer Elements

```
LinkedList instance:           32 bytes
1000 Node objects:
  1000 × 32 bytes:        32,000 bytes

1000 Integer objects:
  1000 × 16 bytes:        16,000 bytes

─────────────────────────────────────────
TOTAL:                         48,032 bytes ≈ 48 KB
```

## Comparison: ArrayList vs LinkedList (1000 Integers)

```
┌──────────────────┬──────────────┬───────────────┐
│                  │  ArrayList   │  LinkedList   │
├──────────────────┼──────────────┼───────────────┤
│ Container        │     24 B     │     32 B      │
│ Per-element      │      4 B     │     32 B      │
│ Object overhead  │  16,000 B    │  16,000 B     │
│ Structure        │  6,016 B     │  32,000 B     │
├──────────────────┼──────────────┼───────────────┤
│ TOTAL (1000)     │  ≈ 22 KB    │   ≈ 48 KB     │
│ TOTAL (10000)    │ ≈ 178 KB    │  ≈ 480 KB     │
│ TOTAL (100000)   │ ≈ 1.4 MB    │  ≈ 4.8 MB     │
└──────────────────┴──────────────┴───────────────┘
```

## Memory Layout Comparison

```
ArrayList (contiguous memory):
┌────┬────┬────┬────┬────┬────┐
│ A  │ B  │ C  │ D  │ E  │    │  ← Array of references
└────┴────┴────┴────┴────┴────┘
  ↓    ↓    ↓    ↓    ↓
  Objects scattered on heap

LinkedList (scattered nodes):
┌─────┐  ┌─────┐  ┌─────┐  ┌─────┐
│Node1│→ │Node2│→ │Node3│→ │Node4│
└─────┘  └─────┘  └─────┘  └─────┘
  ↓        ↓        ↓        ↓
  Objects scattered on heap
```

## Cache Performance

```
ArrayList:
  [A][B][C][D][E]  ← References contiguous in memory
  ✓ CPU cache line fetches multiple elements
  ✓ Better spatial locality

LinkedList:
  [Node1]→[Node2]→[Node3]→[Node4]
    ↓         ↓         ↓
  Random heap locations
  ✗ Cache misses on every traversal
  ✗ Poor spatial locality
```

## Empty LinkedList

```
new LinkedList<>()
  Instance:           32 bytes
  first = null
  last  = null
  No node objects allocated
  Total:              32 bytes
```

## Why LinkedList Is Rarely Better

```
1. Memory:  8x more per element (32B vs 4B)
2. Cache:   Poor locality hurts real performance
3. API:     get(index) is O(n), not O(1)
4. GC:      More objects = more GC pressure
5. Overhead: 36 bytes node for just 4 bytes of reference

ArrayList wins in almost every scenario.
LinkedList only wins at extreme addFirst/removeFirst at head.
```
