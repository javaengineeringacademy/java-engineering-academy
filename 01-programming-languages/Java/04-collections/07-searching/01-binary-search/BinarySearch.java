package academy.javaengineering.collections.searching.binarysearch;

import java.util.*;

public class BinarySearchTest {
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
        int[] arr = {10, 20, 30, 40, 50};
        assert binarySearch(arr, 30) == 2 : "Index should be 2";
        System.out.println("BinarySearchTest passed");
    }
}
