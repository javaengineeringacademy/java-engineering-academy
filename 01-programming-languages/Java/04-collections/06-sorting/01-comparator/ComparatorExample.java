package academy.javaengineering.collections.sorting.comparator;

import java.util.*;

public class ComparatorExample {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(Arrays.asList("Banana", "Apple", "Cherry", "Date"));
        list.sort(Comparator.comparingInt(String::length));
        System.out.println("By length: " + list);
        list.sort(Comparator.comparingInt(String::length).reversed());
        System.out.println("By length desc: " + list);
    }
}
