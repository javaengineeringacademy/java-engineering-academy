/*
 * Preprocessor — C Language
 * Exercises: Macros, Conditional Compilation, Include Guards, Token Pasting
 *
 * Compile with: gcc -Wall -Wextra -std=c99 -DDEBUG -o exercises exercises.c
 */

#include <stdio.h>
#include <stdlib.h>

/* ============================================================
 * Problem 1: Macro Safety — Safe MAX/MIN
 *
 * Write macros MAX(a,b) and MIN(a,b) that:
 *   a) Don't evaluate arguments multiple times
 *   b) Work correctly with any expression (including those with commas)
 *   c) Are safe from operator precedence issues
 *
 * Test with: MAX(2+3, 4*5), MAX(i++, j++), MAX(x & 0xFF, y | 0x10)
 * ============================================================ */
/* TODO: Define safe MAX and MIN macros */

void problem1_macros(void) {
    printf("=== Problem 1: Macro Safety ===\n");
    /* TODO: Test your macros with the test cases above */
    printf("TODO: Implement safe MAX/MIN macros\n\n");
}

/* ============================================================
 * Problem 2: Type-Safe Container Macro
 *
 * Create a macro `DECLARE_STACK(type, name, capacity)` that declares
 * a stack data structure for a specific type. It should create:
 *   - An array of the given type
 *   - A size variable
 *   - push/pop/peek functions (as macros or inline)
 *
 * Example usage:
 *   DECLARE_STACK(int, int_stack, 100)
 *   int_stack_push(42);
 *   int val = int_stack_pop();
 * ============================================================ */
/* TODO: Implement DECLARE_STACK macro */

void problem2_container_macro(void) {
    printf("=== Problem 2: Container Macro ===\n");
    printf("TODO: Implement DECLARE_STACK macro\n\n");
}

/* ============================================================
 * Problem 3: Conditional Compilation — Debug Logging
 *
 * Create a debug logging system using the preprocessor:
 *   - When DEBUG is defined: print file, line, function, and message
 *   - When DEBUG is not defined: debug_log is a no-op (no overhead)
 *   - Support log levels: LOG_ERROR, LOG_WARN, LOG_INFO, LOG_DEBUG
 *
 * The macro should be efficient — no runtime cost when disabled.
 * ============================================================ */
/* TODO: Define debug_log macro with log levels */

void problem3_debug_logging(void) {
    printf("=== Problem 3: Debug Logging ===\n");
    /* TODO: Use your debug_log macro */
    printf("TODO: Implement debug logging system\n\n");
}

/* ============================================================
 * Problem 4: Stringification and Token Pasting
 *
 * a) Write a macro STRINGIFY(x) that converts its argument to a string
 * b) Write a macro CONCAT(a,b) that concatenates two tokens
 * c) Write a macro MAKE_STRUCT(name) that creates a struct typedef
 *    and a create function for it
 *
 * Demonstrate: STRINGIFY(123) → "123"
 *              CONCAT(hello, world) → helloworld
 * ============================================================ */
/* TODO: Implement STRINGIFY, CONCAT, and MAKE_STRUCT */

void problem4_stringification(void) {
    printf("=== Problem 4: Stringification & Token Pasting ===\n");
    /* TODO: Demonstrate your macros */
    printf("TODO: Implement STRINGIFY and CONCAT macros\n\n");
}

/* ============================================================
 * Problem 5: Include Guard Simulation
 *
 * Write the content of a header file "mylib.h" that:
 *   a) Uses include guards (#ifndef / #define / #endif)
 *   b) Declares a function `int add(int a, int b)`
 *   c) Defines a macro `MYLIB_VERSION "1.0.0"`
 *   d) Uses #pragma once as well (non-standard but common)
 *
 * Then write "mylib.c" that implements the function.
 * ============================================================ */
/* TODO: Write mylib.h and mylib.c content as comments below */

void problem5_include_guards(void) {
    printf("=== Problem 5: Include Guards ===\n");
    printf("TODO: Write mylib.h with include guards\n\n");
}

/* ============================================================
 * Main
 * ============================================================ */
int main(void) {
    printf("====================================\n");
    printf("  Preprocessor — Exercises\n");
    printf("====================================\n\n");

    problem1_macros();
    problem2_container_macro();
    problem3_debug_logging();
    problem4_stringification();
    problem5_include_guards();

    return 0;
}
