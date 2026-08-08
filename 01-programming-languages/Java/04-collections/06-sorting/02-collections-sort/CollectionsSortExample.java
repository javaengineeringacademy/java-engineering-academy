package academy.javaengineering.collections.sorting.collectionssort;

import java.util.*;

public class CollectionsSortExample {
    public static void main(String[] args) {
        List<Integer> nums = new ArrayList<>(Arrays.asList(5, 1, 3, 2, 4));
        Collections.sort(nums);
        System.out.println("Sorted: " + nums);
        List<String> words = new ArrayList<>(Arrays.asList("banana", "apple", "cherry"));
        Collections.sort(words, Comparator.reverseOrder());
        System.out.println("Reverse: " + words);
    }
}
