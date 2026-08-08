package academy.javaengineering.collections.solutions;

import java.util.*;
import java.util.stream.*;
import java.util.function.*;

public class MapSolutions {

    // Exercise 11: Character frequency
    public static Map<Character, Integer> charFrequency(String str) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : str.toCharArray()) {
            freq.merge(c, 1, Integer::sum);
        }
        return freq;
    }

    // Exercise 12: Invert map
    public static Map<String, Integer> invertMap(Map<Integer, String> map) {
        Map<String, Integer> result = new HashMap<>();
        map.forEach((k, v) -> result.put(v, k));
        return result;
    }

    // Exercise 13: Key with max value
    public static <K> K keyWithMaxValue(Map<K, Integer> map) {
        return map.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    // Exercise 14: Merge two maps
    public static Map<String, Integer> mergeMaps(Map<String, Integer> map1, Map<String, Integer> map2) {
        Map<String, Integer> result = new HashMap<>(map1);
        map2.forEach((k, v) -> result.merge(k, v, Integer::sum));
        return result;
    }

    // Exercise 15: Word index
    public static Map<String, List<Integer>> wordIndex(String text) {
        Map<String, List<Integer>> index = new HashMap<>();
        String[] words = text.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            String word = words[i].toLowerCase().replaceAll("[^a-z]", "");
            index.computeIfAbsent(word, k -> new ArrayList<>()).add(i);
        }
        return index;
    }
}