# Memory Management Quiz

## Questions

### 1. What is the main advantage of stack allocation?
A) Larger memory capacity
B) Faster allocation and deallocation
C) Automatic garbage collection
D) More control over memory

### 2. What happens if you forget to `delete` allocated memory?
A) Program crashes immediately
B) Memory leak occurs
C) Compiler error
D) Memory is automatically freed

### 3. What is RAII?
A) A design pattern for resource management
B) A garbage collection technique
C) A memory allocation algorithm
D) A debugging tool

### 4. What is the difference between `delete` and `delete[]`?
A) No difference
B) `delete[]` is for arrays
C) `delete` is faster
D) `delete[]` is deprecated

### 5. Which tool is commonly used to detect memory leaks?
A) gdb
B) Valgrind
C) make
D) gcc

### 6. What is the difference between `new` and `malloc`?
A) No difference
B) `new` calls constructors and returns typed pointers; `malloc` does not
C) `malloc` is faster
D) `new` is deprecated

### 7. What is placement `new`?
A) Allocating on a specific memory address
B) Allocating with custom alignment
C) Allocating on the stack
D) Allocating with a custom allocator

### 8. What is a memory leak sanitizer?
A) A tool that fixes memory leaks
B) A compiler feature (e.g., `-fsanitize=leak`) that detects leaks at runtime
C) A hardware feature
D) A static analysis tool only

### 9. What is the Rule of Five?
A) A class should have five methods
B) If you define any of destructor, copy/move constructor, or copy/move assignment, define all five
C) Five objects should be created per class
D) Five tests per function

### 10. What happens if you `delete` a void pointer?
A) Nothing — it works fine
B) Undefined behavior — the destructor is not called
C) Compiler error
D) Memory is freed correctly

## Answers
1. B) Faster allocation and deallocation
2. B) Memory leak occurs
3. A) A design pattern for resource management
4. B) `delete[]` is for arrays
5. B) Valgrind
6. B) `new` calls constructors and returns typed pointers; `malloc` does not
7. A) Allocating on a specific memory address
8. B) A compiler feature (e.g., `-fsanitize=leak`) that detects leaks at runtime
9. B) If you define any of destructor, copy/move constructor, or copy/move assignment, define all five
10. B) Undefined behavior — the destructor is not called
