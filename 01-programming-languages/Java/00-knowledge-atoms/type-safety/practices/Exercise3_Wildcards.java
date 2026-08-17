import java.util.List;

/**
 * Exercise: Upper and Lower Bounded Wildcards
 *
 * Task: Implement methods using wildcards correctly.
 * - printSum: accepts List of any Number subclass
 * - copyList: copies from any source list to a destination list
 * - findMax: finds the maximum in a list of Comparable objects
 */
public class Exercise3_Wildcards {
    public static void main(String[] args) {
        // Test printSum
        List<Integer> ints = List.of(1, 2, 3);
        List<Double> doubles = List.of(1.1, 2.2, 3.3);
        System.out.println("Sum of ints: " + printSum(ints));
        System.out.println("Sum of doubles: " + printSum(doubles));

        // Test findMax
        List<String> words = List.of("apple", "banana", "cherry");
        System.out.println("Max word: " + findMax(words));
    }

    /**
     * TODO: Implement using upper bounded wildcard
     * Accepts List<? extends Number> and returns sum as double
     */
    static double printSum(List<? extends Number> list) {
        // Your code here
        return 0;
    }

    /**
     * TODO: Implement using lower bounded wildcard
     * Copies all elements from source to destination
     */
    static <T> void copyList(List<? extends T> source, List<? super T> destination) {
        // Your code here
    }

    /**
     * TODO: Implement using bounded type parameter
     * Returns the maximum element in the list
     */
    static <T extends Comparable<T>> T findMax(List<T> list) {
        // Your code here
        return null;
    }
}
