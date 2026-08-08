/*
 * Knowledge Atoms — C Language
 * Solutions: Complete implementations for all exercises
 *
 * Compile with: gcc -Wall -Wextra -std=c99 -DDEBUG -o solutions solutions.c
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>

/* ============================================================
 * Exercise 1: Compilation Model — MAX Macro Side Effects
 * ============================================================ */

/* Naive macro — evaluates arguments multiple times */
#define NAIVE_MAX(a, b) ((a) > (b) ? (a) : (b))

/* Safe macro — evaluates each argument exactly once */
#define SAFE_MAX(a, b) ({ \
    typeof(a) _a = (a);  \
    typeof(b) _b = (b);  \
    _a > _b ? _a : _b;   \
})

void exercise1_compilation_model(void) {
    printf("=== Exercise 1: Compilation Model ===\n");

    int i = 5, j = 10;

    /* Naive MAX: i++ is evaluated TWICE because it appears twice in expansion */
    int result1 = NAIVE_MAX(i++, j++);
    printf("After NAIVE_MAX(i++, j++): result=%d, i=%d, j=%d\n",
           result1, i, j);
    printf("  Explanation: i++ evaluated twice (once in condition, once in body)\n");

    /* Reset */
    i = 5; j = 10;

    /* Safe MAX: each argument evaluated once via statement expression */
    int result2 = SAFE_MAX(i++, j++);
    printf("After SAFE_MAX(i++, j++):  result=%d, i=%d, j=%d\n",
           result2, i, j);
    printf("  Explanation: each argument evaluated exactly once\n\n");
}

/* ============================================================
 * Exercise 2: Type System — Implicit Conversions
 * ============================================================ */
void exercise2_type_system(void) {
    printf("=== Exercise 2: Type System ===\n");

    /* 1. Integer overflow with signed types (undefined behavior!) */
    int big = 3000000000LL;  /* 3 billion exceeds INT_MAX (2^31-1 = 2147483647) */
    printf("1. int big = 3000000000LL → %d (overflow: %s)\n",
           big, (big < 0) ? "YES, negative due to wrap-around" : "unexpected");

    /* 2. Truncation: int to char */
    char c = 256;  /* 256 = 0x100, char holds 0-255 (or -128 to 127) */
    printf("2. char c = 256 → %d (truncated: %s)\n",
           c, (c == 0) ? "YES, 256 mod 256 = 0" : "no");

    /* 3. Float to int truncation */
    float f = 3.99f;
    int truncated = f;
    printf("3. int = 3.99f → %d (truncated: %s)\n",
           truncated, (truncated == 3) ? "YES, fractional part lost" : "no");

    /* 4. Signed/unsigned comparison surprise */
    int signed_val = -1;
    unsigned int unsigned_val = 0;
    printf("4. -1 > 0u? → %s (-1 as unsigned is %u)\n",
           (signed_val > unsigned_val) ? "true" : "false (unsigned comparison wins)",
           (unsigned int)signed_val);

    printf("\n");
}

/* ============================================================
 * Exercise 3: Memory Model — Stack vs Heap
 * ============================================================ */

/* DANGEROUS: Returns pointer to local (stack) variable */
int *one_that_returns_local(void) {
    int local = 42;
    printf("  [one_that_returns_local] local = %d, address = %p\n",
           local, (void *)&local);
    return &local;  /* BAD: local destroyed when function returns */
}

/* SAFE: Returns pointer to heap-allocated memory */
int *one_that_returns_heap(void) {
    int *p = malloc(sizeof(int));
    if (p != NULL) {
        *p = 42;
        printf("  [one_that_returns_heap] *p = %d, address = %p\n",
               *p, (void *)p);
    }
    return p;  /* GOOD: heap memory persists until free() */
}

void exercise3_memory_model(void) {
    printf("=== Exercise 3: Memory Model ===\n");

    /* Stack: dangerous */
    printf("--- Stack (DANGEROUS) ---\n");
    int *local_ptr = one_that_returns_local();
    printf("  Back in main: *local_ptr = %d (UNDEFINED — stack frame gone!)\n",
           *local_ptr);  /* Undefined behavior! */

    /* Heap: safe */
    printf("--- Heap (SAFE) ---\n");
    int *heap_ptr = one_that_returns_heap();
    if (heap_ptr != NULL) {
        printf("  Back in main: *heap_ptr = %d (safe — still allocated)\n",
               *heap_ptr);
        free(heap_ptr);
        heap_ptr = NULL;
    }

    printf("  Key difference: stack memory is freed on return,\n");
    printf("  heap memory persists until explicitly freed.\n\n");
}

/* ============================================================
 * Exercise 4: Preprocessor — Conditional Compilation
 * ============================================================ */
void exercise4_preprocessor(void) {
    printf("=== Exercise 4: Preprocessor ===\n");

#ifdef DEBUG
    printf("DEBUG mode is ON — debug messages will be printed\n");
#else
    printf("DEBUG mode is OFF — debug messages hidden\n");
#endif

    /* Predefined macros */
    printf("  File: %s\n", __FILE__);
    printf("  Line: %d\n", __LINE__);
    printf("  Function: %s\n", __func__);

#ifdef __STDC_VERSION__
    printf("  C Standard: %ldL\n", __STDC_VERSION__);
#else
    printf("  C Standard: C89/C90 (no __STDC_VERSION__)\n");
#endif

    printf("\n");
}

/* ============================================================
 * Exercise 5: Linker — External Symbols
 * ============================================================ */

/* Uncomment this declaration after creating helper.c with external_add() */
/* extern int external_add(int a, int b); */

void exercise5_linker(void) {
    printf("=== Exercise 5: Linker ===\n");
    /* Uncomment after linking with helper.o:
     * int result = external_add(3, 4);
     * printf("external_add(3, 4) = %d\n", result);
     */
    printf("  To test: create helper.c with external_add(), compile both,\n");
    printf("  and link: gcc exercises.c helper.o -o exercises\n\n");
}

/* ============================================================
 * Main
 * ============================================================ */
int main(void) {
    printf("====================================\n");
    printf("  Knowledge Atoms — Solutions\n");
    printf("====================================\n\n");

    exercise1_compilation_model();
    exercise2_type_system();
    exercise3_memory_model();
    exercise4_preprocessor();
    exercise5_linker();

    printf("====================================\n");
    printf("  All solutions demonstrated!\n");
    printf("====================================\n");

    return 0;
}
