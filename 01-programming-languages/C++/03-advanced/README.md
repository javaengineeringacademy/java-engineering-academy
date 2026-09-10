# Advanced C++ — C++

## Overview

Advanced C++ covers techniques that go beyond basic language features: CRTP for static polymorphism, type erasure for runtime polymorphism without inheritance, perfect forwarding for generic factories, and advanced constexpr programming for compile-time computation.

### Why It Matters

When you've mastered the fundamentals and OOP, you hit a ceiling: code that works but isn't efficient, flexible, or maintainable enough for production systems. Advanced C++ techniques like CRTP, type erasure, and perfect forwarding let you write zero-overhead abstractions that are both generic and fast. Without these skills, you're stuck choosing between template complexity and runtime overhead.

### What It Is

Advanced C++ covers techniques that go beyond basic language features: CRTP for static polymorphism, type erasure for runtime polymorphism without inheritance, perfect forwarding for generic factories, and advanced constexpr programming for compile-time computation.

## Learning Objectives

By the end of this module, you will be able to:

- Implement CRTP for static polymorphism and mixin classes
- Design type-safe type erasure patterns (std::function, std::any)
- Use perfect forwarding and universal references correctly
- Write compile-time computation with constexpr and consteval
- Apply policy-based design for flexible component configuration
- Avoid common pitfalls: template bloat, SFINAE failures, dangling references
- Choose between static and runtime polymorphism based on requirements

## Prerequisites

- Module 01: C++ Fundamentals (variables, functions, pointers)
- Module 02: Object-Oriented Programming (inheritance, virtual functions)
- Module 03: Templates (function/class templates, specialization)

## History

| Year | Feature | Impact |
|------|---------|--------|
| 1994 | CRTP first described by Jim Coplien | Static polymorphism without virtual dispatch |
| 1998 | std::auto_ptr (limited type erasure) | First standard library type erasure |
| 2011 | Move semantics, std::function, std::any | Modern type erasure and forwarding |
| 2014 | Generic lambdas | Simplified perfect forwarding in practice |
| 2017 | constexpr if, std::optional, std::variant | Compile-time branching, better type erasure |
| 2020 | Concepts, consteval, non-static data members | Cleaner template constraints, immediate compilation |

## Production Notes

### Compiler Support
CRTP, type erasure, and perfect forwarding are well-supported in GCC 7+, Clang 5+, MSVC 2017+. However, error messages for template-heavy code can be cryptic — use C++20 concepts to improve diagnostics.

### ABI Stability
Type erasure through `std::function` and `std::any` have stable ABI across compiler versions. Custom type erasure implementations may not — be careful when sharing libraries across compilation boundaries.

## Internal Working

### CRTP Internal Mechanism
CRTP works by having the base class template accept the derived class as a template argument. When `area()` is called, the base class performs a `static_cast<Derived*>(this)` to obtain a pointer to the derived type, then calls the derived implementation. This cast is safe because the base is only ever instantiated with the correct derived type. The compiler resolves the call at compile time — no vtable, no indirect call, no runtime overhead.

### Type Erasure Internals
Type erasure uses a two-layer indirection: a `Concept` base class defines the interface, and a `Model<T>` derived class stores the concrete object. The wrapper holds a `unique_ptr<Concept>` and delegates calls through virtual dispatch on `Model`. Small buffer optimization avoids the heap allocation by storing the `Model` object in a fixed-size aligned buffer within the wrapper itself. The buffer size is a compile-time constant — typically 64 bytes.

### Perfect Forwarding Mechanism
Universal references (`T&&`) use reference collapsing rules: `T&&` with `T = int&` collapses to `int&`, and with `T = int` yields `int&&`. `std::forward<T>(x)` casts `x` to `T&&` only when `T` is deduced as a non-reference type, preserving the original value category. This happens entirely at compile time — no runtime branching.

### Constexpr Evaluation
The compiler evaluates `constexpr` functions during compilation when arguments are constant expressions. The function body must contain only expressions valid at compile time: no I/O, no mutation of non-local state, no dynamic allocation (until C++20). When arguments are runtime values, the function executes normally at runtime.

### Policy-Based Design Internals
Policies are passed as template parameters. The main class inherits privately from each policy, giving it direct access to policy methods. The compiler instantiates only the policies used — `FileStorage` for production, `MemoryStorage` for tests. Each instantiation is a separate compilation, enabling full inlining and dead code elimination.

```
Call flow: CRTP static_cast → direct method call
Type erasure: wrapper → unique_ptr<Concept> → virtual dispatch → Model<T>::method
Forwarding: T&& → reference collapsing → std::forward → target function
Constexpr: compile-time AST evaluation → constant in binary
```

## Syntax

### CRTP Syntax
```cpp
// Base class template with derived as parameter
template <typename Derived>
class Base {
public:
    void interface() {
        static_cast<Derived*>(this)->implementation();
    }
};

// Derived class passes itself as template argument
class Derived : public Base<Derived> {
public:
    void implementation() { /* ... */ }
};
```

### Type Erasure Syntax
```cpp
// Concept (interface) definition
struct Concept {
    virtual ~Concept() = default;
    virtual void call() const = 0;
    virtual std::unique_ptr<Concept> clone() const = 0;
};

// Model (concrete storage)
template <typename T>
struct Model : Concept {
    T value_;
    explicit Model(T v) : value_(std::move(v)) {}
    void call() const override { /* use value_ */ }
    std::unique_ptr<Concept> clone() const override {
        return std::make_unique<Model>(value_);
    }
};

// Wrapper class
class Erased {
    std::unique_ptr<Concept> impl_;
public:
    template <typename T>
    Erased(T value) : impl_(std::make_unique<Model<T>>(std::move(value))) {}
};
```

### Perfect Forwarding Syntax
```cpp
// Universal reference parameter
template <typename T>
void wrapper(T&& arg) {
    target(std::forward<T>(arg));  // Preserves value category
}

// Variadic perfect forwarding
template <typename T, typename... Args>
std::unique_ptr<T> make(Args&&... args) {
    return std::make_unique<T>(std::forward<Args>(args)...);
}
```

### Constexpr Syntax
```cpp
// C++11/14: simple constexpr function
constexpr int square(int x) { return x * x; }

// C++14: mutable local variables in constexpr
constexpr int fibonacci(int n) {
    int a = 0, b = 1;
    for (int i = 0; i < n; ++i) {
        int temp = a;
        a = b;
        b = temp + b;
    }
    return a;
}

// C++17: constexpr if
template <typename T>
auto process(T val) {
    if constexpr (std::is_integral_v<T>)
        return val * 2;
    else
        return val * 2.5;
}

// C++20: consteval (must run at compile time)
consteval int compile_time_only(int x) { return x + 1; }
```

### Policy-Based Design Syntax
```cpp
// Policy structs
struct LoggingPolicy {
    void log(const std::string& msg) { std::cout << msg; }
};
struct StoragePolicy {
    void store(const std::string& k, const std::string& v) { /* ... */ }
};

// Class using policies via private inheritance
template <typename Logger, typename Storage>
class Service : private Logger, private Storage {
public:
    void run() {
        this->log("running");
        this->store("key", "value");
    }
};
```

## Examples

### Easy: CRTP Static Counter
```cpp
#include <iostream>

template <typename Derived>
class Counter {
    static inline int count_ = 0;
public:
    Counter() { ++count_; }
    ~Counter() { --count_; }
    static int count() { return count_; }
};

class Dog : public Counter<Dog> {};
class Cat : public Counter<Cat> {};

int main() {
    Dog d1, d2;
    Cat c1;
    std::cout << "Dogs: " << Dog::count() << "\n";  // 2
    std::cout << "Cats: " << Cat::count() << "\n";  // 1
}
```

### Medium: Perfect Forwarding Factory
```cpp
#include <iostream>
#include <memory>
#include <string>
#include <utility>

class Widget {
    std::string name_;
    int value_;
public:
    Widget(std::string name, int value)
        : name_(std::move(name)), value_(value) {
        std::cout << "Constructed: " << name_ << "=" << value_ << "\n";
    }
};

template <typename T, typename... Args>
std::unique_ptr<T> create(Args&&... args) {
    return std::make_unique<T>(std::forward<Args>(args)...);
}

int main() {
    auto w1 = create<Widget>("hello", 42);
    std::string key = "world";
    auto w2 = create<Widget>(key, 100);  // lvalue forwarded
    auto w3 = create<Widget>(std::string("temp"), 99);  // rvalue forwarded
}
```

### Hard: Custom Type Erasure with SBO
```cpp
#include <iostream>
#include <memory>
#include <cstring>

template <typename Interface, size_t BufSize = 64>
class SBOContainer {
    struct Concept {
        virtual ~Concept() = default;
        virtual void invoke() const = 0;
        virtual std::unique_ptr<Concept> clone() const = 0;
    };

    template <typename T>
    struct Model : Concept {
        T value_;
        explicit Model(T v) : value_(std::move(v)) {}
        void invoke() const override { value_(); }
        std::unique_ptr<Concept> clone() const override {
            return std::make_unique<Model>(value_);
        }
    };

    alignas(alignof(void*)) char buf_[BufSize];
    std::unique_ptr<Concept> heap_;
    Concept* ptr_;

    template <typename T>
    void store(T value) {
        using M = Model<T>;
        if constexpr (sizeof(M) <= BufSize && alignof(M) <= alignof(void*)) {
            ptr_ = new (buf_) M(std::move(value));
            heap_.reset();
        } else {
            heap_ = std::make_unique<M>(std::move(value));
            ptr_ = heap_.get();
        }
    }

public:
    template <typename T>
    SBOContainer(T value) { store(std::move(value)); }

    SBOContainer(const SBOContainer& o) : ptr_(nullptr) {
        if (o.ptr_) {
            heap_ = o.ptr_->clone();
            ptr_ = heap_.get();
        }
    }

    SBOContainer& operator=(const SBOContainer& o) {
        if (this != &o) {
            SBOContainer tmp(o);
            swap(tmp);
        }
        return *this;
    }

    void swap(SBOContainer& o) {
        std::swap(buf_, o.buf_);
        std::swap(heap_, o.heap_);
        std::swap(ptr_, o.ptr_);
    }

    void invoke() const { if (ptr_) ptr_->invoke(); }
};

int main() {
    SBOContainer<std::function<void()>> a([]{ std::cout << "a\n"; });
    SBOContainer<std::function<void()>> b(a);  // copy
    a.invoke();
    b.invoke();
}
```

### Enterprise: Policy-Based Database with Type Erasure
```cpp
#include <iostream>
#include <string>
#include <unordered_map>
#include <memory>
#include <vector>
#include <functional>

struct ConsoleLogger {
    void log(const std::string& msg) const { std::cout << "[LOG] " << msg << "\n"; }
};
struct NullLogger { void log(const std::string&) const {} };

struct MemoryStorage {
    std::unordered_map<std::string, std::string> data_;
    void store(const std::string& k, const std::string& v) { data_[k] = v; }
    std::optional<std::string> load(const std::string& k) const {
        auto it = data_.find(k);
        return it != data_.end() ? std::make_optional(it->second) : std::nullopt;
    }
};

template <typename Logger, typename Storage>
class Database : private Logger, private Storage {
    std::vector<std::string> audit_;
public:
    using Logger::log;
    using Storage::store;
    using Storage::load;

    void save(const std::string& key, const std::string& value) {
        log("Saving " + key);
        store(key, value);
        audit_.push_back("save:" + key);
    }

    std::optional<std::string> get(const std::string& key) {
        log("Loading " + key);
        return load(key);
    }

    const std::vector<std::string>& history() const { return audit_; }
};

int main() {
    Database<ConsoleLogger, MemoryStorage> db;
    db.save("user:1", "Alice");
    db.save("user:2", "Bob");

    if (auto val = db.get("user:1"))
        std::cout << "Found: " << *val << "\n";

    for (const auto& entry : db.history())
        std::cout << "Audit: " << entry << "\n";
}
```

## Architecture: How Advanced C++ Fits Together

```
┌─────────────────────────────────────────────────────────────┐
│                   Advanced C++ Techniques                     │
├───────────────┬───────────────┬─────────────────────────────┤
│     CRTP      │  Type Erasure │   Perfect Forwarding        │
│ (Static       │ (Runtime      │   (Generic                  │
│  Polymorphism)│  Polymorphism)│    Factories)               │
├───────────────┴───────────────┴─────────────────────────────┤
│              Constexpr Programming (C++17/20)                │
├─────────────────────────────────────────────────────────────┤
│     Advanced Template Techniques & Policy-Based Design       │
└─────────────────────────────────────────────────────────────┘
```

## CRTP (Curiously Recurring Template Pattern)

### The Problem CRTP Solves

Virtual functions add overhead (vtable lookup, indirect call). When you need polymorphism but want zero overhead, CRTP gives you static polymorphism — the derived class is known at compile time.

```cpp
// CRTP base — static polymorphism
template <typename Derived>
class Shape {
public:
    double area() const {
        return static_cast<Derived*>(this)->area_impl();
    }

    void draw() const {
        static_cast<Derived*>(this)->draw_impl();
    }
};

// Derived class — no virtual functions needed
class Circle : public Shape<Circle> {
    double radius_;
public:
    explicit Circle(double r) : radius_(r) {}

    double area_impl() const { return 3.14159 * radius_ * radius_; }
    void draw_impl() const { /* draw circle */ }
};

class Rectangle : public Shape<Rectangle> {
    double width_, height_;
public:
    Rectangle(double w, double h) : width_(w), height_(h) {}

    double area_impl() const { return width_ * height_; }
    void draw_impl() const { /* draw rectangle */ }
};

// Usage — compile-time dispatch, no vtable
template <typename T>
void print_area(const Shape<T>& shape) {
    std::cout << "Area: " << shape.area() << "\n";
}
```

### CRTP with Mixins

```cpp
// Mixin that adds comparison operators
template <typename Derived>
class Comparable {
public:
    friend bool operator!=(const Derived& a, const Derived& b) {
        return !(a == b);
    }

    friend bool operator<(const Derived& a, const Derived& b) {
        return a.compare(b) < 0;
    }

    friend bool operator>(const Derived& a, const Derived& b) {
        return b < a;
    }
};

class Point : public Comparable<Point> {
    int x_, y_;
public:
    Point(int x, int y) : x_(x), y_(y) {}

    bool operator==(const Point& other) const {
        return x_ == other.x_ && y_ == other.y_;
    }

    int compare(const Point& other) const {
        if (x_ != other.x_) return x_ - other.x_;
        return y_ - other.y_;
    }
};
```

## Type Erasure

### The Problem Type Erasure Solves

You want to store different types in a container without inheritance — like `std::function` storing any callable. Type erasure hides the concrete type behind a uniform interface.

```cpp
#include <memory>
#include <iostream>

// Type-erased wrapper for any printable object
class Printable {
    struct Concept {
        virtual ~Concept() = default;
        virtual void print() const = 0;
        virtual std::unique_ptr<Concept> clone() const = 0;
    };

    template <typename T>
    struct Model : Concept {
        T value_;
        explicit Model(T v) : value_(std::move(v)) {}
        void print() const override { std::cout << value_ << "\n"; }
        std::unique_ptr<Concept> clone() const override {
            return std::make_unique<Model>(value_);
        }
    };

    std::unique_ptr<Concept> impl_;

public:
    template <typename T>
    Printable(T value) : impl_(std::make_unique<Model<T>>(std::move(value))) {}

    Printable(const Printable& other) : impl_(other.impl_->clone()) {}
    Printable& operator=(const Printable& other) {
        impl_ = other.impl_->clone();
        return *this;
    }

    void print() const { impl_->print(); }
};

// Usage — stores int, double, or string without inheritance
void print_all(const std::vector<Printable>& items) {
    for (const auto& item : items) {
        item.print();
    }
}
```

### std::function as Type Erasure

```cpp
#include <functional>
#include <iostream>

// std::function is the canonical type erasure example
std::function<int(int, int)> get_operation(char op) {
    switch (op) {
        case '+': return [](int a, int b) { return a + b; };
        case '-': return [](int a, int b) { return a - b; };
        case '*': return [](int a, int b) { return a * b; };
        default:  return [](int, int) { return 0; };
    }
}

// Custom type erasure for small objects (small buffer optimization)
template <typename Interface, size_t BufferSize = 64>
class SmallBuffer {
    alignas(void*) char buffer_[BufferSize];
    std::unique_ptr<Interface> heap_ptr_;
    Interface* ptr_;

    template <typename T>
    void store(T value) {
        if (sizeof(Model<T>) <= BufferSize) {
            ptr_ = new (buffer_) Model<T>(std::move(value));
        } else {
            heap_ptr_ = std::make_unique<Model<T>>(std::move(value));
            ptr_ = heap_ptr_.get();
        }
    }

    // ... Concept and Model similar to above
};
```

## Perfect Forwarding

### The Problem Perfect Forwarding Solves

When writing generic wrappers (like `std::make_unique`), you need to forward arguments exactly as received — preserving value categories and const-qualification.

```cpp
#include <iostream>
#include <string>
#include <utility>

// Bad: loses value category
void wrapper_bad(const int& x) {
    // Can't forward to move-capable function
}

// Good: perfect forwarding
template <typename T>
void wrapper_good(T&& x) {
    // x is forwarded with its original value category
    target(std::forward<T>(x));
}

void target(int&& x) {
    std::cout << "Moved: " << x << "\n";
}

void target(const int& x) {
    std::cout << "Lvalue: " << x << "\n";
}

// Real-world example: factory function
template <typename T, typename... Args>
std::unique_ptr<T> make(Args&&... args) {
    return std::make_unique<T>(std::forward<Args>(args)...);
}

// Usage
class Widget {
    std::string name_;
    int value_;
public:
    Widget(std::string name, int value)
        : name_(std::move(name)), value_(value) {}
};

auto w = make<Widget>("hello", 42);  // Perfect forwarding
```

## Constexpr Programming

### Compile-Time Computation

```cpp
// Compile-time string hashing
constexpr uint32_t hash_string(std::string_view str) {
    uint32_t hash = 2166136261u;  // FNV offset basis
    for (char c : str) {
        hash ^= static_cast<uint32_t>(c);
        hash *= 16777619u;  // FNV prime
    }
    return hash;
}

// Compile-time lookup table
template <typename T, size_t N>
struct ConstexprArray {
    std::array<T, N> data;

    constexpr T operator[](size_t index) const {
        return data[index];
    }
};

// constexpr factorial
constexpr unsigned long long factorial(int n) {
    return (n <= 1) ? 1 : n * factorial(n - 1);
}

static_assert(factorial(10) == 3628800);

// C++17: constexpr if for compile-time branching
template <typename T>
auto process(T value) {
    if constexpr (std::is_integral_v<T>) {
        return value * 2;
    } else if constexpr (std::is_floating_point_v<T>) {
        return value * 2.5;
    } else {
        return value;
    }
}
```

## Policy-Based Design

```cpp
// Policies as template parameters
template <typename StoragePolicy, typename LoggingPolicy>
class Database : private StoragePolicy, private LoggingPolicy {
public:
    void save(const std::string& key, const std::string& value) {
        this->log("Saving: " + key);
        this->store(key, value);
    }
};

// Different policies
struct FileStorage {
    void store(const std::string& key, const std::string& value) {
        // Write to file
    }
};

struct MemoryStorage {
    std::unordered_map<std::string, std::string> data_;
    void store(const std::string& key, const std::string& value) {
        data_[key] = value;
    }
};

struct ConsoleLogger {
    void log(const std::string& msg) { std::cout << msg << "\n"; }
};

struct NullLogger {
    void log(const std::string&) {}  // No-op
};

// Usage — different behavior at compile time
using ProductionDB = Database<FileStorage, ConsoleLogger>;
using TestDB = Database<MemoryStorage, NullLogger>;
```

## Engineering Decision Framework

### When to Use Advanced Techniques
- When profiling shows virtual dispatch overhead in hot paths (use CRTP)
- When you need to store heterogeneous types without inheritance (use type erasure)
- When writing generic factories or wrappers (use perfect forwarding)
- When computation can be moved to compile time (use constexpr)
- When behavior needs to be selected at compile time (use policy-based design)

### When NOT to Use
- When virtual functions are fast enough (most cases)
- When simple inheritance is clearer than CRTP
- When compile-time complexity hurts readability
- When the team isn't familiar with advanced templates

### Alternatives
| Situation | Advanced Approach | Simple Alternative | Trade-off |
|-----------|-------------------|-------------------|-----------|
| Polymorphism | CRTP | Virtual functions | Zero overhead vs. runtime flexibility |
| Heterogeneous storage | Type erasure | `void*` + type tag | Type safety vs. simplicity |
| Generic forwarding | Perfect forwarding | Overloaded functions | One function vs. many |
| Compile-time computation | constexpr templates | Runtime computation | Zero runtime cost vs. simpler code |

## Production Incidents

### Incident 1: CRTP Causing Debug Difficulty
**Problem**: A template-heavy library using CRTP produced 500-line error messages that took hours to debug.

**Cause**: Deep CRTP inheritance with multiple policies generated nested template errors pointing into CRTP base classes, not the user's code.

**Solution**: Added `static_assert` constraints on CRTP Derived types. Used C++20 concepts to constrain template parameters. Added clear documentation of CRTP requirements.

### Incident 2: Type Erasure Performance Overhead
**Problem**: A type-erased container was 3x slower than expected due to frequent heap allocations.

**Cause**: Each type-erased wrapper allocated its Model on the heap, even for small types. Cache misses from pointer chasing dominated the runtime.

**Solution**: Added small buffer optimization (SBO) to the type erasure wrapper. Types smaller than 64 bytes are stored inline in the buffer. Heap allocation only for larger types.

### Incident 3: Move Semantics Pitfall — Moved-From Object Use
**Problem**: A high-throughput message queue crashed intermittently with use-after-free. Objects were moved from into the queue but then accessed for logging.

**Cause**: After `std::move(msg)` inserted a message into the queue, the original `msg` variable was in a moved-from state. A subsequent `msg.timestamp()` call on the moved-from object accessed invalid memory. The move constructor of `Message` left `data_` as a nullptr, but the destructor still ran, double-freeing.

**Solution**: Audited all move-eligible code paths. Added a `bool valid_` flag to `Message` that is set to `false` in the move constructor. Added `static_assert` checks ensuring move constructors leave objects in a valid-but-unspecified state. Replaced post-move accesses with the queue's returned iterator.

### Incident 4: Perfect Forwarding Failure with braced-init-lists
**Problem**: A generic factory function failed to compile when passed `{1, 2, 3}` as an argument. The error was cryptic template instantiation failure in the forwarding chain.

**Cause**: Perfect forwarding with `std::forward<Args>(args)...` does not work with braced-init-lists because `std::initializer_list` has deduced type `std::initializer_list<E>` — the compiler cannot deduce `T` from `{}`. The forwarded argument lost its type information.

**Solution**: Added explicit `std::initializer_list` overloads to the factory function. Documented that braced-init-lists must be explicitly typed (`std::vector<int>{1,2,3}`) when passed through forwarding references. Added a `static_assert` concept to reject `std::initializer_list` at the call site with a clear error message.

### Incident 5: Lambda Capture Bug — Dangling Reference in Coroutine
**Problem**: A server used lambda callbacks captured by reference in async handlers. Under load, the handler executed after the captured object was destroyed, causing segfaults.

**Cause**: The lambda captured `const auto& request` by reference. When the async operation outlived the request object's scope, the reference dangled. The capture was by reference because the developer wanted to avoid copying large request objects, but did not consider lifetime.

**Solution**: Changed captures to `[request = std::move(request)]` (by move-capture) where ownership was transferred, or `[request = request]` (by copy) where the lambda needed to outlive the scope. Added runtime address sanitizer checks in CI to catch dangling references. Added a lint rule flagging `[&]` captures in async callbacks.

## Production Checklist

- [ ] Use CRTP when virtual dispatch overhead is measured in hot paths
- [ ] Implement small buffer optimization for type erasure wrappers
- [ ] Use `std::forward` in generic functions, never `std::move` on forwarded args
- [ ] Document CRTP requirements with `static_assert` or concepts
- [ ] Use constexpr for compile-time computation when possible
- [ ] Profile before applying advanced techniques — simple code is better
- [ ] Limit template recursion depth to avoid compilation issues
- [ ] Use concepts (C++20) to constrain advanced template parameters

## Maturity Levels

### Beginner (0-6 months)
- Understand what CRTP is and why it exists
- Know when to use virtual functions vs. templates
- Can use `std::function` for type erasure

### Intermediate (6-18 months)
- Implement CRTP for static polymorphism
- Build custom type erasure wrappers
- Use perfect forwarding in generic code
- Write constexpr functions and lookup tables

### Advanced (18+ months)
- Design policy-based architectures
- Implement small buffer optimization for type erasure
- Use CRTP with mixin patterns
- Debug complex template error messages

## Common Myths Debunked

### Myth 1: "CRTP is always faster than virtual functions"
**Reality**: CRTP eliminates vtable overhead but can increase code size (each instantiation). For most applications, the 2-5ns virtual dispatch overhead is negligible. Profile before choosing CRTP.

### Myth 2: "Type erasure has significant overhead"
**Reality**: With small buffer optimization, type erasure can match virtual function performance. The overhead is one indirect call, same as virtual dispatch.

### Myth 3: "Perfect forwarding is only for library writers"
**Reality**: Perfect forwarding is essential for any generic code — factory functions, wrappers, decorators. It's a fundamental C++ technique.

### Myth 4: "constexpr is only for simple computations"
**Reality**: C++17/20 constexpr supports loops, conditionals, and even dynamic allocation. Complex compile-time computation is practical.

## One-Minute Revision

| Technique | What It Is | Why It Matters | Key Rule |
|-----------|-----------|----------------|----------|
| CRTP | Static polymorphism via templates | Zero-overhead polymorphism | Document Derived requirements |
| Type Erasure | Hide type behind uniform interface | Store heterogeneous types safely | Use SBO for small types |
| Perfect Forwarding | Forward arguments with original value category | Generic factories and wrappers | Use `std::forward`, not `std::move` |
| Constexpr | Compile-time computation | Zero runtime cost | Support both compile-time and runtime |
| Policy-Based | Behavior as template parameters | Compile-time strategy selection | Keep policies small and focused |

## Related Topics

- [Templates](../03-templates/) — Foundation for CRTP and policy-based design
- [Modern C++](../08-modern-cpp/) — Concepts, constexpr if, fold expressions
- [Performance](../11-performance/) — When advanced techniques justify their complexity
- [Design Patterns](../09-design-patterns/) — Type erasure enables Strategy pattern without inheritance

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| CRTP producing unreadable 500-line error messages | C++20 concepts + `static_assert` | Add `static_assert(std::is_base_of_v<CRTPBase, Derived>)` in base; constrain with `requires` clauses |
| Type erasure heap allocation overhead | Profiler + small buffer optimization | Profile with `perf`; implement SBO to store small objects inline in a fixed-size buffer |
| Perfect forwarding incorrectly using `std::move` | Compiler warning `-Wpessimizing-move` | Enable warning; ensure `std::forward` is used on forwarding references, never `std::move` |
| Constexpr function failing at runtime with dynamic input | Split constexpr and runtime paths | Use `if constexpr` to branch; constexpr paths must have compile-time-known arguments |
| Policy-based design causing code bloat | `bloaty` binary analysis | Use `bloaty` to identify which policy instantiations consume the most space; factor shared logic |

## Code Review Checklist

- [ ] CRTP Derived requirements documented with `static_assert` or concepts
- [ ] `std::forward` (not `std::move`) used on forwarding reference parameters
- [ ] Type erasure wrappers include small buffer optimization for objects < 64 bytes
- [ ] Constexpr functions support both compile-time and runtime invocation
- [ ] Policy classes are kept small and focused on a single responsibility
- [ ] Template recursion depth limited to avoid compiler resource exhaustion
- [ ] Advanced techniques justified by profiling — not applied speculatively

## Architecture Considerations

Advanced C++ techniques enable zero-cost abstractions that bridge generic programming with runtime flexibility. CRTP eliminates vtable overhead for performance-critical dispatch. Type erasure hides concrete types behind uniform interfaces without inheritance hierarchies. Perfect forwarding enables generic factories that preserve value categories. Policy-based design selects behavior at compile time, creating highly customizable yet efficient components.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| CRTP for mixin functionality | Adding comparison, serialization, or logging to types | Zero overhead vs. reduced debugging clarity and tighter coupling |
| Type erasure (e.g., `std::function`) | Storing heterogeneous callables in a container | Uniform interface vs. heap allocation overhead without SBO |
| Policy-based design | Compile-time strategy selection (storage, logging) | Extreme flexibility vs. combinatorial template explosion |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| CRTP static_cast to wrong derived type | Undefined behavior, memory corruption | Use `static_assert` to validate Derived satisfies CRTP requirements |
| Type erasure dangling reference in small buffer | Use-after-free when SBO object outlives buffer | Ensure SBO buffer is properly aligned and destructor called on replacement |
| Perfect forwarding of sensitive data by value | Unintended copies of credentials/keys | Audit forwarding chains; use `std::string_view` for non-owning sensitive reads |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| C++14 | Generic lambdas for simpler type erasure | Replace `std::function` with generic lambdas where possible |
| C++17 | `if constexpr` for compile-time branching | Replace SFINAE with `if constexpr` in constexpr functions and CRTP helpers |
| C++20 | Concepts for constraining CRTP and policy templates | Replace `static_assert` with `requires` clauses for clearer error messages |

## Version Validation

| Feature | C++ Version | Status |
|---------|------------|--------|
| `std::forward` (perfect forwarding) | C++11 | Widely supported |
| `if constexpr` | C++17 | Widely supported |
| Fold expressions | C++17 | Widely supported |
| Concepts (`requires` clauses) | C++20 | Supported in GCC 10+, Clang 12+, MSVC 19.22+ |

## Interview Questions

1. **Explain CRTP and when to use it**: CRTP (Curiously Recurring Template Pattern) passes the derived class as a template parameter to the base. The base uses `static_cast<Derived*>(this)` to call derived implementations. Use it for zero-overhead static polymorphism when the derived type is known at compile time.
2. **What is type erasure and why is it useful?**: Type erasure hides a concrete type behind a uniform interface — like `std::function` storing any callable. It enables heterogeneous containers without inheritance, combining the flexibility of runtime polymorphism with the efficiency of templates.
3. **How does perfect forwarding work?**: Perfect forwarding uses universal references (`T&&`) and `std::forward<T>()` to pass arguments to another function preserving their value category (lvalue/rvalue) and const-qualification. It's essential for generic factories and wrappers.
4. **When should you prefer `if constexpr` over SFINAE?**: Use `if constexpr` when the branching is based on type traits and both branches are valid code (just different implementations). SFINAE is needed when one branch should not participate in overload resolution at all.
5. **What is policy-based design and what are its trade-offs?**: Policy-based design passes behavior as template parameters (e.g., `Database<StoragePolicy, LoggerPolicy>`). Trade-offs: extreme compile-time flexibility vs. combinatorial template instantiation that can bloat binaries and hurt compile times.
6. **What is the difference between CRTP and virtual functions?**: CRTP resolves calls at compile time (zero overhead, inlined). Virtual functions resolve at runtime (vtable lookup, not inlined). Use CRTP when the derived type is known; use virtual when you need runtime polymorphism.
7. **How does std::function implement type erasure?**: `std::function` uses a small buffer optimization (SBO) for small callables, heap allocation for large ones. It stores a function pointer for the type-erased operation (call, destroy, copy) and invokes through that pointer.
8. **What is the SBO (Small Buffer Optimization)?**: SBO stores small objects inline in the `std::function` object itself, avoiding heap allocation. Typical threshold is 24-32 bytes. Objects larger than this are heap-allocated.
9. **What are universal references and how do they differ from rvalue references?**: Universal references (`T&&` where `T` is a template parameter) can bind to both lvalues and rvalues. Rvalue references (`int&&`) can only bind to rvalues. Universal references are deduced; rvalue references are not.
10. **What is the problem with forwarding references and overloading?**: Forwarding references participate in overload resolution for all value categories, which can cause unexpected matches. Disambiguate with `std::enable_if` or C++20 concepts.
11. **What is constexpr evaluation and what are its limits?**: `constexpr` functions can be evaluated at compile time when all inputs are constexpr. Limits include: no dynamic allocation (until C++20), no undefined behavior, recursion depth limits, no I/O.
12. **How does compile-time polymorphism differ from runtime polymorphism?**: Compile-time (CRTP, templates) resolves at compile time — zero overhead, but binary bloat. Runtime (virtual functions) resolves at runtime — slight overhead, but smaller binaries and runtime flexibility.
13. **What is the Curiously Recurring Template Pattern (CRTP) anti-pattern?**: CRTP anti-patterns include: deep inheritance chains, mixing CRTP with virtual functions, and forgetting to use `static_cast`. These lead to brittle code that's hard to debug.
14. **What is the difference between std::any and std::variant?**: `std::any` can hold any copyable type (type-erased, heap-allocated). `std::variant` holds one of a fixed set of types (stack-allocated, type-safe visitation). Use `std::variant` when types are known; `std::any` when they're not.
15. **How do you test type-erased code?**: Test with different concrete types stored in the same interface. Verify: correct behavior, exception safety, copy/move semantics, and SBO vs heap paths. Use sanitizer tools to catch memory issues in custom type erasure implementations.

## Performance Considerations

| Technique | Overhead | Notes |
|-----------|----------|-------|
| CRTP dispatch | Zero | Compiled to direct function call, same as non-virtual |
| Virtual dispatch | ~2-5 ns | One pointer dereference + indirect call |
| std::function call | ~5-10 ns | Type-erased indirect call, possible heap access |
| Perfect forwarding | Zero | No copy/move, compile-time only |
| constexpr computation | Zero (at runtime) | Computed at compile time, stored as constant |
| Type erasure (SBO) | ~1-2 ns | Inline storage, no heap allocation |

## Best Practices

- Use CRTP when derived type is known at compile time and zero overhead is required
- Use type erasure when you need runtime polymorphism without inheritance hierarchies
- Use perfect forwarding in generic factories and wrappers to avoid unnecessary copies
- Prefer `if constexpr` over SFINAE for readability when both branches are valid
- Use C++20 concepts to constrain templates and improve error messages
- Test type-erased code with multiple concrete types
- Document template requirements in comments or concepts

## Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| CRTP `static_cast` to wrong type | Undefined behavior | Use `static_assert` or concepts to validate |
| Forgetting `std::forward` in forwarding reference | Unnecessary copies | Always forward: `func(std::forward<T>(arg))` |
| Using `std::function` in hot path | Heap allocation overhead | Use templates or CRTP for hot paths |
| Overusing type erasure | Runtime overhead, debugging difficulty | Prefer compile-time solutions when possible |
| Mixing CRTP with virtual functions | Confusing dispatch, potential bugs | Use one or the other, not both |

## Cross-References

- **Previous Module:** [02 - Object-Oriented Programming](../02-oop/)
- **Next Module:** [03 - Templates](../03-templates/)
- **Related:** [04 - STL](../04-stl/) — uses type erasure extensively
- **Related:** [06 - Smart Pointers](../06-smart-pointers/) — type erasure in practice
- **Related:** [08 - Modern C++](../08-modern-cpp/) — constexpr and concepts
- **External:** [C++ Core Guidelines — Templates](https://isocpp.github.io/CppCoreGuidelines/CppCoreGuidelines#S-templates)
- **External:** [CppReference — std::function](https://en.cppreference.com/w/cpp/utility/functional/function)

## References

- [Modern C++ Design — Andrei Alexandrescu (Policy-Based Design)](https://www.amazon.com/Modern-Design-Generative-Programming-Patterns/dp/0201704315)
- [CppReference — Type Erasure Patterns](https://en.cppreference.com/w/cpp)
- [ARTSI — Advanced C++ Techniques](https://isocpp.org/wiki/faq)
- [CppCon Talks on CRTP and Type Erasure](https://youtube.com/cppcon)
