package academy.javaengineering.collections.iteration.iterator;

import java.util.*;

public class IteratorTest {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c", "d"));
        Iterator<String> it = list.iterator();
        while (it.hasNext()) if (it.next().equals("b")) it.remove();
        assert list.size() == 3 : "Size should be 3";
        System.out.println("IteratorTest passed");
    }
}
