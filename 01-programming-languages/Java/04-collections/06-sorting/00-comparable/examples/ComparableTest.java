package academy.javaengineering.collections.sorting.comparable;

import java.util.*;

public class ComparableTest {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(Arrays.asList("Banana", "Apple", "Cherry"));
        Collections.sort(list);
        assert list.get(0).equals("Apple") : "Should be Apple first";
        System.out.println("ComparableTest passed");
    }
}
