# Knowledge Atoms — C++

## Overview

C++ knowledge atoms are the foundational, irreducible concepts that govern how every C++ program is compiled, linked, and executed. They encompass the compilation model (preprocessing → compilation → linking), the static type system, the memory model (stack vs heap, alignment, storage duration), the object model (vtables, vptr, object layout), and template metaprogramming. Understanding these atoms is essential for writing correct, performant, and maintainable C++ code — they are the rules the compiler follows, not features you call.

## Learning Objectives

After studying this module, you will be able to:

- Explain the C++ compilation pipeline (preprocessor → compiler → assembler → linker) and how translation units interact
- Apply the One-Definition Rule (ODR) to prevent linker errors and undefined behavior
- Distinguish stack vs heap allocation and choose appropriate storage durations
- Explain object layout, vtable dispatch, and their performance implications
- Use `constexpr`, `static_assert`, and template metaprogramming for zero-cost abstractions
- Identify and prevent common C++ mistakes (ODR violations, slicing, UB from signed overflow)
- Debug C++ programs using sanitizers, `perf`, and compiler diagnostics

## Prerequisites

- Basic C++ syntax (variables, functions, classes, inheritance)
- Familiarity with compilation: what a compiler and linker do
- Understanding of pointers and references
- Command-line experience (compiling with `g++` or `clang++`)

## Why It Matters

Before writing a single line of C++, you must understand how the language thinks. When you're building performance-critical systems, you need to know why the compiler generates certain code because violating the object model causes mysterious crashes that take months to debug. Engineers who internalize these atoms write code that works by design, not by accident.

## What It Is

Knowledge atoms are the irreducible concepts that underpin every C++ program — the compilation model, type system, memory model, object model, and template metaprogramming. They are not features you call but rules the compiler follows.

## History: C++ Evolution

| Year | Version | Key Changes | Impact on Knowledge Atoms |
|------|---------|-------------|---------------------------|
| 1979 | C with Classes | Classes, derived classes, strong type checking | Foundation of object model |
| 1983 | C++ | Virtual functions, function overloading, references | vtable dispatch introduced |
| 1998 | C++98 | STL, templates, exceptions, namespaces | Template metaprogramming born |
| 2003 | C++03 | Bug fixes, `export` templates | Minor corrections |
| 2011 | C++11 | `auto`, `constexpr`, move semantics, lambdas, smart pointers | Modern C++ era begins |
| 2014 | C++14 | `constexpr` relaxations, generic lambdas | Incremental improvements |
| 2017 | C++17 | `if constexpr`, `inline` variables, `std::optional/variant/filesystem` | Header ODR solved |
| 2020 | C++20 | Concepts, ranges, modules, coroutines | Modules replace headers |
| 2023 | C++23 | `std::print`, `std::expected`, `import std` | Standard library modularization |

### The Five Atoms

```
┌─────────────────────────────────────────────────────┐
│                   C++ Knowledge Atoms                │
├─────────────┬─────────────┬─────────────┬───────────┤
│ Compilation │    Type     │   Memory    │  Object   │
│    Model    │    System   │    Model    │   Model   │
├─────────────┴─────────────┴─────────────┴───────────┤
│              Template Metaprogramming                │
└─────────────────────────────────────────────────────┘
```

## Atom 1: Compilation Model

### The Problem
C++ is a compiled language — your source code doesn't run directly. It passes through multiple stages, each with its own rules. Understanding this pipeline prevents entire categories of bugs.

### How It Works

```
Source Code (.cpp)
       │
       ▼
Preprocessor (#include, #define)
       │
       ▼
Compiler (front-end: parsing, semantic analysis)
       │
       ▼
Intermediate Representation (IR)
       │
       ▼
Optimizer
       │
       ▼
Assembler
       │
       ▼
Object Files (.o / .obj)
       │
       ▼
Linker (resolves symbols, combines objects)
       │
       ▼
Executable (.exe / .out / ELF)
```

### One-Definition Rule (ODR)

The ODR is the most violated and least understood rule in C++. It states:
- Every function, variable, class, enum, and template must have exactly **one definition** across the entire program
- Violating ODR is **undefined behavior** — the compiler can do anything

```cpp
// header.h — VIOLATION: multiple definitions
int global_counter = 0;  // Defined in header, included in 3 translation units
```

```cpp
// header.h — CORRECT: declaration only
extern int global_counter;  // Declaration

// global.cpp — Definition
int global_counter = 0;  // Single definition
```

### Translation Units

Each `.cpp` file is compiled independently into a translation unit (TU). The compiler sees only what's in that TU — headers are literally copy-pasted by the preprocessor.

```cpp
// math.cpp
#include "math.h"  // Preprocessor pastes math.h contents here

int add(int a, int b) {
    return a + b;
}
```

### Inline Functions and Variables

```cpp
// Allowed in multiple TUs — linker picks one
inline int square(int x) { return x * x; }

// C++17: inline variables solve the ODR problem for constants
inline constexpr double PI = 3.14159265358979;
```

### Include Guards vs #pragma once

```cpp
// Traditional include guard
#ifndef MY_HEADER_H
#define MY_HEADER_H
// ... content ...
#endif

// Modern alternative (non-standard but widely supported)
#pragma once
// ... content ...
```

## Atom 2: Type System

### Static vs Dynamic Typing

C++ is **statically typed** — types are checked at compile time. This catches entire classes of bugs before the program runs.

```cpp
int x = 42;        // OK: type known at compile time
x = "hello";       // ERROR: type mismatch caught at compile time
auto y = 42;       // OK: type inferred as int at compile time
auto z = "hello";  // Type inferred as const char*
```

### Strong vs Weak Typing

C++ is **strongly typed** but with explicit conversions:

```cpp
int x = 42;
double y = x;      // Implicit conversion (int → double) — allowed but explicit
// double z = "hello";  // ERROR: no implicit conversion from const char* to double
int z = (int)"hello";  // Explicit cast — compiles but undefined behavior
```

### Type Qualifiers

```cpp
const int x = 42;          // Immutable — cannot be modified
volatile int mmio_reg;     // Tell compiler: value can change unexpectedly
constexpr int N = 100;     // Compile-time constant
mutable int cache_count;   // Can be modified even in const methods
```

### auto and decltype

```cpp
auto x = 42;                    // int
auto y = 3.14;                  // double
auto z = "hello";               // const char*
auto&& ref = std::move(x);     // rvalue reference

decltype(x) w = 100;           // Same type as x (int)
decltype(auto) get_value();     // Preserves value category
```

### Type Casting

```cpp
// C-style (dangerous — bypasses type system)
int x = (int)3.14;

// C++ static_cast (compile-time checked)
int y = static_cast<int>(3.14);

// dynamic_cast (runtime checked, for polymorphic types)
Base* base = new Derived();
Derived* derived = dynamic_cast<Derived*>(base);  // nullptr if wrong type

// const_cast (remove/add const — almost never needed)
const int* p = &x;
int* q = const_cast<int*>(p);  // Dangerous: modifying const is UB

// reinterpret_cast (bitwise reinterpretation — almost never needed)
int* p = reinterpret_cast<int*>(0x12345678);
```

## Atom 3: Memory Model

### Stack vs Heap

```
┌─────────────────────────────────────┐
│              Stack                   │
│  (Fast, automatic, LIFO)            │
│  - Local variables                  │
│  - Function parameters              │
│  - Return addresses                 │
│  - Automatic storage duration       │
│  - Typically 1-8 MB                 │
├─────────────────────────────────────┤
│              Heap                    │
│  (Slow, manual, random access)      │
│  - Dynamic allocation (new/delete)  │
│  - Large objects                    │
│  - Long-lived objects               │
│  - Controlled storage duration      │
│  - Limited by system memory         │
└─────────────────────────────────────┘
```

```cpp
void example() {
    int stack_var = 42;                    // Stack: automatic lifetime
    int* heap_var = new int(100);          // Heap: manual lifetime
    // ...
    delete heap_var;                       // Must manually free
}  // stack_var automatically destroyed here
```

### Storage Duration and Lifetime

```cpp
// Automatic storage (stack)
void func() {
    int x = 10;          // Destroyed when func() returns
}

// Static storage (global/static)
static int count = 0;    // Lives for entire program duration

// Dynamic storage (heap)
void func() {
    int* p = new int(5); // Lives until delete is called
    delete p;
}

// Thread-local storage
thread_local int thread_id = 0;  // One copy per thread
```

### Alignment

Modern CPUs require data to be aligned to specific boundaries for efficient access:

```cpp
struct Misaligned {
    char a;    // 1 byte
    int b;     // 4 bytes — compiler adds 3 bytes padding
};

struct Aligned {
    int b;     // 4 bytes
    char a;    // 1 byte + 3 bytes padding to next struct boundary
};

struct Packed {
    int b;
    char a;
} __attribute__((packed));  // No padding — may be slower on some architectures
```

## Atom 4: Object Model

### Object Layout in Memory

```cpp
class Base {
    int x;           // 4 bytes
    virtual void f(); // vptr (8 bytes on 64-bit)
};

class Derived : public Base {
    int y;           // 4 bytes
    void f() override;
};
```

```
Memory layout of Derived:
┌──────────────┬──────────────┬──────────────┐
│    vptr      │   Base::x    │  Derived::y  │
│  (8 bytes)   │  (4 bytes)   │  (4 bytes)   │
└──────────────┴──────────────┴──────────────┘
     │
     ▼
┌──────────────────────────┐
│   Virtual Table (vtable) │
├──────────────────────────┤
│   &Derived::f()          │
│   &Base::~Base()         │
│   ...                    │
└──────────────────────────┘
```

### Virtual Tables and Dynamic Dispatch

When you call a virtual function, the compiler:
1. Reads the vptr from the object
2. Looks up the function pointer in the vtable
3. Calls through the function pointer

```cpp
Base* ptr = new Derived();
ptr->f();  // Runtime: vtable lookup → Derived::f()
delete ptr;
```

**Cost of virtual dispatch**: One pointer dereference + indirect call. Typically 2-5 ns overhead per call. For most applications, this is negligible.

### Object Slicing

```cpp
class Animal { virtual void speak(); };
class Dog : public Animal { void speak() override; };

std::vector<Animal> animals;
Dog dog;
animals.push_back(dog);  // SLICING: Dog parts lost, only Animal parts copied
```

### Empty Base Optimization

```cpp
class Empty {};  // sizeof(Empty) is typically 1 byte (not 0)

struct Derived : Empty {
    int x;
};
// sizeof(Derived) == 4 (not 12) — Empty contributes no storage
```

## Atom 5: Template Metaprogramming

### Compile-Time Computation

Templates are evaluated at compile time, not runtime. This enables zero-cost abstractions.

```cpp
// Compile-time factorial
template <size_t N>
struct Factorial {
    static constexpr size_t value = N * Factorial<N-1>::value;
};

template <>
struct Factorial<0> {
    static constexpr size_t value = 1;
};

// Usage
static_assert(Factorial<5>::value == 120);  // Computed at compile time
```

### Type Traits

```cpp
#include <type_traits>

static_assert(std::is_integral_v<int>);
static_assert(std::is_floating_point_v<double>);
static_assert(std::is_same_v<int, int32_t>);
static_assert(std::is_base_of_v<Base, Derived>);

// Enable_if / SFINAE
template <typename T>
std::enable_if_t<std::is_arithmetic_v<T>, T>
safe_divide(T a, T b) {
    return b != 0 ? a / b : T{};
}
```

### Constexpr if (C++17)

```cpp
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

## Engineering Decision Framework

### When to Deep-Dive Into Knowledge Atoms
- When debugging undefined behavior or mysterious crashes
- When optimizing for memory layout or cache performance
- When designing APIs that rely on virtual dispatch or templates
- When mentoring junior developers who write "C++-like" code
- When working on performance-critical systems (game engines, trading, embedded)

### When NOT to Over-Analyze
- For simple scripts or prototypes (atoms matter less)
- When the compiler handles it correctly (trust the toolchain)
- When premature optimization would waste time

### Alternatives to Understanding Atoms
| Situation | Alternative | Trade-off |
|-----------|-------------|-----------|
| Don't understand ODR | Use `inline` everywhere | Larger binaries |
| Don't understand memory model | Use `std::shared_ptr` for everything | Performance overhead |
| Don't understand object model | Avoid virtual functions | Less flexible design |
| Don't understand templates | Use `void*` and macros | Type safety lost |

### Real-World Production Examples
1. **Chrome Browser**: V8 JavaScript engine uses template metaprogramming to generate optimized code paths at compile time
2. **Unreal Engine**: Custom object model with garbage collection built on top of C++'s object model
3. **MySQL**: Uses knowledge of memory alignment for buffer pool management, achieving near-zero-copy data access
4. **High-Frequency Trading**: Firms like Jump Trading use object layout knowledge to minimize cache misses in order books

## Production Notes

### Build Configuration

| Setting | Recommended Value | Why |
|---------|------------------|-----|
| Compiler warnings | `-Wall -Wextra -Wpedantic -Werror` | Catch bugs at compile time |
| Sanitizers (CI) | `-fsanitize=address,undefined` | Detect memory errors and UB |
| Optimization level | `-O2` for release, `-O0 -g` for debug | Balance between speed and debuggability |
| Standard | `-std=c++17` or `-std=c++20` | Access to modern features |
| LTO (Link-Time Optimization) | `-flto` | Cross-TU inlining and optimization |

### Header File Guidelines

- Use `#pragma once` or include guards — never both
- Forward-declare instead of `#include` when possible (reduces compilation dependencies)
- Place `#include` directives in this order: corresponding `.h`, C system, C++ stdlib, other libraries, project headers
- Never define non-`inline` functions or variables in headers

### Template Compilation

- Templates are instantiated per translation unit — each `.cpp` that uses `vector<int>` instantiates it separately
- Explicit instantiation (`template class std::vector<int>;`) in one TU reduces compile time
- Consider C++20 modules to eliminate header-based template compilation

### ABI Considerations

- `std::string`, `std::vector`, and other STL types have ABI-stable layouts within a compiler version
- ABI breaks occur when changing class layouts (adding virtual functions, changing member order)
- Use the Itanium C++ ABI (GCC/Clang) or MSVC ABI consistently across linked code

## Production Checklist
- [ ] Understand ODR — never define variables/functions in headers without `inline`/`extern`
- [ ] Use `constexpr` for compile-time constants
- [ ] Prefer `static_cast` over C-style casts
- [ ] Understand object layout — profile cache misses with `perf`
- [ ] Use `alignas()` for performance-critical data structures
- [ ] Enable compiler warnings (`-Wall -Wextra -Wpedantic`)
- [ ] Run sanitizers in CI (`-fsanitize=address,undefined`)
- [ ] Use `static_assert` for compile-time checks
- [ ] Document template constraints with `static_assert` or C++20 concepts

## Maturity Levels

### Beginner (0-6 months)
- Understand that C++ is compiled
- Know the difference between stack and heap
- Can use basic type inference (`auto`)
- Understand what `const` means

### Intermediate (6-18 months)
- Understand ODR and how to avoid violations
- Can explain virtual dispatch and vtables
- Know alignment and padding implications
- Can use `static_assert` and type traits

### Advanced (18+ months)
- Can design cache-friendly data structures
- Understand template instantiation and bloat
- Can debug undefined behavior with sanitizer output
- Know when to use CRTP over virtual functions

## Common Myths Debunked

### Myth 1: "C++ is just C with classes"
**Reality**: C++ is a multi-paradigm language with templates, lambdas, move semantics, concepts, and a standard library that bears little resemblance to C. Modern C++ (C++17/20) is dramatically different from C.

### Myth 2: "Manual memory management is always necessary"
**Reality**: Modern C++ uses RAII, smart pointers, and containers that manage memory automatically. Manual `new`/`delete` is rare in well-written C++ code.

### Myth 3: "Templates are just macros"
**Reality**: Templates are type-safe, compile-time polymorphism with full language support. Macros are text substitution with no type checking.

### Myth 4: "Virtual functions are always slow"
**Reality**: Virtual dispatch adds ~2-5 ns per call. For most applications, this is negligible. The bigger cost is cache misses from pointer-chasing, not the dispatch itself.

### Myth 5: "C++ has no garbage collection, so it leaks"
**Reality**: RAII and smart pointers provide automatic resource management without GC pauses. Leaks are a programming error, not a language limitation.

## Internal Working

### How the Compiler Processes Your Code

When you write `g++ -o program main.cpp`, the compiler performs these steps internally:

1. **Preprocessing**: `#include` directives expand headers textually; `#define` macros are substituted; `#if`/`#ifdef` blocks are evaluated. The output is a single translation unit (TU) with all macros resolved.

2. **Parsing & Semantic Analysis**: The compiler parses the TU into an Abstract Syntax Tree (AST), checks types, resolves overloads, instantiates templates, and verifies const-correctness. Errors here are compile-time errors.

3. **IR Generation**: The AST is lowered to Intermediate Representation (SSA form). This is platform-independent and where most optimizations happen (dead code elimination, inlining, loop unrolling).

4. **Optimization**: The optimizer applies transformations: constant folding, function inlining, vectorization (auto-SIMD), and devirtualization (when the compiler can prove the concrete type).

5. **Code Generation**: IR is lowered to platform-specific assembly. The compiler maps virtual registers to physical registers, handles instruction scheduling, and emits machine code.

6. **Assembly & Linking**: Assembler produces object files (`.o`). The linker resolves symbols across TUs, performs section merging, applies relocations, and produces the final executable.

### Name Mangling in Detail

The compiler encodes function signatures to support overloading:

```cpp
// Source
int add(int a, int b);

// Mangled (GCC/Clang)
_Z3addii    // _Z = prefix, 3 = length, add = name, i i = two ints
```

This is why `extern "C"` is needed for C interoperability — it disables mangling.

### Template Instantiation Process

When the compiler encounters `std::vector<int>`, it:

1. Parses the template definition from `<vector>`
2. Substitutes `T = int` throughout the template body
3. Compiles the resulting code as if it were hand-written
4. Each unique type combination creates a new instantiation (template bloat)

```cpp
std::vector<int> vi;    // Instantiates vector<int>
std::vector<double> vd; // Separate instantiation of vector<double>
```

## Syntax

### Core Syntax Patterns

```cpp
// Variables and initialization
int x = 42;            // Copy initialization
int y{42};             // Direct list initialization (C++11)
int z = {42};          // Copy list initialization
auto w = 42;           // Type deduction

// Functions
int add(int a, int b) { return a + b; }        // Definition
int add(int, int);                              // Declaration (no names)
constexpr int square(int x) { return x * x; }  // Compile-time function

// Classes
class Widget {
public:
    Widget(int id) : id_(id) {}         // Constructor with initializer list
    virtual ~Widget() = default;        // Virtual destructor
    int id() const { return id_; }      // Const member function
private:
    int id_;
};

// Templates
template <typename T>
T max_value(T a, T b) { return a > b ? a : b; }

// C++20 Concepts
template <typename T>
concept Numeric = std::is_arithmetic_v<T>;

template <Numeric T>
T safe_add(T a, T b) { return a + b; }
```

### Preprocessor Directives

```cpp
#include <iostream>        // System header (searches system paths)
#include "myheader.h"      // Local header (searches current directory first)
#define PI 3.14159         // Object-like macro
#define SQUARE(x) ((x)*(x)) // Function-like macro (avoid — use constexpr)
#ifdef DEBUG               // Conditional compilation
    #define LOG(msg) std::cerr << msg << '\n'
#else
    #define LOG(msg)
#endif
#pragma once               // Include guard (non-standard, widely supported)
```

## Examples

### Easy: Stack vs Heap Allocation

```cpp
#include <iostream>
#include <memory>

int main() {
    // Stack: fast, automatic lifetime
    int stack_var = 42;
    std::cout << "Stack: " << stack_var << '\n';

    // Heap: flexible, manual/RAII lifetime
    auto heap_var = std::make_unique<int>(100);
    std::cout << "Heap: " << *heap_var << '\n';
    // Automatically freed when unique_ptr goes out of scope

    return 0;
}
```

### Medium: Virtual Dispatch and Object Layout

```cpp
#include <iostream>
#include <vector>
#include <memory>

class Shape {
public:
    virtual double area() const = 0;
    virtual ~Shape() = default;
};

class Circle : public Shape {
    double radius_;
public:
    explicit Circle(double r) : radius_(r) {}
    double area() const override { return 3.14159 * radius_ * radius_; }
};

class Rectangle : public Shape {
    double w_, h_;
public:
    Rectangle(double w, double h) : w_(w), h_(h) {}
    double area() const override { return w_ * h_; }
};

int main() {
    std::vector<std::unique_ptr<Shape>> shapes;
    shapes.push_back(std::make_unique<Circle>(5.0));
    shapes.push_back(std::make_unique<Rectangle>(4.0, 6.0));

    for (const auto& s : shapes) {
        std::cout << "Area: " << s->area() << '\n';  // Virtual dispatch
    }
}
```

### Hard: Compile-Time Computation with Templates

```cpp
#include <type_traits>
#include <iostream>

// Compile-time Fibonacci
template <unsigned N>
struct Fibonacci {
    static constexpr unsigned value = Fibonacci<N-1>::value + Fibonacci<N-2>::value;
};

template <>
struct Fibonacci<0> { static constexpr unsigned value = 0; };

template <>
struct Fibonacci<1> { static constexpr unsigned value = 1; };

// Constexpr function (C++14 allows loops)
constexpr unsigned fib(unsigned n) {
    if (n <= 1) return n;
    unsigned a = 0, b = 1;
    for (unsigned i = 2; i <= n; ++i) {
        unsigned temp = a + b;
        a = b;
        b = temp;
    }
    return b;
}

int main() {
    static_assert(Fibonacci<10>::value == 55);  // Compile-time
    std::cout << "fib(10) = " << fib(10) << '\n';  // Runtime (but optimized)
}
```

### Enterprise: Header-Only Library with ODR Safety

```cpp
// math_utils.h — Header-only, ODR-safe
#pragma once
#include <concepts>
#include <cmath>

namespace math_utils {

// inline function: ODR-safe, one definition across all TUs
template <std::floating_point T>
inline T distance(T x1, T y1, T x2, T y2) {
    T dx = x2 - x1;
    T dy = y2 - y1;
    return std::sqrt(dx * dx + dy * dy);
}

// inline constexpr: ODR-safe constant
inline constexpr double PI = 3.14159265358979323846;

// C++20 concept for type constraints
template <typename T>
concept Numeric = std::is_arithmetic_v<T>;

template <Numeric T>
constexpr T clamp(T value, T lo, T hi) {
    return (value < lo) ? lo : (value > hi) ? hi : value;
}

}  // namespace math_utils
```

## Performance Considerations

| Factor | Impact | Recommendation |
|--------|--------|----------------|
| Stack vs Heap | Stack allocation is ~100x faster than heap | Prefer stack; use heap only for large/long-lived objects |
| Virtual dispatch | ~2-5 ns per call (pointer dereference + indirect call) | Negligible for most code; use CRTP for hot paths |
| Template instantiation | Each unique type combo creates new code | Limit template parameter combinations; use type erasure |
| Cache misses | ~100 ns per miss (L1 ~1ns, L2 ~4ns, L3 ~12ns) | Use struct-of-arrays for hot data; align to cache lines |
| `constexpr` computation | Zero runtime cost | Move computation to compile time when possible |
| Move semantics | Avoids deep copies for large objects | Return by value (NRVO); use `std::move` for transfers |
| `reinterpret_cast` | No runtime cost but dangerous | Avoid; use `static_cast` or `memcpy` for type punning |
| Branch prediction | ~15 ns misprediction penalty | Use `[[likely]]`/`[[unlikely]]` hints (C++20) |

### Cache-Friendly Data Layout

```cpp
// Bad: Array of Structures (AoS) — scattered cache lines
struct Particle {
    float x, y, z;     // Position (12 bytes)
    float vx, vy, vz;  // Velocity (12 bytes)
    int type;           // Type (4 bytes)
};  // sizeof = 28 bytes, padded to 32

// Good: Structure of Arrays (SoA) — contiguous access
struct Particles {
    std::vector<float> x, y, z;      // Positions contiguous
    std::vector<float> vx, vy, vz;   // Velocities contiguous
    std::vector<int> type;            // Types contiguous
};
```

## Best Practices

| Practice | Why | Example |
|----------|-----|---------|
| Use `constexpr` for constants | Compile-time safety, zero runtime cost | `constexpr int MAX = 100;` |
| Prefer `static_cast` over C-style casts | Type-safe, searchable, explicit | `static_cast<int>(3.14)` |
| Use smart pointers, not raw `new`/`delete` | Automatic lifetime management | `auto p = std::make_unique<T>();` |
| Mark destructors `virtual` in base classes | Prevents UB when deleting derived via base | `virtual ~Base() = default;` |
| Use `override` keyword | Compiler catches signature mismatches | `void f() override;` |
| Prefer `auto` when type is obvious | Reduces verbosity, preserves refactorability | `auto v = std::vector<int>();` |
| Use range-based for with `const&` | Avoids copies, prevents modification | `for (const auto& x : vec)` |
| Enable compiler warnings | Catches bugs before runtime | `-Wall -Wextra -Wpedantic -Werror` |
| Use `[[nodiscard]]` on important returns | Prevents ignoring error codes | `[[nodiscard]] int compute();` |
| Prefer algorithms over hand-written loops | Readable, optimizable, less error-prone | `std::transform`, `std::accumulate` |

## Common Mistakes

| Mistake | Consequence | Fix |
|---------|-------------|-----|
| Defining variables in headers | ODR violations, linker errors | Use `extern` declarations, `inline` variables |
| Assuming `sizeof(pointer)` is constant | Code breaks on 32-bit vs 64-bit | Use `sizeof(T*)` explicitly |
| Ignoring alignment | Performance penalties, crashes on ARM | Use `alignas()` or let compiler handle |
| Overusing virtual functions | Cache misses, indirect call overhead | Consider CRTP for static polymorphism |
| Using `reinterpret_cast` freely | Undefined behavior, portability issues | Use `static_cast` or `dynamic_cast` |
| Forgetting virtual destructor | UB when deleting derived via base pointer | Always add `virtual ~Base() = default;` |
| Slicing objects in containers | Derived data lost silently | Use `std::unique_ptr<Base>` in containers |
| Signed integer overflow | Undefined behavior | Use `int64_t` or unsigned types for large values |
| Modifying container during range-for | Iterator invalidation, UB | Collect changes separately, apply after loop |
| Mixing `new`/`delete` with `malloc`/`free` | Undefined behavior, no destructor calls | Use `new`/`delete` or smart pointers consistently |

## Cross-References

| Topic | Related Module | Connection |
|-------|---------------|------------|
| Compilation model | Build systems, Make/CMake | Understanding TUs informs build dependency design |
| Type system | Templates, Concepts | Templates extend the type system with compile-time polymorphism |
| Memory model | Smart pointers, RAII | RAII ties resource lifetime to scope, preventing leaks |
| Object model | OOP, Polymorphism | Virtual dispatch enables runtime polymorphism |
| Template metaprogramming | Design patterns, Policy-based design | Templates implement compile-time strategy patterns |
| ODR | Linking, Shared libraries | ODR governs how symbols are resolved across TUs |
| Undefined behavior | Security, Sanitizers | UB is a security vulnerability; sanitizers detect it |
| Cache alignment | High-performance computing | Data layout determines cache efficiency |

## Security

| Risk | Impact | Mitigation |
|------|--------|------------|
| Buffer overflow from unchecked pointer arithmetic | Remote code execution, memory corruption | Use `std::array`, bounds-checked access, and AddressSanitizer in CI |
| Undefined behavior from `reinterpret_cast` | Exploitable memory corruption, non-portable code | Use `static_cast` or `dynamic_cast`; ban `reinterpret_cast` except for serialization |
| Object layout assumptions across platforms | ABI breaks, security-critical misinterpretation of data | Use fixed-width types, test cross-compilation, avoid platform-specific packing |
| Use-after-free from dangling pointers | Arbitrary code execution | Use `std::unique_ptr`/`std::shared_ptr`; run AddressSanitizer |
| Integer overflow in size calculations | Heap buffer overflow | Use safe arithmetic libraries; validate sizes before allocation |
| Format string vulnerabilities | Information disclosure, code execution | Never use user input as format string; use `std::format` (C++20) |
| DLL/shared library ABI breaks | Crash on load, security vulnerabilities | Use stable ABI boundaries; prefer static linking for internal code |

## One-Minute Revision

| Atom | What It Is | Why It Matters | Key Rule |
|------|-----------|----------------|----------|
| Compilation Model | Source → Preprocessor → Compiler → Linker | Determines how code is built and linked | ODR: one definition per entity |
| Type System | Static, strong typing with `auto` | Catches bugs at compile time | Prefer `static_cast` over C-style casts |
| Memory Model | Stack (fast) vs Heap (flexible) | Performance and lifetime control | RAII: tie resource lifetime to scope |
| Object Model | vptr + vtable for virtual dispatch | Polymorphism has predictable cost | Virtual destructor in base classes |
| Template Metaprogramming | Compile-time code generation | Zero-cost abstractions | Templates are evaluated at compile time |

## Related Topics
- [Fundamentals](../01-fundamentals/) — Apply these atoms in practice
- [OOP](../02-oop/) — Object model in action with classes and inheritance
- [Templates](../03-templates/) — Deep dive into template metaprogramming
- [Memory Management](../05-memory-management/) — Master the memory model
- [Performance](../11-performance/) — Use knowledge atoms for optimization

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| ODR violations across translation units | Linker error messages + `-Werror` | Compile with `-Wall -Wextra -Wpedantic` and examine "multiple definition" errors to find header-defined variables |
| Dangling pointer from wrong storage duration | AddressSanitizer (`-fsanitize=address`) | Run binary under ASan; it reports use-after-free and stack-buffer-overflows with allocation traces |
| Virtual dispatch overhead in hot paths | `perf record` + `perf report` | Profile with `perf record -g ./program` then check for `vtable` and indirect call entries in the hot path |
| Template instantiation bloat increasing binary size | `bloaty` or `nm --size-sort` | Run `bloaty binary` to identify which template instantiations consume the most binary space |
| Undefined behavior from strict aliasing violations | `-fno-strict-aliasing` + Valgrind | Compile with `-fno-strict-aliasing` to test; use Valgrind to detect invalid memory access patterns |

## Code Review Checklist

- [ ] No variables or functions defined in headers without `inline`/`extern` (ODR compliance)
- [ ] `constexpr` used for compile-time constants instead of raw `#define`
- [ ] `static_cast` preferred over C-style casts
- [ ] Virtual destructors present in base classes with virtual methods
- [ ] `alignas()` used for cache-critical data structures
- [ ] `static_assert` validates compile-time invariants in template code
- [ ] Compiler warnings enabled (`-Wall -Wextra -Wpedantic`) and treated as errors

## Architecture Considerations

Understanding knowledge atoms is foundational to every C++ architecture decision. The compilation model determines how code is organized into translation units and headers, directly impacting build times and modularity. The object model (vtable layout, empty base optimization) influences how class hierarchies are designed for cache efficiency. Template metaprogramming enables zero-cost abstractions that form the backbone of modern C++ libraries and frameworks.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Header-only libraries (ODR-safe via `inline`) | Small utility functions and templates | Faster compilation per TU vs. larger binary from duplicate code |
| CRTP for static polymorphism | Performance-critical dispatch paths | Zero overhead vs. reduced readability and debugging difficulty |
| `constexpr` compile-time computation | Lookup tables, hashing, type-safe constants | Zero runtime cost vs. increased compile time |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Buffer overflow from unchecked pointer arithmetic | Remote code execution, memory corruption | Use `std::array`, bounds-checked access, and AddressSanitizer in CI |
| Undefined behavior from `reinterpret_cast` | Exploitable memory corruption, non-portable code | Use `static_cast` or `dynamic_cast`; ban `reinterpret_cast` except for serialization |
| Object layout assumptions across platforms | ABI breaks, security-critical misinterpretation of data | Use fixed-width types, test cross-compilation, avoid platform-specific packing |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| C++11 | `constexpr` functions for compile-time computation | Replace `#define` constants with `constexpr` variables and functions |
| C++17 | `constexpr if`, `inline` variables | Replace SFINAE with `constexpr if`; use `inline constexpr` for header constants |
| C++20 | Concepts for template constraints | Replace `static_assert` and SFINAE with `requires` clauses for clearer errors |

## Version Validation

| Feature | C++ Version | Status |
|---------|------------|--------|
| `constexpr` variables and functions | C++11 | Widely supported |
| `inline` variables | C++17 | Widely supported |
| `constexpr if` | C++17 | Widely supported |
| Concepts (`requires` clauses) | C++20 | Supported in GCC 10+, Clang 12+, MSVC 19.22+ |

## Interview Questions

1. **What is the One-Definition Rule (ODR) and why does it matter?**: ODR states that every entity (function, variable, class, template) must have exactly one definition across the entire program. Violating ODR is undefined behavior — the compiler can do anything. It matters because header-defined variables without `inline`/`extern` cause linker errors or silent corruption.
2. **Explain the difference between stack and heap allocation**: Stack allocation is automatic, fast (~100x faster than heap), and scoped to function lifetime. Heap allocation is manual or RAII-managed, flexible in size and lifetime, but slower due to free-list searches and potential system calls.
3. **What is object slicing and how do you prevent it?**: Object slicing occurs when a derived class object is assigned to a base class variable by value, silently losing derived-specific data and virtual overrides. Prevent it by using pointers or smart pointers (`std::unique_ptr<Base>`) for polymorphic containers.
4. **How does virtual dispatch work at the machine level?**: The compiler reads the vptr from the object, looks up the function pointer in the vtable (an array of function pointers), and calls through that pointer. Cost is one pointer dereference plus indirect call (~2-5 ns).
5. **What is the purpose of `constexpr` in modern C++?**: `constexpr` enables compile-time computation, producing zero runtime cost. It replaces `#define` constants, enables template metaprogramming with values, and allows the compiler to validate invariants at compile time via `static_assert`.
6. **What is undefined behavior and how does it affect optimization?**: UB is behavior the C++ standard doesn't define — the compiler can do anything. Optimizers exploit UB to generate faster code (e.g., assuming no signed overflow enables loop optimizations). UB can cause crashes, data corruption, or appear to "work" until a different optimization level breaks it.
7. **What is the difference between `struct` and `class` in C++?**: The only difference is default access: `struct` members are public by default; `class` members are private. Use `struct` for passive data aggregates; use `class` for encapsulated objects with invariants.
8. **What is name mangling and why is it used?**: Name mangling encodes function/variable names with type information to support overloading. The compiler transforms `foo(int)` into something like `_Z3fooi`. This allows the linker to distinguish between overloaded functions.
9. **What is SFINAE and when should you use it?**: Substitution Failure Is Not An Error — when template argument substitution fails, the compiler silently removes that overload from consideration instead of producing an error. Use SFINAE (or better, C++20 concepts) to enable/disable overloads based on type properties.
10. **What is the difference between `static_cast`, `dynamic_cast`, `const_cast`, and `reinterpret_cast`?**: `static_cast` performs compile-time conversions (numeric, up/downcast without RTTI). `dynamic_cast` performs safe downcasts with RTTI (returns nullptr for pointers). `const_cast` adds/removes const. `reinterpret_cast` reinterprets bit patterns (dangerous, non-portable).
11. **What is RAII and why is it fundamental to C++?**: Resource Acquisition Is Initialization — tie resource lifetime to object lifetime. Constructor acquires, destructor releases. Enables exception-safe code without explicit cleanup. Fundamental because it prevents resource leaks and enables deterministic destruction.
12. **What is the difference between `new`/`delete` and `malloc`/`free`?**: `new`/`delete` call constructors/destructors and are type-safe. `malloc`/`free` only allocate/deallocate memory without calling constructors. Never mix them — use `new`/`delete` for C++ objects, `malloc`/`free` only for C-compatible code.
13. **What is template specialization and when should you use it?**: Providing a different implementation for specific template arguments. Use when the generic implementation is inefficient or incorrect for certain types (e.g., `bool` specialization for vector to pack bits). Prefer full specialization over partial specialization when possible.
14. **What is the compilation model in C++?**: Each `.cpp` file is compiled independently into an object file (translation unit). The linker combines object files into an executable. Headers are textually included (`#include`), causing repeated compilation. Modules (C++20) aim to fix this.
15. **What is the difference between `auto` and explicit type declarations?**: `auto` deduces type from initializer, reducing verbosity and enabling generic code. Explicit declarations document intent clearly. Use `auto` when type is obvious; use explicit types when clarity requires it.

## Production Incidents

### Incident 1: One-Definition Rule Violation

**Problem:** A header file defined a global variable without `extern`, causing linker errors when included in multiple translation units.

```cpp
// config.h
int global_timeout = 30;  // ODR violation — defined in header
```

**Cause:** Defining variables in headers violates ODR. Each translation unit that includes the header creates its own copy.

**Impact:** Linker error: "multiple definition of `global_timeout`".

**Solution:** Use `extern` in header, define in `.cpp` file:

```cpp
// config.h
extern int global_timeout;  // Declaration

// config.cpp
int global_timeout = 30;    // Definition
```

**Prevention:** Never define non-`inline` variables in headers. Use `constexpr` or `inline` for header constants.

### Incident 2: Undefined Behavior from Signed Integer Overflow

**Problem:** A financial calculation used `int` for amounts. When total exceeded 2^31-1, signed overflow occurred — undefined behavior.

```cpp
int total = 0;
for (const auto& transaction : transactions) {
    total += transaction.amount;  // UB when overflow occurs
}
```

**Cause:** Signed integer overflow is undefined behavior in C++. The compiler can optimize based on the assumption it never happens.

**Impact:** Incorrect totals, potential exploitation, crashes.

**Solution:** Use unsigned types or check for overflow:

```cpp
int64_t total = 0;  // 64-bit prevents overflow for realistic amounts
for (const auto& transaction : transactions) {
    total += transaction.amount;
}
```

**Prevention:** Use fixed-width types (`int64_t`) for financial calculations. Enable `-fsanitize=undefined` in CI.

### Incident 3: Dangling Reference from Range-based For

**Problem:** A range-based for loop captured elements by reference, but the container was modified during iteration.

```cpp
std::vector<int> vec = {1, 2, 3, 4, 5};
for (const auto& elem : vec) {
    if (elem == 3) {
        vec.push_back(6);  // Invalidates iterators
    }
}
```

**Cause:** Modifying a container while iterating over it invalidates iterators, causing undefined behavior.

**Impact:** Crashes, corrupted data, undefined behavior.

**Solution:** Copy container or use index-based loop:

```cpp
std::vector<int> vec = {1, 2, 3, 4, 5};
std::vector<int> to_add;
for (const auto& elem : vec) {
    if (elem == 3) {
        to_add.push_back(6);
    }
}
vec.insert(vec.end(), to_add.begin(), to_add.end());
```

**Prevention:** Never modify a container while iterating. Use algorithms or collect modifications separately.

### Incident 4: Null Pointer Dereference from Unchecked `dynamic_cast`

**Problem:** `dynamic_cast` returned `nullptr` for invalid casts, but the code didn't check the result.

```cpp
Base* base = get_object();
Derived* derived = dynamic_cast<Derived*>(base);
derived->do_something();  // Crash if base is not Derived
```

**Cause:** `dynamic_cast` returns `nullptr` for pointer casts when the cast fails. Code didn't check for `nullptr`.

**Impact:** Null pointer dereference, crash.

**Solution:** Check result before use:

```cpp
Base* base = get_object();
Derived* derived = dynamic_cast<Derived*>(base);
if (derived) {
    derived->do_something();
} else {
    // Handle error
}
```

**Prevention:** Always check `dynamic_cast` result. Prefer `static_cast` when type is known.

### Incident 5: Memory Leak from Raw `new` Without `delete`

**Problem:** Code used raw `new` without corresponding `delete`, causing memory leaks.

```cpp
void process() {
    int* data = new int[1000];
    // ... process data ...
    // forgot to delete[] data
}
```

**Cause:** Raw `new` requires manual `delete`. Forgetting to delete causes memory leaks.

**Impact:** Memory leaks, eventually OOM.

**Solution:** Use smart pointers or RAII:

```cpp
void process() {
    auto data = std::make_unique<int[]>(1000);
    // ... process data ...
    // automatically deleted when function exits
}
```

**Prevention:** Never use raw `new`/`delete`. Use `std::unique_ptr` or `std::shared_ptr`.

## References

- [ISO C++ Standard](https://isocpp.org/std/the-standard)
- [CppReference — Knowledge Atoms Topics](https://en.cppreference.com/w/)
- [C++ Core Guidelines — B, C, and F Sections](https://isocpp.github.io/CppCoreGuidelines/CppCoreGuidelines)
- [Compiler Explorer (Godbolt)](https://godbolt.org/) — Inspect generated assembly for ODR and layout questions
