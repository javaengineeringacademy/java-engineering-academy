# Vector Memory Usage

## Per-Instance Overhead

```
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
Same as ArrayList:
  Header:   16 bytes
  Per slot: 4 bytes (reference)
```

## Per-Element Cost

### Same as ArrayList
```
4 bytes per slot in backing array
```

### Example: Integer objects

```
Vector<Integer> vec = new Vector<>();
vec.add(42);
```

```
Vector instance:            36 bytes
Backing array header:       16 bytes
  Slot [0]:                  4 bytes
Integer object:             16 bytes

Total per element:          20 bytes (array + object)
```

## Scaling: 1000 Integer Elements

```
Vector instance:              36 bytes
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

## Comparison: Vector vs ArrayList (1000 Integers)

```
┌──────────────────┬──────────────┬──────────────┐
│                  │  ArrayList   │    Vector    │
├──────────────────┼──────────────┼──────────────┤
│ Instance         │     24 B     │     36 B     │
│ Array slots      │   1,500      │    2,000     │
│ Array size       │   6,016 B    │   8,016 B    │
│ Elements         │  16,000 B    │  16,000 B    │
├──────────────────┼──────────────┼──────────────┤
│ TOTAL            │   ≈ 22 KB   │   ≈ 24 KB   │
│ Wasted capacity  │     12 KB   │     16 KB    │
└──────────────────┴──────────────┴──────────────┘
```

## Growth Comparison

```
Starting capacity: 10

ArrayList (1.5x):  10 → 15 → 22 → 33 → 49 → 73
Vector (2x):       10 → 20 → 40 → 80 → 160 → 320

At size 100:
  ArrayList capacity: 149   (wasted: 58 slots = 232 bytes)
  Vector capacity:    160   (wasted: 60 slots = 240 bytes)

At size 1000:
  ArrayList capacity: 1496  (wasted: 496 slots = 1,984 bytes)
  Vector capacity:    2048  (wasted: 1048 slots = 4,192 bytes)
```

## Synchronization Overhead

```
Per synchronized method call:
  - Monitor enter:  ~20-30 ns
  - Monitor exit:   ~10-20 ns
  ────────────────────────────
  Total overhead:   ~30-50 ns per operation

For 1000 sequential add operations:
  ArrayList:  ~5 µs total
  Vector:     ~50 µs total (10x slower)

In multi-threaded scenarios, contention adds more:
  Thread contention: 100-1000+ ns per lock acquisition
```

## Memory Layout

```
Vector with 5 elements, capacity 20:

Vector instance (36 bytes):
┌──────────────────────────────────────────────┐
│ header │ monitor │ count=5 │ incr=0 │ ref ─────┐
└──────────────────────────────────────────────┘
                                                 │
                                                 ▼
Backing array (8016 bytes for Integer):
┌────┬────┬────┬────┬────┬────┬────┬────┬─ ─ ─┬────┐
│ A  │ B  │ C  │ D  │ E  │ n  │ n  │ n  │ ... │ n  │
└────┴────┴────┴────┴────┴────┴────┴────┴─ ─ ─┴────┘
  ↑    ↑    ↑    ↑    ↑    ↑
  └────┴────┴────┴────┴────┴── 5 used slots
                              ↑
                         15 unused slots = 60 bytes wasted
```

## Empty Vector

```
new Vector<>()
  Instance:            36 bytes
  elementData → shared empty array
  elementCount = 0
  capacityIncrement = 0
  Total:               36 bytes
```

## Recommendation

```
┌─────────────────────────────────────────────────┐
│  Vector adds 12 bytes overhead per instance     │
│  + 2x growth wastes more memory                 │
│  + synchronization slows single-threaded code   │
│                                                 │
│  Use ArrayList for all new code.                │
│  Add synchronization externally if needed.      │
└─────────────────────────────────────────────────┘
```
