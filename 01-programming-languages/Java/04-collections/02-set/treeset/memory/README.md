# TreeSet Memory Usage

## Per-Instance Overhead

```
┌────────────────────────────────────────┐
│  Object header (Mark + Klass)     12B  │
│  TreeMap m ref                    8B   │
├────────────────────────────────────────┤
│  TOTAL INSTANCE:                  20B  │
└────────────────────────────────────────┘
```

## Backed by TreeMap

```
TreeSet = TreeSet instance + TreeMap overhead + TreeEntry objects
```

## TreeMap Internal Overhead

```
┌────────────────────────────────────────┐
│  TreeMap instance:                48B  │
│  Entry root ref                   8B   │
│  Comparator ref                   8B   │
│  size                             4B   │
└────────────────────────────────────────┘
```

## Per-Element Overhead

```
TreeMap.Entry structure:
┌────────────────────────────────────────┐
│  Object header (Mark + Klass)     12B  │
│  K key                            8B   │
│  V value (PRESENT)                8B   │
│  Entry left ref                   8B   │
│  Entry right ref                  8B   │
│  Entry parent ref                 8B   │
│  boolean color                     4B  │
│  Padding                          4B   │
├────────────────────────────────────────┤
│  TOTAL PER ENTRY:                 60B  │
└────────────────────────────────────────┘
```

## Scaling: 1000 Elements

```
TreeSet instance:             20 bytes
TreeMap instance:             48 bytes

1000 TreeMap.Entry objects:
  1000 × 60 bytes:        60,000 bytes

1000 Integer keys:
  1000 × 16 bytes:        16,000 bytes

PRESENT object:                16 bytes (once)

─────────────────────────────────────────
TOTAL:                     76,084 bytes ≈ 74 KB
```

## Comparison: TreeSet vs HashSet (1000 Integers)

```
┌──────────────────┬──────────────┬──────────────┐
│                  │   HashSet    │   TreeSet    │
├──────────────────┼──────────────┼──────────────┤
│ Instance         │     20 B     │     20 B     │
│ Container        │     48 B     │     48 B     │
│ Structure        │   8,208 B    │     None     │
│ Nodes/Entries    │  44,000 B    │  60,000 B    │
│ Elements         │  16,000 B    │  16,000 B    │
├──────────────────┼──────────────┼──────────────┤
│ TOTAL            │   ≈ 67 KB   │   ≈ 74 KB   │
│ Per-element      │    44 B      │     60 B     │
│ add/get          │     O(1)     │   O(log n)   │
│ Sorted           │      No      │      Yes     │
└──────────────────┴──────────────┴──────────────┘
```

## Memory Layout

```
TreeSet<Integer> with 5 elements:

TreeSet instance (20B):
┌─────────────────────────┐
│ header │ m ref ────────────┐
└─────────────────────────┘
                             │
                             ▼
TreeMap instance (48B):
┌──────────────────────────────────────┐
│ header │ root ──► Entry(5)            │
└──────────────────────────────────────┘

Tree structure:
     ┌────────────┐
     │ Entry(5)   │
     │ left→  right→ │
     └─────┬──────┘
      ┌────┴────┐
      ▼         ▼
┌──────────┐ ┌──────────┐
│ Entry(3) │ │ Entry(7) │
│ left→  right→ │ left→  right→ │
└────┬─────┘ └────┬─────┘
     │             │
     ▼             ▼
┌──────────┐ ┌──────────┐
│ Entry(1) │ │ Entry(9) │
│ left=null │ │ left=null │
│ right=null│ │ right=null│
└──────────┘ └──────────┘

Each Entry: 60 bytes
```

## Why TreeSet Costs More

```
Each entry needs 5 extra references vs HashMap.Node:
  left:    8 bytes
  right:   8 bytes
  parent:  8 bytes
  color:   4 bytes (boolean)
  ─────────────────────
  Extra:  28 bytes vs HashMap.Node

HashMap.Node:  44 bytes
TreeMap.Entry: 60 bytes
Difference:    16 bytes per element
```

## Empty TreeSet

```
new TreeSet<>()
  Instance:           20 bytes
  TreeMap:            48 bytes
  root: null
  Total:              68 bytes
```

## Memory Optimization

```
1. Use HashSet if sorted order not needed:
   Saves 16 bytes per element + O(1) vs O(log n)

2. Pre-size TreeMap to avoid rebalancing overhead

3. Consider EnumMap for enum keys:
   Much more memory efficient than TreeMap
```
