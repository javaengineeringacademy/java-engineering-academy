package academy.javaengineering.collections.iteration.listiterator;

import java.util.*;

public class ListIteratorExample {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(Arrays.asList("Java", "Python", "C++"));
        ListIterator<String> it = list.listIterator();
        System.out.println("Forward:");
        while (it.hasNext()) System.out.println(it.next());
        System.out.println("Backward:");
        while (it.hasPrevious()) System.out.println(it.previous());
    }
}
