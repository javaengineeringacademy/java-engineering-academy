package academy.javaengineering.collections.iteration.listiterator;

import java.util.*;

public class ListIteratorTest {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
        ListIterator<Integer> it = list.listIterator();
        while (it.hasNext()) it.set(it.next() * 10);
        assert list.get(0) == 10 : "Should be 10";
        System.out.println("ListIteratorTest passed");
    }
}
