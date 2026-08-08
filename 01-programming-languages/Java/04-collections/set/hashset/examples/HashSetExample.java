package set.hashset.examples;

import java.util.*;

public class HashSetExample {

    public static void main(String[] args) {
        example1_BasicHashSet();
        example2_RemoveDuplicates();
        example3_SetOperations();
        example4_HashSetPerformance();
        example5_HashSetVsTreeSet();
    }

    static void example1_BasicHashSet() {
        System.out.println("=== Example 1: Basic HashSet ===");
        HashSet<String> set = new HashSet<>();
        set.add("Java");
        set.add("Python");
        set.add("Java");
        set.add("C++");
        System.out.println("Set: " + set);
        System.out.println("Size: " + set.size());
    }

    static void example2_RemoveDuplicates() {
        System.out.println("\n=== Example 2: Remove Duplicates ===");
        List<Integer> list = Arrays.asList(1, 2, 2, 3, 3, 3, 4, 4, 5);
        Set<Integer> unique = new HashSet<>(list);
        System.out.println("Original: " + list);
        System.out.println("Unique: " + unique);
    }

    static void example3_SetOperations() {
        System.out.println("\n=== Example 3: Set Operations ===");
        HashSet<Integer> set1 = new HashSet<>(Arrays.asList(1, 2, 3, 4));
        HashSet<Integer> set2 = new HashSet<>(Arrays.asList(3, 4, 5, 6));
        HashSet<Integer> union = new HashSet<>(set1);
        union.addAll(set2);
        System.out.println("Union: " + union);
        HashSet<Integer> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        System.out.println("Intersection: " + intersection);
    }

    static void example4_HashSetPerformance() {
        System.out.println("\n=== Example 4: Performance ===");
        HashSet<Integer> set = new HashSet<>();
        long start = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            set.add(i);
        }
        long addTime = System.nanoTime() - start;
        start = System.nanoTime();
        set.contains(50000);
        long containsTime = System.nanoTime() - start;
        System.out.println("Add 100k: " + addTime + " ns");
        System.out.println("Contains: " + containsTime + " ns");
    }

    static void example5_HashSetVsTreeSet() {
        System.out.println("\n=== Example 5: HashSet vs TreeSet ===");
        HashSet<String> hashSet = new HashSet<>(Arrays.asList("Banana", "Apple", "Cherry"));
        TreeSet<String> treeSet = new TreeSet<>(Arrays.asList("Banana", "Apple", "Cherry"));
        System.out.println("HashSet (unordered): " + hashSet);
        System.out.println("TreeSet (sorted): " + treeSet);
    }
}
