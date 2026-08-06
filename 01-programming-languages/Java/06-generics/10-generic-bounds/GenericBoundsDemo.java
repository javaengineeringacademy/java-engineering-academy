package genericbounds;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Generic Bounds Demo - Complete Guide
 * 
 * Covers upper bounded wildcards, lower bounded wildcards,
 * multiple bounds, and practical use cases.
 */
public class GenericBoundsDemo {

    // ==========================================
    // SECTION 1: Upper Bounded Wildcards (? extends T)
    // ==========================================
    static class UpperBoundedWildcards {

        // Number and its subclasses
        static double sumOfList(List<? extends Number> list) {
            double sum = 0;
            for (Number num : list) {
                sum += num.doubleValue();
            }
            return sum;
        }

        // Comparable upper bound
        static <T extends Comparable<T>> T findMax(List<T> list) {
            if (list.isEmpty()) throw new IllegalArgumentException("List is empty");
            T max = list.get(0);
            for (T item : list) {
                if (item.compareTo(max) > 0) {
                    max = item;
                }
            }
            return max;
        }

        // Multiple upper bounds
        static <T extends Number & Comparable<T>> T findMaxNumber(List<T> list) {
            if (list.isEmpty()) throw new IllegalArgumentException("List is empty");
            T max = list.get(0);
            for (T item : list) {
                if (item.compareTo(max) > 0) {
                    max = item;
                }
            }
            return max;
        }

        static void demonstrateUpperBounded() {
            System.out.println("=== Upper Bounded Wildcards ===\n");

            List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
            List<Double> doubles = Arrays.asList(1.5, 2.5, 3.5);
            List<Number> numbers = Arrays.asList(1, 2.5, 3L, 4.0f);

            System.out.println("Integer sum: " + sumOfList(integers));
            System.out.println("Double sum: " + sumOfList(doubles));
            System.out.println("Number sum: " + sumOfList(numbers));

            System.out.println("\nMax Integer: " + findMax(integers));
            System.out.println("Max Double: " + findMax(doubles));

            System.out.println("\nMax Number (extends & Comparable): " + findMaxNumber(integers));

            // Reading from upper bounded - can read as Number
            System.out.println("\nReading from List<? extends Number>:");
            for (Number num : numbers) {
                System.out.print(num + " ");
            }
            System.out.println();

            // Limitation: cannot write to upper bounded
            // numbers.add(42); // COMPILE ERROR - cannot add to ? extends
            System.out.println("\n  Note: Cannot add to List<? extends T>");
            System.out.println("  Note: Can only read as T or supertype of T");
        }
    }

    // ==========================================
    // SECTION 2: Lower Bounded Wildcards (? super T)
    // ==========================================
    static class LowerBoundedWildcards {

        // PECS: Producer Extends, Consumer Super
        static <T> void copy(List<? super T> dest, List<? extends T> src) {
            for (T item : src) {
                dest.add(item);
            }
        }

        // Adding Integer to Number list
        static void addNumbers(List<? super Integer> list) {
            for (int i = 1; i <= 5; i++) {
                list.add(i);
            }
        }

        // Adding objects to Serializable list
        static void addAll(List<? super String> list, String... items) {
            for (String item : items) {
                list.add(item);
            }
        }

        static void demonstrateLowerBounded() {
            System.out.println("\n=== Lower Bounded Wildcards ===\n");

            // Can add Integer to List<Integer>, List<Number>, List<Object>
            List<Integer> integerList = new ArrayList<>();
            List<Number> numberList = new ArrayList<>();
            List<Object> objectList = new ArrayList<>();

            addNumbers(integerList);
            addNumbers(numberList);
            addNumbers(objectList);

            System.out.println("Added to List<Integer>: " + integerList);
            System.out.println("Added to List<Number>: " + numberList);
            System.out.println("Added to List<Object>: " + objectList);

            // PECS pattern
            System.out.println("\n=== PECS Pattern ===");
            List<Integer> source = Arrays.asList(1, 2, 3, 4, 5);
            List<Number> dest1 = new ArrayList<>();
            List<Object> dest2 = new ArrayList<>();

            copy(dest1, source);
            copy(dest2, source);

            System.out.println("Copied to List<Number>: " + dest1);
            System.out.println("Copied to List<Object>: " + dest2);

            // Adding to generic collections
            System.out.println("\n=== Adding to Lower Bounded ===");
            List<Serializable> serializableList = new ArrayList<>();
            addAll(serializableList, "Hello", "World");
            System.out.println("Serializable list: " + serializableList);

            List<Comparable<?>> comparableList = new ArrayList<>();
            comparableList.add("test");
            comparableList.add(42);
            System.out.println("Comparable list: " + comparableList);
        }
    }

    // ==========================================
    // SECTION 3: Multiple Bounds
    // ==========================================
    static class MultipleBounds {

        // Multiple bounds with class first, then interfaces
        static <T extends Number & Comparable<T> & Serializable> T findMaxInArray(T[] array) {
            if (array == null || array.length == 0) {
                throw new IllegalArgumentException("Array must not be empty");
            }
            T max = array[0];
            for (T item : array) {
                if (item.compareTo(max) > 0) {
                    max = item;
                }
            }
            return max;
        }

        // Bounded type parameter with multiple constraints
        static <T extends Comparable<T> & Serializable> List<T> sortAndSerialize(List<T> list) {
            list.sort(Comparator.naturalOrder());
            return list;
        }

        // Using bounds with wildcards
        static <T extends Number & Comparable<T>> void processList(List<? extends T> list) {
            System.out.print("  Processing: ");
            for (T item : list) {
                System.out.print(item + " ");
            }
            System.out.println(" (max=" + findMax(list) + ")");
        }

        static <T extends Comparable<T>> T findMax(List<? extends T> list) {
            T max = null;
            for (T item : list) {
                if (max == null || item.compareTo(max) > 0) {
                    max = item;
                }
            }
            return max;
        }

        static void demonstrateMultipleBounds() {
            System.out.println("\n=== Multiple Bounds ===\n");

            Integer[] ints = {3, 1, 4, 1, 5, 9, 2, 6};
            Double[] doubles = {3.14, 2.71, 1.41, 1.73};

            System.out.println("Max Integer: " + findMaxInArray(ints));
            System.out.println("Max Double: " + findMaxInArray(doubles));

            List<Integer> intList = new ArrayList<>(Arrays.asList(5, 3, 1, 4, 2));
            System.out.println("\nSorted: " + sortAndSerialize(intList));

            System.out.println("\nProcessing bounded lists:");
            processList(Arrays.asList(1, 2, 3));
            processList(Arrays.asList(1.5, 2.5, 3.5));
        }
    }

    // ==========================================
    // SECTION 4: Practical Examples
    // ==========================================
    static class PracticalExamples {

        // Type-safe min/max utility
        static class MinMax<T extends Comparable<T>> {
            private final List<T> items = new ArrayList<>();

            public void add(T item) { items.add(item); }

            public T min() {
                if (items.isEmpty()) throw new IllegalStateException("No items");
                T min = items.get(0);
                for (T item : items) {
                    if (item.compareTo(min) < 0) min = item;
                }
                return min;
            }

            public T max() {
                if (items.isEmpty()) throw new IllegalStateException("No items");
                T max = items.get(0);
                for (T item : items) {
                    if (item.compareTo(max) > 0) max = item;
                }
                return max;
            }
        }

        // Generic builder with bounds
        static class Builder<T extends Comparable<T>> {
            private final List<T> items = new ArrayList<>();

            @SafeVarargs
            public final Builder<T> add(T... items) {
                this.items.addAll(Arrays.asList(items));
                return this;
            }

            public List<T> build() {
                List<T> sorted = new ArrayList<>(items);
                sorted.sort(Comparator.naturalOrder());
                return sorted;
            }
        }

        // Type-safe heterogeneous container
        static class TypeSafeMap {
            private final java.util.Map<String, Object> map = new java.util.HashMap<>();

            public <T> void put(String key, T value, Class<T> type) {
                if (!type.isInstance(value)) {
                    throw new ClassCastException("Cannot cast " + value.getClass() + " to " + type);
                }
                map.put(key, value);
            }

            @SuppressWarnings("unchecked")
            public <T> T get(String key, Class<T> type) {
                Object value = map.get(key);
                if (value == null) return null;
                if (!type.isInstance(value)) {
                    throw new ClassCastException("Cannot cast " + value.getClass() + " to " + type);
                }
                return (T) value;
            }
        }

        static void demonstratePractical() {
            System.out.println("\n=== Practical Examples ===\n");

            // MinMax utility
            System.out.println("--- MinMax Utility ---");
            MinMax<Integer> minMax = new MinMax<>();
            minMax.add(5);
            minMax.add(2);
            minMax.add(8);
            minMax.add(1);
            System.out.println("Min: " + minMax.min());
            System.out.println("Max: " + minMax.max());

            // Builder pattern
            System.out.println("\n--- Builder Pattern ---");
            List<String> sorted = new Builder<String>()
                    .add("banana", "apple", "cherry", "date")
                    .build();
            System.out.println("Sorted: " + sorted);

            // Type-safe map
            System.out.println("\n--- Type-Safe Map ---");
            TypeSafeMap tsMap = new TypeSafeMap();
            tsMap.put("name", "Alice", String.class);
            tsMap.put("age", 30, Integer.class);
            tsMap.put("score", 95.5, Double.class);

            System.out.println("Name: " + tsMap.get("name", String.class));
            System.out.println("Age: " + tsMap.get("age", Integer.class));
            System.out.println("Score: " + tsMap.get("score", Double.class));
        }
    }

    // ==========================================
    // SECTION 5: Common Patterns and Pitfalls
    // ==========================================
    static class PatternsAndPitfalls {

        // Safe casting with bounds
        @SuppressWarnings("unchecked")
        static <T extends Comparable<T>> T safeCast(Object obj, Class<T> type) {
            if (type.isInstance(obj)) {
                return type.cast(obj);
            }
            throw new ClassCastException("Cannot cast " + obj.getClass() + " to " + type);
        }

        // Wildcard capture pattern
        static void swap(List<?> list) {
            swapInternal(list, 0, list.size() - 1);
        }

        private static <T> void swapInternal(List<T> list, int i, int j) {
            T temp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, temp);
        }

        // Generic method with wildcards
        static <T extends Comparable<T>> List<T> mergeSorted(
                List<? extends T> list1, List<? extends T> list2) {
            List<T> result = new ArrayList<>();
            int i = 0, j = 0;
            while (i < list1.size() && j < list2.size()) {
                if (list1.get(i).compareTo(list2.get(j)) <= 0) {
                    result.add(list1.get(i++));
                } else {
                    result.add(list2.get(j++));
                }
            }
            while (i < list1.size()) result.add(list1.get(i++));
            while (j < list2.size()) result.add(list2.get(j++));
            return result;
        }

        static void demonstratePatterns() {
            System.out.println("\n=== Common Patterns ===\n");

            // Wildcard capture
            System.out.println("--- Wildcard Capture ---");
            List<Integer> nums = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
            System.out.println("Before swap: " + nums);
            swap(nums);
            System.out.println("After swap: " + nums);

            // Safe casting
            System.out.println("\n--- Safe Casting ---");
            Object[] objects = {42, "hello", 3.14, true};
            for (Object obj : objects) {
                try {
                    if (obj instanceof Integer) {
                        Integer i = safeCast(obj, Integer.class);
                        System.out.println("Integer: " + i);
                    } else if (obj instanceof String) {
                        String s = safeCast(obj, String.class);
                        System.out.println("String: " + s);
                    }
                } catch (ClassCastException e) {
                    System.out.println("Cast failed: " + e.getMessage());
                }
            }

            // Merging sorted lists
            System.out.println("\n--- Merge Sorted Lists ---");
            List<Integer> list1 = Arrays.asList(1, 3, 5, 7);
            List<Integer> list2 = Arrays.asList(2, 4, 6, 8);
            List<Integer> merged = mergeSorted(list1, list2);
            System.out.println("List1: " + list1);
            System.out.println("List2: " + list2);
            System.out.println("Merged: " + merged);

            // Common pitfalls
            System.out.println("\n--- Common Pitfalls ---");
            System.out.println("  1. Cannot create generic arrays: new T[] = compile error");
            System.out.println("  2. Cannot use instanceof with generics: list instanceof List<String>");
            System.out.println("  3. Cannot catch generic type: catch (T e) = compile error");
            System.out.println("  4. Cannot create: new T() = compile error");
            System.out.println("  5. Type erasure: List<String> == List<Integer> at runtime");
        }
    }

    // ==========================================
    // MAIN
    // ==========================================
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║    GENERIC BOUNDS DEMO                  ║");
        System.out.println("╚══════════════════════════════════════════╝\n");

        UpperBoundedWildcards.demonstrateUpperBounded();
        LowerBoundedWildcards.demonstrateLowerBounded();
        MultipleBounds.demonstrateMultipleBounds();
        PracticalExamples.demonstratePractical();
        PatternsAndPitfalls.demonstratePatterns();

        System.out.println("\nAll generic bounds demos complete!");
    }
}
