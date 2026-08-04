# Queues

## Table of Contents

- [Overview](#overview)
- [Types of Queues](#types-of-queues)
  - [Simple Queue](#simple-queue)
  - [Circular Queue](#circular-queue)
  - [Double-Ended Queue (Deque)](#double-ended-queue-deque)
  - [Priority Queue](#priority-queue)
- [Implementations](#implementations)
- [Time Complexity](#time-complexity)
- [Applications](#applications)

---

## Overview

A queue is a linear data structure that follows the **FIFO (First In, First Out)** principle. Elements are added at the rear and removed from the front.

```
Queue Operations:

  enqueue(1)  enqueue(2)  enqueue(3)
      │           │           │
      ▼           ▼           ▼
  ┌───────┬───────┬───────┬───────┐
  │   1   │   2   │   3   │       │
  └───────┴───────┴───────┴───────┘
    FRONT                       REAR

  dequeue() removes 1 (front)
  enqueue(4) adds to rear (4)

  ┌───────┬───────┬───────┬───────┐
  │   2   │   3   │   4   │       │
  └───────┴───────┴───────┴───────┘
    FRONT                   REAR
```

### Key Characteristics

- **FIFO ordering** - First element added is first removed
- **Enqueue** - Add to rear
- **Dequeue** - Remove from front
- **Peek/Front** - View front without removing

---

## Types of Queues

### Simple Queue

```python
from typing import Any, Optional

class SimpleQueue:
    def __init__(self):
        self._data = []

    def enqueue(self, item: Any) -> None:
        self._data.append(item)

    def dequeue(self) -> Any:
        if self.is_empty():
            raise IndexError("Queue is empty")
        return self._data.pop(0)  # O(n) operation!

    def front(self) -> Any:
        if self.is_empty():
            raise IndexError("Queue is empty")
        return self._data[0]

    def rear(self) -> Any:
        if self.is_empty():
            raise IndexError("Queue is empty")
        return self._data[-1]

    def is_empty(self) -> bool:
        return len(self._data) == 0

    def size(self) -> int:
        return len(self._data)

    def __repr__(self) -> str:
        return f"Queue({self._data})"
```

### Circular Queue

Uses a fixed-size array with wraparound to avoid wasted space.

```
Circular Queue (capacity=5):

State 1:        State 2:        State 3:
┌───┬───┬───┐   ┌───┬───┬───┐   ┌───┬───┬───┐
│ A │ B │ C │   │ D │ B │ C │   │ D │ E │ C │
└───┴───┴───┘   └───┴───┴───┘   └───┴───┴───┘
  0   1   2       3   4   0       4   0   1
  ↑       ↑       ↑       ↑           ↑
 front  rear    front  rear        front rear

rear = (rear + 1) % capacity
front = (front + 1) % capacity
```

```python
class CircularQueue:
    def __init__(self, capacity: int):
        self._capacity = capacity
        self._data = [None] * capacity
        self._front = 0
        self._rear = -1
        self._size = 0

    def enqueue(self, item: Any) -> None:
        if self.is_full():
            raise OverflowError("Queue is full")
        self._rear = (self._rear + 1) % self._capacity
        self._data[self._rear] = item
        self._size += 1

    def dequeue(self) -> Any:
        if self.is_empty():
            raise IndexError("Queue is empty")
        item = self._data[self._front]
        self._front = (self._front + 1) % self._capacity
        self._size -= 1
        return item

    def front(self) -> Any:
        if self.is_empty():
            raise IndexError("Queue is empty")
        return self._data[self._front]

    def is_empty(self) -> bool:
        return self._size == 0

    def is_full(self) -> bool:
        return self._size == self._capacity

    def size(self) -> int:
        return self._size
```

### Double-Ended Queue (Deque)

Elements can be added or removed from both ends.

```python
from typing import Any

class Deque:
    def __init__(self):
        self._data = []

    def add_front(self, item: Any) -> None:
        self._data.insert(0, item)

    def add_rear(self, item: Any) -> None:
        self._data.append(item)

    def remove_front(self) -> Any:
        if self.is_empty():
            raise IndexError("Deque is empty")
        return self._data.pop(0)

    def remove_rear(self) -> Any:
        if self.is_empty():
            raise IndexError("Deque is empty")
        return self._data.pop()

    def peek_front(self) -> Any:
        if self.is_empty():
            raise IndexError("Deque is empty")
        return self._data[0]

    def peek_rear(self) -> Any:
        if self.is_empty():
            raise IndexError("Deque is empty")
        return self._data[-1]

    def is_empty(self) -> bool:
        return len(self._data) == 0

    def size(self) -> int:
        return len(self._data)

    def __repr__(self) -> str:
        return f"Deque({self._data})"

# Usage
deque = Deque()
deque.add_rear(1)      # [1]
deque.add_rear(2)      # [1, 2]
deque.add_front(0)     # [0, 1, 2]
deque.remove_rear()    # Returns 2, [0, 1]
deque.remove_front()   # Returns 0, [1]
```

### Priority Queue

Elements are dequeued based on priority, not insertion order.

```python
import heapq
from typing import Any, List, Tuple

class PriorityQueue:
    def __init__(self):
        self._heap: List[Tuple[int, Any]] = []
        self._index: int = 0

    def push(self, item: Any, priority: int) -> None:
        heapq.heappush(self._heap, (priority, self._index, item))
        self._index += 1

    def pop(self) -> Any:
        if self.is_empty():
            raise IndexError("Priority queue is empty")
        priority, _, item = heapq.heappop(self._heap)
        return item

    def peek(self) -> Any:
        if self.is_empty():
            raise IndexError("Priority queue is empty")
        return self._heap[0][2]

    def is_empty(self) -> bool:
        return len(self._heap) == 0

    def size(self) -> int:
        return len(self._heap)

# Usage
pq = PriorityQueue()
pq.push("low priority task", 3)
pq.push("high priority task", 1)
pq.push("medium priority task", 2)

print(pq.pop())  # high priority task (priority 1)
print(pq.pop())  # medium priority task (priority 2)
print(pq.pop())  # low priority task (priority 3)
```

---

## Implementations

### Linked List Queue

```python
class Node:
    def __init__(self, data, next_node=None):
        self.data = data
        self.next = next_node

class LinkedQueue:
    def __init__(self):
        self._front = None
        self._rear = None
        self._size = 0

    def enqueue(self, item) -> None:
        new_node = Node(item)
        if self.is_empty():
            self._front = self._rear = new_node
        else:
            self._rear.next = new_node
            self._rear = new_node
        self._size += 1

    def dequeue(self):
        if self.is_empty():
            raise IndexError("Queue is empty")
        item = self._front.data
        self._front = self._front.next
        if self._front is None:
            self._rear = None
        self._size -= 1
        return item

    def is_empty(self) -> bool:
        return self._front is None

    def size(self) -> int:
        return self._size
```

---

## Time Complexity

| Operation | Array | Circular | Linked List | Deque |
|-----------|-------|----------|-------------|-------|
| Enqueue (rear) | O(1)* | O(1) | O(1) | O(1) |
| Enqueue (front) | O(n) | O(n) | O(1) | O(1) |
| Dequeue (front) | O(n) | O(1) | O(1) | O(1) |
| Dequeue (rear) | O(1) | O(1) | O(n) | O(1) |
| Peek | O(1) | O(1) | O(1) | O(1) |
| Search | O(n) | O(n) | O(n) | O(n) |

*Amortized for dynamic arrays

---

## Applications

| Application | Queue Type | Description |
|-------------|------------|-------------|
| CPU scheduling | Simple | Round-robin scheduling |
| Print spooling | Simple | Jobs processed in order |
| BFS traversal | Simple | Level-order tree/graph traversal |
| Buffering | Circular | Streaming, I/O buffers |
| Sliding window | Deque | Maximum in window |
| Job scheduling | Priority | Task prioritization |
| Dijkstra's algorithm | Priority | Shortest path |
| Operating systems | Multiple | Process scheduling |

### BFS Example

```python
from collections import deque
from typing import List, Dict, Set

def bfs(graph: Dict[str, List[str]], start: str) -> List[str]:
    """Breadth-First Search using queue."""
    visited: Set[str] = set()
    queue = deque([start])
    visited.add(start)
    result = []

    while queue:
        vertex = queue.popleft()
        result.append(vertex)

        for neighbor in graph[vertex]:
            if neighbor not in visited:
                visited.add(neighbor)
                queue.append(neighbor)

    return result

# Example
graph = {
    'A': ['B', 'C'],
    'B': ['D', 'E'],
    'C': ['F'],
    'D': [],
    'E': ['F'],
    'F': []
}
print(bfs(graph, 'A'))  # ['A', 'B', 'C', 'D', 'E', 'F']
```

---

## Summary

| Type | Best For | Trade-off |
|------|----------|-----------|
| Simple Queue | Basic FIFO | Wasted space |
| Circular Queue | Fixed-size buffers | No dynamic sizing |
| Deque | Both-end operations | Slightly more complex |
| Priority Queue | Priority-based processing | No FIFO guarantee |
