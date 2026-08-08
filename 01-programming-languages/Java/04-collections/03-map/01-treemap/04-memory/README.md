# TreeMap Memory Usage

## Per-Instance Overhead

```
┌────────────────────────────────────────┐
│  Object header (Mark + Klass)     12B  │
│  Entry root ref                  8B   │
│  int size                         4B   │
│  Comparator comp ref              8B   │
│  int modCount                     4B   │
│  Padding                         12B   │
├────────────────────────────────────────┤
│  TOTAL INSTANCE:                 48B  │
└────────────────────────────────────────┘
```

## Per-Entry Overhead

```
TreeMap.Entry structure:
┌────────────────────────────────────────┐
│  Object header (Mark + Klass)     12B  │
│  K key ref                        8B   │
│  V value ref                      8B   │
│  Entry left ref                   8B   │
│  Entry right ref                  8B   │
│  Entry parent ref                 8B   │
│  boolean color                     4B  │
│  Padding                          4B   │
├────────────────────────────────────────┤
│  TOTAL PER ENTRY:                 60B  │
└────────────────────────────────────────┘
```

## Scaling: 1000 Entries

```
TreeMap instance:              48 bytes

1000 TreeMap.Entry objects:
  1000 × 60 bytes:        60,000 bytes

1000 Integer keys:
  1000 × 16 bytes:        16,000 bytes

1000 Integer values:
  1000 × 16 bytes:        16,000 bytes

─────────────────────────────────────────
TOTAL:                     92,048 bytes ≈ 90 KB
```

## Comparison: TreeMap vs HashMap (1000 Entries)

```
┌──────────────────┬──────────────┬──────────────┐
│                  │   TreeMap    │   HashMap    │
├──────────────────┼──────────────┼──────────────┤
│ Instance         │     48 B     │     56 B     │
│ Table/Structure  │      None    │   8,208 B    │
│ Nodes/Entries    │  60,000 B    │  44,000 B    │
│ Keys             │  16,000 B    │  16,000 B    │
│ Values           │  16,000 B    │  16,000 B    │
├──────────────────┼──────────────┼──────────────┤
│ TOTAL            │   ≈ 90 KB   │   ≈ 82 KB   │
│ Per-entry        │     60 B     │     44 B     │
│ get/put          │   O(log n)   │     O(1)     │
│ Sorted           │      Yes     │      No      │
└──────────────────┴──────────────┴──────────────┘
```

## Memory Layout

```
TreeMap with 5 entries:

TreeMap instance (48B):
┌──────────────────────────────────────┐
│ header │ root ──► Entry(5)            │
│ size=5 │ comp                        │
└──────────────────────────────────────┘

Tree structure:
     ┌──────────────────┐
     │ Entry(5)         │
     │ key=5, val="E"   │
     │ left→   right→   │
     └────────┬─────────┘
        ┌─────┴─────┐
        ▼           ▼
  ┌──────────┐ ┌──────────┐
  │ Entry(3) │ │ Entry(7) │
  │ key=3    │ │ key=7    │
  │ val="C"  │ │ val="G"  │
  └────┬─────┘ └────┬─────┘
       │             │
       ▼             ▼
  ┌──────────┐ ┌──────────┐
  │ Entry(1) │ │ Entry(4) │
  │ key=1    │ │ key=4    │
  │ val="A"  │ │ val="D"  │
  └──────────┘ └──────────┘

Each Entry: 60 bytes
```

## Entry Size Breakdown

```
TreeMap.Entry per-field:
  key ref:     8 bytes (always)
  value ref:   8 bytes (always)
  left ref:    8 bytes (always, null if leaf)
  right ref:   8 bytes (always, null if leaf)
  parent ref:  8 bytes (always, null if root)
  color:       4 bytes (boolean, always)
  ─────────────────────────────
  Total:      52 bytes + 8 header = 60 bytes
```

## Empty TreeMap

```
new TreeMap<>()
  Instance:            48 bytes
  root: null
  size: 0
  comp: null
  Total:               48 bytes
```

## Comparison: TreeMap vs LinkedHashMap (1000 Entries)

```
┌──────────────────┬──────────────┬──────────────┐
│                  │   TreeMap    │ LinkedHashMap│
├──────────────────┼──────────────┼──────────────┤
│ Instance         │     48 B     │     56 B     │
│ Structure        │      None    │   8,208 B    │
│ Nodes/Entries    │  60,000 B    │  60,000 B    │
│ Keys             │  16,000 B    │  16,000 B    │
│ Values           │  16,000 B    │  16,000 B    │
├──────────────────┼──────────────┼──────────────┤
│ TOTAL            │   ≈ 90 KB   │   ≈ 92 KB   │
│ Sorted           │      Yes     │      No      │
│ Ordered          │      No      │      Yes     │
│ get/put          │   O(log n)   │     O(1)     │
└──────────────────┴──────────────┴──────────────┘
```

## Memory Optimization

```
1. Use HashMap if sorted order not needed:
   Saves 16 bytes per entry + O(1) vs O(log n)

2. Use EnumMap for enum keys:
   Much more memory efficient

3. Pre-size TreeMap to avoid rebalancing:
   Not as critical as HashMap (no bucket array)

4. Consider ArrayMap for small sorted maps:
   Binary search on sorted arrays
```
