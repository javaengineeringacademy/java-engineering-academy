# Arrays - Examples

```java
import java.util.Arrays;

public class ArrayExamples {
    public static void main(String[] args) {
        // Declaration and initialization
        int[] nums = {10, 20, 30, 40, 50};
        String[] names = new String[]{"Alice", "Bob", "Charlie"};

        // Access and modify
        System.out.println("First: " + nums[0]);
        nums[2] = 99;
        System.out.println("After modify: " + Arrays.toString(nums));

        // Length
        System.out.println("Length: " + nums.length);

        // Iteration
        System.out.print("Values: ");
        for (int n : nums) System.out.print(n + " ");
        System.out.println();

        // Sorting
        int[] unsorted = {5, 2, 8, 1, 9};
        Arrays.sort(unsorted);
        System.out.println("Sorted: " + Arrays.toString(unsorted));

        // Searching (requires sorted array)
        int idx = Arrays.binarySearch(unsorted, 8);
        System.out.println("Index of 8: " + idx);

        // Copying
        int[] copy = Arrays.copyOf(nums, nums.length);
        int[] partial = Arrays.copyOfRange(nums, 1, 4);
        System.out.println("Copy: " + Arrays.toString(copy));
        System.out.println("Range: " + Arrays.toString(partial));

        // 2D array
        int[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }

        // Fill
        int[] filled = new int[5];
        Arrays.fill(filled, 7);
        System.out.println("Filled: " + Arrays.toString(filled));
    }
}
```
