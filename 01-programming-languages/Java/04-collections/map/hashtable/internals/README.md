# Hashtable Internals

## Legacy Synchronized Map

```
Hashtable is a legacy synchronized Map from JDK 1.0.

private transient Entry<?,?>[] table;
private int count;
```

## How Hashtable Works

```
Same bucket structure as HashMap but synchronized:

Entry[] table:
┌────┬────┬────┬────┬────┬────┬────┬────┐
│  0 │  1 │  2 │  3 │  4 │  5 │  6 │  7 │
└──┬─┴────┴────┴──┬─┴────┴────┴────┴────┘
   │              │
   ▼              ▼
 ┌──────┐      ┌──────┐
 │Entry │      │Entry │
 │ A=1  │      │ B=2  │
 │ next─┼──►   │ next │
 └──────┘      └──────┘
```

## Entry Structure

```
static class Entry<K,V> {
    int hash;
    K key;
    V value;
    Entry<K,V> next;
}

Same as HashMap.Node but in Hashtable class.
```

## Growth Algorithm

```
initialCapacity = 11 (default)
loadFactor = 0.75f

newCapacity = 2 * oldCapacity + 1

Growth sequence:
  11 → 23 → 47 → 95 → 191 → 383 → 767 → 1535 → ...
```

### Growth Example

```
Initial: capacity=11, threshold=8 (11 × 0.75)

After 8 inserts:
  count = 8, threshold = 8
  Next insert triggers resize

After resize:
  capacity = 23
  threshold = 17 (23 × 0.75)
```

## Thread Safety: Synchronized Methods

```java
public synchronized V put(K key, V value) { ... }
public synchronized V get(Object key) { ... }
public synchronized V remove(Object key) { ... }
public synchronized int size() { ... }
```

### Monitor Lock

```
Thread 1: put("A", 1)  ──► Acquires lock ──► Executes ──► Releases
Thread 2: get("A")     ──► Waits for lock ──────────────► Acquires → Executes
Thread 3: remove("B")  ──► Waits for lock ──────────────────────────► Acquires
```

## Null Restrictions

```
Hashtable does NOT allow null keys or values:

  put(null, "value")   → NullPointerException
  put("key", null)     → NullPointerException
  containsKey(null)    → NullPointerException

HashMap allows:
  put(null, "value")   → OK (null key at index 0)
  put("key", null)     → OK
```

## Core Operations

### put(key, value)

```
1. synchronized(this)
2. if key == null: throw NullPointerException
3. Compute index: hash(key) % table.length
4. Traverse chain at index
5. If key exists: replace value
6. Else: prepend new Entry
7. if ++count > threshold: rehash()

Time: O(1) amortized
Lock: held during entire operation
```

### get(key)

```
1. synchronized(this)
2. if key == null: throw NullPointerException
3. Compute index: hash(key) % table.length
4. Traverse chain
5. Return value if key.equals(entry.key)

Time: O(1) average
Lock: held during operation
```

### containsKey(key)

```
1. synchronized(this)
2. Find entry with key
3. Return true if found

Time: O(1) average
```

## Rehash Process

```
BEFORE (capacity=3, entries at 0,2):
table: [ A→C ] [   ] [ B ]
         ↓              ↓
        Node("A",1)  Node("B",2)
        Node("C",3)

AFTER rehash (capacity=7):
table: [   ] [ A ] [   ] [   ] [ B ] [   ] [ C ]
                ↓              ↓              ↓
           Node("A",1)   Node("B",2)   Node("C",3)

All entries rehash to new positions.
```

## Memory Layout Diagram

```
Hashtable instance:
┌──────────────────────────────────────┐
│  Object header         (12 bytes)    │
│  Entry[] table ref      (8 bytes)    │
│  int count              (4 bytes)    │
│  int threshold          (4 bytes)    │
│  float loadFactor       (4 bytes)    │
│  int modCount           (4 bytes)    │
│  float loadFactor       (4 bytes)    │
│  Padding                (4 bytes)    │
└──────────────┬───────────────────────┘
               │
               ▼
     ┌──────────────────────────────────────┐
     │  Entry[] table                       │
     │  ┌────┬────┬────┬────┬────┬────┐    │
     │  │  0 │  1 │  2 │  3 │  4 │  5 │    │
     │  └──┬─┴────┴────┴──┬─┴────┴────┘    │
     │     │              │                 │
     │     ▼              ▼                 │
     │  ┌──────┐      ┌──────┐             │
     │  │Entry │      │Entry │             │
     │  │ A=1  │      │ B=2  │             │
     │  │ next─┼──►   │ next │             │
     │  └──────┘      └──────┘             │
     └──────────────────────────────────────┘
```

## Hashtable vs HashMap

```
┌─────────────────┬────────────────┬────────────────┐
│ Feature         │   Hashtable    │    HashMap     │
├─────────────────┼────────────────┼────────────────┤
│ Thread safety   │  Synchronized  │  Not safe      │
│ Null keys       │  Not allowed   │  1 allowed     │
│ Null values     │  Not allowed   │  Allowed       │
│ Growth          │  2x + 1        │  2x            │
│ Initial cap     │  11            │  16            │
│ Hash function   │  hash(k) % n   │  hash(k) & n-1 │
│ Legacy          │  Since 1.0     │  Since 1.2     │
│ Recommended     │  No            │  Yes           │
└─────────────────┴────────────────┴────────────────┘
```

## Legacy Status

Hashtable is a legacy class from JDK 1.0:
- All methods synchronized → unnecessary overhead
- Growth 2x+1 wastes more memory
- Does not allow null keys/values
- Use HashMap + Collections.synchronizedMap() if needed
- Or better: use ConcurrentHashMap

## Key Implementation Details

1. **Synchronized everything** — Every public method acquires monitor
   lock. Single lock for entire map.

2. **No null** — NullPointerException on null key or value.
   This is a design choice from JDK 1.0.

3. **Modulo hashing** — Uses `hash % table.length` instead of
   bitwise AND. Slower than HashMap's approach.

4. **Enumeration** — Uses Enumeration (legacy Iterator) for
   entrySet(), keySet(), values().

5. **Thread-safe iteration** — Uses Enumeration which is snapshot-based.
   ConcurrentModificationException is possible but less likely.

6. **Properties integration** — Hashtable is used by System.getProperties()
   and ResourceBundle. This is its primary remaining use.
