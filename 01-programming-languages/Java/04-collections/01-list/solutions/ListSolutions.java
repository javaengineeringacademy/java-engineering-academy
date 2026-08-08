package list.solutions;

import java.util.*;

public class ListSolutions {

    public static <T> void rotateRight(List<T> list, int n) {
        if (list.isEmpty()) return;
        n = n % list.size();
        if (n < 0) n += list.size();
        List<T> rotated = new ArrayList<>(list.subList(list.size() - n, list.size()));
        rotated.addAll(list.subList(0, list.size() - n));
        list.clear();
        list.addAll(rotated);
    }

    public static <T> List<T> removeDuplicates(List<T> list) {
        Set<T> seen = new LinkedHashSet<>(list);
        return new ArrayList<>(seen);
    }

    public static int findSecondLargest(List<Integer> list) {
        if (list.size() < 2) throw new IllegalArgumentException("Need at least 2 elements");
        int largest = Integer.MIN_VALUE;
        int secondLargest = Integer.MIN_VALUE;
        for (int num : list) {
            if (num > largest) {
                secondLargest = largest;
                largest = num;
            } else if (num > secondLargest && num != largest) {
                secondLargest = num;
            }
        }
        return secondLargest;
    }

    public static <T extends Comparable<T>> List<T> mergeSortedLists(List<T> list1, List<T> list2) {
        List<T> merged = new ArrayList<>();
        int i = 0, j = 0;
        while (i < list1.size() && j < list2.size()) {
            if (list1.get(i).compareTo(list2.get(j)) <= 0) {
                merged.add(list1.get(i++));
            } else {
                merged.add(list2.get(j++));
            }
        }
        merged.addAll(list1.subList(i, list1.size()));
        merged.addAll(list2.subList(j, list2.size()));
        return merged;
    }

    public static Map<String, List<Integer>> partitionEvenOdd(List<Integer> list) {
        Map<String, List<Integer>> result = new HashMap<>();
        result.put("even", new ArrayList<>());
        result.put("odd", new ArrayList<>());
        for (Integer num : list) {
            if (num % 2 == 0) {
                result.get("even").add(num);
            } else {
                result.get("odd").add(num);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        List<Integer> nums = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
        System.out.println("Original: " + nums);
        rotateRight(nums, 3);
        System.out.println("Rotated right 3: " + nums);

        List<Integer> dupes = Arrays.asList(1, 2, 2, 3, 3, 3, 4);
        System.out.println("Remove duplicates: " + removeDuplicates(dupes));

        System.out.println("Second largest: " + findSecondLargest(Arrays.asList(5, 1, 9, 3, 7)));

        List<Integer> sorted1 = Arrays.asList(1, 3, 5, 7);
        List<Integer> sorted2 = Arrays.asList(2, 4, 6, 8);
        System.out.println("Merged sorted: " + mergeSortedLists(sorted1, sorted2));

        System.out.println("Partition: " + partitionEvenOdd(Arrays.asList(1, 2, 3, 4, 5, 6)));
    }
}
