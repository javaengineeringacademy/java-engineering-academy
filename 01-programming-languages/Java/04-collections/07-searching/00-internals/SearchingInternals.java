package academy.javaengineering.collections.searching.internals;

import java.util.*;

public class SearchingInternals {

    public static void main(String[] args) {
        System.out.println("=== Searching Algorithms Internals ===\n");

        // 1. Linear search
        System.out.println("--- Linear Search ---");
        List<Integer> list = Arrays.asList(5, 2, 8, 1, 9, 3);
        int index = list.indexOf(8);
        System.out.println("Found 8 at index: " + index);
        System.out.println("O(n) - checks each element");

        // 2. Binary search
        System.out.println("\n--- Binary Search ---");
        List<Integer> sorted = Arrays.asList(1, 2, 3, 5, 8, 9);
        int idx = Collections.binarySearch(sorted, 5);
        System.out.println("Found 5 at index: " + idx);
        System.out.println("O(log n) - requires sorted list");

        // 3. Collections.frequency()
        System.out.println("\n--- Frequency Count ---");
        List<String> names = Arrays.asList("Java", "Python", "Java", "C++", "Java");
        int freq = Collections.frequency(names, "Java");
        System.out.println("Java appears: " + freq + " times");

        // 4. Collections.indexOfSubList()
        System.out.println("\n--- SubList Search ---");
        List<String> source = Arrays.asList("A", "B", "C", "D", "E");
        List<String> target = Arrays.asList("C", "D");
        int pos = Collections.indexOfSubList(source, target);
        System.out.println("SubList at index: " + pos);

        // 5. Stream search
        System.out.println("\n--- Stream Search ---");
        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        Optional<Integer> first = nums.stream()
            .filter(n -> n > 5)
            .findFirst();
        System.out.println("First > 5: " + first.orElse(-1));

        // 6. anyMatch, allMatch, noneMatch
        System.out.println("\n--- Predicate Search ---");
        System.out.println("anyMatch(>5): " + nums.stream().anyMatch(n -> n > 5));
        System.out.println("allMatch(>0): " + nums.stream().allMatch(n -> n > 0));
        System.out.println("noneMatch(>10): " + nums.stream().noneMatch(n -> n > 10));
    }
}
