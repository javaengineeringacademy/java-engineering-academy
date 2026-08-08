package academy.javaengineering.collections.searching.solutions;

import java.util.*;

public class SearchingSolutions {
    public static <T> int linearSearch(List<T> list, T target) {
        for (int i = 0; i < list.size(); i++) if (list.get(i).equals(target)) return i;
        return -1;
    }
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
        System.out.println(linearSearch(Arrays.asList("a","b","c"), "b"));
        System.out.println(binarySearch(new int[]{1,2,3,4,5}, 3));
    }
}
