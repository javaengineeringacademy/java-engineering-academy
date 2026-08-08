# Senior Level — C Language

## Why It Matters

When you're leading C engineering teams, challenges go beyond writing correct code: designing systems that last years, making technology decisions affecting entire organizations, mentoring teams, and balancing competing constraints — performance vs maintainability, features vs stability, speed vs correctness. This module bridges the gap between writing code and leading engineering.

## Engineering Decision Framework

| Factor | Use This | Consider Alternatives |
|--------|----------|----------------------|
| When to use | Architectural decisions, ABI-stable APIs, cross-platform layers | Simple code for small, isolated modules |
| When NOT to use | Over-engineering for current needs | Start simple, refactor as needed |
| Alternatives | Design reviews, ADRs, pair programming | Different approaches to decision-making |
| Production Examples | Linux kernel ABI stability, OpenSSL versioning, database engines | Opaque pointers for ABI stability |
| Common Mistakes | ABI breaks from struct layout changes, tight coupling, no ADRs | Append-only structs, modular design, document decisions |

## What It Is

Senior-level C development encompasses:

| Area | Skills |
|------|--------|
| Architecture | System design, module boundaries, API design |
| ABI Stability | Opaque pointers, versioning, compatibility |
| Cross-platform | Portability layers, feature detection |
| Performance | Profiling, optimization, cache-aware design |
| Code generation | Macros, metaprogramming, X-macros |
| Leadership | Code reviews, mentoring, technical decisions |
| Operations | Monitoring, debugging production systems |

## Why It Exists

Senior developers exist because:
- Someone must make architectural decisions that affect the entire codebase
- Someone must ensure code quality across a team
- Someone must balance competing technical constraints
- Someone must mentor junior developers
- Someone must handle production incidents

### Architecture: Module Design Pattern

```c
// interface.h — Public API (stable ABI)
#ifndef MODULE_H
#define MODULE_H

typedef struct Module Module;

Module *module_create(const char *config);
void module_destroy(Module *m);
int module_process(Module *m, const char *input, char *output, size_t output_size);
const char *module_error(const Module *m);

#endif

// implementation.c — Private details (can change freely)
#include "interface.h"
#include <stdlib.h>
#include <string.h>

struct Module {
    char *config;
    char *error;
    // Private members — not exposed in header
    int state;
    void *internal_buffer;
    size_t buffer_size;
};

Module *module_create(const char *config) {
    Module *m = malloc(sizeof(Module));
    if (!m) return NULL;

    m->config = strdup(config);
    m->error = NULL;
    m->state = 0;
    m->internal_buffer = NULL;
    m->buffer_size = 0;

    if (!m->config) {
        free(m);
        return NULL;
    }
    return m;
}

void module_destroy(Module *m) {
    if (m) {
        free(m->config);
        free(m->error);
        free(m->internal_buffer);
        free(m);
    }
}
```

## Expanded Code Examples

### ABI-Stable API Design

```c
// Version info for ABI compatibility
#define MODULE_VERSION_MAJOR 2
#define MODULE_VERSION_MINOR 1
#define MODULE_VERSION_PATCH 0

// Version check macro
#define MODULE_CHECK_VERSION(major, minor, patch) \
    ((major) < MODULE_VERSION_MAJOR || \
     ((major) == MODULE_VERSION_MAJOR && (minor) < MODULE_VERSION_MINOR) || \
     ((major) == MODULE_VERSION_MAJOR && (minor) == MODULE_VERSION_MINOR && (patch) <= MODULE_VERSION_PATCH))

// Opaque pointer pattern (ABI stable)
typedef struct Database Database;

Database *db_open(const char *path, int *version);
int db_query(Database *db, const char *sql, void **result);
void db_close(Database *db);
const char *db_error(Database *db);

// Versioned function pointers for future extensibility
typedef struct {
    int version;
    int (*open)(Database **db, const char *path);
    int (*query)(Database *db, const char *sql, void **result);
    void (*close)(Database *db);
} DatabaseVTable;
```

### Cross-Platform Abstraction Layer

```c
// platform.h — Platform abstraction
#ifndef PLATFORM_H
#define PLATFORM_H

#ifdef _WIN32
    #include <windows.h>
    typedef CRITICAL_SECTION PlatformMutex;
    typedef HANDLE PlatformThread;
    #define mutex_init(m) InitializeCriticalSection(m)
    #define mutex_lock(m) EnterCriticalSection(m)
    #define mutex_unlock(m) LeaveCriticalSection(m)
    #define mutex_destroy(m) DeleteCriticalSection(m)
    #define thread_create(t, f, a) CreateThread(NULL, 0, (LPTHREAD_START_ROUTINE)(f), a, 0, NULL)
    #define thread_join(t) WaitForSingleObject(t, INFINITE)
    #define msleep(ms) Sleep(ms)
    #define PATH_SEP "\\"
#else
    #include <pthread.h>
    #include <unistd.h>
    #include <time.h>
    typedef pthread_mutex_t PlatformMutex;
    typedef pthread_t PlatformThread;
    #define mutex_init(m) pthread_mutex_init(m, NULL)
    #define mutex_lock(m) pthread_mutex_lock(m)
    #define mutex_unlock(m) pthread_mutex_unlock(m)
    #define mutex_destroy(m) pthread_mutex_destroy(m)
    #define thread_create(t, f, a) pthread_create(t, NULL, f, a)
    #define thread_join(t) pthread_join(t, NULL)
    #define msleep(ms) usleep((ms) * 1000)
    #define PATH_SEP "/"
#endif

#endif
```

### Dynamic Dispatch Table

```c
#include <stdio.h>
#include <string.h>

// Plugin interface
typedef int (*PluginInit)(void);
typedef int (*PluginExecute)(const char *input, char *output, size_t output_size);
typedef void (*PluginShutdown)(void);

typedef struct {
    const char *name;
    const char *version;
    PluginInit init;
    PluginExecute execute;
    PluginShutdown shutdown;
} Plugin;

// Plugin registry
typedef struct {
    Plugin *plugins[32];
    int count;
} PluginRegistry;

void registry_init(PluginRegistry *reg) {
    reg->count = 0;
}

int registry_register(PluginRegistry *reg, Plugin *plugin) {
    if (reg->count >= 32) return -1;
    if (plugin->init() != 0) return -2;
    reg->plugins[reg->count++] = plugin;
    return 0;
}

Plugin *registry_find(PluginRegistry *reg, const char *name) {
    for (int i = 0; i < reg->count; i++) {
        if (strcmp(reg->plugins[i]->name, name) == 0) {
            return reg->plugins[i];
        }
    }
    return NULL;
}

int registry_execute(PluginRegistry *reg, const char *name,
                     const char *input, char *output, size_t output_size) {
    Plugin *p = registry_find(reg, name);
    if (!p) return -1;
    return p->execute(input, output, output_size);
}
```

### Code Review Checklist

```c
/**
 * CODE REVIEW CHECKLIST FOR C CODE
 *
 * Security:
 * - [ ] All input validated
 * - [ ] Buffer bounds checked
 * - [ ] Integer overflow checked
 * - [ ] Format strings use %s, not user input
 * - [ ] No use of gets(), sprintf(), strcpy()
 *
 * Memory:
 * - [ ] All malloc/calloc/realloc return values checked
 * - [ ] All allocated memory freed
 * - [ ] No use-after-free
 * - [ ] No double-free
 * - [ ] Pointers set to NULL after free
 *
 * Error Handling:
 * - [ ] All return values checked
 * - [ ] Error messages are informative
 * - [ ] Resources cleaned up on error paths
 * - [ ] No silent failures
 *
 * Concurrency:
 * - [ ] Shared data protected by mutex
 * - [ ] Lock ordering documented and followed
 * - [ ] No potential deadlocks
 * - [ ] Atomics used where appropriate
 *
 * Maintainability:
 * - [ ] Functions under 50 lines
 * - [ ] Single responsibility per function
 * - [ ] Descriptive naming
 * - [ ] Public interfaces documented
 * - [ ] No magic numbers (use named constants)
 */
```

### Performance Monitoring

```c
#include <stdio.h>
#include <time.h>

typedef struct {
    struct timespec start;
    struct timespec end;
    const char *name;
} Timer;

void timer_start(Timer *t) {
    clock_gettime(CLOCK_MONOTONIC, &t->start);
}

void timer_stop(Timer *t) {
    clock_gettime(CLOCK_MONOTONIC, &t->end);
}

double timer_elapsed_ms(const Timer *t) {
    double seconds = (t->end.tv_sec - t->start.tv_sec);
    double nanos = (t->end.tv_nsec - t->start.tv_nsec) / 1e9;
    return (seconds + nanos) * 1000.0;
}

// Usage
void performance_critical_function(void) {
    Timer t = {.name = "critical_function"};
    timer_start(&t);

    // ... work ...

    timer_stop(&t);
    printf("%s: %.3f ms\n", t.name, timer_elapsed_ms(&t));
}

// Compile-time assertions for structure sizes
_Static_assert(sizeof(int) == 4, "int must be 32 bits");
_Static_assert(sizeof(void *) >= 4, "pointers must be at least 32 bits");
_Static_assert(_Alignof(max_align_t) >= 16, "max alignment must be at least 16");
```

## Production Incidents

### Incident 1: ABI Break in Shared Library

**Problem**: Upgrading a shared library breaks all applications using it.

**Cause**: Struct layout changed, adding a member in the middle:

```c
// Version 1.0
struct Config {
    int port;      // offset 0
    int timeout;   // offset 4
};

// Version 2.0 (BREAKING: added member in middle)
struct Config {
    int port;      // offset 0
    int max_conn;  // offset 4 (NEW)
    int timeout;   // offset 8 (MOVED)
};
```

**Solution**: Use opaque pointers and append new members at the end:

```c
// Version 2.0 (COMPATIBLE)
struct Config {
    int port;      // offset 0 (unchanged)
    int timeout;   // offset 4 (unchanged)
    int max_conn;  // offset 8 (new, appended)
};
```

### Incident 2: Architecture Decision Reversal

**Problem**: Early architectural decision (single-threaded) cannot be changed without rewriting the entire codebase.

**Cause**: No modular architecture, tight coupling between components.

**Solution**: Design for change:

```c
// Abstract the execution model
typedef struct {
    int (*execute)(void *task);
    void (*complete)(void *result);
} Executor;

// Can swap implementations without changing callers
Executor *create_threaded_executor(int num_threads);
Executor *create_sequential_executor(void);
Executor *create_process_pool_executor(int num_workers);
```

## Production Checklist

- [ ] Design for ABI stability (opaque pointers, append-only structs)
- [ ] Use cross-platform abstraction layers
- [ ] Document architectural decisions (ADRs)
- [ ] Code review all changes
- [ ] Profile before optimizing
- [ ] Monitor production metrics
- [ ] Plan for failure modes
- [ ] Mentor junior developers
- [ ] Write postmortems for incidents
- [ ] Keep dependencies minimal

## Maturity Levels

| Level | Description | Indicators |
|-------|-------------|------------|
| **Advanced** | Solves complex problems | Implements advanced patterns |
| **Senior** | Designs systems and leads teams | Makes architectural decisions |
| **Expert** | Defines standards and mentors | Influences organization-wide practices |

## Common Myths Debunked

1. **Myth**: Senior means knowing everything
   **Truth**: Senior means knowing how to find solutions, delegate effectively, and make good trade-offs.

2. **Myth**: Architecture is upfront design
   **Truth**: Good architecture evolves with requirements. Start simple, refactor as needed, document decisions.

3. **Myth**: Code should be written once
   **Truth**: Code is rewritten and refactored constantly. Design for change, not permanence.

4. **Myth**: Technical debt doesn't matter
   **Truth**: Technical debt compounds. Regular refactoring prevents codebase degradation.

## One-Minute Revision

| Concept | Description | Key Detail |
|---------|-------------|------------|
| Architecture | System design and structure | Opaque pointers, module boundaries |
| ABI | Application Binary Interface | Stable across library versions |
| Portability | Cross-platform compatibility | Abstraction layers |
| Optimization | Performance improvement | Profile first, measure after |
| Leadership | Guiding teams and decisions | Code reviews, mentoring |
| Review | Quality assurance process | Checklists, automation |
| Postmortem | Learn from incidents | Document, prevent recurrence |
| ADR | Architecture Decision Record | Document why decisions were made |

## Related Topics

- [Best Practices](../15-best-practices/README.md) — Coding standards and patterns
- [Performance](../12-performance/README.md) — Optimization techniques
- [Build Systems](../14-build-systems/README.md) — Cross-platform build management
- [Security](../11-security/README.md) — Security architecture

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| ABI break in shared library (binary incompatibility) | `pahole` / `dwarfdump` | Inspect struct layout with `pahole -C struct_name lib.so`; compare layouts across versions |
| Production crash without debug symbols | `addr2line -e program address` | Convert crash addresses to source locations; use `-g -rdynamic` in release for symbol resolution |
| Architecture drift (code doesn't match design) | Architecture review + ADR audit | Compare code structure against Architecture Decision Records; identify divergence |
| Performance regression after refactor | `git bisect` + profiling | Use `git bisect` to find regression commit; profile before/after with `perf` |
| Thread-safety issues in production | ThreadSanitizer in staging | Compile with `-fsanitize=thread`; run production-like workload in staging environment |

## Code Review Checklist

- [ ] Security: All input validated, buffer bounds checked, integer overflow checked
- [ ] Memory: All malloc/calloc/realloc checked, all freed, no use-after-free, no double-free
- [ ] Error Handling: All return values checked, resources cleaned up on error paths, no silent failures
- [ ] Concurrency: Shared data protected by mutex, lock ordering documented, no potential deadlocks
- [ ] Maintainability: Functions under 50 lines, single responsibility, descriptive naming
- [ ] Architecture: Opaque pointers for ABI stability, modular design, minimal coupling
- [ ] API Design: All public interfaces documented, versioned, backward-compatible

## Architecture Considerations

Senior-level architecture in C focuses on designing systems that last years and survive many developers. Key principles: opaque pointers for ABI stability, modular design with clear boundaries, cross-platform abstraction layers, and architectural decision records (ADRs) to document why decisions were made. Design for change — code will be rewritten and refactored constantly.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Opaque pointer + vtable | ABI-stable library APIs | Internal changes don't break consumers; but prevents stack allocation |
| Platform abstraction layer | Cross-platform code | Single codebase for multiple OS; but adds indirection and potential performance overhead |
| Architecture Decision Record | Documenting design decisions | Preserves institutional knowledge; but requires discipline to maintain |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| ABI break exposing internal state | Binary incompatibility, potential exploitation | Use opaque pointers; never expose struct internals in public headers |
| Missing input validation at API boundaries | Vulnerabilities propagated through system | Validate at every module boundary; use centralized validation functions |
| Insecure defaults in configuration | System deployed with weak security | Secure-by-default configuration; require explicit opt-in for weaker settings |

## Evolution & Modernization

| Era | Change | Migration Path |
|-----|--------|----------------|
| C89 → C99 | Added `bool`, `restrict`, `inline`, flexible array members | Adopt `stdbool.h`, add `restrict` to hot-path pointers, use flexible arrays |
| C99 → C11 | Added `<stdatomic.h>`, `<threads.h>`, `_Static_assert`, `_Noreturn` | Use atomics for concurrent code, `_Static_assert` for compile-time checks |
| C11 → C23 | Added `typeof`, `constexpr`, `#embed`, improved `_Generic` | Use `typeof` for type-generic code, `constexpr` for compile-time constants |

## Version Validation

| Feature | C Standard | Status |
|---------|-----------|--------|
| Opaque pointers (forward declaration) | C89 | Standard — use for ABI-stable APIs |
| `_Static_assert` for structure validation | C11 | Standard — use for compile-time layout checks |
| `<stdatomic.h>` for concurrent code | C11 | Standard — use for lock-free operations |
| `typeof` for type-generic operations | C23 (standardized) | Use for type-safe macros and abstractions |

## Interview Questions

1. **How do you ensure ABI stability across library versions?**: Use opaque pointers (forward-declare structs in public headers, define in implementation). Never add/remove/reorder struct members in public APIs. Append new members at the end. Use versioned function pointers (vtables) for extensibility.
2. **What is an Architecture Decision Record (ADR)?**: An ADR documents a significant architectural decision: the context, the decision, and the consequences. Format: Title, Status, Context, Decision, Consequences. ADRs preserve institutional knowledge and prevent repeated debates.
3. **How do you balance performance with maintainability?**: Profile first to identify real bottlenecks. Optimize only hot paths (top 1% of code). Keep optimization changes localized and well-documented. Use abstraction layers to isolate performance-critical code from business logic.
4. **How do you design for failure in C systems?**: Plan for: allocation failure (check every `malloc`), I/O failure (handle every `fread`/`fwrite`), network failure (timeout and retry), and corruption (checksums, CRC). Use fail-safe defaults — deny by default, grant access only when validated.
5. **How do you mentor junior C developers?**: Focus on: memory safety (use ASan/Valgrind from day one), defensive programming (check every return value), code review (teach by reviewing), and debugging skills (GDB, `strace`, `perf`). Establish coding standards early and enforce through automated tools.

## References

- [C Standard (N3220)](https://www.open-std.org/jtc1/sc22/wg14/www/docs/n3220.pdf)
- [Expert C Programming: Deep C Secrets (van der Linden)](https://www.amazon.com/Expert-C-Programming-Deep-Secrets/dp/0131774298)
- [Secure Coding in C and CERT C Coding Standard](https://wiki.sei.cmu.edu/confluence/display/c/)
