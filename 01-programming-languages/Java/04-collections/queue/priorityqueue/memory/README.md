# PriorityQueue Memory Usage

## Per-Instance Overhead

```
┌────────────────────────────────────────┐
│  Object header (Mark + Klass)     12B  │
│  int size                         4B   │
│  Comparator comp ref              8B   │
│  Object[] queue ref               8B   │
│  Padding                          4B   │
├────────────────────────────────────────┤
│  TOTAL INSTANCE:                 36B  │
└────────────────────────────────────────┘
```

## Backing Array Overhead

```
Header:   16 bytes
Per slot: 4 bytes (reference)
```

## Per-Element Cost

```
4 bytes per slot in backing array
+ Object overhead per element
```

## Scaling: 1000 Integer Elements

```
PriorityQueue instance:      36 bytes
Backing array:
  Header:                   16 bytes
  1024 slots (power of 2): 4,096 bytes
  ──────────────────────────────────
  Array total:            4,112 bytes

1000 Integer objects:
  1000 × 16 bytes:       16,000 bytes

─────────────────────────────────────────
TOTAL:                    20,148 bytes ≈ 20 KB
```

## Comparison: PriorityQueue vs ArrayList (1000 Integers)

```
┌──────────────────┬──────────────┬──────────────┐
│                  │ PriorityQueue│  ArrayList   │
├──────────────────┼──────────────┼──────────────┤
│ Instance         │     36 B     │     24 B     │
│ Array slots      │   1,024      │   1,500      │
│ Array size       │   4,112 B    │   6,016 B    │
│ Elements         │  16,000 B    │  16,000 B    │
├──────────────────┼──────────────┼──────────────┤
│ TOTAL            │   ≈ 20 KB   │   ≈ 22 KB   │
│ poll()           │   O(log n)   │    O(n)      │
│ get(index)       │   Not avail  │    O(1)      │
└──────────────────┴──────────────┴──────────────┘
```

## Memory Layout

```
PriorityQueue with 5 Integer elements:

PriorityQueue instance (36B):
┌──────────────────────────────────────┐
│ header │ size=5 │ comp │ queue ref ─────┐
└──────────────────────────────────────┘
                                         │
                                         ▼
Object[] queue (36 bytes):
┌────┬────┬────┬────┬────┬─ ─ ─┬────┐
│ 1  │ 2  │ 8  │ 5  │ 3  │ ... │    │
└────┴────┴────┴────┴────┴─ ─ ─┴────┘
  ↑
  min element (always at index 0)
```

## Growth Pattern

```
Default initial capacity: 11
Growth: 2x when full

Capacity: 11 → 22 → 44 → 88 → 176 → 352 → 704 → 1408

At size 1000:
  capacity = 1024 (next power of 2)
  wasted: 24 slots × 4 = 96 bytes
```

## Empty PriorityQueue

```
new PriorityQueue<>()
  Instance:            36 bytes
  queue: empty shared array
  size: 0
  comp: null
  Total:               36 bytes
```

## Heap vs Array Memory

```
PriorityQueue uses array, not tree nodes:

Array heap (PriorityQueue):
  [0][1][2][3][4][5][6]  ← contiguous, cache-friendly

Tree heap (if nodes were used):
  Node → Node → Node → Node  ← scattered, cache-unfriendly

Savings: No per-node overhead (left, right, parent pointers)
  Array: 4 bytes per element
  Tree:  36 bytes per element (header + 3 pointers + value)
```

## Comparator Memory

```
Natural ordering (no comparator):
  comp = null, 0 bytes extra

Custom comparator:
  comp ref: 8 bytes
  Comparator object: varies
  Example: Comparator.reverseOrder() → 16 bytes
```
