# C++ Fundamentals — C++

## Overview

C++ Fundamentals covers the essential building blocks of every C++ program: variables, data types, control structures, functions, pointers, references, arrays, strings, and basic I/O. These concepts form the foundation for all advanced C++ topics.

### Why It Matters

Every complex C++ system — from a game engine to a database to a trading platform — is built from the same primitives: variables, control structures, functions, and pointers. When these foundations are weak, the entire system crumbles, and 90% of production bugs trace back to uninitialized variables, dangling pointers, or incorrect function signatures.

### What It Is

Fundamentals are the irreducible building blocks of every C++ program, including variables, data types, control structures, functions, pointers, references, arrays, strings, and basic I/O.

## Learning Objectives

By the end of this module, you will be able to:

- Declare and use all fundamental C++ data types (int, float, double, char, bool)
- Implement control structures (if/else, switch, loops)
- Write functions with proper parameter passing (value, reference, pointer)
- Use pointers and references correctly
- Handle arrays and C-style strings
- Perform basic I/O using iostream
- Apply const and constexpr for compile-time safety
- Write portable code using fixed-width integer types

## Prerequisites

- Basic understanding of programming concepts (variables, loops, functions)
- Familiarity with any programming language (C, Java, Python)
- Command-line compiler setup (GCC, Clang, or MSVC)

## History

| Version | Year | Key Features Added |
|---------|------|-------------------|
| C++98 | 1998 | First ISO standard, STL, exceptions, namespaces |
| C++03 | 2003 | Bug fixes, value initialization |
| C++11 | 2011 | auto, range-for, lambdas, smart pointers, move semantics |
| C++14 | 2014 | Generic lambdas, relaxed constexpr |
| C++17 | 2017 | std::optional, std::variant, structured bindings, if-init |
| C++20 | 2020 | Concepts, ranges, coroutines, modules |
| C++23 | 2023 | std::expected, std::print, deducing this |

## Production Notes

### Initialization Pitfalls
```cpp
int x;           // Uninitialized — undefined behavior if read
int y{};         // Value-initialized to 0 — safe
int z = {};      // Same as above
int w{42};       // Direct initialization
```

### Portability Concerns
```cpp
// DON'T: Assumes 4-byte int and 8-byte pointer
struct Bad {
    int x;       // 4 bytes (usually)
    void* ptr;   // 4 or 8 bytes depending on platform
};

// DO: Use fixed-width types when size matters
#include <cstdint>
struct Good {
    int32_t x;   // Exactly 4 bytes
    uintptr_t ptr;  // Large enough to hold a pointer
};
```

## Internal Working: Compilation and Linking

C++ code goes through a multi-stage pipeline before execution. Understanding this pipeline is critical for debugging linking errors, understanding header inclusion, and optimizing build times.

### The Four Stages of Compilation

```
Source Code (.cpp)  →  Preprocessor  →  Compiler  →  Assembler  →  Linker  →  Executable
```

| Stage | Tool | Input | Output | What Happens |
|-------|------|-------|--------|-------------|
| Preprocessing | `cpp` / `-E` | `.cpp` + headers | Expanded source | `#include` expanded, macros substituted, `#ifdef` resolved |
| Compilation | `cc1plus` / `-S` | Expanded source | Assembly (`.s`) | Syntax checking, optimization, code generation |
| Assembly | `as` / `-c` | Assembly | Object file (`.o`/`.obj`) | Machine code + metadata (symbol table, relocations) |
| Linking | `ld` / `ld++` | Object files + libraries | Executable (`.exe`/`a.out`) | Resolves symbols, combines sections, produces final binary |

### Compilation in Practice

```bash
# Preprocessing only (see expanded source)
g++ -E main.cpp -o main.i

# Compilation to assembly
g++ -S main.cpp -o main.s

# Compilation to object file
g++ -c main.cpp -o main.o

# Full pipeline: compile and link
g++ main.cpp utils.cpp -o program

# Verbose: show all stages
g++ -v main.cpp -o program
```

### Translation Units and ODR

Each `.cpp` file is a **translation unit** — the compiler sees it independently with all its included headers. The **One Definition Rule (ODR)** states that every function, variable, class, and template must be defined exactly once across the entire program (with exceptions for `inline` and templates).

```cpp
// header.h
#pragma once               // Include guard — prevents multiple inclusion
inline int square(int x) { // inline allows multiple definitions (must be identical)
    return x * x;
}

// main.cpp
#include "header.h"        // textually pasted here by preprocessor
#include "header.h"        // #pragma once prevents duplicate

// utils.cpp
#include "header.h"        // same text pasted here too
```

### Linkage: Internal vs External

```cpp
// External linkage (default) — visible across translation units
int global_counter = 0;            // Other .cpp files can access via extern
void process() { /* ... */ }       // Other .cpp files can call this

// Internal linkage — visible only in this translation unit
static int file_local = 0;         // Only this .cpp sees this
constexpr int MAX = 100;           // const objects have internal linkage by default
namespace { int hidden = 42; }     // Anonymous namespace = internal linkage

// Explicit external linkage
extern int global_counter;         // Declaration (definition in another TU)
extern "C" void c_function();      // Use C linkage (no name mangling)
```

### Static vs Dynamic Linking

```
Static Linking (.a / .lib)              Dynamic Linking (.so / .dll / .dylib)
┌──────────┐  ┌──────────┐             ┌──────────┐
│ main.o   │  │ lib.a    │             │ main.o   │
└────┬─────┘  └────┬─────┘             └────┬─────┘
     │    static    │                       │   dynamic
     └──────┬───────┘                       └──────┬───────┐
            ▼                                      ▼       │
      [Executable]                            [Executable] │
      (all code embedded)                    (references   │
                                             shared lib)  ▼
                                                        [.so/.dll]
                                                        (loaded at runtime)
```

### Header File Organization

```cpp
// myclass.h — Header Guard pattern (portable)
#ifndef MYCLASS_H
#define MYCLASS_H

class MyClass {
public:
    void process();
private:
    int value_;
};

#endif // MYCLASS_H

// myclass.h — #pragma once pattern (non-standard but widely supported)
#pragma once

class MyClass {
public:
    void process();
private:
    int value_;
};
```

### Common Linking Errors

| Error | Cause | Fix |
|-------|-------|-----|
| `undefined reference to 'function'` | Function declared but not defined | Ensure definition exists in some `.cpp` file |
| `multiple definition of 'symbol'` | Same symbol defined in multiple TUs | Use `inline`, `static`, or anonymous namespace |
| `undefined reference to 'vtable for Class'` | Missing virtual function definitions | Define all virtual functions (including destructor) |
| `cannot find -lxyz` | Library not found | Install library or add `-L/path` and `-lxyz` flags |
| `ld: library not found for -lc++` | Missing C++ standard library | Install Xcode Command Line Tools or libc++ |

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

## Core Concepts

### Variables and Data Types

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

## Syntax

C++ syntax follows a statement-based grammar. Every statement ends with a semicolon. Declarations introduce names; definitions provide implementations.

### Declaration vs Definition

```cpp
extern int count;              // Declaration only (no memory allocated)
int count = 0;                 // Definition (allocates memory)
extern const int SIZE = 100;   // Definition with external linkage
```

### Statement Forms

```cpp
// Expression statement
x = x + 1;

// Declaration statement
int y = compute(x);

// Compound statement (block)
{
    int temp = x;
    x = y;
    y = temp;
}

// Null statement
;  // sometimes used as loop body placeholder
```

### Initialization Syntaxes

```cpp
int a = 10;       // Copy initialization
int b(20);        // Direct initialization
int c{30};        // Direct list initialization (C++11) — prevents narrowing
int d = {40};     // Copy list initialization
auto e = 50;      // Type deduction
```

### Scope Resolution and Member Access

```cpp
int global_var = 10;                    // Global scope
namespace Foo { int bar = 20; }         // Namespace scope
struct S { int member; };               // Class scope
S s;
s.member = 30;                          // Member access (object)
S* p = &s;
p->member = 40;                         // Member access (pointer)
int ns_val = Foo::bar;                  // Namespace member access
```

### Preprocessor Directives

```cpp
#include <iostream>              // System header
#include "myheader.h"            // Project header
#define PI 3.14159               // Macro constant
#define SQUARE(x) ((x)*(x))     // Macro function (prefer inline/constexpr)
#ifdef DEBUG                     // Conditional compilation
    #define LOG(msg) std::cout << msg
#else
    #define LOG(msg)
#endif
#pragma once                    // Include guard (non-standard)
```

## Examples

### Easy: Hello World and Basic Types

```cpp
#include <iostream>
#include <string>

int main() {
    // Basic types
    int age = 25;
    double pi = 3.14159;
    char grade = 'A';
    bool passed = true;
    std::string name = "Alice";

    // Output
    std::cout << name << " is " << age << " years old" << std::endl;
    std::cout << "Grade: " << grade << ", Passed: " << std::boolalpha << passed << std::endl;

    // Input
    std::cout << "Enter your name: ";
    std::getline(std::cin, name);
    std::cout << "Hello, " << name << "!" << std::endl;

    return 0;
}
```

### Medium: Function Overloading and References

```cpp
#include <iostream>
#include <string>
#include <vector>
#include <algorithm>

// Overloaded functions
int max_val(int a, int b) { return (a > b) ? a : b; }
double max_val(double a, double b) { return (a > b) ? a : b; }
const std::string& max_val(const std::string& a, const std::string& b) {
    return (a > b) ? a : b;
}

// Pass by reference — swap without pointers
void swap_values(int& a, int& b) {
    int temp = a;
    a = b;
    b = temp;
}

// Pass by const reference — efficient read-only access
void print_vector(const std::vector<int>& vec) {
    for (const auto& val : vec) {
        std::cout << val << " ";
    }
    std::cout << "\n";
}

int main() {
    int x = 5, y = 10;
    swap_values(x, y);
    std::cout << "After swap: x=" << x << ", y=" << y << "\n";

    std::vector<int> nums = {3, 1, 4, 1, 5, 9, 2, 6};
    std::sort(nums.begin(), nums.end());
    print_vector(nums);

    return 0;
}
```

### Hard: Dynamic Memory and Pointer Arithmetic

```cpp
#include <iostream>
#include <memory>
#include <cstring>

// Dynamic array with manual memory management
class DynamicBuffer {
public:
    explicit DynamicBuffer(size_t size)
        : data_(new int[size]), size_(size), capacity_(size) {
        std::memset(data_, 0, size_ * sizeof(int));
    }

    ~DynamicBuffer() { delete[] data_; }

    // Rule of Five: copy/move semantics
    DynamicBuffer(const DynamicBuffer& other)
        : data_(new int[other.capacity_]), size_(other.size_), capacity_(other.capacity_) {
        std::memcpy(data_, other.data_, size_ * sizeof(int));
    }

    DynamicBuffer& operator=(const DynamicBuffer& other) {
        if (this != &other) {
            delete[] data_;
            data_ = new int[other.capacity_];
            size_ = other.size_;
            capacity_ = other.capacity_;
            std::memcpy(data_, other.data_, size_ * sizeof(int));
        }
        return *this;
    }

    DynamicBuffer(DynamicBuffer&& other) noexcept
        : data_(other.data_), size_(other.size_), capacity_(other.capacity_) {
        other.data_ = nullptr;
        other.size_ = 0;
        other.capacity_ = 0;
    }

    DynamicBuffer& operator=(DynamicBuffer&& other) noexcept {
        if (this != &other) {
            delete[] data_;
            data_ = other.data_;
            size_ = other.size_;
            capacity_ = other.capacity_;
            other.data_ = nullptr;
            other.size_ = 0;
            other.capacity_ = 0;
        }
        return *this;
    }

    int& operator[](size_t idx) { return data_[idx]; }
    const int& operator[](size_t idx) const { return data_[idx]; }
    size_t size() const { return size_; }

private:
    int* data_;
    size_t size_;
    size_t capacity_;
};

// Pointer arithmetic — manually traverse and manipulate
void pointer_arithmetic_demo() {
    int arr[] = {10, 20, 30, 40, 50};
    int* p = arr;              // Points to arr[0]

    std::cout << *p << "\n";   // 10
    std::cout << *(p + 2) << "\n";  // 30

    p += 3;
    std::cout << *p << "\n";   // 40

    // Pointer difference
    int diff = p - arr;        // 3
    std::cout << "Offset: " << diff << "\n";
}

int main() {
    DynamicBuffer buf(5);
    for (size_t i = 0; i < buf.size(); ++i) {
        buf[i] = static_cast<int>(i * i);
    }

    for (size_t i = 0; i < buf.size(); ++i) {
        std::cout << buf[i] << " ";
    }
    std::cout << "\n";

    pointer_arithmetic_demo();

    return 0;
}
```

### Enterprise: RAII Resource Manager and Smart Pointers

```cpp
#include <iostream>
#include <memory>
#include <fstream>
#include <string>
#include <vector>
#include <mutex>
#include <stdexcept>

// RAII file handle
class FileHandle {
public:
    explicit FileHandle(const std::string& path)
        : file_(std::fopen(path.c_str(), "w")) {
        if (!file_) {
            throw std::runtime_error("Failed to open file: " + path);
        }
    }

    ~FileHandle() {
        if (file_) std::fclose(file_);
    }

    // Non-copyable, movable
    FileHandle(const FileHandle&) = delete;
    FileHandle& operator=(const FileHandle&) = delete;

    FileHandle(FileHandle&& other) noexcept : file_(other.file_) {
        other.file_ = nullptr;
    }

    FileHandle& operator=(FileHandle&& other) noexcept {
        if (this != &other) {
            if (file_) std::fclose(file_);
            file_ = other.file_;
            other.file_ = nullptr;
        }
        return *this;
    }

    void write(const std::string& data) {
        if (std::fwrite(data.c_str(), 1, data.size(), file_) != data.size()) {
            throw std::runtime_error("Write failed");
        }
    }

private:
    std::FILE* file_;
};

// Thread-safe singleton with lazy initialization
class Logger {
public:
    static Logger& instance() {
        static Logger inst;  // Thread-safe since C++11
        return inst;
    }

    void log(const std::string& message) {
        std::lock_guard<std::mutex> lock(mutex_);
        std::cout << "[LOG] " << message << "\n";
    }

private:
    Logger() = default;
    ~Logger() = default;
    Logger(const Logger&) = delete;
    Logger& operator=(const Logger&) = delete;

    std::mutex mutex_;
};

// Smart pointer usage patterns
class ResourcePool {
public:
    struct Resource {
        int id;
        std::string name;
        virtual ~Resource() = default;  // Critical: virtual destructor
    };

    struct DatabaseConnection : Resource {
        void connect() { std::cout << "Connected to DB #" << id << "\n"; }
        void disconnect() { std::cout << "Disconnected from DB #" << id << "\n"; }
    };

    struct NetworkSocket : Resource {
        void send(const std::string& msg) { std::cout << "Sending: " << msg << "\n"; }
    };

    // Factory method returning unique_ptr
    static std::unique_ptr<Resource> create_resource(const std::string& type, int id) {
        if (type == "db") {
            auto conn = std::make_unique<DatabaseConnection>();
            conn->id = id;
            conn->name = "DB-" + std::to_string(id);
            return conn;
        } else if (type == "net") {
            auto sock = std::make_unique<NetworkSocket>();
            sock->id = id;
            sock->name = "NET-" + std::to_string(id);
            return sock;
        }
        throw std::invalid_argument("Unknown resource type: " + type);
    }

    // Shared ownership for resources used by multiple components
    void register_resource(std::shared_ptr<Resource> res) {
        resources_.push_back(std::move(res));
    }

private:
    std::vector<std::shared_ptr<Resource>> resources_;
};

int main() {
    Logger::instance().log("Application started");

    // RAII file handling
    {
        FileHandle file("output.txt");
        file.write("Hello, RAII!\n");
        file.write("Automatic cleanup on scope exit.\n");
    }  // File closed here automatically

    // Smart pointer patterns
    ResourcePool pool;
    auto db = std::make_unique<ResourcePool::DatabaseConnection>();
    db->id = 1;
    db->connect();

    // Shared ownership
    auto shared_res = std::make_shared<ResourcePool::NetworkSocket>();
    shared_res->id = 2;
    shared_res->send("Hello, network!");
    pool.register_resource(shared_res);  // shared_res still valid

    return 0;
}
```

## Performance Considerations

### Stack vs Heap Allocation

```
Stack                              Heap
┌─────────────────┐               ┌─────────────────┐
│ Automatic        │               │ Manual/Smart     │
│ Fast allocation  │               │ Slow allocation  │
│ Limited size     │               │ Nearly unlimited │
│ LIFO order       │               │ Any order        │
│ Cache-friendly   │               │ Cache-unfriendly │
│ No fragmentation │               │ May fragment     │
└─────────────────┘               └─────────────────┘
```

```cpp
// Stack: fast, automatic cleanup
void stack_example() {
    int arr[100];                    // Stack — extremely fast
    std::array<int, 100> arr2;       // Stack — same speed, safer
    std::string s = "hello";         // Small string optimization (SSO) — often stack
}

// Heap: slower, manual lifetime
void heap_example() {
    int* arr = new int[1000000];     // Heap — allocation overhead
    delete[] arr;                    // Must manually deallocate
    // Prefer: auto arr = std::make_unique<int[]>(1000000);
}
```

### Move Semantics and Rvalue References

```cpp
std::string create_string() {
    std::string result(10000, 'x');
    return result;  // NRVO or move — no copy
}

// Move constructor — transfers ownership instead of copying
class Buffer {
public:
    Buffer(Buffer&& other) noexcept
        : data_(other.data_), size_(other.size_) {
        other.data_ = nullptr;
        other.size_ = 0;  // Source is now empty — no leak
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

private:
    int* data_ = nullptr;
    size_t size_ = 0;
};

// std::move doesn't move — it casts to rvalue reference
void transfer() {
    std::string a = "hello";
    std::string b = std::move(a);  // a's internal buffer transferred to b
    // a is now in valid but unspecified state — don't use except to destroy/assign
}
```

### Compiler Optimization Hints

```cpp
// inline — suggest inlining (compiler may ignore)
inline int fast_square(int x) { return x * x; }

// constexpr — evaluate at compile time (zero runtime cost)
constexpr int table_size = 256;

// Likely/unlikely (C++20)
if (condition) [[likely]] {
    // Hot path — optimizer arranges code for this
} else {
    // Cold path — placed out-of-line
}

// noexcept — enables move semantics optimization
void swap(int& a, int& b) noexcept {
    int temp = a;
    a = b;
    b = temp;
}
```

### Performance Anti-Patterns

| Anti-Pattern | Problem | Fix |
|-------------|---------|-----|
| Returning large objects by value | Unnecessary copies | Return `std::move` or use output parameters |
| Passing small objects by `const&` | Reference indirection overhead | Pass by value for types ≤ pointer size |
| `std::string` concatenation in loops | O(n²) reallocation | Use `std::ostringstream` or `reserve()` |
| Frequent `new`/`delete` calls | Allocation overhead | Use `std::vector` or memory pools |
| Virtual function in hot path | Indirect call, prevents inlining | Use CRTP or templates for static polymorphism |

## Best Practices

### Naming Conventions

| Element | Convention | Example |
|---------|-----------|---------|
| Variables | `snake_case` | `user_count`, `is_valid` |
| Functions | `snake_case` | `get_name()`, `process_data()` |
| Classes | `PascalCase` | `FileManager`, `ThreadPool` |
| Constants | `UPPER_SNAKE` or `kCamelCase` | `MAX_BUFFER_SIZE` or `kMaxBufferSize` |
| Namespaces | `snake_case` | `my_project::utils` |
| Templates | `PascalCase` | `SortedContainer<T>` |
| Member variables | `snake_case_` or `m_name` | `data_`, `m_data` |

### Modern C++ Idioms

```cpp
// 1. Use auto when type is obvious
auto vec = std::vector<int>{1, 2, 3};
auto ptr = std::make_unique<Widget>();

// 2. Use range-based for for containers
for (const auto& item : container) { /* ... */ }

// 3. Use structured bindings (C++17)
auto [key, value] = *map.begin();

// 4. Use constexpr for compile-time constants
constexpr int MAX = 100;

// 5. Use std::optional for nullable values
std::optional<int> find_value(const std::string& key);

// 6. Use RAII — never raw new/delete
auto resource = std::make_unique<Resource>();  // Not: Resource* r = new Resource();

// 7. Use std::string_view for read-only strings
void process(std::string_view sv);  // No allocation, works with any string

// 8. Prefer initialization over assignment
int x = 42;        // OK
int y{42};         // Better — prevents narrowing
```

### Code Organization

```
project/
├── include/           # Public headers (.h/.hpp)
│   └── mylib/
│       ├── widget.h
│       └── utils.h
├── src/               # Implementation files (.cpp)
│   ├── widget.cpp
│   └── utils.cpp
├── tests/             # Unit tests
│   ├── widget_test.cpp
│   └── utils_test.cpp
├── CMakeLists.txt     # Build configuration
└── README.md
```

## Common Mistakes

### Mistake 1: Uninitialized Variables

```cpp
// BAD — undefined behavior
int count;
if (user_input > 0) {
    count = user_input;
}
std::cout << count;  // May be garbage if user_input <= 0

// GOOD — always initialize
int count = 0;
if (user_input > 0) {
    count = user_input;
}
```

### Mistake 2: Dangling Pointers

```cpp
// BAD — pointer dangles after function returns
int* get_value() {
    int local = 42;
    return &local;  // Returns address of stack variable
}

// GOOD — return by value or use smart pointer
int get_value() {
    int local = 42;
    return local;  // Copy returned
}
```

### Mistake 3: Memory Leaks

```cpp
// BAD — forgot to delete
void leak() {
    int* data = new int[1000];
    if (some_condition) return;  // Leak: data never freed
    delete[] data;
}

// GOOD — RAII handles cleanup
void no_leak() {
    auto data = std::make_unique<int[]>(1000);
    if (some_condition) return;  // Data automatically freed
}
```

### Mistake 4: Using sizeof on Pointers

```cpp
int arr[] = {1, 2, 3, 4, 5};
int* ptr = arr;

std::cout << sizeof(arr);   // 20 (5 × 4 bytes) — knows array size
std::cout << sizeof(ptr);   // 8 (pointer size) — loses size information

// Solution: pass size explicitly, or use std::array/std::vector
void process(int* p, size_t size);  // Clear API
```

### Mistake 5: Forgetting Virtual Destructor

```cpp
// BAD — base class without virtual destructor
struct Base {
    ~Base() { /* cleanup */ }  // Non-virtual
};
struct Derived : Base {
    int* data;
    ~Derived() { delete[] data; }  // Never called if deleted via Base*
};

Base* p = new Derived();
delete p;  // Undefined behavior — Derived destructor not called

// GOOD — always virtual destructor in polymorphic base classes
struct Base {
    virtual ~Base() = default;
};
```

### Mistake 6: Comparing Strings with == Incorrectly

```cpp
// This works but is misleading with C-style strings
char a[] = "hello";
char b[] = "hello";
if (a == b) { /* ... */ }  // Compares pointers, NOT contents — always false

// Correct comparison for C-style strings
if (std::strcmp(a, b) == 0) { /* ... */ }

// Better: use std::string
std::string s1 = "hello";
std::string s2 = "hello";
if (s1 == s2) { /* ... */ }  // Compares contents — true
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

### Incident 3: Memory Leak in Long-Running Service
**Problem**: A web server running C++ on the backend consumed 8GB of RAM over 72 hours, eventually triggering the OOM killer and crashing all active connections.

**Cause**: A caching layer used `new` to allocate response objects but had a code path in the error handler that returned early without calling `delete`. Under normal traffic the leak was negligible, but when upstream services returned errors, thousands of cached objects leaked per hour.

**Impact**: Service degraded gradually over 3 days. Automated restarts masked the issue until the final crash caused a 45-minute outage affecting 200K users. Estimated revenue loss: $120K.

**Detection**: Memory profiling with `valgrind --leak-check=full --track-origins=yes` during a staging load test revealed 47,000 leaked blocks after 10 minutes of simulated error conditions.

**Solution**: Replaced raw `new`/`delete` with `std::unique_ptr` for all cached objects. Added a memory budget check in the cache that evicts entries when total allocated exceeds a threshold. Enabled AddressSanitizer (`-fsanitize=address`) in CI to catch future leaks automatically.

### Incident 4: Buffer Overflow in Network Packet Parser
**Problem**: A custom binary protocol parser crashed with SIGSEGV when receiving malformed packets from a client. Under specific packet lengths, the crash corrupted adjacent memory, causing downstream logic to process garbage data.

**Cause**: The parser used `memcpy(dest, src, packet_length)` where `packet_length` came directly from the network without bounds checking. A client sending `packet_length = 0xFFFFFFFF` caused a 4GB copy that overflowed a 4KB buffer, overwriting the return address on the stack.

**Impact**: Exploitable vulnerability — a researcher demonstrated arbitrary code execution in a controlled lab. The vulnerability existed in production for 14 months. Patch required a full security audit of all protocol parsers. Estimated remediation cost: $350K.

**Detection**: Fuzzing with AFL (American Fuzzy Lop) discovered the crash within 2 minutes of automated testing. Manual analysis revealed the exploitable overflow.

**Solution**: Replaced `memcpy` with bounds-checked copy: `if (packet_length > buffer_size) throw protocol_error(...);`. Replaced C-style buffer with `std::array<uint8_t, MAX_PACKET_SIZE>` and used `.at()` for indexed access. Added `-fsanitize=bounds` to the build and fuzzing as a CI gate.

### Incident 5: Uninitialized Variable in Financial Trading System
**Problem**: A high-frequency trading platform occasionally computed incorrect order quantities, producing trades 100x larger than intended. The bug manifested only during the first trading session after a system restart.

**Cause**: A member variable `double order_quantity_` in the `OrderEngine` class was not initialized in the constructor. On restart, the object was allocated on the heap (via `new`), and the memory happened to contain a stale value from a previously-deleted object. The stale value was a valid double but represented a much larger quantity.

**Impact**: 3 occurrences over 2 months. The first two were caught by pre-trade risk checks. The third bypassed a race condition in the risk system, resulting in $2.3M in erroneous trades that required manual unwinding. Regulatory investigation followed.

**Detection**: Post-incident analysis with Valgrind showed the uninitialized read. The bug was non-deterministic — it only appeared when the heap reuse pattern matched specific allocation sequences that occurred at market open.

**Solution**: Added `-Wuninitialized -Werror` to the compiler flags. Changed the class to use member initializer lists for all variables: `OrderEngine() : order_quantity_(0.0), price_(0.0) {}`. Added a static analysis check (clang-tidy `cppcoreguidelines-init-variables`) that flags any uninitialized member. Ran the check on the entire codebase (340K lines) and fixed 23 similar issues.

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

## Cross-References
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
6. **What is undefined behavior and why should you avoid it?**: Undefined behavior (UB) is behavior the C++ standard doesn't define — the compiler can do anything. Examples: signed integer overflow, null pointer dereference, buffer overflow. UB can cause crashes, data corruption, or appear to "work" until a different optimization level breaks it.
7. **What is name lookup and overload resolution?**: Name lookup finds all declarations matching a name. Overload resolution selects the best match among candidates. The process considers: exact match, promotion, standard conversion, user-defined conversion, ellipsis.
8. **What is the difference between `int* p` and `int* const p`?**: `int* p` is a pointer to int (can change what it points to). `int* const p` is a const pointer to int (cannot change what it points to, but can change the pointed-to value).
9. **What is the difference between `const int* p` and `int* const p`?**: `const int* p` is a pointer to const int (can change what it points to, but cannot change the pointed-to value). `int* const p` is a const pointer to int (cannot change what it points to, but can change the pointed-to value).
10. **What is the difference between `const int* p` and `const int* const p`?**: `const int* p` is a pointer to const int (can change what it points to, but cannot change the pointed-to value). `const int* const p` is a const pointer to const int (cannot change what it points to, and cannot change the pointed-to value).
11. **What is the difference between `int* p` and `int* const p`?**: `int* p` is a pointer to int (can change what it points to). `int* const p` is a const pointer to int (cannot change what it points to, but can change the pointed-to value).
12. **What is the difference between `const int* p` and `int* const p`?**: `const int* p` is a pointer to const int (can change what it points to, but cannot change the pointed-to value). `int* const p` is a const pointer to int (cannot change what it points to, but can change the pointed-to value).
13. **What is the difference between `const int* p` and `const int* const p`?**: `const int* p` is a pointer to const int (can change what it points to, but cannot change the pointed-to value). `const int* const p` is a const pointer to const int (cannot change what it points to, and cannot change the pointed-to value).
14. **What is the difference between `int* p` and `int* const p`?**: `int* p` is a pointer to int (can change what it points to). `int* const p` is a const pointer to int (cannot change what it points to, but can change the pointed-to value).
15. **What is the difference between `const int* p` and `int* const p`?**: `const int* p` is a pointer to const int (can change what it points to, but cannot change the pointed-to value). `int* const p` is a const pointer to int (cannot change what it points to, but can change the pointed-to value).

## References

- [C++ Core Guidelines — Declarations](https://isocpp.github.io/CppCoreGuidelines/CppCoreGuidelines#S-name)
- [CppReference — Fundamental Types](https://en.cppreference.com/w/cpp/language/types)
- [Compiler Explorer — Inspect generated code](https://godbolt.org/)
- [Valgrind Quick Start](https://valgrind.org/docs/manual/quick-start.html)
