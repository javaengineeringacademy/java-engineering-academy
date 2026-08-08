# ConcurrentHashMap Memory Usage

## Per-Instance Overhead

```
┌────────────────────────────────────────┐
│  Object header (Mark + Klass)     12B  │
│  volatile Node[] table ref        8B   │
│  volatile int sizeCtl             4B   │
│  volatile long baseCount          8B   │
│  CounterCell[] counterCells ref   8B   │
│  int transferIndex               4B   │
│  int nCells                      4B   │
│  int cellBusy                    4B   │
│  Node[] nextTable ref            8B   │
│  long baseCount                  8B   │
│  int sizeCtl                     4B   │
│  Padding                        16B   │
├────────────────────────────────────────┤
│  TOTAL INSTANCE:                 88B  │
└────────────────────────────────────────┘
```

## Per-Entry Overhead

```
ConcurrentHashMap.Node:
┌────────────────────────────────────────┐
│  Object header (Mark + Klass)     12B  │
│  int hash                         4B   │
│  K key ref                        8B   │
│  volatile V val ref               8B   │
│  volatile Node next ref           8B   │
│  Padding                          4B   │
├────────────────────────────────────────┤
│  TOTAL PER NODE:                 44B  │
└────────────────────────────────────────┘
```

## Scaling: 1000 Entries

```
ConcurrentHashMap instance:   88 bytes
CounterCell array:
  Header:                    16 bytes
  4 cells:                   32 bytes
  ──────────────────────────────────
  Counter total:            48 bytes

Node[] table:
  Header:                    16 bytes
  2048 slots:             8,192 bytes
  ──────────────────────────────────
  Table total:            8,208 bytes

1000 Node objects:
  1000 × 44 bytes:       44,000 bytes

1000 Integer keys:
  1000 × 16 bytes:       16,000 bytes

1000 Integer values:
  1000 × 16 bytes:       16,000 bytes

─────────────────────────────────────────
TOTAL:                     84,344 bytes ≈ 82 KB
```

## Comparison: ConcurrentHashMap vs HashMap (1000 Entries)

```
┌──────────────────┬──────────────┬──────────────┐
│                  │  ConcurrentHashMap │ HashMap  │
├──────────────────┼──────────────┼──────────────┤
│ Instance         │     88 B     │     56 B     │
│ CounterCell      │     48 B     │      None    │
│ Table            │   8,208 B    │   8,208 B    │
│ Nodes            │  44,000 B    │  44,000 B    │
│ Keys             │  16,000 B    │  16,000 B    │
│ Values           │  16,000 B    │  16,000 B    │
├──────────────────┼──────────────┼──────────────┤
│ TOTAL            │   ≈ 84 KB   │   ≈ 82 KB   │
│ Thread safe      │     Yes      │      No      │
│ Null keys/values │      No      │     Yes      │
└──────────────────┴──────────────┴──────────────┘
```

## Volatile Overhead

```
volatile fields add memory barriers:
  val:      8 bytes (volatile read/write)
  next:     8 bytes (volatile read/write)
  table:    8 bytes (volatile array ref)

Total volatile overhead: 24 bytes per instance

This ensures visibility across threads but adds
~1-2 ns per volatile access vs normal field.
```

## CounterCell Array Memory

```
For contended counting:

CounterCell[]:
  Header:            16 bytes
  Cell[0]:            8 bytes (long value)
  Cell[1]:            8 bytes
  Cell[2]:            8 bytes
  Cell[3]:            8 bytes
  ──────────────────────────
  Total:             48 bytes

Each thread increments its own cell.
No contention between threads.
```

## Memory During Resize

```
During resize, TWO tables exist:
  Old table:  8,208 bytes
  New table: 16,384 bytes (double)
  ────────────────────────────
  Peak:     24,592 bytes

After resize completes:
  Old table: eligible for GC
  New table: 16,384 bytes
```

## Empty ConcurrentHashMap

```
new ConcurrentHashMap<>()
  Instance:           88 bytes
  table: null (lazy init)
  counterCells: null
  Total:              88 bytes
```

## CAS Overhead

```
CAS operations add CPU overhead:
  - Atomic compare-and-swap instruction
  - Cache line invalidation
  - Memory barriers

Per CAS: ~10-20 ns
Per synchronized: ~30-50 ns

CAS wins when contention is low.
Synchronized wins when contention is high.
```

## Comparison: ConcurrentHashMap vs Hashtable (1000 Entries)

```
┌──────────────────┬──────────────┬──────────────┐
│                  │ConcurrentHM  │  Hashtable   │
├──────────────────┼──────────────┼──────────────┤
│ Instance         │     88 B     │     48 B     │
│ Table            │   8,208 B    │   6,156 B    │
│ Entries          │  44,000 B    │  44,000 B    │
│ Keys             │  16,000 B    │  16,000 B    │
│ Values           │  16,000 B    │  16,000 B    │
│ Lock overhead    │     Low      │     High     │
├──────────────────┼──────────────┼──────────────┤
│ TOTAL            │   ≈ 84 KB   │   ≈ 82 KB   │
│ Performance      │   Excellent  │     Poor     │
│ Scalability      │     High     │     Low      │
└──────────────────┴──────────────┴──────────────┘
```

## Memory Optimization

```
1. Pre-size to avoid resize:
   new ConcurrentHashMap<>(expectedSize / 0.75 + 1)

2. Use computeIfAbsent for lazy init:
   map.computeIfAbsent(key, k -> createValue())

3. Consider LongAdder for size counting:
   More efficient than AtomicInteger for high contention

4. Use parallel bulk operations:
   map.forEach(threshold, action, parallelism)
```
