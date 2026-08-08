package academy.javaengineering.collections.iteration.solutions;

import java.util.*;

public class IterationSolutions {
    public static int sumEvenNumbers(List<Integer> list) {
        int sum = 0;
        for (int n : list) if (n % 2 == 0) sum += n;
        return sum;
    }
    public static List<String> filterByLength(List<String> list, int minLen) {
        List<String> result = new ArrayList<>();
        for (String s : list) if (s.length() >= minLen) result.add(s);
        return result;
    }
    public static void removeDuplicates(List<String> list) {
        Set<String> seen = new LinkedHashSet<>(list);
        list.clear();
        list.addAll(seen);
    }
    public static int findIndex(List<String> list, String target) {
        for (int i = 0; i < list.size(); i++) if (list.get(i).equals(target)) return i;
        return -1;
    }
    public static <T> List<T> reverseList(List<T> list) {
        List<T> result = new ArrayList<>(list);
        Collections.reverse(result);
        return result;
    }
    public static void main(String[] args) {
        System.out.println(sumEvenNumbers(Arrays.asList(1,2,3,4,5)));
        System.out.println(filterByLength(Arrays.asList("Hi","Hello","Hey"), 4));
        System.out.println(findIndex(Arrays.asList("a","b","c"), "b"));
        System.out.println(reverseList(Arrays.asList(1,2,3)));
    }
}
