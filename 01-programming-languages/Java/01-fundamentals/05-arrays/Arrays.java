package academy.javaengineering.fundamentals;

import java.util.Arrays;

/**
 * Arrays in Java
 *
 * This file covers:
 * - Array declaration and initialization
 * - Accessing elements
 * - Array length
 * - Iterating arrays
 * - Multi-dimensional arrays
 * - Array copying
 * - Array utility methods
 */
public class Arrays {

    public static void main(String[] args) {

        // =========================================================
        // 1. ARRAY DECLARATION AND INITIALIZATION
        // =========================================================
        System.out.println("=== Array Declaration and Initialization ===");

        // Method 1: Declare then allocate
        int[] numbers1;
        numbers1 = new int[5]; // Creates array of 5 integers, all default to 0

        // Method 2: Declare and allocate in one line
        int[] numbers2 = new int[5];

        // Method 3: Declare and initialize with values
        int[] numbers3 = {10, 20, 30, 40, 50};

        // Method 4: Explicit new syntax
        int[] numbers4 = new int[]{10, 20, 30, 40, 50};

        // Method 5: Array of strings
        String[] names = {"Alice", "Bob", "Charlie", "Diana"};

        // Method 6: Empty array
        int[] empty = new int[0];

        System.out.println("numbers3: " + java.util.Arrays.toString(numbers3));
        System.out.println("names:    " + java.util.Arrays.toString(names));
        System.out.println("Empty array length: " + empty.length);

        // =========================================================
        // 2. ACCESSING ELEMENTS
        // =========================================================
        System.out.println("\n=== Accessing Elements ===");

        // Arrays are zero-indexed
        int[] scores = {85, 92, 78, 95, 88};

        System.out.println("First element:  scores[0] = " + scores[0]);  // 85
        System.out.println("Second element: scores[1] = " + scores[1]);  // 92
        System.out.println("Last element:   scores[4] = " + scores[4]);  // 88

        // Modifying elements
        scores[2] = 80; // Change third element from 78 to 80
        System.out.println("After modification scores[2] = " + scores[2]); // 80

        // Accessing out of bounds throws ArrayIndexOutOfBoundsException
        try {
            int outOfBounds = scores[10];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ArrayIndexOutOfBoundsException caught: " + e.getMessage());
        }

        // =========================================================
        // 3. ARRAY LENGTH
        // =========================================================
        System.out.println("\n=== Array Length ===");

        int[] data = {1, 2, 3, 4, 5, 6, 7, 8};
        System.out.println("Array length: " + data.length); // 8

        // Note: .length is a property, not a method (no parentheses)
        // Arrays are fixed-size - cannot resize after creation
        // To "resize", create a new array and copy elements

        // =========================================================
        // 4. ITERATING ARRAYS
        // =========================================================
        System.out.println("\n=== Iterating Arrays ===");

        String[] colors = {"Red", "Green", "Blue", "Yellow", "Purple"};

        // Method 1: Traditional for loop
        System.out.print("Traditional for: ");
        for (int i = 0; i < colors.length; i++) {
            System.out.print(colors[i]);
            if (i < colors.length - 1) System.out.print(", ");
        }
        System.out.println();

        // Method 2: Enhanced for loop (for-each)
        System.out.print("Enhanced for:    ");
        for (String color : colors) {
            System.out.print(color + " ");
        }
        System.out.println();

        // Method 3: While loop
        System.out.print("While loop:      ");
        int index = 0;
        while (index < colors.length) {
            System.out.print(colors[index] + " ");
            index++;
        }
        System.out.println();

        // Method 4: Reverse iteration
        System.out.print("Reverse:         ");
        for (int i = colors.length - 1; i >= 0; i--) {
            System.out.print(colors[i] + " ");
        }
        System.out.println();

        // =========================================================
        // 5. COMMON ARRAY OPERATIONS
        // =========================================================
        System.out.println("\n=== Common Array Operations ===");

        int[] values = {5, 2, 8, 1, 9, 3, 7, 4, 6};

        // Find minimum and maximum
        int min = values[0];
        int max = values[0];
        for (int val : values) {
            if (val < min) min = val;
            if (val > max) max = val;
        }
        System.out.println("Array: " + java.util.Arrays.toString(values));
        System.out.println("Min: " + min + ", Max: " + max);

        // Calculate sum and average
        int sum = 0;
        for (int val : values) {
            sum += val;
        }
        double avg = (double) sum / values.length;
        System.out.println("Sum: " + sum + ", Average: " + String.format("%.2f", avg));

        // Reverse array in-place
        int[] reversable = {1, 2, 3, 4, 5};
        System.out.println("Before reverse: " + java.util.Arrays.toString(reversable));
        for (int i = 0; i < reversable.length / 2; i++) {
            int temp = reversable[i];
            reversable[i] = reversable[reversable.length - 1 - i];
            reversable[reversable.length - 1 - i] = temp;
        }
        System.out.println("After reverse:  " + java.util.Arrays.toString(reversable));

        // =========================================================
        // 6. MULTI-DIMENSIONAL ARRAYS
        // =========================================================
        System.out.println("\n=== Multi-Dimensional Arrays ===");

        // 2D Array - declaration and initialization
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };

        // Accessing 2D array elements
        System.out.println("matrix[0][0] = " + matrix[0][0]); // 1
        System.out.println("matrix[1][2] = " + matrix[1][2]); // 6
        System.out.println("matrix[2][1] = " + matrix[2][1]); // 8

        // Iterating 2D array
        System.out.println("\n2D Array:");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("%4d", matrix[i][j]);
            }
            System.out.println();
        }

        // Jagged arrays (rows of different lengths)
        int[][] jagged = new int[3][];
        jagged[0] = new int[]{1, 2};
        jagged[1] = new int[]{3, 4, 5};
        jagged[2] = new int[]{6, 7, 8, 9};

        System.out.println("\nJagged Array:");
        for (int i = 0; i < jagged.length; i++) {
            for (int j = 0; j < jagged[i].length; j++) {
                System.out.printf("%4d", jagged[i][j]);
            }
            System.out.println();
        }

        // 3D Array
        int[][][] threeD = new int[2][3][4];
        // Fill with values
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 4; k++) {
                    threeD[i][j][k] = i * 12 + j * 4 + k;
                }
            }
        }
        System.out.println("\n3D Array [0][1][2] = " + threeD[0][1][2]); // 6

        // =========================================================
        // 7. ARRAY COPYING
        // =========================================================
        System.out.println("\n=== Array Copying ===");

        int[] original = {1, 2, 3, 4, 5};

        // Method 1: Arrays.copyOf()
        int[] copy1 = java.util.Arrays.copyOf(original, original.length);
        System.out.println("copyOf:     " + java.util.Arrays.toString(copy1));

        // Method 2: Arrays.copyOfRange()
        int[] copy2 = java.util.Arrays.copyOfRange(original, 1, 4);
        System.out.println("copyOfRange: " + java.util.Arrays.toString(copy2));

        // Method 3: System.arraycopy()
        int[] copy3 = new int[5];
        System.arraycopy(original, 0, copy3, 0, original.length);
        System.out.println("arraycopy:  " + java.util.Arrays.toString(copy3));

        // Method 4: Clone
        int[] copy4 = original.clone();
        System.out.println("clone:      " + java.util.Arrays.toString(copy4));

        // Shallow vs deep copy
        System.out.println("\nShallow vs Deep Copy:");
        int[][] shallowOriginal = {{1, 2}, {3, 4}};
        int[][] shallowCopy = shallowOriginal.clone();
        shallowCopy[0][0] = 99;
        System.out.println("Shallow copy - original[0][0] = " + shallowOriginal[0][0]); // 99!

        int[][] deepOriginal = {{1, 2}, {3, 4}};
        int[][] deepCopy = new int[deepOriginal.length][];
        for (int i = 0; i < deepOriginal.length; i++) {
            deepCopy[i] = deepOriginal[i].clone();
        }
        deepCopy[0][0] = 99;
        System.out.println("Deep copy - original[0][0]    = " + deepOriginal[0][0]); // 1

        // =========================================================
        // 8. ARRAY UTILITY METHODS
        // =========================================================
        System.out.println("\n=== Array Utility Methods (java.util.Arrays) ===");

        int[] utilArray = {5, 2, 8, 1, 9, 3};
        System.out.println("Original: " + java.util.Arrays.toString(utilArray));

        // Sort
        java.util.Arrays.sort(utilArray);
        System.out.println("Sorted:   " + java.util.Arrays.toString(utilArray));

        // Binary search (array must be sorted)
        int searchIndex = java.util.Arrays.binarySearch(utilArray, 8);
        System.out.println("Binary search for 8: index " + searchIndex);

        // Fill
        int[] filled = new int[5];
        java.util.Arrays.fill(filled, 7);
        System.out.println("Filled with 7: " + java.util.Arrays.toString(filled));

        // Equals
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        System.out.println("Arrays.equals(arr1, arr2): " + java.util.Arrays.equals(arr1, arr2));

        // toString
        System.out.println("toString: " + java.util.Arrays.toString(utilArray));

        // hashCode
        System.out.println("hashCode: " + java.util.Arrays.hashCode(utilArray));

        // =========================================================
        // 9. COMMON PATTERNS
        // =========================================================
        System.out.println("\n=== Common Array Patterns ===");

        // Find duplicate elements
        int[] withDuplicates = {1, 2, 3, 2, 4, 3, 5};
        System.out.println("Array: " + java.util.Arrays.toString(withDuplicates));
        System.out.print("Duplicates: ");
        for (int i = 0; i < withDuplicates.length; i++) {
            for (int j = i + 1; j < withDuplicates.length; j++) {
                if (withDuplicates[i] == withDuplicates[j]) {
                    System.out.print(withDuplicates[i] + " ");
                    break;
                }
            }
        }
        System.out.println();

        // Flatten 2D array
        int[][] nested = {{1, 2, 3}, {4, 5}, {6, 7, 8, 9}};
        int totalElements = 0;
        for (int[] row : nested) {
            totalElements += row.length;
        }
        int[] flattened = new int[totalElements];
        int pos = 0;
        for (int[] row : nested) {
            for (int val : row) {
                flattened[pos++] = val;
            }
        }
        System.out.println("Flattened: " + java.util.Arrays.toString(flattened));

        System.out.println("\n=== Arrays Demo Complete ===");
    }
}
