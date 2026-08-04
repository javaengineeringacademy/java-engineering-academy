# Heaps

## Table of Contents

- [Overview](#overview)
- [Heap Properties](#heap-properties)
  - [Min Heap](#min-heap)
  - [Max Heap](#max-heap)
- [Array Representation](#array-representation)
- [Heap Operations](#heap-operations)
- [Priority Queue](#priority-queue)
- [Heap Sort](#heap-sort)
- [Time Complexity](#time-complexity)
- [Use Cases](#use-cases)

---

## Overview

A heap is a complete binary tree that satisfies the heap property. It's typically implemented as an array for efficiency.

```
Min Heap:              Max Heap:
      1                     9
     / \                   / \
    3   2                 7   8
   / \ / \               / \ / \
  5  4 6   7             3  4 5   6
```

### Key Characteristics

- **Complete binary tree** - All levels filled except possibly last
- **Heap property** - Parent is smaller (min) or larger (max) than children
- **Array implementation** - No pointers needed
- **Efficient operations** - O(log n) insert/delete
- **No ordering between siblings** - Only parent-child relationship matters

---

## Heap Properties

### Min Heap

Every parent node is **less than or equal to** its children.

```
Min Heap Property:
parent ≤ left child
parent ≤ right child

      1              ✓ 1 ≤ 3, 1 ≤ 2
     / \             ✓ 3 ≤ 5, 3 ≤ 4
    3   2            ✓ 2 ≤ 6, 2 ≤ 7
   / \ / \
  5  4 6   7
```

### Max Heap

Every parent node is **greater than or equal to** its children.

```
Max Heap Property:
parent ≥ left child
parent ≥ right child

      9              ✓ 9 ≥ 7, 9 ≥ 8
     / \             ✓ 7 ≥ 3, 7 ≥ 4
    7   8            ✓ 8 ≥ 5, 8 ≥ 6
   / \ / \
  3  4 5   6
```

---

## Array Representation

Heap stored as array using level-order traversal:

```
Tree:              Array Index:
      1            Index: 0 1 2 3 4 5 6
     / \           Value:  1 3 2 5 4 6 7
    3   2          Parent of i: (i-1)/2
   / \ / \         Left child: 2i + 1
  5  4 6   7       Right child: 2i + 2
```

```python
class ArrayBasedHeap:
    def __init__(self):
        self._data = []

    @property
    def size(self) -> int:
        return len(self._data)

    def _parent(self, index: int) -> int:
        return (index - 1) // 2

    def _left_child(self, index: int) -> int:
        return 2 * index + 1

    def _right_child(self, index: int) -> int:
        return 2 * index + 2

    def _swap(self, i: int, j: int) -> None:
        self._data[i], self._data[j] = self._data[j], self._data[i]
```

---

## Heap Operations

### Insert (Push Up)

```python
class MinHeap:
    def __init__(self):
        self._data = []

    def push(self, value: int) -> None:
        self._data.append(value)
        self._sift_up(len(self._data) - 1)

    def _sift_up(self, index: int) -> None:
        """Move element up until heap property is restored."""
        while index > 0:
            parent = (index - 1) // 2
            if self._data[index] < self._data[parent]:
                self._data[index], self._data[parent] = self._data[parent], self._data[index]
                index = parent
            else:
                break

# Push operation visualization:
# Insert 1 into heap [2, 5, 7, 3]:

# Step 1: Add to end       Step 2: Sift up
#      2                        2
#     / \                      / \
#    5   7                    1   7   ← 1 < 5, swap
#   / \                      / \
#  3   1                    3   5
```

### Extract Min/Max (Push Down)

```python
    def pop(self) -> int:
        if not self._data:
            raise IndexError("Heap is empty")

        min_val = self._data[0]
        last = self._data.pop()

        if self._data:
            self._data[0] = last
            self._sift_down(0)

        return min_val

    def _sift_down(self, index: int) -> None:
        """Move element down until heap property is restored."""
        size = len(self._data)
        while True:
            smallest = index
            left = 2 * index + 1
            right = 2 * index + 2

            if left < size and self._data[left] < self._data[smallest]:
                smallest = left
            if right < size and self._data[right] < self._data[smallest]:
                smallest = right

            if smallest != index:
                self._data[index], self._data[smallest] = self._data[smallest], self._data[index]
                index = smallest
            else:
                break

# Pop operation visualization:
# Pop from [1, 3, 2, 5, 4]:

# Step 1: Remove 1        Step 2: Move 4 to root
#      3                     4
#     / \                   / \
#    4   2                 3   2
#   /                     /
#  5                     5

# Step 3: Sift down
#      3                 ← 3 < 4, swap with smaller child
#     / \
#    4   2
#   /
#  5
```

### Peek and Build

```python
    def peek(self) -> int:
        if not self._data:
            raise IndexError("Heap is empty")
        return self._data[0]

    @classmethod
    def heapify(cls, array: list) -> 'MinHeap':
        """Build heap from array in O(n)."""
        heap = cls()
        heap._data = array[:]
        for i in range(len(array) // 2 - 1, -1, -1):
            heap._sift_down(i)
        return heap

    def is_empty(self) -> bool:
        return len(self._data) == 0

    def __len__(self) -> int:
        return len(self._data)

    def __repr__(self) -> str:
        return f"MinHeap({self._data})"
```

---

## Priority Queue

Priority queue using heap - elements dequeued by priority.

```python
import heapq
from typing import Any, List, Tuple

class PriorityQueue:
    """Min-priority queue using heapq."""

    def __init__(self):
        self._heap: List[Tuple[int, int, Any]] = []
        self._index: int = 0

    def push(self, item: Any, priority: int) -> None:
        heapq.heappush(self._heap, (priority, self._index, item))
        self._index += 1

    def pop(self) -> Any:
        if not self._heap:
            raise IndexError("Priority queue is empty")
        priority, _, item = heapq.heappop(self._heap)
        return item

    def peek(self) -> Any:
        if not self._heap:
            raise IndexError("Priority queue is empty")
        return self._heap[0][2]

    def is_empty(self) -> bool:
        return len(self._heap) == 0

    def __len__(self) -> int:
        return len(self._heap)

class MaxPriorityQueue:
    """Max-priority queue using negated priorities."""

    def __init__(self):
        self._heap: List[Tuple[int, int, Any]] = []
        self._index: int = 0

    def push(self, item: Any, priority: int) -> None:
        heapq.heappush(self._heap, (-priority, self._index, item))
        self._index += 1

    def pop(self) -> Any:
        if not self._heap:
            raise IndexError("Priority queue is empty")
        _, _, item = heapq.heappop(self._heap)
        return item

    def peek(self) -> Any:
        if not self._heap:
            raise IndexError("Priority queue is empty")
        return self._heap[0][2]

# Usage
pq = PriorityQueue()
pq.push("Low priority", 3)
pq.push("High priority", 1)
pq.push("Medium priority", 2)

print(pq.pop())  # High priority
print(pq.pop())  # Medium priority
print(pq.pop())  # Low priority
```

---

## Heap Sort

Using heap to sort array in O(n log n).

```python
def heap_sort(arr: list) -> list:
    """Heap sort algorithm."""
    n = len(arr)

    # Build max heap
    for i in range(n // 2 - 1, -1, -1):
        max_heapify(arr, n, i)

    # Extract elements one by one
    for i in range(n - 1, 0, -1):
        arr[0], arr[i] = arr[i], arr[0]
        max_heapify(arr, i, 0)

    return arr

def max_heapify(arr: list, n: int, i: int) -> None:
    largest = i
    left = 2 * i + 1
    right = 2 * i + 2

    if left < n and arr[left] > arr[largest]:
        largest = left
    if right < n and arr[right] > arr[largest]:
        largest = right

    if largest != i:
        arr[i], arr[largest] = arr[largest], arr[i]
        max_heapify(arr, n, largest)

# Example
arr = [12, 11, 13, 5, 6, 7]
print(heap_sort(arr))  # [5, 6, 7, 11, 12, 13]
```

### Heap Sort Visualization

```
Array: [4, 10, 3, 5, 1]

Step 1: Build Max Heap
      10
     /  \
    5    3
   / \
  4   1

Array: [10, 5, 3, 4, 1]

Step 2: Swap root with last, sift down
Swap 10 and 1: [1, 5, 3, 4, 10]
Sift down 1:
      5
     / \
    4   3
   /
  1

Array: [5, 4, 3, 1, 10]

Repeat until sorted...
```

---

## Time Complexity

| Operation | Time | Space |
|-----------|------|-------|
| Insert (push) | O(log n) | O(1) |
| Extract min/max (pop) | O(log n) | O(1) |
| Peek (find min/max) | O(1) | O(1) |
| Build heap | O(n) | O(1) |
| Heap sort | O(n log n) | O(1) |
| Search | O(n) | O(1) |
| Delete arbitrary | O(n) | O(1) |

---

## Use Cases

| Use Case | Description |
|----------|-------------|
| Priority queue | Task scheduling, Dijkstra's algorithm |
| Heap sort | Efficient in-place sorting |
| Median finding | Two heaps approach |
| Top K elements | Find K largest/smallest |
| Merge K sorted | Merge multiple sorted streams |
| Event-driven simulation | Process events by time |
| Operating systems | Process scheduling |
| Network packets | QoS prioritization |

### Finding Median with Two Heaps

```python
class MedianFinder:
    def __init__(self):
        self.max_heap = []  # Lower half (inverted for max heap)
        self.min_heap = []  # Upper half

    def add_number(self, num: int) -> None:
        import heapq

        heapq.heappush(self.max_heap, -num)

        # Ensure max of lower half <= min of upper half
        if (self.min_heap and -self.max_heap[0] > self.min_heap[0]):
            val = -heapq.heappop(self.max_heap)
            heapq.heappush(self.min_heap, val)

        # Balance sizes
        if len(self.max_heap) > len(self.min_heap) + 1:
            val = -heapq.heappop(self.max_heap)
            heapq.heappush(self.min_heap, val)
        elif len(self.min_heap) > len(self.max_heap):
            val = heapq.heappop(self.min_heap)
            heapq.heappush(self.max_heap, -val)

    def find_median(self) -> float:
        if len(self.max_heap) > len(self.min_heap):
            return -self.max_heap[0]
        return (-self.max_heap[0] + self.min_heap[0]) / 2

# Usage
mf = MedianFinder()
for num in [5, 15, 1, 3]:
    mf.add_number(num)
    print(f"Added {num}, median: {mf.find_median()}")
# Added 5, median: 5
# Added 15, median: 10.0
# Added 1, median: 5
# Added 3, median: 4.0
```

---

## Summary

| Aspect | Description |
|--------|-------------|
| Structure | Complete binary tree (array) |
| Property | Parent ≤ children (min) or ≥ children (max) |
| Insert | O(log n) with sift up |
| Extract | O(log n) with sift down |
| Peek | O(1) |
| Best For | Priority-based processing, top K |
| Limitation | No efficient search, no ordering |
