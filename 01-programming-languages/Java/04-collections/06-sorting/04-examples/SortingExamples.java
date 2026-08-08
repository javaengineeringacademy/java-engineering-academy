package academy.javaengineering.collections.sorting.examples;

import java.util.*;

public class SortingExamples {
    public static void main(String[] args) {
        System.out.println("=== Sorting Examples ===\n");
        List<String> names = new ArrayList<>(Arrays.asList("Charlie", "Alice", "Bob", "David"));
        Collections.sort(names);
        System.out.println("Natural: " + names);
        names.sort(Comparator.comparingInt(String::length));
        System.out.println("By length: " + names);
        names.sort(Comparator.comparingInt(String::length).reversed());
        System.out.println("By length desc: " + names);
    }
}
