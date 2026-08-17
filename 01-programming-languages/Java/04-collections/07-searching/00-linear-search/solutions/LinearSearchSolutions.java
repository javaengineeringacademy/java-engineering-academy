package academy.javaengineering.collections.searching.linearsearch.solutions;

import java.util.*;

public class LinearSearchSolutions {
    public static <T> int linearSearch(List<T> list, T target) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(target)) return i;
        }
        return -1;
    }
    public static <T> List<Integer> findAllOccurrences(List<T> list, T target) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(target)) indices.add(i);
        }
        return indices;
    }
    public static <T extends Comparable<T>> int indexOfMin(List<T> list) {
        if (list.isEmpty()) return -1;
        int minIndex = 0;
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).compareTo(list.get(minIndex)) < 0) {
                minIndex = i;
            }
        }
        return minIndex;
    }
    public static <T extends Comparable<T>> int indexOfMax(List<T> list) {
        if (list.isEmpty()) return -1;
        int maxIndex = 0;
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).compareTo(list.get(maxIndex)) > 0) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }
    public static <T> boolean contains(List<T> list, T target) {
        return linearSearch(list, target) != -1;
    }
    public static void main(String[] args) {
        List<String> list = Arrays.asList("Java", "Python", "C++", "Java");
        System.out.println("Found Python at: " + linearSearch(list, "Python"));
        System.out.println("All occurrences of Java: " + findAllOccurrences(list, "Java"));
        System.out.println("Index of min: " + indexOfMin(list));
        System.out.println("Index of max: " + indexOfMax(list));
        System.out.println("Contains Go: " + contains(list, "Go"));
    }
}