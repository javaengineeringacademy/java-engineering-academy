# Pointers — C Language

## What it is
Pointers are variables that store memory addresses of other variables.

## Why it exists
To enable dynamic memory management, efficient data passing, and low-level memory access.

## When to use it
Whenever you need to reference memory locations directly.

## How it works

### Pointer Declaration

```c
int x = 10;
int *ptr = &x;  // ptr holds address of x
```

### Dereferencing

```c
printf("%d\n", *ptr);  // Access value at address (10)
*ptr = 20;             // Modify value at address
```

### Pointer Arithmetic

```c
int arr[] = {10, 20, 30};
int *p = arr;
p++;        // Points to arr[1]
p += 2;     // Points to arr[3]
```

### Pointers and Arrays

```c
int arr[5] = {1, 2, 3, 4, 5};
int *p = arr;
// arr[i] is equivalent to *(p + i)
```

### Function Pointers

```c
int add(int a, int b) { return a + b; }
int (*func_ptr)(int, int) = add;
int result = func_ptr(5, 3);
```

### Void Pointers

```c
void *vp;
int x = 10;
vp = &x;
printf("%d\n", *(int *)vp);  // Cast required
```

### Null Pointer

```c
int *ptr = NULL;
if (ptr != NULL) {
    // safe to dereference
}
```

## Production Incidents

### Incident 1: Null Pointer Dereference Causing Crash

**Problem:** A web server crashes intermittently under heavy load with SIGSEGV.

**Cause:** A function received a NULL pointer from a failed lookup but proceeded to dereference it without checking.

```c
User *user = find_user(user_id);
// find_user returns NULL when user not found
strcpy(user->name, input);  // SIGSEGV
```

**Impact:** Server process terminates, all connected clients drop, requires restart. Occurs under load when cache misses happen.

**Detection:** Core dump analysis shows crash at `strcpy` with NULL pointer. `gdb` backtrace reveals the call path.

**Solution:** Add NULL check before dereferencing:

```c
User *user = find_user(user_id);
if (user == NULL) {
    log_error("User %d not found", user_id);
    return HTTP_404;
}
strcpy(user->name, input);
```

**Prevention:** Enable compiler warnings (`-Wnull-dereference`), use static analyzers (Coverity, cppcheck), and always check return values of functions that can return NULL.

---

### Incident 2: Dangling Pointer After free()

**Problem:** A caching system produces corrupted data and random crashes after running for several hours.

**Cause:** A cached entry was freed when evicted, but another thread still held a pointer to it. The memory was reused for a new allocation, causing data corruption.

```c
void evict_cache(const char *key) {
    CacheEntry *e = lookup(key);
    free(e);          // Memory freed
    remove_from_map(key);
}

// Another thread still has pointer from lookup
void use_cache(CacheEntry *e) {
    printf("%s\n", e->data);  // Dangling pointer access
}
```

**Impact:** Silent data corruption, occasional crashes, hard to reproduce. Production data integrity compromised.

**Detection:** AddressSanitizer reports use-after-free. Core dumps show garbage data at valid-looking pointers.

**Solution:** Use reference counting or ensure eviction happens only when no references exist:

```c
void evict_cache(const char *key) {
    CacheEntry *e = lookup(key);
    if (atomic_load(&e->refcount) > 0) {
        return;  // Still in use
    }
    free(e);
    remove_from_map(key);
}
```

**Prevention:** Use AddressSanitizer during development, implement reference counting for shared objects, and set pointers to NULL after free.

## Production Checklist

- [ ] Always initialize pointers
- [ ] Check for NULL before dereferencing
- [ ] Avoid dangling pointers
- [ ] Use const for read-only pointers
- [ ] Free dynamically allocated memory

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Understands basic pointer syntax |
| Intermediate | Uses pointer arithmetic and arrays |
| Advanced | Masters function pointers and void pointers |

## Common Myths

1. **Myth**: Pointers are always dangerous
   **Truth**: Pointers are safe when used correctly with proper checks

2. **Myth**: NULL and 0 are different
   **Truth**: NULL is typically defined as ((void*)0) or 0

## One-Minute Revision

| Concept | Description |
|---------|-------------|
| Declaration | type *ptr |
| Address-of | &variable |
| Dereference | *ptr |
| Arithmetic | ptr++, ptr + n |
| Arrays | arr[i] ≡ *(arr + i) |
| Function | return_type (*ptr)(params) |
| Void | void *vp |
| Null | ptr = NULL |

## Related Topics

- [Advanced Pointers](../../05-pointers-advanced/README.md)
- [Memory Management](../../08-memory-management/README.md)
