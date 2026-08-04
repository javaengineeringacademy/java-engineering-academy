# Arrays

## Table of Contents

- [Overview](#overview)
- [Types of Arrays](#types-of-arrays)
  - [Static Arrays](#static-arrays)
  - [Dynamic Arrays](#dynamic-arrays)
- [Memory Layout](#memory-layout)
- [Operations](#operations)
  - [Access](#access)
  - [Search](#search)
  - [Insertion](#insertion)
  - [Deletion](#deletion)
- [Time Complexity](#time-complexity)
- [Multi-Dimensional Arrays](#multi-dimensional-arrays)
- [Array Algorithms](#array-algorithms)
- [Use Cases](#use-cases)
- [Limitations](#limitations)
- [Comparison with Other Data Structures](#comparison-with-other-data-structures)

---

## Overview

An array is a collection of elements stored at contiguous memory locations. Each element can be accessed directly using its index.

```
Index:   0     1     2     3     4
      ┌─────┬─────┬─────┬─────┬─────┐
      │  10 │  20 │  30 │  40 │  50 │
      └─────┴─────┴─────┴─────┴─────┘
Address: 100   104   108   112   116
         (assuming 4-byte integers)
```

### Key Characteristics

- **Contiguous memory**: Elements are stored next to each other
- **Fixed size** (static) or **dynamic resizing**
- **O(1) random access** - direct index access
- **Cache-friendly** - spatial locality improves performance
- **Homogeneous** - typically stores elements of the same type

---

## Types of Arrays

### Static Arrays

Fixed-size arrays allocated at compile time or on the stack.

```python
# Python doesn't have true static arrays, but lists with fixed size
class StaticArray:
    def __init__(self, capacity: int):
        self._capacity = capacity
        self._data = [None] * capacity
        self._size = 0

    def __setitem__(self, index: int, value):
        if index < 0 or index >= self._capacity:
            raise IndexError("Index out of bounds")
        self._data[index] = value
        if index >= self._size:
            self._size = index + 1

    def __getitem__(self, index: int):
        if index < 0 or index >= self._size:
            raise IndexError("Index out of bounds")
        return self._data[index]

    def __len__(self):
        return self._size

# Usage
arr = StaticArray(5)
arr[0] = 10
arr[1] = 20
print(arr[0])  # 10
print(len(arr))  # 2
```

```java
// Java static array
int[] arr = new int[5];  // Fixed size of 5
arr[0] = 10;
arr[1] = 20;
// arr[5] = 60;  // ArrayIndexOutOfBoundsException
```

```c
// C static array
int arr[5] = {10, 20, 30, 40, 50};
```

### Dynamic Arrays

Arrays that can grow or shrink during runtime.

```python
class DynamicArray:
    def __init__(self, capacity: int = 2):
        self._capacity = capacity
        self._size = 0
        self._data = [None] * capacity

    def _resize(self, new_capacity: int):
        new_data = [None] * new_capacity
        for i in range(self._size):
            new_data[i] = self._data[i]
        self._data = new_data
        self._capacity = new_capacity

    def append(self, value):
        if self._size == self._capacity:
            self._resize(self._capacity * 2)  # Double capacity
        self._data[self._size] = value
        self._size += 1

    def pop(self):
        if self._size == 0:
            raise IndexError("pop from empty array")
        self._size -= 1
        value = self._data[self._size]
        self._data[self._size] = None
        # Shrink if too empty
        if self._size > 0 and self._size == self._capacity // 4:
            self._resize(self._capacity // 2)
        return value

    def __getitem__(self, index: int):
        if index < 0 or index >= self._size:
            raise IndexError("Index out of bounds")
        return self._data[index]

    def __len__(self):
        return self._size

    def __repr__(self):
        return f"DynamicArray({self._data[:self._size]})"

# Usage
arr = DynamicArray()
for i in range(10):
    arr.append(i * 10)
    print(f"Size: {len(arr)}, Capacity: {arr._capacity}")
# Size: 1, Capacity: 2
# Size: 2, Capacity: 2
# Size: 3, Capacity: 4
# Size: 4, Capacity: 4
# Size: 5, Capacity: 8
# Size: 6, Capacity: 8
# Size: 7, Capacity: 16
# Size: 8, Capacity: 16
# Size: 9, Capacity: 16
# Size: 10, Capacity: 16
```

---

## Memory Layout

```
Array in Memory:
┌─────────┬─────────┬─────────┬─────────┬─────────┐
│ Element │ Element │ Element │ Element │ Element │
│    0    │    1    │    2    │    3    │    4    │
└─────────┴─────────┴─────────┴─────────┴─────────┘
    ▲
    │
    Base Address (base + index * element_size)
```

### Address Calculation

```
address(i) = base_address + i × element_size

Example:
- Base address: 1000
- Element size: 4 bytes (int)
- Index: 3

address(3) = 1000 + 3 × 4 = 1012
```

### Why Arrays Are Cache-Friendly

```
CPU Cache Line (64 bytes typically):
┌────────────────────────────────────────────────┐
│  arr[0] │ arr[1] │ arr[2] │ ... │ arr[15]    │
└────────────────────────────────────────────────┘

Accessing arr[0] loads arr[0] through arr[15] into cache
Accessing arr[1] is a cache hit (already loaded)
```

---

## Operations

### Access

| Operation | Time Complexity | Description |
|-----------|-----------------|-------------|
| Read by index | O(1) | Direct memory access |
| Write by index | O(1) | Direct memory access |

```python
arr = [10, 20, 30, 40, 50]
print(arr[2])   # O(1) - Read: 30
arr[2] = 99     # O(1) - Write: arr becomes [10, 20, 99, 40, 50]
```

### Search

| Operation | Time Complexity | Description |
|-----------|-----------------|-------------|
| Linear search | O(n) | Check each element |
| Binary search | O(log n) | Sorted array only |

```python
def linear_search(arr: list, target: int) -> int:
    for i, val in enumerate(arr):
        if val == target:
            return i
    return -1

def binary_search(arr: list, target: int) -> int:
    left, right = 0, len(arr) - 1
    while left <= right:
        mid = (left + right) // 2
        if arr[mid] == target:
            return mid
        elif arr[mid] < target:
            left = mid + 1
        else:
            right = mid - 1
    return -1

sorted_arr = [1, 3, 5, 7, 9, 11, 13, 15, 17, 19]
print(binary_search(sorted_arr, 11))  # 5
```

### Insertion

| Position | Time Complexity | Description |
|----------|-----------------|-------------|
| End | O(1) amortized | Dynamic array may resize |
| Beginning | O(n) | Must shift all elements |
| Middle | O(n) | Must shift half elements on average |

```python
# Insert at end - O(1) amortized
arr = [1, 2, 3, 4, 5]
arr.append(6)  # [1, 2, 3, 4, 5, 6]

# Insert at beginning - O(n)
arr.insert(0, 0)  # [0, 1, 2, 3, 4, 5, 6]

# Insert at middle - O(n)
arr.insert(3, 99)  # [0, 1, 2, 99, 3, 4, 5, 6]
```

```
Insert at index 2 (value 99):

Before: [10, 20, 30, 40, 50]
                    ↑ shift right

After:  [10, 20, 99, 30, 40, 50]
```

### Deletion

| Position | Time Complexity | Description |
|----------|-----------------|-------------|
| End | O(1) | Just decrement size |
| Beginning | O(n) | Must shift all elements |
| Middle | O(n) | Must shift elements |

```python
# Delete from end - O(1)
arr = [10, 20, 30, 40, 50]
arr.pop()  # Returns 50, arr is [10, 20, 30, 40]

# Delete from beginning - O(n)
arr.pop(0)  # Returns 10, arr is [20, 30, 40]

# Delete specific value - O(n) for search + O(n) for shift
arr.remove(30)  # arr is [20, 40]
```

---

## Time Complexity

| Operation | Best | Average | Worst | Space |
|-----------|------|---------|-------|-------|
| Access | O(1) | O(1) | O(1) | O(1) |
| Search (unsorted) | O(1) | O(n) | O(n) | O(1) |
| Search (sorted) | O(1) | O(log n) | O(log n) | O(1) |
| Insertion (end) | O(1) | O(1) | O(n) | O(n) |
| Insertion (beginning) | O(n) | O(n) | O(n) | O(n) |
| Insertion (middle) | O(n) | O(n) | O(n) | O(n) |
| Deletion (end) | O(1) | O(1) | O(1) | O(1) |
| Deletion (beginning) | O(n) | O(n) | O(n) | O(n) |
| Deletion (middle) | O(n) | O(n) | O(n) | O(n) |
| Traversal | O(n) | O(n) | O(n) | O(1) |

---

## Multi-Dimensional Arrays

### 2D Array (Matrix)

```python
# 2D Array - Row-major order
matrix = [
    [1, 2, 3, 4],
    [5, 6, 7, 8],
    [9, 10, 11, 12]
]

# Access element: matrix[row][col]
print(matrix[1][2])  # 7

# Memory layout (row-major):
# [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]

# Address calculation for 2D array:
# address(i, j) = base + (i * cols + j) * element_size
```

```python
# Dynamic 2D array
def create_matrix(rows: int, cols: int, default=0) -> list:
    return [[default for _ in range(cols)] for _ in range(rows)]

# Matrix operations
def matrix_add(A: list, B: list) -> list:
    rows, cols = len(A), len(A[0])
    return [[A[i][j] + B[i][j] for j in range(cols)] for i in range(rows)]

def matrix_multiply(A: list, B: list) -> list:
    rows_A, cols_A = len(A), len(A[0])
    cols_B = len(B[0])
    result = create_matrix(rows_A, cols_B)
    for i in range(rows_A):
        for j in range(cols_B):
            for k in range(cols_A):
                result[i][j] += A[i][k] * B[k][j]
    return result

# Transpose
def transpose(matrix: list) -> list:
    return [[matrix[j][i] for j in range(len(matrix))] for i in range(len(matrix[0]))]
```

### 3D Array

```python
# 3D Array - useful for images, voxels
tensor = [
    [  # Layer 0
        [1, 2, 3],
        [4, 5, 6]
    ],
    [  # Layer 1
        [7, 8, 9],
        [10, 11, 12]
    ]
]

# Access: tensor[layer][row][col]
print(tensor[1][0][2])  # 9
```

---

## Array Algorithms

### Two Pointers

```python
def two_sum_sorted(arr: list, target: int) -> tuple:
    """Find two numbers that sum to target in sorted array."""
    left, right = 0, len(arr) - 1
    while left < right:
        current_sum = arr[left] + arr[right]
        if current_sum == target:
            return (left, right)
        elif current_sum < target:
            left += 1
        else:
            right -= 1
    return None

def remove_duplicates(arr: list) -> int:
    """Remove duplicates in-place from sorted array."""
    if not arr:
        return 0
    write = 1
    for read in range(1, len(arr)):
        if arr[read] != arr[read - 1]:
            arr[write] = arr[read]
            write += 1
    return write
```

### Sliding Window

```python
def max_subarray_sum(arr: list, k: int) -> int:
    """Maximum sum of subarray of size k."""
    window_sum = sum(arr[:k])
    max_sum = window_sum
    for i in range(k, len(arr)):
        window_sum += arr[i] - arr[i - k]
        max_sum = max(max_sum, window_sum)
    return max_sum

def contains_nearby_duplicate(arr: list, k: int) -> bool:
    """Check if duplicate exists within distance k."""
    window = set()
    for i, num in enumerate(arr):
        if num in window:
            return True
        window.add(num)
        if len(window) > k:
            window.remove(arr[i - k])
    return False
```

### Kadane's Algorithm

```python
def max_subarray(arr: list) -> int:
    """Find maximum sum contiguous subarray."""
    max_current = max_global = arr[0]
    for i in range(1, len(arr)):
        max_current = max(arr[i], max_current + arr[i])
        if max_current > max_global:
            max_global = max_current
    return max_global

# Example
arr = [-2, 1, -3, 4, -1, 2, 1, -5, 4]
print(max_subarray(arr))  # 6 (subarray: [4, -1, 2, 1])
```

### Prefix Sum

```python
def build_prefix_sum(arr: list) -> list:
    """Build prefix sum array for O(1) range queries."""
    prefix = [0] * (len(arr) + 1)
    for i in range(len(arr)):
        prefix[i + 1] = prefix[i] + arr[i]
    return prefix

def range_sum(prefix: list, left: int, right: int) -> int:
    """Get sum of arr[left..right] in O(1)."""
    return prefix[right + 1] - prefix[left]

arr = [1, 2, 3, 4, 5]
prefix = build_prefix_sum(arr)
print(range_sum(prefix, 1, 3))  # 9 (2 + 3 + 4)
```

---

## Use Cases

| Use Case | Why Arrays |
|----------|------------|
| Lookup tables | O(1) access |
| Buffers | Contiguous memory for I/O |
| Matrices | Natural 2D structure |
| Sorting | In-place algorithms |
| Hash table values | Underlying storage |
| CPU caches | Spatial locality |
| Image processing | Pixel data storage |
| Implementing stacks | O(1) push/pop at end |

---

## Limitations

1. **Fixed size** (static arrays)
2. **Costly insertion/deletion** in middle - O(n)
3. **Wasted space** if allocated too large
4. **No built-in operations** (unlike lists/sets)

---

## Comparison with Other Data Structures

| Feature | Array | Linked List | Hash Table | BST |
|---------|-------|-------------|------------|-----|
| Access by index | O(1) | O(n) | N/A | O(log n) |
| Search | O(n) | O(n) | O(1) avg | O(log n) |
| Insert at beginning | O(n) | O(1) | O(1) avg | O(log n) |
| Insert at end | O(1)* | O(1) | O(1) avg | O(log n) |
| Insert in middle | O(n) | O(1)** | O(1) avg | O(log n) |
| Delete at beginning | O(n) | O(1) | O(1) avg | O(log n) |
| Delete at end | O(1) | O(n) | O(1) avg | O(log n) |
| Memory | Contiguous | Scattered | Scattered | Scorted |
| Cache performance | Excellent | Poor | Good | Good |

*Amortized for dynamic arrays
**With pointer to position
