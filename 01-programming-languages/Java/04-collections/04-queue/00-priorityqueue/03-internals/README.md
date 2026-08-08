# PriorityQueue Internals

## Binary Min-Heap Structure

```
PriorityQueue uses a binary min-heap stored in an Object[] array.

The smallest element is always at index 0 (the root).
```

## How PriorityQueue Works

```
PriorityQueue<Integer> pq = new PriorityQueue<>();
pq.add(5);
pq.add(2);
pq.add(8);
pq.add(1);
pq.add(3);

Array representation:
index:  0    1    2    3    4
      [1]  [2]  [8]  [5]  [3]

Tree representation:
           1            ← root (index 0)
         /   \
        2     8         ← indices 1, 2
       / \
      5   3             ← indices 3, 4
```

## Array-to-Tree Mapping

```
For element at index i:
  Parent:     (i - 1) / 2
  Left child:  2 * i + 1
  Right child: 2 * i + 2

Example:
  Index 0: parent = (0-1)/2 = -1 (root, no parent)
  Index 1: parent = 0, left = 3, right = 4
  Index 2: parent = 0, left = 5, right = 6
```

## Heap Properties

```
1. Parent ≤ Children (min-heap)
2. Complete binary tree (filled left to right)
3. Array representation (no pointers needed)

Violation at index i:
  If array[i] > array[2*i+1] or array[i] > array[2*i+2]
  → sift down (restore heap property)
```

## Core Operations

### add(element) / offer(element)

```
1. Ensure capacity:  size + 1 > queue.length → grow()
2. Add element at index size
3. size++
4. siftUp(size - 1)  // Restore heap property

siftUp(index):
  while index > 0:
    parent = (index - 1) / 2
    if queue[index] < queue[parent]:
      swap(index, parent)
      index = parent
    else:
      break

Time: O(log n) — at most height of tree
```

### poll()

```
1. if size == 0: return null
2. Save root: result = queue[0]
3. Move last to root: queue[0] = queue[size-1]
4. size--
5. queue[size] = null  // help GC
6. siftDown(0)  // Restore heap property

siftDown(index):
  while true:
    left = 2 * index + 1
    right = 2 * index + 2
    smallest = index
    if left < size and queue[left] < queue[smallest]:
      smallest = left
    if right < size and queue[right] < queue[smallest]:
      smallest = right
    if smallest != index:
      swap(index, smallest)
      index = smallest
    else:
      break

Time: O(log n)
```

### peek()

```
1. if size == 0: return null
2. return queue[0]

Time: O(1) — root is always minimum
```

## Sift Operations Diagram

```
SIFT DOWN example:
Before: [1] [2] [8] [5] [3]
        Root is 1 (already min, no sift needed)

SIFT UP example:
Add 0 to [1] [2] [8] [5] [3]:
Step 1: [1] [2] [8] [5] [3] [0]
Step 2: 0 < 3 (parent), swap
        [1] [2] [8] [5] [0] [3]
Step 3: 0 < 2 (parent), swap
        [1] [0] [8] [5] [2] [3]
Step 4: 0 < 1 (parent), swap
        [0] [1] [8] [5] [2] [3]
Done: heap property restored
```

## Memory Layout Diagram

```
PriorityQueue instance:
┌──────────────────────────────────┐
│  Object header      (12 bytes)   │
│  int size            (4 bytes)   │
│  Comparator comp ref (8 bytes)   │
│  Object[] queue ref  (8 bytes)   │
└──────────────┬───────────────────┘
               │
               ▼
     ┌──────────────────────────────────────┐
     │  Object[] queue array                │
     │  ┌────┬────┬────┬────┬────┬────┐    │
     │  │ 1  │ 2  │ 8  │ 5  │ 3  │    │    │
     │  └────┴────┴────┴────┴────┴────┘    │
     │  index: 0  1  2  3  4               │
     └──────────────────────────────────────┘

Tree visualization:
         [1]           ← index 0
        /   \
      [2]   [8]        ← indices 1, 2
      / \
    [5] [3]            ← indices 3, 4
```

## Thread Safety

PriorityQueue is **not** synchronized. Concurrent access can cause:
- Corrupted heap structure
- Lost elements
- ConcurrentModificationException

Use `PriorityBlockingQueue` for concurrent access.

## Key Implementation Details

1. **Array-based heap** — No node objects, no pointers.
   Parent/child computed by index arithmetic.

2. **Min-heap** — Smallest element at root. Use Collections.reverseOrder()
   comparator for max-heap behavior.

3. **Not stable** — Equal elements have no guaranteed order.
   Priority is based solely on comparison.

4. **No random access** — Can only peek/poll the minimum.
   Cannot efficiently access arbitrary elements.

5. **Iterator order** — Not sorted! Returns elements in heap order
   (level-order traversal), not sorted order.

6. **initialCapacity** — Default 11. Grows by 2x when full.
   Pre-size if you know approximate element count.
