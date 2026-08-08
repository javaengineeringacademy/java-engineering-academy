// Module 05: Memory Management — Exercises
// Compile: g++ -std=c++17 -fsanitize=address -g -o exercises exercises.cpp

#include <iostream>
#include <cstring>
#include <cassert>

// ============================================================================
// Exercise 1: RAII File Wrapper
// Implement a simple RAII wrapper for a C-style file handle (FILE*).
// ============================================================================

// TODO: Implement a FileGuard class that:
// - Opens a file in the constructor
// - Closes it in the destructor
// - Provides read/write methods
// - Prevents copying (Rule of Five: delete copy ctor and assignment)
// - Allows moving (move ctor and move assignment)

// class FileGuard {
//     FILE* file_;
// public:
//     FileGuard(const char* path, const char* mode) : file_(nullptr) {
//         // TODO: Open file
//     }
//     ~FileGuard() {
//         // TODO: Close file if open
//     }
//     // TODO: Delete copy operations
//     // TODO: Implement move operations
//     bool isOpen() const { return file_ != nullptr; }
//     // TODO: Implement readLine() and write()
// };

void exercise1() {
    std::cout << "\n=== Exercise 1: RAII File Wrapper ===\n";

    // FileGuard fg("test.txt", "w");
    // assert(fg.isOpen());
    // fg.write("Hello, RAII!\n");
    // // fg is automatically closed when it goes out of scope

    // // Test move
    // FileGuard fg2 = std::move(fg);
    // assert(!fg.isOpen());  // fg no longer owns the file
    // assert(fg2.isOpen());  // fg2 owns it now
}

// ============================================================================
// Exercise 2: Dynamic Array (Rule of Three)
// Implement a IntArray class that manages a dynamically allocated array.
// ============================================================================

// TODO: Implement IntArray with:
// - Constructor: allocate array of given size, initialize to 0
// - Destructor: free the array
// - Copy constructor: deep copy
// - Copy assignment: deep copy with self-assignment check
// - get(index) and set(index, value) with bounds checking
// - size() method

// class IntArray {
//     int* data_;
//     size_t size_;
// public:
//     explicit IntArray(size_t size) : data_(new int[size]()), size_(size) {}
//     ~IntArray() { delete[] data_; }
//     // TODO: Copy constructor
//     // TODO: Copy assignment
//     // TODO: get, set, size
// };

void exercise2() {
    std::cout << "\n=== Exercise 2: Dynamic Array ===\n";

    // IntArray arr(5);
    // assert(arr.size() == 5);
    // arr.set(0, 10);
    // arr.set(4, 50);
    // assert(arr.get(0) == 10);
    // assert(arr.get(4) == 50);

    // // Test copy
    // IntArray arr2 = arr;
    // arr2.set(0, 99);
    // assert(arr.get(0) == 10);  // Original unchanged
    // assert(arr2.get(0) == 99);
}

// ============================================================================
// Exercise 3: Fix the Memory Bug
// This code has multiple memory bugs. Find and fix them.
// ============================================================================

struct Buffer {
    char* data;
    size_t size;

    Buffer(size_t sz) : data(new char[sz]), size(sz) {
        std::memset(data, 0, sz);
    }

    // BUG 1: Missing destructor — leaks memory
    // BUG 2: Copy constructor does shallow copy
    // BUG 3: No copy assignment operator

    void print() const {
        std::cout << "Buffer(" << size << "): ";
        for (size_t i = 0; i < size; ++i) {
            std::cout << data[i];
        }
        std::cout << "\n";
    }
};

// TODO: Fix all bugs. Apply Rule of Five.
// After fixing, uncomment the test code below.

void exercise3() {
    std::cout << "\n=== Exercise 3: Fix Memory Bugs ===\n";

    // Buffer b1(10);
    // b1.print();

    // Buffer b2 = b1;  // After fix: should be a deep copy
    // b2.data[0] = 'X';
    // b1.print();  // Should NOT show 'X'
    // b2.print();  // Should show 'X'
}

// ============================================================================
// Exercise 4: Pool Allocator
// Implement a simple fixed-size memory pool.
// ============================================================================

// TODO: Implement a PoolAllocator that:
// - Pre-allocates a block of N objects
// - allocate() returns a pointer from the pool
// - deallocate() returns memory to the pool
// - Tracks which blocks are free

// class PoolAllocator {
//     struct Block { Block* next; };
//     Block* free_list_;
//     void* memory_;
//     size_t block_size_;
// public:
//     PoolAllocator(size_t count, size_t block_size);
//     ~PoolAllocator();
//     void* allocate();
//     void deallocate(void* ptr);
// };

void exercise4() {
    std::cout << "\n=== Exercise 4: Pool Allocator ===\n";

    // PoolAllocator pool(100, sizeof(int));
    // int* p1 = static_cast<int*>(pool.allocate());
    // int* p2 = static_cast<int*>(pool.allocate());
    // *p1 = 42;
    // *p2 = 100;
    // pool.deallocate(p1);
    // int* p3 = static_cast<int*>(pool.allocate());  // Reuses p1's block
    // assert(p3 == p1);  // Same address
}

// ============================================================================
// Exercise 5: Stack vs Heap Benchmark
// Measure the difference between stack and heap allocation.
// ============================================================================

// TODO: Implement a function that:
// - Allocates N objects on the stack and returns the count
// - Allocates N objects on the heap and returns the count
// - Use std::chrono to measure both
// - Print the results

void exercise5() {
    std::cout << "\n=== Exercise 5: Stack vs Heap Benchmark ===\n";

    const int N = 100000;

    // TODO: Time stack allocation of N small objects
    // TODO: Time heap allocation of N small objects (new/delete)
    // TODO: Print comparison
}

int main() {
    std::cout << "=== Module 05: Memory Management Exercises ===\n";

    exercise1();
    exercise2();
    exercise3();
    exercise4();
    exercise5();

    return 0;
}
