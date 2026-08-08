package academy.javaengineering.collections.solutions;

import java.util.*;
import java.util.stream.*;

public class StreamSolutions {

    // Exercise 26: Find strings starting with 'a'
    public static List<String> startsWithA(List<String> strings) {
        return strings.stream()
            .filter(s -> s.toLowerCase().startsWith("a"))
            .collect(Collectors.toList());
    }

    // Exercise 27: Convert to map (string -> length)
    public static Map<String, Integer> toLengthMap(List<String> strings) {
        return strings.stream()
            .collect(Collectors.toMap(s -> s, String::length));
    }

    // Exercise 28: Find average
    public static double average(List<Integer> numbers) {
        return numbers.stream()
            .mapToInt(Integer::intValue)
            .average()
            .orElse(0);
    }

    // Exercise 29: Join with comma
    public static String joinWithComma(List<String> strings) {
        return strings.stream()
            .collect(Collectors.joining(", "));
    }

    // Exercise 30: Partition into even and odd
    public static Map<Boolean, List<Integer>> partitionEvensOdds(List<Integer> numbers) {
        return numbers.stream()
            .collect(Collectors.partitioningBy(n -> n % 2 == 0));
    }
}