# Sorting Algorithms

## Table of Contents

- [Overview](#overview)
- [Comparison-Based Sorting](#comparison-based-sorting)
  - [Bubble Sort](#bubble-sort)
  - [Selection Sort](#selection-sort)
  - [Insertion Sort](#insertion-sort)
  - [Merge Sort](#merge-sort)
  - [Quick Sort](#quick-sort)
  - [Heap Sort](#heap-sort)
- [Non-Comparison Sorting](#non-comparison-sorting)
  - [Counting Sort](#counting-sort)
  - [Radix Sort](#radix-sort)
- [Complexity Comparison](#complexity-comparison)
- [Choosing an Algorithm](#choosing-an-algorithm)

---

## Overview

Sorting arranges elements in a specific order (ascending/descending).

### Why Sorting Matters

- Enables binary search (O(log n) vs O(n))
- Data presentation and reporting
- Preprocessing for other algorithms
- Database operations

### Comparison-Based vs Non-Comparison

| Type | Time Lower Bound | Examples |
|------|------------------|----------|
| Comparison | Ω(n log n) | Bubble, Merge, Quick |
| Non-Comparison | O(n) possible | Counting, Radix |

---

## Comparison-Based Sorting

### Bubble Sort

Repeatedly swap adjacent elements if they're in wrong order.

```python
def bubble_sort(arr: list) -> list:
    n = len(arr)
    for i in range(n):
        swapped = False
        for j in range(0, n - i - 1):
            if arr[j] > arr[j + 1]:
                arr[j], arr[j + 1] = arr[j + 1], arr[j]
                swapped = True
        if not swapped:  # Optimization: already sorted
            break
    return arr

# Visualization:
# [5, 3, 8, 4, 2]
# Pass 1: [3, 5, 4, 2, 8] - 8 bubbles to end
# Pass 2: [3, 4, 2, 5, 8] - 5 bubbles to position
# Pass 3: [3, 2, 4, 5, 8] - 4 bubbles to position
# Pass 4: [2, 3, 4, 5, 8] - sorted!
```

| Case | Time | Space | Stable |
|------|------|-------|--------|
| Best | O(n) | O(1) | Yes |
| Average | O(n²) | O(1) | Yes |
| Worst | O(n²) | O(1) | Yes |

---

### Selection Sort

Find minimum element and place it at the beginning.

```python
def selection_sort(arr: list) -> list:
    n = len(arr)
    for i in range(n):
        min_idx = i
        for j in range(i + 1, n):
            if arr[j] < arr[min_idx]:
                min_idx = j
        arr[i], arr[min_idx] = arr[min_idx], arr[i]
    return arr

# Visualization:
# [64, 25, 12, 22, 11]
# i=0: Find min(11), swap with 64 → [11, 25, 12, 22, 64]
# i=1: Find min(12), swap with 25 → [11, 12, 25, 22, 64]
# i=2: Find min(22), swap with 25 → [11, 12, 22, 25, 64]
# i=3: Find min(25), already in place → [11, 12, 22, 25, 64]
```

| Case | Time | Space | Stable |
|------|------|-------|--------|
| All | O(n²) | O(1) | No |

---

### Insertion Sort

Build sorted portion one element at a time.

```python
def insertion_sort(arr: list) -> list:
    for i in range(1, len(arr)):
        key = arr[i]
        j = i - 1
        while j >= 0 and arr[j] > key:
            arr[j + 1] = arr[j]
            j -= 1
        arr[j + 1] = key
    return arr

# Visualization:
# [5, 2, 4, 6, 1, 3]
# i=1: key=2, insert → [2, 5, 4, 6, 1, 3]
# i=2: key=4, insert → [2, 4, 5, 6, 1, 3]
# i=3: key=6, insert → [2, 4, 5, 6, 1, 3] (no change)
# i=4: key=1, insert → [1, 2, 4, 5, 6, 3]
# i=5: key=3, insert → [1, 2, 3, 4, 5, 6]
```

| Case | Time | Space | Stable |
|------|------|-------|--------|
| Best | O(n) | O(1) | Yes |
| Average | O(n²) | O(1) | Yes |
| Worst | O(n²) | O(1) | Yes |

**Best for**: Nearly sorted or small arrays

---

### Merge Sort

Divide array in half, sort recursively, merge sorted halves.

```python
def merge_sort(arr: list) -> list:
    if len(arr) <= 1:
        return arr

    mid = len(arr) // 2
    left = merge_sort(arr[:mid])
    right = merge_sort(arr[mid:])

    return merge(left, right)

def merge(left: list, right: list) -> list:
    result = []
    i = j = 0

    while i < len(left) and j < len(right):
        if left[i] <= right[j]:
            result.append(left[i])
            i += 1
        else:
            result.append(right[j])
            j += 1

    result.extend(left[i:])
    result.extend(right[j:])
    return result

# Visualization:
# [38, 27, 43, 3, 9, 82, 10]
#        /               \
# [38, 27, 43, 3]   [9, 82, 10]
#    /       \         /     \
# [38, 27] [43, 3]  [9, 82] [10]
#   / \     / \      / \      |
# [38][27] [43][3]  [9][82]  [10]
#   \ /     \ /      \ /      |
# [27, 38] [3, 43]  [9, 82] [10]
#    \       /         \     /
# [3, 27, 38, 43]   [9, 10, 82]
#        \               /
# [3, 9, 10, 27, 38, 43, 82]
```

| Case | Time | Space | Stable |
|------|------|-------|--------|
| All | O(n log n) | O(n) | Yes |

**Best for**: Large datasets, guaranteed O(n log n), stable sort needed

---

### Quick Sort

Pick pivot, partition around it, sort partitions recursively.

```python
import random

def quick_sort(arr: list, low: int = 0, high: int = None) -> list:
    if high is None:
        high = len(arr) - 1

    if low < high:
        pivot_idx = partition(arr, low, high)
        quick_sort(arr, low, pivot_idx - 1)
        quick_sort(arr, pivot_idx + 1, high)

    return arr

def partition(arr: list, low: int, high: int) -> int:
    pivot = arr[high]  # Last element as pivot
    i = low - 1

    for j in range(low, high):
        if arr[j] <= pivot:
            i += 1
            arr[i], arr[j] = arr[j], arr[i]

    arr[i + 1], arr[high] = arr[high], arr[i + 1]
    return i + 1

# Lomuto partition visualization:
# [10, 80, 30, 90, 40, 50, 70], pivot=70
# Compare each with pivot:
# [10, 30, 40, 50, 90, 80, 70]
# Swap pivot to correct position:
# [10, 30, 40, 50, 70, 80, 90]
```

```python
# Randomized Quick Sort (avoids worst case)
def randomized_quick_sort(arr: list) -> list:
    if len(arr) <= 1:
        return arr

    pivot = random.choice(arr)
    left = [x for x in arr if x < pivot]
    middle = [x for x in arr if x == pivot]
    right = [x for x in arr if x > pivot]

    return randomized_quick_sort(left) + middle + randomized_quick_sort(right)
```

| Case | Time | Space | Stable |
|------|------|-------|--------|
| Best | O(n log n) | O(log n) | No |
| Average | O(n log n) | O(log n) | No |
| Worst | O(n²) | O(n) | No |

**Best for**: General purpose, in-place sorting, cache-friendly

---

### Heap Sort

Use heap data structure for sorting.

```python
def heap_sort(arr: list) -> list:
    n = len(arr)

    # Build max heap
    for i in range(n // 2 - 1, -1, -1):
        max_heapify(arr, n, i)

    # Extract elements
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
```

| Case | Time | Space | Stable |
|------|------|-------|--------|
| All | O(n log n) | O(1) | No |

---

## Non-Comparison Sorting

### Counting Sort

Count occurrences of each element.

```python
def counting_sort(arr: list, min_val: int = None, max_val: int = None) -> list:
    if not arr:
        return arr

    if min_val is None:
        min_val = min(arr)
    if max_val is None:
        max_val = max(arr)

    range_size = max_val - min_val + 1
    count = [0] * range_size
    output = [0] * len(arr)

    # Count occurrences
    for num in arr:
        count[num - min_val] += 1

    # Cumulative count
    for i in range(1, range_size):
        count[i] += count[i - 1]

    # Build output (stable - iterate backwards)
    for num in reversed(arr):
        output[count[num - min_val] - 1] = num
        count[num - min_val] -= 1

    return output

# Example
arr = [4, 2, 2, 8, 3, 3, 1]
print(counting_sort(arr))  # [1, 2, 2, 3, 3, 4, 8]
```

| Time | Space | Stable |
|------|-------|--------|
| O(n + k) | O(n + k) | Yes |

*k = range of input values

---

### Radix Sort

Sort digit by digit from least significant to most significant.

```python
def radix_sort(arr: list) -> list:
    if not arr:
        return arr

    max_val = max(arr)
    exp = 1

    while max_val // exp > 0:
        counting_sort_by_digit(arr, exp)
        exp *= 10

    return arr

def counting_sort_by_digit(arr: list, exp: int) -> None:
    n = len(arr)
    output = [0] * n
    count = [0] * 10

    for num in arr:
        digit = (num // exp) % 10
        count[digit] += 1

    for i in range(1, 10):
        count[i] += count[i - 1]

    for i in range(n - 1, -1, -1):
        digit = (arr[i] // exp) % 10
        output[count[digit] - 1] = arr[i]
        count[digit] -= 1

    arr[:] = output

# Example
arr = [170, 45, 75, 90, 802, 24, 2, 66]
print(radix_sort(arr))  # [2, 24, 45, 66, 75, 90, 170, 802]
```

| Time | Space | Stable |
|------|-------|--------|
| O(d × (n + k)) | O(n + k) | Yes |

*d = number of digits, k = base (10 for decimal)

---

## Complexity Comparison

| Algorithm | Best | Average | Worst | Space | Stable | In-place |
|-----------|------|---------|-------|-------|--------|----------|
| Bubble | O(n) | O(n²) | O(n²) | O(1) | Yes | Yes |
| Selection | O(n²) | O(n²) | O(n²) | O(1) | No | Yes |
| Insertion | O(n) | O(n²) | O(n²) | O(1) | Yes | Yes |
| Merge | O(n log n) | O(n log n) | O(n log n) | O(n) | Yes | No |
| Quick | O(n log n) | O(n log n) | O(n²) | O(log n) | No | Yes |
| Heap | O(n log n) | O(n log n) | O(n log n) | O(1) | No | Yes |
| Counting | O(n + k) | O(n + k) | O(n + k) | O(n + k) | Yes | No |
| Radix | O(d(n + k)) | O(d(n + k)) | O(d(n + k)) | O(n + k) | Yes | No |

---

## Choosing an Algorithm

| Scenario | Recommended | Why |
|----------|-------------|-----|
| Small array (n < 50) | Insertion | Low overhead, fast for small n |
| Nearly sorted | Insertion | O(n) for nearly sorted |
| General purpose | Quick (randomized) | Fast average, cache-friendly |
| Guaranteed O(n log n) | Merge or Heap | No worst case degradation |
| Stable sort needed | Merge | Preserves relative order |
| Integer with limited range | Counting | O(n + k) linear time |
| Memory constrained | Heap | O(1) extra space |
| Linked list | Merge | No random access needed |
| External sorting | Merge | Handles data larger than memory |
