# Searching Algorithms

## Table of Contents

- [Overview](#overview)
- [Linear Search](#linear-search)
- [Binary Search](#binary-search)
- [Interpolation Search](#interpolation-search)
- [Exponential Search](#exponential-search)
- [Trie Search](#trie-search)
- [Complexity Comparison](#complexity-comparison)
- [Use Cases](#use-cases)

---

## Overview

Searching finds the position of a target value within a data structure.

| Algorithm | Precondition | Time | Space |
|-----------|--------------|------|-------|
| Linear | None | O(n) | O(1) |
| Binary | Sorted | O(log n) | O(1) |
| Interpolation | Sorted, uniform | O(log log n)* | O(1) |
| Exponential | Sorted | O(log n) | O(1) |

*Average case for uniform distribution

---

## Linear Search

Check each element sequentially.

```python
def linear_search(arr: list, target: any) -> int:
    for i, val in enumerate(arr):
        if val == target:
            return i
    return -1

# Sentinel version (avoids bounds checking)
def linear_search_sentinel(arr: list, target: any) -> int:
    n = len(arr)
    last = arr[n - 1]
    arr[n - 1] = target  # Sentinel

    i = 0
    while arr[i] != target:
        i += 1

    arr[n - 1] = last  # Restore

    if i < n - 1 or arr[n - 1] == target:
        return i
    return -1

# Example
arr = [2, 3, 4, 10, 40]
print(linear_search(arr, 10))  # 3
print(linear_search(arr, 5))   # -1
```

| Case | Time | Space |
|------|------|-------|
| Best | O(1) | O(1) |
| Average | O(n) | O(1) |
| Worst | O(n) | O(1) |

---

## Binary Search

Divide sorted array in half repeatedly.

```python
def binary_search(arr: list, target: int) -> int:
    left, right = 0, len(arr) - 1

    while left <= right:
        mid = left + (right - left) // 2  # Avoids overflow

        if arr[mid] == target:
            return mid
        elif arr[mid] < target:
            left = mid + 1
        else:
            right = mid - 1

    return -1

# Recursive version
def binary_search_recursive(arr: list, target: int, left: int = 0, right: int = None) -> int:
    if right is None:
        right = len(arr) - 1

    if left > right:
        return -1

    mid = left + (right - left) // 2

    if arr[mid] == target:
        return mid
    elif arr[mid] < target:
        return binary_search_recursive(arr, target, mid + 1, right)
    else:
        return binary_search_recursive(arr, target, left, mid - 1)

# Example
arr = [2, 3, 4, 10, 40]
print(binary_search(arr, 10))  # 3

# Visualization:
# Array: [2, 3, 4, 10, 40], target: 10
# Step 1: left=0, right=4, mid=2, arr[2]=4 < 10 → left=3
# Step 2: left=3, right=4, mid=3, arr[3]=10 = 10 → return 3
```

### Binary Search Variants

```python
# Find first occurrence
def binary_search_first(arr: list, target: int) -> int:
    left, right = 0, len(arr) - 1
    result = -1

    while left <= right:
        mid = left + (right - left) // 2
        if arr[mid] == target:
            result = mid
            right = mid - 1  # Continue searching left
        elif arr[mid] < target:
            left = mid + 1
        else:
            right = mid - 1

    return result

# Find last occurrence
def binary_search_last(arr: list, target: int) -> int:
    left, right = 0, len(arr) - 1
    result = -1

    while left <= right:
        mid = left + (right - left) // 2
        if arr[mid] == target:
            result = mid
            left = mid + 1  # Continue searching right
        elif arr[mid] < target:
            left = mid + 1
        else:
            right = mid - 1

    return result

# Find insertion position
def binary_search_insert(arr: list, target: int) -> int:
    left, right = 0, len(arr)

    while left < right:
        mid = left + (right - left) // 2
        if arr[mid] < target:
            left = mid + 1
        else:
            right = mid

    return left

# Example
arr = [1, 2, 2, 2, 3, 4, 5]
print(binary_search_first(arr, 2))   # 1
print(binary_search_last(arr, 2))    # 3
print(binary_search_insert(arr, 2))  # 1
print(binary_search_insert(arr, 6))  # 7
```

| Case | Time | Space |
|------|------|-------|
| Best | O(1) | O(1) |
| Average | O(log n) | O(1) |
| Worst | O(log n) | O(1) |

---

## Interpolation Search

Estimate position based on value distribution (like finding a name in phone book).

```python
def interpolation_search(arr: list, target: int) -> int:
    left, right = 0, len(arr) - 1

    while left <= right and arr[left] <= target <= arr[right]:
        if arr[left] == arr[right]:
            if arr[left] == target:
                return left
            break

        # Interpolation formula
        pos = left + ((target - arr[left]) * (right - left)) // (arr[right] - arr[left])

        if arr[pos] == target:
            return pos
        elif arr[pos] < target:
            left = pos + 1
        else:
            right = pos - 1

    return -1

# Example
arr = [10, 20, 30, 40, 50, 60, 70, 80, 90, 100]
print(interpolation_search(arr, 70))  # 6
```

| Case | Time | Space |
|------|------|-------|
| Best | O(1) | O(1) |
| Average | O(log log n) | O(1) |
| Worst | O(n) | O(1) |

**Best for**: Uniformly distributed sorted data

---

## Exponential Search

Find range, then binary search within range.

```python
def exponential_search(arr: list, target: int) -> int:
    n = len(arr)
    if n == 0:
        return -1

    # Find range
    bound = 1
    while bound < n and arr[bound] < target:
        bound *= 2

    # Binary search in range
    left = bound // 2
    right = min(bound, n - 1)

    while left <= right:
        mid = left + (right - left) // 2
        if arr[mid] == target:
            return mid
        elif arr[mid] < target:
            left = mid + 1
        else:
            right = mid - 1

    return -1

# Example
arr = [2, 3, 4, 10, 40, 50, 60, 70]
print(exponential_search(arr, 10))  # 3
```

| Case | Time | Space |
|------|------|-------|
| Best | O(1) | O(1) |
| Average | O(log n) | O(1) |
| Worst | O(log n) | O(1) |

**Best for**: Unbounded/infinite sorted arrays

---

## Trie Search

Search for strings/prefixes in a trie.

```python
class TrieNode:
    def __init__(self):
        self.children = {}
        self.is_end = False

class Trie:
    def __init__(self):
        self.root = TrieNode()

    def insert(self, word: str) -> None:
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
        node.is_end = True

    def search(self, word: str) -> bool:
        node = self.root
        for char in word:
            if char not in node.children:
                return False
            node = node.children[char]
        return node.is_end

    def starts_with(self, prefix: str) -> bool:
        node = self.root
        for char in prefix:
            if char not in node.children:
                return False
            node = node.children[char]
        return True

    def autocomplete(self, prefix: str) -> list:
        node = self.root
        for char in prefix:
            if char not in node.children:
                return []
            node = node.children[char]

        results = []
        self._dfs(node, prefix, results)
        return results

    def _dfs(self, node, current, results):
        if node.is_end:
            results.append(current)
        for char, child in node.children.items():
            self._dfs(child, current + char, results)

# Usage
trie = Trie()
words = ["apple", "app", "application", "banana", "bat"]
for word in words:
    trie.insert(word)

print(trie.search("apple"))      # True
print(trie.starts_with("app"))   # True
print(trie.autocomplete("app"))  # ["app", "apple", "application"]
```

---

## Complexity Comparison

| Algorithm | Time | Space | Sorted | Random |
|-----------|------|-------|--------|--------|
| Linear | O(n) | O(1) | O(n) | O(n) |
| Binary | O(log n) | O(1) | O(log n) | N/A |
| Interpolation | O(log log n)* | O(1) | O(log log n)* | N/A |
| Exponential | O(log n) | O(1) | O(log n) | N/A |
| Trie | O(m) | O(ALPHABET × m × n) | N/A | N/A |

*m = string length, *average case

---

## Use Cases

| Scenario | Algorithm |
|----------|-----------|
| Small dataset | Linear |
| Sorted array | Binary |
| Uniform distribution | Interpolation |
| Unbounded data | Exponential |
| String prefix matching | Trie |
| Database indexing | Binary/B-Tree |
| Autocomplete | Trie |
| Real-time systems | Binary (predictable) |
