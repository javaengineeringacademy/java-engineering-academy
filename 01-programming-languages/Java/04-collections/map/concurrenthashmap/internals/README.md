# ConcurrentHashMap Internals

## CAS + Synchronized Per Node

```
ConcurrentHashMap uses fine-grained locking:
- CAS operations for simple updates
- Synchronized per Node for complex operations
- No full-table locking

static class Node<K,V> {
    final int hash;
    final K key;
    volatile V val;
    volatile Node<K,V> next;
}
```

## How ConcurrentHashMap Works

```
ConcurrentHashMap<Integer, String> map = new ConcurrentHashMap<>();
map.put(5, "E");
map.put(3, "C");
map.put(7, "G");

Thread-safe without full locking:
  Thread 1: put(1, "A")  → CAS on bucket[1]
  Thread 2: get(5)       → no lock, read volatile
  Thread 3: put(4, "D")  → CAS on bucket[4]
  All concurrent, no blocking
```

## CAS Operations

```
CAS = Compare-And-Swap (atomic hardware instruction)

CAS(table, index, expected, update):
  if table[index] == expected:
    table[index] = update
    return true
  else:
    return false

Used for:
  - Adding new nodes (if bucket is null)
  - Updating value (if node exists)
  - Size updates (baseCount + CounterCell)
```

## Synchronized Per Node

```
When CAS fails (concurrent modification):
  synchronized(node) {
    // Modify node safely
  }

Only ONE thread can modify a node at a time.
Other threads can still read other nodes.
```

## Node Structure

```
ConcurrentHashMap.Node:
┌────────────────────────────────────────┐
│  Object header (Mark + Klass)     12B  │
│  int hash                         4B   │
│  K key ref                        8B   │
│  volatile V val ref               8B   │
│  volatile Node next ref           8B   │
│  Padding                          4B   │
├────────────────────────────────────────┤
│  TOTAL PER NODE:                 44B  │
└────────────────────────────────────────┘
```

## Volatile Fields for Visibility

```
volatile val:
  Thread 1: put(key, "A") → writes "A"
  Thread 2: get(key)      → reads "A" (guaranteed to see latest)

volatile next:
  Thread 1: adds new node to chain
  Thread 2: sees new node immediately

No memory barriers needed for reads.
```

## Core Operations

### put(key, value)

```
1. Compute index: hash(key) & (n-1)
2. if table[i] == null:
     CAS table[i] to new Node
   else:
     synchronized(table[i])
     traverse chain
     add/update node
3. if ++size > threshold: helpResize()

Time: O(1) amortized
```

### get(key)

```
1. Compute index: hash(key) & (n-1)
2. Read table[i] (volatile read)
3. Traverse chain (volatile reads)
4. Return value

Time: O(1) — NO LOCKS
```

### remove(key)

```
1. Compute index
2. Find node
3. CAS to mark node as deleted
4. If CAS fails: synchronized(node) and retry

Time: O(1) amortized
```

## Size Tracking

```
ConcurrentHashMap uses distributed counting:

baseCount (long):
  Updated via CAS for uncontended cases

CounterCell[]:
  Each thread increments its own cell
  Size = baseCount + sum(counterCells)

  Thread 1 → cell[0] += 1
  Thread 2 → cell[1] += 1
  Thread 3 → cell[2] += 1
  Total = baseCount + cell[0] + cell[1] + cell[2]
```

## Resize Process

```
ConcurrentHashMap supports concurrent resize:

1. Multiple threads help transfer entries
2. Each thread processes a range of buckets
3. Old and new tables coexist during resize
4. Readers can still access old table

  Thread 1: transfer buckets 0-7
  Thread 2: transfer buckets 8-15
  Thread 3: transfer buckets 16-23
  All concurrent, no locking
```

## Memory Layout Diagram

```
ConcurrentHashMap instance:
┌──────────────────────────────────────────┐
│  Object header           (12 bytes)      │
│  volatile Node[] table ref (8 bytes)     │
│  volatile int sizeCtl     (4 bytes)      │
│  volatile long baseCount  (8 bytes)      │
│  CounterCell[] counterCells (8 bytes)    │
│  int transferIndex        (4 bytes)      │
│  int nCells               (4 bytes)      │
│  int cellBusy             (4 bytes)      │
│  Node<K,V>[] nextTable    (8 bytes)      │
│  long baseCount           (8 bytes)      │
│  int sizeCtl              (4 bytes)      │
│  Padding                  (4 bytes)      │
└──────────────┬───────────────────────────┘
               │
               ▼
     ┌──────────────────────────────────────────┐
     │  volatile Node[] table                    │
     │  ┌────┬────┬────┬────┬────┬────┬────┐   │
     │  │  0 │  1 │  2 │  3 │  4 │  5 │... │   │
     │  └──┬─┴────┴────┴──┬─┴────┴────┴────┘   │
     │     │              │                     │
     │     ▼              ▼                     │
     │  ┌──────┐      ┌──────┐                 │
     │  │ Node │      │ Node │                 │
     │  │ A=1  │      │ B=2  │                 │
     │  │ next─┼──►   │ next │                 │
     │  └──────┘      └──────┘                 │
     └──────────────────────────────────────────┘
```

## Thread Safety Model

```
Readers:  No locks, volatile reads
          - Always see latest value
          - No blocking

Writers:  CAS or per-node synchronized
          - Only one writer per node
          - Other nodes unaffected

Resizers: Multiple threads help
          - Range-based transfer
          - No full-stop pauses
```

## Key Implementation Details

1. **No null keys or values** — ConcurrentHashMap does not allow null.
   Null signals "not found" in get().

2. **CAS for simple cases** — When bucket is null, CAS adds node
   without locking.

3. **Synchronized for complex cases** — When chain exists, synchronize
   on the first node.

4. **Volatile reads** — get() is lock-free. Reads see latest value
   due to volatile semantics.

5. **Size is approximate** — size() returns approximate count.
   Use mappingCount() for large maps.

6. **Bulk operations** — forEach(), search(), reduce() can run
   in parallel across segments.

7. **compute()** — Atomic compute-if-absent/update operations
   that hold lock on specific node.
