/*
 * Sorting Algorithms — C Language
 * Solutions: Complete implementations for all exercises
 *
 * Compile with: gcc -Wall -Wextra -std=c99 -o solutions solutions.c
 */

#include <stdio.h>

#define SIZE 8

/* ============================================================
 * Helper: Swap
 * ============================================================ */
void swap(int *a, int *b) {
    int temp = *a;
    *a = *b;
    *b = temp;
}

/* ============================================================
 * Helper: Print Array
 * ============================================================ */
void print_array(int arr[], int size) {
    printf("  ");
    for (int i = 0; i < size; i++) printf("%d ", arr[i]);
    printf("\n");
}

/* ============================================================
 * Helper: Copy Array
 * ============================================================ */
void copy_array(int source[], int dest[], int size) {
    for (int i = 0; i < size; i++) dest[i] = source[i];
}

/* ============================================================
 * Helper: Check if Sorted
 * ============================================================ */
int is_sorted(int arr[], int size) {
    for (int i = 0; i < size - 1; i++) {
        if (arr[i] > arr[i + 1]) return 0;
    }
    return 1;
}

/* ============================================================
 * Problem 1: Bubble Sort — O(n²), Stable
 * ============================================================ */
void bubble_sort(int arr[], int size) {
    for (int i = 0; i < size - 1; i++) {
        int swapped = 0;
        for (int j = 0; j < size - 1 - i; j++) {
            if (arr[j] > arr[j + 1]) {
                swap(&arr[j], &arr[j + 1]);
                swapped = 1;
            }
        }
        if (!swapped) break;  /* Early exit if already sorted */
    }
}

/* ============================================================
 * Problem 2: Selection Sort — O(n²), Not Stable
 * ============================================================ */
void selection_sort(int arr[], int size) {
    for (int i = 0; i < size - 1; i++) {
        int min_idx = i;
        for (int j = i + 1; j < size; j++) {
            if (arr[j] < arr[min_idx]) min_idx = j;
        }
        if (min_idx != i) swap(&arr[i], &arr[min_idx]);
    }
}

/* ============================================================
 * Problem 3: Insertion Sort — O(n²), Stable
 * ============================================================ */
void insertion_sort(int arr[], int size) {
    for (int i = 1; i < size; i++) {
        int key = arr[i];
        int j = i - 1;
        while (j >= 0 && arr[j] > key) {
            arr[j + 1] = arr[j];
            j--;
        }
        arr[j + 1] = key;
    }
}

/* ============================================================
 * Main
 * ============================================================ */
int main(void) {
    int original[] = {64, 34, 25, 12, 22, 11, 90, 45};
    int arr[SIZE];

    printf("====================================\n");
    printf("  Sorting Algorithms — Solutions\n");
    printf("====================================\n\n");

    printf("Original array:");
    print_array(original, SIZE);
    printf("\n");

    /* Bubble Sort */
    copy_array(original, arr, SIZE);
    bubble_sort(arr, SIZE);
    printf("Bubble Sort:   ");
    print_array(arr, SIZE);
    printf("  Sorted: %s\n\n", is_sorted(arr, SIZE) ? "Yes" : "No");

    /* Selection Sort */
    copy_array(original, arr, SIZE);
    selection_sort(arr, SIZE);
    printf("Selection Sort:");
    print_array(arr, SIZE);
    printf("  Sorted: %s\n\n", is_sorted(arr, SIZE) ? "Yes" : "No");

    /* Insertion Sort */
    copy_array(original, arr, SIZE);
    insertion_sort(arr, SIZE);
    printf("Insertion Sort:");
    print_array(arr, SIZE);
    printf("  Sorted: %s\n\n", is_sorted(arr, SIZE) ? "Yes" : "No");

    /* Test on already sorted */
    int sorted[] = {1, 2, 3, 4, 5, 6, 7, 8};
    bubble_sort(sorted, SIZE);
    printf("Bubble on sorted: ");
    print_array(sorted, SIZE);

    /* Test on reverse sorted */
    int reverse[] = {8, 7, 6, 5, 4, 3, 2, 1};
    insertion_sort(reverse, SIZE);
    printf("Insertion on reverse: ");
    print_array(reverse, SIZE);

    return 0;
}
