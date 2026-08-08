# HashMap Memory Usage

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
│  Padding                         12B   │
├────────────────────────────────────────┤
│  TOTAL INSTANCE:                 56B  │
└────────────────────────────────────────┘
```

## Bucket Array Overhead

```
Header:   16 bytes
Per slot: 4 bytes (reference to Node or null)
```

## Per-Entry Overhead

```
HashMap.Node structure:
┌────────────────────────────────────────┐
│  Object header (Mark + Klass)     12B  │
│  int hash                         4B   │
│  K key ref                        8B   │
│  V value ref                      8B   │
│  Node next ref                    8B   │
│  Padding                          4B   │
├────────────────────────────────────────┤
│  TOTAL PER NODE:                 44B  │
└────────────────────────────────────────┘
```

## Scaling: 1000 Entries

```
HashMap instance:              56 bytes
Node[] table:
  Header:                    16 bytes
  2048 slots (at 0.75): 8,192 bytes
  ──────────────────────────────────
  Table total:            8,208 bytes

1000 Node objects:
  1000 × 44 bytes:       44,000 bytes

1000 Integer keys:
  1000 × 16 bytes:       16,000 bytes

1000 Integer values:
  1000 × 16 bytes:       16,000 bytes

─────────────────────────────────────────
TOTAL:                     84,264 bytes ≈ 82 KB
```

## Empty HashMap

```
new HashMap<>()
  Instance:            56 bytes
  table: empty shared array (EMPTY_TABLE)
  Total:               56 bytes
```

## Memory Layout

```
HashMap with 3 entries:

HashMap instance (56B):
┌──────────────────────────────────────────┐
│ header │ table ref ──► Node[]             │
│ size=3 │ threshold=12                     │
└──────────────────────────────────────────┘

Node[] table (128 bytes for capacity 32):
┌────┬────┬────┬────┬────┬─ ─ ─┬────┐
│ 0  │ 1  │ 2  │ 3  │ 4  │ ... │ 31 │
└──┬─┴────┴────┴──┬─┴────┴─ ─ ─┴────┘
   │              │
   ▼              ▼
┌──────┐      ┌──────┐
│"key1"│      │"key3"│
│val=1 │      │val=3 │
│next  │      │next  │
└──────┘      └──────┘
```

## Comparison: HashMap vs TreeMap (1000 Entries)

```
┌──────────────────┬──────────────┬──────────────┐
│                  │   HashMap    │   TreeMap    │
├──────────────────┼──────────────┼──────────────┤
│ Instance         │     56 B     │     48 B     │
│ Table/Structure  │   8,208 B    │      None    │
│ Nodes/Entries    │  44,000 B    │  60,000 B    │
│ Keys             │  16,000 B    │  16,000 B    │
│ Values           │  16,000 B    │  16,000 B    │
├──────────────────┼──────────────┼──────────────┤
│ TOTAL            │   ≈ 82 KB   │   ≈ 92 KB   │
│ get/put          │     O(1)     │   O(log n)   │
│ Sorted           │      No      │      Yes     │
└──────────────────┴──────────────┴──────────────┘
```

## Comparison: HashMap vs ArrayMap (1000 Entries)

```
┌──────────────────┬──────────────┬──────────────┐
│                  │   HashMap    │   ArrayMap   │
├──────────────────┼──────────────┼──────────────┤
│ Instance         │     56 B     │     32 B     │
│ Structure        │   8,208 B    │   8,016 B    │
│ Nodes            │  44,000 B    │      None    │
│ Keys + Values    │  32,000 B    │  32,000 B    │
├──────────────────┼──────────────┼──────────────┤
│ TOTAL            │   ≈ 82 KB   │   ≈ 40 KB   │
│ get/put          │     O(1)     │   O(log n)   │
│ Best for         │  Large maps  │  Small maps  │
└──────────────────┴──────────────┴──────────────┘
```

## Load Factor Impact

```
Load Factor 0.5:
  capacity=2048, threshold=1024
  Table: 8,208 bytes
  More empty slots, fewer collisions

Load Factor 0.75 (default):
  capacity=2048, threshold=1536
  Table: 8,208 bytes
  Balanced

Load Factor 1.0:
  capacity=2048, threshold=2048
  Table: 8,208 bytes
  Fewer empty slots, more collisions
```

## Table Wastage

```
At 0.75 load factor:
  25% of slots are null (empty)
  1000 entries → 2048 slots → 512 null slots × 4 = 2,048 bytes wasted

After clear():
  table array remains allocated
  Wasted: 8,192 bytes until GC or new put()
```

## Growth Pattern

```
Capacity: 16 → 32 → 64 → 128 → 256 → 512 → 1024 → 2048

Each resize:
  - Allocates new array (double size)
  - Copies all entries (rehash)
  - Old array becomes garbage

Memory during resize:
  Old array: capacity × 4 + header
  New array: capacity × 8 + header
  Peak: ~1.5x normal usage
```
