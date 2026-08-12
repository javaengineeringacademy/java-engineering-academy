package academy.javaengineering.generics.type-inference.examples;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Type Inference Demo - Working examples of Java type inference.
 */
public class TypeInferenceDemo {

    // Generic class
    static class Container<T> {
        private T value;
        public Container(T value) { this.value = value; }
        public T getValue() { return value; }
    }

    // Generic method
    public static <T> List<T> listOf(T... items) {
        List<T> list = new ArrayList<>();
        for (T item : items) {
            list.add(item);
        }
        return list;
    }

    public static void main(String[] args) {
        System.out.println("=== Type Inference Demo ===\n");

        // 1. Diamond operator (Java 7+)
        System.out.println("1. Diamond Operator:");
        Container<String> cs = new Container<>("Hello");
        Container<Integer> ci = new Container<>(42);
        System.out.println("  String container: " + cs.getValue());
        System.out.println("  Integer container: " + ci.getValue());

        // 2. Method type inference
        System.out.println("\n2. Method Type Inference:");
        List<String> strings = listOf("A", "B", "C");
        List<Integer> integers = listOf(1, 2, 3);
        System.out.println("  Strings: " + strings);
        System.out.println("  Integers: " + integers);

        // 3. Target type inference
        System.out.println("\n3. Target Type Inference:");
        var list = new ArrayList<String>();  // Java 10+ var
        list.add("Hello");
        System.out.println("  Var list: " + list);

        // 4. Complex inference
        System.out.println("\n4. Complex Inference:");
        Map<String, List<Integer>> map = new HashMap<>();
        map.put("numbers", listOf(1, 2, 3));
        System.out.println("  Map: " + map);

        // 5. Generic method with bounds
        System.out.println("\n5. Bounded Inference:");
        System.out.println("  Max of 3, 5: " + max(3, 5));
        System.out.println("  Max of A, Z: " + max("A", "Z"));
    }

    public static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }
}
