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

## Overview

The Senior Topics module covers advanced C engineering: ABI-stable API design, cross-platform abstraction layers, dynamic dispatch, architecture patterns, and senior-level decision-making. This module prepares you for staff and principal engineering roles.

## Learning Objectives

- Design ABI-stable shared library APIs
- Build cross-platform abstraction layers
- Implement dynamic dispatch tables
- Make sound architecture decisions
- Lead technical design reviews

## Prerequisites

- Completion of Module 15 (Best Practices)
- Strong understanding of all previous modules
- 3+ years of C development experience

## History

- **1972** — C created at Bell Labs
- **1985** — First ABI conventions established for Unix
- **1989** — ANSI C standardized
- **1999** — C99 introduced flexible array members
- **2011** — C11 introduced `_Generic`, `_Static_assert`
- **2018** — C18 minor revision
- **2023** — C23 introduces major features (`typeof`, `nullptr`, `#embed`)

## Production Notes

- **Where is it used?** Systems libraries, OS kernels, embedded firmware, game engines
- **Why is it useful?** Defines how senior engineers make long-lasting design decisions
- **When should it be avoided?** Simple projects where KISS applies
- **Alternative?** Higher-level languages trade control for safety

## Core Concepts

### Senior Engineering Skills

| Skill | Description | Impact |
|-------|-------------|--------|
| ABI stability | Maintain binary compatibility | Library evolution without recompilation |
| Platform abstraction | Uniform API across OSes | Portable codebases |
| Dynamic dispatch | Runtime polymorphism in C | Plugin architectures |
| Architecture decisions | Long-term design trade-offs | System longevity |
| Technical leadership | Guide team decisions | Consistent codebase quality |

### Architecture Decision Records

| Component | Purpose | Example |
|-----------|---------|---------|
| Context | Problem statement | "We need cross-platform networking" |
| Decision | What was chosen | "Abstract via function pointers" |
| Consequences | Trade-offs | "Slight runtime overhead, full portability" |

## Internal Working

### ABI Stability Mechanism

```
Public API (stable)
    ↓
Opaque pointer pattern
    ↓
Internal struct (can change)
    ↓
Versioned symbol naming
    ↓
Forward/backward compatibility
```

### Cross-Platform Abstraction Layer

```
Application Code
    ↓
Platform Abstraction API
    ↓┌─────────┬─────────┬─────────┐
  Linux     Windows    macOS     Embedded
    ↓         ↓         ↓         ↓
POSIX APIs  Win32    Cocoa    Bare Metal
```

## Syntax

```c
// ABI-stable API with opaque pointer
// mylib.h (public, stable)
#ifndef MYLIB_H
#define MYLIB_H

#include <stddef.h>

typedef struct mylib_context mylib_context_t;

mylib_context_t *mylib_create(void);
void mylib_destroy(mylib_context_t *ctx);
int mylib_process(mylib_context_t *ctx, const void *data, size_t len);
const char *mylib_version(void);

#endif

// mylib.c (private, can change)
#include "mylib.h"
#include <stdlib.h>
#include <string.h>

struct mylib_context {
    int version;
    char *buffer;
    size_t capacity;
    size_t length;
};

mylib_context_t *mylib_create(void) {
    mylib_context_t *ctx = calloc(1, sizeof(*ctx));
    if (!ctx) return NULL;
    ctx->version = 1;
    ctx->capacity = 4096;
    ctx->buffer = malloc(ctx->capacity);
    if (!ctx->buffer) { free(ctx); return NULL; }
    return ctx;
}

void mylib_destroy(mylib_context_t *ctx) {
    if (!ctx) return;
    free(ctx->buffer);
    free(ctx);
}

const char *mylib_version(void) { return "1.2.3"; }

// Dynamic dispatch table
typedef struct {
    const char *name;
    int (*init)(void *ctx);
    int (*execute)(void *ctx, const void *input, size_t len);
    void (*cleanup)(void *ctx);
} plugin_ops_t;

static plugin_ops_t plugins[] = {
    { .name = "compress", .init = compress_init, .execute = compress_run, .cleanup = compress_free },
    { .name = "encrypt",  .init = encrypt_init,  .execute = encrypt_run,  .cleanup = encrypt_free  },
    { .name = NULL }
};

const plugin_ops_t *plugin_find(const char *name) {
    for (int i = 0; plugins[i].name; i++) {
        if (strcmp(plugins[i].name, name) == 0) return &plugins[i];
    }
    return NULL;
}
```

## Examples

### Easy Example: Version Negotiation

```c
#include <stdio.h>
#include <stdint.h>

#define MYLIB_API_VERSION 3
#define MYLIB_MIN_VERSION 2

int negotiate_version(int client_version) {
    if (client_version < MYLIB_MIN_VERSION) {
        fprintf(stderr, "Client version %d too old (need %d)\n",
                client_version, MYLIB_MIN_VERSION);
        return -1;
    }
    if (client_version > MYLIB_API_VERSION) {
        fprintf(stderr, "Client version %d too new (max %d)\n",
                client_version, MYLIB_API_VERSION);
        return -1;
    }
    printf("Negotiated version %d\n", client_version);
    return client_version;
}

int main(void) {
    negotiate_version(2);  // OK
    negotiate_version(3);  // OK
    negotiate_version(1);  // Fails
    return 0;
}
```

### Medium Example: Platform Abstraction

```c
// platform.h — uniform API
#ifndef PLATFORM_H
#define PLATFORM_H

#include <stddef.h>

typedef struct {
    const char *(*get_name)(void);
    int (*get_cpu_count)(void);
    size_t (*get_page_size)(void);
    int (*get_endian)(void);  // 0=LE, 1=BE
} platform_ops_t;

const platform_ops_t *platform_get(void);

#endif

// platform_linux.c
#include "platform.h"
#include <unistd.h>
#include <endian.h>

static const char *linux_name(void) { return "Linux"; }
static int linux_cpu_count(void) { return (int)sysconf(_SC_NPROCESSORS_ONLN); }
static size_t linux_page_size(void) { return (size_t)sysconf(_SC_PAGESIZE); }
static int linux_endian(void) {
#if __BYTE_ORDER == __LITTLE_ENDIAN
    return 0;
#else
    return 1;
#endif
}

static const platform_ops_t linux_ops = {
    .get_name = linux_name,
    .get_cpu_count = linux_cpu_count,
    .get_page_size = linux_page_size,
    .get_endian = linux_endian
};

const platform_ops_t *platform_get(void) { return &linux_ops; }
```

### Hard Example: Plugin Architecture

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Plugin interface
typedef struct plugin {
    const char *name;
    const char *version;
    int (*init)(struct plugin *self);
    int (*process)(struct plugin *self, const char *input, char *output, size_t out_len);
    void (*destroy)(struct plugin *self);
    void *private_data;
} plugin_t;

// Plugin registry
#define MAX_PLUGINS 32
static plugin_t *registry[MAX_PLUGINS];
static int plugin_count = 0;

int plugin_register(plugin_t *p) {
    if (plugin_count >= MAX_PLUGINS) return -1;
    registry[plugin_count++] = p;
    return 0;
}

plugin_t *plugin_find_by_name(const char *name) {
    for (int i = 0; i < plugin_count; i++) {
        if (strcmp(registry[i]->name, name) == 0) return registry[i];
    }
    return NULL;
}

int plugin_init_all(void) {
    for (int i = 0; i < plugin_count; i++) {
        if (registry[i]->init(registry[i]) != 0) {
            fprintf(stderr, "Failed to init plugin: %s\n", registry[i]->name);
            return -1;
        }
    }
    return 0;
}

void plugin_destroy_all(void) {
    for (int i = 0; i < plugin_count; i++) {
        registry[i]->destroy(registry[i]);
    }
    plugin_count = 0;
}
```

### Enterprise Example: Semantic Versioning and ABI Guarantees

```c
// Versioned ABI with forward compatibility
typedef struct {
    uint32_t magic;       // 0x4D594C42 ("MYLB")
    uint16_t major;       // Breaking changes
    uint16_t minor;       // New features (backward compatible)
    uint16_t patch;       // Bug fixes only
    uint16_t reserved;    // Must be 0
} mylib_header_t;

// Layout-compatible struct across versions
typedef struct {
    mylib_header_t hdr;
    uint64_t flags;       // v1.0+
    uint32_t max_items;   // v1.1+
    // New fields go here in future versions
} mylib_config_t;

#define MYLIB_MAGIC    0x4D594C42
#define MYLIB_VERSION_MAJOR 2
#define MYLIB_VERSION_MINOR 1
#define MYLIB_VERSION_PATCH 0

int mylib_config_init(mylib_config_t *cfg) {
    memset(cfg, 0, sizeof(*cfg));
    cfg->hdr.magic = MYLIB_MAGIC;
    cfg->hdr.major = MYLIB_VERSION_MAJOR;
    cfg->hdr.minor = MYLIB_VERSION_MINOR;
    cfg->hdr.patch = MYLIB_VERSION_PATCH;
    cfg->max_items = 1000;
    return 0;
}

int mylib_config_validate(const mylib_config_t *cfg) {
    if (cfg->hdr.magic != MYLIB_MAGIC) return -1;
    if (cfg->hdr.major != MYLIB_VERSION_MAJOR) return -2;
    return 0;
}
```

## Performance Considerations

| Aspect | Consideration | Optimization |
|--------|---------------|--------------|
| Dynamic dispatch | Indirect function call overhead | Inline hot paths |
| ABI stability | Extra indirection | Opaque pointers |
| Platform abstraction | Virtual dispatch | Compile-time selection |
| Version checks | Runtime cost | Cache at init time |
| Plugin loading | dlopen overhead | Pre-link known plugins |

## Best Practices

- Do:
  - Use opaque pointers for ABI stability
  - Version your public APIs semantically
  - Write Architecture Decision Records (ADRs)
  - Design for backward compatibility
  - Use function pointer tables for extensibility
  
- Don't:
  - Expose struct layouts in public headers
  - Break ABI without bumping major version
  - Use platform-specific code in core logic
  - Skip version negotiation at load time
  - Make architecture decisions without documentation

## Common Mistakes

| Mistake | Consequence | Prevention |
|---------|-------------|------------|
| Exposing struct internals | ABI breaks on layout change | Use opaque pointers |
| No versioning | Unmanageable compatibility | Semantic versioning |
| Hardcoded platform code | Portability failures | Abstract layer |
| Ignoring alignment | Crash on some architectures | Use aligned allocations |
| Skipping ADRs | Lost design context | Document all major decisions |

## Interview Questions

### Q1: What is ABI stability and why does it matter?
**Answer:** ABI (Application Binary Interface) stability ensures compiled libraries remain compatible without recompilation. Critical for shared libraries distributed to third parties.

### Q2: What is the opaque pointer pattern?
**Answer:** Forward-declaring a struct in the public header but defining it only in the implementation. Changes to internal layout don't break ABI.

### Q3: What is semantic versioning and how does it apply to C libraries?
**Answer:** `MAJOR.MINOR.PATCH` — MAJOR: breaking ABI/API changes. MINOR: backward-compatible features. PATCH: bug fixes. Libraries must follow this for compatibility.

### Q4: How do you implement runtime polymorphism in C?
**Answer:** Function pointer tables (vtables). A struct contains pointers to functions, allowing different implementations to be swapped at runtime.

### Q5: What is an Architecture Decision Record?
**Answer:** A lightweight document capturing context, decision, and consequences of a technical design choice. Ensures design rationale is preserved.

### Q6: How do you handle cross-platform compilation?
**Answer:** Platform abstraction layer with function pointers or compile-time macros. Separate platform-specific code into distinct translation units.

### Q7: What is the difference between static and dynamic linking?
**Answer:** Static: library code copied into binary at compile time. Dynamic: library loaded at runtime. Dynamic enables shared code but adds dependency management.

### Q8: What are the risks of dlopen/dlsym?
**Answer:** Symbol not found at runtime, version mismatches, memory leaks if not properly managed, security risks from loading untrusted code.

### Q9: How do you maintain backward compatibility in a C API?
**Answer:** Never remove or reorder existing functions, use versioned symbols, add new functions rather than modifying existing ones, use feature flags.

### Q10: What is the purpose of `__attribute__((visibility))`?
**Answer:** Controls symbol visibility in shared libraries. `default`: exported. `hidden`: internal. Reduces ABI surface and improves load time.

### Q11: What is a design pattern for plugin systems in C?
**Answer:** Registry pattern: plugins register function pointers at load time. Host iterates registry to invoke plugins. Each plugin implements a standard interface.

### Q12: What is the difference between HAL and platform abstraction?
**Answer:** HAL: hardware abstraction layer (direct hardware). Platform abstraction: OS-level abstraction (file I/O, threading). Both serve portability at different levels.

### Q13: How do you test ABI compatibility?
**Answer:** Use tools like `abi-compliance-checker`, `abidiff`, or `libabigail`. Compare header files and symbol lists between versions.

### Q14: What is the purpose of symbol versioning in ELF?
**Answer:** Allows multiple versions of a function to coexist in a shared library. Old binaries use old version; new binaries use new version.

### Q15: What is the role of a tech lead in C project architecture?
**Answer:** Defines coding standards, reviews architecture decisions, mentors team on C-specific concerns (memory, ABI, portability), ensures long-term maintainability.

## Cross-References

- **Previous Module:** [15 - Best Practices](../15-best-practices/)
- **Related:** [05 - Pointers Advanced](../05-pointers-advanced/) — Opaque pointers
- **Related:** [08 - Memory Management](../08-memory-management/) — Memory patterns
- **Related:** [09 - Concurrency](../09-concurrency/) — Thread-safe design
- **Related:** [11 - Security](../11-security/) — Secure API design
- **External:** [Expert C Programming: Deep C Secrets](https://www.amazon.com/Expert-C-Programming-Deep-Secrets/dp/0131774298)
- **External:** [CERT C Coding Standard](https://wiki.sei.cmu.edu/confluence/display/c/)
