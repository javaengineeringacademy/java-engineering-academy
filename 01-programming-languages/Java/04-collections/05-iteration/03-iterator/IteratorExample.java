package academy.javaengineering.collections.iteration.iterator;

import java.util.*;

public class IteratorExample {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            int n = it.next();
            if (n % 2 == 0) it.remove();
        }
        System.out.println("After remove evens: " + list);
    }
}
