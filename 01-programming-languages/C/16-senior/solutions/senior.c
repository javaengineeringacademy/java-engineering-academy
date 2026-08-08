/*
 * Senior Level — C Language
 * Solutions: Complete implementations for all exercises
 *
 * Compile with: gcc -Wall -Wextra -std=c99 -o senior senior.c -lpthread
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

/* ============================================================
 * Exercise 1: ABI-Stable Library Design (Opaque Pointer DB)
 * ============================================================ */

#define DB_VERSION_MAJOR 1
#define DB_VERSION_MINOR 0
#define DB_VERSION_PATCH 0

#define DB_CHECK_VERSION(major, minor, patch) \
    ((major) < DB_VERSION_MAJOR || \
     ((major) == DB_VERSION_MAJOR && (minor) < DB_VERSION_MINOR) || \
     ((major) == DB_VERSION_MAJOR && (minor) == DB_VERSION_MINOR && \
      (patch) <= DB_VERSION_PATCH))

typedef struct {
    char *key;
    char *value;
} DBEntry;

typedef struct DB {
    DBEntry *entries;
    int count;
    int capacity;
    char *error;
    char *path;
} DB;

DB *db_create(const char *path) {
    if (!path) return NULL;

    DB *db = malloc(sizeof(DB));
    if (!db) return NULL;

    db->path = strdup(path);
    db->entries = NULL;
    db->count = 0;
    db->capacity = 0;
    db->error = NULL;

    if (!db->path) {
        free(db);
        return NULL;
    }
    return db;
}

void db_destroy(DB *db) {
    if (!db) return;
    for (int i = 0; i < db->count; i++) {
        free(db->entries[i].key);
        free(db->entries[i].value);
    }
    free(db->entries);
    free(db->error);
    free(db->path);
    free(db);
}

int db_insert(DB *db, const char *key, const char *value) {
    if (!db || !key || !value) {
        if (db) { free(db->error); db->error = strdup("Null argument"); }
        return -1;
    }

    for (int i = 0; i < db->count; i++) {
        if (strcmp(db->entries[i].key, key) == 0) {
            char *new_val = strdup(value);
            if (!new_val) {
                free(db->error);
                db->error = strdup("Out of memory");
                return -1;
            }
            free(db->entries[i].value);
            db->entries[i].value = new_val;
            return 0;
        }
    }

    if (db->count >= db->capacity) {
        int new_cap = db->capacity == 0 ? 8 : db->capacity * 2;
        DBEntry *tmp = realloc(db->entries, new_cap * sizeof(DBEntry));
        if (!tmp) {
            free(db->error);
            db->error = strdup("Out of memory");
            return -1;
        }
        db->entries = tmp;
        db->capacity = new_cap;
    }

    db->entries[db->count].key = strdup(key);
    db->entries[db->count].value = strdup(value);
    if (!db->entries[db->count].key || !db->entries[db->count].value) {
        free(db->entries[db->count].key);
        free(db->entries[db->count].value);
        free(db->error);
        db->error = strdup("Out of memory");
        return -1;
    }
    db->count++;
    return 0;
}

const char *db_query(DB *db, const char *key) {
    if (!db || !key) return NULL;
    for (int i = 0; i < db->count; i++) {
        if (strcmp(db->entries[i].key, key) == 0) {
            return db->entries[i].value;
        }
    }
    return NULL;
}

const char *db_error(DB *db) {
    if (!db) return NULL;
    return db->error;
}

void exercise1_abi_stable(void) {
    printf("--- Exercise 1: ABI-Stable Library ---\n");
    printf("Version: %d.%d.%d\n", DB_VERSION_MAJOR, DB_VERSION_MINOR, DB_VERSION_PATCH);

    DB *db = db_create("/tmp/test.db");
    if (!db) { printf("Failed to create DB\n\n"); return; }

    db_insert(db, "name", "Alice");
    db_insert(db, "lang", "C");
    db_insert(db, "level", "Senior");

    const char *val = db_query(db, "lang");
    printf("Query 'lang': %s\n", val ? val : "(not found)");

    db_insert(db, "lang", "C++");
    printf("Updated 'lang': %s\n", db_query(db, "lang"));

    printf("Query 'missing': %s\n", db_query(db, "missing") ? : "(not found)");

    db_destroy(db);
    printf("DB destroyed cleanly\n\n");
}

/* ============================================================
 * Exercise 2: Cross-Platform Abstraction
 * (Demonstrates the pattern; pthreads used on all platforms here)
 * ============================================================ */

#include <pthread.h>
#include <unistd.h>

typedef pthread_mutex_t PlatformMutex;
typedef pthread_t PlatformThread;

#define mutex_init(m)    pthread_mutex_init((m), NULL)
#define mutex_lock(m)    pthread_mutex_lock(m)
#define mutex_unlock(m)  pthread_mutex_unlock(m)
#define mutex_destroy(m) pthread_mutex_destroy(m)
#define thread_create(t, f, a) pthread_create((t), NULL, (f), (a))
#define thread_join(t)   pthread_join((t), NULL)
#define msleep(ms)       usleep((ms) * 1000)
#define PATH_SEP "/"

typedef struct {
    PlatformMutex mutex;
    int counter;
} SharedCounter;

static void *increment_thread(void *arg) {
    SharedCounter *sc = (SharedCounter *)arg;
    for (int i = 0; i < 1000; i++) {
        mutex_lock(&sc->mutex);
        sc->counter++;
        mutex_unlock(&sc->mutex);
    }
    return NULL;
}

void exercise2_cross_platform(void) {
    printf("--- Exercise 2: Cross-Platform Abstraction ---\n");
    printf("Path separator: \"%s\"\n", PATH_SEP);

    SharedCounter sc = { .counter = 0 };
    mutex_init(&sc.mutex);

    PlatformThread t1, t2;
    thread_create(&t1, increment_thread, &sc);
    thread_create(&t2, increment_thread, &sc);

    thread_join(t1);
    thread_join(t2);

    printf("Counter after 2 threads x 1000 increments: %d (expected: 2000)\n",
           sc.counter);

    mutex_destroy(&sc.mutex);
    printf("Platform abstraction working correctly\n\n");
}

/* ============================================================
 * Exercise 3: Dynamic Dispatch / Plugin System
 * ============================================================ */

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

typedef struct {
    Plugin *plugins[32];
    int count;
} PluginRegistry;

void registry_init(PluginRegistry *reg) {
    reg->count = 0;
}

int registry_register(PluginRegistry *reg, Plugin *plugin) {
    if (!reg || !plugin) return -1;
    if (reg->count >= 32) return -2;
    if (plugin->init && plugin->init() != 0) return -3;
    reg->plugins[reg->count++] = plugin;
    return 0;
}

Plugin *registry_find(PluginRegistry *reg, const char *name) {
    if (!reg || !name) return NULL;
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
    if (!p || !p->execute) return -1;
    return p->execute(input, output, output_size);
}

void registry_shutdown_all(PluginRegistry *reg) {
    if (!reg) return;
    for (int i = 0; i < reg->count; i++) {
        if (reg->plugins[i]->shutdown) {
            reg->plugins[i]->shutdown();
        }
    }
    reg->count = 0;
}

/* Example plugin: echo */
static int echo_init(void) { return 0; }
static int echo_execute(const char *input, char *output, size_t size) {
    if (!input || !output) return -1;
    strncpy(output, input, size - 1);
    output[size - 1] = '\0';
    return 0;
}
static void echo_shutdown(void) { }

static Plugin echo_plugin = {
    .name = "echo", .version = "1.0",
    .init = echo_init, .execute = echo_execute, .shutdown = echo_shutdown
};

/* Example plugin: uppercase */
static int upper_init(void) { return 0; }
static int upper_execute(const char *input, char *output, size_t size) {
    if (!input || !output) return -1;
    size_t i;
    for (i = 0; input[i] && i < size - 1; i++) {
        output[i] = (char)((input[i] >= 'a' && input[i] <= 'z')
                       ? input[i] - 32 : input[i]);
    }
    output[i] = '\0';
    return 0;
}
static void upper_shutdown(void) { }

static Plugin upper_plugin = {
    .name = "uppercase", .version = "1.0",
    .init = upper_init, .execute = upper_execute, .shutdown = upper_shutdown
};

void exercise3_dynamic_dispatch(void) {
    printf("--- Exercise 3: Dynamic Dispatch / Plugin System ---\n");

    PluginRegistry reg;
    registry_init(&reg);

    registry_register(&reg, &echo_plugin);
    registry_register(&reg, &upper_plugin);

    printf("Registered %d plugins\n", reg.count);

    char output[256];
    registry_execute(&reg, "echo", "Hello, World!", output, sizeof(output));
    printf("echo('Hello, World!') = '%s'\n", output);

    registry_execute(&reg, "uppercase", "Hello, World!", output, sizeof(output));
    printf("uppercase('Hello, World!') = '%s'\n", output);

    Plugin *found = registry_find(&reg, "echo");
    printf("Found plugin: %s v%s\n", found->name, found->version);

    registry_shutdown_all(&reg);
    printf("All plugins shut down\n\n");
}

/* ============================================================
 * Exercise 4: Architecture Decision Record
 * ============================================================ */

static const char *ADR_001 =
    "ADR-001: Data Structure for Configuration Store\n"
    "================================================\n"
    "Status: Accepted\n"
    "\n"
    "Context:\n"
    "  The configuration store is read at startup (once) and written\n"
    "  rarely (on config change). It holds ~50 key-value pairs.\n"
    "  Lookup speed matters more than insert speed.\n"
    "\n"
    "Decision:\n"
    "  Use a sorted dynamic array (not a linked list).\n"
    "  - Binary search for O(log n) lookups\n"
    "  - Insertion is O(n) but happens rarely\n"
    "  - Better cache locality than linked lists\n"
    "  - Simpler memory management (single allocation)\n"
    "\n"
    "Alternatives Considered:\n"
    "  1. Linked list: O(n) lookup, poor cache locality, complex free\n"
    "  2. Hash table: O(1) lookup but overkill for 50 entries\n"
    "  3. Red-black tree: O(log n) but unnecessary complexity\n"
    "\n"
    "Consequences:\n"
    "  + Fast lookups via binary search\n"
    "  + Simple implementation and debugging\n"
    "  + Memory-efficient (single contiguous block)\n"
    "  - Insertion requires shifting elements (acceptable for rare writes)\n"
    "  - Must keep array sorted after each insert\n"
    "\n"
    "Reviewers: Team Lead, Senior Dev\n"
    "Date: 2026-08-08\n";

void exercise4_adr(void) {
    printf("--- Exercise 4: Architecture Decision Record ---\n\n");
    printf("%s\n", ADR_001);
}

/* ============================================================
 * Exercise 5: Performance Profiling
 * ============================================================ */

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
    double seconds = (double)(t->end.tv_sec - t->start.tv_sec);
    double nanos = (double)(t->end.tv_nsec - t->start.tv_nsec) / 1e9;
    return (seconds + nanos) * 1000.0;
}

void bubble_sort(int *arr, int n) {
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            if (arr[j] > arr[j + 1]) {
                int tmp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = tmp;
            }
        }
    }
}

void selection_sort(int *arr, int n) {
    for (int i = 0; i < n - 1; i++) {
        int min_idx = i;
        for (int j = i + 1; j < n; j++) {
            if (arr[j] < arr[min_idx]) min_idx = j;
        }
        if (min_idx != i) {
            int tmp = arr[i];
            arr[i] = arr[min_idx];
            arr[min_idx] = tmp;
        }
    }
}

void exercise5_performance(void) {
    printf("--- Exercise 5: Performance Profiling ---\n");

    const int N = 1000;
    int *original = malloc(N * sizeof(int));
    int *copy1 = malloc(N * sizeof(int));
    int *copy2 = malloc(N * sizeof(int));

    if (!original || !copy1 || !copy2) {
        printf("Allocation failed\n");
        free(original); free(copy1); free(copy2);
        return;
    }

    srand(42);
    for (int i = 0; i < N; i++) original[i] = rand() % 10000;
    memcpy(copy1, original, N * sizeof(int));
    memcpy(copy2, original, N * sizeof(int));

    Timer t1 = { .name = "bubble_sort" };
    timer_start(&t1);
    bubble_sort(copy1, N);
    timer_stop(&t1);
    double bubble_ms = timer_elapsed_ms(&t1);

    Timer t2 = { .name = "selection_sort" };
    timer_start(&t2);
    selection_sort(copy2, N);
    timer_stop(&t2);
    double selection_ms = timer_elapsed_ms(&t2);

    printf("Data size: %d elements\n", N);
    printf("Bubble sort:    %.3f ms\n", bubble_ms);
    printf("Selection sort: %.3f ms\n", selection_ms);
    printf("Winner: %s\n", bubble_ms < selection_ms ? "bubble_sort" : "selection_sort");

    free(original);
    free(copy1);
    free(copy2);
    printf("\n");
}

/* ============================================================
 * Main
 * ============================================================ */

int main(void) {
    printf("====================================\n");
    printf("  Senior Level — Solutions\n");
    printf("====================================\n\n");

    exercise1_abi_stable();
    exercise2_cross_platform();
    exercise3_dynamic_dispatch();
    exercise4_adr();
    exercise5_performance();

    return 0;
}
