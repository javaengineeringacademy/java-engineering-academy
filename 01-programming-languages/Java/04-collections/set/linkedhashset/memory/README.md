# LinkedHashSet Memory Usage

## Per-Instance Overhead

```
┌────────────────────────────────────────┐
│  Object header (Mark + Klass)     12B  │
│  LinkedHashMap map ref             8B  │
├────────────────────────────────────────┤
│  TOTAL INSTANCE:                  20B  │
└────────────────────────────────────────┘
```

## Backed by LinkedHashMap

```
LinkedHashSet = HashSet instance + LinkedHashMap overhead
```

## LinkedHashMap Internal Overhead

```
┌────────────────────────────────────────┐
│  HashMap instance:                48B  │
│  Node[] table:                   16B   │
│  head ref (linked list)           8B   │
│  tail ref (linked list)           8B   │
│  Per slot:                       4B    │
└────────────────────────────────────────┘
```

## Per-Element Overhead

```
LinkedHashMap.Node (extends HashMap.Node):
┌────────────────────────────────────────┐
│  Object header (Mark + Klass)     12B  │
│  int hash                         4B   │
│  K key ref                        8B   │
│  V value ref (PRESENT)            8B   │
│  Node next ref (hash chain)       8B   │
│  Node before ref (linked list)    8B   │
│  Node after ref (linked list)     8B   │
│  Padding                          4B   │
├────────────────────────────────────────┤
│  TOTAL PER NODE:                 60B   │
└────────────────────────────────────────┘
```

## Scaling: 1000 Elements

```
LinkedHashSet instance:        20 bytes
LinkedHashMap instance:        48 bytes
Node[] table:
  Header:                    16 bytes
  2048 slots:             8,192 bytes
  ──────────────────────────────────
  Table total:            8,208 bytes

head/tail pointers:            16 bytes

1000 LinkedHashMap.Node objects:
  1000 × 60 bytes:        60,000 bytes

1000 Integer keys:
  1000 × 16 bytes:        16,000 bytes

PRESENT object:                16 bytes (once)

─────────────────────────────────────────
TOTAL:                     84,308 bytes ≈ 82 KB
```

## Comparison: LinkedHashSet vs HashSet (1000 Integers)

```
┌──────────────────┬──────────────┬──────────────┐
│                  │  HashSet     │ LinkedHashSet│
├──────────────────┼──────────────┼──────────────┤
│ Instance         │     20 B     │     20 B     │
│ Structure        │   8,208 B    │   8,224 B    │
│ Nodes            │  44,000 B    │  60,000 B    │
│ Elements         │  16,000 B    │  16,000 B    │
├──────────────────┼──────────────┼──────────────┤
│ TOTAL            │   ≈ 67 KB   │   ≈ 82 KB   │
│ Per-element      │    44 B      │     60 B     │
│ Extra overhead   │    None      │  16 B/entry  │
└──────────────────┴──────────────┴──────────────┘
```

## Memory Layout

```
LinkedHashSet<String> with 3 elements:

LinkedHashSet instance (20B):
┌─────────────────────────┐
│ header │ map ref ──────────┐
└─────────────────────────┘
                             │
                             ▼
LinkedHashMap instance (48B):
┌──────────────────────────────────────┐
│ header │ size=3 │ table │ head │ tail │
└──────────────────────────────────────┘

Node[] table:
┌────┬────┬────┬─ ─ ─┬────┐
│ 0  │ 1  │ 2  │ ... │ 31 │
└──┬─┴────┴────┴─ ─ ─┴────┘
   │
   ▼
  Node("A")
  ┌──────────────────────┐
  │ hash, key="A", val=P │
  │ before← → after      │──► Node("C")
  │ next                  │
  └──────────────────────┘
                          │
                          ▼
                        Node("C")
                        ┌──────────────────────┐
                        │ hash, key="C", val=P │
                        │ before← → after      │──► Node("B")
                        │ next                  │
                        └──────────────────────┘
                                              │
                                              ▼
                                            Node("B")
                                            ┌──────────────────────┐
                                            │ hash, key="B", val=P │
                                            │ before← → after=null │
                                            │ next=null             │
                                            └──────────────────────┘

Linked list traversal: head → A → C → B → tail
```

## Why LinkedHashSet Costs More

```
Each node needs TWO extra references:
  - before: 8 bytes (previous in linked list)
  - after:  8 bytes (next in linked list)
  ─────────────────────────────
  Extra: 16 bytes per element

For 1000 elements:
  HashSet:    44 × 1000 = 44,000 bytes
  LinkedHashSet: 60 × 1000 = 60,000 bytes
  Difference: 16,000 bytes = 16 KB
```

## Empty LinkedHashSet

```
new LinkedHashSet<>()
  Instance:              20 bytes
  LinkedHashMap:         48 bytes
  head/tail:             16 bytes
  Total:                 84 bytes
```

## Memory Optimization

```
1. Use HashSet if order doesn't matter:
   Saves 16 bytes per element

2. Pre-size to avoid rehashing:
   new LinkedHashSet<>(expectedSize)

3. Consider TreeMap if you need sorted order:
   Uses less memory than LinkedHashMap for sorted access
```
