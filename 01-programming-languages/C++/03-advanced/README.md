# Advanced C++ — C++

## Why It Matters

When you've mastered the fundamentals and OOP, you hit a ceiling: code that works but isn't efficient, flexible, or maintainable enough for production systems. Advanced C++ techniques like CRTP, type erasure, and perfect forwarding let you write zero-overhead abstractions that are both generic and fast. Without these skills, you're stuck choosing between template complexity and runtime overhead.

## What It Is

Advanced C++ covers techniques that go beyond basic language features: CRTP for static polymorphism, type erasure for runtime polymorphism without inheritance, perfect forwarding for generic factories, and advanced constexpr programming for compile-time computation.

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

## References

- [Modern C++ Design — Andrei Alexandrescu (Policy-Based Design)](https://www.amazon.com/Modern-Design-Generative-Programming-Patterns/dp/0201704315)
- [CppReference — Type Erasure Patterns](https://en.cppreference.com/w/cpp)
- [ARTSI — Advanced C++ Techniques](https://isocpp.org/wiki/faq)
- [CppCon Talks on CRTP and Type Erasure](https://youtube.com/cppcon)
