# ArrayList Memory Usage

## Per-Instance Overhead

```
┌────────────────────────────────────┐
│  Object header (Mark + Klass)  12B │
│  int size                      4B  │
│  Object[] elementData ref      8B  │
├────────────────────────────────────┤
│  TOTAL INSTANCE:              24B  │
└────────────────────────────────────┘
```

## Backing Array Overhead

```
┌────────────────────────────────────┐
│  Object header                16B  │
│  int length                    4B  │
│  Padding                       4B  │
├────────────────────────────────────┤
│  TOTAL ARRAY HEADER:          16B  │
└────────────────────────────────────┘

Per slot: 4 bytes (reference to object or null)
```

## Per-Element Cost

### Reference storage
```
4 bytes per slot in backing array
```

### Example: Integer objects

```
ArrayList<Integer> list = new ArrayList<>();
list.add(42);
```

```
ArrayList instance:         24 bytes
Backing array header:      16 bytes
  Slot [0] (reference):     4 bytes
Integer object:
  Object header:           12 bytes
  int value:                4 bytes
  ─────────────────────────────────
  Integer total:           16 bytes
```

## Scaling: 1000 Integer Elements

```
ArrayList instance:          24 bytes
Backing array:
  Header:                   16 bytes
  1500 slots (1.5x growth):  6,000 bytes   (capacity = 1500)
  ─────────────────────────────────────
  Array total:             6,016 bytes

1000 Integer objects:
  1000 × 16 bytes:        16,000 bytes

─────────────────────────────────────────
TOTAL:                     22,040 bytes ≈ 22 KB
```

## Comparison: ArrayList vs LinkedList (1000 Integers)

```
┌──────────────────┬──────────────┬───────────────┐
│                  │  ArrayList   │  LinkedList   │
├──────────────────┼──────────────┼───────────────┤
│ Container        │     24 B     │     24 B      │
│ Per-element      │      4 B     │     32 B      │
│ Object overhead  │  16,000 B    │  16,000 B     │
│ Structure        │  6,016 B     │  32,000 B     │
├──────────────────┼──────────────┼───────────────┤
│ TOTAL (1000)     │  ≈ 22 KB    │   ≈ 48 KB     │
│ TOTAL (10000)    │ ≈ 178 KB    │  ≈ 480 KB     │
└──────────────────┴──────────────┴───────────────┘
```

## Capacity vs Size

```
list.size()     → actual elements stored
list.capacity() → elementData.length (internal)

ArrayList grows by 1.5x, so capacity > size after first resize.

Example progression:
  size=10  → capacity=10   (initial)
  size=11  → capacity=15   (grew to 15)
  size=16  → capacity=22   (grew to 22)
  size=23  → capacity=33   (grew to 33)
```

## Memory Wastage

```
Unused slots = (capacity - size) × 4 bytes

Example: size=11, capacity=15
  Wasted: (15 - 11) × 4 = 16 bytes

After clear(): size=0 but capacity unchanged
  Wasted: 1500 × 4 = 6,000 bytes

Fix: list.trimToSize() shrinks array to exact size
```

## Empty ArrayList

```
new ArrayList<>()
  Instance:           24 bytes
  elementData:     →  empty shared array (EMPTY_ELEMENTDATA)
                       No allocation until first add()
  Total:              24 bytes
```

## Serialization Size

```
ArrayList uses custom serialization.
Only `size` and elements are written.

Serialization overhead per element: 4 bytes (field header in stream)
1000 elements ≈ 4 KB of stream data + element data
```
