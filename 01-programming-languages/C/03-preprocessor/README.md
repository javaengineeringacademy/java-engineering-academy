# Preprocessor — C Language

## Why It Matters

When you're building cross-platform software or large C projects, you need code reuse across files, platform-independent builds, and compile-time abstractions — without runtime cost. The preprocessor solves these by letting you share declarations with `#include`, write one codebase for multiple platforms with `#ifdef`, and define constants with `#define`. It's a separate text-processing engine that runs before the compiler, not part of C syntax itself.

## Engineering Decision Framework

| Factor | Use This | Consider Alternatives |
|--------|----------|----------------------|
| When to use | Platform-specific code, compile-time constants, include guards | Inline functions for type-safe macros |
| When NOT to use | Complex logic in macros (use inline functions instead) | Macros have no type checking or scope |
| Alternatives | C11 `_Generic`, C99 inline functions, build-system flags | More type safety, less portability |
| Production Examples | Linux kernel `#ifdef CONFIG_*`, header include guards | Every major C project uses preprocessor |
| Common Mistakes | Unparenthesized macro args, missing include guards, side-effect macros | Always parenthesize: `#define MAX(a,b) ((a)>(b)?(a):(b))` |

## What It Is

The preprocessor performs four operations on your source code before compilation:

| Operation | Directive | Purpose |
|-----------|-----------|---------|
| File inclusion | `#include` | Insert contents of another file |
| Macro definition | `#define` | Text replacement rule |
| Conditional compilation | `#ifdef`, `#if`, `#elif`, `#else` | Include/exclude code blocks |
| Compiler hints | `#pragma`, `#error`, `#warning` | Control compiler behavior |

## Why It Exists

C was designed in 1972 when compilers were simple text processors. The preprocessor allowed:
- Sharing header files across multiple `.c` files (the "include" model)
- Writing one codebase that compiles on different platforms
- Defining constants and macros without runtime overhead
- Embedding compile-time metadata (`__FILE__`, `__LINE__`)

Today, the preprocessor remains essential because:
- Conditional compilation is the only way to write platform-specific C code
- Include guards are the only way to prevent multiple header inclusion
- Macros enable zero-cost abstractions (type-generic code, compile-time constants)

## Expanded Code Examples

### File Inclusion — The Include Model

```c
// config.h — Configuration header
#ifndef CONFIG_H
#define CONFIG_H

#define MAX_CONNECTIONS 1024
#define VERSION "2.1.0"

#ifdef DEBUG
    #define LOG_LEVEL 3
#else
    #define LOG_LEVEL 0
#endif

#endif  // CONFIG_H

// network.c — Implementation file
#include <stdio.h>
#include "config.h"   // Local header
#include "network.h"  // Project header

void init_network(void) {
    printf("Initializing %d connections (v%s)\n",
           MAX_CONNECTIONS, VERSION);
    #if LOG_LEVEL > 0
    printf("Debug logging enabled\n");
    #endif
}
```

### Macro Definitions — Beyond Simple Constants

```c
#include <stdio.h>

// Simple constant
#define PI 3.14159265358979323846
#define MAX(a, b) ((a) > (b) ? (a) : (b))
#define MIN(a, b) ((a) < (b) ? (a) : (b))

// Stringify: convert token to string literal
#define STRINGIFY(x) #x
#define TOSTRING(x) STRINGIFY(x)

// Token paste: concatenate tokens
#define CONCAT(a, b) a##b
#define MAKE_VAR(name) int CONCAT(var_, name)

// Variadic macro (C99)
#define LOG(fmt, ...) fprintf(stderr, "[%s:%d] " fmt "\n", \
    __FILE__, __LINE__, ##__VA_ARGS__)

// Type-generic macro (pre-C11 _Generic)
#define PRINT(x) _Generic((x), \
    int: printf("%d\n", (int)(x)), \
    double: printf("%f\n", (double)(x)), \
    char*: printf("%s\n", (char*)(x)), \
    default: printf("unknown type\n") \
)

// Multi-line macro with do-while(0)
#define SWAP(a, b) do { \
    typeof(a) temp = (a); \
    (a) = (b); \
    (b) = temp; \
} while(0)

int main(void) {
    printf("PI = %f\n", PI);
    printf("MAX(3,5) = %d\n", MAX(3, 5));

    // Stringify usage
    printf("Value of PI: %s\n", TOSTRING(PI));

    // Variadic macro
    LOG("Connection count: %d", 1024);

    // Swap macro
    int x = 1, y = 2;
    SWAP(x, y);
    printf("x=%d, y=%d\n", x, y);  // x=2, y=1

    return 0;
}
```

### Conditional Compilation — Platform Independence

```c
#include <stdio.h>

// Platform detection
#if defined(_WIN32)
    #define PLATFORM_NAME "Windows"
    #define PATH_SEPARATOR "\\"
    #include <windows.h>
    #define SLEEP(ms) Sleep(ms)
#elif defined(__APPLE__)
    #define PLATFORM_NAME "macOS"
    #define PATH_SEPARATOR "/"
    #include <unistd.h>
    #define SLEEP(ms) usleep((ms) * 1000)
#elif defined(__linux__)
    #define PLATFORM_NAME "Linux"
    #define PATH_SEPARATOR "/"
    #include <unistd.h>
    #define SLEEP(ms) usleep((ms) * 1000)
#else
    #error "Unsupported platform"
#endif

// Feature detection
#if __STDC_VERSION__ >= 201112L
    #define HAS_C11 1
#else
    #define HAS_C11 0
#endif

// Debug vs Release
#ifdef NDEBUG
    #define DEBUG_LOG(...)
#else
    #define DEBUG_LOG(fmt, ...) \
        fprintf(stderr, "[DEBUG] " fmt "\n", ##__VA_ARGS__)
#endif

// Compile-time assertion (C11)
_Static_assert(sizeof(int) >= 4, "int must be at least 32 bits");
```

### Include Guards — Preventing Multiple Inclusion

```c
// option 1: Traditional include guard (portable)
#ifndef NETWORK_H
#define NETWORK_H

// header content

#endif  // NETWORK_H

// option 2: #pragma once (non-standard but widely supported)
#pragma once

// header content

// Recommendation: use both for maximum compatibility
#ifndef NETWORK_H
#define NETWORK_H
#pragma once

// header content

#endif  // NETWORK_H
```

### Predefined Macros — Compile-Time Metadata

```c
#include <stdio.h>

void log_error(const char *msg) {
    fprintf(stderr, "ERROR in %s at %s:%d (%s): %s\n",
            __func__,           // Current function name
            __FILE__,           // Source file name
            __LINE__,           // Current line number
            __DATE__ " " __TIME__, // Compilation timestamp
            msg);
}

// Use with _Static_assert for compile-time checks
#define ASSERT_SIZE(type, expected) \
    _Static_assert(sizeof(type) == expected, \
                   #type " must be " #expected " bytes")

ASSERT_SIZE(int, 4);
ASSERT_SIZE(double, 8);
```

### X-Macros — Code Generation Pattern

```c
#include <stdio.h>

// X-Macro: define data once, use everywhere
#define COLOR_LIST \
    X(RED,   0xFF0000) \
    X(GREEN, 0x00FF00) \
    X(BLUE,  0x0000FF) \
    X(WHITE, 0xFFFFFF)

// Generate enum
enum Color {
    #define X(name, value) name = value,
    COLOR_LIST
    #undef X
};

// Generate name lookup
const char *color_name(enum Color c) {
    switch (c) {
        #define X(name, value) case name: return #name;
        COLOR_LIST
        #undef X
        default: return "UNKNOWN";
    }
}

// Generate array of all colors
enum Color all_colors[] = {
    #define X(name, value) name,
    COLOR_LIST
    #undef X
};

int main(void) {
    printf("RED = 0x%06X\n", RED);
    printf("Color name: %s\n", color_name(GREEN));
    return 0;
}
```

## Production Incidents

### Incident 1: Macro Side Effects

**Problem**: A macro that evaluates its argument multiple times causes subtle bugs.

```c
#define MAX(a, b) ((a) > (b) ? (a) : (b))

int arr[] = {1, 5, 3, 9, 2};
int idx = 0;
int result = MAX(arr[idx++], arr[3]);
// idx is incremented TWICE — undefined behavior in C
```

**Solution**: Use inline functions (C99) or add parentheses and `do-while(0)`:

```c
// Inline function (type-safe, no side effects)
static inline int max_int(int a, int b) {
    return (a > b) ? a : b;
}

// Or: document the macro limitation
// WARNING: Arguments must not have side effects
#define MAX(a, b) ((a) > (b) ? (a) : (b))
```

### Incident 2: Missing Include Guard

**Problem**: A header file without include guards causes "redefinition" errors when included multiple times.

```c
// config.h (no include guard)
#define MAX_SIZE 1024
struct Config { int timeout; };

// a.h includes config.h
// b.h includes config.h
// main.c includes a.h and b.h → "MAX_SIZE redefined", "Config redefined"
```

**Solution**: Always use include guards:

```c
#ifndef CONFIG_H
#define CONFIG_H
#define MAX_SIZE 1024
struct Config { int timeout; };
#endif
```

## Production Checklist

- [ ] Every header file has include guards (`#ifndef`/`#define`/`#endif` or `#pragma once`)
- [ ] Macro arguments are fully parenthesized: `#define SQUARE(x) ((x) * (x))`
- [ ] Multi-line macros use `do { ... } while(0)` wrapper
- [ ] Inline functions used instead of macros when type safety matters
- [ ] `__VA_ARGS__` macros use `##` to handle empty argument lists
- [ ] Platform-specific code uses `#ifdef` with clear feature detection
- [ ] Complex macros are documented with usage examples
- [ ] Compile-time assertions validate platform assumptions

## Maturity Levels

| Level | Description | Indicators |
|-------|-------------|------------|
| **Beginner** | Uses `#include` and simple `#define` | Writes basic headers, uses constants |
| **Intermediate** | Uses conditional compilation, include guards | Writes portable code, uses `#ifdef` |
| **Advanced** | Writes complex macros, X-macros, type-generic code | Uses `_Generic`, variadic macros, token pasting |
| **Expert** | Designs macro libraries, understands preprocessor limitations | Writes macro-heavy libraries, documents edge cases |

## Common Myths Debunked

1. **Myth**: Macros are just like functions
   **Truth**: Macros are text substitution — they have no type checking, no scope, and can cause side effects when arguments are evaluated multiple times. Use inline functions when possible.

2. **Myth**: `#define` creates a variable
   **Truth**: `#define` creates a text replacement rule. The preprocessor replaces every occurrence of the name with the replacement text before compilation. There is no variable, no type, and no storage.

3. **Myth**: `#include` is always slow
   **Truth**: `#include` is fast when headers use include guards. The preprocessor skips already-included headers immediately. The cost is minimal compared to compilation.

4. **Myth**: You should never use macros
   **Truth**: Macros are essential for conditional compilation, stringification, token pasting, and type-generic code. Use inline functions for computation; use macros for code generation.

## One-Minute Revision

| Directive | Purpose | Key Detail |
|-----------|---------|------------|
| `#include` | Include file | `<>` for system, `""` for local |
| `#define` | Define macro | Text replacement, not a variable |
| `#undef` | Undefine macro | Remove a previous `#define` |
| `#ifdef` | If defined | Conditional compilation |
| `#ifndef` | If not defined | Include guards |
| `#if` | Conditional | `#if EXPR` |
| `#elif` | Else if | `#elif EXPR` |
| `#else` | Else | Default branch |
| `#endif` | End conditional | Closes `#if`/`#ifdef` block |
| `#pragma` | Compiler directive | `#pragma once`, `#pragma GCC diagnostic` |
| `#error` | Compile error | `#error "Unsupported platform"` |
| `#warning` | Compile warning | `#warning "Deprecated function"` |

## Related Topics

- [Build Systems](../14-build-systems/README.md) — How make and CMake use preprocessor directives
- [Best Practices](../15-best-practices/README.md) — Coding standards for macro usage
- [Fundamentals](../01-fundamentals/README.md) — Basic C concepts

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Macro side effects (arguments evaluated multiple times) | `gcc -E` preprocessor output | Run `gcc -E file.c` to expand macros and inspect replacement text for unexpected evaluation |
| Missing include guard causing redefinition errors | `grep -r "ifndef.*_H"` | Search headers for include guards; add `#ifndef`/`#define`/`#endif` if missing |
| Conditional compilation producing wrong build | `gcc -dM -E file.c` | Print all predefined macros to verify `#ifdef` conditions match expected platform defines |
| Token pasting producing unexpected identifiers | `gcc -E` + manual inspection | Expand macros and verify concatenation produces the intended token names |
| Variadic macro argument mismatch | `-Wvariadic-macros` | Enable compiler warnings for variadic macro usage; check `__VA_ARGS__` expansion |

## Code Review Checklist

- [ ] Every header file has include guards (`#ifndef`/`#define`/`#endif` or `#pragma once`)
- [ ] Multi-line macros wrapped in `do { ... } while(0)` for statement safety
- [ ] All macro arguments fully parenthesized: `#define SQ(x) ((x)*(x))`
- [ ] Inline functions preferred over macros when type safety matters
- [ ] Complex macros documented with usage examples and edge cases
- [ ] Platform-specific code uses `#ifdef` with clear feature detection, not platform names
- [ ] `_Static_assert` validates platform assumptions at compile time

## Architecture Considerations

The preprocessor is a text-processing engine that runs before compilation. It is the only mechanism for conditional compilation in C and remains essential for cross-platform code, include guards, and type-generic abstractions. However, macros lack type checking and scope — prefer inline functions for computation and reserve macros for code generation, conditional compilation, and stringification.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| X-Macro | Code generation from shared data | Reduces duplication but hurts readability for newcomers |
| Include guard (`#ifndef`) | Preventing multiple inclusion | Portable but verbose; `#pragma once` is simpler but non-standard |
| Type-generic macro (`_Generic`) | Type-safe abstractions | More type-safe than traditional macros but only available from C11 |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Macro injection via header inclusion | Unintended code execution if headers are attacker-controlled | Validate header paths; use `-I` only for trusted directories |
| `#include` of untrusted headers | Code injection through malicious header files | Restrict include paths; audit third-party headers |
| Preprocessor-based data exfiltration | Leaking build environment details via `__FILE__` in release builds | Strip debug info in release builds; use `-DNDEBUG` |

## Evolution & Modernization

| Era | Change | Migration Path |
|-----|--------|----------------|
| C89 → C99 | Added `//` comments, variadic macros (`__VA_ARGS__`), `_Pragma` | Adopt `//` comments; use `##__VA_ARGS__` for empty args |
| C99 → C11 | Added `_Generic`, `_Static_assert`, `__has_include` | Use `_Generic` for type-generic code; use `__has_include` for feature detection |
| C11 → C23 | Added `#embed` binary inclusion, improved `constexpr` | Use `#embed` for embedding binary data; adopt `constexpr` for compile-time constants |

## Version Validation

| Feature | C Standard | Status |
|---------|-----------|--------|
| `//` single-line comments | C99 | Standard — universally supported |
| Variadic macros (`__VA_ARGS__`) | C99 (standardized in C99, widely available earlier) | Standard — use `##__VA_ARGS__` for empty lists |
| `_Generic` selection | C11 | Standard — replaces pre-C11 type-generic macros |
| `__has_include` | C23 (available as extension in GCC/Clang earlier) | Use for conditional header inclusion |

## Interview Questions

1. **What is the difference between a macro and an inline function?**: Macros are text substitution with no type checking, no scope, and potential side effects (arguments evaluated multiple times). Inline functions are type-safe, have proper scope, and the compiler handles optimization. Use macros for code generation and conditional compilation; use inline functions for computation.
2. **Why wrap multi-line macros in `do { ... } while(0)`?**: Without the wrapper, using a macro inside an `if` without braces causes the `else` to bind to the wrong statement. `do { ... } while(0)` creates a single statement that is safe in all control flow contexts.
3. **What is the X-Macro pattern?**: X-Macros define data once (e.g., a list of enum values) and generate multiple outputs (enums, name strings, arrays) by redefining the `X` macro before each inclusion. This eliminates duplication but requires understanding of macro expansion order.
4. **How does `_Generic` improve type safety?**: `_Generic` selects expressions based on the type of a controlling expression, enabling type-safe macros. For example, `print(x)` can dispatch to `printf("%d", x)` for `int` or `printf("%s", x)` for `char*` without runtime overhead.
5. **Why should you prefer `#pragma once` over `#ifndef` guards?**: `#pragma once` is simpler (no need to choose unique guard names) and faster (compiler skips the file immediately). However, it is non-standard. For maximum portability, use both `#pragma once` and `#ifndef` guards.

## References

- [C Standard (N3220)](https://www.open-std.org/jtc1/sc22/wg14/www/docs/n3220.pdf)
- [The Preprocessor — GNU C Manual](https://gcc.gnu.org/onlinedocs/gcc/Preprocessor.html)
- [Secure Coding in C and CERT C Coding Standard](https://wiki.sei.cmu.edu/confluence/display/c/)
