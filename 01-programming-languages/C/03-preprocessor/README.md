# Preprocessor — C Language

## What it is
The preprocessor processes source code before compilation, performing text substitution and conditional compilation.

## Why it exists
To enable code reuse, conditional compilation, and macro definitions.

## When to use it
Whenever you need code flexibility, platform independence, or compile-time computation.

## How it works

### File Inclusion

```c
#include <stdio.h>    // System header
#include "myheader.h" // Local header
```

### Macro Definitions

```c
#define PI 3.14159
#define MAX(a, b) ((a) > (b) ? (a) : (b))
```

### Conditional Compilation

```c
#ifdef DEBUG
    printf("Debug mode\n");
#endif

#if defined(PLATFORM_WINDOWS)
    // Windows code
#elif defined(PLATFORM_LINUX)
    // Linux code
#endif
```

### Include Guards

```c
#ifndef MYHEADER_H
#define MYHEADER_H

// header content

#endif
```

### Predefined Macros

```c
__FILE__    // Current file name
__LINE__    // Current line number
__DATE__    // Compilation date
__TIME__    // Compilation time
__func__    // Current function name
```

### Stringification and Concatenation

```c
#define STRINGIFY(x) #x
#define CONCAT(a, b) a##b

printf("%s\n", STRINGIFY(hello));  // "hello"
```

## Production Checklist

- [ ] Always use include guards
- [ ] Use parentheses in macros
- [ ] Prefer inline functions over macros
- [ ] Use #pragma once or include guards
- [ ] Document complex macros

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Uses #include and simple #define |
| Intermediate | Uses conditional compilation |
| Advanced | Writes complex macros and meta-programming |

## Common Myths

1. **Myth**: Macros are just like functions
   **Truth**: Macros are text substitution and can cause side effects

2. **Myth**: #define creates a variable
   **Truth**: #define creates a text replacement rule

## One-Minute Revision

| Directive | Purpose |
|-----------|---------|
| #include | Include file |
| #define | Define macro |
| #undef | Undefine macro |
| #ifdef | If defined |
| #ifndef | If not defined |
| #if | Conditional |
| #elif | Else if |
| #else | Else |
| #endif | End conditional |
| #pragma | Compiler directive |

## Related Topics

- [Build Systems](../14-build-systems/README.md)
- [Best Practices](../15-best-practices/README.md)
