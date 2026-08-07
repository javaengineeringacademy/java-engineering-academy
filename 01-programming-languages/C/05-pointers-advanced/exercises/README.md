# Advanced Pointers Exercises

## Exercise 1: Dynamic 2D Array
Allocate a 2D array dynamically using pointer-to-pointer.

```c
#include <stdio.h>
#include <stdlib.h>

int **create_matrix(int rows, int cols) {
    int **matrix = malloc(rows * sizeof(int *));
    for (int i = 0; i < rows; i++) {
        matrix[i] = malloc(cols * sizeof(int));
    }
    return matrix;
}

void free_matrix(int **matrix, int rows) {
    for (int i = 0; i < rows; i++) {
        free(matrix[i]);
    }
    free(matrix);
}
```

## Exercise 2: Function Pointer Table
Create a dispatch table using an array of function pointers.

## Exercise 3: Generic Sort
Implement a sort function that uses a comparison function pointer.

## Exercise 4: Opaque Handle
Design an opaque pointer interface for a resource manager.

## Exercise 5: Variable Argument Processing
Implement a function that processes variable arguments using va_list.
