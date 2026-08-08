/*
 * Knowledge Atoms — C Language
 * Exercises: Compilation Model, Type System, Memory Model, Preprocessor, Linker
 *
 * Complete the TODO sections below. Each exercise tests a core C knowledge atom.
 * Compile with: gcc -Wall -Wextra -std=c99 -o exercises exercises.c
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>

/* ============================================================
 * Exercise 1: Compilation Model
 *
 * The preprocessor runs before compilation. Write a program that
 * uses #define to create a macro MAX(a,b) that returns the larger
 * of two values. Then demonstrate that macros are text substitution
 * by showing what happens when MAX(i++, j++) is called.
 * Print the result to show the side-effect issue.
 * ============================================================ */
void exercise1_compilation_model(void) {
    /* TODO: Define a macro MAX(a,b) that returns the larger value.
     * Then call it with MAX(i++, j++) where i=5, j=10.
     * Observe and print the unexpected result caused by text substitution.
     * Hint: i++ is evaluated multiple times in a naive macro.
     */
    printf("=== Exercise 1: Compilation Model ===\n");
    printf("TODO: Implement MAX macro and demonstrate side-effect issue\n\n");
}

/* ============================================================
 * Exercise 2: Type System — Implicit Conversions
 *
 * C is weakly typed and allows implicit conversions that can lose
 * data. Write a program that demonstrates:
 *   a) Integer overflow with signed types
 *   b) Truncation from int to char
 *   c) Floating-point to integer truncation
 * Print each result and explain what happened.
 * ============================================================ */
void exercise2_type_system(void) {
    /* TODO: Demonstrate three types of implicit conversion:
     * 1. Assign 3000000000 to an int (32-bit) — what happens?
     * 2. Assign 256 to a char — what happens?
     * 3. Assign 3.14 to an int — what happens?
     * Print each result.
     */
    printf("=== Exercise 2: Type System ===\n");
    printf("TODO: Demonstrate implicit type conversions\n\n");
}

/* ============================================================
 * Exercise 3: Memory Model — Stack vs Heap
 *
 * Write two functions:
 *   a) one_that_returns_local() — returns a pointer to a local variable
 *   b) one_that_returns_heap() — returns a pointer to heap-allocated memory
 *
 * Call both from main. Explain why one is safe and the other is undefined.
 * ============================================================ */
int *one_that_returns_local(void) {
    /* TODO: Allocate a local int on the stack, set it to 42,
     * and return its address. This is dangerous — why?
     */
    return NULL; /* placeholder */
}

int *one_that_returns_heap(void) {
    /* TODO: Allocate an int on the heap using malloc, set it to 42,
     * and return its pointer. This is safe — why?
     */
    return NULL; /* placeholder */
}

void exercise3_memory_model(void) {
    printf("=== Exercise 3: Memory Model ===\n");
    printf("TODO: Implement both functions and explain the difference\n\n");
}

/* ============================================================
 * Exercise 4: Preprocessor — Conditional Compilation
 *
 * Write a program that uses conditional compilation (#ifdef, #ifndef)
 * to print different messages based on whether DEBUG is defined.
 * Also demonstrate __FILE__, __LINE__, and __func__ macros.
 * Compile with: gcc -DDEBUG exercises.c -o exercises
 * ============================================================ */
void exercise4_preprocessor(void) {
    /* TODO: Use #ifdef DEBUG to print a debug message.
     * Also print __FILE__, __LINE__, and __func__ to show
     * predefined preprocessor macros.
     */
    printf("=== Exercise 4: Preprocessor ===\n");
    printf("TODO: Use conditional compilation and predefined macros\n\n");
}

/* ============================================================
 * Exercise 5: Linker — External Symbols
 *
 * This file declares an external function `external_add` that is
 * defined in another translation unit. In this exercise, comment
 * out the extern declaration and observe the linker error.
 *
 * Step 1: Uncomment the extern declaration below.
 * Step 2: Create a file helper.c with:
 *         int external_add(int a, int b) { return a + b; }
 * Step 3: Compile: gcc -c helper.c -o helper.o
 *         gcc exercises.c helper.o -o exercises
 * Step 4: Run and verify the result.
 * ============================================================ */
/* TODO: Uncomment the following line: */
// extern int external_add(int a, int b);

void exercise5_linker(void) {
    printf("=== Exercise 5: Linker ===\n");
    /* TODO: Call external_add(3, 4) and print the result.
     * First, uncomment the extern declaration above.
     */
    printf("TODO: Uncomment extern and call external_add(3, 4)\n\n");
}

/* ============================================================
 * Main — Run all exercises
 * ============================================================ */
int main(void) {
    printf("====================================\n");
    printf("  Knowledge Atoms — Exercises\n");
    printf("====================================\n\n");

    exercise1_compilation_model();
    exercise2_type_system();
    exercise3_memory_model();
    exercise4_preprocessor();
    exercise5_linker();

    printf("====================================\n");
    printf("  All exercises completed!\n");
    printf("====================================\n");

    return 0;
}
