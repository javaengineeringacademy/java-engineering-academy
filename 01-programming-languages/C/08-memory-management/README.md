# Memory Management — C Language

## The Problem

C gives you direct control over memory — and direct responsibility for getting it right. Without a garbage collector, every byte you allocate must be explicitly freed. Get it wrong, and you get memory leaks (memory exhausted over time), dangling pointers (using freed memory), double frees (corrupting the allocator), or buffer overflows (writing past allocated boundaries).

Memory bugs are the most dangerous class of software vulnerabilities. The Heartbleed bug, the Morris worm, and countless remote code execution exploits all trace back to memory management errors in C code.

## What It Is

Memory management in C involves four operations:

| Operation | Function | Purpose |
|-----------|----------|---------|
| Allocate | `malloc` | Allocate uninitialized memory |
| Allocate zeroed | `calloc` | Allocate zero-initialized memory |
| Resize | `realloc` | Grow or shrink allocation |
| Free | `free` | Release memory back to the system |

## Why It Exists

Manual memory management exists because:
- **Performance**: No GC pauses, no runtime overhead
- **Control**: You decide when and how memory is allocated
- **Predictability**: Deterministic allocation/deallocation timing
- **Portability**: Works on systems without garbage collectors (embedded, kernel)

### Architecture: The C Memory Model

```
High Address
┌─────────────────────┐
│   Command-line args  │
│   Environment vars   │
├─────────────────────┤
│       Stack          │ ← Grows downward
│  (local variables,   │
│   function calls)    │
│                      │
│         ↓            │
│         ↑            │
│       Heap           │ ← Grows upward
│  (malloc, calloc)    │
├─────────────────────┤
│  BSS (uninitialized  │
│   global variables)  │
├─────────────────────┤
│  Data (initialized   │
│   global variables)  │
├─────────────────────┤
│  Text (code)         │ ← Read-only
└─────────────────────┘
Low Address
```

## Expanded Code Examples

### malloc and calloc — When to Use Which

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void malloc_vs_calloc(void) {
    // malloc: faster, but contains garbage values
    int *arr1 = malloc(10 * sizeof(int));
    if (!arr1) return;
    // arr1[0] could be any value — uninitialized

    // calloc: zero-initialized, slightly slower
    int *arr2 = calloc(10, sizeof(int));
    if (!arr2) { free(arr1); return; }
    // arr2[0] is guaranteed to be 0

    // When to use which:
    // malloc: when you will initialize every element before reading
    // calloc: when you need zero-initialization (counters, flags, etc.)

    free(arr1);
    free(arr2);
}
```

### realloc — Growing Dynamic Arrays

```c
#include <stdio.h>
#include <stdlib.h>

typedef struct {
    int *data;
    int size;
    int capacity;
} DynamicArray;

DynamicArray *array_create(int initial_cap) {
    DynamicArray *arr = malloc(sizeof(DynamicArray));
    if (!arr) return NULL;
    arr->data = malloc(initial_cap * sizeof(int));
    if (!arr->data) { free(arr); return NULL; }
    arr->size = 0;
    arr->capacity = initial_cap;
    return arr;
}

int array_push(DynamicArray *arr, int value) {
    if (arr->size >= arr->capacity) {
        int new_cap = arr->capacity * 2;
        int *new_data = realloc(arr->data, new_cap * sizeof(int));
        if (!new_data) return -1;
        arr->data = new_data;
        arr->capacity = new_cap;
    }
    arr->data[arr->size++] = value;
    return 0;
}

void array_free(DynamicArray *arr) {
    if (arr) {
        free(arr->data);
        free(arr);
    }
}

int main(void) {
    DynamicArray *arr = array_create(4);
    if (!arr) return 1;

    for (int i = 0; i < 100; i++) {
        array_push(arr, i);
    }

    for (int i = 0; i < arr->size; i++) {
        printf("%d ", arr->data[i]);
    }
    printf("\n");

    array_free(arr);
    return 0;
}
```

### Common Memory Bugs — And How to Prevent Them

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// BUG 1: Memory leak
void leak_example(void) {
    int *p = malloc(100 * sizeof(int));
    if (!p) return;
    // ... use p ...
    return;  // Forgot free(p) — memory leaked
}

// FIX 1: Always pair malloc with free
void leak_fixed(void) {
    int *p = malloc(100 * sizeof(int));
    if (!p) return;
    // ... use p ...
    free(p);  // Always free
}

// BUG 2: Dangling pointer
void dangling_example(void) {
    int *p = malloc(sizeof(int));
    *p = 42;
    free(p);
    // p is now dangling — still points to freed memory
    printf("%d\n", *p);  // Undefined behavior
}

// FIX 2: Set pointer to NULL after free
void dangling_fixed(void) {
    int *p = malloc(sizeof(int));
    *p = 42;
    free(p);
    p = NULL;  // Now safe — dereferencing NULL is caught by sanitizers
}

// BUG 3: Double free
void double_free_example(void) {
    int *p = malloc(sizeof(int));
    free(p);
    free(p);  // Double free — corrupts allocator metadata
}

// FIX 3: NULL after free (free(NULL) is safe)
void double_free_fixed(void) {
    int *p = malloc(sizeof(int));
    free(p);
    p = NULL;
    free(p);  // Safe: free(NULL) does nothing
}

// BUG 4: Use after free
void use_after_free_example(void) {
    char *str = malloc(64);
    strcpy(str, "hello");
    free(str);
    // ... later ...
    if (strcmp(str, "hello") == 0) {  // Use after free
        printf("Match\n");
    }
}

// FIX 4: NULL check or don't use after free
void use_after_free_fixed(void) {
    char *str = malloc(64);
    if (!str) return;
    strcpy(str, "hello");
    free(str);
    str = NULL;
    // Don't use str after this point
}

// BUG 5: Buffer overflow
void overflow_example(int n) {
    int *arr = malloc(n * sizeof(int));
    for (int i = 0; i <= n; i++) {  // Off-by-one: writes past end
        arr[i] = i;
    }
    free(arr);
}

// FIX 5: Correct bounds
void overflow_fixed(int n) {
    int *arr = malloc(n * sizeof(int));
    if (!arr) return;
    for (int i = 0; i < n; i++) {  // Correct: i < n
        arr[i] = i;
    }
    free(arr);
    arr = NULL;
}
```

### Memory Pool — Custom Allocator

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define POOL_SIZE (1024 * 1024)  // 1 MB

typedef struct {
    char memory[POOL_SIZE];
    size_t offset;
} MemoryPool;

void pool_init(MemoryPool *pool) {
    pool->offset = 0;
}

void *pool_alloc(MemoryPool *pool, size_t size) {
    // Align to 8 bytes
    size = (size + 7) & ~(size_t)7;

    if (pool->offset + size > POOL_SIZE) {
        return NULL;  // Pool exhausted
    }

    void *ptr = pool->memory + pool->offset;
    pool->offset += size;
    return ptr;
}

// No individual free — entire pool freed at once
void pool_reset(MemoryPool *pool) {
    pool->offset = 0;
}

// Usage
int main(void) {
    MemoryPool pool;
    pool_init(&pool);

    int *arr = pool_alloc(&pool, 100 * sizeof(int));
    char *str = pool_alloc(&pool, 256);

    // ... use arr and str ...

    pool_reset(&pool);  // Free everything at once
    return 0;
}
```

## Production Incidents

### Incident 1: Use-After-Free Vulnerability (Heartbleed-style)

**Problem**: A network daemon allows remote code execution through crafted packets.

**Cause**: Connection structure freed while async operations still reference it:

```c
void handle_disconnect(Connection *conn) {
    free(conn->buffer);
    free(conn);  // conn freed, but callbacks still reference it
}

void on_data_ready(Connection *conn, char *data) {
    memcpy(conn->buffer, data, len);  // Use-after-free
}
```

**Impact**: CVSS 9.8 critical vulnerability. Attacker gains control of freed memory.

**Solution**: Reference counting:

```c
void conn_ref(Connection *conn) { atomic_fetch_add(&conn->refcount, 1); }
void conn_unref(Connection *conn) {
    if (atomic_fetch_sub(&conn->refcount, 1) == 1) {
        free(conn->buffer);
        free(conn);
    }
}
```

### Incident 2: Double Free Corrupting Database

**Problem**: Database engine's allocator corrupts internal structures, causing data loss.

**Cause**: Error path and cleanup both free the same memory:

```c
int process_query(Query *q) {
    char *result = execute(q);
    if (result == NULL) {
        free(q->params);  // Free on error
        return ERROR;
    }
    free(q->params);  // Also freed in cleanup
    return OK;
}

void cleanup_query(Query *q) {
    free(q->params);  // Double free when execute fails
    free(q);
}
```

**Solution**: NULL after free:

```c
void cleanup_query(Query *q) {
    free(q->params);   // free(NULL) is safe
    q->params = NULL;
    free(q);
}
```

## Production Checklist

- [ ] Always check `malloc`/`calloc`/`realloc` return values
- [ ] Free all allocated memory before program exit
- [ ] Set pointers to NULL after `free`
- [ ] Never use memory after freeing it
- [ ] Never free the same memory twice
- [ ] Match allocation method: `malloc`→`free`, `calloc`→`free`, `strdup`→`free`
- [ ] Run Valgrind or AddressSanitizer in testing
- [ ] Use memory pools for high-frequency allocations
- [ ] Check for integer overflow before size calculations
- [ ] Use `sizeof(*ptr)` instead of `sizeof(type)` for auto-updating

## Maturity Levels

| Level | Description | Indicators |
|-------|-------------|------------|
| **Beginner** | Uses `malloc`/`free` | Allocates and frees basic structures |
| **Intermediate** | Avoids leaks and dangling pointers | Uses Valgrind, NULL after free |
| **Advanced** | Masters memory pools and custom allocators | Implements pool allocators, arena allocators |
| **Expert** | Designs allocation strategies, debugs memory corruption | Uses ASan/TSan, understands allocator internals |

## Common Myths Debunked

1. **Myth**: `malloc` returns zeroed memory
   **Truth**: `malloc` returns uninitialized memory. Use `calloc` for zeroed memory. The contents of `malloc`'d memory are indeterminate.

2. **Myth**: `free` sets the pointer to NULL
   **Truth**: `free` does not modify the pointer. You must set it to NULL manually: `free(p); p = NULL;`

3. **Myth**: Memory leaks don't matter in short-running programs
   **Truth**: Leaks in long-running servers accumulate over time, eventually exhausting memory. Even short programs should be leak-free for correctness.

4. **Myth**: `realloc` never fails
   **Truth**: `realloc` can return NULL when memory is exhausted. Always check the return value with a temporary pointer.

## One-Minute Revision

| Function | Purpose | Key Detail |
|----------|---------|------------|
| `malloc(n)` | Allocate n bytes | Uninitialized, may be garbage |
| `calloc(n, size)` | Allocate n×size bytes | Zero-initialized |
| `realloc(p, n)` | Resize to n bytes | May move memory, may fail |
| `free(p)` | Deallocate | Does NOT set p to NULL |
| Memory leak | Not freeing allocated memory | Accumulates over time |
| Dangling pointer | Pointer to freed memory | Use-after-free = UB |
| Double free | Freeing same memory twice | Corrupts allocator |
| Buffer overflow | Writing past allocation | Security vulnerability |

## Related Topics

- [Memory Basics (Fundamentals)](../01-fundamentals/08-memory/README.md) — Stack vs heap basics
- [Pointers Advanced](../05-pointers-advanced/README.md) — Pointer patterns for memory management
- [Performance](../12-performance/README.md) — Custom allocators and memory pools
- [Testing](../13-testing/README.md) — Valgrind and AddressSanitizer
- [Security](../11-security/README.md) — Preventing memory-based vulnerabilities
