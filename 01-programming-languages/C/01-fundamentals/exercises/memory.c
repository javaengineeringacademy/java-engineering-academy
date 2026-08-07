/*
 * Exercise: Dynamic Memory in C
 * Difficulty: ★★★☆☆ (3/5)
 * Learning Objectives:
 *   - Understand dynamic memory allocation
 *   - Master malloc(), calloc(), realloc(), and free()
 *   - Learn about memory leaks and how to avoid them
 *   - Practice working with dynamic arrays
 */

#include <stdio.h>
#include <stdlib.h>

int main(void) {
    /*
     * TODO 1: Use malloc() to allocate memory for an integer
     * Initialize it to 42, print the value, then free the memory
     * Always check if malloc returned NULL
     */

    /*
     * TODO 2: Use malloc() to allocate an array of 5 integers
     * Fill it with values 1-5, print all elements
     */

    /*
     * TODO 3: Use calloc() to allocate memory for 10 integers
     * Verify all elements are initialized to 0
     */

    /*
     * TODO 4: Use realloc() to resize the array from TODO 2
     * Expand from 5 to 10 elements
     * Fill new elements with 6-10, print all elements
     */

    /*
     * TODO 5: Demonstrate proper memory deallocation
     * Allocate, use, and free memory in the correct order
     * Show what happens if you don't free (conceptual - don't actually leak)
     */

    /*
     * TODO 6: Create a dynamic array of strings
     * Allocate memory for 3 strings, each holding up to 50 characters
     * Store names, print them, then free all memory
     */

    /*
     * TODO 7: Demonstrate the risk of using freed memory
     * Allocate an int, free it, explain why accessing it is undefined behavior
     * (Comment: Do NOT actually access freed memory)
     */

    /*
     * TODO 8: Check for malloc failure
     * Try to allocate an extremely large amount of memory
     * Handle the NULL return gracefully
     */

    /* Test cases */
    /*
    printf("=== Test Cases ===\n");
    printf("Allocated value: %d (expected: 42)\n", *ptr);
    printf("Array elements: ");
    for (int i = 0; i < 5; i++) printf("%d ", arr[i]);
    printf("\n");
    printf("Calloc array (should be zeros): ");
    for (int i = 0; i < 10; i++) printf("%d ", calloc_arr[i]);
    printf("\n");
    printf("After realloc (10 elements): ");
    for (int i = 0; i < 10; i++) printf("%d ", arr[i]);
    printf("\n");
    */

    return 0;
}
