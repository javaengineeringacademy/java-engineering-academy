# C++ Fundamentals — C++

## Why It Matters

Every complex C++ system — from a game engine to a database to a trading platform — is built from the same primitives: variables, control structures, functions, and pointers. When these foundations are weak, the entire system crumbles, and 90% of production bugs trace back to uninitialized variables, dangling pointers, or incorrect function signatures.

## What It Is

Fundamentals are the irreducible building blocks of every C++ program, including variables, data types, control structures, functions, pointers, references, arrays, strings, and basic I/O.

## Architecture: How Fundamentals Fit Together

```
┌─────────────────────────────────────────────────────────┐
│                    C++ Fundamentals                      │
├──────────────┬──────────────┬──────────────┬────────────┤
│  Variables   │   Control    │  Functions   │   I/O      │
│  & Types     │   Flow       │              │            │
├──────────────┼──────────────┼──────────────┼────────────┤
│  Pointers    │  References  │   Arrays     │  Strings   │
│  & Memory    │  & Aliases   │  & Strides   │  & Views   │
└──────────────┴──────────────┴──────────────┴────────────┘
```

## Variables and Data Types

### The Problem Variables Solve
Computers process data, but programs need to name and organize that data. Variables provide named, typed storage that the compiler can track and optimize.

### Fundamental Types

```cpp
// Integer types — size depends on platform
short s = 32767;              // At least 16 bits
int i = 2147483647;           // At least 16 bits, typically 32
long l = 2147483647L;         // At least 32 bits
long long ll = 9223372036854775807LL;  // At least 64 bits

// Fixed-width types (C++11) — use these when size matters
#include <cstdint>
int32_t precise = 1000000;    // Exactly 32 bits
uint64_t big = 18446744073709551615ULL;  // Exactly 64 bits unsigned

// Floating point
float f = 3.14f;              // 32-bit, ~7 decimal digits precision
double d = 3.14159265358979;  // 64-bit, ~15 decimal digits precision
long double ld = 3.14L;       // 80-bit on x86 (typically)

// Character types
char c = 'A';                 // 1 byte (may be signed or unsigned)
wchar_t wc = L'Ω';           // Wide character (2 or 4 bytes)
char8_t u8c = u8'A';         // UTF-8 (C++20)
char16_t u16c = u'Ω';        // UTF-16 (C++11)
char32_t u32c = U'Ω';        // UTF-32 (C++11)

// Boolean
bool flag = true;             // 1 byte, true or false

// Void — no value
void func();                  // Function returns nothing
```

### Type Sizes and Portability

```cpp
#include <cstdint>
#include <iostream>

int main() {
    std::cout << "int: " << sizeof(int) << " bytes\n";
    std::cout << "long: " << sizeof(long) << " bytes\n";
    std::cout << "pointer: " << sizeof(void*) << " bytes\n";
    // Platform-dependent! Always use fixed-width types when size matters
}
```

### const and constexpr

```cpp
const int MAX_SIZE = 100;           // Runtime constant
constexpr int BUFFER_SIZE = 1024;  // Compile-time constant

// constexpr enables compile-time computation
constexpr int factorial(int n) {
    return (n <= 1) ? 1 : n * factorial(n - 1);
}

static_assert(factorial(5) == 120);  // Verified at compile time
```

### Scoped Enums (C++11)

```cpp
// Old-style enum (pollutes namespace)
enum Color { RED, GREEN, BLUE };

// Scoped enum (type-safe)
enum class Direction : uint8_t {
    North = 0,
    South = 1,
    East = 2,
    West = 3
};

Direction dir = Direction::North;
// int x = dir;  // ERROR: no implicit conversion
int x = static_cast<int>(dir);  // OK: explicit conversion
```

## Control Structures

### The Problem Control Flow Solves
Programs need to make decisions and repeat actions. Control structures provide the grammar for expressing conditional logic and iteration.

### if-else and switch

```cpp
// if-else with initialization (C++17)
if (auto result = compute(); result.has_value()) {
    process(result.value());
} else {
    handle_error(result.error());
}

// switch — use for discrete values
enum class LogLevel { Debug, Info, Warning, Error };

void log(LogLevel level, const std::string& msg) {
    switch (level) {
        case LogLevel::Debug:   std::cout << "[DEBUG] " << msg; break;
        case LogLevel::Info:    std::cout << "[INFO] " << msg; break;
        case LogLevel::Warning: std::cout << "[WARN] " << msg; break;
        case LogLevel::Error:   std::cout << "[ERROR] " << msg; break;
    }
}
```

### Loops

```cpp
// for loop — when count is known
for (int i = 0; i < 100; ++i) {
    process(i);
}

// while loop — when condition is primary
while (has_more_data()) {
    process_next();
}

// do-while — at least one iteration
do {
    input = read_user_input();
} while (input != "quit");

// Range-based for (C++11) — for containers
std::vector<int> vec = {1, 2, 3, 4, 5};
for (const auto& elem : vec) {  // const ref: no copy, no modification
    std::cout << elem << " ";
}

// Structured bindings (C++17)
std::map<std::string, int> ages = {{"Alice", 30}, {"Bob", 25}};
for (const auto& [name, age] : ages) {
    std::cout << name << " is " << age << "\n";
}
```

### Early Return and Guard Clauses

```cpp
// Bad: nested conditions
void process(const Data& data) {
    if (data.is_valid()) {
        if (data.has_permission()) {
            if (data.size() > 0) {
                // actual work
            }
        }
    }
}

// Good: guard clauses
void process(const Data& data) {
    if (!data.is_valid()) return;
    if (!data.has_permission()) return;
    if (data.size() == 0) return;
    
    // actual work — flat, readable
}
```

## Functions

### The Problem Functions Solve
Functions encapsulate reusable logic, enable abstraction, and make code testable. Without functions, programs are linear scripts impossible to maintain.

### Function Declaration and Definition

```cpp
// Declaration (in header) — tells compiler about the function
int add(int a, int b);

// Definition (in source) — provides the implementation
int add(int a, int b) {
    return a + b;
}

// Inline definition (in header) — ODR-safe
inline int square(int x) {
    return x * x;
}
```

### Pass by Value vs Reference vs Pointer

```cpp
// Pass by value — copies the argument
void process_by_value(int x) {
    x = 100;  // Modifies local copy only
}

// Pass by reference — aliases the argument
void process_by_ref(int& x) {
    x = 100;  // Modifies original
}

// Pass by const reference — read-only alias (preferred for large objects)
void process_by_const_ref(const std::string& str) {
    std::cout << str;  // Can read, cannot modify
}

// Pass by pointer — nullable reference
void process_by_ptr(int* ptr) {
    if (ptr) {
        *ptr = 100;  // Modifies original if not null
    }
}
```

### Function Overloading

```cpp
// Same name, different signatures
int add(int a, int b) { return a + b; }
double add(double a, double b) { return a + b; }
std::string add(const std::string& a, const std::string& b) { return a + b; }

// Compiler resolves which overload to call based on argument types
add(1, 2);          // Calls int version
add(1.5, 2.5);      // Calls double version
add("hello", " world");  // Calls string version
```

### Default Arguments

```cpp
void log(const std::string& msg, 
         LogLevel level = LogLevel::Info,
         bool newline = true) {
    // ...
}

log("Started");                    // Uses defaults
log("Error!", LogLevel::Error);    // Overrides level
```

### constexpr Functions

```cpp
// Can be evaluated at compile time or runtime
constexpr int fibonacci(int n) {
    if (n <= 1) return n;
    return fibonacci(n - 1) + fibonacci(n - 2);
}

// Compile-time evaluation
constexpr int fib10 = fibonacci(10);  // 55, computed at compile time

// Runtime evaluation
int x;
std::cin >> x;
int fib_x = fibonacci(x);  // Computed at runtime
```

## Pointers

### The Problem Pointers Solve
Pointers enable dynamic memory allocation, data structures (linked lists, trees), function pointers, and interfacing with C APIs. They are C++'s most powerful and dangerous feature.

### Pointer Basics

```cpp
int x = 42;
int* ptr = &x;      // ptr holds address of x
int value = *ptr;    // Dereference: get value at address

*ptr = 100;          // Modify x through pointer
// x is now 100
```

### Pointer Arithmetic

```cpp
int arr[] = {10, 20, 30, 40, 50};
int* p = arr;        // Points to arr[0]

p++;                 // Points to arr[1]
p += 2;              // Points to arr[3]
int diff = p - arr;  // 3 (pointer difference)
```

### Null Pointers

```cpp
int* p1 = nullptr;    // C++11: preferred way to represent "no pointer"
int* p2 = NULL;       // C: macro, type-unsafe
int* p3 = 0;          // Also null, but less clear

if (p1 == nullptr) {
    // Handle null case
}
```

### Dynamic Memory

```cpp
// Allocation
int* p = new int(42);           // Single object
int* arr = new int[100];        // Array of 100 ints

// Deallocation
delete p;                       // Free single object
delete[] arr;                   // Free array — MUST use delete[]

// Overloading (rare)
void* operator new(size_t size);
void operator delete(void* ptr) noexcept;
```

### Function Pointers

```cpp
// Declaration
int (*func_ptr)(int, int);

// Assignment
func_ptr = add;  // Points to add function

// Call
int result = func_ptr(3, 4);  // Calls add(3, 4)

// Using function pointers with algorithms
std::sort(arr, arr + n, greater<int>);  // Sort in descending order
```

## References

### The Problem References Solve
References provide safer, more ergonomic aliases for variables. They eliminate the null-pointer problem and enable pass-by-reference without pointer syntax.

### Reference Basics

```cpp
int x = 42;
int& ref = x;   // ref is an alias for x
ref = 100;      // x is now 100

// Must be initialized at declaration
// int& ref2;   // ERROR: reference must be initialized
```

### Lvalue vs Rvalue References

```cpp
// Lvalue reference — binds to named objects
int& lref = x;

// Rvalue reference — binds to temporary objects (C++11)
int&& rref = 42;              // Binds to temporary
std::string&& rstr = std::string("hello");  // Move semantics

// Const lvalue reference — binds to anything
const int& cref = 42;         // OK: binds to temporary
const int& cref2 = x;         // OK: binds to lvalue
```

### Reference vs Pointer

```cpp
// Reference: cannot be null, cannot be reseated
void process(int& ref) {
    ref = 100;  // Always valid
}

// Pointer: can be null, can be reseated
void process(int* ptr) {
    if (ptr) {
        *ptr = 100;  // Must check for null
    }
}
```

## Arrays and Strings

### C-style Arrays

```cpp
int arr[5] = {1, 2, 3, 4, 5};
int arr2[] = {1, 2, 3};  // Size deduced as 3

// Arrays decay to pointers
int* p = arr;  // p points to arr[0]
```

### std::array (C++11)

```cpp
#include <array>

std::array<int, 5> arr = {1, 2, 3, 4, 5};
arr.size();    // 5 — knows its size
arr.at(2);     // Bounds-checked access
arr[2];        // Unchecked access
```

### std::string

```cpp
#include <string>

std::string s1 = "Hello";
std::string s2 = s1 + " World";    // Concatenation
s2.size();                          // 11
s2.substr(0, 5);                   // "Hello"
s2.find("World");                  // 6

// String view (C++17) — non-owning, read-only
#include <string_view>
std::string_view sv = s2;          // No copy, just a view
```

## Production Incidents

### Incident 1: Uninitialized Variable Causing Crash
**Problem**: A radar processing system produced intermittent NaN values that propagated through calculations, causing a missile guidance system to output invalid coordinates.

**Cause**: A `double` variable used in a critical calculation was not initialized. On most runs, the stack happened to contain zero at that location. On one specific execution path, it contained garbage data that became NaN after a division.

**Impact**: 3 occurrences over 6 months. Each required full system diagnostic. The third occurrence nearly caused a failed test launch, costing $4M in delayed testing.

**Detection**: Valgrind's memcheck tool identified the uninitialized read in a simulation. The bug only manifested under specific memory layouts.

**Solution**: Compile with `-Wuninitialized` and `-Werror`. Initialize all variables at declaration: `double result = 0.0;`. Use `std::optional` for variables that might not have a value.

### Incident 2: Implicit Conversion Overflow
**Problem**: A payment processing system calculated refunds incorrectly for amounts over $65,535, issuing refunds 65,536x too large.

**Cause**: A `uint16_t` was used to store payment amounts in cents. When a refund of $70,000.00 (7,000,000 cents) was processed, it overflowed the 16-bit integer, wrapping to 44,192 cents ($441.92). The code then multiplied by 100 again, producing $44,192.00.

**Impact**: $180,000 in incorrect refunds over 2 weeks before detection. Required manual reconciliation with payment processor.

**Detection**: An accountant noticed refund amounts didn't match original charges. Code review revealed the `uint16_t` usage.

**Solution**: Use `int64_t` for all monetary calculations. Add runtime overflow checks: `if (amount > INT16_MAX) throw overflow_error(...)`. Use `static_assert(sizeof(int64_t) >= 8)` to ensure sufficient range.

## Production Checklist
- [ ] Initialize all variables at declaration
- [ ] Use fixed-width types (`int32_t`, `uint64_t`) when size matters
- [ ] Prefer `const` and `constexpr` for constants
- [ ] Use `nullptr` instead of `NULL` or `0`
- [ ] Prefer references over pointers when null is not needed
- [ ] Use `std::array` instead of C-style arrays
- [ ] Use `std::string` instead of `char*`
- [ ] Enable compiler warnings (`-Wall -Wextra -Wpedantic`)
- [ ] Compile with `-Werror` in CI
- [ ] Run sanitizers (`-fsanitize=address,undefined`)

## Maturity Levels

### Beginner
- Declare variables with appropriate types
- Write if-else and switch statements
- Use for, while, and do-while loops
- Call and define simple functions
- Understand basic pointer syntax

### Intermediate
- Use pass-by-reference and pass-by-const-reference
- Write overloaded functions
- Use `constexpr` for compile-time computation
- Understand pointer arithmetic
- Use `std::array` and `std::string`

### Advanced
- Write template functions with type constraints
- Use function pointers and `std::function`
- Understand ABI and calling conventions
- Optimize for cache alignment
- Write exception-safe code

## Common Myths Debunked

### Myth 1: "C++ is too complex for beginners"
**Reality**: You can write useful C++ programs with just variables, loops, and functions. Complexity is additive — you learn advanced features as needed.

### Myth 2: "Pointers are always dangerous"
**Reality**: Pointers are dangerous when misused. With RAII and smart pointers, raw pointer usage is rare in modern C++.

### Myth 3: "You need to manually manage all memory"
**Reality**: Stack allocation, `std::vector`, `std::string`, and smart pointers handle most memory management automatically.

### Myth 4: "C-style arrays are faster than std::array"
**Reality**: They compile to identical machine code. `std::array` adds zero overhead while providing `.size()`, bounds checking, and STL compatibility.

## One-Minute Revision

| Concept | What It Is | When to Use | Watch Out For |
|---------|-----------|-------------|---------------|
| Variables | Named typed storage | Everywhere | Uninitialized values |
| const/constexpr | Immutable values | Constants, API contracts | `const` doesn't mean compile-time |
| if-else/switch | Conditional execution | Discrete decisions | Missing `break` in switch |
| Functions | Reusable logic blocks | Always | Default args order |
| Pointers | Memory addresses | Dynamic allocation, C interop | Null dereference, dangling |
| References | Aliases | Pass-by-reference, APIs | Must initialize, cannot reseat |
| std::array | Fixed-size array | Known-size collections | Size is part of type |
| std::string | Dynamic string | Text processing | Allocation overhead |
| Range-based for | Container iteration | When index not needed | Copy vs reference |

## Related Topics
- [Knowledge Atoms](../00-knowledge-atoms/) — The foundation beneath fundamentals
- [OOP](../02-oop/) — Organizing fundamentals into classes
- [Memory Management](../05-memory-management/) — Deep dive into stack vs heap
- [Modern C++](../08-modern-cpp/) — Modern alternatives to C-style fundamentals
- [Best Practices](../14-best-practices/) — Guidelines for clean fundamental code

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Uninitialized variable causing intermittent NaN/crash | Valgrind memcheck + `-Wuninitialized` | Compile with `-Wuninitialized -Werror`; run `valgrind --tool=memcheck ./program` to find uninitialized reads |
| Implicit conversion overflow (e.g., `uint16_t` for large values) | Compiler warnings + static assertions | Enable `-Wconversion`; use `static_assert(sizeof(T) >= required_bytes)` to catch overflow-prone types |
| Dangling pointer from returning address of local variable | AddressSanitizer (`-fsanitize=address`) | ASan catches stack-use-after-return with precise allocation/deallocation traces |
| Array out-of-bounds access | `-fsanitize=bounds` or `std::array::at()` | Use bounds-checked `.at()` during development; enable UBSan in CI |
| Missing `break` in switch statement causing fallthrough | Compiler warning `-Wimplicit-fallthrough` | Enable the warning; use `[[fallthrough]]` attribute explicitly when intentional |

## Code Review Checklist

- [ ] All variables initialized at declaration (no uninitialized reads)
- [ ] Fixed-width types (`int32_t`, `uint64_t`) used when size matters
- [ ] `nullptr` used instead of `NULL` or `0`
- [ ] References preferred over pointers where null is not needed
- [ ] `std::array` and `std::string` used instead of C-style equivalents
- [ ] `const`/`constexpr` applied to all constants and read-only parameters
- [ ] Range-based for loops used where index is not needed

## Architecture Considerations

Fundamentals are the atomic units of every C++ system. Variables and types define data contracts between components. Control structures determine execution flow and error-handling paths. Functions encapsulate reusable logic and define API boundaries. Pointers and references govern memory relationships between components. Getting these right prevents entire categories of production bugs that are expensive to debug in complex systems.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| RAII for local resources | Automatic cleanup of files, locks, memory | Exception-safe but requires understanding move semantics |
| `std::string_view` for read-only parameters | Avoiding unnecessary string copies | Non-owning — caller must ensure underlying string outlives the view |
| Guard clauses over nested `if` | Improving readability of validation logic | Flatter code but may obscure business-rule grouping |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Buffer overflow from C-style arrays | Remote code execution, stack corruption | Use `std::array`, `.at()`, and `std::string` instead of raw arrays and `char*` |
| Integer overflow in payment/financial calculations | Incorrect amounts, financial loss | Use `int64_t` for monetary values; add runtime overflow checks with `if` guards |
| Use-after-free from dangling pointers | Exploitable memory corruption, crashes | Use `std::unique_ptr` and ensure pointer lifetime exceeds usage scope |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| C++11 | `nullptr`, `auto`, range-based for, `std::array` | Replace `NULL` with `nullptr`; replace C arrays with `std::array` |
| C++17 | `std::string_view`, structured bindings, `if` with initializer | Use `string_view` for read-only string params; use structured bindings for map iteration |
| C++20 | `char8_t` for UTF-8 | Replace `char` for UTF-8 data with `char8_t` for type safety |

## Version Validation

| Feature | C++ Version | Status |
|---------|------------|--------|
| `nullptr` | C++11 | Widely supported |
| `auto` type inference | C++11 | Widely supported |
| `std::string_view` | C++17 | Widely supported |
| `char8_t` | C++20 | Supported in GCC 10+, Clang 10+, MSVC 19.24+ |

## Interview Questions

1. **What is the difference between pass-by-value and pass-by-reference?**: Pass-by-value copies the argument (modifications don't affect the original). Pass-by-reference creates an alias — modifications affect the original. Pass by `const&` for read-only large objects; pass by value for small, cheap-to-copy types.
2. **When should you use `nullptr` instead of `NULL`?**: Always. `nullptr` is type-safe (`std::nullptr_t`) and doesn't ambiguity with integer overloads. `NULL` is a macro that may expand to `0`, causing incorrect overload resolution.
3. **Explain the Rule of Zero, Three, and Five**: Rule of Zero — if your class manages no resources, don't declare any special member functions. Rule of Three — if you define one of destructor/copy-ctor/copy-assign, define all three. Rule of Five — add move-ctor and move-assignment to Rule of Three for efficient resource transfer.
4. **Why prefer `std::array` over C-style arrays?**: `std::array` knows its size (`.size()`), is compatible with STL algorithms, supports bounds-checked access via `.at()`, and has zero overhead — it compiles to identical machine code as C arrays.
5. **What is `constexpr` and when should you use it?**: `constexpr` marks values and functions that can be evaluated at compile time. Use it for constants, lookup tables, and functions whose inputs are known at compile time — it eliminates runtime cost entirely.

## References

- [C++ Core Guidelines — Declarations](https://isocpp.github.io/CppCoreGuidelines/CppCoreGuidelines#S-name)
- [CppReference — Fundamental Types](https://en.cppreference.com/w/cpp/language/types)
- [Compiler Explorer — Inspect generated code](https://godbolt.org/)
- [Valgrind Quick Start](https://valgrind.org/docs/manual/quick-start.html)
