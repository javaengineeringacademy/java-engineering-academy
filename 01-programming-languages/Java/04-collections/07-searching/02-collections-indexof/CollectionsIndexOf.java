package academy.javaengineering.collections.searching.collectionsindexof;

import java.util.*;

public class CollectionsIndexOfTest {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        assert list.indexOf(3) == 2 : "Index should be 2";
        assert Collections.binarySearch(list, 4) == 3 : "Binary search index 3";
        System.out.println("CollectionsIndexOfTest passed");
    }
}
