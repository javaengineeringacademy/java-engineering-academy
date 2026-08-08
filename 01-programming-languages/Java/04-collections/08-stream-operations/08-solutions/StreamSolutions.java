package academy.javaengineering.collections.streamoperations.solutions;

import java.util.*;
import java.util.stream.*;

public class StreamSolutions {
    public static List<String> filterStartingWithA(List<String> list) {
        return list.stream().filter(s -> s.toLowerCase().startsWith("a")).collect(Collectors.toList());
    }
    public static <T> Map<T, Integer> toLengthMap(List<T> list) {
        return list.stream().collect(Collectors.toMap(e -> e, e -> String.valueOf(e).length()));
    }
    public static int sumEven(List<Integer> list) {
        return list.stream().filter(n -> n % 2 == 0).mapToInt(Integer::intValue).sum();
    }
    public static String joinWithPipe(List<String> list) {
        return list.stream().collect(Collectors.joining(" | "));
    }
    public static Optional<Integer> findMax(List<Integer> list) {
        return list.stream().max(Integer::compareTo);
    }
    public static Map<Integer, List<String>> groupByLength(List<String> list) {
        return list.stream().collect(Collectors.groupingBy(String::length));
    }
    public static long countLongStrings(List<String> list) {
        return list.stream().filter(s -> s.length() > 3).count();
    }
    public static Optional<String> findFirstContainingE(List<String> list) {
        return list.stream().filter(s -> s.contains("e")).findFirst();
    }
    public static List<String> sortByLengthThenAlpha(List<String> list) {
        return list.stream().sorted(Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder())).collect(Collectors.toList());
    }
    public static boolean allPositive(List<Integer> list) {
        return list.stream().allMatch(n -> n > 0);
    }
    public static void main(String[] args) {
        List<String> words = Arrays.asList("apple", "hi", "banana", "ok", "avocado");
        System.out.println(filterStartingWithA(words));
        System.out.println(toLengthMap(Arrays.asList("hi", "hello")));
        System.out.println(sumEven(Arrays.asList(1,2,3,4,5)));
        System.out.println(joinWithPipe(Arrays.asList("a","b","c")));
        System.out.println(findMax(Arrays.asList(1,5,3)));
        System.out.println(groupByLength(words));
        System.out.println(countLongStrings(words));
        System.out.println(findFirstContainingE(words));
        System.out.println(sortByLengthThenAlpha(words));
        System.out.println(allPositive(Arrays.asList(1,2,3)));
    }
}
