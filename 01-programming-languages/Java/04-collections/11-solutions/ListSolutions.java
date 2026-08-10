package academy.javaengineering.collections.solutions;

import java.util.*;
import java.util.stream.*;

public class ListSolutions {

    // Exercise 1: Remove duplicates while maintaining order
    public static List<Integer> removeDuplicates(List<Integer> list) {
        return new ArrayList<>(new LinkedHashSet<>(list));
    }

    // Exercise 2: Rotate list by k positions to the right
    public static List<Integer> rotateRight(List<Integer> list, int k) {
        if (list == null || list.isEmpty()) return list;
        k = k % list.size();
        if (k == 0) return new ArrayList<>(list);
        List<Integer> result = new ArrayList<>(list);
        Collections.rotate(result, k);
        return result;
    }

    // Exercise 3: Find second largest element
    public static int findSecondLargest(List<Integer> list) {
        int first = Integer.MIN_VALUE;
        int second = Integer.MIN_VALUE;
        for (int num : list) {
            if (num > first) {
                second = first;
                first = num;
            } else if (num > second && num != first) {
                second = num;
            }
        }
        return second;
    }

    // Exercise 4: Merge two sorted lists
    public static List<Integer> mergeSorted(List<Integer> list1, List<Integer> list2) {
        List<Integer> result = new ArrayList<>();
        int i = 0, j = 0;
        while (i < list1.size() && j < list2.size()) {
            if (list1.get(i) <= list2.get(j)) {
                result.add(list1.get(i++));
            } else {
                result.add(list2.get(j++));
            }
        }
        while (i < list1.size()) result.add(list1.get(i++));
        while (j < list2.size()) result.add(list2.get(j++));
        return result;
    }

    // Exercise 5: Find intersection
    public static List<Integer> intersection(List<Integer> list1, List<Integer> list2) {
        Set<Integer> set2 = new HashSet<>(list2);
        return list1.stream()
            .filter(set2::contains)
            .distinct()
            .collect(Collectors.toList());
    }
}