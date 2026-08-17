# ArrayList Internals

## Backing Array Structure

```
transient Object[] elementData;
int size;
```

ArrayList is backed by a plain Object array. The `size` field tracks how many
elements are actually stored. Unused slots hold `null`.

## How ArrayList Works

```
index:    0       1       2       3       4       5       6       7
        +-------+-------+-------+-------+-------+-------+-------+-------+
elementData: |  "A"  |  "B"  |  "C"  |  "D"  | null  | null  | null  | null  |
        +-------+-------+-------+-------+-------+-------+-------+-------+
                                                     ↑
                                                    size = 4
```

## Growth Algorithm

```
DEFAULT_CAPACITY = 10
MAX_ARRAY_SIZE   = Integer.MAX_VALUE - 8   (2,147,483,639)

newCapacity = oldCapacity + (oldCapacity >> 1)   // 1.5x growth
```

### Growth Sequence

```
Capacity:  10 → 15 → 22 → 33 → 49 → 73 → 109 → ...
```

### Resize Process

```
BEFORE GROWTH (size=10, capacity=10):
+---+---+---+---+---+---+---+---+---+---+
| A | B | C | D | E | F | G | H | I | J |
+---+---+---+---+---+---+---+---+---+---+
                                                  Arrays.copyOf(elementData, 15)
AFTER GROWTH (size=10, capacity=15):
+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
| A | B | C | D | E | F | G | H | I | J | n | n | n | n | n |
+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
                                                      ↑
                                                     size=10, capacity=15
```

## Core Operations

### get(index)

```
Range check: index < size
Return: elementData[index]

Time: O(1) — direct array access
```

### add(element)

```
1. Ensure capacity:  size + 1 > elementData.length → grow()
2. elementData[size] = element
3. size++

Time: O(1) amortized (occasional O(n) copy during growth)
```

### add(index, element)

```
1. Range check
2. Ensure capacity
3. Shift elements right: System.arraycopy(elementData, index, elementData, index+1, size-index)
4. elementData[index] = element
5. size++

Time: O(n)
```

### remove(index)

```
1. Range check
2. numMoved = size - index - 1
3. If numMoved > 0: System.arraycopy(elementData, index+1, elementData, index, numMoved)
4. elementData[--size] = null   // GC help

Time: O(n)
```

## Memory Layout Diagram

```
ArrayList instance on heap:
┌─────────────────────────────────┐
│  Object header     (12 bytes)   │
│  int size          (4 bytes)    │
│  Object[] elementData (8 bytes) │
└──────────────┬──────────────────┘
               │
               ▼
     ┌─────────────────────────────────────────┐
     │  Object[] backing array                 │
     │  ┌─────────────────────────────────┐    │
     │  │ [0] → Element1 ──→ heap object  │    │
     │  │ [1] → Element2 ──→ heap object  │    │
     │  │ [2] → Element3 ──→ heap object  │    │
     │  │ ...                              │    │
     │  │ [n-1] → ElementN ──→ heap obj   │    │
     │  │ [n] null                         │    │
     │  │ ...                              │    │
     │  │ [capacity-1] null                │    │
     │  └─────────────────────────────────┘    │
     └─────────────────────────────────────────┘
```

## Key Implementation Details

1. **transient keyword** — `elementData` is not serialized directly.
   Custom `writeObject`/`readObject` handle serialization.

2. **elementData[size] = null** — After removal, the old reference is cleared
   to prevent memory leaks and help the garbage collector.

3. **MAX_ARRAY_SIZE** — Arrays can't exceed `Integer.MAX_VALUE - 8` because
   some VMs reserve header words in arrays.

4. **trimToSize()** — Copies `elementData` to a new array of exactly `size`
   length, releasing unused slots.

5. **subList()** — Returns a view backed by the same `elementData` array.
   Structural modifications to the parent invalidate the sub-list.

## Thread Safety

ArrayList is **not** synchronized. Concurrent access from multiple threads
can cause:
- ArrayIndexOutOfBoundsException
- Lost updates
- Corrupted size

Use `Collections.synchronizedList()` or `CopyOnWriteArrayList` for threads.
