import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Solutions for Wildcard Practice Exercises
 */
public class WildcardSolutions {

    // ============================================================
    // Exercise 1: Sum Method Solution
    // ============================================================
    public static double sum(List<? extends Number> list) {
        double sum = 0;
        for (Number number : list) {
            sum += number.doubleValue();
        }
        return sum;
    }

    // ============================================================
    // Exercise 2: AddAll Method Solution
    // ============================================================
    public static <T> void addAll(List<T> destination, List<? extends T> source) {
        for (T element : source) {
            destination.add(element);
        }
    }

    // ============================================================
    // Exercise 3: Copy Method Solution
    // ============================================================
    public static <T> void copy(List<? extends T> source, List<? super T> destination) {
        for (T element : source) {
            destination.add(element);
        }
    }

    // ============================================================
    // Exercise 4: Min Method Solution
    // ============================================================
    public static <T extends Comparable<T>> T min(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List must not be null or empty");
        }
        T minValue = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            T current = list.get(i);
            if (current.compareTo(minValue) < 0) {
                minValue = current;
            }
        }
        return minValue;
    }

    // ============================================================
    // Exercise 5: PrintAll Method Solution
    // ============================================================
    public static void printAll(List<?> list) {
        for (Object element : list) {
            System.out.println(element);
        }
    }

    // ============================================================
    // Test all implementations
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Exercise 1: Sum Method ===");
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
        List<Double> doubles = Arrays.asList(1.5, 2.5, 3.5);
        System.out.println("Sum of integers: " + sum(integers));
        System.out.println("Sum of doubles: " + sum(doubles));

        System.out.println("\n=== Exercise 2: AddAll Method ===");
        List<Number> numbers = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> moreNumbers = Arrays.asList(4, 5, 6);
        addAll(numbers, moreNumbers);
        System.out.println("Combined: " + numbers);

        System.out.println("\n=== Exercise 3: Copy Method ===");
        List<Object> destination = new ArrayList<>();
        List<String> source = Arrays.asList("hello", "world");
        copy(source, destination);
        System.out.println("Copied: " + destination);

        System.out.println("\n=== Exercise 4: Min Method ===");
        List<Integer> nums = Arrays.asList(5, 2, 8, 1, 9);
        List<String> words = Arrays.asList("cherry", "apple", "banana");
        System.out.println("Min number: " + min(nums));
        System.out.println("Min word: " + min(words));

        System.out.println("\n=== Exercise 5: PrintAll Method ===");
        List<String> strings = Arrays.asList("a", "b", "c");
        List<Integer> numsList = Arrays.asList(1, 2, 3);
        System.out.println("Strings:");
        printAll(strings);
        System.out.println("Numbers:");
        printAll(numsList);
    }
}
