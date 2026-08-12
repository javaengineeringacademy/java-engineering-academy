package academy.javaengineering.collections.sorting.internals;

import java.util.*;
import java.util.concurrent.*;

public class SortingInternals {

    public static void main(String[] args) {
        System.out.println("=== Sorting Algorithms Internals ===\n");

        // 1. Comparable natural ordering
        System.out.println("--- Comparable Natural Ordering ---");
        List<String> names = new ArrayList<>(Arrays.asList("Charlie", "Alice", "Bob"));
        Collections.sort(names);
        System.out.println("Sorted: " + names);
        System.out.println("Implements Comparable<T>.compareTo(T o)");

        // 2. Comparator custom ordering
        System.out.println("\n--- Comparator Custom Ordering ---");
        Comparator<String> byLength = Comparator.comparingInt(String::length);
        names.sort(byLength);
        System.out.println("By length: " + names);

        // 3. TimSort algorithm
        System.out.println("\n--- TimSort (Arrays.sort) ---");
        System.out.println("Hybrid: Insertion sort + Merge sort");
        System.out.println("Best case: O(n) for nearly sorted");
        System.out.println("Worst case: O(n log n)");
        System.out.println("Stable sort");

        // 4. Collections.sort() vs Arrays.sort()
        System.out.println("\n--- sort() Methods ---");
        Integer[] arr = {5, 2, 8, 1, 9};
        Arrays.sort(arr);
        System.out.println("Arrays.sort(): " + Arrays.toString(arr));

        List<Integer> list = new ArrayList<>(Arrays.asList(5, 2, 8, 1, 9));
        Collections.sort(list);
        System.out.println("Collections.sort(): " + list);

        // 5. Parallel sort
        System.out.println("\n--- Parallel Sort ---");
        int[] parallel = {9, 1, 6, 3, 7, 2, 8, 4, 5};
        Arrays.parallelSort(parallel);
        System.out.println("parallelSort(): " + Arrays.toString(parallel));
        System.out.println("Uses ForkJoinPool for multi-core");

        // 6. Shuffling
        System.out.println("\n--- Collections.shuffle() ---");
        List<Integer> nums = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        Collections.shuffle(nums);
        System.out.println("Shuffled: " + nums);
    }
}
