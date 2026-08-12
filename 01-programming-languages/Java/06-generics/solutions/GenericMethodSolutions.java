package academy.javaengineering.generics.solutions;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * Solutions for Generic Method Practice Exercises
 *
 * <p>Complexity: O(n) for most operations</p>
 * <p>Thread-safety: Not thread-safe</p>
 * <p>Key characteristics: Complete implementations of identity, asList, swap, max, and filter generic methods</p>
 */
public class GenericMethodSolutions {

    // ============================================================
    // Exercise 1: Identity Method Solution
    // ============================================================
    public static <T> T identity(T value) {
        return value;
    }

    // ============================================================
    // Exercise 2: AsList Method Solution
    // ============================================================
    @SafeVarargs
    public static <T> List<T> asList(T... elements) {
        List<T> list = new ArrayList<>();
        for (T element : elements) {
            list.add(element);
        }
        return list;
    }

    // ============================================================
    // Exercise 3: Swap Method Solution
    // ============================================================
    public static <T> void swap(List<T> list, int i, int j) {
        if (i < 0 || j < 0 || i >= list.size() || j >= list.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    // ============================================================
    // Exercise 4: Max Method Solution
    // ============================================================
    public static <T extends Comparable<T>> T max(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List must not be null or empty");
        }
        T maxValue = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            T current = list.get(i);
            if (current.compareTo(maxValue) > 0) {
                maxValue = current;
            }
        }
        return maxValue;
    }

    // ============================================================
    // Exercise 5: Filter Method Solution
    // ============================================================
    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        for (T element : list) {
            if (predicate.test(element)) {
                result.add(element);
            }
        }
        return result;
    }

    // ============================================================
    // Test all implementations
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Exercise 1: Identity Method ===");
        String str = identity("hello");
        Integer num = identity(42);
        System.out.println(str);
        System.out.println(num);

        System.out.println("\n=== Exercise 2: AsList Method ===");
        List<String> strings = asList("a", "b", "c");
        List<Integer> numbers = asList(1, 2, 3, 4, 5);
        System.out.println(strings);
        System.out.println(numbers);

        System.out.println("\n=== Exercise 3: Swap Method ===");
        List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
        System.out.println("Before swap: " + list);
        swap(list, 0, 3);
        System.out.println("After swap: " + list);

        System.out.println("\n=== Exercise 4: Max Method ===");
        List<Integer> nums = Arrays.asList(3, 7, 2, 8, 1);
        List<String> words = Arrays.asList("apple", "banana", "cherry");
        System.out.println("Max number: " + max(nums));
        System.out.println("Max word: " + max(words));

        System.out.println("\n=== Exercise 5: Filter Method ===");
        List<Integer> numsList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        List<Integer> even = filter(numsList, n -> n % 2 == 0);
        List<Integer> greaterThanFour = filter(numsList, n -> n > 4);
        System.out.println("Even: " + even);
        System.out.println("Greater than 4: " + greaterThanFour);
    }
}
