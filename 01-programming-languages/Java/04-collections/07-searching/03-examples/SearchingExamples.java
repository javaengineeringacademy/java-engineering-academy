package academy.javaengineering.collections.searching.examples;

import java.util.*;

public class SearchingExamples {
    public static void main(String[] args) {
        System.out.println("=== Searching Examples ===\n");
        List<String> list = new ArrayList<>(Arrays.asList("Java", "Python", "C++"));
        System.out.println("indexOf: " + list.indexOf("Python"));
        System.out.println("contains: " + list.contains("Java"));
        List<Integer> sorted = Arrays.asList(10, 20, 30, 40, 50);
        System.out.println("binarySearch: " + Collections.binarySearch(sorted, 30));
    }
}
