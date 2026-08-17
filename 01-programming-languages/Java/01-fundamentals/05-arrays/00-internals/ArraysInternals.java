package academy.javaengineering.fundamentals.arrays;

import java.util.Arrays;

/**
 * Demonstrates array internals in Java.
 */
public class ArraysInternals {

    public static void main(String[] args) {
        System.out.println("=== Arrays Internals Demo ===\n");

        // 1. Array object layout
        System.out.println("--- Array Object Layout ---");
        int[] arr = {1, 2, 3, 4, 5};
        System.out.println("Array: " + Arrays.toString(arr));
        System.out.println("Length: " + arr.length);
        System.out.println("Class: " + arr.getClass().getName());

        // 2. Array bounds checking
        System.out.println("\n--- Bounds Checking ---");
        try {
            int value = arr[10];
            System.out.println("Value: " + value);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        // 3. Multi-dimensional arrays
        System.out.println("\n--- Multi-Dimensional Arrays ---");
        int[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        System.out.println("Matrix class: " + matrix.getClass().getName());
        System.out.println("matrix[0] class: " + matrix[0].getClass().getName());

        // 4. Array copy performance
        System.out.println("\n--- Array Copy ---");
        int[] original = {1, 2, 3, 4, 5};
        int[] copy1 = Arrays.copyOf(original, original.length);
        int[] copy2 = new int[original.length];
        System.arraycopy(original, 0, copy2, 0, original.length);
        System.out.println("Original: " + Arrays.toString(original));
        System.out.println("copyOf:   " + Arrays.toString(copy1));
        System.out.println("arraycopy: " + Arrays.toString(copy2));

        // 5. Array sort
        System.out.println("\n--- Array Sort ---");
        int[] unsorted = {5, 3, 8, 1, 2};
        System.out.println("Before sort: " + Arrays.toString(unsorted));
        Arrays.sort(unsorted);
        System.out.println("After sort:  " + Arrays.toString(unsorted));

        System.out.println("\n=== Internals Demo Complete ===");
    }
}
