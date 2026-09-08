# C Capstone Project

## Overview

The capstone project integrates all 17 modules into a comprehensive, production-grade C application. This project demonstrates mastery of C fundamentals, data structures, memory management, concurrency, networking, and systems programming — the skills that make C the backbone of operating systems, databases, and embedded systems.

## Learning Objectives

- Apply all 17 C modules in a real-world systems project
- Design and implement a complete C application from scratch
- Follow production best practices for memory safety and security
- Demonstrate architectural thinking in a low-level language
- Create production-ready code with comprehensive tests

## Prerequisites

- Completion of all 17 C modules (00-16)
- Understanding of Make/CMake build tools
- Git version control knowledge

## History

Capstone projects have been used in computer science education since the 1970s. The concept originated at Carnegie Mellon University as a way to integrate learning across courses. Industry adopted capstone projects for technical interviews and hiring assessments, especially for systems programming roles where C mastery is critical.

## Production Notes

- **Where is it used?** Technical interviews, coding bootcamps, university courses, portfolio projects
- **Why is it useful?** Demonstrates holistic understanding and ability to integrate multiple low-level concepts
- **When should it be avoided?** When learning individual concepts; focus on fundamentals first
- **Alternative?** None — capstone projects are the gold standard for demonstrating C mastery

## Core Concepts

### Project Architecture

| Layer | Responsibility | Technologies |
|-------|---------------|--------------|
| Application | User interface, CLI | Argument parsing, I/O |
| Business Logic | Domain rules, workflows | Data structures, algorithms |
| Data Access | Persistence, caching | File I/O, memory management |
| Infrastructure | Cross-cutting concerns | Threading, networking, security |

### Module Integration Map

| Module | Capstone Application |
|--------|---------------------|
| 00 - Knowledge Atoms | Integer types, type promotion, bitwise operations |
| 01 - Fundamentals | Variables, control flow, string handling |
| 02 - Structures | Domain model, struct composition, linked structures |
| 03 - Preprocessor | Conditional compilation, macros, build configuration |
| 04 - File I/O | Configuration loading, data persistence, logging |
| 05 - Pointers Advanced | Function pointers, callbacks, opaque pointers |
| 06 - Data Structures | Hash tables, trees, dynamic arrays |
| 07 - Algorithms | Sorting, searching, graph traversal |
| 08 - Memory Management | Custom allocators, pool management, leak detection |
| 09 - Concurrency | Thread pool, producer-consumer, synchronization |
| 10 - Networking | TCP/UDP server, protocol handling, connection management |
| 11 - Security | Input validation, buffer overflow prevention, encryption |
| 12 - Performance | Profiling, cache optimization, SIMD, benchmarks |
| 13 - Testing | Unit tests, integration tests, test framework |
| 14 - Build Systems | Makefile, CMake, cross-platform builds |
| 15 - Best Practices | Coding standards, documentation, code review |
| 16 - Senior Topics | ABI-stable API, dynamic dispatch, architecture decisions |

## Internal Working

### Project Structure

```
capstone-project/
├── include/
│   ├── app.h            # Public API (ABI-stable)
│   ├── config.h         # Configuration types
│   ├── database.h       # Database interface
│   ├── network.h        # Network interface
│   └── platform.h       # Platform abstraction
├── src/
│   ├── main.c           # Entry point
│   ├── app.c            # Application logic
│   ├── config.c         # Configuration handling
│   ├── database.c       # Database implementation
│   ├── network.c        # Network server
│   ├── thread_pool.c    # Thread pool
│   ├── allocator.c      # Custom memory allocator
│   ├── hash_table.c     # Hash table implementation
│   └── platform/
│       ├── linux.c      # Linux-specific code
│       ├── macos.c      # macOS-specific code
│       └── windows.c    # Windows-specific code
├── tests/
│   ├── test_main.c      # Test runner
│   ├── test_database.c  # Database tests
│   ├── test_network.c   # Network tests
│   └── test_hash_table.c # Hash table tests
├── CMakeLists.txt
├── Makefile
└── README.md
```

## Syntax

### Public API (ABI-Stable)

```c
// app.h — Public interface, never changes layout
#ifndef APP_H
#define APP_H

#include <stddef.h>
#include <stdbool.h>

typedef struct app_context app_context_t;

app_context_t *app_create(const char *config_path);
void app_destroy(app_context_t *ctx);

int app_start(app_context_t *ctx);
int app_stop(app_context_t *ctx);

int app_process_request(app_context_t *ctx,
                        const char *request,
                        char *response,
                        size_t response_size);

const char *app_version(void);

#endif  // APP_H
```

### Implementation (Private, Can Change)

```c
// app.c — Private implementation
#include "app.h"
#include "config.h"
#include "database.h"
#include "network.h"
#include "thread_pool.h"
#include <stdlib.h>
#include <string.h>

struct app_context {
    config_t *config;
    database_t *db;
    network_server_t *server;
    thread_pool_t *pool;
    bool running;
};

app_context_t *app_create(const char *config_path) {
    if (!config_path) return NULL;

    app_context_t *ctx = calloc(1, sizeof(*ctx));
    if (!ctx) return NULL;

    ctx->config = config_load(config_path);
    if (!ctx->config) { free(ctx); return NULL; }

    ctx->db = database_open(ctx->config->db_path);
    if (!ctx->db) { config_destroy(ctx->config); free(ctx); return NULL; }

    ctx->pool = thread_pool_create(ctx->config->thread_count);
    if (!ctx->pool) {
        database_close(ctx->db);
        config_destroy(ctx->config);
        free(ctx);
        return NULL;
    }

    return ctx;
}

void app_destroy(app_context_t *ctx) {
    if (!ctx) return;
    if (ctx->running) app_stop(ctx);
    thread_pool_destroy(ctx->pool);
    database_close(ctx->db);
    config_destroy(ctx->config);
    free(ctx);
}

int app_start(app_context_t *ctx) {
    if (!ctx || ctx->running) return -1;
    ctx->running = true;
    ctx->server = network_server_create(ctx->config->port, ctx);
    return network_server_start(ctx->server);
}

const char *app_version(void) { return "1.0.0"; }
```

## Examples

### Easy Example: Configuration Loader

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct {
    int port;
    int thread_count;
    char db_path[256];
    char log_path[256];
} config_t;

config_t *config_load(const char *path) {
    config_t *cfg = calloc(1, sizeof(*cfg));
    if (!cfg) return NULL;

    FILE *f = fopen(path, "r");
    if (!f) { free(cfg); return NULL; }

    char line[512];
    while (fgets(line, sizeof(line), f)) {
        if (line[0] == '#' || line[0] == '\n') continue;

        char *eq = strchr(line, '=');
        if (!eq) continue;
        *eq = '\0';

        char *key = line;
        char *val = eq + 1;
        val[strcspn(val, "\n")] = '\0';

        if (strcmp(key, "port") == 0) cfg->port = atoi(val);
        else if (strcmp(key, "threads") == 0) cfg->thread_count = atoi(val);
        else if (strcmp(key, "db_path") == 0) strncpy(cfg->db_path, val, 255);
        else if (strcmp(key, "log_path") == 0) strncpy(cfg->log_path, val, 255);
    }

    fclose(f);
    return cfg;
}

void config_destroy(config_t *cfg) { free(cfg); }
```

### Medium Example: Thread Pool

```c
#include <pthread.h>
#include <stdlib.h>
#include <stdbool.h>

typedef void (*task_fn)(void *arg);

typedef struct task {
    task_fn fn;
    void *arg;
    struct task *next;
} task_t;

typedef struct {
    pthread_t *threads;
    int thread_count;
    task_t *head;
    task_t *tail;
    pthread_mutex_t mutex;
    pthread_cond_t not_empty;
    bool shutdown;
} thread_pool_t;

static void *worker(void *arg) {
    thread_pool_t *pool = arg;
    while (true) {
        pthread_mutex_lock(&pool->mutex);
        while (!pool->head && !pool->shutdown)
            pthread_cond_wait(&pool->not_empty, &pool->mutex);

        if (pool->shutdown && !pool->head) {
            pthread_mutex_unlock(&pool->mutex);
            break;
        }

        task_t *task = pool->head;
        pool->head = task->next;
        if (!pool->head) pool->tail = NULL;
        pthread_mutex_unlock(&pool->mutex);

        task->fn(task->arg);
        free(task);
    }
    return NULL;
}

thread_pool_t *thread_pool_create(int count) {
    thread_pool_t *pool = calloc(1, sizeof(*pool));
    if (!pool) return NULL;
    pool->thread_count = count;
    pool->threads = malloc(count * sizeof(pthread_t));
    pthread_mutex_init(&pool->mutex, NULL);
    pthread_cond_init(&pool->not_empty, NULL);
    for (int i = 0; i < count; i++)
        pthread_create(&pool->threads[i], NULL, worker, pool);
    return pool;
}

bool thread_pool_submit(thread_pool_t *pool, task_fn fn, void *arg) {
    task_t *task = malloc(sizeof(*task));
    if (!task) return false;
    task->fn = fn;
    task->arg = arg;
    task->next = NULL;

    pthread_mutex_lock(&pool->mutex);
    if (pool->tail) pool->tail->next = task;
    else pool->head = task;
    pool->tail = task;
    pthread_cond_signal(&pool->not_empty);
    pthread_mutex_unlock(&pool->mutex);
    return true;
}

void thread_pool_destroy(thread_pool_t *pool) {
    pthread_mutex_lock(&pool->mutex);
    pool->shutdown = true;
    pthread_cond_broadcast(&pool->not_empty);
    pthread_mutex_unlock(&pool->mutex);

    for (int i = 0; i < pool->thread_count; i++)
        pthread_join(pool->threads[i], NULL);

    free(pool->threads);
    pthread_mutex_destroy(&pool->mutex);
    pthread_cond_destroy(&pool->not_empty);
    free(pool);
}
```

### Hard Example: Hash Table with Open Addressing

```c
#include <stdlib.h>
#include <string.h>
#include <stdint.h>

#define HT_EMPTY  0
#define HT_USED   1
#define HT_DELETED 2

typedef struct {
    char *key;
    void *value;
    int state;
} ht_entry_t;

typedef struct {
    ht_entry_t *entries;
    size_t capacity;
    size_t size;
    size_t (*hash)(const char *);
} hash_table_t;

static size_t fnv1a(const char *key) {
    size_t hash = 14695981039346656037ULL;
    for (; *key; key++) {
        hash ^= (uint8_t)*key;
        hash *= 1099511628211ULL;
    }
    return hash;
}

hash_table_t *ht_create(size_t capacity) {
    hash_table_t *ht = malloc(sizeof(*ht));
    if (!ht) return NULL;
    ht->capacity = capacity;
    ht->size = 0;
    ht->hash = fnv1a;
    ht->entries = calloc(capacity, sizeof(ht_entry_t));
    if (!ht->entries) { free(ht); return NULL; }
    return ht;
}

static void ht_resize(hash_table_t *ht, size_t new_cap) {
    ht_entry_t *old = ht->entries;
    size_t old_cap = ht->capacity;
    ht->entries = calloc(new_cap, sizeof(ht_entry_t));
    ht->capacity = new_cap;
    ht->size = 0;
    for (size_t i = 0; i < old_cap; i++) {
        if (old[i].state == HT_USED)
            ht_insert(ht, old[i].key, old[i].value);
        if (old[i].state == HT_USED) free(old[i].key);
    }
    free(old);
}

int ht_insert(hash_table_t *ht, const char *key, void *value) {
    if (ht->size >= ht->capacity * 0.7)
        ht_resize(ht, ht->capacity * 2);

    size_t idx = ht->hash(key) % ht->capacity;
    while (ht->entries[idx].state == HT_USED) {
        if (strcmp(ht->entries[idx].key, key) == 0) {
            ht->entries[idx].value = value;
            return 0;
        }
        idx = (idx + 1) % ht->capacity;
    }
    ht->entries[idx].key = strdup(key);
    ht->entries[idx].value = value;
    ht->entries[idx].state = HT_USED;
    ht->size++;
    return 0;
}

void *ht_get(hash_table_t *ht, const char *key) {
    size_t idx = ht->hash(key) % ht->capacity;
    while (ht->entries[idx].state != HT_EMPTY) {
        if (ht->entries[idx].state == HT_USED &&
            strcmp(ht->entries[idx].key, key) == 0)
            return ht->entries[idx].value;
        idx = (idx + 1) % ht->capacity;
    }
    return NULL;
}

void ht_destroy(hash_table_t *ht) {
    for (size_t i = 0; i < ht->capacity; i++)
        if (ht->entries[i].state == HT_USED) free(ht->entries[i].key);
    free(ht->entries);
    free(ht);
}
```

### Enterprise Example: Custom Memory Allocator

```c
#include <stddef.h>
#include <stdint.h>
#include <string.h>
#include <pthread.h>

#define POOL_SIZE (1024 * 1024)  // 1MB pool

typedef struct block {
    size_t size;
    bool free;
    struct block *next;
} block_t;

typedef struct {
    uint8_t memory[POOL_SIZE];
    block_t *head;
    pthread_mutex_t mutex;
} pool_allocator_t;

static pool_allocator_t global_pool;

void pool_init(void) {
    global_pool.head = (block_t *)global_pool.memory;
    global_pool.head->size = POOL_SIZE - sizeof(block_t);
    global_pool.head->free = true;
    global_pool.head->next = NULL;
    pthread_mutex_init(&global_pool.mutex, NULL);
}

void *pool_malloc(size_t size) {
    pthread_mutex_lock(&global_pool.mutex);
    block_t *curr = global_pool.head;
    while (curr) {
        if (curr->free && curr->size >= size) {
            if (curr->size > size + sizeof(block_t) + 64) {
                block_t *new_block = (block_t *)((uint8_t *)curr + sizeof(block_t) + size);
                new_block->size = curr->size - size - sizeof(block_t);
                new_block->free = true;
                new_block->next = curr->next;
                curr->next = new_block;
                curr->size = size;
            }
            curr->free = false;
            pthread_mutex_unlock(&global_pool.mutex);
            return (uint8_t *)curr + sizeof(block_t);
        }
        curr = curr->next;
    }
    pthread_mutex_unlock(&global_pool.mutex);
    return NULL;
}

void pool_free(void *ptr) {
    if (!ptr) return;
    pthread_mutex_lock(&global_pool.mutex);
    block_t *block = (block_t *)((uint8_t *)ptr - sizeof(block_t));
    block->free = true;
    // Coalesce adjacent free blocks
    block_t *curr = global_pool.head;
    while (curr && curr->next) {
        if (curr->free && curr->next->free) {
            curr->size += sizeof(block_t) + curr->next->size;
            curr->next = curr->next->next;
        } else {
            curr = curr->next;
        }
    }
    pthread_mutex_unlock(&global_pool.mutex);
}

void pool_destroy(void) {
    pthread_mutex_destroy(&global_pool.mutex);
}
```

## Performance Considerations

| Aspect | Consideration | Optimization |
|--------|---------------|--------------|
| Memory allocation | Pool allocator for fixed-size objects | Avoid malloc/free overhead |
| Hash table | Load factor < 0.7 | Resizing with power-of-2 capacities |
| Thread pool | Thread count = CPU cores | Avoid context switch overhead |
| Cache | Struct alignment | Pad to cache line boundaries (64 bytes) |
| Networking | epoll/kqueue | Event-driven I/O, not thread-per-connection |
| Algorithms | Big-O complexity | Choose O(n log n) over O(n^2) |

## Best Practices

- Do:
  - Use opaque pointers for ABI stability
  - Always check malloc return values
  - Initialize all struct members
  - Use `const` for read-only parameters
  - Free resources in reverse order of allocation
  - Use `size_t` for sizes and indices
  
- Don't:
  - Ignore compiler warnings (`-Wall -Wextra -Werror`)
  - Use `gets()` or unchecked `strcpy()`
  - Cast `malloc()` return value in C
  - Use magic numbers; define constants
  - Leave file descriptors or sockets open
  - Mix `malloc/free` with `calloc/realloc`

## Common Mistakes

| Mistake | Consequence | Prevention |
|---------|-------------|------------|
| Double free | Corruption, crash | Set pointer to NULL after free |
| Use-after-free | Undefined behavior | Check all references before free |
| Buffer overflow | Security vulnerability | Use `snprintf`, bounds checking |
| Integer overflow | Wrap-around, security | Check before arithmetic operations |
| Uninitialized memory | Undefined behavior | Use `calloc`, initialize explicitly |

## Interview Questions

### Q1: How would you design a key-value store in C?
**Answer:** Use a hash table with open addressing for O(1) lookups. Implement a write-ahead log for durability. Use a thread pool for concurrent access. Add bloom filters for efficient negative lookups.

### Q2: Explain how you would implement a memory pool allocator.
**Answer:** Pre-allocate a large memory block. Divide into fixed-size chunks. Use a free list for available chunks. Thread-safe with mutex. Coalesce adjacent free blocks to reduce fragmentation.

### Q3: How do you handle concurrency in a C server?
**Answer:** Use event-driven I/O (epoll/kqueue) for high concurrency. Thread pool for CPU-bound work. Producer-consumer pattern for request handling. Use mutexes and condition variables for synchronization.

### Q4: Describe your approach to error handling in C.
**Answer:** Use return codes (0 = success, negative = error). Set `errno` for system call failures. Use `goto` for cleanup in complex functions. Log errors with context. Never ignore errors.

### Q5: How do you ensure memory safety in C?
**Answer:** Use address sanitizer (`-fsanitize=address`). Implement bounds checking. Use `const` correctness. Free resources with `goto cleanup` pattern. Use static analysis tools (Clang Static Analyzer).

### Q6: Explain your approach to building a cross-platform C application.
**Answer:** Platform abstraction layer with function pointers. Compile-time platform detection. Separate platform-specific code into distinct files. Use POSIX APIs where possible; wrap platform-specific calls.

### Q7: How do you design an ABI-stable shared library?
**Answer:** Use opaque pointers. Never expose struct internals in public headers. Version your APIs semantically. Use `__attribute__((visibility("hidden")))` for internal symbols. Maintain backward compatibility.

### Q8: Describe your approach to testing in C.
**Answer:** Use a lightweight test framework (e.g., Unity, Check). Unit test each module independently. Use mock objects for external dependencies. Run tests under Valgrind for memory errors. Achieve 80%+ coverage.

### Q9: How do you optimize a C application for performance?
**Answer:** Profile with `perf`/`gprof`. Optimize hot loops. Use cache-friendly data structures. Avoid unnecessary allocations. Use SIMD intrinsics for bulk operations. Enable compiler optimizations (`-O2`, `-march=native`).

### Q10: How do you handle configuration in a C application?
**Answer:** Parse config file at startup. Use environment variables for overrides. Validate all config values. Provide sensible defaults. Use `getopt` for command-line arguments. Document all options.

### Q11: How do you implement a thread pool in C?
**Answer:** Fixed number of worker threads. Task queue with mutex and condition variable. Workers wait on condition variable. Producer submits tasks to queue. Shutdown with flag and broadcast.

### Q12: Explain your approach to networking in C.
**Answer:** Non-blocking sockets with `epoll`/`kqueue`. Event loop for I/O multiplexing. Connection pooling for clients. Timeout handling. Graceful shutdown. Use `select()` for simple cases.

### Q13: How do you prevent buffer overflows in C?
**Answer:** Use `snprintf` instead of `sprintf`. Bounds-check all array accesses. Use `strncpy` with explicit null termination. Enable compiler stack protector (`-fstack-protector`). Use AddressSanitizer.

### Q14: Describe your approach to logging in a C application.
**Answer:** Use a logging library (e.g., `log.c`). Support multiple log levels. Log to file and stdout. Include timestamps and thread IDs. Use structured logging for machine parsing. Rotate log files.

### Q15: How do you ensure code quality in a C codebase?
**Answer:** Enforce coding standards (CERT C, MISRA C). Use static analysis (Clang-Tidy, Coverity). Run code reviews. Use `-Wall -Wextra -Werror`. Document public APIs. Maintain comprehensive tests.

## Cross-References

- **Previous Module:** [16 - Senior Topics](../16-senior/)
- **Related:** All modules (00-16)
- **External:** [SEI CERT C Coding Standard](https://wiki.sei.cmu.edu/confluence/display/c/)
- **External:** [Modern C (Jens Gustedt)](https://gustedt.gitlabpages.inria.fr/modern-c/)

## Debugging Tips

| Tool | Purpose | Usage |
|------|---------|-------|
| `gdb` | Debugger | `gdb ./program`, `break`, `run`, `backtrace` |
| `valgrind` | Memory errors | `valgrind --leak-check=full ./program` |
| AddressSanitizer | Memory bugs | `-fsanitize=address` |
| `strace` | System calls | `strace ./program` |
| `ltrace` | Library calls | `ltrace ./program` |
| `gprof` | Profiling | `-pg`, then `gprof program gmon.out` |

## Code Review Checklist

- [ ] All functions have clear, documented interfaces
- [ ] No memory leaks (all mallocs have matching frees)
- [ ] No buffer overflows (bounds checking everywhere)
- [ ] Error return values are checked and handled
- [ ] No race conditions in concurrent code
- [ ] Constants used instead of magic numbers
- [ ] `const` used for read-only parameters
- [ ] Public headers use include guards
- [ ] ABI compatibility maintained for shared libraries
- [ ] Platform-specific code is isolated

## Architecture Considerations

| Concern | Approach | Rationale |
|---------|----------|-----------|
| Modularity | Separate .h/.c per module | Clear interfaces, independent compilation |
| ABI Stability | Opaque pointers | Library updates without client recompilation |
| Platform | Abstraction layer | Code portability across OSes |
| Concurrency | Thread pool + event loop | Scalable request handling |
| Memory | Custom pool allocator | Predictable performance, reduced fragmentation |
| Error handling | Return codes + goto cleanup | Consistent, testable error paths |

## Security Considerations

| Risk | Mitigation | Implementation |
|------|------------|----------------|
| Buffer overflow | Bounds checking | `snprintf`, validated array access |
| Integer overflow | Pre-check arithmetic | Validate before multiply/add |
| Use-after-free | NULL after free | Set pointer = NULL immediately |
| Format string | No user input in printf | Use `%s`, never `printf(user_input)` |
| Race conditions | Mutex protection | Lock shared state access |
| DoS | Rate limiting, timeouts | Limit connections, set socket timeouts |

## Production Incidents

### Incident 1: Memory Leak in Long-Running Server

**Problem:** Memory usage grew 10MB/hour until the server was OOM-killed after 48 hours.
**Cause:** Small allocations in a logging function were never freed because the log buffer was overwritten without freeing the old string.
**Impact:** Server crashed every 48 hours; required automated restart; affected 5,000+ connected clients.
**Detection:** `valgrind --leak-check=full` on a long-running test instance revealed the leak pattern.
**Solution:** Changed logging to use a ring buffer with pre-allocated strings; added periodic leak checks.
**Prevention:** Use pool allocators for frequently allocated objects; run AddressSanitizer in CI; monitor RSS in production.

### Incident 2: Buffer Overflow in Protocol Parser

**Problem:** A crafted network packet caused a stack buffer overflow, leading to remote code execution.
**Cause:** Protocol parser used `strcpy` without bounds checking on a variable-length field.
**Impact:** CVE assigned; emergency patch required; 2-hour downtime for all clients; security audit triggered.
**Detection:** Fuzzing with AFL discovered the crash; static analysis later confirmed the vulnerability.
**Solution:** Replaced `strcpy` with `strncpy` with explicit null termination; added length validation for all protocol fields.
**Prevention:** Never use `strcpy`/`sprintf`; use `snprintf`/`strlcpy`; fuzz all network-facing parsers; enable `-fstack-protector-strong`.

### Incident 3: Deadlock Under High Concurrency

**Problem:** Server deadlocked when more than 100 concurrent connections were handled simultaneously.
**Cause:** Two mutexes were acquired in inconsistent order in different code paths (lock ordering violation).
**Impact:** Server became unresponsive; all connected clients timed out; required manual restart.
**Detection:** `gdb` attached to hung process showed threads blocked on mutexes in circular wait.
**Solution:** Established global lock ordering; refactored code to always acquire locks in the same sequence.
**Prevention:** Document lock ordering; use static analysis tools to detect lock ordering violations; add lock timeout with retry.

### Incident 4: Integer Overflow in Size Calculation

**Problem:** Calculating total buffer size for an array of large structs caused integer overflow, allocating a buffer too small.
**Cause:** `count * sizeof(struct)` overflowed `size_t` when count was very large (from malformed input).
**Impact:** Heap buffer overflow; heap corruption; crash or data corruption in subsequent operations.
**Detection:** AddressSanitizer in CI caught the overflow during fuzzing.
**Solution:** Added overflow check: `if (count > SIZE_MAX / sizeof(struct)) return NULL;` before allocation.
**Prevention:** Always check for integer overflow before multiplication in allocation sizes; use safe multiplication helpers.

### Incident 5: Use-After-Free in Connection Cleanup

**Problem:** Server crashed sporadically with SIGSEGV in connection cleanup code.
**Cause:** A connection was freed by one thread while another thread was still using a pointer to it.
**Impact:** Random crashes under load; 0.1% of connections affected; difficult to reproduce.
**Detection:** Core dump analysis with `gdb` showed the freed pointer; ThreadSanitizer confirmed the race.
**Solution:** Added reference counting to connection objects; connections are only freed when reference count reaches zero.
**Prevention:** Use reference counting for shared objects; enable ThreadSanitizer in CI; document ownership semantics.

## Production Checklist

- [ ] All 17 modules integrated into application
- [ ] Proper error handling (return codes + cleanup)
- [ ] Thread-safe code (mutexes, atomic operations)
- [ ] Unit tests with 80%+ coverage
- [ ] Integration tests for network protocols
- [ ] Memory leak testing with Valgrind/ASan
- [ ] Security review completed (fuzzing, static analysis)
- [ ] Logging configured with levels and rotation
- [ ] Configuration externalized (config file + CLI args)
- [ ] Platform abstraction for cross-platform builds
- [ ] ABI-stable API for shared library consumers
- [ ] Performance profiling and optimization completed
- [ ] Documentation complete (README, API docs, man pages)

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Can write basic C programs; follows tutorials; needs guidance on memory management |
| Intermediate | Can implement data structures and algorithms; understands pointers; can review junior code |
| Advanced | Can design systems; makes architecture decisions; mentors team on C-specific concerns |
| Expert | Can design ABI-stable libraries; makes platform-specific optimizations; leads technical direction |

## Common Myths

| Myth | Reality |
|------|---------|
| "C is too low-level for modern projects" | C is used in Linux kernel, databases, embedded systems — it's everywhere |
| "Memory management is too hard" | With proper patterns (pool allocators, RAII-style goto cleanup), it's manageable |
| "No garbage collector means leaks" | Tools like Valgrind and ASan catch leaks; design patterns prevent them |
| "C has no abstraction" | Opaque pointers, function pointers, and struct composition provide powerful abstractions |
| "C is not testable" | Lightweight frameworks (Unity, Check) make C testing straightforward |

## One-Minute Revision

| Module | Key Takeaway for Capstone |
|--------|---------------------------|
| 00 | Integer types, type promotion, bitwise operations |
| 01 | Variables, control flow, string handling |
| 02 | Domain model, struct composition, linked structures |
| 03 | Conditional compilation, macros, build configuration |
| 04 | Configuration loading, data persistence, logging |
| 05 | Function pointers, callbacks, opaque pointers |
| 06 | Hash tables, trees, dynamic arrays |
| 07 | Sorting, searching, graph traversal |
| 08 | Custom allocators, pool management, leak detection |
| 09 | Thread pool, producer-consumer, synchronization |
| 10 | TCP/UDP server, protocol handling, connection management |
| 11 | Input validation, buffer overflow prevention, encryption |
| 12 | Profiling, cache optimization, SIMD, benchmarks |
| 13 | Unit tests, integration tests, test framework |
| 14 | Makefile, CMake, cross-platform builds |
| 15 | Coding standards, documentation, code review |
| 16 | ABI-stable API, dynamic dispatch, architecture decisions |
