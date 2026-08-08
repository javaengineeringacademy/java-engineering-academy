# Best Practices — C Language

## The Problem

C code that works today becomes unmaintainable tomorrow without consistent practices. Different developers use different naming conventions, different error handling strategies, different memory management patterns. The result: a codebase that nobody wants to touch, where every change risks breaking something.

Best practices transform individual skill into team productivity. They make code readable, maintainable, and safe — regardless of who wrote it.

## What It Is

Best practices are guidelines that professional C developers follow:

| Practice | Purpose | Impact |
|----------|---------|--------|
| Naming conventions | Consistent, readable code | Faster onboarding |
| Code organization | Modular, testable code | Easier maintenance |
| Error handling | Robust, reliable code | Fewer production bugs |
| Documentation | Understandable interfaces | Faster development |
| Memory discipline | Leak-free, safe code | Fewer crashes |
| Testing | Verified correctness | Confident refactoring |

## Why It Exists

Best practices exist because:
- Code is read 10x more than it is written
- Bugs found early cost 1-10x to fix; bugs found in production cost 10-100x
- Consistent code allows teams to collaborate without friction
- Professional codebases must last years and survive many developers

### Architecture: Code Quality Hierarchy

```
┌─────────────────────────────────────┐
│        Correctness                   │ ← Does it work?
├─────────────────────────────────────┤
│        Safety                        │ ← Does it handle errors?
├─────────────────────────────────────┤
│        Readability                   │ ← Can others understand it?
├─────────────────────────────────────┤
│        Maintainability               │ ← Can it be changed safely?
├─────────────────────────────────────┤
│        Performance                   │ ← Is it fast enough?
└─────────────────────────────────────┘
```

## Expanded Code Examples

### Naming Conventions

```c
// BAD: Inconsistent, unclear names
int calc(int a, int b) { return a * b + a + b; }
void f(int x) { /* what does f do? */ }

// GOOD: Descriptive, consistent names
int calculate_total_price(int quantity, float unit_price) {
    float subtotal = quantity * unit_price;
    float tax = subtotal * TAX_RATE;
    return (int)(subtotal + tax);
}

void process_order_item(OrderItem *item) {
    item->total = item->quantity * item->unit_price;
}

// Naming convention rules:
// - Functions: lowercase_with_underscores
// - Types: PascalCase (struct Point, typedef enum Color)
// - Constants: UPPER_CASE (MAX_SIZE, PI)
// - Variables: lowercase_with_underscores (item_count, total_price)
// - Files: lowercase_with_underscores (string_utils.c)
// - Macros: UPPER_CASE (ASSERT_NULL, ARRAY_SIZE)
// - Private functions: prefix with underscore or module name (ht_insert)
```

### Error Handling Patterns

```c
#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>

// Pattern 1: Return error code (most common in C)
typedef enum { OK = 0, ERR_NOMEM, ERR_INVALID, ERR_IO } ErrorCode;

ErrorCode read_config(const char *path, Config *cfg) {
    FILE *fp = fopen(path, "r");
    if (!fp) {
        fprintf(stderr, "Cannot open %s: %s\n", path, strerror(errno));
        return ERR_IO;
    }

    if (parse_config(fp, cfg) != 0) {
        fclose(fp);
        return ERR_INVALID;
    }

    fclose(fp);
    return OK;
}

// Pattern 2: goto for cleanup (Linux kernel style)
int process_file(const char *filename) {
    FILE *fp = NULL;
    char *buffer = NULL;
    int result = -1;

    fp = fopen(filename, "r");
    if (!fp) goto cleanup;

    buffer = malloc(4096);
    if (!buffer) goto cleanup;

    if (fread(buffer, 1, 4096, fp) == 0) goto cleanup;

    result = 0;  // Success

cleanup:
    free(buffer);
    if (fp) fclose(fp);
    return result;
}

// Pattern 3: Output parameters
int divide(int a, int b, int *result) {
    if (result == NULL) return ERR_INVALID;
    if (b == 0) return ERR_INVALID;
    *result = a / b;
    return OK;
}

// Pattern 4: Error context
typedef struct {
    ErrorCode code;
    char message[256];
    int line;
    const char *file;
} Error;

void error_set(Error *err, ErrorCode code, const char *msg, int line, const char *file) {
    if (err) {
        err->code = code;
        snprintf(err->message, sizeof(err->message), "%s", msg);
        err->line = line;
        err->file = file;
    }
}
```

### Code Organization

```c
// string_utils.h — Public interface
#ifndef STRING_UTILS_H
#define STRING_UTILS_H

#include <stddef.h>

// Creates a duplicate of the input string
// Returns NULL on allocation failure
// Caller must free the returned string
char *str_duplicate(const char *src);

// Trims whitespace from both ends of a string
// Modifies the string in place
// Returns the trimmed string
char *str_trim(char *str);

// Splits a string by delimiter
// Returns NULL-terminated array of strings
// Caller must free each string and the array
char **str_split(const char *str, char delimiter, int *count);

#endif

// string_utils.c — Implementation
#include "string_utils.h"
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

// Static: file-scoped, not visible outside this file
static int is_whitespace(char c) {
    return c == ' ' || c == '\t' || c == '\n' || c == '\r';
}

// No static: visible to other files (declared in header)
char *str_duplicate(const char *src) {
    if (!src) return NULL;
    size_t len = strlen(src);
    char *dup = malloc(len + 1);
    if (!dup) return NULL;
    memcpy(dup, src, len + 1);
    return dup;
}

char *str_trim(char *str) {
    if (!str) return NULL;

    // Trim leading
    while (is_whitespace(*str)) str++;

    // Trim trailing
    char *end = str + strlen(str) - 1;
    while (end > str && is_whitespace(*end)) *end-- = '\0';

    return str;
}
```

### Documentation Standards

```c
/**
 * @brief Calculate the factorial of a non-negative integer.
 *
 * Computes n! (n factorial) iteratively. Returns -1 on overflow
 * or if n is negative.
 *
 * @param n The non-negative integer to compute factorial for
 * @return The factorial of n, or -1 on error (negative input or overflow)
 *
 * @note Maximum n for 64-bit result is 20 (20! = 2,432,902,008,176,640,000)
 *
 * @code
 * long long result = factorial(5);  // Returns 120
 * long long error = factorial(-1);  // Returns -1
 * @endcode
 *
 * @warning This function has O(n) time complexity.
 *
 * @see factorial_memo for memoized version
 */
long long factorial(int n);

/**
 * @brief Configuration structure for the application
 *
 * All fields are read-only after initialization.
 * Use config_create() to initialize and config_destroy() to free.
 */
typedef struct {
    int port;           /**< Server port (1-65535) */
    int max_conn;       /**< Maximum concurrent connections */
    int timeout_sec;    /**< Connection timeout in seconds */
    char log_path[256]; /**< Path to log file */
} Config;
```

### Memory Management Best Practices

```c
#include <stdlib.h>
#include <string.h>

// Pattern 1: RAII-style with goto
char *process_input(const char *input) {
    char *result = NULL;
    char *temp = NULL;

    result = malloc(256);
    if (!result) goto fail;

    temp = strdup(input);
    if (!temp) goto fail;

    // Process...
    snprintf(result, 256, "Processed: %s", temp);

    free(temp);  // Free intermediate
    return result;

fail:
    free(result);
    free(temp);
    return NULL;
}

// Pattern 2: Ownership transfer
typedef struct {
    char *name;
    int age;
} Person;

Person *person_create(const char *name, int age) {
    Person *p = malloc(sizeof(Person));
    if (!p) return NULL;
    p->name = strdup(name);  // Takes ownership of copy
    p->age = age;
    return p;
}

void person_destroy(Person *p) {
    if (p) {
        free(p->name);  // Free owned resources
        free(p);
    }
}

// Pattern 3: Arena allocator for batch allocation
typedef struct {
    char *memory;
    size_t offset;
    size_t size;
} Arena;

void *arena_alloc(Arena *a, size_t size) {
    size = (size + 7) & ~(size_t)7;  // Align
    if (a->offset + size > a->size) return NULL;
    void *ptr = a->memory + a->offset;
    a->offset += size;
    return ptr;
}
```

## Production Incidents

### Incident 1: Inconsistent Naming Causing Merge Conflicts

**Problem**: Two developers implement the same function with different names.

**Cause**: No naming convention document:

```c
// Developer A: string_utils.c
char *str_dup(const char *s);

// Developer B: stringutil.c
char *duplicate_string(const char *s);
```

**Solution**: Document and enforce naming conventions:

```c
// All functions: module_action_noun
char *str_duplicate(const char *src);  // In string_utils.h
char *str_trim(char *str);
int str_compare(const char *a, const char *b);
```

### Incident 2: Missing Documentation on API Change

**Problem**: Library upgrade breaks all consumers because function behavior changed.

**Cause**: No documentation on return values and side effects:

```c
// Before: returned 0 on success
// After: returns 0 on success, negative on warning (breaking change)
int process_data(const char *input);
```

**Solution**: Document all public interfaces with Doxygen:

```c
/**
 * @brief Process input data.
 * @return 0 on success, negative on error.
 * @warning Previous versions returned 0 on warning — updated in v2.0.
 */
int process_data(const char *input);
```

## Production Checklist

- [ ] Follow consistent naming conventions
- [ ] Document all public interfaces (Doxygen)
- [ ] Handle all error cases (no ignored return values)
- [ ] Use `static` for file-scoped functions
- [ ] Separate interface (`.h`) from implementation (`.c`)
- [ ] Use `const` for read-only parameters
- [ ] Write functions with single responsibility
- [ ] Keep functions under 50 lines
- [ ] Keep files under 500 lines
- [ ] Use version control for all changes
- [ ] Review code before merging
- [ ] Write tests for new functionality

## Maturity Levels

| Level | Description | Indicators |
|-------|-------------|------------|
| **Beginner** | Follows basic style rules | Consistent indentation, naming |
| **Intermediate** | Writes documented, error-handled code | Uses error codes, documents interfaces |
| **Advanced** | Maintains professional codebase | Code reviews, automated testing, CI/CD |
| **Expert** | Defines standards, mentors teams | Style guides, architecture reviews |

## Common Myths Debunked

1. **Myth**: Code comments are always good
   **Truth**: Comments should explain *why*, not *what*. Good code is self-documenting; comments explain non-obvious decisions.

2. **Myth**: Shorter code is better
   **Truth**: Readable code is better than clever code. A 10-line function that is clear beats a 3-line function that is obscure.

3. **Myth**: You should always optimize for performance
   **Truth**: Optimize for readability first. Only optimize hot paths after profiling proves they are bottlenecks.

4. **Myth**: Tools can enforce all best practices
   **Truth**: Tools help (linters, formatters, static analysis), but code review and team discipline are essential.

## One-Minute Revision

| Practice | Description | Key Detail |
|----------|-------------|------------|
| Naming | Descriptive, consistent names | `module_action_noun` convention |
| Comments | Explain why, not what | Document decisions, not code |
| Functions | Single responsibility | Under 50 lines, one task |
| Error handling | Check all return values | Use goto cleanup, error codes |
| Memory | Always free allocated memory | RAII-style with goto |
| Testing | Write tests for all code | Unit, integration, fuzz |
| Documentation | Document public interfaces | Doxygen comments |

## Related Topics

- [Security](../11-security/README.md) — Secure coding practices
- [Testing](../13-testing/README.md) — Writing testable code
- [Build Systems](../14-build-systems/README.md) — Enforcing standards through build flags
