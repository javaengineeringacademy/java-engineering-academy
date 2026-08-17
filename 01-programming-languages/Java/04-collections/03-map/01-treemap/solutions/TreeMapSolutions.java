package academy.javaengineering.collections.map.treemap.solutions;

import java.util.*;

public class TreeMapSolutions {
    public static TreeMap<String, Integer> sortByValue(Map<String, Integer> map) {
        TreeMap<String, Integer> sorted = new TreeMap<>(Comparator.comparingInt(map::get));
        sorted.putAll(map);
        return sorted;
    }
    public static NavigableMap<String, Integer> subMapInRange(TreeMap<String, Integer> map, String start, String end) {
        return map.subMap(start, true, end, true);
    }
    public static void main(String[] args) {
        TreeMap<String, Integer> m = new TreeMap<>(Map.of("a", 3, "b", 1, "c", 2));
        System.out.println(sortByValue(m));
        System.out.println(subMapInRange(m, "a", "b"));
    }
}
