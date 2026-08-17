package academy.javaengineering.collections.sorting.collectionssort;

import java.util.*;

public class CollectionsSortTest {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(Arrays.asList(3, 1, 2));
        Collections.sort(list);
        assert list.equals(Arrays.asList(1, 2, 3)) : "Should be sorted";
        System.out.println("CollectionsSortTest passed");
    }
}
