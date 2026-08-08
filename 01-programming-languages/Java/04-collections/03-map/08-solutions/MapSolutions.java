package academy.javaengineering.collections.map.solutions;

import java.util.*;
import java.util.stream.*;

public class MapSolutions {
    public static Map<String, Integer> wordFrequency(String text) {
        Map<String, Integer> freq = new HashMap<>();
        for (String word : text.split("\\s+")) freq.merge(word, 1, Integer::sum);
        return freq;
    }
    public static <K, V> Map<V, K> reverseMap(Map<K, V> map) {
        Map<V, K> reversed = new HashMap<>();
        map.forEach((k, v) -> reversed.put(v, k));
        return reversed;
    }
    public static Map<String, List<String>> groupByFirstLetter(List<String> words) {
        return words.stream().collect(Collectors.groupingBy(w -> w.substring(0, 1).toLowerCase()));
    }
    public static void main(String[] args) {
        System.out.println(wordFrequency("hello world hello"));
        System.out.println(reverseMap(Map.of("a", 1, "b", 2)));
        System.out.println(groupByFirstLetter(List.of("apple", "banana", "avocado")));
    }
}
