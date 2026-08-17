package academy.javaengineering.fundamentals.arrays;

import java.util.Arrays;

/**
 * Demonstrates array memory usage patterns.
 */
public class ArraysMemory {

    public static void main(String[] args) {
        System.out.println("=== Arrays Memory Demo ===\n");

        // 1. Primitive array memory
        System.out.println("--- Primitive Array Memory ---");
        int[] primitiveArr = new int[1000];
        System.out.println("int[1000] array created");
        System.out.println("Memory per element: 4 bytes");
        System.out.println("Total element memory: " + (primitiveArr.length * 4) + " bytes");
        System.out.println("Array class: " + primitiveArr.getClass().getName());

        // 2. Object array memory
        System.out.println("\n--- Object Array Memory ---");
        Integer[] objectArr = new Integer[1000];
        System.out.println("Integer[1000] array created");
        System.out.println("Array reference: 8 bytes per element");
        System.out.println("Integer objects: 16+ bytes each (when populated)");

        // 3. Multi-dimensional array
        System.out.println("\n--- Multi-Dimensional Array Memory ---");
        int[][] matrix = new int[10][10];
        System.out.println("int[10][10] matrix created");
        System.out.println("Outer array: 10 references (80 bytes)");
        System.out.println("Each inner array: 10 ints (40 bytes)");
        System.out.println("Total inner arrays: 10 × 40 = 400 bytes");

        // 4. Array copy memory
        System.out.println("\n--- Array Copy Memory ---");
        int[] original = {1, 2, 3, 4, 5};
        int[] shallowCopy = original;
        int[] deepCopy = Arrays.copyOf(original, original.length);
        System.out.println("Shallow copy: Same reference (" + (original == shallowCopy) + ")");
        System.out.println("Deep copy: Different object (" + (original == deepCopy) + ")");

        System.out.println("\n=== Memory Demo Complete ===");
    }
}
