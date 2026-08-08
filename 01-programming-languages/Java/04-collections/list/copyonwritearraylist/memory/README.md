# CopyOnWriteArrayList Memory Usage

## Per-Instance Overhead

```
┌────────────────────────────────────────┐
│  Object header (Mark + Klass)     12B  │
│  Object[] array ref (volatile)     8B  │
├────────────────────────────────────────┤
│  TOTAL INSTANCE:                  20B  │
└────────────────────────────────────────┘
```

## Backing Array Overhead

```
Same as ArrayList:
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
COWAL instance:                20 bytes
Backing array:
  Header:                     16 bytes
  1000 slots:              4,000 bytes   (exact size, no growth)
  ─────────────────────────────────────
  Array total:             4,016 bytes

1000 Integer objects:
  1000 × 16 bytes:        16,000 bytes

─────────────────────────────────────────
TOTAL:                     20,036 bytes ≈ 20 KB
```

## Write Overhead

```
Every write operation creates a temporary copy:

add(element):
  Old array:     4,016 bytes
  New array:     4,020 bytes (one more slot)
  Temporary:     4,016 bytes (old array, now garbage)
  ─────────────────────────────────
  Peak memory:   8,036 bytes during write

remove(index):
  Old array:     4,016 bytes
  New array:     4,012 bytes (one less slot)
  Temporary:     4,016 bytes (old array, now garbage)
  ─────────────────────────────────
  Peak memory:   8,028 bytes during write
```

## Comparison: COWAL vs ArrayList vs synchronizedList

```
┌──────────────────┬──────────┬──────────┬──────────────┐
│                  │  COWAL   │ ArrayList│ synchronized │
├──────────────────┼──────────┼──────────┼──────────────┤
│ Instance         │   20 B   │   24 B   │     24 B     │
│ Array (1000)     │ 4,016 B  │ 6,016 B  │   6,016 B    │
│ Elements         │ 16,000 B │ 16,000 B │  16,000 B    │
│ Sync overhead    │    None  │    None  │     Lock     │
├──────────────────┼──────────┼──────────┼──────────────┤
│ TOTAL (1000)     │ ≈ 20 KB │ ≈ 22 KB │   ≈ 22 KB   │
│ Write overhead   │ High     │ None     │     Lock     │
│ Read performance │ Excellent│ Fast     │     Slow     │
│ Thread safe      │    Yes   │    No    │      Yes     │
└──────────────────┴──────────┴──────────┴──────────────┘
```

## Memory During Concurrent Operations

```
Scenario: 3 readers + 1 writer

Reader 1: sees snapshot_1  [A, B, C]
Reader 2: sees snapshot_1  [A, B, C]
Reader 3: sees snapshot_2  [A, B, C, D]  (read after swap)
Writer:   creating snapshot_3 [A, B, C, D, E]

Peak memory:
  snapshot_1:  4,016 bytes
  snapshot_2:  4,020 bytes
  snapshot_3:  4,024 bytes (being created)
  ────────────────────────────
  Total:      12,060 bytes + elements
```

## Garbage Collection Impact

```
Each write creates garbage:
  - Old array becomes unreachable
  - GC must collect old array + its header
  - Frequent writes = frequent short-lived objects

For 1000 writes:
  1000 temporary arrays × ~4KB = ~4MB of garbage
  GC must process this garbage
```

## Empty COWAL

```
new CopyOnWriteArrayList<>()
  Instance:           20 bytes
  array → shared empty array
  Total:              20 bytes
```

## Iterator Memory

```
Iterator holds reference to snapshot array:
  Iterator instance:        16 bytes
  Snapshot array reference:  8 bytes
  ────────────────────────────────
  Total per iterator:       24 bytes

Multiple iterators can exist simultaneously,
each holding different snapshot arrays.
```

## Memory Optimization Tips

```
1. Pre-size the list if you know approximate size
   to avoid growth copies:
   new CopyOnWriteArrayList<>(initialCapacity)

2. Batch writes when possible:
   List<String> batch = new ArrayList<>();
   // ... add multiple items ...
   copyOnWriteList.addAll(batch);  // One copy instead of many

3. Consider alternatives for write-heavy scenarios:
   - Collections.synchronizedList()
   - ConcurrentLinkedQueue for producer-consumer
   - BlockingQueue implementations
```
