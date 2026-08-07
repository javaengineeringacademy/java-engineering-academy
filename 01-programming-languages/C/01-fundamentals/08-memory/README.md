# Memory Basics — C Language

## What it is
Memory management in C involves understanding stack, heap, and how data is stored.

## Why it exists
To control how data is allocated, accessed, and freed during program execution.

## When to use it
Always. Understanding memory is essential for writing correct C programs.

## How it works

### Stack Memory

```c
void function(void) {
    int local = 10;        // Stored on stack
    // Automatically freed when function returns
}
```

### Heap Memory

```c
int *ptr = malloc(sizeof(int) * 100);  // Allocate on heap
if (ptr == NULL) {
    // Handle allocation failure
}
free(ptr);  // Must free manually
```

### Memory Layout

```
+------------------+ High address
|      Stack       | ↓ (grows downward)
|                  |
|        ↑        |
|      Heap       | (grows upward)
|                  |
|       BSS       | (uninitialized globals)
|       Data      | (initialized globals)
|     Text        | (code)
+------------------+ Low address
```

### Static vs Dynamic

| Feature | Stack | Heap |
|---------|-------|------|
| Allocation | Automatic | Manual |
| Speed | Fast | Slower |
| Size | Limited | Large |
| Lifetime | Scope-based | Manual free |

### Memory Alignment

```c
struct Aligned {
    char a;     // 1 byte
    // 3 bytes padding
    int b;      // 4 bytes
};  // Total: 8 bytes
```

## Production Incidents

### Incident 1: Memory Leak in Long-Running Server

**Problem:** A production HTTP server's memory usage grows steadily from 200MB to 2GB over 48 hours, eventually triggering OOM killer.

**Cause:** Each request allocates a buffer for parsing headers, but an error path forgets to free it:

```c
int handle_request(int fd) {
    char *buffer = malloc(4096);
    int n = read(fd, buffer, 4096);
    if (n < 0) {
        log_error("read failed");
        return -1;  // buffer leaked
    }
    parse_headers(buffer, n);
    free(buffer);
    return 0;
}
```

**Impact:** Server runs out of memory, OOM killer terminates it, all connections dropped. Happens more frequently when network errors spike.

**Detection:** `top` shows RSS growing over time. `valgrind --leak-check=full` confirms leak in `handle_request`.

**Solution:** Free buffer before error return:

```c
int handle_request(int fd) {
    char *buffer = malloc(4096);
    int n = read(fd, buffer, 4096);
    if (n < 0) {
        log_error("read failed");
        free(buffer);
        return -1;
    }
    parse_headers(buffer, n);
    free(buffer);
    return 0;
}
```

**Prevention:** Use `valgrind` or AddressSanitizer in CI, track memory allocations with metrics, use `goto cleanup` pattern for error handling.

---

### Incident 2: Buffer Overflow in String Copy

**Problem:** A configuration parser allows remote code execution via a crafted config file.

**Cause:** `strcpy` copies user-controlled config value without bounds checking:

```c
void parse_config(const char *key, const char *value) {
    char buf[128];
    strcpy(buf, value);  // No bounds check
    // ...
}
```

**Impact:** Attacker crafts a config file with 256-byte value, overwrites return address, gains shell access. Critical security vulnerability.

**Detection:** Fuzzing with AFL finds crashes. Manual audit reveals the overflow.

**Solution:** Use bounded copy:

```c
void parse_config(const char *key, const char *value) {
    char buf[128];
    strncpy(buf, value, sizeof(buf) - 1);
    buf[sizeof(buf) - 1] = '\0';
    // ...
}
```

**Prevention:** Never use `strcpy`/`strcat` with untrusted input, compile with `-fstack-protector-strong`, use `-D_FORTIFY_SOURCE=2`, enable ASLR.

## Production Checklist

- [ ] Always check malloc/calloc return values
- [ ] Free all allocated memory
- [ ] Avoid memory leaks
- [ ] Use valgrind to detect leaks
- [ ] Be aware of stack size limits

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Uses only stack variables |
| Intermediate | Uses malloc/free for heap |
| Advanced | Understands memory layout and alignment |

## Common Myths

1. **Myth**: malloc initializes memory
   **Truth**: malloc returns uninitialized memory; use calloc for zeroed memory

2. **Myth**: Freeing NULL is undefined
   **Truth**: free(NULL) is safe and does nothing

## One-Minute Revision

| Concept | Description |
|---------|-------------|
| Stack | Automatic, fast, limited |
| Heap | Manual, slower, large |
| malloc | Allocate uninitialized memory |
| calloc | Allocate zeroed memory |
| free | Deallocate memory |
| NULL | Invalid pointer |
| Alignment | Data padding for efficiency |
| Leak | Memory not freed |

## Related Topics

- [Pointers](../07-pointers/README.md)
- [Memory Management](../../08-memory-management/README.md)
