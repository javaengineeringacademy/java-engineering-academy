// Module 03: Advanced C++ — Solutions
// Study these after attempting the exercises yourself.

#include <iostream>
#include <string>
#include <vector>
#include <memory>
#include <algorithm>
#include <functional>
#include <cassert>
#include <type_traits>
#include <cstring>
#include <array>

// ============================================================================
// Exercise 1 Solution: Advanced Templates
// ============================================================================

constexpr long long power(long long base, int exp) {
    long long result = 1;
    for (int i = 0; i < exp; i++) {
        result *= base;
    }
    return result;
}

template<typename T, typename = void>
struct has_size : std::false_type {};

template<typename T>
struct has_size<T, std::void_t<decltype(std::declval<T>().size())>> : std::true_type {};

template<typename T>
T safe_add(T a, T b) {
    return a + b;
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
// Exercise 2 Solution: Smart Pointer Patterns
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

void exercise2() {
    std::cout << "\n=== Exercise 2: Smart Pointer Patterns ===\n";

    std::unique_ptr<Resource, std::function<void(Resource*)>> ptr(
        new Resource("Test"),
        [](Resource* r) {
            std::cout << "Custom deleter called\n";
            delete r;
        }
    );

    std::cout << "Resource name: " << ptr->name << "\n";
    std::cout << "Exercise 2: OK\n";
}

// ============================================================================
// Exercise 3 Solution: Move Semantics
// ============================================================================

class String {
    char* data_;
    size_t size_;
public:
    String(const char* str = "") : size_(std::strlen(str)), data_(new char[size_ + 1]) {
        std::strcpy(data_, str);
    }

    String(const String& other) : size_(other.size_), data_(new char[size_ + 1]) {
        std::strcpy(data_, other.data_);
    }

    String(String&& other) noexcept : data_(other.data_), size_(other.size_) {
        other.data_ = nullptr;
        other.size_ = 0;
    }

    String& operator=(const String& other) {
        if (this != &other) {
            delete[] data_;
            size_ = other.size_;
            data_ = new char[size_ + 1];
            std::strcpy(data_, other.data_);
        }
        return *this;
    }

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

template<typename T, typename... Args>
std::unique_ptr<T> create(Args&&... args) {
    return std::make_unique<T>(std::forward<Args>(args)...);
}

void exercise3() {
    std::cout << "\n=== Exercise 3: Move Semantics ===\n";

    String s1("Hello");
    assert(s1.size() == 5);
    assert(std::string(s1.c_str()) == "Hello");

    String s2 = s1;
    assert(s2.size() == 5);

    String s3 = std::move(s1);
    assert(s3.size() == 5);
    assert(s1.empty());

    std::cout << "String move: OK\n";
}

// ============================================================================
// Exercise 4 Solution: Lambda Captures
// ============================================================================

auto by_value_capture(int x) {
    return [x](int y) { return x + y; };
}

auto by_reference_capture(int& x) {
    return [&x]() { return ++x; };
}

auto mixed_capture(int a, const std::string& b) {
    return [a, &b]() { return std::to_string(a) + b; };
}

auto init_capture(std::vector<int> vec) {
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

    int counter = 0;
    auto increment = by_reference_capture(counter);
    increment();
    increment();
    assert(counter == 2);

    int num = 42;
    auto combined = mixed_capture(num, " is the answer");
    assert(combined() == "42 is the answer");

    std::vector<int> nums = {1, 2, 3, 4, 5};
    auto sum_func = init_capture(std::move(nums));
    assert(sum_func() == 15);

    std::cout << "All lambda capture tests passed!\n";
}

// ============================================================================
// Exercise 5 Solution: Compile-Time Programming
// ============================================================================

constexpr int str_len(const char* str) {
    int len = 0;
    while (*str++) len++;
    return len;
}

template<int N>
struct Factorial {
    static constexpr int value = N * Factorial<N - 1>::value;
};

template<>
struct Factorial<0> {
    static constexpr int value = 1;
};

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
    std::cout << "=== Module 03: Advanced C++ Solutions ===\n";

    exercise1();
    exercise2();
    exercise3();
    exercise4();
    exercise5();

    std::cout << "\nAll exercises completed!\n";
    return 0;
}
