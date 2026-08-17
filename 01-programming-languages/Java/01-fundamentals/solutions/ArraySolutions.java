package academy.javaengineering.exercises.solutions;

import java.util.Arrays;

/**
 * Solutions: Arrays (Matrix, Rotation, Missing Number)
 */
public class ArraySolutions {

    public int[][] transpose(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] transposed = new int[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposed[j][i] = matrix[i][j];
            }
        }
        return transposed;
    }

    public int[] rotateInPlace(int[] arr, int k) {
        int n = arr.length;
        k = k % n;
        reverse(arr, 0, k - 1);
        reverse(arr, k, n - 1);
        reverse(arr, 0, n - 1);
        return arr;
    }

    private void reverse(int[] arr, int start, int end) {
        while (start < end) {
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++;
            end--;
        }
    }

    public int findMissingNumber(int[] nums) {
        int n = nums.length;
        int expectedSum = n * (n + 1) / 2;
        int actualSum = 0;
        for (int num : nums) actualSum += num;
        return expectedSum - actualSum;
    }

    public static void main(String[] args) {
        ArraySolutions solutions = new ArraySolutions();
        System.out.println("=== Array Solutions ===\n");

        System.out.println("1. Matrix Transpose:");
        int[][] matrix = {{1, 2, 3}, {4, 5, 6}};
        int[][] transposed = solutions.transpose(matrix);
        for (int[] row : transposed) {
            System.out.println("   " + Arrays.toString(row));
        }

        System.out.println("\n2. Rotate In-Place [1,2,3,4,5,6,7] k=3:");
        int[] rotated = solutions.rotateInPlace(new int[]{1, 2, 3, 4, 5, 6, 7}, 3);
        System.out.println("   " + Arrays.toString(rotated));

        System.out.println("\n3. Find Missing Number [3,0,1]: " + solutions.findMissingNumber(new int[]{3, 0, 1}));
        System.out.println("   Find Missing [9,6,4,2,3,5,7,0,1]: " + solutions.findMissingNumber(new int[]{9, 6, 4, 2, 3, 5, 7, 0, 1}));
    }
}
