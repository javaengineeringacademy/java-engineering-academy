# Best Practices Quiz

## Questions

### 1. What is const correctness?
A) Making variables constant
B) Using const for immutable data and methods
C) Making classes constant
D) Using const in comments

### 2. Why prefer references over pointers?
A) References are faster
B) References are safer and cleaner
C) References use less memory
D) References are older

### 3. What is the principle of least surprise?
A) Code should do what users expect
B) Code should be surprising
C) Code should be complex
D) Code should be simple

### 4. What is RAII?
A) Resource Acquisition Is Initialization
B) Runtime Allocation Is Immediate
C) Reference Association Is Instant
D) Random Access Is Immediate

### 5. What is the benefit of small functions?
A) Faster execution
B) Easier to understand and test
C) Less memory usage
D) Better performance

### 6. What is the difference between `emplace_back` and `push_back`?
A) No difference
B) `emplace_back` constructs in-place avoiding copies; `push_back` may copy/move
C) `push_back` is always faster
D) `emplace_back` is deprecated

### 7. What is the purpose of `explicit` on constructors?
A) To make constructors public
B) To prevent implicit conversions from single-argument constructors
C) To make constructors faster
D) To make constructors virtual

### 8. What is the Rule of Zero?
A) Always use zero initialization
B) If possible, don't define any special member functions — let the compiler generate them
C) Use zero pointers everywhere
D) Set all members to zero

### 9. What is the benefit of using `enum class` over plain `enum`?
A) No benefit
B) `enum class` is strongly typed and scoped, preventing implicit conversions and name collisions
C) `enum class` is faster
D) `enum class` uses less memory

### 10. What is the purpose of `[[nodiscard]]` (C++17)?
A) To mark functions whose return values should not be ignored
B) To optimize performance
C) To enable move semantics
D) To declare pure functions

## Answers
1. B) Using const for immutable data and methods
2. B) References are safer and cleaner
3. A) Code should do what users expect
4. A) Resource Acquisition Is Initialization
5. B) Easier to understand and test
6. B) `emplace_back` constructs in-place avoiding copies; `push_back` may copy/move
7. B) To prevent implicit conversions from single-argument constructors
8. B) If possible, don't define any special member functions — let the compiler generate them
9. B) `enum class` is strongly typed and scoped, preventing implicit conversions and name collisions
10. A) To mark functions whose return values should not be ignored
