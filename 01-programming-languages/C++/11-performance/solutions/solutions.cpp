// Module 11: Performance Optimization — Solutions
// Study these after attempting the exercises yourself.

#include <iostream>
#include <vector>
#include <array>
#include <chrono>
#include <numeric>
#include <algorithm>
#include <memory>
#include <string>
#include <cstring>
#include <cassert>
#include <sstream>
#include <list>
#include <atomic>

// ============================================================================
// Exercise 1 Solution: Move Semantics Benchmark
// ============================================================================

class LargeObject {
    std::vector<int> data;
public:
    LargeObject(size_t size) : data(size) {
        std::iota(data.begin(), data.end(), 0);
    }

    LargeObject(const LargeObject& other) : data(other.data) {}
    LargeObject(LargeObject&& other) noexcept : data(std::move(other.data)) {}

    size_t size() const { return data.size(); }
};

// ============================================================================
// Exercise 2 Solution: Cache-Friendly Access
// ============================================================================

const int MATRIX_SIZE = 1000;

void row_major_sum(double matrix[MATRIX_SIZE][MATRIX_SIZE], double &sum) {
    sum = 0;
    for (int i = 0; i < MATRIX_SIZE; i++) {
        for (int j = 0; j < MATRIX_SIZE; j++) {
            sum += matrix[i][j];
        }
    }
}

void column_major_sum(double matrix[MATRIX_SIZE][MATRIX_SIZE], double &sum) {
    sum = 0;
    for (int j = 0; j < MATRIX_SIZE; j++) {
        for (int i = 0; i < MATRIX_SIZE; i++) {
            sum += matrix[i][j];
        }
    }
}

// ============================================================================
// Exercise 3 Solution: Pool Allocator
// ============================================================================

class PoolAllocator {
    char *pool;
    size_t pool_size;
    size_t offset;

public:
    PoolAllocator(size_t size) : pool_size(size), offset(0) {
        pool = new char[pool_size];
    }

    ~PoolAllocator() {
        delete[] pool;
    }

    void* allocate(size_t size) {
        if (offset + size > pool_size) return nullptr;
        void* ptr = pool + offset;
        offset += size;
        return ptr;
    }

    void reset() {
        offset = 0;
    }

    size_t get_usage() const {
        return offset;
    }
};

// ============================================================================
// Exercise 4 Solution: Branch Prediction
// ============================================================================

void sort_and_process(std::vector<int> &data) {
    std::sort(data.begin(), data.end());
}

double process_sorted(const std::vector<int> &data) {
    long long sum = 0;
    for (int val : data) {
        if (val > 50) {
            sum += val;
        }
    }
    return static_cast<double>(sum);
}

// ============================================================================
// Exercise 5 Solution: Container Benchmarks
// ============================================================================

class Timer {
    std::chrono::high_resolution_clock::time_point start_time;
public:
    void start() { start_time = std::chrono::high_resolution_clock::now(); }
    double elapsed_ms() const {
        auto end = std::chrono::high_resolution_clock::now();
        return std::chrono::duration<double, std::milli>(end - start_time).count();
    }
};

void benchmark_vector_operations() {
    Timer timer;

    timer.start();
    std::vector<int> vec;
    for (int i = 0; i < 100000; i++) vec.push_back(i);
    double vec_time = timer.elapsed_ms();
    std::cout << "vector push_back 100k: " << vec_time << " ms\n";

    timer.start();
    std::vector<int> vec2;
    for (int i = 0; i < 1000; i++) vec2.insert(vec2.begin(), i);
    double vec_front = timer.elapsed_ms();
    std::cout << "vector insert front 1k: " << vec_front << " ms\n";
}

void benchmark_list_operations() {
    Timer timer;

    timer.start();
    std::list<int> lst;
    for (int i = 0; i < 100000; i++) lst.push_back(i);
    double list_time = timer.elapsed_ms();
    std::cout << "list push_back 100k: " << list_time << " ms\n";

    timer.start();
    std::list<int> lst2;
    for (int i = 0; i < 1000; i++) lst2.push_front(i);
    double list_front = timer.elapsed_ms();
    std::cout << "list push_front 1k: " << list_front << " ms\n";
}

// ============================================================================
// Exercise 6 Solution: Inline and Compiler Optimizations
// ============================================================================

inline int fast_square(int x) { return x * x; }

void loop_unrolling_demo(const std::vector<int> &data, long long &sum) {
    sum = 0;
    size_t n = data.size();
    size_t i = 0;
    for (; i + 3 < n; i += 4) {
        sum += data[i] + data[i+1] + data[i+2] + data[i+3];
    }
    for (; i < n; i++) {
        sum += data[i];
    }
}

// ============================================================================
// Exercise 8 Solution: Optimized Hot Loop
// ============================================================================

void hot_loop_original(const std::vector<int> &data, long long &sum) {
    sum = 0;
    for (size_t i = 0; i < data.size(); i++) {
        if (data[i] % 2 == 0) {
            sum += data[i];
        }
    }
}

void hot_loop_optimized(const std::vector<int> &data, long long &sum) {
    sum = 0;
    for (int val : data) {
        sum += val & ~1;
    }
}

// ============================================================================
// Exercise 9 Solution: Vector Reserve
// ============================================================================

void exercise9() {
    std::cout << "\n=== Exercise 9: Vector Reserve ===\n";
    Timer timer;

    timer.start();
    std::vector<int> v1;
    for (int i = 0; i < 1000000; i++) v1.push_back(i);
    std::cout << "Without reserve: " << timer.elapsed_ms() << " ms\n";

    timer.start();
    std::vector<int> v2;
    v2.reserve(1000000);
    for (int i = 0; i < 1000000; i++) v2.push_back(i);
    std::cout << "With reserve: " << timer.elapsed_ms() << " ms\n";
}

// ============================================================================
// Exercise 10 Solution: Lock-Free Stack Concept
// ============================================================================

template<typename T>
class LockFreeStack {
    struct Node {
        T data;
        Node* next;
    };
    std::atomic<Node*> head{nullptr};

public:
    void push(const T& value) {
        Node* new_node = new Node{value, nullptr};
        Node* old_head = head.load(std::memory_order_relaxed);
        do {
            new_node->next = old_head;
        } while (!head.compare_exchange_weak(old_head, new_node,
                    std::memory_order_release, std::memory_order_relaxed));
    }

    bool pop(T& value) {
        Node* old_head = head.load(std::memory_order_acquire);
        while (old_head && !head.compare_exchange_weak(old_head, old_head->next,
                    std::memory_order_acq_rel, std::memory_order_acquire)) {}
        if (!old_head) return false;
        value = old_head->data;
        delete old_head;
        return true;
    }
};

// ============================================================================
// Exercise 11 Solution: String Operations
// ============================================================================

void benchmark_string_concat() {
    Timer timer;
    const int N = 10000;

    timer.start();
    std::string s1;
    for (int i = 0; i < N; i++) {
        s1 += "hello";
    }
    std::cout << "Naive concat: " << timer.elapsed_ms() << " ms\n";

    timer.start();
    std::string s2;
    s2.reserve(N * 5);
    for (int i = 0; i < N; i++) {
        s2 += "hello";
    }
    std::cout << "Reserved concat: " << timer.elapsed_ms() << " ms\n";

    timer.start();
    std::ostringstream oss;
    for (int i = 0; i < N; i++) {
        oss << "hello";
    }
    std::string s3 = oss.str();
    std::cout << "ostringstream: " << timer.elapsed_ms() << " ms\n";
}

// ============================================================================
// Exercise 12 Solution: Algorithm Selection
// ============================================================================

void exercise12() {
    std::cout << "\n=== Exercise 12: Algorithm Selection ===\n";
    Timer timer;

    std::vector<int> data(100000);
    std::iota(data.begin(), data.end(), 0);

    timer.start();
    auto it = std::find(data.begin(), data.end(), 99999);
    std::cout << "Linear search: " << timer.elapsed_ms() << " ms\n";

    timer.start();
    auto it2 = std::lower_bound(data.begin(), data.end(), 99999);
    std::cout << "Binary search: " << timer.elapsed_ms() << " ms\n";
}

// ============================================================================
// Main
// ============================================================================

int main() {
    std::cout << "=== Module 11: Performance Solutions ===\n";
    Timer timer;

    // Exercise 1: Move semantics
    std::cout << "\n--- Exercise 1: Move Semantics ---\n";
    timer.start();
    for (int i = 0; i < 1000; i++) {
        LargeObject obj(1000000);
        LargeObject obj2 = obj;
    }
    std::cout << "Copy time: " << timer.elapsed_ms() << " ms\n";

    timer.start();
    for (int i = 0; i < 1000; i++) {
        LargeObject obj(1000000);
        LargeObject obj2 = std::move(obj);
    }
    std::cout << "Move time: " << timer.elapsed_ms() << " ms\n";

    // Exercise 2: Cache access
    std::cout << "\n--- Exercise 2: Cache Access ---\n";
    static double matrix[MATRIX_SIZE][MATRIX_SIZE];
    for (int i = 0; i < MATRIX_SIZE; i++)
        for (int j = 0; j < MATRIX_SIZE; j++)
            matrix[i][j] = i + j;
    double sum;
    timer.start();
    row_major_sum(matrix, sum);
    std::cout << "Row-major: " << timer.elapsed_ms() << " ms (sum=" << sum << ")\n";

    timer.start();
    column_major_sum(matrix, sum);
    std::cout << "Column-major: " << timer.elapsed_ms() << " ms (sum=" << sum << ")\n";

    // Exercise 3: Pool allocator
    std::cout << "\n--- Exercise 3: Pool Allocator ---\n";
    PoolAllocator pool(1024 * 1024);
    timer.start();
    for (int i = 0; i < 10000; i++) {
        pool.allocate(128);
    }
    std::cout << "Pool allocation: " << timer.elapsed_ms() << " ms\n";

    // Exercise 5: Container benchmarks
    std::cout << "\n--- Exercise 5: Container Performance ---\n";
    benchmark_vector_operations();
    benchmark_list_operations();

    // Exercise 9: Reserve
    exercise9();

    // Exercise 11: String operations
    std::cout << "\n--- Exercise 11: String Operations ---\n";
    benchmark_string_concat();

    // Exercise 12: Algorithm selection
    exercise12();

    std::cout << "\nAll exercises completed!\n";
    return 0;
}
