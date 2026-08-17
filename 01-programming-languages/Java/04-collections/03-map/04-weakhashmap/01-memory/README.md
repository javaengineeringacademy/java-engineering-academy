# WeakHashMap Memory Usage

## Per-Instance Overhead

```
┌────────────────────────────────────────┐
│  Object header (Mark + Klass)     12B  │
│  Entry[] table ref                 8B   │
│  int size                         4B   │
│  ReferenceQueue ref               8B   │
│  int threshold                    4B   │
│  float loadFactor                 4B   │
│  int modCount                     4B   │
│  Padding                         12B   │
├────────────────────────────────────────┤
│  TOTAL INSTANCE:                 56B  │
└────────────────────────────────────────┘
```

## Per-Entry Overhead

```
WeakHashMap.Entry (extends WeakReference):
┌────────────────────────────────────────┐
│  Object header (Mark + Klass)     12B  │
│  Object referent (weak key)       8B   │
│  V value                          8B   │
│  int hash                         4B   │
│  Entry next ref                   8B   │
│  Padding                          4B   │
├────────────────────────────────────────┤
│  TOTAL PER ENTRY:                 44B  │
└────────────────────────────────────────┘

WeakReference overhead:
  - ReferenceQueue ref: 8 bytes
  - GC enqueue overhead: variable
```

## Scaling: 1000 Entries

```
WeakHashMap instance:         56 bytes
ReferenceQueue:               16 bytes

Entry[] table:
  Header:                    16 bytes
  2048 slots (at 0.75): 8,192 bytes
  ──────────────────────────────────
  Table total:            8,208 bytes

1000 Entry objects:
  1000 × 44 bytes:       44,000 bytes

1000 Integer keys (weak refs):
  1000 × 16 bytes:       16,000 bytes

1000 Integer values:
  1000 × 16 bytes:       16,000 bytes

─────────────────────────────────────────
TOTAL:                     84,296 bytes ≈ 82 KB
```

## Comparison: WeakHashMap vs HashMap (1000 Entries)

```
┌──────────────────┬──────────────┬──────────────┐
│                  │ WeakHashMap  │   HashMap    │
├──────────────────┼──────────────┼──────────────┤
│ Instance         │     56 B     │     56 B     │
│ ReferenceQueue   │     16 B     │      None    │
│ Table            │   8,208 B    │   8,208 B    │
│ Entries          │  44,000 B    │  44,000 B    │
│ Keys             │  16,000 B    │  16,000 B    │
│ Values           │  16,000 B    │  16,000 B    │
├──────────────────┼──────────────┼──────────────┤
│ TOTAL            │   ≈ 82 KB   │   ≈ 82 KB   │
│ Keys GC'd        │     Yes      │      No      │
│ Auto cleanup     │     Yes      │      No      │
└──────────────────┴──────────────┴──────────────┘
```

## Memory Behavior Over Time

```
Initial (1000 entries):
  TOTAL: ≈ 82 KB

After key GC (500 keys collected):
  Entries: 500 × 44 = 22,000 bytes
  Keys: 500 × 16 = 8,000 bytes (weak refs still exist)
  Values: 500 × 16 = 8,000 bytes
  TOTAL: ≈ 42 KB

After expungeStaleEntries():
  Entries: 500 × 44 = 22,000 bytes
  Keys: 0 (removed)
  Values: 0 (removed)
  TOTAL: ≈ 30 KB
```

## WeakReference vs StrongReference

```
Strong reference:
  String key = "hello";
  map.put(key, value);
  // key prevents GC of "hello"
  // Entry stays in map forever

Weak reference:
  String key = new String("hello");
  map.put(key, value);
  key = null;  // no more strong references
  // GC can collect "hello"
  // Entry removed on next operation
```

## Memory During GC

```
When GC runs:
  1. Finds entries with unreachable keys
  2. Enqueues WeakReferences in ReferenceQueue
  3. WeakHashMap.expungeStaleEntries() removes them

Memory freed:
  - Entry objects: 44 bytes each
  - Key objects: 16 bytes each (if collected)
  - Value objects: 16 bytes each
  ──────────────────────────────
  Total per entry: 76 bytes freed

For 1000 collected entries:
  76,000 bytes freed
```

## Empty WeakHashMap

```
new WeakHashMap<>()
  Instance:            56 bytes
  ReferenceQueue:      16 bytes
  table: empty shared array
  Total:               72 bytes
```

## Memory Optimization

```
1. Pre-size to avoid rehashing:
   new WeakHashMap<>(expectedSize / 0.75 + 1)

2. Use WeakReference keys intentionally:
   For caches, metadata, temporary associations

3. Consider ReferenceQueue processing:
   Manual cleanup if you need immediate memory release

4. Alternatives:
   - Caffeine cache with weak keys (better eviction)
   - Guava CacheBuilder with weakKeys()
```
