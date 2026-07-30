package com.javaacademy.sprint1.arrays;

/**
 * ArrayAlgorithms - Demonstrates common array algorithms.
 *
 * <p><b>Common Array Algorithms:</b>
 * <ul>
 *   <li>Linear search - O(n)</li>
 *   <li>Binary search - O(log n) (requires sorted)</li>
 *   <li>Sorting: bubble, selection, insertion, merge, quick</li>
 *   <li>Min/Max finding - O(n)</li>
 *   <li>Reverse, rotate, partition</li>
 * </ul>
 *
 * <p><b>Real-world analogy:</b> Like organizing a library -
 * search = finding a book, sort = arranging by author/title.
 *
 * <p><b>Best Practice:</b> Use {@link java.util.Arrays} and {@link java.util.Collections}
 * for production code - they're optimized and well-tested.
 *
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class ArrayAlgorithms {

    private ArrayAlgorithms() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("=== Array Algorithms ===\n");

        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        System.out.println("Original: " + java.util.Arrays.toString(arr));

        // Linear Search
        System.out.println("\n--- Linear Search O(n) ---");
        int target = 22;
        int index = linearSearch(arr, target);
        System.out.println("Found " + target + " at index: " + index); // 4

        // Binary Search (requires sorted array)
        System.out.println("\n--- Binary Search O(log n) ---");
        int[] sorted = {11, 12, 22, 25, 34, 64, 90};
        int bsIndex = binarySearch(sorted, 25);
        System.out.println("Found 25 at index: " + bsIndex); // 3

        // Using Arrays.binarySearch
        System.out.println("Arrays.binarySearch: " + java.util.Arrays.binarySearch(sorted, 25));

        // Find Min/Max
        System.out.println("\n--- Min/Max ---");
        System.out.println("Min: " + findMin(arr));
        System.out.println("Max: " + findMax(arr));

        // Bubble Sort
        System.out.println("\n--- Bubble Sort O(n²) ---");
        int[] bubbleArr = arr.clone();
        bubbleSort(bubbleArr);
        System.out.println("Sorted: " + java.util.Arrays.toString(bubbleArr));

        // Selection Sort
        System.out.println("\n--- Selection Sort O(n²) ---");
        int[] selectionArr = arr.clone();
        selectionSort(selectionArr);
        System.out.println("Sorted: " + java.util.Arrays.toString(selectionArr));

        // Insertion Sort
        System.out.println("\n--- Insertion Sort O(n²) ---");
        int[] insertionArr = arr.clone();
        insertionSort(insertionArr);
        System.out.println("Sorted: " + java.util.Arrays.toString(insertionArr));

        // Arrays.sort (Dual-Pivot Quicksort for primitives, Timsort for objects)
        System.out.println("\n--- Arrays.sort (Optimized) ---");
        int[] sortArr = arr.clone();
        java.util.Arrays.sort(sortArr);
        System.out.println("Sorted: " + java.util.Arrays.toString(sortArr));

        // Parallel Sort (Java 8+) - uses ForkJoinPool
        System.out.println("\n--- Arrays.parallelSort ---");
        int[] largeArr = new int[10000];
        for (int i = 0; i < largeArr.length; i++) {
            largeArr[i] = (int) (Math.random() * 10000);
        }
        long start = System.nanoTime();
        java.util.Arrays.sort(largeArr);
        System.out.println("Sequential sort: " + (System.nanoTime() - start) / 1_000_000 + " ms");

        for (int i = 0; i < largeArr.length; i++) {
            largeArr[i] = (int) (Math.random() * 10000);
        }
        start = System.nanoTime();
        java.util.Arrays.parallelSort(largeArr);
        System.out.println("Parallel sort: " + (System.nanoTime() - start) / 1_000_000 + " ms");

        // Reverse array
        System.out.println("\n--- Reverse Array ---");
        int[] toReverse = {1, 2, 3, 4, 5};
        reverse(toReverse);
        System.out.println("Reversed: " + java.util.Arrays.toString(toReverse));

        // Rotate array (left by k)
        System.out.println("\n--- Rotate Array ---");
        int[] toRotate = {1, 2, 3, 4, 5, 6, 7};
        rotateLeft(toRotate, 3);
        System.out.println("Rotated left by 3: " + java.util.Arrays.toString(toRotate));

        // Remove duplicates (requires sorted)
        System.out.println("\n--- Remove Duplicates ---");
        int[] withDupes = {1, 1, 2, 2, 2, 3, 4, 4, 5};
        int[] unique = removeDuplicates(withDupes);
        System.out.println("Unique: " + java.util.Arrays.toString(unique));

        // Expected output demonstrates all algorithms
    }

    // Linear Search - O(n)
    static int linearSearch(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) return i;
        }
        return -1;
    }

    // Binary Search - O(log n), array MUST be sorted
    static int binarySearch(int[] arr, int target) {
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) return mid;
            if (arr[mid] < target) left = mid + 1;
            else right = mid - 1;
        }
        return -1;
    }

    static int findMin(int[] arr) {
        int min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < min) min = arr[i];
        }
        return min;
    }

    static int findMax(int[] arr) {
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) max = arr[i];
        }
        return max;
    }

    // Bubble Sort - O(n²), stable
    static void bubbleSort(int[] arr) {
        boolean swapped;
        for (int i = 0; i < arr.length - 1; i++) {
            swapped = false;
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) break; // Optimized: early exit if sorted
        }
    }

    // Selection Sort - O(n²), not stable
    static void selectionSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIdx]) minIdx = j;
            }
            if (minIdx != i) swap(arr, i, minIdx);
        }
    }

    // Insertion Sort - O(n²), stable, good for small/nearly sorted
    static void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Reverse in place
    static void reverse(int[] arr) {
        for (int i = 0, j = arr.length - 1; i < j; i++, j--) {
            swap(arr, i, j);
        }
    }

    // Rotate left by k (using reverse trick: O(n), O(1) space)
    static void rotateLeft(int[] arr, int k) {
        k = k % arr.length;
        if (k < 0) k += arr.length;
        reverse(arr, 0, k - 1);
        reverse(arr, k, arr.length - 1);
        reverse(arr, 0, arr.length - 1);
    }

    static void reverse(int[] arr, int start, int end) {
        while (start < end) swap(arr, start++, end--);
    }

    // Remove duplicates from sorted array - returns new array
    static int[] removeDuplicates(int[] arr) {
        if (arr.length <= 1) return arr;
        int uniqueCount = 1;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] != arr[uniqueCount - 1]) {
                arr[uniqueCount++] = arr[i];
            }
        }
        return java.util.Arrays.copyOf(arr, uniqueCount);
    }
}