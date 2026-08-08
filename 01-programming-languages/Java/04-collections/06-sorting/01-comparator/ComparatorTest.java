package academy.javaengineering.collections.sorting.comparator;

import java.util.*;

public class ComparatorTest {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(Arrays.asList("cc", "aaa", "bb"));
        list.sort(Comparator.comparingInt(String::length));
        assert list.get(0).length() == 2 : "Shortest first";
        System.out.println("ComparatorTest passed");
    }
}
