# Memory Management Exercises

## Exercise 1: Dynamic Array
Implement a dynamic array that grows when full.

```c
#include <stdio.h>
#include <stdlib.h>

typedef struct {
    int *data;
    int size;
    int capacity;
} DynArray;

void init(DynArray *da, int initial_cap) {
    da->data = malloc(initial_cap * sizeof(int));
    da->size = 0;
    da->capacity = initial_cap;
}

void push(DynArray *da, int value) {
    if (da->size == da->capacity) {
        da->capacity *= 2;
        da->data = realloc(da->data, da->capacity * sizeof(int));
    }
    da->data[da->size++] = value;
}
```

## Exercise 2: Memory Pool
Implement a simple memory pool allocator.

## Exercise 3: String Builder
Create a dynamic string builder that grows as needed.

## Exercise 4: Memory Leak Detector
Write a wrapper for malloc/free that tracks allocations.

## Exercise 5: Custom Allocator
Implement a simple free-list allocator.
