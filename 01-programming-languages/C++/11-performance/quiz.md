# Performance Quiz

## Questions

### 1. Why should you profile before optimizing?
A) To find bottlenecks
B) To make code faster
C) To reduce memory usage
D) All of the above

### 2. What is cache-friendly code?
A) Code that runs fast
B) Code that accesses memory in predictable patterns
C) Code that uses less memory
D) Code that runs on multiple cores

### 3. What is SIMD?
A) Single Instruction Multiple Data
B) Simple Instruction Multiple Data
C) Single Instruction Manual Data
D) Simple Instruction Manual Data

### 4. What is a memory pool?
A) A large block of memory
B) Pre-allocated memory for frequent allocations
C) A memory leak detector
D) A memory optimization tool

### 5. What is the benefit of move semantics?
A) Faster memory access
B) Reduced copying overhead
C) Better cache performance
D) Less memory usage

### 6. What is the difference between `reserve` and `resize` for a vector?
A) No difference
B) `reserve` allocates capacity without changing size; `resize` changes the actual number of elements
C) `resize` is faster
D) `reserve` changes size

### 7. What is copy elision (NRVO)?
A) A compiler optimization that avoids unnecessary copies of objects
B) A way to copy objects faster
C) Removing copy constructors
D) Copying objects in parallel

### 8. What is the impact of cache misses on performance?
A) No impact
B) Significant slowdown as CPU waits for data from main memory
C) Slight improvement
D) Only affects I/O operations

### 9. What is data-oriented design?
A) Organizing code around classes and inheritance
B) Organizing data for cache efficiency, separating hot and cold data
C) Writing data to files
D) Designing database schemas

### 10. What is `[[nodiscard]]` (C++17) and how does it relate to performance?
A) Marks return values that should not be ignored, preventing resource leaks and unnecessary work
B) A performance optimization attribute
C) Makes functions faster
D) Disables copy elision

## Answers
1. A) To find bottlenecks
2. B) Code that accesses memory in predictable patterns
3. A) Single Instruction Multiple Data
4. B) Pre-allocated memory for frequent allocations
5. B) Reduced copying overhead
6. B) `reserve` allocates capacity without changing size; `resize` changes the actual number of elements
7. A) A compiler optimization that avoids unnecessary copies of objects
8. B) Significant slowdown as CPU waits for data from main memory
9. B) Organizing data for cache efficiency, separating hot and cold data
10. A) Marks return values that should not be ignored, preventing resource leaks and unnecessary work
