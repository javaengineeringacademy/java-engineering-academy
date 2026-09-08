# Advanced Pointers — C Language

## Why It Matters

When you're building data structures, callback systems, plugin architectures, or any polymorphic behavior in C, you need advanced pointer patterns beyond basic dereferencing. Function pointers enable callbacks and dynamic dispatch, opaque pointers enable API design, and pointer-to-pointer enables modifying pointers in other functions. Without these, you cannot implement the building blocks of real-world C libraries.

## Engineering Decision Framework

| Factor | Use This | Consider Alternatives |
|--------|----------|----------------------|
| When to use | Callbacks, dynamic dispatch, API boundaries, output parameters | Simple pointers for straightforward cases |
| When NOT to use | When typedefs make code unclear (overuse of function pointers) | Keep interfaces simple |
| Alternatives | C++ virtual methods, Rust trait objects, Go interfaces | More abstraction, different trade-offs |
| Production Examples | Linux VFS (`file_operations`), SQLite VFS, OpenSSL callbacks | Every major C library uses function pointers |
| Common Mistakes | Dangling function pointers after `dlclose`, not checking `realloc` temp | Always null-check, use temp ptr for realloc |

## What It Is

Advanced pointers extend the basic pointer concept into powerful patterns:

| Pattern | Syntax | Use Case |
|---------|--------|----------|
| Pointer-to-pointer | `int **pp` | Modifying pointers in functions |
| Array of pointers | `int *arr[]` | Variable-length argument lists |
| Pointer to array | `int (*ptr)[N]` | Passing 2D arrays |
| Function pointer | `int (*func)(int)` | Callbacks, dynamic dispatch |
| Opaque pointer | `typedef struct Handle Handle` | API boundaries, ABI stability |
| Complex declarations | Clockwise/Spiral Rule | Reading any C declaration |

## Why It Exists

C has no classes, no interfaces, no virtual method tables. Function pointers are C's mechanism for polymorphism. Opaque pointers are C's mechanism for information hiding. Pointer-to-pointer is C's mechanism for output parameters. These patterns are not academic — they are the foundation of every real-world C library.

### Architecture: How Function Pointers Enable Polymorphism

```c
// Define an interface through function pointers
typedef struct {
    int (*open)(void *ctx);
    int (*read)(void *ctx, char *buf, int len);
    int (*write)(void *ctx, const char *buf, int len);
    void (*close)(void *ctx);
} IODevice;

// Implement the interface for different backends
static int file_open(void *ctx) { /* ... */ }
static int file_read(void *ctx, char *buf, int len) { /* ... */ }
// ...

static IODevice file_device = {
    .open = file_open,
    .read = file_read,
    .write = file_write,
    .close = file_close
};

// Generic code that works with any IODevice
void process(IODevice *dev, void *ctx) {
    dev->open(ctx);
    // ... use dev->read, dev->write ...
    dev->close(ctx);
}
```

This is the same pattern used by Linux's `file_operations`, SQLite's VFS, and every C callback system.

## Expanded Code Examples

### Pointer-to-Pointer

```c
#include <stdio.h>
#include <stdlib.h>

// Modifying a pointer in a function (output parameter)
int allocate_array(int **arr, int size) {
    *arr = malloc(size * sizeof(int));
    if (*arr == NULL) return -1;
    for (int i = 0; i < size; i++) (*arr)[i] = i;
    return 0;
}

// Reallocating through pointer-to-pointer
int append_to_array(int **arr, int *count, int *capacity, int value) {
    if (*count >= *capacity) {
        int new_cap = (*capacity) * 2;
        int *new_arr = realloc(*arr, new_cap * sizeof(int));
        if (new_arr == NULL) return -1;
        *arr = new_arr;
        *capacity = new_cap;
    }
    (*arr)[(*count)++] = value;
    return 0;
}

int main(void) {
    int *arr = NULL;
    int count = 0, capacity = 4;

    allocate_array(&arr, capacity);

    for (int i = 0; i < 10; i++) {
        append_to_array(&arr, &count, &capacity, i * 10);
    }

    for (int i = 0; i < count; i++) {
        printf("%d ", arr[i]);
    }
    printf("\n");

    free(arr);
    return 0;
}
```

### Function Pointers and Callbacks

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Generic sort with function pointer comparator
void sort(void *arr, int n, int elem_size,
          int (*cmp)(const void *, const void *)) {
    char *base = (char *)arr;
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            void *a = base + j * elem_size;
            void *b = base + (j + 1) * elem_size;
            if (cmp(a, b) > 0) {
                // Swap
                char temp[elem_size];
                memcpy(temp, a, elem_size);
                memcpy(a, b, elem_size);
                memcpy(b, temp, elem_size);
            }
        }
    }
}

// Comparator functions
int cmp_int(const void *a, const void *b) {
    return (*(const int *)a - *(const int *)b);
}

int cmp_str(const void *a, const void *b) {
    return strcmp(*(const char **)a, *(const char **)b);
}

// Callback-based event system
typedef void (*EventHandler)(const char *event, void *data);

typedef struct {
    EventHandler handlers[16];
    void *data[16];
    int count;
} EventSystem;

void events_subscribe(EventSystem *es, EventHandler handler, void *data) {
    if (es->count < 16) {
        es->handlers[es->count] = handler;
        es->data[es->count] = data;
        es->count++;
    }
}

void events_emit(EventSystem *es, const char *event) {
    for (int i = 0; i < es->count; i++) {
        es->handlers[i](event, es->data[i]);
    }
}

void on_login(const char *event, void *data) {
    printf("User logged in: %s\n", (const char *)data);
}

void on_logout(const char *event, void *data) {
    printf("User logged out: %s\n", (const char *)data);
}
```

### Opaque Pointers — API Boundaries

```c
// db.h — Public API (users cannot see internals)
#ifndef DB_H
#define DB_H

typedef struct Database Database;

Database *db_open(const char *path);
int db_get(Database *db, const char *key, char *value, int maxlen);
int db_put(Database *db, const char *key, const char *value);
void db_close(Database *db);

#endif

// db.c — Implementation (private details hidden)
#include "db.h"
#include <stdlib.h>
#include <string.h>

struct Database {
    FILE *index;
    FILE *data;
    char *buffer;
    size_t buffer_size;
};

Database *db_open(const char *path) {
    Database *db = malloc(sizeof(Database));
    if (!db) return NULL;

    char path_idx[256], path_dat[256];
    snprintf(path_idx, sizeof(path_idx), "%s.idx", path);
    snprintf(path_dat, sizeof(path_dat), "%s.dat", path);

    db->index = fopen(path_idx, "r+b");
    db->data = fopen(path_dat, "r+b");
    db->buffer = malloc(4096);
    db->buffer_size = 4096;

    if (!db->index || !db->data || !db->buffer) {
        db_close(db);
        return NULL;
    }
    return db;
}

void db_close(Database *db) {
    if (db) {
        if (db->index) fclose(db->index);
        if (db->data) fclose(db->data);
        free(db->buffer);
        free(db);
    }
}
```

### Complex Declarations — The Spiral Rule

```c
#include <stdio.h>

// Read declarations right-to-left using the clockwise/spiral rule

int *p;              // p is a pointer to int
int **p;             // p is a pointer to pointer to int
int *arr[10];        // arr is an array of 10 pointers to int
int (*arr)[10];      // arr is a pointer to array of 10 ints
int (*func)(int);    // func is a pointer to function taking int, returning int
int (*func[10])(int); // func is an array of 10 pointers to functions

// Function pointer types (useful for typedef)
typedef int (*Comparator)(const void *, const void *);
typedef void (*Callback)(void *data, int error);

// Array of function pointers (dispatch table)
typedef void (*CommandHandler)(const char *args);

typedef struct {
    const char *name;
    CommandHandler handler;
} Command;

Command commands[] = {
    {"help",    cmd_help},
    {"quit",    cmd_quit},
    {"status",  cmd_status},
};

void dispatch(const char *cmd_name, const char *args) {
    for (int i = 0; i < 3; i++) {
        if (strcmp(commands[i].name, cmd_name) == 0) {
            commands[i].handler(args);
            return;
        }
    }
    printf("Unknown command: %s\n", cmd_name);
}
```

### Flexible Array Members (C99)

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Flexible array member: variable-length trailing array
typedef struct {
    int length;
    char data[];  // Flexible array member (must be last)
} String;

String *string_new(const char *src) {
    size_t len = strlen(src);
    String *s = malloc(sizeof(String) + len + 1);
    if (!s) return NULL;
    s->length = (int)len;
    memcpy(s->data, src, len + 1);
    return s;
}

// Dynamic struct with flexible array
typedef struct {
    int count;
    int capacity;
    int items[];  // Flexible array member
} IntArray;

IntArray *intarray_new(int initial_capacity) {
    IntArray *a = malloc(sizeof(IntArray) + initial_capacity * sizeof(int));
    if (!a) return NULL;
    a->count = 0;
    a->capacity = initial_capacity;
    return a;
}
```

## Production Incidents

### Incident 1: Dangling Function Pointer

**Problem**: A plugin system crashes after unloading a plugin.

**Cause**: Function pointer table retains pointer to unloaded code:

```c
void call_plugin(Plugin *p) {
    p->on_event();  // Plugin library unloaded — code at this address is gone
}
```

**Solution**: Invalidate function pointers on unload:

```c
void unload_plugin(Plugin *p) {
    dlclose(p->handle);
    p->on_event = NULL;  // Clear dangling pointer
}

void call_plugin(Plugin *p) {
    if (p->on_event == NULL) {
        log_error("Plugin not loaded");
        return;
    }
    p->on_event();
}
```

### Incident 2: Pointer-to-Pointer Misuse

**Problem**: A function that reallocates memory through pointer-to-pointer loses the original pointer on allocation failure.

```c
void grow(int **arr, int size) {
    *arr = realloc(*arr, size * sizeof(int));  // If realloc fails, *arr is NULL
    // Original memory is leaked
}
```

**Solution**: Use a temporary pointer:

```c
int grow(int **arr, int size) {
    int *tmp = realloc(*arr, size * sizeof(int));
    if (tmp == NULL) return -1;
    *arr = tmp;
    return 0;
}
```

## Production Checklist

- [ ] Validate all pointers before dereferencing
- [ ] Use `const` for read-only pointers
- [ ] Document complex pointer declarations with typedefs
- [ ] Free all dynamically allocated memory
- [ ] Use function pointers for callbacks and dynamic dispatch
- [ ] Invalidate function pointers after unloading libraries
- [ ] Use opaque pointers for API boundaries
- [ ] Check `realloc` return value with temporary pointer

## Maturity Levels

| Level | Description | Indicators |
|-------|-------------|------------|
| **Beginner** | Understands basic pointers | Uses `*` and `&` correctly |
| **Intermediate** | Uses pointer-to-pointer and arrays | Implements dynamic arrays, output parameters |
| **Advanced** | Masters function pointers and opaque pointers | Builds callback systems, plugin architectures |
| **Expert** | Designs ABI-stable APIs, writes type-safe pointer libraries | Uses opaque pointers, `_Generic`, complex typedefs |

## Common Myths Debunked

1. **Myth**: Function pointers are too complicated
   **Truth**: Function pointers follow simple syntax rules. Use typedefs to simplify: `typedef void (*Callback)(int)` is just a type name.

2. **Myth**: Opaque pointers hide implementation details unnecessarily
   **Truth**: Opaque pointers provide ABI stability, information hiding, and compile-time isolation. They are essential for library boundaries.

3. **Myth**: You can cast any pointer to `void *` and back safely
   **Truth**: `void *` is guaranteed to round-trip for any object pointer, but casting between unrelated struct pointer types is undefined behavior.

4. **Myth**: Function pointers and regular pointers have different sizes
   **Truth**: On all common platforms, a function pointer has the same size as a data pointer. But this is not guaranteed by the standard.

## One-Minute Revision

| Concept | Description | Key Detail |
|---------|-------------|------------|
| `**p` | Pointer to pointer | Used for output parameters |
| `*arr[]` | Array of pointers | Variable-length lists |
| `(*arr)[]` | Pointer to array | 2D array parameter |
| `(*func)()` | Function pointer | Callbacks, dispatch tables |
| Callback | Function passed as argument | Enables polymorphism |
| Opaque | Hidden implementation | `typedef struct Handle Handle` |
| Spiral Rule | Read declarations right-to-left | `int (*func)(int)` → func is ptr to func(int)→int |
| Flexible array | `data[]` at end of struct | Variable-length trailing data |

## Related Topics

- [Pointers (Fundamentals)](../01-fundamentals/07-pointers/README.md) — Basic pointer concepts
- [Data Structures](../06-data-structures/README.md) — Linked lists, trees built with pointers
- [Memory Management](../08-memory-management/README.md) — Dynamic allocation patterns
- [Best Practices](../15-best-practices/README.md) — Coding standards for pointer usage

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Dangling function pointer after `dlclose` | GDB + backtrace | Set breakpoint on crash; inspect function pointer addresses with `info functions` and verify they point to loaded code |
| Memory leak from realloc via pointer-to-pointer | AddressSanitizer | Compile with `-fsanitize=address`; leaked allocations are reported with stack traces |
| Incorrect complex declaration parsing | Clockwise/Spiral Rule | Read declarations right-to-left: start at the variable name, follow parentheses and brackets outward |
| Flexible array member misuse (accessing `data[]` before allocation) | Valgrind / ASan | Compile with `-fsanitize=address`; out-of-bounds access to flexible array triggers immediate error |
| Function pointer type mismatch (calling with wrong signature) | `-Wincompatible-pointer-types` | Enable strict warnings; use typedef'd function pointer types to enforce signature consistency |

## Code Review Checklist

- [ ] All pointer dereferences preceded by null check
- [ ] `const` used for read-only pointer parameters
- [ ] Complex pointer declarations use typedef for readability
- [ ] `realloc` result assigned to temporary pointer before overwriting original
- [ ] Function pointers invalidated (set to `NULL`) after unloading associated library
- [ ] Flexible array member is last member of struct
- [ ] Opaque pointer pattern used for API boundaries (public header forward-declares only)

## Architecture Considerations

Advanced pointer patterns enable C's most powerful abstractions: function pointers provide polymorphism (dispatch tables), opaque pointers provide information hiding, and pointer-to-pointer enables output parameters. These patterns are the foundation of every major C library — Linux VFS, SQLite VFS, OpenSSL callbacks — and replace OOP constructs that languages like C++ and Rust provide natively.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Function pointer dispatch | Plugin systems, callbacks, vtables | Flexible but no compile-time type safety |
| Opaque pointer | Library API boundaries | Hides internals but prevents stack allocation |
| Pointer-to-pointer | Output parameters, dynamic array growth | Verbose but allows modifying pointers in called functions |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Dangling function pointer after library unload | Code execution at invalid address | Null-check function pointers before calling; invalidate on `dlclose` |
| Use-after-free through stale pointer-to-pointer | Heap corruption, code execution | Set all pointers to `NULL` after `free`; use reference counting |
| Arbitrary function execution via corrupted vtable | Remote code execution | Validate function pointers against known-good tables; use ASLR |

## Evolution & Modernization

| Era | Change | Migration Path |
|-----|--------|----------------|
| C89 → C99 | Added flexible array members, restricted pointers (`restrict`), inline | Use flexible arrays instead of the "struct hack"; add `restrict` to hot-path pointer parameters |
| C99 → C11 | Added `_Generic` for type-safe pointer dispatch, `_Static_assert` for pointer sizes | Use `_Generic` for type-generic pointer operations; assert pointer sizes at compile time |
| C11 → C23 | Added `typeof`, improved `_Generic`, `constexpr` functions | Use `typeof` for type-generic pointer macros; use `constexpr` for compile-time pointer constants |

## Version Validation

| Feature | C Standard | Status |
|---------|-----------|--------|
| Flexible array members (`int data[]`) | C99 | Standard — preferred over the "struct hack" |
| `restrict` qualified pointers | C99 | Standard — enables compiler optimizations for non-overlapping pointers |
| `_Generic` for type dispatch | C11 | Standard — enables type-safe pointer macros |
| `typeof` operator | C23 (standardized) | Use for type-generic pointer operations |

## Interview Questions

1. **What is the clockwise/spiral rule for reading C declarations?**: Start at the variable name, move right to the type, then spiral inward through parentheses and brackets. For `int (*func)(int)`, `func` is a pointer to a function taking `int` and returning `int`.
2. **Why use opaque pointers for library APIs?**: Opaque pointers hide implementation details, providing ABI stability (internal layout can change without recompiling callers), information hiding (users cannot access internal state), and compile-time isolation (header changes don't trigger cascading recompilation).
3. **What is the danger of casting between unrelated struct pointer types?**: Casting between unrelated struct pointer types is undefined behavior in C (unlike `void *`, which round-trips safely). The compiler may assume the pointer types are compatible and generate incorrect code.
4. **How does `realloc` through pointer-to-pointer differ from direct `realloc`?**: Direct `realloc` overwrites the original pointer, losing the old value on failure (memory leak). Through pointer-to-pointer, assign to a temporary first: `int *tmp = realloc(*arr, size); if (tmp) *arr = tmp;` — this preserves the original on failure.
5. **What are flexible array members and when were they introduced?**: Flexible array members (`int data[]` as the last member of a struct) were introduced in C99. They allow variable-length trailing data without pointer indirection, reducing allocations from two (struct + array) to one.

## References

- [C Standard (N3220)](https://www.open-std.org/jtc1/sc22/wg14/www/docs/n3220.pdf)
- [Expert C Programming: Deep C Secrets (van der Linden)](https://www.amazon.com/Expert-C-Programming-Deep-Secrets/dp/0131774298)
- [Secure Coding in C and CERT C Coding Standard](https://wiki.sei.cmu.edu/confluence/display/c/)

## Overview

The Advanced Pointers module covers function pointers, opaque pointers, pointer-to-pointer, and complex declarations. These patterns enable callbacks, dynamic dispatch, plugin architectures, and polymorphic behavior in C. Every major C library uses these patterns.

## Learning Objectives

- Use function pointers for callbacks and dynamic dispatch
- Implement opaque pointers for API boundaries
- Work with pointer-to-pointer for modifying pointers in functions
- Read complex declarations using the Spiral Rule
- Implement flexible array members (C99)

## Prerequisites

- Completion of Module 04 (File I/O)
- Understanding of basic pointers
- Function declaration and calling

## History

- **1972** — Function pointers included in original C
- **1978** — K&R C documented pointer syntax
- **1989** — ANSI C standardized function pointer behavior
- **1999** — C99 added flexible array members
- **2011** — C11 added `_Generic` for type-generic macros
- **2023** — C23 added improved function pointer syntax

## Production Notes

- **Where is it used?** Callbacks, plugin systems, event handlers, dynamic dispatch
- **Why is it useful?** Enables polymorphism, decouples interfaces from implementations
- **When should it be avoided?** When simple function calls suffice
- **Alternative?** C++ virtual methods, Rust trait objects, Go interfaces

## Core Concepts

### Advanced Pointer Patterns

| Pattern | Syntax | Use Case |
|---------|--------|----------|
| Pointer-to-pointer | `int **pp` | Modifying pointers in functions |
| Array of pointers | `int *arr[]` | Variable-length argument lists |
| Pointer to array | `int (*ptr)[N]` | Passing 2D arrays |
| Function pointer | `int (*func)(int)` | Callbacks, dynamic dispatch |
| Opaque pointer | `typedef struct Handle Handle` | API boundaries, ABI stability |

### Function Pointer Types

| Type | Declaration | Use Case |
|------|-------------|----------|
| Simple | `int (*func)(int)` | Single parameter callback |
| Multiple params | `int (*func)(int, int)` | Multi-parameter callback |
| Return pointer | `int *(*func)(int)` | Returns pointer |
| No return | `void (*func)(void)` | Notification callback |

## Internal Working

### Function Pointer Memory Layout

```
Function Pointer Variable
├── Address of function (8 bytes on 64-bit)
├── Points to: function code in text segment
└── Called via: indirect call instruction
```

### Opaque Pointer Pattern

```
Header File (.h)
├── typedef struct Handle Handle;
├── Handle *handle_new(void);
└── void handle_free(Handle *h);

Implementation File (.c)
├── struct Handle { int data; };
├── Handle *handle_new(void) { ... }
└── void handle_free(Handle *h) { ... }
```

## Syntax

```c
// Function pointer declaration
int add(int a, int b) { return a + b; }
int (*func_ptr)(int, int) = add;
int result = func_ptr(2, 3);  // 5

// Pointer-to-pointer
int x = 10;
int *p = &x;
int **pp = &p;
printf("%d\n", **pp);  // 10

// Opaque pointer
typedef struct Handle Handle;
Handle *handle_new(void);
void handle_free(Handle *h);

// Complex declaration (Spiral Rule)
int (*(*func)(int))(int, int);  // Function returning function pointer

// Flexible array member (C99)
struct flex {
    int count;
    int data[];
};
```

## Examples

### Easy Example: Function Pointer

```c
#include <stdio.h>

int add(int a, int b) { return a + b; }
int sub(int a, int b) { return a - b; }

int main(void) {
    int (*op)(int, int) = add;
    printf("add: %d\n", op(2, 3));
    op = sub;
    printf("sub: %d\n", op(2, 3));
    return 0;
}
```

### Medium Example: Callback System

```c
#include <stdio.h>

typedef void (*callback)(int);

void process(int *arr, int n, callback cb) {
    for (int i = 0; i < n; i++) {
        cb(arr[i]);
    }
}

void print(int val) { printf("%d ", val); }
void square(int val) { printf("%d ", val * val); }

int main(void) {
    int arr[] = {1, 2, 3, 4, 5};
    printf("Values: ");
    process(arr, 5, print);
    printf("\nSquares: ");
    process(arr, 5, square);
    return 0;
}
```

### Hard Example: Opaque Pointer API

```c
// handle.h
typedef struct Handle Handle;
Handle *handle_create(void);
void handle_destroy(Handle *h);
int handle_get_value(Handle *h);
void handle_set_value(Handle *h, int val);

// handle.c
#include "handle.h"
#include <stdlib.h>

struct Handle {
    int value;
    int (*validator)(int);
};

Handle *handle_create(void) {
    Handle *h = malloc(sizeof(Handle));
    h->value = 0;
    h->validator = NULL;
    return h;
}

void handle_destroy(Handle *h) { free(h); }
int handle_get_value(Handle *h) { return h->value; }
void handle_set_value(Handle *h, int val) { h->value = val; }
```

### Enterprise Example: Plugin System

```c
#include <stdio.h>
#include <stdlib.h>

typedef struct {
    const char *name;
    int version;
    int (*init)(void);
    void (*execute)(const char *input);
    void (*cleanup)(void);
} Plugin;

int plugin_init_a(void) { printf("Plugin A initialized\n"); return 0; }
void plugin_execute_a(const char *input) { printf("Plugin A: %s\n", input); }
void plugin_cleanup_a(void) { printf("Plugin A cleaned up\n"); }

Plugin plugins[] = {
    {"Alpha", 1, plugin_init_a, plugin_execute_a, plugin_cleanup_a},
    {NULL, 0, NULL, NULL, NULL}
};

int load_plugins(void) {
    for (Plugin *p = plugins; p->name; p++) {
        if (p->init() != 0) return -1;
    }
    return 0;
}
```

## Performance Considerations

| Aspect | Consideration | Optimization |
|--------|---------------|--------------|
| Indirection | Function pointer calls | Inline small functions |
| Cache | Indirect call prediction | Minimize function pointer usage in hot paths |
| Optimization | Compiler can't inline through pointers | Use direct calls when possible |
| Virtual dispatch | vtable lookup overhead | Consider direct calls for performance |

## Best Practices

- Do:
  - Use `typedef` for function pointer types
  - Null-check function pointers before calling
  - Use opaque pointers for API boundaries
  - Document callback behavior
  - Use `const` for read-only parameters
  
- Don't:
  - Overuse function pointers (readability)
  - Store dangling function pointers
  - Ignore compiler warnings about pointer types
  - Use complex declarations without explanation
  - Assume function pointer size

## Common Mistakes

| Mistake | Consequence | Prevention |
|---------|-------------|------------|
| Dangling function pointer | Crash, undefined behavior | Null-check before call |
| Wrong function signature | Undefined behavior | Match types exactly |
| Missing `volatile` on callback | Optimization issues | Use `volatile` for hardware callbacks |
| Overuse of function pointers | Unreadable code | Use direct calls when possible |
| Not checking `realloc` temp | Memory leak | Use temp pointer for realloc |

## Interview Questions

### Q1: What is a function pointer?
**Answer:** A variable that stores the address of a function. Used for callbacks, dynamic dispatch, and event handling.

### Q2: What is the Spiral Rule for reading declarations?
**Answer:** Start at the variable name, spiral right for array/function, left for pointer/reference. Explains complex C declarations.

### Q3: What is an opaque pointer?
**Answer:** A pointer to a struct whose definition is hidden in the implementation file. Provides data hiding and ABI stability.

### Q4: What is the difference between `int *arr[10]` and `int (*arr)[10]`?
**Answer:** `int *arr[10]` is an array of 10 pointers to int. `int (*arr)[10]` is a pointer to an array of 10 ints.

### Q5: What is a flexible array member?
**Answer:** `struct { int n; int data[]; }` — array of unknown size at end of structure. Requires `malloc` with extra space.

### Q6: What is the difference between function pointer and void pointer?
**Answer:** Function pointer stores function address. Void pointer stores data address. Function pointers can be called; void pointers must be cast.

### Q7: What is the purpose of `typedef` with function pointers?
**Answer:** Creates a readable alias: `typedef int (*operation)(int, int);` instead of `int (*operation)(int, int);`.

### Q8: What is a callback function?
**Answer:** A function passed as an argument to another function. Called back when an event occurs or operation completes.

### Q9: What is the difference between `int (*func)(int)` and `int (*func)(int, int)`?
**Answer:** First takes one parameter. Second takes two parameters. Function pointer types must match exactly.

### Q10: What is the difference between `sizeof` on function pointer and data pointer?
**Answer:** Both are typically 8 bytes on 64-bit systems. Function pointer size is implementation-defined.

### Q11: What is the difference between `extern` function and function pointer?
**Answer:** `extern` function is resolved at link time. Function pointer is resolved at runtime (dynamic dispatch).

### Q12: What is the purpose of `__attribute__((constructor))`?
**Answer:** GCC attribute that runs a function before `main()`. Useful for initialization.

### Q13: What is the difference between `register` and `static` variables?
**Answer:** `register` suggests CPU register allocation (no address). `static` persists for program lifetime.

### Q14: What is the difference between `const int *p` and `int * const p`?
**Answer:** `const int *p` — pointer to const data. `int * const p` — const pointer to data.

### Q15: What is the difference between `sizeof` and `strlen`?
**Answer:** `sizeof` returns size in bytes (compile-time). `strlen` returns string length (runtime).

## Cross-References

- **Previous Module:** [04 - File I/O](../04-file-io/)
- **Next Module:** [06 - Data Structures](../06-data-structures/)
- **Related:** [02 - Structures](../02-structures/) — Structure pointers
- **Related:** [09 - Concurrency](../09-concurrency/) — Thread callbacks
- **External:** [Expert C Programming](https://www.amazon.com/Expert-C-Programming-Deep-Secrets/dp/0131774298)
- **External:** [C Standard (N3220)](https://www.open-std.org/jtc1/sc22/wg14/www/docs/n3220.pdf)
