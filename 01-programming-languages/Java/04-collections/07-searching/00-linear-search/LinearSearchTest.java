package academy.javaengineering.collections.searching.linearsearch;

import java.util.*;

public class LinearSearchTest {
    public static <T> int linearSearch(List<T> list, T target) {
        for (int i = 0; i < list.size(); i++) if (list.get(i).equals(target)) return i;
        return -1;
    }
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(10, 20, 30, 40);
        assert linearSearch(list, 30) == 2 : "Index should be 2";
        assert linearSearch(list, 50) == -1 : "Not found";
        System.out.println("LinearSearchTest passed");
    }
}
