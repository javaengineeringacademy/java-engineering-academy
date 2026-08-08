package set.examples;

import java.util.*;

public class SetExample {

    public static void main(String[] args) {
        example1_BasicSetOperations();
        example2_SetFromCollection();
        example3_SetUnionIntersection();
        example4_SetEquality();
        example5_ImmutableSet();
    }

    static void example1_BasicSetOperations() {
        System.out.println("=== Example 1: Basic Set Operations ===");
        Set<String> set = new HashSet<>();
        set.add("Java");
        set.add("Python");
        set.add("Java");
        System.out.println("Set (no duplicates): " + set);
        System.out.println("Size: " + set.size());
        System.out.println("Contains Java: " + set.contains("Java"));
    }

    static void example2_SetFromCollection() {
        System.out.println("\n=== Example 2: Set from Collection ===");
        List<String> list = Arrays.asList("A", "B", "A", "C", "B");
        Set<String> set = new LinkedHashSet<>(list);
        System.out.println("List: " + list);
        System.out.println("Set (preserves order, no dupes): " + set);
    }

    static void example3_SetUnionIntersection() {
        System.out.println("\n=== Example 3: Set Union and Intersection ===");
        Set<Integer> set1 = new HashSet<>(Arrays.asList(1, 2, 3, 4));
        Set<Integer> set2 = new HashSet<>(Arrays.asList(3, 4, 5, 6));
        Set<Integer> union = new HashSet<>(set1);
        union.addAll(set2);
        System.out.println("Union: " + union);
        Set<Integer> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        System.out.println("Intersection: " + intersection);
        Set<Integer> difference = new HashSet<>(set1);
        difference.removeAll(set2);
        System.out.println("Difference: " + difference);
    }

    static void example4_SetEquality() {
        System.out.println("\n=== Example 4: Set Equality ===");
        Set<String> set1 = new HashSet<>(Arrays.asList("A", "B", "C"));
        Set<String> set2 = new HashSet<>(Arrays.asList("C", "B", "A"));
        System.out.println("set1: " + set1);
        System.out.println("set2: " + set2);
        System.out.println("Equal: " + set1.equals(set2));
    }

    static void example5_ImmutableSet() {
        System.out.println("\n=== Example 5: Immutable Set ===");
        Set<String> immutable = Set.of("Java", "Python", "C++");
        System.out.println("Immutable set: " + immutable);
        try {
            immutable.add("Go");
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify immutable set");
        }
    }
}
