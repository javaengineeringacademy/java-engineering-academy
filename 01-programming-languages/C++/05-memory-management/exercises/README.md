# Memory Management Exercises

## Exercise 1: Stack vs Heap
Compare stack and heap allocation performance.

**Requirements:**
- Allocate large arrays on stack and heap
- Measure allocation time
- Demonstrate stack overflow with deep recursion
- Show heap fragmentation

## Exercise 2: RAII Implementation
Implement RAII for file handling.

**Requirements:**
- Create a `FileHandler` class
- Open file in constructor
- Close file in destructor
- Demonstrate automatic cleanup with exceptions

## Exercise 3: Memory Pool
Implement a simple memory pool.

**Requirements:**
- Pre-allocate large memory block
- Allocate small objects from pool
- Track allocated and free blocks
- Implement deallocation

## Exercise 4: Memory Leak Detection
Use tools to detect memory leaks.

**Requirements:**
- Create program with intentional leaks
- Use Valgrind or AddressSanitizer
- Identify and fix leaks
- Document findings