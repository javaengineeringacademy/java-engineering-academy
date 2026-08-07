# Best Practices — C Language

## What it is
Best practices are guidelines for writing maintainable, efficient, and safe C code.

## Why it exists
To ensure code quality, readability, and long-term maintainability.

## When to use it
Always. Following best practices leads to professional-quality code.

## How it works

### Code Style

```c
// Use descriptive names
int calculate_total_price(int quantity, float unit_price);

// Consistent formatting
if (condition) {
    do_something();
} else {
    do_something_else();
}
```

### Error Handling

```c
int process_file(const char *filename) {
    FILE *fp = fopen(filename, "r");
    if (fp == NULL) {
        perror("Error opening file");
        return -1;
    }

    // Process file

    if (fclose(fp) != 0) {
        perror("Error closing file");
        return -1;
    }

    return 0;
}
```

### Documentation

```c
/**
 * Calculate the factorial of a non-negative integer.
 * @param n The number to calculate factorial for
 * @return The factorial of n, or -1 on overflow
 */
long long factorial(int n);
```

### Code Organization

```c
// header.h - Interface
#ifndef MYMODULE_H
#define MYMODULE_H

int public_function(void);

#endif

// mymodule.c - Implementation
#include "mymodule.h"

static void helper(void) { /* ... */ }

int public_function(void) { /* ... */ }
```

### Memory Management

```c
void process(void) {
    char *buffer = malloc(1024);
    if (buffer == NULL) {
        return;
    }

    // Use buffer

    free(buffer);
    buffer = NULL;
}
```

## Production Checklist

- [ ] Follow coding standards
- [ ] Document public interfaces
- [ ] Handle all error cases
- [ ] Write readable code
- [ ] Use version control

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Follows basic style rules |
| Intermediate | Writes documented, error-handled code |
| Advanced | Maintains professional codebase |

## Common Myths

1. **Myth**: Code comments are always good
   **Truth**: Comments should explain why, not what

2. **Myth**: Shorter code is better
   **Truth**: Readable code is better than clever code

## One-Minute Revision

| Practice | Description |
|----------|-------------|
| Naming | Descriptive, consistent names |
| Comments | Explain why, not what |
| Functions | Single responsibility |
| Error handling | Check all return values |
| Memory | Always free allocated memory |
| Testing | Write tests for all code |
| Documentation | Document public interfaces |

## Related Topics

- [Security](../11-security/README.md)
- [Testing](../13-testing/README.md)
