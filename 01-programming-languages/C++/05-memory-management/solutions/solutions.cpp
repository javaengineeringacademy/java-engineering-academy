// Module 05: Memory Management — Solutions

#include <iostream>
#include <cstring>
#include <chrono>
#include <cassert>
#include <stdexcept>
#include <new>

// ============================================================================
// Exercise 1 Solution: RAII File Wrapper
// ============================================================================

class FileGuard {
    FILE* file_;
public:
    FileGuard(const char* path, const char* mode) : file_(fopen(path, mode)) {}

    ~FileGuard() {
        if (file_) fclose(file_);
    }

    FileGuard(const FileGuard&) = delete;
    FileGuard& operator=(const FileGuard&) = delete;

    FileGuard(FileGuard&& other) noexcept : file_(other.file_) {
        other.file_ = nullptr;
    }

    FileGuard& operator=(FileGuard&& other) noexcept {
        if (this != &other) {
            if (file_) fclose(file_);
            file_ = other.file_;
            other.file_ = nullptr;
        }
        return *this;
    }

    bool isOpen() const { return file_ != nullptr; }

    bool write(const char* data) {
        if (!file_) return false;
        return fputs(data, file_) != EOF;
    }

    std::string readLine() {
        if (!file_) return "";
        char buf[256];
        if (fgets(buf, sizeof(buf), file_)) {
            return std::string(buf);
        }
        return "";
    }
};

void exercise1() {
    std::cout << "\n=== Exercise 1: RAII File Wrapper ===\n";

    {
        FileGuard fg("test_raii.txt", "w");
        assert(fg.isOpen());
        fg.write("Hello, RAII!\n");
        fg.write("Second line\n");
    }

    FileGuard reader("test_raii.txt", "r");
    assert(reader.isOpen());
    std::string line1 = reader.readLine();
    std::string line2 = reader.readLine();
    std::cout << "Read: " << line1;
    std::cout << "Read: " << line2;

    FileGuard fg("test_move.txt", "w");
    FileGuard fg2 = std::move(fg);
    assert(!fg.isOpen());
    assert(fg2.isOpen());
    fg2.write("Moved!\n");

    remove("test_raii.txt");
    remove("test_move.txt");

    std::cout << "Exercise 1 passed!\n";
}

// ============================================================================
// Exercise 2 Solution: Dynamic Array (Rule of Three)
// ============================================================================

class IntArray {
    int* data_;
    size_t size_;
public:
    explicit IntArray(size_t size) : data_(new int[size]()), size_(size) {}

    ~IntArray() { delete[] data_; }

    IntArray(const IntArray& other)
        : data_(new int[other.size_]), size_(other.size_) {
        std::copy(other.data_, other.data_ + other.size_, data_);
    }

    IntArray& operator=(const IntArray& other) {
        if (this != &other) {
            delete[] data_;
            size_ = other.size_;
            data_ = new int[size_];
            std::copy(other.data_, other.data_ + other.size_, data_);
        }
        return *this;
    }

    int get(size_t index) const {
        if (index >= size_) throw std::out_of_range("Index out of range");
        return data_[index];
    }

    void set(size_t index, int value) {
        if (index >= size_) throw std::out_of_range("Index out of range");
        data_[index] = value;
    }

    size_t size() const { return size_; }
};

void exercise2() {
    std::cout << "\n=== Exercise 2: Dynamic Array ===\n";

    IntArray arr(5);
    assert(arr.size() == 5);
    arr.set(0, 10);
    arr.set(4, 50);
    assert(arr.get(0) == 10);
    assert(arr.get(4) == 50);

    IntArray arr2 = arr;
    arr2.set(0, 99);
    assert(arr.get(0) == 10);
    assert(arr2.get(0) == 99);

    IntArray arr3(1);
    arr3 = arr;
    assert(arr3.get(0) == 10);
    assert(arr3.size() == 5);

    std::cout << "Exercise 2 passed!\n";
}

// ============================================================================
// Exercise 3 Solution: Fixed Buffer
// ============================================================================

struct FixedBuffer {
    char* data;
    size_t size;

    FixedBuffer(size_t sz) : data(new char[sz]), size(sz) {
        std::memset(data, 0, sz);
    }

    ~FixedBuffer() {
        delete[] data;
    }

    FixedBuffer(const FixedBuffer& other)
        : data(new char[other.size]), size(other.size) {
        std::memcpy(data, other.data, size);
    }

    FixedBuffer& operator=(const FixedBuffer& other) {
        if (this != &other) {
            delete[] data;
            size = other.size;
            data = new char[size];
            std::memcpy(data, other.data, size);
        }
        return *this;
    }

    FixedBuffer(FixedBuffer&& other) noexcept
        : data(other.data), size(other.size) {
        other.data = nullptr;
        other.size = 0;
    }

    FixedBuffer& operator=(FixedBuffer&& other) noexcept {
        if (this != &other) {
            delete[] data;
            data = other.data;
            size = other.size;
            other.data = nullptr;
            other.size = 0;
        }
        return *this;
    }

    void print() const {
        std::cout << "Buffer(" << size << "): ";
        for (size_t i = 0; i < size; ++i) {
            std::cout << data[i];
        }
        std::cout << "\n";
    }
};

void exercise3() {
    std::cout << "\n=== Exercise 3: Fixed Buffer ===\n";

    FixedBuffer b1(10);
    b1.print();

    FixedBuffer b2 = b1;
    b2.data[0] = 'X';
    b1.print();
    b2.print();

    assert(b1.data[0] != 'X');
    assert(b2.data[0] == 'X');

    std::cout << "Exercise 3 passed!\n";
}

// ============================================================================
// Exercise 4 Solution: Pool Allocator
// ============================================================================

class PoolAllocator {
    struct Block { Block* next; };
    Block* free_list_;
    void* memory_;
    size_t block_size_;
    size_t count_;

public:
    PoolAllocator(size_t count, size_t block_size)
        : free_list_(nullptr), block_size_(block_size), count_(count) {
        memory_ = std::malloc(count * block_size);
        if (!memory_) throw std::bad_alloc();

        char* ptr = static_cast<char*>(memory_);
        free_list_ = reinterpret_cast<Block*>(ptr);
        Block* current = free_list_;
        for (size_t i = 1; i < count; ++i) {
            current->next = reinterpret_cast<Block*>(ptr + i * block_size);
            current = current->next;
        }
        current->next = nullptr;
    }

    ~PoolAllocator() {
        std::free(memory_);
    }

    void* allocate() {
        if (!free_list_) return nullptr;
        Block* block = free_list_;
        free_list_ = free_list_->next;
        return block;
    }

    void deallocate(void* ptr) {
        if (!ptr) return;
        Block* block = reinterpret_cast<Block*>(ptr);
        block->next = free_list_;
        free_list_ = block;
    }
};

void exercise4() {
    std::cout << "\n=== Exercise 4: Pool Allocator ===\n";

    PoolAllocator pool(100, sizeof(int));
    int* p1 = static_cast<int*>(pool.allocate());
    int* p2 = static_cast<int*>(pool.allocate());
    *p1 = 42;
    *p2 = 100;
    pool.deallocate(p1);
    int* p3 = static_cast<int*>(pool.allocate());
    assert(p3 == p1);

    std::cout << "Exercise 4 passed!\n";
}

// ============================================================================
// Exercise 5 Solution: Stack vs Heap Benchmark
// ============================================================================

struct SmallObj { int data[4]; };

void exercise5() {
    std::cout << "\n=== Exercise 5: Stack vs Heap Benchmark ===\n";

    const int N = 100000;

    auto start_stack = std::chrono::high_resolution_clock::now();
    {
        SmallObj* stack_objs = new SmallObj[N];
        for (int i = 0; i < N; ++i) stack_objs[i].data[0] = i;
        delete[] stack_objs;
    }
    auto end_stack = std::chrono::high_resolution_clock::now();
    auto stack_time = std::chrono::duration_cast<std::chrono::nanoseconds>(
        end_stack - start_stack).count();

    auto start_heap = std::chrono::high_resolution_clock::now();
    {
        SmallObj* heap_objs = new SmallObj[N];
        for (int i = 0; i < N; ++i) heap_objs[i].data[0] = i;
        delete[] heap_objs;
    }
    auto end_heap = std::chrono::high_resolution_clock::now();
    auto heap_time = std::chrono::duration_cast<std::chrono::nanoseconds>(
        end_heap - start_heap).count();

    std::cout << "Stack allocation: " << stack_time / 1000.0 << " us\n";
    std::cout << "Heap allocation:  " << heap_time / 1000.0 << " us\n";
}

// ============================================================================
// Main
// ============================================================================

int main() {
    std::cout << "=== Module 05: Memory Management Solutions ===\n";

    exercise1();
    exercise2();
    exercise3();
    exercise4();
    exercise5();

    std::cout << "\nAll exercises completed!\n";
    return 0;
}
