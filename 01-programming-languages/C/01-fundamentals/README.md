# Fundamentals — C Language

## The Problem

Every C program, from a 20-line embedded firmware to a 10-million-line operating system, is built from the same atoms: variables, operators, control flow, functions, arrays, strings, pointers, and memory basics. Without mastery of these, you cannot write correct C code — you will fight syntax errors, undefined behavior, and subtle bugs that experienced developers avoid instinctively.

The fundamentals are not just syntax — they are the mental model for how C thinks about data and execution.

## What It Is

The fundamentals module covers the complete set of building blocks that every C program uses:

- **Variables**: Named storage with specific types
- **Operators**: Symbols that perform operations on values
- **Control Flow**: Decision-making and loop constructs
- **Functions**: Reusable blocks of code with clear interfaces
- **Arrays**: Fixed-size collections of same-type elements
- **Strings**: Null-terminated character arrays
- **Pointers**: Variables that store memory addresses
- **Memory Basics**: Stack vs heap allocation

## Why It Exists

C was designed as a "portable assembly language" — giving programmers direct control over hardware while maintaining readability. Every concept in this module maps to a hardware reality:

| C Concept | Hardware Reality |
|-----------|-----------------|
| Variable | Memory location |
| Pointer | Memory address |
| Array | Contiguous memory block |
| Function | Stack frame + call instruction |
| `sizeof` | Actual memory consumed |

Understanding this mapping is what makes C uniquely powerful — and uniquely dangerous.

## Expanded Code Examples

### Variables and Types

```c
#include <stdio.h>
#include <stdint.h>
#include <limits.h>

int main(void) {
    // Fixed-width types (preferred for portability)
    int8_t   a = -128;
    uint8_t  b = 255;
    int32_t  c = 2000000000;
    uint64_t d = 18446744073709551615ULL;

    // Platform-dependent types (avoid in portable code)
    int      e = 42;        // At least 16 bits, usually 32
    long     f = 100000L;   // At least 32 bits
    size_t   g = sizeof(e); // Unsigned, platform-dependent

    // Constants
    const int MAX_SIZE = 1024;      // Read-only variable
    #define BUFFER_SIZE 4096        // Text replacement (no type info)

    // Type limits
    printf("int range: %d to %d\n", INT_MIN, INT_MAX);
    printf("uint8_t range: 0 to %u\n", UINT8_MAX);

    return 0;
}
```

### Operators in Practice

```c
#include <stdio.h>

int main(void) {
    int a = 10, b = 3;

    // Arithmetic
    printf("%d + %d = %d\n", a, b, a + b);    // 13
    printf("%d - %d = %d\n", a, b, a - b);    // 7
    printf("%d * %d = %d\n", a, b, a * b);    // 30
    printf("%d / %d = %d\n", a, b, a / b);    // 3 (integer division)
    printf("%d %% %d = %d\n", a, b, a % b);   // 1 (remainder)

    // Bitwise (critical for systems programming)
    unsigned int flags = 0xFF;
    flags &= ~(1 << 3);        // Clear bit 3
    flags |= (1 << 5);         // Set bit 5
    int bit3 = (flags >> 3) & 1;  // Read bit 3

    // Comparison and logical
    int x = 5, y = 10;
    int is_between = (x > 0) && (x < y);  // 1 (true)

    // Ternary operator
    int max = (a > b) ? a : b;

    // Comma operator (use sparingly)
    int i, j;
    for (i = 0, j = 10; i < j; i++, j--) {
        // i goes up, j goes down
    }

    return 0;
}
```

### Control Flow — Real Patterns

```c
#include <stdio.h>
#include <stdbool.h>

// Guard clauses — early returns reduce nesting
int process_data(int *data, int size) {
    if (data == NULL) return -1;
    if (size <= 0) return -2;
    if (size > 1000) return -3;

    for (int i = 0; i < size; i++) {
        if (data[i] < 0) continue;  // Skip negatives
        printf("%d\n", data[i]);
    }
    return 0;
}

// Switch with fall-through (intentional)
void print_month(int month) {
    switch (month) {
        case 12: case 1: case 2:
            printf("Winter\n");
            break;
        case 3: case 4: case 5:
            printf("Spring\n");
            break;
        case 6: case 7: case 8:
            printf("Summer\n");
            break;
        case 9: case 10: case 11:
            printf("Autumn\n");
            break;
        default:
            printf("Invalid month\n");
            break;
    }
}

// Finite state machine pattern
typedef enum { STATE_IDLE, STATE_RUNNING, STATE_DONE } State;

State process(State current, int input) {
    switch (current) {
        case STATE_IDLE:
            return (input == 1) ? STATE_RUNNING : STATE_IDLE;
        case STATE_RUNNING:
            return (input == 0) ? STATE_DONE : STATE_RUNNING;
        case STATE_DONE:
            return STATE_IDLE;
        default:
            return STATE_IDLE;
    }
}
```

### Functions — Interfaces and Error Handling

```c
#include <stdio.h>
#include <stdbool.h>

// Error codes as return values (C convention)
typedef enum {
    OK = 0,
    ERR_NULL_PTR = -1,
    ERR_OUT_OF_RANGE = -2,
    ERR_NO_MEMORY = -3
} ErrorCode;

// Clear function interface
ErrorCode divide(double a, double b, double *result) {
    if (result == NULL) return ERR_NULL_PTR;
    if (b == 0.0) return ERR_OUT_OF_RANGE;

    *result = a / b;
    return OK;
}

// Usage
int main(void) {
    double result;
    ErrorCode err = divide(10.0, 3.0, &result);

    switch (err) {
        case OK:
            printf("Result: %f\n", result);
            break;
        case ERR_NULL_PTR:
            fprintf(stderr, "Internal error\n");
            break;
        case ERR_OUT_OF_RANGE:
            fprintf(stderr, "Division by zero\n");
            break;
    }
    return (err == OK) ? 0 : 1;
}
```

### Arrays — Beyond Basic

```c
#include <stdio.h>
#include <string.h>

int main(void) {
    // Array initialization
    int matrix[3][4] = {
        {1, 2, 3, 4},
        {5, 6, 7, 8},
        {9, 10, 11, 12}
    };

    // Array as function parameter (decays to pointer)
    // void process(int arr[], int size);  // Same as int *arr
    // void process(int arr[10], int size); // Size is ignored

    // Variable-length arrays (C99, optional in C11+)
    int n = 10;
    int vla[n];  // Stack-allocated, limited size

    // Array bounds — C does NOT check them
    int arr[5] = {1, 2, 3, 4, 5};
    // arr[5] = 10;  // Undefined behavior! Writes past end

    // Safe array access pattern
    #define ARRAY_SIZE(arr) (sizeof(arr) / sizeof((arr)[0]))
    for (size_t i = 0; i < ARRAY_SIZE(arr); i++) {
        printf("arr[%zu] = %d\n", i, arr[i]);
    }

    return 0;
}
```

### Strings — The Null-Terminated Reality

```c
#include <stdio.h>
#include <string.h>

int main(void) {
    // String literals (stored in read-only memory)
    const char *literal = "Hello, World!";

    // Character arrays (modifiable)
    char buffer[64];
    strcpy(buffer, literal);     // Copies characters + null terminator

    // String functions (all potentially unsafe without bounds)
    size_t len = strlen(buffer);          // Length excluding null
    char *found = strchr(buffer, 'W');    // Find character
    char *pos = strstr(buffer, "World");  // Find substring

    // SAFE string handling — always use bounded versions
    char dest[32];
    strncpy(dest, literal, sizeof(dest) - 1);
    dest[sizeof(dest) - 1] = '\0';  // Ensure null termination

    // snprintf — the gold standard for safe string formatting
    char msg[128];
    int written = snprintf(msg, sizeof(msg), "Hello, %s! Length: %zu",
                           literal, len);
    if (written >= (int)sizeof(msg)) {
        // Truncation occurred
        fprintf(stderr, "String was truncated\n");
    }

    // String as array of chars
    char word[] = {'H', 'i', '!', '\0'};  // Explicit null terminator
    printf("word: %s (len: %zu)\n", word, strlen(word));

    return 0;
}
```

### Pointers — The Power and the Danger

```c
#include <stdio.h>
#include <stdlib.h>

int main(void) {
    // Pointer basics
    int x = 42;
    int *p = &x;       // p points to x

    printf("x = %d\n", x);         // 42
    printf("*p = %d\n", *p);       // 42 (dereference)
    printf("p = %p\n", (void *)p); // Memory address

    // Pointer arithmetic
    int arr[] = {10, 20, 30, 40, 50};
    int *start = arr;          // Arrays decay to pointers
    int *end = arr + 5;       // One past the last element (legal to compute, not dereference)

    while (start < end) {
        printf("%d ", *start);
        start++;  // Moves to next int (next 4 bytes typically)
    }
    printf("\n");

    // Dynamic allocation
    int *heap_arr = malloc(10 * sizeof(int));
    if (heap_arr == NULL) {
        fprintf(stderr, "Allocation failed\n");
        return 1;
    }

    for (int i = 0; i < 10; i++) {
        heap_arr[i] = i * i;
    }

    free(heap_arr);
    heap_arr = NULL;  // Prevent dangling pointer

    return 0;
}
```

## Production Incidents

### Incident 1: Integer Overflow in Buffer Size Calculation

**Problem**: A network service allocates buffers based on a user-supplied count, causing heap overflow.

**Cause**: Multiplication overflows without checking:

```c
void process_items(uint32_t count) {
    // If count = 0x40000001, count * sizeof(int) overflows to 4
    int *items = malloc(count * sizeof(int));  // Tiny allocation
    for (uint32_t i = 0; i < count; i++) {
        items[i] = i;  // Heap buffer overflow
    }
}
```

**Impact**: Remote code execution via heap overflow. CVSS 9.8.

**Solution**: Check for overflow before allocating:

```c
#include <stdint.h>
#include <stddef.h>

int process_items(uint32_t count) {
    if (count > SIZE_MAX / sizeof(int)) {
        return -1;  // Would overflow
    }
    int *items = malloc(count * sizeof(int));
    if (items == NULL) return -2;
    // ... safe to use
    free(items);
    return 0;
}
```

**Prevention**: Compile with `-fsanitize=undefined`, use safe multiplication helpers, validate all user input before allocation.

---

### Incident 2: Dangling Pointer After Stack Return

**Problem**: A function returns a pointer to a local variable, causing intermittent crashes.

```c
int *get_value(void) {
    int local = 42;
    return &local;  // Returns address of stack variable
}

int main(void) {
    int *p = get_value();
    printf("%d\n", *p);  // Undefined behavior: stack frame overwritten
    return 0;
}
```

**Solution**: Allocate on the heap or use static storage:

```c
// Option 1: Heap allocation (caller must free)
int *get_value(void) {
    int *p = malloc(sizeof(int));
    if (p) *p = 42;
    return p;
}

// Option 2: Static (persists for program lifetime)
int *get_value(void) {
    static int val = 42;
    return &val;
}

// Option 3: Caller provides buffer
void get_value(int *out) {
    *out = 42;
}
```

## Production Checklist

- [ ] All variables initialized before first use
- [ ] Array bounds checked before access
- [ ] Null pointers checked before dereferencing
- [ ] Return values from library functions checked
- [ ] Compiler warnings enabled (`-Wall -Wextra -Werror`)
- [ ] Fixed-width types (`int32_t`, `uint64_t`) used for portability
- [ ] `snprintf` used instead of `sprintf`
- [ ] `strncpy` or `strlcpy` used instead of `strcpy`
- [ ] `free` called for every `malloc`, pointer set to `NULL`
- [ ] Integer overflow checked before arithmetic
- [ ] String literals stored in `const char *`

## Maturity Levels

| Level | Description | Indicators |
|-------|-------------|------------|
| **Beginner** | Can write simple programs with variables and loops | Understands basic types, if/else, for/while |
| **Intermediate** | Uses functions, arrays, and basic pointers | Writes modular code, handles arrays, uses `*` and `&` |
| **Advanced** | Manages memory, understands pointer arithmetic | Uses `malloc`/`free`, understands decay, writes safe code |
| **Expert** | Designs APIs, prevents undefined behavior, mentors others | Writes production-quality C, uses sanitizers, understands the standard |

## Common Myths Debunked

1. **Myth**: C is outdated
   **Truth**: C powers operating systems, embedded systems, and performance-critical applications. The Linux kernel had over 10,000 commits in 2025, almost all in C.

2. **Myth**: Pointers are dangerous and should be avoided
   **Truth**: Pointers are powerful tools. When used with discipline (bounds checking, null checks), they enable efficient, high-performance code.

3. **Myth**: C has no string type
   **Truth**: C uses null-terminated character arrays, which are flexible and efficient. C23 adds improved string handling with `typeof` and `typeof_unqual`.

4. **Myth**: `sizeof` tells you the size of an array
   **Truth**: `sizeof` returns the size in bytes. For arrays, use `sizeof(arr) / sizeof(arr[0])` to get element count. When arrays decay to pointers, `sizeof` returns pointer size.

5. **Myth**: Global variables are always bad
   **Truth**: In C, globals with `static` linkage are essential for singletons, lookup tables, and module-private state. The key is controlled access.

## One-Minute Revision

| Concept | Description | Key Detail |
|---------|-------------|------------|
| Variables | Named storage with types | Use `int32_t` for portability |
| Operators | Symbols for operations | Bitwise ops are critical in systems code |
| Control Flow | `if`/`else`, `for`, `while`, `switch` | Guard clauses reduce nesting |
| Functions | Reusable code blocks | Return error codes, not exceptions |
| Arrays | Fixed-size collections | Bounds are YOUR responsibility |
| Strings | Null-terminated `char` arrays | Always use bounded string functions |
| Pointers | Memory address holders | Always check before dereferencing |
| Memory | Stack (auto) vs Heap (manual) | Free what you malloc, NULL after free |

## Related Topics

- [Structures](../02-structures/README.md) — Group related data into custom types
- [Advanced Pointers](../05-pointers-advanced/README.md) — Function pointers, opaque pointers, complex declarations
- [Memory Management](../08-memory-management/README.md) — Advanced allocation patterns, custom allocators
- [Security](../11-security/README.md) — Preventing buffer overflows, integer overflows, and other vulnerabilities
