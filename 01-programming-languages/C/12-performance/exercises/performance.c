/*
 * Exercise: Performance Optimization in C
 * Difficulty: ★★★★☆ (4/5)
 * Learning Objectives:
 *   - Understand CPU cache effects on performance
 *   - Practice loop optimization techniques
 *   - Learn about compiler optimizations and intrinsics
 *   - Measure and profile code performance
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define N 4096
#define ITERATIONS 1000

/* ============================================================
 * Problem 1: Cache-Friendly vs Cache-Unfriendly Access
 *
 * Compare performance of column-major vs row-major traversal
 * of a 2D array. Show that row-major is faster for C arrays.
 * ============================================================ */
static int matrix[N][N];

void problem1_cache_access(void) {
    /* TODO: Benchmark row-major vs column-major traversal
     * 1. Fill matrix with values
     * 2. Time row-major: for(i) for(j) sum += matrix[i][j]
     * 3. Time column-major: for(j) for(i) sum += matrix[i][j]
     * 4. Print both times and show the difference
     */
    printf("TODO: Problem 1 - Cache Access Patterns\n\n");
}

/* ============================================================
 * Problem 2: Loop Optimization Techniques
 *
 * Apply these optimizations to a matrix multiplication:
 * - Loop unrolling
 * - Blocking/tiling for cache
 * - Compare naive vs optimized versions
 * ============================================================ */
void naive_matrix_multiply(int C[N][N], int A[N][N], int B[N][N]) {
    /* TODO: Standard O(n^3) matrix multiplication */
    (void)C; (void)A; (void)B;
}

void optimized_matrix_multiply(int C[N][N], int A[N][N], int B[N][N]) {
    /* TODO: Apply loop tiling/blocking for cache efficiency
     * Use block size of 32 or 64
     */
    (void)C; (void)A; (void)B;
}

void problem2_loop_optimization(void) {
    /* TODO: Benchmark naive vs optimized matrix multiply */
    printf("TODO: Problem 2 - Loop Optimization\n\n");
}

/* ============================================================
 * Problem 3: Branch Prediction Optimization
 *
 * Compare performance of:
 * - Branching approach (if/else)
 * - Branchless approach (ternary or arithmetic)
 * Use a large array with random data.
 * ============================================================ */
long branching_count(int *arr, int n, int threshold) {
    /* TODO: Count elements > threshold using if/else */
    (void)arr; (void)n; (void)threshold;
    return 0;
}

long branchless_count(int *arr, int n, int threshold) {
    /* TODO: Count elements > threshold without branches
     * Hint: Use arithmetic: count += (arr[i] > threshold)
     */
    (void)arr; (void)n; (void)threshold;
    return 0;
}

void problem3_branch_optimization(void) {
    /* TODO: Generate random data, benchmark both approaches */
    printf("TODO: Problem 3 - Branch Optimization\n\n");
}

/* ============================================================
 * Problem 4: Memory Allocation Strategies
 *
 * Compare performance of:
 * - Many small mallocs vs one large malloc with offsets
 * - Stack allocation vs heap allocation
 * ============================================================ */
void problem4_allocation_strategies(void) {
    /* TODO: Benchmark different allocation strategies
     * 1. Allocate 10000 small structs individually with malloc
     * 2. Allocate one large block, carve into structs
     * 3. Use stack arrays (VLA or fixed size)
     * 4. Compare times
     */
    printf("TODO: Problem 4 - Allocation Strategies\n\n");
}

/* ============================================================
 * Problem 5: SIMD Intrinsics (Conceptual)
 *
 * Demonstrate how SIMD can speed up array operations.
 * Use compiler intrinsics if available, or show the concept.
 * ============================================================ */
void array_add_scalar(int *result, int *a, int b, int n) {
    /* TODO: Add scalar b to each element of array a
     * Show how this could be vectorized
     */
    (void)result; (void)a; (void)b; (void)n;
}

void array_multiply(int *result, int *a, int *b, int n) {
    /* TODO: Element-wise multiply a and b into result
     * Discuss SIMD opportunities
     */
    (void)result; (void)a; (void)b; (void)n;
}

void problem5_simd_concept(void) {
    /* TODO: Implement and explain SIMD potential */
    printf("TODO: Problem 5 - SIMD Concepts\n\n");
}

int main(void) {
    printf("====================================\n");
    printf("  Performance — Exercises\n");
    printf("====================================\n\n");

    problem1_cache_access();
    problem2_loop_optimization();
    problem3_branch_optimization();
    problem4_allocation_strategies();
    problem5_simd_concept();

    return 0;
}
