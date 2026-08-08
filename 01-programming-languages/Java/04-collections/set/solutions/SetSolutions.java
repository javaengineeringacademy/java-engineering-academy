package set.solutions;

import java.util.*;

public class SetSolutions {

    public static <T> Set<Set<T>> powerSet(Set<T> set) {
        List<T> list = new ArrayList<>(set);
        Set<Set<T>> result = new HashSet<>();
        int n = list.size();
        for (int i = 0; i < (1 << n); i++) {
            Set<T> subset = new HashSet<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) != 0) {
                    subset.add(list.get(j));
                }
            }
            result.add(subset);
        }
        return result;
    }

    public static <T> boolean areDisjoint(Set<T> set1, Set<T> set2) {
        for (T item : set1) {
            if (set2.contains(item)) return false;
        }
        return true;
    }

    public static <T> Set<T> symmetricDifference(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<>(set1);
        result.addAll(set2);
        Set<T> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        result.removeAll(intersection);
        return result;
    }

    public static Set<Integer> subsetGreaterThan(Set<Integer> set, int value) {
        Set<Integer> result = new HashSet<>();
        for (Integer num : set) {
            if (num > value) result.add(num);
        }
        return result;
    }

    public static Map<Character, Integer> charFrequency(String str) {
        Map<Character, Integer> freq = new TreeMap<>();
        for (char c : str.toCharArray()) {
            freq.merge(c, 1, Integer::sum);
        }
        return freq;
    }

    public static void main(String[] args) {
        Set<Integer> small = new HashSet<>(Arrays.asList(1, 2));
        System.out.println("Power set size: " + powerSet(small).size());

        Set<Integer> s1 = new HashSet<>(Arrays.asList(1, 2, 3));
        Set<Integer> s2 = new HashSet<>(Arrays.asList(3, 4, 5));
        System.out.println("Disjoint: " + areDisjoint(new HashSet<>(Arrays.asList(1, 2)), new HashSet<>(Arrays.asList(3, 4))));
        System.out.println("Symmetric diff: " + symmetricDifference(s1, s2));
        System.out.println("Subset > 3: " + subsetGreaterThan(new HashSet<>(Arrays.asList(1, 3, 5, 7, 2)), 3));
        System.out.println("Char freq: " + charFrequency("hello"));
    }
}
