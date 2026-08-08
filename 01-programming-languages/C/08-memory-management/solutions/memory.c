/*
 * Advanced Memory Management — C Language
 * Solutions: Complete implementations for all exercises
 *
 * Compile with: gcc -Wall -Wextra -std=c99 -o solutions solutions.c
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* ============================================================
 * Problem 1: Dynamic String (Growable)
 * ============================================================ */
typedef struct {
    char *data;
    int length;
    int capacity;
} DynamicString;

DynamicString *create_string(int initial_capacity) {
    DynamicString *ds = malloc(sizeof(DynamicString));
    if (!ds) return NULL;

    ds->capacity = initial_capacity > 0 ? initial_capacity : 16;
    ds->length = 0;
    ds->data = malloc(ds->capacity);
    if (!ds->data) { free(ds); return NULL; }

    ds->data[0] = '\0';
    return ds;
}

void append_char(DynamicString *ds, char c) {
    if (ds->length + 1 >= ds->capacity) {
        ds->capacity *= 2;
        char *tmp = realloc(ds->data, ds->capacity);
        if (!tmp) return;
        ds->data = tmp;
    }
    ds->data[ds->length++] = c;
    ds->data[ds->length] = '\0';
}

void append_string(DynamicString *ds, const char *str) {
    while (*str) {
        append_char(ds, *str++);
    }
}

void free_string(DynamicString *ds) {
    if (ds) { free(ds->data); free(ds); }
}

void problem1_dynamic_string(void) {
    printf("=== Problem 1: Dynamic String ===\n");

    DynamicString *ds = create_string(4);
    append_string(ds, "Hello");
    append_char(ds, ' ');
    append_string(ds, "World!");

    printf("  String: \"%s\"\n", ds->data);
    printf("  Length: %d, Capacity: %d\n", ds->length, ds->capacity);

    free_string(ds);
    printf("  Freed successfully\n\n");
}

/* ============================================================
 * Problem 2: Simple Memory Pool
 * ============================================================ */
#define POOL_SIZE 1024

typedef struct {
    char pool[POOL_SIZE];
    int allocated[POOL_SIZE];
} MemoryPool;

void pool_init(MemoryPool *mp) {
    memset(mp->pool, 0, POOL_SIZE);
    memset(mp->allocated, 0, sizeof(mp->allocated));
}

void *pool_alloc(MemoryPool *mp, int size) {
    if (size <= 0 || size > POOL_SIZE) return NULL;

    /* First-fit allocation */
    for (int i = 0; i <= POOL_SIZE - size; i++) {
        int fits = 1;
        for (int j = 0; j < size; j++) {
            if (mp->allocated[i + j]) { fits = 0; break; }
        }
        if (fits) {
            for (int j = 0; j < size; j++) mp->allocated[i + j] = 1;
            return &mp->pool[i];
        }
    }
    return NULL;
}

void pool_free(MemoryPool *mp, void *ptr) {
    if (!ptr) return;
    int offset = (int)((char *)ptr - mp->pool);
    if (offset < 0 || offset >= POOL_SIZE) return;

    /* Find the extent of this allocation and free it */
    int start = offset;
    while (start > 0 && mp->allocated[start - 1]) start--;

    int end = start;
    while (end < POOL_SIZE && mp->allocated[end]) end++;

    for (int i = start; i < end; i++) mp->allocated[i] = 0;
}

void problem2_memory_pool(void) {
    printf("=== Problem 2: Memory Pool ===\n");

    MemoryPool mp;
    pool_init(&mp);

    void *chunk1 = pool_alloc(&mp, 128);
    void *chunk2 = pool_alloc(&mp, 256);
    void *chunk3 = pool_alloc(&mp, 64);

    printf("  Chunk1: %p (128 bytes)\n", chunk1);
    printf("  Chunk2: %p (256 bytes)\n", chunk2);
    printf("  Chunk3: %p (64 bytes)\n", chunk3);

    pool_free(&mp, chunk1);
    pool_free(&mp, chunk2);
    pool_free(&mp, chunk3);

    void *chunk4 = pool_alloc(&mp, 512);
    printf("  After freeing, chunk4 (512): %p\n", chunk4);

    if (chunk4) pool_free(&mp, chunk4);
    printf("  Pool operations completed\n\n");
}

/* ============================================================
 * Problem 3: Allocate Multiple with Cleanup
 * ============================================================ */
void **allocate_multiple(int count, int size) {
    void **ptrs = malloc(count * sizeof(void *));
    if (!ptrs) return NULL;

    for (int i = 0; i < count; i++) {
        ptrs[i] = malloc(size);
        if (!ptrs[i]) {
            /* Cleanup on failure */
            for (int j = 0; j < i; j++) free(ptrs[j]);
            free(ptrs);
            return NULL;
        }
        memset(ptrs[i], 0, size);
    }
    return ptrs;
}

void problem3_allocate_multiple(void) {
    printf("=== Problem 3: Allocate Multiple ===\n");

    void **ptrs = allocate_multiple(5, sizeof(int));
    if (ptrs) {
        printf("  Allocated 5 int blocks successfully\n");
        for (int i = 0; i < 5; i++) {
            *(int *)ptrs[i] = (i + 1) * 100;
            printf("    ptrs[%d] = %d\n", i, *(int *)ptrs[i]);
            free(ptrs[i]);
        }
        free(ptrs);
    }
    printf("\n");
}

/* ============================================================
 * Problem 4: Allocation Tracker
 * ============================================================ */
typedef struct Allocation {
    void *ptr;
    int size;
    char *file;
    int line;
} Allocation;

typedef struct {
    Allocation *allocations;
    int count;
    int capacity;
} AllocationTracker;

void tracker_init(AllocationTracker *tracker) {
    tracker->capacity = 16;
    tracker->count = 0;
    tracker->allocations = malloc(tracker->capacity * sizeof(Allocation));
}

void tracker_add(AllocationTracker *tracker, void *ptr, int size, char *file, int line) {
    if (tracker->count >= tracker->capacity) {
        tracker->capacity *= 2;
        tracker->allocations = realloc(tracker->allocations,
                                       tracker->capacity * sizeof(Allocation));
    }
    tracker->allocations[tracker->count++] = (Allocation){ptr, size, file, line};
}

void tracker_remove(AllocationTracker *tracker, void *ptr) {
    for (int i = 0; i < tracker->count; i++) {
        if (tracker->allocations[i].ptr == ptr) {
            tracker->allocations[i] = tracker->allocations[--tracker->count];
            return;
        }
    }
}

void tracker_report(AllocationTracker *tracker) {
    if (tracker->count == 0) {
        printf("  No memory leaks detected.\n");
        return;
    }
    printf("  %d unfreed allocation(s):\n", tracker->count);
    for (int i = 0; i < tracker->count; i++) {
        printf("    %p (%d bytes) at %s:%d\n",
               tracker->allocations[i].ptr,
               tracker->allocations[i].size,
               tracker->allocations[i].file,
               tracker->allocations[i].line);
    }
}

void tracker_free(AllocationTracker *tracker) {
    free(tracker->allocations);
    tracker->allocations = NULL;
    tracker->count = tracker->capacity = 0;
}

void problem4_tracker(void) {
    printf("=== Problem 4: Allocation Tracker ===\n");

    AllocationTracker tracker;
    tracker_init(&tracker);

    int *a = malloc(sizeof(int));
    int *b = malloc(sizeof(int) * 10);
    int *c = malloc(sizeof(int) * 5);

    tracker_add(&tracker, a, sizeof(int), __FILE__, __LINE__);
    tracker_add(&tracker, b, sizeof(int) * 10, __FILE__, __LINE__);
    tracker_add(&tracker, c, sizeof(int) * 5, __FILE__, __LINE__);

    free(a);
    tracker_remove(&tracker, a);
    printf("  After freeing 'a':\n");
    tracker_report(&tracker);

    free(b);
    tracker_remove(&tracker, b);
    free(c);
    tracker_remove(&tracker, c);
    printf("  After freeing all:\n");
    tracker_report(&tracker);

    tracker_free(&tracker);
    printf("\n");
}

/* ============================================================
 * Problem 5: 2D Matrix Allocation
 * ============================================================ */
int **allocate_matrix(int rows, int cols) {
    int **matrix = malloc(rows * sizeof(int *));
    if (!matrix) return NULL;

    for (int i = 0; i < rows; i++) {
        matrix[i] = calloc(cols, sizeof(int));
        if (!matrix[i]) {
            for (int j = 0; j < i; j++) free(matrix[j]);
            free(matrix);
            return NULL;
        }
    }
    return matrix;
}

void free_matrix(int **matrix, int rows) {
    if (!matrix) return;
    for (int i = 0; i < rows; i++) free(matrix[i]);
    free(matrix);
}

void print_matrix(int **matrix, int rows, int cols) {
    for (int i = 0; i < rows; i++) {
        printf("    ");
        for (int j = 0; j < cols; j++) {
            printf("%3d ", matrix[i][j]);
        }
        printf("\n");
    }
}

void problem5_matrix(void) {
    printf("=== Problem 5: 2D Matrix ===\n");

    int rows = 3, cols = 4;
    int **matrix = allocate_matrix(rows, cols);

    if (matrix) {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                matrix[i][j] = i * cols + j + 1;

        printf("  %dx%d matrix:\n", rows, cols);
        print_matrix(matrix, rows, cols);
        free_matrix(matrix, rows);
    }
    printf("\n");
}

/* ============================================================
 * Main
 * ============================================================ */
int main(void) {
    printf("====================================\n");
    printf("  Advanced Memory — Solutions\n");
    printf("====================================\n\n");

    problem1_dynamic_string();
    problem2_memory_pool();
    problem3_allocate_multiple();
    problem4_tracker();
    problem5_matrix();

    return 0;
}
