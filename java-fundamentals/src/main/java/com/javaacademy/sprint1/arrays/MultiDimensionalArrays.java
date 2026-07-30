package com.javaacademy.sprint1.arrays;

/**
 * MultiDimensionalArrays - Demonstrates 2D and 3D arrays (arrays of arrays).
 *
 * <p><b>Java has no true multi-dimensional arrays - only arrays of arrays.</b>
 * <pre>
 * int[][] matrix = new int[3][4];  // 3 rows, each row is an int[4]
 * // Actually: int[][] = {int[4], int[4], int[4]}
 * // Each row can have DIFFERENT length! (jagged arrays)
 * </pre>
 *
 * <p><b>Real-world analogy:</b>
 * - 2D array = spreadsheet (rows x columns)
 * - 3D array = stack of spreadsheets (pages x rows x columns)
 * - Jagged array = spreadsheet where each row has different column count
 *
 * <p><b>Memory Layout:</b> Row-major order.
 * matrix[i][j] means: get i-th row (array), then j-th element in that row.
 *
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class MultiDimensionalArrays {

    private MultiDimensionalArrays() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("=== Multi-Dimensional Arrays ===\n");

        // 2D Array - Rectangular (matrix)
        System.out.println("--- 2D Rectangular Array ---");
        int[][] matrix = new int[3][4]; // 3 rows, 4 columns
        System.out.println("Rows: " + matrix.length);        // 3
        System.out.println("Cols in row 0: " + matrix[0].length); // 4

        // Initialize with nested loops
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = i * 10 + j; // 00, 01, 02... 10, 11...
            }
        }
        printMatrix(matrix);

        // 2D Array literal
        System.out.println("\n--- 2D Array Literal ---");
        int[][] grid = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        printMatrix(grid);

        // Jagged Array (each row different length)
        System.out.println("\n--- Jagged Array ---");
        int[][] jagged = {
            {1, 2},           // Row 0: 2 elements
            {3, 4, 5, 6},     // Row 1: 4 elements
            {7},              // Row 2: 1 element
            {8, 9, 10, 11, 12} // Row 3: 5 elements
        };
        for (int i = 0; i < jagged.length; i++) {
            System.out.print("Row " + i + " (len=" + jagged[i].length + "): ");
            for (int val : jagged[i]) {
                System.out.print(val + " ");
            }
            System.out.println();
        }

        // 3D Array
        System.out.println("\n--- 3D Array ---");
        int[][][] cube = new int[2][3][4]; // 2 layers, 3 rows, 4 cols
        System.out.println("Layers: " + cube.length);
        System.out.println("Rows in layer 0: " + cube[0].length);
        System.out.println("Cols in layer 0 row 0: " + cube[0][0].length);

        // Initialize 3D
        int value = 1;
        for (int layer = 0; layer < cube.length; layer++) {
            for (int row = 0; row < cube[layer].length; row++) {
                for (int col = 0; col < cube[layer][row].length; col++) {
                    cube[layer][row][col] = value++;
                }
            }
        }
        System.out.println("Total elements: " + (value - 1)); // 24

        // Practical: Matrix operations
        System.out.println("\n--- Matrix Operations ---");
        int[][] a = {{1, 2}, {3, 4}};
        int[][] b = {{5, 6}, {7, 8}};

        // Addition
        int[][] sum = addMatrices(a, b);
        System.out.println("A + B:");
        printMatrix(sum);

        // Multiplication
        int[][] product = multiplyMatrices(a, b);
        System.out.println("A * B:");
        printMatrix(product);

        // Enhanced for with 2D
        System.out.println("\n--- Enhanced For with 2D ---");
        for (int[] row : grid) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }

        // Arrays.deepToString for nested arrays
        System.out.println("\n--- Deep ToString ---");
        System.out.println("grid = " + java.util.Arrays.deepToString(grid));
        System.out.println("jagged = " + java.util.Arrays.deepToString(jagged));
        System.out.println("cube = " + java.util.Arrays.deepToString(cube));

        // Expected output shows all multi-dimensional array operations
    }

    static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.printf("%4d", val);
            }
            System.out.println();
        }
    }

    static int[][] addMatrices(int[][] a, int[][] b) {
        int rows = a.length;
        int cols = a[0].length;
        int[][] result = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = a[i][j] + b[i][j];
            }
        }
        return result;
    }

    static int[][] multiplyMatrices(int[][] a, int[][] b) {
        int rowsA = a.length;
        int colsA = a[0].length;
        int colsB = b[0].length;
        int[][] result = new int[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return result;
    }
}