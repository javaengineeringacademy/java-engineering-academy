package academy.javaengineering.collections.map.hashtable.solutions;

import java.util.*;

public class HashtableSolutions {
    public static Hashtable<String, Integer> synchronizeMap(Map<String, Integer> map) {
        return new Hashtable<>(map);
    }
    public static void main(String[] args) {
        Map<String, Integer> m = new HashMap<>(Map.of("a", 1));
        System.out.println(synchronizeMap(m));
    }
}
