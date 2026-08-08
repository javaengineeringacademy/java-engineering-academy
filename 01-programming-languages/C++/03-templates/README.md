# Templates — C++ Language

## The Problem Templates Solve

Every language faces the same tension: write code once for many types, or write specialized code for each type. Without templates, you either duplicate logic for every type (error-prone, unmaintainable) or use `void*` and macros (type-unsafe, un-debuggable). C++ templates solve this by generating type-safe, zero-overhead generic code at compile time.

**Production reality**: A financial library needed pricing models for `float`, `double`, and 12 custom fixed-point types. Without templates, that's 14 copies of 2000-line algorithms. With templates, one implementation handles all types — and the compiler generates specialized machine code for each, achieving identical performance to hand-written type-specific code.

## What Are Templates?

Templates are C++'s mechanism for **generic programming** — writing code that works with any type while preserving full type safety and zero runtime overhead. They are evaluated at compile time, meaning the compiler generates specialized code for each type you use.

## Architecture: How Templates Fit Together

```
┌─────────────────────────────────────────────────────────┐
│                   C++ Templates                          │
├───────────────┬───────────────┬─────────────────────────┤
│   Function    │    Class      │     Variable            │
│   Templates   │    Templates  │     Templates (C++14)   │
├───────────────┴───────────────┴─────────────────────────┤
│          Template Specialization (Full & Partial)        │
├─────────────────────────────────────────────────────────┤
│    SFINAE / Concepts (C++20) / constexpr if (C++17)     │
├─────────────────────────────────────────────────────────┤
│      Variadic Templates / Fold Expressions (C++17)       │
├─────────────────────────────────────────────────────────┤
│     Template Metaprogramming (Type Traits, Compile-time)│
└─────────────────────────────────────────────────────────┘
```

## Function Templates

### The Problem Function Templates Solve
You want a `max` function that works for `int`, `double`, `std::string`, and any type with `operator>`. Without templates, you'd write separate overloads for each type.

```cpp
// Generic max — works with ANY type that supports >
template <typename T>
T max_value(T a, T b) {
    return (a > b) ? a : b;
}

// Usage — compiler deduces T from arguments
int m1 = max_value(3, 7);           // T = int
double m2 = max_value(3.14, 2.71);  // T = double
std::string m3 = max_value(std::string("hello"), std::string("world"));

// Explicit template argument
auto m4 = max_value<double>(3, 2.5);  // T = double, implicit conversion for int
```

### Template Argument Deduction

```cpp
template <typename T>
void process(T value) {
    std::cout << value << "\n";
}

process(42);        // T deduced as int
process(3.14);      // T deduced as double
// process();       // ERROR: cannot deduce T
process<int>(42);   // Explicit — no deduction needed
```

### Multiple Template Parameters

```cpp
template <typename T, typename U>
auto add(T a, U b) -> decltype(a + b) {
    return a + b;
}

auto result = add(1, 2.5);  // T=int, U=double, return type double
```

## Class Templates

### The Problem Class Templates Solve
A container like `Stack` should work for `int`, `std::string`, or any type — without rewriting the implementation.

```cpp
template <typename T>
class Stack {
private:
    std::vector<T> elements_;
public:
    void push(const T& elem) {
        elements_.push_back(elem);
    }

    T pop() {
        if (elements_.empty()) throw std::out_of_range("Stack empty");
        T top = elements_.back();
        elements_.pop_back();
        return top;
    }

    const T& top() const {
        if (elements_.empty()) throw std::out_of_range("Stack empty");
        return elements_.back();
    }

    bool empty() const { return elements_.empty(); }
    size_t size() const { return elements_.size(); }
};

// Usage
Stack<int> int_stack;
int_stack.push(42);
int val = int_stack.pop();

Stack<std::string> str_stack;
str_stack.push("hello");
```

### Class Template Argument Deduction (CTAD, C++17)

```cpp
// C++17: compiler deduces template arguments from constructor arguments
std::pair p(1, 3.14);           // std::pair<int, double>
std::vector v = {1, 2, 3};     // std::vector<int>
std::tuple t(1, "hello", 3.14); // std::tuple<int, const char*, double>
```

## Template Specialization

### Full Specialization
Provide a completely custom implementation for a specific type:

```cpp
// Generic version
template <typename T>
class Printer {
public:
    void print(const T& val) {
        std::cout << "Value: " << val << "\n";
    }
};

// Specialized for bool — different behavior
template <>
class Printer<bool> {
public:
    void print(const bool& val) {
        std::cout << (val ? "TRUE" : "FALSE") << "\n";
    }
};

// Specialized for pointer types
template <typename T>
class Printer<T*> {
public:
    void print(T* val) {
        if (val)
            std::cout << "Pointer to: " << *val << "\n";
        else
            std::cout << "nullptr\n";
    }
};
```

### Partial Specialization

```cpp
// Generic template
template <typename T, typename U>
class Pair {
    T first_;
    U second_;
public:
    Pair(const T& f, const U& s) : first_(f), second_(s) {}
    // ...
};

// Partial specialization: both types same
template <typename T>
class Pair<T, T> {
    T first_;
    T second_;
public:
    Pair(const T& f, const T& s) : first_(f), second_(s) {}
    bool isEqual() const { return first_ == second_; }
};

// Partial specialization: second type is pointer
template <typename T, typename U>
class Pair<T, U*> {
    // Special handling for pointer second member
};
```

## SFINAE (Substitution Failure Is Not An Error)

### The Problem SFINAE Solves
You want a function to only be available for certain types, but without causing a hard compilation error when called with the wrong type.

```cpp
#include <type_traits>

// Only available for arithmetic types (int, double, float, etc.)
template <typename T>
std::enable_if_t<std::is_arithmetic_v<T>, T>
safe_multiply(T a, T b) {
    return a * b;
}

// Usage
safe_multiply(3, 4);        // OK: int is arithmetic
safe_multiply(2.5, 4.0);    // OK: double is arithmetic
// safe_multiply("a", "b"); // SFINAE: const char* is not arithmetic — not a hard error

// Modern C++20 approach: requires clause
template <typename T>
    requires std::is_arithmetic_v<T>
T safe_multiply_v2(T a, T b) {
    return a * b;
}
```

### C++20 Concepts (Replacing SFINAE)

```cpp
#include <concepts>

// Define a concept
template <typename T>
concept Numeric = std::is_arithmetic_v<T>;

template <typename T>
concept Addable = requires(T a, T b) {
    { a + b } -> std::convertible_to<T>;
};

// Use concepts to constrain templates
template <Numeric T>
T multiply(T a, T b) { return a * b; }

template <Addable T>
T add(T a, T b) { return a + b; }

// Shorthand syntax
auto safe_divide(Numeric auto a, Numeric auto b) -> decltype(a) {
    return b != 0 ? a / b : decltype(a){};
}
```

## Variadic Templates

### The Problem Variadic Templates Solve
You need to write functions that accept any number of arguments of any type — like `std::make_unique`, `std::tuple`, or `std::printf`.

```cpp
// Base case: no arguments
void print() {}

// Recursive case: first + rest
template <typename T, typename... Args>
void print(const T& first, const Args&... rest) {
    std::cout << first;
    if constexpr (sizeof...(rest) > 0) {
        std::cout << ", ";
        print(rest...);
    }
}

print(1, "hello", 3.14, 'x');  // Output: 1, hello, 3.14, x

// Fold expressions (C++17)
template <typename... Args>
auto sum(const Args&... args) {
    return (args + ...);  // Right fold
}

auto total = sum(1, 2, 3, 4, 5);  // 15

// Parameter pack size
template <typename... Args>
constexpr size_t count_args(Args&&...) {
    return sizeof...(Args);
}
```

## Constexpr and Compile-Time Templates

```cpp
// Compile-time computation
template <size_t N>
struct Factorial {
    static constexpr size_t value = N * Factorial<N-1>::value;
};

template <>
struct Factorial<0> {
    static constexpr size_t value = 1;
};

static_assert(Factorial<5>::value == 120);

// constexpr function template
template <typename T>
constexpr T clamp(T value, T lo, T hi) {
    return (value < lo) ? lo : (value > hi) ? hi : value;
}

constexpr int x = clamp(15, 0, 10);  // 10, computed at compile time
```

## Template Metaprogramming

```cpp
// Compile-time type selection
template <bool Condition, typename TrueType, typename FalseType>
struct If {
    using type = TrueType;
};

template <typename TrueType, typename FalseType>
struct If<false, TrueType, FalseType> {
    using type = FalseType;
};

// Type list and index
template <typename... Types>
struct TypeList {};

// Get Nth type from a type list
template <size_t N, typename... Types>
struct NthType;

template <typename T, typename... Types>
struct NthType<0, T, Types...> {
    using type = T;
};

template <size_t N, typename T, typename... Types>
struct NthType<N, T, Types...> {
    using type = typename NthType<N-1, Types...>::type;
};

// Usage
using MyTypes = TypeList<int, double, std::string>;
using Second = typename NthType<1, MyTypes>::type;  // double
```

## Engineering Decision Framework

### When to Use Templates
- When writing generic algorithms (sort, search, transform)
- When building type-safe containers (vectors, maps)
- When you need zero-overhead abstractions (no virtual dispatch)
- When implementing compile-time computation
- When building libraries used across multiple types

### When NOT to Use Templates
- When runtime polymorphism is simpler (virtual functions)
- When compilation time is critical (templates increase compile time)
- When the logic is type-specific (no benefit from generics)
- When debugging template errors is costing too much time

### Alternatives to Templates
| Situation | Alternative | Trade-off |
|-----------|-------------|-----------|
| Type-erased containers | `std::function` + type erasure | Slight runtime overhead |
| Runtime-polymorphic behavior | Virtual functions | Vtable overhead but simpler errors |
| Simple macros | `#define` | No type safety, no scope |
| Compile-time config | `constexpr` variables | No type parameterization |

### Real-World Production Examples
1. **STL**: `std::vector`, `std::sort`, `std::find` — all templates
2. **Boost**: Extensive use of template metaprogramming for type-safe libraries
3. **Abseil (Google)**: Template-based utilities for production C++
4. **Folly (Facebook)**: Template-heavy concurrent data structures

### Common Mistakes

| Mistake | Consequence | Fix |
|---------|-------------|-----|
| Putting template implementations in .cpp files | Linker errors (instantiation not visible) | Keep all template code in headers |
| Overusing template specialization | Code bloat, confusing overload resolution | Prefer function overloading over specialization |
| Not constraining templates | Cryptic error messages | Use C++20 concepts or `static_assert` |
| Implicit conversions in template args | Unexpected type deductions | Use `static_cast` at call sites |
| Deep template recursion | Slow compilation, deep error messages | Use `constexpr if` or iterative approaches |

## Production Incidents

### Incident 1: Template Bloat Increasing Binary Size
**Problem**: A financial analytics library grew from 12MB to 180MB after introducing template-heavy pricing models.

**Cause**: A `PricingModel<T>` template was instantiated for 47 different types. Each instantiation generated a full copy — including ~2000 lines of validation, logging, and interpolation logic.

**Impact**: Library couldn't fit on target trading device (200MB flash). Build times went from 3 to 25 minutes.

**Solution**: Factored type-independent logic into a non-template base class. Used `extern template` to control instantiation across TUs.

---

### Incident 2: SFINAE Error Taking 6 Hours to Debug
**Problem**: A developer spent 6 hours debugging a 200-line error message from a SFINAE constraint failure.

**Cause**: A `serialize()` function template had SFINAE requiring `std::is_arithmetic_v<T>`. The error pointed into STL internals, not the actual constraint.

**Solution**: Replaced SFINAE with C++20 `requires` clauses for clear error messages.

---

### Incident 3: Unexpected Template Instantiation
**Problem**: A library header accidentally instantiated a heavy template for `int` in every translation unit that included it, causing link-time errors.

**Cause**: A function template with default arguments implicitly instantiated for `int`. Without `extern template`, each TU created its own copy.

**Solution**: Added `extern template int heavy_function<int>(int);` in the header and an explicit instantiation in a single .cpp file.

---

## Production Checklist
- [ ] Keep template implementations in header files
- [ ] Use C++20 concepts instead of SFINAE where possible
- [ ] Use `extern template` to control instantiation and reduce compile time
- [ ] Prefer function overloading over template specialization
- [ ] Use `constexpr if` (C++17) instead of SFINAE for conditional logic
- [ ] Limit template recursion depth to avoid compilation issues
- [ ] Use `static_assert` for clear compile-time error messages
- [ ] Monitor binary size when adding template-heavy code
- [ ] Use type traits for compile-time type checking
- [ ] Document template requirements in comments or concepts

## Maturity Levels

### Beginner (0-6 months)
- Write basic function and class templates
- Understand template argument deduction
- Use standard library templates (`std::vector`, `std::sort`)

### Intermediate (6-18 months)
- Implement template specialization (full and partial)
- Use SFINAE and `enable_if` for type constraints
- Write variadic templates
- Use `constexpr if` for conditional compilation

### Advanced (18+ months)
- Design template metaprogramming libraries
- Use C++20 concepts for clear constraints
- Implement type-erased containers
- Optimize template instantiation with `extern template`
- Debug complex template error messages

## Common Myths Debunked

### Myth 1: "Templates are slow because they generate code"
**Reality**: Templates generate code at compile time, not runtime. The generated code is identical to hand-written type-specific code. There is zero runtime overhead.

### Myth 2: "Templates are only for containers"
**Reality**: Templates are used for algorithms, smart pointers, function wrappers, type traits, compile-time computation, policy-based design, and much more. They are a fundamental language feature.

### Myth 3: "Template errors are impossible to understand"
**Reality**: Modern compilers (GCC 13+, Clang 17+) produce readable template errors. C++20 concepts make errors even clearer by stating exactly what constraint was violated.

### Myth 4: "You should avoid templates for compilation speed"
**Reality**: For frequently used types, templates compile fast because the compiler caches instantiation results. Use `extern template` to reduce duplicate work across translation units.

### Myth 5: "Templates and macros are similar"
**Reality**: Macros are text substitution with no type safety, no scope, and no debugging support. Templates are fully type-checked, scope-aware, and debuggable. Never use macros where templates suffice.

## One-Minute Revision

| Concept | What It Is | Why It Matters | Key Rule |
|---------|-----------|----------------|----------|
| Function Template | Generic function parameterized by type | Write once, use with any type | Template code must be in headers |
| Class Template | Generic class parameterized by type | Type-safe containers and utilities | Use CTAD (C++17) for convenience |
| Full Specialization | Custom implementation for specific type | Optimized behavior for specific types | Avoid over-specialization |
| Partial Specialization | Partial custom implementation | Handle patterns like pointer types | Specialize for pointer/reference patterns |
| SFINAE | Substitution failure is not an error | Enable/disable functions based on types | Use concepts (C++20) instead |
| Variadic Templates | Templates with variable number of args | Implement printf-like functions | Use fold expressions (C++17) |
| constexpr if | Compile-time conditional in templates | Replace SFINAE for type branching | Branches not taken are not instantiated |
| Concepts | Named template constraints (C++20) | Clear errors, self-documenting code | Define concepts for common requirements |

## Related Topics
- [Knowledge Atoms](../00-knowledge-atoms/) — Template metaprogramming foundations
- [STL](../04-stl/) — Templates powering the Standard Template Library
- [Modern C++](../08-modern-cpp/) — Concepts, constexpr if, fold expressions
- [Performance](../11-performance/) — Template overhead and optimization
- [Design Patterns](../09-design-patterns/) — Policy-based design with templates
