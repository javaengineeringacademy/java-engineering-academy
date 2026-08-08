package set.treeset.examples;

import java.util.*;

public class TreeSetExample {

    public static void main(String[] args) {
        example1_NaturalOrdering();
        example2_CustomComparator();
        example3_NavigableSetMethods();
        example4_PartialOrdering();
        example5_TreeSetPerformance();
    }

    static void example1_NaturalOrdering() {
        System.out.println("=== Example 1: Natural Ordering ===");
        TreeSet<Integer> set = new TreeSet<>();
        set.add(5);
        set.add(2);
        set.add(8);
        set.add(1);
        set.add(3);
        System.out.println("TreeSet (sorted): " + set);
        System.out.println("First: " + set.first());
        System.out.println("Last: " + set.last());
    }

    static void example2_CustomComparator() {
        System.out.println("\n=== Example 2: Custom Comparator ===");
        TreeSet<String> byLength = new TreeSet<>(Comparator.comparingInt(String::length));
        byLength.addAll(Arrays.asList("Banana", "Apple", "Fig", "Cherry"));
        System.out.println("By length: " + byLength);
        TreeSet<String> reverse = new TreeSet<>(Comparator.reverseOrder());
        reverse.addAll(Arrays.asList("Banana", "Apple", "Cherry"));
        System.out.println("Reverse alpha: " + reverse);
    }

    static void example3_NavigableSetMethods() {
        System.out.println("\n=== Example 3: NavigableSet Methods ===");
        TreeSet<Integer> set = new TreeSet<>(Arrays.asList(10, 20, 30, 40, 50));
        System.out.println("Set: " + set);
        System.out.println("Lower(25): " + set.lower(25));
        System.out.println("Higher(25): " + set.higher(25));
        System.out.println("Floor(25): " + set.floor(25));
        System.out.println("Ceiling(25): " + set.ceiling(25));
        System.out.println("HeadSet(30): " + set.headSet(30));
        System.out.println("TailSet(30): " + set.tailSet(30));
        System.out.println("SubSet(20,40): " + set.subSet(20, 40));
    }

    static void example4_PartialOrdering() {
        System.out.println("\n=== Example 4: SubSet Views ===");
        TreeSet<Integer> set = new TreeSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
        System.out.println("HeadSet(5, true): " + set.headSet(5, true));
        System.out.println("TailSet(5, false): " + set.tailSet(5, false));
    }

    static void example5_TreeSetPerformance() {
        System.out.println("\n=== Example 5: Performance Comparison ===");
        HashSet<Integer> hs = new HashSet<>();
        TreeSet<Integer> ts = new TreeSet<>();
        int size = 100000;
        long start = System.nanoTime();
        for (int i = 0; i < size; i++) hs.add(i);
        long hsTime = System.nanoTime() - start;
        start = System.nanoTime();
        for (int i = 0; i < size; i++) ts.add(i);
        long tsTime = System.nanoTime() - start;
        System.out.println("HashSet add " + size + ": " + hsTime + " ns");
        System.out.println("TreeSet add " + size + ": " + tsTime + " ns");
    }
}
