package academy.javaengineering.collections.iteration.examples;

import java.util.*;

public class IterationExamples {
    public static void main(String[] args) {
        System.out.println("=== Iteration Examples ===\n");

        // Example 1: for loop with index - find max
        System.out.println("--- Example 1: Find Max with for loop ---");
        List<Integer> nums = Arrays.asList(10, 25, 3, 47, 8);
        int max = nums.get(0);
        for (int i = 1; i < nums.size(); i++) {
            if (nums.get(i) > max) max = nums.get(i);
        }
        System.out.println("Max: " + max);

        // Example 2: Enhanced for - count words
        System.out.println("\n--- Example 2: Count Words ---");
        String text = "Java is great and Java is fun";
        int count = 0;
        for (String word : text.split(" ")) {
            if (word.equals("Java")) count++;
        }
        System.out.println("Java count: " + count);

        // Example 3: Iterator - remove negatives
        System.out.println("\n--- Example 3: Remove Negatives ---");
        List<Integer> mixed = new ArrayList<>(Arrays.asList(1, -2, 3, -4, 5));
        Iterator<Integer> it = mixed.iterator();
        while (it.hasNext()) {
            if (it.next() < 0) it.remove();
        }
        System.out.println("After remove: " + mixed);

        // Example 4: ListIterator - reverse list
        System.out.println("\n--- Example 4: Reverse with ListIterator ---");
        List<String> langs = new ArrayList<>(Arrays.asList("Java", "Python", "C++"));
        ListIterator<String> lit = langs.listIterator(langs.size());
        System.out.print("Reversed: ");
        while (lit.hasPrevious()) System.out.print(lit.previous() + " ");
        System.out.println();

        // Example 5: while loop - fibonacci
        System.out.println("\n--- Example 5: Fibonacci with while ---");
        int a = 0, b = 1, n = 10;
        System.out.print("Fibonacci: ");
        int i = 0;
        while (i < n) {
            System.out.print(a + " ");
            int temp = a + b;
            a = b;
            b = temp;
            i++;
        }
        System.out.println();
    }
}
