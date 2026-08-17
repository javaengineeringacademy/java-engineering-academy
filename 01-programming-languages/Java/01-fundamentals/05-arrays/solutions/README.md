# Arrays - Solutions

```java
import java.util.Arrays;

public class ArraySolutions {

    // Find max and min
    static int[] findMinMax(int[] arr) {
        int min = arr[0], max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < min) min = arr[i];
            if (arr[i] > max) max = arr[i];
        }
        return new int[]{min, max};
    }

    // Reverse array
    static void reverse(int[] arr) {
        for (int i = 0, j = arr.length - 1; i < j; i++, j--) {
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
    }

    // Array intersection
    static int[] intersection(int[] a, int[] b) {
        return Arrays.stream(a)
                .filter(x -> Arrays.stream(b).anyMatch(y -> y == x))
                .distinct()
                .toArray();
    }

    // Matrix multiplication
    static int[][] multiply(int[][] a, int[][] b) {
        int[][] result = new int[a.length][b[0].length];
        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < b[0].length; j++)
                for (int k = 0; k < a[0].length; k++)
                    result[i][j] += a[i][k] * b[k][j];
        return result;
    }

    // Remove duplicates from sorted array
    static int removeDuplicates(int[] arr) {
        int writeIdx = 1;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] != arr[i - 1]) {
                arr[writeIdx++] = arr[i];
            }
        }
        return writeIdx;
    }

    public static void main(String[] args) {
        int[] arr = {3, 1, 4, 1, 5, 9, 2, 6};
        int[] mm = findMinMax(arr);
        System.out.printf("min=%d, max=%d%n", mm[0], mm[1]);

        reverse(arr);
        System.out.println("Reversed: " + Arrays.toString(arr));

        int[] sorted = {1, 1, 2, 3, 3, 4};
        int newLen = removeDuplicates(sorted);
        System.out.println("Without dupes: " + Arrays.toString(Arrays.copyOf(sorted, newLen)));
    }
}
```
