package academy.javaengineering.collections.sorting.solutions;

import java.util.*;

public class SortingSolutions {
    public static List<String> sortByLength(List<String> list) {
        List<String> result = new ArrayList<>(list);
        result.sort(Comparator.comparingInt(String::length));
        return result;
    }
    public static List<Integer> sortDescending(List<Integer> list) {
        List<Integer> result = new ArrayList<>(list);
        result.sort(Comparator.reverseOrder());
        return result;
    }
    public static void main(String[] args) {
        System.out.println(sortByLength(Arrays.asList("Banana", "Hi", "Apple")));
        System.out.println(sortDescending(Arrays.asList(1, 5, 2, 4, 3)));
    }
}
