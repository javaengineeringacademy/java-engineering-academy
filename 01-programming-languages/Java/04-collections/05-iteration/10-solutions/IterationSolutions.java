package academy.javaengineering.collections.iteration.solutions;

import java.util.*;

public class IterationSolutions {
    // Solution 1: Find second largest
    public static int findSecondLargest(List<Integer> list) {
        int first = Integer.MIN_VALUE, second = Integer.MIN_VALUE;
        for (int num : list) {
            if (num > first) { second = first; first = num; }
            else if (num > second && num != first) second = num;
        }
        return second;
    }

    // Solution 2: Count occurrences
    public static int countOccurrences(List<String> list, String target) {
        int count = 0;
        for (String s : list) if (s.equals(target)) count++;
        return count;
    }

    // Solution 3: Remove short strings
    public static void removeShortStrings(List<String> list, int minLength) {
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            if (it.next().length() < minLength) it.remove();
        }
    }

    // Solution 4: Create reversed list
    public static <T> List<T> createReversedList(List<T> list) {
        List<T> reversed = new ArrayList<>();
        ListIterator<T> lit = list.listIterator(list.size());
        while (lit.hasPrevious()) reversed.add(lit.previous());
        return reversed;
    }

    // Solution 5: Find first duplicate
    public static Integer findFirstDuplicate(List<Integer> list) {
        Set<Integer> seen = new HashSet<>();
        for (int num : list) {
            if (!seen.add(num)) return num;
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println("Second largest: " + findSecondLargest(Arrays.asList(10, 20, 4, 45, 99)));
        System.out.println("Java count: " + countOccurrences(Arrays.asList("Java", "Python", "Java"), "Java"));

        List<String> langs = new ArrayList<>(Arrays.asList("Hi", "Hello", "Hey", "Ok"));
        removeShortStrings(langs, 4);
        System.out.println("After remove: " + langs);

        System.out.println("Reversed: " + createReversedList(Arrays.asList("A", "B", "C")));
        System.out.println("First duplicate: " + findFirstDuplicate(Arrays.asList(1, 2, 3, 2, 4)));
    }
}
