# STL Quiz

## Questions

### 1. Which container provides O(1) random access?
A) `std::list`
B) `std::vector`
C) `std::set`
D) `std::map`

### 2. What is the time complexity of inserting at the end of a vector?
A) O(1) amortized
B) O(n)
C) O(log n)
D) O(n²)

### 3. Which algorithm is used to sort elements in a range?
A) `std::sort`
B) `std::order`
C) `std::arrange`
D) `std::organize`

### 4. What is the difference between `std::map` and `std::unordered_map`?
A) `std::map` is faster
B) `std::unordered_map` maintains order
C) `std::map` maintains order, `std::unordered_map` has O(1) average lookup
D) No difference

### 5. What is an iterator?
A) A pointer to the first element
B) A pointer-like object for traversing containers
C) A function that iterates
D) A loop construct

### 6. What is the difference between `std::vector` and `std::deque`?
A) No difference
B) `std::deque` supports O(1) insertion at both ends; `std::vector` only at the back
C) `std::vector` is always faster
D) `std::deque` uses more memory

### 7. What does `std::reserve` do for a vector?
A) Resizes the vector
B) Allocates capacity without changing size
C) Removes all elements
D) Sorts the vector

### 8. Which container should you use for frequent insertions and deletions in the middle?
A) `std::vector`
B) `std::list`
C) `std::array`
D) `std::string`

### 9. What is `std::move` used for with containers?
A) Physically moving data in memory
B) Casting an object to an rvalue reference to enable move semantics
C) Deleting an element
D) Swapping two containers

### 10. What is the purpose of `std::algorithm`?
A) Container management
B) Generic functions for operating on ranges of elements
C) Memory allocation
D) I/O operations

## Answers
1. B) `std::vector`
2. A) O(1) amortized
3. A) `std::sort`
4. C) `std::map` maintains order, `std::unordered_map` has O(1) average lookup
5. B) A pointer-like object for traversing containers
6. B) `std::deque` supports O(1) insertion at both ends; `std::vector` only at the back
7. B) Allocates capacity without changing size
8. B) `std::list`
9. B) Casting an object to an rvalue reference to enable move semantics
10. B) Generic functions for operating on ranges of elements
