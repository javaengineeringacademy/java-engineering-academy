/*
 * Exercise: Sorting Algorithms in C
 * Difficulty: ★★★★☆ (4/5)
 * Learning Objectives:
 *   - Implement bubble sort algorithm
 *   - Implement selection sort algorithm
 *   - Implement insertion sort algorithm
 *   - Understand time complexity of each algorithm
 */

#include <stdio.h>

#define SIZE 8

void print_array(int arr[], int size) {
    for (int i = 0; i < size; i++) {
        printf("%d ", arr[i]);
    }
    printf("\n");
}

/*
 * TODO 1: Implement Bubble Sort
 * - Compare adjacent elements and swap if needed
 * - Repeat until no swaps are made
 * - Time complexity: O(n²)
 * - Stable sort
 */
void bubble_sort(int arr[], int size) {
    /* Your code here */
}

/*
 * TODO 2: Implement Selection Sort
 * - Find the minimum element in unsorted portion
 * - Swap it with the first unsorted element
 * - Time complexity: O(n²)
 * - Not stable
 */
void selection_sort(int arr[], int size) {
    /* Your code here */
}

/*
 * TODO 3: Implement Insertion Sort
 * - Take each element and insert it into its correct position
 * - Shift larger elements to the right
 * - Time complexity: O(n²)
 * - Stable sort, efficient for small or nearly sorted arrays
 */
void insertion_sort(int arr[], int size) {
    /* Your code here */
}

/*
 * TODO 4: Implement a helper function to swap two integers
 */
void swap(int *a, int *b) {
    /* Your code here */
}

/*
 * TODO 5: Implement a function to check if an array is sorted
 * Returns 1 if sorted, 0 otherwise
 */
int is_sorted(int arr[], int size) {
    /* Your code here */
    return 0;
}

/*
 * TODO 6: Implement a function to copy an array
 */
void copy_array(int source[], int dest[], int size) {
    /* Your code here */
}

int main(void) {
    int original[] = {64, 34, 25, 12, 22, 11, 90, 45};
    int arr[SIZE];

    /* Test cases */
    /*
    printf("=== Sorting Algorithm Tests ===\n\n");

    // Test Bubble Sort
    copy_array(original, arr, SIZE);
    printf("Original: ");
    print_array(original, SIZE);
    bubble_sort(arr, SIZE);
    printf("Bubble Sort result: ");
    print_array(arr, SIZE);
    printf("Sorted correctly: %s\n\n", is_sorted(arr, SIZE) ? "Yes" : "No");

    // Test Selection Sort
    copy_array(original, arr, SIZE);
    selection_sort(arr, SIZE);
    printf("Selection Sort result: ");
    print_array(arr, SIZE);
    printf("Sorted correctly: %s\n\n", is_sorted(arr, SIZE) ? "Yes" : "No");

    // Test Insertion Sort
    copy_array(original, arr, SIZE);
    insertion_sort(arr, SIZE);
    printf("Insertion Sort result: ");
    print_array(arr, SIZE);
    printf("Sorted correctly: %s\n\n", is_sorted(arr, SIZE) ? "Yes" : "No");

    // Test with already sorted array
    int sorted[] = {1, 2, 3, 4, 5, 6, 7, 8};
    printf("Already sorted input: ");
    print_array(sorted, SIZE);
    bubble_sort(sorted, SIZE);
    printf("Bubble Sort on sorted: ");
    print_array(sorted, SIZE);

    // Test with reverse sorted array
    int reverse[] = {8, 7, 6, 5, 4, 3, 2, 1};
    printf("\nReverse sorted input: ");
    print_array(reverse, SIZE);
    insertion_sort(reverse, SIZE);
    printf("Insertion Sort on reverse: ");
    print_array(reverse, SIZE);
    */

    return 0;
}
