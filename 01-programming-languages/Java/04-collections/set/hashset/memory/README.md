# HashSet Memory Usage

## Per-Instance Overhead

```
┌────────────────────────────────────────┐
│  Object header (Mark + Klass)     12B  │
│  HashMap map ref                  8B   │
├────────────────────────────────────────┤
│  TOTAL INSTANCE:                  20B  │
└────────────────────────────────────────┘
```

## Backed by HashMap

```
HashSet memory = HashSet instance + HashMap overhead + Node objects
```

## HashMap Internal Overhead

```
┌────────────────────────────────────────┐
│  HashMap instance:                48B  │
│  Node[] table:                   16B   │
│  Per slot:                       4B    │
└────────────────────────────────────────┘
```

## Per-Element Overhead

```
HashMap Node structure:
┌────────────────────────────────────────┐
│  Object header (Mark + Klass)     12B  │
│  int hash                         4B   │
│  K key ref                        8B   │
│  V value ref (PRESENT)            8B   │
│  Node next ref                    8B   │
│  Padding                          4B   │
├────────────────────────────────────────┤
│  TOTAL PER NODE:                 44B   │
└────────────────────────────────────────┘

PRESENT object: 16 bytes (shared, allocated once)
```

## Scaling: 1000 Elements

```
HashSet instance:            20 bytes
HashMap instance:            48 bytes
Node[] table:
  Header:                   16 bytes
  2048 slots (at 0.75): 8,192 bytes
  ──────────────────────────────────
  Table total:           8,208 bytes

1000 Node objects:
  1000 × 44 bytes:      44,000 bytes

1000 Integer keys:
  1000 × 16 bytes:      16,000 bytes

PRESENT object:                 16 bytes (once)

─────────────────────────────────────────
TOTAL:                     68,292 bytes ≈ 67 KB
```

## Memory Layout

```
HashSet<Integer> with 3 elements:

HashSet instance (20B):
┌─────────────────────────┐
│ header │ map ref ──────────┐
└─────────────────────────┘
                             │
                             ▼
HashMap instance (48B):
┌──────────────────────────────────────┐
│ header │ size=3 │ table ref ──────────────┐
└──────────────────────────────────────┘
                                     │
                                     ▼
Node[] table (128B for capacity 32):
┌────┬────┬────┬────┬────┬─ ─ ─┬────┐
│ 0  │ 1  │ 2  │ 3  │ 4  │ ... │ 31 │
└──┬─┴────┴──┬─┴────┴────┴─ ─ ─┴────┘
   │         │
   ▼         ▼
┌──────┐  ┌──────┐
│Node 1│  │Node 2│
│key=42│  │key=100│
│val= P│  │val= P │
│next  │  │next   │
└──────┘  └──────┘
  │
  ▼
┌──────┐
│Node 3│
│key=7 │
│val= P│
│next  │
└──────┘
```

## Comparison: HashSet vs TreeSet (1000 Integers)

```
┌──────────────────┬──────────────┬──────────────┐
│                  │   HashSet    │   TreeSet    │
├──────────────────┼──────────────┼──────────────┤
│ Instance         │     20 B     │     20 B     │
│ Structure        │   8,208 B    │   4,112 B    │
│ Nodes            │  44,000 B    │  48,000 B    │
│ Elements         │  16,000 B    │  16,000 B    │
├──────────────────┼──────────────┼──────────────┤
│ TOTAL            │   ≈ 67 KB   │   ≈ 68 KB   │
│ get/add          │     O(1)     │   O(log n)   │
│ Sorted           │      No      │      Yes     │
└──────────────────┴──────────────┴──────────────┘
```

## Comparison: HashSet vs ArrayList (1000 Integers)

```
┌──────────────────┬──────────────┬──────────────┐
│                  │   HashSet    │  ArrayList   │
├──────────────────┼──────────────┼──────────────┤
│ Instance         │     20 B     │     24 B     │
│ Structure        │   8,208 B    │   6,016 B    │
│ Nodes            │  44,000 B    │     None     │
│ Elements         │  16,000 B    │  16,000 B    │
├──────────────────┼──────────────┼──────────────┤
│ TOTAL            │   ≈ 67 KB   │   ≈ 22 KB   │
│ contains         │     O(1)     │     O(n)     │
│ Ordered          │      No      │     Yes      │
└──────────────────┴──────────────┴──────────────┘
```

## Empty HashSet

```
new HashSet<>()
  Instance:           20 bytes
  map: empty HashMap   48 bytes
  Total:              68 bytes
```

## Memory Optimization

```
1. Pre-size if known:
   new HashSet<>(expectedSize / 0.75 + 1)
   Avoids rehashing and wasted table space

2. Use smaller objects as keys:
   Integer (16B) vs String (40B+)

3. Consider EnumSet for enum types:
   125 bytes for 1000 enums vs 67KB for HashSet
```
