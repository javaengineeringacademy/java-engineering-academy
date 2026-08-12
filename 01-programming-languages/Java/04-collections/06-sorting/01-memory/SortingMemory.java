package academy.javaengineering.collections.sorting.memory;

import java.util.*;

public class SortingMemory {

    public static void main(String[] args) {
        System.out.println("=== Sorting Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. In-place vs copy sort
        System.out.println("--- In-place vs Copy Sort ---");
        int[] original = {5, 2, 8, 1, 9, 3, 7, 4, 6};

        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        int[] copy = original.clone();
        Arrays.sort(copy);
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Arrays.sort() in-place: " + (after - before) + " bytes");

        // 2. Collections.sort() uses TimSort
        System.out.println("\n--- TimSort Memory ---");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) list.add(10000 - i);

        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        Collections.sort(list);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("TimSort 10K: " + (after - before) + " bytes");
        System.out.println("Uses temp array of size n/2");

        // 3. Parallel sort overhead
        System.out.println("\n--- Parallel Sort Overhead ---");
        int[] large = new int[100000];
        for (int i = 0; i < large.length; i++) large[i] = large.length - i;

        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        Arrays.parallelSort(large);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("parallelSort 100K: " + (after - before) + " bytes");
        System.out.println("ForkJoinPool overhead for small arrays");

        // 4. Stable sort memory
        System.out.println("\n--- Stable Sort Memory ---");
        System.out.println("TimSort is stable (preserves equal order)");
        System.out.println("Merge sort: O(n) extra space");
        System.out.println("TimSort: O(n) worst case");
    }
}
