import java.util.ArrayList;
import java.util.List;

/**
 * Solution: Upper and Lower Bounded Wildcards
 */
public class Solution3_Wildcards {
    public static void main(String[] args) {
        List<Integer> ints = List.of(1, 2, 3);
        List<Double> doubles = List.of(1.1, 2.2, 3.3);
        System.out.println("Sum of ints: " + printSum(ints));
        System.out.println("Sum of doubles: " + printSum(doubles));

        List<String> words = List.of("apple", "banana", "cherry");
        System.out.println("Max word: " + findMax(words));
    }

    static double printSum(List<? extends Number> list) {
        double sum = 0;
        for (Number n : list) {
            sum += n.doubleValue();
        }
        return sum;
    }

    static <T> void copyList(List<? extends T> source, List<? super T> destination) {
        destination.addAll(source);
    }

    static <T extends Comparable<T>> T findMax(List<T> list) {
        T max = list.get(0);
        for (T item : list) {
            if (item.compareTo(max) > 0) {
                max = item;
            }
        }
        return max;
    }
}
