package academy.javaengineering.collections.searching.binarysearch;

import java.util.*;

public class BinarySearchExample {
    public static int binarySearch(int[] arr, int target) {
        int low = 0, high = arr.length - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (arr[mid] == target) return mid;
            else if (arr[mid] < target) low = mid + 1;
            else high = mid - 1;
        }
        return -1;
    }
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        System.out.println("Found 7 at: " + binarySearch(arr, 7));
        System.out.println("Found 11 at: " + binarySearch(arr, 11));
    }
}
