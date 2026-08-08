/*
 * Performance Optimization — C Language
 * Solutions: Complete implementations for all exercises
 *
 * Compile with: gcc -Wall -Wextra -std=c99 -O2 -o solutions solutions.c
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define N 2048
#define ITERATIONS 100
#define BLOCK_SIZE 32

/* ============================================================
 * Helper: Get time in seconds
 * ============================================================ */
static double get_time(void) {
    struct timespec ts;
    clock_gettime(CLOCK_MONOTONIC, &ts);
    return ts.tv_sec + ts.tv_nsec / 1e9;
}

/* ============================================================
 * Problem 1: Cache-Friendly vs Cache-Unfriendly Access
 * ============================================================ */
void problem1_cache_access(void) {
    printf("=== Problem 1: Cache Access Patterns ===\n");

    /* Allocate and fill matrix */
    int **matrix = malloc(N * sizeof(int *));
    for (int i = 0; i < N; i++) {
        matrix[i] = malloc(N * sizeof(int));
        for (int j = 0; j < N; j++) matrix[i][j] = i + j;
    }

    long sum = 0;
    double start, elapsed;

    /* Row-major (cache-friendly in C) */
    sum = 0;
    start = get_time();
    for (int iter = 0; iter < ITERATIONS; iter++) {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                sum += matrix[i][j];
    }
    elapsed = get_time() - start;
    printf("  Row-major (cache-friendly):  %.4f seconds (sum=%ld)\n", elapsed, sum);

    /* Column-major (cache-unfriendly in C) */
    sum = 0;
    start = get_time();
    for (int iter = 0; iter < ITERATIONS; iter++) {
        for (int j = 0; j < N; j++)
            for (int i = 0; i < N; i++)
                sum += matrix[i][j];
    }
    elapsed = get_time() - start;
    printf("  Column-major (cache-hostile): %.4f seconds (sum=%ld)\n", elapsed, sum);
    printf("  Row-major is faster because C arrays are row-major.\n");

    for (int i = 0; i < N; i++) free(matrix[i]);
    free(matrix);
    printf("\n");
}

/* ============================================================
 * Problem 2: Loop Optimization — Matrix Multiply
 * ============================================================ */
void naive_matrix_multiply(int **C, int **A, int **B, int n) {
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++) {
            C[i][j] = 0;
            for (int k = 0; k < n; k++)
                C[i][j] += A[i][k] * B[k][j];
        }
}

void optimized_matrix_multiply(int **C, int **A, int **B, int n) {
    /* Zero output */
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            C[i][j] = 0;

    /* Blocked/tiled multiplication */
    for (int ii = 0; ii < n; ii += BLOCK_SIZE)
        for (int jj = 0; jj < n; jj += BLOCK_SIZE)
            for (int kk = 0; kk < n; kk += BLOCK_SIZE) {
                int i_end = ii + BLOCK_SIZE < n ? ii + BLOCK_SIZE : n;
                int j_end = jj + BLOCK_SIZE < n ? jj + BLOCK_SIZE : n;
                int k_end = kk + BLOCK_SIZE < n ? kk + BLOCK_SIZE : n;

                for (int i = ii; i < i_end; i++)
                    for (int j = jj; j < j_end; j++)
                        for (int k = kk; k < k_end; k++)
                            C[i][j] += A[i][k] * B[k][j];
            }
}

void problem2_loop_optimization(void) {
    printf("=== Problem 2: Loop Optimization ===\n");
    printf("  Matrix size: %dx%d, Block size: %d\n\n", N, N, BLOCK_SIZE);

    int **A = malloc(N * sizeof(int *));
    int **B = malloc(N * sizeof(int *));
    int **C = malloc(N * sizeof(int *));
    for (int i = 0; i < N; i++) {
        A[i] = malloc(N * sizeof(int));
        B[i] = malloc(N * sizeof(int));
        C[i] = malloc(N * sizeof(int));
        for (int j = 0; j < N; j++) {
            A[i][j] = i + j;
            B[i][j] = i - j;
        }
    }

    double start, elapsed;

    start = get_time();
    naive_matrix_multiply(C, A, B, N);
    elapsed = get_time() - start;
    printf("  Naive multiply:        %.4f seconds\n", elapsed);

    start = get_time();
    optimized_matrix_multiply(C, A, B, N);
    elapsed = get_time() - start;
    printf("  Blocked multiply:      %.4f seconds\n", elapsed);

    for (int i = 0; i < N; i++) { free(A[i]); free(B[i]); free(C[i]); }
    free(A); free(B); free(C);
    printf("\n");
}

/* ============================================================
 * Problem 3: Branch Prediction
 * ============================================================ */
long branching_count(int *arr, int n, int threshold) {
    long count = 0;
    for (int i = 0; i < n; i++) {
        if (arr[i] > threshold) count++;
    }
    return count;
}

long branchless_count(int *arr, int n, int threshold) {
    long count = 0;
    for (int i = 0; i < n; i++) {
        count += (arr[i] > threshold);
    }
    return count;
}

void problem3_branch_optimization(void) {
    printf("=== Problem 3: Branch Optimization ===\n");

    int n = 10000000;
    int *arr = malloc(n * sizeof(int));

    srand(42);
    for (int i = 0; i < n; i++) arr[i] = rand() % 100;

    double start, elapsed;
    long result;
    int threshold = 50;
    int iters = 20;

    start = get_time();
    for (int i = 0; i < iters; i++) result = branching_count(arr, n, threshold);
    elapsed = get_time() - start;
    printf("  Branching count:   %.4f seconds (result=%ld)\n", elapsed, result);

    start = get_time();
    for (int i = 0; i < iters; i++) result = branchless_count(arr, n, threshold);
    elapsed = get_time() - start;
    printf("  Branchless count:  %.4f seconds (result=%ld)\n", elapsed, result);

    free(arr);
    printf("\n");
}

/* ============================================================
 * Problem 4: Allocation Strategies
 * ============================================================ */
typedef struct { int x, y, z; } SmallStruct;

void problem4_allocation_strategies(void) {
    printf("=== Problem 4: Allocation Strategies ===\n");

    int count = 100000;
    double start, elapsed;

    /* Many small mallocs */
    start = get_time();
    SmallStruct **ptrs = malloc(count * sizeof(SmallStruct *));
    for (int i = 0; i < count; i++) {
        ptrs[i] = malloc(sizeof(SmallStruct));
        ptrs[i]->x = i; ptrs[i]->y = i * 2; ptrs[i]->z = i * 3;
    }
    for (int i = 0; i < count; i++) free(ptrs[i]);
    free(ptrs);
    elapsed = get_time() - start;
    printf("  Many small mallocs:  %.6f seconds\n", elapsed);

    /* One large block */
    start = get_time();
    SmallStruct *block = malloc(count * sizeof(SmallStruct));
    for (int i = 0; i < count; i++) {
        block[i].x = i; block[i].y = i * 2; block[i].z = i * 3;
    }
    free(block);
    elapsed = get_time() - start;
    printf("  One large block:     %.6f seconds\n", elapsed);

    /* Stack allocation */
    start = get_time();
    SmallStruct stack_arr[10000];
    for (int i = 0; i < 10000; i++) {
        stack_arr[i].x = i; stack_arr[i].y = i * 2; stack_arr[i].z = i * 3;
    }
    elapsed = get_time() - start;
    printf("  Stack allocation:    %.6f seconds (10000 elements)\n", elapsed);

    printf("\n");
}

/* ============================================================
 * Problem 5: SIMD Concepts
 * ============================================================ */
void array_add_scalar(int *result, int *a, int b, int n) {
    for (int i = 0; i < n; i++) {
        result[i] = a[i] + b;
    }
    /* SIMD potential: process 4 or 8 elements at once using
     * SSE/AVX intrinsics like _mm256_add_epi32() */
}

void array_multiply(int *result, int *a, int *b, int n) {
    for (int i = 0; i < n; i++) {
        result[i] = a[i] * b[i];
    }
    /* SIMD potential: use _mm256_mullo_epi32() for parallel multiply */
}

void problem5_simd_concept(void) {
    printf("=== Problem 5: SIMD Concepts ===\n");

    int n = 1000000;
    int *a = malloc(n * sizeof(int));
    int *b = malloc(n * sizeof(int));
    int *result = malloc(n * sizeof(int));

    for (int i = 0; i < n; i++) { a[i] = i; b[i] = i * 2; }

    double start = get_time();
    array_add_scalar(result, a, 10, n);
    double elapsed = get_time() - start;
    printf("  Scalar add (n=%d): %.6f seconds\n", n, elapsed);
    printf("  result[0]=%d, result[999]=%d\n", result[0], result[999]);

    start = get_time();
    array_multiply(result, a, b, n);
    elapsed = get_time() - start;
    printf("  Scalar multiply:   %.6f seconds\n", n, elapsed);
    printf("  result[0]=%d, result[999]=%d\n", result[0], result[999]);

    printf("  With SIMD intrinsics, these could be 4-8x faster.\n");

    free(a); free(b); free(result);
    printf("\n");
}

/* ============================================================
 * Main
 * ============================================================ */
int main(void) {
    printf("====================================\n");
    printf("  Performance — Solutions\n");
    printf("====================================\n\n");

    problem1_cache_access();
    problem2_loop_optimization();
    problem3_branch_optimization();
    problem4_allocation_strategies();
    problem5_simd_concept();

    return 0;
}
