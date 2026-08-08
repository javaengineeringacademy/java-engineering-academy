package academy.javaengineering.collections.searching.linearsearch;

import java.util.*;

public class LinearSearchExample {
    public static <T> int linearSearch(List<T> list, T target) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(target)) return i;
        }
        return -1;
    }
    public static void main(String[] args) {
        List<String> list = Arrays.asList("Java", "Python", "C++");
        System.out.println("Found Python at: " + linearSearch(list, "Python"));
        System.out.println("Found Go at: " + linearSearch(list, "Go"));
    }
}
