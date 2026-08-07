/*
 * Exercise: Advanced Pointer Concepts in C
 * Difficulty: ★★★★☆ (4/5)
 * Learning Objectives:
 *   - Master pointer arithmetic
 *   - Understand function pointers
 *   - Practice pointer arrays and arrays of pointers
 *   - Learn about void pointers and their uses
 */

#include <stdio.h>
#include <stdlib.h>

/*
 * TODO 1: Demonstrate pointer arithmetic with different data types
 * Create an int pointer, show how it advances by sizeof(int)
 * Create a char pointer, show how it advances by sizeof(char)
 */

/*
 * TODO 2: Use pointer arithmetic to implement array traversal
 * Don't use array indexing - only use pointers
 * Traverse and print: {10, 20, 30, 40, 50}
 */

/*
 * TODO 3: Implement a function using pointer arithmetic
 * Function: reverse_array(int *arr, int size)
 * Reverse the array in place using only pointers
 */

/*
 * TODO 4: Create an array of function pointers
 * Function pointers should point to: add, subtract, multiply, divide
 * Use the function pointers to perform calculations
 */

/*
 * TODO 5: Implement a callback function pattern
 * Write a function that takes a function pointer as parameter
 * Apply the function to each element of an array
 */

/*
 * TODO 6: Demonstrate void pointer usage
 * Create a void* that can point to different types
 * Show how to properly dereference it
 */

/*
 * TODO 7: Create a dynamic 2D array using pointer arithmetic
 * Allocate memory for a 3x4 matrix
 * Access elements using pointer arithmetic
 * Free all allocated memory
 */

/*
 * TODO 8: Implement a simple string array using pointer to pointer
 * Create an array of 3 strings
 * Access and print each string using pointer arithmetic
 */

/* Helper functions for function pointer exercise */
int add(int a, int b) { return a + b; }
int subtract(int a, int b) { return a - b; }
int multiply(int a, int b) { return a * b; }
int divide(int a, int b) { return b != 0 ? a / b : 0; }

/* Callback function for apply_to_array */
void double_value(int *value) { *value *= 2; }

int main(void) {
    /* Test cases */
    /*
    printf("=== Test Cases ===\n");

    // Test pointer arithmetic
    int arr[] = {10, 20, 30, 40, 50};
    int *p = arr;
    printf("Array via pointer arithmetic: ");
    for (int i = 0; i < 5; i++) {
        printf("%d ", *(p + i));
    }
    printf("\n");

    // Test reverse array
    int test_arr[] = {1, 2, 3, 4, 5};
    reverse_array(test_arr, 5);
    printf("Reversed: %d %d %d %d %d (expected: 5 4 3 2 1)\n",
           test_arr[0], test_arr[1], test_arr[2], test_arr[3], test_arr[4]);

    // Test function pointers
    int (*operations[])(int, int) = {add, subtract, multiply, divide};
    char *op_names[] = {"+", "-", "*", "/"};
    int a = 10, b = 3;
    for (int i = 0; i < 4; i++) {
        printf("%d %s %d = %d\n", a, op_names[i], b, operations[i](a, b));
    }

    // Test callback
    int values[] = {1, 2, 3, 4, 5};
    apply_to_array(values, 5, double_value);
    printf("After doubling: ");
    for (int i = 0; i < 5; i++) printf("%d ", values[i]);
    printf(" (expected: 2 4 6 8 10)\n");

    // Test dynamic 2D array
    int **matrix = create_matrix(3, 4);
    printf("Dynamic matrix (3x4):\n");
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 4; j++) {
            printf("%3d ", matrix[i][j]);
        }
        printf("\n");
    }
    free_matrix(matrix, 3);
    */

    return 0;
}
