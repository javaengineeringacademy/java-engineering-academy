# Hashtable Memory Usage

## Per-Instance Overhead

```
┌────────────────────────────────────────┐
│  Object header (Mark + Klass)     12B  │
│  Entry[] table ref                8B   │
│  int count                        4B   │
│  int threshold                    4B   │
│  float loadFactor                 4B   │
│  int modCount                     4B   │
│  Padding                         12B   │
├────────────────────────────────────────┤
│  TOTAL INSTANCE:                 48B  │
└────────────────────────────────────────┘
```

## Per-Entry Overhead

```
Hashtable.Entry structure:
┌────────────────────────────────────────┐
│  Object header (Mark + Klass)     12B  │
│  int hash                         4B   │
│  K key ref                        8B   │
│  V value ref                      8B   │
│  Entry next ref                   8B   │
│  Padding                          4B   │
├────────────────────────────────────────┤
│  TOTAL PER ENTRY:                 44B  │
└────────────────────────────────────────┘
```

## Scaling: 1000 Entries

```
Hashtable instance:            48 bytes
Entry[] table:
  Header:                    16 bytes
  1535 slots (2x+1 growth): 6,140 bytes
  ──────────────────────────────────
  Table total:             6,156 bytes

1000 Entry objects:
  1000 × 44 bytes:        44,000 bytes

1000 Integer keys:
  1000 × 16 bytes:        16,000 bytes

1000 Integer values:
  1000 × 16 bytes:        16,000 bytes

─────────────────────────────────────────
TOTAL:                     82,204 bytes ≈ 80 KB
```

## Comparison: Hashtable vs HashMap (1000 Entries)

```
┌──────────────────┬──────────────┬──────────────┐
│                  │  Hashtable   │   HashMap    │
├──────────────────┼──────────────┼──────────────┤
│ Instance         │     48 B     │     56 B     │
│ Table slots      │   1,535      │   2,048      │
│ Table size       │   6,156 B    │   8,208 B    │
│ Entries          │  44,000 B    │  44,000 B    │
│ Keys             │  16,000 B    │  16,000 B    │
│ Values           │  16,000 B    │  16,000 B    │
├──────────────────┼──────────────┼──────────────┤
│ TOTAL            │   ≈ 80 KB   │   ≈ 82 KB   │
│ Thread safe      │     Yes      │      No      │
│ Null keys/values │      No      │     Yes      │
│ Sync overhead    │  Every op    │     None     │
└──────────────────┴──────────────┴──────────────┘
```

## Synchronization Overhead

```
Per synchronized method call:
  Monitor enter:  ~20-30 ns
  Monitor exit:   ~10-20 ns
  ────────────────────────────
  Total overhead: ~30-50 ns per operation

For 1000 sequential operations:
  HashMap:     ~50 µs total
  Hashtable:   ~500 µs total (10x slower)
```

## Growth Pattern

```
Hashtable capacity: 11 → 23 → 47 → 95 → 191 → 383 → 767 → 1535

HashMap capacity:  16 → 32 → 64 → 128 → 256 → 512 → 1024 → 2048

At size 1000:
  Hashtable capacity: 1535 (wasted: 535 slots × 4 = 2,140 bytes)
  HashMap capacity:   2048 (wasted: 1048 slots × 4 = 4,192 bytes)
```

## Empty Hashtable

```
new Hashtable<>()
  Instance:            48 bytes
  table: empty shared array
  count: 0
  Total:               48 bytes
```

## Memory Layout

```
Hashtable with 3 entries:

Hashtable instance (48B):
┌──────────────────────────────────────────┐
│ header │ table ref ──► Entry[]            │
│ count=3│ threshold=8                      │
└──────────────────────────────────────────┘

Entry[] table (88 bytes for capacity 23):
┌────┬────┬────┬────┬────┬─ ─ ─┬────┐
│ 0  │ 1  │ 2  │ 3  │ 4  │ ... │ 22 │
└──┬─┴────┴────┴──┬─┴────┴─ ─ ─┴────┘
   │              │
   ▼              ▼
┌──────┐      ┌──────┐
│Entry │      │Entry │
│ A=1  │      │ B=2  │
│ next─┼──►   │ next │
└──────┘      └──────┘
```

## Recommendation

```
┌──────────────────────────────────────────────────┐
│  Hashtable: Legacy, synchronized, no nulls       │
│  HashMap:   Modern, faster, allows nulls         │
│                                                  │
│  For thread safety:                              │
│    1. ConcurrentHashMap (best)                   │
│    2. Collections.synchronizedMap()              │
│    3. Hashtable (avoid)                          │
└──────────────────────────────────────────────────┘
```
