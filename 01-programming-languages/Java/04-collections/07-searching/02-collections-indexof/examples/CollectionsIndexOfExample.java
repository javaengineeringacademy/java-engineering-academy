package academy.javaengineering.collections.searching.collectionsindexof;

import java.util.*;

public class CollectionsIndexOfExample {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(Arrays.asList("Java", "Python", "C++"));
        System.out.println("indexOf Python: " + list.indexOf("Python"));
        System.out.println("contains Java: " + list.contains("Java"));

        List<Integer> sorted = Arrays.asList(10, 20, 30, 40, 50);
        int idx = Collections.binarySearch(sorted, 30);
        System.out.println("binarySearch 30: " + idx);
    }
}
