# WeakHashMap Internals

## WeakReference Keys

```
WeakHashMap uses WeakReference for keys.
Entries are eligible for GC when key has no strong references.

static class Entry<K,V> extends WeakReference<Object> {
    V value;
    int hash;
    Entry<K,V> next;
}
```

## How WeakHashMap Works

```
WeakHashMap<String, Integer> map = new WeakHashMap<>();
String key = new String("hello");
map.put(key, 42);

Memory:
  Strong ref: key ──► String("hello") ◄── WeakRef: Entry
                                        ──► value: Integer(42)

When key = null (no strong references):
  GC runs → String("hello") collected → Entry removed from map
```

## WeakReference Behavior

```
Strong reference:    String key = "hello";  // prevents GC
Soft reference:      SoftReference<String>  // GC last resort
Weak reference:      WeakReference<String>  // GC when no strong refs
Phantom reference:   PhantomReference       // post-mortem cleanup

WeakHashMap uses WeakReference for keys:
  - Entry extends WeakReference<Object>
  - key is the referent
  - When referent is GC'd, entry is enqueued
```

## ReferenceQueue for Cleanup

```
WeakHashMap maintains a ReferenceQueue:

1. Key becomes weakly reachable
2. GC enqueues WeakReference in ReferenceQueue
3. expungeStaleEntries() called on next operation
4. Stale entries removed from hash table

┌──────────────┐    GC     ┌──────────────────┐
│ WeakHashMap  │──────────►│ ReferenceQueue   │
│              │           │ (stale entries)  │
│ Entry A ─────┼──weak──► │ Object A (enqueued)│
│ Entry B ─────┼──weak──► │ Object B (enqueued)│
└──────────────┘           └──────────────────┘
        │
        ▼ expungeStaleEntries()
  Remove Entry A and Entry B from table
```

## Entry Structure

```
WeakHashMap.Entry extends WeakReference<Object>:

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
```

## Core Operations

### put(key, value)

```
1. expungeStaleEntries()  // cleanup first
2. Compute index: hash(key) & (table.length - 1)
3. Traverse chain at index
4. If key exists: replace value
5. Else: create new Entry, add to chain
6. if ++size > threshold: resize()

Time: O(1) amortized
```

### get(key)

```
1. expungeStaleEntries()
2. Compute index
3. Traverse chain
4. If key.equals(entry.get()): return value
5. Return null

Time: O(1) average
```

### remove(key)

```
1. expungeStaleEntries()
2. Find entry
3. Remove from chain
4. size--

Time: O(1)
```

### expungeStaleEntries()

```
Called on every put/get/remove:

1. while (entry = queue.poll()) != null:
2.   Find entry in table
3.   Remove from chain
4.   Resize if needed

Time: O(k) where k = stale entries
```

## Memory Layout Diagram

```
WeakHashMap instance:
┌──────────────────────────────────────┐
│  Object header         (12 bytes)    │
│  Entry[] table ref      (8 bytes)    │
│  int size               (4 bytes)    │
│  ReferenceQueue ref     (8 bytes)    │
│  int threshold          (4 bytes)    │
│  float loadFactor       (4 bytes)    │
│  int modCount           (4 bytes)    │
│  Padding                (8 bytes)    │
└──────────────┬───────────────────────┘
               │
               ▼
     ┌──────────────────────────────────────────┐
     │  Entry[] table                            │
     │  ┌────┬────┬────┬────┬────┬────┬────┐   │
     │  │  0 │  1 │  2 │  3 │  4 │  5 │... │   │
     │  └──┬─┴────┴────┴──┬─┴────┴────┴────┘   │
     │     │              │                     │
     │     ▼              ▼                     │
     │  ┌──────────┐   ┌──────────┐            │
     │  │ Entry    │   │ Entry    │            │
     │  │ weak(key)│   │ weak(key)│            │
     │  │ value=1  │   │ value=2  │            │
     │  │ next ────┼──►│ next     │            │
     │  └──────────┘   └──────────┘            │
     └──────────────────────────────────────────┘
               │
               ▼
     ┌──────────────────────────────────────────┐
     │  ReferenceQueue                           │
     │  (holds entries whose keys were GC'd)    │
     └──────────────────────────────────────────┘
```

## Thread Safety

WeakHashMap is **not** synchronized. Concurrent access can cause:
- Corrupted hash table
- ConcurrentModificationException
- Memory leaks from stale entries

## Key Implementation Details

1. **WeakReference keys** — Keys are held via WeakReference.
   When no strong reference exists, key is GC'd.

2. **Automatic cleanup** — Stale entries removed on next operation
   via expungeStaleEntries().

3. **Use case: caches** — Perfect for metadata caches where keys
   are transient objects.

4. **No null keys** — WeakHashMap does not allow null keys
   (WeakReference(null) is invalid).

5. **Identity-based cleanup** — Only keys that are no longer
   strongly reachable are collected.

6. **Performance** — Slightly slower than HashMap due to
   ReferenceQueue processing.

7. **Iteration** — Iterator may see stale entries and remove them
   during iteration. Size may change.
