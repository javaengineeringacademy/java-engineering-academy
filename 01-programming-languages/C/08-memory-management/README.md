# Memory Management — C Language

## What it is
Memory management involves allocating, using, and freeing memory dynamically.

## Why it exists
To control memory usage, prevent leaks, and optimize performance.

## When to use it
Whenever you need dynamic data structures, variable-size data, or resource management.

## How it works

### malloc

```c
int *arr = malloc(10 * sizeof(int));
if (arr == NULL) {
    // Handle error
}
```

### calloc

```c
int *arr = calloc(10, sizeof(int));  // Zero-initialized
```

### realloc

```c
arr = realloc(arr, 20 * sizeof(int));  // Resize
```

### free

```c
free(arr);
arr = NULL;  // Good practice
```

### Memory Leaks

```c
void leak(void) {
    int *p = malloc(100);
    // No free = memory leak
}
```

### Dangling Pointers

```c
int *p = malloc(sizeof(int));
free(p);
*p = 10;  // Undefined behavior
```

### Double Free

```c
int *p = malloc(sizeof(int));
free(p);
free(p);  // Undefined behavior
```

### Memory Debugging Tools

```bash
valgrind --leak-check=full ./program
```

## Production Incidents

### Incident 1: Use-After-Free Vulnerability

**Problem:** A network daemon allows remote code execution through a crafted packet sequence.

**Cause:** A connection structure is freed when the client disconnects, but pending asynchronous operations still reference it:

```c
void handle_disconnect(Connection *conn) {
    free(conn->buffer);
    free(conn);           // conn freed
}

// Async callback still references conn
void on_data_ready(Connection *conn, char *data) {
    memcpy(conn->buffer, data, len);  // Use-after-free
}
```

**Impact:** Attacker triggers disconnect while async operation is in flight, gains control of freed memory, executes arbitrary code. CVSS 9.8 critical vulnerability.

**Detection:** AddressSanitizer detects use-after-free in `on_data_ready`. Crash reports show writes to freed memory.

**Solution:** Reference count connections, defer free until all operations complete:

```c
void conn_ref(Connection *conn) { atomic_fetch_add(&conn->refcount, 1); }
void conn_unref(Connection *conn) {
    if (atomic_fetch_sub(&conn->refcount, 1) == 1) {
        free(conn->buffer);
        free(conn);
    }
}

void handle_disconnect(Connection *conn) {
    conn_unref(conn);  // Decrements, doesn't free if refs remain
}
```

**Prevention:** Use reference counting for shared objects, run with AddressSanitizer, audit async code paths carefully.

---

### Incident 2: Double Free Causing Heap Corruption

**Problem:** A database engine's memory allocator corrupts its internal structures, causing random crashes and data loss.

**Cause:** An error handling path frees memory that was already freed by the cleanup routine:

```c
int process_query(Query *q) {
    char *result = execute(q);
    if (result == NULL) {
        free(q->params);  // Free params on error
        return ERROR;
    }
    free(q->params);      // Also freed in cleanup
    return OK;
}

void cleanup_query(Query *q) {
    free(q->params);      // Double free when execute fails
    free(q);
}
```

**Impact:** Heap metadata corrupted, subsequent allocations return overlapping memory, silent data corruption in database. Takes down production database.

**Detection:** Valgrind reports "double-free" in `cleanup_query`. Malloc debug shows corrupted heap after error path.

**Solution:** Set pointer to NULL after free, as `free(NULL)` is safe:

```c
int process_query(Query *q) {
    char *result = execute(q);
    if (result == NULL) {
        free(q->params);
        q->params = NULL;  // Prevent double free
        return ERROR;
    }
    free(q->params);
    q->params = NULL;
    return OK;
}

void cleanup_query(Query *q) {
    free(q->params);  // Safe: NULL check implicit in free
    q->params = NULL;
    free(q);
}
```

**Prevention:** Set pointers to NULL after free, use `free()` wrappers that track state, compile with `-fsanitize=undefined`, use tcmalloc or jemalloc with guard pages.

## Production Checklist

- [ ] Always check malloc/calloc return values
- [ ] Free all allocated memory
- [ ] Set pointers to NULL after free
- [ ] Avoid memory leaks
- [ ] Use tools like valgrind

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Uses malloc/free |
| Intermediate | Avoids leaks and dangling pointers |
| Advanced | Masters memory pools and custom allocators |

## Common Myths

1. **Myth**: malloc returns zeroed memory
   **Truth**: malloc returns uninitialized memory; use calloc

2. **Myth**: free sets pointer to NULL
   **Truth**: You must set pointer to NULL manually

## One-Minute Revision

| Function | Purpose |
|----------|---------|
| malloc | Allocate memory |
| calloc | Allocate zeroed memory |
| realloc | Resize allocation |
| free | Deallocate memory |
| Memory leak | Not freeing allocated memory |
| Dangling pointer | Pointer to freed memory |
| Double free | Freeing same memory twice |

## Related Topics

- [Memory Basics](../01-fundamentals/08-memory/README.md)
- [Testing](../13-testing/README.md)
