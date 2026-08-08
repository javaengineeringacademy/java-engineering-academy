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
