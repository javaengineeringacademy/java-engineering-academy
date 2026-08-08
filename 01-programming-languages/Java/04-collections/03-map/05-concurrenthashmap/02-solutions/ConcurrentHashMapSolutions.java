package academy.javaengineering.collections.map.concurrentexamples;

import java.util.concurrent.*;

public class ConcurrentHashMapSolutions {
    public static ConcurrentHashMap<String, Integer> parallelCount(String[] words) {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        for (String word : words) map.merge(word, 1, Integer::sum);
        return map;
    }
    public static void main(String[] args) {
        System.out.println(parallelCount(new String[]{"a", "b", "a", "c", "a"}));
    }
}
