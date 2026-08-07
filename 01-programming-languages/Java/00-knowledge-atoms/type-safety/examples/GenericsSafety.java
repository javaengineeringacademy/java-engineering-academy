import java.util.ArrayList;
import java.util.List;

public class GenericsSafety {
    public static void main(String[] args) {
        // 1. Type-safe list with generics
        List<String> names = new ArrayList<>();
        names.add("Alice");
        names.add("Bob");
        // names.add(42); // Error: Integer cannot be converted to String

        String first = names.get(0); // No cast needed
        System.out.println("First name: " + first);

        // 2. Compile-time checking with multiple types
        Pair<String, Integer> pair = new Pair<>("Age", 30);
        System.out.println("Pair: " + pair.getKey() + "=" + pair.getValue());

        // 3. Bounded type parameters
        System.out.println("Max of 3, 7: " + max(3, 7));
        System.out.println("Max of 3.14, 2.71: " + max(3.14, 2.71));

        // 4. Wildcards for flexibility
        List<Integer> integers = List.of(1, 2, 3);
        List<Double> doubles = List.of(1.1, 2.2, 3.3);
        printNumbers(integers);
        printNumbers(doubles);
    }

    // Generic pair class
    static class Pair<K, V> {
        private final K key;
        private final V value;

        Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        K getKey() { return key; }
        V getValue() { return value; }
    }

    // Bounded type parameter - T must be Comparable
    static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    // Upper bounded wildcard - accepts any Number or its subclasses
    static void printNumbers(List<? extends Number> numbers) {
        for (Number n : numbers) {
            System.out.print(n + " ");
        }
        System.out.println();
    }
}
