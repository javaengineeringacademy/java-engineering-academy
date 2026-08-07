/*
 * Exercise: Pointers in C
 * Difficulty: ★★★☆☆ (3/5)
 * Learning Objectives:
 *   - Understand pointer declaration and initialization
 *   - Learn about address-of (&) and dereference (*) operators
 *   - Practice pointer arithmetic
 *   - Understand pointer-to-pointer concepts
 */

#include <stdio.h>

int main(void) {
    int x = 42;
    int *ptr = &x;

    /*
     * TODO 1: Print the address of x using & operator
     * Expected output format: "Address of x: %p\n"
     */

    /*
     * TODO 2: Print the value stored in ptr (the address of x)
     * Expected output: Same address as TODO 1
     */

    /*
     * TODO 3: Print the value pointed to by ptr using dereference operator
     * Expected output: "Value at ptr: 42\n"
     */

    /*
     * TODO 4: Modify the value of x through the pointer
     * Set it to 100 using *ptr = 100
     * Print x to verify: "x = 100\n"
     */

    /*
     * TODO 5: Demonstrate pointer-to-pointer
     * Create a pointer to ptr (int **pptr)
     * Print the address of ptr, the value of pptr, and the value it points to
     */

    /*
     * TODO 6: Swap two numbers using pointers
     * a = 10, b = 20
     * After swap: a = 20, b = 10
     */

    /*
     * TODO 7: Demonstrate pointer arithmetic with arrays
     * Create an array {10, 20, 30, 40, 50}
     * Use pointer arithmetic to access each element
     * Print: "Element at index %d: %d\n"
     */

    /*
     * TODO 8: Show the difference between pointers and arrays
     * int arr[] = {1, 2, 3};
     * int *p = arr;
     * Verify arr[i] == *(p+i) for all i
     */

    /* Test cases */
    /*
    printf("=== Test Cases ===\n");
    printf("Address of x: %p\n", (void*)&x);
    printf("Pointer value: %p\n", (void*)ptr);
    printf("Value at pointer: %d (expected: 42)\n", *ptr);
    printf("x after modification: %d (expected: 100)\n", x);
    printf("a after swap: %d (expected: 20)\n", a);
    printf("b after swap: %d (expected: 10)\n", b);
    */

    return 0;
}
