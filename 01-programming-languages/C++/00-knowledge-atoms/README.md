# Knowledge Atoms — C++

## Why It Matters

Before writing a single line of C++, you must understand how the language thinks. When you're building performance-critical systems, you need to know why the compiler generates certain code because violating the object model causes mysterious crashes that take months to debug. Engineers who internalize these atoms write code that works by design, not by accident.

## What It Is

Knowledge atoms are the irreducible concepts that underpin every C++ program — the compilation model, type system, memory model, object model, and template metaprogramming. They are not features you call but rules the compiler follows.

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

### Common Mistakes

| Mistake | Consequence | Fix |
|---------|-------------|-----|
| Defining variables in headers | ODR violations, linker errors | Use `extern` declarations, `inline` variables |
| Assuming `sizeof(pointer)` is constant | Code breaks on 32-bit vs 64-bit | Use `sizeof(T*)` explicitly |
| Ignoring alignment | Performance penalties, crashes on ARM | Use `alignas()` or let compiler handle |
| Overusing virtual functions | Cache misses, indirect call overhead | Consider CRTP for static polymorphism |
| Using `reinterpret_cast` freely | Undefined behavior, portability issues | Use `static_cast` or `dynamic_cast` |

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
