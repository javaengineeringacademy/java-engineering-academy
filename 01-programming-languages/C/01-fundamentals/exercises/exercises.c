/*
 * Fundamentals — C Language
 * Exercises: Variables, Operators, Control Flow, Functions, Arrays, Strings, Pointers, Memory
 *
 * Complete the TODO sections. Each exercise has 5 problems.
 * Compile with: gcc -Wall -Wextra -std=c99 -o exercises exercises.c
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <limits.h>

/* ============================================================
 * Problem 1: Variables — Temperature Converter
 *
 * Write a function `celsius_to_fahrenheit` that takes a double
 * (Celsius) and returns the equivalent Fahrenheit value using
 * the formula: F = C * 9.0/5.0 + 32.
 *
 * Then write `celsius_to_kelvin` that converts Celsius to Kelvin:
 * K = C + 273.15
 *
 * Both functions must handle negative values correctly.
 * ============================================================ */
double celsius_to_fahrenheit(double celsius) {
    /* TODO: Implement conversion */
    return 0.0;
}

double celsius_to_kelvin(double celsius) {
    /* TODO: Implement conversion */
    return 0.0;
}

void problem1_variables(void) {
    printf("=== Problem 1: Variables ===\n");
    double temps[] = {0.0, 100.0, -40.0, 37.5};
    for (int i = 0; i < 4; i++) {
        printf("%.1f°C = %.1f°F = %.1fK\n",
               temps[i],
               celsius_to_fahrenheit(temps[i]),
               celsius_to_kelvin(temps[i]));
    }
    printf("\n");
}

/* ============================================================
 * Problem 2: Operators — Bitwise Flags
 *
 * A system uses bit flags to represent permissions:
 *   bit 0 (0x01): READ
 *   bit 1 (0x02): WRITE
 *   bit 2 (0x04): EXECUTE
 *   bit 3 (0x08): DELETE
 *
 * Write functions to:
 *   a) `set_permission(unsigned int *perms, unsigned int flag)` — set a flag
 *   b) `clear_permission(unsigned int *perms, unsigned int flag)` — clear a flag
 *   c) `check_permission(unsigned int perms, unsigned int flag)` — check if set
 *   d) `print_permissions(unsigned int perms)` — print all active permissions
 * ============================================================ */
#define PERM_READ    0x01
#define PERM_WRITE   0x02
#define PERM_EXECUTE 0x04
#define PERM_DELETE  0x08

void set_permission(unsigned int *perms, unsigned int flag) {
    /* TODO: Set the bit using bitwise OR */
}

void clear_permission(unsigned int *perms, unsigned int flag) {
    /* TODO: Clear the bit using bitwise AND with complement */
}

int check_permission(unsigned int perms, unsigned int flag) {
    /* TODO: Check the bit using bitwise AND */
    return 0;
}

void print_permissions(unsigned int perms) {
    /* TODO: Print each active permission name */
    printf("  Permissions: ");
    /* Your code here */
    printf("\n");
}

void problem2_operators(void) {
    printf("=== Problem 2: Operators ===\n");
    unsigned int perms = 0;
    set_permission(&perms, PERM_READ);
    set_permission(&perms, PERM_WRITE);
    print_permissions(perms);

    printf("Has READ? %s\n", check_permission(perms, PERM_READ) ? "yes" : "no");
    printf("Has EXECUTE? %s\n", check_permission(perms, PERM_EXECUTE) ? "yes" : "no");

    clear_permission(&perms, PERM_WRITE);
    print_permissions(perms);
    printf("\n");
}

/* ============================================================
 * Problem 3: Control Flow — FizzBuzz with a Twist
 *
 * Print numbers 1 to 100, but:
 *   - For multiples of 3, print "Fizz"
 *   - For multiples of 5, print "Buzz"
 *   - For multiples of both 3 and 5, print "FizzBuzz"
 *   - For prime numbers, print "Prime"
 *   - If a number is both FizzBuzz and Prime, print "FizzBuzz+Prime"
 *
 * Hint: You'll need a helper function `is_prime`.
 * ============================================================ */
int is_prime(int n) {
    /* TODO: Return 1 if n is prime, 0 otherwise */
    return 0;
}

void problem3_control_flow(void) {
    printf("=== Problem 3: Control Flow ===\n");
    /* TODO: Loop from 1 to 100 and apply the FizzBuzz+Prime rules */
    printf("TODO: Implement FizzBuzz with Prime detection\n\n");
}

/* ============================================================
 * Problem 4: Functions — Recursive Array Operations
 *
 * Write these functions using recursion (no loops allowed):
 *   a) `array_sum(int *arr, int size)` — return the sum of all elements
 *   b) `array_max(int *arr, int size)` — return the maximum element
 *   c) `array_reverse(int *arr, int size)` — reverse the array in place
 *   d) `array_count_occurrences(int *arr, int size, int target)` — count occurrences
 * ============================================================ */
int array_sum(int *arr, int size) {
    /* TODO: Recursive sum */
    return 0;
}

int array_max(int *arr, int size) {
    /* TODO: Recursive max */
    return 0;
}

void array_reverse(int *arr, int size) {
    /* TODO: Recursive in-place reverse */
}

int array_count_occurrences(int *arr, int size, int target) {
    /* TODO: Recursive count */
    return 0;
}

void problem4_functions(void) {
    printf("=== Problem 4: Functions ===\n");
    int arr[] = {3, 7, 2, 9, 4, 7, 1, 8, 7};
    int size = sizeof(arr) / sizeof(arr[0]);

    printf("Array: ");
    for (int i = 0; i < size; i++) printf("%d ", arr[i]);
    printf("\n");

    printf("Sum: %d\n", array_sum(arr, size));
    printf("Max: %d\n", array_max(arr, size));
    printf("Count of 7: %d\n", array_count_occurrences(arr, size, 7));

    array_reverse(arr, size);
    printf("Reversed: ");
    for (int i = 0; i < size; i++) printf("%d ", arr[i]);
    printf("\n\n");
}

/* ============================================================
 * Problem 5: Strings — String Utilities
 *
 * Implement these string functions WITHOUT using <string.h>:
 *   a) `my_strlen(const char *s)` — return string length
 *   b) `my_strcpy(char *dest, const char *src)` — copy string
 *   c) `my_strcat(char *dest, const char *src)` — concatenate
 *   d) `my_strcmp(const char *a, const char *b)` — compare strings
 *   e) `reverse_string(char *s)` — reverse in place
 * ============================================================ */
int my_strlen(const char *s) {
    /* TODO: Count characters until '\0' */
    return 0;
}

void my_strcpy(char *dest, const char *src) {
    /* TODO: Copy each character including null terminator */
}

void my_strcat(char *dest, const char *src) {
    /* TODO: Find end of dest, then append src */
}

int my_strcmp(const char *a, const char *b) {
    /* TODO: Compare character by character */
    return 0;
}

void reverse_string(char *s) {
    /* TODO: Reverse in place using two pointers */
}

void problem5_strings(void) {
    printf("=== Problem 5: Strings ===\n");
    char dest[100] = "Hello";
    const char *src = " World!";

    printf("Length of \"%s\": %d\n", dest, my_strlen(dest));
    my_strcat(dest, src);
    printf("After strcat: \"%s\"\n", dest);

    reverse_string(dest);
    printf("After reverse: \"%s\"\n", dest);

    printf("strcmp(\"abc\", \"abd\"): %d\n", my_strcmp("abc", "abd"));
    printf("strcmp(\"abc\", \"abc\"): %d\n", my_strcmp("abc", "abc"));
    printf("strcmp(\"abd\", \"abc\"): %d\n", my_strcmp("abd", "abc"));
    printf("\n");
}

/* ============================================================
 * Main
 * ============================================================ */
int main(void) {
    printf("====================================\n");
    printf("  Fundamentals — Exercises\n");
    printf("====================================\n\n");

    problem1_variables();
    problem2_operators();
    problem3_control_flow();
    problem4_functions();
    problem5_strings();

    return 0;
}
