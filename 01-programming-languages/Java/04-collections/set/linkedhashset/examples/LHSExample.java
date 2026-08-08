package set.linkedhashset.examples;

import java.util.*;

public class LHSExample {

    public static void main(String[] args) {
        example1_InsertionOrder();
        example2_PerformanceComparison();
        example3_LHSSetOperations();
        example4_LHSVsHashSet();
        example5_LHSIteration();
    }

    static void example1_InsertionOrder() {
        System.out.println("=== Example 1: Insertion Order Maintained ===");
        LinkedHashSet<String> set = new LinkedHashSet<>();
        set.add("Banana");
        set.add("Apple");
        set.add("Cherry");
        set.add("Apple");
        System.out.println("LinkedHashSet: " + set);
        System.out.println("Size (no dupes): " + set.size());
    }

    static void example2_PerformanceComparison() {
        System.out.println("\n=== Example 2: Performance ===");
        LinkedHashSet<Integer> lhs = new LinkedHashSet<>();
        HashSet<Integer> hs = new HashSet<>();
        int size = 100000;
        long start = System.nanoTime();
        for (int i = 0; i < size; i++) lhs.add(i);
        long lhsTime = System.nanoTime() - start;
        start = System.nanoTime();
        for (int i = 0; i < size; i++) hs.add(i);
        long hsTime = System.nanoTime() - start;
        System.out.println("LinkedHashSet add " + size + ": " + lhsTime + " ns");
        System.out.println("HashSet add " + size + ": " + hsTime + " ns");
    }

    static void example3_LHSSetOperations() {
        System.out.println("\n=== Example 3: Set Operations ===");
        LinkedHashSet<Integer> set1 = new LinkedHashSet<>(Arrays.asList(1, 2, 3));
        LinkedHashSet<Integer> set2 = new LinkedHashSet<>(Arrays.asList(2, 3, 4));
        LinkedHashSet<Integer> union = new LinkedHashSet<>(set1);
        union.addAll(set2);
        System.out.println("Union: " + union);
        LinkedHashSet<Integer> intersection = new LinkedHashSet<>(set1);
        intersection.retainAll(set2);
        System.out.println("Intersection: " + intersection);
    }

    static void example4_LHSVsHashSet() {
        System.out.println("\n=== Example 4: LinkedHashSet vs HashSet ===");
        LinkedHashSet<String> lhs = new LinkedHashSet<>(Arrays.asList("C", "A", "B"));
        HashSet<String> hs = new HashSet<>(Arrays.asList("C", "A", "B"));
        System.out.println("LinkedHashSet (ordered): " + lhs);
        System.out.println("HashSet (unordered): " + hs);
    }

    static void example5_LHSIteration() {
        System.out.println("\n=== Example 5: Iteration Order ===");
        LinkedHashSet<String> set = new LinkedHashSet<>();
        set.add("First");
        set.add("Second");
        set.add("Third");
        set.add("Fourth");
        System.out.print("Iteration: ");
        for (String s : set) {
            System.out.print(s + " ");
        }
        System.out.println();
    }
}
