package academy.javaengineering.collections.map.hashmap.solutions;

import java.util.*;

public class HashMapSolutions {
    public static Map<Character, Integer> charFrequency(String str) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : str.toCharArray()) freq.merge(c, 1, Integer::sum);
        return freq;
    }
    public static <K, V> Map<V, K> invert(Map<K, V> map) {
        Map<V, K> result = new HashMap<>();
        map.forEach((k, v) -> result.put(v, k));
        return result;
    }
    public static <K> K keyWithMaxValue(Map<K, Integer> map) {
        return map.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(null);
    }
    public static void main(String[] args) {
        System.out.println(charFrequency("hello"));
        Map<String, Integer> m = new HashMap<>(Map.of("a", 1, "b", 2, "c", 3));
        System.out.println(invert(m));
        System.out.println(keyWithMaxValue(m));
    }
}
