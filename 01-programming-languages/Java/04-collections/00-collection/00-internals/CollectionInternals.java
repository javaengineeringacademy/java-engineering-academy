package academy.javaengineering.collections.internals;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class CollectionInternals {

    public static void main(String[] args) {
        System.out.println("=== Collection Interface Internals ===\n");

        // 1. Collection Hierarchy
        System.out.println("--- Collection Hierarchy ---");
        Collection<String> collection = new ArrayList<>();
        System.out.println("Collection interface methods:");
        System.out.println("  add(E e): " + collection.add("Java"));
        System.out.println("  contains(Object o): " + collection.contains("Java"));
        System.out.println("  isEmpty(): " + collection.isEmpty());
        System.out.println("  size(): " + collection.size());
        System.out.println("  remove(Object o): " + collection.remove("Java"));
        System.out.println("  clear(): void (removes all)");

        // 2. Iterable vs Collection
        System.out.println("\n--- Iterable vs Collection ---");
        Iterable<String> iterable = Arrays.asList("A", "B", "C");
        Iterator<String> iter = iterable.iterator();
        System.out.print("Iterable iterator: ");
        while (iter.hasNext()) System.out.print(iter.next() + " ");
        System.out.println();

        // 3. Collection.toArray() behavior
        System.out.println("\n--- toArray() Internals ---");
        Collection<Integer> nums = Arrays.asList(1, 2, 3, 4, 5);
        Object[] objArray = nums.toArray();
        Integer[] intArray = nums.toArray(new Integer[0]);
        System.out.println("toArray(): " + Arrays.toString(objArray));
        System.out.println("toArray(T[]): " + Arrays.toString(intArray));

        // 4. Collection.stream() default method
        System.out.println("\n--- Stream Support ---");
        Collection<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        List<String> upper = names.stream()
            .map(String::toUpperCase)
            .collect(Collectors.toList());
        System.out.println("Stream result: " + upper);

        // 5. Collection.removeIf() internal
        System.out.println("\n--- removeIf() Internal ---");
        List<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
        System.out.println("Before: " + numbers);
        numbers.removeIf(n -> n % 2 == 0);
        System.out.println("After removeIf(odd): " + numbers);

        // 6. Collection.addAll() vs add()
        System.out.println("\n--- addAll() vs add() ---");
        Collection<String> c1 = new ArrayList<>(Arrays.asList("A", "B"));
        Collection<String> c2 = Arrays.asList("C", "D");
        c1.addAll(c2);
        System.out.println("addAll: " + c1);

        // 7. Collection.containsAll()
        System.out.println("\n--- containsAll() ---");
        Collection<Integer> full = Arrays.asList(1, 2, 3, 4, 5);
        Collection<Integer> partial = Arrays.asList(2, 3);
        System.out.println("containsAll: " + full.containsAll(partial));
    }
}
