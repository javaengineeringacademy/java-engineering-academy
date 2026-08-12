package academy.javaengineering.collections.searching.memory;

import java.util.*;

public class SearchingMemory {

    public static void main(String[] args) {
        System.out.println("=== Searching Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Linear vs Binary search
        System.out.println("--- Linear vs Binary Search ---");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100000; i++) list.add(i);

        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        list.indexOf(99999);
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Linear search 100K: " + (after - before) + " bytes");

        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        Collections.binarySearch(list, 99999);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Binary search 100K: " + (after - before) + " bytes");

        // 2. HashSet vs List lookup
        System.out.println("\n--- Set vs List Lookup ---");
        List<Integer> arrayList = new ArrayList<>();
        Set<Integer> hashSet = new HashSet<>();
        for (int i = 0; i < 100000; i++) {
            arrayList.add(i);
            hashSet.add(i);
        }

        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        arrayList.contains(99999);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("ArrayList.contains(): " + (after - before) + " bytes");

        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        hashSet.contains(99999);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("HashSet.contains(): " + (after - before) + " bytes");

        // 3. Tree vs HashMap lookup
        System.out.println("\n--- TreeMap vs HashMap ---");
        TreeMap<Integer, Integer> tree = new TreeMap<>();
        HashMap<Integer, Integer> hash2 = new HashMap<>();
        for (int i = 0; i < 100000; i++) {
            tree.put(i, i);
            hash2.put(i, i);
        }

        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        tree.containsKey(99999);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("TreeMap.containsKey(): " + (after - before) + " bytes");

        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        hash2.containsKey(99999);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("HashMap.containsKey(): " + (after - before) + " bytes");

        System.out.println("\nHashMap: O(1) lookup, TreeMap: O(log n)");
    }
}
