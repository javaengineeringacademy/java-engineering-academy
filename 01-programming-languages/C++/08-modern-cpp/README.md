# Modern C++ — C++

## Overview

Modern C++ refers to the collection of features introduced in C++11 and subsequent standards (C++14, C++17, C++20, C++23). These features fundamentally change how you write C++ code — enabling safer resource management, cleaner syntax, better performance, and more expressive type systems. Modern C++ is not a separate language; it is an evolution of C++ that builds on decades of backward compatibility while addressing long-standing pain points in the language.

## Learning Objectives

After completing this module, you will be able to:

- Use `auto` type inference, range-based for loops, and `nullptr` to write cleaner code
- Apply move semantics and perfect forwarding to eliminate unnecessary copies
- Write lambda expressions and use them with STL algorithms
- Use `std::optional`, `std::variant`, and `std::string_view` for safer, more expressive designs
- Apply `constexpr` and `if constexpr` for compile-time programming
- Constrain templates with C++20 concepts
- Recognize and avoid common pitfalls with modern C++ features
- Understand when to use modern versus legacy approaches

## Prerequisites

- Solid understanding of C++ basics: variables, functions, classes, pointers, references
- Familiarity with the STL: `std::vector`, `std::string`, iterators
- Basic knowledge of templates (template syntax, function templates)
- Understanding of heap vs. stack memory and manual resource management (`new`/`delete`)
- A C++17-capable compiler (GCC 7+, Clang 5+, MSVC 19.14+)

## Why It Matters

Modern C++ (C++11/14/17/20) is not just syntax sugar — it fundamentally changes how you express intent, manage resources, and write safe, performant code. When you master these features, you get fewer bugs, clearer code, and the ability to use the full power of the language instead of writing "C++03-style" code with the parking brake on.

## What It Is

Modern C++ encompasses features from C++11 onwards, including auto type inference, lambdas, move semantics, smart pointers, constexpr, optional, variant, string_view, and concepts that transform how you write C++ code.

## History

Modern C++ began with the C++11 standard, which was a major overhaul of the language after over a decade of stability (C++03).

| Standard | Year | Key Features |
|----------|------|--------------|
| **C++03** | 2003 | Last "legacy" standard; `auto` as storage class specifier; no lambdas |
| **C++11** | 2011 | `auto` type inference, lambdas, `std::move`, `constexpr`, `nullptr`, range-based for, smart pointers, `std::thread`, uniform initialization |
| **C++14** | 2014 | Generic lambdas, `std::make_unique`, `std::index_sequence`, relaxed `constexpr` restrictions, variable templates |
| **C++17** | 2017 | `std::optional`, `std::variant`, `std::string_view`, structured bindings, `if constexpr`, `std::filesystem`, class template argument deduction (CTAD), parallel algorithms |
| **C++20** | 2020 | Concepts, ranges, coroutines, `std::format`, modules, `std::span`, three-way comparison (`<=>`), `consteval`, `constinit` |
| **C++23** | 2023 | `std::expected`, `std::print`, `std::mdspan`, `std::generator`, `std::flat_map`, deducing `this`, `std::stacktrace` |

The term "Modern C++" typically refers to C++11 and later. Many features were backported from Boost and other libraries after years of real-world use. The committee's philosophy: if a pattern is widely used and could benefit from language-level support, standardize it.

## Engineering Decision Framework

| Decision | Modern Approach | Legacy Approach | When to Use Which |
|----------|----------------|-----------------|-------------------|
| Type declaration | `auto x = expr;` | `int x = expr;` | Use `auto` when type is obvious from context |
| Loop over container | `for (const auto& x : v)` | `for (int i = 0; i < v.size(); i++)` | Always prefer range-based when iterating all elements |
| Anonymous function | Lambda | Functor class | Lambdas for short callbacks; functors for stateful reusable policies |
| Ownership transfer | `std::move` | Raw pointer + manual delete | Always use move semantics for non-copyable types |
| Null pointer | `nullptr` | `NULL` / `0` | Always use `nullptr` — it's type-safe |
| Compile-time constant | `constexpr` | `const` + manual inlining | Use `constexpr` for values computed at compile time |
| Optional value | `std::optional` | Sentinel values / `bool + T` | Use `std::optional` when absence is a valid state |
| Type-safe union | `std::variant` | `union` + type tag | Use `std::variant` for discriminated unions |
| Non-owning string ref | `std::string_view` | `const std::string&` | Use `string_view` for read-only string parameters |

## Production Notes

- **Compiler support varies**: C++17 is widely supported (GCC 7+, Clang 5+, MSVC 19.14+). C++20 requires GCC 10+, Clang 12+, MSVC 19.22+. Check your target compilers before adopting features.
- **Compile times increase**: Modules (C++20) reduce compile times but are not yet universally supported. Heavy template/metaprogramming with concepts can slow builds.
- **ABI compatibility**: `std::string`, `std::vector`, and other types have stable ABI across most compilers. However, adding new members to `std::optional` or `std::variant` in future standards may break ABI — use `-fno-exceptions` or custom types for ABI-sensitive code.
- **Sanitizer compatibility**: Enable AddressSanitizer, UndefinedBehaviorSanitizer, and ThreadSanitizer in CI. Modern C++ features like move semantics and lambdas interact with sanitizers correctly.
- **Deployment considerations**: When deploying across heterogeneous environments (different Linux distros, compiler versions), pin your compiler version and test with the oldest supported toolchain.

## Syntax

### Type Inference

```cpp
auto x = 42;              // int
auto y = 3.14;            // double
auto z = std::string("hi"); // std::string
decltype(x) w = 10;       // same type as x (int)
```

### Lambda Expressions

```cpp
// Basic lambda
auto f = [](int x) { return x * 2; };

// Capture by value
auto g = [factor](int x) { return x * factor; };

// Capture by reference
auto h = [&count]() { return ++count; };

// Generic lambda (C++14)
auto add = [](auto a, auto b) { return a + b; };

// Mutable lambda
auto counter = [n = 0]() mutable { return ++n; };
```

### Move Semantics

```cpp
std::vector<int> a = {1, 2, 3};
std::vector<int> b = std::move(a);  // a is now empty, b has {1, 2, 3}

std::string s = "hello";
std::string t = std::move(s);       // s is moved-from (valid but unspecified)
```

### Smart Pointers

```cpp
auto p1 = std::make_unique<int>(42);         // unique ownership
auto p2 = std::make_shared<int>(42);         // shared ownership
std::weak_ptr<int> wp = p2;                  // non-owning observer
```

### std::optional / std::variant / std::string_view

```cpp
std::optional<int> opt = 42;          // has value
std::optional<int> empty;             // no value (std::nullopt)

std::variant<int, std::string> v = "hello";  // holds string
std::get<std::string>(v);                     // access as string

std::string_view sv = "hello world";  // non-owning view
sv.substr(0, 5);                      // "hello" — no allocation
```

### Structured Bindings

```cpp
auto [x, y] = std::pair(1, 2.0);
auto [key, value] = *map.begin();
auto [a, b, c] = std::tuple(1, 2.0, "three");
```

### Concepts (C++20)

```cpp
template <typename T>
concept Addable = requires(T a, T b) {
    { a + b } -> std::convertible_to<T>;
};

template <Addable T>
T add(T a, T b) { return a + b; }

// Abbreviated form
auto multiply(Addable auto a, Addable auto b) { return a * b; }
```

### constexpr and if constexpr

```cpp
constexpr int factorial(int n) {
    return (n <= 1) ? 1 : n * factorial(n - 1);
}

template <typename T>
auto process(T value) {
    if constexpr (std::is_integral_v<T>) {
        return value * 2;
    } else {
        return value + 0.5;
    }
}
```

## Expanded Code Examples

### Auto and Type Inference

```cpp
#include <iostream>
#include <vector>
#include <memory>

int main() {
    // auto deduces the type from the initializer
    auto x = 42;           // int
    auto pi = 3.14159;     // double
    auto name = std::string("Alice");  // std::string

    // Use auto with iterators — avoids verbose iterator types
    std::vector<int> vec = {10, 20, 30};
    for (auto it = vec.begin(); it != vec.end(); ++it) {
        std::cout << *it << " ";
    }

    // Use auto with structured bindings (C++17)
    auto [key, value] = std::make_pair(std::string("answer"), 42);
    std::cout << key << " = " << value << "\n";

    //decltype — get the type of an expression without evaluating it
    int i = 0;
    decltype(i) j = 5;  // j is int
}
```

**When NOT to use auto**: When the type is not obvious, or when you need a specific type for API contracts.

```cpp
// Bad — what type is result?
auto result = compute_value();  // Is it int? double? string?

// Good — explicit for public APIs
double compute_value();
```

### Lambda Expressions — Deep Dive

```cpp
#include <iostream>
#include <vector>
#include <algorithm>
#include <functional>

int main() {
    int factor = 3;

    // Capture by value — factor is copied
    auto multiply_by_value = [factor](int x) { return x * factor; };

    // Capture by reference — factor is referenced (dangerous if factor goes out of scope)
    auto multiply_by_ref = [&factor](int x) { return x * factor; };

    // Capture all by value
    auto all_by_value = [=](int x) { return x * factor; };

    // Capture all by reference
    auto all_by_ref = [&](int x) { return x * factor; };

    // Mutable lambda — can modify captured values
    int count = 0;
    auto counter = [count]() mutable { return ++count; };
    std::cout << counter() << "\n";  // 1
    std::cout << counter() << "\n";  // 2

    // Generic lambda (C++14)
    auto add = [](auto a, auto b) { return a + b; };
    std::cout << add(1, 2) << "\n";       // 3
    std::cout << add(1.5, 2.5) << "\n";  // 4.0

    // Using lambdas with STL algorithms
    std::vector<int> nums = {5, 3, 1, 4, 2};
    std::sort(nums.begin(), nums.end(), [](int a, int b) {
        return a < b;
    });

    // Lambda with std::function — type-erased callable
    std::function<int(int, int)> op = [](int a, int b) { return a + b; };
    std::cout << op(10, 20) << "\n";
}
```

### Move Semantics — The Complete Picture

```cpp
#include <iostream>
#include <vector>
#include <string>

class Buffer {
    size_t size_;
    int* data_;
public:
    // Constructor
    explicit Buffer(size_t size) : size_(size), data_(new int[size]()) {
        std::cout << "  Constructed (" << size_ << " elements)\n";
    }

    // Copy constructor — expensive!
    Buffer(const Buffer& other) : size_(other.size_), data_(new int[other.size_]) {
        std::copy(other.data_, other.data_ + other.size_, data_);
        std::cout << "  Copied (" << size_ << " elements)\n";
    }

    // Move constructor — cheap!
    Buffer(Buffer&& other) noexcept : size_(other.size_), data_(other.data_) {
        other.size_ = 0;
        other.data_ = nullptr;
        std::cout << "  Moved (" << size_ << " elements)\n";
    }

    // Copy assignment
    Buffer& operator=(const Buffer& other) {
        if (this != &other) {
            delete[] data_;
            size_ = other.size_;
            data_ = new int[other.size_];
            std::copy(other.data_, other.data_ + other.size_, data_);
        }
        return *this;
    }

    // Move assignment
    Buffer& operator=(Buffer&& other) noexcept {
        if (this != &other) {
            delete[] data_;
            size_ = other.size_;
            data_ = other.data_;
            other.size_ = 0;
            other.data_ = nullptr;
        }
        return *this;
    }

    ~Buffer() { delete[] data_; }

    size_t size() const { return size_; }
};

int main() {
    Buffer a(1000);

    // Without std::make_move_iterator or std::move — copies!
    Buffer b = a;  // Copy constructor called

    // With std::move — moves!
    Buffer c = std::move(a);  // Move constructor called, a is now empty

    // std::move is a cast — it doesn't move anything by itself
    // It just enables move overloads
    std::cout << "a size after move: " << a.size() << "\n";  // 0
    std::cout << "c size after move: " << c.size() << "\n";  // 1000
}
```

### Structured Bindings and std::optional (C++17)

```cpp
#include <iostream>
#include <map>
#include <optional>
#include <string>
#include <tuple>

// Return multiple values cleanly
std::tuple<std::string, int, double> get_user_info() {
    return {"Alice", 30, 95.5};
}

// Use std::optional for values that might not exist
std::optional<std::string> find_user(int id) {
    if (id == 1) return "Alice";
    return std::nullopt;  // User not found
}

int main() {
    // Structured bindings
    auto [name, age, score] = get_user_info();
    std::cout << name << ", " << age << ", " << score << "\n";

    // With maps
    std::map<std::string, int> ages = {{"Alice", 30}, {"Bob", 25}};
    for (const auto& [person, age] : ages) {
        std::cout << person << " is " << age << " years old\n";
    }

    // std::optional usage
    auto user = find_user(1);
    if (user.has_value()) {
        std::cout << "Found: " << user.value() << "\n";
    }

    // Or use the shorter syntax
    if (auto user2 = find_user(2); user2) {
        std::cout << "Found: " << *user2 << "\n";
    } else {
        std::cout << "User not found\n";
    }

    // value_or for defaults
    std::string name2 = find_user(99).value_or("Unknown");
    std::cout << "User: " << name2 << "\n";
}
```

### Concepts (C++20)

```cpp
#include <iostream>
#include <concepts>
#include <vector>
#include <string>

// Define a concept
template <typename T>
concept Numeric = std::is_arithmetic_v<T>;

template <typename T>
concept Printable = requires(std::ostream& os, const T& val) {
    { os << val } -> std::convertible_to<std::ostream&>;
};

// Constrained template — only compiles for numeric types
template <Numeric T>
T square(T value) {
    return value * value;
}

// Using requires clause
template <typename T>
    requires Printable<T>
void print(const T& val) {
    std::cout << val << "\n";
}

// Abbreviated function templates
auto add(Numeric auto a, Numeric auto b) {
    return a + b;
}

int main() {
    std::cout << square(5) << "\n";      // 25
    std::cout << square(3.14) << "\n";   // 9.8596

    // This would cause a compile error:
    // square("hello");  // Error: const char* is not Numeric

    print(42);        // OK
    print("hello");   // OK
    // print(std::vector<int>{});  // Error: vector doesn't have operator<<
}
```

### constexpr and Compile-Time Programming

```cpp
#include <iostream>
#include <array>

// Compile-time factorial
constexpr unsigned long long factorial(int n) {
    return (n <= 1) ? 1 : n * factorial(n - 1);
}

// Compile-time array generation
constexpr auto make_table() {
    std::array<int, 10> table{};
    for (int i = 0; i < 10; ++i) {
        table[i] = i * i;
    }
    return table;
}

// constexpr if (C++17) — compile-time branching
template <typename T>
auto clamp_value(T value, T low, T high) {
    if constexpr (std::is_integral_v<T>) {
        // Integer-specific optimization
        return std::max(low, std::min(high, value));
    } else {
        // Floating-point path
        return std::clamp(value, low, high);
    }
}

int main() {
    // These are computed at compile time — zero runtime cost
    constexpr auto fact10 = factorial(10);
    std::cout << "10! = " << fact10 << "\n";  // 3628800

    constexpr auto squares = make_table();
    for (int i = 0; i < 10; ++i) {
        std::cout << i << "^2 = " << squares[i] << "\n";
    }

    // constexpr if
    int x = clamp_value(15, 0, 10);
    std::cout << "Clamped: " << x << "\n";  // 10
}
```

### std::string_view (C++17)

```cpp
#include <iostream>
#include <string>
#include <string_view>
#include <cstring>

// string_view is a non-owning reference to a contiguous sequence of characters
// It avoids copies when you only need to read a string

// Bad: forces callers to create a std::string even for string literals
void process_bad(const std::string& s) {
    std::cout << s << "\n";
}

// Good: accepts strings, string literals, substrings without allocation
void process(std::string_view sv) {
    std::cout << sv << "\n";
    // sv is valid as long as the underlying string exists
}

// Example: efficient substring without allocation
std::string_view get_extension(std::string_view filename) {
    auto dot = filename.rfind('.');
    return (dot != std::string_view::npos) ? filename.substr(dot) : "";
}

int main() {
    process("hello");          // No temporary std::string created
    std::string s = "world";
    process(s);                // Works with std::string too
    process(s.substr(0, 3));   // No allocation for substring

    auto ext = get_extension("document.txt");
    std::cout << "Extension: " << ext << "\n";  // .txt

    // Warning: don't use string_view after the source string is destroyed!
    // std::string_view dangling = get_extension(std::string("temp.txt"));  // Dangling!
}
```

### std::variant and std::any (C++17)

```cpp
#include <iostream>
#include <variant>
#include <string>
#include <vector>

// variant — type-safe union
using Value = std::variant<int, double, std::string>;

void print_value(const Value& v) {
    // std::visit with overloaded lambda (C++17 pattern)
    std::visit([](const auto& val) {
        std::cout << val << "\n";
    }, v);
}

// Using std::get
void process_value(const Value& v) {
    if (std::holds_alternative<int>(v)) {
        int i = std::get<int>(v);
        std::cout << "Int: " << i << "\n";
    } else if (std::holds_alternative<double>(v)) {
        double d = std::get<double>(v);
        std::cout << "Double: " << d << "\n";
    } else if (std::holds_alternative<std::string>(v)) {
        const auto& s = std::get<std::string>(v);
        std::cout << "String: " << s << "\n";
    }
}

int main() {
    Value v1 = 42;
    Value v2 = 3.14;
    Value v3 = std::string("hello");

    print_value(v1);  // 42
    print_value(v2);  // 3.14
    print_value(v3);  // hello

    process_value(v1);  // Int: 42
}
```

## Core Concepts

| Concept | Description | Standard |
|---------|-------------|----------|
| **Type inference** (`auto`, `decltype`) | Let the compiler deduce types from expressions, reducing verbosity | C++11 |
| **Move semantics** | Transfer resource ownership instead of copying, using rvalue references (`T&&`) | C++11 |
| **Lambda expressions** | Anonymous function objects with capture lists, enabling inline callbacks | C++11 |
| **Perfect forwarding** | `std::forward` preserves value category when passing arguments through templates | C++11 |
| **RAII + smart pointers** | Automatic resource management via `std::unique_ptr`, `std::shared_ptr`, `std::weak_ptr` | C++11 |
| **constexpr** | Functions and variables evaluated at compile time, zero runtime cost | C++11/14/17 |
| **Structured bindings** | Decompose tuples, pairs, structs into named variables | C++17 |
| **`std::optional`** | Explicit nullable value type — absence is a valid, type-safe state | C++17 |
| **`std::variant`** | Type-safe discriminated union — replaces raw `union` + type tag | C++17 |
| **`std::string_view`** | Non-owning, read-only reference to a contiguous character sequence | C++17 |
| **`if constexpr`** | Compile-time branching — only instantiates the taken branch | C++17 |
| **Concepts** | Named constraints on template parameters — readable error messages | C++20 |
| **Ranges** | Lazy, composable algorithm pipelines with views | C++20 |
| **Coroutines** | Stackless suspend/resume for generators, async I/O, state machines | C++20 |
| **Modules** | Replacement for `#include` — faster builds, no macro leakage | C++20 |

## Performance Considerations

| Feature | Overhead | Notes |
|---------|----------|-------|
| `auto` | Zero | Purely compile-time type deduction |
| Range-based for | Zero | Equivalent to iterator-based loop |
| Lambdas | Zero | Inlined by the compiler like regular functions |
| `std::move` | Zero | Compile-time cast only; actual move cost depends on type |
| `std::forward` | Zero | Perfect forwarding preserves value category at zero cost |
| `std::optional` | Minimal | Size of `T` + 1 byte (bool); may add padding |
| `std::variant` | Size of largest alternative + 1 byte | `std::visit` is optimized via jump tables |
| `std::string_view` | 16 bytes (pointer + size) | No heap allocation; avoids copies |
| `constexpr` | Zero (when computed at compile time) | Falls back to runtime if arguments are not constexpr |
| `std::shared_ptr` | Atomic reference count + heap control block | Prefer `std::unique_ptr` when shared ownership is not needed |
| `std::function` | Heap allocation for non-trivial callables | Use direct lambda type or `std::move_only_function` (C++23) when possible |
| Concepts | Zero | Compile-time constraint; no runtime overhead |

**Key principle**: Modern C++ features follow the zero-overhead abstraction principle. You should not pay for what you do not use, and what you do use should be as efficient as hand-written low-level code.

## Production Incidents

### Incident 1: Lambda Capture Dangling Reference
**Problem**: An async task scheduler crashed sporadically in production with segfaults in lambda callbacks.

**Cause**: A lambda captured a local `std::string` by reference (`[&name]`) and was dispatched to a thread pool. The originating function returned before the lambda executed, destroying `name` on the stack. The lambda accessed a dangling reference.

**Impact**: ~5 crashes/day, 0.3% of user jobs failed silently. 12 customer escalation tickets in one week.

**Detection**: AddressSanitizer caught the use-after-free in a nightly stress test. ASan output showed "stack-use-after-scope" with the lambda's capture list.

**Solution**: Changed capture from `[&name]` to `[name]` (capture by value) for all variables outliving the originating scope. For large objects, used `std::shared_ptr` to share ownership with the lambda.

**Prevention**: Enable ASan in CI. Clang-tidy rule: `-Wdangling-captured-reference` for lambdas. Code review checklist must verify capture mode vs. variable lifetime for every lambda dispatched to another thread.

### Incident 2: std::optional Misuse Causing Crash
**Problem**: A configuration service crashed on startup with `std::bad_optional_access` in production on Kubernetes.

**Cause**: `std::optional<Config>` was used to lazily initialize a global config. The code accessed `.value()` before checking `.has_value()`. In production, the config file loaded slower than expected due to NFS latency, so the optional was still empty.

**Impact**: Service failed to start in 30% of pods. Kubernetes restart loops burned cluster resources. Deployment rollback took 20 minutes — partial outage.

**Detection**: Core dump analysis showed `std::bad_optional_access` at `.value()`. `strace` on slow-starting pods confirmed NFS mount delays exceeding the initialization timeout.

**Solution**: Replaced `.value()` with `.value_or(default)` for non-critical config. Added explicit `.has_value()` checks with logging for critical config. Added a startup readiness probe that blocks traffic until config is loaded.

**Prevention**: Lint rule — ban `.value()` calls; use `*opt` (unchecked, fast) or `.value_or()` (safe) instead. Add startup health checks to Kubernetes manifests. Use `std::optional` only when absence is a valid runtime state, not for deferred initialization.

### Incident 3: Move Semantics Misuse Creating Use-After-Move
**Problem**: A network packet processing pipeline silently dropped 5% of packets in production.

**Cause**: A developer used `std::move(packet)` to pass a packet to a processing function, then logged the packet size afterward. The move left the packet in a valid-but-unspecified state — the size was 0.

**Impact**: 5% packet loss in production. Customers reported intermittent API failures. Network monitoring showed packets being "processed" but with zero payload.

**Detection**: Added logging of packet data before and after processing. Discovered post-move accesses were reading garbage/empty state.

**Solution**: Removed the `std::move()` — the processing function was actually copying, not moving. The original code intended to move but the function took `const Packet&`. The `std::move` was converting an lvalue to an rvalue reference, which then bound to `const Packet&` — effectively a no-op for the move, but it prevented other optimizations.

**Prevention**: Never use `std::move` on a `const` object (it won't move). After a move, the source object is in a valid-but-unspecified state — document this. Use compiler warnings `-Wpessimizing-move` to catch unnecessary moves.

### Incident 4: Structured Bindings Dangling Reference in Lambda
**Problem**: A real-time data processing service segfaulted intermittently in production, crashing worker threads.

**Cause**: A developer used structured bindings to decompose a `std::map` entry, then captured the binding by reference in a lambda dispatched to an async task. The structured binding was a reference to the map element, and the map was modified (elements erased) before the lambda executed.

```cpp
// Bug: structured binding captures reference to map element
for (const auto& [key, value] : data_map) {
    thread_pool.submit([&key, &value]() {  // dangling if map is modified
        process(key, value);
    });
}
```

**Impact**: ~2 crashes/day under high load. 0.1% of data processing jobs failed. Customer data pipeline showed gaps.

**Detection**: AddressSanitizer in staging caught `heap-use-after-free` pointing to the lambda's captured references. GDB backtrace showed the crash inside the lambda body.

**Solution**: Changed capture to by-value: `[key, value]`. For large objects, used `std::shared_ptr` to the map entry. Added a rule: when dispatching lambdas to other threads, always capture by value or via `std::shared_ptr`.

**Prevention**: Enable `-Wdangling-captured-reference` in CI. Code review must verify that structured binding captures outlive the source container. Prefer capturing the container element by shared ownership for async tasks.

### Incident 5: Structured Bindings Causing Unintended Copies
**Problem**: A high-throughput trading system showed unexpected latency spikes under load.

**Cause**: A developer used structured bindings in a hot loop to decompose a `std::pair<int, double>` returned by a function. The binding created copies of both elements on every iteration, instead of references. The compiler could not optimize away the copies because the pair was returned by value.

```cpp
// Bug: structured binding creates copies, not references
for (const auto& [id, price] : get_prices()) {  // copies pair elements
    aggregate(id, price);  // price is a copy, not a reference
}
```

**Impact**: 3x latency increase in the hot path. Trading system missed SLA targets during peak hours.

**Detection**: Profiling with `perf` showed high CPU cache misses and excessive memory allocation in the loop. Disassembling the function revealed copy constructors being called.

**Solution**: Stored the returned pair in a local variable and used `const auto&` on the structured binding to bind by reference. For `std::tuple`, used `std::tie` or explicitly accessed `.first`/`.second`.

**Prevention**: Use `const auto&` with structured bindings when you don't need ownership. Add a lint rule to flag structured bindings from temporary returns. Profile hot loops regularly with `perf` or `VTune`.

## Production Checklist

- [ ] Use `auto` when type is obvious from context
- [ ] Use range-based for loops for iteration
- [ ] Use lambdas for short callbacks and STL algorithm predicates
- [ ] Use move semantics for non-copyable or large objects
- [ ] Use `nullptr` instead of `NULL` or `0`
- [ ] Use `constexpr` for compile-time computable values
- [ ] Use `std::optional` when absence is a valid state
- [ ] Use `std::variant` instead of raw `union`
- [ ] Use `std::string_view` for read-only string parameters
- [ ] Use concepts (C++20) to constrain templates
- [ ] Enable compiler warnings: `-Wall -Wextra -Wpedantic`
- [ ] Test with multiple compiler versions (GCC, Clang, MSVC)

## Internal Working

### How Modern C++ Features Are Implemented at Compile Time

Modern C++ features are not runtime constructs — they are resolved entirely by the compiler. Understanding this helps you reason about performance and behavior.

### The `__cplusplus` Macro

The `__cplusplus` macro is defined by all C++ compilers and indicates the supported standard version. Use it for feature detection:

```cpp
#if __cplusplus >= 202002L
    // C++20 features available
    #include <concepts>
#elif __cplusplus >= 201703L
    // C++17 features available
    #include <optional>
#elif __cplusplus >= 201402L
    // C++14 features available
#else
    // C++11 or earlier
#endif
```

### Compiler Feature Detection

| Macro | Value | Indicates |
|-------|-------|-----------|
| `__cplusplus` | `201103L` | C++11 |
| `__cplusplus` | `201402L` | C++14 |
| `__cplusplus` | `201703L` | C++17 |
| `__cplusplus` | `202002L` | C++20 |
| `__cplusplus` | `202302L` | C++23 |
| `__cpp_constexpr` | `201907L` | Extended `constexpr` (non-literal types in C++23) |
| `__cpp_concepts` | `201907L` | Concepts support |
| `__cpp_structured_bindings` | `201606L` | Structured bindings |
| `__cpp_lib_optional` | `201606L` | `std::optional` in library |
| `__cpp_lib_variant` | `201606L` | `std::variant` in library |
| `_MSVC_LANG` | Similar values | MSVC-specific (does not update `__cplusplus` by default) |

### How `auto` Works

`auto` is pure compile-time type deduction. The compiler examines the initializer's type and substitutes `auto` with the deduced type. There is zero runtime cost — the generated code is identical to writing the type explicitly.

### How Move Semantics Work

`std::move` does not move anything. It is a `static_cast` to an rvalue reference (`T&&`). The actual move happens when a move constructor or move assignment operator is called. The compiler selects the move overload when the argument is an rvalue (or cast to one via `std::move`).

### How `constexpr` Works

`constexpr` functions are evaluated at compile time when their arguments are compile-time constants. When called with runtime values, they fall back to runtime evaluation. The compiler maintains two code paths — one for compile-time, one for runtime.

### How Concepts Work

Concepts are evaluated during template instantiation. The compiler checks if the template arguments satisfy the concept's requirements (expressions, type traits, nested requirements). If not, the template is removed from overload resolution via SFINAE, producing a clear error message instead of a deep template instantiation error.

## Maturity Levels

| Level | Features |
|-------|----------|
| **Beginner** | `auto`, range-based for, `nullptr`, `constexpr` |
| **Intermediate** | Lambdas, move semantics, `std::optional`, `std::string_view` |
| **Advanced** | Concepts, coroutines, modules, `std::format`, `std::ranges` |

## Common Myths — Debunked

| Myth | Reality |
|------|---------|
| "Modern C++ is incompatible with old code" | C++ is backward-compatible. Modern features are additive, not replacing. |
| "You should always use the latest standard" | Use the standard your compiler and team support. Don't chase features you can't test. |
| "Lambdas are always slower than functions" | Lambdas are zero-overhead. The compiler inlines them just like regular functions. |
| "`auto` makes code less readable" | `auto` makes code *more* readable when the type is obvious. `auto it = vec.begin()` is clearer than `std::vector<int>::iterator it`. |
| "Move semantics means no copies" | Move semantics reduces unnecessary copies. Copy elision (NRVO) already eliminated many. |
| "Smart pointers are always better than raw pointers" | Raw pointers are fine for non-owning references. Use smart pointers for ownership. |

## Best Practices

1. **Use `auto` when type is obvious**: `auto it = vec.begin()`, `auto [key, value] = *map.begin()`. Avoid `auto` for return types of unclear functions.
2. **Prefer `std::unique_ptr` over `std::shared_ptr`**: Shared ownership has atomic reference-counting overhead. Only use `std::shared_ptr` when ownership is genuinely shared.
3. **Capture by value in thread-dispatched lambdas**: `[name]` not `[&name]`. The originating scope may be destroyed before the lambda runs.
4. **Use `std::string_view` for read-only string parameters**: Avoids unnecessary `std::string` construction from literals and substrings.
5. **Use `constexpr` for compile-time computable values**: Enables zero-cost abstractions and compile-time validation.
6. **Use `std::optional` instead of sentinel values**: `std::nullopt` is explicit; `-1` or `""` are ambiguous.
7. **Use `std::variant` instead of raw `union`**: Type-safe, no manual tag management, no undefined behavior from wrong access.
8. **Use structured bindings for unpacking**: `auto [name, age] = get_user()` is clearer than `.first`/`.second`.
9. **Mark move constructors and move assignment as `noexcept`**: The STL containers only use move operations if they are `noexcept`.
10. **Use `nullptr` instead of `NULL` or `0`**: `nullptr` is type-safe and works with all pointer types.
11. **Enable compiler warnings**: `-Wall -Wextra -Wpedantic -Werror` catches many modern C++ issues.
12. **Test with multiple compilers**: GCC, Clang, and MSVC may differ in template instantiation behavior and conformance.
13. **Use C++20 concepts to replace SFINAE**: Clear error messages, self-documenting code, easier maintenance.

## Common Mistakes

| Mistake | Why It's Wrong | Correct Approach |
|---------|---------------|------------------|
| Using `auto` for unclear return types | Code becomes unreadable; refactoring breaks silently | Use explicit types for function returns and complex expressions |
| `std::move` on `const` objects | Casts to `const T&&`, which binds to `const T&` — no move happens | Remove `std::move` or remove `const` |
| Using `.value()` on `std::optional` without checking | Throws `std::bad_optional_access` if empty | Use `.value_or()` or check `.has_value()` first |
| Capturing by reference in async lambdas | Dangling reference when originating scope exits | Capture by value or use `std::shared_ptr` |
| Using `std::string_view` past source lifetime | Dangling view — undefined behavior | Ensure the underlying string outlives the view |
| `std::get` on `std::variant` without checking type | Throws `std::bad_variant_access` | Use `std::holds_alternative` or `std::visit` |
| Making move operations throw exceptions | STL containers won't use non-`noexcept` move operations | Mark move operations `noexcept` |
| Using `std::function` for hot-path callbacks | Heap allocation + type erasure overhead | Use template parameters or direct lambda types |
| Raw `new`/`delete` in modern code | Manual memory management, leak-prone | Use `std::unique_ptr`, `std::make_unique`, RAII |
| Ignoring compiler warnings | Missed bugs, undefined behavior | Treat warnings as errors in CI |
| Using `NULL` or `0` as null pointer | `NULL` may be `0` (integer), not a pointer literal | Use `nullptr` |
| Forgetting `override` on virtual functions | Silently creates new virtual function instead of overriding | Always use `override` keyword |
| Not using `constexpr` for compile-time values | Runtime computation where compile-time suffices | Use `constexpr` for constants and simple computations |

## One-Minute Revision Table

| Feature | Standard | Purpose | Example |
|---------|----------|---------|---------|
| `auto` | C++11 | Type inference | `auto x = 42;` |
| Lambda | C++11 | Anonymous function | `[](int x) { return x * 2; }` |
| `std::move` | C++11 | Enable move semantics | `auto b = std::move(a);` |
| `constexpr` | C++11 | Compile-time evaluation | `constexpr int sq(int x) { return x*x; }` |
| `nullptr` | C++11 | Type-safe null pointer | `int* p = nullptr;` |
| Range-based for | C++11 | Clean iteration | `for (auto& x : vec)` |
| Structured bindings | C++17 | Unpack tuples/pairs | `auto [k, v] = map.begin();` |
| `std::optional` | C++17 | Nullable value type | `std::optional<int> maybe;` |
| `std::string_view` | C++17 | Non-owning string ref | `std::string_view sv = "hi";` |
| `std::variant` | C++17 | Type-safe union | `std::variant<int, string> v;` |
| `if constexpr` | C++17 | Compile-time branching | `if constexpr (sizeof(T) > 4)` |
| Concepts | C++20 | Template constraints | `template<Numeric T> T add(T, T);` |
| Ranges | C++20 | Lazy pipeline algorithms | `vec \| std::views::filter(...)` |
| Coroutines | C++20 | Async/generator functions | `task<int> compute();` |

## Cross-References

- **Templates** → [Module 03: Templates](../03-templates/) — Concepts constrain template parameters
- **STL** → [Module 04: STL](../04-stl/) — Algorithms with lambdas, structured bindings
- **Memory Management** → [Module 05: Memory](../05-memory-management/) — Move semantics, RAII
- **Smart Pointers** → [Module 06: Smart Pointers](../06-smart-pointers/) — `unique_ptr`, `shared_ptr` with modern idioms
- **Concurrency** → [Module 07: Concurrency](../07-concurrency/) — `std::future`, `std::async`, `std::jthread`
- **Design Patterns** → [Module 09: Patterns](../09-design-patterns/) — Strategy, Observer with lambdas
- **Best Practices** → [Module 14: Best Practices](../14-best-practices/) — Modern C++ style guide

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Lambda capturing variable by reference causing dangling | AddressSanitizer + lifetime analysis | Use `[=]` or `[name]` instead of `[&name]` for captured variables that outlive the lambda scope |
| `std::optional::value()` called without checking `has_value()` | Code review + lint rule | Ban `.value()` calls; use `*opt` (unchecked) or `.value_or(default)` (safe) instead |
| `std::move` on const object producing no move | Compiler warning `-Wpessimizing-move` | Enable warning; never `std::move` on `const` objects — it binds to `const&` instead |
| Concept constraint error producing confusing output | C++20 concepts + clear `requires` clauses | Define concepts with descriptive names; use `requires` clauses that match the intended constraint |
| `std::variant` `std::get` throwing `std::bad_variant_access` | Use `std::holds_alternative` check first | Always check `std::holds_alternative<T>(v)` before `std::get<T>(v)`, or use `std::visit` |

## Code Review Checklist

- [ ] `auto` used when type is obvious from context
- [ ] Range-based for loops used for container iteration
- [ ] `nullptr` used instead of `NULL` or `0`
- [ ] Move semantics applied for non-copyable/large objects
- [ ] `constexpr` used for compile-time computable values
- [ ] `std::optional` used instead of sentinel values
- [ ] `std::string_view` used for read-only string parameters
- [ ] C++20 concepts used to constrain templates

## Architecture Considerations

Modern C++ fundamentally changes how you express intent. Lambdas enable inline callbacks without functor classes. Move semantics eliminate unnecessary copies of large objects. `constexpr` shifts computation from runtime to compile time. `std::optional` and `std::variant` make type-safe designs explicit. Concepts make template constraints self-documenting. Together, these features reduce boilerplate, prevent entire categories of bugs, and make code more maintainable.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Lambda + `std::function` for callbacks | Event handlers, signal-slot systems | Type-erased flexibility vs. heap allocation overhead |
| `std::optional` for nullable values | Function return values that may be absent | Explicit absence vs. pointer-based alternatives |
| `std::variant` + `std::visit` for discriminated unions | Type-safe polymorphic values | Safety vs. `std::visit` verbosity |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Lambda capturing local by reference dispatched to thread pool | Use-after-free, exploitable crash | Capture by value for thread-dispatched lambdas; enable ASan in CI |
| `std::variant` type confusion via unchecked `std::get` | Undefined behavior, crash | Always check `std::holds_alternative` before `std::get` |
| `std::string_view` dangling after source string destroyed | Use-after-free, information leakage | Ensure source string outlives `string_view`; prefer `std::string` for owned data |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| C++11 | `auto`, lambdas, move semantics, `nullptr`, `constexpr` | Replace manual iterator declarations with `auto`; use lambdas for callbacks |
| C++17 | `std::optional`, `std::variant`, `std::string_view`, structured bindings | Replace sentinel values with `std::optional`; replace `union` with `std::variant` |
| C++20 | Concepts, ranges, coroutines, `std::format` | Replace SFINAE with concepts; use ranges for lazy pipelines |

## Version Validation

| Feature | C++ Version | Status |
|---------|------------|--------|
| `auto` / lambdas / `std::move` | C++11 | Widely supported |
| `std::optional` / `std::variant` / `std::string_view` | C++17 | Widely supported |
| `if constexpr` / structured bindings | C++17 | Widely supported |
| Concepts / ranges / coroutines | C++20 | Supported in GCC 10+, Clang 12+, MSVC 19.22+ |

## Interview Questions

1. **When should you use `auto` and when should you avoid it?**: Use `auto` when the type is obvious from context (`auto it = vec.begin()`). Avoid it when the type is not obvious (`auto result = compute_value()`) or when a specific type is needed for API contracts.

2. **Explain move semantics and when `std::move` is needed**: Move semantics transfer ownership of resources (heap memory, file handles) from one object to another instead of copying. `std::move` is a cast that enables move overloads — use it when transferring ownership of large or non-copyable objects.

3. **What is `std::optional` and when should you use it?**: `std::optional<T>` represents a value that may or may not exist. Use it instead of sentinel values (`-1`, `""`, `nullptr`) or `bool + T` pairs for function return values that may legitimately be absent.

4. **How do C++20 concepts improve on SFINAE?**: Concepts produce clear error messages stating which constraint was violated, are self-documenting, enable constrained auto, and replace the cryptic `std::enable_if` pattern. They make template code readable and maintainable.

5. **What is the difference between `std::move` and copy elision (NRVO)?**: `std::move` casts an lvalue to an rvalue reference to enable move construction. NRVO (Named Return Value Optimization) eliminates the copy/move entirely by constructing the return value directly in the caller's stack frame. C++17 mandates copy elision for prvalues (guaranteed copy elision).

6. **What is `std::string_view` and what are its lifetime pitfalls?**: `std::string_view` is a non-owning, read-only view of a contiguous character sequence. It does not own the data. Pitfall: if the underlying string is destroyed (e.g., a temporary), the `string_view` becomes dangling. Always ensure the source string outlives the view. Use `std::string` when you need ownership.

7. **When should you use `std::variant` over a class hierarchy?**: Use `std::variant` for a small, fixed set of types where you need a discriminated union (e.g., JSON values: int, double, string, array, object). Use class hierarchies when the set of types is open-ended (extensible via inheritance) or when you need virtual dispatch.

8. **What is the difference between `std::unique_ptr` and `std::shared_ptr`?**: `std::unique_ptr` is a lightweight, non-copyable smart pointer with exclusive ownership — zero overhead over raw pointer. `std::shared_ptr` uses atomic reference counting for shared ownership, with overhead for the control block. Prefer `unique_ptr` unless ownership must be shared.

9. **How does `constexpr` differ from `const`?**: `const` means "read-only at runtime." `constexpr` means "evaluable at compile time." A `constexpr` function can be called with runtime arguments (falling back to runtime evaluation), but when called with compile-time constants, it produces a compile-time result. `const` members can be initialized at runtime; `constexpr` members must be initialized with constant expressions.

10. **Explain structured bindings and their limitations**: Structured bindings decompose tuples, pairs, arrays, or structs into named variables: `auto [x, y] = get_point()`. Limitation: the binding names cannot be used as runtime values (they are aliases, not variables). You cannot rebind them. For `const auto&` bindings, the original object must outlive the binding.

11. **What is `if constexpr` and how does it differ from runtime `if`?**: `if constexpr` branches at compile time — only the taken branch is instantiated. This enables template specialization without SFINAE or `std::enable_if`. The discarded branch is not compiled, so it can contain invalid code for that type (e.g., calling member functions that don't exist on all types).

12. **How do you handle exceptions in move constructors?**: Move constructors and move assignment operators should be marked `noexcept`. If a move can throw, the STL containers will fall back to copying. Use `try`/`catch` inside the move operation if necessary, but prefer operations that don't throw (e.g., `std::swap` uses `noexcept` moves).

13. **What is the Rule of Five in modern C++?**: If you define any of these, define all five: destructor, copy constructor, copy assignment operator, move constructor, move assignment operator. Modern C++ simplifies this: if you use RAII correctly (smart pointers, containers), you often only need `= default` or don't need to declare any of them (Rule of Zero).

14. **When should you use `std::function` versus a direct lambda type?**: `std::function` is a type-erased callable wrapper — it can store any callable with the right signature, but it may heap-allocate. Use it when you need to store heterogeneous callables (e.g., function pointers, lambdas, bind expressions) in the same container. For performance-critical code, use the lambda type directly or `std::move_only_function` (C++23).

15. **What are coroutines and when should you use them?**: Coroutines (C++20) are functions that can suspend and resume execution. Use them for generators (`std::generator`), asynchronous I/O, lazy evaluation, and state machines. They avoid callback nesting ("callback hell") and stack allocation for suspended frames. Use them when you need cooperative multitasking without the overhead of OS threads.

## References

- [Effective Modern C++ — Scott Meyers](https://www.amazon.com/Effective-Modern-CUDA-Improve-Specific/dp/1491903996)
- [CppReference — C++11/17/20 Features](https://en.cppreference.com/w/cpp/17)
- [C++ Core Guidelines — Modern C++](https://isocpp.github.io/CppCoreGuidelines/CppCoreGuidelines#S-.invokeLater)
- [CppCon Talk: C++17 Features](https://youtube.com/cppcon)
