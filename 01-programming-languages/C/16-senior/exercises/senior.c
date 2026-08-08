/*
 * Exercise: Senior Level C Development
 * Difficulty: ★★★★★ (5/5)
 * Learning Objectives:
 *   - Design ABI-stable library interfaces with opaque pointers
 *   - Write cross-platform code using abstraction layers
 *   - Implement dynamic dispatch tables for plugin systems
 *   - Apply architecture decision records in practice
 *   - Profile and optimize performance-critical code
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

/* ============================================================
 * Exercise 1: ABI-Stable Library Design
 *
 * Design a database library using opaque pointers so the internal
 * struct layout can change between versions without breaking ABI.
 *
 * Requirements:
 * - Use opaque pointer pattern (typedef struct DB DB;)
 * - Provide create, destroy, insert, query, and error functions
 * - Include version macros (MAJOR, MINOR, PATCH)
 * - Ensure the public header exposes NO internal fields
 * ============================================================ */

/* TODO: Define version macros */
/* #define DB_VERSION_MAJOR ? */
/* #define DB_VERSION_MINOR ? */
/* #define DB_VERSION_PATCH ? */

/* TODO: Declare opaque type and public API */
/* typedef struct DB DB; */

/* DB *db_create(const char *path); */
/* void db_destroy(DB *db); */
/* int db_insert(DB *db, const char *key, const char *value); */
/* const char *db_query(DB *db, const char *key); */
/* const char *db_error(DB *db); */

void exercise1_abi_stable(void) {
    printf("--- Exercise 1: ABI-Stable Library ---\n");
    printf("TODO: Implement opaque pointer DB library\n\n");
}

/* ============================================================
 * Exercise 2: Cross-Platform Abstraction
 *
 * Create a platform abstraction layer that works on Linux, macOS,
 * and Windows for: mutexes, threads, sleep, and path separators.
 *
 * Requirements:
 * - Use #ifdef to detect platform (_WIN32, __APPLE__, __linux__)
 * - Define PlatformMutex, PlatformThread typedefs
 * - Create macros: mutex_init, mutex_lock, mutex_unlock, mutex_destroy
 * - Create macros: thread_create, thread_join, msleep
 * - Define PATH_SEP correctly per platform
 * ============================================================ */

/* TODO: Implement cross-platform abstraction layer */
/* #ifdef _WIN32 */
/*     typedef CRITICAL_SECTION PlatformMutex; */
/*     #define mutex_init(m) InitializeCriticalSection(m) */
/*     ... etc ... */
/* #else */
/*     typedef pthread_mutex_t PlatformMutex; */
/*     #define mutex_init(m) pthread_mutex_init(m, NULL) */
/*     ... etc ... */
/* #endif */

void exercise2_cross_platform(void) {
    printf("--- Exercise 2: Cross-Platform Abstraction ---\n");
    printf("TODO: Implement platform abstraction layer\n\n");
}

/* ============================================================
 * Exercise 3: Dynamic Dispatch / Plugin System
 *
 * Implement a plugin registry with dynamic dispatch using
 * function pointers.
 *
 * Requirements:
 * - Define a Plugin struct with: name, version, init, execute, shutdown
 * - Create a PluginRegistry that holds up to 32 plugins
 * - Implement: registry_init, registry_register, registry_find,
 *   registry_execute, registry_shutdown_all
 * - Each plugin's init() is called on register, shutdown() on cleanup
 * ============================================================ */

typedef int (*PluginInit)(void);
typedef int (*PluginExecute)(const char *input, char *output, size_t output_size);
typedef void (*PluginShutdown)(void);

/* TODO: Define Plugin struct */
/* typedef struct { */
/*     const char *name; */
/*     const char *version; */
/*     PluginInit init; */
/*     PluginExecute execute; */
/*     PluginShutdown shutdown; */
/* } Plugin; */

/* TODO: Define PluginRegistry */
/* typedef struct { */
/*     Plugin *plugins[32]; */
/*     int count; */
/* } PluginRegistry; */

/* TODO: Implement these functions */
/* void registry_init(PluginRegistry *reg); */
/* int registry_register(PluginRegistry *reg, Plugin *plugin); */
/* Plugin *registry_find(PluginRegistry *reg, const char *name); */
/* int registry_execute(PluginRegistry *reg, const char *name, */
/*                      const char *input, char *output, size_t output_size); */
/* void registry_shutdown_all(PluginRegistry *reg); */

void exercise3_dynamic_dispatch(void) {
    printf("--- Exercise 3: Dynamic Dispatch / Plugin System ---\n");
    printf("TODO: Implement plugin registry with function pointers\n\n");
}

/* ============================================================
 * Exercise 4: Architecture Decision Record (ADR)
 *
 * Document an architecture decision using the ADR template.
 * Scenario: Choosing between a linked list and a dynamic array
 * for a configuration store that is read-heavy, write-rare.
 *
 * Write an ADR covering:
 * - Title
 * - Status (proposed / accepted / deprecated / superseded)
 * - Context (what is the issue)
 * - Decision (what was decided)
 * - Consequences (positive and negative outcomes)
 * ============================================================ */

/* TODO: Write an ADR as a C string constant and print it */
/*
static const char *ADR_TEMPLATE =
    "ADR-001: Data Structure for Configuration Store\n"
    "Status: Accepted\n"
    "Context: ...\n"
    "Decision: ...\n"
    "Consequences: ...\n";
*/

void exercise4_adr(void) {
    printf("--- Exercise 4: Architecture Decision Record ---\n");
    printf("TODO: Write an ADR for a data structure choice\n\n");
}

/* ============================================================
 * Exercise 5: Performance Profiling
 *
 * Implement a simple timer and use it to compare two approaches
 * to the same problem (e.g., bubble sort vs selection sort on
 * the same data).
 *
 * Requirements:
 * - Implement Timer struct using clock_gettime(CLOCK_MONOTONIC, ...)
 * - timer_start(), timer_stop(), timer_elapsed_ms()
 * - Generate a random array of 1000 integers
 * - Sort with bubble sort, measure time
 * - Sort with selection sort, measure time
 * - Print both times and declare the winner
 * ============================================================ */

typedef struct {
    struct timespec start;
    struct timespec end;
    const char *name;
} Timer;

/* TODO: Implement timer functions */
/* void timer_start(Timer *t); */
/* void timer_stop(Timer *t); */
/* double timer_elapsed_ms(const Timer *t); */

/* TODO: Implement bubble sort */
/* void bubble_sort(int *arr, int n); */

/* TODO: Implement selection sort */
/* void selection_sort(int *arr, int n); */

void exercise5_performance(void) {
    printf("--- Exercise 5: Performance Profiling ---\n");
    printf("TODO: Implement timer and compare sort algorithms\n\n");
}

int main(void) {
    printf("====================================\n");
    printf("  Senior Level — Exercises\n");
    printf("====================================\n\n");

    exercise1_abi_stable();
    exercise2_cross_platform();
    exercise3_dynamic_dispatch();
    exercise4_adr();
    exercise5_performance();

    printf("Review each exercise and implement the TODO sections.\n");
    printf("Focus on: ABI safety, portability, extensibility, and measurement.\n\n");

    return 0;
}
