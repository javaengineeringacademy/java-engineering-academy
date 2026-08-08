package academy.javaengineering.collections.solutions;

import java.util.*;
import java.util.stream.*;

public class SetSolutions {

    // Exercise 6: Union of two sets
    public static Set<Integer> union(Set<Integer> set1, Set<Integer> set2) {
        Set<Integer> result = new HashSet<>(set1);
        result.addAll(set2);
        return result;
    }

    // Exercise 7: Difference of two sets
    public static Set<Integer> difference(Set<Integer> set1, Set<Integer> set2) {
        Set<Integer> result = new HashSet<>(set1);
        result.removeAll(set2);
        return result;
    }

    // Exercise 8: Check if subset
    public static boolean isSubset(Set<Integer> subset, Set<Integer> superset) {
        return superset.containsAll(subset);
    }

    // Exercise 9: Power set
    public static Set<Set<Integer>> powerSet(Set<Integer> set) {
        Set<Set<Integer>> result = new HashSet<>();
        List<Integer> list = new ArrayList<>(set);
        int n = list.size();
        for (int i = 0; i < (1 << n); i++) {
            Set<Integer> subset = new HashSet<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) != 0) {
                    subset.add(list.get(j));
                }
            }
            result.add(subset);
        }
        return result;
    }

    // Exercise 10: Group by first character
    public static Map<Character, Set<String>> groupByFirstChar(List<String> strings) {
        return strings.stream()
            .filter(s -> !s.isEmpty())
            .collect(Collectors.groupingBy(
                s -> Character.toLowerCase(s.charAt(0)),
                Collectors.toSet()
            ));
    }
}