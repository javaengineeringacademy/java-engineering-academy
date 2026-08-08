package collection.examples;

import java.util.*;

public class CollectionExample {

    public static void main(String[] args) {
        example1_BasicCollection();
        example2_AddRemoveContains();
        example3_Iterators();
        example4_CollectionToArray();
        example5_UnmodifiableCollection();
    }

    static void example1_BasicCollection() {
        System.out.println("=== Example 1: Basic Collection Interface ===");
        Collection<String> collection = new ArrayList<>();
        collection.add("Apple");
        collection.add("Banana");
        collection.add("Cherry");
        System.out.println("Collection: " + collection);
        System.out.println("Size: " + collection.size());
        System.out.println("Is empty: " + collection.isEmpty());
    }

    static void example2_AddRemoveContains() {
        System.out.println("\n=== Example 2: Add, Remove, Contains ===");
        Collection<String> collection = new ArrayList<>(Arrays.asList("Java", "Python", "C++"));
        System.out.println("Original: " + collection);
        collection.add("JavaScript");
        System.out.println("After add: " + collection);
        collection.remove("C++");
        System.out.println("After remove: " + collection);
        System.out.println("Contains Java: " + collection.contains("Java"));
        System.out.println("Contains C++: " + collection.contains("C++"));
    }

    static void example3_Iterators() {
        System.out.println("\n=== Example 3: Iterator Traversal ===");
        Collection<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        Iterator<Integer> iterator = numbers.iterator();
        System.out.print("Using Iterator: ");
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();
    }

    static void example4_CollectionToArray() {
        System.out.println("\n=== Example 4: Collection to Array ===");
        Collection<String> collection = new ArrayList<>(Arrays.asList("Red", "Green", "Blue"));
        String[] array = collection.toArray(new String[0]);
        System.out.println("Array: " + Arrays.toString(array));
    }

    static void example5_UnmodifiableCollection() {
        System.out.println("\n=== Example 5: Unmodifiable Collection ===");
        Collection<String> original = new ArrayList<>(Arrays.asList("A", "B", "C"));
        Collection<String> unmodifiable = Collections.unmodifiableCollection(original);
        System.out.println("Unmodifiable: " + unmodifiable);
        try {
            unmodifiable.add("D");
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify unmodifiable collection: " + e.getClass().getSimpleName());
        }
    }
}
