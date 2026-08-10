package academy.javaengineering.collections.iteration.listiterator;

import java.util.*;

public class ListIteratorModifyExample {
    public static void main(String[] args) {
        System.out.println("=== ListIterator Collection Modification ===\n");

        // 1. Replace elements using set()
        System.out.println("--- 1. Replace with set() ---");
        List<String> names = new ArrayList<>(Arrays.asList("Alice", "Bob", "Charlie"));
        System.out.println("Before: " + names);

        ListIterator<String> it = names.listIterator();
        while (it.hasNext()) {
            String name = it.next();
            it.set(name.toUpperCase());  // Replace current element
        }
        System.out.println("After set(): " + names);

        // 2. Add elements during forward iteration
        System.out.println("\n--- 2. Add during forward iteration ---");
        List<Integer> numbers = new ArrayList<>(Arrays.asList(1, 3, 5));
        System.out.println("Before: " + numbers);

        ListIterator<Integer> lit = numbers.listIterator();
        while (lit.hasNext()) {
            int num = lit.next();
            if (num == 3) {
                lit.add(2);  // Add 2 before 3
            }
            if (num == 5) {
                lit.add(4);  // Add 4 before 5
            }
        }
        System.out.println("After add(): " + numbers);

        // 3. Add elements during backward iteration
        System.out.println("\n--- 3. Add during backward iteration ---");
        List<String> langs = new ArrayList<>(Arrays.asList("Java", "Python"));
        System.out.println("Before: " + langs);

        ListIterator<String> lit2 = langs.listIterator(langs.size());
        while (lit2.hasPrevious()) {
            String lang = lit2.previous();
            if (lang.equals("Python")) {
                lit2.add("C++");  // Add after Python
            }
        }
        System.out.println("After backward add: " + langs);

        // 4. Replace and add in single pass
        System.out.println("\n--- 4. Replace and add in single pass ---");
        List<String> words = new ArrayList<>(Arrays.asList("Hello", "World"));
        System.out.println("Before: " + words);

        ListIterator<String> lit3 = words.listIterator();
        while (lit3.hasNext()) {
            String word = lit3.next();
            lit3.set(word + "!");      // Replace
            lit3.add(word.toLowerCase()); // Add after
        }
        System.out.println("After modify: " + words);

        // 5. Practical: Insert sorted
        System.out.println("\n--- 5. Insert sorted ---");
        List<Integer> sorted = new ArrayList<>(Arrays.asList(1, 3, 5, 7));
        System.out.println("Before: " + sorted);

        int newNum = 4;
        ListIterator<Integer> lit4 = sorted.listIterator();
        while (lit4.hasNext()) {
            if (lit4.next() > newNum) {
                lit4.previous();
                lit4.add(newNum);
                break;
            }
        }
        System.out.println("After insert " + newNum + ": " + sorted);
    }
}
