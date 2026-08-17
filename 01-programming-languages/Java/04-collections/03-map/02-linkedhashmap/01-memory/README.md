# LinkedHashMap Memory Usage

## Per-Instance Overhead

```
┌────────────────────────────────────────┐
│  Object header (Mark + Klass)     12B  │
│  Node[] table ref                 8B   │
│  int size                         4B   │
│  int threshold                    4B   │
│  float loadFactor                 4B   │
│  int modCount                     4B   │
│  Set<Entry> entrySet ref          8B   │
│  Entry head ref                   8B   │
│  Entry tail ref                   8B   │
│  boolean accessOrder              4B   │
│  Padding                         12B   │
├────────────────────────────────────────┤
│  TOTAL INSTANCE:                 76B  │
└────────────────────────────────────────┘
```

## Per-Entry Overhead

```
LinkedHashMap.Entry (extends HashMap.Node):

HashMap.Node:           44 bytes
  + before ref:          8 bytes
  + after ref:           8 bytes
  ─────────────────────────────
  TOTAL PER ENTRY:      60 bytes
```

## Scaling: 1000 Entries

```
LinkedHashMap instance:    76 bytes
Node[] table:
  Header:                16 bytes
  2048 slots:         8,192 bytes
  ──────────────────────────────────
  Table total:         8,208 bytes

head/tail pointers:        16 bytes

1000 Entry objects:
  1000 × 60 bytes:    60,000 bytes

1000 Integer keys:
  1000 × 16 bytes:    16,000 bytes

1000 Integer values:
  1000 × 16 bytes:    16,000 bytes

─────────────────────────────────────────
TOTAL:                 100,300 bytes ≈ 98 KB
```

## Comparison: LinkedHashMap vs HashMap (1000 Entries)

```
┌──────────────────┬──────────────┬──────────────┐
│                  │ LinkedHashMap│   HashMap    │
├──────────────────┼──────────────┼──────────────┤
│ Instance         │     76 B     │     56 B     │
│ Table            │   8,208 B    │   8,208 B    │
│ Nodes/Entries    │  60,000 B    │  44,000 B    │
│ Keys             │  16,000 B    │  16,000 B    │
│ Values           │  16,000 B    │  16,000 B    │
├──────────────────┼──────────────┼──────────────┤
│ TOTAL            │   ≈ 98 KB   │   ≈ 82 KB   │
│ Per-entry        │     60 B     │     44 B     │
│ Extra overhead   │   16 B/entry │     None     │
│ Ordered          │      Yes     │      No      │
└──────────────────┴──────────────┴──────────────┘
```

## Extra Memory for Linked List

```
Each entry needs 2 extra references:
  before: 8 bytes
  after:  8 bytes
  ─────────────────────
  Extra: 16 bytes per entry

For 1000 entries:
  HashMap:    44 × 1000 = 44,000 bytes
  LinkedHashMap: 60 × 1000 = 60,000 bytes
  Difference: 16,000 bytes = 16 KB
```

## Memory Layout

```
LinkedHashMap with 3 entries:

LinkedHashMap instance (76B):
┌──────────────────────────────────────────┐
│ header │ table │ head ──► Entry("C")      │
│ size=3 │       │ tail ──► Entry("B")      │
└──────────────────────────────────────────┘

Linked list:
head ⇄ "C" ⇄ "A" ⇄ "B" ⇄ tail

Node("C"):
┌──────────────────────────┐
│ hash, key="C", val=3     │
│ before=null, after→"A"   │
│ next=null (hash chain)   │
└──────────────────────────┘

Node("A"):
┌──────────────────────────┐
│ hash, key="A", val=1     │
│ before→"C", after→"B"    │
│ next=null (hash chain)   │
└──────────────────────────┘

Node("B"):
┌──────────────────────────┐
│ hash, key="B", val=2     │
│ before→"A", after=null   │
│ next=null (hash chain)   │
└──────────────────────────┘
```

## Empty LinkedHashMap

```
new LinkedHashMap<>()
  Instance:            76 bytes
  table: empty shared array
  head = null, tail = null
  Total:               76 bytes
```

## LRU Cache Memory

```
LRU cache with max 1000 entries:

LinkedHashMap instance:    76 bytes
Table (1024 slots):     4,112 bytes
Entries (1000):        60,000 bytes
Keys (1000):           16,000 bytes
Values (1000):         16,000 bytes
─────────────────────────────────────
TOTAL:                 96,188 bytes ≈ 94 KB

When entry evicted:
  - Entry object eligible for GC
  - Key/value objects eligible for GC
  - Linked list pointers updated
```

## Memory Optimization

```
1. Use HashMap if order doesn't matter:
   Saves 16 bytes per entry

2. Use accessOrder=true for LRU:
   Slightly more overhead but automatic eviction

3. Pre-size to avoid rehashing:
   new LinkedHashMap<>(expectedSize / 0.75 + 1)

4. Consider Caffeine/Guava caches for production:
   Better eviction policies, more features
```
