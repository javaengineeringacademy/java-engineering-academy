package academy.javaengineering.collections.sorting.timsort;

import java.util.*;

public class TimSortTest {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(Arrays.asList(5, 3, 8, 1, 9, 2));
        Collections.sort(list);
        for (int i = 1; i < list.size(); i++) assert list.get(i) >= list.get(i-1) : "Should be sorted";
        System.out.println("TimSortTest passed");
    }
}
