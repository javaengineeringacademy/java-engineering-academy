package academy.javaengineering.exercises;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Exercises: Set (HashSet, TreeSet) Operations
 *
 * Complete the TODO sections below.
 */
public class SetExercises {

    // TODO 1: Find all common elements between multiple sets
    public <T> Set<T> commonElements(Set<T>... sets) {
        // TODO: implement this
        return new HashSet<>();
    }

    // TODO 2: Check if one set is a subset of another
    public <T> boolean isSubset(Set<T> subset, Set<T> superset) {
        // TODO: implement this
        return false;
    }

    // TODO 3: Find the power set of a set
    // Return a set containing all possible subsets
    // Example: {1, 2} -> {{}, {1}, {2}, {1, 2}}
    public <T> Set<Set<T>> powerSet(Set<T> set) {
        // TODO: implement this
        return new HashSet<>();
    }

    // TODO 4: Get elements that are in set A but not in set B (A - B)
    public <T> Set<T> difference(Set<T> setA, Set<T> setB) {
        // TODO: implement this
        return new HashSet<>();
    }

    // TODO 5: Get the symmetric difference (elements in either A or B but not both)
    public <T> Set<T> symmetricDifference(Set<T> setA, Set<T> setB) {
        // TODO: implement this
        return new HashSet<>();
    }

    // TODO 6: Group strings by their length using TreeSet
    // Returns a TreeSet sorted by string length
    public TreeSet<String> sortByLength(String[] words) {
        // TODO: implement this using a TreeSet with a custom comparator
        return new TreeSet<>();
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        SetExercises exercises = new SetExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== SetExercises Tests ===\n");

        // Test 1
        total++;
        Set<Integer> s1 = Set.of(1, 2, 3, 4, 5);
        Set<Integer> s2 = Set.of(3, 4, 5, 6, 7);
        Set<Integer> s3 = Set.of(5, 6, 7, 8, 9);
        Set<Integer> common = exercises.commonElements(s1, s2, s3);
        if (common.equals(Set.of(5))) {
            System.out.println("Test 1 PASSED: commonElements");
            passed++;
        } else {
            System.out.println("Test 1 FAILED: commonElements - got " + common);
        }

        // Test 2
        total++;
        Set<Integer> sub = Set.of(1, 2, 3);
        Set<Integer> sup = Set.of(1, 2, 3, 4, 5);
        Set<Integer> notSub = Set.of(1, 2, 6);
        if (exercises.isSubset(sub, sup) && !exercises.isSubset(notSub, sup)) {
            System.out.println("Test 2 PASSED: isSubset");
            passed++;
        } else {
            System.out.println("Test 2 FAILED: isSubset");
        }

        // Test 3
        total++;
        Set<Integer> psSet = Set.of(1, 2);
        Set<Set<Integer>> powerSet = exercises.powerSet(psSet);
        if (powerSet.size() == 4
            && powerSet.contains(Set.of())
            && powerSet.contains(Set.of(1))
            && powerSet.contains(Set.of(2))
            && powerSet.contains(Set.of(1, 2))) {
            System.out.println("Test 3 PASSED: powerSet");
            passed++;
        } else {
            System.out.println("Test 3 FAILED: powerSet - got " + powerSet);
        }

        // Test 4
        total++;
        Set<Integer> diffA = Set.of(1, 2, 3, 4);
        Set<Integer> diffB = Set.of(3, 4, 5, 6);
        Set<Integer> diff = exercises.difference(diffA, diffB);
        if (diff.equals(Set.of(1, 2))) {
            System.out.println("Test 4 PASSED: difference");
            passed++;
        } else {
            System.out.println("Test 4 FAILED: difference - got " + diff);
        }

        // Test 5
        total++;
        Set<Integer> symA = Set.of(1, 2, 3);
        Set<Integer> symB = Set.of(3, 4, 5);
        Set<Integer> symDiff = exercises.symmetricDifference(symA, symB);
        if (symDiff.equals(Set.of(1, 2, 4, 5))) {
            System.out.println("Test 5 PASSED: symmetricDifference");
            passed++;
        } else {
            System.out.println("Test 5 FAILED: symmetricDifference - got " + symDiff);
        }

        // Test 6
        total++;
        String[] words = {"apple", "bat", "cat", "dog", "elephant", "fig"};
        TreeSet<String> sorted = exercises.sortByLength(words);
        String[] sortedArray = sorted.toArray(new String[0]);
        if (sortedArray.length == 6
            && sortedArray[0].length() <= sortedArray[1].length()
            && sortedArray[1].length() <= sortedArray[2].length()) {
            System.out.println("Test 6 PASSED: sortByLength");
            passed++;
        } else {
            System.out.println("Test 6 FAILED: sortByLength - got " + sorted);
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
