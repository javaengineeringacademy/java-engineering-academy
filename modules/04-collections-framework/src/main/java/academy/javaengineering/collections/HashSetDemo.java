package academy.javaengineering.collections;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates HashSet operations for unique element storage.
 * HashSet provides O(1) average time for add, remove, and contains operations.
 */
public class HashSetDemo {

    public static void main(String[] args) {
        demonstrateBasicOperations();
        demonstrateSetOperations();
        demonstrateAdvancedPatterns();
    }

    /**
     * Demonstrates basic HashSet operations.
     */
    private static void demonstrateBasicOperations() {
        System.out.println("=== HashSet Basic Operations ===");

        // Create and populate
        Set<String> fruits = new HashSet<>();
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Cherry");
        fruits.add("Apple"); // Duplicate ignored

        System.out.println("Set: " + fruits);
        System.out.println("Size: " + fruits.size());
        System.out.println("Contains Apple: " + fruits.contains("Apple"));

        // Remove
        fruits.remove("Banana");
        System.out.println("After removing Banana: " + fruits);

        // Remove duplicates from list
        List<Integer> numbersWithDuplicates = List.of(1, 2, 3, 1, 2, 4, 5, 3);
        Set<Integer> uniqueNumbers = new HashSet<>(numbersWithDuplicates);
        System.out.println("Original: " + numbersWithDuplicates);
        System.out.println("Unique: " + uniqueNumbers);
        System.out.println();
    }

    /**
     * Demonstrates set operations (union, intersection, difference).
     */
    private static void demonstrateSetOperations() {
        System.out.println("=== Set Operations ===");

        Set<String> set1 = new HashSet<>(Set.of("A", "B", "C", "D"));
        Set<String> set2 = new HashSet<>(Set.of("C", "D", "E", "F"));

        // Union
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        System.out.println("Union: " + union);

        // Intersection
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        System.out.println("Intersection: " + intersection);

        // Difference
        Set<String> difference = new HashSet<>(set1);
        difference.removeAll(set2);
        System.out.println("Difference (set1 - set2): " + difference);

        // Symmetric Difference
        Set<String> symDiff = new HashSet<>(set1);
        symDiff.addAll(set2);
        Set<String> common = new HashSet<>(set1);
        common.retainAll(set2);
        symDiff.removeAll(common);
        System.out.println("Symmetric Difference: " + symDiff);
        System.out.println();
    }

    /**
     * Demonstrates advanced HashSet patterns.
     */
    private static void demonstrateAdvancedPatterns() {
        System.out.println("=== Advanced Patterns ===");

        // Pattern 1: Find missing numbers
        Set<Integer> allNumbers = new HashSet<>();
        for (int i = 1; i <= 10; i++) {
            allNumbers.add(i);
        }
        Set<Integer> present = Set.of(1, 2, 4, 6, 7, 9);
        allNumbers.removeAll(present);
        System.out.println("Missing numbers: " + allNumbers);

        // Pattern 2: Anagram detection
        String word1 = "listen";
        String word2 = "silent";
        boolean areAnagrams = isAnagram(word1, word2);
        System.out.println("'" + word1 + "' and '" + word2 + "' are anagrams: " + areAnagrams);

        // Pattern 3: Find common elements in multiple lists
        List<String> list1 = List.of("A", "B", "C", "D");
        List<String> list2 = List.of("B", "C", "E", "F");
        List<String> list3 = List.of("B", "C", "G", "H");
        Set<String> commonInAll = findCommonElements(list1, list2, list3);
        System.out.println("Common in all lists: " + commonInAll);
    }

    /**
     * Checks if two strings are anagrams.
     */
    private static boolean isAnagram(String s1, String s2) {
        if (s1.length() != s2.length()) return false;
        Set<Character> chars1 = new HashSet<>();
        Set<Character> chars2 = new HashSet<>();
        for (char c : s1.toCharArray()) chars1.add(c);
        for (char c : s2.toCharArray()) chars2.add(c);
        return chars1.equals(chars2);
    }

    /**
     * Finds common elements in multiple lists.
     */
    @SafeVarargs
    private static <T> Set<T> findCommonElements(List<T>... lists) {
        if (lists.length == 0) return Set.of();
        Set<T> common = new HashSet<>(lists[0]);
        for (int i = 1; i < lists.length; i++) {
            common.retainAll(new HashSet<>(lists[i]));
        }
        return common;
    }
}
