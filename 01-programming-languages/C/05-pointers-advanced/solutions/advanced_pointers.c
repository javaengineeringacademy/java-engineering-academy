/*
 * Advanced Pointers — C Language
 * Solutions: Complete implementations for all exercises
 *
 * Compile with: gcc -Wall -Wextra -std=c99 -o solutions solutions.c
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* ============================================================
 * Problem 1: Pointer Arithmetic with Different Types
 * ============================================================ */
void problem1_pointer_arithmetic(void) {
    printf("=== Problem 1: Pointer Arithmetic ===\n");

    int int_arr[] = {10, 20, 30, 40, 50};
    int *ip = int_arr;

    printf("  int pointer sizeof: %zu bytes\n", sizeof(int));
    printf("  int_arr address:    %p\n", (void *)int_arr);
    printf("  ip + 1 address:     %p (offset %zu bytes)\n",
           (void *)(ip + 1), (char *)(ip + 1) - (char *)int_arr);
    printf("  ip + 2 address:     %p (offset %zu bytes)\n",
           (void *)(ip + 2), (char *)(ip + 2) - (char *)int_arr);

    char char_arr[] = {'A', 'B', 'C', 'D', 'E'};
    char *cp = char_arr;

    printf("\n  char pointer sizeof: %zu bytes\n", sizeof(char));
    printf("  char_arr address:    %p\n", (void *)char_arr);
    printf("  cp + 1 address:      %p (offset %zu bytes)\n",
           (void *)(cp + 1), (char *)(cp + 1) - (char *)char_arr);

    printf("  Each pointer type advances by sizeof(type) per increment.\n\n");
}

/* ============================================================
 * Problem 2: Pointer Arithmetic Array Traversal
 * ============================================================ */
void problem2_array_traversal(void) {
    printf("=== Problem 2: Array Traversal via Pointers ===\n");

    int arr[] = {10, 20, 30, 40, 50};
    int size = 5;

    printf("  Forward traversal:  ");
    for (int *p = arr; p < arr + size; p++) {
        printf("%d ", *p);
    }
    printf("\n");

    printf("  Reverse traversal:  ");
    for (int *p = arr + size - 1; p >= arr; p--) {
        printf("%d ", *p);
    }
    printf("\n");

    printf("  Pointer increment:  ");
    int *p = arr;
    for (int i = 0; i < size; i++) {
        printf("arr[%d]=%d ", i, *p++);
    }
    printf("\n\n");
}

/* ============================================================
 * Problem 3: Reverse Array with Pointers
 * ============================================================ */
void reverse_array(int *arr, int size) {
    int *left = arr;
    int *right = arr + size - 1;

    while (left < right) {
        int temp = *left;
        *left = *right;
        *right = temp;
        left++;
        right--;
    }
}

void problem3_reverse_array(void) {
    printf("=== Problem 3: Reverse Array ===\n");

    int arr[] = {1, 2, 3, 4, 5};
    int size = 5;

    printf("  Before: ");
    for (int i = 0; i < size; i++) printf("%d ", arr[i]);
    printf("\n");

    reverse_array(arr, size);

    printf("  After:  ");
    for (int i = 0; i < size; i++) printf("%d ", arr[i]);
    printf("\n\n");
}

/* ============================================================
 * Problem 4: Array of Function Pointers
 * ============================================================ */
int add(int a, int b) { return a + b; }
int subtract(int a, int b) { return a - b; }
int multiply(int a, int b) { return a * b; }
int divide(int a, int b) { return b != 0 ? a / b : 0; }

void problem4_function_pointers(void) {
    printf("=== Problem 4: Function Pointers ===\n");

    int (*operations[])(int, int) = {add, subtract, multiply, divide};
    const char *op_names[] = {"+", "-", "*", "/"};
    int num_ops = sizeof(operations) / sizeof(operations[0]);

    int a = 20, b = 6;
    printf("  Operations on %d and %d:\n", a, b);
    for (int i = 0; i < num_ops; i++) {
        printf("    %d %s %d = %d\n", a, op_names[i], b, operations[i](a, b));
    }
    printf("\n");
}

/* ============================================================
 * Problem 5: Callback Function Pattern
 * ============================================================ */
void apply_to_array(int *arr, int size, void (*func)(int *)) {
    for (int i = 0; i < size; i++) {
        func(&arr[i]);
    }
}

void double_value(int *val) { *val *= 2; }
void negate_value(int *val) { *val = -*val; }
void square_value(int *val) { *val *= *val; }

void problem5_callback(void) {
    printf("=== Problem 5: Callback Pattern ===\n");

    int arr[] = {1, 2, 3, 4, 5};
    int size = 5;

    printf("  Original:    ");
    for (int i = 0; i < size; i++) printf("%d ", arr[i]);
    printf("\n");

    apply_to_array(arr, size, double_value);
    printf("  After double: ");
    for (int i = 0; i < size; i++) printf("%d ", arr[i]);
    printf("\n");

    apply_to_array(arr, size, negate_value);
    printf("  After negate: ");
    for (int i = 0; i < size; i++) printf("%d ", arr[i]);
    printf("\n");

    apply_to_array(arr, size, square_value);
    printf("  After square: ");
    for (int i = 0; i < size; i++) printf("%d ", arr[i]);
    printf("\n\n");
}

/* ============================================================
 * Problem 6: Void Pointer Usage
 * ============================================================ */
void print_generic(void *data, char type) {
    switch (type) {
        case 'i': printf("  int: %d\n", *(int *)data); break;
        case 'f': printf("  float: %.2f\n", *(float *)data); break;
        case 'c': printf("  char: %c\n", *(char *)data); break;
        case 's': printf("  string: %s\n", (char *)data); break;
    }
}

void problem6_void_pointer(void) {
    printf("=== Problem 6: Void Pointer ===\n");

    int i = 42;
    float f = 3.14f;
    char c = 'Z';
    const char *s = "Hello, void pointers!";

    print_generic(&i, 'i');
    print_generic(&f, 'f');
    print_generic(&c, 'c');
    print_generic((void *)s, 's');

    printf("  void* can point to any type; cast before dereferencing.\n\n");
}

/* ============================================================
 * Problem 7: Dynamic 2D Array
 * ============================================================ */
int **create_matrix(int rows, int cols) {
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

void problem7_dynamic_2d(void) {
    printf("=== Problem 7: Dynamic 2D Array ===\n");

    int rows = 3, cols = 4;
    int **matrix = create_matrix(rows, cols);

    if (matrix) {
        /* Fill with row*col values */
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = i * cols + j + 1;
            }
        }

        printf("  %dx%d matrix:\n", rows, cols);
        for (int i = 0; i < rows; i++) {
            printf("    ");
            for (int j = 0; j < cols; j++) {
                printf("%3d ", matrix[i][j]);
            }
            printf("\n");
        }
        free_matrix(matrix, rows);
    }
    printf("\n");
}

/* ============================================================
 * Problem 8: String Array with Pointer-to-Pointer
 * ============================================================ */
void problem8_string_array(void) {
    printf("=== Problem 8: String Array (char**) ===\n");

    const char *names[] = {"Alice", "Bob", "Charlie"};
    int count = 3;

    printf("  Using pointer-to-pointer:\n");
    const char **p = names;
    for (int i = 0; i < count; i++) {
        printf("    names[%d] = \"%s\" (address: %p)\n", i, p[i], (void *)p[i]);
    }

    /* Dynamic string array */
    char **dyn_names = malloc(count * sizeof(char *));
    for (int i = 0; i < count; i++) {
        dyn_names[i] = malloc(strlen(names[i]) + 1);
        strcpy(dyn_names[i], names[i]);
    }

    printf("  Dynamic copy:\n");
    for (int i = 0; i < count; i++) {
        printf("    dyn[%d] = \"%s\"\n", i, dyn_names[i]);
        free(dyn_names[i]);
    }
    free(dyn_names);
    printf("\n");
}

/* ============================================================
 * Main
 * ============================================================ */
int main(void) {
    printf("====================================\n");
    printf("  Advanced Pointers — Solutions\n");
    printf("====================================\n\n");

    problem1_pointer_arithmetic();
    problem2_array_traversal();
    problem3_reverse_array();
    problem4_function_pointers();
    problem5_callback();
    problem6_void_pointer();
    problem7_dynamic_2d();
    problem8_string_array();

    return 0;
}
