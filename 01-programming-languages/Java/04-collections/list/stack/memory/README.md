# Stack Memory Usage

## Per-Instance Overhead

```
Same as Vector:

┌─────────────────────────────────────┐
│  Object header (Mark + Klass)   12B │
│  Monitor lock info              8B  │
│  int elementCount               4B  │
│  int capacityIncrement          4B  │
│  Object[] elementData ref       8B  │
├─────────────────────────────────────┤
│  TOTAL INSTANCE:               36B  │
└─────────────────────────────────────┘
```

## Backing Array Overhead

```
Same as Vector:
  Header:   16 bytes
  Per slot: 4 bytes (reference)
```

## Per-Element Cost

```
Same as Vector:
  4 bytes per slot in backing array
  + Object overhead per pushed element
```

## Scaling: 1000 Integer Elements

```
Stack instance:               36 bytes
Backing array:
  Header:                    16 bytes
  2000 slots (2x growth): 8,000 bytes   (capacity = 2000)
  ─────────────────────────────────────
  Array total:            8,016 bytes

1000 Integer objects:
  1000 × 16 bytes:       16,000 bytes

─────────────────────────────────────────
TOTAL:                    24,052 bytes ≈ 24 KB
```

## Comparison: Stack vs ArrayDeque (1000 Integers)

```
┌──────────────────┬──────────────┬──────────────┐
│                  │    Stack     │  ArrayDeque  │
├──────────────────┼──────────────┼──────────────┤
│ Instance         │     36 B     │     56 B     │
│ Array slots      │   2,000      │   1,024      │
│ Array size       │   8,016 B    │   4,112 B    │
│ Elements         │  16,000 B    │  16,000 B    │
├──────────────────┼──────────────┼──────────────┤
│ TOTAL            │   ≈ 24 KB   │   ≈ 20 KB   │
│ Thread safe      │      Yes     │      No      │
│ Performance      │    Slower    │    Faster    │
└──────────────────┴──────────────┴──────────────┘
```

## Memory Layout

```
Stack with 3 elements ("A", "B", "C"):

Stack instance (36 bytes):
┌──────────────────────────────────────────┐
│ header │ monitor │ count=3 │ incr=0 │ref──┐
└──────────────────────────────────────────┘
                                                 │
                                                 ▼
Backing array:
┌────┬────┬────┬────┬────┬─ ─ ─┬────┐
│ A  │ B  │ C  │ n  │ n  │ ... │ n  │
└────┴────┴────┴────┴────┴─ ─ ─┴────┘
  ↑                  ↑
 bottom             top (index = size-1 = 2)
```

## Synchronization Overhead

```
Same as Vector:
  Every push/pop/peek acquires and releases monitor lock
  Overhead: ~30-50 ns per operation

In multi-threaded scenarios with contention:
  Lock wait time: 100-1000+ ns per operation
```

## Why Stack Wastes Memory

```
1. 2x growth vs ArrayDeque 2x but no lock overhead
2. Unused slots after growth:
   capacity=2000, size=1000 → 1000 empty slots = 4,000 bytes wasted

3. Monitor lock info: 8 bytes per instance (not needed in ArrayDeque)

4. capacityIncrement field: 4 bytes (usually unused)
```

## Empty Stack

```
new Stack<>()
  Instance:            36 bytes
  elementData → shared empty array
  elementCount = 0
  capacityIncrement = 0
  Total:               36 bytes
```

## Recommendation

```
┌──────────────────────────────────────────────────────┐
│  Stack memory:  ≈ 24 KB for 1000 elements            │
│  ArrayDeque:    ≈ 20 KB for 1000 elements            │
│                                                      │
│  Stack adds:                                         │
│    - 8 bytes sync overhead per instance              │
│    - ~30-50 ns per operation (lock/unlock)           │
│    - No functional benefit over ArrayDeque           │
│                                                      │
│  Use ArrayDeque for new code.                        │
└──────────────────────────────────────────────────────┘
```
