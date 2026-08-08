// Module 15: Senior-Level C++ — Exercises
// Complete each exercise by filling in the TODO sections.
// Compile: g++ -std=c++17 -Wall -Wextra -o exercises exercises.cpp

#include <iostream>
#include <string>
#include <vector>
#include <memory>
#include <functional>
#include <cassert>
#include <thread>
#include <mutex>
#include <atomic>
#include <chrono>
#include <algorithm>
#include <numeric>

// ============================================================================
// Exercise 1: CRTP (Curiously Recurring Template Pattern)
// Implement static polymorphism using CRTP.
// ============================================================================

// TODO: Implement a Base class using CRTP that provides:
// - A common interface for all derived classes
// - Static dispatch (no virtual functions needed)
// - A method that calls the derived class's implementation

template<typename Derived>
class Base {
public:
    void interface() {
        static_cast<Derived*>(this)->implementation();
    }

    // TODO: Provide a default implementation
    void default_impl() {
        std::cout << "Base default implementation\n";
    }
};

// TODO: Create derived classes that implement their own version
class WidgetA : public Base<WidgetA> {
public:
    void implementation() {
        std::cout << "WidgetA specific implementation\n";
    }
};

class WidgetB : public Base<WidgetB> {
public:
    void implementation() {
        std::cout << "WidgetB specific implementation\n";
    }
};

// TODO: Create a derived class that uses the default implementation
class WidgetC : public Base<WidgetC> {
    // Uses default implementation from Base
};

void exercise1() {
    std::cout << "\n=== Exercise 1: CRTP ===\n";

    WidgetA a;
    WidgetB b;
    WidgetC c;

    a.interface();  // Should call WidgetA::implementation
    b.interface();  // Should call WidgetB::implementation
    c.interface();  // Should call Base::default_impl

    // Demonstrate compile-time polymorphism
    Base<WidgetA>* pa = &a;
    Base<WidgetB>* pb = &b;
    pa->interface();  // No virtual dispatch needed
    pb->interface();

    std::cout << "Exercise 1: OK\n";
}

// ============================================================================
// Exercise 2: Type Erasure
// Implement a simple type-erased container (like std::function).
// ============================================================================

// TODO: Implement a simple Any class that can hold any copyable type
class Any {
    struct Concept {
        virtual ~Concept() = default;
        virtual std::unique_ptr<Concept> clone() const = 0;
        virtual std::string to_string() const = 0;
    };

    template<typename T>
    struct Model : Concept {
        T data_;
        Model(T value) : data_(std::move(value)) {}
        std::unique_ptr<Concept> clone() const override {
            return std::make_unique<Model>(data_);
        }
        std::string to_string() const override {
            return std::to_string(data_);
        }
    };

    std::unique_ptr<Concept> content_;

public:
    // TODO: Constructor for any copyable type
    template<typename T>
    Any(T value) : content_(std::make_unique<Model<T>>(std::move(value))) {}

    // TODO: Copy constructor
    Any(const Any& other) : content_(other.content_->clone()) {}

    // TODO: Move constructor
    Any(Any&& other) noexcept : content_(std::move(other.content_)) {}

    // TODO: Assignment operators
    Any& operator=(const Any& other) {
        content_ = other.content_->clone();
        return *this;
    }

    Any& operator=(Any&& other) noexcept {
        content_ = std::move(other.content_);
        return *this;
    }

    // TODO: Get the stored value as a specific type
    template<typename T>
    T& get() {
        auto model = dynamic_cast<Model<T>*>(content_.get());
        if (!model) throw std::bad_cast();
        return model->data_;
    }

    std::string to_string() const {
        return content_ ? content_->to_string() : "empty";
    }
};

void exercise2() {
    std::cout << "\n=== Exercise 2: Type Erasure ===\n";

    Any a(42);
    Any b(std::string("Hello"));
    Any c(3.14);

    assert(a.get<int>() == 42);
    assert(b.get<std::string>() == "Hello");
    assert(c.get<double>() == 3.14);

    // Test copy
    Any d = a;
    assert(d.get<int>() == 42);

    // Test assignment
    d = b;
    assert(d.get<std::string>() == "Hello");

    std::cout << "Any values: " << a.to_string() << ", " << b.to_string() << "\n";
    std::cout << "Exercise 2: OK\n";
}

// ============================================================================
// Exercise 3: Lock-Free Data Structure
// Implement a simple lock-free stack.
// ============================================================================

template<typename T>
class LockFreeStack {
    struct Node {
        T data;
        Node* next;
    };
    std::atomic<Node*> head_{nullptr};

public:
    void push(const T& value) {
        Node* new_node = new Node{value, nullptr};
        Node* old_head = head_.load(std::memory_order_relaxed);
        do {
            new_node->next = old_head;
        } while (!head_.compare_exchange_weak(old_head, new_node,
                    std::memory_order_release, std::memory_order_relaxed));
    }

    bool pop(T& value) {
        Node* old_head = head_.load(std::memory_order_acquire);
        while (old_head && !head_.compare_exchange_weak(old_head, old_head->next,
                    std::memory_order_acq_rel, std::memory_order_acquire)) {}
        if (!old_head) return false;
        value = old_head->data;
        delete old_head;
        return true;
    }

    bool empty() const {
        return head_.load(std::memory_order_acquire) == nullptr;
    }
};

void exercise3() {
    std::cout << "\n=== Exercise 3: Lock-Free Stack ===\n";

    LockFreeStack<int> stack;

    // Test basic operations
    stack.push(1);
    stack.push(2);
    stack.push(3);

    int value;
    stack.pop(value);
    assert(value == 3);
    stack.pop(value);
    assert(value == 2);
    stack.pop(value);
    assert(value == 1);
    assert(stack.empty());

    // Test concurrent access
    const int N = 1000;
    std::vector<std::thread> threads;

    for (int i = 0; i < N; i++) {
        threads.emplace_back([&stack, i]() {
            stack.push(i);
        });
    }
    for (auto& t : threads) t.join();

    int count = 0;
    while (stack.pop(value)) {
        count++;
    }
    assert(count == N);
    std::cout << "Concurrent push/pop: " << N << " items\n";

    std::cout << "Exercise 3: OK\n";
}

// ============================================================================
// Exercise 4: Expression Templates
// Implement a simple expression template for vector addition.
// ============================================================================

// TODO: Implement a vector class with expression templates
template<typename T>
class Vector {
    std::vector<T> data_;
public:
    explicit Vector(size_t size) : data_(size) {}
    Vector(std::initializer_list<T> init) : data_(init) {}

    size_t size() const { return data_.size(); }
    T operator[](size_t i) const { return data_[i]; }
    T& operator[](size_t i) { return data_[i]; }

    // TODO: Element-wise addition with expression template
    template<typename Other>
    Vector<T> operator+(const Other& other) const {
        Vector<T> result(size());
        for (size_t i = 0; i < size(); i++) {
            result[i] = data_[i] + other[i];
        }
        return result;
    }
};

void exercise4() {
    std::cout << "\n=== Exercise 4: Expression Templates ===\n";

    Vector<int> a = {1, 2, 3};
    Vector<int> b = {4, 5, 6};

    // This triggers evaluation
    Vector<int> c = a + b;
    assert(c[0] == 5 && c[1] == 7 && c[2] == 9);

    // Chain operations (simplified - real expr templates would be lazy)
    Vector<int> d = a + b + c;
    assert(d[0] == 10 && d[1] == 14 && d[2] == 18);

    std::cout << "a + b: " << c[0] << " " << c[1] << " " << c[2] << "\n";
    std::cout << "Exercise 4: OK\n";
}

// ============================================================================
// Exercise 5: SFINAE and Type Traits
// Implement compile-time function dispatch using SFINAE.
// ============================================================================

// TODO: Implement a function that behaves differently based on type properties
template<typename T>
typename std::enable_if<std::is_integral<T>::value, T>::type
safe_multiply(T a, T b) {
    std::cout << "  Integer multiply: ";
    return a * b;
}

template<typename T>
typename std::enable_if<std::is_floating_point<T>::value, T>::type
safe_multiply(T a, T b) {
    std::cout << "  Float multiply: ";
    return a * b;
}

// TODO: Implement a function that only works with containers
template<typename T>
typename std::enable_if<has_size<T>::value, size_t>::type
get_size(const T& container) {
    return container.size();
}

// has_size trait (from earlier module)
template<typename T, typename = void>
struct has_size : std::false_type {};

template<typename T>
struct has_size<T, std::void_t<decltype(std::declval<T>().size())>> : std::true_type {};

// TODO: Implement conditional return type
template<typename T>
auto safe_get(const std::vector<T>& vec, size_t index)
    -> typename std::enable_if<true, T>::type {
    if (index >= vec.size()) throw std::out_of_range("Index out of range");
    return vec[index];
}

void exercise5() {
    std::cout << "\n=== Exercise 5: SFINAE ===\n";

    // Test integral types
    assert(safe_multiply(3, 4) == 12);
    std::cout << "3 * 4 = " << safe_multiply(3, 4) << "\n";

    // Test floating point types
    assert(safe_multiply(2.5, 4.0) == 10.0);
    std::cout << "2.5 * 4.0 = " << safe_multiply(2.5, 4.0) << "\n";

    // Test has_size
    static_assert(has_size<std::vector<int>>::value, "vector has size()");
    static_assert(!has_size<int>::value, "int has no size()");
    std::cout << "has_size: OK\n";

    std::cout << "Exercise 5: OK\n";
}

// ============================================================================
// Exercise 6: Policy-Based Design
// Implement a generic container with configurable policies.
// ============================================================================

// TODO: Define storage policies
template<typename T>
class HeapStorage {
    std::vector<T> data_;
public:
    void push(const T& value) { data_.push_back(value); }
    T pop() {
        T val = data_.back();
        data_.pop_back();
        return val;
    }
    size_t size() const { return data_.size(); }
    const T& top() const { return data_.back(); }
};

template<typename T, size_t N>
class StackStorage {
    std::array<T, N> data_;
    size_t size_ = 0;
public:
    void push(const T& value) {
        if (size_ >= N) throw std::overflow_error("Stack full");
        data_[size_++] = value;
    }
    T pop() {
        if (size_ == 0) throw std::underflow_error("Stack empty");
        return data_[--size_];
    }
    size_t size() const { return size_; }
    const T& top() const { return data_[size_ - 1]; }
};

// TODO: Define sorting policies
struct AscendingSort {
    template<typename Container>
    static void sort(Container& c) {
        std::sort(c.begin(), c.end());
    }
};

struct DescendingSort {
    template<typename Container>
    static void sort(Container& c) {
        std::sort(c.begin(), c.end(), std::greater<typename Container::value_type>());
    }
};

// TODO: Container with configurable policies
template<typename T, template<typename> class StoragePolicy>
class Stack {
    StoragePolicy<T> storage_;
public:
    void push(const T& value) { storage_.push(value); }
    T pop() { return storage_.pop(); }
    size_t size() const { return storage_.size(); }
};

void exercise6() {
    std::cout << "\n=== Exercise 6: Policy-Based Design ===\n";

    Stack<int, HeapStorage> heap_stack;
    heap_stack.push(1);
    heap_stack.push(2);
    assert(heap_stack.pop() == 2);

    Stack<int, StackStorage> fixed_stack;  // Uses default StackStorage< int, 100>
    fixed_stack.push(10);
    fixed_stack.push(20);
    assert(fixed_stack.pop() == 20);

    std::vector<int> v = {3, 1, 4, 1, 5};
    AscendingSort::sort(v);
    assert(v[0] == 1);

    DescendingSort::sort(v);
    assert(v[0] == 5);

    std::cout << "Exercise 6: OK\n";
}

// ============================================================================
// Main
// ============================================================================

int main() {
    std::cout << "=== Module 15: Senior-Level C++ Exercises ===\n";

    exercise1();
    exercise2();
    exercise3();
    exercise4();
    exercise5();
    exercise6();

    std::cout << "\nAll exercises completed!\n";
    return 0;
}
