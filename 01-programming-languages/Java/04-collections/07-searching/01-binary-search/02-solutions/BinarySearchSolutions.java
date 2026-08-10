package academy.javaengineering.collections.searching.binarysearch.solutions;

import java.util.*;

public class BinarySearchSolutions {
    public static int binarySearch(int[] arr, int target) {
        int low = 0, high = arr.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (arr[mid] == target) return mid;
            else if (arr[mid] < target) low = mid + 1;
            else high = mid - 1;
        }
        return -1;
    }
    public static int binarySearchRecursive(int[] arr, int target, int low, int high) {
        if (low > high) return -1;
        int mid = low + (high - low) / 2;
        if (arr[mid] == target) return mid;
        else if (arr[mid] < target) return binarySearchRecursive(arr, target, mid + 1, high);
        else return binarySearchRecursive(arr, target, low, mid - 1);
    }
    public static int findFirstOccurrence(int[] arr, int target) {
        int low = 0, high = arr.length - 1, result = -1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (arr[mid] == target) {
                result = mid;
                high = mid - 1; // Continue searching left
            } else if (arr[mid] < target) low = mid + 1;
            else high = mid - 1;
        }
        return result;
    }
    public static int findLastOccurrence(int[] arr, int target) {
        int low = 0, high = arr.length - 1, result = -1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (arr[mid] == target) {
                result = mid;
                low = mid + 1; // Continue searching right
            } else if (arr[mid] < target) low = mid + 1;
            else high = mid - 1;
        }
        return result;
    }
    public static int findInsertionPoint(int[] arr, int target) {
        int low = 0, high = arr.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (arr[mid] < target) low = mid + 1;
            else high = mid - 1;
        }
        return low;
    }
    public static void main(String[] args) {
        int[] arr = {1, 2, 2, 2, 3, 4, 5, 5, 6};
        System.out.println("Found 2 at: " + binarySearch(arr, 2));
        System.out.println("First occurrence of 2: " + findFirstOccurrence(arr, 2));
        System.out.println("Last occurrence of 2: " + findLastOccurrence(arr, 2));
        System.out.println("Insertion point for 3: " + findInsertionPoint(arr, 3));
        System.out.println("Recursive search for 5: " + binarySearchRecursive(arr, 5, 0, arr.length - 1));
    }
}