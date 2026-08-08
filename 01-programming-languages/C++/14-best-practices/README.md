# Best Practices — C++

## Why It Matters

Best practices are not arbitrary rules — they're hard-won lessons from millions of lines of production code. When you follow them, you prevent entire categories of bugs, make code review faster, and ensure your team ships reliable software instead of writing messy, inconsistent code that's hard to maintain.

## What It Is

Best practices in C++ encompass const correctness, RAII, smart pointers, error handling patterns, naming conventions, and code organization principles that make code clear, correct, and maintainable by anyone on your team.

## Engineering Decision Framework

| Practice | Approach | When to Use | When NOT to Use |
|----------|----------|-------------|-----------------|
| Const correctness | Mark everything `const` by default | Always — unless the value genuinely needs modification | Don't add `const` to output parameters |
| RAII | Wrap all resources in RAII objects | Always — mutexes, files, sockets, memory | Raw `new`/`delete` outside RAII wrappers |
| Smart pointers | `unique_ptr` for ownership, `raw ptr` for non-owning | When managing heap-allocated objects | `shared_ptr` when `unique_ptr` suffices |
| Error handling | Exceptions for exceptional cases, error codes for expected | Exceptions for unrecoverable errors, codes for validation | Exceptions in performance-critical loops |
| Naming | `camelCase` for functions, `PascalCase` for types | Follow your team's convention consistently | Mixing conventions in one project |
| Functions | Small, single-responsibility functions | Always — aim for 5-30 lines | Functions doing multiple unrelated things |
| Headers | Minimal includes, forward declarations | Always — reduce compilation dependencies | Including `<bits/stdc++.h>` in production |

## Expanded Code Examples

### Const Correctness

```cpp
#include <string>
#include <vector>

class User {
    std::string name_;
    int age_;
public:
    User(std::string name, int age) : name_(std::move(name)), age_(age) {}

    // const member function — doesn't modify object state
    const std::string& getName() const { return name_; }
    int getAge() const { return age_; }

    // Non-const member function — modifies object
    void setName(const std::string& name) { name_ = name; }
    void setAge(int age) { age_ = age; }
};

// const reference parameter — promise not to modify
void printUser(const User& user) {
    std::cout << user.getName() << " (age " << user.getAge() << ")\n";
}

// Mutable reference — will modify
void birthday(User& user) {
    user.setAge(user.getAge() + 1);
}

// Const pointer to const — can't modify pointer or object
void processUser(const User* const user) {
    std::cout << user->getName() << "\n";
}

// When NOT to use const: output parameters
// Bad: const prevents the function from writing to the output
// void getValues(const int& out1, const int& out2);  // Wrong!

// Good: use references or return values for output
std::pair<int, int> getValues() {
    return {42, 99};
}
```

### RAII Resource Management

```cpp
#include <fstream>
#include <mutex>
#include <memory>
#include <stdexcept>

// File resource — RAII
class FileGuard {
    std::FILE* file_;
public:
    explicit FileGuard(const char* filename, const char* mode)
        : file_(std::fopen(filename, mode)) {
        if (!file_) {
            throw std::runtime_error("Failed to open file");
        }
    }

    ~FileGuard() {
        if (file_) std::fclose(file_);
    }

    // Delete copy, allow move
    FileGuard(const FileGuard&) = delete;
    FileGuard& operator=(const FileGuard&) = delete;
    FileGuard(FileGuard&& other) noexcept : file_(other.file_) {
        other.file_ = nullptr;
    }
    FileGuard& operator=(FileGuard&& other) noexcept {
        if (this != &other) {
            if (file_) std::fclose(file_);
            file_ = other.file_;
            other.file_ = nullptr;
        }
        return *this;
    }

    std::FILE* get() const { return file_; }
};

// Mutex guard — RAII
class ThreadSafeCounter {
    mutable std::mutex mutex_;
    int count_ = 0;
public:
    void increment() {
        std::lock_guard<std::mutex> lock(mutex_);  // RAII lock
        ++count_;
    }

    int get() const {
        std::lock_guard<std::mutex> lock(mutex_);
        return count_;
    }
    // Mutex automatically unlocked when lock_guard goes out of scope
};

// Smart pointer RAII
void process_data() {
    auto data = std::make_unique<int[]>(1000);  // Automatically freed
    auto shared = std::make_shared<ThreadSafeCounter>();  // Ref-counted

    // No manual delete needed — RAII handles cleanup
}
```

### Rule of Zero / Rule of Five

```cpp
#include <string>
#include <vector>
#include <memory>

// Rule of Zero: If class manages no resources, don't declare special members
class UserProfile {
    std::string name_;
    std::string email_;
    int age_;
public:
    UserProfile(std::string name, std::string email, int age)
        : name_(std::move(name)), email_(std::move(email)), age_(age) {}

    // No destructor, copy/move constructors needed — compiler generates correct ones
};

// Rule of Five: If class manages a resource, declare all five
class Buffer {
    int* data_;
    size_t size_;
public:
    explicit Buffer(size_t size) : size_(size), data_(new int[size]()) {}

    ~Buffer() { delete[] data_; }

    Buffer(const Buffer& other) : size_(other.size_), data_(new int[other.size_]) {
        std::copy(other.data_, other.data_ + other.size_, data_);
    }

    Buffer& operator=(const Buffer& other) {
        if (this != &other) {
            delete[] data_;
            size_ = other.size_;
            data_ = new int[other.size_];
            std::copy(other.data_, other.data_ + other.size_, data_);
        }
        return *this;
    }

    Buffer(Buffer&& other) noexcept : data_(other.data_), size_(other.size_) {
        other.data_ = nullptr;
        other.size_ = 0;
    }

    Buffer& operator=(Buffer&& other) noexcept {
        if (this != &other) {
            delete[] data_;
            data_ = other.data_;
            size_ = other.size_;
            other.data_ = nullptr;
            other.size_ = 0;
        }
        return *this;
    }

    size_t size() const { return size_; }
    int* data() { return data_; }
    const int* data() const { return data_; }
};
```

### Error Handling Patterns

```cpp
#include <stdexcept>
#include <optional>
#include <string>
#include <iostream>

// Exception-based: for exceptional cases (file not found, network error)
std::string read_config(const std::string& path) {
    std::ifstream file(path);
    if (!file.is_open()) {
        throw std::runtime_error("Cannot open config: " + path);
    }
    std::string content((std::istreambuf_iterator<char>(file)),
                        std::istreambuf_iterator<char>());
    return content;
}

// Error code pattern: for expected, recoverable failures
enum class ParseError {
    Success,
    EmptyInput,
    InvalidFormat,
    Overflow
};

std::optional<int> parse_int(const std::string& s, ParseError& error) {
    if (s.empty()) {
        error = ParseError::EmptyInput;
        return std::nullopt;
    }
    try {
        size_t pos;
        int val = std::stoi(s, &pos);
        if (pos != s.size()) {
            error = ParseError::InvalidFormat;
            return std::nullopt;
        }
        error = ParseError::Success;
        return val;
    } catch (const std::out_of_range&) {
        error = ParseError::Overflow;
        return std::nullopt;
    } catch (...) {
        error = ParseError::InvalidFormat;
        return std::nullopt;
    }
}

// Usage
void safe_parse() {
    ParseError error;
    auto value = parse_int("42", error);
    if (error == ParseError::Success) {
        std::cout << "Parsed: " << *value << "\n";
    } else {
        std::cerr << "Parse failed with error code: " << static_cast<int>(error) << "\n";
    }
}
```

### Naming and Code Style

```cpp
// Class names: PascalCase
class ShoppingCart {
public:
    // Member functions: camelCase
    void addItem(const std::string& name, int quantity);

    // Getters: get prefix
    int itemCount() const;  // Or just itemCount() without get

    // Setters: set prefix
    void setMaxItems(int max);

    // Boolean getters: is/has/can prefix
    bool isEmpty() const;
    bool hasItem(const std::string& name) const;
    bool canCheckout() const;
private:
    // Member variables: trailing underscore
    std::vector<Item> items_;
    int maxItems_;
};

// Free functions: camelCase
double calculateTotal(const ShoppingCart& cart);

// Constants: kPrefix or ALL_CAPS
constexpr double kTaxRate = 0.08;
constexpr int kMaxRetries = 3;

// Template parameters: PascalCase
template <typename ValueType>
class Cache {
    // ...
};

// Namespaces: camelCase or snake_case (pick one)
namespace network {
    class TcpClient { /* ... */ };
}

// Enum values: PascalCase or ALL_CAPS (pick one)
enum class Color { Red, Green, Blue };
// or
enum class Color { RED, GREEN, BLUE };
```

### Composition Over Inheritance

```cpp
#include <memory>
#include <string>

// BAD: Deep inheritance hierarchy
class ShapeBase {
public:
    virtual ~ShapeBase() = default;
    virtual double area() const = 0;
    virtual void draw() const = 0;
};

class Circle : public ShapeBase {
    double radius_;
public:
    double area() const override { return 3.14159 * radius_ * radius_; }
    void draw() const override { /* draw circle */ }
};

// GOOD: Composition
class Circle {
    double radius_;
    Color color_;
public:
    Circle(double r, Color c) : radius_(r), color_(c) {}
    double area() const { return 3.14159 * radius_ * radius_; }
    void draw() const { /* draw circle */ }
};

class DrawableShape {
    Circle circle_;         // Composed, not inherited
    std::string label_;
public:
    DrawableShape(double r, Color c, std::string label)
        : circle_(r, c), label_(std::move(label)) {}

    double area() const { return circle_.area(); }
    void draw() const {
        circle_.draw();
        // Also draw label
    }
};
```

## Production Incidents

### Incident 1: Missing Const Causing Accidental Modification
**Problem**: A rendering engine produced different output on different platforms despite identical source code, causing cross-platform visual inconsistencies.

**Cause**: A function `processVertex(vec3& vertex)` was accidentally modifying the vertex position. On one platform, the caller happened to copy the vertex before passing it; on another, it passed by reference directly. The missing `const` allowed the modification, but platform-specific calling conventions hid the bug.

**Impact**: Visual glitches on 30% of platforms. 2 weeks of debugging. Customer complaints about inconsistent rendering.

**Detection**: Adding `const` to the function signature revealed 5 other callers that were accidentally relying on (or being affected by) the modification.

**Solution**: Added `const` to all non-modifying functions. Enabled `-Werror` to catch future const violations. Ran a codebase-wide audit for non-const reference parameters that should be const.

**Prevention**: Enable `-Werror` in CI. Code review checklist must verify const correctness. Use `const` by default and remove only when modification is needed.

### Incident 2: Memory Leak from Missing Destructor
**Problem**: A logging system leaked 10MB/hour of heap memory, eventually consuming all available RAM after 3 days.

**Cause**: The `LogBuffer` class allocated a `char*` buffer in its constructor but had no destructor to free it. The class was used in a short-lived scope, so the leak wasn't obvious in testing. In production, the buffer was reallocated thousands of times per minute.

**Impact**: Server OOM crash every 3 days. Auto-restart mitigated but caused 30-second outages. Memory usage monitoring showed steady climb.

**Detection**: `valgrind --leak-check=full` showed thousands of `LogBuffer` allocations without matching frees.

**Solution**: Added destructor to `LogBuffer` to free the buffer. Switched to `std::vector<char>` to eliminate manual memory management entirely. Added the class to the RAII resource tracking list.

**Prevention**: Follow Rule of Zero (use `std::vector` instead of raw `new[]`). If raw allocation is unavoidable, follow Rule of Five. Run valgrind in CI for long-running processes.

### Incident 3: Function Doing Too Many Things
**Problem**: A single 200-line function handled authentication, database lookup, caching, and response formatting. A bug in the caching logic was impossible to fix without risking the authentication logic.

**Cause**: The function violated single responsibility. Developers were afraid to modify it because changes in one section could break another. The function had 15 local variables and 8 levels of nesting.

**Impact**: Bug fix took 3 days instead of 2 hours. Code review took 4 hours per change. 2 developers quit citing code quality issues.

**Detection**: Code review flagged the function as "too complex." Cyclomatic complexity metric showed 23 (target: <10).

**Solution**: Split into 4 functions: `authenticate()`, `lookupUser()`, `getCachedUser()`, `formatResponse()`. Each function was 15-30 lines with clear responsibility. Added unit tests for each function independently.

**Prevention**: Enforce maximum function length (50 lines) in code review. Use cyclomatic complexity tools. Extract functions proactively when you notice nesting depth > 3.

## Production Checklist

- [ ] Mark everything `const` by default
- [ ] Use RAII for all resource management (files, locks, memory)
- [ ] Follow Rule of Zero or Rule of Five
- [ ] Prefer smart pointers over raw `new`/`delete`
- [ ] Use error codes for expected failures, exceptions for unexpected
- [ ] Keep functions small and single-responsibility (5-30 lines)
- [ ] Use descriptive naming conventions consistently
- [ ] Prefer composition over inheritance
- [ ] Enable compiler warnings (`-Wall -Wextra -Wpedantic -Werror`)
- [ ] Run static analysis (clang-tidy, cppcheck) in CI
- [ ] Code review every change — no exceptions
- [ ] Write documentation for public APIs

## Maturity Levels

| Level | Capabilities |
|-------|-------------|
| **Beginner** | Consistent naming, basic const correctness, simple error handling |
| **Intermediate** | RAII everywhere, smart pointers, Rule of Five, composition over inheritance |
| **Advanced** | Design principles (SOLID), static analysis, code review leadership, style guide enforcement |

## Common Myths — Debunked

| Myth | Reality |
|------|---------|
| "Comments are always good" | Bad comments are worse than no comments. Code should be self-documenting; comments explain *why*, not *what*. |
| "More features mean better code" | YAGNI (You Aren't Gonna Need It). Add features when needed, not speculatively. |
| "Optimization is always necessary" | Optimize only when profiling shows a bottleneck. Clear code is more valuable than fast code. |
| "Smart pointers solve all memory problems" | Smart pointers manage ownership, but they don't fix circular references or logical leaks. |
| "Code review slows down development" | Code review catches bugs before production. The time saved in debugging far exceeds review time. |
| "Best practices are optional" | Best practices prevent entire categories of bugs. Skipping them is gambling with production stability. |

## One-Minute Revision Table

| Practice | Description | Key Benefit |
|----------|-------------|-------------|
| Const correctness | Mark non-modifying code `const` | Prevents accidental modification |
| RAII | Resource management via constructors/destructors | Zero resource leaks |
| Rule of Five | Declare all special members if managing resources | Correct copy/move/delete behavior |
| Smart pointers | `unique_ptr` for ownership, raw ptr for non-owning | Automatic memory management |
| Single responsibility | One function, one job | Easier testing, debugging, modification |
| Composition over inheritance | Build from smaller parts, don't extend | Flexible, less coupling |
| Compiler warnings | Enable `-Wall -Wextra -Werror` | Catch bugs at compile time |
| Static analysis | clang-tidy, cppcheck | Automated code quality checks |
| Code review | Every change reviewed by another developer | Knowledge sharing, bug prevention |

## Cross-Linked Related Topics

- **OOP** → [Module 02: OOP](../02-oop/) — Inheritance, polymorphism, encapsulation principles
- **Memory Management** → [Module 05: Memory](../05-memory-management/) — RAII, smart pointers, Rule of Five
- **Modern C++** → [Module 08: Modern C++](../08-modern-cpp/) — `auto`, lambdas, `constexpr` best practices
- **Design Patterns** → [Module 09: Design Patterns](../09-design-patterns/) — SOLID principles guide pattern selection
- **Testing** → [Module 10: Testing](../10-testing/) — Testability as a best practice
- **Build Systems** → [Module 13: Build Systems](../13-build-systems/) — Compiler warnings, static analysis in CI

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Missing `const` causing accidental modification | `-Werror` + clang-tidy `readability-make-member-function-const` | Enable the clang-tidy check; add `const` to all non-modifying member functions |
| Memory leak from missing destructor (Rule of Five violation) | Valgrind + clang-tidy `cppcoreguidelines-special-member-functions` | Run Valgrind; enable the clang-tidy check to detect missing special members |
| Function doing too many things (cyclomatic complexity > 10) | `lizard` or `cccc` complexity tools | Run `lizard --ncs file.cpp`; extract functions when complexity exceeds 10 |
| Naming inconsistency across codebase | clang-tidy readability checks | Enable `readability-naming-conventions`; enforce consistent naming in code review |
| Deep inheritance causing fragile base class | Code review + composition refactor | Replace inheritance with composition; limit hierarchy to 2-3 levels |

## Code Review Checklist

- [ ] Everything marked `const` by default (remove only when modification needed)
- [ ] RAII used for all resource management (files, locks, memory)
- [ ] Rule of Zero or Rule of Five followed for all classes
- [ ] Functions are small and single-responsibility (5-30 lines)
- [ ] Smart pointers used for ownership; raw pointers for non-owning references
- [ ] Compiler warnings enabled (`-Wall -Wextra -Wpedantic -Werror`)
- [ ] Static analysis (clang-tidy, cppcheck) run in CI

## Architecture Considerations

Best practices encode hard-won lessons from millions of lines of production code. Const correctness prevents accidental modification and enables compiler optimizations. RAII eliminates resource leaks by design. Single-responsibility functions make code testable and maintainable. Composition over inheritance reduces coupling. These practices form the foundation that makes complex systems maintainable by teams over long periods.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Rule of Zero (use STL types) | Classes managing no raw resources | Zero boilerplate vs. less explicit ownership semantics |
| Composition over inheritance | Building complex objects from simpler parts | Clearer ownership vs. more delegation code |
| Error codes for expected failures | Recoverable errors (parse failures, validation) | No stack unwinding overhead vs. less ergonomic error propagation |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Missing `const` allowing unintended modification of security-critical data | Privilege escalation, data corruption | Audit all non-const reference parameters; enable `-Werror` |
| Deep inheritance hierarchy enabling unintended virtual dispatch | Control-flow hijacking | Limit inheritance depth; prefer composition; use `final` |
| Functions doing too many things hiding security checks | Bypassing authentication/authorization | Single-responsibility: separate auth, validation, and business logic |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| C++11 | `= default`, `= delete` for explicit special member control | Use `= default` for trivial special members; `= delete` to prevent copying |
| C++17 | `std::optional`, `std::variant` for safer type design | Replace sentinel values with `std::optional`; replace unions with `std::variant` |
| C++20 | Concepts for self-documenting constraints | Replace `static_assert` with `requires` clauses for template constraints |

## Version Validation

| Feature | C++ Version | Status |
|---------|------------|--------|
| `const` correctness | All versions | Universal |
| `= default` / `= delete` | C++11 | Widely supported |
| `std::optional` | C++17 | Widely supported |
| Concepts for constraints | C++20 | Supported in GCC 10+, Clang 12+, MSVC 19.22+ |

## Interview Questions

1. **What is the Rule of Zero and when should you follow it?**: Rule of Zero — if your class manages no raw resources, don't declare any special member functions (destructor, copy/move). Use RAII types (`std::string`, `std::vector`, `std::unique_ptr`) that handle their own resources. The compiler generates correct special members automatically.
2. **Why is const correctness important?**: `const` prevents accidental modification, documents intent, enables compiler optimizations, and makes code self-documenting. A `const` member function promises not to modify state, making code reasoning easier.
3. **What is the single-responsibility principle?**: A function or class should have one reason to change — one job. Functions doing multiple unrelated things are hard to test, debug, and modify. Keep functions small (5-30 lines) and focused.
4. **How does composition improve on inheritance?**: Composition models HAS-A relationships, provides flexible ownership, avoids fragile base class problems, and enables runtime behavior swapping. Inheritance should only be used for true IS-A behavioral contracts.
5. **When should you use error codes vs exceptions?**: Use error codes for expected, recoverable failures (file not found, parse error). Use exceptions for unexpected, unrecoverable errors (out of memory, invariant violation). Exceptions propagate errors automatically; error codes require explicit checking.

## References

- [Effective C++ — Scott Meyers](https://www.amazon.com/Effective-Specific-Ways-Improve-Programs/dp/0321334876)
- [C++ Core Guidelines](https://isocpp.github.io/CppCoreGuidelines/CppCoreGuidelines)
- [CppCoreGuidelines: Best Practices](https://isocpp.github.io/CppCoreGuidelines/CppCoreGuidelines#S-iostream)
- [Google C++ Style Guide](https://google.github.io/styleguide/cppguide.html)
