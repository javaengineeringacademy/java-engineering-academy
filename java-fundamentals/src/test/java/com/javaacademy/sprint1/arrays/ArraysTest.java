package com.javaacademy.sprint1.arrays;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

class ArrayBasicsTest {

    @Test
    void testDeclarationAndCreation() {
        int[] numbers = new int[5];
        assertEquals(5, numbers.length);
        assertArrayEquals(new int[]{0,0,0,0,0}, numbers);
    }

    @Test
    void testLiteral() {
        String[] names = {"Alice", "Bob", "Charlie"};
        assertEquals(3, names.length);
        assertEquals("Alice", names[0]);
    }

    @Test
    void testAnonymousArray() {
        assertArrayEquals(new int[]{1,2,3}, createArray());
    }

    @Test
    void testAccessAndModify() {
        int[] scores = {90, 85, 95};
        assertEquals(90, scores[0]);
        assertEquals(95, scores[scores.length - 1]);
        
        scores[0] = 100;
        assertEquals(100, scores[0]);
    }

    @Test
    void testDefaultValues() {
        int[] intArr = new int[3];
        double[] doubleArr = new double[3];
        boolean[] boolArr = new boolean[3];
        String[] strArr = new String[3];

        assertArrayEquals(new int[]{0,0,0}, intArr);
        assertArrayEquals(new double[]{0.0,0.0,0.0}, doubleArr);
        assertArrayEquals(new boolean[]{false,false,false}, boolArr);
        assertArrayEquals(new String[]{null,null,null}, strArr);
    }

    @Test
    void testReferenceVsCopy() {
        int[] original = {1, 2, 3};
        int[] reference = original;
        int[] copy = original.clone();

        reference[0] = 99;
        assertEquals(99, original[0]);

        copy[0] = 88;
        assertEquals(99, original[0]);
        assertEquals(88, copy[0]);
    }

    @Test
    void testIteration() {
        int[] scores = {100, 90, 80};
        int sumFor = 0, sumForEach = 0;
        
        for (int i = 0; i < scores.length; i++) sumFor += scores[i];
        for (int score : scores) sumForEach += score;
        
        assertEquals(sumFor, sumForEach);
    }

    @Test
    void testVarargs() {
        assertEquals(6, sum(1, 2, 3));
        assertEquals(10, sum(new int[]{1, 2, 3, 4}));
        assertEquals(0, sum());
    }

    int[] createArray() { return new int[]{1, 2, 3}; }
    int sum(int... numbers) { int s = 0; for (int n : numbers) s += n; return s; }
}

class MultiDimensionalArraysTest {

    @Test
    void test2DArray() {
        int[][] matrix = new int[3][4];
        assertEquals(3, matrix.length);
        assertEquals(4, matrix[0].length);
    }

    @Test
    void test2DArrayLiteral() {
        int[][] grid = {{1,2,3}, {4,5,6}, {7,8,9}};
        assertEquals(5, grid[1][1]);
    }

    @Test
    void testJaggedArray() {
        int[][] jagged = {{1,2}, {3,4,5,6}, {7}};
        assertEquals(2, jagged[0].length);
        assertEquals(4, jagged[1].length);
        assertEquals(1, jagged[2].length);
    }

    @Test
    void test3DArray() {
        int[][][] cube = new int[2][3][4];
        assertEquals(2, cube.length);
        assertEquals(3, cube[0].length);
        assertEquals(4, cube[0][0].length);
    }

    @Test
    void testMatrixAddition() {
        int[][] a = {{1,2}, {3,4}};
        int[][] b = {{5,6}, {7,8}};
        int[][] sum = addMatrices(a, b);
        assertArrayEquals(new int[]{6,8}, sum[0]);
        assertArrayEquals(new int[]{10,12}, sum[1]);
    }

    @Test
    void testMatrixMultiplication() {
        int[][] a = {{1,2}, {3,4}};
        int[][] b = {{5,6}, {7,8}};
        int[][] product = multiplyMatrices(a, b);
        assertArrayEquals(new int[]{19,22}, product[0]);
        assertArrayEquals(new int[]{43,50}, product[1]);
    }

    @Test
    void testDeepToString() {
        int[][] grid = {{1,2}, {3,4}};
        assertEquals("[[1, 2], [3, 4]]", Arrays.deepToString(grid));
    }

    static int[][] addMatrices(int[][] a, int[][] b) {
        int[][] r = new int[a.length][a[0].length];
        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < a[0].length; j++)
                r[i][j] = a[i][j] + b[i][j];
        return r;
    }

    static int[][] multiplyMatrices(int[][] a, int[][] b) {
        int[][] r = new int[a.length][b[0].length];
        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < b[0].length; j++)
                for (int k = 0; k < a[0].length; k++)
                    r[i][j] += a[i][k] * b[k][j];
        return r;
    }
}

class ArrayAlgorithmsTest {

    @Test
    void testLinearSearch() {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        assertEquals(4, linearSearch(arr, 22));
        assertEquals(-1, linearSearch(arr, 100));
    }

    @Test
    void testBinarySearch() {
        int[] sorted = {11, 12, 22, 25, 34, 64, 90};
        assertEquals(3, binarySearch(sorted, 25));
        assertEquals(-1, binarySearch(sorted, 100));
        assertEquals(3, Arrays.binarySearch(sorted, 25));
    }

    @Test
    void testMinMax() {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        assertEquals(11, findMin(arr));
        assertEquals(90, findMax(arr));
    }

    @Test
    void testBubbleSort() {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        bubbleSort(arr);
        assertArrayEquals(new int[]{11, 12, 22, 25, 34, 64, 90}, arr);
    }

    @Test
    void testSelectionSort() {
        int[] arr = {64, 25, 12, 22, 11};
        selectionSort(arr);
        assertArrayEquals(new int[]{11, 12, 22, 25, 64}, arr);
    }

    @Test
    void testInsertionSort() {
        int[] arr = {64, 25, 12, 22, 11};
        insertionSort(arr);
        assertArrayEquals(new int[]{11, 12, 22, 25, 64}, arr);
    }

    @Test
    void testArraysSort() {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        Arrays.sort(arr);
        assertArrayEquals(new int[]{11, 12, 22, 25, 34, 64, 90}, arr);
    }

    @Test
    void testReverse() {
        int[] arr = {1, 2, 3, 4, 5};
        reverse(arr);
        assertArrayEquals(new int[]{5, 4, 3, 2, 1}, arr);
    }

    @Test
    void testRotateLeft() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7};
        rotateLeft(arr, 3);
        assertArrayEquals(new int[]{4, 5, 6, 7, 1, 2, 3}, arr);
    }

    @Test
    void testRemoveDuplicates() {
        int[] arr = {1, 1, 2, 2, 2, 3, 4, 4, 5};
        int[] unique = removeDuplicates(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, unique);
    }

    static int linearSearch(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) if (arr[i] == target) return i;
        return -1;
    }

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
        for (int i = 1; i < arr.length; i++) if (arr[i] < min) min = arr[i];
        return min;
    }

    static int findMax(int[] arr) {
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) if (arr[i] > max) max = arr[i];
        return max;
    }

    static void bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++)
            for (int j = 0; j < arr.length - 1 - i; j++)
                if (arr[j] > arr[j + 1]) { swap(arr, j, j + 1); }
    }

    static void selectionSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < arr.length; j++) if (arr[j] < arr[minIdx]) minIdx = j;
            if (minIdx != i) swap(arr, i, minIdx);
        }
    }

    static void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i], j = i - 1;
            while (j >= 0 && arr[j] > key) arr[j + 1] = arr[j--];
            arr[j + 1] = key;
        }
    }

    static void reverse(int[] arr) {
        for (int i = 0, j = arr.length - 1; i < j; i++, j--) swap(arr, i, j);
    }

    static void rotateLeft(int[] arr, int k) {
        k %= arr.length; if (k < 0) k += arr.length;
        reverse(arr, 0, k - 1);
        reverse(arr, k, arr.length - 1);
        reverse(arr, 0, arr.length - 1);
    }

    static void reverse(int[] arr, int start, int end) {
        while (start < end) swap(arr, start++, end--);
    }

    static int[] removeDuplicates(int[] arr) {
        if (arr.length <= 1) return arr;
        int uniqueCount = 1;
        for (int i = 1; i < arr.length; i++)
            if (arr[i] != arr[uniqueCount - 1]) arr[uniqueCount++] = arr[i];
        return Arrays.copyOf(arr, uniqueCount);
    }

    static void swap(int[] arr, int i, int j) { int t = arr[i]; arr[i] = arr[j]; arr[j] = t; }
}