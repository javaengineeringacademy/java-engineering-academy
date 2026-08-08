// Module 03: Advanced C++ — Exercises
// Complete each exercise by filling in the TODO sections.
// Compile: g++ -std=c++17 -Wall -Wextra -o exercises exercises.cpp

#include <iostream>
#include <string>
#include <vector>
#include <memory>
#include <algorithm>
#include <functional>
#include <cassert>
#include <type_traits>

// ============================================================================
// Exercise 1: Advanced Templates
// Implement a compile-time power function and type traits utilities.
// ============================================================================

// TODO: Implement constexpr power function
// power(2, 10) should return 1024 at compile time
constexpr long long power(long long base, int exp) {
    // Your code here
    return 0;
}

// TODO: Implement a template that checks if a type is a container
// has_size<T>::value should be true for std::vector, std::string, etc.
template<typename T, typename = void>
struct has_size : std::false_type {};

template<typename T>
struct has_size<T, std::void_t<decltype(std::declval<T>().size())>> : std::true_type {};

// TODO: Implement a template that returns the sum of two values
// Only compiles for types that support operator+
template<typename T>
T safe_add(T a, T b) {
    // Your code here
    return T{};
}

void exercise1() {
    std::cout << "\n=== Exercise 1: Advanced Templates ===\n";

    static_assert(power(2, 10) == 1024, "power(2,10) must be 1024");
    static_assert(power(5, 3) == 125, "power(5,3) must be 125");
    static_assert(power(10, 0) == 1, "power(10,0) must be 1");
    std::cout << "constexpr power: OK\n";

    static_assert(has_size<std::vector<int>>::value, "vector has size()");
    static_assert(has_size<std::string>::value, "string has size()");
    static_assert(!has_size<int>::value, "int does not have size()");
    std::cout << "has_size trait: OK\n";

    assert(safe_add(3, 4) == 7);
    assert(safe_add(1.5, 2.5) == 4.0);
    std::cout << "safe_add: OK\n";
}

// ============================================================================
// Exercise 2: Smart Pointer Patterns
// Implement custom deleters and observer patterns with smart pointers.
// ============================================================================

struct Resource {
    std::string name;
    Resource(const std::string& n) : name(n) {
        std::cout << "Resource created: " << name << "\n";
    }
    ~Resource() {
        std::cout << "Resource destroyed: " << name << "\n";
    }
};

// TODO: Create a unique_ptr with a custom deleter that logs destruction
// using a lambda deleter

// TODO: Implement an observer pattern using weak_ptr
// Observers watch a resource but don't own it

void exercise2() {
    std::cout << "\n=== Exercise 2: Smart Pointer Patterns ===\n";

    // TODO: Create unique_ptr with custom deleter
    // std::unique_ptr<Resource, std::function<void(Resource*)>> ptr(
    //     new Resource("Test"),
    //     [](Resource* r) { std::cout << "Custom delete\n"; delete r; }
    // );

    std::cout << "Exercise 2: OK\n";
}

// ============================================================================
// Exercise 3: Move Semantics and Perfect Forwarding
// ============================================================================

// TODO: Implement a String类 that uses move semantics
// - Constructor allocates and copies
// - Move constructor transfers ownership
// - Move assignment transfers ownership
// - Destructor frees memory

class String {
    char* data_;
    size_t size_;
public:
    // TODO: Constructor from C-string
    String(const char* str = "") : size_(std::strlen(str)), data_(new char[size_ + 1]) {
        std::strcpy(data_, str);
    }

    // TODO: Copy constructor (deep copy)
    String(const String& other) : size_(other.size_), data_(new char[size_ + 1]) {
        std::strcpy(data_, other.data_);
    }

    // TODO: Move constructor
    String(String&& other) noexcept : data_(other.data_), size_(other.size_) {
        other.data_ = nullptr;
        other.size_ = 0;
    }

    // TODO: Copy assignment
    String& operator=(const String& other) {
        if (this != &other) {
            delete[] data_;
            size_ = other.size_;
            data_ = new char[size_ + 1];
            std::strcpy(data_, other.data_);
        }
        return *this;
    }

    // TODO: Move assignment
    String& operator=(String&& other) noexcept {
        if (this != &other) {
            delete[] data_;
            data_ = other.data_;
            size_ = other.size_;
            other.data_ = nullptr;
            other.size_ = 0;
        }
        return *this;
    }

    ~String() { delete[] data_; }

    size_t size() const { return size_; }
    bool empty() const { return size_ == 0; }
    const char* c_str() const { return data_ ? data_ : ""; }
};

// TODO: Implement perfect forwarding factory
template<typename T, typename... Args>
std::unique_ptr<T> create(Args&&... args) {
    // Your code here
    return nullptr;
}

void exercise3() {
    std::cout << "\n=== Exercise 3: Move Semantics ===\n";

    String s1("Hello");
    assert(s1.size() == 5);
    assert(std::string(s1.c_str()) == "Hello");

    String s2 = s1;  // Copy
    assert(s2.size() == 5);

    String s3 = std::move(s1);  // Move
    assert(s3.size() == 5);
    assert(s1.empty());  // s1 is now empty

    std::cout << "String move: OK\n";
}

// ============================================================================
// Exercise 4: Lambda Capture Modes
// ============================================================================

// TODO: Implement functions demonstrating different lambda capture modes

auto by_value_capture(int x) {
    // Lambda that captures x by value
    // TODO: Return a lambda that returns x + captured value
    return [x](int y) { return x + y; };
}

auto by_reference_capture(int& x) {
    // Lambda that captures x by reference
    // TODO: Return a lambda that increments x
    return [&x]() { return ++x; };
}

auto mixed_capture(int a, const std::string& b) {
    // TODO: Capture a by value, b by reference
    // Return a lambda that combines them
    return [a, &b]() { return std::to_string(a) + b; };
}

auto init_capture(std::vector<int> vec) {
    // TODO: Capture vec by move (init capture)
    return [v = std::move(vec)]() mutable {
        int sum = 0;
        for (int x : v) sum += x;
        return sum;
    };
}

void exercise4() {
    std::cout << "\n=== Exercise 4: Lambda Captures ===\n";

    auto add5 = by_value_capture(5);
    assert(add5(3) == 8);
    std::cout << "by_value: OK\n";

    int counter = 0;
    auto increment = by_reference_capture(counter);
    increment();
    increment();
    assert(counter == 2);
    std::cout << "by_reference: OK\n";

    int num = 42;
    auto combined = mixed_capture(num, " is the answer");
    assert(combined() == "42 is the answer");
    std::cout << "mixed: OK\n";

    std::vector<int> nums = {1, 2, 3, 4, 5};
    auto sum_func = init_capture(std::move(nums));
    assert(sum_func() == 15);
    std::cout << "init_capture: OK\n";
}

// ============================================================================
// Exercise 5: Compile-Time Programming
// ============================================================================

// TODO: Implement a constexpr string length
constexpr int str_len(const char* str) {
    int len = 0;
    while (*str++) len++;
    return len;
}

// TODO: Implement a template that computes factorial at compile time
template<int N>
struct Factorial {
    static constexpr int value = N * Factorial<N - 1>::value;
};

template<>
struct Factorial<0> {
    static constexpr int value = 1;
};

// TODO: Implement a compile-time array sum
template<typename T, size_t N>
constexpr T array_sum(const std::array<T, N>& arr) {
    T sum = 0;
    for (size_t i = 0; i < N; i++) {
        sum += arr[i];
    }
    return sum;
}

void exercise5() {
    std::cout << "\n=== Exercise 5: Compile-Time Programming ===\n";

    static_assert(str_len("Hello") == 5, "str_len test");
    static_assert(str_len("") == 0, "str_len empty");
    std::cout << "constexpr str_len: OK\n";

    static_assert(Factorial<5>::value == 120, "Factorial<5>");
    static_assert(Factorial<0>::value == 1, "Factorial<0>");
    std::cout << "constexpr Factorial: OK\n";

    constexpr std::array<int, 4> arr = {1, 2, 3, 4};
    static_assert(array_sum(arr) == 10, "array_sum");
    std::cout << "constexpr array_sum: OK\n";
}

// ============================================================================
// Main
// ============================================================================

int main() {
    std::cout << "=== Module 03: Advanced C++ Exercises ===\n";

    exercise1();
    exercise2();
    exercise3();
    exercise4();
    exercise5();

    std::cout << "\nAll exercises completed!\n";
    return 0;
}
