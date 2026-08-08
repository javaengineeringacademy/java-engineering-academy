# CopyOnWriteArrayList Internals

## Snapshot Array Structure

```java
private transient volatile Object[] array;
```

The array reference is volatile, ensuring visibility across threads.

## How CopyOnWriteArrayList Works

```
READ PATH (no lock):
┌──────────┐     ┌──────────────────────────┐
│  Thread  │     │  CopyOnWriteArrayList     │
│  reader  │────►│  array ──────────────────────► [A] [B] [C] [D]
└──────────┘     │  (volatile reference)     │   (current snapshot)
                 └──────────────────────────┘
  Direct array access, no synchronization

WRITE PATH (copy + modify + swap):
┌──────────┐     ┌──────────────────────────┐
│  Thread  │     │  CopyOnWriteArrayList     │
│  writer  │────►│  array ──► [A] [B] [C] [D]  (original)
└──────────┘     └──────────────────────────┘
                          │
                          ▼  1. Copy array
                 ┌──────────────────────┐
                 │ [A] [B] [C] [D] [E]  │  (new copy)
                 └──────────────────────┘
                          │
                          ▼  2. Modify copy
                 ┌──────────────────────┐
                 │ [A] [B] [C] [D] [E]  │  (modified)
                 └──────────────────────┘
                          │
                          ▼  3. Swap volatile reference
                 ┌──────────────────────────┐
                 │  array ──► [A] [B] [C] [D] [E]  (new snapshot)
                 └──────────────────────────┘
```

## Iterator Snapshot Semantics

```java
CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
list.add("A");
list.add("B");
list.add("C");

Iterator<String> it = list.iterator();  // Snapshot: [A, B, C]

list.add("D");                          // Modifies array
list.remove("B");                       // Modifies array

// Iterator still sees: A, B, C (snapshot at creation)
while (it.hasNext()) {
    System.out.println(it.next());      // Prints A, B, C
}
```

```
Timeline:
  t1: list = [A, B, C]
  t2: iterator created → snapshot = [A, B, C]
  t3: list.add("D") → list = [A, B, C, D]
  t4: list.remove("B") → list = [A, C, D]
  t5: iterator.next() → still returns from [A, B, C]
```

## Core Operations

### get(index)

```
1. return array[index]     // Direct array access
2. No lock needed

Time: O(1)
```

### add(element)

```
1. synchronized(this) {
2.   Object[] oldArray = array
3.   Object[] newArray = Arrays.copyOf(oldArray, oldArray.length + 1)
4.   newArray[oldArray.length] = element
5.   array = newArray       // Volatile write
6. }

Time: O(n) — full array copy
```

### remove(index)

```
1. synchronized(this) {
2.   Object[] oldArray = array
3.   int numMoved = oldArray.length - index - 1
4.   Object[] newArray = new Object[oldArray.length - 1]
5.   System.arraycopy(oldArray, 0, newArray, 0, index)
6.   System.arraycopy(oldArray, index+1, newArray, index, numMoved)
7.   array = newArray
8. }

Time: O(n) — full array copy
```

### set(index, element)

```
1. synchronized(this) {
2.   Object[] oldArray = array
3.   Object[] newArray = Arrays.copyOf(oldArray, oldArray.length)
4.   newArray[index] = element
5.   array = newArray
6. }

Time: O(n) — full array copy
```

## Memory Layout Diagram

```
CopyOnWriteArrayList instance:
┌──────────────────────────────────┐
│  Object header     (12 bytes)    │
│  Object[] array ref (8 bytes)    │  ← volatile
└──────────────┬───────────────────┘
               │
               ▼
     Current array snapshot:
     ┌────┬────┬────┬────┐
     │ A  │ B  │ C  │ D  │
     └────┴────┴────┴────┘

During write operation (concurrent):
     Old snapshot:
     ┌────┬────┬────┬────┐
     │ A  │ B  │ C  │ D  │  ← Readers still see this
     └────┴────┴────┴────┘

     New snapshot (being created):
     ┌────┬────┬────┬────┬────┐
     │ A  │ B  │ C  │ D  │ E  │  ← Writer creates this
     └────┴────┴────┴────┴────┘

     After swap:
     Old: [A][B][C][D]     ← eligible for GC
     array ref ──► [A][B][C][D][E]  ← new current
```

## Thread Safety Model

```
Readers:  No lock, no synchronization
          - Reads are always consistent with some snapshot
          - Never blocked by writers

Writers:  Synchronized, copy-on-write
          - Only one writer at a time
          - Readers never block

Trade-off:
  ✓ Read performance: Excellent (no contention)
  ✗ Write performance: Poor (full copy every time)
  ✓ Iterator consistency: Snapshot semantics
```

## Key Implementation Details

1. **volatile array** — The `array` reference is volatile, ensuring that
   when a writer publishes a new array, all threads see it immediately.

2. **synchronized writes** — Only write operations are synchronized.
   Multiple readers can read concurrently without any locking.

3. **Snapshot iterators** — Iterators hold a reference to the array at
   creation time. They never see modifications made after creation.

4. **Thread-safe without Collections.synchronizedList** — No need to
   wrap in synchronizedList. The class handles thread safety internally.

5. **Best for read-heavy workloads** — When reads >> writes, COWAL
   provides excellent concurrent performance.

6. **No consistency guarantees across operations** — Each get() is
   independent. Two consecutive reads may return different data.
