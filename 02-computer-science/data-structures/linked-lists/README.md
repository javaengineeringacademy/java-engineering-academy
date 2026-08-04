# Linked Lists

## Table of Contents

- [Overview](#overview)
- [Types of Linked Lists](#types-of-linked-lists)
  - [Singly Linked List](#singly-linked-list)
  - [Doubly Linked List](#doubly-linked-list)
  - [Circular Linked List](#circular-linked-list)
- [Operations](#operations)
- [Time Complexity](#time-complexity)
- [Memory Management](#memory-management)
- [Use Cases](#use-cases)
- [Comparison with Arrays](#comparison-with-arrays)
- [Advanced Topics](#advanced-topics)

---

## Overview

A linked list is a linear data structure where elements are stored in nodes. Each node contains data and a reference (pointer) to the next node.

```
Singly Linked List:
┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐
│ 10 | ●──┼───►│ 20 | ●──┼───►│ 30 | ●──┼───►│ 40 |null│
└─────────┘    └─────────┘    └─────────┘    └─────────┘
   HEAD
```

### Key Characteristics

- **Dynamic size** - grows/shrinks at runtime
- **Non-contiguous memory** - nodes can be anywhere
- **Efficient insertion/deletion** - O(1) at known positions
- **No random access** - must traverse from head
- **Extra memory** - for storing pointers/references

---

## Types of Linked Lists

### Singly Linked List

Each node points to the next node only.

```python
from typing import Any, Optional, Iterator

class Node:
    def __init__(self, data: Any, next_node: Optional['Node'] = None):
        self.data = data
        self.next = next_node

class SinglyLinkedList:
    def __init__(self):
        self.head: Optional[Node] = None
        self._size: int = 0

    def __len__(self) -> int:
        return self._size

    def __iter__(self) -> Iterator[Any]:
        current = self.head
        while current:
            yield current.data
            current = current.next

    def append(self, data: Any) -> None:
        new_node = Node(data)
        if not self.head:
            self.head = new_node
        else:
            current = self.head
            while current.next:
                current = current.next
            current.next = new_node
        self._size += 1

    def prepend(self, data: Any) -> None:
        new_node = Node(data, self.head)
        self.head = new_node
        self._size += 1

    def insert_at(self, index: int, data: Any) -> None:
        if index < 0 or index > self._size:
            raise IndexError("Index out of bounds")
        if index == 0:
            self.prepend(data)
            return
        new_node = Node(data)
        current = self.head
        for _ in range(index - 1):
            current = current.next
        new_node.next = current.next
        current.next = new_node
        self._size += 1

    def delete(self, data: Any) -> bool:
        if not self.head:
            return False
        if self.head.data == data:
            self.head = self.head.next
            self._size -= 1
            return True
        current = self.head
        while current.next:
            if current.next.data == data:
                current.next = current.next.next
                self._size -= 1
                return True
            current = current.next
        return False

    def search(self, data: Any) -> Optional[int]:
        current = self.head
        index = 0
        while current:
            if current.data == data:
                return index
            current = current.next
            index += 1
        return None

    def __repr__(self) -> str:
        nodes = [str(x) for x in self]
        return " -> ".join(nodes) + " -> None"

# Usage
ll = SinglyLinkedList()
for i in [1, 2, 3, 4, 5]:
    ll.append(i)
print(ll)  # 1 -> 2 -> 3 -> 4 -> 5 -> None

ll.prepend(0)
print(ll)  # 0 -> 1 -> 2 -> 3 -> 4 -> 5 -> None

ll.insert_at(3, 99)
print(ll)  # 0 -> 1 -> 2 -> 99 -> 3 -> 4 -> 5 -> None

ll.delete(99)
print(ll)  # 0 -> 1 -> 2 -> 3 -> 4 -> 5 -> None
```

### Doubly Linked List

Each node has pointers to both next and previous nodes.

```python
from typing import Any, Optional, Iterator

class DoublyNode:
    def __init__(self, data: Any, prev: Optional['DoublyNode'] = None,
                 next_node: Optional['DoublyNode'] = None):
        self.data = data
        self.prev = prev
        self.next = next_node

class DoublyLinkedList:
    def __init__(self):
        self.head: Optional[DoublyNode] = None
        self.tail: Optional[DoublyNode] = None
        self._size: int = 0

    def __len__(self) -> int:
        return self._size

    def __iter__(self) -> Iterator[Any]:
        current = self.head
        while current:
            yield current.data
            current = current.next

    def append(self, data: Any) -> None:
        new_node = DoublyNode(data)
        if not self.head:
            self.head = self.tail = new_node
        else:
            new_node.prev = self.tail
            self.tail.next = new_node
            self.tail = new_node
        self._size += 1

    def prepend(self, data: Any) -> None:
        new_node = DoublyNode(data)
        if not self.head:
            self.head = self.tail = new_node
        else:
            new_node.next = self.head
            self.head.prev = new_node
            self.head = new_node
        self._size += 1

    def delete(self, data: Any) -> bool:
        current = self.head
        while current:
            if current.data == data:
                if current.prev:
                    current.prev.next = current.next
                else:
                    self.head = current.next

                if current.next:
                    current.next.prev = current.prev
                else:
                    self.tail = current.prev

                self._size -= 1
                return True
            current = current.next
        return False

    def reverse(self) -> None:
        current = self.head
        self.tail = current
        while current:
            current.prev, current.next = current.next, current.prev
            self.head = current
            current = current.prev  # Note: prev is now the old next

    def __repr__(self) -> str:
        nodes = [str(x) for x in self]
        return "None <-> " + " <-> ".join(nodes) + " <-> None"
```

### Circular Linked List

Last node points back to first node.

```python
class CircularLinkedList:
    def __init__(self):
        self.head: Optional[Node] = None
        self._size: int = 0

    def append(self, data: Any) -> None:
        new_node = Node(data)
        if not self.head:
            self.head = new_node
            new_node.next = self.head  # Points to itself
        else:
            current = self.head
            while current.next != self.head:
                current = current.next
            current.next = new_node
            new_node.next = self.head  # Complete the circle
        self._size += 1

    def display(self) -> list:
        if not self.head:
            return []
        result = [self.head.data]
        current = self.head.next
        while current != self.head:
            result.append(current.data)
            current = current.next
        return result

    def __len__(self) -> int:
        return self._size
```

```
Circular Linked List:
┌─────────┐    ┌─────────┐    ┌─────────┐
│ 10 | ●──┼───►│ 20 | ●──┼───►│ 30 | ●──┤
└─────────┘    └─────────┘    └─────────┘
     ▲                           │
     └───────────────────────────┘
```

---

## Operations

### Insertion

```python
# Insert after given node - O(1)
def insert_after(node: Node, data: Any) -> None:
    new_node = Node(data, node.next)
    node.next = new_node

# Insert at head - O(1)
def insert_at_head(head: Optional[Node], data: Any) -> Node:
    return Node(data, head)

# Insert at tail - O(n) for singly, O(1) for doubly with tail pointer
def insert_at_tail(head: Optional[Node], data: Any) -> Node:
    new_node = Node(data)
    if not head:
        return new_node
    current = head
    while current.next:
        current = current.next
    current.next = new_node
    return head
```

### Deletion

```python
# Delete by value - O(n)
def delete_node(head: Optional[Node], data: Any) -> Optional[Node]:
    if not head:
        return None
    if head.data == data:
        return head.next
    current = head
    while current.next:
        if current.next.data == data:
            current.next = current.next.next
            return head
        current = current.next
    return head

# Delete at position - O(n)
def delete_at_position(head: Optional[Node], pos: int) -> Optional[Node]:
    if pos == 0:
        return head.next if head else None
    current = head
    for _ in range(pos - 1):
        if not current or not current.next:
            return head
        current = current.next
    if current.next:
        current.next = current.next.next
    return head
```

### Reversal

```python
# Iterative reversal - O(n) time, O(1) space
def reverse(head: Optional[Node]) -> Optional[Node]:
    prev = None
    current = head
    while current:
        next_node = current.next
        current.next = prev
        prev = current
        current = next_node
    return prev

# Recursive reversal - O(n) time, O(n) space (stack)
def reverse_recursive(head: Optional[Node]) -> Optional[Node]:
    if not head or not head.next:
        return head
    new_head = reverse_recursive(head.next)
    head.next.next = head
    head.next = None
    return new_head
```

### Finding Middle Node

```python
# Slow and fast pointer technique - O(n)
def find_middle(head: Optional[Node]) -> Optional[Node]:
    slow = fast = head
    while fast and fast.next:
        slow = slow.next
        fast = fast.next.next
    return slow
```

### Detecting Cycle

```python
# Floyd's Cycle Detection - O(n)
def has_cycle(head: Optional[Node]) -> bool:
    slow = fast = head
    while fast and fast.next:
        slow = slow.next
        fast = fast.next.next
        if slow == fast:
            return True
    return False
```

---

## Time Complexity

| Operation | Singly | Doubly | Circular |
|-----------|--------|--------|----------|
| Access by index | O(n) | O(n) | O(n) |
| Search | O(n) | O(n) | O(n) |
| Insert at head | O(1) | O(1) | O(1)* |
| Insert at tail | O(n) | O(1) | O(n) |
| Insert after node | O(1) | O(1) | O(1) |
| Delete at head | O(1) | O(1) | O(1)* |
| Delete at tail | O(n) | O(1) | O(n) |
| Delete by value | O(n) | O(n) | O(n) |
| Space per element | 1 pointer | 2 pointers | 1 pointer |

*With tail pointer or circular traversal

---

## Memory Management

```
Linked List in Memory:
┌─────────────────────────────────────────────┐
│  Node 1 (addr: 1000)                        │
│  ┌─────────┬───────────┐                    │
│  │ data=10 │ next=2048 │────────┐           │
│  └─────────┴───────────┘        │           │
│                                 ▼           │
│  Node 2 (addr: 2048)                        │
│  ┌─────────┬───────────┐                    │
│  │ data=20 │ next=1536 │────────┐           │
│  └─────────┴───────────┘        │           │
│                                 ▼           │
│  Node 3 (addr: 1536)                        │
│  ┌─────────┬───────────┐                    │
│  │ data=30 │ next=null │                    │
│  └─────────┴───────────┘                    │
└─────────────────────────────────────────────┘

vs. Array in Memory:
┌─────────────────────────────────────────────┐
│  Contiguous Block (addr: 3000)              │
│  ┌─────────┬─────────┬─────────┐            │
│  │   10    │   20    │   30    │            │
│  └─────────┴─────────┴─────────┘            │
│  3000    3004    3008    3012               │
└─────────────────────────────────────────────┘
```

### Advantages

- Dynamic memory allocation
- No wasted space (exactly what's needed)
- Efficient insertion/deletion

### Disadvantages

- Memory overhead for pointers
- Poor cache locality
- External fragmentation possible

---

## Use Cases

| Use Case | Why Linked List |
|----------|-----------------|
| Implementation of stacks/queues | Easy insertion/deletion |
| Undo functionality | Maintain history |
| Music playlist | Sequential access, dynamic size |
| Browser back/forward | Doubly linked list |
| Hash table chaining | Handle collisions |
| Large file operations | Sequential access pattern |
| Polynomial arithmetic | Variable number of terms |
| Memory manager | Free block management |

---

## Comparison with Arrays

| Feature | Array | Linked List |
|---------|-------|-------------|
| Memory layout | Contiguous | Non-contiguous |
| Size | Fixed or dynamic | Always dynamic |
| Access | O(1) random | O(n) sequential |
| Search | O(log n) sorted | O(n) |
| Insert at beginning | O(n) | O(1) |
| Insert at end | O(1) amortized | O(1)* |
| Delete at beginning | O(n) | O(1) |
| Delete at end | O(1) | O(n)** |
| Memory overhead | None | Pointer(s) per node |
| Cache performance | Excellent | Poor |
| Fragmentation | Possible | More likely |

*Doubly linked with tail pointer
**Singly linked without tail pointer

---

## Advanced Topics

### Skip List

```python
import random

class SkipNode:
    def __init__(self, data: Any, level: int):
        self.data = data
        self.forward = [None] * (level + 1)

class SkipList:
    def __init__(self, max_level: int = 16, p: float = 0.5):
        self.max_level = max_level
        self.p = p
        self.header = SkipNode(-1, max_level)
        self.level = 0

    def random_level(self) -> int:
        lvl = 0
        while random.random() < self.p and lvl < self.max_level:
            lvl += 1
        return lvl

    def insert(self, data: Any) -> None:
        update = [None] * (self.max_level + 1)
        current = self.header

        for i in range(self.level, -1, -1):
            while current.forward[i] and current.forward[i].data < data:
                current = current.forward[i]
            update[i] = current

        level = self.random_level()
        if level > self.level:
            for i in range(self.level + 1, level + 1):
                update[i] = self.header
            self.level = level

        new_node = SkipNode(data, level)
        for i in range(level + 1):
            new_node.forward[i] = update[i].forward[i]
            update[i].forward[i] = new_node

    def search(self, data: Any) -> bool:
        current = self.header
        for i in range(self.level, -1, -1):
            while current.forward[i] and current.forward[i].data < data:
                current = current.forward[i]
        current = current.forward[0]
        return current is not None and current.data == data
```

### XOR Linked List

Memory-efficient doubly linked list using XOR of addresses.

```python
# Conceptual - requires actual memory addresses
class XORNode:
    def __init__(self, data: Any):
        self.data = data
        self.xor = 0  # XOR of prev and next addresses

# addr(next) = addr(prev) XOR node.xor
# Allows traversal in both directions with single pointer
```

---

## Summary

| Type | Best For | Trade-off |
|------|----------|-----------|
| Singly | Simple sequences, stacks | No backward traversal |
| Doubly | Deques, browser history | Extra memory per node |
| Circular | Round-robin, playlists | Complexity in termination |
| Skip List | Sorted data, O(log n) search | More memory, complexity |
