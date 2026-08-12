package academy.javaengineering.collections.iteration.internals;

import java.util.*;
import java.util.stream.*;

public class IterationInternals {

    public static void main(String[] args) {
        System.out.println("=== Iteration Internals ===\n");

        List<String> list = Arrays.asList("Java", "Python", "JavaScript", "C++");

        // 1. Iterator pattern
        System.out.println("--- Iterator Pattern ---");
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            System.out.print(it.next() + " ");
        }
        System.out.println("\nhasNext() checks bounds");
        System.out.println("next() returns element and advances");

        // 2. ListIterator bidirectional
        System.out.println("\n--- ListIterator Bidirectional ---");
        ListIterator<String> lit = list.listIterator();
        System.out.print("Forward: ");
        while (lit.hasNext()) System.out.print(lit.next() + " ");
        System.out.println();
        System.out.print("Backward: ");
        while (lit.hasPrevious()) System.out.print(lit.previous() + " ");
        System.out.println();

        // 3. for-each desugars to Iterator
        System.out.println("\n--- for-each = Iterator ---");
        System.out.println("for (String s : list) { ... }");
        System.out.println("Becomes: Iterator it = list.iterator();");
        System.out.println("         while (it.hasNext()) { String s = it.next(); ... }");

        // 4. Enumeration (legacy)
        System.out.println("\n--- Enumeration (Legacy) ---");
        Vector<String> vector = new Vector<>(list);
        Enumeration<String> enum_ = vector.elements();
        System.out.print("Enumeration: ");
        while (enum_.hasMoreElements()) System.out.print(enum_.nextElement() + " ");
        System.out.println();

        // 5. Spliterator
        System.out.println("\n--- Spliterator ---");
        Spliterator<String> spliterator = list.spliterator();
        System.out.println("Characteristics: " + spliterator.characteristics());
        spliterator.forEachRemaining(s -> System.out.print(s + " "));
        System.out.println();

        // 6. Stream traversal
        System.out.println("\n--- Stream Traversal ---");
        list.stream()
            .filter(s -> s.length() > 2)
            .map(String::toUpperCase)
            .forEach(s -> System.out.print(s + " "));
        System.out.println();
    }
}
