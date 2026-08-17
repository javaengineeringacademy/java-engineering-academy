package academy.javaengineering.collections.searching.collectionsindexof.solutions;

import java.util.*;

public class CollectionsIndexOfSolutions {
    public static <T> int indexOf(List<T> list, T target) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(target)) return i;
        }
        return -1;
    }
    public static <T> boolean contains(List<T> list, T target) {
        return indexOf(list, target) != -1;
    }
    public static <T extends Comparable<T>> int binarySearch(List<T> list, T target) {
        int low = 0, high = list.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int cmp = list.get(mid).compareTo(target);
            if (cmp == 0) return mid;
            else if (cmp < 0) low = mid + 1;
            else high = mid - 1;
        }
        return -1;
    }
    public static <T> int lastIndexOf(List<T> list, T target) {
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).equals(target)) return i;
        }
        return -1;
    }
    public static <T> List<Integer> findAll(List<T> list, T target) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(target)) indices.add(i);
        }
        return indices;
    }
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(Arrays.asList("Java", "Python", "C++", "Java"));
        System.out.println("indexOf Java: " + indexOf(list, "Java"));
        System.out.println("contains Python: " + contains(list, "Python"));
        System.out.println("lastIndexOf Java: " + lastIndexOf(list, "Java"));
        System.out.println("findAll Java: " + findAll(list, "Java"));

        List<Integer> sorted = Arrays.asList(10, 20, 30, 40, 50);
        System.out.println("binarySearch 30: " + binarySearch(sorted, 30));
    }
}