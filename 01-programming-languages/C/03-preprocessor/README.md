# Preprocessor — C Language

## The Problem

Without a preprocessor, you would need to copy-paste code across files, manually create platform-specific builds, and maintain multiple versions of the same logic. The preprocessor solves three fundamental problems:

1. **Code reuse**: `#include` lets you share declarations across files
2. **Platform independence**: `#ifdef` lets you write one codebase for multiple platforms
3. **Compile-time computation**: `#define` macros enable zero-cost abstractions

The preprocessor is not part of the C language — it is a separate text-processing engine that runs before the compiler. This distinction is critical: the preprocessor does not understand C syntax, types, or scope.

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
