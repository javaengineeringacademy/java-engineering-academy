# Best Practices — C++

## Overview

Best practices in C++ are standardized guidelines and patterns derived from decades of production use, codified in the C++ Core Guidelines (Bjarne Stroustrup & Herb Sutter), Scott Meyers' Effective C++ series, and MISRA/CERT safety standards. They address const correctness, RAII resource management, Rule of Zero/Three/Five, smart pointer ownership semantics, and exception safety guarantees. Following these practices prevents memory leaks, undefined behavior, data races, and entire categories of security vulnerabilities while enabling compiler optimizations through predictable code patterns.

## Why It Matters

Best practices are not arbitrary rules — they're hard-won lessons from millions of lines of production code. When you follow them, you prevent entire categories of bugs, make code review faster, and ensure your team ships reliable software instead of writing messy, inconsistent code that's hard to maintain.

## What It Is

Best practices in C++ encompass const correctness, RAII, smart pointers, error handling patterns, naming conventions, and code organization principles that make code clear, correct, and maintainable by anyone on your team.

## Learning Objectives

- Understand the C++ Core Guidelines and their rationale
- Apply const correctness systematically to prevent accidental modification
- Implement RAII for all resource management (memory, files, locks, handles)
- Distinguish when to use Rule of Zero vs. Rule of Three vs. Rule of Five
- Select appropriate smart pointer types (`unique_ptr`, `shared_ptr`, `weak_ptr`) based on ownership semantics
- Apply exception safety guarantees (nothrow, strong, basic) in function design
- Use `noexcept` specifiers to enable move semantics and compiler optimizations
- Compose classes using HAS-A relationships instead of deep inheritance hierarchies
- Apply SOLID principles in C++ class and module design
- Configure and enforce compiler warnings, static analysis, and CI quality gates

## Prerequisites

| Module | Why It's Needed |
|--------|----------------|
| [02 — OOP](../02-oop/) | Virtual dispatch, inheritance semantics, encapsulation — understand why composition often beats inheritance |
| [05 — Memory Management](../05-memory-management/) | Heap vs stack, `new`/`delete`, lifetime semantics — understand why RAII exists |
| [06 — Smart Pointers](../06-smart-pointers/) | `unique_ptr`, `shared_ptr`, `weak_ptr` ownership models — foundational to modern memory best practices |

## History

| Year | Milestone | Significance |
|------|-----------|--------------|
| 1998 | C++98 Standard | First ISO standard; established core language but limited library support for best practices |
| 2003 | C++03修订 | Defect fixes; no new best practice patterns |
| 2011 | C++11 | `= default`/`= delete`, move semantics, `noexcept`, `unique_ptr`/`shared_ptr` — enabled modern RAII and Rule of Zero |
| 2014 | C++14 | `constexpr` relaxations, `std::make_unique` — reduced raw resource handling |
| 2015 | C++ Core Guidelines published | Stroustrup & Sutter codified best practices into a searchable, enforceable reference |
| 2017 | C++17 | `std::optional`, `std::variant`, `std::string_view` — safer alternatives to sentinel values and unions |
| 2020 | C++20 | Concepts, ranges, `std::format` — self-documenting template constraints, safer range-based operations |
| 2023 | C++23 | `std::expected`, `std::flat_map` — standardized error handling without exceptions |

## Production Notes

- **Compiler warnings are non-negotiable**: Always compile with `-Wall -Wextra -Wpedantic -Werror` in CI. GCC and Clang catch const violations, unused variables, and narrowing conversions that cause silent bugs.
- **Static analysis runs on every commit**: clang-tidy with C++ Core Guidelines checks (`cppcoreguidelines-*`, `modernize-*`, `readability-*`) catches violations before code review.
- **Valgrind/ASan/TSan in CI**: Run memory leak detection (valgrind), address sanitizer (ASan), and thread sanitizer (TSan) on every test suite run.
- **No `#include` of implementation details**: Forward-declare classes in headers; include only in `.cpp` files to minimize compilation dependencies.
- **Exception safety is a design-time decision**: Choose your exception safety guarantee (nothrow, strong, basic) before writing the function body, not after.

## Core Concepts

| Concept | Description | Core Guideline Reference |
|---------|-------------|------------------------|
| **Const correctness** | Mark everything `const` by default; remove only when modification is needed | [Con: Constants](https://isocpp.github.io/CppCoreGuidelines/CppCoreGuidelines#S-constants) |
| **RAII** | Acquire resources in constructors, release in destructors; no manual cleanup | [R: Resource management](https://isocpp.github.io/CppCoreGuidelines/CppCoreGuidelines#S-resource) |
| **Rule of Zero** | If class manages no raw resources, declare no special members; let compiler generate them | [C.20](https://isocpp.github.io/CppCoreGuidelines/CppCoreGuidelines#c20-if-possible-avoid-define-defaulted-or-deleted-special-members) |
| **Rule of Three/Five** | If class manages a resource, explicitly declare or delete all five special members | [C.21](https://isocpp.github.io/CppCoreGuidelines/CppCoreGuidelines#c21-if-you-define-or-delete-any-default-copy-move-destructor-or-copy-move-operator-define-or-delete-them-all) |
| **Smart pointer ownership** | `unique_ptr` for sole ownership, `shared_ptr` for shared ownership, `raw ptr` for non-owning观察 | [R.20-34](https://isocpp.github.io/CppCoreGuidelines/CppCoreGuidelines#r-resource-management) |
| **Exception safety** | `noexcept` for moves and destructors; basic/strong guarantee for functions | [E.12-16](https://isocpp.github.io/CppCoreGuidelines/CppCoreGuidelines#e-error-handling) |
| **Composition over inheritance** | Model HAS-A with composition; use inheritance only for true IS-A behavioral contracts | [C.120](https://isocpp.github.io/CppCoreGuidelines/CppCoreGuidelines#c120-use-class-hierarchy-for-related-variants-of-entities-and-level-concepts-for-common-utils) |
| **Single responsibility** | One function, one job; one class, one reason to change | [F: Functions](https://isocpp.github.io/CppCoreGuidelines/CppCoreGuidelines#S-functions) |

## Internal Working

Best practices in C++ are not arbitrary — they exploit compile-time and runtime mechanisms that produce correct, efficient code.

**How const correctness works at compile-time**: The compiler enforces `const` as a type qualifier. A `const` reference parameter prevents calling non-const member functions on the object, effectively freezing its state. This enables the compiler to place objects in read-only memory sections and eliminate redundant loads. `constexpr` extends this to compile-time evaluation, eliminating runtime cost entirely.

**How RAII works at runtime**: Constructor acquires a resource (allocates memory, opens file, locks mutex). Destructor releases it when the object goes out of scope (stack unwinding from normal return, exception, or `break`/`continue`). This creates a deterministic, exception-safe cleanup path. The compiler generates destructor calls automatically via stack unwinding — no garbage collector needed.

**How smart pointer ownership works at compile-time**: `std::unique_ptr` uses move semantics (non-copyable) to enforce single ownership. The compiler prevents accidental copies that would cause double-free. `std::shared_ptr` uses an atomic reference count allocated on the heap; the last owner's destructor triggers deletion. The type system encodes ownership semantics — misuse is a compile-time error, not a runtime bug.

**How compiler warnings prevent bugs**: `-Wall -Wextra` enables warnings for uninitialized variables, narrowing conversions, missing return statements, and implicit fallthrough. `-Werror` promotes these to errors, preventing code with warnings from compiling. Clang-tidy checks like `cppcoreguidelines-owning-memory` analyze code patterns against the Core Guidelines and flag violations at build time.

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

## Syntax

### Const Correctness Patterns

```cpp
// Const reference parameter — read-only access, no copy
void process(const std::string& data);

// Const member function — promise not to modify object state
int getSize() const;

// Const pointer to const — can't modify pointer or pointed-to object
void read(const int* const ptr);

// Constexpr — compile-time evaluation
constexpr int factorial(int n) { return n <= 1 ? 1 : n * factorial(n - 1); }
```

### RAII Declaration Patterns

```cpp
// Rule of Zero — no special members declared
class Widget {
    std::string name_;
    std::vector<int> data_;
};

// Rule of Five — all five special members declared
class Buffer {
    int* data_;
    size_t size_;
public:
    explicit Buffer(size_t n);
    ~Buffer();
    Buffer(const Buffer&);
    Buffer& operator=(const Buffer&);
    Buffer(Buffer&&) noexcept;
    Buffer& operator=(Buffer&&) noexcept;
};

// = default / = delete — explicit special member control
class NonCopyable {
public:
    NonCopyable() = default;
    NonCopyable(const NonCopyable&) = delete;
    NonCopyable& operator=(const NonCopyable&) = delete;
};
```

### Smart Pointer Ownership Patterns

```cpp
// Unique ownership — non-copyable, transferable
auto resource = std::make_unique<Connection>(addr);

// Shared ownership — reference-counted
auto shared = std::make_shared<Session>(socket);

// Weak observer — breaks circular references
std::weak_ptr<Node> parent;

// Non-owning observation — raw pointer
void observe(const Node* node);
```

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

## Performance Considerations

| Guideline | Performance Impact | Mechanism |
|-----------|-------------------|-----------|
| **`const` correctness** | Enables compiler to place data in read-only memory, eliminate redundant loads, and inline more aggressively | Type system conveys aliasing information to optimizer |
| **`constexpr` functions** | Compile-time evaluation eliminates runtime cost entirely | Computed at compile time; result embedded as constant |
| **`noexcept` on moves** | Enables `std::vector` reallocation to use moves instead of copies | `std::vector` checks `noexcept` to decide move vs. copy |
| **`unique_ptr` over `shared_ptr`** | `unique_ptr` has zero overhead vs. raw pointer; `shared_ptr` has atomic ref-count overhead | `unique_ptr` is a zero-cost abstraction; `shared_ptr` allocates control block |
| **RAII vs manual cleanup** | No performance penalty; deterministic destruction enables optimizer | Stack-based cleanup eliminates branch prediction misses from manual checks |
| **Rule of Zero** | Compiler-generated special members are often inlined and optimized better than hand-written | Compiler has full visibility into member-wise operations |
| **Small function inlining** | 5-30 line functions are candidates for inlining; large functions are not | Inliner thresholds vary by compiler (typically 25-100 instructions) |
| **Forward declarations** | Reduces header includes, improving compilation parallelism and reducing PCH invalidation | Fewer includes = less dependency = more parallel builds |
| **`std::move` for large parameters** | Prevents unnecessary copies when transferring ownership into functions | Enables move constructor invocation instead of copy constructor |
| **`emplace_back` over `push_back`** | Constructs element in-place, avoiding temporary object creation and move | Perfect forwarding to in-place constructor |

## Best Practices

| Category | Practice | Why |
|----------|----------|-----|
| **Const correctness** | Mark everything `const` by default; remove only when modification is needed | Prevents accidental mutation, enables compiler optimizations, documents intent |
| **RAII** | Acquire resources in constructors, release in destructors | Eliminates resource leaks; exception-safe cleanup; deterministic lifetime |
| **Rule of Zero** | Use RAII types (`std::string`, `std::vector`, `std::unique_ptr`); declare no special members | Compiler generates correct copy/move/delete; zero boilerplate |
| **Rule of Five** | If managing raw resources, declare or `=delete` all five special members | Prevents double-free, use-after-free, shallow-copy bugs |
| **Smart pointers** | `unique_ptr` for sole ownership; `shared_ptr` for shared; raw ptr for non-owning | Encodes ownership semantics in the type system; misuse is a compile error |
| **Error handling** | Exceptions for exceptional errors; error codes for expected failures; `std::optional` for nullable returns | Each mechanism fits its use case; exceptions for stack unwinding, codes for control flow |
| **Single responsibility** | One function, one job; one class, one reason to change | Testable, debuggable, modifiable units |
| **Composition over inheritance** | HAS-A with composition; IS-A only for true behavioral contracts | Flexible ownership, avoids fragile base class problem |
| **Compiler warnings** | `-Wall -Wextra -Wpedantic -Werror` in CI | Catches uninitialized variables, narrowing, missing returns at build time |
| **Static analysis** | clang-tidy with `cppcoreguidelines-*` and `modernize-*` checks | Automated Core Guidelines enforcement; catches violations before code review |
| **Minimal includes** | Forward-declare in headers; include only in `.cpp` files | Reduces compilation dependencies; faster builds |
| **Move semantics** | Use `std::move` to transfer ownership; return by value to enable NRVO | Eliminates unnecessary copies; enables efficient resource transfer |

## Common Mistakes

| Mistake | Why It's Wrong | Correct Approach |
|---------|---------------|------------------|
| Using `shared_ptr` when `unique_ptr` suffices | Atomic ref-count overhead; prevents `std::vector` optimization; harder to reason about ownership | Use `unique_ptr` by default; escalate to `shared_ptr` only when shared ownership is truly needed |
| Writing `catch(...)` without rethrowing | Swallows all errors silently; hides bugs; makes debugging impossible | Log the error, rethrow, or use `std::current_exception()` — never silently swallow |
| Deep inheritance hierarchies (>3 levels) | Fragile base class problem; tight coupling; hard to test and refactor | Use composition; flatten hierarchies; prefer interfaces (pure virtual) over base classes |
| Raw `new`/`delete` outside RAII | Manual memory management is error-prone; leads to leaks, double-frees, use-after-free | Use `std::unique_ptr`, `std::make_unique`, `std::vector` instead |
| Missing `const` on member functions | Prevents calling the function on const objects; hides intent; prevents compiler optimizations | Mark every non-modifying member function `const` |
| Using `#include <bits/stdc++.h>` | Non-standard header; includes everything; dramatically increases compilation time | Include only what you use; forward-declare when possible |
| Returning `nullptr` for error state | Forces caller to null-check; easy to forget; leads to null dereferences | Use `std::optional<T>` or return error codes/exceptions |
| Copying `std::unique_ptr` | Compile-time error; prevents accidental ownership transfer | Use `std::move` to transfer; use raw ptr for non-owning observation |
| Not using `noexcept` on moves | Prevents `std::vector` reallocation from using moves; forces copies | Mark move constructors and move assignment operators `noexcept` |
| Catching exceptions by value | Slices derived exception types; prevents accessing derived-class information | Catch by reference: `catch (const std::exception& e)` |
| Ignoring compiler warnings | Warnings indicate potential bugs; `-Werror` promotes them to errors | Fix all warnings; enable `-Werror` in CI |
| Overusing `std::shared_ptr` | Atomic operations on ref count; prevents `std::vector` optimization; unclear ownership | Use `std::unique_ptr` for sole ownership; only `shared_ptr` when sharing is required |

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

### Incident 4: Shared Pointer Circular Reference Causing Memory Leak
**Problem**: A tree-structured cache held `std::shared_ptr` references between parent and child nodes. The cache reported 2GB memory usage after 1 hour of operation despite containing only 50,000 nodes (expected ~200MB).

**Cause**: Parent nodes held `shared_ptr` to children; children held `shared_ptr` back to parent. The atomic reference counts never reached zero because each node kept the other alive. The circular reference was invisible to valgrind's leak checker because the memory was technically "reachable" through the cycle.

**Impact**: Server OOM crash after 4 hours. Cache eviction logic was bypassed because the leaked nodes were never destroyed. Memory usage grew linearly with request count.

**Detection**: Heap dump analysis showed thousands of `TreeNode` objects with ref count > 0 but no external references. The cycle was identified by tracing `shared_ptr` ownership chains in the debugger.

**Solution**: Changed child-to-parent references from `std::shared_ptr` to `std::weak_ptr`. The parent still owns children via `shared_ptr`, but children observe the parent without extending its lifetime. When the parent is destroyed, the weak pointers expire automatically.

**Prevention**: Use `std::weak_ptr` for back-pointers and observer patterns. Run ASan leak detection in CI. Code review checklist must flag `shared_ptr` usage and verify no cycles exist.

### Incident 5: Missing noexcept on Move Constructor Preventing Vector Optimization
**Problem**: A `std::vector<std::string>` reallocation was copying all elements instead of moving them, causing a 10x performance regression during vector growth.

**Cause**: The custom `Buffer` class had a move constructor that threw an exception (it called `malloc` which can throw `std::bad_alloc`). Without `noexcept`, `std::vector` fell back to copying because the move constructor wasn't guaranteed to be non-throwing. The performance regression was invisible in small-scale testing but catastrophic in production with 100K+ element vectors.

**Impact**: Vector reallocation took 15 seconds instead of 1.5 seconds. JSON parsing pipeline timed out under load. 30% of API requests failed with timeout errors.

**Detection**: Profiling showed `std::copy` being called during vector reallocation instead of `std::move`. Adding `noexcept` to the move constructor resolved the performance issue.

**Solution**: Added `noexcept` to the move constructor and move assignment operator. For operations that could throw, used a swap-based move pattern that guarantees `noexcept`. Verified with `std::is_nothrow_move_constructible<Buffer>::value`.

**Prevention**: Mark all move constructors and move assignment operators `noexcept`. Use `static_assert(std::is_nothrow_move_constructible<T>::value)` in critical classes. Code review checklist must verify `noexcept` on all move operations.

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
6. **What are the three exception safety guarantees?**: Basic guarantee — no leaks, invariants preserved, but object state may change. Strong guarantee — commit-or-rollback semantics, state unchanged if exception thrown. `noexcept` guarantee — operation never throws. Design functions for the strongest guarantee you can achieve.
7. **When should you use `std::shared_ptr` vs `std::unique_ptr`?**: Use `unique_ptr` by default — zero overhead, sole ownership, clear semantics. Use `shared_ptr` only when multiple owners must extend object lifetime simultaneously (e.g., caches, observer lists). Never use `shared_ptr` when `unique_ptr` suffices — the atomic ref-count overhead prevents `std::vector` optimization.
8. **What is the fragile base class problem?**: When a derived class depends on the implementation details of its base class, changes to the base class can silently break derived classes. Adding a new virtual function, changing data members, or modifying non-virtual methods can cause subtle, hard-to-detect bugs. Composition avoids this by depending only on the public interface.
9. **How does `noexcept` affect move semantics?**: `std::vector` reallocation checks if the move constructor is `noexcept`. If it is, the vector uses moves during reallocation (O(n) move operations). If not, it falls back to copies (O(n) copy operations). Mark all move constructors and move assignment operators `noexcept` to enable this optimization.
10. **What is copy-and-swap and when should you use it?**: Copy-and-swap implements the copy assignment operator by creating a local copy (via copy constructor), then swapping with `std::swap`. This provides strong exception safety automatically. Use it when a class manages a resource and you need a safe, concise copy assignment implementation.
11. **Why should you catch exceptions by reference?**: Catching by value slices derived exception types, losing derived-class information. Catching by non-const reference allows catching by value semantics. Catch by `const&` to preserve the full exception hierarchy and avoid slicing: `catch (const std::exception& e)`.
12. **What are the performance implications of `std::make_shared` vs `new`?**: `std::make_shared` allocates the object and control block in a single memory allocation. Using `new` separately allocates the object, then `shared_ptr` constructor allocates the control block — two allocations, worse cache locality. `make_shared` is almost always preferred for `shared_ptr` creation.
13. **When should you use `std::optional` over return codes or exceptions?**: Use `std::optional<T>` when a function may legitimately not return a value (lookup that finds nothing, parse that yields no result). It's type-safe, self-documenting, and avoids sentinel values (`-1`, `nullptr`). Use error codes for recoverable failures with context; exceptions for truly exceptional errors.
14. **What is the Rule of Three and how does it relate to the Rule of Five?**: Rule of Three — if you declare any of destructor, copy constructor, or copy assignment operator, you should declare all three. Rule of Five extends this to move constructor and move assignment operator (C++11). If a class manages resources, all five must be explicitly defined or `=delete`d.
15. **How do you prevent resource leaks in exception-throwing code?**: RAII — wrap all resources (memory, files, sockets, locks) in RAII objects whose destructors clean up. Stack unwinding calls destructors automatically when exceptions propagate. Never use raw `new`/`delete` across exception-throwing boundaries. Use `std::unique_ptr`, `std::lock_guard`, `std::fstream` to ensure cleanup.

## References

- [Effective C++ — Scott Meyers](https://www.amazon.com/Effective-Specific-Ways-Improve-Programs/dp/0321334876)
- [C++ Core Guidelines](https://isocpp.github.io/CppCoreGuidelines/CppCoreGuidelines)
- [CppCoreGuidelines: Best Practices](https://isocpp.github.io/CppCoreGuidelines/CppCoreGuidelines#S-iostream)
- [Google C++ Style Guide](https://google.github.io/styleguide/cppguide.html)
