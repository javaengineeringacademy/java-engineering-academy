package academy.javaengineering.fundamentals;

import java.util.Arrays;
import java.util.Collections;

/**
 * Demonstrates Java arrays: declaration, initialization, multi-dimensional
 * arrays, the Arrays utility class, System.arraycopy, iteration, and
 * common algorithms (search, sort, reverse).
 *
 * <p>Arrays are fixed-size containers that hold elements of the same type.
 * They provide O(1) random access but cannot be resized after creation.</p>
 */
public class ArraysDemo {

    public static void main(String[] args) {
        System.out.println("=== Arrays Demo ===\n");

        demoDeclarationAndInitialization();
        demoArrayProperties();
        demoMultidimensionalArrays();
        demoArraysUtilityClass();
        demoSystemArraycopy();
        demoIteration();
        demoSearchAlgorithms();
        demoSortAlgorithms();
        demoReverseArray();
    }

    // --- Declaration and Initialization ---

    /**
     * Demonstrates different ways to declare and initialize arrays.
     */
    public static void demoDeclarationAndInitialization() {
        System.out.println("--- Declaration and Initialization ---");

        // Declaration + allocation
        int[] numbers = new int[5]; // Default values: 0
        System.out.println("new int[5]: " + java.util.Arrays.toString(numbers));

        // Declaration + initialization (literal)
        int[] primes = {2, 3, 5, 7, 11, 13};
        System.out.println("Prime numbers: " + java.util.Arrays.toString(primes));

        // Alternative initialization
        int[] evens = new int[]{2, 4, 6, 8, 10};
        System.out.println("Even numbers: " + java.util.Arrays.toString(evens));

        // String array
        String[] colors = {"Red", "Green", "Blue"};
        System.out.println("Colors: " + java.util.Arrays.toString(colors));

        // Default values for different types
        boolean[] bools = new boolean[3];
        double[] doubles = new double[3];
        char[] chars = new char[3];
        String[] strings = new String[3];

        System.out.println("\nDefault values:");
        System.out.println("  boolean[]: " + java.util.Arrays.toString(bools));
        System.out.println("  double[]:  " + java.util.Arrays.toString(doubles));
        System.out.println("  char[]:    " + java.util.Arrays.toString(chars));
        System.out.println("  String[]:  " + java.util.Arrays.toString(strings));

        // Array of objects
        java.time.LocalDate[] dates = new java.time.LocalDate[3];
        dates[0] = java.time.LocalDate.of(2024, 1, 1);
        dates[1] = java.time.LocalDate.of(2024, 6, 15);
        dates[2] = java.time.LocalDate.of(2024, 12, 31);
        System.out.println("Dates: " + java.util.Arrays.toString(dates));
        System.out.println();
    }

    // --- Array Properties ---

    /**
     * Demonstrates array length and bounds checking.
     */
    public static void demoArrayProperties() {
        System.out.println("--- Array Properties ---");

        int[] arr = {10, 20, 30, 40, 50};

        // Length property
        System.out.println("Array: " + java.util.Arrays.toString(arr));
        System.out.println("Length: " + arr.length);

        // Bounds checking
        System.out.println("First element (arr[0]): " + arr[0]);
        System.out.println("Last element (arr[arr.length-1]): " + arr[arr.length - 1]);

        try {
            int outOfBounds = arr[10];
            System.out.println("This won't print: " + outOfBounds);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("arr[10] throws: " + e.getMessage());
        }

        // Zero-length array
        int[] empty = new int[0];
        System.out.println("Empty array length: " + empty.length);
        System.out.println("Empty array toString: " + java.util.Arrays.toString(empty));
        System.out.println();
    }

    // --- Multi-dimensional Arrays ---

    /**
     * Demonstrates multi-dimensional and jagged arrays.
     */
    public static void demoMultidimensionalArrays() {
        System.out.println("--- Multi-dimensional Arrays ---");

        // 2D array - matrix
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };

        System.out.println("3x3 Matrix:");
        for (int[] row : matrix) {
            System.out.println("  " + java.util.Arrays.toString(row));
        }
        System.out.println("Dimensions: " + matrix.length + " rows x " + matrix[0].length + " cols");

        // Accessing elements
        System.out.println("matrix[1][2] = " + matrix[1][2]);
        System.out.println("matrix[0] = " + java.util.Arrays.toString(matrix[0]));

        // 2D array allocation
        int[][] grid = new int[3][4];
        System.out.println("\n3x4 Grid (default values):");
        for (int[] row : grid) {
            System.out.println("  " + java.util.Arrays.toString(row));
        }

        // Jagged array (rows of different lengths)
        int[][] jagged = new int[3][];
        jagged[0] = new int[]{1, 2};
        jagged[1] = new int[]{3, 4, 5};
        jagged[2] = new int[]{6};

        System.out.println("\nJagged array:");
        for (int i = 0; i < jagged.length; i++) {
            System.out.println("  Row " + i + " (length " + jagged[i].length + "): " + java.util.Arrays.toString(jagged[i]));
        }

        // 3D array
        int[][][] cube = new int[2][3][4];
        System.out.println("\n3D array dimensions: " + cube.length + " x " + cube[0].length + " x " + cube[0][0].length);
        System.out.println();
    }

    // --- Arrays Utility Class ---

    /**
     * Demonstrates java.util.Arrays utility methods.
     */
    public static void demoArraysUtilityClass() {
        System.out.println("--- Arrays Utility Class ---");

        int[] arr = {5, 2, 8, 1, 9, 3};
        System.out.println("Original: " + java.util.Arrays.toString(arr));

        // sort
        java.util.Arrays.sort(arr);
        System.out.println("Sorted:   " + java.util.Arrays.toString(arr));

        // binarySearch (array must be sorted)
        int index = java.util.Arrays.binarySearch(arr, 8);
        System.out.println("binarySearch(arr, 8) = " + index);

        // fill
        int[] filled = new int[5];
        java.util.Arrays.fill(filled, 42);
        System.out.println("fill(arr, 42): " + java.util.Arrays.toString(filled));

        // copyOf
        int[] copied = java.util.Arrays.copyOf(arr, arr.length);
        System.out.println("copyOf: " + java.util.Arrays.toString(copied));

        // copyOfRange
        int[] range = java.util.Arrays.copyOfRange(arr, 1, 4);
        System.out.println("copyOfRange(1,4): " + java.util.Arrays.toString(range));

        // equals
        int[] arr2 = {1, 2, 3, 5, 8, 9};
        System.out.println("equals(arr, arr2): " + java.util.Arrays.equals(arr, arr2));

        // deepEquals for multi-dimensional
        int[][] a = {{1, 2}, {3, 4}};
        int[][] b = {{1, 2}, {3, 4}};
        System.out.println("deepEquals: " + java.util.Arrays.deepEquals(a, b));

        // toString for 2D
        System.out.println("deepToString: " + java.util.Arrays.deepToString(a));

        // hashCode and deepHashCode
        System.out.println("hashCode: " + java.util.Arrays.hashCode(arr));
        System.out.println("deepHashCode: " + java.util.Arrays.deepHashCode(a));

        // asList (returns a fixed-size List backed by the array)
        java.util.List<String> list = java.util.Arrays.asList("A", "B", "C");
        System.out.println("asList: " + list);

        // Stream conversion
        int sum = java.util.Arrays.stream(arr).sum();
        System.out.println("stream().sum(): " + sum);
        System.out.println();
    }

    // --- System.arraycopy ---

    /**
     * Demonstrates System.arraycopy for efficient array copying.
     */
    public static void demoSystemArraycopy() {
        System.out.println("--- System.arraycopy ---");

        int[] src = {1, 2, 3, 4, 5, 6, 7, 8};
        int[] dest = new int[8];

        // Basic copy
        System.arraycopy(src, 0, dest, 0, src.length);
        System.out.println("Source:      " + java.util.Arrays.toString(src));
        System.out.println("Full copy:   " + java.util.Arrays.toString(dest));

        // Partial copy
        int[] partial = new int[4];
        System.arraycopy(src, 2, partial, 0, 4);
        System.out.println("Copy [2..5]: " + java.util.Arrays.toString(partial));

        // Overlapping copy (arraycopy handles this correctly)
        int[] overlap = {1, 2, 3, 4, 5};
        System.out.println("Before shift: " + java.util.Arrays.toString(overlap));
        System.arraycopy(overlap, 0, overlap, 1, 4); // Shift right by 1
        overlap[0] = 0;
        System.out.println("After shift:  " + java.util.Arrays.toString(overlap));

        // Performance comparison concept
        long start = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            int[] temp = new int[1000];
            System.arraycopy(src, 0, temp, 0, Math.min(src.length, 1000));
        }
        long elapsed = System.nanoTime() - start;
        System.out.println("100k arraycopy operations: " + (elapsed / 1_000_000) + "ms");
        System.out.println();
    }

    // --- Iteration ---

    /**
     * Demonstrates different ways to iterate over arrays.
     */
    public static void demoIteration() {
        System.out.println("--- Array Iteration ---");

        String[] languages = {"Java", "Python", "JavaScript", "Go", "Rust"};

        // Traditional for loop
        System.out.print("Traditional for: ");
        for (int i = 0; i < languages.length; i++) {
            System.out.print(languages[i] + " ");
        }
        System.out.println();

        // Enhanced for-each loop
        System.out.print("Enhanced for:    ");
        for (String lang : languages) {
            System.out.print(lang + " ");
        }
        System.out.println();

        // While loop
        System.out.print("While loop:      ");
        int i = 0;
        while (i < languages.length) {
            System.out.print(languages[i] + " ");
            i++;
        }
        System.out.println();

        // Reverse iteration
        System.out.print("Reverse for:     ");
        for (int j = languages.length - 1; j >= 0; j--) {
            System.out.print(languages[j] + " ");
        }
        System.out.println();

        // Using forEach with lambda
        System.out.print("forEach lambda:  ");
        java.util.Arrays.asList(languages).forEach(lang -> System.out.print(lang + " "));
        System.out.println();

        // 2D array iteration
        int[][] matrix = {{1, 2, 3}, {4, 5, 6}};
        System.out.println("\n2D iteration:");
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                System.out.printf("matrix[%d][%d] = %d  ", row, col, matrix[row][col]);
            }
            System.out.println();
        }
        System.out.println();
    }

    // --- Search Algorithms ---

    /**
     * Demonstrates linear and binary search.
     */
    public static void demoSearchAlgorithms() {
        System.out.println("--- Search Algorithms ---");

        int[] arr = {2, 5, 8, 12, 16, 23, 38, 56, 72, 91};
        int target = 23;

        System.out.println("Array: " + java.util.Arrays.toString(arr));
        System.out.println("Target: " + target);

        // Linear search
        int linearResult = linearSearch(arr, target);
        System.out.println("Linear search: index = " + linearResult);

        // Binary search (array must be sorted)
        int binaryResult = binarySearch(arr, target);
        System.out.println("Binary search: index = " + binaryResult);

        // Using Arrays.binarySearch
        int arraysResult = java.util.Arrays.binarySearch(arr, target);
        System.out.println("Arrays.binarySearch: index = " + arraysResult);

        // Search for non-existent element
        int missing = 50;
        int missingLinear = linearSearch(arr, missing);
        int missingBinary = binarySearch(arr, missing);
        System.out.println("\nSearching for " + missing + ":");
        System.out.println("Linear: " + missingLinear + ", Binary: " + missingBinary);

        // Find all occurrences
        int[] duplicates = {1, 3, 5, 3, 7, 3, 9};
        System.out.println("\nArray with duplicates: " + java.util.Arrays.toString(duplicates));
        System.out.print("All indices of 3: ");
        findAll(duplicates, 3);
        System.out.println();
    }

    /**
     * Linear search - O(n) time complexity.
     */
    public static int linearSearch(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) return i;
        }
        return -1;
    }

    /**
     * Binary search - O(log n) time complexity.
     */
    public static int binarySearch(int[] arr, int target) {
        int low = 0, high = arr.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (arr[mid] == target) return mid;
            else if (arr[mid] < target) low = mid + 1;
            else high = mid - 1;
        }
        return -1;
    }

    /**
     * Find all indices of a target value.
     */
    public static void findAll(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) System.out.print(i + " ");
        }
        System.out.println();
    }

    // --- Sort Algorithms ---

    /**
     * Demonstrates different sorting approaches.
     */
    public static void demoSortAlgorithms() {
        System.out.println("--- Sort Algorithms ---");

        // Bubble sort
        int[] arr1 = {64, 34, 25, 12, 22, 11, 90};
        System.out.println("Original: " + java.util.Arrays.toString(arr1));
        bubbleSort(arr1.clone());
        System.out.println("Bubble sort: " + java.util.Arrays.toString(bubbleSort(arr1.clone())));

        // Selection sort
        System.out.println("Selection sort: " + java.util.Arrays.toString(selectionSort(arr1.clone())));

        // Insertion sort
        System.out.println("Insertion sort: " + java.util.Arrays.toString(insertionSort(arr1.clone())));

        // Arrays.sort (Dual-Pivot Quicksort for primitives)
        int[] arr2 = arr1.clone();
        java.util.Arrays.sort(arr2);
        System.out.println("Arrays.sort:    " + java.util.Arrays.toString(arr2));

        // Sorting strings
        String[] words = {"banana", "apple", "cherry", "date"};
        java.util.Arrays.sort(words);
        System.out.println("Sorted strings: " + java.util.Arrays.toString(words));

        // Custom sort with comparator
        Integer[] nums = {5, 2, 8, 1, 9};
        java.util.Arrays.sort(nums, Collections.reverseOrder());
        System.out.println("Reverse sort:   " + java.util.Arrays.toString(nums));
        System.out.println();
    }

    /**
     * Bubble sort - O(n^2) time, O(1) space.
     */
    public static int[] bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
        return arr;
    }

    /**
     * Selection sort - O(n^2) time, O(1) space.
     */
    public static int[] selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }
            int temp = arr[minIdx];
            arr[minIdx] = arr[i];
            arr[i] = temp;
        }
        return arr;
    }

    /**
     * Insertion sort - O(n^2) time, O(1) space.
     */
    public static int[] insertionSort(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
        return arr;
    }

    // --- Reverse Array ---

    /**
     * Demonstrates different approaches to reverse an array.
     */
    public static void demoReverseArray() {
        System.out.println("--- Reverse Array ---");

        int[] arr = {1, 2, 3, 4, 5};
        System.out.println("Original: " + java.util.Arrays.toString(arr));

        // Two-pointer approach
        int[] reversed = arr.clone();
        int left = 0, right = reversed.length - 1;
        while (left < right) {
            int temp = reversed[left];
            reversed[left] = reversed[right];
            reversed[right] = temp;
            left++;
            right--;
        }
        System.out.println("Reversed: " + java.util.Arrays.toString(reversed));

        // Using Collections.reverse
        Integer[] boxed = {1, 2, 3, 4, 5};
        Collections.reverse(java.util.Arrays.asList(boxed));
        System.out.println("Collections.reverse: " + java.util.Arrays.toString(boxed));

        // Reverse a string using char array
        String original = "Hello, World!";
        String reversedStr = new StringBuilder(original).reverse().toString();
        System.out.println("String reverse: \"" + original + "\" -> \"" + reversedStr + "\"");
        System.out.println();
    }
}
