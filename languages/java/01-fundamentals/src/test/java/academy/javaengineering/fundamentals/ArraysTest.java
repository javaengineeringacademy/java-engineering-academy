package academy.javaengineering.fundamentals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ArraysDemo}.
 */
class ArraysTest {

    @Test
    @DisplayName("Array declaration creates correct default values")
    void testDefaultValues() {
        int[] ints = new int[3];
        double[] doubles = new double[3];
        boolean[] bools = new boolean[3];
        String[] strings = new String[3];

        assertEquals(0, ints[0]);
        assertEquals(0.0, doubles[0]);
        assertFalse(bools[0]);
        assertNull(strings[0]);
    }

    @Test
    @DisplayName("Array literal initialization works correctly")
    void testLiteralInit() {
        int[] arr = {5, 3, 8, 1, 9};
        assertEquals(5, arr.length);
        assertEquals(5, arr[0]);
        assertEquals(9, arr[4]);
    }

    @Test
    @DisplayName("Array length property returns correct size")
    void testArrayLength() {
        int[] empty = new int[0];
        int[] five = new int[5];
        assertEquals(0, empty.length);
        assertEquals(5, five.length);
    }

    @Test
    @DisplayName("Array bounds checking throws exception")
    void testBoundsChecking() {
        int[] arr = {1, 2, 3};
        assertArrayEquals(new int[]{1, 2, 3}, arr);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            int val = arr[5];
        });
    }

    @Test
    @DisplayName("2D array has correct dimensions")
    void test2DArray() {
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6}
        };
        assertEquals(2, matrix.length);
        assertEquals(3, matrix[0].length);
        assertEquals(5, matrix[1][1]);
    }

    @Test
    @DisplayName("Jagged array rows have different lengths")
    void testJaggedArray() {
        int[][] jagged = new int[3][];
        jagged[0] = new int[]{1, 2};
        jagged[1] = new int[]{3, 4, 5};
        jagged[2] = new int[]{6};

        assertEquals(2, jagged[0].length);
        assertEquals(3, jagged[1].length);
        assertEquals(1, jagged[2].length);
    }

    @Test
    @DisplayName("Arrays.sort sorts in ascending order")
    void testSort() {
        int[] arr = {5, 2, 8, 1, 9, 3};
        Arrays.sort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 5, 8, 9}, arr);
    }

    @Test
    @DisplayName("Arrays.sort with comparator sorts in custom order")
    void testSortWithComparator() {
        Integer[] arr = {5, 2, 8, 1, 9};
        Arrays.sort(arr, Collections.reverseOrder());
        assertArrayEquals(new Integer[]{9, 8, 5, 2, 1}, arr);
    }

    @Test
    @DisplayName("Arrays.binarySearch finds correct index")
    void testBinarySearch() {
        int[] arr = {1, 2, 3, 5, 8, 9};
        assertEquals(3, Arrays.binarySearch(arr, 5));
        assertEquals(0, Arrays.binarySearch(arr, 1));
        assertEquals(5, Arrays.binarySearch(arr, 9));
        assertTrue(Arrays.binarySearch(arr, 4) < 0); // Not found
    }

    @Test
    @DisplayName("Arrays.fill fills array with specified value")
    void testFill() {
        int[] arr = new int[5];
        Arrays.fill(arr, 42);
        for (int val : arr) {
            assertEquals(42, val);
        }
    }

    @Test
    @DisplayName("Arrays.copyOf creates correct copy")
    void testCopyOf() {
        int[] original = {1, 2, 3, 4, 5};
        int[] copy = Arrays.copyOf(original, original.length);
        assertArrayEquals(original, copy);
        assertNotSame(original, copy);
    }

    @Test
    @DisplayName("Arrays.copyOfRange extracts correct range")
    void testCopyOfRange() {
        int[] arr = {1, 2, 3, 4, 5};
        int[] range = Arrays.copyOfRange(arr, 1, 4);
        assertArrayEquals(new int[]{2, 3, 4}, range);
    }

    @Test
    @DisplayName("Arrays.equals compares arrays correctly")
    void testArraysEquals() {
        int[] a = {1, 2, 3};
        int[] b = {1, 2, 3};
        int[] c = {1, 2, 4};
        assertTrue(Arrays.equals(a, b));
        assertFalse(Arrays.equals(a, c));
    }

    @Test
    @DisplayName("System.arraycopy copies elements correctly")
    void testSystemArraycopy() {
        int[] src = {1, 2, 3, 4, 5};
        int[] dest = new int[5];
        System.arraycopy(src, 0, dest, 0, src.length);
        assertArrayEquals(src, dest);
    }

    @Test
    @DisplayName("System.arraycopy with partial copy works correctly")
    void testPartialCopy() {
        int[] src = {1, 2, 3, 4, 5};
        int[] dest = new int[3];
        System.arraycopy(src, 1, dest, 0, 3);
        assertArrayEquals(new int[]{2, 3, 4}, dest);
    }

    @Test
    @DisplayName("Linear search finds correct index")
    void testLinearSearch() {
        int[] arr = {10, 20, 30, 40, 50};
        assertEquals(2, ArraysDemo.linearSearch(arr, 30));
        assertEquals(-1, ArraysDemo.linearSearch(arr, 25));
    }

    @Test
    @DisplayName("Binary search finds correct index in sorted array")
    void testCustomBinarySearch() {
        int[] arr = {1, 2, 3, 5, 8, 9};
        assertEquals(3, academy.javaengineering.fundamentals.ArraysDemo.binarySearch(arr, 5));
        assertEquals(-1, academy.javaengineering.fundamentals.ArraysDemo.binarySearch(arr, 4));
    }

    @Test
    @DisplayName("Bubble sort produces correct result")
    void testBubbleSort() {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        int[] sorted = academy.javaengineering.fundamentals.ArraysDemo.bubbleSort(arr.clone());
        assertArrayEquals(new int[]{11, 12, 22, 25, 34, 64, 90}, sorted);
    }

    @Test
    @DisplayName("Selection sort produces correct result")
    void testSelectionSort() {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        int[] sorted = academy.javaengineering.fundamentals.ArraysDemo.selectionSort(arr.clone());
        assertArrayEquals(new int[]{11, 12, 22, 25, 34, 64, 90}, sorted);
    }

    @Test
    @DisplayName("Insertion sort produces correct result")
    void testInsertionSort() {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        int[] sorted = academy.javaengineering.fundamentals.ArraysDemo.insertionSort(arr.clone());
        assertArrayEquals(new int[]{11, 12, 22, 25, 34, 64, 90}, sorted);
    }
}
