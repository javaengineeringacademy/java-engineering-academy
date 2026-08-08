# Standard Template Library (STL) Quiz

## Questions

---

### MCQ 1: Container Selection
Which container provides O(1) random access?

A) `std::list`
B) `std::vector`
C) `std::set`
D) `std::map`

---

### MCQ 2: Iterator Invalidation
After calling `vec.push_back(elem)` on a `std::vector` that triggers reallocation, which of the following become invalid?

A) Only `vec.end()`
B) All iterators to the vector's elements
C) Only iterators after the insertion point
D) No iterators become invalid

---

### MCQ 3: Container Properties
What is the key difference between `std::map` and `std::unordered_map`?

A) `std::map` is faster
B) `std::map` maintains sorted order (O(log n)); `std::unordered_map` has O(1) average but no order
C) `std::unordered_map` maintains order
D) No difference

---

### MCQ 4: Algorithm Selection
Which algorithm is used to sort elements in a range?

A) `std::order`
B) `std::sort`
C) `std::arrange`
D) `std::organize`

---

### MCQ 5: Vector Memory
What does `std::vector::reserve(100)` do?

A) Creates 100 elements
B) Allocates capacity for 100 elements without changing size
C) Resizes the vector to 100 elements
D) Removes all elements

---

### Code Output 1
What is the output?

```cpp
std::vector<int> v = {1, 2, 3, 4, 5};
v.erase(std::remove(v.begin(), v.end(), 3), v.end());
std::cout << v.size() << " ";
for (int x : v) std::cout << x << " ";
```

A) `5 1 2 3 4 5`
B) `4 1 2 4 5`
C) `4 1 2 3 4 5`
D) `5 1 2 4 5`

---

### Code Output 2
What is the output?

```cpp
std::map<std::string, int> m;
m["a"] = 1;
m["b"] = 2;
m["c"] = 3;
m["a"] = 10;
std::cout << m.size() << " " << m["a"];
```

A) `2 1`
B) `3 10`
C) `3 1`
D) `2 10`

---

### Code Output 3
What is the output?

```cpp
std::vector<int> v = {5, 3, 1, 4, 2};
std::sort(v.begin() + 1, v.begin() + 4);
for (int x : v) std::cout << x << " ";
```

A) `1 2 3 4 5`
B) `5 1 3 4 2`
C) `5 1 3 4 2`
D) `5 1 2 3 4 2`

---

### Bug Finding 1
Find the bug:

```cpp
std::vector<int> v = {1, 2, 3, 4, 5};
for (auto it = v.begin(); it != v.end(); ++it) {
    if (*it % 2 == 0) {
        v.erase(it);  // Remove even numbers
    }
}
```

What happens and how do you fix it?

---

### Bug Finding 2
Find the bug:

```cpp
std::map<std::string, std::vector<int>> data;
data["key"].push_back(42);

for (auto& [key, vec] : data) {
    if (vec.empty()) {
        data.erase(key);  // Remove empty entries
    }
}
```

---

### Scenario 1: Performance
You need to store 10 million integers and frequently check if a specific integer exists. What container should you use?

A) `std::vector` with `std::find`
B) `std::unordered_set`
C) `std::set`
D) `std::list`

---

### Scenario 2: Design
You need a container where elements maintain insertion order, duplicates are allowed, and you need O(1) lookup by value. Which approach?

A) `std::vector` with linear search
B) `std::unordered_map<T, int>` to store insertion order + `std::vector<T>` for ordered storage
C) `std::list`
D) `std::set`

---

## Answers

---

### MCQ 1: B
**`std::vector`** provides O(1) random access via `operator[]` and `.at()`. `std::list` and `std::map` are O(n) and O(log n) respectively for access.

### MCQ 2: B
**All iterators become invalid** when reallocation occurs. The vector's data moves to a new memory location. Any iterator pointing to the old location is dangling. After `push_back`, only iterators/end pointers you obtain AFTER the call are valid.

### MCQ 3: B
**`std::map` maintains sorted order with O(log n) operations** (red-black tree). `std::unordered_map` uses a hash table with O(1) average but no ordering guarantee. Choose based on whether you need sorted iteration.

### MCQ 4: B
**`std::sort`** sorts elements in a range. It requires random access iterators and uses introsort (quicksort + heapsort hybrid). Options A, C, D are not real STL algorithms.

### MCQ 5: B
**Allocates capacity for 100 elements without changing size.** The vector's `.size()` remains 0, but `.capacity()` becomes at least 100. This prevents reallocation during subsequent `push_back` calls.

### Code Output 1: B
`4 1 2 4 5`. The erase-remove idiom first moves non-3 elements to the front: `{1, 2, 4, 5}`. `v.size()` is now 4. The vector contains `{1, 2, 4, 5}`.

### Code Output 2: B
`3 10`. `m["a"] = 10` overwrites the previous value of 1. The map has 3 entries: a=10, b=2, c=3.

### Code Output 3: B
`5 1 3 4 2`. The sort operates on range `[begin+1, begin+4)`, which is elements `{3, 1, 4}`. After sorting: `{1, 3, 4}`. The full vector becomes `{5, 1, 3, 4, 2}`.

### Bug Finding 1
**Erasing invalidates the iterator, then `++it` dereferences freed memory.** Fix: use the return value of `erase()`:

```cpp
for (auto it = v.begin(); it != v.end(); ) {
    if (*it % 2 == 0) {
        it = v.erase(it);  // erase returns next valid iterator
    } else {
        ++it;
    }
}
```

### Bug Finding 2
**Modifying a map while iterating over it invalidates iterators.** `data.erase(key)` invalidates the iterator `key` in the loop, causing undefined behavior. Fix: collect keys to erase first, then erase after the loop:

```cpp
std::vector<std::string> to_erase;
for (auto& [key, vec] : data) {
    if (vec.empty()) to_erase.push_back(key);
}
for (const auto& key : to_erase) {
    data.erase(key);
}
```

### Scenario 1: B
**`std::unordered_set`** provides O(1) average-case lookup and insertion. For 10 million integers, `std::find` on a vector would be O(n) per query — 10 million comparisons worst case. `std::unordered_set` hashes the value directly.

### Scenario 2: B
**Use both `std::vector` for ordered storage and `std::unordered_map<T, size_t>` for O(1) position lookup.** No single STL container provides both insertion-order preservation and O(1) value lookup. This dual-container approach is common in production systems.
