package academy.javaengineering.collections.iteration.examples;

import java.util.*;

public class IterationExamples {
    public static void main(String[] args) {
        System.out.println("=== Iteration Examples ===\n");
        List<String> list = new ArrayList<>(Arrays.asList("Java", "Python", "C++", "JavaScript"));

        System.out.println("--- for loop ---");
        for (int i = 0; i < list.size(); i++) System.out.println(i + ": " + list.get(i));

        System.out.println("\n--- for-each ---");
        for (String s : list) System.out.println(s);

        System.out.println("\n--- Iterator (remove) ---");
        Iterator<String> it = list.iterator();
        while (it.hasNext()) { if (it.next().length() > 4) it.remove(); }
        System.out.println("After filter: " + list);

        System.out.println("\n--- ListIterator (bidirectional) ---");
        ListIterator<String> lit = list.listIterator(list.size());
        while (lit.hasPrevious()) System.out.println(lit.previous());
    }
}
