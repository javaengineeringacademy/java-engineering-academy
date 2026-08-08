# Modern C++ — C++

## Why It Matters

Modern C++ (C++11/14/17/20) is not just syntax sugar — it fundamentally changes how you express intent, manage resources, and write safe, performant code. When you master these features, you get fewer bugs, clearer code, and the ability to use the full power of the language instead of writing "C++03-style" code with the parking brake on.

## What It Is

Modern C++ encompasses features from C++11 onwards, including auto type inference, lambdas, move semantics, smart pointers, constexpr, optional, variant, string_view, and concepts that transform how you write C++ code.

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

## Cross-Linked Related Topics

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

## References

- [Effective Modern C++ — Scott Meyers](https://www.amazon.com/Effective-Modern-CUDA-Improve-Specific/dp/1491903996)
- [CppReference — C++11/17/20 Features](https://en.cppreference.com/w/cpp/17)
- [C++ Core Guidelines — Modern C++](https://isocpp.github.io/CppCoreGuidelines/CppCoreGuidelines#S-.invokeLater)
- [CppCon Talk: C++17 Features](https://youtube.com/cppcon)
