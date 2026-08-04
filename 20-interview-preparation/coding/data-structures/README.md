# Data Structures for Coding Interviews

Master essential data structures for technical interviews.

## Overview

Data structures are fundamental building blocks for solving algorithmic problems. Understanding their characteristics, use cases, and trade-offs is crucial for coding interviews.

## Linear Data Structures

### Arrays

**Characteristics:**
- Contiguous memory allocation
- O(1) random access
- O(n) insertion/deletion
- Fixed or dynamic size

**Use Cases:**
- Storing sequential data
- Implementing stacks and queues
- Lookup tables
- Inverse permutation arrays

```python
# Array Operations
class DynamicArray:
    def __init__(self):
        self.capacity = 2
        self.size = 0
        self.data = [None] * self.capacity

    def append(self, value):
        if self.size == self.capacity:
            self._resize(2 * self.capacity)
        self.data[self.size] = value
        self.size += 1

    def _resize(self, new_capacity):
        new_data = [None] * new_capacity
        for i in range(self.size):
            new_data[i] = self.data[i]
        self.data = new_data
        self.capacity = new_capacity

    def get(self, index):
        if index < 0 or index >= self.size:
            raise IndexError("Index out of bounds")
        return self.data[index]
```

**Common Problems:**
- Two Sum
- Best Time to Buy and Sell Stock
- Contains Duplicate
- Product of Array Except Self

### Linked Lists

**Characteristics:**
- Non-contiguous memory
- O(1) insertion/deletion at head
- O(n) access by index
- Dynamic size

**Types:**
- Singly Linked List
- Doubly Linked List
- Circular Linked List

```python
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

class LinkedList:
    def __init__(self):
        self.head = None

    def append(self, val):
        if not self.head:
            self.head = ListNode(val)
            return
        current = self.head
        while current.next:
            current = current.next
        current.next = ListNode(val)

    def prepend(self, val):
        new_node = ListNode(val)
        new_node.next = self.head
        self.head = new_node

    def delete(self, val):
        if not self.head:
            return
        if self.head.val == val:
            self.head = self.head.next
            return
        current = self.head
        while current.next:
            if current.next.val == val:
                current.next = current.next.next
                return
            current = current.next
```

**Common Problems:**
- Reverse Linked List
- Merge Two Sorted Lists
- Linked List Cycle
- Remove Nth Node From End

### Stacks

**Characteristics:**
- LIFO (Last In, First Out)
- O(1) push and pop
- Used for recursion, undo operations

```python
class Stack:
    def __init__(self):
        self.items = []

    def push(self, item):
        self.items.append(item)

    def pop(self):
        if not self.is_empty():
            return self.items.pop()
        raise IndexError("Stack is empty")

    def peek(self):
        if not self.is_empty():
            return self.items[-1]
        raise IndexError("Stack is empty")

    def is_empty(self):
        return len(self.items) == 0

    def size(self):
        return len(self.items)
```

**Common Problems:**
- Valid Parentheses
- Min Stack
- Implement Queue using Stacks
- Daily Temperatures

### Queues

**Characteristics:**
- FIFO (First In, First Out)
- O(1) enqueue and dequeue
- Used for BFS, scheduling

```python
from collections import deque

class Queue:
    def __init__(self):
        self.items = deque()

    def enqueue(self, item):
        self.items.append(item)

    def dequeue(self):
        if not self.is_empty():
            return self.items.popleft()
        raise IndexError("Queue is empty")

    def front(self):
        if not self.is_empty():
            return self.items[0]
        raise IndexError("Queue is empty")

    def is_empty(self):
        return len(self.items) == 0
```

**Common Problems:**
- Implement Stack using Queues
- Sliding Window Maximum
- Rotting Oranges
- Open the Lock

## Non-Linear Data Structures

### Hash Maps

**Characteristics:**
- O(1) average lookup/insert/delete
- Key-value pairs
- Unordered
- Collision handling required

```python
class HashMap:
    def __init__(self, size=16):
        self.size = size
        self.buckets = [[] for _ in range(size)]

    def _hash(self, key):
        return hash(key) % self.size

    def put(self, key, value):
        index = self._hash(key)
        bucket = self.buckets[index]
        for i, (k, v) in enumerate(bucket):
            if k == key:
                bucket[i] = (key, value)
                return
        bucket.append((key, value))

    def get(self, key):
        index = self._hash(key)
        bucket = self.buckets[index]
        for k, v in bucket:
            if k == key:
                return v
        raise KeyError(key)

    def delete(self, key):
        index = self._hash(key)
        bucket = self.buckets[index]
        for i, (k, v) in enumerate(bucket):
            if k == key:
                del bucket[i]
                return
        raise KeyError(key)
```

**Common Problems:**
- Two Sum
- Group Anagrams
- Longest Consecutive Sequence
- LRU Cache

### Trees

**Binary Tree:**
```python
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right
```

**Binary Search Tree:**
```python
class BST:
    def __init__(self):
        self.root = None

    def insert(self, val):
        self.root = self._insert(self.root, val)

    def _insert(self, node, val):
        if not node:
            return TreeNode(val)
        if val < node.val:
            node.left = self._insert(node.left, val)
        elif val > node.val:
            node.right = self._insert(node.right, val)
        return node

    def search(self, val):
        return self._search(self.root, val)

    def _search(self, node, val):
        if not node or node.val == val:
            return node
        if val < node.val:
            return self._search(node.left, val)
        return self._search(node.right, val)
```

**Tree Traversals:**
```python
def inorder(node):
    if node:
        inorder(node.left)
        print(node.val)
        inorder(node.right)

def preorder(node):
    if node:
        print(node.val)
        preorder(node.left)
        preorder(node.right)

def postorder(node):
    if node:
        postorder(node.left)
        postorder(node.right)
        print(node.val)

def level_order(root):
    if not root:
        return []
    result = []
    queue = [root]
    while queue:
        level = []
        for _ in range(len(queue)):
            node = queue.pop(0)
            level.append(node.val)
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
        result.append(level)
    return result
```

**Common Problems:**
- Maximum Depth of Binary Tree
- Validate Binary Search Tree
- Lowest Common Ancestor
- Binary Tree Level Order Traversal

### Heaps

**Characteristics:**
- Complete binary tree
- Min-heap: parent <= children
- Max-heap: parent >= children
- O(log n) insert/delete
- O(1) find min/max

```python
import heapq

class MinHeap:
    def __init__(self):
        self.heap = []

    def push(self, val):
        heapq.heappush(self.heap, val)

    def pop(self):
        return heapq.heappop(self.heap)

    def peek(self):
        return self.heap[0] if self.heap else None

    def size(self):
        return len(self.heap)
```

**Common Problems:**
- Kth Largest Element
- Find Median from Data Stream
- Merge K Sorted Lists
- Top K Frequent Elements

### Graphs

**Representations:**
```python
# Adjacency List
graph = {
    'A': ['B', 'C'],
    'B': ['A', 'D'],
    'C': ['A', 'D'],
    'D': ['B', 'C']
}

# Adjacency Matrix
matrix = [
    [0, 1, 1, 0],
    [1, 0, 0, 1],
    [1, 0, 0, 1],
    [0, 1, 1, 0]
]
```

**BFS:**
```python
from collections import deque

def bfs(graph, start):
    visited = set()
    queue = deque([start])
    visited.add(start)

    while queue:
        node = queue.popleft()
        for neighbor in graph[node]:
            if neighbor not in visited:
                visited.add(neighbor)
                queue.append(neighbor)
```

**DFS:**
```python
def dfs(graph, node, visited=None):
    if visited is None:
        visited = set()
    visited.add(node)
    for neighbor in graph[node]:
        if neighbor not in visited:
            dfs(graph, neighbor, visited)
    return visited
```

**Common Problems:**
- Number of Islands
- Course Schedule
- Clone Graph
- Word LII

## Data Structure Selection Guide

| Problem Type | Recommended Data Structure |
|--------------|---------------------------|
| Fast lookup by key | Hash Map |
| Sorted data | BST, Heap |
| LIFO operations | Stack |
| FIFO operations | Queue |
| Priority processing | Heap |
| Hierarchical data | Tree |
| Relationships | Graph |
| Sequential access | Array, Linked List |

## Practice Tips

1. **Understand Trade-offs**: Know time/space complexity
2. **Visualize**: Draw the data structure
3. **Edge Cases**: Empty, single element, duplicates
4. **Pattern Recognition**: Identify which structure fits
5. **Implement from Scratch**: Don't just use libraries

## Study Plan

### Week 1: Arrays and Linked Lists
- Implementation and operations
- Two pointers technique
- Fast/slow pointers

### Week 2: Stacks and Queues
- Implementation
- Monotonic stack
- BFS with queue

### Week 3: Hash Maps and Sets
- Collision handling
- Frequency counting
- Two sum patterns

### Week 4: Trees and Heaps
- Traversals
- BST operations
- Heap operations

### Week 5: Graphs
- Representations
- BFS/DFS
- Topological sort