package academy.javaengineering.collections.sorting.timsort;

import java.util.*;

public class TimSortExample {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) list.add(new Random().nextInt(100));
        System.out.println("Before: " + list);
        Collections.sort(list);
        System.out.println("After: " + list);
    }
}
