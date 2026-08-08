package academy.javaengineering.collections.examples;

import java.util.*;

public class IteratorExamples {
    public static void main(String[] args) {
        System.out.println("=== Iterator Examples ===\n");

        // Iterator - forward only
        System.out.println("--- Iterator ---");
        List<String> languages = new ArrayList<>(Arrays.asList("Java", "Python", "C++", "JavaScript"));
        Iterator<String> it = languages.iterator();
        while (it.hasNext()) {
            String lang = it.next();
            System.out.println("Language: " + lang);
            if (lang.equals("C++")) {
                it.remove(); // Remove C++
            }
        }
        System.out.println("After remove: " + languages);

        // ListIterator - bidirectional
        System.out.println("\n--- ListIterator ---");
        List<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        ListIterator<Integer> listIt = numbers.listIterator();
        System.out.println("Forward:");
        while (listIt.hasNext()) {
            System.out.println("  " + listIt.next() + " (index: " + listIt.previousIndex() + ")");
        }
        System.out.println("Backward:");
        while (listIt.hasPrevious()) {
            System.out.println("  " + listIt.previous() + " (index: " + listIt.nextIndex() + ")");
        }

        // ListIterator with set
        System.out.println("\n--- ListIterator set ---");
        listIt = numbers.listIterator();
        while (listIt.hasNext()) {
            Integer num = listIt.next();
            listIt.set(num * 10);
        }
        System.out.println("After set * 10: " + numbers);

        // Spliterator
        System.out.println("\n--- Spliterator ---");
        List<Integer> splist = IntStream.rangeClosed(1, 20).boxed().toList();
        Spliterator<Integer> spliterator = splist.spliterator();
        System.out.println("Exact size: " + spliterator.getExactSizeIfKnown());
        System.out.println("Has characteristics: " + spliterator.hasCharacteristics(Spliterator.ORDERED));

        // for-each loop (syntactic sugar for iterator)
        System.out.println("\n--- for-each loop ---");
        for (String lang : languages) {
            System.out.println("  " + lang);
        }

        // Enumeration (legacy)
        System.out.println("\n--- Enumeration ---");
        Vector<String> vector = new Vector<>(Arrays.asList("A", "B", "C"));
        Enumeration<String> enum = vector.elements();
        while (enum.hasMoreElements()) {
            System.out.println("  " + enum.nextElement());
        }
    }
}
